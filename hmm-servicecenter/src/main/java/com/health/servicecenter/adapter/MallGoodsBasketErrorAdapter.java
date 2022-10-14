package com.health.servicecenter.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.health.servicecenter.R;
import com.healthy.library.model.GoodsBasketCell;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.widget.CornerImageView;

public class MallGoodsBasketErrorAdapter extends BaseAdapter<GoodsBasketCell> {




    @Override
    public int getItemViewType(int position) {
        return 42;
    }

    public MallGoodsBasketErrorAdapter() {
        this(R.layout.service_item_goodsbasket_error);
    }

    private MallGoodsBasketErrorAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {
        final GoodsBasketCell goodsBasketCell= getDatas().get(i);
         TextView basketCheck;
         CornerImageView basketImg;
         TextView basketTitle;
         TextView basketSku;
         TextView basketMoney;

        basketCheck = (TextView) baseHolder.itemView.findViewById(R.id.basketCheck);
        basketImg = (CornerImageView)baseHolder.itemView. findViewById(R.id.basketImg);
        basketTitle = (TextView) baseHolder.itemView.findViewById(R.id.basketTitle);
        basketSku = (TextView) baseHolder.itemView.findViewById(R.id.basketSku);
        basketMoney = (TextView) baseHolder.itemView.findViewById(R.id.basketMoney);

        com.healthy.library.businessutil.GlideCopy.with(context)
                .load(goodsBasketCell.goodsImage)
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_1_1_default)

                .into(basketImg);
        basketTitle.setText(goodsBasketCell.goodsTitle);
        basketSku.setText(goodsBasketCell.goodsSpecDesc);
        basketMoney.setText("￥"+ FormatUtils.moneyKeep2Decimals(goodsBasketCell.curGoodsAmount)+"");
        baseHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_DETAIL)
                        .withString("id", goodsBasketCell.goodsId + "")
                        .navigation();
            }
        });
    }

    private void initView() {


    }
}
