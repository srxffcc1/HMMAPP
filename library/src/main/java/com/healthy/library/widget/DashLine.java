package com.healthy.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import com.healthy.library.R;
import com.hss01248.dialog.ScreenUtil;

/**
 * 虚线
 *
 * @author luoyang
 * @version [AndroidLibrary, 2018-03-09]
 */
public class DashLine extends View {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    float strokeWidth;
    boolean isVertical;

    public DashLine(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public DashLine(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 禁止硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.com_DashLine);
        isVertical = a.getBoolean(R.styleable.com_DashLine_com_vertical, false);
        strokeWidth = a.getDimensionPixelSize(R.styleable.com_DashLine_com_strokeWidth, ScreenUtil.dip2px(2));
        paint.setColor(a.getColor(R.styleable.com_DashLine_com_color, Color.parseColor("#dddddd")));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        paint.setPathEffect(new DashPathEffect(new float[]{
                a.getDimensionPixelSize(R.styleable.com_DashLine_com_dashWidth, ScreenUtil.dip2px(6)),
                a.getDimensionPixelSize(R.styleable.com_DashLine_com_dashGap, ScreenUtil.dip2px(3))}, 0));

        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (isVertical) {
            setMeasuredDimension((int) strokeWidth, getMeasuredHeight());
        } else {
            setMeasuredDimension(getMeasuredWidth(), (int) strokeWidth);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isVertical) {
            canvas.drawLine(0, 0, 0, getMeasuredHeight(), paint);
        } else {
            canvas.drawLine(0, 0, getMeasuredWidth(), 0, paint);
        }
    }

    public void setDashLineColor(@ColorInt int color) {
        paint.setColor(color);
    }
}
