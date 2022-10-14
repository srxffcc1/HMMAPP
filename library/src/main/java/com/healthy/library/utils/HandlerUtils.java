package com.healthy.library.utils;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

/**
 * @author long
 * @description
 * @date 2021/8/26
 */
public class HandlerUtils {
    
    private HandlerUtils() {
        throw new UnsupportedOperationException("Guy, r u crazy? u can NOT instantiate me...");
    }

    public static class HandlerHolder extends Handler {
        SoftReference<OnReceiveMessageListener> mListenerWeakReference;

        /**
         * 使用必读：推荐在Activity或者Activity内部持有类中实现该接口，不要使用匿名类，可能会被GC
         *
         * @param listener 收到消息回调接口
         */
        public HandlerHolder(OnReceiveMessageListener listener) {
            mListenerWeakReference = new SoftReference<>(listener);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mListenerWeakReference != null && mListenerWeakReference.get() != null) {
                mListenerWeakReference.get().handlerMessage(msg);
            }
        }
    }

    /**
     * 收到消息回调接口
     */
    public interface OnReceiveMessageListener {
        void handlerMessage(Message msg);
    }

}
