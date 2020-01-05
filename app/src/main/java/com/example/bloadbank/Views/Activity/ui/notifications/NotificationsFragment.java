package com.example.bloadbank.Views.Activity.ui.notifications;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloadbank.R;
import com.example.bloadbank.Views.Fragment.BaseFragment;
import com.example.bloadbank.adapters.NotificationSettingAdapter;
import com.example.bloadbank.data.api.UserApi;
import com.example.bloadbank.data.local.Saveddata;
import com.example.bloadbank.data.model.generalResponse.GeneralResponse;
import com.example.bloadbank.data.model.generalResponse.GeneralResponseData;
import com.example.bloadbank.data.model.notificationssetting.NotificationSsetting;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.bloadbank.data.api.ClientApi.GetClient;
import static com.example.bloadbank.data.local.Saveddata.showPositiveToast;


//import android.widget.SettingAdapter;

public class NotificationsFragment extends BaseFragment {
    public List<Integer> newSelectedIdnotifications;
    Saveddata saveddata;
    NotificationSettingAdapter adapter1, adapter2;
    ArrayList<GeneralResponseData> govs, bloads;
    UserApi userApi;
    @BindView(R.id.notifications_bloads)
    RecyclerView notificationsBloads;
    @BindView(R.id.notifications_fragment_rv_goverments_list)
    RecyclerView notificationsGovs;
    @BindView(R.id.notifications_save)
    Button notificationsSave;
    GridLayoutManager gridLayoutManager, gridLayoutManager2;
    ArrayList<String> oldselectedbloads;
    ArrayList<String> oldselectedgovs;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ;
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        ButterKnife.bind(this, root);
        saveddata = new Saveddata(getActivity());
        gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        gridLayoutManager2 = new GridLayoutManager(getActivity(), 3);

        notificationsBloads.setHasFixedSize(true);
        notificationsGovs.setHasFixedSize(true);

        notificationsBloads.setLayoutManager(gridLayoutManager);
        notificationsGovs.setLayoutManager(gridLayoutManager2);
        Init();

        GetSetting();

        return root;
    }

    public void Init() {
        govs = new ArrayList<>();
        bloads = new ArrayList<>();
        oldselectedbloads = new ArrayList<>();
        oldselectedgovs = new ArrayList<>();
        userApi = GetClient().create(UserApi.class);
    }


    public void Gettypes() {
        userApi.GetBloads().enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                try {
                    bloads.addAll(response.body().getData());

                    adapter1 = new NotificationSettingAdapter(getActivity(), baseActivity, bloads, oldselectedbloads);
                    notificationsBloads.setAdapter(adapter1);

                } catch (Exception e) {

                }


            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });
    }

    public void GetSetting() {
        saveddata.Read_data();
        userApi.GetNotificationsSetting(saveddata.api_token).enqueue(new Callback<NotificationSsetting>() {
            @Override
            public void onResponse(Call<NotificationSsetting> call, Response<NotificationSsetting> response) {
                try {
                    if (response.body().getStatus() == 1) {
                        showPositiveToast(getActivity() , response.body().getMsg());
                        oldselectedbloads.addAll(response.body().getData().getBloodTypes());
                        oldselectedgovs.addAll(response.body().getData().getGovernorates());
                        Log.d("DATA", oldselectedbloads.toString());

                        Gettypes();
                        getGovernements();


                    }
                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<NotificationSsetting> call, Throwable t) {

            }
        });

    }

    public void getGovernements() {

        userApi.GetGov().enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                try {
                    govs.addAll(response.body().getData());

                    adapter2 = new NotificationSettingAdapter(getActivity(), baseActivity, govs, oldselectedgovs);
                    notificationsGovs.setAdapter(adapter2);

                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });
    }

    public void changeSetting() {
        saveddata.Read_data();
        userApi.changetNotificationsSetting(saveddata.api_token,
               adapter2.newSelectedIds,adapter1.newSelectedIds).enqueue(new Callback<NotificationSsetting>() {
            @Override
            public void onResponse(Call<NotificationSsetting> call, Response<NotificationSsetting> response) {
                try {
Toast.makeText(getActivity() , response.body().getMsg() , Toast.LENGTH_SHORT).show();



                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<NotificationSsetting> call, Throwable t) {

            }
        });
    }


    @OnClick(R.id.notifications_save)
    public void onViewClicked() {
        changeSetting();
    }
}