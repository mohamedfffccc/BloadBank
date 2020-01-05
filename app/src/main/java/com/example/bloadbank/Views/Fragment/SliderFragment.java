package com.example.bloadbank.Views.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.viewpager.widget.ViewPager;

import com.example.bloadbank.Views.Activity.HomeCycle;
import com.example.bloadbank.adapters.SlideAdapter;
import com.example.bloadbank.data.local.Saveddata;
import com.example.bloadbank.helper.HelperMethod;
import com.example.bloadbank.R;
import com.example.bloadbank.Views.Activity.UserCycleActivity;

import static com.example.bloadbank.data.local.SharedPreferencesManger.LoadData;
import static com.example.bloadbank.helper.BloodBankConstants.API_TOKEN;
import static com.example.bloadbank.helper.BloodBankConstants.REMEMBER_USER;


public class SliderFragment extends BaseFragment {
    public HelperMethod helperMethod = new HelperMethod();
    public LoginFragment loginFragment = new LoginFragment();
    public SlideAdapter slideAdapter;
     ViewPager viewPager;
//     Saveddata saveddata;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        }
        View view = inflater.inflate(R.layout.fragment_slider, container, false);

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        slideAdapter = new SlideAdapter(getActivity());
        viewPager.setAdapter(slideAdapter);

        Button skip = (Button) view.findViewById(R.id.btn_skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserCycleActivity.class);
                getActivity().startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onback() {
        super.onback();
    }
}
