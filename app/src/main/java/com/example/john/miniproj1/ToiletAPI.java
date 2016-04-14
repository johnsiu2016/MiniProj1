package com.example.john.miniproj1;

import com.example.john.miniproj1.Model.Toilet;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by John on 15/4/2016.
 */
public interface ToiletAPI {
    @GET("lbitest/json-toilet-v2.php")
    Call<Toilet> getToilet(@Query("lat") String lat, @Query("lng") String lng, @Query("lang") String lang);
}
