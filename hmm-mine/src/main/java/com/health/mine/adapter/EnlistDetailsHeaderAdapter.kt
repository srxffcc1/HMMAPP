package com.health.mine.adapter

import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.example.lib_ShapeView.layout.ShapeConstraintLayout
import com.health.mine.R
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.model.EnlistActivityModel
import com.healthy.library.utils.DateUtils

/**
 * @description
 * @author long
 * @date 2021/7/23
 */
class EnlistDetailsHeaderAdapter :
    BaseAdapter<EnlistActivityModel>(R.layout.mine_recyclerview_enlist_item_header) {

    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper()
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        val mMineShapeCl = holder.getView<ShapeConstraintLayout>(R.id.mine_scl_enlist)
        getModel()?.let {
            it.enlistActivity?.let { it ->

                val mEnlistEndTime = DateUtils.str2Long(
                    it.enlistEndTime,
                    DateUtils.DATE_FORMAT_PATTERN_YMD_HMS
                )
                val mTime =
                    if (mEnlistEndTime > System.currentTimeMillis()) { //报名未结束展示报名时间
                        DateUtils.getDateDay(
                            it.enlistStartTime,
                            "yyyy-MM-dd HH:mm:ss",
                            "yyyy.MM.dd"
                        ) + "-" +
                                DateUtils.getDateDay(
                                    it.enlistEndTime,
                                    "yyyy-MM-dd HH:mm:ss",
                                    "yyyy.MM.dd"
                                )
                    } else {//否则展示活动时间
                        DateUtils.getDateDay(
                            it.startTime,
                            "yyyy-MM-dd HH:mm:ss",
                            "yyyy.MM.dd"
                        ) + "-" +
                                DateUtils.getDateDay(
                                    it.endTime,
                                    "yyyy-MM-dd HH:mm:ss",
                                    "yyyy.MM.dd"
                                )
                    }

                holder.setVisible(R.id.tv_prize, true)
                    .setVisible(R.id.tv_action, false)
                    .setText(
                        R.id.tv_prize,
                        if (it.price > 0 && it.isFree == 0) changTVsize("¥" + it.price.toString()) else "免费"
                    )
                    .setText(R.id.item_activity_title, it.name)
                    .setText(R.id.item_enlist_Time, mTime)
                    .setText(R.id.item_enlist_Address, it.activityAddress())
                    .setImg(
                        R.id.iv_enlist,
                        it.photoUrl,
                        R.drawable.img_1_1_default,
                        R.drawable.img_1_1_default
                    )
            }
        }

        val layoutParams = mMineShapeCl.layoutParams
        if (layoutParams is VirtualLayoutManager.LayoutParams) {
            layoutParams.bottomMargin = 0
            mMineShapeCl.layoutParams = layoutParams
        }
        mMineShapeCl.shapeDrawableBuilder
            .setBottomLeftRadius(0f)
            .setBottomRightRadius(0f)
            .intoBackground()

    }

    private fun changTVsize(value: String): SpannableString {
        val spannableString = SpannableString(value);
        if (value.contains(".")) {
            spannableString.setSpan(
                RelativeSizeSpan(0.6f),
                value.indexOf("."),
                value.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        return spannableString;
    }

}