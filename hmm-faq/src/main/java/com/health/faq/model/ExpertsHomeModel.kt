package com.health.faq.model

import java.io.Serializable

data class ExpertsHomeModel(
        val data: Data,
        val code: Int,
        val msg: String
) : Serializable

data class Data(
        //问专家
        val listInfo: ListInfo,
        val rewardQuestionFirstPageDTO: RewardQuestionFirstPageDTO
) : Serializable

data class ListInfo(
        val currentPage: Int,
        val isMore: Int,
        val items: MutableList<Item>,
        val pageSize: Int,
        val startIndex: Int,
        val totalNum: Int,
        val totalPage: Int
) : Serializable

data class Item(
        val detail: String,
        val faceUrl: String,
        val id: Int,
        val questionId: Int,
        val rank: String,
        var readCount: Int,
        val realName: String,
        val expertId: Int
) : Serializable

data class RewardQuestionFirstPageDTO(
        //热门专家
        val hotExpertInfoDTOList: MutableList<HotExpertInfoDTO>,
        //悬赏
        val rewardQuestionList: MutableList<RewardQuestion>,
        val expertPhotoUrls: MutableList<String>
) : Serializable

data class RewardQuestion(
        val answerType: Any,
        val createDate: String,
        val currentStatus: String,
        val currentStatusType: Int,
        val detail: String,
        val expertId: Int,
        val faceUrl: String,
        val hot: Int,
        val id: Int,
        val introduction: String,
        val isExpert: Int,
        val isHidden: Int,
        val isReturn: Int,
        val isShow: Int,
        val memberId: String,
        val nickName: String,
        val orderId: String,
        val payDate: String,
        val payType: Int,
        val phone: Any,
        val photo: String,
        val platformCode: String,
        val replyCount: Int,
        val rewardMoney: Float,
        val status: Int,
        val type: Int,
        val updateDate: String,
        val visitingCount: Int
) : Serializable

data class HotExpertInfoDTO(
        val faceUrl: String,
        val rank: String,
        val realName: String,
        val replyCount: Int,
        val userId: Int
) : Serializable