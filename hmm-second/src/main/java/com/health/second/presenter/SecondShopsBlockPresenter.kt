package com.health.second.presenter

import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.reflect.TypeToken
import com.health.second.contract.SecondShopsBlockContract
import com.healthy.library.constant.Functions
import com.healthy.library.constant.SpKey
import com.healthy.library.model.MainBlockDetailModel
import com.healthy.library.model.MainBlockModel
import com.healthy.library.model.PageInfoEarly
import com.healthy.library.net.ObservableHelper
import com.healthy.library.net.StringObserver
import com.healthy.library.utils.SpUtils
import org.json.JSONException
import org.json.JSONObject
import java.util.*

/**
 * @desc
 * @author long
 * @createTime
 */
class SecondShopsBlockPresenter(var mContext: Context, var mView: SecondShopsBlockContract.View) :
    SecondShopsBlockContract.Presenter {


    override fun getBlockList(map: MutableMap<String, Any>) {
        map[Functions.FUNCTION] = "101007"
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : StringObserver(mView, mContext, false) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    mView.onSuccessGetBlockList(resolveBlockListData(obj))
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

    override fun getGoodsList(
        map: MutableMap<String, Any>, itemOrg: MainBlockModel,
        itemDes: MainBlockModel.AllianceMerchant,
        position: Int
    ) {
        map[Functions.FUNCTION] = "101008"
        map["shopId"] = SpUtils.getValue(mContext, SpKey.CHOSE_SHOP)
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : StringObserver(mView, mContext, false) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    itemDes.detailList = resolveBlockDetailData(obj)
                    mView.onSuccessGetList(itemOrg, position)
                }

                override fun onFinish() {
                    super.onFinish()
                    mView.onRequestFinish()
                }

                override fun onFailure() {
                    super.onFailure()
                    itemDes.detailList = mutableListOf()
                    mView.onSuccessGetList(itemOrg, position)
                }
            })
    }

    private fun resolveBlockListData(obj: String): MutableList<MainBlockModel>? {
        var result: MutableList<MainBlockModel>? = null
        try {
            val data = JSONObject(obj).getJSONArray("data")
            val userShopInfoDTOS = data.toString()
            val builder = GsonBuilder()
            builder.registerTypeAdapter(
                Date::class.java,
                JsonDeserializer { json, typeOfT, context -> Date(json.asJsonPrimitive.asLong) })
            val gson = builder.create()
            val type = object : TypeToken<MutableList<MainBlockModel>?>() {}.type
            result = gson.fromJson(userShopInfoDTOS, type)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return result
    }

    private fun resolveBlockDetailData(obj: String): MutableList<MainBlockDetailModel> {
        var result: MutableList<MainBlockDetailModel> = ArrayList()
        try {
            val data = JSONObject(obj).getJSONObject("data").getJSONArray("list")
            val userShopInfoDTOS = data.toString()
            val builder = GsonBuilder()
            builder.registerTypeAdapter(
                Date::class.java,
                JsonDeserializer { json, typeOfT, context -> Date(json.asJsonPrimitive.asLong) })
            val gson = builder.create()
            val type = object : TypeToken<MutableList<MainBlockDetailModel>>() {}.type
            result = gson.fromJson(userShopInfoDTOS, type)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return result
    }

}