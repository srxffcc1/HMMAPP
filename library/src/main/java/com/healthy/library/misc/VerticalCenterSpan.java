//package com.healthy.library.span;
//
//import android.graphics.Canvas;
//import android.graphics.Paint;
//import androidx.annotation.NonNull;
//import android.text.TextPaint;
//import android.text.style.ReplacementSpan;
//
///**
// * Author: Li
// * Date: 2018/12/17 0017
// * Description:
// */
//public class VerticalCenterSpan extends ReplacementSpan {
//
//    private float scale;
//
//    public VerticalCenterSpan(float scale) {
//        this.scale = scale;
//    }
//
//    @Override
//    public int getSize(@NonNull Paint paint, CharSequence text, int start,
//                       int end, Paint.FontMetricsInt fm) {
//        text = text.subSequence(start, end);
//        Paint p = getCustomTextPaint(paint);
//        return (int) p.measureText(text.toString());
//    }
//
//    @Override
//    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end,
//                     float x, int top, int y, int bottom, Paint paint) {
//        text = text.subSequence(start, end);
//        Paint p = getCustomTextPaint(paint);
//        Paint.FontMetricsInt fm = p.getFontMetricsInt();
//        // 此处重新计算y坐标，使字体居中
//        canvas.drawText(text.toString(), x, y - ((y + fm.descent + y + fm.ascent) / 2 - (bottom + top) / 2), p);
//    }
//
//    private TextPaint getCustomTextPaint(Paint srcPaint) {
//        TextPaint paint = new TextPaint(srcPaint);
//        paint.setTextSize(srcPaint.getTextSize() * scale);
//        return paint;
//    }
//}
