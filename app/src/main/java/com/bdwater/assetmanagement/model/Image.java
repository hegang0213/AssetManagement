package com.bdwater.assetmanagement.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hegang on 17/11/18.
 */

public class Image {
    public static final int FROM_LOCAL = 0;
    public static final int FROM_REMOTE = 1;

    public String LocalPath;
    @SerializedName(value="ImageUrl")
    public String RemotePath;
    @SerializedName(value="TroubleNoteDetailImageID")
    public String ImageID;
    public boolean IsOnlyForAdd;
    public int From = FROM_LOCAL;
    public boolean IsUploaded = false;
    public static Image IconForAdd() {
        Image image = new Image();
        image.IsOnlyForAdd = true;
        return image;
    }
}
