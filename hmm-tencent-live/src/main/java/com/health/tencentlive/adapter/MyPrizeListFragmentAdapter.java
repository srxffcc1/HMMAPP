package com.health.tencentlive.adapter;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.tencentlive.R;
import com.health.tencentlive.model.JackpotList;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.utils.FormatUtils;

public class MyPrizeListFragmentAdapter extends BaseAdapter<JackpotList> {

    public MyPrizeListFragmentAdapter() {
        this(R.layout.live_myprizelistfragment_adapter_layout);
    }

    public MyPrizeListFragmentAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        TextView prizeName = (TextView) holder.getView(R.id.prizeName);
        TextView prizeNum = (TextView) holder.getView(R.id.prizeNum);
        TextView prizeContent = (TextView) holder.getView(R.id.prizeContent);
        LinearLayout prizeLayout = (LinearLayout) holder.getView(R.id.prizeLayout);

        JackpotList jackpotList = getDatas().get(position);
        if (jackpotList == null) {
            return;
        }
        prizeName.setText(jackpotList.name);
        if (jackpotList.needNumberOfInvitees <= 0) {
            prizeContent.setText(Html.fromHtml(String.format("需助力%s人，已完成", jackpotList.numberOfInvitees)));
        } else {
            prizeContent.setText(Html.fromHtml(String.format("需助力%s人，还差", jackpotList.numberOfInvitees) + "<font color='#F44263'>" + jackpotList.needNumberOfInvitees + "</font>人"));
        }

