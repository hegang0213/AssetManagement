package com.bdwater.assetmanagement.questionmgr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bdwater.assetmanagement.R;
import com.bdwater.assetmanagement.common.IconicsHelper;
import com.bdwater.assetmanagement.common.NetworkUtils;
import com.bdwater.assetmanagement.model.Image;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hegang on 17/11/18.
 */

public class ImageListAdapter extends SwipeMenuRecyclerView.Adapter<SwipeMenuRecyclerView.ViewHolder> {
    public static final int ADD = 1;
    public static final int NORMAL = 0;

    boolean isReadonly = false;

    List<Image> mList = new ArrayList<>();
    public ImageListAdapter(List<Image> list) {
        this.mList = list;
    }

    public void setReadonly(boolean readonly) {
        isReadonly = readonly;
    }

    Context context;
    @Override
    public SwipeMenuRecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view;
        if(viewType == ADD)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trouble_note_detail_add_image, parent, false);
        else
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trouble_note_detail_image, parent, false);
        ImageViewHolder viewHolder = new ImageViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SwipeMenuRecyclerView.ViewHolder holder, final int position) {
        final ImageViewHolder viewHolder = (ImageViewHolder)holder;

        if(getItemViewType(position) == NORMAL) {
            final Image image = getItem(position);
            if(isReadonly)
                viewHolder.deleteView.setVisibility(View.GONE);
            else
                viewHolder.deleteView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mList.remove(position);
                        notifyItemRemoved(position);
                        if(getAddPosition() == -1) {
                            insertAddView();
                            notifyItemInserted(getAddPosition());
                        }
                    }
                });
            viewHolder.view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    viewHolder.view.getViewTreeObserver().removeOnPreDrawListener(this);
                    int width = viewHolder.view.getWidth();

                    String imagePath = image.From == Image.FROM_LOCAL ? image.LocalPath :
                            NetworkUtils.IMAGE_URL + image.RemotePath;
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.centerCrop();
                    requestOptions.override(width, width);
                    requestOptions.priority(Priority.HIGH);
                    requestOptions.placeholder(IconicsHelper.getBigIcon(context, CommunityMaterial.Icon.cmd_image));
                    Glide.with(context)
                            .setDefaultRequestOptions(requestOptions)
                            .load(imagePath)
                            .into(viewHolder.imageView);
                    return true;
                }
            });


        }
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).IsOnlyForAdd ? ADD: NORMAL;
    }

    public int getAddPosition() {
        for(int i = 0; i < mList.size(); i++) {
            if(getItemViewType(i) == ADD)
                return i;
        }
        return -1;
    }
    public void removeAddView() {
        if(getAddPosition() != -1)
            mList.remove(getAddPosition());
    }
    public void insertAddView() {
        if(getAddPosition() == -1)
            mList.add(Image.IconForAdd());
    }
    public Image getItem(int position) {
        return mList.get(position);
    }
    @Override
    public int getItemCount() {
        return mList.size();
    }
    public int getRealItemCount() {
        int count = getItemCount();
        for(Image image : mList) {
            if(image.IsOnlyForAdd)
                return count - 1;
        }
        return getItemCount();
    }

    static class ImageViewHolder extends  SwipeMenuRecyclerView.ViewHolder {
        ImageView imageView;
        FrameLayout view;
        View deleteView;
        public ImageViewHolder(View itemView) {
            super(itemView);

            view = (FrameLayout)itemView;
            imageView = (ImageView)itemView.findViewById(R.id.imageView);
            if(itemView.findViewById(R.id.trash) != null)
                deleteView = itemView.findViewById(R.id.trash);
        }

    }
}
