package com.health.index.adapter

import android.util.Base64
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.health.index.R
import com.healthy.library.model.IndexAllQuestion
import com.healthy.library.LibApplication
import com.healthy.library.adapter.IndexSpeedyConsultationAdapter
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.businessutil.ListUtil
import com.healthy.library.constant.SpKey
import com.healthy.library.model.AppQuestionTmp
import com.healthy.library.model.FaqExportQuestion
import com.healthy.library.routes.FaqRoutes
import com.healthy.library.routes.IndexRoutes
import com.healthy.library.utils.SpUtils
import com.healthy.library.utils.SpanUtils
import com.healthy.library.utils.TransformUtil
import com.healthy.library.widget.AutoPollRecyclerView
import com.healthy.library.widget.RoundedImageView

/**
 * 创建日期：2021/12/23 10:10
 * @author LiuWei
 * @version 1.0
 * 包名： com.health.index.adapter
 * 类说明：
 */

class IndexSearchAllQuestionAdapter :
    BaseAdapter<String>(R.layout.index_search_all_question_list_adapter_layout) {

    private var key: String? = null
    private var records: MutableList<FaqExportQuestion>? = null

    public fun setAdapterData(data: MutableList<FaqExportQuestion>) {
        this.records = data
    }
    public fun setKey(key: String) {
        this.key = key
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        var questionLiner = holder.getView<LinearLayout>(R.id.questionLiner)
        var more = holder.getView<AppCompatImageView>(R.id.more)
        holder.setText(R.id.keyTitle, key)
        more.setOnClickListener {
            if (isClickInit) {
                moutClickListener.outClick("跳转搜索问答", null)
            }
        }
        questionLiner.removeAllViews()
        if (!ListUtil.isEmpty(records)) {
            for ((index, e) in records!!.withIndex()) {
                var view = LayoutInflater.from(context)
                    .inflate(R.layout.item_index_search_question_list_layout, null)
                var iv_expert_avatar = view.findViewById<RoundedImageView>(R.id.iv_expert_avatar)
                var tv_expert_title = view.findViewById<TextView>(R.id.tv_expert_title)
                var tv_expert_name = view.findViewById<TextView>(R.id.tv_expert_name)
                var tv_expert_GoodAt = view.findViewById<TextView>(R.id.tv_expert_GoodAt)
                com.healthy.library.businessutil.GlideCopy.with(context)
                    .load(e.faceUrl)
                    
                    .into(iv_expert_avatar)
                tv_expert_title.text = SpanUtils.getBuilder(context, "")
                    .append(" ")
                    .setResourceId(com.healthy.library.R.drawable.index_ask)
                    .append(" ")
                    .append(e.introduction).create()
                tv_expert_name.text = e.realName + "\t" + e.rankName
                tv_expert_GoodAt.text = "擅长：" + e.getExpertCategoryName()
                view.setOnClickListener {
                    ARouter.getInstance().build(FaqRoutes.FAQ_EXPERT_QUESTION_DETAIL)
                        .withString("questionId", e.questionId.toString() + "")
                        .withInt("pos", position)
                        .withBoolean("index", true)
                        .navigation()
                }
                questionLiner.addView(view)
            }
        }
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        val linearLayoutHelper = LinearLayoutHelper()
        linearLayoutHelper.marginTop = TransformUtil.newDp2px(LibApplication.getAppContext(), 12f)
        linearLayoutHelper.marginLeft = TransformUtil.newDp2px(LibApplication.getAppContext(), 10f)
        linearLayoutHelper.marginRight = TransformUtil.newDp2px(LibApplication.getAppContext(), 10f)
        return linearLayoutHelper
    }
}