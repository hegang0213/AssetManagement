package com.bdwater.assetmanagement.common;

import android.graphics.pdf.PdfDocument;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by hegang on 17/11/2.
 */

public abstract class ListRecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
        boolean onItemLongClick(View view, int position);
    }

    boolean headerShowing = false;
    HeaderViewHolder.HeaderMode headerMode = HeaderViewHolder.HeaderMode.NoData;

    boolean footerShowing = false;
    protected PageList<T> list;

    public static final int TYPE_ITEM = 0;
    public static final int TYPE_HEADER = 0x10;
    public static final int TYPE_FOOTER = 0x20;

    protected ListRecyclerViewAdapter.OnRecyclerViewItemClickListener listener;
    public void setOnItemClickListener(ListRecyclerViewAdapter.OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    public ListRecyclerViewAdapter(PageList<T> list) {
        this.list = list;
    }

    public void setHeaderShowing(boolean headerShowing) {
        if(this.headerShowing == headerShowing) return;
        this.headerShowing = headerShowing;
        if(headerShowing)
            notifyItemInserted(0);
        else
            notifyItemRemoved(0);
    }
    public boolean getHeaderShowing() {
        return headerShowing;
    }
    public void setFooterShowing(boolean footerShowing) {
        if(this.footerShowing == footerShowing) return;
        this.footerShowing = footerShowing;
        if(footerShowing)
            notifyItemInserted(getItemCount());
        else
            notifyItemRemoved(getItemCount());
    }
    public boolean getFooterShowing() {
        return footerShowing;
    }
    public void setHeaderMode(HeaderViewHolder.HeaderMode mode) {
        this.headerMode = mode;
    }


    public HeaderViewHolder.HeaderMode getHeaderMode() {
        return headerMode;
    }

    public T getItem(int position) {
        int index = position;
        if(headerShowing) index++;
        return list.get(index);
    }

    @Override
    public int getItemViewType(int position) {
        if(isHeader(position)) return TYPE_HEADER;
        if(isFooter(position)) return TYPE_FOOTER;
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        int count = list.size();
        if(headerShowing) count++;
        if(footerShowing) count++;
        return count;
    }
    public int getRealItemCount() {
        int count = getItemCount();
        if(headerShowing) count--;
        if(footerShowing) count--;
        if(count < 0) count = 0;
        return count;
    }
    boolean isHeader(int position) {
        return (position == 0 && headerShowing);
    }
    boolean isFooter(int position) {
        return (position + 1 == getItemCount() && footerShowing);
    }
}
