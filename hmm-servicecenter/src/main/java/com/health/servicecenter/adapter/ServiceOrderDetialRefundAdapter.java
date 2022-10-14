package com.health.servicecenter.adapter;

import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.servicecenter.R;
import com.health.servicecenter.model.OrderDetailModel;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.routes.ServiceRoutes;

import java.util.ArrayList;
import java.util.List;

public class ServiceOrderDetialRefundAdapter extends BaseAdapter<OrderDetailModel> {

    private List<OrderDetailModel.Refunds> ticketBackIng = new ArrayList<>();//退款中
    private List<OrderDetailModel.Ticket> ticketBackSuccess = new ArrayList<>();//退款成功
    private List<OrderDetailModel.Refunds> ticketBackFaild = new ArrayList<>();//退款失败
    private List<OrderDetailModel.Refunds> ticketBackCancle = new ArrayList<>();//退款撤销

    public ServiceOrderDetialRefundAdapter() {
        this(R.layout.item_service_order_detial_refund_zxing);
    }

    public ServiceOrderDetialRefundAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, final int position) {
        LinearLayout zxingNouseList = holder.getView(R.id.zxingNouseList);
        RelativeLayout refundRel = holder.getView(R.id.refundRel);
        TextView refundTxT = holder.getView(R.id.refundTxT);
        TextView useNum = holder.getView(R.id.useNum);
        if (getModel() == null) {
            return;
        }
        ticketBackIng.clear();
        ticketBackSuccess.clear();
        ticketBackFaild.clear();
        ticketBackCancle.clear();
        OrderDetailModel mOrderDetailModel = getModel();
        int refundsNum = 0;
        for (int i = 0; i < 1; i++) {
            final OrderDetailModel.Refunds goodsr = mOrderDetailModel.orderRefundsList.get(i);
            refundsNum = goodsr.refundNumber;
            if (goodsr.refundStatus == 0) {
                ticketBackIng.add(goodsr);
            } else if (goodsr.refundStatus == 1) {
                ticketBackFaild.add(goodsr);
            } else if (goodsr.refundStatus == 2) {
                for (int j = 0; j < goodsr.orderGoodsRefundsList.size(); j++) {
                    if (goodsr.orderGoodsRefundsList.get(j).ticketNoList != null) {
                        ticketBackSuccess.addAll(goodsr.orderGoodsRefundsList.get(j).ticketNoList);
                    }
                }
            } else if (goodsr.refundStatus == -1) {
                ticketBackCancle.add(goodsr);
            }
            refundRel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ARouter.getInstance()
                            .build(ServiceRoutes.SERVICE_ORDERBACKDETIAL)
                            .withString("refundId", goodsr.refundId + "")
                            .navigation();
                }
            });
        }
        if (ticketBackIng.size() > 0) {
            useNum.setText("退款售后中 数量" + refundsNum);
        }
        if (ticketBackIng.size() == 0 && ticketBackSuccess.size() > 0) {
            useNum.setText("退款成功 数量" + refundsNum);
        }
//        if (ticketBackFaild.size() > 0) {
//            useNum.setText("退款失败 数量" + ticketBackFaild.size());
//        }
//        if (ticketBackCancle.size() > 0 && ticketBackIng.size() == 0) {
//            useNum.setText("退款撤销 数量" + ticketBackCancle.size());
//        }
        if (ticketBackSuccess.size() > 0) {
            zxingNouseList.removeAllViews();
            for (int i = 0; i < ticketBackSuccess.size(); i++) {
                OrderDetailModel.Ticket ticket = ticketBackSuccess.get(i);
                View view = LayoutInflater.from(context).inflate(R.layout.item_service_order_detial_use_zxing_layout, null);
                TextView tv_zxing = view.findViewById(R.id.zxing_value);
                if (!TextUtils.isEmpty(ticket.ticketNo)) {
                    String bankCard = ticket.ticketNo;
                    String str = "";
                    str = bankCard.substring(0, 4) + " " + bankCard.substring(4, bankCard.length());
                    tv_zxing.setText(str);
                    tv_zxing.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                }
                zxingNouseList.addView(view);
            }
        }
    }
}
