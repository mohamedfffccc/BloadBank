package com.example.bloadbank.Views.Activity;

import android.os.Bundle;

import com.example.bloadbank.R;
import com.example.bloadbank.Views.Fragment.Splash_Fragment;

import static com.example.bloadbank.helper.HelperMethod.ReplaceFragment;

public class SplashActivity extends BaseActivity {


    public Splash_Fragment splash_fragment = new Splash_Fragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Splash_theme);
        setContentView(R.layout.activity_main);
        ReplaceFragment(getSupportFragmentManager()
                , splash_fragment, R.id.Splash_Cycle
                , null, "medo");

    }
}
