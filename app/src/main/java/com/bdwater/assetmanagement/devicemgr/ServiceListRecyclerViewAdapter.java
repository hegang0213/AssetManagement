package com.bdwater.assetmanagement.devicemgr;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bdwater.assetmanagement.R;
import com.bdwater.assetmanagement.model.Service;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by hegang on 17/11/14.
 */

public class ServiceListRecyclerViewAdapter extends SwipeMenuRecyclerView.Adapter<SwipeMenuRecyclerView.ViewHolder> {
    List<Service> mList;
    public ServiceListRecyclerViewAdapter(List<Service> services) {
        super();
        this.mList = services;
    }

    @Override
    public SwipeMenuRecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.service_card_item, null);
        ServiceViewHolder viewHolder = new ServiceViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SwipeMenuRecyclerView.ViewHolder holder, int position) {
        ServiceViewHolder viewHolder = (ServiceViewHolder)holder;
        Service service = getItem(position);
        viewHolder.titleTextView.setText(service.Title);
        viewHolder.serviceDateTextView.setText(service.ServiceDate);
        viewHolder.contentTextView.setText(service.Content);
        viewHolder.bigRepairIndexTextView.setText(Integer.toString(service.BigRepairIndex));
        viewHolder.typeNameTextView.setText(service.TypeName);
        viewHolder.bigRepairIndexTextView.setVisibility(service.Type == 0 ? View.INVISIBLE: View.VISIBLE);

    }
    public Service getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ServiceViewHolder extends SwipeMenuRecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView serviceDateTextView;
        public TextView contentTextView;
        public TextView bigRepairIndexTextView;
        public TextView typeNameTextView;

        public ServiceViewHolder(View itemView) {
            super(itemView);

            titleTextView = (TextView)itemView.findViewById(R.id.titleTextView);
            serviceDateTextView = (TextView)itemView.findViewById(R.id.serviceDateTextView);
            contentTextView = (TextView)itemView.findViewById(R.id.contentTextView);
            bigRepairIndexTextView = (TextView)itemView.findViewById(R.id.bigRepairIndexTextView);
            typeNameTextView = (TextView)itemView.findViewById(R.id.typeNameTextView);
        }
    }
}
