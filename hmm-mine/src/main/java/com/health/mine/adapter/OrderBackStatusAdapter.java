package com.health.mine.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.mine.R;
import com.health.mine.model.OrderDetailLineModel;

/**
 * @author Li
 * @date 2019/04/10 10:51
 * @des 订单
 */

public class OrderBackStatusAdapter extends BaseQuickAdapter<OrderDetailLineModel, BaseViewHolder> {




    public OrderBackStatusAdapter() {
        this(R.layout.mine_activity_back_timeline);
    }

    private OrderBackStatusAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, final OrderDetailLineModel item) {
         TextView tvTopLine;
         TextView tvDot;
         TextView tvBottomLine;
         TextView tvTitle;
         TextView tvContent;
         TextView tvTime;
        tvTopLine = (TextView) helper.itemView.findViewById(R.id.tvTopLine);
        tvDot = (TextView) helper.itemView.findViewById(R.id.tvDot);
        tvBottomLine = (TextView) helper.itemView.findViewById(R.id.tvBottomLine);
        tvTitle = (TextView) helper.itemView.findViewById(R.id.tvTitle);
        tvContent = (TextView) helper.itemView.findViewById(R.id.tvContent);
        tvTime = (TextView) helper.itemView.findViewById(R.id.tvTime);
        tvTopLine.setVisibility(helper.getAdapterPosition()==0? View.INVISIBLE:View.VISIBLE);
        tvDot.setBackgroundResource(item.dotbackGroudRes);
        tvBottomLine.setVisibility(item.isfinal? View.GONE:View.VISIBLE);
        tvTitle.setText(item.title);
        tvContent.setText(item.spannableStringBuilder);
        tvTime.setText(item.time);

    }

    private void initView() {

    }
}