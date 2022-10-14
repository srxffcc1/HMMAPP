package com.health.second.presenter

import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.reflect.TypeToken
import com.health.second.contract.SecondGoodsContract
import com.healthy.library.constant.Functions
import com.healthy.library.model.*
import com.healthy.library.net.NoStringObserver
import com.healthy.library.net.ObservableHelper
import com.healthy.library.net.StringObserver
import org.json.JSONException
import org.json.JSONObject
import java.util.*

/**
 * @author Long
 * @desc:
 * @createTime :2021/10/14 11:10
 */
class SecondGoodsPresenter(context: Context, view: SecondGoodsContract.View) :
    SecondGoodsContract.Presenter {

    private var mContext = context
    private var mView = view

    override fun getGoodsDetail(map: MutableMap<String, Any>) {
        map[Functions.FUNCTION] = "9202"
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : StringObserver(mView, mContext, false) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    mView.successGetGoodsDetail(resolveDetailData(obj))
                }

                override fun onFailure() {
                    super.onFailure()
                    mView.successGetGoodsDetail(null)
                }

                override fun onFinish() {
                    super.onFinish()
                    mView.onRequestFinish()
                }
            })

    }

    /**
     * 砍价详情数据
     */
    override fun getGoodsDetailKick(map: MutableMap<String, Any>) {
        map[Functions.FUNCTION] = "7051"
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : StringObserver(mView, mContext, false) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    mView.successGetGoodsDetailKick(resolveDetailDataKick(obj))
                }

                override fun onFailure() {
                    super.onFailure()
                    mView.successGetGoodsDetailKick(null)
                }

                override fun onFinish() {
                    super.onFinish()
                    mView.onRequestFinish()
                }
            })
    }

    /**
     * 获取已参与拼团人员列表
     */
    override fun getTeamList(map: MutableMap<String, Any>) {
        map[Functions.FUNCTION] = "9015"
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : NoStringObserver(mView, mContext, false) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    mView.successTeamList(resolveTeamListData(obj))
                }

                override fun onFailure() {
                    super.onFailure()
                }
            })
    }

    /**
     * 拼团详情数据
     */
    override fun getGoodsDetailPin(map: MutableMap<String, Any>) {
        map[Functions.FUNCTION] = "9014"
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : StringObserver(mView, mContext, false) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    mView.successGetGoodsDetailPin(resolveDetailDataPin(obj))
                }

                override fun onFailure() {
                    super.onFailure()
                    mView.successGetGoodsDetailPin(null)
                }

                override fun onFinish() {
                    super.onFinish()
                    mView.onRequestFinish()
                }
            })
    }

    /**
     * 店铺推荐数据
     */
    override fun getRecommendList(map: MutableMap<String, Any>) {
        map[Functions.FUNCTION] = "9119"
        if (map["shopId"] == null) {
            return
        }
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : NoStringObserver(mView, mContext, false) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    try {
                        mView.successGetRecommendList(
                            resolveRecommendListData(obj),
                            JSONObject(obj).getJSONObject("data").getInt("firstPageSize")
                        )
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        mView.successGetRecommendList(null, 0)// 兼容一下错误code依旧是0的情况
                    }
                }

                override fun onFailure() {
                    super.onFailure()
                    mView.successGetRecommendList(null, 0)
                }

                override fun onFinish() {
                    super.onFinish()
                }
            })
    }

    /**
     * 商品条码
     */
    override fun getGoodsDetailSkuEx(map: MutableMap<String, Any>) {
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : NoStringObserver(mView, mContext, false) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    mView.successGetSkuExList(resolveSkuListData(obj))
                }

                override fun onFailure() {
                    super.onFailure()
                    mView.successGetSkuExList(null)
                }

                override fun onFinish() {
                    super.onFinish()
                }
            })
    }

    /**
     * 加入购物车
     */
    override fun addShoppingCat(map: MutableMap<String, Any>) {
        map[Functions.FUNCTION] = "25013"
        ObservableHelper.createObservable(mContext, map)
            .subscribe(object : StringObserver(mView, mContext, false) {
                override fun onGetResultSuccess(obj: String) {
                    super.onGetResultSuccess(obj)
                    mView.successAddShoppingCat(null)
                }

                override fun onFinish() {
                    super.onFinish()
                    mView.onRequestFinish()
                }
            })
    }

    private fun resolveDetailDataPin(obj: String): Goods2DetailPin? {
        var result: Goods2DetailPin? = null
        try {
            val data = JSONObject(obj).getJSONObject("data")
            val userShopInfoDTOS = data.toString()
            val builder = GsonBuilder()
            builder.registerTypeAdapter(
                Date::class.java,
                JsonDeserializer { json, typeOfT, context -> Date(json.asJsonPrimitive.asLong) })
            val gson = builder.create()
            val type = object : TypeToken<Goods2DetailPin?>() {}.type
            result = gson.fromJson(userShopInfoDTOS, type)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        if (result != null) {
            result.goodsDetails.marketingGoodsChildren = ArrayList()
            try {
                val goodsChildren =
                    GoodsDetail.GoodsChildren(result.assembleDTO.marketingGoodsChildDTOS[0])
                result.goodsDetails.marketingGoodsChildren.add(goodsChildren)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
        return result
    }

    private fun resolveTeamListData(obj: String): MutableList<AssemableTeam>? {
        var result: MutableList<AssemableTeam> = mutableListOf()
        try {
            val data = JSONObject(obj)
            val userShopInfoDTOS = data.getJSONObject("data").getJSONArray("items").toString()
            val builder = GsonBuilder()
            builder.registerTypeAdapter(
                Date::class.java,
                JsonDeserializer { json, typeOfT, context -> Date(json.asJsonPrimitive.asLong) })
            val gson = builder.create()
            val type = object : TypeToken<List<AssemableTeam?>?>() {}.type
            result = gson.fromJson(userShopInfoDTOS, type)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return result
    }

    private fun resolveDetailDataKick(obj: String): Goods2DetailKick? {
        var result: Goods2DetailKick? = null
        try {
            val data = JSONObject(obj).getJSONObject("data")
            val userShopInfoDTOS = data.toString()
            val builder = GsonBuilder()
            builder.registerTypeAdapter(
                Date::class.java,
                JsonDeserializer { json, typeOfT, context -> Date(json.asJsonPrimitive.asLong) })
            val gson = builder.create()
            val type = object : TypeToken<Goods2DetailKick?>() {}.type
            result = gson.fromJson(userShopInfoDTOS, type)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        if (result != null) {
            result.goodsDetails.marketingGoodsChildren = ArrayList()
            try {
                val goodsChildren =
                    GoodsDetail.GoodsChildren(result.bargainInfoDTO.marketingGoodsChildDTOS[0])
                result.goodsDetails.marketingGoodsChildren.add(goodsChildren)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return result
    }

    private fun resolveDetailData(obj: String): GoodsDetail? {
        var result: GoodsDetail? = null
        try {
            val data = JSONObject(obj).getJSONObject("data")
            val userShopInfoDTOS = data.toString()
            val builder = GsonBuilder()
            builder.registerTypeAdapter(
                Date::class.java,
                JsonDeserializer { json, typeOfT, context -> Date(json.asJsonPrimitive.asLong) })
            val gson = builder.create()
            val type = object : TypeToken<GoodsDetail?>() {}.type
            result = gson.fromJson(userShopInfoDTOS, type)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return result
    }

    private fun resolveRecommendListData(obj: String): MutableList<RecommendList> {
        var result: MutableList<RecommendList> = mutableListOf()
        try {
            val data = JSONObject(obj).getJSONObject("data").getJSONArray("goodsList")
            val userShopInfoDTOS = data.toString()
            val builder = GsonBuilder()
            builder.registerTypeAdapter(
                Date::class.java,
                JsonDeserializer { json, typeOfT, context -> Date(json.asJsonPrimitive.asLong) })
            val gson = builder.create()
            val type = object : TypeToken<MutableList<RecommendList>>() {}.type
            result = gson.fromJson(userShopInfoDTOS, type)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        for (i in result.indices) {
            result[i].setType(2)
        }
        return result
    }

    private fun resolveSkuListData(obj: String): MutableList<GoodsSpecDetail>? {
        var result: MutableList<GoodsSpecDetail> = mutableListOf()
        try {
            val data = JSONObject(obj).getJSONArray("data")
            val userShopInfoDTOS = data.toString()
            val builder = GsonBuilder()
            builder.registerTypeAdapter(
                Date::class.java,
                JsonDeserializer { json, typeOfT, context -> Date(json.asJsonPrimitive.asLong) })
            val gson = builder.create()
            val type = object : TypeToken<List<GoodsSpecDetail?>?>() {}.type
            result = gson.fromJson(userShopInfoDTOS, type)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return result
    }


}