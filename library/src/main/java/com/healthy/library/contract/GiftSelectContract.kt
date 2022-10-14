package com.healthy.library.contract

import com.healthy.library.base.BasePresenter
import com.healthy.library.base.BaseView
import com.healthy.library.model.ShopDetailModel

/**
 * @description
 * @author long
 * @date 2021/9/16
 */
interface GiftSelectContract {
    interface View : BaseView {
        /**
         * 门店列表成功回调
         * @param detialModel
         */
        fun onGetShopListSuccess(detialModel: MutableList<ShopDetailModel>)
    }

    interface Presenter : BasePresenter {
        /**
         * 查询门店列表
         * @param map
         */
        fun getShopList(map: MutableMap<String, Any>)
    }
}