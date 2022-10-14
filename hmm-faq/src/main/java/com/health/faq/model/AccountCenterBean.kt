package com.health.faq.model

import java.io.Serializable

data class AccountCenterBean(
        val createDate: String?,
        val id: Int?,
        val incomeBalance: Float?=0.0f,
        val memberId: String?,
        val status: Int?,
        val updateDate: String?,
        val virtualBalance: Float?=0.0f
) : Serializable