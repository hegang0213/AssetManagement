package com.bdwater.assetmanagement.common;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

/**
 * Created by hegang on 17/11/22.
 */

public class PaddingDecoration extends SwipeMenuRecyclerView.ItemDecoration {
    private final int topPadding;
    private final int bottomPadding;
    private final int leftPadding;
    private final int rightPadding;

    public PaddingDecoration(int leftPadding, int topPadding, int rightPadding, int bottomPadding) {
        this.leftPadding = leftPadding;
        this.topPadding = topPadding;
        this.rightPadding = rightPadding;
        this.bottomPadding = bottomPadding;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams)view.getLayoutParams();
        int position = lp.getViewLayoutPosition();
        if (position == 0) {
            outRect.set(leftPadding, topPadding, rightPadding , 0);
        }
        else if (position == parent.getAdapter().getItemCount() - 1) {
            outRect.set(leftPadding, 0, rightPadding, bottomPadding);
        }
        else {
            outRect.set(leftPadding, topPadding, rightPadding, 0);
        }
    }
}