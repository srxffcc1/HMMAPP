package com.health.servicecenter.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.servicecenter.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;

public class MallGoodsDetialSelectAdapter extends BaseAdapter<String> {
    @Override
    public int getItemViewType(int position) {
        return 499;
    }

    public MallGoodsDetialSelectAdapter() {
        this(R.layout.service_item_goodsdetail_select);
    }

    private MallGoodsDetialSelectAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {
        ConstraintLayout basketLL;
        TextView basketTitle;
        TextView basketCount;
        ImageView basketMore;

        basketLL = (ConstraintLayout) baseHolder.itemView.findViewById(R.id.basketLL);
        basketTitle = (TextView) baseHolder.itemView.findViewById(R.id.basketTitle);
        basketCount = (TextView) baseHolder.itemView.findViewById(R.id.basketCount);
        basketMore = (ImageView) baseHolder.itemView.findViewById(R.id.basketMore);
        basketTitle.setText("已选");
        basketCount.setText(getModel());
        baseHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(moutClickListener!=null){
                    moutClickListener.outClick("规格选择",null);
                }
            }
        });
    }
}
