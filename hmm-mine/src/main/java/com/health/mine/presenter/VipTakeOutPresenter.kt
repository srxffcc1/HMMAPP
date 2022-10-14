package com.health.mine.presenter

import android.app.Activity
import android.content.Context
import android.util.Base64
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.reflect.TypeToken
import com.health.mine.contract.VipTakeOutContract
import com.healthy.library.constant.Functions
import com.healthy.library.constant.SpKey
import com.healthy.library.model.BankCardModel
import com.healthy.library.model.VipProfitModel
import com.healthy.library.net.ObservableHelper
import com.healthy.library.net.StringObserver
import com.healthy.library.utils.LogUtils
import com.healthy.library.utils.SpUtils
import org.json.JSONObject
import java.util.*

/**
 * @Description: (描述)
 * @author:LiuWei
 * @date 2022/9/14
 * @version V1.0
 */
class VipTakeOutPresenter(var context: Context?, mView: VipTakeOutContract.View) :
    VipTakeOutContract.Presenter {

    private var mContext: Context? = context
    private var mView: VipTakeOutContract.View = mView
    private var isMore: Boolean? = true

    /**
     * 解析数据
     */
    fun resolveRefreshDetailsData(result: String): BankCardModel? {
        var resultData: BankCardModel? = null
        try {
            val jsonObject = JSONObject(result).getJSONObject("data").getJSONObject("cardBinInfo")
            val userShopInfoDTOS = jsonObject.toString()
            val builder = GsonBuilder()
            builder.registerTypeAdapter(
                Date::class.java,
                JsonDeserializer { json, typeOfT, context -> Date(json.asJsonPrimitive.asLong) })
            val gson = builder.create()
            val type = object : TypeToken<BankCardModel>() {}.type
            resultData = gson.fromJson(userShopInfoDTOS, type)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return resultData
    }

    /**
     * 解析数据
     */
    fun resolveRefreshData(result: String): MutableList<BankCardModel> {
        var resultData: MutableList<BankCardModel> = mutableListOf()
        try {
            val jsonObject = JSONObject(result).getJSONArray("data")
            val userShopInfoDTOS = jsonObject.toString()
            val builder = GsonBuilder()
            builder.registerTypeAdapter(
                Date::class.java,
                JsonDeserializer { json, typeOfT, context -> Date(json.asJsonPrimitive.asLong) })
            val gson = builder.create()
            val type = object : TypeToken<List<BankCardModel>>() {}.type
            resultData = gson.fromJson(userShopInfoDTOS, type)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return resultData
    }

    override fun getSignContract() {
        var map = mutableMapOf<String, Any>()
        map[Functions.FUNCTION] = "allin_account_signContract"
        map["source"] = 1
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
                        mView.getSignContractSuccess(
                            JSONObject(obj).getJSONObject("data").getString("url")
                        )
                    } catch (e: Exception) {
                    }
                }

                override fun onFinish() {
                    super.onFinish()
                    mView.onRequestFinish()
                }
            })
    }

    override fun getBankCardList() {
        var map = mutableMapOf<String, Any>()
        map[Functions.FUNCTION] = "allin_account_queryBankCard"
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
                    mView.getBankCardListSuccess(resolveRefreshData(obj))
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


    override fun bindBankCard(map: MutableMap<String, Any>) {
        map[Functions.FUNCTION] = "allin_account_applyBindBankCard"
        map["memberId"] = String(
            Base64.decode(
                SpUtils.getValue(
                    mContext,
                    SpKey.USER_ID
                ).toByteArray(), Base64.DEFAULT
            )
        )
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : StringObserver(mView, mContext, true) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    mView.bindBankCardSuccess()
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

    override fun withdrawApply(map: MutableMap<String, Any>) {
        map[Functions.FUNCTION] = "allin_account_withdrawApply"
        map["validateType"] = 1
        map["source"] = 1
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
                        mView.withdrawApplySuccess(
                            JSONObject(obj).getJSONObject("data").getString("orderNo"),JSONObject(obj).getJSONObject("data").getString("bizOrderNo")
                        )
                    } catch (e: Exception) {
                    }
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

    override fun payByBackSMS(map: MutableMap<String, Any>) {
        map[Functions.FUNCTION] = "allin_account_payByBackSMS"
        map["vtype"] = 1
        map["memberId"] = String(
            Base64.decode(
                SpUtils.getValue(
                    mContext,
                    SpKey.USER_ID
                ).toByteArray(), Base64.DEFAULT
            )
        )
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : StringObserver(mView, mContext, true) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    LogUtils.e(obj)
                    mView.payByBackSMSSuccess("成功")
                }

                override fun onFinish() {
                    super.onFinish()
                    mView.onRequestFinish()
                }

                override fun onFailure() {
                    super.onFailure()
                    mView.payByBackSMSSuccess("失败")
                }

                override fun onFailure(msg: String?) {
                    super.onFailure(msg)
                    mView.payByBackSMSSuccess("失败")
                }

                override fun onFailure(msg: String?, obj: String?) {
                    super.onFailure(msg, obj)
                    mView.payByBackSMSSuccess("失败")
                }

                override fun onGetResultFail(result: String?) {
                    super.onGetResultFail(result)
                    mView.payByBackSMSSuccess("失败")
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    mView.payByBackSMSSuccess("失败")
                }
            })
    }

    override fun getBankCardBin(cardNo: String) {
        var map = mutableMapOf<String, Any>()
        map[Functions.FUNCTION] = "allin_account_getBankCardBin"
        map["cardNo"] = cardNo
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : StringObserver(mView, mContext, false,true,true,true) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    mView.getBankCardBinSuccess(resolveRefreshDetailsData(obj))
                }

                override fun onFinish() {
                    super.onFinish()
                    mView.onRequestFinish()
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    mView.getBankCardBinSuccess(null)
                }
                override fun onFailure() {
                    super.onFailure()
                    mView.getBankCardBinSuccess(null)
                }
            })
    }

    override fun unbindBankCard(cardNo: String,activity:Activity) {
        var map = mutableMapOf<String, Any>()
        map[Functions.FUNCTION] = "allin_account_unbindBankCard"
        map["cardNo"] = cardNo
        map["memberId"] = String(
            Base64.decode(
                SpUtils.getValue(
                    mContext,
                    SpKey.USER_ID
                ).toByteArray(), Base64.DEFAULT
            )
        )
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : StringObserver(mView, mContext, true) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    activity.finish()
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


}