package com.health.index.presenter

import android.content.Context
import android.os.Handler
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.reflect.TypeToken
import com.health.index.contract.IndexMonthlyContract
import com.health.index.model.ActivityInfo
import com.health.index.model.IndexAllSee
import com.healthy.library.businessutil.ListUtil
import com.healthy.library.constant.Functions
import com.healthy.library.constant.SpKey
import com.healthy.library.model.*
import com.healthy.library.net.NoStringObserver
import com.healthy.library.net.ObservableHelper
import com.healthy.library.net.StringObserver
import com.healthy.library.utils.SpUtils
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class IndexMonthlyPresenter(
    private val mContext: Context,
    private val mView: IndexMonthlyContract.View
) : IndexMonthlyContract.Presenter {

    var ptype = 0

    /**
     * 获取帖子搜索列表
     */
    override fun queryPostList(map: MutableMap<String, Any>) {
        map[Functions.FUNCTION] = "7000"
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : StringObserver(mView, mContext, false) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    val resolvePostListData = resolvePostListData(obj)
                    if ("1" == map["currentPage"].toString() && !ListUtil.isEmpty(
                            resolvePostListData
                        )
                    ) {
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

    /**
     * 获取憨妈课堂视频搜索列表
     */
    override fun queryHmmVideoList(map: MutableMap<String, Any>) {
        map[Functions.FUNCTION] = "8096-list"
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : StringObserver(mView, mContext, false) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    mView.onQueryHmmVideoListSuccess(resolveListData(obj), false)
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
        map["merchantId"] = SpUtils.getValue(mContext, SpKey.CHOSE_MC)
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

    private fun resolveStringList(s: String): MutableList<SearchRecordsModel>? {
        var list: MutableList<SearchRecordsModel>? = null
        try {
            val data = JSONObject(s).getJSONObject("data").getJSONArray("items")
            val userShopInfoDTOS = data.toString()
            val builder = GsonBuilder()
            builder.registerTypeAdapter(
                Date::class.java,
                JsonDeserializer { json, typeOfT, context -> Date(json.asJsonPrimitive.asLong) })
            val gson = builder.create()
            val type = object : TypeToken<MutableList<SearchRecordsModel>?>() {}.type
            list = gson.fromJson(userShopInfoDTOS, type)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
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

    private fun resolveListData(s: String): MutableList<VideoListModel>? {
        var list: MutableList<VideoListModel>? = null
        try {
            val data = JSONObject(s).getJSONArray("data")
            val userShopInfoDTOS = data.toString()
            val builder = GsonBuilder()
            builder.registerTypeAdapter(
                Date::class.java,
                JsonDeserializer { json, typeOfT, context -> Date(json.asJsonPrimitive.asLong) })
            val gson = builder.create()
            val type = object : TypeToken<MutableList<VideoListModel>>() {}.type
            list = gson.fromJson(userShopInfoDTOS, type)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
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
            try {
                val data = JSONObject(obj).getJSONObject("data").getJSONObject("listInfo")
                result = data.getString("isMore") == "1"
            } catch (e: Exception) {
            }
            e.printStackTrace()
        }
        return result
    }

    private fun resolveGoodsRefreshData(obj: String): Boolean {
        var result: Boolean = false
        try {
            val data = JSONObject(obj).getJSONObject("data")
            result = data.getInt("nextPage") > 0
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return result
    }

    override fun queryGoodsList(map: MutableMap<String, Any>) {
        map["function"] = "9119"
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : StringObserver(
                mView, mContext,
                false
            ) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    mView.onQueryGoodsListSuccess(
                        resolveGoodsData(obj),
                        resolveGoodsRefreshData(obj)
                    )
                }

                override fun onFailure() {
                    super.onFailure()
                }

                override fun onFinish() {
                    super.onFinish()
                }
            })
    }

    override fun addPraise(map: MutableMap<String, Any>, type: Int) {
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : NoStringObserver(
                mView, mContext,
                false
            ) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    try {
                        mView.onAddPraiseSuccess(JSONObject(obj).optString("data"), type)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure() {
                    super.onFailure()
                }

                override fun onFinish() {
                    super.onFinish()
                }
            })
    }

    override fun queryQuestionList(map: MutableMap<String, Any>) {
        map["function"] = "5071"
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : NoStringObserver(
                mView, mContext,
                false
            ) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    mView.onQueryQuestionListSuccess(
                        resolveQuestionData(obj),
                        false
                    )
                }

                override fun onFailure() {
                    super.onFailure()
                }

                override fun onFinish() {
                    super.onFinish()
                }
            })
    }

    override fun getAllQuestion(map: MutableMap<String, Any>) {
        map["function"] = "4035"
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : NoStringObserver(
                mView, mContext,
                false
            ) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    mView.onGetAllQuestionListSuccess(resolveQuestionAllData(obj))
                }

                override fun onFailure() {
                    super.onFailure()
                }

                override fun onFinish() {
                    super.onFinish()
                }
            })
    }

    override fun getActivityInfo(map: MutableMap<String, Any>) {
        map["function"] = "7007-month"
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : NoStringObserver(
                mView, mContext,
                false
            ) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    mView.onGetActivityInfoSuccess(resolveResultData(obj),0)
                }

                override fun onFailure() {
                    super.onFailure()
                    mView.onGetActivityInfoSuccess(null,-1)
                }

                override fun onFinish() {
                    super.onFinish()
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    mView.onGetActivityInfoSuccess(null,-1)
                }
            })
    }

    override fun getTipPost(map: MutableMap<String, Any>) {
        map["function"] = "7007"
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : NoStringObserver(
                mView, mContext,
                false
            ) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    mView.onSuccessGetTipPost(resolveTipPostData(obj))
                }

                override fun onFailure() {
                    super.onFailure()
                }

                override fun onFinish() {
                    super.onFinish()
                }
            })
    }

    override fun getPKPostDetail(map: MutableMap<String, Any>) {
        map["function"] = "new_7002"
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : NoStringObserver(
                mView, mContext,
                false
            ) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    mView.onSuccessGetPKPostDetail(resolveDetailData(obj))
                }

                override fun onFailure() {
                    super.onFailure()
                }

                override fun onFinish() {
                    super.onFinish()
                }
            })

    }

    override fun addPkVote(map: MutableMap<String, Any>) {
        map[Functions.FUNCTION] = "pk_1000"
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

    private fun resolveDetailData(obj: String): PostDetail? {
        var result = PostDetail()
        try {
            val data = JSONObject(obj).getJSONObject("data")
            val userShopInfoDTOS = data.toString()
            val builder = GsonBuilder()
            builder.registerTypeAdapter(
                Date::class.java,
                JsonDeserializer { json, typeOfT, context -> Date(json.asJsonPrimitive.asLong) })
            val gson = builder.create()
            val type = object : TypeToken<PostDetail?>() {}.type
            result = gson.fromJson(userShopInfoDTOS, type)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return result
    }

    private fun resolveTipPostData(obj: String): TipPost? {
        var result = TipPost()
        try {
            val data = JSONObject(obj).getJSONObject("data")
            val userShopInfoDTOS = data.toString()
            val builder = GsonBuilder()
            builder.registerTypeAdapter(
                Date::class.java,
                JsonDeserializer { json, typeOfT, context -> Date(json.asJsonPrimitive.asLong) })
            val gson = builder.create()
            val type = object : TypeToken<TipPost?>() {}.type
            result = gson.fromJson(userShopInfoDTOS, type)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return result
    }

    private fun resolveResultData(obj: String): ActivityInfo? {
        var result: ActivityInfo = ActivityInfo()
        try {
            val data = JSONObject(obj).getJSONObject("data")
            val userShopInfoDTOS = data.toString()
            val builder = GsonBuilder()
            builder.registerTypeAdapter(
                Date::class.java,
                JsonDeserializer { json, typeOfT, context -> Date(json.asJsonPrimitive.asLong) })
            val gson = builder.create()
            val type = object : TypeToken<ActivityInfo?>() {}.type
            result = gson.fromJson(userShopInfoDTOS, type)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return result
    }

    private fun resolveQuestionAllData(obj: String): MutableList<IndexAllQuestion>? {
        var result: MutableList<IndexAllQuestion> = ArrayList()
        try {
            val data = JSONObject(obj).getJSONObject("data").getJSONArray("items")
            val userShopInfoDTOS = data.toString()
            val builder = GsonBuilder()
            builder.registerTypeAdapter(
                Date::class.java,
                JsonDeserializer { json, typeOfT, context -> Date(json.asJsonPrimitive.asLong) })
            val gson = builder.create()
            val type = object : TypeToken<List<IndexAllQuestion?>?>() {}.type
            result = gson.fromJson(userShopInfoDTOS, type)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return result
    }

    private fun resolveQuestionData(obj: String): MutableList<FaqExportQuestion>? {
        var result: MutableList<FaqExportQuestion> = ArrayList()
        try {
            val data = JSONObject(obj).getJSONObject("data").getJSONArray("items")
            val userShopInfoDTOS = data.toString()
            val builder = GsonBuilder()
            builder.registerTypeAdapter(
                Date::class.java,
                JsonDeserializer { json, typeOfT, context -> Date(json.asJsonPrimitive.asLong) })
            val gson = builder.create()
            val type = object : TypeToken<List<FaqExportQuestion?>?>() {}.type
            result = gson.fromJson(userShopInfoDTOS, type)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return result
    }

    private fun resolveToolData(obj: String): MutableList<IndexAllSee>? {
        var result: MutableList<IndexAllSee> = ArrayList()
        try {
            val data = JSONObject(obj).getJSONObject("data").getJSONObject("listInfo")
                .getJSONArray("items")
            val userShopInfoDTOS = data.toString()
            val builder = GsonBuilder()
            builder.registerTypeAdapter(
                Date::class.java,
                JsonDeserializer { json, typeOfT, context -> Date(json.asJsonPrimitive.asLong) })
            val gson = builder.create()
            val type = object : TypeToken<List<IndexAllSee?>?>() {}.type
            result = gson.fromJson(userShopInfoDTOS, type)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return result
    }

    private fun resolveShopListData(obj: String): MutableList<ShopDetailModel>? {
        var result: MutableList<ShopDetailModel> = ArrayList()
        try {
            val data = JSONObject(obj).getJSONArray("data")
            val userShopInfoDTOS = data.toString()
            val builder = GsonBuilder()
            builder.registerTypeAdapter(
                Date::class.java,
                JsonDeserializer { json, typeOfT, context -> Date(json.asJsonPrimitive.asLong) })
            val gson = builder.create()
            val type = object : TypeToken<List<ShopDetailModel?>?>() {}.type
            result = gson.fromJson(userShopInfoDTOS, type)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return result
    }

    private fun resolveGoodsData(obj: String): MutableList<ActGoodsItem>? {
        var result: MutableList<ActGoodsItem> = ArrayList()
        try {
            val data = JSONObject(obj).getJSONObject("data").getJSONArray("goodsList")
            val userShopInfoDTOS = data.toString()
            val builder = GsonBuilder()
            builder.registerTypeAdapter(
                Date::class.java,
                JsonDeserializer { json, typeOfT, context -> Date(json.asJsonPrimitive.asLong) })
            val gson = builder.create()
            val type = object : TypeToken<List<ActGoodsItem?>?>() {}.type
            result = gson.fromJson(userShopInfoDTOS, type)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return result
    }

    private fun resolveAdListData(obj: String): MutableList<AdModel>? {
        var result: MutableList<AdModel> = ArrayList()
        try {
            val data = JSONObject(obj).getJSONArray("data")
            val userShopInfoDTOS = data.toString()
            val builder = GsonBuilder()
            builder.registerTypeAdapter(
                Date::class.java,
                JsonDeserializer { json, typeOfT, context -> Date(json.asJsonPrimitive.asLong) })
            val gson = builder.create()
            val type = object : TypeToken<List<AdModel?>?>() {}.type
            result = gson.fromJson(userShopInfoDTOS, type)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return result
    }
}