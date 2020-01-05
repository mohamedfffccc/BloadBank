package com.example.bloadbank.Views.Activity.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.bloadbank.R;
import com.example.bloadbank.Views.Fragment.ArticlesFragment;
import com.example.bloadbank.Views.Fragment.DonationOrdersFragment;
import com.example.bloadbank.adapters.ViewPagerWithFragmentAdapter;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment {




    TabLayout tablayoutHome;

    ViewPager viewpagerHome;
    ViewPagerWithFragmentAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        viewpagerHome = (ViewPager)root.findViewById(R.id.viewpager);
        tablayoutHome = (TabLayout) root.findViewById(R.id.tablayout_home);



        viewpagerHome.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablayoutHome));


        adapter = new ViewPagerWithFragmentAdapter(getChildFragmentManager());
        adapter.addPager(new ArticlesFragment() , getString(R.string.articles));
        adapter.addPager(new DonationOrdersFragment() , getString(R.string.donation_orders));
        viewpagerHome.setAdapter(adapter);
        tablayoutHome.setupWithViewPager(viewpagerHome);

        return root;
    }
}