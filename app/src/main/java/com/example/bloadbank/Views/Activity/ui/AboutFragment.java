package com.example.bloadbank.Views.Activity.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.bloadbank.R;
import com.example.bloadbank.data.local.Saveddata;
import com.example.bloadbank.data.api.UserApi;
import com.example.bloadbank.data.model.settings.Settings;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.bloadbank.data.api.ClientApi.GetClient;


public class AboutFragment extends Fragment {
    UserApi userApi;
    @BindView(R.id.about_tvabout)
    TextView aboutTvabout;
    @BindView(R.id.about_text)
    TextView aboutText;
    Saveddata saveddata;

    // TODO: Rename parameter arguments, choose names that match
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_about, container, false);
        ButterKnife.bind(this, root);
        userApi = GetClient().create(UserApi.class);
        saveddata=new Saveddata(getActivity());
        GetSetting();
        // Inflate the layout for this fragment
        return root;
    }

    public void GetSetting() {
        saveddata.Read_data();
        userApi.GetSetting(saveddata.api_token)
                .enqueue(new Callback<Settings>() {
                    @Override
                    public void onResponse(Call<Settings> call, Response<Settings> response) {
                        try {
                            if (response.body().getStatus() == 1) {
                                Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                aboutText.setText(response.body().getData().getAboutApp());


                            }


                        } catch (Exception e) {

                        }
                    }

                    @Override
                    public void onFailure(Call<Settings> call, Throwable t) {

                    }
                });
    }


}
