package com.health.faq.model

import java.io.Serializable

data class ExpertModel(
    val data: List<Expert>,
    val code: Int,
    val msg: String
):Serializable
/*
*
* {
    "userId": 5,
    "faceUrl": "http://hmm-public.oss-cn-beijing.aliyuncs.com/hmm-user-dev/images/59a63dff-4e10-4066-8be5-9af21b2f52b9.png",
    "realName": "洪燕3",
    "rank": "主治医生-副教授",
    "registerTime": "2019-08-15 10:16:42",
    "consultingFees": 10,
    "counts": 1,
    "goodSkills": "病理性黄疸、婴幼儿腹泻、小儿感冒咳嗽、病毒性心肌炎、哮喘、不孕不育、病理性黄疸"
}
*
*
* */
data class Expert(
    val consultingFees: Float,
    val counts: Int,
    val faceUrl: String,
    val rank: String,
    val realName: String,
    val registerTime: String,
    val userId: Int,
    val goodSkills:String
):Serializable