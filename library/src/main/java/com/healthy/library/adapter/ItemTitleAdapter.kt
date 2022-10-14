package com.healthy.library.adapter

import android.graphics.Color
import android.graphics.Typeface
import android.text.Html
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.healthy.library.R
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.model.ItemTitleModel

/**
 * @description
 * @author long
 * @date 2021/9/15
 */
class ItemTitleAdapter : BaseAdapter<ItemTitleModel>(R.layout.recycler_item_title_layout) {

    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper()
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        getModel()?.let {
            holder.setText(R.id.tv_item_title, it.title)
                .setText(R.id.tv_item_title_right, it.titleRight)
                .setText(R.id.tv_item_Remark, Html.fromHtml(it.getItemRemarkHtml()))
                .setVisible(R.id.tv_item_Remark, it.itemRemark.isNullOrEmpty().not())
                .setTextColor(R.id.tv_item_title, Color.parseColor(it.titleColor))
                .setTextColor(R.id.tv_item_title_right, Color.parseColor(it.titleRightColor))
                .setTextColor(R.id.tv_item_Remark, Color.parseColor(it.itemRemarkColor))
                .setTextTypeface(
                    R.id.tv_item_title,
                    Typeface.defaultFromStyle(if (it.titleIsBold) Typeface.BOLD else Typeface.NORMAL)
                )
                .setTextTypeface(
                    R.id.tv_item_title_right,
                    Typeface.defaultFromStyle(if (it.titleRightIsBold) Typeface.BOLD else Typeface.NORMAL)
                )
                .setTextTypeface(
                    R.id.tv_item_Remark,
                    Typeface.defaultFromStyle(if (it.itemRemarkIsBold) Typeface.BOLD else Typeface.NORMAL)
                )

            if (it.titleSize > 0) {
                holder.setTextSize(R.id.tv_item_title, it.titleSize)
            }
            if (it.titleRightSize > 0) {
                holder.setTextSize(R.id.tv_item_title_right, it.titleRightSize)
            }
            if (it.itemRemarkSize > 0) {
                holder.setTextSize(R.id.tv_item_Remark, it.itemRemarkSize)
            }
        }
    }
}