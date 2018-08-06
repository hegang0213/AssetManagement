package com.bdwater.assetmanagement.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.bdwater.assetmanagement.R;

/**
 * Created by hegang on 17/11/16.
 */

public class CustomProgressDialog extends ProgressDialog {
    public CustomProgressDialog(Context context) {
        super(context);
    }

    View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setCancelable(false);
        setCanceledOnTouchOutside(false);

        view = getLayoutInflater().inflate(R.layout.custom_progress, null);
        setContentView(view);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }
    public void setPercent(int progress) {
        String s = view.getResources().getString(R.string.loading);
        s += progress + "%";
        ((TextView)view.findViewById(R.id.textView)).setText(s);
    }
}
