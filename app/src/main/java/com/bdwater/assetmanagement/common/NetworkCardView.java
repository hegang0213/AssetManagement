package com.bdwater.assetmanagement.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bdwater.assetmanagement.R;
import com.bdwater.assetmanagement.devicemgr.DeviceCard;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.os.Build.VERSION.SDK;

/**
 * Created by hegang on 17/11/13.
 */

public class NetworkCardView extends LinearLayout {
    public static final int VISIBLE = View.VISIBLE;
    public static final int GONE = View.GONE;

    static final int layout_res_id = R.layout.network_cardview;
    CardView extView;
    int viewType = CardViewType.Loading;

    @BindView(R.id.titleTextView)
    public TextView titleTextView;
    @BindView(R.id.bodyTextView)
    public TextView bodyTextView;
    @BindView(R.id.progressBar)
    public ProgressBar progressBar;
    @BindView(R.id.button)
    public Button button;

    @IntDef({VISIBLE, GONE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Visibility {}

    @IntDef({CardViewType.Loading, CardViewType.Exception, CardViewType.EmptyData, CardViewType.Message})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CardViewTypeInt {}

    public NetworkCardView(Context context) {
        super(context);
        this.initUI(null, null, -1);

    }

    public NetworkCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initUI(context, attrs, -1);

    }

    public NetworkCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initUI(context, attrs, defStyleAttr);
    }
    protected void initUI(Context context, AttributeSet attrs, int defStyleAttr) {
        LayoutInflater inflater = LayoutInflater.from(this.getContext());
        extView = (CardView)inflater.inflate(layout_res_id, null);
        ButterKnife.bind(this, extView);
        if(context == null) return;
        this.addView(extView, getCustomLayoutParams());

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.NetworkCardView);
        this.viewType = ta.getInt(R.styleable.NetworkCardView_viewType, CardViewType.Loading);
        ta.recycle();
        this.render();
    }

    public void setTitleVisibility(@NetworkCardView.Visibility int visibility) {
        this.titleTextView.setVisibility(visibility);
    }
    public void setTitle(CharSequence cs) {
        if(cs.equals(""))
            this.setTitleVisibility(GONE);
        else {
            this.titleTextView.setText(cs);
            this.setTitleVisibility(VISIBLE);
        }
    }
    public void setBody(CharSequence cs) {
        this.bodyTextView.setText(cs);
    }
    public void setRetryButtonText(CharSequence cs) {
        this.button.setText(cs);
    }
    public Button getRetryButton() {
        return this.button;
    }
    public void setRetryButtonVisibility(@NetworkCardView.Visibility int visibility) {
        this.button.setVisibility(visibility);
    }

    public void setMargin(int left, int top, int right, int bottom) {
        int cardElevation = getResources().getDimensionPixelOffset(R.dimen.cardElevation);
        ((LinearLayout.LayoutParams)extView.getLayoutParams()).setMargins(
                left + cardElevation,
                top + cardElevation,
                right + cardElevation,
                bottom + cardElevation);

    }
    public void setCardElevation(float value) {
        extView.setCardElevation(value);
    }

    public LinearLayout.LayoutParams getCustomLayoutParams() {
        LayoutParams params = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        return params;
    }
    public void setViewType(@CardViewTypeInt int viewType) {
        if(this.viewType != viewType) {
            this.viewType = viewType;
            this.render();
        }
    }
    public @CardViewTypeInt int getViewType() {
        return this.viewType;
    }
    public void loading() {
        this.setVisibility(View.VISIBLE);
        this.setViewType(CardViewType.Loading);
    }
    public void exception(String title, String body) {
        this.setVisibility(View.VISIBLE);
        this.setViewType(CardViewType.Exception);
        this.setTitle(title);
        this.setBody(body);
    }
    public void noData() {
        this.setVisibility(View.VISIBLE);
        this.setViewType(CardViewType.EmptyData);
    }
    public void message(String title, String body) {
        this.setVisibility(View.VISIBLE);
        this.setViewType(CardViewType.Message);
        this.setTitle(title);
        this.setBody(body);
    }
    public void finish() {
        this.setVisibility(View.GONE);
    }
    void render() {
        if(this.viewType == CardViewType.Loading) {
            titleTextView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            button.setVisibility(View.GONE);
            bodyTextView.setText(getContext().getString(R.string.loading));
        }
        else if(this.viewType == CardViewType.Exception) {
            titleTextView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            button.setVisibility(View.VISIBLE);
            bodyTextView.setVisibility(View.VISIBLE);
        }
        else if(this.viewType == CardViewType.Message) {
            titleTextView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            button.setVisibility(View.GONE);
            bodyTextView.setVisibility(View.VISIBLE);
        }
        else {
            titleTextView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            button.setVisibility(View.GONE);
            bodyTextView.setVisibility(View.VISIBLE);
            bodyTextView.setText(R.string.no_data);
        }
    }

    public class CardViewType {
        public static final int Message = 3;
        public static final int EmptyData = 2;
        public static final int Exception = 1;
        public static final int Loading = 0;
    }
}
