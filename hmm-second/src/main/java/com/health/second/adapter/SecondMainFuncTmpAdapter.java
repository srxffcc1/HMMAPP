package com.health.second.adapter;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.second.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;

public class SecondMainFuncTmpAdapter extends BaseAdapter<String> {



    @Override
    public int getItemViewType(int position) {
        return 143;
    }

    public SecondMainFuncTmpAdapter() {
        this(R.layout.item_second_function_bar_tmp);
    }

    private SecondMainFuncTmpAdapter(int viewId) {
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
