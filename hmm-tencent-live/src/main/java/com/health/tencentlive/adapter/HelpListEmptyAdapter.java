package com.health.tencentlive.adapter;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.tencentlive.R;
import com.health.tencentlive.model.LiveHelpList;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;

public class HelpListEmptyAdapter extends BaseAdapter<String> {

    public HelpListEmptyAdapter() {
        this(R.layout.live_helplist_empty_adapter_layout);
    }

    public HelpListEmptyAdapter(int viewId) {
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
