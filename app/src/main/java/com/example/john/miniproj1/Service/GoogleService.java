package com.example.john.miniproj1.Service;

import com.example.john.miniproj1.Model.CurrentLocations;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by John on 19/4/2016.
 */
public interface GoogleService {
    @GET("maps/api/geocode/json")
    Call<CurrentLocations> getLocations(@Query("latlng") String latlng,
                                        @Query("language") String lang,
                                        @Query("key") String key);
}
