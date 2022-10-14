package com.health.second.adapter;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.second.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;

public class SecondActGoodsEmptyAdapter extends BaseAdapter<String> {

    @Override
    public int getItemViewType(int position) {
        return 29;
    }
    public SecondActGoodsEmptyAdapter() {
        this(R.layout.item_second_empty_layout);
    }
    private SecondActGoodsEmptyAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {

    }
}
