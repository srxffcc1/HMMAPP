package com.health.second.contract

import com.healthy.library.base.BasePresenter
import com.healthy.library.base.BaseView
import com.healthy.library.model.ShopDetailModel

/**
 * @author Long
 * @desc:
 * @createTime :2021/10/14 13:41
 */
interface SecondShopContract {
    interface View : BaseView {

        /*** 门店详情回调 */
        fun onSucessGetShopDetailOnly(detailModel: ShopDetailModel?)
        /*** 门店列表回调 */
        fun onGetShopListSuccess(shopList: MutableList<ShopDetailModel>?)

    }

    interface Presenter : BasePresenter {

        /**
         * 获取门店详情数据
         */
        //fun getShopDetail(map: MutableMap<String, Any>)

        /**
         * 门店列表
         */
        fun getShopList(map: MutableMap<String, Any>)
        fun getShopDetailOnly(map: MutableMap<String, Any>)

    }
}