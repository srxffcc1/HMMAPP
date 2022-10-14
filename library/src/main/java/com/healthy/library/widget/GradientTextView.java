//package com.healthy.library.widget;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.graphics.Rect;
//import android.graphics.drawable.GradientDrawable;
//import android.util.AttributeSet;
//
///**
// * @author Li
// * @date 2019/03/01 13:10
// * @des 渐变背景的TextView
// */
//
//public class GradientTextView extends androidx.appcompat.widget.AppCompatTextView {
//    private GradientDrawable mGradientDrawable;
//    public GradientTextView(Context context) {
//        this(context,null);
//    }
//
//    public GradientTextView(Context context, AttributeSet attrs) {
//        this(context, attrs,0);
//    }
//
//    public GradientTextView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        mGradientDrawable=new GradientDrawable(){
//            @Override
//            protected void onBoundsChange(Rect r) {
//                super.onBoundsChange(r);
//                float radius=Math.min(r.width(),r.height())>>1;
//                setCornerRadii(new float[]{radius,radius,radius,radius,radius,radius,0,0});
//            }
//        };
//        mGradientDrawable.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
//        mGradientDrawable.setColors(new int[]{Color.parseColor("#4DE4B6"),
//                Color.parseColor("#22DDDD")});
//        setBackground(mGradientDrawable);
//        setClickable(true);
//    }
//
//    @Override
//    public void setPressed(boolean pressed) {
//        super.setPressed(pressed);
//        setAlpha(pressed?0.5f:1.0f);
//    }
//}
