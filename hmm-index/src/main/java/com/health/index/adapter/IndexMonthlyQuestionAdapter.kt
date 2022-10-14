package com.health.index.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.health.index.R
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.businessutil.ListUtil
import com.healthy.library.model.FaqExportQuestion
import com.healthy.library.routes.FaqRoutes
import com.healthy.library.utils.SpanUtils
import com.healthy.library.utils.TransformUtil
import com.healthy.library.widget.CornerImageView

class IndexMonthlyQuestionAdapter :
    BaseAdapter<String>(R.layout.index_item_monthly_question_adapter) {

    private var records: MutableList<FaqExportQuestion>? = null

    public fun setAdapterData(data: MutableList<FaqExportQuestion>) {
        this.records = data
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        var questionLayout = holder.getView<LinearLayout>(R.id.questionLayout)
        var more = holder.getView<ImageView>(R.id.more)
        var moreTxt = holder.getView<TextView>(R.id.moreTxt)
        more.setOnClickListener {
            if (isClickInit) {
                moutClickListener.outClick("跳转搜索问答", null)
            }
        }
        moreTxt.setOnClickListener {
            if (isClickInit) {
                moutClickListener.outClick("跳转搜索问答", null)
            }
        }
        questionLayout.removeAllViews()
        if (!ListUtil.isEmpty(records)) {
            for ((index, e) in records!!.withIndex()) {
                var view = LayoutInflater.from(context)
                    .inflate(R.layout.index_item_monthly_question_list, null)
                var userImg = view.findViewById<CornerImageView>(R.id.userImg)
                var tv_expert_title = view.findViewById<TextView>(R.id.tv_expert_title)
                var tipContent = view.findViewById<TextView>(R.id.tipContent)
                var userName = view.findViewById<TextView>(R.id.userName)
                var userTag = view.findViewById<TextView>(R.id.userTag)

                val layoutParams = ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                )
                layoutParams.setMargins(0, (TransformUtil.dp2px(context, 12f)).toInt(), 0, 0)
                view.layoutParams = layoutParams
                com.healthy.library.businessutil.GlideCopy.with(context)
                    .load(e.faceUrl)
                    .into(userImg)
                tv_expert_title.text = SpanUtils.getBuilder(context, "")
                    .append(" ")
                    .setResourceId(R.drawable.item_monthly_question_icon)
                    .append(" ")
                    .append(e.introduction).create()
                tipContent.text = SpanUtils.getBuilder(context, "")
                    .append(" ")
                    .setResourceId(R.drawable.item_monthly_question_ask)
                    .append(" ")
                    .append(e.replyDetail).create()
                userName.text = e.realName
                userTag.text = "\t擅长" + e.getExpertCategoryName()
                view.setOnClickListener {
                    ARouter.getInstance().build(FaqRoutes.FAQ_EXPERT_QUESTION_DETAIL)
                        .withString("questionId", e.questionId.toString() + "")
                        .withInt("pos", position)
                        .withBoolean("index", true)
                        .navigation()
                }
                tipContent.setOnClickListener {
                    ARouter.getInstance().build(FaqRoutes.FAQ_EXPERT_QUESTION_DETAIL)
                        .withString("questionId", e.questionId.toString() + "")
                        .withInt("pos", position)
                        .withBoolean("index", true)
                        .navigation()
                }
                questionLayout.addView(view)
            }
        }
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper()
    }

}