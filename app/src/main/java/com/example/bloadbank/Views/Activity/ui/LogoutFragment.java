package com.example.bloadbank.Views.Activity.ui;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.bloadbank.R;
import com.example.bloadbank.Views.Activity.HomeCycle;
import com.example.bloadbank.Views.Activity.UserCycleActivity;
import com.example.bloadbank.data.local.Saveddata;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class LogoutFragment extends Fragment {
    Saveddata saveddata;


    @BindView(R.id.logout_btnlogout)
    Button logoutBtnlogout;

    public LogoutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_logout, container, false);
        ButterKnife.bind(this, root);
        saveddata=new Saveddata(getActivity());
        // Inflate the layout for this fragment
        return root;
    }

    @OnClick(R.id.logout_btnlogout)
    public void onViewClicked() {
        showAlert();

    }
    public void showAlert()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("تحذير").setMessage(" هو انت يا باشا متاكد ان انت عاوز تسجل الخروج ؟")
                .setIcon(R.drawable.ic_logo);
                builder.setPositiveButton("ااطلع بره", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveddata.clearData();
                        Intent i = new Intent(getActivity() , UserCycleActivity.class);
                        getActivity().startActivity(i);
                    }
                });
                builder.setNegativeButton("لا يا عم", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getActivity() , HomeCycle.class);
                        getActivity().startActivity(i);

                    }
                });
                builder.show();
    }
}
