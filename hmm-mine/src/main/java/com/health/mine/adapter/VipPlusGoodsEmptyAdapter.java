package com.health.mine.adapter;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.mine.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;

public class VipPlusGoodsEmptyAdapter extends BaseAdapter<String> {

    public VipPlusGoodsEmptyAdapter() {
        this(R.layout.item_vip_right_goodempty);
    }

    public VipPlusGoodsEmptyAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {

    }

}
