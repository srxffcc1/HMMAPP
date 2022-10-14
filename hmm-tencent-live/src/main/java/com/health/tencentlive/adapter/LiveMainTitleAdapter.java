package com.health.tencentlive.adapter;

import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.tencentlive.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;

public class LiveMainTitleAdapter extends BaseAdapter<String> {



    @Override
    public int getItemViewType(int position) {
        return 71;
    }

    public LiveMainTitleAdapter() {
        this(R.layout.item_t_live_list_title);
    }

    private LiveMainTitleAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {
        TextView title;
        title = (TextView) baseHolder.itemView.findViewById(R.id.title);
        title.setText(getModel());
    }

    private void initView() {
    }
}
