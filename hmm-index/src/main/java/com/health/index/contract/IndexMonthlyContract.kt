package com.health.index.contract

import com.health.index.model.ActivityInfo
import com.healthy.library.base.BasePresenter
import com.healthy.library.base.BaseView
import com.healthy.library.model.*


interface IndexMonthlyContract {
    interface View : BaseView {
        /**
         * 获取搜索同城圈帖子列表回调
         */
        fun onQueryPostListSuccess(records: MutableList<PostDetail>?, isMore: Boolean)

        /**
         * 获取搜索憨妈课堂视频列表回调
         */
        fun onQueryHmmVideoListSuccess(records: MutableList<VideoListModel>?, isMore: Boolean)

        /**
         * 获取搜索短视频列表回调
         */
        fun onQueryVideoListSuccess(records: MutableList<VideoListModel>?, isMore: Boolean)

        /**
         * 关注接口回调
         */
        fun onSuccessFan(result: Any?)

        /**
         * 点赞接口回调
         */
        fun onSuccessLike()

        /**
         * 部分帖子 优惠券和视频关联商品数据
         */
        fun onSuccessGetActivityList()

        /**
         * 获取搜索憨妈课堂视频列表回调
         */
        fun onQueryGoodsListSuccess(records: MutableList<ActGoodsItem>?, isMore: Boolean)

        /**
         * 视频点赞
         */
        fun onAddPraiseSuccess(result: String, type: Int)


        fun onQueryQuestionListSuccess(records: MutableList<FaqExportQuestion>?, isMore: Boolean)

        fun onGetAllQuestionListSuccess(records: MutableList<IndexAllQuestion>?)

        fun onGetActivityInfoSuccess(records: ActivityInfo?,status:Int)

        fun onSuccessGetTipPost(records: TipPost?)

        fun onSuccessGetPKPostDetail(records: PostDetail?)

        fun onAddPkVoteSuccess()
    }

    interface Presenter : BasePresenter {

        /**
         * 获取帖子搜索列表数据
         */
        fun queryPostList(map: MutableMap<String, Any>)

        /**
         * 获取憨妈课堂视频搜索列表数据
         */
        fun queryHmmVideoList(map: MutableMap<String, Any>)

        /**
         * 点赞
         */
        fun like(map: MutableMap<String, Any>)

        /**
         * 关注
         */
        fun fan(map: MutableMap<String, Any>)

        /**
         * 部分帖子 优惠券和视频关联商品
         */
        fun getActivityList(map: MutableMap<String, Any>, postDetail: PostDetail?)

        /**
         * 举报
         */
        fun warn(map: MutableMap<String, Any>)


        /**
         *   查询搜索页商品
         */
        fun queryGoodsList(map: MutableMap<String, Any>)

        /**
         *   视频点赞
         */
        fun addPraise(map: MutableMap<String, Any>, type: Int)

        /**
         *   查询搜索页问答
         */
        fun queryQuestionList(map: MutableMap<String, Any>)

        /**
         *   查询大家都在问
         */
        fun getAllQuestion(map: MutableMap<String, Any>)

        fun getActivityInfo(map: MutableMap<String, Any>)

        fun getTipPost(map: MutableMap<String, Any>)

        fun getPKPostDetail(map: MutableMap<String, Any>)

        fun addPkVote(map: MutableMap<String, Any>)
    }
}