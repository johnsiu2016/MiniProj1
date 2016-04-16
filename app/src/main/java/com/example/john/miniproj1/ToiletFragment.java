package com.example.john.miniproj1;


import android.app.Fragment;
import android.app.ListFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.john.miniproj1.Adapter.MyAdapter;
import com.example.john.miniproj1.Model.Toilets;
import com.example.john.miniproj1.Service.ToiletAPI;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class ToiletFragment extends ListFragment implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final String TAG = "ToiletFragment";

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    private Retrofit retrofit;
    private ToiletAPI toiletAPI;
    private Call<Toilets> call;

    private String lat;
    private String lng;
    private String lang;
    private String toiletsNum;
    private String baseUrl = "http://plbpc013.ouhk.edu.hk/";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        toiletAPI = retrofit.create(ToiletAPI.class);

        buildGoogleApiClient();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            case R.id.reset:
                getToiletData();
                Toast.makeText(getActivity(), "Updataed", Toast.LENGTH_SHORT).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * Builds a GoogleApiClient. Uses the addApi() method to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        // Provides a simple way of getting a device's location and is well suited for
        // applications that do not require a fine-grained location and that do not need location
        // updates. Gets the best and most recent location currently available, which may be null
        // in rare cases when a location is not available.
        getLocationData();
        getToiletData();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i("aaaaa", "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i("aaaaaaa", "Connection suspended");
        mGoogleApiClient.connect();
    }

    private void getLocationData() {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            lat = Double.toString(mLastLocation.getLatitude());
            lng = Double.toString(mLastLocation.getLongitude());
        } else {
            Toast.makeText(getActivity(), "No location detected", Toast.LENGTH_LONG).show();
        }
    }

    private void getToiletData() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        lang = prefs.getString("lang_list", "zh_tw");
        toiletsNum = prefs.getString("toilets_list", "10");

        call = toiletAPI.getToilet(lat, lng, lang, "0", toiletsNum);

        call.enqueue(new Callback<Toilets>() {
            @Override
            public void onResponse(Call<Toilets> call, Response<Toilets> response) {
                if (response.isSuccessful()) {
                    Toilets toilets = response.body();
                    if (toilets != null) {
                        setListAdapter(new MyAdapter(getActivity(), R.layout.row, R.id.name, toilets.getResults()));
                    }
                }
            }

            @Override
            public void onFailure(Call<Toilets> call, Throwable t) {

            }
        });
    }
}
