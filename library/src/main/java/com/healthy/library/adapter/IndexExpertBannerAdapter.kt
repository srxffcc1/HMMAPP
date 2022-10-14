package com.healthy.library.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.chad.library.adapter.base.BaseViewHolder
import com.healthy.library.R
import com.healthy.library.businessutil.GlideCopy
import com.healthy.library.model.FaqExportQuestion
import com.healthy.library.routes.FaqRoutes
import com.healthy.library.utils.SpanUtils
import com.healthy.library.widget.RoundedImageView

/**
 * author : long
 * Time   :2021/12/13
 * desc   :
 */
class IndexExpertBannerAdapter : RecyclerView.Adapter<BaseViewHolder>() {

    private var records: MutableList<FaqExportQuestion>? = null
    private var context: Context? = null

    public fun setAdapterData(data: MutableList<FaqExportQuestion>, context: Context) {
        this.records = data
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.index_item_expert_layout, parent, false)
        return BaseViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        var tvExpertTitle = holder.getView<AppCompatTextView>(R.id.tv_expert_title)
        var ivExpertAvatar = holder.getView<RoundedImageView>(R.id.iv_expert_avatar)
        var tvExpertName = holder.getView<AppCompatTextView>(R.id.tv_expert_name)
        var tvExpertGoodAt = holder.getView<AppCompatTextView>(R.id.tv_expert_GoodAt)
        tvExpertTitle.text = SpanUtils.getBuilder(context, "")
            .append(" ")
            .setResourceId(R.drawable.index_ask)
            .append(" ")
            .append(records!![position].introduction).create()
        tvExpertName.text = records!![position].realName + " " + records!![position].rankName
        tvExpertGoodAt.text = "擅长：" + records!![position].getExpertCategoryName()
        GlideCopy.with(context)
            .load(records!![position].faceUrl)
            .placeholder(R.drawable.img_avatar_default)
            .error(R.drawable.img_avatar_default)
            .into(ivExpertAvatar)

        holder.itemView.setOnClickListener {
            ARouter.getInstance().build(FaqRoutes.FAQ_EXPERT_QUESTION_DETAIL)
                .withString("questionId", records!![position].questionId.toString() + "")
                .withBoolean("index", true)
                .navigation()
        }
    }

    override fun getItemCount(): Int {
        return if (records != null) records!!.size else 0
    }

}