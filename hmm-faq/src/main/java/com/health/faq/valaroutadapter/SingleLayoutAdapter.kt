package com.health.faq.valaroutadapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.LayoutHelper
import com.health.faq.R
import com.healthy.library.loader.BannerImageLoader
import com.healthy.library.loader.ImageNetAdapter
import com.healthy.library.utils.TransformUtil
import com.youth.banner.Banner

/**
 * @author xinkai.xu
 * @date 2017.7.12
 *@property 轮播daapter
 * */
class SingleLayoutAdapter(context: Context, strings: MutableList<String>, helper: LayoutHelper, mViewTypeItem: Int) : DelegateAdapter.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    private val mHelper: LayoutHelper = helper
    private var mData: MutableList<String>? = null
    private var view: View? = null
    private var context: Context? = null
    private var mViewTypeItem = -1

    init {
        this.mData = strings
        this.context = context
        this.mViewTypeItem = mViewTypeItem
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        return mHelper
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        if (viewType == mViewTypeItem) {
            view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_single_layout, parent, false)
        }


        return RecyclerViewItemHolder(view!!)
    }

    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        val recyclerViewHolder = holder as RecyclerViewItemHolder
        recyclerViewHolder.banner?.let { banner ->
            banner.setDelayTime(3000)
            mData?.let {
                val imageLoader= ImageNetAdapter(it,TransformUtil.dp2px(context, 10f),null)
                banner.setAdapter(imageLoader)
                banner.start()
            }

        }

    }

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

        var banner: Banner<*, ImageNetAdapter> = itemView.findViewById(R.id.faqBanner)

    }
}
