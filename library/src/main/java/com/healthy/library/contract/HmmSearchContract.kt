package com.healthy.library.contract

import com.healthy.library.base.BasePresenter
import com.healthy.library.base.BaseView
import com.healthy.library.model.PostDetail
import com.healthy.library.model.VideoListModel

/**
 * @author Long
 * @desc:
 * @createTime :2021/10/20 11:49
 */
interface HmmSearchContract {

    interface View : BaseView {

        /**
         * 获取搜索纪录列表回调
         */
        fun onQuerySearchRecordsSuccess(records: MutableList<String>?)

        /**
         * 获取搜索同城圈帖子列表回调
         */
        fun onQueryPostListSuccess(records: MutableList<PostDetail>?, isMore: Boolean)

        /**
         * 获取搜索憨妈课堂视频列表回调
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

    }

    interface Presenter : BasePresenter {

        /**
         * 获取搜索纪录列表
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


    }

}