package com.example.john.miniproj1.Model;

/**
 * Created by John on 15/4/2016.
 */
public class Results {
    String name;
    String address;
    String distance;

    @Override
    public String toString() {
        return "Results{" +
                "address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", distance='" + distance + '\'' +
                '}';
    }
}
