package com.bdwater.assetmanagement.common;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.SearchView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.bdwater.assetmanagement.R;

/**
 * Created by hegang on 17/11/1.
 */

public class SearchViewUtils {
    public static SearchView CreateSearchView(Context context, String queryHint)
    {
        SearchView view = new SearchView(context);
        SpannableString spanText = new SpannableString(queryHint);
        float px = (int)context.getResources().getDimension(R.dimen.toolbarTextSize);
        float scale =  context.getResources().getDisplayMetrics().density;
        int dp = (int) (px / scale + 0.5f);
        // 设置字体大小
        spanText.setSpan(new AbsoluteSizeSpan(dp, true), 0, spanText.length(),
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        spanText.setSpan(new ForegroundColorSpan(Color.WHITE), 0,
                spanText.length(),
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        view.setIconifiedByDefault(true);
        view.setSubmitButtonEnabled(true);//设置最右侧的提交按钮
        view.setQueryHint(spanText);
        return view;
    }
    public static void setSearchViewQueryHint(SearchView searchView, String queryHint) {
        Context context = searchView.getContext();
        SpannableString spanText = new SpannableString(queryHint);
        float px = (int)context.getResources().getDimension(R.dimen.toolbarTextSize);
        float scale =  context.getResources().getDisplayMetrics().density;
        int dp = (int) (px / scale + 0.5f);
        // 设置字体大小
        spanText.setSpan(new AbsoluteSizeSpan(dp, true), 0, spanText.length(),
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        spanText.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorQueryHint)), 0,
                spanText.length(),
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        searchView.setQueryHint(spanText);
    }
}
