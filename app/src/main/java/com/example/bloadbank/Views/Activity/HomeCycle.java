package com.example.bloadbank.Views.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.bloadbank.Views.Activity.ui.contactUs.ContactUs;
import com.example.bloadbank.data.local.Saveddata;
import com.example.bloadbank.data.api.UserApi;
import com.example.bloadbank.data.model.notificationscount.NotificationsCount;
import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.widget.Toolbar;

import com.example.bloadbank.R;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.bloadbank.data.api.ClientApi.GetClient;
import static com.example.bloadbank.helper.HelperMethod.ReplaceFragment;

public class HomeCycle extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    ImageView notifications_image;
    TextView count;
    UserApi userApi;
    Saveddata saveddata;
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_cycle);
        saveddata = new Saveddata(getApplicationContext());
        userApi = GetClient().create(UserApi.class);
        count = (TextView) findViewById(R.id.notification_count);
        GetNotificationsCount();
        notifications_image = (ImageView) findViewById(R.id.notification_image);
        notifications_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeCycle.this, NotificationsActivity.class);
                startActivity(i);
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_info, R.id.nav_notifications, R.id.nav_favourite,
                R.id.nav_home, R.id.nav_usage, R.id.nav_contactus, R.id.nav_about, R.id.nav_log_out)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    private void GetNotificationsCount() {
        saveddata.Read_data();
        userApi.GetNotificationsCount(saveddata.api_token).enqueue(new Callback<NotificationsCount>() {
            @Override
            public void onResponse(Call<NotificationsCount> call, Response<NotificationsCount> response) {
                try {
                    if (response.body().getStatus() == 1) {
                        Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        if (response.body().getData().getNotificationsCount() == 0) {
                            count.setVisibility(View.GONE);

                        } else {
                            count.setVisibility(View.VISIBLE);
                            count.setText(response.body().getData().getNotificationsCount() + "");
                        }

                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<NotificationsCount> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_cycle, menu);
        return true;
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_contactus:
                ContactUs contactUs = new ContactUs();
                ReplaceFragment(getSupportFragmentManager(),contactUs , R.id.nav_host_fragment
            , null, "medo");
                break;
        }
        return  true;
    }
}
