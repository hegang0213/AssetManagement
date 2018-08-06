package com.bdwater.assetmanagement.common;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;

import com.bdwater.assetmanagement.R;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.IIcon;

/**
 * Created by hegang on 17/11/15.
 */

public class IconicsHelper {
    public static Drawable getSmallIcon(Context context, IIcon icon) {
        return getIcon(context, icon, (int)context.getResources().getDimension(R.dimen.icon_size));
    }
    public static Drawable getNormalIcon(Context context, IIcon icon) {
        return getIcon(context, icon, (int)context.getResources().getDimension(R.dimen.icon_size_normal), 2);
    }
    public static Drawable getBigIcon(Context context, IIcon icon) {
        return getIcon(context, icon, (int)context.getResources().getDimension(R.dimen.icon_size_big));
    }
    public static Drawable getIcon(Context context, IIcon icon, int sizeDp) {
        return getIcon(context, icon, sizeDp, 0, -9999);
    }
    public static Drawable getIcon(Context context, IIcon icon, int sizeDp, int paddingDp) {
        return getIcon(context, icon, sizeDp, paddingDp, -9999);
    }
    public static Drawable getIcon(Context context, IIcon icon, int sizeDp, int paddingDp, int color) {
        IconicsDrawable drawable = new IconicsDrawable(context, icon);
        if(sizeDp != -1)
            drawable = drawable.sizeDp(sizeDp);
        drawable = drawable.paddingDp(paddingDp);
        if(color != -9999)
            drawable = drawable.color(color);
        return drawable;
    }
}

