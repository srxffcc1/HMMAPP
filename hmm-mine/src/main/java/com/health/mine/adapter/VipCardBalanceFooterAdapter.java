package com.health.mine.adapter;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.example.lib_ShapeView.view.ShapeTextView;
import com.health.mine.R;
import com.health.mine.model.BalanceListModel;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;

public class VipCardBalanceFooterAdapter extends BaseAdapter<BalanceListModel> {

    public VipCardBalanceFooterAdapter() {
        this(R.layout.vip_adapter_layout);
    }

    public VipCardBalanceFooterAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        TextView payType = holder.getView(R.id.payType);
        View indicator = holder.getView(R.id.indicator);
        TextView address = holder.getView(R.id.address);
        TextView contentType = holder.getView(R.id.contentType);
        TextView money = holder.getView(R.id.money);
        TextView createTime = holder.getView(R.id.createTime);
        ShapeTextView mark = holder.getView(R.id.mark);
        address.setText(getDatas().get(position).DepartName
                .replaceAll("【", "[").replaceAll("】", "]")
                .replaceAll("（", "(").replaceAll("）", ")")
                .replaceAll(" ", "").trim());
        contentType.setText(getDatas().get(position).OperType
                .replaceAll("【", "[").replaceAll("】", "]")
                .replaceAll("（", "(").replaceAll("）", ")")
                .replaceAll(" ", "").trim());
        if (Double.parseDouble(getDatas().get(position).OperMoney) > 0) {
            money.setText("+" + getDatas().get(position).OperMoney);
            money.setTextColor(Color.parseColor("#FA3C5A"));
            indicator.setBackgroundResource(R.drawable.shape_circular_red);
        } else {
            money.setText(getDatas().get(position).OperMoney);
            money.setTextColor(Color.parseColor("#333333"));
            indicator.setBackgroundResource(R.drawable.shape_circular_grey);
        }
        if (getDatas().get(position).PayModeName != null && !TextUtils.isEmpty(getDatas().get(position).PayModeName)) {
            payType.setVisibility(View.VISIBLE);
            payType.setText(getDatas().get(position).PayModeName
                    .replaceAll("【", "[").replaceAll("】", "]")
                    .replaceAll("（", "(").replaceAll("）", ")")
                    .replaceAll(" ", "").trim());
        } else {
            payType.setVisibility(View.GONE);
        }
//        String[] time = getDatas().get(position).OperDate.split(" ");
//        createTime.setText(time[0].replaceAll("-", "/"));
        createTime.setText(getDatas().get(position).OperDate);
        if (getDatas().get(position).RuleName != null && !TextUtils.isEmpty(getDatas().get(position).RuleName)) {
            mark.setVisibility(View.VISIBLE);
            mark.setText(getDatas().get(position).RuleName.trim());
        } else {
            mark.setVisibility(View.GONE);
        }
    }

}
