package com.bdwater.assetmanagement.common;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by hegang on 17/11/11.
 */

public class MyScrollingViewBehavior extends AppBarLayout.ScrollingViewBehavior {
    private AppBarLayout appBarLayout;

    public MyScrollingViewBehavior() {
        super();
    }

    public MyScrollingViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {

        if (appBarLayout == null) {
            appBarLayout = (AppBarLayout) dependency;
        }

        final boolean result = super.onDependentViewChanged(parent, child, dependency);
        final int top = dependency.getHeight();
        child.setPadding(
                child.getPaddingLeft(),
                top,
                child.getPaddingRight(),
                child.getPaddingBottom()
        );
//        final int bottomPadding = calculateBottomPadding(appBarLayout);
//        final boolean paddingChanged = bottomPadding != child.getPaddingBottom();
//        if (paddingChanged) {
//            child.setPadding(
//                    child.getPaddingLeft(),
//                    child.getPaddingTop(),
//                    child.getPaddingRight(),
//                    bottomPadding);
//            child.requestLayout();
//        }
        return result;
    }


    // Calculate the padding needed to keep the bottom of the view pager's content at the same location on the screen.
    private int calculateBottomPadding(AppBarLayout dependency) {
        final int totalScrollRange = dependency.getTotalScrollRange();
        return totalScrollRange + dependency.getTop();
    }
}
