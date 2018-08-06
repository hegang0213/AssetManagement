package com.bdwater.assetmanagement;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationViewPager;
import com.bdwater.assetmanagement.common.ExtAppCompatActivity;
import com.bdwater.assetmanagement.common.IconicsHelper;
import com.bdwater.assetmanagement.main.DeviceListFragment;
import com.bdwater.assetmanagement.main.MeFragment;
import com.bdwater.assetmanagement.main.QuestionDoneListFragment;
import com.bdwater.assetmanagement.main.QuestionDraftListFragment;
import com.bdwater.assetmanagement.main.QuestionListFragment;
import com.bdwater.assetmanagement.main.ViewPagerAdapter;
import com.bdwater.assetmanagement.model.Permission;
import com.bdwater.assetmanagement.model.PermissionLocal;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.context.IconicsContextWrapper;
import com.mikepenz.iconics.context.IconicsLayoutInflater;
import com.mikepenz.iconics.typeface.IIcon;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends ExtAppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    CoordinatorLayout coordinatorLayout;
    SearchView searchView;
    @BindView(R.id.bottomNavigation)
    AHBottomNavigation bottomNavigation;
    @BindView(R.id.viewPager)
    AHBottomNavigationViewPager viewPager;

    //    AHBottomNavigationAdapter navigationAdapter;
    ViewPagerAdapter adapter;

    CNavigation navigation = new CNavigation();

    boolean draftEditable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));
        super.onCreate(savedInstanceState);
        coordinatorLayout = (CoordinatorLayout) getLayoutInflater().inflate(R.layout.activity_main, null);
        setContentView(coordinatorLayout);
        ((CApplication) getApplication()).setMainCoordinatorLayout(coordinatorLayout);

        ButterKnife.bind(this);

        // read draft permission
        draftEditable = getPermissionManager().get(PermissionLocal.ModelList.Draft, PermissionLocal.ActionList.DraftEdit);
        draftEditable = true;
        initUI();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase));
    }

    void initUI() {
        initNavigation();

        toolbar.setTitle(navigation.getTitleAt(0));
        setSupportActionBar(toolbar);

        for (CNavigationItem item : navigation.Items) {
            AHBottomNavigationItem ni = new AHBottomNavigationItem(item.title,
                    IconicsHelper.getNormalIcon(this, item.icon), Color.BLACK);
            bottomNavigation.addItem(ni);
        }
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        bottomNavigation.setSelectedBackgroundVisible(true);
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                if (wasSelected) {
                    return true;
                }
                toolbar.setTitle(navigation.getTitleAt(position));
                viewPager.setCurrentItem(position, true);
                return true;
            }
        });


        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.setFragments(navigation.getFragments());
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(adapter);
    }

    void initNavigation() {
        navigation.addItem(CNavigation.ASSERT, getString(R.string.menu_asset),
                CommunityMaterial.Icon.cmd_nutrition, DeviceListFragment.newInstance());
        if (draftEditable)
            navigation.addItem(CNavigation.DRAFT, getString(R.string.menu_draft),
                    CommunityMaterial.Icon.cmd_comment_plus_outline, QuestionDraftListFragment.newInstance());
        navigation.addItem(CNavigation.PROCESSING, getString(R.string.menu_processing),
                CommunityMaterial.Icon.cmd_comment_alert_outline, QuestionListFragment.newInstance());
        navigation.addItem(CNavigation.DONE, getString(R.string.menu_done),
                CommunityMaterial.Icon.cmd_checkbox_marked_circle_outline, QuestionDoneListFragment.newInstance());
        navigation.addItem(CNavigation.ME, getString(R.string.menu_me),
                CommunityMaterial.Icon.cmd_account_circle, MeFragment.newInstance());
    }

    class CNavigation {
        public static final String ASSERT = "asset";
        public static final String DRAFT = "draft";
        public static final String DONE = "done";
        public static final String PROCESSING = "processing";
        public static final String ME = "me";

        public List<CNavigationItem> Items = new ArrayList<>();

        public CNavigationItem get(int index) {
            return Items.get(index);
        }

        public CNavigationItem getByName(String name) {
            for (CNavigationItem item : Items) {
                if (item.name.equals(name))
                    return item;
            }
            return null;
        }

        public void addItem(String name, String title, IIcon icon, Fragment fragment) {
            CNavigationItem item = new CNavigationItem();
            item.name = name;
            item.title = title;
            item.icon = icon;
            item.fragment = fragment;
            Items.add(item);
        }

        public List<Fragment> getFragments() {
            List<Fragment> fragments = new ArrayList<>();
            for (CNavigationItem item : Items) {
                fragments.add(item.fragment);
            }
            return fragments;
        }

        public String getTitleAt(int index) {
            return Items.get(index).title;
        }
    }

    class CNavigationItem {
        public String name;
        public String title;
        public IIcon icon;
        public int notfication;
        public Fragment fragment;
    }

}
