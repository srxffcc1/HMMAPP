package com.health.discount.adapter;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.discount.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;

/**
 * @author Li
 * @date 2019/03/25 11:03
 * @des 推荐阅读
 */

public class PointBlockListAdapter extends BaseAdapter<String> {
    public PointBlockListAdapter() {
        this(R.layout.dis_function_block_big);
    }

    public PointBlockListAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, final int position) {


    }
}
