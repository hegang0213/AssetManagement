package com.bdwater.assetmanagement;

import android.app.Activity;
import android.support.annotation.ColorInt;
import android.support.annotation.MenuRes;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuItem;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hegang on 17/11/23.
 */

public class CAHBottomNavigationAdapter extends AHBottomNavigationAdapter {
    private Menu mMenu;
    private List<AHBottomNavigationItem> navigationItems;

    /**
     * Constructor
     *
     * @param activity
     * @param menuRes
     */
    public CAHBottomNavigationAdapter(Activity activity, @MenuRes int menuRes) {
        super(activity, menuRes);
        PopupMenu popupMenu = new PopupMenu(activity, null);
        mMenu = popupMenu.getMenu();
        activity.getMenuInflater().inflate(menuRes, mMenu);
    }

    public void setupWithBottomNavigation(AHBottomNavigation ahBottomNavigation, @ColorInt int[] colors) {
        if (navigationItems == null) {
            navigationItems = new ArrayList<>();
        } else {
            navigationItems.clear();
        }

        if (mMenu != null) {
            for (int i = 0; i < mMenu.size(); i++) {
                MenuItem item = mMenu.getItem(i);
                if(!item.isVisible()) continue;
                if (colors != null && colors.length >= mMenu.size() && colors[i] != 0) {
                    AHBottomNavigationItem navigationItem = new AHBottomNavigationItem(String.valueOf(item.getTitle()), item.getIcon(), colors[i]);
                    navigationItems.add(navigationItem);
                } else {
                    AHBottomNavigationItem navigationItem = new AHBottomNavigationItem(String.valueOf(item.getTitle()), item.getIcon());
                    navigationItems.add(navigationItem);
                }
            }
            ahBottomNavigation.removeAllItems();
            ahBottomNavigation.addItems(navigationItems);
        }
    }
    /**
     * Get Menu Item
     *
     * @param index
     * @return
     */
    public MenuItem getMenuItem(int index) {
        return mMenu.getItem(index);
    }

    /**
     * Get Navigation Item
     *
     * @param index
     * @return
     */
    public AHBottomNavigationItem getNavigationItem(int index) {
        return navigationItems.get(index);
    }

    /**
     * Get position by menu id
     *
     * @param menuId
     * @return
     */
    public Integer getPositionByMenuId(int menuId) {
        for (int i = 0; i < mMenu.size(); i++) {
            if (mMenu.getItem(i).getItemId() == menuId)
                return i;
        }
        return null;
    }
}
