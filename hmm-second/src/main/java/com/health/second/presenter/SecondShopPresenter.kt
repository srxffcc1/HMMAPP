package com.health.second.presenter

import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.reflect.TypeToken
import com.health.second.contract.SecondShopContract
import com.healthy.library.businessutil.LocUtil
import com.healthy.library.constant.Functions
import com.healthy.library.constant.SpKey
import com.healthy.library.model.ShopDetailModel
import com.healthy.library.net.NoStringObserver
import com.healthy.library.net.ObservableHelper
import com.healthy.library.net.StringObserver
import org.json.JSONException
import org.json.JSONObject
import java.util.*

/**
 * @author Long
 * @desc:
 * @createTime :2021/10/14 13:48
 */
class SecondShopPresenter(var mContext: Context, var mView: SecondShopContract.View) :
    SecondShopContract.Presenter {

    /**
     * 获取门店详情数据
     */
    /*override fun getShopDetail(map: MutableMap<String, Any>) {
        map[Functions.FUNCTION] = "101001"
        map["longitude"] = LocUtil.getLongitude(mContext, SpKey.LOC_ORG)
        map["latitude"] = LocUtil.getLatitude(mContext, SpKey.LOC_ORG)
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : StringObserver(mView, mContext, false) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    mView.onGetShopDetailSuccess(resolveStoreData(obj))
                }

                override fun onFailure() {
                    super.onFailure()
                }
            })
    }*/

    /**
     * 获取门店列表数据
     */
    override fun getShopList(map: MutableMap<String, Any>) {
        map[Functions.FUNCTION] = "101004-1"
        map["longitude"] = LocUtil.getLongitude(mContext, SpKey.LOC_ORG)
        map["latitude"] = LocUtil.getLatitude(mContext, SpKey.LOC_ORG)
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : StringObserver(mView, mContext, false) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    mView.onGetShopListSuccess(resolveStoreListData(obj))
                }

                override fun onFailure() {
                    super.onFailure()
                    mView.onGetShopListSuccess(null)
                }

                override fun onFinish() {
                    super.onFinish()
                    mView.onRequestFinish()
                }
            })
    }

    override fun getShopDetailOnly(map: MutableMap<String, Any>) {
        map[Functions.FUNCTION] = "101001"
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : NoStringObserver(mView, mContext, false) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    mView.onSucessGetShopDetailOnly(resolveStoreData(obj))
                }

                override fun onFailure() {
                    super.onFailure()
                    mView.onSucessGetShopDetailOnly(null)
                }

                override fun onFinish() {
                    super.onFinish()
                    mView.onRequestFinish()
                }
            })
    }

    private fun resolveStoreData(obj: String): ShopDetailModel? {
        var shopDetailModel: ShopDetailModel? = null
        try {
            val data = JSONObject(obj).getJSONObject("data")
            val userShopInfoDTOS = data.toString()
            val builder = GsonBuilder()
            builder.registerTypeAdapter(
                Date::class.java,
                JsonDeserializer { json, typeOfT, context -> Date(json.asJsonPrimitive.asLong) })
            val gson = builder.create()
            val type = object : TypeToken<ShopDetailModel?>() {}.type
            shopDetailModel = gson.fromJson(userShopInfoDTOS, type)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return shopDetailModel
    }

    private fun resolveStoreListData(obj: String): MutableList<ShopDetailModel> {
        var newStoreDetialModel = mutableListOf<ShopDetailModel>()
        try {
            val data = JSONObject(obj).getJSONArray("data")
            val userShopInfoDTOS = data.toString()
            val builder = GsonBuilder()
            builder.registerTypeAdapter(
                Date::class.java,
                JsonDeserializer { json, typeOfT, context -> Date(json.asJsonPrimitive.asLong) })
            val gson = builder.create()
            val type = object : TypeToken<MutableList<ShopDetailModel>>() {}.type
            newStoreDetialModel = gson.fromJson(userShopInfoDTOS, type)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return newStoreDetialModel
    }
}