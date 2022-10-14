package com.health.index.adapter

import androidx.appcompat.widget.AppCompatTextView
import com.alibaba.android.arouter.launcher.ARouter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.lib_ShapeView.view.ShapeTextView
import com.health.index.R
import com.healthy.library.businessutil.GlideCopy
import com.healthy.library.model.VideoListModel
import com.healthy.library.routes.IndexRoutes
import com.healthy.library.widget.CornerImageView

/**
 * author : long
 * Time   :2021/12/14
 * desc   :
 */
class IndexFeaturedCoursesBannerAdapter :
    BaseQuickAdapter<VideoListModel, BaseViewHolder>(R.layout.index_item_featured_courses_banner_layout) {

    override fun convert(helper: BaseViewHolder, item: VideoListModel) {
        val ivAvatar: CornerImageView
        val tvLookNum: ShapeTextView
        val tvTitle: AppCompatTextView
        val tvSubtitle: AppCompatTextView
        val tvIsPay: AppCompatTextView
        ivAvatar = helper.getView(R.id.iv_avatar)
        tvLookNum = helper.getView(R.id.tv_lookNum)
        tvTitle = helper.getView(R.id.tv_title)
        tvSubtitle = helper.getView(R.id.tv_subtitle)
        tvIsPay = helper.getView(R.id.tv_isPay)

        GlideCopy.with(mContext)
            .load(item.photo)
            .placeholder(R.drawable.img_avatar_default)
            .error(R.drawable.img_avatar_default)

            .into(helper.getView(R.id.iv_avatar))
        tvLookNum.setText((item.realView+item.virtualView).toString()+"人已看")
        tvTitle.setText(item.videoName)
        tvSubtitle.setText(item.videoRemark)
        if (item.isFree == 1) {
            tvIsPay.setText("免费")
        } else if (item.isFree == 2) {
            tvIsPay.setText("限时免费")
        } else if (item.isFree == 3) {
            tvIsPay.setText("￥" + item.videoPrice)
        }
        helper.itemView.setOnClickListener {
            ARouter.getInstance()
                .build(IndexRoutes.INDEX_HANMOMVIDEODETAIL)
                .withString("id",item.id)
                .withString("categoryCode",item.categoryCode)
                .navigation()
        }

    }

}