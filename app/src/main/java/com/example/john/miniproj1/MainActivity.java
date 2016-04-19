package com.example.john.miniproj1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.john.miniproj1.Model.Toilets;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity implements ToiletFragment.Contract {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getFragmentManager().findFragmentById(R.id.container) == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new ToiletFragment())
                    .commit();
        }
    }

    @Override
    public void onLocationSelected(Toilets toilets, Double lat, Double lng) {
        Intent intent = new Intent(this, MapsActivity.class);
        String toi = (new Gson()).toJson(toilets);

        intent.putExtra(MapsActivity.TOILETS, toi);
        intent.putExtra(MapsActivity.LAT, lat);
        intent.putExtra(MapsActivity.LNG, lng);

        startActivity(intent);
    }

    @Override
    public void onToiletSelected(String toiletName, String lat, String lng) {
        Uri gmmIntentUri = Uri.parse("geo:" + lat + "," + lng + "?q=" + Uri.encode(toiletName));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }
}
