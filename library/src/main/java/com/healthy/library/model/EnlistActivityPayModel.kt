package com.healthy.library.model

import java.io.Serializable

/**
 * @description 支付单据对象/退款单据对象 公用
 * @author long
 * @date 2021/7/26
 */
class EnlistActivityPayModel : Serializable {

    /** 支付金额 */
    var payAmount: String = ""

    /** 支付方式 */
    var payWay: String = ""

    /** 退款金额 */
    var refundAmount: String = ""

}