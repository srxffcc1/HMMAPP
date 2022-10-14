package com.healthy.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.healthy.library.R;


/**
 * @author: luoyang
 * @verson: [AppZxs，2018/9/11 11:13]
 * @description:
 */
public class ImageTextView extends AppCompatTextView {
    private Drawable mDrawable;//设置的图片
    private int mScaleWidth; // 图片的宽度
    private int mScaleHeight;// 图片的高度
    private int mPosition;// 图片的位置 1左 2上 3右 4下
    private Context context;
    CharSequence text;
    public ImageTextView(Context context) {
        super(context);
        this.context = context;
    }

    public ImageTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
        this.context = context;
    }

    public ImageTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
        this.context = context;
    }

    public void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImageTextView);
        mDrawable = typedArray.getDrawable(R.styleable.ImageTextView_drawable);
        mScaleWidth = typedArray.getDimensionPixelOffset(R.styleable.ImageTextView_drawableWidth, dip2px(context, 20));
        mScaleHeight = typedArray.getDimensionPixelOffset(R.styleable.ImageTextView_drawableHeight, dip2px(context, 20));
        mPosition = typedArray.getInt(R.styleable.ImageTextView_position, 1);
        setDrawable(mDrawable);

    }

    /**
     * 设置左侧图片并重绘 * * @param drawableLeft
     */
    public void setDrawable(Drawable drawable) {
        this.mDrawable = drawable;
        if (mDrawable != null) {
            mDrawable.setBounds(0, 0, mScaleWidth, mScaleHeight);
        }
        switch (mPosition) {
            case 1:
                this.setCompoundDrawables(mDrawable, null, null, null);
                break;
            case 2:
                this.setCompoundDrawables(null, mDrawable, null, null);
                break;
            case 3:
                this.setCompoundDrawables(null, null, mDrawable, null);
                break;
            case 4:
                this.setCompoundDrawables(null, null, null, mDrawable);
                break;
            default:
                break;
        }
    }

    /**
     * 设置左侧图片并重绘 * * @param drawableLeftRes
     */
    public void setDrawable(int drawableRes, Context context) {
        if (drawableRes==-1){
            this.mDrawable = null;
        }else {

            this.mDrawable = context.getResources().getDrawable(drawableRes);
        }
        setDrawable(mDrawable);
    }

    /**
     * dip转pix
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}



