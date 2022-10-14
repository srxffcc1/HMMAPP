package com.health.index.adapter

import android.util.Base64
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
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

class IndexSearchQuestionAdapter :
    BaseAdapter<String>(R.layout.index_search_question_list_adapter_layout) {

    private var mBannerAdapter: IndexSpeedyConsultationAdapter? = null
    private var mAutoPollRecyclerView: AutoPollRecyclerView? = null

    private var records: MutableList<FaqExportQuestion>? = null
    private var mList = mutableListOf<AppQuestionTmp>()

    public fun setAdapterData(data: MutableList<FaqExportQuestion>) {
        this.records = data
    }

    public fun addAdapterData(data: MutableList<FaqExportQuestion>) {
        this.records?.addAll(data)
        notifyItemRangeInserted(this.records!!.size, data.size)
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        var questionLiner = holder.getView<LinearLayout>(R.id.questionLiner)
        var questionAll = holder.getView<AppCompatTextView>(R.id.questionAll)
        // 极速问诊
        mAutoPollRecyclerView =
            holder.getView<AutoPollRecyclerView>(com.healthy.library.R.id.banner_SpeedyConsultation)
        if (mBannerAdapter == null) {
            mList.clear()
            mList.clear()
            mList.add(AppQuestionTmp("儿科", "请问宝宝经常吐奶，头往后仰，是不是奶粉过敏牛乳蛋白过敏导致的？"))
            mList.add(AppQuestionTmp("妇产科", "产后一周，奶水不足，怎么办？"))
            mList.add(AppQuestionTmp("儿科", "6个月的宝宝经常拉绿便便是什么回事？"))
            mList.add(AppQuestionTmp("儿科", "宝贝背部涨了很多小红痘痘，手臂上也有，请问这是国民还是什么？"))
            mList.add(
                AppQuestionTmp(
                    "儿科",
                    "我家宝2个多月，咳嗽有痰，去医院听诊说是肺炎，体温持续37.5，今天做的雾化。阿莫西林克拉维酸钾吃了3天还是咳，尤其是雾化做完后咳嗽加重"
                )
            )
            mList.add(
                AppQuestionTmp(
                    "儿科",
                    "拉肚子一天3-5次，绿色带有黏液，有时候有黑色，吃了蒙脱石散和双歧杆菌四联活菌也没什么效果，目母乳不够，混合喂养，宝宝28号满3个月 "
                )
            )
            mList.add(
                AppQuestionTmp(
                    "儿科",
                    "您好医生，我家孩子五个月，晚上睡觉睡不踏实，老是翻来翻去，频繁夜醒是怎么回事，每天都十一二才睡，白天也不咋睡"
                )
            )
            mList.add(AppQuestionTmp("儿科", "孩子一到春天就全身都是包"))
            mList.add(
                AppQuestionTmp(
                    "儿科",
                    "宝宝快4个月了，最近母乳严重不足，刚给宝宝添加奶粉，宝宝不吃奶粉也不吸奶瓶，每次给他吃奶粉只吃15毫升，现在是2个小时喂一次，在喂就不吃了，他也感觉不到饿，一天就昏睡，尿量也没有，也不敢给他吃母乳，怕吃了母乳更加不吃奶粉，不知道怎么办了，急！"
                )
            )
            mList.add(
                AppQuestionTmp(
                    "妇产科",
                    "你好医生 我生完小孩一个月了 从孩子刚出生就没奶水 到现在也一直没有 一点涨的感觉都没有 乳房软软的 是不是有什么病呀 还有可能有奶水吗"
                )
            )
            mBannerAdapter = IndexSpeedyConsultationAdapter()
            mAutoPollRecyclerView?.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = mBannerAdapter
                mBannerAdapter?.setData(mList)
                isNestedScrollingEnabled = false
                start()
            }
        }
        holder.itemView.setOnClickListener {
            var url =
                "https://h5.yewyw.com/index.html?bmark=hmama&appid=977&flag=app&appUserId=%s&appUserPhone=18511557625#/ConsultQuick"
            url = String.format(
                url!!,
                String(
                    Base64.decode(
                        SpUtils.getValue(context, SpKey.USER_ID).toByteArray(),
                        Base64.DEFAULT
                    )
                )
            )
            ARouter.getInstance()
                .build(IndexRoutes.INDEX_WEBVIEW)
                .withBoolean("isNeedRef", true)
                .withString("title", "极速问诊")
                .withBoolean("isinhome", false)
                .withBoolean("doctorshop", true)
                .withString("url", url)
                .navigation()
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