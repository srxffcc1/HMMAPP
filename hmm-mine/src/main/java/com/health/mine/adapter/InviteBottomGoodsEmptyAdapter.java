package com.health.mine.adapter;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.mine.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;

public class InviteBottomGoodsEmptyAdapter extends BaseAdapter<String> {


    public InviteBottomGoodsEmptyAdapter() {
        this(R.layout.mine_adapter_invitebottom_goods_empty);
    }
    private InviteBottomGoodsEmptyAdapter(int viewId) {
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
