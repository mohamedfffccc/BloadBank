package com.example.bloadbank.Views.Fragment;


import android.app.NotificationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.bloadbank.R;
import com.example.bloadbank.helper.HelperMethod;
import com.example.bloadbank.data.api.UserApi;
import com.example.bloadbank.data.model.reresetPassword.ResetPassword;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.bloadbank.helper.HelperMethod.dismissProgressDialog;
import static com.example.bloadbank.helper.HelperMethod.showProgressDialog;
import static com.example.bloadbank.data.api.ClientApi.GetClient;


public class ForgetPasswordFragment extends BaseFragment {
    NotificationManager manager;
    UserApi userApi;
    EditText forgetPasswordTelephoneNumber;

    Button forgetpassBtnsend;
    String phone;
    NewPasswordFragment newPasswordFragment = new NewPasswordFragment();
    HelperMethod helperMethod = new HelperMethod();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forget_password, container, false);
        //TODO init views
        forgetPasswordTelephoneNumber = (EditText) view.findViewById(R.id.forget_password_telephone_number);
        forgetpassBtnsend = (Button) view.findViewById(R.id.forgetpass_btnsend);
        userApi = GetClient().create(UserApi.class);
        forgetpassBtnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgetPass();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    public void ForgetPass() {
        showProgressDialog(getActivity() , "please wait");
        phone = forgetPasswordTelephoneNumber.getText().toString();
        userApi.ResetPass(phone).enqueue(new Callback<ResetPassword>() {
            @Override
            public void onResponse(Call<ResetPassword> call, Response<ResetPassword> response) {
                dismissProgressDialog();

                if (response.body().getStatus() == 1) {
                    Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();

                    helperMethod.ReplaceFragment(getActivity().getSupportFragmentManager()
                            , newPasswordFragment, R.id.usercycle
                            , null, "medo");
                    Notify(response.body().getData().getPinCodeForTest());

                } else if (response.body().getStatus() == 0) {
                    Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResetPassword> call, Throwable t) {

            }
        });

    }

    public void Notify(int key) {
        int id = 0;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity())
                .setContentTitle(" Bload Bank")
                .setContentText("your verification code is " + String.valueOf(key))
                .setSmallIcon(R.drawable.ic_logo);
        ;
        manager = (NotificationManager) getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);
        manager.notify(id, builder.build());
        id++;
    }


}
