package com.example.bloadbank.Views.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.bloadbank.R;
import com.example.bloadbank.Views.Activity.HomeCycle;
import com.example.bloadbank.data.local.Saveddata;
import com.example.bloadbank.data.local.SharedPreferencesManger;
import com.example.bloadbank.data.api.UserApi;
import com.example.bloadbank.data.model.login.Login;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.bloadbank.data.local.Saveddata.showNegativeToast;
import static com.example.bloadbank.data.local.Saveddata.showPositiveToast;
import static com.example.bloadbank.data.local.SharedPreferencesManger.LoadData;
import static com.example.bloadbank.data.local.SharedPreferencesManger.REMEMBER;
import static com.example.bloadbank.data.local.SharedPreferencesManger.SaveData;
import static com.example.bloadbank.helper.BloodBankConstants.REMEMBER_USER;
import static com.example.bloadbank.helper.HelperMethod.ReplaceFragment;
import static com.example.bloadbank.helper.HelperMethod.dismissProgressDialog;
import static com.example.bloadbank.helper.HelperMethod.showProgressDialog;
import static com.example.bloadbank.data.api.ClientApi.GetClient;


public class LoginFragment extends BaseFragment {
    @BindView(R.id.image_logo)
    ImageView imageLogo;
    @BindView(R.id.login_telephone_number)
    EditText loginTelephoneNumber;
    @BindView(R.id.login_userpassword)
    EditText loginUserpassword;
    @BindView(R.id.login_btn_forgetpassword)
    Button loginBtnForgetpassword;
    @BindView(R.id.login_ch_remember)
    CheckBox loginChRemember;
    @BindView(R.id.linear_remember_forget)
    LinearLayout linearRememberForget;
    @BindView(R.id.login_btn_login)
    Button loginBtnLogin;
    @BindView(R.id.login_btn_register)
    Button loginBtnRegister;
    @BindView(R.id.login_container)
    RelativeLayout loginContainer;
    private UserApi userApi;
    Saveddata saveddata;
    String api_token;
    boolean remember;
    String govid;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        userApi = GetClient().create(UserApi.class);
        ButterKnife.bind(this, view);
        saveddata = new Saveddata(getActivity());
        loginChRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                remember = isChecked;
//                SaveData(getActivity(), REMEMBER_USER, "true");
                Toast.makeText(getActivity() , LoadData(getActivity(), REMEMBER_USER)  , Toast.LENGTH_SHORT).show();


            }
        });

        return view;
    }

    private void Login() {
        showProgressDialog(getActivity(), "جاري تسجيل الدخول");
        userApi.LoginUser(loginTelephoneNumber.getText().toString(), loginUserpassword.getText().toString()).enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {

                dismissProgressDialog();
                try {
                    if (response.body().getStatus() == 1) {
                        govid=   response.body().getData().getClient().getCity().getGovernorate().getGovernorateId();

                        showPositiveToast(getActivity() , response.body().getMsg());
                        api_token = response.body().getData().getApiToken();
                        saveddata.Save_data(loginTelephoneNumber.getText().toString()
                                , loginUserpassword.getText().toString() , api_token , "false" , govid);

                        if (remember) {
                            saveddata.Save_data(loginTelephoneNumber.getText().toString()
                                    , loginUserpassword.getText().toString() , api_token , "true" , govid);
                            saveddata.Read_data();

                            Toast.makeText(getActivity(), saveddata.api_token, Toast.LENGTH_SHORT).show();

                        }


                        Intent HomeIntent = new Intent(getActivity(), HomeCycle.class);
                        getActivity().startActivity(HomeIntent);


                    } else if (response.body().getStatus() == 0) {
                        showNegativeToast(getActivity() , response.body().getMsg());

                    }
                }
                catch (Exception e)
                {

                }

            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                Log.d("uuu", t.getMessage());
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @OnClick({R.id.login_btn_forgetpassword, R.id.login_btn_login, R.id.login_btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_btn_forgetpassword:
                ReplaceFragment(getActivity().getSupportFragmentManager()
                        , new ForgetPasswordFragment(), R.id.usercycle
                        , null, "medo");
                break;
            case R.id.login_btn_login:



                Login();
                                break;
            case R.id.login_btn_register:
                ReplaceFragment(getActivity().getSupportFragmentManager()
                        , new RegisterFragment(), R.id.usercycle
                        , null, "medo");
                break;
        }
    }

}
