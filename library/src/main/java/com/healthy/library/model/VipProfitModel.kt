package com.healthy.library.model

import java.io.Serializable

/**
 * @description
 * @author long
 * @date 2021/8/2
 */
open class VipProfitModel : Serializable {

    var id: String = ""

    var memberId: String = ""

    var totalAmount: String = ""//总收益

    var totalOutstandingAmount: String = ""//未到账金额

    var totalUsableAmount: String = ""//可提现金额

    var totalWithdrawalAmount: String = ""//已提现金额

    var createTime: String = ""

    var updateTime: String = ""



    var shareId: String = ""//明细id
    var sourceType: String = ""//收益来源(0->分享赚)
    var sourceMemberId: String = ""//提供收益人的memberId
    var sourceMemberName: String = ""//sourceMemberName
    var sourceResult: String = ""//项目名称 例如 获得优惠券名称 获得10元现金 获得商品名 或者提现10元
    var incomeType: String = ""//类型 0-现金 1-优惠券 2-商品 3-提现
    var comment: String = ""//备注
    var expectReceivingTime: String = ""//预计到帐时间
    var receivingTime: String = ""//到帐时间
    var sourceMemberPhone: String = ""//sourceMemberPhone
    var actName: String = ""//actName
    var status: String = ""//1->未到帐, 2->已到帐 3->被撤销 4->提现
    var shareEnterType:String =""

}