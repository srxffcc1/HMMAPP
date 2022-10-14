package com.health.index.presenter

import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.reflect.TypeToken
import com.health.index.contract.HealthTestContract
import com.health.index.model.AiCategoryList
import com.health.index.model.AiQuestionDetail
import com.health.index.model.AiResult
import com.healthy.library.constant.Functions
import com.healthy.library.net.ObservableHelper
import com.healthy.library.net.StringObserver
import org.json.JSONObject
import java.util.*

/**
 * 创建日期：2022/02/15 14:34
 * @author LiuWei
 * @version 1.0
 * 包名： com.health.index.adapter
 * 类说明：
 */
class HealthTestPresenter(
        private val mContext: Context,
        private val mView: HealthTestContract.View
) : HealthTestContract.Presenter {


    override fun getAiCategoryList(map: MutableMap<String, Any>) {
        map[Functions.FUNCTION] = "ai_10001"
        ObservableHelper.createObservable(mContext, map)
                .subscribe(object : StringObserver(mView, mContext, false) {
                    override fun onGetResultSuccess(obj: String) {
                        super.onGetResultSuccess(obj)
                        mView.onGetAiCategoryListSuccess(resolveListData(obj))
                    }

                    override fun onFinish() {
                        super.onFinish()
                        mView.onRequestFinish()
                    }
                })
    }

    override fun getAiQuestionDetail(map: MutableMap<String, Any>) {
        map[Functions.FUNCTION] = "ai_10002"
        ObservableHelper.createObservable(mContext, map)
                .subscribe(object : StringObserver(mView, mContext, false) {
                    override fun onGetResultSuccess(obj: String) {
                        super.onGetResultSuccess(obj)
                        mView.onGetAiQuestionDetailSuccess(resolveQuestionData(obj))
                    }

                    override fun onFinish() {
                        super.onFinish()
                        mView.onRequestFinish()
                    }
                })

    }

    override fun getAiResult(map: MutableMap<String, Any>) {
        map[Functions.FUNCTION] = "ai_10003"
        ObservableHelper.createObservable(mContext, map)
                .subscribe(object : StringObserver(mView, mContext, false,false,false,false) {
                    override fun onGetResultSuccess(obj: String) {
                        super.onGetResultSuccess(obj)
                        mView.onGetAiResultSuccess(resolveResultData(obj))
                    }

                    override fun onFinish() {
                        super.onFinish()
                        mView.onRequestFinish()
                    }

                    override fun onFailure() {
                        super.onFailure()
                        mView.onGetAiResultSuccess(null)
                    }
                })

    }

    private fun resolveListData(s: String): MutableList<AiCategoryList>? {
        var list: MutableList<AiCategoryList>? = null
        try {
            val data = JSONObject(s).getJSONArray("data")
            val userShopInfoDTOS = data.toString()
            val builder = GsonBuilder()
            builder.registerTypeAdapter(
                    Date::class.java,
                    JsonDeserializer { json, typeOfT, context -> Date(json.asJsonPrimitive.asLong) })
            val gson = builder.create()
            val type = object : TypeToken<MutableList<AiCategoryList>>() {}.type
            list = gson.fromJson(userShopInfoDTOS, type)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    private fun resolveQuestionData(s: String): MutableList<AiQuestionDetail>? {
        var list: MutableList<AiQuestionDetail>? = null
        try {
            val data = JSONObject(s).getJSONArray("data")
            val userShopInfoDTOS = data.toString()
            val builder = GsonBuilder()
            builder.registerTypeAdapter(
                Date::class.java,
                JsonDeserializer { json, typeOfT, context -> Date(json.asJsonPrimitive.asLong) })
            val gson = builder.create()
            val type = object : TypeToken<MutableList<AiQuestionDetail>>() {}.type
            list = gson.fromJson(userShopInfoDTOS, type)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    private fun resolveResultData(obj: String): AiResult? {
        var result: AiResult? = null
        try {
            val data = JSONObject(obj).getJSONObject("data")
            val userShopInfoDTOS = data.toString()
            val builder = GsonBuilder()
            builder.registerTypeAdapter(
                    Date::class.java,
                    JsonDeserializer { json, typeOfT, context -> Date(json.asJsonPrimitive.asLong) })
            val gson = builder.create()
            val type = object : TypeToken<AiResult?>() {}.type
            result = gson.fromJson(userShopInfoDTOS, type)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return result
    }

}