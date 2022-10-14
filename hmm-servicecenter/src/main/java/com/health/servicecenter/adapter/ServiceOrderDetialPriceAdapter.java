package com.health.servicecenter.adapter;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.servicecenter.R;
import com.health.servicecenter.model.OrderDetailModel;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.utils.FormatUtils;

public class ServiceOrderDetialPriceAdapter extends BaseAdapter<OrderDetailModel> {
    public ServiceOrderDetialPriceAdapter() {
        this(R.layout.item_service_order_detial_price);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        TextView goods_price = holder.getView(R.id.goods_price);
        TextView total_price = holder.getView(R.id.total_price);
        TextView tv_total_price_title = holder.getView(R.id.tv_total_price_title);
        OrderDetailModel model = getModel();
        goods_price.setText("¥" + FormatUtils.moneyKeep2Decimals(model.payMoney));
        SpannableString priceStr = new SpannableString("¥" + FormatUtils.moneyKeep2Decimals(model.payMoney));
        priceStr.setSpan(new AbsoluteSizeSpan(13, true), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        priceStr.setSpan(new StyleSpan(Typeface.NORMAL), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //正常
        if (model.status == 0) {
            tv_total_price_title.setText("需付款 ");
            total_price.setTextColor(Color.parseColor("#F02846"));
            total_price.setText(priceStr);
        } else if (model.status == 3) {
            tv_total_price_title.setText("应付款 ");
            total_price.setTextColor(Color.parseColor("#222222"));
            total_price.setText(priceStr);
        } else {
            tv_total_price_title.setText("实付款 ");
            total_price.setTextColor(Color.parseColor("#222222"));
            total_price.setText(priceStr);
        }


    }

    public ServiceOrderDetialPriceAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

}
