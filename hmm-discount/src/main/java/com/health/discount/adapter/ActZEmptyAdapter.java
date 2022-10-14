package com.health.discount.adapter;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.discount.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;

public class ActZEmptyAdapter extends BaseAdapter<String> {

    @Override
    public int getItemViewType(int position) {
        return 29;
    }
    public ActZEmptyAdapter() {
        this(R.layout.dis_item_fragment_empty);
    }
    private ActZEmptyAdapter(int viewId) {
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
