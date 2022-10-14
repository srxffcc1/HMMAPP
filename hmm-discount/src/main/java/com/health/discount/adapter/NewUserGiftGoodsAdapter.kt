package com.health.discount.adapter

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.health.discount.R
import com.healthy.library.LibApplication
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.model.Coupon
import com.healthy.library.utils.TransformUtil

/**
 * @description
 * @author long
 * @date 2021/9/15
 */
class NewUserGiftGoodsAdapter : BaseAdapter<Coupon>(R.layout.item_new_user_gift_goods_layout) {

    private var mCoupon: Coupon? = null
    private var mIsReceive = false // true 已领取状态 false 无领取状态
    private var mViewType = 0 // 0 默认新客商品券样式 1 节日礼包商品券样式 (后续如果有了在加)

    fun setReceive(isReceive: Boolean) {
        this.mIsReceive = isReceive
    }

    fun getCoupon(): Coupon {
        return mCoupon!!
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        val linearLayoutHelper = LinearLayoutHelper()
        linearLayoutHelper.marginBottom = TransformUtil.dp2px(LibApplication.getAppContext(),10f).toInt()
        return linearLayoutHelper
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {

        getDatas()?.let {
            val coupon = it[position]
            var textSize = 0f
            val mTvMoney = holder.getView<TextView>(R.id.tv_Money)
            val moneyLayoutParams = mTvMoney.layoutParams as? ConstraintLayout.LayoutParams
            var rightMargin = TransformUtil.dp2px(context, 10f).toInt()
            when {
                coupon.Price.length == 3 -> {
                    textSize = TransformUtil.sp2px(context, 8f)
                    rightMargin = TransformUtil.dp2px(context, 8f).toInt()
                }
                coupon.Price.length == 4 -> {
                    textSize = TransformUtil.sp2px(context, 8f)
                    rightMargin = TransformUtil.dp2px(context, 4f).toInt()
                }
                coupon.Price.length > 4 -> {
                    textSize = TransformUtil.sp2px(context, 7f)
                    rightMargin = TransformUtil.dp2px(context, 3f).toInt()
                }
                else -> {
                    textSize = TransformUtil.sp2px(context, 10f)
                }
            }
            moneyLayoutParams?.rightMargin = rightMargin
            mTvMoney.layoutParams = moneyLayoutParams
            val mChangeCheck = holder.setText(R.id.tv_apply, coupon.GoodsName)
                .setText(R.id.tv_Money, coupon.Price)
                .setTextSize(R.id.tv_Money, textSize)
                .setText(R.id.tv_activity, coupon.getGbTypeName())
                .setVisible(R.id.iv_change_check, !mIsReceive)
                .setBackGround(
                    R.id.cl_content,
                    if ("1" == coupon.isReceive) R.drawable.shape_giftplus_leftwhite_gray2 else R.drawable.shape_giftplus_leftwhite
                )
                .setBackGround(
                    R.id.tv_right,
                    if ("1" == coupon.isReceive) R.drawable.shape_giftplus_rightred_gray else R.drawable.shape_giftplus_rightred
                )
                .setBackGround(
                    R.id.tv_rightsplit,
                    if ("1" == coupon.isReceive) R.drawable.shape_giftplus_whitered_gray else R.drawable.shape_giftplus_whitered
                )
                .setVisible(R.id.iv_give, "1" == coupon.isReceive)
                .setVisibility(
                    R.id.bottomView,
                    if (it.size - 1 == position) View.VISIBLE else View.GONE
                )
                .getView<ImageView>(R.id.iv_change_check)

            holder.itemView.setOnClickListener(View.OnClickListener {
                if (isClickInit()) {
                    if ("0" == coupon.SendMode || "1" == coupon.GoodsType) {
                        moutClickListener.outClick("tip", "该劵为固定劵")
                        return@OnClickListener
                    }
                    coupon.ischeck = !coupon.ischeck
                    mCoupon = coupon
                    moutClickListener.outClick("goodsChange", position)
                }
            })
            if ("0" == coupon.isReceive) {
                holder.setTextColor(R.id.tv_apply, Color.parseColor("#CC3815"))
                holder.setTextColor(R.id.tv_activity, Color.parseColor("#CC3815"))
            } else {
                holder.setTextColor(R.id.tv_apply, Color.parseColor("#333333"))
                holder.setTextColor(R.id.tv_activity, Color.parseColor("#333333"))
            }
            val resource =
                if ("0" == coupon.SendMode || "1" == coupon.GoodsType || coupon.ischeck) {
                    coupon.ischeck = true
                    R.drawable.new_user_gift_check
                } else {
                    coupon.ischeck = false
                    R.drawable.new_user_gift_checked
                }
            mChangeCheck.setImageResource(resource)
        }

    }

}