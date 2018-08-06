package com.bdwater.assetmanagement.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hegang on 17/10/17.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    List<Fragment> fragments = new ArrayList<>();
    Fragment currentFragment = null;
    DeviceListFragment deviceListFragment;
    QuestionListFragment questionListFragment;
    TaskListFragment taskListFragment;
    MeFragment meFragment;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);

//        fragments.clear();

//        deviceListFragment = DeviceListFragment.newInstance();
//        fragments.add(deviceListFragment);
//
//
//        questionListFragment = QuestionListFragment.newInstance();
//        fragments.add(questionListFragment);
////        taskListFragment = TaskListFragment.newInstance();
////        fragments.add(taskListFragment);
//
//        meFragment = MeFragment.newInstance();
//        fragments.add(meFragment);
    }

    public void setFragments(List<Fragment> fragments) {
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (getCurrentFragment() != object) {
            currentFragment = ((Fragment) object);
        }
        super.setPrimaryItem(container, position, object);
    }

    /**
     * Get the current fragment
     */
    public Fragment getCurrentFragment() {
        return currentFragment;
    }
}
