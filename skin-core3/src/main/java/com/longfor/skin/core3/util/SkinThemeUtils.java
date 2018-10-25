package com.longfor.skin.core3.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.RequiresApi;

public class SkinThemeUtils {

    private static int[] APPCOMPAT_COLOR_PRIMARY_DARK_ATTRS = {
            android.support.v7.appcompat.R.attr.colorPrimaryDark
    };
    private static int[] STATUSBAR_COLOR_ATTRS = {android.R.attr.statusBarColor, android.R.attr
            .navigationBarColor};


    public static int[] getResID(Context context, int[] attrs) {
        int[] resIDs = new int[attrs.length];
        TypedArray typedArray = context.obtainStyledAttributes(attrs);
        for (int i = 0; i < typedArray.length(); i++) {
            resIDs[i] = typedArray.getResourceId(i, 0);
        }
        typedArray.recycle();
        return resIDs;
    }


    public static void updateStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int[] resIDs = getResID(activity, STATUSBAR_COLOR_ATTRS);
            if (resIDs[0] == 0) {
                int statusBarColorID = getResID(activity, APPCOMPAT_COLOR_PRIMARY_DARK_ATTRS)[0];
                if (statusBarColorID != 0) {

                    activity.getWindow().setStatusBarColor(SkinResource.getInstance().getColor(statusBarColorID));

                }
            } else {
                activity.getWindow().setStatusBarColor(SkinResource.getInstance().getColor(resIDs[0]));
            }
            //修改底部虚拟按键的颜色
            if (resIDs[1] != 0) {
                activity.getWindow().setNavigationBarColor(SkinResource.getInstance().getColor(resIDs[1]));
            }
        }
    }
}
