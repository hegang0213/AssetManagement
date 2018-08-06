package com.bdwater.assetmanagement.model;

import java.util.Date;

/**
 * Created by hegang on 17/10/30.
 */

public class User {
    public static final String TAG = "User";

    public String UserID;
    public String Name;
    public String UserName;
    public String Comment;
    public Date LastLogonDate;
    public Site[] Sites;
    public PermissionGrant[] PermissionGrants;
    public String getSiteName() {
        if(Sites.length == 0) return "";
        return Sites[0].Name;
    }
}
