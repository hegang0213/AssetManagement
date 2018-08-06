package com.bdwater.assetmanagement.model;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hegang on 17/11/14.
 */

public class Service {
    public String ServiceID;
    public int Type;
    public String ServiceDate;
    public String TypeName;
    public String Title;
    public String Content;
    public int BigRepairIndex;
    public static Service parse(@NonNull JSONObject jo) throws JSONException {
        Service service = new Service();
        service.ServiceID = jo.getString("ServiceID");
        service.Type = jo.getInt("Type");
        service.ServiceDate = jo.getString("ServiceDate");
        service.TypeName = jo.getString("TypeName");
        service.Title = jo.getString("Title");
        service.Content = jo.getString("Content");
        service.BigRepairIndex = jo.getInt("BigRepairIndex");
        return service;
    }
    public static List<Service> parseArray(@NonNull JSONArray ja) throws JSONException {
        List<Service> services = new ArrayList<>();
        for(int i = 0; i < ja.length(); i++) {
            Service service = parse(ja.getJSONObject(i));
            services.add(service);
        }
        return services;
    }
}
