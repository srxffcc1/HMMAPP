package com.health.faq.adapter;

import android.graphics.Bitmap;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.faq.R;
import com.healthy.library.model.FaqExportQuestion;
import com.healthy.library.routes.FaqRoutes;
import com.healthy.library.widget.CornerImageView;

public class HotQuestionListAdapter extends BaseQuickAdapter<FaqExportQuestion, BaseViewHolder> {

    public String cityNo;

    public void setCityNo(String cityNo) {
        this.cityNo = cityNo;
    }
    public HotQuestionListAdapter() {
        this(R.layout.item_faq_list_right);
    }

    private HotQuestionListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final FaqExportQuestion item) {

         ImageView ivQ;

        helper.setVisible(R.id.ivTip,cityNo.equals(item.addressCity)?true:false);
        ivQ = (ImageView) helper.getView(R.id.ivQ);
        final CornerImageView ivHeader = (CornerImageView) helper.getView(R.id.ivHeader);
        if (item.faceUrl != null) {
            com.healthy.library.businessutil.GlideCopy.with(mContext)
                    .asBitmap()
                    .load(item.faceUrl )
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
                //System.out.println("跳问答详情3");
                ARouter.getInstance().build(FaqRoutes.FAQ_EXPERT_QUESTION_DETAIL)
                        .withString("questionId", item.questionId+"")
                        .withInt("pos",helper.getPosition())
                        .withBoolean("index",true)
                        .navigation();
            }
        });
        ivQ.setImageResource(R.drawable.ic_question_q);
        helper.setText(R.id.tvQuestions,item.introduction);
        helper.setText(R.id.ivName,item.realName);
        helper.setText(R.id.ivYear,item.rankName);
        helper.setText(R.id.ivSee,item.readCount+"人查看");

    }

    private void initView() {

    }
}
