package com.healthy.library.model

import java.io.Serializable

/**
 * @description 帖子活动 奖品模型（投票活动、报名活动）
 * 2021-07-22 抽出来多活动公用
 * @author long
 * @date 2021/7/22
 */
class PrizeModel : Serializable {
    /** 活动奖品id */
    var id: String = ""

    /** 活动id */
    var activityId: String = ""

    /** 商品id */
    var goodsId: String = ""

    /**  */
    var goodsChildId: String = ""

    /** 营销活动商品表id */
    var mapMarketingGoodsId: String = ""

    /** 营销活动商品子表id */
    var mapMarketingGoodsChildId: String = ""

    /** 奖项名称 */
    var name: String = ""

    /** 该奖项获奖人数 */
    var prizeNum = 0

    /** 商品标题名称 */
    var goodsTitle: String = ""

    /** 商品图片 */
    var goodsImage: String = ""

    /** 商品信息 */
    var goodsDto: GoodsDetail? = null
}