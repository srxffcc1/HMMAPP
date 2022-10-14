package com.health.servicecenter.adapter;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.servicecenter.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.model.OrderList;
import com.healthy.library.utils.FormatUtils;

import java.math.BigDecimal;
import java.util.List;

public class OrderDetialPriceAdapter extends BaseAdapter<OrderList> {
    public OrderDetialPriceAdapter() {
        this(R.layout.item_order_detial_price);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        TextView goods_price = holder.getView(R.id.goods_price);
        TextView distribution_price_title = holder.getView(R.id.distribution_price_title);
        TextView distribution_price = holder.getView(R.id.distribution_price);
        TextView total_price = holder.getView(R.id.total_price);
        TextView tv_total_price_title = holder.getView(R.id.tv_total_price_title);
        TextView point_title = holder.getView(R.id.point_title);
        TextView point_price = holder.getView(R.id.point_price);
        TextView couponPrice_title = holder.getView(R.id.couponPrice_title);
        TextView couponPrice = holder.getView(R.id.couponPrice);

        final OrderList.OrderChild orderChild = getModel().orderChild;
        OrderList.OrderFather orderFather = getModel().orderFather;
        if (orderChild == null && orderFather != null) {//主订单.
            if (orderFather.subOrderList != null && orderFather.subOrderList.size() > 0) {
                if ("5".equals(orderFather.orderType)) {
                    if (orderFather.totalPayAmount != null) {
                        if ("0".equals(FormatUtils.moneyKeep2Decimals(orderFather.totalPayAmount))) {
                            goods_price.setText(FormatUtils.moneyKeep2Decimals(orderFather.totalPayPoints) + "积分");
                        } else {
                            goods_price.setText(FormatUtils.moneyKeep2Decimals(orderFather.totalPayPoints) + "积分+¥" + FormatUtils.moneyKeep2Decimals(orderFather.totalPayAmount));
                        }
                    }
                    couponPrice.setText("¥0");
                } else {
                    goods_price.setText("¥" + FormatUtils.moneyKeep2Decimals(orderFather.totalGoodsAmount));
                    BigDecimal price = new BigDecimal(orderFather.totalGoodsAmount).add(new BigDecimal(orderFather.totalDeliveryAmount)).subtract(new BigDecimal(orderFather.totalPayAmount));
                    if (price.doubleValue() > 0) {
                        couponPrice.setText("¥" + FormatUtils.moneyKeep2Decimals(price.toString()));
                    } else {
                        couponPrice.setText("¥0");
                    }
                }

                if ("5".equals(orderFather.orderType)) {
                    point_title.setVisibility(View.VISIBLE);
                    point_price.setVisibility(View.VISIBLE);
                    point_price.setText("" + FormatUtils.moneyKeep2Decimals(orderFather.totalPayPoints));
                    distribution_price_title.setVisibility(View.GONE);
                    distribution_price.setVisibility(View.GONE);
                } else {
                    point_title.setVisibility(View.GONE);
                    point_price.setVisibility(View.GONE);
                    distribution_price_title.setVisibility(View.VISIBLE);
                    distribution_price.setVisibility(View.VISIBLE);
                }

                distribution_price.setText("¥" + FormatUtils.moneyKeep2Decimals(orderFather.totalDeliveryAmount));
                SpannableString priceStr = new SpannableString("¥" + FormatUtils.moneyKeep2Decimals(orderFather.totalPayAmount));
                priceStr.setSpan(new AbsoluteSizeSpan(13, true), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                priceStr.setSpan(new StyleSpan(Typeface.NORMAL), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //正常
                if (orderFather.getOrderStatus() == 0) {
                    tv_total_price_title.setText("需付款 ");
                    total_price.setTextColor(Color.parseColor("#F02846"));
                    total_price.setText(priceStr);
                } else if (orderFather.getOrderStatus() == 3) {
                    tv_total_price_title.setText("应付款 ");
                    total_price.setTextColor(Color.parseColor("#222222"));
                    total_price.setText(priceStr);
                } else {
                    tv_total_price_title.setText("实付款 ");
                    total_price.setTextColor(Color.parseColor("#222222"));
                    total_price.setText(priceStr);
                }
            }
        } else if (orderChild != null && orderFather == null) {//子订单
            if (orderChild.getOrderType() == 5) {
                if ("0".equals(FormatUtils.moneyKeep2Decimals(orderChild.totalPayAmount))) {
                    goods_price.setText(FormatUtils.moneyKeep2Decimals(orderChild.totalPayPoints) + "积分");
                } else {
                    goods_price.setText(FormatUtils.moneyKeep2Decimals(orderChild.totalPayPoints) + "积分+¥" + FormatUtils.moneyKeep2Decimals(orderChild.totalPayAmount));
                }
                couponPrice.setText("¥0");
            } else {
                goods_price.setText("¥" + FormatUtils.moneyKeep2Decimals(orderChild.totalGoodsAmount));
                BigDecimal price = new BigDecimal(orderChild.totalGoodsAmount).add(new BigDecimal(orderChild.totalDeliveryAmount)).subtract(new BigDecimal(orderChild.totalPayAmount));
                if (price.doubleValue() > 0) {
                    couponPrice.setText("¥" + FormatUtils.moneyKeep2Decimals(price.toString()));
                } else {
                    couponPrice.setText("¥0");
                }
            }

            if ("5".equals(orderChild.orderType)) {
                point_title.setVisibility(View.VISIBLE);
                point_price.setVisibility(View.VISIBLE);
                point_price.setText("" + FormatUtils.moneyKeep2Decimals(orderChild.totalPayPoints));
                distribution_price_title.setVisibility(View.GONE);
                distribution_price.setVisibility(View.GONE);
            } else {
                point_title.setVisibility(View.GONE);
                point_price.setVisibility(View.GONE);
                distribution_price_title.setVisibility(View.VISIBLE);
                distribution_price.setVisibility(View.VISIBLE);
            }

            distribution_price.setText("¥" + FormatUtils.moneyKeep2Decimals(orderChild.totalDeliveryAmount));
            SpannableString priceStr = new SpannableString("¥" + FormatUtils.moneyKeep2Decimals(orderChild.totalPayAmount));
            priceStr.setSpan(new AbsoluteSizeSpan(13, true), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            priceStr.setSpan(new StyleSpan(Typeface.NORMAL), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //正常
            if (orderChild.getOrderStatus() == 0) {
                tv_total_price_title.setText("需付款 ");
                total_price.setTextColor(Color.parseColor("#F02846"));
                total_price.setText(priceStr);
            } else if (orderChild.getOrderStatus() == 3) {
                tv_total_price_title.setText("应付款 ");
                total_price.setTextColor(Color.parseColor("#F02846"));
                total_price.setText(priceStr);
            } else {
                tv_total_price_title.setText("实付款 ");
                total_price.setTextColor(Color.parseColor("#222222"));
                total_price.setText(priceStr);
            }
        }


    }

    public OrderDetialPriceAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

}
