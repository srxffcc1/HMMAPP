package com.health.index.adapter

import android.text.Html
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.example.lib_ShapeView.view.ShapeTextView
import com.health.index.R
import com.healthy.library.LibApplication
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.constant.UrlKeys
import com.healthy.library.model.*
import com.healthy.library.routes.IndexRoutes
import com.healthy.library.routes.SoundRoutes
import com.healthy.library.utils.JsoupCopy
import com.healthy.library.utils.SpUtils
import com.healthy.library.utils.TransformUtil
import com.healthy.library.widget.ImageTextView
import java.net.URLEncoder

/**
 * author : long
 * Time   :2021/12/13
 * desc   : 憨妈头条
 */
class IndexHeadlinesAdapter :
    BaseAdapter<AppIndexCustomNews>(R.layout.index_headlines_item_layout) {

    override fun getItemViewType(position: Int): Int {
        return 4
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        val linearLayoutHelper = LinearLayoutHelper()
        linearLayoutHelper.marginLeft = TransformUtil.newDp2px(LibApplication.getAppContext(), 10f)
        linearLayoutHelper.marginRight = TransformUtil.newDp2px(LibApplication.getAppContext(), 10f)
        return linearLayoutHelper
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        val appIndexCustomNews = model

        val tvHeadlinesOneTitle: AppCompatTextView
        val tvHeadlinesOneBody: AppCompatTextView
        val stvHeadlinesOneAction: ShapeTextView
        val tvHeadlinesTwoTitle: AppCompatTextView
        val tvHeadlinesTwoBody: AppCompatTextView
        val stvHeadlinesTwoAction: ShapeTextView
        val tvCanNotEat: AppCompatTextView

        tvHeadlinesOneTitle = holder.getView<AppCompatTextView>(R.id.tv_headlines_one_title)
        tvHeadlinesOneBody = holder.getView<AppCompatTextView>(R.id.tv_headlines_one_body)
        stvHeadlinesOneAction = holder.getView<ShapeTextView>(R.id.stv_headlines_one_action)
        tvHeadlinesTwoTitle = holder.getView<AppCompatTextView>(R.id.tv_headlines_two_title)
        tvHeadlinesTwoBody = holder.getView<AppCompatTextView>(R.id.tv_headlines_two_body)
        stvHeadlinesTwoAction = holder.getView<ShapeTextView>(R.id.stv_headlines_two_action)
        tvCanNotEat = holder.getView<AppCompatTextView>(R.id.tv_canNotEat)
        val toolEatTip1: ImageTextView
        val toolEatTip2: ImageTextView
        val toolEatTip3: ImageTextView
        val toolEatTip4: ImageTextView
        toolEatTip1 = holder.itemView.findViewById<View>(R.id.toolEatTip1) as ImageTextView
        toolEatTip2 = holder.itemView.findViewById<View>(R.id.toolEatTip2) as ImageTextView
        toolEatTip3 = holder.itemView.findViewById<View>(R.id.toolEatTip3) as ImageTextView
        toolEatTip4 = holder.itemView.findViewById<View>(R.id.toolEatTip4) as ImageTextView
        tvHeadlinesOneTitle.post {
            val oneMaxLines = if (tvHeadlinesOneTitle.lineCount > 1) 1 else 2
            val twoMaxLines = if (tvHeadlinesOneTitle.lineCount > 1) 1 else 2
            //加一层校验，避免多次触发绘制
            if (tvHeadlinesOneBody.maxLines != oneMaxLines) {
                tvHeadlinesOneBody.maxLines = oneMaxLines
            }
            if (tvHeadlinesTwoBody.maxLines != twoMaxLines) {
                tvHeadlinesTwoBody.maxLines = twoMaxLines
            }
        }
        //var album: SoundAlbum? = appIndexCustomNews.album
        var monthlytip: AppIndexCustomNews.ActivityInfo? = appIndexCustomNews.monthlytip
        var notCanEat: ToolsCEList? = appIndexCustomNews.notCanEat
        var knowledge: NewsInfo? = appIndexCustomNews.knowledge
        var eatSuggestInfo: ToolsFoodTop? = appIndexCustomNews.eatSuggestInfo

        if (monthlytip != null) {//备孕期
            stvHeadlinesOneAction.setText("憨妈专题")
            stvHeadlinesTwoAction.setText("知识推荐")
            tvHeadlinesOneTitle.text = ""
            tvHeadlinesOneBody.text = ""
            holder.itemView.findViewById<View>(R.id.cl_headlines_one).setOnClickListener {
            }
            holder.itemView.findViewById<View>(R.id.cl_headlines_two).setOnClickListener {
            }
            holder.itemView.findViewById<View>(R.id.eatParent).setOnClickListener {

            }
            monthlytip?.let {
                tvHeadlinesOneTitle.text = it.name
                tvHeadlinesOneBody.text = JsoupCopy.parse(it.description).text()
                holder.itemView.findViewById<View>(R.id.cl_headlines_one).setOnClickListener {
                    if (monthlytip != null) {
                        ARouter.getInstance()
                            .build(IndexRoutes.INDEX_MONTHLY)
                            .withString("specialld", monthlytip.id.toString() + "")
                            .navigation()
                    }
                }
            }
            knowledge?.let {
                tvHeadlinesTwoTitle.setText(it.title)
                tvHeadlinesTwoBody.setText(JsoupCopy.parse(it.content).text())
                holder.itemView.findViewById<View>(R.id.cl_headlines_two).setOnClickListener {
                    if (knowledge != null) {
                        val urlPrefix = SpUtils.getValue(context, UrlKeys.H5_KNOWLEDGE)
                        val url =
                            String.format("%s?id=%s&scheme=NewsMessage", urlPrefix, knowledge.id)
                        ARouter.getInstance()
                            .build(IndexRoutes.INDEX_WEBVIEWARTICLE)
                            .withString("title", "资讯")
                            .withBoolean("needShare", true)
                            .withBoolean("isinhome", true)
                            .withBoolean("needfindcollect", true)
                            .withString("url", url)
                            .navigation()
                    }

                }
            }
            notCanEat?.let {
                tvCanNotEat.setText(it.foodName)
                toolEatTip1.setDrawable(it.getCanEatImgRes("1"), context)
                toolEatTip2.setDrawable(it.getCanEatImgRes("2"), context)
                toolEatTip3.setDrawable(it.getCanEatImgRes("3"), context)
                toolEatTip4.setDrawable(it.getCanEatImgRes("4"), context)
                holder.itemView.findViewById<View>(R.id.eatParent).setOnClickListener {
                    val result: String = notCanEat.getCanEatStringRes()
                    val urlPrefix = SpUtils.getValue(context, UrlKeys.H5_CAN_EAT)
                    val url = String.format(
                        "%s?id=%s&foodName=%s&scheme=CanEatDetail&foodId=%s",
                        urlPrefix,
                        notCanEat.id.toString() + "",
                        URLEncoder.encode(notCanEat.foodName),
                        notCanEat.id.toString() + ""
                    )
                    ARouter.getInstance()
                        .build(IndexRoutes.INDEX_WEBVIEW_ALL)
                        .withString("title", notCanEat.foodName)
                        .withString("url", url)
                        .withBoolean("needShare", true)
                        .withBoolean("isinhome", true)
                        .withBoolean("doctorshop", true)
                        .withString("zbitmap", notCanEat.image)
                        .withString("stitle", "能不能吃-" + notCanEat.foodName)
                        .withString("sdes", result)
                        .navigation()
                }
            }
        } else {//育儿期 怀孕中
            stvHeadlinesOneAction.setText("食谱推荐")
            stvHeadlinesTwoAction.setText("知识推荐")
            tvHeadlinesOneTitle.text = ""
            tvHeadlinesOneBody.text = ""
            holder.itemView.findViewById<View>(R.id.cl_headlines_one).setOnClickListener {
            }
            holder.itemView.findViewById<View>(R.id.cl_headlines_two).setOnClickListener {
            }
            holder.itemView.findViewById<View>(R.id.eatParent).setOnClickListener {

            }
            eatSuggestInfo?.let {
                tvHeadlinesOneTitle.setText(it.title)
                tvHeadlinesOneBody.setText(JsoupCopy.parse(it.detail).text())
                holder.itemView.findViewById<View>(R.id.cl_headlines_one).setOnClickListener {
                    if (eatSuggestInfo != null) {
                        if ("1".equals(eatSuggestInfo.suggestType)) {
                            ARouter.getInstance()
                                .build(IndexRoutes.INDEX_TOOLS_FOOD)
                                .withString("activityType", "月子食谱")
                                .navigation()
                        } else if ("2".equals(eatSuggestInfo.suggestType)) {
                            ARouter.getInstance()
                                .build(IndexRoutes.INDEX_TOOLS_FOOD)
                                .withString("activityType", "宝宝辅食")
                                .navigation()
                        } else {
                            ARouter.getInstance()
                                .build(IndexRoutes.INDEX_TOOLS_FOOD)
                                .withString("activityType", "孕期食谱")
                                .navigation()
                        }

                    }

                }
            }
            knowledge?.let {
                tvHeadlinesTwoTitle.setText(it.title)
                tvHeadlinesTwoBody.setText(JsoupCopy.parse(it.content).text())
                holder.itemView.findViewById<View>(R.id.cl_headlines_two).setOnClickListener {
                    if (knowledge != null) {
                        val urlPrefix = SpUtils.getValue(context, UrlKeys.H5_KNOWLEDGE)
                        val url =
                            String.format("%s?id=%s&scheme=NewsMessage", urlPrefix, knowledge.id)
                        ARouter.getInstance()
                            .build(IndexRoutes.INDEX_WEBVIEWARTICLE)
                            .withString("title", "资讯")
                            .withBoolean("needShare", true)
                            .withBoolean("isinhome", true)
                            .withBoolean("needfindcollect", true)
                            .withString("url", url)
                            .navigation()
                    }

                }
            }
            notCanEat?.let {
                tvCanNotEat.setText(it.foodName)
                toolEatTip1.setDrawable(it.getCanEatImgRes("1"), context)
                toolEatTip2.setDrawable(it.getCanEatImgRes("2"), context)
                toolEatTip3.setDrawable(it.getCanEatImgRes("3"), context)
                toolEatTip4.setDrawable(it.getCanEatImgRes("4"), context)
                holder.itemView.findViewById<View>(R.id.eatParent).setOnClickListener {
                    val result: String = notCanEat.getCanEatStringRes()
                    val urlPrefix = SpUtils.getValue(context, UrlKeys.H5_CAN_EAT)
                    val url = String.format(
                        "%s?id=%s&foodName=%s&scheme=CanEatDetail&foodId=%s",
                        urlPrefix,
                        notCanEat.id.toString() + "",
                        URLEncoder.encode(notCanEat.foodName),
                        notCanEat.id.toString() + ""
                    )
                    ARouter.getInstance()
                        .build(IndexRoutes.INDEX_WEBVIEW_ALL)
                        .withString("title", notCanEat.foodName)
                        .withString("url", url)
                        .withBoolean("needShare", true)
                        .withBoolean("isinhome", true)
                        .withBoolean("doctorshop", true)
                        .withString("zbitmap", notCanEat.image)
                        .withString("stitle", "能不能吃-" + notCanEat.foodName)
                        .withString("sdes", result)
                        .navigation()
                }
            }

        }


    }

}