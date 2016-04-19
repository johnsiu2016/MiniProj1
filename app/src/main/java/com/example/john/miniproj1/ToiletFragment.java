package com.example.john.miniproj1;


import android.app.ListFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.renderscript.Double2;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.john.miniproj1.Adapter.MyAdapter;
import com.example.john.miniproj1.Model.CurrentLocation;
import com.example.john.miniproj1.Model.CurrentLocations;
import com.example.john.miniproj1.Model.Toilets;
import com.example.john.miniproj1.Service.GoogleService;
import com.example.john.miniproj1.Service.ToiletService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ToiletFragment extends ListFragment implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final String TAG = "ToiletFragment";

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    private Retrofit retrofit;

    private ToiletService toiletService;
    private Call<Toilets> toiletsCall;

    private GoogleService googleSevice;
    private Call<CurrentLocations> googleCall;

    private String lat;
    private String lng;
    private String lang;
    private String toiletsNum;
    private String toiletsBaseUrl = "http://plbpc013.ouhk.edu.hk/";
    private String currentLocationsBaseUrl = "https://maps.googleapis.com/";

    SharedPreferences prefs;

    private Toilets mToilets;
    private Contract mContract;

    private TextView currentLocation;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);

        mContract = (ToiletFragment.Contract)getActivity();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        currentLocation = (TextView) rootView.findViewById(R.id.current_location);

        rootView.findViewById(R.id.current_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lat != null && lng != null) {
                    mContract.onLocationSelected(mToilets, Double.parseDouble(lat), Double.parseDouble(lng));
                }
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(toiletsBaseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        toiletService = retrofit.create(ToiletService.class);

        retrofit = new Retrofit.Builder()
                .baseUrl(currentLocationsBaseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        googleSevice = retrofit.create(GoogleService.class);

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
    public void onListItemClick(ListView l, View v, int position, long id) {
        mContract.onToiletSelected(mToilets.getResults().get(position).getName(), lat, lng);
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
        getGeoData();
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

        lang = prefs.getString("lang_list", "zh_tw");

        mySetTitle(lang);

        toiletsNum = prefs.getString("toilets_list", "10");

        toiletsCall = toiletService.getToilet(lat, lng, lang, "0", toiletsNum);

        toiletsCall.enqueue(new Callback<Toilets>() {
            @Override
            public void onResponse(Call<Toilets> call, Response<Toilets> response) {
                if (response.isSuccessful()) {
                    mToilets = response.body();
                    if (mToilets != null) {
                        setListAdapter(new MyAdapter(getActivity(), R.layout.row, R.id.name, mToilets.getResults()));
                    }
                }
            }

            @Override
            public void onFailure(Call<Toilets> call, Throwable t) {

            }
        });
    }

    private void getGeoData() {
        String latlng = lat + "," + lng;
        String key = "AIzaSyCKAFtLFhufGbucL2lGu3LJbDYWYW44EvA";

        googleCall = googleSevice.getLocations(latlng, lang, key);

        googleCall.enqueue(new Callback<CurrentLocations>() {
            @Override
            public void onResponse(Call<CurrentLocations> call, Response<CurrentLocations> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("OK")) {
                        String currentLoc = response.body().getResults().get(0).getFormattedAddress();
                        currentLocation.setText(currentLoc);
                    }
                }
            }

            @Override
            public void onFailure(Call<CurrentLocations> call, Throwable t) {

            }
        });

    }

    private void mySetTitle(String lang) {
        switch (lang) {
            case "zh_tw":
                getActivity().setTitle(R.string.app_name);
                break;
            case "zh_cn":
                getActivity().setTitle(R.string.app_name_cn);
                break;
            case "en":
                getActivity().setTitle(R.string.app_name_en);
                break;
            default:
                getActivity().setTitle(R.string.app_name);
                break;
        }
    }

    interface Contract {
        void onLocationSelected(Toilets toilets, Double lat, Double lng);
        void onToiletSelected(String toiletName, String lat, String lng);
    }
}
