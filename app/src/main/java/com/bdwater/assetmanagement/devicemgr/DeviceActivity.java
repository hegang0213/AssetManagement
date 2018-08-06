package com.bdwater.assetmanagement.devicemgr;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bdwater.assetmanagement.R;
import com.bdwater.assetmanagement.common.AppCompatSwipeBackActivity;
import com.bdwater.assetmanagement.model.ColumnDescription;
import com.bdwater.assetmanagement.model.Device;
import com.bdwater.assetmanagement.soap.SoapAsyncTask;
import com.bdwater.assetmanagement.soap.SoapClient;
import com.bdwater.assetmanagement.soap.SoapTaskResult;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.context.IconicsContextWrapper;
import com.mikepenz.iconics.context.IconicsLayoutInflater;
import com.mikepenz.iconics.typeface.IIcon;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.imid.swipebacklayout.lib.SwipeBackLayout;

public class DeviceActivity extends AppCompatSwipeBackActivity {
    public static final String DEVICE_ID_STRING = "DeviceIDString";
    public static final String NAME = "Name";
    public static final String INSTALL_DATE = "InstallDate";
    String deviceIDString;

    @BindView(R.id.appbar)
    public AppBarLayout appbar;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.tabLayout)
    public TabLayout tabLayout;
    @BindView(R.id.viewPager)
    public ViewPager viewPager;

    DeviceViewPagerAdapter adapter;
    SoapAsyncTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        ButterKnife.bind(this);

//        getSwipeBackLayout().setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
//        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if(intent.hasExtra(NAME))
            toolbar.setTitle(intent.getStringExtra(NAME));
        else
            toolbar.setTitle(getString(R.string.loading));
//        if(intent.hasExtra(INSTALL_DATE))
//            installDateTextView.setText("安装日期：" + intent.getStringExtra(INSTALL_DATE));

        deviceIDString = getIntent().getStringExtra(DEVICE_ID_STRING);
        adapter = new DeviceViewPagerAdapter(getSupportFragmentManager(), deviceIDString);
        adapter.setActivity(this);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        setTabCustom(tabLayout.getTabAt(0), CommunityMaterial.Icon.cmd_nutrition);
        setTabCustom(tabLayout.getTabAt(1), CommunityMaterial.Icon.cmd_screwdriver);
    }
    public void setTitle(CharSequence cs) {
        toolbar.setTitle(cs);
    }

    private void setTabCustom(TabLayout.Tab tab, IIcon icon) {
        View view = getLayoutInflater().inflate(R.layout.tab_custom, null);
        TextView tv = (TextView)view.findViewById(R.id.tabContent);
        tv.setText(tab.getText());
        tv.setTextColor(Color.WHITE);
        Drawable drawable = new IconicsDrawable(this, icon).color(Color.WHITE)
                .sizeDp(24).paddingDp(4);
        tv.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        tab.setCustomView(tv);
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
}
