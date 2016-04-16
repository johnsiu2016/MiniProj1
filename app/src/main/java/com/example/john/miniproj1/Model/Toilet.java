package com.example.john.miniproj1.Model;

/**
 * Created by John on 16/4/2016.
 */
public class Toilet {
    private String name;
    private String address;
    private String distance;

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return name;
    }
}