package com.health.servicecenter.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.servicecenter.R;
import com.health.servicecenter.model.OrderDetailModel;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;

public class ServiceOrderDetialPayTypeAdapter extends BaseAdapter<OrderDetailModel> {

    public ServiceOrderDetialPayTypeAdapter() {
        this(R.layout.item_service_order_detial_pay_type);
    }

    public ServiceOrderDetialPayTypeAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        final TextView order_num = holder.getView(R.id.order_num);
        final TextView order_time = holder.getView(R.id.order_time);
        final TextView pay_type = holder.getView(R.id.pay_type);
        final TextView pay_time = holder.getView(R.id.pay_time);
        final TextView cancle_time = holder.getView(R.id.cancle_time);
        final TextView remark = holder.getView(R.id.remark);
        OrderDetailModel model = getModel();
        order_num.setText("订单编号：" + model.orderNum);
        order_time.setText("创建时间：" + model.createTime);
        cancle_time.setVisibility(View.GONE);
        if (model.status == 0 || model.status == 3) {
            pay_type.setVisibility(View.GONE);
            pay_time.setVisibility(View.GONE);
        } else {
            pay_type.setVisibility(View.VISIBLE);
            pay_time.setVisibility(View.VISIBLE);
            if (model.payDetails != null) {
                if (model.payDetails.payType == 1) {
                    pay_type.setText("支付方式：支付宝");
                } else if (model.payDetails.payType == 2) {
                    pay_type.setText("支付方式：微信");
                } else if (model.payDetails.payType == 3) {
                    pay_type.setText("支付方式：微信小程序");
                }
                pay_time.setText("付款时间：" + model.payDetails.successTime);

            }
        }
        if (model.cancelTime != null && !TextUtils.isEmpty(model.cancelTime)) {
            cancle_time.setVisibility(View.VISIBLE);
            cancle_time.setText("取消时间：" + model.cancelTime);
        }
        if (model.remark != null && !TextUtils.isEmpty(model.remark)) {
            remark.setVisibility(View.VISIBLE);
            remark.setText("订单备注：" + model.remark);
        } else {
            remark.setVisibility(View.GONE);
        }
    }
}
