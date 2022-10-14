package com.health.mine.presenter

import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.reflect.TypeToken
import com.health.mine.contract.VoteListContract
import com.healthy.library.model.ActivityModel
import com.healthy.library.net.ObservableHelper
import com.healthy.library.net.StringObserver
import org.json.JSONObject
import java.util.*

/**
 * @description
 * @author long
 * @date 2021/6/30
 */
class VoteListPresenter(var context: Context?, mView: VoteListContract.View) :
    VoteListContract.Presenter {

    private var mContext: Context? = context
    private var mView: VoteListContract.View = mView
    private var isMore: Boolean? = true

    /**
     *参与的投票/报名的投票列表
     * @param map 接口所需参数
     */
    override fun getVoteList(map: MutableMap<String, Any>) {
        map["pageSize"] = "5"
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : StringObserver(mView, mContext, false) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    mView.getVoteListSuccess(resolveRefreshData(obj), isMore!!)
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
    fun resolveRefreshData(result: String): MutableList<ActivityModel> {
        var resultData: MutableList<ActivityModel> = mutableListOf()
        try {
            val jsonObject = JSONObject(result).getJSONObject("data")
            isMore = jsonObject.getInt("isMore") == 1
            val data = jsonObject.getJSONArray("items")
            val userShopInfoDTOS = data.toString()
            val builder = GsonBuilder()
            builder.registerTypeAdapter(
                Date::class.java,
                JsonDeserializer { json, typeOfT, context -> Date(json.asJsonPrimitive.asLong) })
            val gson = builder.create()
            val type = object : TypeToken<List<ActivityModel>>() {}.type
            resultData = gson.fromJson(userShopInfoDTOS, type)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return resultData
    }


}