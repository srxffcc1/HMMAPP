package com.health.discount.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.health.discount.R
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.model.Coupon
import com.healthy.library.utils.TransformUtil

/**
 * @description
 * @author long
 * @date 2021/9/15
 */
class NewUserGiftServiceAdapter : BaseAdapter<Coupon>(R.layout.item_new_user_gift_service_layout) {

    private var mCoupon: Coupon? = null
    private var mIsReceive = false // true 已领取状态 false 无领取状态
    private var mCheckCount = 0 //选择数量
    private var mMaxSelCnt = 0

    fun setMaxSelCnt(maxSelCnt: Int) {
        this.mMaxSelCnt = maxSelCnt
    }

    fun setReceive(isReceive: Boolean) {
        this.mIsReceive = isReceive
    }

    fun getCoupon(): Coupon {
        return mCoupon!!
    }

    fun setCheckCount(checkCount: Int) {
        this.mCheckCount = checkCount
    }

    fun getCheckCount(): Int {
        return mCheckCount
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper()
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
            val mChangeCheck = holder.setText(R.id.tv_title, coupon.GoodsName)
                .setText(R.id.tv_Money, coupon.Price)
                .setTextSize(R.id.tv_Money, textSize)
                .setText(R.id.tv_activity, coupon.getGbTypeName())
                .setVisible(R.id.iv_change_check, !mIsReceive)
                .setVisible(R.id.iv_give, "1" == coupon.isReceive)
                .setImage(
                    R.id.iv_left_card,
                    if ("1" == coupon.isReceive) R.drawable.shape_giftplus_leftred_gray else R.drawable.shape_giftplus_leftred
                )
                .setBackGround(
                    R.id.iv_left_card_red2,
                    if ("1" == coupon.isReceive) R.drawable.shape_giftplus_rightred2_gray else R.drawable.shape_giftplus_rightred2
                )
                .setBackGround(
                    R.id.iv_left_card_white,
                    if ("1" == coupon.isReceive) R.drawable.shape_giftplus_leftwhite_gray else R.drawable.shape_giftplus_leftwhite
                )
                .setBackGround(
                    R.id.iv_left_card_white_boder,
                    if ("1" == coupon.isReceive) R.drawable.shape_giftplus_leftwhite_boder_gray else R.drawable.shape_giftplus_leftwhite_boder
                )
                .setTextColorRes(
                    R.id.tv_title,
                    if ("1" == coupon.isReceive) R.color.color_333333 else R.color.white
                )
                .setTextColorRes(
                    R.id.tv_desc_body,
                    if ("1" == coupon.isReceive) R.color.color_666666 else R.color.color_FFFFFF
                )
                .setTextColorRes(
                    R.id.tv_shopName,
                    if ("1" == coupon.isReceive) R.color.color_666666 else R.color.color_FFEFD9
                )
                .setTextColorRes(
                    R.id.tv_Money_left,
                    if ("1" == coupon.isReceive) R.color.color_FFFFFF else R.color.color_EF3D35
                )
                .setTextColorRes(
                    R.id.tv_Money,
                    if ("1" == coupon.isReceive) R.color.color_FFFFFF else R.color.color_EF3D35
                )
                .setTextColorRes(
                    R.id.tv_activity,
                    if ("1" == coupon.isReceive) R.color.color_FFFFFF else R.color.color_EF3D35
                )
                .setText(
                    R.id.tv_shopName,
                    if (coupon.shopName.isNullOrEmpty()) "选择服务门店 >" else "${coupon.getShopName()} >"
                )
                .setVisible(R.id.bottomView, it.size - 1 == position)
                .setOnClickListener(R.id.tv_shopName, View.OnClickListener {
                    if (isClickInit()) {
                        if (mIsReceive) return@OnClickListener
                        moutClickListener.outClick("serviceShop", position)
                    }
                })
                .getView<ImageView>(R.id.iv_change_check)

            if(!mIsReceive){
                holder.setTextColorRes(
                    R.id.tv_shopName,
                   if (coupon.ischeck && coupon.shopName.isNullOrEmpty()) R.color.color_FBFF0C else R.color.color_FFEFD9
                )//新加入选中未选择门店时的特殊文字颜色提醒
            }

            holder.itemView.setOnClickListener(View.OnClickListener {
                if (!isClickInit() || mIsReceive) {
                    return@OnClickListener
                }
                if (mMaxSelCnt > 0) {
                    if (mCheckCount == mMaxSelCnt && !coupon.ischeck) {
                        moutClickListener.outClick("tip", "已达最大可选数")
                        return@OnClickListener
                    }
                }
                coupon.ischeck = !coupon.ischeck
                if (coupon.ischeck) {
                    mCheckCount++;
                    /*if (!mIsReceive && "0" == coupon.GoodsType) {
                        moutClickListener.outClick("serviceShop", position)
                    }*/
                } else {
                    mCheckCount--;
                }
                mCoupon = coupon
                moutClickListener.outClick("serviceChange", position)
            })

            if ("1" == coupon.GoodsType || "2" == coupon.isReceive) {
                holder.setVisibility(R.id.tv_shopName, View.INVISIBLE)
            } else {
                holder.setVisibility(
                    R.id.tv_shopName,
                    if (mIsReceive && coupon.shopName.isNullOrEmpty()) View.INVISIBLE else View.VISIBLE
                )
            }
            coupon.mGuideView = holder.getView(R.id.tv_shopName)
            val resource = if ("0" == coupon.SendMode || coupon.ischeck) {
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