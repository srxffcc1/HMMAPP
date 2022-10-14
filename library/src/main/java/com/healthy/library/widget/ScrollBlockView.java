//package com.healthy.library.widget;
//
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.RectF;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import android.util.AttributeSet;
//import android.view.MotionEvent;
//import android.view.VelocityTracker;
//import android.view.View;
//import android.widget.Scroller;
//
//import com.healthy.library.R;
//import com.healthy.library.utils.TransformUtil;
//
///**
// * Author: Li
// * Date: 2018/10/23 0023
// * Description:
// */
//public class ScrollBlockView extends View {
//    private Paint mPaint;
//    private Scroller mScroller;
//    private VelocityTracker mTracker;
//    private int downX;
//    private RectF mRectF;
//    private int mRadius;
//    private OnScroll mOnScroll;
//    private static final int DEFAULT_COLOR = Color.parseColor("#ffff605f");
//
//    public ScrollBlockView(Context context) {
//        this(context, null);
//    }
//
//    public ScrollBlockView(Context context, @Nullable AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//
//    public ScrollBlockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//
//        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ScrollBlockView);
//        int color = array.getColor(R.styleable.ScrollBlockView_scroll_color, DEFAULT_COLOR);
//        mRadius = array.getDimensionPixelSize(R.styleable.ScrollBlockView_scroll_radius,
//                (int) TransformUtil.dp2px(context, 5));
//        array.recycle();
//        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mPaint.setColor(color);
//        mPaint.setStyle(Paint.Style.FILL);
//        mScroller = new Scroller(context);
//        mTracker = VelocityTracker.obtain();
//        mRectF = new RectF();
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        mRectF.set(0, 0, getWidth(), getHeight());
//        canvas.drawRoundRect(mRectF, mRadius, mRadius, mPaint);
//
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        mTracker.addMovement(event);
//
//        int touchX = (int) event.getX();
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                mScroller.abortAnimation();
//                downX = touchX;
//                break;
//            case MotionEvent.ACTION_MOVE:
//                int dx = downX - touchX;
//                if (dx >= 0 && getScrollX() >= 0) {
//                    downX = touchX;
//                    break;
//                }
//                scrollBy(dx, 0);
//                downX = touchX;
//                break;
//            case MotionEvent.ACTION_UP:
//                if (Math.abs(getScrollX()) > getWidth() / 2) {
//                    mScroller.startScroll(getScrollX(), 0, getScrollX(), 0, 250);
//                    if (mOnScroll != null) {
//                        mOnScroll.onScrollComplete(this);
//                    }
//                } else {
//                    mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0, 250);
//                }
//                invalidate();
//                break;
//        }
//        return true;
//    }
//
//    @Override
//    protected void onDetachedFromWindow() {
//        super.onDetachedFromWindow();
//        if (mTracker != null) {
//            mTracker.recycle();
//            mTracker = null;
//        }
//    }
//
//    @Override
//    public void computeScroll() {
//        if (mScroller.computeScrollOffset()) {
//            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
//            postInvalidate();
//        }
//    }
//
//    @Override
//    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
//        super.onVisibilityChanged(changedView, visibility);
//        if (visibility == VISIBLE) {
//            scrollTo(0, 0);
//        }
//    }
//
//    public void reset() {
//        if (mScroller != null) {
//            mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0, 250);
//            invalidate();
//        }
//
//    }
//
//    public void setOnScroll(OnScroll onScroll) {
//        mOnScroll = onScroll;
//    }
//
//    public interface OnScroll {
//        void onScrollComplete(View view);
//    }
//
//}
