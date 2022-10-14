package com.health.servicecenter.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.servicecenter.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;

public class MallGoodsBasketErrorTopAdapter extends BaseAdapter<String> {



    @Override
    public int getItemViewType(int position) {
        return 44;
    }

    public MallGoodsBasketErrorTopAdapter() {
        this(R.layout.service_item_goodsbasket_error_top);
    }

    private MallGoodsBasketErrorTopAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {
         TextView errorCount;

        errorCount = (TextView) baseHolder.itemView.findViewById(R.id.errorCount);
        errorCount.setText("失效商品"+getModel()+"件");
        baseHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(moutClickListener!=null){
                    moutClickListener.outClick("清空失效商品",null);
                }
            }
        });
    }

    private void initView() {

    }
}
