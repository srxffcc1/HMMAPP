package com.health.index.presenter

import android.content.Context
import android.os.Handler
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.reflect.TypeToken
import com.health.index.contract.IndexBabyContract
import com.healthy.library.model.NewsInfo
import com.healthy.library.businessutil.ListUtil
import com.healthy.library.constant.Functions
import com.healthy.library.constant.SpKey
import com.healthy.library.model.*
import com.healthy.library.net.ObservableHelper
import com.healthy.library.net.StringObserver
import com.healthy.library.utils.SpUtils
import org.json.JSONException
import org.json.JSONObject
import java.util.*

/**
 * 创建日期：2021/12/15 11:34
 * @author LiuWei
 * @version 1.0
 * 包名： com.health.index.adapter
 * 类说明：
 */
class IndexBabyPresenter(
    private val mContext: Context,
    private val mView: IndexBabyContract.View
) : IndexBabyContract.Presenter {

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
        map[Functions.FUNCTION] = "8096-ik"
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : StringObserver(mView, mContext, false) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    mView.onQueryHmmVideoListSuccess(
                        resolveListData(obj),
                        resolveGoodsRefreshData(obj)
                    )
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
    override fun queryVideoList(map: MutableMap<String, Any>) {
        map[Functions.FUNCTION] = "8096"
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : StringObserver(mView, mContext, false) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    mView.onQueryVideoListSuccess(
                        resolveListData(obj),
                        resolveGoodsRefreshData(obj)
                    )
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

    private fun resolveStringList(s: String): MutableList<String>? {
        var list: MutableList<String>? = null
        try {
            val data = JSONObject(s).getJSONArray("data")
            val userShopInfoDTOS = data.toString()
            val builder = GsonBuilder()
            builder.registerTypeAdapter(
                Date::class.java,
                JsonDeserializer { json, typeOfT, context -> Date(json.asJsonPrimitive.asLong) })
            val gson = builder.create()
            val type = object : TypeToken<MutableList<String>?>() {}.type
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
            val data = JSONObject(s).getJSONObject("data").getJSONArray("list")
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

    override fun getAudioNews(map: MutableMap<String, Any>) {
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : StringObserver(mView, mContext, false) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    mView.onSuccessGetAudioNewsList(resolveAudioListData(obj))
                }

                override fun onFinish() {
                    super.onFinish()
                    mView.onRequestFinish()
                }
            })
    }

    override fun getSoundAlbumsDown(map: MutableMap<String, Any>) {
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : StringObserver(mView, mContext, false) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    mView.onSuccessGetSoundAlbumsDownList(resolveIndexListData(obj))
                }

                override fun onFinish() {
                    super.onFinish()
                    mView.onRequestFinish()
                }
            })
    }

    override fun getSoundAlbums(map: MutableMap<String, Any>) {
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : StringObserver(mView, mContext, false) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    mView.successGetSoundAlbum(resolveSoundAlbumsData(obj))
                }

                override fun onFinish() {
                    super.onFinish()
                    mView.onRequestFinish()
                }
            })
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

    private fun resolveAudioListData(obj: String): MutableList<SoundAlbum> {
        var result: MutableList<SoundAlbum> = mutableListOf()
        try {
            val data = JSONObject(obj).getJSONArray("data")
            val userShopInfoDTOS = data.toString()
            val builder = GsonBuilder()
            builder.registerTypeAdapter(
                Date::class.java,
                JsonDeserializer { json, typeOfT, context -> Date(json.asJsonPrimitive.asLong) })
            val gson = builder.create()
            val type = object : TypeToken<MutableList<SoundAlbum>>() {}.type
            result = gson.fromJson(userShopInfoDTOS, type)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return result
    }

    private fun resolveIndexListData(obj: String): SoundHomeSplit? {
        var result: SoundHomeSplit? = null
        try {
            val data = JSONObject(obj).getJSONObject("data")
            val userShopInfoDTOS = data.toString()
            val builder = GsonBuilder()
            builder.registerTypeAdapter(
                Date::class.java,
                JsonDeserializer { json, typeOfT, context -> Date(json.asJsonPrimitive.asLong) })
            val gson = builder.create()
            val type = object : TypeToken<SoundHomeSplit?>() {}.type
            result = gson.fromJson(userShopInfoDTOS, type)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return result
    }

    private fun resolveSoundAlbumsData(obj: String): SoundTypeSplit? {
        var result: SoundTypeSplit? = null
        try {
            val data = JSONObject(obj).getJSONObject("data")
            val userShopInfoDTOS = data.toString()
            val builder = GsonBuilder()
            builder.registerTypeAdapter(
                Date::class.java,
                JsonDeserializer { json, typeOfT, context -> Date(json.asJsonPrimitive.asLong) })
            val gson = builder.create()
            val type = object : TypeToken<SoundTypeSplit?>() {}.type
            result = gson.fromJson(userShopInfoDTOS, type)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return result
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
}