package com.health.discount.adapter;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.discount.R;
import com.health.discount.model.LiveListModel;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;

public class DiscountSelectGoodsDialogAdapter extends BaseAdapter<LiveListModel> {

    public DiscountSelectGoodsDialogAdapter() {
        this(R.layout.item_discount_select_goods_adapter_layout);
    }

    public DiscountSelectGoodsDialogAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
