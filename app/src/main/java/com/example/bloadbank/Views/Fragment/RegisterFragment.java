package com.example.bloadbank.Views.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloadbank.R;
import com.example.bloadbank.data.model.DateModel;
import com.example.bloadbank.data.api.UserApi;
import com.example.bloadbank.data.model.generalResponse.GeneralResponse;
import com.example.bloadbank.data.model.login.AllClientData;
import com.example.bloadbank.data.model.login.Client;
import com.example.bloadbank.data.model.login.Login;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.bloadbank.data.local.SharedPreferencesManger.LoadData;
import static com.example.bloadbank.helper.BloodBankConstants.API_TOKEN;
import static com.example.bloadbank.helper.BloodBankConstants.EMAIL;
import static com.example.bloadbank.helper.BloodBankConstants.NAME;
import static com.example.bloadbank.helper.BloodBankConstants.PHONE;
import static com.example.bloadbank.helper.BloodBankConstants.REMEMBER_USER;
import static com.example.bloadbank.helper.HelperMethod.showCalender;
import static com.example.bloadbank.data.local.SharedPreferencesManger.SaveData;
import static com.example.bloadbank.data.api.ClientApi.GetClient;

public class RegisterFragment extends BaseFragment {
    @BindView(R.id.button_register_cal)
    Button buttonRegisterCal;
    @BindView(R.id.ed_register_date)
    TextView edRegisterDate;
    @BindView(R.id.button_register_donation_date)
    Button buttonRegisterDonationDate;
    @BindView(R.id.ed_register_date_of_donation)
    TextView edRegisterDateOfDonation;
    TextView textviewRegister;
    EditText edRegisterName;
    EditText edRegisterEmail;
    LinearLayout donationDateCal;
    Spinner governate;
    Spinner city;
    Spinner edRegisterBlood;
    EditText registerPhoneNu;
    EditText registerPassword;
    EditText registerPasswordSure;
    Button buttonRegister;
    UserApi userApi;
    String name;
    String email;
    String date_birth;
    String bload_type;
    String donation_date;
    String governate_id, city_id, phone_no, password, reset_password;
    ArrayList<String> govs, cites, bloadtypes;
    ArrayList<Integer> ids, cids, blids;
    ArrayAdapter<String> arrayAdapter;
    int gov_id;
    public DateModel data1 = new DateModel();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register, container, false);
        ButterKnife.bind(this, view);
        govs = new ArrayList<String>();
        cites = new ArrayList<String>();
        bloadtypes = new ArrayList<>();
        ids = new ArrayList<>();
        cids = new ArrayList<>();
        blids = new ArrayList<>();
        userApi = GetClient().create(UserApi.class);
        governate = (Spinner) view.findViewById(R.id.governate);
        city = (Spinner) view.findViewById(R.id.city);
        edRegisterBlood = (Spinner) view.findViewById(R.id.ed_register_blood);
        //TODO govs
        AddGovernements();
        arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item, R.id.textgov, govs);
        governate.setAdapter(arrayAdapter);
        //TODO bloads
        Gettypes();
        arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item, R.id.textgov, bloadtypes);
        edRegisterBlood.setAdapter(arrayAdapter);
        //TODO cites
        gov_id = governate.getSelectedItemPosition();
        AddCites(gov_id);
        arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item, R.id.textgov, cites);
        city.setAdapter(arrayAdapter);
        buttonRegister = (Button) view.findViewById(R.id.button_register);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register();
            }
        });
        return view;
    }


    private void Register() {
        Init();
        userApi.SignUp(name, email, date_birth, city.getSelectedItemPosition()
                , phone_no, donation_date, password, reset_password, edRegisterBlood.getSelectedItemPosition()).enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                try {
                    if (response.body().getStatus() == 1) {
                        AllClientData data = response.body().getData();
                        String apiToken = data.getApiToken();
                        Client client = data.getClient();
                        String phone = client.getPhone();
                        SaveData(getActivity(), API_TOKEN, apiToken);
                        Toast.makeText(getActivity() , LoadData(getActivity(), API_TOKEN) , Toast.LENGTH_SHORT).show();
                        SaveData(getActivity(), REMEMBER_USER, null);
                        SaveData(getActivity(), PHONE, phone);
                        SaveData(getActivity(), NAME, client.getName());
                        SaveData(getActivity(), EMAIL, client.getEmail());
                        Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    } else if (response.body().getStatus() == 0) {
                        Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "try again later", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {


            }
        });
    }

    public void AddGovernements() {
        govs.add("اختر المحافظة");
        ids.add(0);

        userApi.GetGov().enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                for (int i = 0; i < response.body().getData().size(); i++) {
                    govs.add(response.body().getData().get(i).getName());
                    ids.add(response.body().getData().get(i).getId());
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });
    }

    public void AddCites(int governorate_id) {
        cites.add("اختر المدينة");
        cids.add(0);
        userApi.GetCites(governorate_id).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {

                for (int i = 0; i < response.body().getData().size(); i++) {
                    cites.add(response.body().getData().get(i).getName());
                    cids.add(response.body().getData().get(i).getId());

                }

            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });
    }


    public void Init() {
        //TODO init views
        edRegisterName = (EditText) getView().findViewById(R.id.ed_register_name);
        edRegisterEmail = (EditText) getView().findViewById(R.id.ed_register_email);
        edRegisterBlood = (Spinner) getView().findViewById(R.id.ed_register_blood);
        edRegisterDate = (TextView) getView().findViewById(R.id.ed_register_date);
        edRegisterDateOfDonation = (TextView) getView().findViewById(R.id.ed_register_date_of_donation);
        registerPassword = (EditText) getView().findViewById(R.id.register_password);
        registerPasswordSure = (EditText) getView().findViewById(R.id.register_password_sure);
        registerPhoneNu = (EditText) getView().findViewById(R.id.register_phone_nu);
        //TODO init strings
        name = edRegisterName.getText().toString();
        email = edRegisterEmail.getText().toString();
        date_birth = edRegisterDate.getText().toString();
        donation_date = edRegisterDateOfDonation.getText().toString();
        phone_no = registerPhoneNu.getText().toString();
        password = registerPassword.getText().toString();
        reset_password = registerPasswordSure.getText().toString();
    }

    public void Gettypes() {
        bloadtypes.add("اختر فصيلة الدم");
        blids.add(0);
        userApi.GetBloads().enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                for (int i = 0; i < response.body().getData().size(); i++) {
                    bloadtypes.add(response.body().getData().get(i).getName());
                    blids.add(response.body().getData().get(i).getId());

                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });
    }

    @OnClick({R.id.button_register_cal, R.id.button_register_donation_date})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button_register_cal:

                showCalender(getActivity(), "select date", edRegisterDate, data1);
                break;
            case R.id.button_register_donation_date:
                showCalender(getActivity(), "select date", edRegisterDateOfDonation, data1);

                break;
        }
    }
}
