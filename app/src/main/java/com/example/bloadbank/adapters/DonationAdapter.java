package com.example.bloadbank.adapters;

import com.example.bloadbank.R;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.bloadbank.Views.Activity.BaseActivity;
import com.example.bloadbank.Views.Fragment.DonationDetailsFragment;
import com.example.bloadbank.data.model.donationrequests.DonationData;

import java.util.ArrayList;
import java.util.List;

import static com.example.bloadbank.helper.HelperMethod.ReplaceFragment;


/**
 * Created by medo on 13/11/2016.
 */

public class DonationAdapter extends RecyclerView.Adapter<DonationAdapter.DonationViewHolder> {
    private Context context;
    private BaseActivity activity;
    private List<DonationData> donationList = new ArrayList<>();
    String num;


    public DonationAdapter(Context context, BaseActivity activity, List<DonationData> donationList) {
        this.context = context;
        this.activity = activity;
        this.donationList = donationList;
    }

    @NonNull
    @Override
    public DonationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.donationorder_item, parent, false);
        return new DonationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonationViewHolder holder, int position) {
        DonationData data = donationList.get(position);
        num = data.getClient().getPhone();
        holder.donation_name.setText(data.getClient().getName());
        holder.donation_bloadtype.setText(data.getBloodType().getName());
        holder.donation_hospital.setText(data.getHospitalName());
        holder.donation_city.setText(data.getCity().getName());


        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + data.getPhone()));
                activity.startActivity(intent);
//                ReplaceFragment(activity.getSupportFragmentManager(),
//                        new NewDonationFragment(), R.id.nav_host_fragment
//                        , null, "medo");
            }
        });
        holder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               DonationDetailsFragment donationdetail = new DonationDetailsFragment();
               donationdetail.data = donationList.get(position);
                ReplaceFragment(activity.getSupportFragmentManager(),
                        donationdetail, R.id.nav_host_fragment
                        , null, null);
            }
        });

    }

    @Override
    public int getItemCount() {
        return donationList.size();
    }

    public class DonationViewHolder extends RecyclerView.ViewHolder {
        TextView donation_bloadtype, donation_name, donation_hospital, donation_city;
        Button call, details;

        public DonationViewHolder(@NonNull View itemView) {
            super(itemView);
            donation_bloadtype = (TextView) itemView.findViewById(R.id.donation_bloadtype);
            donation_name = (TextView) itemView.findViewById(R.id.donation_name);
            donation_hospital = (TextView) itemView.findViewById(R.id.donnation_hospital);
            donation_city = (TextView) itemView.findViewById(R.id.donation_city);
            call = (Button) itemView.findViewById(R.id.call_button);
            details = (Button) itemView.findViewById(R.id.details_button);


        }
    }
}
