package com.health.mall.contract

import com.health.mall.model.*
import com.healthy.library.base.BasePresenter
import com.healthy.library.base.BaseView
import com.healthy.library.model.MallKKModel

/**
 * @author Li
 * @date 2019-09-12 10:02
 * @des
 */
interface MallContract3 {

    interface View : BaseView {
        fun onGetFirstPageDataSuccess(categoriestohome: ArrayList<CategoryModel>?,
                                      categoriestostore: ArrayList<CategoryModel>?,
                                      banners: ArrayList<BannerModel>?,
                                      discountShops: ArrayList<ShopInfoModel>?,
                                      nearbyShops: ArrayList<ShopInfoModel>?,
                                      refreshLoadModel: RefreshLoadModel?)
        fun onGetKKDataSuccess(mallkkmodels: ArrayList<MallKKModel>?)

        fun onGetMoreDataSuccess(nearbyShops: ArrayList<ShopInfoModel>?,
                                 refreshLoadModel: RefreshLoadModel?)

//        fun onGetTypeDataSuccess(types: ArrayList<TypeModel>?)
    }

    interface Presenter : BasePresenter {

        fun getData(cityNo: String, areaNo: String, lat: String, lng: String, page: String,isCurrentCity:String)
        fun getKKData(cityNo: String, areaNo: String, lat: String, lng: String, page: String,isCurrentCity:String)
    }
}