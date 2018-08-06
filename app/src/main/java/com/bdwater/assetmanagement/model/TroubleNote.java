package com.bdwater.assetmanagement.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hegang on 17/11/14.
 */

public class TroubleNote {
    public String TroubleNoteID;
    public String Name;
    public int Status;
    public String StatusName;
    public String CreateDate;
    public String ModifiedDate;
    public String Comment;
    public String SiteID;
    public String SiteName;
    public String InputerID;
    public TroubleNoteDetail[] Children;
    public static TroubleNote parse(JSONObject jo) throws JSONException {
        TroubleNote entry = new TroubleNote();
        entry.TroubleNoteID = jo.getString("TroubleNoteID");
        entry.SiteID = jo.getString("SiteID");
        entry.SiteName = jo.getString("SiteName");
        entry.Name = jo.getString("Name");
        entry.Status = jo.getInt("Status");
        entry.StatusName = jo.getString("StatusName");
        entry.CreateDate = jo.getString("CreateDate");
        entry.Comment = jo.getString("Comment");
        String s = jo.getString("Children");
        JSONArray jsonArray = new JSONArray(s);
        entry.Children = TroubleNoteDetail.parseArray(jsonArray).toArray(new TroubleNoteDetail[jsonArray.length()]);
        return entry;
    }
    public static List<TroubleNote> parseArray(JSONArray array) throws JSONException {
        List<TroubleNote> entries = new ArrayList<>();
        for(int i = 0; i < array.length(); i++) {
            TroubleNote entry = parse(array.getJSONObject(i));
            entries.add(entry);
        }
        return entries;
    }
    public int getFinishedCount() {
        int i = 0;
        for (TroubleNoteDetail detail : Children) {
            if(detail.Status == 1)
                i++;
        }
        return i;
    }
    public String toJsonString() throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("TroubleNoteID", this.TroubleNoteID);
        jo.put("Name", this.Name);
        jo.put("SiteID", this.SiteID);
        jo.put("InputerID", this.InputerID);
        return jo.toString();
    }

    public static class StatusList {
        public static final int DRAFT = -1;
        public static final int PROGRESS = 0;
        public static final int DONE = 1;
    }
}
