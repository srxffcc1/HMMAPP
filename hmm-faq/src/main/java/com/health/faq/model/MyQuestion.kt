package com.health.faq.model

import com.chad.library.adapter.base.entity.MultiItemEntity
import java.io.Serializable

/*
* {
    "id": 14,
    "questionId": 82,
    "memberId": "HMM190530278000189",
    "faceUrl": "c20cdb0937ed.png",
    "nickName": "匿名用户",
    "replyDetail": "这是一条正儿八经的回答这是一条正儿八经的回答这是一条正儿八经的回答这是一条正儿八经的回答这是一条正儿八经的回答这是一条正儿八经的回答这是一条正儿八经的回答这是一条正儿八经的回答这是一条正儿八经的回答这是一条正儿八经的回答这是一条正儿八经的回答这是一条正儿八经的回答111",
    "createDate": "2019-07-03 16:26:53",
    "photo": null,
    "soundUrl": null,
    "isBest": 1,
    "getQuestionMoney": null,
    "isHidden": 1,
    "updateDate": "2019-07-03 16:26:56",
    "status": 1,
    "phone": "12345678901",
    "currentStatusType": null
    "answerType": 1:我的提问，2：我的回答
    type  1-悬赏问答
}
* */
class MyQuestion constructor(
        val questionId: Int? = 0,
        val replyDetail: String? = "",
        val soundUrl: String? = "",
        val isBest: Int? = 0,
        val getQuestionMoney: String? = "",

        val createDate: String? = "",
        val currentStatus: String? = "",
        val currentStatusType: Int? = 0,
        val detail: String? = "",
        val faceUrl: String? = "",
        val hot: Int? = 0,
        val id: Int? = 0,
        val introduction: String? = "",
        val isHidden: Int? = 0,
        val isReturn: Int? = 0,
        val isShow: Int? = 0,
        val memberId: String? = "",
        val nickName: String? = "",
        val orderId: String? = "",
        val payDate: String? = "",
        val payType: Int? = 0,
        val phone: String? = "",
        val photo: String? = "",
        val platformCode: String? = "",
        val replyCount: Int? = 0,
        val rewardMoney: String = "",
        val status: Int? = 0,
        val type: Int? = 0,
        var readCount:Int=0,
        val updateDate: String? = "",
        val visitingCount: Int? = 0,
        val answerType: Int? = 0,
        val replyCreateDate: String,
        val replyDateIllustration: String


) : Serializable, MultiItemEntity {


    override fun getItemType(): Int {
        return answerType!!
    }


}