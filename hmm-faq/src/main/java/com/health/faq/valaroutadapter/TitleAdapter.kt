package com.health.faq.valaroutadapter

import android.app.Activity
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.LayoutHelper
import com.health.faq.R
import com.health.faq.activity.ExpertListActivity
import com.health.faq.activity.FqaActivity

/**
 *
@author xinkai.xu
title文字 更多
 * */
class TitleAdapter constructor(activity: Activity, type: Int, helper: LayoutHelper, mViewTypeItem: Int) : DelegateAdapter.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    private var mHelper: LayoutHelper = helper
    private var type: Int = type
    private var activity: Activity = activity
    private var mViewTypeItem: Int = -1
    private var view: View? = null

    init {
        this.mViewTypeItem = mViewTypeItem
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        return mHelper
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        if (viewType == mViewTypeItem) {
            view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.title_reward_for_help, parent, false)
        }


        return RecyclerViewItemHolder(view!!)
    }

    override fun getItemViewType(position: Int): Int {
        return mViewTypeItem
    }

    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as RecyclerViewItemHolder
        when (type) {
            1 -> viewHolder.tvTitle.text = "悬赏求助"
            2 -> viewHolder.tvTitle.text = "热门专家"
            3 -> {
                viewHolder.tvTitle.text = "问专家"
                viewHolder.tvMore.visibility = View.GONE

            }
        }
        viewHolder.tvMore.setOnClickListener {
            when (type) {
                1 -> {
                    activity.startActivity(Intent(activity, FqaActivity::class.java))
                }
                2 -> {
                    activity.startActivity(Intent(activity, ExpertListActivity::class.java))
                }

            }
        }


    }

    override fun getItemCount(): Int {
        return 1
    }

    /**
     * 正常条目的item的ViewHolder
     */
    private inner class RecyclerViewItemHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        var tvMore: TextView = itemView.findViewById(R.id.tvMore)
    }
}