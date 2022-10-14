package com.health.servicecenter.adapter;

import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.servicecenter.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.model.BackListModel;

public class OrderBackDetialFooterAdapter extends BaseAdapter<BackListModel> {

    public OrderBackDetialFooterAdapter() {
        this(R.layout.item_order_back_detial_bottom);

    }

    public OrderBackDetialFooterAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        TextView back_num = holder.getView(R.id.back_num);
        TextView order_time = holder.getView(R.id.order_time);
        TextView order_num = holder.getView(R.id.order_num);
        TextView back_reason = holder.getView(R.id.back_reason);
        back_num.setText("售后单号：" + getModel().refundNum);
        order_time.setText("申请时间：" + getModel().createTime);
        order_num.setText("订单编号：" + getModel().mainOrderNum);
        back_reason.setText("申请原因：" + getModel().refundReason);
    }
}
