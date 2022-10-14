package com.healthy.library.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.google.android.material.tabs.TabLayout;
import com.healthy.library.R;
import com.healthy.library.utils.TransformUtil;

import java.lang.reflect.Field;

/**
 * 支持自定义下标，自定义tab宽度
 * <p>
 * 自定义下标    --> {@link #setmSlideIcon}
 * 自定义tab宽度 --> 由{@link #COUNT_DEFAULT_VISIBLE_TAB}和{@link #}共同决定
 *
 * @author gaok
 * @date 2017/10/31 21:18.
 */
public class IndexToolsSlidingTabLayout extends TabLayout {
    /**
     * 每个tab的宽度
     */
    private int tabWidth;
    /**
     * 屏幕宽度
     */
    private int mScreenWidth;
    /**
     * 自定义指示器
     */
    private Bitmap mSlideIcon;
    /**
     * 滑动过程中指示器的水平偏移量
     */
    private int mTranslationX;
    /**
     * 指示器初始X偏移量
     */
    private int mInitTranslationX;
    /**
     * 指示器初始Y偏移量
     */
    private int mInitTranslationY;
    /**
     * 默认的页面可见的tab数量
     */
    private static final int COUNT_DEFAULT_VISIBLE_TAB = 4;
    /**
     * 默认最后一个tab露出百分比
     */
    //private static final float RATIO_DEFAULT_LAST_VISIBLE_TAB = 0.55f;
    /**
     * 页面可见的tab数量，默认4个
     */
    private int mTabVisibleCount = COUNT_DEFAULT_VISIBLE_TAB;

    /**
     * 最后一个tab露出百分比
     */
    // private float mLastTabVisibleRatio = RATIO_DEFAULT_LAST_VISIBLE_TAB;
    public IndexToolsSlidingTabLayout(Context context) {
        super(context);
    }

    public IndexToolsSlidingTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mSlideIcon = BitmapFactory.decodeResource(getResources(), R.drawable.index_tools_main_tab_indictor);
        this.mScreenWidth = getResources().getDisplayMetrics().widthPixels;

        //方案1：反射修改Tab宽度
        //reflectiveModifyTabWidth();

        //方案2：异步修改Tab宽度
        resetTabParams();
    }

    private void reflectiveModifyTabWidth() {
        final Class<?> clz = TabLayout.class;
        try {
            final Field requestedTabMaxWidthField = clz.getDeclaredField("mRequestedTabMaxWidth");
            final Field requestedTabMinWidthField = clz.getDeclaredField("mRequestedTabMinWidth");

            requestedTabMaxWidthField.setAccessible(true);
            //requestedTabMaxWidthField.set(this, (int) (mScreenWidth / (mTabVisibleCount + mLastTabVisibleRatio)));
            requestedTabMaxWidthField.set(this, (int) (mScreenWidth / mTabVisibleCount));

            requestedTabMinWidthField.setAccessible(true);
            //requestedTabMinWidthField.set(this, (int) (mScreenWidth / (mTabVisibleCount + mLastTabVisibleRatio)));
            requestedTabMinWidthField.set(this, (int) (mScreenWidth / mTabVisibleCount));
        } catch (final NoSuchFieldException e) {
            e.printStackTrace();
        } catch (final SecurityException e) {
            e.printStackTrace();
        } catch (final IllegalArgumentException e) {
            e.printStackTrace();
        } catch (final IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 重绘下标
     */
    public void redrawIndicator(int position, float positionOffset) {
        mTranslationX = (int) ((position + positionOffset) * tabWidth);
        invalidate();
    }

    public void setmSlideIcon(Bitmap mSlideIcon) {
        this.mSlideIcon = mSlideIcon;
    }

    /**
     * tab的父容器，注意空指针
     */
    @Nullable
    public LinearLayout getTabStrip() {
        Class<?> tabLayout = TabLayout.class;
        Field tabStrip = null;
        try {

//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//
//            } else {
//                tabStrip = tabLayout.getDeclaredField("mTabStrip");
//            }
            tabStrip = tabLayout.getDeclaredField("slidingTabIndicator");

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(this);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        llTab.setClipChildren(false);
        return llTab;
    }

    /**
     * 绘制指示器
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (mSlideIcon == null) {
            return;
        }
        canvas.save();
        // 平移到正确的位置，修正tabs的平移量
        canvas.translate(mInitTranslationX + mTranslationX, this.mInitTranslationY);
        canvas.drawBitmap(this.mSlideIcon, 0, 0, null);
        canvas.restore();
        //System.out.println("mInitTranslationX:" + mInitTranslationX + " mInitTranslationY:" + mInitTranslationY);
        super.dispatchDraw(canvas);
    }

    /**
     * 重设tab宽度
     */
    public void resetTabParams() {
        LinearLayout tabStrip = getTabStrip();
        if (tabStrip == null) {
            return;
        }
        for (int i = 0; i < tabStrip.getChildCount(); i++) {
            LinearLayout tabView = (LinearLayout) tabStrip.getChildAt(i);
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) (mScreenWidth / (mTabVisibleCount + mLastTabVisibleRatio)), LinearLayout.LayoutParams
//                    .WRAP_CONTENT);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) (mScreenWidth / mTabVisibleCount), LinearLayout.LayoutParams
                    .WRAP_CONTENT);
            tabView.setLayoutParams(params);
            //tab中的图标可以超出父容器
            tabView.setClipChildren(false);
            tabView.setClipToPadding(false);

            tabView.setPadding(0, 13, 0, 0);
        }
        initTranslationParams(tabStrip, mScreenWidth);
        invalidate();
    }

    /**
     * 初始化图片下标的坐标参数
     */
    private void initTranslationParams(LinearLayout llTab, int screenWidth) {
        if (mSlideIcon == null) {
            return;
        }
        //tabWidth = (int) (screenWidth / (mTabVisibleCount + mLastTabVisibleRatio));
        tabWidth = (int) (screenWidth / mTabVisibleCount);
        View firstView = llTab.getChildAt(0);
        if (firstView != null) {
            this.mInitTranslationX = (firstView.getLeft() + tabWidth / 2 - this.mSlideIcon.getWidth() / 2);
            this.mInitTranslationY = (getBottom() - getTop() - this.mSlideIcon.getHeight());
            if(this.mInitTranslationY<10){
                this.mInitTranslationY= (int) TransformUtil.dp2px(getContext(),32);
                //System.out.println("tab高度异常");
            }
        }
    }
}
