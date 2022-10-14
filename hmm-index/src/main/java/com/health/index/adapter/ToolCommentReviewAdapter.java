package com.health.index.adapter;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.index.R;
import com.healthy.library.model.ToolsFoodDiscuss;
import com.healthy.library.utils.DateUtils;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.ExpandTextView;
import com.healthy.library.widget.ImageTextView;

import java.util.List;


/**
 * @author Li
 * @date 2019/03/12 16:14
 * @des 评论列表
 */

public class ToolCommentReviewAdapter extends BaseQuickAdapter<ToolsFoodDiscuss, BaseViewHolder> {



    public ToolCommentReviewAdapter() {
        this(R.layout.index_activity_tools_item_food_detail_list);
    }

    public void setOnReViewClickListener(OnReViewClickListener onReViewClickListener) {
        this.onReViewClickListener = onReViewClickListener;
    }

     OnLikeClickListener onLikeClickListener;
    OnReViewClickListener onReViewClickListener;

    public void setOnLikeClickListener(OnLikeClickListener onLikeClickListener) {
        this.onLikeClickListener = onLikeClickListener;
    }

     ToolCommentReviewAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, final ToolsFoodDiscuss item) {

         ConstraintLayout hasContent;
         CornerImageView avatarIcon;
         TextView avatarName;
         final ExpandTextView tvCommentContent;
         final TextView recoverpagecontent;
         TextView time;
         final ImageTextView like;
         ImageTextView discuss;
         ConstraintLayout conReview;
         final LinearLayout llReview;
         final TextView recoverpage;
         ConstraintLayout noMsgCon;
         ImageView noMsg;
        
        
        hasContent = (ConstraintLayout) helper.itemView.findViewById(R.id.hasContent);
        avatarIcon = (CornerImageView) helper.itemView.findViewById(R.id.avatarIcon);
        avatarName = (TextView) helper.itemView.findViewById(R.id.avatarName);
        tvCommentContent = (ExpandTextView) helper.itemView.findViewById(R.id.tv_comment_content);
        recoverpagecontent = (TextView) helper.itemView.findViewById(R.id.recoverpagecontent);
        time = (TextView) helper.itemView.findViewById(R.id.time);
        like = (ImageTextView) helper.itemView.findViewById(R.id.like);
        discuss = (ImageTextView) helper.itemView.findViewById(R.id.discuss);
        conReview = (ConstraintLayout) helper.itemView.findViewById(R.id.con_review);
        llReview = (LinearLayout) helper.itemView.findViewById(R.id.ll_review);
        recoverpage = (TextView) helper.itemView.findViewById(R.id.recoverpage);
        noMsgCon = (ConstraintLayout) helper.itemView.findViewById(R.id.noMsgCon);
        noMsg = (ImageView) helper.itemView.findViewById(R.id.noMsg);




        if (item == null) {
            hasContent.setVisibility(View.GONE);
            noMsgCon.setVisibility(View.VISIBLE);
            return;
        } else {
            hasContent.setVisibility(View.VISIBLE);
            noMsgCon.setVisibility(View.GONE);
            if(!TextUtils.isEmpty(item.fromMemberFaceUrl)){
                com.healthy.library.businessutil.GlideCopy.with(mContext)
                        .load(item.fromMemberFaceUrl)
                        .placeholder(R.drawable.img_1_1_default2)
                        .error(R.drawable.img_1_1_default2)
                        .into(avatarIcon);
            }
        }

        avatarName.setText(item.fromMemberName);
        tvCommentContent.clearText();
        helper.setText(R.id.tv_comment_content, item.content);
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
                item.isShowContent=!item.isShowContent;
                tvCommentContent.setChanged(item.isShowContent);
            }
        });
        time.setText(DateUtils.getClassDatePost(item.createTime));


        discuss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onReViewClickListener.onClick(v,item.id+"",item.fromMemberId,"0","回复:"+item.fromMemberName,item.memberType+"");

            }
        });

        tvCommentContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onReViewClickListener.onClick(v,item.id+"",item.fromMemberId,"0","回复:"+item.fromMemberName,item.memberType+"");
            }
        });
        if(item.praiseStatus==0){
            like.setDrawable(R.drawable.ic_like,mContext);
        }else {
            like.setDrawable(R.drawable.ic_like_red,mContext);
        }
        if(item.praiseNum==0){
            like.setText("0");
        }else {
            like.setText(item.praiseNum+"");
        }
        if(item.replyNum==0){
            discuss.setText("0");
        }else {
            discuss.setText(item.replyNum+"");
        }
        if(item.isShow){
            recoverpage.setText("收起");
        }else {
            recoverpage.setText("展开");
        }
        recoverpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.isShow=!item.isShow;
                if(item.isShow){
                    recoverpage.setText("收起");
                }else {
                    recoverpage.setText("展开");
                }
                buildReview(mContext,llReview,item.replyDTOList,item);
            }
        });
        if(item.replyDTOList==null||item.replyDTOList.size()<1){
            conReview.setVisibility(View.GONE);
        }else {
            conReview.setVisibility(View.VISIBLE);
            buildReview(mContext,llReview,item.replyDTOList,item);
            if(item.replyDTOList.size()<=2){
                recoverpage.setVisibility(View.GONE);
            }else {
                recoverpage.setVisibility(View.VISIBLE);
            }
        }
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onLikeClickListener!=null){
                    onLikeClickListener.like(item.id+"",item.praiseStatus==0?"0":"1");

                    item.praiseStatus=item.praiseStatus==0?1:0;
                    item.praiseNum=item.praiseNum+(item.praiseStatus==0?-1:1);
                    like.setText(item.praiseNum==0?"0":item.praiseNum+"");
                    if(item.praiseStatus==0){
                        like.setDrawable(R.drawable.ic_like,mContext);
                    }else {
                        like.setDrawable(R.drawable.ic_like_red,mContext);
                    }
                }
            }
        });
    }

    private void buildReview(Context mContext, LinearLayout llReview, List<ToolsFoodDiscuss.Reply> replyDTOList, final ToolsFoodDiscuss item) {
        llReview.removeAllViews();
        int needshowsize = 2 >= replyDTOList.size() ? replyDTOList.size() : 2;
        if (replyDTOList != null && replyDTOList.size() > 0) {
            llReview.setVisibility(View.VISIBLE);
        } else {
            llReview.setVisibility(View.GONE);
        }
        if (item.isShow) {
            needshowsize = replyDTOList.size();
        }
        for (int i = 0; i < needshowsize; i++) {
            final ToolsFoodDiscuss.Reply discussReply = replyDTOList.get(i);
            TextView textView = new TextView(mContext);
            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            textView.setPadding(0, 0, 0, 10);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            textView.setTextColor(Color.parseColor("#ffff6266"));
            textView.setMovementMethod(LinkMovementMethod.getInstance());
            textView.setHighlightColor(mContext.getResources().getColor(android.R.color.transparent));
            if (TextUtils.isEmpty(discussReply.fromMemberName)) {
                discussReply.fromMemberName = "用户已注销";
            }
            SpannableStringBuilder spannableString = new SpannableStringBuilder(discussReply.fromMemberName + "" + " :" + discussReply.content + "");
            ClickableSpan clickableSpan1 = new ClickableSpan() {
                @Override
                public void onClick(View view) {

                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(0xffFF6266);
                    ds.setTypeface(Typeface.DEFAULT_BOLD);
                    ds.setUnderlineText(false);
                }
            };
            spannableString.setSpan(clickableSpan1, 0, (discussReply.fromMemberName + "" + " :").length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
            spannableString.setSpan(boldSpan, 0, (discussReply.fromMemberName + "" + " :").length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            ClickableSpan clickableSpan3 = new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    //System.out.println("回复点击");
                    onReViewClickListener.onClick(view, item.id + "", discussReply.fromMemberId + "", discussReply.id + "", "回复:" + item.fromMemberName, discussReply.fromMemberType + "");
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(0xff444444);
                    ds.setUnderlineText(false);
                }
            };
            spannableString.setSpan(clickableSpan3, (discussReply.fromMemberName + "" + " :").length(), (discussReply.fromMemberName + "" + " :" + discussReply.content + "").length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            textView.setText(spannableString);
            textView.requestLayout();
            llReview.addView(textView);
        }


    }

    public interface OnReViewClickListener {
        void onClick(View view, String commentDiscussId, String toMemberId, String fatherId, String fromName, String toMemberType);
    }


    public interface OnLikeClickListener {
        void like(String commentDiscussId, String type);
    }

}