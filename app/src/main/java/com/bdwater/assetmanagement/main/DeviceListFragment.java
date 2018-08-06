package com.bdwater.assetmanagement.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bdwater.assetmanagement.CApplication;
import com.bdwater.assetmanagement.R;
import com.bdwater.assetmanagement.common.DefinedLoadMoreView;
import com.bdwater.assetmanagement.common.IconicsHelper;
import com.bdwater.assetmanagement.common.PaddingDecoration;
import com.bdwater.assetmanagement.common.PageList;
import com.bdwater.assetmanagement.devicemgr.DeviceActivity;
import com.bdwater.assetmanagement.devicemgr.DeviceListRecyclerViewAdapter;
import com.bdwater.assetmanagement.devicemgr.DeviceSearchActivity;
import com.bdwater.assetmanagement.model.Device;
import com.bdwater.assetmanagement.model.User;
import com.bdwater.assetmanagement.scan.FullScannerActivity;
import com.bdwater.assetmanagement.soap.SoapAsyncTask;
import com.bdwater.assetmanagement.soap.SoapClient;
import com.bdwater.assetmanagement.soap.SoapTaskResult;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.context.IconicsLayoutInflater;
import com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;


/////////////////////////////
//
// Show device list by user's permission
//
/////////////////////////////
public class DeviceListFragment extends Fragment {
    CApplication app;

    SearchView searchView;
    @BindView(R.id.swipeMenuRecyclerView)
    SwipeMenuRecyclerView swipeMenuRecyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    DefinedLoadMoreView loadMoreView;

    DeviceListRecyclerViewAdapter adapter;
    SoapAsyncTask task;
    boolean isLoading = false;
    static final int PAGE_SIZE = 8;
    List<Device> items = new ArrayList<>();

    public DeviceListFragment() {
    }


    // TODO: Rename and change types and number of parameters
    public static DeviceListFragment newInstance() {
        DeviceListFragment fragment = new DeviceListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // gets custom application
        app = (CApplication)getActivity().getApplication();

        // inflates the layout for this fragment
        swipeRefreshLayout = (SwipeRefreshLayout)inflater.inflate(R.layout.fragment_device_list, container, false);
        ButterKnife.bind(this, swipeRefreshLayout);

        // new instance of load more view for recycler view
        loadMoreView = new DefinedLoadMoreView(getContext());
        // set listener of on refresh
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                items.clear();                      // clear all items
                adapter.notifyDataSetChanged();     // notify data set changed
                getData();                          // get new data set of devices
            }
        });
        // set layout manager for recycler view
        swipeMenuRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new DeviceListRecyclerViewAdapter(items);
        // set custom item decoration for recycler view
        swipeMenuRecyclerView.addItemDecoration(new PaddingDecoration(10, 10, 10, 10));
        // set custom load more view
        swipeMenuRecyclerView.addFooterView(loadMoreView);
        loadMoreView.setMargin(0, 10, 0, 10);
        swipeMenuRecyclerView.setLoadMoreView(loadMoreView);

        // sets listener of load more
        swipeMenuRecyclerView.setLoadMoreListener(new SwipeMenuRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                getData();
            }
        });
        swipeMenuRecyclerView.setSwipeItemClickListener(new SwipeItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                // start device activity
                Device device = adapter.getItem(position);
                Intent intent = new Intent(getActivity(), DeviceActivity.class);
                intent.putExtra(DeviceActivity.DEVICE_ID_STRING, device.DeviceID);
                intent.putExtra(DeviceActivity.NAME, device.Name);
                intent.putExtra(DeviceActivity.INSTALL_DATE, device.InstallDate);
                startActivity(intent);
            }
        });
        swipeMenuRecyclerView.setAdapter(adapter);

        // first load
        swipeRefreshLayout.setRefreshing(true);
        getData();
        return swipeRefreshLayout;
    }
    void getData() {
        isLoading = true;
        task = new SoapAsyncTask(SoapClient.GET_DEVICE_LIST_METHOD, taskListener);
        task.execute(
                app.getUser().UserID,               // UserID
                Integer.toString(items.size()),     // Offset
                Integer.toString(PAGE_SIZE));       // PageSize
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.device_list_menu, menu);
        menu.getItem(0).setIcon(
                IconicsHelper.getIcon(getActivity(), CommunityMaterial.Icon.cmd_magnify, 16, 0, Color.WHITE));
        menu.getItem(1).setIcon(
                IconicsHelper.getIcon(getActivity(), CommunityMaterial.Icon.cmd_qrcode_scan, 16, 0, Color.WHITE));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.searchView:
                intent = new Intent(getActivity(), DeviceSearchActivity.class);
                startActivity(intent);
                break;
            case R.id.scan:
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setClassName(getActivity(), FullScannerActivity.class.getName());
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    SoapAsyncTask.OnSoapTaskListener taskListener = new SoapAsyncTask.OnSoapTaskListener() {
        @Override
        public void onTaskExecuted(SoapTaskResult result) {
            try {
                final String message;
                if(result.Result == SoapTaskResult.TaskResultType.Success) {
                    String data = result.JSONResult.getString("data");
                    List<Device> currentList = Device.parseSimpleDeviceArray(new JSONArray(data));

                    int start = items.size();
                    items.addAll(currentList);
                    if(currentList.size() > 0)
                        if(start > 0)   // not first load, refresh inserted range
                            adapter.notifyItemRangeInserted(start, currentList.size());
                        else            // first load, refresh all
                            adapter.notifyDataSetChanged();

                    swipeMenuRecyclerView.loadMoreFinish(items.size() == 0, currentList.size() == PAGE_SIZE);
                }
                else if(result.Result == SoapTaskResult.TaskResultType.Failed) {
                    message = result.Message;
                    swipeMenuRecyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            swipeMenuRecyclerView.loadMoreError(1, message);
                        }
                    });

                }
                else if(result.Result == SoapTaskResult.TaskResultType.Exception) {
                    message = result.Message;
                    swipeMenuRecyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            swipeMenuRecyclerView.loadMoreError(-1, message);
                        }
                    });
                }

            } catch (final JSONException e) {
                e.printStackTrace();
                swipeMenuRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeMenuRecyclerView.loadMoreError(-1, e.getMessage());
                    }
                });
            }
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    isLoading = false;
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
        }
    };
}
