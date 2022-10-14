package com.health.servicecenter.adapter;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.servicecenter.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;

public class MallGoodsPayFinishTopAdapter extends BaseAdapter<String> {

    public boolean isPayOk;

    public String payNoTimeString;

    public String orderType;

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public void setPayNoTimeString(String payNoTimeString) {
        this.payNoTimeString = payNoTimeString;
    }

    public void setPayOk(boolean payOk) {
        isPayOk = payOk;
    }

    @Override
    public int getItemViewType(int position) {
        return 44;
    }

    public MallGoodsPayFinishTopAdapter() {
        this(R.layout.service_item_sucesspay);
    }

    private MallGoodsPayFinishTopAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {
        LinearLayout mPayFinishTopOkLL;
        ImageView mPagImg;
        TextView mPayContext;
        TextView mPayTime;
        LinearLayout mPayFinishTopNoLL;
        ImageView mPagNoImg;
        TextView mPayNoTime;

        TextView leftBtn;
        TextView rightBtn;

        mPayFinishTopOkLL = (LinearLayout) baseHolder.itemView.findViewById(R.id.payFinishTopOkLL);
        mPagImg = (ImageView) baseHolder.itemView.findViewById(R.id.pagImg);
        mPayContext = (TextView) baseHolder.itemView.findViewById(R.id.payContext);
        mPayTime = (TextView) baseHolder.itemView.findViewById(R.id.payTime);
        mPayFinishTopNoLL = (LinearLayout) baseHolder.itemView.findViewById(R.id.payFinishTopNoLL);
        mPagNoImg = (ImageView) baseHolder.itemView.findViewById(R.id.pagNoImg);
        mPayNoTime = (TextView) baseHolder.itemView.findViewById(R.id.payNoTime);

        leftBtn = (TextView) baseHolder.itemView.findViewById(R.id.leftBtn);
        rightBtn = (TextView) baseHolder.itemView.findViewById(R.id.rightBtn);

        if (isPayOk) {
            mPayFinishTopOkLL.setVisibility(View.VISIBLE);
            mPayFinishTopNoLL.setVisibility(View.GONE);
            if ("9".equals(orderType) || "-4".equals(orderType)) {
                mPagImg.setImageResource(R.drawable.vote_prize_success_img);
                ViewGroup.LayoutParams layoutParams = mPagImg.getLayoutParams();
                layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                mPagImg.setLayoutParams(layoutParams);
                rightBtn.setVisibility(View.GONE);
                mPayContext.setText("- 奖品领取成功 -");
                mPayContext.setTextColor(context.getResources().getColor(R.color.color_999999));
                baseHolder.itemView.findViewById(R.id.tabs).setVisibility(View.GONE);
                leftBtn.setBackgroundResource(R.drawable.shape_action_radius50);
            } else {
                leftBtn.setBackgroundResource(R.drawable.shape_mall_goods_ol_vid_basket_empty_btn);
                rightBtn.setBackgroundResource(R.drawable.shape_mall_goods_ol_vid_basket_empty_btn2);
                leftBtn.setText("返回首页");
                rightBtn.setText("查看订单");
                if ("-1".equals(orderType)) {
                    leftBtn.setText("返回直播");
                }
                leftBtn.setTextColor(Color.parseColor("#ffffffff"));
                rightBtn.setTextColor(Color.parseColor("#fff02846"));
            }
        } else {
            String leftBtnString;
            if ("9".equals(orderType) || "-4".equals(orderType)) {
                leftBtnString = "返回首页";
                rightBtn.setVisibility(View.GONE);
                baseHolder.itemView.findViewById(R.id.tabs).setVisibility(View.GONE);
            } else {
                leftBtnString = "查看订单";
            }
            leftBtn.setBackgroundResource(R.drawable.shape_mall_goods_ol_vid_basket_empty_btn2);
            rightBtn.setBackgroundResource(R.drawable.shape_mall_goods_ol_vid_basket_empty_btn);
            leftBtn.setText(leftBtnString);
            rightBtn.setText("重新支付");
            mPayFinishTopOkLL.setVisibility(View.GONE);
            mPayFinishTopNoLL.setVisibility(View.VISIBLE);
            leftBtn.setTextColor(Color.parseColor("#fff02846"));
            rightBtn.setTextColor(Color.parseColor("#ffffffff"));
            if (payNoTimeString != null) {
                mPayNoTime.setText(payNoTimeString);
            }
        }
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moutClickListener != null) {
                    if (isPayOk) {
                        moutClickListener.outClick("返回首页", null);
                    } else {
                        moutClickListener.outClick("9".equals(orderType) ? "返回首页" : "查看订单", null);
                    }
                }

            }
        });

        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moutClickListener != null) {
                    if (isPayOk) {
                        moutClickListener.outClick("查看订单", null);
                    } else {
                        moutClickListener.outClick("重新支付", null);
                    }
                }
            }
        });

    }

    private void initView() {


    }
}
