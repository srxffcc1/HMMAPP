package com.healthy.library.model

import java.io.Serializable

/**
 * @description 支付单据对象/退款单据对象 公用
 * @author long
 * @date 2021/7/26
 */
class SearchRecordsModel : Serializable {
    var id: String = ""
    var searchContent: String = ""
    var memberStatus: String = ""
    var startTime: String = ""
    var endTime: String = ""
    var h5Url: String = ""
    var androidUrl: String = ""
    var iosUrl: String = ""
    var merchantId: String = ""
    var status: String = ""
    var timeLimit: String = ""

}