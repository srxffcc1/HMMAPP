package com.healthy.library.model

import java.io.Serializable

/**
 * @description
 * @author long
 * @date 2021/8/2
 */
open class BankCardModel : Serializable {

    var bankCode: String = ""

    var cardBin: String = ""

    var cardName: String = ""

    var cardType: String = ""

    var bankName: String = ""

    var bankCardNo: String = ""

    var cardState: String = ""

    var cardLenth: String = ""

    var cardTypeLabel: String = ""

    var isSelect: Boolean = false

}