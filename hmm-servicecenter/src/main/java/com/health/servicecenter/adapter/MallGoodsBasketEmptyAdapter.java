package com.health.servicecenter.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.servicecenter.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;

public class MallGoodsBasketEmptyAdapter extends BaseAdapter<String> {




    @Override
    public int getItemViewType(int position) {
        return 48;
    }

    public MallGoodsBasketEmptyAdapter() {
        this(R.layout.service_item_goodsbasket_empty);
    }

    private MallGoodsBasketEmptyAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {
         ImageView emptyImg;
         TextView emptyContext;
         TextView goGoodsMain;
        emptyImg = (ImageView) baseHolder.itemView.findViewById(R.id.emptyImg);
        emptyContext = (TextView) baseHolder.itemView.findViewById(R.id.emptyContext);
        goGoodsMain = (TextView) baseHolder.itemView.findViewById(R.id.goGoodsMain);
        goGoodsMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(moutClickListener!=null){
                    moutClickListener.outClick("去逛逛吧",null);
                }
            }
        });
    }

    private void initView() {


    }
}
