package com.healthy.library.adapter

import android.graphics.Color
import androidx.core.text.HtmlCompat
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.healthy.library.R
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder

/**
 * @author Long
 * @desc:
 * @createTime :2021/10/20 14:35
 */
class SearchMatchAdapter : BaseAdapter<String>(R.layout.item_searchmatch_data_layout) {

    /** 需要匹配的字符 */
    var mSearchName: String = ""

    override fun onCreateLayoutHelper(): LayoutHelper {
        val helper = LinearLayoutHelper()
        helper.bgColor = Color.WHITE
        return helper
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {

        getDatas().also { data ->
            val mTitle = data[position]
            val mHtml = if (mTitle.contains(mSearchName)) {
                "<font color=\"#FD3956\">${
                    mTitle.substring(
                        0,
                        mSearchName.length
                    )
                }</font><font color=\"#333333\">${
                    mTitle.substring(
                        mSearchName.length,
                        mTitle.length
                    )
                }</font>"
            } else {
                mTitle
            }

            holder.setText(
                R.id.tv_title,
                HtmlCompat.fromHtml(mHtml, HtmlCompat.FROM_HTML_MODE_COMPACT)
            )
            holder.itemView.setOnClickListener {
                if (isClickInit()) {
                    moutClickListener.outClick("matchClick", data[position])
                }
            }
        }

    }
}