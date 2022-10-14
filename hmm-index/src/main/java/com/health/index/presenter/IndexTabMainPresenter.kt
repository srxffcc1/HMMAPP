package com.health.index.presenter

import android.content.Context
import android.os.Handler
import android.text.TextUtils
import android.widget.Toast
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.reflect.TypeToken
import com.health.index.contract.IndexTabMainContract
import com.health.index.model.IndexBean
import com.healthy.library.model.NewsInfo
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

/**
 * author : long
 * Time   :2021/12/17
 * desc   :
 */
class IndexTabMainPresenter(var mContext: Context, var mView: IndexTabMainContract.View) :
    IndexTabMainContract.Presenter {

    var ptype = 0

    override fun getPostingList(map: MutableMap<String, Any>) {
        val currentPage = map["currentPage"].toString()
        map[Functions.FUNCTION] = "7000"
        map["shopId"] = SpUtils.getValue(mContext, SpKey.CHOSE_SHOP)
        map["merchantId"] = SpUtils.getValue(mContext, SpKey.CHOSE_MC)
        ObservableHelper.createObservableNoLife(mContext, map)
            .subscribe(object : StringObserver(mView, mContext, false, false, false, false) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    val postDetails: MutableList<PostDetail> = resolvePostListData(obj)
                    if ("1" == currentPage && !ListUtil.isEmpty(postDetails)) {
                        postDetails[0].isFirst = true
                    }
                    mView.onSuccessGetPostList(postDetails, resolveRefreshData(obj))
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

    override fun getActivityList(map: MutableMap<String, Any>, postDetail: PostDetail) {
        map[Functions.FUNCTION] = "p_70022"
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : StringObserver(mView, mContext, false) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    postDetail.postActivityList = resolvePostingListData(obj)
                    mView.onSuccessGetActivityList()
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

    override fun articleLike(map: MutableMap<String, Any>) {
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : StringObserver(mView, mContext, false) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    if ("KD_10003" == map["function"]) {
                        mView.showToast("点赞成功")
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
                    mView.onRequestFinish()
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
                        val tipMessage =
                            if (ptype == 0) {
                                "关注成功"
                            } else {
                                "关注成功,可刷新查看关注内容"
                            }
                        mView.showToast(tipMessage)
                    } else {
                        Handler().postDelayed({
                            Toast.makeText(
                                mContext,
                                "取消关注成功",
                                Toast.LENGTH_SHORT
                            ).show()
                        }, 100)
                    }
                    mView.onSuccessFan(map["type"])
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
     * 获取视频列表内容
     */
    override fun getVideoList(map: MutableMap<String, Any>) {
        map[Functions.FUNCTION] = "8096"
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : StringObserver(
                mView, mContext, false,
                false, false, false
            ) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    mView.onGetVideoListSuccess(resolveListData(obj), resolveRefreshNextPage(obj))
                }

                override fun onFinish() {
                    super.onFinish()
                    mView.onRequestFinish()
                }

                override fun onFailure(msg: String) {
                    super.onFailure(msg)
                    mView.onGetVideoListSuccess(null, false)
                }
            })
    }

    override fun getQuestionList(map: MutableMap<String, Any>) {
        map[Functions.FUNCTION] = "5071"
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : StringObserver(mView, mContext, false) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    mView.onSuccessGetQuestionList(resolveFaqData(obj), resolveRefreshData(obj))
                }

                override fun onFinish() {
                    super.onFinish()
                    mView.onRequestFinish()
                }
            })
    }

    /**
     * 获取百科内容
     */
    override fun getRecommendNews(map: MutableMap<String, Any>) {
        map[Functions.FUNCTION] = "4034"
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : StringObserver(mView, mContext, false) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    mView.onSuccessGetNewsList(resolveNewsData(obj), resolveRefreshData(obj))
                }

                override fun onFinish() {
                    super.onFinish()
                    mView.onRequestFinish()
                }
            })
    }

    override fun getRecommendGoods(map: MutableMap<String, Any>) {
        map[Functions.FUNCTION] = "9119"
        if ("1".equals(map.get("pageNum").toString())) {
            map["firstPageSize"] = "0"
        }
        if (map["shopId"] == null) {
            return
        }
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : StringObserver(mView, mContext, false) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    try {
                        mView.onSuccessGetGoodsRecommendList(
                            resolveGoodsListData(obj),
                            JSONObject(obj).getJSONObject("data").getInt("firstPageSize")
                        )
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }

                override fun onFinish() {
                    super.onFinish()
                    mView.onRequestFinish()
                }

                override fun onFailure() {
                    super.onFailure()
//                    mView.onSuccessGetGoodsRecommendList(null, 0)
                }
            })
    }

    override fun getAPPIndexCustomRecommand(map: MutableMap<String, Any>) {
        map[Functions.FUNCTION] = "app_index_1001"
        if (!TextUtils.isEmpty(SpUtils.getValue(mContext, SpKey.CHOSE_MC))) {
            map["merchantId"] = SpUtils.getValue(mContext, SpKey.CHOSE_MC)
        } else {
            return
        }
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : NoStringObserver(mView, mContext, false) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    resolveAPPIndexCustomRecommandData(obj)?.let {
                        mView.onGetAPPIndexCustomRecommandSuccess(
                            it
                        )
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

    private fun resolveAPPIndexCustomRecommandData(obj: String): AppIndexCustomRecommandAll? {
        var result: AppIndexCustomRecommandAll? = null
        try {
            val data = JSONObject(obj).getJSONObject("data")
            val userShopInfoDTOS = data.toString()
            val builder = GsonBuilder()
            builder.registerTypeAdapter(
                Date::class.java,
                JsonDeserializer { json, typeOfT, context -> Date(json.asJsonPrimitive.asLong) })
            val gson = builder.create()
            val type = object : TypeToken<AppIndexCustomRecommandAll>() {}.type
            result = gson.fromJson(userShopInfoDTOS, type)
        } catch (e: Exception) {
            //System.out.println("错误:" + obj);
            e.printStackTrace()
        }
        return result
    }

    /**
     * 帖子列表模型解析
     */
    private fun resolvePostListData(obj: String): MutableList<PostDetail> {
        var result: MutableList<PostDetail> = mutableListOf()
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
        } catch (e: Exception) {
            //System.out.println("错误:" + obj);
            e.printStackTrace()
        }
        return result
    }

    private fun resolvePostingListData(obj: String): MutableList<PostActivityList> {
        var result: MutableList<PostActivityList> = mutableListOf()
        try {
            val data = JSONObject(obj).getJSONArray("data")
            val userShopInfoDTOS = data.toString()
            val builder = GsonBuilder()
            builder.registerTypeAdapter(
                Date::class.java,
                JsonDeserializer { json, typeOfT, context -> Date(json.asJsonPrimitive.asLong) })
            val gson = builder.create()
            val type = object : TypeToken<MutableList<PostActivityList>>() {}.type
            result = gson.fromJson<MutableList<PostActivityList>>(userShopInfoDTOS, type)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return result
    }

    /**
     * 视频列表模型解析
     */
    private fun resolveListData(s: String): MutableList<VideoListModel> {
        var list: MutableList<VideoListModel> = mutableListOf()
        try {
            val data = JSONObject(s).getJSONObject("data").getJSONArray("list")
            val userShopInfoDTOS = data.toString()
            val builder = GsonBuilder()
            builder.registerTypeAdapter(
                Date::class.java,
                JsonDeserializer { json, typeOfT, context -> Date(json.asJsonPrimitive.asLong) })
            val gson = builder.create()
            val type = object : TypeToken<MutableList<VideoListModel>>() {}.type
            list = gson.fromJson(userShopInfoDTOS, type)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return list
    }

    /**
     * 资讯列表（百科）列表模型解析
     */
    private fun resolveNewsData(obj: String): MutableList<NewsInfo> {
        var result: MutableList<NewsInfo> = mutableListOf()
        try {
            val data = JSONObject(obj).getJSONObject("data").getJSONArray("items")
            val userShopInfoDTOS = data.toString()
            val builder = GsonBuilder()
            builder.registerTypeAdapter(
                Date::class.java,
                JsonDeserializer { json, typeOfT, context -> Date(json.asJsonPrimitive.asLong) })
            val gson = builder.create()
            val type = object : TypeToken<MutableList<NewsInfo>>() {}.type
            result = gson.fromJson(userShopInfoDTOS, type)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return result
    }

    /**
     * 专家回答列表模型解析
     */
    private fun resolveFaqData(obj: String): MutableList<FaqExportQuestion> {
        var result: MutableList<FaqExportQuestion> = mutableListOf()
        try {
            val data = JSONObject(obj).getJSONObject("data").getJSONArray("items")
            val userShopInfoDTOS = data.toString()
            val builder = GsonBuilder()
            builder.registerTypeAdapter(
                Date::class.java,
                JsonDeserializer { json, typeOfT, context -> Date(json.asJsonPrimitive.asLong) })
            val gson = builder.create()
            val type = object : TypeToken<MutableList<FaqExportQuestion>>() {}.type
            result = gson.fromJson(userShopInfoDTOS, type)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return result
    }

    /**
     * 推荐商品模型解析
     */
    private fun resolveGoodsListData(obj: String): MutableList<ActGoodsItem> {
        var result: MutableList<ActGoodsItem> = mutableListOf()
        try {
            val data = JSONObject(obj).getJSONObject("data").getJSONArray("goodsList")
            val userShopInfoDTOS = data.toString()
            val builder = GsonBuilder()
            builder.registerTypeAdapter(
                Date::class.java,
                JsonDeserializer { json, typeOfT, context -> Date(json.asJsonPrimitive.asLong) })
            val gson = builder.create()
            val type = object : TypeToken<MutableList<ActGoodsItem>>() {}.type
            result = gson.fromJson(userShopInfoDTOS, type)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return result
    }

    private fun resolveRefreshData(obj: String): Boolean {
        var isMore = false
        try {
            isMore = JSONObject(obj).getJSONObject("data").getInt("isMore") == 1
        } catch (e: JSONException) {
//            e.printStackTrace()
        }
        return isMore
    }

    private fun resolveRefreshNextPage(obj: String): Boolean {
        var isMore = false
        try {
            isMore = JSONObject(obj).getJSONObject("data").getInt("nextPage") > 0
        } catch (e: JSONException) {
//            e.printStackTrace()
        }
        return isMore
    }

}