package com.healthy.library.adapter

import android.annotation.SuppressLint
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.healthy.library.R
import com.healthy.library.businessutil.GlideCopy
import com.healthy.library.model.FaqExportQuestion
import com.healthy.library.routes.FaqRoutes
import com.healthy.library.utils.SpanUtils
import com.healthy.library.widget.ImageSpanCentre

/**
 * author : long
 * Time   :2021/12/13
 * desc   :
 */
class IndexExpertListAdapter :BaseQuickAdapter<FaqExportQuestion,BaseViewHolder>(R.layout.index_item_expert_layout) {

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun convert(helper: BaseViewHolder, item: FaqExportQuestion) {
        val drawable = mContext.resources.getDrawable(R.drawable.index_ask)
        helper.setText(R.id.tv_expert_title, SpanUtils.getBuilder(mContext,"")
            .append(" ")
            .setResourceId(R.drawable.index_ask)
            .append(" ")
            .append(item.introduction).create() )
            .setText(R.id.tv_expert_name,item.realName+" "+item.rankName)
            .setText(R.id.tv_expert_GoodAt,"擅长："+item.getExpertCategoryName())
            .setVisible(R.id.tv_expert_GoodAt,!TextUtils.isEmpty(item.getExpertCategoryName()))
        GlideCopy.with(mContext)
            .load(item.faceUrl)
            .placeholder(R.drawable.img_avatar_default)
            .error(R.drawable.img_avatar_default)
            
            .into(helper.getView(R.id.iv_expert_avatar))
        helper.itemView.setOnClickListener {
            ARouter.getInstance().build(FaqRoutes.FAQ_EXPERT_QUESTION_DETAIL)
                .withString("questionId", item.questionId.toString() + "")
                .withBoolean("index", true)
                .navigation()
        }
    }

}