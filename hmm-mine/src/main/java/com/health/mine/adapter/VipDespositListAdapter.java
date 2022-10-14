package com.health.mine.adapter;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.mine.R;
import com.healthy.library.model.DepositList;


public class VipDespositListAdapter extends BaseQuickAdapter<DepositList, BaseViewHolder> {

    public VipDespositListAdapter() {
        this(R.layout.item_deposit_list);
    }

    private VipDespositListAdapter(int layoutResId) {
        super(layoutResId);

    }

    @Override
    protected void convert(BaseViewHolder helper, final DepositList item) {

        View indicator;
        TextView depositName;
        TextView depositNum;
        TextView depositShop;
        TextView depositTime;

        indicator = (View) helper.itemView.findViewById(R.id.indicator);
        depositName = (TextView) helper.itemView.findViewById(R.id.depositName);
        depositNum = (TextView) helper.itemView.findViewById(R.id.depositNum);
        depositShop = (TextView) helper.itemView.findViewById(R.id.depositShop);
        depositTime = (TextView) helper.itemView.findViewById(R.id.depositTime);

        depositShop.setText(getDepartName(item.DepartName.trim()));
        depositTime.setText(item.OperDate);
        if (item.highLight == 0) {
            if (item.Number != null && !TextUtils.isEmpty(item.Number)) {
                if (item.Number.substring(0, 1).equals("-")) {
                    depositNum.setText(item.Number);
                } else {
                    depositNum.setText("-" + item.Number);
                }
                depositNum.setTextColor(Color.parseColor("#333333"));
                indicator.setBackgroundResource(R.drawable.shape_circular_grey);
            } else {
                depositNum.setText("");
            }

        } else {
            if (item.Number != null && !TextUtils.isEmpty(item.Number)) {
                if (item.Number.substring(0, 1).equals("+")) {
                    depositNum.setText(item.Number);
                } else {
                    depositNum.setText("+" + item.Number);
                }
                depositNum.setTextColor(Color.parseColor("#FA3C5A"));
                indicator.setBackgroundResource(R.drawable.shape_circular_red);
            } else {
                depositNum.setText("");
            }

        }
        depositName.setText(item.OperType);
    }

    private String getDepartName(String DepartName) {
        if (DepartName.contains("】")) {
            return DepartName.substring(DepartName.lastIndexOf("】") + 1, DepartName.length());
        }
        return DepartName;
    }
}
