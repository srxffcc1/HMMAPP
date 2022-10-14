package com.health.city.adapter;

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
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.city.R;
import com.healthy.library.model.Discuss;
import com.healthy.library.routes.CityRoutes;
import com.healthy.library.utils.DateUtils;
import com.healthy.library.widget.ExpandTextView;
import com.healthy.library.widget.ImageTextView;

import java.util.List;

/**
 * @author Li
 * @date 2019/03/12 16:14
 * @des 评论列表
 */

public class CityCommentReviewAdapter extends BaseQuickAdapter<Discuss, BaseViewHolder> {
    public int postingStatus=0;

    public void setPostingStatus(int postingStatus) {
        this.postingStatus = postingStatus;
    }

    public CityCommentReviewAdapter() {
        this(R.layout.city_item_activity_post_review_child);
    }

    public void setOnReViewClickListener(OnReViewClickListener onReViewClickListener) {
        this.onReViewClickListener = onReViewClickListener;
    }

    private OnLikeClickListener onLikeClickListener;
    OnReViewClickListener onReViewClickListener;

    public void setOnLikeClickListener(OnLikeClickListener onLikeClickListener) {
        this.onLikeClickListener = onLikeClickListener;
    }

    private CityCommentReviewAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, final Discuss item) {
        if(item==null){
            helper.getView(R.id.hasContent).setVisibility(View.GONE);
            helper.getView(R.id.noMsgCon).setVisibility(View.VISIBLE);
            return;
        }else {
            helper.getView(R.id.hasContent).setVisibility(View.VISIBLE);
            helper.getView(R.id.noMsgCon).setVisibility(View.GONE);
        }






        com.healthy.library.businessutil.GlideCopy.with(mContext)
                .load(item.fromMemberFaceUrl)
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_avatar_default)

                .into((ImageView) helper.getView(R.id.iv_customer_avatar));
        helper.setText(R.id.tv_customer_name, TextUtils.isEmpty(item.fromMemberName)?"用户已注销":item.fromMemberName);

        helper.getView(R.id.user_tip).setVisibility(View.VISIBLE);
        TextView status=helper.getView(R.id.user_tip);
//        if(item.memberState!=null){
//            if(item.memberState.contains("备孕")){//备孕
//                status.setBackgroundResource(R.drawable.shape_city_nofollow_red);
//            }else if(item.memberState.contains("宝")||item.memberState.contains("产后")){//宝宝出身
//                status.setBackgroundResource(R.drawable.shape_city_nofollow_yellow);
//            }else {//孕
//                status.setBackgroundResource(R.drawable.shape_city_nofollow_green);
//            }
//        }


        if("".equals(item.memberState)||item.memberState==null||"null".equals(item.memberState)){

            helper.getView(R.id.user_tip).setVisibility(View.GONE);
        }else {
            helper.setText(R.id.user_tip, item.memberState.replace("育儿期","")+"");
        }
        final TextView recoverpagecontent=helper.getView(R.id.recoverpagecontent);
        final ExpandTextView tv_comment_content=helper.getView(R.id.tv_comment_content);
        tv_comment_content.clearText();
        helper.setText(R.id.tv_comment_content, item.content);
        tv_comment_content.setCallback(new ExpandTextView.Callback() {
            @Override
            public void onExpand() {
                //System.out.println("需要收起");
                recoverpagecontent.setVisibility(View.VISIBLE);
                recoverpagecontent.setText("收起");

            }

            @Override
            public void onCollapse() {
//                //System.out.println("需要展开");
                recoverpagecontent.setVisibility(View.VISIBLE);
                recoverpagecontent.setText("全文");

            }

            @Override
            public void onLoss() {
//                //System.out.println("需要消失");
                recoverpagecontent.setVisibility(View.GONE);
            }
        });
        tv_comment_content.setChanged(item.isShowContent);
        recoverpagecontent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.isShowContent=!item.isShowContent;
                tv_comment_content.setChanged(item.isShowContent);
            }
        });
        helper.setText(R.id.like,item.praiseNum+"");
