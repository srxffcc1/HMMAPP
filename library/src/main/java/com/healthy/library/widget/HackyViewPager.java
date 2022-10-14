package com.healthy.library.widget;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Author: Li
 * Date: 2018/9/1 0001
 * Description:
 */
public class HackyViewPager extends ViewPager {
    public HackyViewPager(@NonNull Context context) {
        super(context);
    }

    public HackyViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            //uncomment if you really want to see these errors
            e.printStackTrace();
            return false;
        }
    }
}
