package com.health.mine.adapter;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.mine.R;
import com.health.mine.model.IntegralListModel;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;

public class VipIntegralAdapter extends BaseAdapter<IntegralListModel> {
    public VipIntegralAdapter() {
        this(R.layout.vip_adapter_layout);
    }

    public VipIntegralAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        IntegralListModel model = getDatas().get(position);
        TextView address = holder.getView(R.id.address);
        TextView contentType = holder.getView(R.id.contentType);
        TextView money = holder.getView(R.id.money);
        TextView createTime = holder.getView(R.id.createTime);
        TextView payType = holder.getView(R.id.payType);
        TextView mark = holder.getView(R.id.mark);
        mark.setVisibility(View.GONE);
        View indicator = holder.getView(R.id.indicator);
        contentType.setText(model.OperType);
        if (model.OperType != null && !TextUtils.isEmpty(model.OperType)) {
            payType.setVisibility(View.VISIBLE);
            if (model.DepartName.contains("】")) {
                payType.setText(model.DepartName.substring(model.DepartName.lastIndexOf("】") + 1, model.DepartName.length()));
            } else {
                payType.setText(model.DepartName);
            }

        } else {
            payType.setVisibility(View.GONE);
        }
        if (Double.parseDouble(model.OperMoney) > 0) {
            money.setText("+" + model.OperMoney);
            money.setTextColor(Color.parseColor("#F93F60"));
            indicator.setBackgroundResource(R.drawable.shape_circular_red);
        } else {
            money.setText(model.OperMoney);
            money.setTextColor(Color.parseColor("#222222"));
            indicator.setBackgroundResource(R.drawable.shape_circular_grey);
        }
//        if (model.Other != null && !TextUtils.isEmpty(model.Other)) {
//            createTime.setText(model.Other);
//        } else {
//            createTime.setText(model.DepartName);
//        }
        address.setText(model.Other.replaceAll("接口扣积分.", ""));
        String[] time = model.OperDate.split(" ");
        createTime.setText(time[0].replaceAll("-", "/"));
    }
}
