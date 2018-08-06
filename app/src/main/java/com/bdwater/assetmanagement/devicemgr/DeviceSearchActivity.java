package com.bdwater.assetmanagement.devicemgr;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bdwater.assetmanagement.R;
import com.bdwater.assetmanagement.common.AppCompatSwipeBackActivity;
import com.bdwater.assetmanagement.common.NetworkCardView;
import com.bdwater.assetmanagement.common.PageList;
import com.bdwater.assetmanagement.common.SearchViewUtils;
import com.bdwater.assetmanagement.model.Device;
import com.bdwater.assetmanagement.soap.SoapAsyncTask;
import com.bdwater.assetmanagement.soap.SoapClient;
import com.bdwater.assetmanagement.soap.SoapTaskResult;
import com.mikepenz.iconics.context.IconicsContextWrapper;
import com.mikepenz.iconics.context.IconicsLayoutInflater;
import com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DeviceSearchActivity extends AppCompatSwipeBackActivity {
    //AppCompatActivity {
    public static final String QUERY = "Query";

    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    public SearchView searchView;
    @BindView(R.id.swipeMenuRecyclerView)
    public SwipeMenuRecyclerView swipeMenuRecyclerView;
    @BindView(R.id.networkCardView)
    public NetworkCardView networkCardView;


    String queryString;
    SoapAsyncTask task;
    List<Device> items = new ArrayList<>();
    DeviceListRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_search);
        ButterKnife.bind(this);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        swipeMenuRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DeviceListRecyclerViewAdapter(items);
        swipeMenuRecyclerView.setSwipeItemClickListener(new SwipeItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Device device = items.get(position);
                Intent intent = new Intent(DeviceSearchActivity.this, DeviceActivity.class);
                intent.putExtra(DeviceActivity.DEVICE_ID_STRING, device.DeviceID);
                intent.putExtra(DeviceActivity.NAME, device.Name);
                intent.putExtra(DeviceActivity.INSTALL_DATE, device.InstallDate);
                startActivity(intent);
            }
        });
        swipeMenuRecyclerView.setAdapter(adapter);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase));
    }
    void search() {
        items.clear();
        networkCardView.loading();
        task = new SoapAsyncTask(SoapClient.GET_DEVICE_LIST_BY_SEARCH_METHOD, taskListener);
        task.execute("", queryString);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_view_only_menu, menu);
        searchView = (SearchView)menu.findItem(R.id.searchView).getActionView();
        SearchViewUtils.setSearchViewQueryHint(searchView, getString(R.string.device_query_hint));
        searchView.setBackgroundResource(R.drawable.search_view_bg);
        searchView.setSubmitButtonEnabled(true);
        searchView.setIconifiedByDefault(false);
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.equals("")) return true;
                queryString = query;
                searchView.clearFocus();
                search();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    void success() {
        searchView.post(new Runnable() {
            @Override
            public void run() {
                networkCardView.finish();
                swipeMenuRecyclerView.setVisibility(View.VISIBLE);
            }
        });
    }
    void failed(final String message) {
        searchView.post(new Runnable() {
            @Override
            public void run() {
                networkCardView.exception("", message);
                swipeMenuRecyclerView.setVisibility(View.GONE);
            }
        });
    }
    SoapAsyncTask.OnSoapTaskListener taskListener = new SoapAsyncTask.OnSoapTaskListener() {
        @Override
        public void onTaskExecuted(SoapTaskResult result) {
            if(result.Result == SoapTaskResult.TaskResultType.Success) {
                try {
                    String dataString = result.JSONResult.getString("data");
                    List<Device> list = Device.parseSimpleDeviceArray(new JSONArray(dataString));
                    items.addAll(list);
                    adapter.notifyDataSetChanged();

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
