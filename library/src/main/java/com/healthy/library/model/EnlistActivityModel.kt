package com.healthy.library.model

import android.widget.EditText
import com.chad.library.adapter.base.entity.MultiItemEntity
import java.io.Serializable

/**
 * @description
 * @author long
 * @date 2021/7/23
 */
open class EnlistActivityModel : Serializable, MultiItemEntity {
    /** type 多布局类型 */
    var type: Int = 0

    /** 地址（省） */
    private var addressProvinceName: String = ""

    /** 地址（市） */
    private var addressCityName: String = ""

    /** 地址（区） */
    private var addressAreaName: String = ""

    /** 详细地址 */
    private var addressDetails: String = ""

    /** 活动地址 */
    fun activityAddress(): String {
        return addressProvinceName + addressCityName + addressAreaName + addressDetails
    }

    /** 商家logo */
    var merchantLogoUrl: String = ""

    /** 活动说明 */
    var description: String = ""

    /** 最大报名人数 */
    var enlistMaxNum: String = ""

    /** 报名人数限制（0：不限制 1：限制） */
    var enlistNumLimit: String = ""

    /** 报名开始时间 */
    var enlistStartTime: String = ""

    /** 报名结束时间 */
    var enlistEndTime: String = ""

    /** 活动开始时间 */
    var startTime: String = ""

    /** 活动结束时间 */
    var endTime: String = ""

    /** 纬度 */
    var latitude: String = ""

    /** 经度 */
    var longitude: String = ""

    /** 活动名称 */
    var name: String = ""

    /** 活动图片链接 */
    var photoUrl: String = ""

    /** 活动价格 */
    var price: Double = 0.00

    /** 是否免费（0：收费 1：免费） */
    var isFree: Int = 0

    /** 已经报名的人数 */
    var enlistCount: String = ""

    /** 活动状态（0：未发布 1：已发布 2：已截止 3：已终止）	 */
    var status: Int = 0

    /** 距离创建这条报名记录的秒数（支付倒计时，此秒数不准调整中）	 */
    var autoCancelSeconds: Long = 0

    /**************** 报名记录数据 *****************/
    /** 报名记录id */
    var id: String = ""

    /** 活动id */
    var enlistActivityId: String = ""

    /** 报名记录单号 */
    var enlistActivityRecordNo: String = ""

    /** 报名姓名(列表 -> 联系人姓名) */
    var enlistName: String = ""
    var editEnlistName: String = ""

    /** 报名性别 0 女 1 男 (列表 -> 联系人性别)*/
    var enlistSex: Int = 1
    var editEnlistSex: Int = 1

    /** 联系方式 (列表 -> 联系人电话)*/
    var enlistPhone: String = ""
    var editEnlistPhone: String = ""

    /** 所属阶段 */
    var enlistStage: String = ""
    var enlistChooseStatus: String = ""

    /** 状态：0未支付 1已取消 2已退款 3待核销 4已完成 */
    var payStatus: Int = -1

    /** 核销码 */
    var verificationCode: String = ""

    /** 报名活动对象模型 */
    var enlistActivity: EnlistActivityModel? = null

    /** 支付单据对象模型 */
    var enlistActivityTrade: EnlistActivityPayModel? = null

    /** 退款单据对象模型 */
    var enlistActivityRefund: EnlistActivityPayModel? = null

    override fun getItemType(): Int {
        return type
    }

    /** 核销码 */
    var payOrderId: String = ""
}