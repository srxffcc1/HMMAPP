package com.healthy.library.model

import java.io.Serializable

/**
 * @description
 * @author long
 * @date 2021/8/2
 */
class LotteryPrizeModel : Serializable {

    /** 奖项id */
    var id: String = ""

    /** 抽奖活动id */
    var lotteryId: String = ""

    /** 奖项名称 */
    var prizeName: String = ""

    /** 商品id */
    var goodsId: String = ""

    /** 规格商品id */
    var goodsChildId: String = ""

    /** 商品标题名称 */
    var goodsTitle: String = ""

    /** 商品规格字符串 */
    var goodsSpecStr: String = ""

    /** 商品头图 */
    var goodsHeadImage: String = ""

    /** 奖品总数量 */
    var prizeNumber: Int = 0

    /** 奖品当前剩余数量 */
    var prizeCurrentNumber: Int = 0

    /** 中奖率 */
    var winProbability: String = ""

}