package com.health.servicecenter.adapter;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.servicecenter.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;

public class MallGoodsDetialDiscussAdapter extends BaseAdapter<String> {
    @Override
    public int getItemViewType(int position) {
        return 4;
    }

    public MallGoodsDetialDiscussAdapter() {
        this(R.layout.service_item_goodsdetail_discuss);
    }

    private MallGoodsDetialDiscussAdapter(int viewId) {
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
