package com.healthy.library.model

import android.os.CountDownTimer
import android.widget.TextView
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.example.lib_ShapeView.view.ShapeTextView
import io.reactivex.rxjava3.disposables.Disposable
import java.io.Serializable

/**
 * author : long
 * Time   :2021/11/13
 * desc   :
 */
class PkVotingActivityModel : Serializable {

    @Transient var timeView: ShapeTextView? = null

    /** PK活动id */
    var id: String = ""

    /** PK活动标题 */
    var activityTitle: String = ""

    /** 发起人id（商家马甲号id） */
    var initiatorAccountId: String = ""

    /** 发起人名称（商家马甲号名称） */
    var initiatorAccountNickname: String = ""

    /** 正方标题 */
    var squareTitle: String = ""

    /** 反方标题 */
    var conSideTitle: String = ""

    /** 正方图片url */
    var squareImgUrl: String = ""

    /** 反方图片url */
    var conSideImgUrl: String = ""

    /** 开始时间 */
    var activityStartTime: String = ""

    /** 开始时间 */
    var activityEndTime: String = ""

    /** 活动状态（0：未发布 1：已发布(进行中) 2：已结束 3：强制关闭）	*/
    var status: String = ""

    /** 人气值 */
    var initialPopularity: String = ""

    /** 一票代表几分，最低1分，最高10分，默认值为“1”	 */
    var voteScore: String = ""

    /** 一个评论代表几分，最低1分，最高10分，默1	 */
    var discussScore: String = ""

    /** 每人每天可以投票的数量	 */
    var onePeopleOneDayNum: String = ""

    /** 是否可以交叉投票 false 不可以 true可以	 */
    var canCrossVote: Boolean = false

    /** 正方得分 */
    var squareScore: String = ""

    /** 反方得分 */
    var conSideScore: String = ""

    /** 当前用户今天投票数量 */
    var currentUserTodayVoteNum: String = ""

    /** 当前用户最后投票立场  1 正方 2 反方 */
    var currentUserLastVoteStand: String = ""

    /** 正方人数 */
    var squareUserNum: String = ""

    /** 反方人数 */
    var conSideUserNum: String = ""

    /** 正方百分比 */
    var squarePercentage: Int = 0

    /** 反方百分比 */
    var conSidePercentage: Int = 0

}