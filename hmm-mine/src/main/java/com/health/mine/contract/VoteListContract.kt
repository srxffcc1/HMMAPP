package com.health.mine.contract

import com.healthy.library.model.ActivityModel
import com.healthy.library.base.BasePresenter
import com.healthy.library.base.BaseView

/**
 * @description
 * @author long
 * @date 2021/6/30
 */
interface VoteListContract {

    interface View : BaseView {

        /**
         * 参与的投票/报名的投票活动列表回调
         * @param resultData 接口回调数据
         * @param isMore 是否还有更多
         */
        fun getVoteListSuccess(resultData: MutableList<ActivityModel>, isMore: Boolean);

    }

    interface Presenter : BasePresenter {
        /**
         * 参与的投票/报名的投票列表
         * @param map 接口所需参数
         */
        fun getVoteList(map: MutableMap<String, Any>)
    }
}