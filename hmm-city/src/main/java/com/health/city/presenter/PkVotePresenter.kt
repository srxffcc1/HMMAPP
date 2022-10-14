package com.health.city.presenter

import android.content.Context
import android.text.TextUtils
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.reflect.TypeToken
import com.health.city.contract.PkVoteContract
import com.healthy.library.businessutil.ListUtil
import com.healthy.library.constant.Functions
import com.healthy.library.constant.SpKey
import com.healthy.library.model.Discuss
import com.healthy.library.net.NoStringObserver
import com.healthy.library.net.ObservableHelper
import com.healthy.library.net.StringObserver
import com.healthy.library.utils.SpUtils
import org.json.JSONObject
import java.util.*

/**
 * author : long
 * Time   :2021/11/15
 * desc   :
 */
class PkVotePresenter :
    PkVoteContract.Presenter {

    private var merchantId: String = ""
    private var mContext: Context? = null
    private var mView: PkVoteContract.View? = null
    var mNetWorkCode = 0

    constructor(merchantId: String?, mContext: Context, mView: PkVoteContract.View) {
        this.merchantId = if (TextUtils.isEmpty(merchantId)) {
            SpUtils.getValue(mContext, SpKey.CHOSE_MC)
        } else {
            merchantId!!
        }
        this.mContext = mContext
        this.mView = mView
    }

    override fun addPkVote(map: MutableMap<String, Any>) {
        map[Functions.FUNCTION] = "pk_1000"
        map["merchantId"] = merchantId
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : StringObserver(mView, mContext, false) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    mView?.onAddPkVoteSuccess()
                }

                override fun onFailure() {
                    super.onFailure()
                }
            })
    }

    override fun addDiscuss(map: MutableMap<String, Any>) {
        map[Functions.FUNCTION] = "7010"
        map["merchantId"] = merchantId
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : StringObserver(mView, mContext, true, true, true, false) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    try {
                        mNetWorkCode = JSONObject(obj).optInt("code")
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                    SpUtils.store(mContext, SpKey.DISCUSS_TMP, "")
                    mView?.onSuccessAdd()
                }

                override fun onGetResultFail(obj: String) {
                    super.onGetResultFail(obj)
                    try {
                        mNetWorkCode = JSONObject(obj).optInt("code")
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                    mView?.onSuccessAdd()
                }

                override fun onFailure() {
                    super.onFailure()
                    //                        mView.onGetStoreDetailFail();
                }
            })
    }

    override fun getCommentList(map: MutableMap<String, Any>) {
        map[Functions.FUNCTION] = "N_7012"
        map["merchantId"] = merchantId
        map["pageSize"] = "10"
        map["type"] = "1"
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : NoStringObserver(mView, mContext, false) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    var isMore = false
                    try {
                        isMore = "1" == JSONObject(obj).getJSONObject("data").getString("isMore")

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    mView?.onGetCommentListSuccess(resolveListData(obj), isMore)
                }

                override fun onFailure() {
                    super.onFailure()
                }
            })
    }

    override fun getCommentReply(map: MutableMap<String, Any>, discuss: Discuss, forSize: Int) {
        map[Functions.FUNCTION] = "reply_7016"
        map["merchantId"] = merchantId
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : NoStringObserver(mView, mContext, false) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    val resolveReplyListData = resolveReplyListData(obj)
                    if("1" == map["currentPage"].toString()) {
                        discuss.discussReplyList = resolveReplyListData
                    } else {
                        if(ListUtil.isEmpty(discuss.discussReplyList)){
                            discuss.discussReplyList = mutableListOf()
                        }
                        discuss.discussReplyList.addAll(resolveReplyListData)
                    }
                    try{
                        val jsonObject = JSONObject(obj).getJSONObject("data")
                        discuss.isMore = "1" == jsonObject.optString("isMore")
                        discuss.totalNum = jsonObject.optInt("totalNum")
                        discuss.isShow = !discuss.isMore
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    mView?.onGetCommentReplyListSuccess(forSize)
                }

                override fun onFailure() {
                    super.onFailure()
                }
            })
    }

    /**
     * 解析数据
     */
    private fun resolveListData(obj: String): MutableList<Discuss> {
        var result: MutableList<Discuss> = mutableListOf()
        try {
            val data = JSONObject(obj).getJSONObject("data").getJSONArray("items")
            val userShopInfoDTOS = data.toString()
            val builder = GsonBuilder()
            builder.registerTypeAdapter(
                Date::class.java,
                JsonDeserializer { json, typeOfT, context -> Date(json.asJsonPrimitive.asLong) })
            val gson = builder.create()
            val type = object : TypeToken<List<Discuss>>() {}.type
            result = gson.fromJson(userShopInfoDTOS, type)
        } catch (e: Exception) {
            //System.out.println("错误:"+obj);
            e.printStackTrace()
        }
        return result
    }

    /**
     * 解析数据
     */
    private fun resolveReplyListData(obj: String): MutableList<Discuss.DiscussReply> {
        var result: MutableList<Discuss.DiscussReply> = mutableListOf()
        try {
            val data = JSONObject(obj).getJSONObject("data").getJSONArray("items")
            val userShopInfoDTOS = data.toString()
            val builder = GsonBuilder()
            builder.registerTypeAdapter(
                Date::class.java,
                JsonDeserializer { json, typeOfT, context -> Date(json.asJsonPrimitive.asLong) })
            val gson = builder.create()
            val type = object : TypeToken<MutableList<Discuss.DiscussReply>>() {}.type
            result = gson.fromJson(userShopInfoDTOS, type)
        } catch (e: Exception) {
            //System.out.println("错误:"+obj);
            e.printStackTrace()
        }
        return result
    }

}