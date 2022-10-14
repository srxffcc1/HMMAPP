package com.healthy.library.model

import android.view.View
import java.io.Serializable

/**
 * @description
 * @author long
 * @date 2021/8/2
 */
open class LotteryModel : Serializable {

    /** 中奖记录id / 抽奖活动id */
    var id: String = ""

    /** 用户手机号 */
    var memberPhone: String = ""

    /** 用户昵称 */
    var memberNickName: String = ""

    /** 结束时间 */
    var endTime: String = ""

    /** 抽奖活动id */
    var lotteryId: String = ""

    /** 中奖奖项id */
    var lotteryPrizeProfileId: String = ""

    /** 抽奖时间 */
    var lotteryTime: String = ""

    /** 使用积分 */
    var usedPoints: String = ""

    /** 获奖状态（1：未领奖 2：已领奖） */
    var awardStatus: Int = 1

    /** 订单id */
    var orderId: String = ""

    /** 订单编号 */
    var orderNo: String = ""

    /** 是否提货 0未核销 1已核销 */
    var verifyStatus: Int = 0

    /** 抽奖活动信息 */
    var lotteryActivity: LotteryActivityModel? = null

    /** 中奖奖项信息 */
    var lotteryPrizeProfile: LotteryPrizeModel? = null


    var isCalculate: Boolean = false

}