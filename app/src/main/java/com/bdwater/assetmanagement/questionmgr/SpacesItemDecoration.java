package com.bdwater.assetmanagement.questionmgr;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

/**
 * Created by hegang on 17/11/21.
 */

public class SpacesItemDecoration extends SwipeMenuRecyclerView.ItemDecoration {
    private int space;
    private int span;

    public SpacesItemDecoration(int space, int span) {
        this.space = space;
        this.span = span;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, SwipeMenuRecyclerView.State state) {
        outRect.left = space;
        outRect.top = space;

//        if(parent.getChildAdapterPosition(view) < span)
//            outRect.top = 0;
//        // Add top margin only for the first item to avoid double space between items
//        if (parent.getChildAdapterPosition(view) % span == 0)
//            outRect.left = 0;
    }
}