package com.health.servicecenter.adapter;

import android.view.View;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.servicecenter.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;

public class MallGoodsItemTitleAdapter extends BaseAdapter<String> {

    @Override
    public int getItemViewType(int position) {
        return 8;
    }
    public MallGoodsItemTitleAdapter() {
        this(R.layout.item_mall_goods_chose_title);

    }
    private MallGoodsItemTitleAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        LinearLayoutHelper helper=new LinearLayoutHelper();
        return helper;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {

        baseHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }
}
