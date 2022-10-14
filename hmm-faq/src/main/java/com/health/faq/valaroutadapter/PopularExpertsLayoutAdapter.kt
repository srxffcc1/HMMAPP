package com.health.faq.valaroutadapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.alibaba.android.vlayout.layout.SingleLayoutHelper
import com.health.faq.R
import com.healthy.library.loader.BannerImageLoader
import com.healthy.library.utils.TransformUtil
import com.youth.banner.Banner
import kotlinx.android.synthetic.main.fragment_ask_the_experts.*
import com.umeng.commonsdk.stateless.UMSLEnvelopeBuild.mContext
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.launcher.ARouter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.health.faq.model.HotExpertInfoDTO
import com.healthy.library.routes.FaqRoutes


/*
* 热门专家
* */

class PopularExpertsLayoutAdapter(context: Context, strings: MutableList<HotExpertInfoDTO>, helper: LayoutHelper, viewTypeItem: Int) : DelegateAdapter.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>(), BaseQuickAdapter.OnItemClickListener {

    private val mHelper: LayoutHelper = helper
    private var mData: MutableList<HotExpertInfoDTO>? = null
    private var view: View? = null
    private var context: Context? = null
    private var mViewTypeItem = -1

    init {
        this.mData = strings
        this.context = context
        this.mViewTypeItem = viewTypeItem;
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        return mHelper
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        if (viewType == mViewTypeItem) {
            view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.adapter_popular_experts, parent, false)
        }
        return RecyclerViewItemHolder(view!!)
    }

    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as RecyclerViewItemHolder
        val linearLayoutManager = LinearLayoutManager(mContext)
        linearLayoutManager.orientation = androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
        viewHolder.rvExperts.layoutManager = linearLayoutManager
        var adapter: ExpertsItemAdapter = mData?.let { ExpertsItemAdapter(it) }!!
        viewHolder.rvExperts.adapter = adapter
        adapter.onItemClickListener = this

    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        var hotExpertInfoDTO: HotExpertInfoDTO = mData!![position]
        ARouter.getInstance().build(FaqRoutes.FAQ_EXPERT_HOMEPAGE)
                .withString("id", hotExpertInfoDTO.userId.toString())
                .navigation()
    }

    /**
     * 必须重写不然会出现滑动不流畅的情况
     *
     * @param position
     * @return
     */
    override fun getItemViewType(position: Int): Int {
        return mViewTypeItem
    }

    override fun getItemCount(): Int {
        return 1
    }

    /**
     * 正常条目的item的ViewHolder
     */
    private inner class RecyclerViewItemHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        var rvExperts: androidx.recyclerview.widget.RecyclerView = itemView.findViewById(R.id.rvExperts)//专家列表

    }
}
