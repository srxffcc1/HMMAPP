package com.health.mine.adapter

import android.graphics.drawable.Drawable
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.health.mine.R
import com.healthy.library.businessutil.GlideCopy
import com.healthy.library.model.EnlistActivityModel
import com.healthy.library.utils.DateUtils

/**
 * @description 报名列表适配器
 * @author long
 * @date 2021/7/22
 */
class EnlistAdapter :
    BaseQuickAdapter<EnlistActivityModel, BaseViewHolder>(R.layout.mine_recyclerview_enlist_item_header) {

    private lateinit var mActionSelect: Drawable
    private lateinit var mActionUnEnabled: Drawable
    private lateinit var mActionBg: Drawable

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        mActionSelect = parent.resources.getDrawable(R.drawable.shape_select_servive_btn)
        mActionUnEnabled = parent.resources.getDrawable(R.drawable.shape_select_service_enabled_btn)
        mActionBg = parent.resources.getDrawable(R.drawable.mine_btn_enlist_enabled_shape)
        return super.onCreateViewHolder(parent, viewType)
    }

    override fun convert(helper: BaseViewHolder, item: EnlistActivityModel) {
        val mEnlistEndTime = DateUtils.str2Long(
            item.enlistActivity?.enlistEndTime,
            DateUtils.DATE_FORMAT_PATTERN_YMD_HMS
        )

        val mTime =
            if (mEnlistEndTime > System.currentTimeMillis()) {
                //报名未结束取报名时间
                DateUtils.getDateDay(
                    item.enlistActivity?.enlistStartTime,
                    "yyyy-MM-dd HH:mm:ss", "yyyy.MM.dd"
                ) + " - " +
                        DateUtils.getDateDay(
                            item.enlistActivity?.enlistEndTime,
                            "yyyy-MM-dd HH:mm:ss",
                            "yyyy.MM.dd"
                        )
            } else {
                //报名结束取活动时间
                DateUtils.getDateDay(
                    item.enlistActivity?.startTime,
                    "yyyy-MM-dd HH:mm:ss", "yyyy.MM.dd"
                ) + " - " +
                        DateUtils.getDateDay(
                            item.enlistActivity?.endTime,
                            "yyyy-MM-dd HH:mm:ss",
                            "yyyy.MM.dd"
                        )
            }

        /*val actionisGone = if (item.payStatus == 0) {
            //去支付状态 根据报名结束时间决定
            mEnlistEndTime > System.currentTimeMillis()
        } else {
            //其他情况默认显示
            true
        }*/

        val mTvAction = helper.setText(R.id.item_activity_title, item.enlistActivity?.name)
            .setText(R.id.item_enlist_Time, mTime)
            .setText(
                R.id.item_enlist_Address,
                item.enlistActivity?.activityAddress()
            )
            //.setGone(R.id.tv_action, actionisGone)
            .getView<TextView>(R.id.tv_action)

        val mEnlistImage = helper.getView<ImageView>(R.id.iv_enlist)

        GlideCopy.with(mEnlistImage.context)
            .load(item.enlistActivity?.photoUrl)
            .error(R.drawable.img_1_1_default)
            .placeholder(R.drawable.img_1_1_default)
            .into(mEnlistImage)

        mTvAction.apply {
            text = when (item.payStatus) {
                0 -> "去支付"
                1 -> "已取消"
                2 -> "已退款"
                3 -> "待核销"
                4 -> "已完成"
                else -> ""
            }
            background = when (item.payStatus) {
                3 -> mActionBg
                0 -> mActionSelect
                else -> mActionUnEnabled
            }
            isEnabled = (item.payStatus == 0 || item.payStatus == 3)
        }
    }

}