package com.healthy.library.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import android.content.Context
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener

/**
 * @author Li
 * @date 2019-08-15 09:58
 * @des 定位
 */
class LocationUtil(mcontext: Context, private var onLocationListener: OnLocationListener) : LifecycleObserver, AMapLocationListener {

    private var aMapLocationClient: AMapLocationClient = AMapLocationClient(mcontext)
    private var aMapLocationClientOption: AMapLocationClientOption = AMapLocationClientOption()

    init {
        aMapLocationClientOption.isOnceLocation = true
        aMapLocationClient.setLocationListener(this)
        aMapLocationClientOption.locationMode =
                AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        aMapLocationClient.setLocationOption(aMapLocationClientOption)
    }

    fun startLocation() {
        aMapLocationClient.startLocation()
        onLocationListener.onLocationStart()
    }
//    fun reset() {
//        aMapLocationClient.onDestroy()
//        aMapLocationClient = AMapLocationClient(mcontext)
//        aMapLocationClientOption = AMapLocationClientOption()
//        aMapLocationClientOption.isOnceLocation = true
//        aMapLocationClient.setLocationListener(this)
//        aMapLocationClientOption.locationMode =
//                AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
//        aMapLocationClient.setLocationOption(aMapLocationClientOption)
//
//    }

    override fun onLocationChanged(p0: AMapLocation?) {
        p0?.let {
            if (p0.errorCode == 0) {
                aMapLocationClient.stopLocation()
                onLocationListener.onLocationSuccess(p0)
            } else {
                onLocationListener.onLocationFail(p0.errorCode)
            }
        }
    }

    interface OnLocationListener {
        fun onLocationStart()
        fun onLocationSuccess(aMapLocation: AMapLocation)
        fun onLocationFail(errCode: Int)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        aMapLocationClient.onDestroy()
    }
}