package com.healthy.library.model

/**
 * author : long
 * Time   :2021/11/23
 * desc   :
 */
class LotteryInfoModel {

    /** 抽奖活动id */
    var lotteryId: String = ""

    /** 触发类型 1:下单抽奖 2:积分抽奖 3:签到抽奖 */
    var triggerType: String = ""

    /** 单笔订单满足金额：0为不限制，其它数值为满足金额 */
    var limitAmount: Double = 0.0

    /** 下单触发时,抽奖次数是否可倍率递增 */
    var dynamicMagnification:Int = 0

    /** 抽奖次数 */
    var lotteryNumber:Int = 0

    /** 是否指定商品：0-不限制，1-限制 */
    var goodsRestrict:Int = 0

    /** 是抽奖消耗积分 */
    var integral:Int = 0

    /** 签到抽奖类型 0-连续抽奖 1-累计抽奖 */
    var memberSignType:Int = 0

    /** 签到抽奖满足天数 */
    var memberSignCount:Int = 0

    /** 状态：0-正常，1-终止 */
    var status:Int = 0

}