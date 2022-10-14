package com.health.index.contract

import com.healthy.library.model.NewsInfo
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
interface IndexBabyContract {
    interface View : BaseView {

        fun onQueryPostListSuccess(records: MutableList<PostDetail>?, isMore: Boolean)

        fun onQueryHmmVideoListSuccess(records: MutableList<VideoListModel>?, isMore: Boolean)

        fun onQueryVideoListSuccess(records: MutableList<VideoListModel>?, isMore: Boolean)

        fun onSuccessFan(result: Any?)

        fun onSuccessLike()

        fun onSuccessGetActivityList()

        fun onSuccessGetNewsList(result: MutableList<NewsInfo>?, isMore: Boolean) //4034

        fun onSuccessGetAudioNewsList(result: MutableList<SoundAlbum>?)

        fun onSuccessGetSoundAlbumsDownList(result: SoundHomeSplit?)

        fun successGetSoundAlbum(result: SoundTypeSplit?)
    }

    interface Presenter : BasePresenter {

        fun queryPostList(map: MutableMap<String, Any>)

        fun queryHmmVideoList(map: MutableMap<String, Any>)

        fun queryVideoList(map: MutableMap<String, Any>)

        fun like(map: MutableMap<String, Any>)

        fun fan(map: MutableMap<String, Any>)

        fun getActivityList(map: MutableMap<String, Any>, postDetail: PostDetail?)

        fun warn(map: MutableMap<String, Any>)

        fun getRecommendNews(map: MutableMap<String, Any>) // 4034

        fun getAudioNews(map: MutableMap<String, Any>)

        fun getSoundAlbumsDown(map: MutableMap<String, Any>)

        fun getSoundAlbums(map: MutableMap<String, Any>)

        /**
         *   文章点赞
         */
        fun articleLike(map: MutableMap<String, Any>)
    }
}