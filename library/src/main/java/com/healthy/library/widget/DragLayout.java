package com.healthy.library.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.customview.widget.ViewDragHelper;

public class DragLayout extends RelativeLayout {

    private ViewDragHelper mDragHelper;
    //true left, false right
    private boolean leftOrRight;
    //
    private View child;

    public DragLayout(Context context) {
        super(context);
        init();
    }

    public DragLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DragLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        /**
         * @param ViewGroup forParent 必须是一个ViewGroup
         * @param float sensitivity 灵敏度
         * @param Callback cb 回调
         */
        mDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragCallback());
    }

    private class ViewDragCallback extends ViewDragHelper.Callback {
        /**
         * 尝试捕获子view，一定要返回true
         *
         * @param view      尝试捕获的view
         * @param pointerId 指示器id？
         *                  这里可以决定哪个子view可以拖动
         */
        @Override
        public boolean tryCaptureView(View view, int pointerId) {
            return view == child;
        }

        /**
         * 处理水平方向上的拖动
         *
         * @param child 被拖动到view
         * @param left  移动到达的x轴的距离
         * @param dx    建议的移动的x距离
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            LayoutParams params = (LayoutParams) child.getLayoutParams();
            return left;
        }

        /**
         * 处理竖直方向上的拖动
         *
         * @param child 被拖动到view
         * @param top   移动到达的y轴的距离
         * @param dy    建议的移动的y距离
         */
        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            LayoutParams params = (LayoutParams) child.getLayoutParams();
            // 两个if主要是为了让View在ViewGroup里面滑动
            return top;
        }

        /**
         * 手指释放的时候回调
         */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
//            System.out.println("定点数据-----------------");
//            System.out.println(releasedChild.getRight());
//            System.out.println(getRight());
//            System.out.println(releasedChild.getLeft());
//            System.out.println(getLeft());
//            System.out.println(releasedChild.getTop());
//            System.out.println(getTop());
//            System.out.println(releasedChild.getBottom());
//            System.out.println(getBottom());
//            System.out.println(releasedChild.getX()+","+releasedChild.getY());
//            System.out.println("-----------------定点数据");
            int finalX=(int) releasedChild.getX();
            int finalY=(int) releasedChild.getY();
            if(0<=releasedChild.getLeft()&&getRight()>=releasedChild.getRight()){//x有效

            }else {//x无效
                System.out.println("finalX无效");
                if(releasedChild.getRight()>getRight()){
                    finalX=getRight()-releasedChild.getWidth();
                }else {
                    finalX=getLeft();
                }
            }
            if(0<=releasedChild.getTop()&&getBottom()>=releasedChild.getBottom()){//y有效

            }else {//y无效
                System.out.println("finalY无效");
                if(releasedChild.getBottom()>getBottom()){
                    finalY=getBottom()-releasedChild.getHeight();
                }else {
                    finalY=getTop();
                }
            }

            mDragHelper.settleCapturedViewAt(finalX,finalY);
            invalidate();
        }

        /**
         * 如果子View不消耗事件，那么整个手势（DOWN-MOVE*-UP）都是直接进入onTouchEvent，在onTouchEvent的DOWN的时候就确定了captureView。
         * 如果消耗事件，那么就会先走onInterceptTouchEvent方法，判断是否可以捕获，
         * 而在判断的过程中会去判断另外两个回调的方法：getViewHorizontalDragRange和getViewVerticalDragRange，只有这两个方法返回大于0的值才能正常的捕获。
         */
        @Override
        public int getViewHorizontalDragRange(View child) {
            return getMeasuredWidth() - child.getMeasuredWidth();
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return getMeasuredHeight() - child.getMeasuredHeight();
        }

        /**
         * 当拖拽到状态改变时回调
         *
         * @param state 新的状态
         */
        @Override
        public void onViewDragStateChanged(int state) {
            switch (state) {
                case ViewDragHelper.STATE_DRAGGING:  // 正在被拖动
                    break;
                case ViewDragHelper.STATE_IDLE:  // view没有被拖拽或者 正在进行fling/snap
                    break;
                case ViewDragHelper.STATE_SETTLING: // fling完毕后被放置到一个位置
                    break;
            }
            super.onViewDragStateChanged(state);
        }
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        /**
         * 检查是否可以拦截touch事件
         * 如果onInterceptTouchEvent可以return true 则这里return true
         */
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (checkNeedIntercept(event)) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (event.getX() >= getWidth() / 2) {
                    leftOrRight = false;
                } else {
                    leftOrRight = true;
                }
                break;
        }
        /**
         * 处理拦截到的事件
         * 这个方法会在返回前分发事件
         */
        mDragHelper.processTouchEvent(event);
        return false;
    }
    /**
     * 如果view不在边上停靠时，触摸view需要拦截，否则会出现无法滑动的情况
     */
    private boolean checkNeedIntercept(MotionEvent event) {
        if(event.getX()>=child.getLeft()&&event.getX()<=child.getRight()
            &&event.getY()>=child.getTop()&&event.getY()<=child.getBottom()){
            System.out.println("需要被拦截");
            mDragHelper.processTouchEvent(event);
            return true;
        }
        return false;
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() != 1) {
            throw new IllegalStateException("DragLayout can host only one direct child");
        }
        child = getChildAt(0);
    }
}
