package com.health.city.contract

import com.healthy.library.base.BasePresenter
import com.healthy.library.base.BaseView
import com.healthy.library.model.PostDetail

/**
 * author : long
 * Time   :2021/11/9
 * desc   :
 */
interface EssencesContract {

    interface View : BaseView {
        /**
         * 获取搜索同城圈帖子列表回调
         */
        fun onQueryPostListSuccess(records: MutableList<PostDetail>?, isMore: Boolean)

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
         * 获取精华帖列表
         */
        fun getPostingList(map: MutableMap<String, Any>)

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