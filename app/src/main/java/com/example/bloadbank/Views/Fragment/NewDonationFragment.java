package com.example.bloadbank.Views.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.bloadbank.R;
import com.example.bloadbank.Views.Activity.MapsActivity;
import com.example.bloadbank.data.local.Saveddata;
import com.example.bloadbank.data.api.UserApi;
import com.example.bloadbank.data.model.donationrequestcreate.OneDonationRequest;
import com.example.bloadbank.data.model.generalResponse.GeneralResponse;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.bloadbank.helper.BloodBankConstants.LATITUDE;
import static com.example.bloadbank.helper.BloodBankConstants.LOCATION;
import static com.example.bloadbank.helper.BloodBankConstants.LONGITUDE;
import static com.example.bloadbank.helper.HelperMethod.ReplaceFragment;
import static com.example.bloadbank.helper.HelperMethod.dismissProgressDialog;
import static com.example.bloadbank.helper.HelperMethod.showProgressDialog;
import static com.example.bloadbank.data.local.SharedPreferencesManger.LoadData;
import static com.example.bloadbank.data.local.SharedPreferencesManger.SaveData;
import static com.example.bloadbank.data.api.ClientApi.GetClient;

public class NewDonationFragment extends Fragment {
    public String latitude, langtude;
    @BindView(R.id.newdonation_name)
    EditText newdonationName;
    @BindView(R.id.newdonation_age)
    EditText newdonationAge;
    @BindView(R.id.newdonation_bloadtype)
    Spinner newdonationBloadtype;
    @BindView(R.id.newdonation_numberbload)
    EditText newdonationNumberbload;
    @BindView(R.id.newdonation_hospitalname)
    EditText newdonationHospitalname;
    @BindView(R.id.newdonation_hospitaaladdress)
    TextView newdonationHospitaaladdress;
    @BindView(R.id.newdonation_governemente)
    Spinner newdonationGovernemente;
    @BindView(R.id.newdonation_city)
    Spinner newdonationCity;
    @BindView(R.id.newdonation_phonenumber)
    EditText newdonationPhonenumber;
    @BindView(R.id.newdonation_notes)
    EditText newdonationNotes;
    @BindView(R.id.newdonation_btnsend)
    Button newdonationBtnsend;
    ArrayList<String> govs, city, bloads;
    ArrayList<Integer> gids, cids, bids;
    ArrayAdapter<String> adapter;
    UserApi userApi;
    int governaementid;
    @BindView(R.id.newrequest_addmap)
    ImageView newrequestAddmap;
    Saveddata saveddata;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_donation, container, false);
        ButterKnife.bind(this, view);
        saveddata = new Saveddata(getActivity());
        Init();
        userApi = GetClient().create(UserApi.class);
        //TODO add governement
        AddGovernements();
        adapter = new ArrayAdapter<String>(getActivity(), R.layout.item, R.id.textgov, govs);
        newdonationGovernemente.setAdapter(adapter);
        //TODO add bloads
        Gettypes();
        adapter = new ArrayAdapter<String>(getActivity(), R.layout.item, R.id.textgov, bloads);
        newdonationBloadtype.setAdapter(adapter);
        //TODO add cites
        governaementid = newdonationGovernemente.getSelectedItemPosition();


        AddCites(governaementid);
        adapter = new ArrayAdapter<String>(getActivity(), R.layout.item, R.id.textgov, city);
        newdonationCity.setAdapter(adapter);
//        Toast.makeText(getActivity(), LoadData(getActivity() , LOCATION), Toast.LENGTH_LONG).show();


        // Inflate the layout for this fragment
        return view;
    }


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

    public void CreateNewOrder() {
        showProgressDialog(getActivity(), "please wait");
        saveddata.Read_data();

        userApi.CreateRequest(saveddata.api_token
                , newdonationName.getText().toString()
                , Integer.parseInt(newdonationAge.getText().toString())
                , newdonationBloadtype.getSelectedItemPosition()
                , Integer.parseInt(newdonationNumberbload.getText().toString())
                , newdonationHospitalname.getText().toString()
                , newdonationHospitaaladdress.getText().toString()
                , newdonationCity.getSelectedItemPosition()
                , newdonationPhonenumber.getText().toString()
                , newdonationNotes.getText().toString()
                , LoadData(getActivity(), LATITUDE), LoadData(getActivity(), LONGITUDE))
                .enqueue(new Callback<OneDonationRequest>() {
                    @Override
                    public void onResponse(Call<OneDonationRequest> call, Response<OneDonationRequest> response) {
                        dismissProgressDialog();
                        try {
                            if (response.body().getStatus() == 1) {
                                Toast.makeText(getActivity(), response.body().getData().getId(), Toast.LENGTH_SHORT).show();


                                Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                Toast.makeText(getActivity(), response.body().getMsg() + response.body().getData().getLatitude(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();

                            }

                        } catch (Exception e) {

                        }
                    }

                    @Override
                    public void onFailure(Call<OneDonationRequest> call, Throwable t) {

                    }
                });
    }


    @OnClick({R.id.newrequest_addmap, R.id.newdonation_btnsend})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.newrequest_addmap:
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.newdonation_btnsend:
                CreateNewOrder();
                SaveData(getActivity(), LOCATION, null);
                SaveData(getActivity(), LATITUDE, null);
                SaveData(getActivity(), LONGITUDE, null);

                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        newdonationHospitaaladdress.setText(LoadData(getActivity(), LOCATION));

    }
}
