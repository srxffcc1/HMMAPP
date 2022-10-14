package com.health.servicecenter.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.health.servicecenter.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.model.OrderList;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.IncreaseDecreaseView;

public class SubmitBackGoodsAdapter extends BaseAdapter<OrderList.OrderDetailListBean> {
    private NumChangedListener numChangedListener;

    public SubmitBackGoodsAdapter() {
        this(R.layout.order_back_submit_goods_adapter_layout);
    }

    public SubmitBackGoodsAdapter(int viewId) {
        super(viewId);
    }

    public void setNumChanged(NumChangedListener listener) {
        this.numChangedListener = listener;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull final BaseHolder holder, int position) {
        ConstraintLayout top;
        CornerImageView goodsImg;
        TextView pointLable;
        ImageView actFlag;
        TextView goodsTitle;
        final IncreaseDecreaseView increaseDecrease;
        TextView goodsSpec;
        TextView goodsMoney;
        TextView goodsCount;

        top = (ConstraintLayout) holder.getView(R.id.top);
        goodsImg = (CornerImageView) holder.getView(R.id.goodsImg);
        pointLable = (TextView) holder.getView(R.id.pointLable);
        actFlag = (ImageView) holder.getView(R.id.actFlag);
        goodsTitle = (TextView) holder.getView(R.id.goodsTitle);
        increaseDecrease = (IncreaseDecreaseView) holder.getView(R.id.increase_decrease);
        goodsSpec = (TextView) holder.getView(R.id.goodsSpec);
        goodsMoney = (TextView) holder.getView(R.id.goodsMoney);
        goodsCount = (TextView) holder.getView(R.id.goodsCount);

        final OrderList.OrderDetailListBean detailsBean = getDatas().get(position);
        increaseDecrease.setOnNumChangedListener(new IncreaseDecreaseView.OnNumChangedListener() {
            @Override
            public void onNumChanged(int num) {
                if (numChangedListener != null) {
                    numChangedListener.onNumChanged(detailsBean.orderDetailId, num);
                }
            }
        });
        increaseDecrease.setMaxCount(detailsBean.goodsQuantity);
        increaseDecrease.setMinCount(1);
        increaseDecrease.setNoCount(detailsBean.goodsQuantity);
        increaseDecrease.setCheckTextDialog(false);

        if (detailsBean.goodsPayPoints != null && !TextUtils.isEmpty(detailsBean.goodsPayPoints)
                && !"0".equals(FormatUtils.moneyKeep2Decimals(detailsBean.goodsPayPoints))) {//表示是积分退订
            pointLable.setVisibility(View.VISIBLE);
            pointLable.getBackground().setAlpha(150);//积分兑换的标签透明度
            if ("0".equals(FormatUtils.moneyKeep2Decimals(detailsBean.goodsPayAmount))) {
                goodsMoney.setText(FormatUtils.moneyKeep2Decimals(detailsBean.goodsPoints) + "积分");
            } else {
                goodsMoney.setText(FormatUtils.moneyKeep2Decimals(detailsBean.goodsPayPoints) + "积分\t+¥" + FormatUtils.moneyKeep2Decimals(detailsBean.goodsPayAmount) + "");
            }
        } else {
            actFlag.setVisibility(View.GONE);
            goodsMoney.setText("¥" + FormatUtils.moneyKeep2Decimals(detailsBean.goodsPayAmount));
        }
        goodsCount.setText("" + detailsBean.goodsQuantity);
        goodsTitle.setText(detailsBean.goodsTitle);
        goodsSpec.setText("");
        if (!TextUtils.isEmpty(detailsBean.goodsSpecDesc)) {
            goodsSpec.setText(detailsBean.goodsSpecDesc);
        }
        com.healthy.library.businessutil.GlideCopy.with(context).load(detailsBean.goodsImage)
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_1_1_default)

                .into(goodsImg);
    }

    public interface NumChangedListener {
        void onNumChanged(String id, int num);
    }
}
