package com.healthy.library.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

import androidx.annotation.IntegerRes;
import androidx.annotation.StringRes;
@Deprecated
/**
 * Author: Li
 * Date: 2018/8/31 0031
 * Description:
 */
public class ResUtils {

    public static String getStrById(Context context, @StringRes int id) {
        return context.getResources().getString(id);
    }

    public static int getIntById(Context context, @IntegerRes int id) {
        return context.getResources().getInteger(id);
    }

    public static int getStatusBarHeight(Context context) {
        try {
            Resources resources = context.getApplicationContext().getResources();
            int height;
            int id = resources.getIdentifier("status_bar_height", "dimen", "android");
            if (id > 0) {
                height = resources.getDimensionPixelSize(id);
            } else {
                height = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 22, resources.getDisplayMetrics());
            }
            return height;
        } catch (Exception e) {
            return 66;
        }
    }
}
