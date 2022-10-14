//package com.healthy.library.widget;
//
///**
// * Author: Li
// * Date: 2018/8/21 0021
// * Description:
// */
//
//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.Path;
//import android.graphics.RectF;
//import android.util.AttributeSet;
//
//
///**
// * @author Li
// * @date 2019/03/04 10:34
// * @des 圆形图片
// */
//
//public class RoundCornerImageView extends androidx.appcompat.widget.AppCompatImageView {
//
//    public RoundCornerImageView(Context context) {
//        super(context);
//    }
//    public RoundCornerImageView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//    public RoundCornerImageView(Context context, AttributeSet attrs,
//                                int defStyle) {
//        super(context, attrs, defStyle);
//    }
//    @Override
//    protected void onDraw(Canvas canvas) {
//        Path clipPath = new Path();
//        int w = this.getWidth();
//        int h = this.getHeight();
//        clipPath.addRoundRect(new RectF(0, 0, w, h), 20.0f, 20.0f, Path.Direction.CW);
//        canvas.clipPath(clipPath);
//        super.onDraw(canvas);
//    }
//
//}
