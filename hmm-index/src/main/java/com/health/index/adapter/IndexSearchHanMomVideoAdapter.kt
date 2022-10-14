package com.health.index.adapter

import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.health.index.R
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.model.VideoListModel
import com.healthy.library.routes.IndexRoutes
import com.healthy.library.routes.ServiceRoutes

/**
 * 创建日期：2021/12/15 10:34
 * @author LiuWei
 * @version 1.0
 * 包名： com.health.index.adapter
 * 类说明：
 */

class IndexSearchHanMomVideoAdapter :
    BaseAdapter<VideoListModel>(R.layout.item_index_search_hanmom_video_adapter_layout) {

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        holder.setImg(R.id.iv_avatar, datas[position].photo)
        holder.setText(R.id.tv_title, datas[position].videoName)
        holder.setText(R.id.tv_subtitle, datas[position].videoRemark)
        holder.setText(
            R.id.tv_lookNum,
            (datas[position].realView + datas[position].virtualView).toString() + "人已看"
        )
        when (datas[position].isFree) {
            1 -> holder.setText(R.id.tv_isPay, "免费")
            2 -> holder.setText(R.id.tv_isPay, "限时免费")
            3 -> holder.setText(R.id.tv_isPay, "￥" + datas[position].videoPrice)
        }
        holder.itemView.setOnClickListener {
            ARouter.getInstance()
                .build(IndexRoutes.INDEX_HANMOMVIDEODETAIL)
                .withString("id", datas[position].id)
                .withString("categoryCode", datas[position].categoryCode)
                .navigation()
        }
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper()
    }
}