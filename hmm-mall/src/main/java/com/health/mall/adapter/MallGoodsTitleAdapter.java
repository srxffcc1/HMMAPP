package com.health.mall.adapter;

import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.mall.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;

public class MallGoodsTitleAdapter extends BaseAdapter<String> {

    public MallGoodsTitleAdapter() {
        this(R.layout.mall_goods_title_layout);
    }

    private MallGoodsTitleAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {
        ConstraintLayout parentCategory;
        LinearLayout nameandicon;
        TextView title1;
        TextView title2;
        parentCategory = (ConstraintLayout) baseHolder.itemView.findViewById(R.id.parent_category);
        nameandicon = (LinearLayout) baseHolder.itemView.findViewById(R.id.nameandicon);
        title1 = (TextView) baseHolder.itemView.findViewById(R.id.title1);
        title2 = (TextView) baseHolder.itemView.findViewById(R.id.title2);
        if ("".equals(getModel()) || "null".equals(getModel())) {

        } else {
            title1.setText(getModel());
        }


    }

    private void initView() {

    }
}
