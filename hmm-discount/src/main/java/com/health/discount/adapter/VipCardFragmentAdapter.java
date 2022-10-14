package com.health.discount.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.discount.R;
import com.health.discount.model.CouponVip;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.widget.ExpandTextView;
import com.healthy.library.widget.ImageTextView;

/**
 * @author Li
 * @date 2019/03/25 11:03
 * @des 推荐阅读
 */

public class VipCardFragmentAdapter extends BaseQuickAdapter<CouponVip, BaseViewHolder> {




    public VipCardFragmentAdapter() {
        this(R.layout.dis_item_vip_card);
    }

    private VipCardFragmentAdapter(int layoutResId) {
        super(layoutResId);

    }

    @Override
    protected void convert(BaseViewHolder helper, final CouponVip item) {
         TextView cardMoney;
        ConstraintLayout cardMsTopLL;
        ImageView cardLeftImg;
        ConstraintLayout cardLeftLL;
        LinearLayout cardTopMoneyLL;
        TextView cardName;
        TextView cardTT;
        TextView cardTime;
        TextView cardTip;
        TextView vipDiv;
        ImageTextView canUseCardStoreButton;
        final LinearLayout canUseCardStoreScP;
        final ExpandTextView tvCommentContent;
        final ImageTextView recoverpagecontent;
        ScrollView canUseCardStoreSc;
        LinearLayout canUseCardStoreScLL;
        TextView cardNumer;
        cardNumer=(TextView) helper.itemView.findViewById(R.id.cardNumer);
        cardMoney = (TextView) helper.itemView.findViewById(R.id.cardMoney);
        cardMsTopLL = (ConstraintLayout) helper.itemView.findViewById(R.id.cardMsTopLL);
        cardLeftImg = (ImageView) helper.itemView.findViewById(R.id.cardLeftImg);
        cardLeftLL = (ConstraintLayout) helper.itemView.findViewById(R.id.cardLeftLL);
        cardTopMoneyLL = (LinearLayout) helper.itemView.findViewById(R.id.cardTopMoneyLL);
        cardName = (TextView) helper.itemView.findViewById(R.id.cardName);
        cardTT = (TextView) helper.itemView.findViewById(R.id.cardTT);
        cardTime = (TextView) helper.itemView.findViewById(R.id.cardTime);
        cardTip = (TextView) helper.itemView.findViewById(R.id.cardTip);
        vipDiv = (TextView) helper.itemView.findViewById(R.id.vipDiv);
        canUseCardStoreButton = (ImageTextView) helper.itemView.findViewById(R.id.canUseCardStoreButton);
        canUseCardStoreScP = (LinearLayout) helper.itemView.findViewById(R.id.canUseCardStoreScP);
        tvCommentContent = (ExpandTextView) helper.itemView.findViewById(R.id.tv_comment_content);
        recoverpagecontent = (ImageTextView) helper.itemView.findViewById(R.id.recoverpagecontent);
        canUseCardStoreSc = (ScrollView) helper.itemView.findViewById(R.id.canUseCardStoreSc);
        canUseCardStoreScLL = (LinearLayout) helper.itemView.findViewById(R.id.canUseCardStoreScLL);
        cardMoney.setText(FormatUtils.moneyKeep2Decimals(item.Price.replace("-","")));
        cardName.setText(item.GoodsName);
        cardTime.setText(item.StartDate+"至"+item.EndDate);
        cardTip.setText(item.PopName);
        cardTT.setVisibility(View.VISIBLE);
        cardNumer.setText(item.YeNumber+"张");
        if("2".equals(item.State)){
            cardTT.setText("已过期");
            cardTT.setBackgroundResource(R.drawable.shape_desposit_ll_bgtg);
            cardLeftImg.setImageResource(R.drawable.vip_card_left_g);
        }else if("1".equals(item.State)){
            cardTT.setVisibility(View.GONE);
            cardTT.setText("");
            cardLeftImg.setImageResource(R.drawable.vip_card_left);
        }else {
            cardTT.setText("待使用");
            cardTT.setBackgroundResource(R.drawable.shape_desposit_ll_bgty);
            cardLeftImg.setImageResource(R.drawable.vip_card_left);
        }

        canUseCardStoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.isShowCan = !item.isShowCan;
                if (item.isShowCan) {
                    canUseCardStoreScP.setVisibility(View.VISIBLE);
                } else {
                    canUseCardStoreScP.setVisibility(View.GONE);
                }
            }
        });
        tvCommentContent.clearText();
        tvCommentContent.setText(item.LimiteDepartID);
        tvCommentContent.setCallback(new ExpandTextView.Callback() {
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
                recoverpagecontent.setText("查看更多");

            }

            @Override
            public void onLoss() {
//                //System.out.println("需要消失");
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
    }

    private void initView() {

    }
//    public class ScrollViewHolder extends com.chad.library.adapter.base.BaseViewHolder{
//        ScrollView mall_nsv;
//
//        public ScrollViewHolder(View view) {
//            super(view);
//            mall_nsv=view.findViewById(R.id.canUseCardStoreSc);
//        }
//
//        public boolean isTouchNsv(float x, float y) {
//            int[] pos = new int[2];
//            //获取sv在屏幕上的位置
//            mall_nsv.getLocationOnScreen(pos);
//            int width = mall_nsv.getMeasuredWidth();
//            int height = mall_nsv.getMeasuredHeight();
//            return x >= pos[0] && x <= pos[0] + width && y >= pos[1] && y <= pos[1] + height;
//        }
//    }
}
