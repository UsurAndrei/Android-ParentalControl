package com.example.parentapp;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.parentapp.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
        // Get intent that started the activity, which has the coords we need, save them in local double variables
        Intent intent = getIntent();
        Double lat = Double.parseDouble(intent.getStringExtra("latitude"));
        Double lng = Double.parseDouble(intent.getStringExtra("longitude"));
        String timestamp = intent.getStringExtra("timestamp");

        // Create LatLng object using our coords
        LatLng coordinates = new LatLng(lat, lng);

        // Add marker using our coords
        mMap.addMarker(new MarkerOptions().position(coordinates).title(timestamp));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(coordinates));
    }

    // Application was crashing if going back from Maps (did not look into it much)
    @Override
    public void onBackPressed() {
        finish();
    }
}