package com.health.faq.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import androidx.annotation.IntDef;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.health.faq.R;

import java.util.Locale;


/**
 * @author Li
 * @date 2019/07/17 11:26
 * @des 专家回答的语音控件
 */
public class ExpertVoiceView extends View {
    private float mSimplePadding;
    private float mPaddingGap;
    private float mGap;
    private float mLineMarginStart;
    private Paint mLinePaint;
    private float mStrokeWidth;
    private Paint mTextPaint;
    private String mContent = "";
    private float mTxtMargin;
    private Bitmap mLoadingBitmap;
    private Bitmap mPlayBitmap;
    private Bitmap mPauseBitmap;
    private Matrix mMatrix;
    private float mBitmapSize;
    private float mDegrees = 0;
    private ValueAnimator mAnimator;
    private ValueAnimator mJumpAnimator;
    private int mCurrentState;
    public static final int STATE_LOADING = 1;
    public static final int STATE_PLAY = 2;
    public static final int STATE_PAUSE = 3;
    private float mBitmapMargin;
    private float mPercent = 0;
    private float mChangeHeight;

    public ExpertVoiceView(Context context) {
        this(context, null);
    }

    public ExpertVoiceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpertVoiceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColors(new int[]{Color.parseColor("#4DE4B6"),
                Color.parseColor("#22DDDD")});
        drawable.setCornerRadius(dp2px(context, 6));
        drawable.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
        setBackground(drawable);

        mStrokeWidth = dp2px(context, 2);

        mGap = dp2px(context, 3);
        mLineMarginStart = dp2px(context, 22);
        mPaddingGap = dp2px(context, 3);
        mTxtMargin = dp2px(context, 10);
        mBitmapMargin = dp2px(context, 22);

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStrokeCap(Paint.Cap.ROUND);
        mLinePaint.setStyle(Paint.Style.FILL);
        mLinePaint.setStrokeWidth(mStrokeWidth);
        mLinePaint.setColor(Color.WHITE);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(dp2px(context, 12));
        mMatrix = new Matrix();
        mLoadingBitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.faq_ic_voice_loading);

        mPlayBitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.faq_ic_play);
        mPauseBitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.faq_ic_pause);

        mChangeHeight = dp2px(context, 10);

        mCurrentState = STATE_PLAY;
    }

    private float dp2px(Context context, float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,
                context.getResources().getDisplayMetrics());
    }


    @Override
    protected void onDetachedFromWindow() {
        stopRotate();
        stopJump();
        super.onDetachedFromWindow();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBitmapSize == 0) {
            mBitmapSize = dp2px(getContext(), 20);
        }
        if (mSimplePadding == 0) {
            mSimplePadding = getHeight() * 0.3f;
        }
        int middle = 5 / 2;
        float offset;
        for (int i = 0; i < 5; i++) {
            if (middle == i) {
                offset = mSimplePadding;
            } else {
                offset = mSimplePadding + mPaddingGap * (middle - i % middle);
            }
            float startX = mLineMarginStart + mGap * i + mStrokeWidth * i;
            float startY = offset + mPercent * mChangeHeight;
            float endY = getHeight() - offset - mPercent * mChangeHeight;
            canvas.drawLine(startX, startY, startX, endY, mLinePaint);
        }
        float x = mLineMarginStart + mGap * 4 + mStrokeWidth * 4 + mTxtMargin;
        float y = getHeight() * 0.5f + Math.abs(mTextPaint.ascent() + mTextPaint.descent()) * 0.5f;
        canvas.drawText(mContent, x, y, mTextPaint);
        mMatrix.reset();
        mMatrix.postTranslate(getWidth() - mLoadingBitmap.getWidth() - mBitmapMargin,
                (getHeight() - mLoadingBitmap.getHeight()) * 0.5f);
        switch (mCurrentState) {
            case STATE_LOADING:
                mMatrix.postRotate(mDegrees, getWidth() - mLoadingBitmap.getWidth() - mBitmapMargin +
                                mLoadingBitmap.getWidth() * 0.5f,
                        (getHeight() - mLoadingBitmap.getHeight()) * 0.5f + mLoadingBitmap.getWidth() * 0.5f);
                canvas.drawBitmap(mLoadingBitmap, mMatrix, null);
                break;
            case STATE_PLAY:
                canvas.drawBitmap(mPlayBitmap, mMatrix, null);
                break;
            case STATE_PAUSE:
                canvas.drawBitmap(mPauseBitmap, mMatrix, null);
                break;
            default:
                break;
        }
    }

    public void setDuration(long duration) {
        long seconds = duration / 1000;
        String content;
        if (seconds <= 0) {
            content = "";
        } else if (seconds < 60) {
            content = String.format(Locale.CHINA, "%d''", seconds);
        } else {
            content = String.format(Locale.CHINA, "%d'%02d''", seconds / 60, seconds % 60);
        }
        mContent = content;
        invalidate();
    }

    private void startRotate() {
        if (mAnimator == null) {
            mAnimator = ValueAnimator.ofFloat(0, 360);
            mAnimator.setInterpolator(new LinearInterpolator());
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mDegrees = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            mAnimator.setDuration(1000);
            mAnimator.setRepeatMode(ValueAnimator.RESTART);
            mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        }
        if (!mAnimator.isStarted() && !mAnimator.isRunning()) {
            mAnimator.start();
        }
    }

    private void stopRotate() {
        if (mAnimator != null) {
            mAnimator.cancel();
        }
    }


    private void startJump() {
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

    private void stopJump() {
        if (mJumpAnimator != null) {
            mJumpAnimator.setCurrentPlayTime(0);
            mJumpAnimator.cancel();
        }
    }

    public void setState(@VoiceStatus int state) {
        mCurrentState = state;
        switch (state) {
            case STATE_LOADING:
                stopJump();
                startRotate();
                break;
            case STATE_PLAY:
                stopJump();
                stopRotate();
                break;
            case STATE_PAUSE:
                stopRotate();
                startJump();
                break;
            default:
                break;
        }
        invalidate();
    }

    public int getCurrentState() {
        return mCurrentState;
    }

    @IntDef({STATE_LOADING, STATE_PLAY, STATE_PAUSE})
    public @interface VoiceStatus {

    }
}
