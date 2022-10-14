package com.health.second.contract

import com.healthy.library.base.BasePresenter
import com.healthy.library.base.BaseView
import com.healthy.library.model.MainBlockDetailModel
import com.healthy.library.model.MainBlockModel
import com.healthy.library.model.PageInfoEarly

/**
 * @desc
 * @author long
 * @createTime
 */
interface SecondShopsBlockContract {

    interface View : BaseView {

        /**
         * 专题列表回调
         */
        fun onSuccessGetBlockList(list: MutableList<MainBlockModel>?)

        /**
         * 获取专区商品分页回调
         */
        fun onSuccessGetList(item: MainBlockModel, position: Int)

    }

    interface Presenter : BasePresenter {

        /**
         * 获取专区数据
         */
        fun getBlockList(map: MutableMap<String, Any>)

        /**
         * 获取专区商品
         */
        fun getGoodsList(
            map: MutableMap<String, Any>,
            itemOrg: MainBlockModel,
            itemDes: MainBlockModel.AllianceMerchant,
            position: Int
        )
    }

}