package com.health.mine.adapter;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.mine.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;

public class VipPlusGoodsTitleAdapter extends BaseAdapter<String> {

    public VipPlusGoodsTitleAdapter() {
        this(R.layout.item_vip_right_goodtitle);
    }

    public VipPlusGoodsTitleAdapter(int viewId) {
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
