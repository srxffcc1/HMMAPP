package com.health.faq.model

import java.io.Serializable

data class DetatiledBean constructor(
        val createDate: String? = "",
        val id: Int? = 0,
        val incomeMoney: Double? = 0.00,
        val incomeSource: String? = "",
        val memberId: String? = "",
        val orderId: String? = "",
        val status: Int? = 0,
        val updateDate: String? = "",
        val virtualMoney: Double? = 0.00,
        val virtualSource: Int? = 0,//125是+ 34是-
        val yearAndMonth: String? = "",
        val yearAndMonthDate: String? = "",
        val dayOfWeek: String? = "",
        val monthAndDay: String? = "",
        val sourceDescription: String? = "",
        val moneyDescription: String? = "",
        val virtualSourceDescription: String? = "",
        val virtualMoneyDescription: String? = "",
        val hourAndMinAsecond:String?=""
) : Serializable