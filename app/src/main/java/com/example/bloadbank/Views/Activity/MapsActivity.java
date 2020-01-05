package com.example.bloadbank.Views.Activity;
import com.example.bloadbank.R;
import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.bloadbank.Views.Fragment.NewDonationFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.example.bloadbank.helper.BloodBankConstants.LATITUDE;
import static com.example.bloadbank.helper.BloodBankConstants.LOCATION;
import static com.example.bloadbank.helper.BloodBankConstants.LONGITUDE;
import static com.example.bloadbank.data.local.SharedPreferencesManger.LoadData;
import static com.example.bloadbank.data.local.SharedPreferencesManger.SaveData;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    double lat,lang;
    NewDonationFragment newDonationFragment;
    Button setlocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        newDonationFragment = new NewDonationFragment();
        setlocation=(Button) findViewById(R.id.setlocation);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(888.22, 522.17);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        //TODO map on click
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.addMarker(new MarkerOptions().title("my place").position(latLng));
                Toast.makeText(getApplicationContext(), "coordinate : " + latLng, Toast.LENGTH_LONG).show();
//
                lat = latLng.latitude;
                lang =latLng.longitude;



            }
        });
        setlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getLocation(lat,lang);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                finish();
            }
        });

    }
    public  void getLocation(double latitude , double longitude) throws IOException {
        Geocoder gc = new Geocoder(MapsActivity.this , Locale.getDefault());
        if(gc.isPresent()){
            List<Address> list = gc.getFromLocation(latitude , longitude , 1);
            Address address = list.get(0);
            StringBuffer str = new StringBuffer();
            str.append(address.getAddressLine(0));
            Log.d(LOCATION , str.toString());
            String location = str.toString();
            SaveData(MapsActivity.this , LOCATION , location);
            SaveData(MapsActivity.this , LATITUDE , String.valueOf(address.getLatitude()));
            SaveData(MapsActivity.this , LONGITUDE , String.valueOf(address.getLongitude()));

            Toast.makeText(MapsActivity.this, LoadData(MapsActivity.this , LOCATION), Toast.LENGTH_LONG).show();
        }

    }

}
