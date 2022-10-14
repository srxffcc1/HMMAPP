package com.healthy.library.adapter;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.healthy.library.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.model.VideoCommentModel;
import com.healthy.library.routes.CityRoutes;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.ExpandTextView;
import com.healthy.library.widget.ImageTextView;
import com.healthy.library.widget.RatingView;

import java.util.List;

public class VideoCommentListAdapter extends BaseAdapter<VideoCommentModel> {


    public VideoCommentListAdapter() {
        this(R.layout.item_video_comment_list_adapter);
    }

    public VideoCommentListAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull final BaseHolder holder, int position) {
        ConstraintLayout hasContent;
        CornerImageView ivCustomerAvatar;
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
        TextView tvCommentDate;

        hasContent = (ConstraintLayout) holder.itemView.findViewById(R.id.hasContent);
        ivCustomerAvatar = (CornerImageView) holder.itemView.findViewById(R.id.iv_customer_avatar);
        tvCustomerName = (TextView) holder.itemView.findViewById(R.id.tv_customer_name);
        userTip = (TextView) holder.itemView.findViewById(R.id.user_tip);
        like = (ImageTextView) holder.itemView.findViewById(R.id.like);
        ratingCustomerScore = (RatingView) holder.itemView.findViewById(R.id.rating_customer_score);
        tvScore = (TextView) holder.itemView.findViewById(R.id.tv_score);
        tvCommentContent = (ExpandTextView) holder.itemView.findViewById(R.id.tv_comment_content);
        recoverpagecontent = (TextView) holder.itemView.findViewById(R.id.recoverpagecontent);
        conReview = (ConstraintLayout) holder.itemView.findViewById(R.id.con_review);
        llReview = (LinearLayout) holder.itemView.findViewById(R.id.ll_review);
        recoverpage = (TextView) holder.itemView.findViewById(R.id.recoverpage);
        tvCommentDate = (TextView) holder.itemView.findViewById(R.id.tv_comment_date);

        final VideoCommentModel videoCommentModel = getDatas().get(position);
        if (videoCommentModel == null) {
            hasContent.setVisibility(View.GONE);
            return;
        } else {
            hasContent.setVisibility(View.VISIBLE);
        }
        Glide.with(context)
                .load(videoCommentModel.fromMemberFaceUrl)
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_avatar_default)

                .into(ivCustomerAvatar);
        tvCustomerName.setText(TextUtils.isEmpty(videoCommentModel.fromMemberName) ? "" : videoCommentModel.fromMemberName);
        if ("".equals(videoCommentModel.memberState) || videoCommentModel.memberState == null || "null".equals(videoCommentModel.memberState)) {
            userTip.setVisibility(View.GONE);
        } else {
            userTip.setText(videoCommentModel.memberState + "");
        }
        tvCommentContent.clearText();
        tvCommentContent.setText(videoCommentModel.content);
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
        tvCommentContent.setChanged(videoCommentModel.isShowContent);
        recoverpagecontent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoCommentModel.isShowContent = !videoCommentModel.isShowContent;
                tvCommentContent.setChanged(videoCommentModel.isShowContent);
            }
        });
        like.setText(videoCommentModel.praiseCount + "");
        final Drawable likeSelect = context.getResources().getDrawable(R.drawable.hanmom_videodetial_click_praise);
        final Drawable likeNormal = context.getResources().getDrawable(R.drawable.hanmom_videocomment_click_icon);
        if (videoCommentModel.praise) {
            like.setDrawable(likeSelect);
        } else {
            like.setDrawable(likeNormal);
        }
        //tvCommentDate.setText(DateUtils.getClassDatePost(VideoCommentModel.createTime));
        tvCommentDate.setText(videoCommentModel.timeRemark);
        if (videoCommentModel.isShow) {
            recoverpage.setText("收起");
        } else {
            recoverpage.setText("展开更多回复");
        }
        recoverpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoCommentModel.isShow = !videoCommentModel.isShow;
                if (videoCommentModel.isShow) {
                    recoverpage.setText("收起");
                } else {
                    recoverpage.setText("展开更多回复");
                }
                buildReview(context, llReview, videoCommentModel.replyList, videoCommentModel);
            }
        });

        if (videoCommentModel.replyList == null || videoCommentModel.replyList.size() < 1) {
            conReview.setVisibility(View.GONE);
        } else {
            conReview.setVisibility(View.VISIBLE);
            buildReview(context, llReview, videoCommentModel.replyList, videoCommentModel);
            if (videoCommentModel.replyList.size() <= 6) {
                recoverpage.setVisibility(View.GONE);
            } else {
                recoverpage.setVisibility(View.VISIBLE);
            }
        }
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moutClickListener != null) {
                    if (videoCommentModel.praise) {
                        moutClickListener.outClick("unPraise", videoCommentModel.id);
                    } else {
                        moutClickListener.outClick("praise", videoCommentModel.id);
                    }
                }
            }
        });
        tvCustomerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moutClickListener != null) {
                    moutClickListener.outClick("comment", videoCommentModel);
                }
            }
        });
        tvCommentContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moutClickListener != null) {
                    moutClickListener.outClick("comment", videoCommentModel);
                }
            }
        });
    }

    public void buildReview(Context context, LinearLayout reviewparent, List<VideoCommentModel.ReplyListBean> discussReplyList, VideoCommentModel videoCommentModel) {
        reviewparent.removeAllViews();
        int needshowsize = 6 >= discussReplyList.size() ? discussReplyList.size() : 6;
        if (discussReplyList != null && discussReplyList.size() > 0) {
            reviewparent.setVisibility(View.VISIBLE);
        } else {
            reviewparent.setVisibility(View.GONE);
        }
        if (videoCommentModel.isShow) {
            needshowsize = discussReplyList.size();
        }
        for (int i = 0; i < needshowsize; i++) {
            final VideoCommentModel.ReplyListBean discussReply = discussReplyList.get(i);
            View view = LayoutInflater.from(context).inflate(R.layout.item_video_comment_reply_layout, null);
            ConstraintLayout hasContent = (ConstraintLayout) view.findViewById(R.id.hasContent);
            CornerImageView avatarImg = (CornerImageView) view.findViewById(R.id.avatarImg);
            TextView commentNickName = (TextView) view.findViewById(R.id.commentNickName);
            TextView replyNickName = (TextView) view.findViewById(R.id.replyNickName);
            TextView commentTime = (TextView) view.findViewById(R.id.commentTime);
            ExpandTextView commentContent = (ExpandTextView) view.findViewById(R.id.commentContent);

            Glide.with(context)
                    .load(discussReply.fromMemberFaceUrl)
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_avatar_default)

                    .into(avatarImg);
            commentNickName.setText(TextUtils.isEmpty(discussReply.fromMemberName) ? "" : discussReply.fromMemberName);
            replyNickName.setText("回复 " + new String(TextUtils.isEmpty(discussReply.toMemberName) ? "" : discussReply.toMemberName));
            commentTime.setText(discussReply.timeRemark);
            commentContent.setText(discussReply.content);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (moutClickListener != null) {
                        moutClickListener.outClick("reply", discussReply);
                    }
                }
            });
            reviewparent.addView(view);
        }
    }
}
