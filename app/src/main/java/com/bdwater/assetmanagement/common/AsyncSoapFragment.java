package com.bdwater.assetmanagement.common;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;

import com.bdwater.assetmanagement.soap.SoapAsyncTask;
import com.bdwater.assetmanagement.soap.SoapTaskResult;

import org.json.JSONObject;

/**
 * Created by hegang on 17/11/14.
 */

public abstract class AsyncSoapFragment extends ExtFragment {
    SoapAsyncTask soapAsyncTask;
    NetworkCardView networkCardView;

    protected abstract NetworkCardView onGetNetworkCarView();
    protected abstract String onGetSoapMethodName();
    protected abstract void onSoapAsyncTaskExecute(SoapAsyncTask task);
    protected abstract void onLoadSuccess(JSONObject result);
    protected void onLoadFailed(int code, String message) {}
    protected void onLoadException(String message){}

    public void execute() {
        this.onLoading();
        this.soapAsyncTask = new SoapAsyncTask(onGetSoapMethodName(), listener);
        this.onSoapAsyncTaskExecute(this.soapAsyncTask);
    }

    @NonNull
    protected NetworkCardView getNetworkCardView() {
        if(this.networkCardView == null) {
            this.networkCardView = onGetNetworkCarView();
            if(this.networkCardView != null) {
                this.networkCardView.getRetryButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        execute();
                    }
                });
            }
        }
        return this.networkCardView;
    }

    protected void onLoading() {
        if(this.getNetworkCardView() != null)
            this.getNetworkCardView().loading();
    }
    protected void onLoaded() {
        if(this.getNetworkCardView() != null)
            this.getNetworkCardView().finish();
    }
    protected void showMessage(String message) {
        if(this.getNetworkCardView() != null) {
            this.getNetworkCardView().exception("", message);
        }
    }

    SoapAsyncTask.OnSoapTaskListener listener = new SoapAsyncTask.OnSoapTaskListener() {
        @Override
        public void onTaskExecuted(SoapTaskResult result) {
            final int code = result.Code;
            final String message = result.Message;
            if(result.Result == SoapTaskResult.TaskResultType.Success) {
                final JSONObject data = result.JSONResult;
                getNetworkCardView().post(new Runnable() {
                    @Override
                    public void run() {
                        onLoaded();
                        onLoadSuccess(data);
                    }
                });

            }
            else if(result.Result == SoapTaskResult.TaskResultType.Failed) {
                getNetworkCardView().post(new Runnable() {
                    @Override
                    public void run() {
                        showMessage(message);
                        onLoadFailed(code, message);
                    }
                });

            }
            else {
                getNetworkCardView().post(new Runnable() {
                    @Override
                    public void run() {
                        showMessage(message);
                        onLoadException(message);
                    }
                });

            }
        }
    };
}
