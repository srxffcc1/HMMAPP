package com.healthy.library.business;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.healthy.library.LibApplication;
import com.healthy.library.R;
import com.healthy.library.routes.TencentLiveRoutes;
import com.healthy.library.utils.SpUtils;

import java.lang.reflect.Field;

/**
 * 悬浮窗
 */

public class HmmFloatWindowView extends RelativeLayout {


    public static final int REMOVE_FLOATING_WINDOW = 1111;
    public static final int STOP_FLOATING_WINDOW = REMOVE_FLOATING_WINDOW + 1;
    public static final int CLICK_FLOATING_WINDOW = STOP_FLOATING_WINDOW+1;

    private static final String TAG = "KSYFloatingWindowView";

    private static final int JUST_CLICK = 5;

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;

    public FrameLayout pptContainer;   // 添加白板的布局容器
    public FrameLayout videoViewContainer;  // 添加摄像头视频播放器的布局容器
    private ImageView quit;

    private Handler mHandler;

    private int statusBarHeight;

    private float xInScreen, yInScreen; //当前手指位置

    private float xDownInScreen, yDownInScreen; //手指按下位置

    private float xInView, yInView;  //手指相对于悬浮窗位置




    String course_id;
    String liveStatus;
    public void videoPass(){
        mWindowManager.removeView(HmmFloatWindowView.this);
        removeAllViews();
        if(!SpUtils.getValue(LibApplication.getAppContext(),course_id+"Pass",false)){
            ARouter.getInstance()
                    .build(TencentLiveRoutes.LiveNotice)
                    .withString("courseId", course_id)
                    .navigation();
        }else {
            ARouter.getInstance()
                    .build(TencentLiveRoutes.LIVE_LOOK)
                    .withString("courseId", course_id)
                    .navigation();
        }


    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public void setLiveStatus(String liveStatus) {
        this.liveStatus = liveStatus;
    }

    public HmmFloatWindowView(Context context) {
        super(context);
        init(context);
    }

    public HmmFloatWindowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HmmFloatWindowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    private void init(Context context) {
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.hmm_floating_player_window, this);
        pptContainer = (FrameLayout) findViewById(R.id.ppt_container);
        videoViewContainer = (FrameLayout) findViewById(R.id.video_container);
        quit = (ImageView) findViewById(R.id.close_floating_window);
        quit.setOnClickListener(mOnClickListener);
//        pptContainer.setOnClickListener(mOnClickListener);
    }

    public void updateViewLayoutParams(WindowManager.LayoutParams params) {
        mLayoutParams = params;
    }
    public void setHandler(Handler handler) {
        mHandler = handler;
    }

    // 在此处重写 onTouchEvent 处理相应的事件
    // 必须返回 true 表示在此已处理相应事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (Math.abs(xDownInScreen - xInScreen) < JUST_CLICK && Math.abs(yDownInScreen - yInScreen) < JUST_CLICK) {
                    if (mHandler != null) {
                        mHandler.obtainMessage(REMOVE_FLOATING_WINDOW).sendToTarget();
                    }
                    videoPass();

                }
                break;
            case MotionEvent.ACTION_DOWN:
                xInView = event.getX(); //相对于view的坐标
                yInView = event.getY();

                //getRaw()返回相对于屏幕左上角坐标
                xDownInScreen = event.getRawX();
                yDownInScreen = event.getRawY() - getStatusBarHeight();

                xInScreen = xDownInScreen;
                yInScreen = yDownInScreen;
                break;
            case MotionEvent.ACTION_MOVE:
                xInScreen = event.getRawX();
                yInScreen = event.getRawY() - getStatusBarHeight();
                updateViewPosition();
                break;
        }
        return true;
    }

    private void updateViewPosition() {
        mLayoutParams.x = (int) (xInScreen - xInView);
        mLayoutParams.y = (int) (yInScreen - yInView);
        mWindowManager.updateViewLayout(this, mLayoutParams);
    }

    private int getStatusBarHeight() {
        if (statusBarHeight == 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = (Integer) field.get(o);
                statusBarHeight = getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusBarHeight;
    }

    private OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getId()== R.id.close_floating_window){
                if (mHandler != null) {
                    mHandler.obtainMessage(STOP_FLOATING_WINDOW).sendToTarget();
                }
                mWindowManager.removeView(HmmFloatWindowView.this);
                removeAllViews();
            }

        }
    };

}
