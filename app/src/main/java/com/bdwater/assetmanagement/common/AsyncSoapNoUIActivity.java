package com.bdwater.assetmanagement.common;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;

import com.bdwater.assetmanagement.soap.SoapAsyncTask;
import com.bdwater.assetmanagement.soap.SoapTaskResult;

import org.json.JSONObject;

/**
 * Created by hegang on 17/11/14.
 */

public abstract class AsyncSoapNoUIActivity extends AppCompatSwipeBackActivity {
    SoapAsyncTask soapAsyncTask;
    View view;

    protected abstract String onGetSoapMethodName();
    protected abstract void onSoapAsyncTaskExecute(SoapAsyncTask task);
    protected abstract void onLoadSuccess(JSONObject result);
    protected void onLoadFailed(int code, String message) {}
    protected void onLoadException(String message){}
    protected abstract View getView();

    public void execute() {
        this.onLoading();
        this.soapAsyncTask = new SoapAsyncTask(onGetSoapMethodName(), listener);
        this.onSoapAsyncTaskExecute(this.soapAsyncTask);
    }


    protected void onLoading() {

    }
    protected void onLoaded() {

    }

    SoapAsyncTask.OnSoapTaskListener listener = new SoapAsyncTask.OnSoapTaskListener() {
        @Override
        public void onTaskExecuted(SoapTaskResult result) {
            final int code = result.Code;
            final String message = result.Message;
            if(result.Result == SoapTaskResult.TaskResultType.Success) {
                final JSONObject data = result.JSONResult;
                getView().post(new Runnable() {
                    @Override
                    public void run() {
                        onLoaded();
                        onLoadSuccess(data);
                    }
                });

            }
            else if(result.Result == SoapTaskResult.TaskResultType.Failed) {
                getView().post(new Runnable() {
                    @Override
                    public void run() {
                        onLoaded();
                        onLoadFailed(code, message);
                    }
                });

            }
            else {
                getView().post(new Runnable() {
                    @Override
                    public void run() {
                        onLoaded();
                        onLoadException(message);
                    }
                });

            }
        }
    };
}
