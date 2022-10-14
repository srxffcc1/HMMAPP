package com.health.index.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.index.R;
import com.health.index.model.CommentModel;
import com.healthy.library.model.Discuss;
import com.healthy.library.routes.CityRoutes;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.ExpandTextView;
import com.healthy.library.widget.ImageTextView;
import com.healthy.library.widget.RatingView;

import java.util.List;

/**
 * 创建日期：2022/1/6 10:01
 *
 * @author LiuWei
 * @version 1.0
 * 包名： com.health.index.adapter
 * 类说明：
 */

public class WebViewActivityAdapter extends BaseQuickAdapter<CommentModel, BaseViewHolder> {

    public WebViewActivityAdapter(int layoutResId) {
        super(layoutResId);
    }

    public WebViewActivityAdapter() {
        this(R.layout.index_webview_article_activity_adapter_layout);
    }

    public void setOnReViewClickListener(OnReViewClickListener onReViewClickListener) {
        this.onReViewClickListener = onReViewClickListener;
    }

    private OnLikeClickListener onLikeClickListener;
    private OnReViewClickListener onReViewClickListener;

    public void setOnLikeClickListener(OnLikeClickListener onLikeClickListener) {
        this.onLikeClickListener = onLikeClickListener;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final CommentModel item) {
        ConstraintLayout hasContent;
        CornerImageView ivCustomerAvatar;
        ConstraintLayout llTopUserName;
        TextView tvCustomerName;
        TextView userTip;
        final ImageTextView like;
        RatingView ratingCustomerScore;
        TextView tvScore;
        final ExpandTextView tvCommentContent;
        final TextView recoverpagecontent;
        ConstraintLayout conReview;
        final LinearLayout llReview;
        final TextView recoverpage;
        GridLayout layoutImgs;
        TextView tvCommentDate;

        hasContent = (ConstraintLayout) helper.itemView.findViewById(R.id.hasContent);
        ivCustomerAvatar = (CornerImageView) helper.itemView.findViewById(R.id.iv_customer_avatar);
        llTopUserName = (ConstraintLayout) helper.itemView.findViewById(R.id.ll_top_userName);
        tvCustomerName = (TextView) helper.itemView.findViewById(R.id.tv_customer_name);
        userTip = (TextView) helper.itemView.findViewById(R.id.user_tip);
        like = (ImageTextView) helper.itemView.findViewById(R.id.like);
        ratingCustomerScore = (RatingView) helper.itemView.findViewById(R.id.rating_customer_score);
        tvScore = (TextView) helper.itemView.findViewById(R.id.tv_score);
        tvCommentContent = (ExpandTextView) helper.itemView.findViewById(R.id.tv_comment_content);
        recoverpagecontent = (TextView) helper.itemView.findViewById(R.id.recoverpagecontent);
        conReview = (ConstraintLayout) helper.itemView.findViewById(R.id.con_review);
        llReview = (LinearLayout) helper.itemView.findViewById(R.id.ll_review);
        recoverpage = (TextView) helper.itemView.findViewById(R.id.recoverpage);
        layoutImgs = (GridLayout) helper.itemView.findViewById(R.id.layout_imgs);
        tvCommentDate = (TextView) helper.itemView.findViewById(R.id.tv_comment_date);
        if (item.id.equals("null")) {
            helper.getView(R.id.noMsgCon).setVisibility(View.VISIBLE);
            helper.getView(R.id.hasContent).setVisibility(View.GONE);
            return;
        } else {
            helper.getView(R.id.noMsgCon).setVisibility(View.GONE);
            helper.getView(R.id.hasContent).setVisibility(View.VISIBLE);
        }
        recoverpagecontent.setVisibility(View.GONE);
        com.healthy.library.businessutil.GlideCopy.with(mContext)
                .load(item.fromMemberFaceUrl)
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_avatar_default)
                .dontAnimate()
                .into(ivCustomerAvatar);
        tvCustomerName.setText(TextUtils.isEmpty(item.fromMemberName) ? "用户已注销" : item.fromMemberName);
        if ("".equals(item.memberState) || item.memberState == null || "null".equals(item.memberState)) {
            userTip.setVisibility(View.GONE);
        } else {
            userTip.setVisibility(View.VISIBLE);
            userTip.setText(item.memberState.replace("育儿期", "") + "");
        }
        tvCommentContent.clearText();
        tvCommentContent.setText(item.content);
        tvCommentContent.setCallback(new ExpandTextView.Callback() {
            @Override
            public void onExpand() {
                recoverpagecontent.setVisibility(View.VISIBLE);
                recoverpagecontent.setText("收起");
            }

            @Override
            public void onCollapse() {
                recoverpagecontent.setVisibility(View.VISIBLE);
                recoverpagecontent.setText("全文");
            }

            @Override
            public void onLoss() {
                recoverpagecontent.setVisibility(View.GONE);
            }
        });
        tvCommentContent.setChanged(item.isShowContent);
        recoverpagecontent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.isShowContent = !item.isShowContent;
                tvCommentContent.setChanged(item.isShowContent);
            }
        });
        tvCommentDate.setText(item.timeRemark);
        like.setText(item.praiseCount + "");
        tvCommentContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onReViewClickListener.onClick(v, item.id + "", item.fromMemberId, "0", "评论:" + item.fromMemberName, item.memberType + "");
            }
        });
        if (TextUtils.isEmpty(item.fromMemberName)) {
            item.fromMemberName = "用户已注销";
        }
        ivCustomerAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!"用户已注销".equals(item.fromMemberName)) {
                    ARouter.getInstance()
                            .build(CityRoutes.CITY_FANDETAIL)
                            .withString("searchMemberType", item.memberType + "")
                            .withString("memberId", item.fromMemberId + "")
                            .navigation();
                }
            }
        });
        tvCustomerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!"用户已注销".equals(item.fromMemberName)) {
                    ARouter.getInstance()
                            .build(CityRoutes.CITY_FANDETAIL)
                            .withString("searchMemberType", item.memberType + "")
                            .withString("memberId", item.fromMemberId + "")
                            .navigation();
                }
            }
        });
        final Drawable likeNormal = mContext.getResources().getDrawable(R.drawable.cityrightlike);
        final Drawable likeSelect = mContext.getResources().getDrawable(R.drawable.cityrightliker);
        if (!item.praise) {
            like.setDrawable(likeNormal);
            like.setTextColor(Color.parseColor("#444444"));
        } else {
            like.setTextColor(Color.parseColor("#F93F60"));
            like.setDrawable(likeSelect);
        }
        like.setVisibility(View.VISIBLE);
        if ("用户已注销".equals(item.fromMemberName)) {
            like.setVisibility(View.GONE);
        }
        if (item.isShow) {
            recoverpage.setText("收起");
        } else {
            recoverpage.setText("查看更多回复");
        }
        recoverpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.isShow = !item.isShow;
                if (item.isShow) {
                    recoverpage.setText("收起");
                } else {
                    recoverpage.setText("查看更多回复");
                }
                buildReview(mContext, llReview, item.replyList, item);
                notifyItemChanged(helper.getAdapterPosition());
            }
        });
        if (item.replyList == null || item.replyList.size() < 1) {
            helper.getView(R.id.con_review).setVisibility(View.GONE);
        } else {
            helper.getView(R.id.con_review).setVisibility(View.VISIBLE);
            buildReview(mContext, llReview, item.replyList, item);
            if (item.replyList.size() <= 3) {
                recoverpage.setVisibility(View.GONE);
            } else {
                recoverpage.setVisibility(View.VISIBLE);
            }
        }
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("用户已注销".equals(item.fromMemberName)) {
                    return;
                }
                if (onLikeClickListener != null) {
                    onLikeClickListener.like(item.id + "", item.praise ? "0" : "1");
                    item.praiseCount = item.praiseCount + (item.praise ? -1 : 1);
                    like.setText(item.praiseCount == 0 ? "  " : item.praiseCount + "");
                    if (item.praise) {
                        like.setDrawable(likeNormal);
                        like.setTextColor(Color.parseColor("#444444"));
                    } else {
                        like.setTextColor(Color.parseColor("#F93F60"));
                        like.setDrawable(likeSelect);
                    }
                }
            }
        });
    }

    public void buildReview(final Context context, final LinearLayout reviewparent, final List<CommentModel.ReplyListBean> discussReplyList, final CommentModel discuss) {
        reviewparent.removeAllViews();

        reviewparent.post(new Runnable() {
            @Override
            public void run() {
                int needshowsize = 3 >= discussReplyList.size() ? discussReplyList.size() : 3;
                if (discussReplyList != null && discussReplyList.size() > 0) {
                    reviewparent.setVisibility(View.VISIBLE);
                } else {
                    reviewparent.setVisibility(View.GONE);
                }
                if (discuss.isShow) {
                    needshowsize = discussReplyList.size();
                }
                for (int i = 0; i < needshowsize; i++) {
                    final CommentModel.ReplyListBean discussReply = discussReplyList.get(i);
                    View view = LayoutInflater.from(context).inflate(com.healthy.library.R.layout.item_article_comment_reply_layout, null);
                    ConstraintLayout hasContent = (ConstraintLayout) view.findViewById(com.healthy.library.R.id.hasContent);
                    CornerImageView avatarImg = (CornerImageView) view.findViewById(com.healthy.library.R.id.avatarImg);
                    TextView commentNickName = (TextView) view.findViewById(com.healthy.library.R.id.commentNickName);
                    TextView replyNickName = (TextView) view.findViewById(com.healthy.library.R.id.replyNickName);
                    TextView commentTime = (TextView) view.findViewById(com.healthy.library.R.id.commentTime);
                    ExpandTextView commentContent = (ExpandTextView) view.findViewById(com.healthy.library.R.id.commentContent);

                    Glide.with(context)
                            .load(discussReply.fromMemberFaceUrl)
                            .placeholder(com.healthy.library.R.drawable.img_1_1_default2)
                            .error(com.healthy.library.R.drawable.img_avatar_default)
                            .into(avatarImg);
                    commentNickName.setText(TextUtils.isEmpty(discussReply.fromMemberName) ? "" : discussReply.fromMemberName);
//            replyNickName.setText("回复 " + new String(TextUtils.isEmpty(discussReply.toMemberName) ? "" : discussReply.toMemberName));
                    commentTime.setText(discussReply.timeRemark);
                    commentContent.setText(discussReply.content);
                    avatarImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!"用户已注销".equals(discussReply.fromMemberName)) {
                                ARouter.getInstance()
                                        .build(CityRoutes.CITY_FANDETAIL)
                                        .withString("searchMemberType", discussReply.fromMemberType + "")
                                        .withString("memberId", discussReply.fromMemberId + "")
                                        .navigation();
                            }
                        }
                    });
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onReViewClickListener.onClick(v, discussReply.knowledgeDiscussId + "", discussReply.fromMemberId + "", discussReply.id + "", "回复:" + discussReply.fromMemberName, discussReply.fromMemberType + "");
                        }
                    });

                    reviewparent.addView(view);
                }
            }
        });
    }

    public interface OnReViewClickListener {
        void onClick(View view, String commentDiscussId, String toMemberId, String fatherId, String fromName, String toMemberType);
    }


    public interface OnLikeClickListener {
        void like(String commentDiscussId, String type);
    }
}
