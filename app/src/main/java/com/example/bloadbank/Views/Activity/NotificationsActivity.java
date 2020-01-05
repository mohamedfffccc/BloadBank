package com.example.bloadbank.Views.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloadbank.R;
import com.example.bloadbank.adapters.NotificationsAdapter;
import com.example.bloadbank.helper.OnEndLess;
import com.example.bloadbank.data.local.Saveddata;
import com.example.bloadbank.data.api.UserApi;
import com.example.bloadbank.data.model.notifications.Notifications;
import com.example.bloadbank.data.model.notifications.NotificationsData;

import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.bloadbank.data.api.ClientApi.GetClient;
import static com.example.bloadbank.data.local.Saveddata.showPositiveToast;

public class NotificationsActivity extends AppCompatActivity {
    UserApi userApi;
    Saveddata saveddata;
    RecyclerView bloadnotificationsNotificationlist;
    ArrayList<NotificationsData> data;
    NotificationsAdapter adapter;
    int MaxPage;
    private OnEndLess onEndLess;
    LinearLayoutManager linearLayoutManager;
    private BaseActivity baseActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Splash_theme);
        setContentView(R.layout.activity_notifications);
        baseActivity = new BaseActivity();
        bloadnotificationsNotificationlist = (RecyclerView) findViewById(R.id.bloadnotifications_notificationlist);
        saveddata = new Saveddata(getApplicationContext());
        userApi = GetClient().create(UserApi.class);
        data = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        bloadnotificationsNotificationlist.setLayoutManager(linearLayoutManager);
        adapter = new NotificationsAdapter(getApplicationContext() , baseActivity , data);
        GetNotifications(1);
        onEndLess = new OnEndLess(linearLayoutManager, 1) {
            @Override
            public void onLoadMore(int current_page) {

                if (current_page <= MaxPage) {
                    if (MaxPage != 0 && current_page != 1) {
                        onEndLess.previous_page = current_page;
                        GetNotifications(current_page);
                        adapter.notifyDataSetChanged();
                    } else {
                        onEndLess.current_page = onEndLess.previous_page;
                    }
                } else {
                    onEndLess.current_page = onEndLess.previous_page;

                }


            }
        };
        bloadnotificationsNotificationlist.addOnScrollListener(onEndLess);


    }
    public void GetNotifications(int page) {
        saveddata.Read_data();
        userApi.GetNotifications(saveddata.api_token , page).enqueue(new Callback<Notifications>() {
            @Override
            public void onResponse(Call<Notifications> call, Response<Notifications> response) {
                try{
                    if (response.body().getStatus()==1)
                    {

                        showPositiveToast(NotificationsActivity.this , response.body().getMsg());
                        MaxPage = response.body().getData().getLastPage();
                        data.addAll(response.body().getData().getData());
                        bloadnotificationsNotificationlist.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                    }

                }
                catch (Exception e)
                {

                }
            }

            @Override
            public void onFailure(Call<Notifications> call, Throwable t) {

            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
