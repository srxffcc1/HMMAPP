package com.healthy.library.utils

import android.app.Activity
import com.healthy.library.interfaces.OnRxFFmpegListener
import io.microshow.rxffmpeg.RxFFmpegSubscriber
import java.lang.ref.WeakReference

/**
 * @author long
 * @description 压缩工具类 （内部统一回调在主线程）
 * @date 2021/7/3
 */
class MyRxFFmpegSubscriber @JvmOverloads constructor(
    activity: Activity,
    onRxFFmpegListener: OnRxFFmpegListener? = null
) : RxFFmpegSubscriber() {
    private val mWeakReference: WeakReference<Activity> = WeakReference(activity)
    private var onRxFFmpegListener: OnRxFFmpegListener?

    init {
        this.onRxFFmpegListener = onRxFFmpegListener
    }

    fun setOnRxFFmpegListener(onRxFFmpegListener: OnRxFFmpegListener?) {
        this.onRxFFmpegListener = onRxFFmpegListener
    }

    override fun onFinish() {
        onRxFFmpegListener?.onFinish()
    }

    override fun onProgress(progress: Int, progressTime: Long) {
        onRxFFmpegListener?.onProgress(progress, progressTime)
    }

    override fun onCancel() {
        println("压缩取消了")
    }

    override fun onError(message: String) {
        onRxFFmpegListener?.onError(message)
    }
}