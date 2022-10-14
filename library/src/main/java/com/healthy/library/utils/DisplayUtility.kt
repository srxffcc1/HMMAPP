package com.healthy.library.utils

import android.content.res.Resources
import android.os.Build
import android.view.Display

/**
 * author : long
 * Time   :2021/11/11
 * desc   :
 */
object DisplayUtility {
    /**
     * 禁用7.0（23）以上显示大小改变和文字大小
     */
    fun disabledDisplayDpiChange(res: Resources): Resources {
        val newConfig = res.configuration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //字体非默认值
            if (res.configuration.fontScale != 1f) {
                newConfig.fontScale = 1f
            }
            newConfig.densityDpi = 408/*getDefaultDisplayDensity()*/
            res.updateConfiguration(newConfig, res.displayMetrics)
        } else {
            //字体非默认值
            if (res.configuration.fontScale != 1f) {
                newConfig.fontScale = 1f //设置默认
                res.updateConfiguration(newConfig, res.displayMetrics)
            }
        }
        return res
    }

    /**
     * 获取手机出厂时默认的densityDpi
     */
    fun getDefaultDisplayDensity(): Int {
        return try {
            val aClass = Class.forName("android.view.WindowManagerGlobal")
            val method = aClass.getMethod("getWindowManagerService")
            method.isAccessible = true
            val iwm = method.invoke(aClass)
            val getInitialDisplayDensity = iwm.javaClass.getMethod(
                "getInitialDisplayDensity",
                Int::class.javaPrimitiveType
            )
            getInitialDisplayDensity.isAccessible = true
            val densityDpi = getInitialDisplayDensity.invoke(iwm, Display.DEFAULT_DISPLAY)
            densityDpi as Int
        } catch (e: Exception) {
            e.printStackTrace()
            -1
        }
    }
}