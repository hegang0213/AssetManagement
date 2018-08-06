package com.bdwater.assetmanagement.common;

import android.support.v7.app.AppCompatActivity;

import com.bdwater.assetmanagement.CApplication;
import com.bdwater.assetmanagement.model.Permission;
import com.bdwater.assetmanagement.model.PermissionLocal;
import com.bdwater.assetmanagement.model.User;

/**
 * Created by hegang on 17/11/23.
 */

public abstract class ExtAppCompatActivity extends AppCompatActivity {
    protected PermissionLocal getPermissionManager() {
        return ((CApplication)getApplication()).PermissionManager;
    }
    protected User getUser() {
        return ((CApplication)getApplication()).getUser();
    }
}
