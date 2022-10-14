package com.health.city.contract

import com.healthy.library.base.BasePresenter
import com.healthy.library.base.BaseView
import com.healthy.library.model.Discuss

/**
 * author : long
 * Time   :2021/11/15
 * desc   :
 */
interface PkVoteContract {

    interface View : BaseView {

        fun onAddPkVoteSuccess()

        fun onSuccessAdd()

        fun onGetCommentListSuccess(commentList: MutableList<Discuss>, isMore: Boolean)

        fun onGetCommentReplyListSuccess(position: Int)

    }

    interface Presenter : BasePresenter {

        fun addPkVote(map: MutableMap<String, Any>)

        fun addDiscuss(map:MutableMap<String,Any>)

        fun getCommentList(map: MutableMap<String, Any>)

        fun getCommentReply(map: MutableMap<String, Any>, discuss: Discuss, forSize: Int)

    }

}