package com.health.servicecenter.adapter;

import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;

import com.health.servicecenter.R;
import com.health.servicecenter.model.OrderDetailModel;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;

import java.util.ArrayList;
import java.util.List;

public class ServiceOrderDetialUseZxingAdapter extends BaseAdapter<ArrayList<OrderDetailModel.Ticket>> {

    public ServiceOrderDetialUseZxingAdapter() {
        this(R.layout.item_service_order_detial_use_zxing);
    }

    public ServiceOrderDetialUseZxingAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, final int position) {
        LinearLayout zxingNouseList = holder.getView(R.id.zxingNouseList);
        TextView useNum = holder.getView(R.id.useNum);
        final List<OrderDetailModel.Ticket> use = getDatas().get(0);
        useNum.setText("已使用 数量 " + use.size());
        zxingNouseList.removeAllViews();
        for (int i = 0; i < use.size(); i++) {
            OrderDetailModel.Ticket ticket = use.get(i);
            View view = LayoutInflater.from(context).inflate(R.layout.item_service_order_detial_use_zxing_layout, null);
            TextView tv_zxing = view.findViewById(R.id.zxing_value);
            if (!TextUtils.isEmpty(ticket.ticketNo)){
                String bankCard = ticket.ticketNo;
                String str = "";
                str = bankCard.substring(0, 4) + " " + bankCard.substring(4, bankCard.length());
                tv_zxing.setText(str);
                tv_zxing.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }
            zxingNouseList.addView(view);
        }
    }
}
