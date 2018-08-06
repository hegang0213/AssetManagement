package com.bdwater.assetmanagement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bdwater.assetmanagement.common.AsyncSoapNoUIActivity;
import com.bdwater.assetmanagement.model.User;
import com.bdwater.assetmanagement.soap.SoapAsyncTask;
import com.bdwater.assetmanagement.soap.SoapClient;
import com.bdwater.assetmanagement.soap.SoapTaskResult;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Date;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class SplashActivity extends AsyncSoapNoUIActivity {
    CApplication app;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = getLayoutInflater().inflate(R.layout.activity_splash, null);
        setContentView(view);
        setSwipeBackEnable(false);

        app = (CApplication)getApplication();
        app.loadSharedPreferences();
        if(!app.isLogin()) {
            if(!app.noUser())   // load and check user which was logon ast last
                execute();
            else                // start login activity
                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startLogin();
                        finish();
                    }
                }, 3000);
        }
        else                    // the task has been logon, continue main activity
            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startMainActivity();
                    finish();
                }
            }, 3000);
    }

    @Override
    protected String onGetSoapMethodName() {
        return SoapClient.CHECK_LOGIN_METHOD;
    }

    @Override
    protected void onSoapAsyncTaskExecute(SoapAsyncTask task) {
        task.execute(app.getUser().UserID);
    }

    @Override
    protected void onLoadSuccess(JSONObject result) {
        try {
            // login is success, store user info
            String jsonString = result.getString("data");
            Gson gson = new Gson();
            User user = gson.fromJson(jsonString, User.class);
            user.LastLogonDate = new Date();
            app.setUser(user);
            app.login(user);
            startMainActivity();
            finish();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected View getView() {
        return view;
    }

    void startLogin() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
    }
    void startMainActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
