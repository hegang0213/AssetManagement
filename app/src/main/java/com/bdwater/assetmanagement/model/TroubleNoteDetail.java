package com.bdwater.assetmanagement.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hegang on 17/11/14.
 */

public class TroubleNoteDetail {
    public String TroubleNoteID;
    public String TroubleNoteDetailID;
    public String ParentID;
    public String TopID;
    public String Content;
    public int Status;
    public String StatusName;
    public String StatusDateTime;
    public String CreateDate;
    public TroubleNoteDetail[] Children = new TroubleNoteDetail[0];
    public Image[] Images = new Image[0];
    public static TroubleNoteDetail parse(JSONObject jo) throws JSONException {
        TroubleNoteDetail entry = new TroubleNoteDetail();
        entry.TroubleNoteID = jo.getString("TroubleNoteID");
        entry.TroubleNoteDetailID = jo.getString("TroubleNoteDetailID");
        entry.Content = jo.getString("Content");
        entry.Status = jo.getInt("Status");
        entry.StatusName = jo.getString("StatusName");
        entry.StatusDateTime = jo.getString("StatusDateTime");
        entry.CreateDate = jo.getString("CreateDate");
        entry.Children = new TroubleNoteDetail[0];
        return entry;
    }
    public static List<TroubleNoteDetail> parseArray(JSONArray array) throws JSONException {
        List<TroubleNoteDetail> entries = new ArrayList<>();
        for(int i = 0; i < array.length(); i++) {
            TroubleNoteDetail entry = parse(array.getJSONObject(i));
            entries.add(entry);
        }
        return entries;
    }
    public String getParentID() {
        if(this.Children.length == 0) return this.TroubleNoteDetailID;
        return this.Children[this.Children.length - 1].TroubleNoteDetailID;
    }

    public static class Status {
        public static final int PROCESSING = 0;
        public static final int DONE = 1;
        public static final int RESPONSE = 2;
        public static final int REJECT = 3;
        public static String getName(int status) {
            switch (status) {
                case PROCESSING: return "进行中...";
                case DONE: return "完成";
                case RESPONSE: return "已回复";
                case REJECT: return "驳回";
                default:
                    return "";
            }
        }
    }
}
