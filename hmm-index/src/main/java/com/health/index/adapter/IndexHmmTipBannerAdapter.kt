package com.health.index.adapter

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.alibaba.android.arouter.launcher.ARouter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.lib_ShapeView.layout.ShapeFrameLayout
import com.health.index.R
import com.healthy.library.model.TipPost
import com.healthy.library.model.TipPostOther
import com.healthy.library.routes.CityRoutes
import com.healthy.library.utils.StringUtils
import com.healthy.library.widget.ImageSpanCentre
import com.healthy.library.widget.ImageTextView

/**
 * author : long
 * Time   :2021/12/15
 * desc   :
 */
class IndexHmmTipBannerAdapter :
    BaseQuickAdapter<TipPostOther, BaseViewHolder>(R.layout.index_hmm_tip_item_layout) {

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("UseCompatTextViewDrawableApis")
    override fun convert(helper: BaseViewHolder, item: TipPostOther) {

        val mTitle = helper.getView<TextView>(R.id.tv_title)
        val chat_bgz = helper.getView<ImageView>(R.id.chat_bgz)
        val mIvDiscussBg = helper.getView<AppCompatImageView>(R.id.iv_discuss_bg)
        //更新控件
        val mSflRenew = helper.getView<ShapeFrameLayout>(R.id.sfl_renew)
        val mItvRenew = helper.getView<ImageTextView>(R.id.itv_renew)

        val shapeDrawableBuilder = mSflRenew.shapeDrawableBuilder
        val content = "   " + item?.topicName  //加个空格 后面代替图标位置
        var drawable: Drawable? = null
        when (helper.layoutPosition) {
            0 -> {
                chat_bgz.setImageResource(R.drawable.chat_bg_zblue)
                drawable = mContext.resources.getDrawable(R.drawable.icon_index_hmm_tip_left)
                if (shapeDrawableBuilder.solidColor != Color.parseColor("#3374BCFF")) {
                    mIvDiscussBg.imageTintList = ColorStateList.valueOf(Color.parseColor("#3E7EFF"))
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {//如果Android版本大于6.0 才能兼容此方法
                        mItvRenew.compoundDrawableTintList =
                            ColorStateList.valueOf(Color.parseColor("#0066FF"))
                    } else {//低于Android6.0的使用下面的方法
                        //不好弄  还得导v4包  不填充Drawable颜色了 也没啥影响
                    }
                    mItvRenew.setTextColor(Color.parseColor("#1272FF"))
                    shapeDrawableBuilder.setSolidColor(Color.parseColor("#3374BCFF"))
                        .intoBackground()
                }
            }
            1 -> {
                chat_bgz.setImageResource(R.drawable.chat_bg_zpink)
                drawable = mContext.resources.getDrawable(R.drawable.icon_index_hmm_tip_left_two)
                if (shapeDrawableBuilder.solidColor != Color.parseColor("#33FF4675")) {
                    mIvDiscussBg.imageTintList = ColorStateList.valueOf(Color.parseColor("#FF4675"))
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {//如果Android版本大于6.0 才能兼容此方法
                        mItvRenew.compoundDrawableTintList =
                            ColorStateList.valueOf(Color.parseColor("#FF0041"))
                    }
                    mItvRenew.setTextColor(Color.parseColor("#FF4675"))
                    shapeDrawableBuilder.setSolidColor(Color.parseColor("#33FF4675"))
                        .intoBackground()
                }
            }
            2 -> {
                chat_bgz.setImageResource(R.drawable.chat_bg_zyellow)
                drawable = mContext.resources.getDrawable(R.drawable.icon_index_hmm_tip_left_three)
                if (shapeDrawableBuilder.solidColor != Color.parseColor("#33FFB946")) {
                    mIvDiscussBg.imageTintList = ColorStateList.valueOf(Color.parseColor("#FFB946"))
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {//如果Android版本大于6.0 才能兼容此方法
                        mItvRenew.compoundDrawableTintList =
                            ColorStateList.valueOf(Color.parseColor("#FF9300"))
                    }
                    mItvRenew.setTextColor(Color.parseColor("#FFB946"))
                    shapeDrawableBuilder.setSolidColor(Color.parseColor("#33FFB946"))
                        .intoBackground()
                }
            }
        }
        val spannableString = SpannableString(content)
        drawable!!.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        val imageSpan = ImageSpanCentre(drawable, ImageSpanCentre.CENTRE)
        spannableString.setSpan(imageSpan, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        mTitle.text = spannableString

        val ivDiscussBg: ImageView
        val tvTitle: AppCompatTextView
        val tvParticipateNum: AppCompatTextView
        val sflRenew: ShapeFrameLayout
        val itvRenew: ImageTextView
        val ivPostingOneLabel: AppCompatImageView
        val ivPostingOneTitle: AppCompatTextView
        val ivPostingTwoLabel: AppCompatImageView
        val ivPostingTwoTitle: AppCompatTextView
        val ivPostingThreeLabel: AppCompatImageView
        val ivPostingThreeTitle: AppCompatTextView
        ivDiscussBg = helper.getView(R.id.iv_discuss_bg)
        tvTitle = helper.getView(R.id.tv_title)
        tvParticipateNum = helper.getView(R.id.tv_participateNum)
        sflRenew = helper.getView(R.id.sfl_renew)
        itvRenew = helper.getView(R.id.itv_renew)
        ivPostingOneLabel = helper.getView(R.id.iv_posting_one_label)
        ivPostingOneTitle = helper.getView(R.id.iv_posting_one_title)
        ivPostingTwoLabel = helper.getView(R.id.iv_posting_two_label)
        ivPostingTwoTitle = helper.getView(R.id.iv_posting_two_title)
        ivPostingThreeLabel = helper.getView(R.id.iv_posting_three_label)
        ivPostingThreeTitle = helper.getView(R.id.iv_posting_three_title)

        tvParticipateNum.setText(item.partInNum + "人参与")
        if (item.newestPostingNum.equals("0")) {
            itvRenew.setText("最新更新")
        } else {
            itvRenew.setText(item.newestPostingNum + "条更新")
        }
        item?.apply {
            when (item.postingList.size) {
                0 -> {

                }
                1 -> {
                    helper.setText(R.id.iv_posting_one_title, item?.postingList[0]?.postingTitle)
                        .setVisible(R.id.iv_posting_one_title, item?.postingList[0]?.postingTitle != null)
                        .setVisible(R.id.iv_posting_one_label, item?.postingList[0]?.postingTitle != null)
                        .setVisible(R.id.iv_posting_two_label, false)
                        .setVisible(R.id.iv_posting_three_label, false)
                        .setImageResource(
                            R.id.iv_posting_one_label,
                            if (TextUtils.isEmpty(item?.postingList[0]?.postingTag)) {
                                R.drawable.icon_index_default_posting
                            } else {
                                R.drawable.icon_index_essences_posting
                            }
                        )
                }
                2 -> {
                    helper.setText(R.id.iv_posting_one_title, item?.postingList[0]?.postingTitle)
                        .setText(R.id.iv_posting_two_title, item?.postingList[1]?.postingTitle)
                        .setVisible(R.id.iv_posting_one_title, item?.postingList[0]?.postingTitle != null)
                        .setVisible(R.id.iv_posting_two_title, item?.postingList[1]?.postingTitle != null)
                        .setVisible(R.id.iv_posting_one_label, item?.postingList[0]?.postingTitle != null)
                        .setVisible(R.id.iv_posting_two_label, item?.postingList[1]?.postingTitle != null)
                        .setVisible(R.id.iv_posting_three_label, false)
                        .setImageResource(
                            R.id.iv_posting_one_label,
                            if (TextUtils.isEmpty(item?.postingList[0]?.postingTag)) {
                                R.drawable.icon_index_default_posting
                            } else {
                                R.drawable.icon_index_essences_posting
                            }
                        )
                        .setImageResource(
                            R.id.iv_posting_two_label,
                            if (TextUtils.isEmpty(item?.postingList[1]?.postingTag)) {
                                R.drawable.icon_index_default_posting
                            } else {
                                R.drawable.icon_index_essences_posting
                            }
                        )
                }
                3 -> {
                    helper.setText(R.id.iv_posting_one_title, item?.postingList[0]?.postingTitle)
                        .setText(R.id.iv_posting_two_title, item?.postingList[1]?.postingTitle)
                        .setText(R.id.iv_posting_three_title, item?.postingList[2]?.postingTitle)
                        .setVisible(R.id.iv_posting_one_title, item?.postingList[0]?.postingTitle != null)
                        .setVisible(R.id.iv_posting_two_title, item?.postingList[1]?.postingTitle != null)
                        .setVisible(R.id.iv_posting_three_title, item?.postingList[2]?.postingTitle != null)
                        .setVisible(R.id.iv_posting_one_label, item?.postingList[0]?.postingTitle != null)
                        .setVisible(R.id.iv_posting_two_label, item?.postingList[1]?.postingTitle != null)
                        .setVisible(R.id.iv_posting_three_label, item?.postingList[2]?.postingTitle != null)
                        .setImageResource(
                            R.id.iv_posting_one_label,
                            if (TextUtils.isEmpty(item?.postingList[0]?.postingTag)) {
                                R.drawable.icon_index_default_posting
                            } else {
                                R.drawable.icon_index_essences_posting
                            }
                        )
                        .setImageResource(
                            R.id.iv_posting_two_label,
                            if (TextUtils.isEmpty(item?.postingList[1]?.postingTag)) {
                                R.drawable.icon_index_default_posting
                            } else {
                                R.drawable.icon_index_essences_posting
                            }
                        )
                        .setImageResource(
                            R.id.iv_posting_three_label,
                            if (TextUtils.isEmpty(item?.postingList[2]?.postingTag)) {
                                R.drawable.icon_index_default_posting
                            } else {
                                R.drawable.icon_index_essences_posting
                            }
                        )
                }
            }
        }
        helper.itemView.setOnClickListener {
            ARouter.getInstance()
                .build(CityRoutes.CITY_TIP)
                .withString("topicId", item.id)
                .navigation()
        }
    }
}