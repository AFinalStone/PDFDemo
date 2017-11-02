package com.afinalstone.androidstudy.pdfdemo.util;

import android.content.Context;

public class DensityUtil {
	 
    /**
     * dip转换成px
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
 
    /**
     * px转换成dip
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
 
 
}
