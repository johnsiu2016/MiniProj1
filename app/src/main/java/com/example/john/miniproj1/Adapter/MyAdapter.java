package com.example.john.miniproj1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.john.miniproj1.Model.Toilet;
import com.example.john.miniproj1.R;

import java.util.List;

/**
 * Created by John on 16/4/2016.
 */
public class MyAdapter extends ArrayAdapter<Toilet> {
    private Context mContext;

    public MyAdapter(Context context, int resource, int textViewResourceId, List<Toilet> objects) {
        super(context, resource, textViewResourceId, objects);
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ToiletViewHolder wrapper;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.row, null);
            wrapper = new ToiletViewHolder(mContext, convertView);
            convertView.setTag(wrapper);
        } else {
            wrapper = (ToiletViewHolder) convertView.getTag();
        }

        wrapper.populateFrom(getItem(position));

        return convertView;
    }
}