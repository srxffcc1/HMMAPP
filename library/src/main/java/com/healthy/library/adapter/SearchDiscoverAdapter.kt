package com.healthy.library.adapter

import android.graphics.Color
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.GridLayoutHelper
import com.healthy.library.R
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder

class SearchDiscoverAdapter : BaseAdapter<String>(R.layout.item_search_discover_adapter) {

    private var changeBg = false
    private var color: Int? = null

    override fun onCreateLayoutHelper(): LayoutHelper {
        val helper = GridLayoutHelper(2)
        helper.marginTop = 20
        helper.setAutoExpand(false)
        helper.bgColor = Color.WHITE
        return helper
    }

    public fun setAdapterBackgroundColor(color: Int, changeBg: Boolean) {
        this.changeBg = changeBg
        this.color = color
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        val it = getDatas()[position]
        if (changeBg) {
            holder.setBackGround(R.id.text, color!!)
        }
        holder.setText(R.id.text, it)
            .setTextColor(R.id.text, Color.parseColor("#666666"))

        holder.itemView.setOnClickListener {
            if (isClickInit()) {
                moutClickListener.outClick("matchClick", getDatas()[position])
            }
        }

    }

}
