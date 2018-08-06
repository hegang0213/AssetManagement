package com.bdwater.assetmanagement.devicemgr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bdwater.assetmanagement.R;
import com.bdwater.assetmanagement.model.Device;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.w3c.dom.Text;

import java.util.List;

import static com.bdwater.assetmanagement.common.ListRecyclerViewAdapter.TYPE_HEADER;

/**
 * Created by hegang on 17/11/1.
 */

public class DeviceListRecyclerViewAdapter extends SwipeMenuRecyclerView.Adapter<SwipeMenuRecyclerView.ViewHolder> {
    List<Device> mList;
    Context mContext;
    OnServiceClickListener onServiceClickListener;
    public DeviceListRecyclerViewAdapter(List<Device> list) {
        super();
        mList = list;
    }

    public void SetOnServiceClickListener(OnServiceClickListener onServiceClickListener) {
        this.onServiceClickListener = onServiceClickListener;
    }

    @Override
    public SwipeMenuRecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        mContext = parent.getContext();
        SwipeMenuRecyclerView.ViewHolder viewHolder;
        viewHolder = createItemViewHolder(inflater);
        return viewHolder;
    }
    SwipeMenuRecyclerView.ViewHolder createItemViewHolder(LayoutInflater inflater) {
        final View view = inflater.inflate(R.layout.simple_device_card_item, null);
        final SwipeMenuRecyclerView.ViewHolder viewHolder = new SimpleDeviceViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SwipeMenuRecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof SimpleDeviceViewHolder) {
            SimpleDeviceViewHolder viewHolder = (SimpleDeviceViewHolder) holder;
            Device entry = getItem(position);
            viewHolder.nameTextView.setText(entry.Name);
            viewHolder.typeTextView.setText(entry.InstallDate);
            viewHolder.bigRepairCountTextView.setText("-");
            viewHolder.remainDaysTextView.setText("-");
            viewHolder.addressTextView.setText(entry.Address);
//            viewHolder.bigRepairCountTextView.setText(
//                    (entry.BigRepairCount.equals("") || entry.BigRepairCount.equals("null") ? "-": entry.BigRepairCount));
//            viewHolder.remainDaysTextView.setText(
//                    (entry.NextBigRepairDaysRemain == "" || entry.NextBigRepairDaysRemain == null ? "-": entry.NextBigRepairDaysRemain));
        }
    }
    @Override
    public int getItemCount() {
        int count = mList.size();
        return count;
    }
    public Device getItem(int position) {
        return mList.get(position);
    }

    interface OnServiceClickListener {
        void onServiceClick(View view, int position);
    }
    static class SimpleDeviceViewHolder extends SwipeMenuRecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView nameTextView;
        public TextView typeTextView;
        public TextView bigRepairCountTextView;
        public TextView remainDaysTextView;
        public ImageView exclamationImageView;
        public TextView addressTextView;

        public SimpleDeviceViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.imageView);
            nameTextView = (TextView)itemView.findViewById(R.id.nameTextView);
            typeTextView = (TextView)itemView.findViewById(R.id.typeTextView);
            bigRepairCountTextView = (TextView)itemView.findViewById(R.id.bigRepairCountTextView);
            remainDaysTextView = (TextView)itemView.findViewById(R.id.remainDaysTextView);
            exclamationImageView = (ImageView)itemView.findViewById(R.id.exclamationImageView);
            addressTextView = (TextView)itemView.findViewById(R.id.addressTextView);
        }

    }
}
