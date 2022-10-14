package com.health.mine.adapter;


import android.graphics.Paint;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.health.mine.R;
import com.health.mine.model.HanMomGoodsListModel;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.widget.CornerImageView;

public class HanMomAdapter extends BaseAdapter<HanMomGoodsListModel> {

    public HanMomAdapter() {
        this(R.layout.han_mom_goods_layout);
    }

    private HanMomAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, final int position) {
        CornerImageView mall_goods_img = baseHolder.getView(R.id.mall_goods_img);
        TextView mall_goods_context = baseHolder.getView(R.id.mall_goods_context);
        TextView mall_goods_moneyflag = baseHolder.getView(R.id.mall_goods_moneyflag);
        TextView mall_goods_moneyvalue = baseHolder.getView(R.id.mall_goods_moneyvalue);
        TextView sharePrice = baseHolder.getView(R.id.sharePrice);
        com.healthy.library.businessutil.GlideCopy.with(context)
                .load(getDatas().get(position).headImage)
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_1_1_default)

                .into(mall_goods_img);
        mall_goods_context.setText(getDatas().get(position).goodsTitle);
        mall_goods_moneyflag.setText("￥"+ getDatas().get(position).sharePrice + "");
        if (getDatas().get(position).goodsType == 1) {//标品取零售价 服务取门店价
            mall_goods_moneyvalue.setText("￥"+ getDatas().get(position).storePrice + "");
        } else {
            mall_goods_moneyvalue.setText("￥"+ getDatas().get(position).retailPrice + "");
        }
        mall_goods_moneyvalue.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
        sharePrice.setText(getDatas().get(position).shareSalesMoney + "");
        baseHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moutClickListener != null) {
                    moutClickListener.outClick("item", getDatas().get(position).id);
                }
            }
        });
    }

}