package com.healthy.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.healthy.library.R;

/**
 * @author Li
 * @date 2019/03/05 11:42
 * @des 分割线-虚线
 */
@Deprecated
public class DashView extends View {
    private Paint mPaint;
    private static final int VERTICAL = 1;
    private static final int HORIZONTAL = 2;
    private int mDirection;
    private int mDashColor;

    public DashView(Context context) {
        this(context, null);
    }

    public DashView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DashView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mPaint.setPathEffect(new DashPathEffect(new float[]{3, 3}, 0));
        setLayerType(LAYER_TYPE_SOFTWARE, mPaint);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.DashView);
        mDirection = array.getInt(R.styleable.DashView_dash_direction, VERTICAL);
        mDashColor = array.getColor(R.styleable.DashView_dash_color,
                Color.parseColor("#CDCDD7"));
        mPaint.setColor(mDashColor);
        array.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mDirection == VERTICAL) {
            canvas.translate(0, getHeight() >> 1);
            canvas.drawLine(0, 0, getWidth(), 0, mPaint);
        } else if (mDirection == HORIZONTAL) {
            canvas.translate(getWidth() >> 1, 0);
            canvas.drawLine(0, 0, 0, getHeight(), mPaint);
        }

    }
}
