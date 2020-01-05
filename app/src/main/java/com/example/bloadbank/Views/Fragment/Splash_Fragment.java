package com.example.bloadbank.Views.Fragment;

import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.bloadbank.Views.Activity.HomeCycle;
import com.example.bloadbank.helper.HelperMethod;
import com.example.bloadbank.R;
import com.example.bloadbank.data.local.Saveddata;

import butterknife.ButterKnife;

import static com.example.bloadbank.helper.HelperMethod.ReplaceFragment;

public class Splash_Fragment extends BaseFragment {
    //TODO set up activity

Saveddata saveddata;
ImageView imageView;

   public SliderFragment sliderFragment = new SliderFragment();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        getActivity().setTheme(R.style.Splash_theme);
        View view = inflater.inflate(R.layout.fragment_splash_, container, false);
        imageView = view.findViewById(R.id.splash_image);
        saveddata = new Saveddata(getActivity());
        AnimationSet set = (AnimationSet) AnimationUtils.loadAnimation(getActivity() , R.anim.ic_logotween);
        imageView.startAnimation(set);
//        setUpActivity();
        int secondsDelayed = 2;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Saveddata saveddata = new Saveddata(getActivity());
                saveddata.Read_data();
                if (saveddata.remember_user.equals("true"))
                {
                    Intent i = new Intent(getActivity() , HomeCycle.class);
                    startActivity(i);
                }
                else
                {
                ReplaceFragment(getActivity().getSupportFragmentManager(),
                        sliderFragment  , R.id.Splash_Cycle
                        , null, "medo");}
//                saveddata.Read_data();
//                Toast.makeText(getActivity() , getString(R.string.city) , Toast.LENGTH_SHORT).show();
            }
        }, secondsDelayed * 1000);

        // Inflate the layout for this fragment
        return view;

    }

  //TODO override onback

    @Override
    public void onback() {
        //TODO write what you need
    }
}

