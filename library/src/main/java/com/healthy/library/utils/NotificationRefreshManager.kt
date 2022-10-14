package com.healthy.library.utils

import android.text.TextUtils
import android.util.Log
import com.healthy.library.BuildConfig
import com.healthy.library.interfaces.OnNotificationListener
import java.util.concurrent.CopyOnWriteArrayList

class NotificationRefreshManager {
    private val TAG = "NotificationRefreshManager"
    /**
     * 注册的接口集合，发送广播的时候都能收到
     */
    private val iListenerList: MutableList<OnNotificationListener> = CopyOnWriteArrayList()

    /**
     * 注册监听
     */
    fun registerListener(iListener: OnNotificationListener) {
        iListenerList.add(iListener)
    }

    /**
     * 注销监听
     */
    fun unRegisterListener(iListener: OnNotificationListener?) {
        if (iListenerList.contains(iListener)) {
            iListenerList.remove(iListener)
        }
    }

    /**
     * 发送广播
     * @param className 如传入想要接受的class类，会执行单独发送空或空字符串所有统一发送
     */
    fun sendBroadCast(type: String?, className: String) {

        for (iListener in iListenerList) {
            if (!TextUtils.isEmpty(className)) {
                if(iListener.javaClass.simpleName == className) {
                    iListener.onNotification(type, className)
                    if (BuildConfig.DEBUG) {
                        //Log.e(TAG, "sendBroadCast:目标class: ${iListener.javaClass.simpleName}")
                    }
                    return
                }
                continue
            }
            iListener.onNotification(type, className)
        }
    }

    companion object {
        /**
         * 单例模式
         */
        @Volatile
        private var listenerManager: NotificationRefreshManager? = null

        /**
         * 获得单例对象
         */
        val instance: NotificationRefreshManager?
            get() {
                if (listenerManager == null) {
                    synchronized(NotificationRefreshManager::class.java) {
                        if (null == listenerManager) {
                            listenerManager = NotificationRefreshManager()
                        }
                    }
                }
                return listenerManager
            }
    }
}