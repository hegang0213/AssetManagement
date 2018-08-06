package com.bdwater.assetmanagement.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hegang on 17/11/16.
 */

public class Site {
    public String SiteID;
    public String ParentID;
    public String Name;
    public int Level;
    public static Site parse(JSONObject jo) throws JSONException {
        Site entry = new Site();
        entry.SiteID = jo.getString("SiteID");
        entry.ParentID = jo.getString("ParentID");
        entry.Name = jo.getString("Name");
        entry.Level = jo.getInt("Level");
        return entry;
    }
    public static List<Site> parseArray(JSONArray ja) throws JSONException {
        List<Site> result = new ArrayList<>();
        for(int i = 0; i < ja.length(); i++) {
            result.add(Site.parse(ja.getJSONObject(i)));
        }
        return result;
    }
}
