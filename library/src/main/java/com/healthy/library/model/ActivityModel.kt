package com.healthy.library.model

import com.chad.library.adapter.base.entity.MultiItemEntity
import java.io.Serializable

/**
 * @description 活动帖子 model
 * @author long
 * @date 2021/7/2
 */
open class ActivityModel : Serializable, MultiItemEntity {
    /** type 多布局类型 */
    var type: Int = 0

    /** 列表最外层对应报名记录id/内层活动id  */
    var id: String = ""

    /** 活动名称  */
    var name: String = ""

    /** 活动说明  */
    var description: String = ""

    /** 背景颜色  */
    var backgroundUrl: String = ""

    /** 背景图片链接  */
    var backgroundColor: String = ""

    /** 活动状态（0：未发布 1：已发布 2：已关闭 3：已结束） 我的投票列表状态对应（状态 -1冻结 0正常） */
    var status: Int = 0

    /** 帖子列表/详情 状态（-1冻结 0正常) */
    var freezeStatus: Int = 0

    /** 冻结原因 */
    var freezeReason: String = ""

    /** 活动关联id */
    var activitySignupId: Int = 0

    /** 票数 */
    var votingNum: String = ""

    /** 排名 */
    var ranking: String = ""

    /** 投票编号 */
    var voteNo: String = ""

    /** 获奖状态（0：未中奖 1：未领奖 2：已领奖） */
    var awardStatus: Int = -1

    /** 获奖奖品id */
    var awardPrizeId: String = ""

    /** 获奖订单Id */
    var awardOrderId: String = ""

    /** 获奖订单号 */
    var awardOrderNo: String = ""

    /** 当前投票/报名状态 */
    var isActivityStatus: String = ""

    /** 投票类型（1：图片+文字 2：视频+文字）  */
    var votingType = 0

    /** 报名内容配置，多个配置逗号分割（1：姓名 2：性别 3：身份证号 4：联系方式 5：联系地址
     * 6：参赛口号 7：照片 8：视频封面 9：视频）	  */
    var enlistOption: String = ""

    /** 报名开始时间	  */
    var enlistStartTime: String = ""

    /** 报名结束时间  */
    var enlistEndTime: String = ""

    /** 投票开始时间  */
    var votingStartTime: String = ""

    /** 投票结束时间  */
    var votingEndTime: String = ""

    /** 领奖结束日期  */
    var rewardEndTime: String = ""

    /** 投票规则，每人（1：一共 2：每天 3：每周）  */
    var votingRule = 0

    /** 根据规则，每人投票数  */
    var votingRuleNum = 0

    /** 活动奖品列表  */
    var prizeList: MutableList<PrizeModel>? = null

    /** 活动信息 */
    var activity: ActivityModel? = null

    /** 商户id */
    var merchantId: Int = 0

    /** 报名人数 */
    var signupCont: Int = 0

    /** 总投票数 */
    var votingCount: Int = 0

    /** 当前用户是否已报名 true 已报名 false未报名 */
    var signUpStatus: Boolean = false

    /** 商家信息 */
    var merchantInfo: ShopDetailModel? = null

    /** 活动门店id列表 */
    var shopIdList: MutableList<String>? = null

    override fun getItemType(): Int {
        return type
    }
}