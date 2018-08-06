package com.bdwater.assetmanagement.main;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bdwater.assetmanagement.R;
import com.bdwater.assetmanagement.model.Device;

import java.util.zip.Inflater;

/**
 * Created by hegang on 17/10/31.
 */

public class SimpleDeviceArrayAdapter extends ArrayAdapter<Device> {
    LayoutInflater inflater;
    Device[] devices;
    ViewHolder viewHolder;
    public SimpleDeviceArrayAdapter(@NonNull Context context, @NonNull Device[] objects) {
        super(context, R.layout.simple_device_item, objects);
        devices = objects;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            View view = inflater.inflate(R.layout.simple_device_item, null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView)view.findViewById(R.id.imageView);
            viewHolder.nameTextView = (TextView)view.findViewById(R.id.nameTextView);
            viewHolder.typeTextView = (TextView)view.findViewById(R.id.typeTextView);
            view.setTag(viewHolder);
            convertView = view;
        }
        else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.nameTextView.setText(devices[position].Name);
        viewHolder.typeTextView.setText(devices[position].InstallDate);
        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
        TextView nameTextView;
        TextView typeTextView;
    }
}
