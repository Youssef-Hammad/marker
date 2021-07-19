package com.example.marker.packagemanager;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.marker.R;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private int mResource;

    public CustomAdapter(Context context, int resource, ArrayList<String> objects) {
        super(context,resource,objects);
        mContext=context;
        mResource=resource;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView textViewName = (TextView) convertView.findViewById(R.id.listPkgName);

        textViewName.setText(getItem(position));

        //textViewName.setTextColor(Color.BLACK);

        return convertView;
    }

}
