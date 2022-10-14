package com.health.city.model

/**
 * 创建日期：2021/11/15 11:50
 * @author LiuWei
 * @version 1.0
 * 包名： com.health.city.model
 * 类说明：
 */

class TalentList {
    var memberId: String? = null
    var createSource = 0
    var accountNickname: String? = null
    var accountFaceUrl: String? = null
    var postingCount = 0//发帖数
    var discussCount = 0//评论数
    var discussReplyCount = 0
    var postingPraiseCount = 0//点赞数
    var postingShareCount = 0//分享数
    var livelyValue = 0.0//活跃值
    var isCurrent = false//是否为当前用户
    var memberState: String? = null

    constructor(
        accountNickname: String?,
        accountFaceUrl: String?,
        postingCount: Int,
        discussCount: Int,
        postingPraiseCount: Int,
        postingShareCount: Int,
        livelyValue: Double,
        memberState: String?
    ) {
        this.accountNickname = accountNickname
        this.accountFaceUrl = accountFaceUrl
        this.postingCount = postingCount
        this.discussCount = discussCount
        this.postingPraiseCount = postingPraiseCount
        this.postingShareCount = postingShareCount
        this.livelyValue = livelyValue
        this.memberState = memberState
    }

    constructor()

}