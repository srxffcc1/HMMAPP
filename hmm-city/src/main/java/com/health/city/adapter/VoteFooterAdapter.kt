package com.health.city.adapter

import android.util.Log
import android.view.View
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.chad.library.adapter.base.MultipleItemRvAdapter
import com.health.city.R
import com.health.city.model.VoteModel
import com.healthy.library.base.BaseHolder
import com.healthy.library.base.BaseMultiItemAdapter
import com.healthy.library.utils.TransformUtil

/**
 * @description
 * @author long
 * @date 2021/6/22
 */
class VoteFooterAdapter : BaseMultiItemAdapter<VoteModel> {

    private var mClickCount = 0

    constructor() : super(R.layout.city_item_vote_enroll_footer_layout) {
        addItemType(1, R.layout.city_item_vote_enroll_footer_layout)// 报名
        addItemType(2, R.layout.city_item_vote_footer_layout)// 投票
    }

    override fun getItemViewType(position: Int): Int {
        if (getModel() != null) {
            return getModel()?.itemType!!
        }
        return getDefItemViewType(position)
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        val linearLayoutHelper = LinearLayoutHelper()
        linearLayoutHelper.paddingBottom = 20
        linearLayoutHelper.paddingTop = 40
        return linearLayoutHelper
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {

        when (getItemViewType(position)) {
            1 -> buildEnrollFooter(holder, position)
            2 -> buildVoteFooter(holder, position)
        }

        holder.itemView.setOnClickListener {
            var mFunction = ""
            if (getItemViewType(position) == 2) {
                if (mClickCount % 2 == 0) {
                    //查看视频
                    mFunction = "video"
                } else {
                    //查看图片
                    mFunction = "photo"
                }
                mClickCount++
                //监听已初始化
                if (isClickInit()) {
                    moutClickListener.outClick(mFunction, position)
                }
            }
        }
    }

    /**
     * 报名底部页
     */
    private fun buildEnrollFooter(holder: BaseHolder, position: Int) {

    }

    /**
     * 投票底部页
     */
    private fun buildVoteFooter(holder: BaseHolder, position: Int) {

        holder.setOnClickListener(R.id.item_footer_action, View.OnClickListener {
            if (isClickInit()) {
                moutClickListener.outClick("voteAction", position)
            }
        })
    }
}