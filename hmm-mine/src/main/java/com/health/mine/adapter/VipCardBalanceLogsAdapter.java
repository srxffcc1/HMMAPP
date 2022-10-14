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
import com.healthy.library.model.BalanceModel;

public class VipCardBalanceLogsAdapter extends BaseAdapter<BalanceModel.VipClassInfoList> {

    public VipCardBalanceLogsAdapter() {
        this(R.layout.vip_adapter_layout);
    }

    public VipCardBalanceLogsAdapter(int viewId) {
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
        BalanceModel.VipClassInfoList model = getDatas().get(position);
        if (!TextUtils.isEmpty(model.RuleName)) {
            address.setText(model.RuleName
                    .replaceAll("【", "[").replaceAll("】", "]")
                    .replaceAll("（", "(").replaceAll("）", ")")
                    .replaceAll(" ", "").trim());
        } else {
            address.setText("");
        }
        if (!TextUtils.isEmpty(model.RuleType)) {
            contentType.setText(model.RuleType
                    .replaceAll("【", "[").replaceAll("】", "]")
                    .replaceAll("（", "(").replaceAll("）", ")")
                    .replaceAll(" ", "").trim());
        } else {
            contentType.setText("");
        }
        if (Double.parseDouble(model.CardYe) > 0) {
            money.setText("+" + model.CardYe);
            money.setTextColor(Color.parseColor("#FA3C5A"));
            indicator.setBackgroundResource(R.drawable.shape_circular_red);
        } else {
            money.setText(model.CardYe);
            money.setTextColor(Color.parseColor("#333333"));
            indicator.setBackgroundResource(R.drawable.shape_circular_grey);
        }
        payType.setVisibility(View.GONE);
        createTime.setText("");
        try {
            createTime.setText(model.RegDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mark.setVisibility(View.GONE);
    }

}
