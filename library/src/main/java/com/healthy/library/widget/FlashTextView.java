package com.healthy.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Shader;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.healthy.library.R;
@Deprecated
/**
 * Author: Li
 * Date: 2018/10/31 0031
 * Description: 闪光字体
 */
public class FlashTextView extends androidx.appcompat.widget.AppCompatTextView {
    private LinearGradient mLinearGradient;
    private int mViewWidth;
    private int mTranslate = 0;
    private Matrix mGradientMatrix;
    private int mDuration;
    private int mStartColor;
    private int mEndColor;
    private int mFlashGap;

    public FlashTextView(Context context) {
        this(context, null);
    }

    public FlashTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlashTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.FlashTextView);
        mDuration = array.getInteger(R.styleable.FlashTextView_flash_duration, 10);
        mStartColor = array.getColor(R.styleable.FlashTextView_flash_start_color,
                Color.parseColor("#ffffff"));
        mEndColor = array.getColor(R.styleable.FlashTextView_flash_end_color,
                Color.parseColor("#33ffffff"));
        mFlashGap = array.getInteger(R.styleable.FlashTextView_flash_gap, 30);
        array.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mViewWidth == 0) {
            mViewWidth = getMeasuredWidth();
            mLinearGradient = new LinearGradient(0, 0, getWidth(), 0, mStartColor,
                    mEndColor, Shader.TileMode.MIRROR);
            mGradientMatrix = new Matrix();
            getPaint().setShader(mLinearGradient);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mGradientMatrix != null) {
            mTranslate += mViewWidth / mDuration;
            if (mTranslate > mViewWidth) {
                mTranslate = -mViewWidth;
            }
            mGradientMatrix.setTranslate(mTranslate, 0);
            mLinearGradient.setLocalMatrix(mGradientMatrix);
            postInvalidateDelayed(mFlashGap);
        }
    }
}
