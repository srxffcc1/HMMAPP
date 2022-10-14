package com.healthy.library.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;

public class AutoPollRecyclerView extends RecyclerView {
    private long TIME_AUTO_POLL = 16;
    AutoPollTask autoPollTask;
    private boolean running; //标示是否正在自动轮询
    private boolean canRun;//标示是否可以自动轮询,可在不需要的是否置false
    private int dx = 1;
    private int dy = 1;

    public void setDxy(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public AutoPollRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        autoPollTask = new AutoPollTask(this);
    }

    public void setTIME_AUTO_POLL(long TIME_AUTO_POLL) {
        this.TIME_AUTO_POLL = TIME_AUTO_POLL;
    }

    class AutoPollTask implements Runnable {
        private final WeakReference<AutoPollRecyclerView> mReference;
        private AutoPollRecyclerView mRecyclerView;

        //使用弱引用持有外部类引用->防止内存泄漏
        public AutoPollTask(AutoPollRecyclerView reference) {
            this.mReference = new WeakReference<AutoPollRecyclerView>(reference);
            this.mRecyclerView = reference;
        }

        @Override
        public void run() {
            AutoPollRecyclerView recyclerView = mReference.get();
            if (recyclerView == null) {
                recyclerView = mRecyclerView;
            }
            if (recyclerView == null) {
                stop();
            }
            if (recyclerView != null && recyclerView.running && recyclerView.canRun) {
                recyclerView.scrollBy(dx, dy);
                recyclerView.postDelayed(recyclerView.autoPollTask, recyclerView.TIME_AUTO_POLL);
            }

        }
    }

    //开启:如果正在运行,先停止->再开启
    public void start() {
        if (running) {
            stop();
        }
        canRun = true;
        running = true;
        postDelayed(autoPollTask, TIME_AUTO_POLL);
    }

    public void stop() {
        running = false;
        removeCallbacks(autoPollTask);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (running)
//                    stop();
                {
                    break;
                }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                if (canRun)
//                    start();
                {
                    break;
                }
        }
        //return  false，注释掉onTouchEvent()方法里面的stop和start方法，则列表自动滚动且不可触摸
        return super.onTouchEvent(e);
    }
}
