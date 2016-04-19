package com.example.john.miniproj1;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.example.john.miniproj1.Model.Toilet;
import com.example.john.miniproj1.Model.Toilets;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    public static final String TOILETS = "TOILETS";
    public static final String LAT = "LAT";
    public static final String LNG = "LNG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
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

        Double lat = getIntent().getDoubleExtra(LAT, 40.714224);
        Double lng = getIntent().getDoubleExtra(LNG, -73.961452);

        mMap.addMarker(new MarkerOptions()
                .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                .position(new LatLng(lat, lng))
                .title("Current Location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(lat, lng), 15));

        addToiletsMarker();

    }



    private void addToiletsMarker() {
        String toi = getIntent().getStringExtra(TOILETS);
        Toilets toilets = (new Gson()).fromJson(toi, Toilets.class);

        Double lat;
        Double lng;
        for (Toilet toilet : toilets.getResults()) {
            lat = Double.parseDouble(toilet.getLat());
            lng = Double.parseDouble(toilet.getLng());

            Double dist = Math.round((Double.parseDouble(toilet.getDistance())*100))/100.0;
            // You can customize the marker image using images bundled with
            // your app, or dynamically generated bitmaps.
            mMap.addMarker(new MarkerOptions()
                    .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                    .position(new LatLng(lat, lng))
                    .title(toilet.getName())
                    .snippet(dist.toString() + "m"));

        }
    }
}
