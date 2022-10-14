package com.healthy.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.healthy.library.R;

/**
 * @author Li
 * @date 2019/03/25 16:41
 * @des 可以指定文本start/top/end/bottom的drawable大小的文本控件
 */
@Deprecated //ImageTextView
public class DrawableTextView extends androidx.appcompat.widget.AppCompatTextView {
    int mDrawableSize;
    int mDrawableWidth;
    int mDrawableHeight;

    public DrawableTextView(Context context) {
        super(context);
    }

    public DrawableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);

    }

    public DrawableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.DrawableTextView);
        mDrawableSize = array.getDimensionPixelSize(R.styleable.DrawableTextView_drawable_size, 0);
        mDrawableWidth = array.getDimensionPixelSize(R.styleable.DrawableTextView_drawable_width, 0);
        mDrawableHeight = array.getDimensionPixelSize(R.styleable.DrawableTextView_drawable_height, 0);
        setMaxEms(12);
        array.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        Drawable[] drawables = getCompoundDrawablesRelative();
        if (mDrawableSize != 0) {
            for (Drawable drawable : drawables) {
                setBounds(drawable, mDrawableSize, mDrawableSize);
            }
            setCompoundDrawablesRelative(drawables[0], drawables[1],
                    drawables[2], drawables[3]);
        } else if (mDrawableWidth != 0 && mDrawableHeight != 0) {
            for (Drawable drawable : drawables) {
                setBounds(drawable, mDrawableWidth, mDrawableHeight);
            }
            setCompoundDrawablesRelative(drawables[0], drawables[1],
                    drawables[2], drawables[3]);
        }
    }

    private void setBounds(Drawable drawable, int right, int bottom) {
        if (drawable != null) {
            drawable.setBounds(0, 0, right, bottom);
        }
    }
}