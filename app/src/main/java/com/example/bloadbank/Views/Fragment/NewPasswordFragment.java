package com.example.bloadbank.Views.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.bloadbank.R;
import com.example.bloadbank.helper.HelperMethod;
import com.example.bloadbank.data.local.SharedPreferencesManger;
import com.example.bloadbank.data.api.UserApi;
import com.example.bloadbank.data.model.newPassword.NewPassword;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.bloadbank.data.api.ClientApi.GetClient;


public class NewPasswordFragment extends Fragment {
    private UserApi userApi;
    SharedPreferencesManger sharedPreferencesManger = new SharedPreferencesManger();
     HelperMethod helperMethod = new HelperMethod();
    LoginFragment loginFragment = new LoginFragment();
    String phone;
    Button changePasswordButton;
    EditText confimationCode,newPass,confirmNewPass;
    String code,pass,passConfirmation;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_password, container, false);
        phone = sharedPreferencesManger.LoadData(getActivity(), "phone");
        userApi = GetClient().create(UserApi.class);

        changePasswordButton = (Button) view.findViewById(R.id.change_password_button);
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Changepass();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    public void Changepass() {
        confimationCode=(EditText) getView().findViewById(R.id.confirmation_code_password);
        code=confimationCode.getText().toString();
        newPass=(EditText) getView().findViewById(R.id.new_password_edittext);
        pass=newPass.getText().toString();
      confirmNewPass  =(EditText) getView().findViewById(R.id.confirm_new_password);
        passConfirmation=confirmNewPass.getText().toString();
        userApi.ChangePassword(phone ,code , pass , passConfirmation).enqueue(new Callback<NewPassword>() {
            @Override
            public void onResponse(Call<NewPassword> call, Response<NewPassword> response) {
                if(response.body().getStatus()==1)
                {
                    Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    helperMethod.ReplaceFragment(getActivity().getSupportFragmentManager()
                            , loginFragment  , R.id.usercycle
                            , null, "medo");
                }
                else if(response.body().getStatus()==0)
                {
                    Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NewPassword> call, Throwable t) {

            }
        });


    }


}
