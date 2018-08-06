package com.bdwater.assetmanagement;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.CoordinatorLayout;
import android.util.Base64;

import com.bdwater.assetmanagement.model.Permission;
import com.bdwater.assetmanagement.model.PermissionLocal;
import com.bdwater.assetmanagement.model.TroubleNote;
import com.bdwater.assetmanagement.model.User;
import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

/**
 * Created by hegang on 17/10/30.
 */

public class CApplication extends Application {
    public PermissionLocal PermissionManager = PermissionLocal.instance();
    CoordinatorLayout mainCoordinatorLayout;

    public void setMainCoordinatorLayout(CoordinatorLayout mainCoordinatorLayout) {
        this.mainCoordinatorLayout = mainCoordinatorLayout;
    }

    public CoordinatorLayout getMainCoordinatorLayout() {
        return mainCoordinatorLayout;
    }

    boolean mIsLogin = false;
    User user = null;
    public boolean noUser() { return user == null; }
    public boolean isLogin() {
        return mIsLogin;
    }
    public void login(User user) {
        setObject(User.TAG, user);
        mIsLogin = true;
    }
    public User getUser() {
        return this.user;
    }
    public void setUser(String userID, String username, String comment) {
        user.UserID = userID;
        user.UserName = username;
        user.Comment = comment;
    }
    public void setUser(User user) {
        this.user = user;
    }

    TroubleNote troubleNote;
    public TroubleNote getTroubleNote() {
        return troubleNote;
    }
    public void setTroubleNote(TroubleNote troubleNote) {
        this.troubleNote = troubleNote;
    }

    // share preferences
    private SharedPreferences getMySharedPreferences() {
        return getSharedPreferences("root", Context.MODE_PRIVATE);
    }
    private void setObject(String key, Object object) {
        SharedPreferences sp = getMySharedPreferences();
        try {
            //然后通过将字对象进行64转码，写入key值为key的sp中
            String jsonString = new Gson().toJson(object);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(key, jsonString);
            editor.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public <T> T getObject(String key, Class<T> clazz) {
        SharedPreferences sp = getMySharedPreferences();
        if (sp.contains(key)) {
            String jsonString = sp.getString(key, null);
            try {
                T t = (T)(new Gson().fromJson(jsonString, clazz));
                return t;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public void loadSharedPreferences() {
        SharedPreferences sp = getMySharedPreferences();
        if(sp.contains(User.TAG)) {
            User user = getObject(User.TAG, User.class);
            setUser(user);
        }
    }
}
