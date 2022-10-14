package com.health.servicecenter.adapter;

import android.text.Html;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.servicecenter.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.model.GoodsDetail;

public class MallGoodsDetialIntroducePointAdapter extends BaseAdapter<GoodsDetail> {




    @Override
    public int getItemViewType(int position) {
        return 43;
    }

    public MallGoodsDetialIntroducePointAdapter() {
        this(R.layout.service_item_goodsdetail_introduce_point);
    }

    private MallGoodsDetialIntroducePointAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {
        GoodsDetail goodsDetail = getModel();
         LinearLayout imgLL;
         TextView pointTitle;
         TextView pointTxtValue;
        imgLL = (LinearLayout) baseHolder.itemView.findViewById(R.id.imgLL);
        pointTitle = (TextView) baseHolder.itemView.findViewById(R.id.pointTitle);
        pointTxtValue = (TextView) baseHolder.itemView.findViewById(R.id.pointTxtValue);

        if(goodsDetail.pointsGoodsNote!=null){
            pointTxtValue.setText(Html.fromHtml(goodsDetail.pointsGoodsNote));
        }
    }

    private void initView() {

    }
}
