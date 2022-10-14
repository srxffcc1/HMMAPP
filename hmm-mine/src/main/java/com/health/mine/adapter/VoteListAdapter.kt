package com.health.mine.adapter

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.bumptech.glide.Glide
import com.health.mine.R
import com.healthy.library.model.ActivityModel
import com.healthy.library.base.BaseHolder
import com.healthy.library.base.BaseMultiItemAdapter
import com.healthy.library.businessutil.ListUtil
import com.healthy.library.constant.SpKey
import com.healthy.library.utils.DateUtils
import com.healthy.library.utils.SpUtils
import com.healthy.library.utils.TransformUtil
import com.healthy.library.widget.CornerImageView

/**
 * @description
 * @author long
 * @date 2021/6/21
 */
class VoteListAdapter :
    BaseMultiItemAdapter<ActivityModel> {
    private var mViewType: Int = 0

    constructor(viewType: Int) : super(R.layout.item_participate_vote_layout) {
        mViewType = viewType
        addItemType(1, R.layout.item_participate_vote_layout)// 参与的投票
        addItemType(2, R.layout.item_enroll_vote_layout)// 报名的投票
    }

    lateinit var mLayoutYes: Drawable
    lateinit var mLayoutNo: Drawable

    override fun getItemViewType(position: Int): Int {
        return mViewType
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): BaseHolder {
        mLayoutYes = p0.resources.getDrawable(R.drawable.shape_enroll_vote_yes_bg)
        mLayoutNo = p0.resources.getDrawable(R.drawable.shape_enroll_vote_no_bg)
        return super.onCreateViewHolder(p0, p1)
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper()
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        val item = getDatas()[position]

        val mTvAction = holder.getView<AppCompatTextView>(R.id.tv_action)
        mTvAction.apply {
            //结束禁用按钮或者已领奖
            isEnabled =
                !(item.awardStatus == 2 || if (item.awardStatus == 1) false else item.activity?.status == 3)

            background = when {
                item.status == -1 -> context.getDrawable(R.drawable.shape_select_service_enabled_btn)
                else -> context.getDrawable(R.drawable.select_action_enabled)
            }

            text = when {
                item.awardStatus == 1 -> "去领奖"
                item.awardStatus == 2 -> "已领奖"
                mViewType == 1 -> "去投票"
                else -> "去拉票"
            }
            setOnClickListener {
                if (isClickInit()) {
                    moutClickListener.outClick("action", position)
                }
            }
        }

        when (mViewType) {
            1 -> buildParticipateVote(holder, item)
            2 -> buildVote(holder, item)
        }
    }

    /**
     * 参与的投票
     */
    private fun buildParticipateVote(
        holder: BaseHolder,
        item: ActivityModel
    ) {
        val mTime =
            DateUtils.getDateDay(item.votingStartTime, "yyyy-MM-dd HH:mm:ss", "yyyy.MM.dd") +
                    " - " + DateUtils.getDateDay(
                item.votingEndTime,
                "yyyy-MM-dd HH:mm:ss",
                "yyyy.MM.dd"
            )
        val mPrizeLayout = holder.setImg(
            R.id.iv_participate_vote, item.backgroundUrl,
            R.drawable.img_1_1_default, R.drawable.img_1_1_default
        )
            .setText(R.id.item_activity_title, item.name)
            .setText(R.id.item_activity_endTime, mTime)
            .getView<LinearLayout>(R.id.item_prize_layout)

        holder.itemView.setOnClickListener {
            if (isClickInit()) {
                moutClickListener.outClick("action", holder.layoutPosition)
            }
        }

        mPrizeLayout.removeAllViews()
        if (ListUtil.isEmpty(item.prizeList)) {
            mPrizeLayout.visibility = View.GONE
        } else {
            var prizeNumber: Int = 0
            item.prizeList?.forEachIndexed { index, prizeModel ->
                if (prizeNumber == 6) {
                    return@forEachIndexed
                }
                prizeNumber++
                val inflate =
                    LayoutInflater.from(context).inflate(R.layout.item_round_banner_image, null)

                mPrizeLayout.post {
                    val prizeWidth = mPrizeLayout.width / 7

                    val layoutParams = LinearLayout.LayoutParams(
                        prizeWidth,
                        prizeWidth
                    )
                    layoutParams.rightMargin = prizeWidth / 6

                    val mImg = inflate.findViewById<CornerImageView>(R.id.img)
                    mImg.setCornerRadius(4f)
                    var mGoodsImage = prizeModel.goodsImage
                    prizeModel.goodsDto?.let { it ->
                        mGoodsImage = it.goodsImage
                    }
                    Glide.with(mImg.context).load(mGoodsImage)
                        .error(R.drawable.img_1_1_default).placeholder(R.drawable.img_1_1_default)
                        .into(mImg)

                    mPrizeLayout.addView(inflate, layoutParams)
                }
            }
        }
    }

    /**
     * 报名的投票
     */
    private fun buildVote(
        holder: BaseHolder,
        item: ActivityModel
    ) {
        val mTime = if (item.awardStatus != 0)
            "领奖截止时间: " + DateUtils.getDateDay(
                item.activity?.rewardEndTime,
                "yyyy-MM-dd HH:mm:ss",
                "yyyy.MM.dd"
            )
        else DateUtils.getDateDay(
            item.activity?.votingStartTime,
            "yyyy-MM-dd HH:mm:ss",
            "yyyy.MM.dd"
        ) +
                " - " + DateUtils.getDateDay(
            item.activity?.votingEndTime,
            "yyyy-MM-dd HH:mm:ss",
            "yyyy.MM.dd"
        )

        val mVoteLayout = holder.getView<ConstraintLayout>(R.id.layout_enroll_vote_content)
        val mPrizeLayout = holder.setText(R.id.tv_ticket_count, item.votingNum + "票")
            .setText(
                R.id.tv_rank,
                String.format("第%s名", if (item.status == -1) "-" else item.ranking)
            )
            .setImg(
                R.id.iv_avatar, SpUtils.getValue(context, SpKey.USER_ICON),
                R.drawable.img_avatar_default, R.drawable.img_avatar_default
            )
            .setText(R.id.item_activity_title, item.activity?.name)
            .setText(R.id.item_activity_endTime, mTime)
            .getView<LinearLayout>(R.id.item_prize_layout)

        holder.getView<ImageView>(R.id.iv_vote_activity_status)?.apply {
            visibility =
                if (item.activity?.status == 3 || item.status == -1 || item.awardStatus != 0) View.VISIBLE else View.GONE
            val mResource = when {
                item.awardStatus != 0 -> R.drawable.vote_activity_win_a_lottery_img
                item.activity?.status == 3 -> R.drawable.vote_activity_end_img
                item.status == -1 -> R.drawable.vote_activity_freeze_img
                else -> 0
            }
            setImageResource(mResource)
        }
        //背景也需调整颜色
        mVoteLayout.background = when {
            item.awardStatus != 0 -> mLayoutYes
            else -> mLayoutNo
        }
        val mTvMessageTip = holder.getView<TextView>(R.id.tv_message_tip)
        mTvMessageTip.apply {
            visibility = if (item.awardStatus == 2 || item.status == -1) View.VISIBLE else View.GONE
            text = if (item.awardStatus != 0) "领奖单号：" + item.awardOrderNo else item.freezeReason
        }

        holder.itemView.setOnClickListener {
            if (isClickInit()) {
                if (!item.awardOrderNo.isNullOrEmpty()) {
                    moutClickListener.outClick("orderDetail", holder.layoutPosition)
                    return@setOnClickListener
                }
                moutClickListener.outClick("voteDetail", holder.layoutPosition)
            }
        }

        if (ListUtil.isEmpty(item.activity?.prizeList)) {
            mPrizeLayout.visibility = View.GONE
        } else {
            var prizeNumber: Int = 0
            /** 绘制奖品 */
            mPrizeLayout.visibility = View.VISIBLE
            mPrizeLayout.removeAllViews()

            item.activity?.prizeList?.forEachIndexed { index, prizeModel ->
                if (prizeNumber == 6) {
                    return@forEachIndexed
                }
                prizeNumber++
                val inflate =
                    LayoutInflater.from(context).inflate(R.layout.item_round_banner_image, null)
                mPrizeLayout.post {
                    val prizeWidth = mPrizeLayout.width / 7

                    val layoutParams = LinearLayout.LayoutParams(
                        prizeWidth,
                        prizeWidth
                    )
                    layoutParams.rightMargin = prizeWidth / 6

                    val mImg = inflate.findViewById<CornerImageView>(R.id.img)
                    mImg.setCornerRadius(4f)
                    var mGoodsImage = prizeModel.goodsImage
                    prizeModel.goodsDto?.let {
                        mGoodsImage = it.goodsImage
                    }
                    Glide.with(mImg.context).load(mGoodsImage)
                        .error(R.drawable.img_1_1_default).placeholder(R.drawable.img_1_1_default)
                        .into(mImg)

                    mPrizeLayout.addView(inflate, layoutParams)
                }
            }
        }
    }
}