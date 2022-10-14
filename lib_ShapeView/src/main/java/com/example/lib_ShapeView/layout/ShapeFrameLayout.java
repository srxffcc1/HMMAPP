package com.example.lib_ShapeView.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.example.lib_ShapeView.R;
import com.example.lib_ShapeView.builder.ShapeDrawableBuilder;
import com.example.lib_ShapeView.styleable.ShapeFrameLayoutStyleable;


/**
 *    time   : 2021/07/17
 *    desc   : 支持直接定义 Shape 背景的 FrameLayout
 */
public class ShapeFrameLayout extends FrameLayout {

    private static final ShapeFrameLayoutStyleable STYLEABLE = new ShapeFrameLayoutStyleable();

    private final ShapeDrawableBuilder mShapeDrawableBuilder;

    public ShapeFrameLayout(Context context) {
        this(context, null);
    }

    public ShapeFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShapeFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShapeFrameLayout);
        mShapeDrawableBuilder = new ShapeDrawableBuilder(this, typedArray, STYLEABLE);
        typedArray.recycle();

        mShapeDrawableBuilder.intoBackground();
    }

    public ShapeDrawableBuilder getShapeDrawableBuilder() {
        return mShapeDrawableBuilder;
    }
}