package com.bdwater.assetmanagement.devicemgr;

import android.graphics.Rect;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bdwater.assetmanagement.R;
import com.bdwater.assetmanagement.common.AsyncSoapActivity;
import com.bdwater.assetmanagement.common.AsyncSoapFragment;
import com.bdwater.assetmanagement.common.NetworkCardView;
import com.bdwater.assetmanagement.model.ColumnDescription;
import com.bdwater.assetmanagement.model.Device;
import com.bdwater.assetmanagement.soap.SoapAsyncTask;
import com.bdwater.assetmanagement.soap.SoapClient;
import com.bdwater.assetmanagement.soap.SoapTaskResult;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hegang on 17/11/12.
 */

public class DeviceFragment extends AsyncSoapFragment {
    public static final String DEVICE_ID = "DeviceIDString";

    Device device;
    String mDeviceIDString;

    @BindView(R.id.networkCardView)
    public NetworkCardView networkCardView;
    @BindView(R.id.bodyLayout)
    public LinearLayout bodyLayout;
    @BindView(R.id.installDateTextView)
    public TextView installDateTextView;
    @BindView(R.id.addressTextView)
    public TextView addressTextView;

    public DeviceFragment() {
    }
    public static DeviceFragment newInstance(String deviceIDString) {
        DeviceFragment fragment = new DeviceFragment();
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
        super.execute();
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

    @Override
    protected NetworkCardView onGetNetworkCarView() {
        return this.networkCardView;
    }

    @Override
    protected String onGetSoapMethodName() {
        return SoapClient.GET_DEVICE_BY_ID_METHOD;
    }

    @Override
    protected void onSoapAsyncTaskExecute(SoapAsyncTask task) {
        task.execute("", mDeviceIDString);
    }

    @Override
    protected void onLoadSuccess(JSONObject result) {
        try {
            String dataString = result.getString("data");
            device = Device.parseDevice(new JSONObject(dataString));
            ((DeviceActivity)getActivity()).setTitle(device.Name);
            installDateTextView.setText(device.InstallDate);
            addressTextView.setText(device.Address);
            render();
        } catch (JSONException e) {
            showMessage(e.getMessage());
            e.printStackTrace();
        }
    }

}
