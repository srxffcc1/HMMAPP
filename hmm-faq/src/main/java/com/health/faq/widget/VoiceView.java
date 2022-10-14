package com.health.faq.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.healthy.library.utils.TransformUtil;

/**
 * @author Li
 * @date 2019/07/02 11:11
 * @des
 */
public class VoiceView extends View {
    private Paint mPaint;
    private Path mPath;
    private Paint mLinePaint;
    private LinearGradient mLinearGradient;
    private float mGap;
    private float mLineMarginStart;
    private float mSimplePadding;
    private float mPaddingGap;
    private ValueAnimator mJumpAnimator;
    private float mPercent = 0;
    private float mChangeHeight;
    public VoiceView(Context context) {
        this(context, null);
    }

    public VoiceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VoiceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.RED);
        mPath = new Path();


        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStrokeCap(Paint.Cap.ROUND);
        mLinePaint.setStyle(Paint.Style.FILL);
        mLinePaint.setStrokeWidth(6);
        mLinePaint.setColor(Color.WHITE);

        mLineMarginStart = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30,
                context.getResources().getDisplayMetrics());
        mGap = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3,
                context.getResources().getDisplayMetrics());

        mPaddingGap = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3,
                context.getResources().getDisplayMetrics());
        mChangeHeight = TransformUtil.dp2px(context, 10);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mSimplePadding == 0) {
            mSimplePadding = getHeight() * 0.3f;
        }
        if (mLinearGradient == null) {
            mLinearGradient = new LinearGradient(0, getHeight(),
                    getWidth(), getHeight(),
                    Color.parseColor("#4DE4B6"),
                    Color.parseColor("#22DDDD"),
                    Shader.TileMode.CLAMP);
            mPaint.setShader(mLinearGradient);

        }

        float width = getWidth() - getPaddingEnd();
        float height = getHeight();
        mPath.reset();
        mPath.moveTo(0, 0);
        mPath.lineTo(width - 12, 0);
        mPath.cubicTo(width - 5.373f, 0, width, 5.373f, width, 12);
        mPath.lineTo(width, height - 12);
        mPath.cubicTo(width, height - 5.373f, width - 4.373f, height, width - 12,
                height);
        mPath.lineTo(23.659f, height);
        mPath.cubicTo(23.659f - 6.627f, height, 23.659f - 12, height - 5.373f,
                23.659f - 12, height - 12);
        mPath.lineTo(23.659f - 12, 14.413f);
        mPath.cubicTo(23.659f - 12, 9.063f, 7.773f, 4.258f, 0, -0);
        mPath.close();
        canvas.drawPath(mPath, mPaint);

        int middle = 5 / 2;
        for (int i = 0; i < 5; i++) {
            float offset;
            if (middle == i) {
                offset = mSimplePadding;
            } else {
                offset = mSimplePadding + mPaddingGap * (middle - i % middle);
            }
            float startX = mLineMarginStart + mGap * i + mLinePaint.getStrokeWidth() * i;
            float startY = offset+ mPercent * mChangeHeight;;
            float endY = getHeight() - offset- mPercent * mChangeHeight;
            canvas.drawLine(startX, startY, startX, endY, mLinePaint);
        }
    }
    public void startJump() {
        if (mJumpAnimator == null) {
            mJumpAnimator = ValueAnimator.ofFloat(0, 1);
            mJumpAnimator.setDuration(1000);
            mJumpAnimator.setRepeatCount(ValueAnimator.INFINITE);
            mJumpAnimator.setRepeatMode(ValueAnimator.REVERSE);
            mJumpAnimator.setInterpolator(new LinearInterpolator());
            mJumpAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mPercent = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
        }
        if (!mJumpAnimator.isStarted() && !mJumpAnimator.isRunning()) {
            mJumpAnimator.start();
        }
    }
    public void stopJump() {
        if (mJumpAnimator != null) {
            mJumpAnimator.setCurrentPlayTime(0);
            mJumpAnimator.cancel();
        }
    }
    @Override
    protected void onDetachedFromWindow() {
        stopJump();
        super.onDetachedFromWindow();
    }
}