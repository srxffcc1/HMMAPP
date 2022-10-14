package com.health.index.adapter

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.lib_ShapeView.view.ShapeTextView
import com.health.index.R
import com.healthy.library.model.VideoCategory
import com.healthy.library.utils.TransformUtil

/**
 * author : long
 * Time   :2021/12/16
 * desc   :
 */
class IndexTabClassListAdapter :
    BaseQuickAdapter<VideoCategory, BaseViewHolder>(R.layout.index_tab_classlist_item_layout) {

    var mSelectPosition: Int = 0
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun convert(helper: BaseViewHolder, item: VideoCategory) {
        helper.setText(R.id.tv_label, item.categoryName)

        val mLabel = helper.getView<TextView>(R.id.tv_label)
        mLabel.isSelected = mSelectPosition == helper.layoutPosition
        val layoutParams = mLabel.layoutParams as RecyclerView.LayoutParams
        var leftMargin = 0
        if (helper.layoutPosition == 0) {
            leftMargin = TransformUtil.newDp2px(mContext, 20f)
        }
        layoutParams.leftMargin = leftMargin
        mLabel.layoutParams = layoutParams

    }
}