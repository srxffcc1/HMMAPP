package com.healthy.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.healthy.library.R;

/**
 * @author Li
 * @date 2019/03/22 15:47
 * @des 点
 */
@Deprecated
public class DotView extends View {

    private Paint mDotPaint;
    private LinearGradient mLinearGradient;
    private int[] colors;

    public DotView(Context context) {
        this(context, null);
    }

    public DotView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DotView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDotPaint.setStyle(Paint.Style.FILL);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.DotView);

        int startColor = array.getColor(R.styleable.DotView_dot_start_color, Color.BLACK);
        int endColor = array.getColor(R.styleable.DotView_dot_end_color, startColor);
        colors = new int[]{startColor, endColor};
        array.recycle();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float centerX = getWidth() * 0.5f;
        float centerY = getHeight() * 0.5f;
        if (mLinearGradient == null) {
            mLinearGradient = new LinearGradient(
                    0, 0, getWidth(), getHeight(), colors, null, TileMode.CLAMP);
            mDotPaint.setShader(mLinearGradient);
        }
        canvas.drawCircle(centerX, centerY, Math.min(centerX, centerY), mDotPaint);
    }

    public void setDotColor(int... color) {
        colors = color;
        if (color.length == 1) {
            colors = new int[]{color[0], color[0]};
        }
        mLinearGradient = null;
        invalidate();
    }

}
