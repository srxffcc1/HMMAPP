package com.health.index.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.health.index.R
import com.healthy.library.LibApplication
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.businessutil.GlideCopy
import com.healthy.library.businessutil.ListUtil
import com.healthy.library.model.AppIndexList
import com.healthy.library.model.AppIndexService
import com.healthy.library.routes.SecondRoutes
import com.healthy.library.utils.MARouterUtils
import com.healthy.library.utils.TransformUtil

/**
 * author : long
 * Time   :2021/12/14
 * desc   :
 */
class IndexServiceProjectAdapter :
    BaseAdapter<AppIndexService>(R.layout.index_service_project_layout) {

    private var mItemHeight = 0

    private lateinit var listener: (mViewHeight: Int, className: String) -> Unit

    fun getItemHeight(listener: (mViewHeight: Int, className: String) -> Unit) {
        this.listener = listener
    }

    override fun getItemViewType(position: Int): Int {
        return 8
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        val linearLayoutHelper = LinearLayoutHelper()
        linearLayoutHelper.marginTop = TransformUtil.newDp2px(LibApplication.getAppContext(), 12f)
        linearLayoutHelper.marginLeft = TransformUtil.newDp2px(LibApplication.getAppContext(), 10f)
        linearLayoutHelper.marginRight = TransformUtil.newDp2px(LibApplication.getAppContext(), 10f)
        return linearLayoutHelper
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        if (::listener.isInitialized && mItemHeight == 0) {
            holder.itemView.post {
                mItemHeight = holder.itemView.height + TransformUtil.newDp2px(
                    LibApplication.getAppContext(),
                    12f
                )
                listener.invoke(mItemHeight, this.javaClass.simpleName)
            }
        }
        val appIndexService = datas[position]
        val tv_title = holder.getView<TextView>(R.id.tv_title)
        val tv_subtitle = holder.getView<TextView>(R.id.tv_subtitle)
        val serviceList = holder.getView<LinearLayout>(R.id.serviceList)
        tv_title.setText(appIndexService.title.mainTitle)
        tv_subtitle.setText(appIndexService.title.subTitle)
        buildRecommandList(serviceList, appIndexService)
//        holder.itemView.setOnClickListener {
//            ARouter.getInstance()
//                .build(SecondRoutes.MAIN_MODULE)
//                .navigation()
//        }
    }

    fun buildRecommandList(linearLayout: LinearLayout, appIndexService: AppIndexService) {
        linearLayout.removeAllViews()
        if (ListUtil.isEmpty(appIndexService.list)){
            return
        }
        for (index in 0 until appIndexService!!.list.size) {
            val view = LayoutInflater.from(context)
                .inflate(R.layout.index_recommend_goods_layout, linearLayout, false)
            var goodsMain: AppIndexList = appIndexService!!.list.get(index) as AppIndexList
            GlideCopy.with(context)
                .load(goodsMain.imageUrl)
                .placeholder(R.drawable.img_1_1_default)
                .error(R.drawable.img_1_1_default)
                .into(view.findViewById(R.id.iv_avatar))
            val tv_title = view.findViewById<TextView>(R.id.tv_title)
            val tvsub_title = view.findViewById<TextView>(R.id.tv_goods_label)
            tv_title.setText(goodsMain.mainTitle)
            tvsub_title.setText(goodsMain.subTitle)
            if (index == 0) {
                tvsub_title.setTextColor(Color.parseColor("#E57D26"))
            } else if (index == 1) {
                tvsub_title.setTextColor(Color.parseColor("#4674DB"))

            } else if (index == 2) {
                tvsub_title.setTextColor(Color.parseColor("#F335B7"))

            } else {
                tvsub_title.setTextColor(Color.parseColor("#FF2222"))

            }
            view.setOnClickListener {
                ARouter.getInstance()
                    .build(SecondRoutes.MAIN_BLOCKWITHMAIN)
                    .withString("id", goodsMain.id)
                    .withString("bannerUrl", goodsMain.listImageUrl)
                    .withString("mainTitle", goodsMain.mainTitle)
                    .navigation()
//                MARouterUtils.passToTarget(context,goodsMain)
            }
            linearLayout.addView(view)
        }
        var needFixSize = 4 - appIndexService!!.list.size
        for (index in 0 until needFixSize) {
            val view = LayoutInflater.from(context)
                .inflate(R.layout.index_recommend_goods_layout, linearLayout, false)
            view.visibility = View.INVISIBLE
            linearLayout.addView(view)
        }
    }

}