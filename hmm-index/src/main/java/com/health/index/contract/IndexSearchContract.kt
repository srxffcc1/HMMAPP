package com.health.index.contract

import com.healthy.library.model.IndexAllQuestion
import com.health.index.model.IndexAllSee
import com.healthy.library.base.BasePresenter
import com.healthy.library.base.BaseView
import com.healthy.library.model.*

/**
 * 创建日期：2021/12/15 11:34
 * @author LiuWei
 * @version 1.0
 * 包名： com.health.index.adapter
 * 类说明：
 */
interface IndexSearchContract {
    interface View : BaseView {
        /**
         * 获取搜索关键词列表
         */
        fun onQuerySearchRecordsSuccess(records: MutableList<SearchRecordsModel>?, isMore: Boolean)

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
         * 查询搜索页banner
         */
        fun onGetBannerSuccess(adModels: MutableList<AdModel>?)

        /**
         * 获取搜索憨妈课堂视频列表回调
         */
        fun onQueryGoodsListSuccess(records: MutableList<SortGoodsListModel>?, isMore: Boolean)

        /**
         * 视频点赞
         */
        fun onAddPraiseSuccess(result: String, type: Int)

        /**
         * 获取门店
         */
        fun onSearchShopListSuccess(records: MutableList<ShopDetailModel>?)

        /**
         * 获取百科文章
         */
        fun onQueryArticleListSuccess(records: MutableList<IndexAllSee>?, isMore: Boolean)

        fun onQueryQuestionListSuccess(records: MutableList<FaqExportQuestion>?, isMore: Boolean)

        fun onGetAllQuestionListSuccess(records: MutableList<IndexAllQuestion>?)
    }

    interface Presenter : BasePresenter {
        /**
         * 获取搜索关键词列表
         */
        fun querySearchRecords(map: MutableMap<String, Any>)

        /**
         * 获取帖子搜索列表数据
         */
        fun queryPostList(map: MutableMap<String, Any>)

        /**
         * 获取憨妈课堂视频搜索列表数据
         */
        fun queryHmmVideoList(map: MutableMap<String, Any>)

        /**
         * 获取憨妈短视频搜索列表数据
         */
        fun queryVideoList(map: MutableMap<String, Any>)

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
         *   查询搜索页banner
         */
        fun getAdBanner(map: MutableMap<String, Any>)

        /**
         *   查询搜索页商品
         */
        fun queryGoodsList(map: MutableMap<String, Any>)

        /**
         *   视频点赞
         */
        fun addPraise(map: MutableMap<String, Any>, type: Int)

        /**
         *   查询搜索页门店
         */
        fun queryShopList(map: MutableMap<String, Any>)

        /**
         *   查询搜索页百科文章
         */
        fun queryArticleList(map: MutableMap<String, Any>)

        /**
         *   查询搜索页问答
         */
        fun queryQuestionList(map: MutableMap<String, Any>)

        /**
         *   查询大家都在问
         */
        fun getAllQuestion(map: MutableMap<String, Any>)
        /**
         *   记录搜索历史
         */
        fun addSearchRecord(map: MutableMap<String, Any>)
    }
}