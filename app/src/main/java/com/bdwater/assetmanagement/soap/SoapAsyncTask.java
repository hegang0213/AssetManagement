package com.bdwater.assetmanagement.soap;

import android.os.AsyncTask;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by hegang on 17/10/18.
 */

public class SoapAsyncTask extends AsyncTask<String, Void, String> {

    private OnSoapTaskListener TaskListener;
    private String SoapMethodName;
    private Boolean HasException = false;

    public SoapAsyncTask(OnSoapTaskListener listener) {
        super();
        this.TaskListener = listener;
    }
    public SoapAsyncTask(String soapMethodName, OnSoapTaskListener listener) {
        super();
        this.SoapMethodName = soapMethodName;
        this.TaskListener = listener;
    }

    @Override
    protected String doInBackground(String... params) {
        HasException = false;
        String result = null;
        try {
            if (this.SoapMethodName.equals(SoapClient.LOGIN_METHOD)) {
                return SoapClient.Login(params[0], params[1]);
            }
            else if(this.SoapMethodName.equals(SoapClient.CHECK_LOGIN_METHOD)) {
                return SoapClient.CheckLogin(params[0]);
            }
            else if(this.SoapMethodName.equals(SoapClient.GET_DEVICE_LIST_METHOD)) {
                return SoapClient.GetDeviceListOrderByDate(params[0], Integer.parseInt(params[1]), Integer.parseInt(params[2]));
            }
            else if(this.SoapMethodName.equals(SoapClient.GET_DEVICE_LIST_BY_SEARCH_METHOD)) {
                return SoapClient.GetDeviceListBySearch(params[0], params[1]);
            }
            else if(this.SoapMethodName.equals(SoapClient.GET_DEVICE_BY_ID_METHOD)) {
                return SoapClient.GetDeviceByID(params[0], params[1]);
            }
            else if(this.SoapMethodName.equals(SoapClient.GET_SERVICES_BY_DEVICE_ID_METHOD)) {
                return SoapClient.GetServicesByDeviceID(params[0]);
            }
            else if(this.SoapMethodName.equals(SoapClient.GET_TROUBLE_NOTES_METHOD)) {
                return SoapClient.GetTroubleNotes(params[0], params[1], params[2], params[3]);
            }
            else if(this.SoapMethodName.equals(SoapClient.GET_SITES_METHOD)) {
                return SoapClient.GetSites();
            }
            else if(this.SoapMethodName.equals(SoapClient.UPDATE_TROUBLE_NOTE_METHOD)) {
                return SoapClient.UpdateTroubleNote(Integer.parseInt(params[0]), params[1]);
            }
            else if(this.SoapMethodName.equals(SoapClient.PUBLISH_TROUBLE_NOTE_METHOD)) {
                return SoapClient.PublishTroubleNote(params[0]);
            }
            else if(this.SoapMethodName.equals(SoapClient.UPDATE_RESPONSE_METHOD)) {
                return SoapClient.UpdateResponse(Integer.parseInt(params[0]), Integer.parseInt(params[1]), params[2]);
            } else {
                throw new Exception("No this method name:" + this.SoapMethodName);
            }

        } catch (IOException e) {
            e.printStackTrace();
            onTaskException(e);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            onTaskException(e);
        } catch (Exception e) {
            e.printStackTrace();
            onTaskException(e);
        }
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        if(HasException)
            return;

        try {
            SoapResult sr = new SoapResult();
            sr = new Gson().fromJson(s, SoapResult.class);
//            JSONObject jsonObject = new JSONObject(s);
//            Integer result = jsonObject.getInt("code");
            switch (sr.code) {
                case 0:
                    JSONObject jsonObject = new JSONObject(s);
                    onTaskSuccess(jsonObject);
                    break;
                default:
//                    String message = jsonObject.getString("message");
                    onTaskFailed(sr.code, sr.message);
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            onTaskException(e);
        }

    }

    protected void onTaskSuccess(JSONObject data) {
        if(null != this.TaskListener)
            this.TaskListener.onTaskExecuted(SoapTaskResult.Success(data));
    }
    protected void onTaskException(Exception exception) {
        HasException = true;
        if(null != this.TaskListener)
            this.TaskListener.onTaskExecuted(SoapTaskResult.Exception(exception));
    }

    protected void onTaskFailed(Integer code, String message) {
        if(null != this.TaskListener)
            this.TaskListener.onTaskExecuted(SoapTaskResult.Failed(code, message));
    }

    public interface OnSoapTaskListener {
        void onTaskExecuted(SoapTaskResult result);
    }
}