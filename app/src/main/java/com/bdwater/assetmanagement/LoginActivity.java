package com.bdwater.assetmanagement;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bdwater.assetmanagement.common.AppCompatSwipeBackActivity;
import com.bdwater.assetmanagement.common.CustomProgressDialog;
import com.bdwater.assetmanagement.model.User;
import com.bdwater.assetmanagement.soap.SoapAsyncTask;
import com.bdwater.assetmanagement.soap.SoapClient;
import com.bdwater.assetmanagement.soap.SoapTaskResult;
import com.google.gson.Gson;
import com.mikepenz.iconics.context.IconicsContextWrapper;
import com.mikepenz.iconics.context.IconicsLayoutInflater;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatSwipeBackActivity {
    public static final String ARG_BY_LOGOUT = "ByLogout";
    public static final Boolean BY_LOGOUT = true;

    CApplication app;

    View view;
    CustomProgressDialog progressDialog;
    SoapAsyncTask task;

    @BindView(R.id.usernameEditText)
    EditText usernameEditText;
    @BindView(R.id.passwordEditText)
    EditText passwordEditText;
    @BindView(R.id.loginButton)
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));
        super.onCreate(savedInstanceState);
        view = getLayoutInflater().inflate(R.layout.activity_login, null);
        setContentView(view);

        app = (CApplication)getApplication();

        ButterKnife.bind(this);
        this.init();
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase));
    }

    void init() {
        Intent intent = getIntent();
        if(intent.hasExtra(ARG_BY_LOGOUT)) {
            // logout by user, enables swipe back to true
            if(intent.getBooleanExtra(ARG_BY_LOGOUT, false) == BY_LOGOUT)
                setSwipeBackEnable(true);
        }
        else
            // disables swipe back, call by splash activity
            setSwipeBackEnable(false);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }
    void login() {
        view.requestFocus();

        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if(username.trim().equals("")) {
            showMessage(R.string.username_validate_not_empty);
            return;
        }
        if(password.trim().equals("")) {
            showMessage(R.string.password_validate_not_empty);
            return;
        }

        progressDialog = new CustomProgressDialog(this);
        progressDialog.show();

        task = new SoapAsyncTask(SoapClient.LOGIN_METHOD, listener);
        task.execute(username, password);
    }
    void startMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    void showMessage(int resId) {
        showMessage(getString(resId));
    }
    void showMessage(String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }
    SoapAsyncTask.OnSoapTaskListener listener = new SoapAsyncTask.OnSoapTaskListener() {
        @Override
        public void onTaskExecuted(SoapTaskResult result) {
            view.post(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                }
            });
            try {
                if (result.Result == SoapTaskResult.TaskResultType.Success) {
                    // login is success, store user info
                    String jsonString = result.JSONResult.getString("data");
                    Gson gson = new Gson();
                    User user = gson.fromJson(jsonString, User.class);
                    user.LastLogonDate = new Date();
                    app.setUser(user);
                    app.login(user);
                    startMainActivity();
                } else {
                    showMessage(result.Message);
                }
            }
            catch (Exception exception) {
                showMessage(exception.getMessage());
            }
        }
    };

}
