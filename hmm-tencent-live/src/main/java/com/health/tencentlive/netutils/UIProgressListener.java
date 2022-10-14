package com.health.tencentlive.netutils;

import android.os.Handler;
import android.os.Message;


public abstract class UIProgressListener implements ProgressListener {
    private int id;
    private boolean isFirst = false;
    private long updateTime = System.currentTimeMillis();
    private boolean isCancled = false;

    //主线程Handler
    private Handler mHandler ;

    public UIProgressListener() {
        this.id = id;
        mHandler = new UIHandler(this, id);
    }

    //处理UI层的Handler子类
    private static class UIHandler extends ProgressHandler {
        private int id;

        public UIHandler(UIProgressListener uiProgressListener, int id) {
            super(uiProgressListener);
            this.id = id;
        }

        @Override
        public void start(UIProgressListener uiProgressListener, long currentBytes, long contentLength, boolean done) {
            if (uiProgressListener!=null) {
                uiProgressListener.onUIStart(id, currentBytes, contentLength, done);
            }
        }

        @Override
        public void progress(UIProgressListener uiProgressListener, long currentBytes, long contentLength, boolean done) {
            if (uiProgressListener!=null){
                uiProgressListener.onUIProgress(id, currentBytes, contentLength, done);
            }
        }

        @Override
        public void finish(UIProgressListener uiProgressListener, long currentBytes, long contentLength, boolean done) {
            if (uiProgressListener!=null){
                uiProgressListener.onUIFinish(id, currentBytes, contentLength,done);
            }
        }
    }

    @Override
    public void onProgress(long bytesWrite, long contentLength, boolean done) {
        if(isCancled()) {
            return;
        }
        //如果是第一次，发送消息
        if (!isFirst) {
            isFirst = true;
            updateTime = System.currentTimeMillis();
            Message start = Message.obtain();
            start.obj = new ProgressModel(bytesWrite, contentLength, done);
            start.what = ProgressHandler.START;
            mHandler.sendMessage(start);
        }

        //通过Handler发送进度消息
        if(System.currentTimeMillis()-updateTime>=1000){
            updateTime = System.currentTimeMillis();
            Message message = Message.obtain();
            message.obj = new ProgressModel(bytesWrite, contentLength, done);
            message.what = ProgressHandler.UPDATE;
            mHandler.sendMessage(message);
        }

        if(done) {
            Message finish = Message.obtain();
            finish.obj = new ProgressModel(bytesWrite, contentLength, done);
            finish.what = ProgressHandler.FINISH;
            mHandler.sendMessage(finish);
        }
    }

    @Override
    public boolean isCancled() {
        return isCancled;
    }

    @Override
    public void setCancled() {
        isCancled = true;
    }

    /**
     * UI层回调抽象方法
     *
     * @param currentBytes    当前的字节长度
     * @param contentLength 总字节长度
     * @param done          是否写入完成
     */
    public abstract void onUIProgress(int taskId, long currentBytes, long contentLength, boolean done);

    /**
     * UI层开始请求回调方法
     * @param currentBytes 当前的字节长度
     * @param contentLength 总字节长度
     * @param done 是否写入完成
     */
    public void onUIStart(int taskId, long currentBytes, long contentLength, boolean done) {

    }

    /**
     * UI层结束请求回调方法
     * @param currentBytes 当前的字节长度
     * @param contentLength 总字节长度
     * @param done 是否写入完成
     */
    public void onUIFinish(int taskId, long currentBytes, long contentLength, boolean done) {

    }
}
