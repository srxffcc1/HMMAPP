package com.health.city.adapter

import android.graphics.Color
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.lib_ShapeView.layout.ShapeLinearLayout
import com.health.city.R
import com.healthy.library.model.Topic

/**
 * author : long
 * Time   :2021/11/5
 * desc   :
 */
class CityHotTipAdapter :
    BaseQuickAdapter<Topic, BaseViewHolder>(R.layout.city_item_fragment_hottip) {

    override fun convert(helper: BaseViewHolder, item: Topic) {
        val mSllContent = helper.getView<ShapeLinearLayout>(R.id.sll_content)

        val mShapeBuilder = mSllContent.shapeDrawableBuilder
        var mTipNoColor: Int? = 0
        var mTipNameColor: Int? = 0
        when (helper.layoutPosition) {
            0 -> {
                mShapeBuilder.solidColor = Color.parseColor("#0FFF284A")
                mTipNoColor = Color.parseColor("#FF544F")
                mTipNameColor = Color.parseColor("#FF544F")
            }
            1 -> {
                mShapeBuilder.solidColor = Color.parseColor("#0FFF0056")
                mTipNoColor = Color.parseColor("#FF4986")
                mTipNameColor = Color.parseColor("#FF4986")
            }
            2 -> {
                mShapeBuilder.solidColor = Color.parseColor("#0FFA8800")
                mTipNoColor = Color.parseColor("#FA8800")
                mTipNameColor = Color.parseColor("#FA8800")
            }
            else -> {
                mShapeBuilder.solidColor = Color.parseColor("#f2f2f2")
                mTipNoColor = Color.parseColor("#9596A4")
                mTipNameColor = Color.parseColor("#444444")
            }
        }
        mShapeBuilder.intoBackground()
        helper.setText(R.id.tipName, item.topicName)
            .setText(R.id.tipNo, String.format("%02d", helper.layoutPosition + 1))
            .setTextColor(R.id.tipNo, mTipNoColor)
            .setTextColor(R.id.tipName, mTipNameColor)
            .setGone(R.id.iv_tip_hot, helper.layoutPosition < 3)

    }
}