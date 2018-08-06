package com.bdwater.assetmanagement.main;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bdwater.assetmanagement.R;
import com.bdwater.assetmanagement.common.IconicsHelper;
import com.bdwater.assetmanagement.model.TroubleNote;
import com.bdwater.assetmanagement.model.TroubleNoteDetail;
import com.bdwater.assetmanagement.model.TroubleNoteDetailStatus;
import com.bdwater.assetmanagement.questionmgr.QuestionDetailActivity;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by hegang on 17/11/14.
 */

public class QuestionListRecyclerViewAdapter extends SwipeMenuRecyclerView.Adapter<SwipeMenuRecyclerView.ViewHolder> {
    public interface MenuItemClickListener {
        void onEditClick(View view, int position);
        void onPublishClick(View view, int position);
        void onDeleteClick(View view, int position);
        void onAddChildClick(View view, int position);
        void onChildClick(View view, int position, int childIndex);
        void onChildEditClick(View view, int position, int childIndex);
    }

    List<TroubleNote> mList;
    LayoutInflater inflater;
    ViewGroup parent;
    MenuItemClickListener listener;

    boolean allowEdit;
    boolean allowChildEdit;

    public void setAllowEdit(boolean allowEdit) {
        this.allowEdit = allowEdit;
    }

    public void setAllowChildEdit(boolean allowChildEdit) {
        this.allowChildEdit = allowChildEdit;
    }

    public QuestionListRecyclerViewAdapter(List<TroubleNote> list) {
        this.mList = list;
    }

    public void setMenuItemClickListener(MenuItemClickListener listener) {
        this.listener = listener;
    }
    @Override
    public SwipeMenuRecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = parent;
        inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.question_cardview_item, null);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SwipeMenuRecyclerView.ViewHolder holder, final int position) {
        TroubleNote entry = getItem(position);
        final QuestionViewHolder viewHolder = (QuestionViewHolder)holder;
        viewHolder.nameTextView.setText(entry.Name);
        viewHolder.taskCountTextView.setText(parent.getContext().getString(R.string.task)
                + " " + entry.getFinishedCount()
                + "/" + entry.Children.length);
        viewHolder.statusNameTextView.setText(entry.StatusName);
        viewHolder.createDateTextView.setText(entry.CreateDate);
        viewHolder.detailLayout.removeAllViews();
        viewHolder.swipeRevealLayout.setLockDrag(!allowEdit);
        if(allowEdit) {

            viewHolder.editLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) listener.onEditClick(view, position);
                }
            });
            viewHolder.publishLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) listener.onPublishClick(view, position);
                }
            });
            viewHolder.addChildLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) listener.onAddChildClick(view, position);
                }
            });
        }
        addChildrenView(position, entry, viewHolder);
    }
    @Override
    public int getItemCount() {
        return mList.size();
    }

    void addChildrenView(final int position, TroubleNote troubleNote, QuestionViewHolder viewHolder) {
        if(troubleNote.Children.length > 0) {
            int i = 0;
            for (TroubleNoteDetail child : troubleNote.Children) {
                View view = inflater.inflate(R.layout.question_cardview_sub_item, null);
                TextView content = (TextView)view.findViewById(R.id.contentTextView);
                content.setText(child.Content);
                ImageView checkImageView = (ImageView)view.findViewById(R.id.checkImageView);
                View editLayout = view.findViewById(R.id.editLayout);
                View childLayout = view.findViewById(R.id.childLayout);
                if(child.Status == TroubleNoteDetailStatus.DONE) {
                    content.setPaintFlags(content.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    content.setTextColor(view.getResources().getColor(R.color.colorDark));
                    checkImageView.setImageDrawable(IconicsHelper.getIcon(parent.getContext(),
                            CommunityMaterial.Icon.cmd_checkbox_marked_outline, 16, 0));
                }

                editLayout.setTag(i);
                editLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(listener != null) {
                            int childIndex = Integer.parseInt(view.getTag().toString());
                            listener.onChildEditClick(view, position, childIndex);
                        }
                    }
                });
                childLayout.setTag(i);
                childLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(listener != null) {
                            int childIndex = Integer.parseInt(view.getTag().toString());
                            listener.onChildClick(view, position, childIndex);
                        }
                    }
                });
                childLayout.measure(0, 0);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, childLayout.getMeasuredHeight());

                viewHolder.detailLayout.addView(view, params);
                i++;
            }
        }
    }

    public TroubleNote getItem(int position) {
        return mList.get(position);
    }
    public TroubleNoteDetail getChildItem(int position, int childIndex) {
        return getItem(position).Children[childIndex];
    }

    static class QuestionViewHolder extends SwipeMenuRecyclerView.ViewHolder implements View.OnClickListener {
        public SwipeRevealLayout swipeRevealLayout;
        public View editLayout;
        public View addChildLayout;
        public View publishLayout;
        public View bannerLayout;
        public TextView nameTextView;
        public TextView expandTextView;
        public TextView createDateTextView;
        public LinearLayout detailLayout;
        public TextView taskCountTextView;
        public TextView statusNameTextView;
        public QuestionViewHolder(View itemView) {
            super(itemView);

            swipeRevealLayout = itemView.findViewById(R.id.swipeRevealLayout);
            bannerLayout = itemView.findViewById(R.id.bannerLayout);
            bannerLayout.setOnClickListener(this);
            editLayout = itemView.findViewById(R.id.editLayout);
            addChildLayout = itemView.findViewById(R.id.addChildLayout);
            publishLayout = itemView.findViewById(R.id.publishLayout);
            nameTextView = (TextView)itemView.findViewById(R.id.nameTextView);
            expandTextView = (TextView)itemView.findViewById(R.id.expandTextView);
            createDateTextView = (TextView)itemView.findViewById(R.id.createDateTextView);
            taskCountTextView = (TextView)itemView.findViewById(R.id.taskCountTextView);
            statusNameTextView = (TextView) itemView.findViewById(R.id.statusNameTextView);
            detailLayout = (LinearLayout)itemView.findViewById(R.id.detailLayout);
            detailLayout.setVisibility(View.GONE);
        }

        @Override
        public void onClick(View view) {
            if(view.getTag() == null || Boolean.parseBoolean(view.getTag().toString()) == false ) {
                // expand detail
                expand();
                view.setTag(true);
            }
            else {
                collapse();
                view.setTag(false);
            }
        }
        public void expand() {
            // expand detail
            detailLayout.setVisibility(View.VISIBLE);
            expandTextView.setText(R.string.collapse);
        }
        public void collapse() {
            // collapse detail
            detailLayout.setVisibility(View.GONE);
            expandTextView.setText(R.string.expand);
        }
    }
}
