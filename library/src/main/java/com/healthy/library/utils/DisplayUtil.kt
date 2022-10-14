package com.healthy.library.utils

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.Display
import com.healthy.library.BuildConfig

/**
 * @author Long
 * @desc:
 * @createTime :2021/10/13 14:13
 */
object DisplayUtil {
    private val TAG = DisplayUtil::class.java.simpleName

    /**
     * 系统设置"显示大小"时原有UI样式保持不变：
     *
     *
     * 1、当调节手机系统"显示大小"【调大】的时候，相应的dpi会变大【dp = (dpi/160) * px】,此时dp就会变大，所以相应的UI布局就会变大。
     * 2、当调节手机系统"分辨率"【调小】的时候，相应的dpi会变小【比如由480-->320】。如果此时使用技术手段使dpi保持不变，那么相同的dp就会占用更多的px，所以UI布局就会变大。
     *
     * @param context
     */
    fun setDefaultDisplay(context: Context) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            val origConfig = context.resources.configuration
            //获取手机出厂时默认的densityDpi【注释1】
            origConfig.densityDpi = 408//defaultDisplayDensity
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "class = ${context.javaClass.simpleName};densityDpi: ${origConfig.densityDpi}")
            }
            val displayMetrics = context.resources.displayMetrics
            /*displayMetrics.density  = 2.55f
            displayMetrics.densityDpi = 408
            displayMetrics.scaledDensity  = 2.55f*/
            context.resources.updateConfiguration(origConfig, displayMetrics)
        }
    }

    val defaultDisplayDensity: Int
        get() = try {
            val clazz = Class.forName("android.view.WindowManagerGlobal")
            val method = clazz.getMethod("getWindowManagerService")
            method.isAccessible = true
            val iwm = method.invoke(clazz)
            val getInitialDisplayDensity =
                iwm.javaClass.getMethod("getInitialDisplayDensity", Int::class.javaPrimitiveType)
            getInitialDisplayDensity.isAccessible = true
            val densityDpi = getInitialDisplayDensity.invoke(iwm, Display.DEFAULT_DISPLAY)
            densityDpi.toString().toInt()
        } catch (e: Exception) {
            e.printStackTrace()
            -1
        }
}