package com.health.mine.presenter

import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.reflect.TypeToken
import com.health.mine.contract.EnlistContract
import com.healthy.library.constant.Constants
import com.healthy.library.constant.Functions
import com.healthy.library.model.EnlistActivityModel
import com.healthy.library.net.ObservableHelper
import com.healthy.library.net.StringObserver
import com.healthy.library.utils.JsonUtils
import org.json.JSONException
import org.json.JSONObject
import java.util.*

/**
 * @description
 * @author long
 * @date 2021/6/30
 */
class EnListPresenter(var context: Context?, private var mView: EnlistContract.View) :
    EnlistContract.Presenter {

    private var mContext: Context? = context
    private var isMore: Boolean = true

    /**
     * 获取报名活动列表
     * @param map 接口所需参数
     */
    override fun getEnList(map: MutableMap<String, Any>) {
        map[Functions.FUNCTION] = "t_100102"
        map["pageSize"] = "5"
        //map["memberId"] = SpUtils.getValue(mContext, SpKey.USER_ID)
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : StringObserver(mView, mContext, false) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    mView.getEnListSuccess(resolveRefreshData(obj), isMore)
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
     * 获取报名活动详情数据
     * @param map 接口所需参数
     */
    override fun getEnlistDetails(map: MutableMap<String, Any>) {
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : StringObserver(mView, mContext, false) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    mView.getEnlistDetailsSuccess(resolveRefreshDetailsData(obj))
                }

                override fun onFinish() {
                    super.onFinish()
                }

                override fun onFailure() {
                    super.onFailure()
                    mView.onRequestFinish()
                }
            })
    }

    /**
     * 新增报名信息
     */
    override fun addEnlist(map: MutableMap<String, Any>) {
        map[Functions.FUNCTION] = "t_100101-1"
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : StringObserver(mView, mContext, false) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    var id = ""
                    var payOrderId = ""
                    try {
                        id = JSONObject(obj).getJSONObject("data").getString("id")
                        payOrderId = JSONObject(obj).getJSONObject("data").getString("payOrderId")
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    mView.addEnlistSuccess(id, payOrderId)
                }

                override fun onFinish() {
                    super.onFinish()
                }

                override fun onFailure() {
                    super.onFailure()
                    mView.onRequestFinish()
                }
            })
    }

    /**
     * 取消报名
     */
    override fun removeEnlist(map: MutableMap<String, Any>) {
        map[Functions.FUNCTION] = "t_100105"
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : StringObserver(mView, mContext, false) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    mView.removeEnlistSuccess()
                }

                override fun onFinish() {
                    super.onFinish()
                }

                override fun onFailure() {
                    super.onFailure()
                    mView.onRequestFinish()
                }
            })
    }

    override fun getPayInfo(map: MutableMap<String, Any>) {
//        map[Functions.FUNCTION] = "t_100108"
        map[Functions.FUNCTION] = "25001-2"
        map["payConfigKey"] = "app"// 使用的支付配置：app:App支付 hmz:憨妈赚支付 hg:同城憨购支付 applet:憨妈购支付
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : StringObserver(mView, mContext, false) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    try {
                        mView.onGetPayInfoSuccess(
                            JSONObject(obj).optJSONObject("data").optString("payInfo")
                        )
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }

                override fun onFinish() {
                    super.onFinish()
                }

                override fun onFailure() {
                    super.onFailure()
                    mView.onRequestFinish()
                }
            })
    }
    override fun getPayStatus(payId: String) {
        val map: MutableMap<String, Any> = HashMap()
        map[Functions.FUNCTION] = "pay_1000" //通联支付状态: 1-未支付 3-交易失败 4-交易成功 5-交易成功-发生退款 6-关闭 99-进行中
        map["payId"] = payId
        ObservableHelper
            .createObservable(mContext, map)
            .subscribe(object : StringObserver(mView, mContext, false, false,false,false) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    try {
                        mView.getPayStatusSuccess(
                            JSONObject(obj).optJSONObject("data").optString("orderStatus")
                        )
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(msg: String) {
                    super.onFailure(msg)
                    mView.getPayStatusSuccess(null)
                }
            })
    }

    /**
     * 解析数据
     */
    fun resolveRefreshData(result: String): MutableList<EnlistActivityModel> {
        var resultData: MutableList<EnlistActivityModel> = mutableListOf()
        try {
            val jsonObject = JSONObject(result).getJSONObject("data")
            isMore = jsonObject.getBoolean("hasNextPage")
            val data = jsonObject.getJSONArray("list")
            val userShopInfoDTOS = data.toString()
            val builder = GsonBuilder()
            builder.registerTypeAdapter(
                Date::class.java,
                JsonDeserializer { json, typeOfT, context -> Date(json.asJsonPrimitive.asLong) })
            val gson = builder.create()
            val type = object : TypeToken<List<EnlistActivityModel>>() {}.type
            resultData = gson.fromJson(userShopInfoDTOS, type)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return resultData
    }

    /**
     * 解析数据
     */
    fun resolveRefreshDetailsData(result: String): EnlistActivityModel? {
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
        return resultData
    }

    /**
     * 解析支付信息
     *
     * @param obj 支付信息字符串
     * @return 支付信息map
     */
    private fun resolvePayInfo(obj: String): MutableMap<String, Any> {
        val payInfoMap: MutableMap<String, Any> = HashMap(5)
        try {
            var jsonObject: JSONObject? = JSONObject(obj)
            jsonObject = JsonUtils.getJsonObject(jsonObject, "data")
            val payType = JsonUtils.getString(jsonObject, "payType")
            payInfoMap["payType"] = payType
            if (Constants.PAY_IN_A_LI == payType) {
//                payInfoMap["ali"] = JsonUtils.getString(jsonObject, "aliPayContent")
                payInfoMap["payInfo"] = JsonUtils.getString(jsonObject, "payInfo")
            } else if (Constants.PAY_IN_WX == payType) {
                val wxObj = JSONObject(JsonUtils.getString(jsonObject, "payContent"))
                payInfoMap["partnerId"] = JsonUtils.getString(wxObj, "mch_id")
                payInfoMap["prepayId"] = JsonUtils.getString(wxObj, "prepay_id")
                payInfoMap["packageValue"] = "Sign=WXPay"
                payInfoMap["nonceStr"] = JsonUtils.getString(wxObj, "nonce_str")
                payInfoMap["timeStamp"] = JsonUtils.getString(wxObj, "timeStamp")
                payInfoMap["sign"] = JsonUtils.getString(wxObj, "sign")
                payInfoMap["appId"] = JsonUtils.getString(wxObj, "appid")
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return payInfoMap
    }
}