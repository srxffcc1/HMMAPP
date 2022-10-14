package com.health.mine.presenter

import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.reflect.TypeToken
import com.health.mine.contract.EnlistContract
import com.health.mine.contract.EnlistSignOffContract
import com.healthy.library.businessutil.ListUtil
import com.healthy.library.constant.Functions
import com.healthy.library.constant.SpKey
import com.healthy.library.model.EnlistActivityModel
import com.healthy.library.net.ObservableHelper
import com.healthy.library.net.StringObserver
import com.healthy.library.utils.SpUtils
import org.json.JSONObject
import java.util.*

/**
 * @description
 * @author long
 * @date 2021/6/30
 */
class EnListSignOffPresenter(var context: Context?, private var mView: EnlistSignOffContract.View) :
    EnlistSignOffContract.Presenter {

    private var mContext: Context? = context

    /**
     * 获取消息信息
     */
    override fun getCodeInfo(map: MutableMap<String, Any>) {
        map[Functions.FUNCTION] = "t_100107"
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : StringObserver(mView, mContext, false) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    mView.getCodeInfoSuccess(resolveRefreshDetailsData(obj))
                }

                override fun onFinish() {
                    super.onFinish()
                    mView.onRequestFinish()
                }

                override fun onFailure() {
                    super.onFailure()
                }
            })
    }

    /**
     * 确认核销
     */
    override fun confirmSignOff(map: MutableMap<String, Any>) {
        map[Functions.FUNCTION] = "t_100106"
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : StringObserver(mView, mContext, false) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    mView.confirmSignOffSuccess()
                }

                override fun onFinish() {
                    super.onFinish()
                    mView.onRequestFinish()
                }

                override fun onFailure() {
                    super.onFailure()
                }
            })
    }

    /**
     * 解析数据
     */
    fun resolveRefreshDetailsData(result: String): EnlistActivityModel {
        var resultData: EnlistActivityModel? = null
        try {
            val jsonObject = JSONObject(result).getJSONObject("data")
            val userShopInfoDTOS = jsonObject.toString()
            val builder = GsonBuilder()
            builder.registerTypeAdapter(
                Date::class.java,
                JsonDeserializer { json, typeOfT, context -> Date(json.asJsonPrimitive.asLong) })
            val gson = builder.create()
            val type = object : TypeToken<EnlistActivityModel>() {}.type
            resultData = gson.fromJson(userShopInfoDTOS, type)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return resultData!!
    }

}