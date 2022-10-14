package com.health.faq.adapter;

import android.graphics.Bitmap;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.faq.R;
import com.healthy.library.model.FaqRewardQuestion;
import com.healthy.library.routes.FaqRoutes;
import com.healthy.library.utils.StringUtils;
import com.healthy.library.widget.CornerImageView;

public class HotRewardListAdapter extends BaseQuickAdapter<FaqRewardQuestion, BaseViewHolder> {
    public HotRewardListAdapter() {
        this(R.layout.item_faq_reward);
    }

    private HotRewardListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final FaqRewardQuestion item) {
//        TextView ivName=helper.getView(R.id.ivName);
//        TextView ivStatus=helper.getView(R.id.ivStatus);
//        TextView money=helper.getView(R.id.money);
//        TextView iv_content=helper.getView(R.id.iv_content);
        helper.setText(R.id.ivName, item.nickName);
        helper.setText(R.id.ivStatus, item.currentStatus);
        helper.setText(R.id.money, "¥" + StringUtils.getResultPrice(item.rewardMoney + ""));
        helper.setText(R.id.iv_content, item.introduction);
        if(TextUtils.isEmpty(item.introduction)){
            helper.setText(R.id.iv_content, item.detail);
        }
        int gg=(helper.getPosition()+1)%3;
        if(gg==0){
            helper.setBackgroundRes(R.id.needShowBg,R.drawable.shape_faq_blue);
        }else if(gg==1){
            helper.setBackgroundRes(R.id.needShowBg,R.drawable.shape_faq_red);
        }else {
            helper.setBackgroundRes(R.id.needShowBg,R.drawable.shape_faq_yellow);
        }


        final CornerImageView ivHeader = helper.getView(R.id.ivHeader);
        if (item.faceUrl != null) {
            com.healthy.library.businessutil.GlideCopy.with(mContext)
                    .asBitmap()
                    .load(item.faceUrl)
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_avatar_default)
                    
                    .into(new BitmapImageViewTarget(ivHeader) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            ivHeader.setImageDrawable(circularBitmapDrawable);
                        }
                    });
        }
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //System.out.println("跳问答详情:3");
                ARouter.getInstance()
                        .build(FaqRoutes.FAQ_QUESTION_DETAIL)
                        .withString("questionId", item.id + "")
                        .withInt("pos", helper.getAdapterPosition())
                        .withBoolean("host", true)
                        .navigation();
            }
        });
    }
}
