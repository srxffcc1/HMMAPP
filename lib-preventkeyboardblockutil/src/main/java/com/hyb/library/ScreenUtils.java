package com.hyb.library;

import android.content.Context;
import android.graphics.Point;
import android.view.WindowManager;

public class ScreenUtils {
    /**
     * Return the application's height of screen, in pixel.
     *
     * @return the application's height of screen, in pixel
     */
    public static int getAppScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) {
            return -1;
        }
        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
        return point.y;
    }
}
