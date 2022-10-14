package com.health.mine.adapter

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.health.mine.R
import com.healthy.library.businessutil.GlideCopy
import com.healthy.library.model.LotteryModel
import com.healthy.library.utils.DateUtils
import com.healthy.library.utils.TransformUtil

/**
 * @author long
 * @description
 * @date 2021/8/2
 */
class LotteryListAdapter :
    BaseQuickAdapter<LotteryModel, BaseViewHolder>(R.layout.mine_recyclerview_lottery_list_item) {


    override fun convert(helper: BaseViewHolder, item: LotteryModel) {

        val mTime = if (item.lotteryTime.isNullOrEmpty()) {
            ""
        } else {
            "中奖时间：${
                DateUtils.getDateDay(
                    item.lotteryTime,
                    "yyyy-MM-dd HH:mm:ss",
                    "yyyy/MM/dd HH:mm"
                )
            }"
        }

        val mEndTime = if (item.lotteryActivity?.restrict?.latestExchangeTime.isNullOrEmpty()) {
            ""
        } else {
            "领奖截止时间：${
                DateUtils.getDateDay(
                    item.lotteryActivity?.restrict?.latestExchangeTime,
                    "yyyy-MM-dd HH:mm:ss",
                    "yyyy/MM/dd HH:mm"
                )
            }"
        }

        val mIvGoodsImg =
            helper
                .setText(R.id.mine_tv_ActivityTitle, item.lotteryActivity?.title)
                .setText(R.id.mine_tv_prizeDesc, item.lotteryPrizeProfile?.goodsTitle)
                .setText(R.id.mine_tv_level, item.lotteryPrizeProfile?.prizeName)
                .setText(R.id.tv_action, if (item.awardStatus == 1) "去领奖" else "已领奖")
                .setText(R.id.mine_tv_awardOrderNo, "领奖单号：${item.orderNo}")
                .setText(R.id.mine_tv_awardTime, mTime)
                .setText(R.id.mine_tv_prizeEndTime, mEndTime)
                .setGone(R.id.mine_tv_awardOrderNo, item.awardStatus == 2)
                .addOnClickListener(R.id.tv_action)
                .getView<ImageView>(R.id.mine_iv_goodsImg)

        GlideCopy.with(mIvGoodsImg.context)
            .load(item.lotteryPrizeProfile?.goodsHeadImage)
            .error(R.drawable.img_1_1_default)
            .into(mIvGoodsImg)

        val mTvAction = helper.getView<TextView>(R.id.tv_action)
        mTvAction.isEnabled = item.awardStatus == 1
        val mTvActivityTitle = helper.getView<TextView>(R.id.mine_tv_ActivityTitle)
        val mTvActivityType = helper.getView<TextView>(R.id.mine_tv_activityType)
        val mClContent = helper.getView<ConstraintLayout>(R.id.mine_cl_content)


        val titleLayoutParams =
            mTvActivityTitle.layoutParams as ConstraintLayout.LayoutParams

        titleLayoutParams.width = ConstraintLayout.LayoutParams.WRAP_CONTENT
        titleLayoutParams.rightToLeft = -1
        mTvActivityTitle.layoutParams = titleLayoutParams
        val typeLayoutParams =
            mTvActivityType.layoutParams as ConstraintLayout.LayoutParams
        typeLayoutParams.rightToRight = -1
        mTvActivityType.layoutParams = typeLayoutParams

        mTvActivityTitle.post {
            if (mTvActivityTitle.width + mTvActivityType.width >= mClContent.width) {
                titleLayoutParams.width =
                    mTvActivityTitle.width - mTvActivityType.width - TransformUtil.dp2px(
                        mContext,
                        6f
                    ).toInt()
            }
            mTvActivityTitle.layoutParams = titleLayoutParams
            mTvActivityType.layoutParams = typeLayoutParams
        }
    }
}