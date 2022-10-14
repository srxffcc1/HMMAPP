package com.youth.banner.indicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;

import com.youth.banner.util.BannerUtils;

/**
 * 自定长条指示器
 */
public class RectangleIndicator extends BaseIndicator {
    private int selectedWidth;//选中宽度
    private int normalWidth;//未选中宽度
    private int height;//指示器高度
    private int radius;//指示器圆角
    private int margin;//间距
    private int normalColor;//未选中颜色
    private int selectedColor;//选中颜色

    public RectangleIndicator(Context context) {
        this(context, null);
    }

    public RectangleIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RectangleIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        selectedWidth = (int) BannerUtils.dp2px(9);
        normalWidth = (int) BannerUtils.dp2px(6);
        height = (int) BannerUtils.dp2px(3);
        radius = (int) BannerUtils.dp2px(2);
        margin = (int) BannerUtils.dp2px(2);
        normalColor = Color.parseColor("#fff5f5f9");
        selectedColor = Color.parseColor("#ffffffff");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = config.getIndicatorSize();
        if (count <= 1) {
            return;
        }
        //间距*（总数-1）+默认宽度*总数
        int width = (int) ((count - 1) * margin + normalWidth * (count - 1) + selectedWidth);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int count = config.getIndicatorSize();
        if (count <= 1) {
            return;
        }
        float left = 0;
        RectF rectF;
        for (int i = 0; i < count; i++) {
            mPaint.setColor(config.getCurrentPosition() == i ? selectedColor : normalColor);
            rectF = new RectF(left, 0, config.getCurrentPosition() == i ? left + selectedWidth : left + normalWidth, height);
            //left = (i + 1) * ((config.getCurrentPosition() == i ? selectedWidth : normalWidth) + margin);
            left = rectF.right + margin;
            canvas.drawRoundRect(rectF, radius, radius, mPaint);
        }
    }
}
