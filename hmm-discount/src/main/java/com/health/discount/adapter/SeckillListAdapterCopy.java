package com.health.discount.adapter;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.discount.R;
import com.healthy.library.model.NewUserListModel;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.CornerImageView;

public class SeckillListAdapterCopy extends BaseQuickAdapter<NewUserListModel, BaseViewHolder>
{

    public SeckillListAdapterCopy() {
        super(R.layout.item_seckill_adapter_layout);
    }
    public String getNumberWanTwo(int value) {
        if (value < 10000) {
            return value + "";
        } else {//销量超过万处理下
            double n = (double) value / 10000;
            Double result = Double.valueOf(String.format("%.2f", n));
            return FormatUtils.moneyKeep2Decimals(result) + "万";
        }
    }

        @Override
        protected void convert(BaseViewHolder holder, NewUserListModel item) {
            {
                TextView price = holder.itemView.findViewById(R.id.price);
                TextView linePrice = holder.itemView.findViewById(R.id.linePrice);
                TextView goodsTitle = holder.itemView.findViewById(R.id.goodsTitle);
                ImageView bgImg = holder.itemView.findViewById(R.id.bgImg);
                LinearLayout lableLiner = holder.itemView.findViewById(R.id.lableLiner);
                TextView numTxt = holder.itemView.findViewById(R.id.numTxt);
                CardView goodsCard = holder.itemView.findViewById(R.id.goodsCard);
                final TextView progressT = holder.itemView.findViewById(R.id.progressT);
                final TextView progressTZ = holder.itemView.findViewById(R.id.progressTZ);
                CornerImageView goodsImg = holder.itemView.findViewById(R.id.goodsImg);
                final NewUserListModel model = getData().get(holder.getPosition());
                if (holder.getPosition() == 0) {
                    bgImg.setVisibility(View.VISIBLE);
                    lableLiner.setVisibility(View.VISIBLE);
                } else {
                    bgImg.setVisibility(View.GONE);
                    lableLiner.setVisibility(View.GONE);
                }
                com.healthy.library.businessutil.GlideCopy.with(mContext)
                        .load(model.filePath)
                        .error(R.drawable.img_1_1_default)
                        .placeholder(R.drawable.img_1_1_default)
                        .into(goodsImg);
                goodsTitle.setText(model.goodsTitle);
                if (model.goodsType == 1) {
                    linePrice.setText("¥" + FormatUtils.moneyKeep2Decimals(model.platformPrice));
                } else {
                    linePrice.setText("¥" + FormatUtils.moneyKeep2Decimals(model.platformPrice));
                }
                linePrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
                SpannableString goodsprice = new SpannableString("￥" + FormatUtils.moneyKeep2Decimals(model.marketingPrice));
                goodsprice.setSpan(new AbsoluteSizeSpan(14, true), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                goodsprice.setSpan(new StyleSpan(Typeface.NORMAL), 1, goodsprice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //正常
                price.setText(goodsprice);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (model.goodsType == 1) {
                            ARouter.getInstance()
                                    .build(ServiceRoutes.SERVICE_DETAIL)
                                    .withString("id", model.goodsId)
                                    .withString("marketingType", "3")
                                    .navigation();
                        } else {
                            ARouter.getInstance()
                                    .build(ServiceRoutes.SERVICE_DETAIL)
                                    .withString("id", model.goodsId)
                                    .withString("marketingType", "3")
                                    .navigation();
                        }
                    }
                });
                if ((model.sales + model.inventory) > 0) {
                    double biz = model.sales * 1.0 / model.inventory;
                    int bizint = model.sales * 100 / model.inventory;
                    if (biz > 1) {
                        biz = 1.0;
                        bizint = 100;
                    }
                    if (bizint != 0) {
                        progressT.setVisibility(View.VISIBLE);
                        progressTZ.setText(bizint + "%");
                    } else {
                        progressT.setVisibility(View.GONE);
                    }
                    if (model.width != -1) {
                        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) progressT.getLayoutParams();
                        layoutParams.width = model.width;
                        progressT.setLayoutParams(layoutParams);
                    } else {
//                    //System.out.println("获得得内容长度" + "错误"+":"+model.goodsTitle);
                        final double finalBiz = biz;
                        progressTZ.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                if (progressTZ.getWidth() == 0) {
                                    progressTZ.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                        @Override
                                        public void onGlobalLayout() {
//                                            //System.out.println("获得得内容长度" + "错误2"+":"+model.goodsTitle);
                                            int width = progressTZ.getWidth();
                                            if (width > 0) {
//                                                //System.out.println("获得得内容长度" + width+":"+model.goodsTitle);
                                                if (finalBiz * TransformUtil.dp2px(mContext, 140) > width) {
//                                                    //System.out.println("获得得内容长度大于" + width+":"+model.goodsTitle);
                                                    ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) progressT.getLayoutParams();
                                                    layoutParams.width = (int) (finalBiz * TransformUtil.dp2px(mContext, 140));
                                                    progressT.setLayoutParams(layoutParams);
                                                    model.width = (int) (finalBiz * TransformUtil.dp2px(mContext, 140));
//                                                    //System.out.println("获得得内容长度实际" + model.width+":"+model.goodsTitle);
                                                } else {
//                                                    //System.out.println("获得得内容长度大于小" + width+":"+model.goodsTitle);
                                                    ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) progressT.getLayoutParams();
                                                    layoutParams.width = width;
                                                    progressT.setLayoutParams(layoutParams);
                                                    model.width = layoutParams.width;
                                                }
                                                progressTZ.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                            }
                                        }
                                    });
                                } else {
//                                    //System.out.println("获得得内容长度大于无问题" + progressTZ.getWidth()+":"+model.goodsTitle);
                                    int width = progressTZ.getWidth();
                                    if (finalBiz * TransformUtil.dp2px(mContext, 140) > width) {
//                                        //System.out.println("获得得内容长度大于" + width+":"+model.goodsTitle);
                                        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) progressT.getLayoutParams();
                                        layoutParams.width = (int) (finalBiz * TransformUtil.dp2px(mContext, 140));
                                        progressT.setLayoutParams(layoutParams);
                                        model.width = (int) (finalBiz * TransformUtil.dp2px(mContext, 140));
//                                        //System.out.println("获得得内容长度实际" + model.width+":"+model.goodsTitle);
                                    } else {
//                                        //System.out.println("获得得内容长度大于小" + width+":"+model.goodsTitle);
                                        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) progressT.getLayoutParams();
                                        layoutParams.width = width;
                                        progressT.setLayoutParams(layoutParams);
                                        model.width = layoutParams.width;
                                    }
                                }
                            }
                        }, 100);


                    }
                    progressT.setText(bizint + "%");
                    numTxt.setText("已抢" + getNumberWanTwo(model.sales) + "件");
                } else {
                    ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) progressT.getLayoutParams();
                    layoutParams.width = (int) TransformUtil.dp2px(mContext, 140);
                    progressT.setLayoutParams(layoutParams);
                    progressT.setText(100 + "%");
                    numTxt.setText("已售完");
                }
            }

        }
}
