package com.health.index.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.index.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;

public class IndexTabEmptyAdapter extends BaseAdapter<String> {
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean visible=true;
    @Override
    public int getItemViewType(int position) {
        return 13;
    }
    public IndexTabEmptyAdapter() {
        this(R.layout.index_mon_empty);
    }
    private IndexTabEmptyAdapter(int viewId) {
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
