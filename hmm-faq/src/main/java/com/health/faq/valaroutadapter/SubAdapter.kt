package com.health.faq.valaroutadapter

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.health.faq.R
import com.health.faq.model.Item

 class SubAdapter constructor(ifLoadMore: Boolean, helper: LayoutHelper) : DelegateAdapter.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    private val mHelper: LayoutHelper = helper
    private var view: View? = null
    public var ifLoadMore: Boolean = ifLoadMore


    override fun onCreateLayoutHelper(): LayoutHelper {
        return mHelper
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_footer, parent, false)

        return RecyclerViewItemHolder(view!!)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as RecyclerViewItemHolder
        if (ifLoadMore) {
            viewHolder.textView.setText(R.string.loading)
            viewHolder.progressbar.visibility = View.VISIBLE
        } else {
            viewHolder.textView.setText(R.string.loadfinish)
            viewHolder.progressbar.visibility = View.GONE
        }

    }

    override fun getItemCount(): Int {
        return 1
    }

    /**
     * 正常条目的item的ViewHolder
     */
    private inner class RecyclerViewItemHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        var textView: TextView = itemView.findViewById(R.id.textView)
        var progressbar: ProgressBar = itemView.findViewById(R.id.progressbar)


    }
}
