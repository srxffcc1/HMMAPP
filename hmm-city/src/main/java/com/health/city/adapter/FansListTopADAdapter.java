package com.health.city.adapter;


import androidx.annotation.NonNull;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.city.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FansListTopADAdapter extends BaseAdapter<String> {

    public FansListTopADAdapter(){
        this(R.layout.fans_list_top_ad_item);
    }

    public FansListTopADAdapter(int viewId) {
        super(viewId);
    }
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean visible=true;
    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    public FansListTopADAdapter(@NotNull ArrayList<String> list, int viewId) {
        super(list, viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {

    }
}
