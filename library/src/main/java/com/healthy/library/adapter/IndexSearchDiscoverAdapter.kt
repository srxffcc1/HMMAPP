package com.healthy.library.adapter

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.text.TextUtils
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.GridLayoutHelper
import com.healthy.library.R
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.model.SearchRecordsModel
import com.healthy.library.utils.MARouterUtils

class IndexSearchDiscoverAdapter :
    BaseAdapter<SearchRecordsModel>(R.layout.item_search_discover_adapter) {

    private var changeBg = false
    private var color: Int? = null

    override fun onCreateLayoutHelper(): LayoutHelper {
        val helper = GridLayoutHelper(2)
        helper.marginTop = 20
        helper.setAutoExpand(false)
        helper.bgColor = Color.parseColor("#F7F7FA")
        return helper
    }

    public fun setAdapterBackgroundColor(color: Int, changeBg: Boolean) {
        this.changeBg = changeBg
        this.color = color
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        if (changeBg) {
            holder.setBackGround(R.id.text, color!!)
        }
        holder.setText(R.id.text, datas[position].searchContent)
            .setTextColor(R.id.text, Color.parseColor("#666666"))

        holder.itemView.setOnClickListener {
            if (isClickInit) {
                if (!TextUtils.isEmpty(datas[position].androidUrl) || !TextUtils.isEmpty(datas[position].h5Url)) {
                    if (!TextUtils.isEmpty(datas[position].androidUrl)) {
                        if (datas[position].androidUrl.startsWith("/")) { //是路由
                            MARouterUtils.passToTarget(context, datas[position].androidUrl)
                        } else {
                            moutClickListener.outClick(
                                "matchClick",
                                datas[position].searchContent
                            )
                        }
                    }
                    if (!TextUtils.isEmpty(datas[position].h5Url)) {
                        if (datas[position].h5Url.startsWith("http")) { //是网页
                            MARouterUtils.passToTarget(context, datas[position].h5Url)
                        } else {
                            moutClickListener.outClick(
                                "matchClick",
                                datas[position].searchContent
                            )
                        }
                    }
                } else {
                    moutClickListener.outClick("matchClick", datas[position].searchContent)
                }

            }
        }

    }

}
