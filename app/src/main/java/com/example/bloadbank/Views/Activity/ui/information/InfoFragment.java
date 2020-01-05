package com.example.bloadbank.Views.Activity.ui.information;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.bloadbank.R;
import com.example.bloadbank.data.local.Saveddata;
import com.example.bloadbank.data.api.UserApi;
import com.example.bloadbank.data.model.generalResponse.GeneralResponse;
import com.example.bloadbank.data.model.login.Login;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.bloadbank.data.local.Saveddata.showPositiveToast;
import static com.example.bloadbank.helper.HelperMethod.dismissProgressDialog;
import static com.example.bloadbank.helper.HelperMethod.showProgressDialog;
import static com.example.bloadbank.data.api.ClientApi.GetClient;

public class InfoFragment extends Fragment {


    @BindView(R.id.info_tvchange)
    TextView infoTvchange;
    @BindView(R.id.info_name)
    EditText infoName;
    @BindView(R.id.info_email)
    EditText infoEmail;
    @BindView(R.id.info_butcal)
    Button infoButcal;
    @BindView(R.id.info_datebirth)
    TextView infoDatebirth;
    @BindView(R.id.info_bloadtype)
    Spinner infoBloadtype;
    @BindView(R.id.info_but_donationdate)
    Button infoButDonationdate;
    @BindView(R.id.ed_info_date_of_donation)
    TextView edInfoDateOfDonation;
    @BindView(R.id.info_governate)
    Spinner infoGovernate;
    @BindView(R.id.info_city)
    Spinner infoCity;
    @BindView(R.id.info_phone_nu)
    EditText infoPhoneNu;
    @BindView(R.id.info_password)
    EditText infoPassword;
    @BindView(R.id.info_password_sure)
    EditText infoPasswordSure;
    @BindView(R.id.info_button_register)
    Button infoButtonRegister;
    ArrayList<String> govs, city, bloads;
    ArrayList<Integer> gids, cids, bids;
    ArrayAdapter<String> adapter;
    UserApi userApi;
    int governaementid;
    Saveddata saveddata;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_info, container, false);
        ButterKnife.bind(this, root);
        Init();
        userApi = GetClient().create(UserApi.class);
        saveddata.Read_data();
        //TODO add governement
        AddGovernements();
        adapter = new ArrayAdapter<String>(getActivity(), R.layout.item, R.id.textgov, govs);
        infoGovernate.setAdapter(adapter);
        //TODO add bloads
        Gettypes();
        adapter = new ArrayAdapter<String>(getActivity(), R.layout.item, R.id.textgov, bloads);
        infoBloadtype.setAdapter(adapter);
        //TODO add cites
        governaementid = infoGovernate.getSelectedItemPosition();


        AddCites(governaementid);
        adapter = new ArrayAdapter<String>(getActivity(), R.layout.item, R.id.textgov, city);
        infoCity.setAdapter(adapter);
        GetProfile();


        return root;
    }

    @OnClick(R.id.info_button_register)
    public void onViewClicked() {
        Editprof();


    }


    //TODO Methods
    public void AddGovernements() {
        govs.add("اختر المحافظة");
        gids.add(0);

        userApi.GetGov().enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                for (int i = 0; i < response.body().getData().size(); i++) {
                    govs.add(response.body().getData().get(i).getName());
                    gids.add(response.body().getData().get(i).getId());

                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });
    }

    public void Init() {
        saveddata = new Saveddata(getActivity());
        govs = new ArrayList<>();
        gids = new ArrayList<>();
        city = new ArrayList<>();
        cids = new ArrayList<>();
        bloads = new ArrayList<>();
        bids = new ArrayList<>();

    }

    public void Gettypes() {
        bloads.add("اختر فصيلة الدم");
        bids.add(0);
        userApi.GetBloads().enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                for (int i = 0; i < response.body().getData().size(); i++) {
                    bloads.add(response.body().getData().get(i).getName());
                    bids.add(response.body().getData().get(i).getId());

                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });
    }

    public void AddCites(int governorate_id) {
        city.add("اختر المدينة");
        cids.add(0);
        userApi.GetCites(governorate_id).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {

                for (int i = 0; i < response.body().getData().size(); i++) {
                    city.add(response.body().getData().get(i).getName());
                    cids.add(response.body().getData().get(i).getId());

                }

            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });
    }

    public void GetProfile() {
        showProgressDialog(getActivity(), "please wait");
        userApi.GetProfData(saveddata.api_token).enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                try {
                    if (response.body().getStatus() == 1) {
                        dismissProgressDialog();
                        showPositiveToast(getActivity() , response.body().getMsg());
                        infoName.setText(response.body().getData().getClient().getName());
                        infoEmail.setText(response.body().getData().getClient().getEmail());
                        infoDatebirth.setText(response.body().getData().getClient().getBirthDate());
                        edInfoDateOfDonation.setText(response.body().getData().getClient().getDonationLastDate());
                        infoBloadtype.setSelection(response.body().getData().getClient().getBloodType().getId());
                        infoGovernate.setSelection(response.body().getData().getClient().getCity().getGovernorate().getId());
                        infoCity.setSelection(response.body().getData().getClient().getCity().getId());
                        infoPhoneNu.setText(response.body().getData().getClient().getPhone());
                        infoPassword.setText(response.body().getData().getClient().getUpdatedAt());
                        infoPasswordSure.setText(response.body().getData().getClient().getUpdatedAt());


                    }

                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {

            }
        });
    }

    public void Editprof() {
        showProgressDialog(getActivity(), "please wait");
        userApi.EditProfile(infoName.getText().toString()
                , infoEmail.getText().toString(),
                infoDatebirth.getText().toString(),
                infoCity.getSelectedItemPosition(),
                infoPhoneNu.getText().toString(),
                edInfoDateOfDonation.getText().toString(),
                infoPassword.getText().toString(),
                infoPasswordSure.getText().toString(),
                infoBloadtype.getSelectedItemPosition(),
                saveddata.api_token)
                .enqueue(new Callback<Login>() {
                    @Override
                    public void onResponse(Call<Login> call, Response<Login> response) {
                        dismissProgressDialog();
                        try {

                            if (response.body().getStatus() == 1) {

                                Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {

                        }
                    }

                    @Override
                    public void onFailure(Call<Login> call, Throwable t) {

                    }
                });
    }
}