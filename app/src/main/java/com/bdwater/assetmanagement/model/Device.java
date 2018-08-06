package com.bdwater.assetmanagement.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hegang on 17/10/18.
 */

public class Device {
    public String DeviceID;
    public String ParentID;
    public String Name;
    public String BigRepairCount;
    public String NextBigRepairDaysRemain;
    public String Address;
    public String Comment;
    public int Level;
    public String InstallDate;

    public boolean HasChild;

    public ColumnDescription[] Columns;
    public Device[] Children;

    public Device() {
        this.Columns = new ColumnDescription[0];
        this.Children = new Device[0];
    }

    public static List<Device> parseSimpleDeviceArray(JSONArray array) throws JSONException {
        List<Device> devices = new ArrayList<>();
        for(int i = 0; i < array.length(); i++) {
            Device device = parseSimpleDevice(array.getJSONObject(i));
            devices.add(device);
        }
        return devices;
    }
    public static Device parseSimpleDevice(JSONObject jsonObject) throws JSONException {
        Device device = new Device();
        device.DeviceID = jsonObject.getString("DeviceID");
        device.ParentID = jsonObject.getString("ParentID");
        device.Name = jsonObject.getString("Name");
        device.Address = jsonObject.getString("Address");
        device.InstallDate = jsonObject.getString("InstallDate");
        device.BigRepairCount = jsonObject.getString("BigRepairCount");
        device.NextBigRepairDaysRemain = jsonObject.getString("NextBigRepairDaysRemain");
        return device;
    }
    public static Device parseDevice(JSONObject jsonObject) throws JSONException {
        Device device = new Device();
        device.DeviceID = jsonObject.getString("DeviceID");
        device.ParentID = jsonObject.getString("ParentID");
        device.Name = jsonObject.getString("Name");
        device.Address = jsonObject.getString("Address");
        device.InstallDate = jsonObject.getString("InstallDate");
        device.Level = jsonObject.getInt("Level");
        if(jsonObject.has("BigRepairCount"))
            device.BigRepairCount = jsonObject.getString("BigRepairCount");
        if(jsonObject.has("NextBigRepairDaysRemain"))
            device.NextBigRepairDaysRemain = jsonObject.getString("NextBigRepairDaysRemain");
        JSONArray array = jsonObject.getJSONArray("Columns");
        device.Columns = new ColumnDescription[array.length()];
        for(int i = 0; i < array.length(); i++) {
            ColumnDescription column = new ColumnDescription();
            JSONObject columnObject = array.getJSONObject(i);
            column.Name = columnObject.getString("Name");
            column.Title = columnObject.getString("Title");
            column.Value = columnObject.getString("Value");
            device.Columns[i] = column;
        }
        array = jsonObject.getJSONArray("Children");
        device.Children = new Device[array.length()];
        for (int i = 0; i < array.length(); i++) {
            device.Children[i] = parseDevice(array.getJSONObject(i));
        }
        device.HasChild = device.Children.length > 0;
        return device;
    }
}


