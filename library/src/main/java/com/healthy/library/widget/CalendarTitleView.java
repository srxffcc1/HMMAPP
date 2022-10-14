package com.healthy.library.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * @author Li
 * @date 2019/03/22 17:57
 * @des
 */
@Deprecated
public class CalendarTitleView extends View {
    private String[] days = new String[]{"日", "一", "二", "三", "四", "五", "六"};
    private Paint mBgPaint;
    private Paint mTextPaint;

    public CalendarTitleView(Context context) {
        this(context, null);
    }

    public CalendarTitleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarTitleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                12, context.getResources().getDisplayMetrics()));
        mTextPaint.setColor(Color.parseColor("#444444"));
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBgPaint.setColor(Color.parseColor("#F5F5F9"));
        mBgPaint.setStyle(Paint.Style.FILL);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float simpleWidth = getWidth() * 1.0f / days.length;

        float centerY = getHeight() * 0.5f + Math.abs(mTextPaint.ascent() + mTextPaint.descent()) * 0.5f;
        canvas.drawRoundRect(0, 0, getWidth(),
                getHeight(), getHeight() * 0.5f, getHeight() * 0.5f, mBgPaint);
        for (int i = 0; i < days.length; i++) {
            float centerX = simpleWidth * 0.5f + simpleWidth * i;
            canvas.drawText(days[i], centerX, centerY, mTextPaint);
        }
    }
}
