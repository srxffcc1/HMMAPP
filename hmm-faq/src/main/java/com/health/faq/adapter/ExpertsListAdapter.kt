package com.health.faq.adapter

import android.graphics.Bitmap
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.health.faq.R
import com.health.faq.model.Expert
import com.healthy.library.utils.FormatUtils
import org.jsoup.Jsoup

/**
 * @author xinkai.xu
 * 专家列表
 * */
class ExpertsListAdapter constructor(data: MutableList<Expert>) : BaseQuickAdapter<Expert, BaseViewHolder>(R.layout.adapter_expert_list, data) {

    override fun convert(helper: BaseViewHolder, item: Expert) {
        helper.setText(R.id.tvName, item.realName)
                .setText(R.id.tvTitle, item.rank)
                .setText(R.id.tvAskCount, "${item.counts}个回答")
                .setText(R.id.tvAskMoney, "￥${FormatUtils.formatRewardMoney(item.consultingFees.toString())}")



        try {
            helper.setText(R.id.tvBeGoodAt, "擅长：${com.healthy.library.utils.JsoupCopy.parse(item.goodSkills).text()}")
        } catch (e: Exception) {
        }
        val headerImage: ImageView = helper.getView(R.id.ivHeader)
        //头像
        com.healthy.library.businessutil.GlideCopy.with(mContext)
                .asBitmap()
                .load(item.faceUrl)
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_avatar_default)
                
                .centerCrop()
                .into(object : BitmapImageViewTarget(headerImage) {
                    override fun setResource(resource: Bitmap?) {
                        val circularBitmapDrawable = RoundedBitmapDrawableFactory.create(mContext.resources, resource)
                        circularBitmapDrawable.isCircular = true
                        headerImage.setImageDrawable(circularBitmapDrawable)
                    }
                })
        helper.addOnClickListener(R.id.ivAsk)


    }
}