//        String date = TimestampUtils.timestamp2String(
//                TimestampUtils.string2Date(item.createTime, Constants.DATE_PATTERN).getTime(),
//                "MM-dd HH:mm");
        helper.setText(R.id.tv_comment_date, item.createTimeStr/*DateUtils.getClassDatePost(item.createTime)*/);
        helper.getView(R.id.tv_comment_content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onReViewClickListener.onClick(view,item.id+"",item.fromMemberId,"0","评论:"+item.fromMemberName,item.memberType+"");
      }
        });
        if(TextUtils.isEmpty(item.fromMemberName)){
            item.fromMemberName="用户已注销";

        }
        helper.getView(R.id.iv_customer_avatar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!"用户已注销".equals(item.fromMemberName)){
                    ARouter.getInstance()
                            .build(CityRoutes.CITY_FANDETAIL)
                            .withString("searchMemberType",item.memberType+"")
                            .withString("memberId",item.fromMemberId+"")
                            .navigation();
                }

            }
        });
        helper.getView(R.id.tv_customer_name).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!"用户已注销".equals(item.fromMemberName)){
                    ARouter.getInstance()
                            .build(CityRoutes.CITY_FANDETAIL)
                            .withString("searchMemberType",item.memberType+"")
                            .withString("memberId",item.fromMemberId+"")
                            .navigation();
                }

            }
        });
        final ImageTextView like=helper.getView(R.id.like);
        like.setText(item.praiseNum==0?"  ":item.praiseNum+"");
        final Drawable likeNormal = mContext.getResources().getDrawable(R.drawable.cityrightlike);
        final Drawable likeSelect = mContext.getResources().getDrawable(R.drawable.cityrightliker);
        if(item.praiseState==0){
            like.setDrawable(likeNormal);
            like.setTextColor(Color.parseColor("#444444"));
        }else {
            like.setTextColor(Color.parseColor("#F93F60"));
            like.setDrawable(likeSelect);
        }
        like.setVisibility(View.VISIBLE);
        if("用户已注销".equals(item.fromMemberName)){
            like.setVisibility(View.GONE);
        }

        final TextView recoverpage=helper.getView(R.id.recoverpage);
        final LinearLayout reviewparent=helper.itemView.findViewById(R.id.ll_review);
        if(item.isShow){
            recoverpage.setText("收起");
        }else {
            recoverpage.setText("展开剩余回复");
        }
        recoverpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.isShow=!item.isShow;
                if(item.isShow){
                    recoverpage.setText("收起");
                }else {
                    recoverpage.setText("展开剩余回复");
                }
                buildReview(mContext,reviewparent,item.discussReplyList,item);
            }
        });

        if(item.discussReplyList==null||item.discussReplyList.size()<1){
            helper.getView(R.id.con_review).setVisibility(View.GONE);
        }else {
            helper.getView(R.id.con_review).setVisibility(View.VISIBLE);
            buildReview(mContext,reviewparent,item.discussReplyList,item);
            if(item.discussReplyList.size()<=3){
                recoverpage.setVisibility(View.GONE);
            }else {
                recoverpage.setVisibility(View.VISIBLE);
            }
        }
        helper.setOnClickListener(R.id.like, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (postingStatus == 1) {
                    //说明帖子屏蔽了 直接Toast一下就 关掉吧
                    Toast.makeText(mContext, "对不起，该帖子已经被屏蔽，暂时无法操作", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(postingStatus==1||"用户已注销".equals(item.fromMemberName)){
                    return;
                }
                if(onLikeClickListener!=null){
                    onLikeClickListener.like(item.id+"",item.praiseState==0?"0":"1");

                    item.praiseState=item.praiseState==0?1:0;
                    item.praiseNum=item.praiseNum+(item.praiseState==0?-1:1);
                    like.setText(item.praiseNum==0?"  ":item.praiseNum+"");
                    if(item.praiseState==0){
                        like.setDrawable(likeNormal);
                        like.setTextColor(Color.parseColor("#444444"));
                    }else {
                        like.setTextColor(Color.parseColor("#F93F60"));
                        like.setDrawable(likeSelect);
                    }
                }
            }
        });
    }

    public void buildReview(Context context, LinearLayout reviewparent,List<Discuss.DiscussReply> discussReplyList,Discuss discuss){
        reviewparent.removeAllViews();
        int needshowsize=3>=discussReplyList.size()?discussReplyList.size():3;
        if(discussReplyList!=null&&discussReplyList.size()>0){
            reviewparent.setVisibility(View.VISIBLE);
        }else {
            reviewparent.setVisibility(View.GONE);
        }
        if(discuss.isShow){
            needshowsize=discussReplyList.size();
        }
        //System.out.println("需要的size:"+needshowsize);
        for (int i = 0; i <needshowsize ; i++) {
            final Discuss.DiscussReply discussReply=discussReplyList.get(i);
            TextView textView=new TextView(context);
            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            textView.setPadding(0,0,0,10);
            textView.setTextSize(12);
            textView.setTextColor(Color.parseColor("#ff444444"));
            textView.setMovementMethod(LinkMovementMethod.getInstance());
            textView.setHighlightColor(mContext.getResources().getColor(android.R.color.transparent));
            if(TextUtils.isEmpty(discussReply.fromMemberName)){
                discussReply.fromMemberName="用户已注销";
            }
            if(TextUtils.isEmpty(discussReply.toMemberName)){
                discussReply.toMemberName="用户已注销";
            }

            SpannableStringBuilder spannableString = new SpannableStringBuilder(discussReply.fromMemberName+" 回复 "+discussReply.toMemberName+" :"+discussReply.content+"");
            ClickableSpan clickableSpan1=new ClickableSpan() {
                @Override
                public void onClick( View view) {
                    if(!"用户已注销".equals(discussReply.fromMemberName)) {
                        ARouter.getInstance()
                                .build(CityRoutes.CITY_FANDETAIL)
                                .withString("searchMemberType", discussReply.fromMemberType + "")
                                .withString("memberId", discussReply.fromMemberId + "")
                                .navigation();
                    }

                }

                @Override
                public void updateDrawState( TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(0xff333333);
                    ds.setTypeface(Typeface.DEFAULT_BOLD);
                    ds.setUnderlineText(false);
                }
            };

            spannableString.setSpan(clickableSpan1, 0, discussReply.fromMemberName.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
            spannableString.setSpan(boldSpan, 0, discussReply.fromMemberName.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            StyleSpan boldSpan2 = new StyleSpan(Typeface.BOLD);
            ClickableSpan clickableSpan2=new ClickableSpan() {
                @Override
                public void onClick( View view) {
                    if(!"用户已注销".equals(discussReply.toMemberName)){
                        ARouter.getInstance()
                                .build(CityRoutes.CITY_FANDETAIL)
                                .withString("searchMemberType",discussReply.toMemberType+"")
                                .withString("memberId",discussReply.toMemberId+"")
                                .navigation();
                    }

                }
                @Override
                public void updateDrawState( TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(0xff333333);
                    ds.setTypeface(Typeface.DEFAULT_BOLD);
                    ds.setUnderlineText(false);
                }
            };
            spannableString.setSpan(clickableSpan2, (discussReply.fromMemberName+" 回复 ").length(), (discussReply.fromMemberName+" 回复 ").length()+discussReply.toMemberName.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(boldSpan2, (discussReply.fromMemberName+" 回复 ").length(), (discussReply.fromMemberName+" 回复 ").length()+discussReply.toMemberName.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            ClickableSpan clickableSpan3=new ClickableSpan() {
                @Override
                public void onClick( View view) {
                    onReViewClickListener.onClick(view,discussReply.postingDiscussId+"",discussReply.fromMemberId+"",discussReply.id+"","回复:"+discussReply.fromMemberName,discussReply.fromMemberType+"");
                }
                @Override
                public void updateDrawState( TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(0xff444444);
                    ds.setUnderlineText(false);
                }
            };
            spannableString.setSpan(clickableSpan3,(discussReply.fromMemberName+" 回复 ").length()+discussReply.toMemberName.length(),(discussReply.fromMemberName+" 回复 "+discussReply.toMemberName+" :"+discussReply.content+"").length(),Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//            textView.setText("p9Won2D1 回复 p9Won2D1:……在这里我们自己也是");
            textView.setText(spannableString);
            textView.requestLayout();
            reviewparent.addView(textView);
        }
    }
    public  interface OnReViewClickListener{
        void onClick(View view, String commentDiscussId, String toMemberId, String fatherId, String fromName,String toMemberType);
    }


    public interface OnLikeClickListener{
        void like(String commentDiscussId, String type);
    }

}