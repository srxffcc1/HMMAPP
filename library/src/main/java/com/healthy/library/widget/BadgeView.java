package com.healthy.library.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
@Deprecated
/**
 * Author: Li
 * Date: 2018/10/9 0009
 * Description:
 */
public class BadgeView extends androidx.appcompat.widget.AppCompatTextView {
    private Paint mCirclePaint;

    public BadgeView(Context context) {
        this(context, null);
    }

    public BadgeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BadgeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(Color.RED);
        mCirclePaint.setStyle(Paint.Style.FILL);
        setPadding(10,10,10,10);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size = Math.max(MeasureSpec.getSize(widthMeasureSpec),
                MeasureSpec.getSize(heightMeasureSpec));
        super.onMeasure(MeasureSpec.makeMeasureSpec(size, MeasureSpec.getMode(widthMeasureSpec)),
                MeasureSpec.makeMeasureSpec(size,MeasureSpec.getMode(heightMeasureSpec)));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, mCirclePaint);

        super.onDraw(canvas);
    }
}
