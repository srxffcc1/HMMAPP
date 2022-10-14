package com.health.client.adapter;

import androidx.constraintlayout.widget.ConstraintLayout;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.client.R;
import com.health.client.model.MonMessageOtherHelper;
import com.healthy.library.routes.CityRoutes;
import com.healthy.library.utils.DateUtils;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.ExpandTextView;

/**
 * @author Li
 * @date 2019/03/25 11:03
 * @des 推荐阅读
 */

public class MessageLikeAdapter extends BaseQuickAdapter<MonMessageOtherHelper, BaseViewHolder> {


    public MessageLikeAdapter() {
        this(R.layout.home_item_activity_messagedis);
    }

    private MessageLikeAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, final MonMessageOtherHelper item) {
        ConstraintLayout readlView;
        CornerImageView ivCustomerAvatar;
        TextView tvCustomerName;
        TextView tvCommentDate;
        TextView userTip;
        TextView toReview;
        TextView like;
        ExpandTextView tvCommentContent;
        ConstraintLayout conReview;
        LinearLayout llReview;
        GridLayout layoutImgs;
        ConstraintLayout noMsgCon;
        ImageView noMsg;
        final ImageView postImg;
        TextView postContent;

        postImg = (ImageView) helper.getView(R.id.postImg);
        postContent = (TextView) helper.getView(R.id.postContent);
        readlView = (ConstraintLayout) helper.getView(R.id.readlView);
        ivCustomerAvatar = (CornerImageView) helper.getView(R.id.iv_customer_avatar);
        tvCustomerName = (TextView) helper.getView(R.id.tv_customer_name);
        tvCommentDate = (TextView) helper.getView(R.id.tv_comment_date);
        userTip = (TextView) helper.getView(R.id.user_tip);
        toReview = (TextView) helper.getView(R.id.toReview);
        like = (TextView) helper.getView(R.id.like);
        tvCommentContent = (ExpandTextView) helper.getView(R.id.tv_comment_content);
        conReview = (ConstraintLayout) helper.getView(R.id.con_review);
        llReview = (LinearLayout) helper.getView(R.id.ll_review);
        layoutImgs = (GridLayout) helper.getView(R.id.layout_imgs);
        noMsgCon = (ConstraintLayout) helper.getView(R.id.noMsgCon);
        noMsg = (ImageView) helper.getView(R.id.noMsg);
        ConstraintLayout mLLTopUserName = helper.getView(R.id.ll_top_userName);
        like.setVisibility(View.VISIBLE);
        like.setText(item.hmmCityRecordByMemberIdDTOS.upvoteType == 1 ? "赞了这条帖子" : "赞了这条评论");
        com.healthy.library.businessutil.GlideCopy.with(ivCustomerAvatar.getContext())
                .asBitmap()
                .load(item.hmmCityRecordByMemberIdDTOS.icon)
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_avatar_default)

                .into(ivCustomerAvatar);
        tvCommentContent.setVisibility(View.GONE);
        tvCustomerName.setText(item.hmmCityRecordByMemberIdDTOS.nickName);
        tvCommentDate.setText(DateUtils.getClassDate(item.hmmCityRecordByMemberIdDTOS.createTime));
        tvCommentContent.setText(item.hmmCityRecordByMemberIdDTOS.replyContent);
        postContent.setText(item.hmmCityRecordByMemberIdDTOS.resourceContent);
        tvCustomerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        ivCustomerAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.hmmCityRecordByMemberIdDTOS.postingStatus != 0) {//说明帖子有问题
                    Toast.makeText(mContext, "帖子已删除不可查看", Toast.LENGTH_SHORT).show();
                    return;
                }
                ARouter.getInstance()
                        .build(CityRoutes.CITY_POSTDETAIL)
                        .withString("id", item.hmmCityRecordByMemberIdDTOS.allTypeId + "")
                        .withBoolean("isshowDiscuss", false)
                        .navigation();
            }
        });
        if (item.postingInfoByUrlStatusAndPostingIdDTO != null) {
            if (!TextUtils.isEmpty(item.postingInfoByUrlStatusAndPostingIdDTO.url) && item.hmmCityRecordByMemberIdDTOS.upvoteType == 1) {
                postImg.setVisibility(View.VISIBLE);
                com.healthy.library.businessutil.GlideCopy.with(postImg.getContext())
                        .asBitmap()
                        .load(item.postingInfoByUrlStatusAndPostingIdDTO.url)
                        .placeholder(R.drawable.img_1_1_default2)
                        .error(R.drawable.img_avatar_default)

                        .into(postImg);
            } else {
                postImg.setVisibility(View.GONE);
            }
        } else {
            postImg.setVisibility(View.GONE);
        }
        userTip.setVisibility(View.INVISIBLE);
        if (item.memberStateDTO != null) {
            if (item.memberStateDTO.memberState != null) {
                userTip.setVisibility(View.VISIBLE);
                userTip.setText(item.memberStateDTO.memberState);
                item.memberStateDTO.memberState = item.memberStateDTO.memberState.replace("育儿期", "宝宝 ");
                if (item.memberStateDTO.memberState.contains("备孕")) {//备孕
                    userTip.setBackgroundResource(R.drawable.shape_city_nofollow_red);
                } else if (item.memberStateDTO.memberState.contains("宝") || item.memberStateDTO.memberState.contains("产后")) {//宝宝出身
                    userTip.setBackgroundResource(R.drawable.shape_city_nofollow_yellow);
                } else {//孕
                    userTip.setBackgroundResource(R.drawable.shape_city_nofollow_green);
                }
            }
        }

        /*mLLTopUserName.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mLLTopUserName.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                ViewGroup.LayoutParams layoutParams = tvCustomerName.getLayoutParams();
                layoutParams.width = mLLTopUserName.getWidth() - userTip.getWidth() - 20;
                tvCustomerName.setLayoutParams(layoutParams);
            }
        });*/

//        if(!TextUtils.isEmpty(item.postingInfoByUrlStatusAndPostingIdDTO.url)){
//            postImg.setVisibility(View.VISIBLE);
//            if(MediaFileUtil.isVideoFileType(item.postingInfoByUrlStatusAndPostingIdDTO.url)){
//                postImg.setImageResource(R.drawable.img_1_1_default);
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Bitmap bitmap = null;
//                        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//                        try {
//                            //根据url获取缩略图
//                            retriever.setDataSource(item.postingInfoByUrlStatusAndPostingIdDTO.url, new HashMap());
//                            //获得第一帧图片
//                            bitmap = retriever.getFrameAtTime();
//                            final Bitmap finalBitmap = BitmapUtils.zoomImg(bitmap,postImg.getWidth()+10,postImg.getHeight());
//                            ((Activity)mContext).runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    postImg.setImageBitmap(finalBitmap);
//                                }
//                            });
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).start();
//            }else {
//                com.healthy.library.businessutil.GlideCopy.with(mContext)
//                        .asBitmap()
//                        .load(item.postingInfoByUrlStatusAndPostingIdDTO.url)
//                        .placeholder(R.drawable.img_avatar_default)
//                        .error(R.drawable.img_avatar_default)
//
//                        .into(ivCustomerAvatar);
//            }
//
//        }else {
//            postImg.setVisibility(View.GONE);
//        }

    }

}
