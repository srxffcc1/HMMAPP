package com.health.mall.model

import com.healthy.library.model.CouponInfoByMerchant

/**
 * @author Li
 * @date 2019-08-06 11:34
 * @des 商店model
 *
 * @param shopId    门店id
 * @param shopName  门店名称
 * @param shopAddress   门店地址
 * @param shopDistance  门店距离（单位：m）
 * @param shopDistanceDes   距离文本（1.2km/100m）
 * @param goodsInfoList 门店包含商品
 */
data class ShopInfoModel(val shopId: String = "",
                         val shopName: String = "",
                         val brandName: String = "",
                         val shopAddress: String = "",
                         val shopImg: String = "",
                         val lat:String="",
                         val lng:String="",
                         val cityNo:String="",
                         val areaNo:String="",
                         val categoryNo:String="",
                         val categoryName:String="",
                         val shopDistance: Int = 0,
                         val overallRatingAvg:Double=0.0,
                         val phoneNum: String = "",
                         val shopDistanceDes: String =
                                 if (shopDistance >= 1000)
                                     "${String.format("%.1f", shopDistance / 1000.toDouble())}km"
                                 else "${shopDistance}m",
                         var showFullGoods: Boolean = false,
                         val goodsInfoList: ArrayList<GoodsInfoModel>? = null,
                         val maxDiscountGoods: GoodsInfoModel? = null,
                         val couponinfobymerchant: ArrayList<CouponInfoByMerchant>? = null,
                         val imgInfoList: ArrayList<StoreImgModel>? = null,
                         var courseId: String? = null,
                         var courseFlag: Int = 0,
                         val topicList: ArrayList<String> = java.util.ArrayList())

/**
 * @author Li
 * @date 2019-08-08 09:39
 * @des 商品信息
 *
 */
data class GoodsInfoModel(val goodsId: String = "",
                          val goodsName: String = "",
                          val goodsEffect: String = "",
                          val discount: Double = 1.0,
                          val headImages: ArrayList<String>? = null,
                          val price: Double = 0.0,
                          val storePrice: Double = 0.0,
                          val platformPrice: Double = 0.0,
                          val goodsUrl: String = "",
                          val tagOfCoupon: String = "",
                          val saleCount: Int = 0)

data class StoreImgModel(val id: String = "",
                          val url: String = "",
                          val type: String = "")

data class ClassificationModel(val name: String,
                               val id: String)

data class CategoryModel(val name: String,
                         val id: String,
                         val no: String,
                         val img:String,
                         val rank:Int, val toHomeTag:Int)
data class TypeModel(val id: String,
                     val categoryLevel: String,
                     val categoryName: String,
                     val goodsCategoryNo: String,
                     val filePath: String, val toHomeTag:Int)


//data class CategoryToStoreModel(val name: String,
//                               val id: String,
//                               val no: String,
//                                val img:String,
//                                val rank:String)

data class BannerModel(val imgUrl: String)

/**
 * 商城首页信息
 * @param categories 分类
 * @param banners 轮播图
 * @param shopEnter 是否有商家入驻
 * @param discountShops 优惠商家
 * @param nearbyShops 附近门店
 */
data class MallIndex2Model(
        val categories: ArrayList<CategoryModel>? = null,
        val banners: ArrayList<BannerModel>? = null,
        val shopEnter: Int = 0,
        val hasEntered: Boolean = shopEnter == 1,
        val discountShops: ArrayList<ShopInfoModel>? = null,
        val nearbyShops: ArrayList<ShopInfoModel>? = null)

data class RefreshLoadModel(
        var shopEnter: Int = 0,
        var isMore: Int = 0,
        var currentPage:Int=1,
        var maxGoodsDiscount:Double=0.0
)