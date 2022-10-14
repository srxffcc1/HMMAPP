package com.example.lib_ShapeView.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatRadioButton;

import com.example.lib_ShapeView.R;
import com.example.lib_ShapeView.builder.ButtonDrawableBuilder;
import com.example.lib_ShapeView.builder.ShapeDrawableBuilder;
import com.example.lib_ShapeView.builder.TextColorBuilder;
import com.example.lib_ShapeView.styleable.ShapeRadioButtonStyleable;


/**
 *    time   : 2021/07/17
 *    desc   : 支持直接定义 Shape 背景的 RadioButton
 */
public class ShapeRadioButton extends AppCompatRadioButton {

    private static final ShapeRadioButtonStyleable STYLEABLE = new ShapeRadioButtonStyleable();

    private final ShapeDrawableBuilder mShapeDrawableBuilder;
    private final TextColorBuilder mTextColorBuilder;
    private final ButtonDrawableBuilder mButtonDrawableBuilder;

    public ShapeRadioButton(Context context) {
        this(context, null);
    }

    public ShapeRadioButton(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.radioButtonStyle);
    }

    public ShapeRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShapeRadioButton);
        mShapeDrawableBuilder = new ShapeDrawableBuilder(this, typedArray, STYLEABLE);
        mTextColorBuilder = new TextColorBuilder(this, typedArray, STYLEABLE);
        mButtonDrawableBuilder = new ButtonDrawableBuilder(this, typedArray, STYLEABLE);
        typedArray.recycle();

        mShapeDrawableBuilder.intoBackground();

        if (mTextColorBuilder.isTextGradientColors()) {
            setText(getText());
        } else {
            mTextColorBuilder.intoTextColor();
        }

        mButtonDrawableBuilder.intoButtonDrawable();
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

    @Override
    public void setButtonDrawable(Drawable drawable) {
        super.setButtonDrawable(drawable);
        if (mButtonDrawableBuilder == null) {
            return;
        }
        mButtonDrawableBuilder.setButtonDrawable(drawable);
    }

    public ShapeDrawableBuilder getShapeDrawableBuilder() {
        return mShapeDrawableBuilder;
    }

    public TextColorBuilder getTextColorBuilder() {
        return mTextColorBuilder;
    }

    public ButtonDrawableBuilder getButtonDrawableBuilder() {
        return mButtonDrawableBuilder;
    }
}