package com.bdwater.assetmanagement.questionmgr;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bdwater.assetmanagement.R;
import com.bdwater.assetmanagement.model.Site;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.List;

/**
 * Created by hegang on 17/11/16.
 */

public class SiteListAdapter extends SwipeMenuRecyclerView.Adapter<SwipeMenuRecyclerView.ViewHolder> {
    int selectedPosition = -1;
    List<Site> mList;
    public  SiteListAdapter(List<Site> list) {
        super();
        this.mList = list;

    }

    @Override
    public SwipeMenuRecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.site_list_item, parent, false);
        SiteViewHolder viewHolder = new SiteViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SwipeMenuRecyclerView.ViewHolder holder, int position) {
        int indent = 20;
        Site entry = getItem(position);
        SiteViewHolder viewHolder = (SiteViewHolder)holder;
        viewHolder.position = position;
        viewHolder.nameTextView.setText(entry.Name);
        viewHolder.nameTextView.setPadding(
                indent * (entry.Level + 1),
                10,
                10,
                10
        );
        viewHolder.setSelected(position == selectedPosition);
    }
    public Site getSelectedItem() {
        if(selectedPosition == -1) return null;
        return getItem(selectedPosition);
    }
    public void setSelected(int position) {
        if(selectedPosition == position) return;
        if(selectedPosition != -1)
            notifyItemChanged(selectedPosition);

        selectedPosition = position;
        notifyItemChanged(selectedPosition);
    }

    public Site getItem(int position) {
        return mList.get(position);
    }
    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class SiteViewHolder extends SwipeMenuRecyclerView.ViewHolder {
        View view;
        TextView nameTextView;
        int position;
        public SiteViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            nameTextView = (TextView)itemView.findViewById(R.id.nameTextView);
        }
        public void setSelected(boolean isSelected) {
            if(isSelected)
                view.setBackgroundResource(R.color.colorDark);
            else
                view.setBackgroundResource(android.R.color.transparent);
        }
    }
}

