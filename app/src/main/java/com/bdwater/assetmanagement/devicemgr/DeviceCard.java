package com.bdwater.assetmanagement.devicemgr;

import android.support.annotation.IntDef;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bdwater.assetmanagement.R;
import com.bdwater.assetmanagement.model.Device;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by hegang on 17/11/13.
 */

public class DeviceCard {
    public static final int VISIBLE = View.VISIBLE;
    public static final int GONE = View.GONE;

    static final int layout_res_id = R.layout.device_content_card;
    LayoutInflater inflater;
    View rootView;

    TextView titleTextView;
    LinearLayout contentLayout;
    public DeviceCard(android.view.LayoutInflater inflater) {
        this.inflater = inflater;
        this.rootView = inflater.inflate(layout_res_id, null);
        this.onBindView();
    }
    void onBindView() {
        titleTextView = (TextView)this.rootView.findViewById(R.id.titleTextView);
        contentLayout = (LinearLayout)this.rootView.findViewById(R.id.contentLayout);
    }
    @IntDef({VISIBLE, GONE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Visibility {}
    public void setTitleVisibility(@Visibility int visibility) {
        this.titleTextView.setVisibility(visibility);
    }
    public void setTitle(CharSequence cs) {
        this.titleTextView.setText(cs);
    }
    public void addView(View view) {
        this.contentLayout.addView(view);
    }
    public View getView() {
        return rootView;
    }
    public LinearLayout getContentLayout() {
        return contentLayout;
    }
    public LinearLayout.LayoutParams getLayoutParams() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        return params;
    }


}
