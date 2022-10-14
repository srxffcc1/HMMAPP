package com.health.city.presenter

import android.content.Context
import android.os.Handler
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.reflect.TypeToken
import com.health.city.contract.EssencesContract
import com.healthy.library.businessutil.ListUtil
import com.healthy.library.constant.Functions
import com.healthy.library.constant.SpKey
import com.healthy.library.model.PostActivityList
import com.healthy.library.model.PostDetail
import com.healthy.library.net.ObservableHelper
import com.healthy.library.net.StringObserver
import com.healthy.library.utils.SpUtils
import org.json.JSONException
import org.json.JSONObject
import java.util.*

/**
 * author : long
 * Time   :2021/11/9
 * desc   :
 */
class EssencesPresenter(var mContext: Context, var mView :EssencesContract.View) : EssencesContract.Presenter {

    var ptype = 0

    override fun getPostingList(map: MutableMap<String, Any>) {
        map[Functions.FUNCTION] = "7000-tag"
        map["pageSize"] = "10"
        map["postingTag"] = "1"
        map["merchantId"] = SpUtils.getValue(mContext, SpKey.CHOSE_MC)
        map["shopId"] = SpUtils.getValue(mContext, SpKey.CHOSE_SHOP)
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : StringObserver(mView, mContext, false) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    val resolvePostListData = resolvePostListData(obj)
                    if("1" == map["currentPage"].toString() && !ListUtil.isEmpty(resolvePostListData)){
                        resolvePostListData!![0].isFirst = true
                    }

                    mView.onQueryPostListSuccess(resolvePostListData, resolveRefreshData(obj))
                }

                override fun onFinish() {
                    super.onFinish()
                    mView.onRequestFinish()
                }
            })
    }

    override fun like(map: MutableMap<String, Any>) {
        map[Functions.FUNCTION] = "7003"
        map["merchantId"] = SpUtils.getValue(mContext, SpKey.CHOSE_MC)
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : StringObserver(mView, mContext, false) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    if ("0" == map["type"]) {
                        mView.showToast("点赞成功")
                    }
                    mView.onSuccessLike()
                }

                override fun onFinish() {
                    super.onFinish()
                }

                override fun onFailure() {
                    super.onFailure()
                }
            })
    }

    override fun fan(map: MutableMap<String, Any>) {
        map[Functions.FUNCTION] = "7018"
        map["merchantId"] = SpUtils.getValue(mContext, SpKey.CHOSE_MC)
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : StringObserver(mView, mContext, false) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    if ("0" == map["type"]) {
                        if (ptype == 0) {
                            mView.showToast("关注成功")
                        } else {
                            mView.showToast("关注成功,可刷新查看关注内容")
                        }
                    } else {
                        Handler().postDelayed({
                            mView.showToast("取消关注成功")
                        }, 100)
                    }
                    mView.onSuccessFan(map["type"])
                }

                override fun onFinish() {
                    super.onFinish()
                }

                override fun onFailure() {
                    super.onFailure()
                }
            })
    }

    override fun getActivityList(map: MutableMap<String, Any>, postDetail: PostDetail?) {
        map[Functions.FUNCTION] = "p_70022"
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : StringObserver(mView, mContext, false) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    postDetail!!.postActivityList = resolveActivityListData(obj)
                    mView.onSuccessGetActivityList()
                }

                override fun onFinish() {
                    super.onFinish()
                }

                override fun onFailure() {
                    super.onFailure()
                }
            })
    }

    override fun warn(map: MutableMap<String, Any>) {
        map[Functions.FUNCTION] = "7017"
        map["merchantId"] = SpUtils.getValue(mContext,SpKey.CHOSE_MC)
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : StringObserver(mView, mContext, true) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                }

                override fun onFinish() {
                    super.onFinish()
                }

                override fun onFailure() {
                    super.onFailure()
                }
            })
    }

    private fun resolvePostListData(obj: String): MutableList<PostDetail>? {
        var result: MutableList<PostDetail>? = null
        try {
            val data = JSONObject(obj).getJSONObject("data").getJSONArray("items")
            val userShopInfoDTOS = data.toString()
            val builder = GsonBuilder()
            builder.registerTypeAdapter(
                Date::class.java,
                JsonDeserializer { json, typeOfT, context -> Date(json.asJsonPrimitive.asLong) })
            val gson = builder.create()
            val type = object : TypeToken<MutableList<PostDetail>>() {}.type
            result = gson.fromJson(userShopInfoDTOS, type)
        } catch (e: java.lang.Exception) {
            //System.out.println("错误:" + obj);
            e.printStackTrace()
        }
        return result
    }

    private fun resolveActivityListData(obj: String): MutableList<PostActivityList>? {
        var result: MutableList<PostActivityList>? = null
        try {
            val data = JSONObject(obj).getJSONArray("data")
            val userShopInfoDTOS = data.toString()
            val builder = GsonBuilder()
            builder.registerTypeAdapter(
                Date::class.java,
                JsonDeserializer { json, typeOfT, context -> Date(json.asJsonPrimitive.asLong) })
            val gson = builder.create()
            val type = object : TypeToken<List<PostActivityList>>() {}.type
            result = gson.fromJson<MutableList<PostActivityList>>(userShopInfoDTOS, type)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return result
    }

    private fun resolveRefreshData(obj: String): Boolean {
        var result: Boolean = false
        try {
            val data = JSONObject(obj).getJSONObject("data")
            result = data.getString("isMore") == "1"
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return result
    }

}