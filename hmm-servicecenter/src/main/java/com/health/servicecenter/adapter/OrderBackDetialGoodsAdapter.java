package com.health.servicecenter.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.health.servicecenter.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.model.BackListModel;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.widget.CornerImageView;

public class OrderBackDetialGoodsAdapter extends BaseAdapter<BackListModel> {

    public OrderBackDetialGoodsAdapter() {
        this(R.layout.item_order_back_detial_goods);
    }

    public OrderBackDetialGoodsAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        TextView tv_total_price = holder.getView(R.id.tv_total_price);
        TextView tv_total_num = holder.getView(R.id.tv_total_num);
        LinearLayout order_goods_listLL = holder.getView(R.id.order_goods_listLL);
        int refundTotalNum = 0;
        order_goods_listLL.removeAllViews();
        if (getModel().refundDetailList != null && getModel().refundDetailList.size() > 0) {
            for (int i = 0; i < getModel().refundDetailList.size(); i++) {
                BackListModel.RefundDetailListBean detailsBean = getModel().refundDetailList.get(i);
                refundTotalNum = detailsBean.refundQuantity + refundTotalNum;
                ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout
                        .LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 10, 0, 0);
                LayoutInflater inflater = LayoutInflater.from(context);
                View view = inflater.inflate(R.layout.item_order_back_detial_goods_layout, null);
                TextView mall_service_title = view.findViewById(R.id.goodsTitle);
                TextView goodsSpace = view.findViewById(R.id.goodsSpace);
                TextView goodsCount = view.findViewById(R.id.goodsCount);
                TextView goodsMoney = view.findViewById(R.id.goodsMoney);
                TextView pointLable = view.findViewById(R.id.pointLable);
                TextView refundPrice = view.findViewById(R.id.refundPrice);
                TextView refundNum = view.findViewById(R.id.refundNum);
                CornerImageView mall_service_img = view.findViewById(R.id.goodsImg);
                mall_service_title.setText(detailsBean.goodsTitle);
                goodsSpace.setText(detailsBean.goodsSpecDesc);
                if (detailsBean.refundPoints != null && !TextUtils.isEmpty(detailsBean.refundPoints)
                        && !"0".equals(FormatUtils.moneyKeep2Decimals(detailsBean.refundPoints))) {//表示是积分退订
                    pointLable.setVisibility(View.VISIBLE);
                    pointLable.getBackground().setAlpha(150);//积分兑换的标签透明度
                    if ("0".equals(FormatUtils.moneyKeep2Decimals(detailsBean.refundAmount))) {
                        goodsMoney.setText("");
                        refundPrice.setText(FormatUtils.moneyKeep2Decimals(detailsBean.refundPoints) + "积分");
                    } else {
                        goodsMoney.setText("");
                        refundPrice.setText(FormatUtils.moneyKeep2Decimals(detailsBean.refundPoints) + "积分+\n¥" + FormatUtils.moneyKeep2Decimals(detailsBean.refundAmount) + "");
                    }
                } else {
                    pointLable.setVisibility(View.GONE);
                    goodsMoney.setText("");
                    refundPrice.setText("¥" + FormatUtils.moneyKeep2Decimals(detailsBean.refundAmount));
                }
                goodsCount.setText("");
                refundNum.setText("" + detailsBean.refundQuantity);
                com.healthy.library.businessutil.GlideCopy.with(context).load(detailsBean.goodsImage)
                        .placeholder(R.drawable.img_1_1_default2)
                        .error(R.drawable.img_1_1_default)

                        .into(mall_service_img);
                order_goods_listLL.addView(view);
            }
        }
        tv_total_num.setText(refundTotalNum + "");
        if (getModel().refundPoints != null && !TextUtils.isEmpty(getModel().refundPoints)
                && !"0".equals(FormatUtils.moneyKeep2Decimals(getModel().refundPoints))) {//表示是积分退订
            if ("0".equals(FormatUtils.moneyKeep2Decimals(getModel().refundAmount))) {
                tv_total_price.setText(FormatUtils.moneyKeep2Decimals(getModel().refundPoints) + "积分");
            } else {
                tv_total_price.setText(FormatUtils.moneyKeep2Decimals(getModel().refundPoints) + "积分+¥" + FormatUtils.moneyKeep2Decimals(getModel().refundAmount));
            }
        } else {
            tv_total_price.setText("¥" + FormatUtils.moneyKeep2Decimals(getModel().refundAmount) + "");
        }
    }
}
