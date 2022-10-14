package com.health.servicecenter.presenter

import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.reflect.TypeToken
import com.health.servicecenter.contract.LotteryContract
import com.health.servicecenter.model.PointsSignInModel
import com.healthy.library.constant.Functions
import com.healthy.library.model.LotteryInfoModel
import com.healthy.library.net.ObservableHelper
import com.healthy.library.net.StringObserver
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception
import java.util.*

class LotteryPresenter(private val mContext: Context, private val mView: LotteryContract.View) :
    LotteryContract.Presenter {

    override fun getLotteryInfo(map: MutableMap<String, Any>) {
        map[Functions.FUNCTION] = "lottery_6005"
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : StringObserver(mView, mContext, false) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    mView.onLotteryInfoSuccess(resolveLotteryData(obj))
                }

                override fun onFailure() {
                    super.onFailure()
                    mView.onLotteryInfoSuccess(null)
                }

                override fun onFinish() {
                    super.onFinish()
                    mView.onRequestFinish()
                }
            })
    }

    override fun checkLottery(map: MutableMap<String, Any>) {
        map[Functions.FUNCTION] = "lottery_6006"
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : StringObserver(mView, mContext, false) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    var triggerPrivilege = 0
                    try {
                        triggerPrivilege =
                            JSONObject(obj).optJSONObject("data").optInt("triggerPrivilege")
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    mView.onCheckLotterySuccess(triggerPrivilege)
                }

                override fun onFinish() {
                    super.onFinish()
                    mView.onRequestFinish()
                }
            })
    }

    private fun resolveLotteryData(obj: String): LotteryInfoModel? {
        var result: LotteryInfoModel? = null
        try {
            val data = JSONObject(obj).getJSONObject("data")
            val userShopInfoDTOS = data.toString()
            val builder = GsonBuilder()
            builder.registerTypeAdapter(
                Date::class.java,
                JsonDeserializer { json, typeOfT, context -> Date(json.asJsonPrimitive.asLong) })
            val gson = builder.create()
            val type = object : TypeToken<LotteryInfoModel>() {}.type
            result = gson.fromJson<LotteryInfoModel>(userShopInfoDTOS, type)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return result
    }

}