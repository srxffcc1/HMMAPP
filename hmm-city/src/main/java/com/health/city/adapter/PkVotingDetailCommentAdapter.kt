package com.health.city.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.lib_ShapeView.view.ShapeTextView
import com.example.lib_ShapeView.view.ShapeView
import com.health.city.R
import com.health.city.activity.PkVotingDetailActivity
import com.healthy.library.LibApplication
import com.healthy.library.base.BaseAdapter
import com.healthy.library.base.BaseHolder
import com.healthy.library.businessutil.GlideCopy
import com.healthy.library.businessutil.ListUtil
import com.healthy.library.model.Discuss
import com.healthy.library.routes.CityRoutes
import com.healthy.library.utils.TransformUtil
import com.healthy.library.widget.CollapsedTextView
import com.healthy.library.widget.ImageTextView
import org.jetbrains.anko.internals.AnkoInternals.createAnkoContext

/**
 * author : long
 * Time   :2021/11/15
 * desc   :PK投票详情评论列表
 */
class PkVotingDetailCommentAdapter :
    BaseQuickAdapter<Discuss, BaseViewHolder>(R.layout.item_pk_voting_detail_comment_layout) {

    private var likeNormal: Drawable? = null
    private var likeSelect: Drawable? = null
    private var postingStatus = 0
    private var onReViewClickListener: OnReViewClickListener? = null
    private var mSquareTitle: String = ""
    private var mConSideTitle: String = ""
    public var isRemoveViews:Boolean = false

    fun setPkTitle(squareTitle: String, conSideTitle: String) {
        this.mSquareTitle = squareTitle
        this.mConSideTitle = conSideTitle
    }

    fun setOnReViewClickListener(onReViewClickListener: OnReViewClickListener) {
        this.onReViewClickListener = onReViewClickListener;
    }

    fun setPostingStatus(postingStatus: Int) {
        this.postingStatus = postingStatus
    }

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        likeNormal = parent.context.resources.getDrawable(R.drawable.cityrightlike)
        likeSelect = parent.context.resources.getDrawable(R.drawable.cityrightliker)
        return super.onCreateDefViewHolder(parent, viewType)
    }

    override fun convert(holder: BaseViewHolder, item: Discuss) {

        val mLine = holder.getView<ShapeView>(R.id.line)
        val layoutParams = mLine.layoutParams as ConstraintLayout.LayoutParams
        holder.setGone(R.id.noMsgCon, data[0].id == 0)
            .setGone(R.id.hasContent, data[0].id != 0)
            .setGone(R.id.line, data[0].id == 0)
            .setGone(R.id.mBottomView, data[0].id == 0)

        if (data[0].id == 0) {
            layoutParams.topToBottom = R.id.noMsgCon
            mLine.layoutParams = layoutParams
            return
        }

        if (holder.layoutPosition == data.size) {
            layoutParams.topToBottom = R.id.hasContent
            mLine.layoutParams = layoutParams
        }

        val tv_comment_content: CollapsedTextView = holder.getView(R.id.tv_comment_content)
        val like: ImageTextView = holder.getView(R.id.like)
        val mLookAllComment = holder.getView<ConstraintLayout>(R.id.look_all)//查看更多评论
        mLookAllComment.isEnabled = true
        val mCommentArrow = holder.getView<ImageView>(R.id.iv_pk_voting_arrow)//查看更多评论
        val recoverpage: TextView = holder.getView(R.id.recoverpage)
        val reviewparent: LinearLayout = holder.getView(R.id.ll_review)
        val mUserBadge = holder.getView<ImageView>(R.id.iv_user_badge)
        val mUserBadgeName = holder.getView<ShapeTextView>(R.id.stv_user_badgeName)
        val mUserSuppory = holder.getView<ShapeTextView>(R.id.UserSupport)

        /*--------------------------- 用户认证 START --------------------------------*/
        val shapeDrawableBuilder = mUserBadgeName.shapeDrawableBuilder
        if (0 == item.badgeType) {
            //达人认证
            mUserBadge.setImageResource(R.drawable.icon_user_certification)
            shapeDrawableBuilder
                .setSolidColor(0)
                .setGradientColors(
                    Color.parseColor("#FF6060"),
                    Color.parseColor("#FF256C")
                ).setBottomLeftRadius(0f)
                .intoBackground()
        }
        if (1 == item.badgeType) {
            //官方认证
            mUserBadge.setImageResource(R.drawable.icon_user_official_certification)
            shapeDrawableBuilder.clearGradientColors()
            shapeDrawableBuilder
                .setSolidColor(Color.parseColor("#3889FD"))
                .setBottomLeftRadius(0f)
                .intoBackground()
        }
        /*--------------------------- 用户认证 END --------------------------------*/

        /*--------------------------- 当前用户最后立场 START --------------------------------*/
        if ("1" == item.lastVoteStand) {
            val shapeBuilder = mUserSuppory.shapeDrawableBuilder
            shapeBuilder.clearGradientColors()
            shapeBuilder.setSolidColor(Color.parseColor("#FFF4F4"))
                .setBottomLeftRadius(shapeBuilder.topLeftRadius)
                .intoBackground()
            mUserSuppory.setTextColor(Color.parseColor("#FA3C5A"))
            mUserSuppory.text = "TA支持 $mSquareTitle"
            holder.setImageResource(R.id.icon_SupportArrow, R.drawable.icon_user_comment_red)
        }
        if ("2" == item.lastVoteStand) {
            val shapeBuilder = mUserSuppory.shapeDrawableBuilder
            shapeBuilder.clearGradientColors()
            shapeBuilder.setSolidColor(Color.parseColor("#F4F8FF"))
                .setBottomLeftRadius(shapeBuilder.topLeftRadius)
                .intoBackground()
            mUserSuppory.setTextColor(Color.parseColor("#4369D3"))
            mUserSuppory.text = "TA支持 $mConSideTitle"
            holder.setImageResource(R.id.icon_SupportArrow, R.drawable.icon_user_comment_blue)
        }

        /*--------------------------- 当前用户最后立场 END --------------------------------*/

        holder.setGone(R.id.noMsg, false)
            .setGone(R.id.line, holder.layoutPosition == data.size)
            .setGone(R.id.mBottomView, holder.layoutPosition == data.size)
            .setGone(R.id.UserSupport, !TextUtils.isEmpty(item.lastVoteStand))
            .setGone(R.id.icon_SupportArrow, !TextUtils.isEmpty(item.lastVoteStand))
            .setGone(R.id.stv_user_badgeName, !TextUtils.isEmpty(item.badgeId))
            .setGone(R.id.iv_user_badge, !TextUtils.isEmpty(item.badgeId))
            .setText(R.id.stv_user_badgeName, item.badgeName)
            .setText(
                R.id.tv_customer_name,
                if (TextUtils.isEmpty(item.fromMemberName)) "用户已注销" else item.fromMemberName
            )
            .setText(R.id.like, item.praiseNum.toString())
            .setText(R.id.tv_comment_date, item.createTimeStr)
            .setText(R.id.tv_comment_content, item.content)

        holder.getView<View>(R.id.iv_customer_avatar).setOnClickListener {
            if ("用户已注销" != item.fromMemberName) {
                ARouter.getInstance()
                    .build(CityRoutes.CITY_FANDETAIL)
                    .withString("searchMemberType", item.memberType.toString())
                    .withString("memberId", item.fromMemberId)
                    .navigation()
            }
        }
        holder.getView<View>(R.id.tv_customer_name).setOnClickListener {
            if ("用户已注销" != item.fromMemberName) {
                ARouter.getInstance()
                    .build(CityRoutes.CITY_FANDETAIL)
                    .withString("searchMemberType", item.memberType.toString() + "")
                    .withString("memberId", item.fromMemberId + "")
                    .navigation()
            }
        }
        holder.getView<View>(R.id.like).setOnClickListener {
            if (postingStatus == 1 || "用户已注销" == item.fromMemberName) {
                return@setOnClickListener
            }
            val pkVotingDetailActivity = mContext as PkVotingDetailActivity
            pkVotingDetailActivity.outClick("commentLike", item)

            item.praiseState = if (item.praiseState == 0) 1 else 0
            item.praiseNum = item.praiseNum + if (item.praiseState == 0) -1 else 1
            like.text =
                if (item.praiseNum == 0) "  " else item.praiseNum.toString() + ""
            if (item.praiseState == 0) {
                like.setDrawable(likeNormal)
                like.setTextColor(Color.parseColor("#444444"))
            } else {
                like.setTextColor(Color.parseColor("#F93F60"))
                like.setDrawable(likeSelect)
            }
        }
        holder.getView<View>(R.id.look_all).setOnClickListener {
            if (item.isMore) {
                item.isShow = false
                //mCommentArrow.rotation = 180f
            } else {
                item.isShow = item.openCount == item.totalNum
                mLookAllComment.isEnabled = false
                //mCommentArrow.rotation = 0f
            }
            item.isClickAction = true
            onReViewClickListener?.onCollapsed(item, holder.layoutPosition)

        }

        GlideCopy.with(mContext)
            .load(item.fromMemberFaceUrl)
            .placeholder(R.drawable.img_1_1_default2)
            .error(R.drawable.img_avatar_default)

            .into(holder.getView(R.id.iv_customer_avatar) as ImageView)

        tv_comment_content.setIsExpanded(false) //默认不展开
        tv_comment_content.setCurrentLine(0)
        tv_comment_content.setOnClickListener {
            onReViewClickListener?.onClick(
                it,
                item.id.toString(),
                item.fromMemberId,
                "0",
                "评论:" + item.fromMemberName,
                item.memberType.toString()
            )
        }

        if (TextUtils.isEmpty(item.fromMemberName)) {
            item.fromMemberName = "用户已注销"
        }

        like.text = if (item.praiseNum == 0) "  " else item.praiseNum.toString()

        if (item.praiseState == 0) {
            like.setDrawable(likeNormal)
            like.setTextColor(Color.parseColor("#444444"))
        } else {
            like.setTextColor(Color.parseColor("#F93F60"))
            like.setDrawable(likeSelect)
        }
        like.visibility = View.VISIBLE
        if ("用户已注销" == item.fromMemberName) {
            like.visibility = View.GONE
        }

        if (item.totalNum < 1) {
            holder.getView<View>(R.id.con_review).visibility = View.GONE
        } else {
            holder.getView<View>(R.id.con_review).visibility = View.VISIBLE
            buildReview(mContext, reviewparent, item.discussReplyList, item)
            if (item.totalNum <= 3) {
                mLookAllComment.visibility = View.GONE
            } else {
                mLookAllComment.visibility = View.VISIBLE
                if (item.openCount >= item.totalNum) {
                    recoverpage.text = "收起"
                    mCommentArrow.rotation = 180f
                } else {
                    recoverpage.text = "${item.totalNum - item.openCount}条回复"
                    mCommentArrow.rotation = 0f
                }
            }
        }
    }

    /**
     * 创建二级回复评论视图
     */
    fun buildReview(
        context: Context,
        reviewparent: LinearLayout,
        discussReplyList: List<Discuss.DiscussReply>,
        discuss: Discuss
    ) {
        reviewparent.removeAllViews()
        var needshowsize = 3
        if (discuss.openCount < discuss.totalNum && discuss.isClickAction) {
            needshowsize = discuss.openCount + 3
            discuss.isClickAction = false
        } else if (discuss.openCount == discuss.totalNum && discuss.isClickAction) {
            needshowsize = discuss.totalNum
            discuss.isClickAction = false
        }
        if (needshowsize > discuss.totalNum) {
            needshowsize = discuss.totalNum
        }

        discuss.openCount = needshowsize
        if (discussReplyList.isNotEmpty()) {
            reviewparent.visibility = View.VISIBLE
        } else {
            reviewparent.visibility = View.GONE
        }

        try {
            for (i in 0 until needshowsize) {
                val discussReply = discussReplyList[i]

                val mView = LayoutInflater.from(context)
                    .inflate(R.layout.item_pk_voting_detail_comment_child_layout, null)

                val mTabs = mView.findViewById<RelativeLayout>(R.id.tabs)
                val mCustomerAvatar = mView.findViewById<ImageView>(R.id.iv_customer_avatar)
                val mUserBadge = mView.findViewById<ImageView>(R.id.iv_user_badge)
                val mCustomerName = mView.findViewById<TextView>(R.id.tv_customer_name)
                val mUserBadgeName = mView.findViewById<ShapeTextView>(R.id.stv_user_badgeName)
                val mReplyContent = mView.findViewById<TextView>(R.id.tvReplyContent)

                /*------------------------ 用户身份及当前用户立场 START ------------------------------*/
                mUserBadgeName.text = discuss.badgeName
                val shapeDrawableBuilder = mUserBadgeName.shapeDrawableBuilder
                var mVisibility = View.GONE
                if (TextUtils.isEmpty(discussReply.fromVoteStand)) {
                    //当立场为空展示 用户凸显标志
                    if (!TextUtils.isEmpty(discussReply.fromBadgeId)) {
                        mVisibility = View.GONE
                        mUserBadgeName.setTextColor(Color.WHITE)
                        if (0 == discussReply.fromBadgeType) {
                            //达人认证
                            mUserBadge.setImageResource(R.drawable.icon_user_certification)
                            shapeDrawableBuilder
                                .setSolidColor(0)
                                .setBottomLeftRadius(0f)
                                .setGradientColors(
                                    Color.parseColor("#FF6060"),
                                    Color.parseColor("#FF256C")
                                )
                                .intoBackground()
                        }
                        if (1 == discussReply.fromBadgeType) {
                            //官方认证
                            mUserBadge.setImageResource(R.drawable.icon_user_official_certification)
                            shapeDrawableBuilder.clearGradientColors()
                            shapeDrawableBuilder
                                .setBottomLeftRadius(0f)
                                .setSolidColor(Color.parseColor("#3889FD"))
                                .intoBackground()
                        }
                    }
                    mUserBadge.visibility = mVisibility
                } else {
                    mUserBadge.visibility = View.GONE
                    mVisibility = View.VISIBLE
                    //正方
                    if ("1" == discussReply.fromVoteStand) {
                        shapeDrawableBuilder.clearGradientColors()
                        shapeDrawableBuilder.setSolidColor(Color.parseColor("#FFF4F4"))
                            .setBottomLeftRadius(shapeDrawableBuilder.topLeftRadius)
                            .intoBackground()
                        mUserBadgeName.setTextColor(Color.parseColor("#FA3C5A"))
                        mUserBadgeName.text = "TA支持 $mSquareTitle"
                    }
                    //反方
                    if ("2" == discussReply.fromVoteStand) {
                        shapeDrawableBuilder.clearGradientColors()
                        shapeDrawableBuilder.setSolidColor(Color.parseColor("#F4F8FF"))
                            .setBottomLeftRadius(shapeDrawableBuilder.topLeftRadius)
                            .intoBackground()
                        mUserBadgeName.setTextColor(Color.parseColor("#4369D3"))
                        mUserBadgeName.text = "TA支持 $mConSideTitle"
                    }
                }
                mUserBadgeName.visibility = mVisibility
                /*------------------------ 用户身份及当前用户立场 END -------------------------------*/

                GlideCopy.with(mCustomerAvatar.context)
                    .load(discussReply.fromMemberFaceUrl)
                    .error(R.drawable.img_avatar_default)
                    .placeholder(R.drawable.img_avatar_default)
                    .into(mCustomerAvatar)

                if (TextUtils.isEmpty(discussReply.fromMemberName)) {
                    discussReply.fromMemberName = "用户已注销"
                }
                if (TextUtils.isEmpty(discussReply.toMemberName)) {
                    discussReply.toMemberName = "用户已注销"
                }
                mCustomerName.text = discussReply.fromMemberName
                val spannableString =
                    SpannableStringBuilder("回复 ${discussReply.toMemberName}:${discussReply.content}")
                val colorSpan = ForegroundColorSpan(Color.parseColor("#444444"))
                spannableString.setSpan(
                    colorSpan,
                    "回复 ${discussReply.toMemberName}:".length,
                    "回复 ${discussReply.toMemberName}:".length + discussReply.content.length,
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                )

                mReplyContent.text = spannableString
                mTabs.setOnClickListener {
                    if ("用户已注销" != discussReply.fromMemberName) {
                        ARouter.getInstance()
                            .build(CityRoutes.CITY_FANDETAIL)
                            .withString(
                                "searchMemberType",
                                discussReply.fromMemberType.toString()
                            )
                            .withString("memberId", discussReply.fromMemberId)
                            .navigation()
                    }
                }
                mView.setOnClickListener {
                    onReViewClickListener?.onClick(
                        it,
                        discussReply.postingDiscussId.toString(),
                        discussReply.fromMemberId,
                        discussReply.id.toString(),
                        "回复:" + discussReply.fromMemberName,
                        discussReply.fromMemberType.toString()
                    )
                }
                if (i == needshowsize - 1) {
                    //如果是最后一条不加入底部填充
                    mView.setPadding(0, 0, 0, 0)
                }
                reviewparent.addView(mView)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    interface OnReViewClickListener {
        fun onClick(
            view: View,
            commentDiscussId: String,
            toMemberId: String,
            fatherId: String,
            fromName: String,
            toMemberType: String
        )

        fun onCollapsed(discuss: Discuss, position: Int)

    }
}