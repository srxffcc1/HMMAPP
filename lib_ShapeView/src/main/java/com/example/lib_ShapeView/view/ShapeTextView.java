package com.example.lib_ShapeView.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.example.lib_ShapeView.R;
import com.example.lib_ShapeView.builder.ShapeDrawableBuilder;
import com.example.lib_ShapeView.builder.TextColorBuilder;
import com.example.lib_ShapeView.styleable.ShapeTextViewStyleable;

/**
 * time   : 2021/07/17
 * desc   : 支持直接定义 Shape 背景的 TextView
 */
public class ShapeTextView extends AppCompatTextView {

    private static final ShapeTextViewStyleable STYLEABLE = new ShapeTextViewStyleable();

    private final ShapeDrawableBuilder mShapeDrawableBuilder;
    private final TextColorBuilder mTextColorBuilder;

    public ShapeTextView(Context context) {
        this(context, null);
    }

    public ShapeTextView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public ShapeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShapeTextView);
        mShapeDrawableBuilder = new ShapeDrawableBuilder(this, typedArray, STYLEABLE);
        mTextColorBuilder = new TextColorBuilder(this, typedArray, STYLEABLE);
        typedArray.recycle();

        mShapeDrawableBuilder.intoBackground();

        if (mTextColorBuilder.isTextGradientColors()) {
            setText(getText());
        } else {
            mTextColorBuilder.intoTextColor();
        }
    }

    @Override
    public void setTextColor(int color) {
        super.setTextColor(color);
        if (mTextColorBuilder == null) {
            return;
        }
        mTextColorBuilder.setTextColor(color);
        mTextColorBuilder.clearTextGradientColors();
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (mTextColorBuilder != null && mTextColorBuilder.isTextGradientColors()) {
            super.setText(mTextColorBuilder.buildLinearGradientSpannable(text), type);
        } else {
            super.setText(text, type);
        }
    }

    public ShapeDrawableBuilder getShapeDrawableBuilder() {
        return mShapeDrawableBuilder;
    }

    public TextColorBuilder getTextColorBuilder() {
        return mTextColorBuilder;
    }
}