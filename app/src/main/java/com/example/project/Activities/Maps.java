package com.example.project.Activities;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.example.project.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Maps extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker for your company and move the camera
        LatLng companyLocation = new LatLng(37.7749, -122.4194); // Replace with your company's latitude and longitude
        mMap.addMarker(new MarkerOptions().position(companyLocation).title("My Company"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(companyLocation, 15));
    }
}