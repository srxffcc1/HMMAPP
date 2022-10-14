package com.health.mall.contract

import com.health.mall.model.RefreshLoadModel
import com.healthy.library.model.ShopBrandModel
import com.healthy.library.base.BasePresenter
import com.healthy.library.base.BaseView

/**
 * @author Li
 * @date 2019-09-12 10:02
 * @des
 */
interface BranchShopContract {

    interface View : BaseView {
        fun onGetListSuccess(nearbyShops: ArrayList<ShopBrandModel>?,
                             refreshLoadModel: RefreshLoadModel?)


    }

    interface Presenter : BasePresenter {

        fun getData(categoryNo:String,shopId:String,cityNo: String, lat: String, lng: String)
    }
}