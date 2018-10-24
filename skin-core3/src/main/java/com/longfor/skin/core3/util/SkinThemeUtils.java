package com.longfor.skin.core3.util;

import android.content.Context;
import android.content.res.TypedArray;

public class SkinThemeUtils {
    public static int[] getResID(Context context,int[] attrs){
        int[] resIDs = new int[attrs.length];
        TypedArray typedArray = context.obtainStyledAttributes(attrs);
        for (int i=0;i<typedArray.length();i++){
            resIDs[i] = typedArray.getResourceId(i,0);
        }
        typedArray.recycle();
        return resIDs;
    }
}
