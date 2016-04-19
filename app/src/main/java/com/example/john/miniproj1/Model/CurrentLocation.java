package com.example.john.miniproj1.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by John on 19/4/2016.
 */
public class CurrentLocation {
    @SerializedName("formatted_address")
    String formattedAddress;

    public String getFormattedAddress() {
        return formattedAddress;
    }
}