        int num = 0;
        prizeLayout.removeAllViews();
        if (jackpotList.helpWinCouponList != null && jackpotList.helpWinCouponList.size() > 0) {
            buildCoupon(jackpotList, prizeLayout);
            num += jackpotList.helpWinCouponList.size();
        }
        if (jackpotList.helpWinGoodsList != null && jackpotList.helpWinGoodsList.size() > 0) {
            buildGoods(jackpotList, prizeLayout);
            num += jackpotList.helpWinGoodsList.size();
        }
        if (jackpotList.helpCouponList != null && jackpotList.helpCouponList.size() > 0) {
            buildCoupon(jackpotList, prizeLayout);
            num += jackpotList.helpCouponList.size();
        }
        if (jackpotList.helpGoodsList != null && jackpotList.helpGoodsList.size() > 0) {
            buildGoods(jackpotList, prizeLayout);
            num += jackpotList.helpGoodsList.size();
        }
        prizeNum.setText(num + "");
    }

    private void buildCoupon(final JackpotList jackpotList, final LinearLayout prizeLayout) {
        if (jackpotList.finished) {
            if (jackpotList.helpWinCouponList == null || jackpotList.helpWinCouponList.size() == 0) {
                return;
            }
            for (int i = 0; i < jackpotList.helpWinCouponList.size(); i++) {
                View view = LayoutInflater.from(context).inflate(R.layout.item_live_prize_coupon_layout, prizeLayout, false);
                TextView couponMoney = (TextView) view.findViewById(R.id.couponMoney);
                TextView couponType = (TextView) view.findViewById(R.id.couponType);
                LinearLayout topView = (LinearLayout) view.findViewById(R.id.topView);
                TextView cardFlag = (TextView) view.findViewById(R.id.cardFlag);
                TextView cardLimit = (TextView) view.findViewById(R.id.cardLimit);
                TextView couponLabel = (TextView) view.findViewById(R.id.couponLabel);
                TextView couponDate = (TextView) view.findViewById(R.id.couponDate);
                TextView cardButton = (TextView) view.findViewById(R.id.cardButton);
                TextView couponNum = (TextView) view.findViewById(R.id.couponNum);
                final JackpotList.HelpWinCouponList bean = jackpotList.helpWinCouponList.get(i);
                String money = FormatUtils.moneyKeep2Decimals(bean.detail.basic.getCouponDenomination());
                SpannableString spannableString = new SpannableString("￥" + money);
                spannableString.setSpan(new AbsoluteSizeSpan(13, true), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new StyleSpan(Typeface.NORMAL), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new StyleSpan(Typeface.BOLD), 1, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                couponMoney.setText(spannableString);
                cardLimit.setText(bean.detail.basic.getCouponUseName());
                if (bean.detail.basic.usableStartTime != null && bean.detail.basic.usableEndTime != null) {
                    couponDate.setText(bean.detail.basic.usableStartTime.split(" ")[0] + "-" + bean.detail.basic.usableEndTime.split(" ")[0]);
                } else {
                    couponDate.setText("");
                }
                couponNum.setText("x" + bean.num);
                cardFlag.setText("通用券");
                couponType.setText(bean.detail.basic.getCouponNormalName());
                couponLabel.setText(bean.detail.basic.getCouponComment());
                if (bean.status == 0) {
                    cardButton.setTextColor(Color.parseColor("#FA3C5A"));
                    cardButton.setText("待领取");
                    cardButton.setBackgroundResource(R.drawable.shape_order_list_red_button);
                    cardButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (moutClickListener != null) {
                                bean.helpId = jackpotList.id;
                                moutClickListener.outClick("coupon2", bean);
                            }
                        }
                    });
                } else {
                    cardButton.setTextColor(Color.parseColor("#9A9A9A"));
                    cardButton.setText("已领取");
                    cardButton.setBackgroundResource(R.drawable.shape_order_list_grey_button);
                    cardButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(context, "已领过了！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                prizeLayout.addView(view);
            }
        } else {
            if (jackpotList.helpCouponList == null || jackpotList.helpCouponList.size() == 0) {
                return;
            }
            for (int i = 0; i < jackpotList.helpCouponList.size(); i++) {
                View view = LayoutInflater.from(context).inflate(R.layout.item_live_prize_coupon_layout, prizeLayout, false);
                TextView couponMoney = (TextView) view.findViewById(R.id.couponMoney);
                TextView couponType = (TextView) view.findViewById(R.id.couponType);
                LinearLayout topView = (LinearLayout) view.findViewById(R.id.topView);
                TextView cardFlag = (TextView) view.findViewById(R.id.cardFlag);
                TextView cardLimit = (TextView) view.findViewById(R.id.cardLimit);
                TextView couponLabel = (TextView) view.findViewById(R.id.couponLabel);
                TextView couponDate = (TextView) view.findViewById(R.id.couponDate);
                TextView cardButton = (TextView) view.findViewById(R.id.cardButton);
                TextView couponNum = (TextView) view.findViewById(R.id.couponNum);
                final JackpotList.HelpCouponList bean = jackpotList.helpCouponList.get(i);
                String money = FormatUtils.moneyKeep2Decimals(bean.detail.basic.getCouponDenomination());
                SpannableString spannableString = new SpannableString("￥" + money);
                spannableString.setSpan(new AbsoluteSizeSpan(13, true), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new StyleSpan(Typeface.NORMAL), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new StyleSpan(Typeface.BOLD), 1, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                couponMoney.setText(spannableString);
                cardLimit.setText(bean.detail.basic.getCouponUseName());
                if (bean.detail.basic.usableStartTime != null && bean.detail.basic.usableEndTime != null) {
                    couponDate.setText(bean.detail.basic.usableStartTime.split(" ")[0] + "-" + bean.detail.basic.usableEndTime.split(" ")[0]);
                } else {
                    couponDate.setText("");
                }
                couponNum.setText("x" + bean.num);
                cardFlag.setText("通用券");
                couponType.setText(bean.detail.basic.getCouponNormalName());
                couponLabel.setText(bean.detail.basic.getCouponComment());
                if (jackpotList.needNumberOfInvitees > 0) {
                    cardButton.setText("去助力");
                    cardButton.setTextColor(Color.parseColor("#FFFFFF"));
                    cardButton.setBackgroundResource(R.drawable.shape_red_btn);
                    cardButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (moutClickListener != null) {
                                moutClickListener.outClick("help", null);
                            }
                        }
                    });
                } else {
                    cardButton.setTextColor(Color.parseColor("#FA3C5A"));
                    cardButton.setText("待领取");
                    cardButton.setBackgroundResource(R.drawable.shape_order_list_red_button);
                    cardButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (moutClickListener != null) {
                                bean.helpId = jackpotList.id;
                                moutClickListener.outClick("coupon1", bean);
                            }
                        }
                    });
                }
                prizeLayout.addView(view);
            }
        }

    }

    private void buildGoods(final JackpotList jackpotList, final LinearLayout prizeLayout) {
        if (jackpotList.finished) {
            if (jackpotList.helpWinGoodsList == null || jackpotList.helpWinGoodsList.size() == 0) {
                return;
            }
            for (int i = 0; i < jackpotList.helpWinGoodsList.size(); i++) {
                View view = LayoutInflater.from(context).inflate(R.layout.item_live_prize_goods_layout, prizeLayout, false);
                ImageView goodsImg = (ImageView) view.findViewById(R.id.goodsImg);
                LinearLayout topView = (LinearLayout) view.findViewById(R.id.topView);
                TextView goodsFlag = (TextView) view.findViewById(R.id.goodsFlag);
                TextView goodsTitle = (TextView) view.findViewById(R.id.goodsTitle);
                TextView goodsLabel = (TextView) view.findViewById(R.id.goodsLabel);
                TextView goodsPrice = (TextView) view.findViewById(R.id.goodsPrice);
                TextView cardButton = (TextView) view.findViewById(R.id.cardButton);
                final JackpotList.HelpWinGoodsList bean = jackpotList.helpWinGoodsList.get(i);
                if (bean.detail==null){
                    return;
                }
                com.healthy.library.businessutil.GlideCopy.with(getContext())
                        .load(bean.detail.getGoodsImage())
                        .placeholder(R.drawable.img_4_3_default2)
                        .error(R.drawable.img_4_3_default2)
                        .into(goodsImg);
                goodsTitle.setText(bean.detail.getGoodsTitle());
                goodsPrice.setText(String.format("原价：%s元", FormatUtils.moneyKeep2Decimals(bean.detail.platformPrice)));
                if (bean.status == 0) {
                    cardButton.setTextColor(Color.parseColor("#FA3C5A"));
                    cardButton.setText("待领取");
                    cardButton.setBackgroundResource(R.drawable.shape_order_list_red_button);
                    cardButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (moutClickListener != null) {
                                moutClickListener.outClick("goods2", bean);
                            }
                        }
                    });
                } else {
                    cardButton.setTextColor(Color.parseColor("#9A9A9A"));
                    cardButton.setText("已领取");
                    cardButton.setBackgroundResource(R.drawable.shape_order_list_grey_button);
                    cardButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(context, "已领过了！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                prizeLayout.addView(view);
            }
        } else {
            if (jackpotList.helpGoodsList == null || jackpotList.helpGoodsList.size() == 0) {
                return;
            }
            for (int i = 0; i < jackpotList.helpGoodsList.size(); i++) {
                View view = LayoutInflater.from(context).inflate(R.layout.item_live_prize_goods_layout, prizeLayout, false);
                ImageView goodsImg = (ImageView) view.findViewById(R.id.goodsImg);
                LinearLayout topView = (LinearLayout) view.findViewById(R.id.topView);
                TextView goodsFlag = (TextView) view.findViewById(R.id.goodsFlag);
                TextView goodsTitle = (TextView) view.findViewById(R.id.goodsTitle);
                TextView goodsLabel = (TextView) view.findViewById(R.id.goodsLabel);
                TextView goodsPrice = (TextView) view.findViewById(R.id.goodsPrice);
                TextView cardButton = (TextView) view.findViewById(R.id.cardButton);
                final JackpotList.HelpGoodsList bean = jackpotList.helpGoodsList.get(i);
                if (bean.detail==null){
                    return;
                }
                com.healthy.library.businessutil.GlideCopy.with(getContext())
                        .load(bean.detail.getGoodsImage())
                        .placeholder(R.drawable.img_4_3_default2)
                        .error(R.drawable.img_4_3_default2)
                        .into(goodsImg);
                goodsTitle.setText(bean.detail.getGoodsTitle());
                goodsPrice.setText(String.format("原价：%s元", FormatUtils.moneyKeep2Decimals(bean.detail.platformPrice)));
                if (jackpotList.needNumberOfInvitees > 0) {
                    cardButton.setText("去助力");
                    cardButton.setTextColor(Color.parseColor("#FFFFFF"));
                    cardButton.setBackgroundResource(R.drawable.shape_red_btn);
                    cardButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (moutClickListener != null) {
                                moutClickListener.outClick("help", null);
                            }
                        }
                    });
                } else {
                    cardButton.setTextColor(Color.parseColor("#FA3C5A"));
                    cardButton.setText("待领取");
                    cardButton.setBackgroundResource(R.drawable.shape_order_list_red_button);
                    cardButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (moutClickListener != null) {
                                moutClickListener.outClick("goods0", bean);
                                moutClickListener.outClick("goods1", String.format("%s,%s", jackpotList.id, bean.id));
                            }
                        }
                    });
                }
                prizeLayout.addView(view);
            }
        }

    }
}
