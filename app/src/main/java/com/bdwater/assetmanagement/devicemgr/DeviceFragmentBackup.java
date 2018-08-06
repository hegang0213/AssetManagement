package com.bdwater.assetmanagement.devicemgr;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bdwater.assetmanagement.R;
import com.bdwater.assetmanagement.model.ColumnDescription;
import com.bdwater.assetmanagement.model.Device;
import com.bdwater.assetmanagement.soap.SoapAsyncTask;
import com.bdwater.assetmanagement.soap.SoapClient;
import com.bdwater.assetmanagement.soap.SoapTaskResult;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hegang on 17/11/12.
 */

public class DeviceFragmentBackup extends Fragment {
    public static final String DEVICE_ID = "DeviceIDString";

    Device device;
    String mDeviceIDString;
    SoapAsyncTask task;

//    @BindView(R.id.progressLayout)
//    public View progressLayout;
//    @BindView(R.id.progressBar)
//    public View progressBar;
//    @BindView(R.id.textView)
//    public TextView textView;
    @BindView(R.id.bodyLayout)
    public LinearLayout bodyLayout;

    public DeviceFragmentBackup() {
    }
    public static DeviceFragmentBackup newInstance(String deviceIDString) {
        DeviceFragmentBackup fragment = new DeviceFragmentBackup();
        Bundle args = new Bundle();
        args.putString(DEVICE_ID, deviceIDString);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDeviceIDString = getArguments().getString(DEVICE_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device, container, false);
        ButterKnife.bind(this, view);
        execute();
        return view;
    }

    LayoutInflater getInflater() {
        return getActivity().getLayoutInflater().from(getActivity());
    }
    // render all content of device
    void render() {
        LayoutInflater inflater = getInflater();
        if(device.Columns.length > 0) {
            // render columns property of device
            DeviceCard card = new DeviceCard(inflater);
            card.setTitleVisibility(DeviceCard.GONE);
            renderColumns(inflater, card.getContentLayout(), device);
            bodyLayout.addView(card.getView(), card.getLayoutParams());
        }
        for(Device child : device.Children) {
            // render sub device
            renderDevice(inflater, child);
        }
    }
    // renders content of sub device
    void renderDevice(LayoutInflater inflater, Device entry) {
        DeviceCard card = new DeviceCard(inflater);
        card.setTitle(entry.Name);  // sets title
        // render ui of columns of device
        renderColumns(inflater, card.getContentLayout(), entry);
        // adds the card view into body layout of this view
        bodyLayout.addView(card.getView(), card.getLayoutParams());

    }
    // render columns content of device
    void renderColumns(LayoutInflater inflater, LinearLayout parent, Device entry) {
        for (ColumnDescription cd : entry.Columns) {
            // creates ui view from device_column,
            // it include name and value
            View view = inflater.inflate(R.layout.device_column, null);
            ((TextView)view.findViewById(R.id.nameTextView)).setText(cd.Title);
            ((TextView)view.findViewById(R.id.valueTextView)).setText(cd.Value);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            // add ui into parent layout
            parent.addView(view, params);
        }
    }
    // executes async task to get data
    void execute() {
        // shows waiting content for user
//        progressLayout.setVisibility(View.VISIBLE);
//        progressBar.setVisibility(View.VISIBLE);
//        textView.setVisibility(View.VISIBLE);
//        textView.setText(getString(R.string.loading));

        // call async task to get data
        task = new SoapAsyncTask(SoapClient.GET_DEVICE_BY_ID_METHOD, taskListener);
        task.execute("", mDeviceIDString);
    }
    void success() {
        // it's success to get data,
        // renders ui content for device
        render();
//        progressLayout.setVisibility(View.GONE);
    }
    void failed(String message) {
        // it's failed to get data
        // shows error message
//        progressBar.setVisibility(View.GONE);
//        textView.setText(message);
//        textView.setVisibility(View.VISIBLE);
    }
    SoapAsyncTask.OnSoapTaskListener taskListener = new SoapAsyncTask.OnSoapTaskListener() {
        @Override
        public void onTaskExecuted(SoapTaskResult result) {
            if (result.Result == SoapTaskResult.TaskResultType.Success) {
                try {
                    String dataString = result.JSONResult.getString("data");
                    device = Device.parseDevice(new JSONObject(dataString));
                    success();
                } catch (JSONException e) {
                    e.printStackTrace();
                    failed(e.getMessage());
                }

            }
            else if(result.Result == SoapTaskResult.TaskResultType.Failed) {
                failed(result.Message);
            }
            else {
                failed(result.Exception.getMessage());
            }
        }
    };
}
