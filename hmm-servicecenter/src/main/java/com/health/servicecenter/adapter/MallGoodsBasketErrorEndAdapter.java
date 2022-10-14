package com.health.servicecenter.adapter;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.servicecenter.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;

public class MallGoodsBasketErrorEndAdapter extends BaseAdapter<String> {




    @Override
    public int getItemViewType(int position) {
        return 43;
    }

    public MallGoodsBasketErrorEndAdapter() {
        this(R.layout.service_item_goodsbasket_error_bottom);
    }

    private MallGoodsBasketErrorEndAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {

    }

    private void initView() {

    }
}
