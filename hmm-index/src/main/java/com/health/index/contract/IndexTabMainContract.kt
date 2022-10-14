package com.health.index.contract

import com.healthy.library.base.BasePresenter
import com.healthy.library.base.BaseView
import com.healthy.library.model.*

/**
 * author : long
 * Time   :2021/12/17
 * desc   :
 */
interface IndexTabMainContract {

    interface View : BaseView {

        fun onSuccessGetPostList(posts: MutableList<PostDetail>?, isMore: Boolean) // 7000

        fun onSuccessGetActivityList()

        fun onSuccessFan(result: Any?)

        fun onSuccessGetNewsList(indexAllSees: MutableList<NewsInfo>?, isMore: Boolean) //4034

        fun onSuccessGetGoodsRecommendList(
            result: MutableList<ActGoodsItem>?,
            firstPageSize: Int
        ) //9119

        fun onSuccessGetQuestionList(result: MutableList<FaqExportQuestion>?, isMore: Boolean)//4035

        /*** ------------------- tab -》 经验回调视频列表  ------------------- */
        fun onGetVideoListSuccess(result: MutableList<VideoListModel>?, isMore: Boolean)
        fun onGetAPPIndexCustomRecommandSuccess(result: AppIndexCustomRecommandAll)

        /**
         * 视频点赞
         */
        fun onAddPraiseSuccess(result: String, type: Int)
    }

    interface Presenter : BasePresenter {

        fun getPostingList(map: MutableMap<String, Any>)

        fun getActivityList(map: MutableMap<String, Any>, postDetail: PostDetail)

        //        ---------------------------------------------------------
        fun like(map: MutableMap<String, Any>)

        fun warn(map: MutableMap<String, Any>)

        fun fan(map: MutableMap<String, Any>)

        fun getRecommendNews(map: MutableMap<String, Any>) // 4034

        /*** ------------------- tab -》 经验 START ------------------- */
        fun getVideoList(map: MutableMap<String, Any>) // 8096

        fun getQuestionList(map: MutableMap<String, Any>)//4035 首页专家回答

        fun getRecommendGoods(map: MutableMap<String, Any>) // 9119
        fun getAPPIndexCustomRecommand(map: MutableMap<String, Any>) // app_index_1001 获取推荐位 妈妈都在聊 精选课程 今日听听

        /**
         *   视频点赞
         */
        fun addPraise(map: MutableMap<String, Any>, type: Int)

        /**
         *   文章点赞
         */
        fun articleLike(map: MutableMap<String, Any>)

    }

}