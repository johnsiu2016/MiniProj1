package com.example.john.miniproj1.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import com.example.john.miniproj1.Model.Toilet;
import com.example.john.miniproj1.R;

/**
 * Created by John on 16/4/2016.
 */
public class ToiletViewHolder {
    private Context mContext;

    private String label;

    private TextView name;
    private TextView address;
    private TextView distance;
    private TextView distanceLabel;
    private TextView unitLabel;

    public ToiletViewHolder(Context context, View row) {
        this.mContext = context;

        this.label = getLabel();

        this.name = (TextView)row.findViewById(R.id.name);
        this.address = (TextView)row.findViewById(R.id.address);
        this.distance = (TextView)row.findViewById(R.id.distance);
        this.distanceLabel = (TextView)row.findViewById(R.id.distance_label);
        this.unitLabel = (TextView) row.findViewById(R.id.unit_label);
    }

    public TextView getName() {
        return name;
    }

    public TextView getAddress() {
        return address;
    }

    public TextView getDistance() {
        return distance;
    }

    public TextView getDistanceLabel() {
        return distanceLabel;
    }

    public TextView getUnitLabel() {
        return unitLabel;
    }

    public void populateFrom(Toilet toilet) {
        getName().setText(toilet.getName());

        getAddress().setText(toilet.getAddress());

        Double dist = Math.round((Double.parseDouble(toilet.getDistance())*100))/100.0;
        String distance = dist.toString();
        getDistance().setText(distance);

        getDistanceLabel().setText(label);
        getUnitLabel().setText("m");
    }

    private String getLabel() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        String lang = prefs.getString("lang_list", "zh_tw");
        String label;
        switch (lang) {
            case "zh_tw:":
                label = mContext.getString(R.string.distance_zh_tw);
                break;
            case "zh_cn":
                label = mContext.getString(R.string.distance_zh_cn);
                break;
            case "en":
                label = mContext.getString(R.string.distance_en_us);
                break;
            default:
                label = mContext.getString(R.string.distance_zh_tw);
                break;
        }
        return label;
    }
}
