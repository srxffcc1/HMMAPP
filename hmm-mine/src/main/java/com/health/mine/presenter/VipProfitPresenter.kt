package com.health.mine.presenter

import android.content.Context
import android.util.Base64
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.reflect.TypeToken
import com.health.mine.contract.VipProfitContract
import com.healthy.library.constant.Functions
import com.healthy.library.constant.SpKey
import com.healthy.library.model.VipProfitModel
import com.healthy.library.net.ObservableHelper
import com.healthy.library.net.StringObserver
import com.healthy.library.utils.SpUtils
import org.json.JSONObject
import java.util.*

/**
 * @Description: (描述)
 * @author:LiuWei
 * @date 2022/9/14
 * @version V1.0
 */
class VipProfitPresenter(var context: Context?, mView: VipProfitContract.View) :
    VipProfitContract.Presenter {

    private var mContext: Context? = context
    private var mView: VipProfitContract.View = mView


    override fun getVipProfitData() {
        var map = mutableMapOf<String, Any>()
        map[Functions.FUNCTION] = "allin_10001_account"
        map["memberId"] = String(
            Base64.decode(
                SpUtils.getValue(
                    mContext,
                    SpKey.USER_ID
                ).toByteArray(), Base64.DEFAULT
            )
        )
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : StringObserver(mView, mContext, false) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    mView.getVipProfitDataSuccess(resolveRefreshDetailsData(obj))
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

    override fun getProfitList(map: MutableMap<String, Any>) {
        map[Functions.FUNCTION] = "allin_10001_account_list"
        map["memberId"] = String(
            Base64.decode(
                SpUtils.getValue(
                    mContext,
                    SpKey.USER_ID
                ).toByteArray(), Base64.DEFAULT
            )
        )
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : StringObserver(mView, mContext, false) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    mView.getProfitListSuccess(resolveListData(obj), resolveRefreshData(obj))
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

    override fun identity(name: String, identityNo: String) {
        var map = mutableMapOf<String, Any>()
        map[Functions.FUNCTION] = "allin_account_setRealName"
        map["name"] = name
        map["identityNo"] = identityNo
        map["memberId"] = String(
            Base64.decode(
                SpUtils.getValue(
                    mContext,
                    SpKey.USER_ID
                ).toByteArray(), Base64.DEFAULT
            )
        )
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : StringObserver(mView, mContext, false) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    try {
                        mView.identitySuccess(JSONObject(obj).getString("msg"))
                    } catch (e: Exception) {
                    }
                }

                override fun onFinish() {
                    super.onFinish()
                    mView.onRequestFinish()
                }

                override fun onFailure(msg: String?) {
                    super.onFailure(msg)
                    try {
                        mView.showToast(msg)
                    } catch (e: Exception) {
                    }
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                }
            })
    }

    /**
     * 解析数据
     */
    fun resolveRefreshDetailsData(result: String): VipProfitModel? {
        var resultData: VipProfitModel? = null
        try {
            val jsonObject = JSONObject(result).getJSONObject("data")
            val userShopInfoDTOS = jsonObject.toString()
            val builder = GsonBuilder()
            builder.registerTypeAdapter(
                Date::class.java,
                JsonDeserializer { json, typeOfT, context -> Date(json.asJsonPrimitive.asLong) })
            val gson = builder.create()
            val type = object : TypeToken<VipProfitModel>() {}.type
            resultData = gson.fromJson(userShopInfoDTOS, type)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return resultData
    }

     fun resolveRefreshData(obj: String): Boolean {
        var isMore = false
        try {
            val data = JSONObject(obj).getJSONObject("data")
            isMore = data.optString("isMore") == "1"
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return isMore
    }
    /**
     * 解析数据
     */
    fun resolveListData(result: String): MutableList<VipProfitModel> {
        var resultData: MutableList<VipProfitModel> = mutableListOf()
        try {
            val jsonObject = JSONObject(result).getJSONObject("data")
            val data = jsonObject.getJSONArray("items")
            val userShopInfoDTOS = data.toString()
            val builder = GsonBuilder()
            builder.registerTypeAdapter(
                Date::class.java,
                JsonDeserializer { json, typeOfT, context -> Date(json.asJsonPrimitive.asLong) })
            val gson = builder.create()
            val type = object : TypeToken<List<VipProfitModel>>() {}.type
            resultData = gson.fromJson(userShopInfoDTOS, type)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return resultData
    }


}