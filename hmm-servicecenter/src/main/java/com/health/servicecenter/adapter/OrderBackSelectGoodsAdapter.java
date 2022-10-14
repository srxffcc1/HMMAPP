package com.health.servicecenter.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.health.servicecenter.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.model.OrderList;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.widget.CornerImageView;

public class OrderBackSelectGoodsAdapter extends BaseAdapter<OrderList.OrderDetailListBean> {
    public OrderBackSelectGoodsAdapter() {
        this(R.layout.order_back_select_goods_adapter_layout);
    }

    public OrderBackSelectGoodsAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull final BaseHolder holder, int position) {
        final ImageView selectImg;
        ConstraintLayout top;
        CornerImageView goodsImg;
        TextView pointLable;
        ImageView actFlag;
        TextView goodsTitle;
        TextView goodsMoney;
        TextView goodsSpec;
        TextView goodsCount;


        selectImg = (ImageView) holder.getView(R.id.selectImg);
        top = (ConstraintLayout) holder.getView(R.id.top);
        goodsImg = (CornerImageView) holder.getView(R.id.goodsImg);
        pointLable = (TextView) holder.getView(R.id.pointLable);
        actFlag = (ImageView) holder.getView(R.id.actFlag);
        goodsTitle = (TextView) holder.getView(R.id.goodsTitle);
        goodsMoney = (TextView) holder.getView(R.id.goodsMoney);
        goodsSpec = (TextView) holder.getView(R.id.goodsSpec);
        goodsCount = (TextView) holder.getView(R.id.goodsCount);

        final OrderList.OrderDetailListBean detailsBean = getDatas().get(position);
        if (detailsBean.goodsPoints != null && !TextUtils.isEmpty(detailsBean.goodsPoints)
                && !"0".equals(FormatUtils.moneyKeep2Decimals(detailsBean.goodsPoints))) {//表示是积分退订
            pointLable.setVisibility(View.VISIBLE);
            pointLable.getBackground().setAlpha(150);//积分兑换的标签透明度
            if ("0".equals(FormatUtils.moneyKeep2Decimals(detailsBean.goodsAmount))) {
                goodsMoney.setText(FormatUtils.moneyKeep2Decimals(detailsBean.goodsPoints) + "积分");
            } else {
                goodsMoney.setText(FormatUtils.moneyKeep2Decimals(detailsBean.goodsPoints) + "积分\n+¥" + FormatUtils.moneyKeep2Decimals(detailsBean.goodsAmount) + "");
            }
        } else {
            actFlag.setVisibility(View.GONE);
            goodsMoney.setText("¥" + FormatUtils.moneyKeep2Decimals(detailsBean.goodsAmount));
        }
        goodsCount.setText("x" + detailsBean.goodsQuantity);
        goodsTitle.setText(detailsBean.goodsTitle);
        goodsSpec.setText("");
        if (!TextUtils.isEmpty(detailsBean.goodsSpecDesc)) {
            goodsSpec.setText(detailsBean.goodsSpecDesc);
        }
        com.healthy.library.businessutil.GlideCopy.with(context).load(detailsBean.goodsImage)
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_1_1_default)

                .into(goodsImg);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (detailsBean.isSelect) {
                    detailsBean.isSelect = false;
                    selectImg.setImageResource(R.drawable.ic_anonymous_unselected);

                } else {
                    detailsBean.isSelect = true;
                    selectImg.setImageResource(R.drawable.ic_anonymous_checked);
                }
                if (moutClickListener != null) {
                    moutClickListener.outClick("selcet", detailsBean);
                }
            }
        });
    }
}
