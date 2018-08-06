package com.bdwater.assetmanagement.devicemgr;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bdwater.assetmanagement.R;
import com.bdwater.assetmanagement.common.AsyncSoapFragment;
import com.bdwater.assetmanagement.common.NetworkCardView;
import com.bdwater.assetmanagement.model.Service;
import com.bdwater.assetmanagement.soap.SoapAsyncTask;
import com.bdwater.assetmanagement.soap.SoapClient;
import com.bdwater.assetmanagement.soap.SoapTaskResult;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ServiceFragment extends AsyncSoapFragment {
    private static final String DEVICE_ID = "DeviceIDString";
    private String mDeviceIDString;

    ServiceListRecyclerViewAdapter adapter;

    @BindView(R.id.networkCardView)
    public NetworkCardView networkCardView;
    @BindView(R.id.swipeMenuRecyclerView)
    public SwipeMenuRecyclerView swipeMenuRecyclerView;

    public ServiceFragment() {
    }


    public static ServiceFragment newInstance(String deviceIDString) {
        ServiceFragment fragment = new ServiceFragment();
        Bundle args = new Bundle();
        args.putString(DEVICE_ID, deviceIDString);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDeviceIDString = getArguments().getString(DEVICE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_service, container, false);
        ButterKnife.bind(this, view);
        swipeMenuRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        execute();
        return view;
    }


    @Override
    protected NetworkCardView onGetNetworkCarView() {
        return this.networkCardView;
    }

    @Override
    protected String onGetSoapMethodName() {
        return SoapClient.GET_SERVICES_BY_DEVICE_ID_METHOD;
    }

    @Override
    protected void onSoapAsyncTaskExecute(SoapAsyncTask task) {
        task.execute(this.mDeviceIDString);
    }

    @Override
    protected void onLoading() {
        super.onLoading();
        this.swipeMenuRecyclerView.setVisibility(View.GONE);
    }

    @Override
    protected void onLoaded() {
        super.onLoaded();
        this.swipeMenuRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onLoadSuccess(JSONObject result) {
        try {
            String dataString = result.getString("data");
            JSONArray ja = new JSONArray(dataString);
            List<Service> services = Service.parseArray(ja);
            adapter = new ServiceListRecyclerViewAdapter(services);
            swipeMenuRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            if(services.size() == 0)
                this.getNetworkCardView().noData();
        } catch (JSONException e) {
            e.printStackTrace();
            showMessage(e.getMessage());
        }
    }
}
