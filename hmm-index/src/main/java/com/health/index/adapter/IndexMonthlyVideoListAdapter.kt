package com.health.index.adapter

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.health.index.R
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.model.VideoListModel
import com.healthy.library.routes.IndexRoutes
import com.healthy.library.utils.DrawableUtils

class IndexMonthlyVideoListAdapter :
    BaseAdapter<VideoListModel>(R.layout.index_item_monthly_video_list_adapter) {
    var sBitmap: Bitmap? = null
    var shareVideo: ShareVideoListener? = null

    public fun setShareVideoListener(data: ShareVideoListener) {
        this.shareVideo = data
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        holder.setText(R.id.videoTitle, datas[position].videoName)
        holder.setText(
            R.id.seeNum,
            (datas[position].realView + datas[position].virtualView).toString()
        )
        holder.setText(R.id.commentNum, datas[position].discussCount.toString())
        holder.setText(R.id.likeNum, datas[position].praiseCount.toString())
        holder.setImg(R.id.videoImg, datas[position].photo)
        Glide.with(context).load(datas[position].photo)
            .placeholder(R.drawable.img_1_1_default2)
            .error(R.drawable.img_1_1_default)
            .into(object : SimpleTarget<Drawable?>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable?>?
                ) {
                    sBitmap = DrawableUtils.drawableToBitmap(resource)
                }
            })
        holder.setOnClickListener(
            R.id.shareLayout,
            View.OnClickListener {
                shareVideo!!.shareVideo(
                    datas[position].id,
                    datas[position].videoName,
                    sBitmap!!
                )
            })
        holder.itemView.setOnClickListener {
            ARouter.getInstance()
                .build(IndexRoutes.INDEX_HANMOMVIDEODETAIL)
                .withString("id",  datas[position].id)
                .withString("categoryCode",  datas[position].categoryCode)
                .navigation()
        }
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper()
    }

    public interface ShareVideoListener {
        fun shareVideo(id: String, title: String, bitmap: Bitmap)
    }
}