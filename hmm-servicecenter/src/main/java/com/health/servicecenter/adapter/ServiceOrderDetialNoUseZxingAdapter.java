package com.health.servicecenter.adapter;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;

import com.health.servicecenter.R;
import com.health.servicecenter.model.OrderDetailModel;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.utils.ImageUtilK;
import com.healthy.library.widget.ImageTextView;

import java.util.ArrayList;
import java.util.List;

public class ServiceOrderDetialNoUseZxingAdapter extends BaseAdapter<ArrayList<OrderDetailModel.Ticket>> {
    int forSize = 0;
    private boolean isShow = false;

    public ServiceOrderDetialNoUseZxingAdapter() {
        this(R.layout.item_service_order_detial_nouse_zxing);
    }

    public ServiceOrderDetialNoUseZxingAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, final int position) {
        CardView zxingCard = holder.getView(R.id.zxingCard);
        CardView zxingShowCard = holder.getView(R.id.zxingShowCard);
        LinearLayout underviewll = holder.getView(R.id.underviewll);
        LinearLayout zxingNouseList = holder.getView(R.id.zxingNouseList);
        final ImageTextView underview = holder.getView(R.id.underview);
        final List<OrderDetailModel.Ticket> nouse = getDatas().get(0);
        zxingNouseList.removeAllViews();
        if (forSize == 0) {
            forSize = nouse.size();
        }
        if (nouse.size() > 1) {
            underviewll.setVisibility(View.VISIBLE);
            if (!isShow) {
                forSize = 1;
                underview.setText("剩余" + (nouse.size() - forSize) + "个核销码");
                underview.setDrawable(R.drawable.goods_arrow_down, context);
            } else {
                forSize = nouse.size();
                underview.setText("收起");
                underview.setDrawable(R.drawable.goods_arrow_up, context);
            }
        } else {
            forSize = nouse.size();
            underviewll.setVisibility(View.GONE);
        }
        underviewll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isShow) {
                    forSize = nouse.size();
                    underview.setText("收起");
                    underview.setDrawable(R.drawable.goods_arrow_up, context);
                    isShow = true;
                } else {
                    forSize = 3;
                    underview.setText("剩余" + (nouse.size() - forSize) + "个核销码");
                    underview.setDrawable(R.drawable.goods_arrow_down, context);
                    isShow = false;
                }
                notifyDataSetChanged();
            }
        });
        for (int i = 0; i < forSize; i++) {
            OrderDetailModel.Ticket ticket = nouse.get(i);
            View view = LayoutInflater.from(context).inflate(R.layout.item_service_order_detial_zxing_layout, null);
            TextView tv_zxing = view.findViewById(R.id.zxing_value);
            ImageView img_zxing = view.findViewById(R.id.img_zxing);
            if (!TextUtils.isEmpty(ticket.ticketNo)){
                String bankCard = ticket.ticketNo;
                String str = "";
                str = bankCard.substring(0, 4) + " " + bankCard.substring(4, bankCard.length());
                tv_zxing.setText(str);
            }
            if (ticket.qrcodeBase64 != null) {
                Bitmap zxing = new ImageUtilK().base64ToBitmap(ticket.qrcodeBase64);
                img_zxing.setImageBitmap(zxing);
            }
            zxingNouseList.addView(view);
        }
    }
}
