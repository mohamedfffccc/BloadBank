package com.example.bloadbank.Views.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.bloadbank.R;
import com.example.bloadbank.data.api.UserApi;
import com.example.bloadbank.data.local.Saveddata;
import com.example.bloadbank.data.model.donationrequests.DonationData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.bloadbank.data.api.ClientApi.GetClient;


public class DonationDetailsFragment extends BaseFragment implements OnMapReadyCallback {
    public DonationData data;
    Saveddata saveddata;
    @BindView(R.id.donationdetails_name)
    TextView donationdetailsName;
    @BindView(R.id.donationdetails_age)
    TextView donationdetailsAge;
    @BindView(R.id.donationdetails_bloadtype)
    TextView donationdetailsBloadtype;
    @BindView(R.id.donationdetails_bagsnumber)
    TextView donationdetailsBagsnumber;
    @BindView(R.id.donationdetails_hospitalname)
    TextView donationdetailsHospitalname;
    @BindView(R.id.donationdetails_hospitaladdress)
    TextView donationdetailsHospitaladdress;
    @BindView(R.id.donationdetails_phone)
    TextView donationdetailsPhone;
    @BindView(R.id.donationdetails_detailnotes)
    TextView donationdetailsDetailnotes;
    @BindView(R.id.details_btncall)
    Button detailsBtncall;
    private GoogleMap mMap;
    UserApi userApi;
    double lat, lang;
    private LatLng sydney;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_donation_details, container, false);
        ButterKnife.bind(this, view);
        userApi = GetClient().create(UserApi.class);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.datailsmap);
        mapFragment.getMapAsync(this);
        AddData();

        return view;
    }

    public void AddData() {
        try {
            donationdetailsName.setText("الاســم :" + data.getPatientName());
            donationdetailsAge.setText("العــمر :" + data.getPatientAge());
            donationdetailsHospitalname.setText("اسم المستـشفي :" + data.getHospitalName());
            donationdetailsHospitaladdress.setText("عنوان المستـشفي :" + data.getHospitalAddress());
            donationdetailsPhone.setText("رقم التليفون :" + data.getPhone());
            donationdetailsBagsnumber.setText("عدد الاكياس :" + data.getBagsNum());
            donationdetailsBloadtype.setText("فصيلة الدم :" + data.getBloodType().getName());
            donationdetailsDetailnotes.setText("ملاحــظات :" + data.getNotes());
            lat = Double.parseDouble(data.getLatitude());
            Log.d("data", "" + lat);
            lang = Double.parseDouble(data.getLongitude());


        } catch (Exception e) {

        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        sydney = new LatLng(lat, lang);
        mMap = googleMap;
        mMap.addMarker(new MarkerOptions().position(sydney).title(data.getHospitalName()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10.0f));


    }

    @OnClick(R.id.details_btncall)
    public void onViewClicked() {
        Intent i = new Intent(Intent.ACTION_CALL , Uri.parse("tel:" + data.getPhone()));
        startActivity(i);
    }

//
}
