package com.healthy.library.model

import java.io.Serializable

/**
 * @description
 * @author long
 * @date 2021/8/3
 */
class LotteryActivityModel : Serializable {

    /** 活动名称 */
    var title: String = ""

    /** 商家id */
    var merchantId: String = ""

    var restrict: RestrictModel? = null

    var deliveryProfiles: MutableList<DeliveryProfilesModel>? = null

    /** 商家信息 */
    var merchantInfo: ShopDetailModel? = null

    open class RestrictModel : Serializable {
        /** 最晚兑奖时间 */
        var latestExchangeTime: String = ""
    }

    open class DeliveryProfilesModel : Serializable {

        /** 门店Id */
        var shopId: String = ""
    }

}