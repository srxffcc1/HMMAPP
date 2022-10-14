package com.health.index.adapter

import android.widget.Toast
import com.alibaba.android.arouter.launcher.ARouter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.health.index.R
import com.healthy.library.businessutil.GlideCopy
import com.healthy.library.model.AppIndexList
import com.healthy.library.routes.SecondRoutes
import com.healthy.library.utils.MARouterUtils
import com.healthy.library.utils.ScreenUtils
import com.healthy.library.utils.TransformUtil

/**
 * author : long
 * Time   :2021/12/14
 * desc   :
 */
class IndexFeaturedBannerAdapter :
    BaseQuickAdapter<AppIndexList, BaseViewHolder>(R.layout.index_item_featured_banner_layout) {

    override fun convert(helper: BaseViewHolder, item: AppIndexList) {

        var width = (ScreenUtils.getScreenWidth(mContext) - TransformUtil.dp2px(mContext, 40f)) / 2
        var pram = helper.itemView.layoutParams
        pram.width = width.toInt()

        helper.setText(R.id.tv_title, item.mainTitle)
            .setText(R.id.tv_subtitle, item.subTitle)
        GlideCopy.with(mContext)
            .load(item.imageUrl)
            .placeholder(R.drawable.img_690_260_default)
            .error(R.drawable.img_690_260_default)
            .into(helper.getView(R.id.iv_item_bg))
        helper.itemView.setOnClickListener {
            MARouterUtils.passToTarget(mContext, item)

        }
    }

}