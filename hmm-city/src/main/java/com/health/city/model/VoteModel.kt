package com.health.city.model

import com.chad.library.adapter.base.entity.MultiItemEntity
import java.io.Serializable

/**
 * @description
 * @author long
 * @date 2021/6/22
 */
class VoteModel : Serializable, MultiItemEntity {

    var type: Int = 0

    /**
     * 模拟 0 活动已结束 1 活动进行中
     */
    var activityStatus: Int = 0

    /**
     * 模拟 0 投票中(去拉票） 1 中奖未领取 2 中奖已领取
     */
    var status: Int = 0

    /**
     * 模拟 是否投票 0 未投票 1 已投票
     */
    var voteStatus: Int = 0

    /**
     * 模拟 0 用户冻结状态 1 未冻结
     */
    var userStatus: Int = 0

    fun isActivityStatus(): Boolean {
        return activityStatus == 0
    }

    fun isVoteStatus(): Boolean {
        return voteStatus == 0
    }

    fun isUserStatus(): Boolean {
        return userStatus == 0
    }

    override fun getItemType(): Int {
        return type
    }
}