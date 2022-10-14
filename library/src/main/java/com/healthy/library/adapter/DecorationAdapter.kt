package com.healthy.library.adapter

import android.view.View
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.healthy.library.R
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder

/**
 * @description 列表分割
 * @author long
 * @date 2021/7/23
 */
class DecorationAdapter
    : BaseAdapter<String>(R.layout.lib_recyclerview_item_decoration) {

    /** 分隔高度 */
    private var mItemLineHeight = 0

    fun setItemHeight(height: Int) {
        this.mItemLineHeight = height
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper()
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {

        val view = holder.getView<View>(R.id.lib_item_line)

        if (mItemLineHeight != 0) {
            val layoutParams = view.layoutParams
            layoutParams.height = mItemLineHeight
            view.layoutParams = layoutParams
        }
    }
}