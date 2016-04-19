package com.example.john.miniproj1.Model;

/**
 * Created by John on 16/4/2016.
 */
public class Toilet {
    private String name;
    private String address;
    private String distance;
    private String lat;
    private String lng;

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getDistance() {
        return distance;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    @Override
    public String toString() {
        return name;
    }
}