package com.health.discount.adapter;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.discount.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.builder.ObjectIteraor;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.model.NewUserListModel;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.AutoClickImageView;
import com.healthy.library.widget.CornerImageView;
import com.umeng.analytics.MobclickAgent;

public class SeckillListAdapter extends BaseAdapter<NewUserListModel> {
    private boolean isStart = true;

    public SeckillListAdapter() {
        super(R.layout.item_seckill_adapter_layout);
    }

    public void setResult(boolean result) {
        this.isStart = result;
    }

    @Override
    public void onBindViewHolder(@NonNull final BaseHolder holder, final int position) {
        final NewUserListModel model = getDatas().get(holder.getPosition());
        TextView price = holder.itemView.findViewById(R.id.price);
        TextView robBtn = holder.itemView.findViewById(R.id.robBtn);
        TextView pricePre = holder.itemView.findViewById(R.id.pricePre);
        View yellowView = holder.itemView.findViewById(R.id.yellowView);
        View redView = holder.itemView.findViewById(R.id.redView);
        LinearLayout linePriceView = holder.itemView.findViewById(R.id.linePriceView);
        ConstraintLayout btnConstraintLayout = holder.itemView.findViewById(R.id.btnConstraintLayout);
        ConstraintLayout progressLL = holder.itemView.findViewById(R.id.progressLL);
        TextView linePrice = holder.itemView.findViewById(R.id.linePrice);
        TextView goodsTitle = holder.itemView.findViewById(R.id.goodsTitle);
        TextView numTxt = holder.itemView.findViewById(R.id.numTxt);
        CardView goodsCard = holder.itemView.findViewById(R.id.goodsCard);
        final TextView progressT = holder.itemView.findViewById(R.id.progressT);
        final TextView progressTZ = holder.itemView.findViewById(R.id.progressTZ);
        CornerImageView goodsImg = holder.itemView.findViewById(R.id.goodsImg);
        AutoClickImageView autoClickImageView=holder.itemView.findViewById(R.id.goodsShareCoupon);
        if(model.shareGiftDTOS!=null&&model.shareGiftDTOS.size()>0){
            autoClickImageView.setVisibility(View.VISIBLE);
            autoClickImageView.setCanTouch(false);
        }else {
            autoClickImageView.setVisibility(View.INVISIBLE);
        }
        com.healthy.library.businessutil.GlideCopy.with(context)
                .load(model.filePath)
                .error(R.drawable.img_1_1_default)
                .placeholder(R.drawable.img_1_1_default)
                .into(goodsImg);
        goodsTitle.setText(model.goodsTitle);
        linePrice.setText("¥" + FormatUtils.moneyKeep2Decimals(model.retailPrice));
        linePrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
        SpannableString goodsPrice = new SpannableString("￥" + FormatUtils.moneyKeep2Decimals(model.marketingPrice));
        goodsPrice.setSpan(new AbsoluteSizeSpan(14, true), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        goodsPrice.setSpan(new StyleSpan(Typeface.NORMAL), 1, goodsPrice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //正常
        price.setText(goodsPrice);
        if ((model.inventory + model.sales) > 0) {
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
                final double finalBiz = biz;
                progressTZ.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (progressTZ.getWidth() == 0) {
                            progressTZ.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                @Override
                                public void onGlobalLayout() {
                                    int width = progressTZ.getWidth();
                                    if (width > 0) {
                                        if (finalBiz * TransformUtil.dp2px(context, 140) > width) {
                                            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) progressT.getLayoutParams();
                                            layoutParams.width = (int) (finalBiz * TransformUtil.dp2px(context, 140));
                                            progressT.setLayoutParams(layoutParams);
                                            model.width = (int) (finalBiz * TransformUtil.dp2px(context, 140));
                                        } else {
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
                            int width = progressTZ.getWidth();
                            if (finalBiz * TransformUtil.dp2px(context, 140) > width) {
                                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) progressT.getLayoutParams();
                                layoutParams.width = (int) (finalBiz * TransformUtil.dp2px(context, 140));
                                progressT.setLayoutParams(layoutParams);
                                model.width = (int) (finalBiz * TransformUtil.dp2px(context, 140));
                            } else {
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
            layoutParams.width = (int) TransformUtil.dp2px(context, 140);
            progressT.setLayoutParams(layoutParams);
            progressT.setText(100 + "%");
            numTxt.setText("已售完");
        }
        if (isStart) {
            progressLL.setVisibility(View.VISIBLE);
            if ((model.inventory - model.sales) > 0) {
                price.setTextColor(Color.parseColor("#F02846"));
                pricePre.setTextColor(Color.parseColor("#F02846"));
                robBtn.setTextColor(Color.parseColor("#FFEFC2"));
                robBtn.setText("抢购");
                robBtn.setCompoundDrawablePadding(3);
                Drawable drawable = context.getDrawable(R.drawable.seckill_list_btn_icon);
                robBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
                yellowView.setBackground(context.getDrawable(R.drawable.seckill_list_btn_yellow_bg));
                linePriceView.setBackground(context.getDrawable(R.drawable.seckill_list_btn_price_bg));
                redView.setBackground(context.getDrawable(R.drawable.seckill_list_btn_red_bg));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MobclickAgent.onEvent(context, "event2APPKillListGoodsImgClick", new SimpleHashMapBuilder<String, String>().puts("soure","秒杀商品图片点击量"));
                        MobclickAgent.onEvent(context, "event2APPKillListGoodsClick", new SimpleHashMapBuilder<String, String>().puts("soure","立即抢购按钮点击量"));
                        ARouter.getInstance()
                                .build(ServiceRoutes.SERVICE_DETAIL)
                                .withString("id", getDatas().get(holder.getPosition()).goodsId)
                                .withString("marketingType", "3")
                                .navigation();
                    }
                });
            } else {
                price.setTextColor(Color.parseColor("#333333"));
                pricePre.setTextColor(Color.parseColor("#333333"));
                robBtn.setTextColor(Color.parseColor("#333333"));
                robBtn.setText("已抢完");
                robBtn.setCompoundDrawables(null, null, null, null);
                yellowView.setBackgroundColor(Color.parseColor("#F1F1F1"));
                linePriceView.setBackground(context.getDrawable(R.drawable.shape_seckill_list_green_line));
                redView.setBackground(context.getDrawable(R.drawable.seckill_list_btn_red_grey));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "当前商品已抢完，看看其他商品吧~", Toast.LENGTH_LONG).show();
                    }
                });
            }
        } else {//未开始抢购
            progressLL.setVisibility(View.INVISIBLE);
            price.setTextColor(Color.parseColor("#F02846"));
            pricePre.setTextColor(Color.parseColor("#F02846"));
            robBtn.setCompoundDrawables(null, null, null, null);
            yellowView.setBackground(context.getDrawable(R.drawable.seckill_list_btn_yellow_bg));
            linePriceView.setBackground(context.getDrawable(R.drawable.seckill_list_btn_price_bg));
            redView.setBackground(context.getDrawable(R.drawable.seckill_list_btn_red_green));
            if (model.remindState == 0) {
                robBtn.setText("提醒我");
                btnConstraintLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (moutClickListener != null) {
                            moutClickListener.outClick("remind", String.format("%s,%s,%s", model.goodsId, model.mapMarketingGoodsId, 0));
                        }

                    }
                });
            } else {
                robBtn.setText("取消提醒");
                btnConstraintLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (moutClickListener != null) {
                            moutClickListener.outClick("remind", String.format("%s,%s,%s", model.goodsId, model.mapMarketingGoodsId, 1));
                        }
                    }
                });
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "当前活动未开始抢购", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
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

    @Nullable
    @Override
    public ObjectIteraor<NewUserListModel> getDuplicateObjectIterator() {
        return new ObjectIteraor<NewUserListModel>() {
            @Override
            public String getDesObj(NewUserListModel newUserListModel) {
                return newUserListModel.goodsId;
            }
        };
    }
}
