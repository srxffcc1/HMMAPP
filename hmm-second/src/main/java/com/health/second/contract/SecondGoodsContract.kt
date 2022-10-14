package com.health.second.contract

import com.healthy.library.base.BasePresenter
import com.healthy.library.base.BaseView
import com.healthy.library.model.*

/**
 * @author Long
 * @desc:
 * @createTime :2021/10/14 11:06
 */
interface SecondGoodsContract {

    interface View : BaseView {
        /**
         * 商品详情回调
         */
        fun successGetGoodsDetail(goodsDetail: GoodsDetail?)

        /**
         * 砍价详情回调
         */
        fun successGetGoodsDetailKick(goodsDetail: Goods2DetailKick?)

        /**
         * 拼团详情回调
         */
        fun successGetGoodsDetailPin(goodsDetail: Goods2DetailPin?)

        /**
         * 获取已参与拼团人员列表回调
         */
        fun successTeamList(couponInfoByMerchants: MutableList<AssemableTeam>?)

        /**
         * 推荐商品列表数据回调
         */
        fun successGetRecommendList(recommendList: MutableList<RecommendList>?, firstPageSize: Int)

        /**
         * 商品条码列表数据
         */
        fun successGetSkuExList(list: MutableList<GoodsSpecDetail>?)

        /**
         * 加入购物车成功回调
         */
        fun successAddShoppingCat(result: String?)
    }

    interface Presenter : BasePresenter {
        /**
         * 获取商品详情数据
         */
        fun getGoodsDetail(map: MutableMap<String, Any>)

        /**
         * 砍价详情数据
         */
        fun getGoodsDetailKick(map: MutableMap<String, Any>)

        /**
         * 获取已参与拼团人员列表
         */
        fun getTeamList(map: MutableMap<String, Any>)

        /**
         * 拼团详情数据
         */
        fun getGoodsDetailPin(map: MutableMap<String, Any>)

        /**
         * 获取推荐商品数据
         */
        fun getRecommendList(map: MutableMap<String, Any>)

        /**
         * 获取商品条码
         */
        fun getGoodsDetailSkuEx(map: MutableMap<String, Any>)

        /**
         * 加入购物车
         */
        fun addShoppingCat(map: MutableMap<String, Any>)
    }

}