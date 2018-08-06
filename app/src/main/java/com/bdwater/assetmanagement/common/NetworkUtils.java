package com.bdwater.assetmanagement.common;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by hegang on 17/10/18.
 */

public class NetworkUtils {
    public static String WEBSERVICE_URL = "http://222.222.178.213:1111/AssetManagementService/webservice.asmx";
    public static String UPDATE_URL = "http://222.222.178.213:1111/waterinputservice/update/update.json";
    public static String IMAGE_URL = "http://222.222.178.213:1111/AssetManagementService/images/";

    public static int getVersionCode(Context context) throws PackageManager.NameNotFoundException {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageInfo(
                context.getPackageName(), 0);
        return packageInfo.versionCode;
    }
    public static String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
}
