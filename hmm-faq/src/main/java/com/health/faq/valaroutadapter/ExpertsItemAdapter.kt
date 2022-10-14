package com.health.faq.valaroutadapter

import android.graphics.Bitmap
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.health.faq.R
import com.health.faq.model.HotExpertInfoDTO

class ExpertsItemAdapter constructor(data: MutableList<HotExpertInfoDTO>) : BaseQuickAdapter<HotExpertInfoDTO, BaseViewHolder>(R.layout.adapter_experts_item, data) {
    override fun convert(helper: BaseViewHolder, item: HotExpertInfoDTO) {
        helper.setText(R.id.tvExpertsName, item.realName)
                .setText(R.id.tvExpertsType, item.rank)
                .setText(R.id.tvExpertsAnswers, "${item.replyCount}个回答")
        var imageView: ImageView = helper.getView(R.id.ivExpertsHead)
        //头像
        com.healthy.library.businessutil.GlideCopy.with(mContext)
                .asBitmap()
                .load(item.faceUrl)
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_avatar_default)
                
                .centerCrop()
                .into(object : BitmapImageViewTarget(imageView) {
                    override fun setResource(resource: Bitmap?) {
                        val circularBitmapDrawable = RoundedBitmapDrawableFactory.create(mContext.resources, resource)
                        circularBitmapDrawable.isCircular = true
                        imageView.setImageDrawable(circularBitmapDrawable)
                    }
                })


    }

}

