package com.health.index.adapter

import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.health.index.R
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.businessutil.ListUtil
import com.healthy.library.model.VideoListModel
import com.healthy.library.routes.IndexRoutes
import com.healthy.library.widget.CornerImageView

/**
 * 创建日期：2022/1/4 14:17
 * @author LiuWei
 * @version 1.0
 * 包名： com.health.index.adapter
 * 类说明：
 */

class IndexSearchAllHmmVideoAdapter :
    BaseAdapter<String>(R.layout.index_search_all_hmm_video_adapter_layout) {
    private var key: String? = null
    private var records: MutableList<VideoListModel>? = null

    public fun setKey(key: String) {
        this.key = key
    }

    public fun setAdapterData(data: MutableList<VideoListModel>) {
        this.records = data
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        var videoLiner = holder.getView<LinearLayout>(R.id.videoLiner)
        var more = holder.getView<AppCompatImageView>(R.id.more)
        more.setOnClickListener {
            if (isClickInit) {
                moutClickListener.outClick("跳转搜索课堂", null)
            }
        }
        videoLiner.removeAllViews()
        holder.setText(R.id.keyTitle, key)
        if (!ListUtil.isEmpty(records)) {
            for ((index, e) in records!!.withIndex()) {
                var view = LayoutInflater.from(context)
                    .inflate(R.layout.item_index_search_all_hmm_video, null)
                var iv_avatar = view.findViewById<CornerImageView>(R.id.iv_avatar)
                var tv_title = view.findViewById<TextView>(R.id.tv_title)
                var tv_subtitle = view.findViewById<TextView>(R.id.tv_subtitle)
                var tv_lookNum = view.findViewById<TextView>(R.id.tv_lookNum)
                var tv_isPay = view.findViewById<TextView>(R.id.tv_isPay)
                var line = view.findViewById<View>(R.id.line)
                com.healthy.library.businessutil.GlideCopy.with(context)
                    .load(e.photo)

                    .into(iv_avatar)
                tv_title.text = e.videoName
                tv_subtitle.text = e.videoRemark
                tv_lookNum.text = (e.realView + e.virtualView).toString() + "人已看"
                when (e.isFree) {
                    1 -> tv_isPay.text = "免费"
                    2 -> tv_isPay.text = "限时免费"
                    3 -> tv_isPay.text = "￥" + e.videoPrice
                }
                if (index == records!!.size - 1) {
                    line.visibility = View.INVISIBLE
                } else {
                    line.visibility = View.VISIBLE
                }
                view.setOnClickListener {
                    ARouter.getInstance()
                        .build(IndexRoutes.INDEX_HANMOMVIDEODETAIL)
                        .withString("id", e.id)
                        .withString("categoryCode", e.categoryCode)
                        .navigation()
                }
                videoLiner.addView(view)
            }
        }
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper()
    }
}

