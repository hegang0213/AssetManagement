package com.bdwater.assetmanagement.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bdwater.assetmanagement.R;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.w3c.dom.Attr;

import java.text.AttributedCharacterIterator;

import butterknife.ButterKnife;

/**
 * Created by hegang on 17/11/27.
 */

public class DefinedLoadMoreView extends NetworkCardView implements SwipeMenuRecyclerView.LoadMoreView, View.OnClickListener {
    public DefinedLoadMoreView(Context context) {
        super(context, null);
        this.setVisibility(View.VISIBLE);
        this.setOnClickListener(this);
    }
    public DefinedLoadMoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setMinimumHeight(50);
    }

    @Override
    protected void initUI(Context context, AttributeSet attrs, int defStyleAttr) {
        LayoutInflater inflater = LayoutInflater.from(this.getContext());
        setLayoutParams(new ViewGroup.LayoutParams(-1, -2));

        extView = (CardView)inflater.inflate(layout_res_id, null);
        extView.setRadius(0);
        LinearLayout.LayoutParams params = getCustomLayoutParams();
//        params.setMargins(15, 5, 15, 5);

        ButterKnife.bind(this, extView);
        this.addView(extView, params);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.NetworkCardView);
        this.viewType = ta.getInt(R.styleable.NetworkCardView_viewType, CardViewType.Loading);
        ta.recycle();
        this.render();
    }

    private SwipeMenuRecyclerView.LoadMoreListener mLoadMoreListener;

    /**
     * 马上开始回调加载更多了，这里应该显示进度条。
     */
    @Override
    public void onLoading() {
        // 展示加载更多的动画和提示信息。
        super.loading();
    }

    /**
     * 加载更多完成了。
     *
     * @param dataEmpty 是否请求到空数据。
     * @param hasMore   是否还有更多数据等待请求。
     */
    @Override
    public void onLoadFinish(boolean dataEmpty, boolean hasMore) {
        // 根据参数，显示没有数据的提示、没有更多数据的提示。
        // 如果都不存在，则都不用显示。
        if(dataEmpty) {
            super.noData();
            return;
        }

        if(!hasMore) {
            super.message("", "没有更多的数据了");
            return;
        }

        super.finish();
    }

    /**
     * 加载出错啦，下面的错误码和错误信息二选一。
     *
     * @param errorCode    错误码。
     * @param errorMessage 错误信息。
     */
    @Override
    public void onLoadError(int errorCode, String errorMessage) {
        super.exception("", errorMessage);
    }

    /**
     * 调用了setAutoLoadMore(false)后，在需要加载更多的时候，此方法被调用，并传入listener。
     */
    @Override
    public void onWaitToLoadMore(SwipeMenuRecyclerView.LoadMoreListener loadMoreListener) {
        this.mLoadMoreListener = loadMoreListener;
    }

    /**
     * 非自动加载更多时mLoadMoreListener才不为空。
     */
    @Override
    public void onClick(View v) {
        if (mLoadMoreListener != null) mLoadMoreListener.onLoadMore();
    }

}
