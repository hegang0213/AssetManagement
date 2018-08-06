package com.bdwater.assetmanagement.devicemgr;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;

import com.bdwater.assetmanagement.R;
import com.bdwater.assetmanagement.main.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hegang on 17/11/12.
 */

public class DeviceViewPagerAdapter extends ViewPagerAdapter {
    List<Fragment> mFragments = new ArrayList<>();
    Fragment mCurrentFragment;
    Activity mActivity;
    String[] mTitles;
    public DeviceViewPagerAdapter(FragmentManager fm, String deviceIDString) {
        super(fm);

        mFragments.add(DeviceFragment.newInstance(deviceIDString));
        mFragments.add(ServiceFragment.newInstance(deviceIDString));
    }
    public void setActivity(Activity activity) {
        mActivity = activity;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(mTitles == null)
            mTitles = mActivity.getResources().getStringArray(R.array.device_navigation);
        return mTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (getCurrentFragment() != object) {
            mCurrentFragment = ((Fragment) object);
        }
        super.setPrimaryItem(container, position, object);
    }

    /**
     * Get the current fragment
     */
    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }
}
