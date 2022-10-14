package com.healthy.library.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;

import com.healthy.library.R;
import com.healthy.library.utils.HandlerUtils;


/**
 * @author LinJ
 * @ClassName: FilterImageView
 * @Description: 点击时显示明暗变化(滤镜效果)的ImageView
 * @date 2015-1-6 下午2:13:46
 */
public class AutoClickImageView extends CornerImageView implements GestureDetector.OnGestureListener {

    public String clicktype = "scale";

    public void setClicktype(String clicktype) {
        this.clicktype = clicktype;
    }

    private final int HANDLER_WHAT_START = 0x00;
    private final int HANDLER_WHAT_NORMAL_SHOW = 0x01;
    private final int HANDLER_WHAT_NORMAL = 0x02;

    public boolean canTouch=true;

    public boolean isAnimation=false;

    public boolean isLoopAnimation=false;

    public void setCanTouch(boolean canTouch) {
        this.canTouch = canTouch;
    }

    //public HandlerUtils.HandlerHolder mHandler = new HandlerUtils.HandlerHolder(this);
    public Handler mHandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            if (what == HANDLER_WHAT_START) {
                startLoopScaleAutoPrivate();
            }
            if (what == HANDLER_WHAT_NORMAL_SHOW) {
                Animation loadAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.scale_xysize_normal_anim_slow);
                clearAnimation();
                startAnimation(loadAnimation);
            }
            if (what == HANDLER_WHAT_NORMAL) {
                Animation loadAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.scale_xysize_normal_anim);
                clearAnimation();
                startAnimation(loadAnimation);
            }
        }
    };

    public void initHander(){
        if(mHandler==null){
            mHandler= new Handler(Looper.myLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    int what = msg.what;
                    if (what == HANDLER_WHAT_START) {
                        startLoopScaleAutoPrivate();
                    }
                    if (what == HANDLER_WHAT_NORMAL_SHOW) {
                        Animation loadAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.scale_xysize_normal_anim_slow);
                        clearAnimation();
                        startAnimation(loadAnimation);
                    }
                    if (what == HANDLER_WHAT_NORMAL) {
                        Animation loadAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.scale_xysize_normal_anim);
                        clearAnimation();
                        startAnimation(loadAnimation);
                    }
                }
            };
        }
    }

    /**
     * 监听手势
     */
    private GestureDetector mGestureDetector;

    public AutoClickImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector = new GestureDetector(context, this);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(canTouch){
            if (event.getActionMasked() == MotionEvent.ACTION_CANCEL || event.getActionMasked() == MotionEvent.ACTION_UP) {
                if ("null".equals(clicktype)) {

                } else {
                    if ("scale".equals(clicktype)) {
                        scaleToNormal();
                    } else {
                        removeFilter();
                    }
                }

            }
        }
        //在cancel里将滤镜取消，注意不要捕获cacncel事件,mGestureDetector里有对cancel的捕获操作
        //在滑动GridView时，AbsListView会拦截掉Move和UP事件，直接给子控件返回Cancel

        return mGestureDetector.onTouchEvent(event);
    }

    /**
     * 设置滤镜
     */
    private void setFilter() {
        //先获取设置的src图片
        Drawable drawable = getDrawable();
        //当src图片为Null，获取背景图片
        if (drawable == null) {
            drawable = getBackground();
        }
        if (drawable != null) {
            //设置滤镜
            drawable.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
            ;
        }
    }

    /*public void startScaleAuto() {
        scaleToSmaller();
        scaleToNormal();
    }*/

    public void startScaleAutoSlow() {
        scaleToSmallerSlow();
        scaleToNormalSlow();
    }

    public void clearLoop(){
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    public void startLoopScaleAuto() {
        if(isAnimation){
           return;
        }
        isAnimation=true;
        startScaleAutoSlow();
        if (mHandler != null) {
//            System.out.println("重新构图设定3");
            mHandler.sendEmptyMessageDelayed(0, 2000);
        }
       /* new Thread(() -> {
            while (isStart) {
                post(() -> startScaleAutoSlow());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();*/
    }


    private void startLoopScaleAutoPrivate() {
        isAnimation=true;
        startScaleAutoSlow();
        if (mHandler != null) {
//            System.out.println("重新构图设定3");
            mHandler.sendEmptyMessageDelayed(0, 2000);
        }
    }

    private void scaleToSmallerSlow() {
        Animation loadAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.scale_xysize_smaller_anim_slow);
        clearAnimation();
        startAnimation(loadAnimation);
    }

    private void scaleToNormalSlow() {
        if (mHandler != null) {
            mHandler.sendEmptyMessageDelayed(HANDLER_WHAT_NORMAL_SHOW, 1000);
        }
        /*postDelayed(() -> {
            Animation loadAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.scale_xysize_normal_anim_slow);
            clearAnimation();
            startAnimation(loadAnimation);
        }, 1000);*/
    }

    private void scaleToSmaller() {
        Animation loadAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.scale_xysize_smaller_anim);
        clearAnimation();
        startAnimation(loadAnimation);
    }

    private void scaleToNormal() {
        if (mHandler != null) {
            mHandler.sendEmptyMessageDelayed(HANDLER_WHAT_NORMAL, 200);
        }
        /*postDelayed(() -> {
            Animation loadAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.scale_xysize_normal_anim);
            clearAnimation();
            startAnimation(loadAnimation);
        }, 200);*/

    }

    /**
     * 清除滤镜
     */
    private void removeFilter() {
        //先获取设置的src图片
        Drawable drawable = getDrawable();
        //当src图片为Null，获取背景图片
        if (drawable == null) {
            drawable = getBackground();
        }
        if (drawable != null) {
            //清除滤镜
            drawable.clearColorFilter();
        }
    }

    @Override
    public boolean onDown(MotionEvent e) {
        if(canTouch){
            if ("null".equals(clicktype)) {

            } else {
                if ("scale".equals(clicktype)) {
                    scaleToNormal();
                } else {
                    removeFilter();
                }
            }
        }

        //这里必须返回true，表示捕获本次touch事件
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if(canTouch){
            if ("null".equals(clicktype)) {

            } else {
                if ("scale".equals(clicktype)) {
                    scaleToNormal();
                } else {
                    removeFilter();
                }
            }
        }

        performClick();

        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        //长安时，手动触发长安事件
        performLongClick();
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected void onDetachedFromWindow() {
        destroy();
        super.onDetachedFromWindow();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if(isAnimation){
            initHander();
            startLoopScaleAutoPrivate();
        }
    }

    public void destroy() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        clearAnimation();
    }

}