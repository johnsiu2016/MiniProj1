package com.example.john.miniproj1.Service;

import com.example.john.miniproj1.Model.Toilets;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by John on 15/4/2016.
 */
public interface ToiletAPI {
    @GET("lbitest/json-toilet-v2.php")
    Call<Toilets> getToilet(@Query("lat") String lat,
                            @Query("lng") String lng,
                            @Query("lang") String lang,
                            @Query("row_index") String row,
                            @Query("display_row") String displayRow
    );
}
