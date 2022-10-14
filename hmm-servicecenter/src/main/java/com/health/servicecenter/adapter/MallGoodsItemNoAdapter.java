package com.health.servicecenter.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.servicecenter.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;

public class MallGoodsItemNoAdapter extends BaseAdapter<String> {

    @Override
    public int getItemViewType(int position) {
        return 8;
    }

    public MallGoodsItemNoAdapter() {
        this(R.layout.item_mall_goods_no);

    }

    private MallGoodsItemNoAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        LinearLayoutHelper helper = new LinearLayoutHelper();
        return helper;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {

         ImageView noMSgImg;
         TextView noMSgTitle;
        noMSgImg = (ImageView) baseHolder.itemView.findViewById(R.id.noMSgImg);
        noMSgTitle = (TextView) baseHolder.itemView.findViewById(R.id.noMSgTitle);
        noMSgTitle.setText(getModel());

    }

    private void initView() {

    }
}
