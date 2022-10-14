package com.healthy.library.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * Author: Li
 * Date: 2017/4/1
 * Description: 转换
 */

public class TransformUtil {

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static float px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return  pxValue / scale + 0.5f;
    }

    public static float dp2px(Context context, float dp) {
        return Math.max(1,TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, context.getApplicationContext().getResources().getDisplayMetrics()));
    }

    public static float sp2px(Context context, float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                sp, context.getApplicationContext().getResources().getDisplayMetrics());
    }

    public static int newDp2px(Context context, float dpValue) {
        if (null == context) {
            return 0;
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
