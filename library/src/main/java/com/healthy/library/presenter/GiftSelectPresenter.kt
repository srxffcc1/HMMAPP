package com.healthy.library.presenter

import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.reflect.TypeToken
import com.healthy.library.businessutil.LocUtil
import com.healthy.library.constant.Functions
import com.healthy.library.constant.SpKey
import com.healthy.library.contract.GiftSelectContract
import com.healthy.library.model.ShopDetailModel
import com.healthy.library.net.ObservableHelper
import com.healthy.library.net.StringObserver
import org.json.JSONException
import org.json.JSONObject
import java.util.*

/**
 * @description
 * @author long
 * @date 2021/9/16
 */
class GiftSelectPresenter(var context:Context,var mView:GiftSelectContract.View) : GiftSelectContract.Presenter {

    private var mContext: Context? = context
    private var isMore: Boolean = true

    override fun getShopList(map: MutableMap<String, Any>) {
        map[Functions.FUNCTION] = "101004-1"
        map["shopTypeList"] = "1,3".split(",").toTypedArray()
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
                    mView.onRequestFinish()
                }
            })
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