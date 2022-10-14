package com.health.discount.adapter;

import android.graphics.Paint;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.discount.R;
import com.healthy.library.model.ActKill;
import com.healthy.library.routes.DiscountRoutes;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.TransformUtil;

/**
 * @author Li
 * @date 2019/03/25 11:03
 * @des 推荐阅读
 */

public class ActKillAdapter extends BaseQuickAdapter<ActKill, BaseViewHolder> {


    public ActKillAdapter() {
        super(R.layout.mall_index_seckill_full_layout);
    }


    @Override
    protected void convert(final BaseViewHolder helper, final ActKill item) {
        ConstraintLayout parentCategory;
        ImageView ivGoods;
        ConstraintLayout progressLinre;
        final TextView progressT;
        TextView goodsPrice;
        TextView retailPrice;
        final TextView progressTZ;
        parentCategory = (ConstraintLayout) helper.itemView.findViewById(R.id.parent_category);
        ivGoods = (ImageView) helper.itemView.findViewById(R.id.iv_goods);
        progressLinre = (ConstraintLayout) helper.itemView.findViewById(R.id.progressLinre);
        progressT = (TextView) helper.itemView.findViewById(R.id.progressT);
        goodsPrice = (TextView) helper.itemView.findViewById(R.id.goodsPrice);
        retailPrice = (TextView) helper.itemView.findViewById(R.id.retailPrice);
        progressTZ = (TextView) helper.itemView.findViewById(R.id.progressTZ);
        com.healthy.library.businessutil.GlideCopy.with(mContext)
                .load(item.filePath)
                .error(R.drawable.img_1_1_default)
                .placeholder(R.drawable.img_1_1_default2)
                .into(ivGoods);
        goodsPrice.setText("￥" + FormatUtils.moneyKeep2Decimals(item.marketingPrice));
        retailPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        retailPrice.setText("￥" + FormatUtils.moneyKeep2Decimals(item.platformPrice));

        progressT.setVisibility(View.GONE);

        if ((item.inventory) > 0) {
             double biz = item.sales * 1.0 / item.inventory;
            int bizint = item.sales * 100 / item.inventory;
            if(biz>1){
                biz=1.0;
                bizint=100;
            }


            if (bizint != 0) {
                progressT.setVisibility(View.VISIBLE);
                progressTZ.setText(bizint + "%");
            } else {
                progressT.setVisibility(View.GONE);
            }
            if (item.width != -1) {
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) progressT.getLayoutParams();
                layoutParams.width = item.width;
                progressT.setLayoutParams(layoutParams);
            } else {
                final double finalBiz = biz;
                progressTZ.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        int width = progressTZ.getWidth();
                        if (width > 0) {
                            //System.out.println("获得得内容长度" + width);
                            if (finalBiz * TransformUtil.dp2px(mContext, 95) > width) {
                                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) progressT.getLayoutParams();
                                layoutParams.width = (int) (finalBiz * TransformUtil.dp2px(mContext, 95));
                                progressT.setLayoutParams(layoutParams);
                                item.width = layoutParams.width;
                            } else {
                                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) progressT.getLayoutParams();
                                layoutParams.width = width;
                                progressT.setLayoutParams(layoutParams);
                                item.width = layoutParams.width;
                            }
                            progressTZ.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    }
                });
            }
            progressT.setText(bizint + "%");

        } else {
            progressT.setVisibility(View.VISIBLE);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) progressT.getLayoutParams();
            layoutParams.width = (int) TransformUtil.dp2px(mContext, 95);
            progressT.setLayoutParams(layoutParams);
            progressT.setText(100 + "%");
        }
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(DiscountRoutes.DIS_SECKILLLISTACTIVITY)
                        .navigation();
            }
        });


    }

    private void initView() {


    }
}
