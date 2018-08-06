package com.bdwater.assetmanagement.common;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;

/**
 * Created by hegang on 17/11/2.
 */

public class HeaderViewHolder extends ViewHolder {
    public HeaderViewHolder(View itemView) {
        super(itemView);
    }

    public enum HeaderMode {
        NoData,
        RefreshHint,
    }
}
