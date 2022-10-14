package com.health.servicecenter.adapter;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.health.servicecenter.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.model.BackListModel;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.DrawableTextView;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

public class OrderBackListAdapter extends BaseAdapter<BackListModel> {
    public OrderBackListAdapter() {
        this(R.layout.item_order_back_list);
    }

    public OrderBackListAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, final int position) {
        final DrawableTextView tv_store_name = holder.itemView.findViewById(R.id.tv_store_name);
        final TextView tv_order_status_black = holder.itemView.findViewById(R.id.tv_order_status_black);
        final TextView tv_order_status_red = holder.itemView.findViewById(R.id.tv_order_status_red);
        final TextView tv_order_status_grey = holder.itemView.findViewById(R.id.tv_order_status_grey);
        final TextView tv_total_price = holder.itemView.findViewById(R.id.tv_total_price);
        final TextView tv_total_num = holder.itemView.findViewById(R.id.tv_total_num);
        final TextView tv_detial = holder.itemView.findViewById(R.id.tv_detial);
        final TextView tv_cancel = holder.itemView.findViewById(R.id.tv_cancel);
        final TextView tv_delete = holder.itemView.findViewById(R.id.tv_delete);
        final LinearLayout goodsLiner = holder.itemView.findViewById(R.id.goodsLiner);
        final BackListModel backListModel = getDatas().get(position);
        int refundQuantity = 0;
        switch (backListModel.status) {
            case 0:
                tv_order_status_black.setVisibility(View.GONE);
                tv_order_status_grey.setVisibility(View.GONE);
                tv_order_status_red.setVisibility(View.VISIBLE);
                tv_delete.setVisibility(View.GONE);
                tv_cancel.setVisibility(View.VISIBLE);
                tv_order_status_red.setText("待商家处理");
                break;
            case 1:
                tv_order_status_black.setVisibility(View.VISIBLE);
                tv_order_status_grey.setVisibility(View.GONE);
                tv_order_status_red.setVisibility(View.GONE);
                tv_delete.setVisibility(View.GONE);
                tv_cancel.setVisibility(View.GONE);
                tv_order_status_black.setText("退款中");
                break;
            case 2:
                tv_order_status_black.setVisibility(View.VISIBLE);
                tv_order_status_grey.setVisibility(View.GONE);
                tv_order_status_red.setVisibility(View.GONE);
                tv_delete.setVisibility(View.GONE);
                tv_cancel.setVisibility(View.GONE);
                tv_order_status_black.setText("退款成功");
                break;
            case 3:
                tv_order_status_black.setVisibility(View.GONE);
                tv_order_status_grey.setVisibility(View.GONE);
                tv_order_status_red.setVisibility(View.VISIBLE);
                tv_delete.setVisibility(View.GONE);
                tv_cancel.setVisibility(View.GONE);
                tv_order_status_red.setText("商家拒绝");
                break;
            case -1:
                tv_order_status_black.setVisibility(View.GONE);
                tv_order_status_grey.setVisibility(View.VISIBLE);
                tv_order_status_red.setVisibility(View.GONE);
                tv_detial.setVisibility(View.VISIBLE);
                tv_delete.setVisibility(View.VISIBLE);
                tv_cancel.setVisibility(View.GONE);
                tv_order_status_grey.setText("申请已撤销");
                break;
            case -2:
                tv_order_status_black.setVisibility(View.GONE);
                tv_order_status_grey.setVisibility(View.GONE);
                tv_order_status_red.setVisibility(View.VISIBLE);
                tv_order_status_red.setText("退款失败");
                tv_detial.setVisibility(View.VISIBLE);
                tv_delete.setVisibility(View.GONE);
                tv_cancel.setVisibility(View.GONE);
                break;
        }
        tv_detial.setVisibility(View.VISIBLE);
        tv_store_name.setText(backListModel.createTime);

        //绘制商品及底部金额
        buildGoods(backListModel, goodsLiner);
        buildBottom(backListModel, tv_total_price, tv_total_num, refundQuantity);

        tv_detial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map nokmap = new HashMap<String, String>();
                nokmap.put("soure", "退款售后列表【售后详情】点击");
                MobclickAgent.onEvent(context, "btn_APP_MaternalandChildGoods_MyOrder_AfterSalesDetail", nokmap);
                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_ORDERBACKDETIAL)
                        .withString("refundId", backListModel.refundId + "")
                        .navigation();
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map nokmap = new HashMap<String, String>();
                nokmap.put("soure", "退款售后列表【售后详情】点击");
                MobclickAgent.onEvent(context, "btn_APP_MaternalandChildGoods_MyOrder_AfterSalesDetail", nokmap);
                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_ORDERBACKDETIAL)
                        .withString("refundId", backListModel.refundId + "")
                        .navigation();
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moutClickListener != null) {
                    moutClickListener.outClick("revoke", backListModel.refundId + "");
                }
            }
        });
        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moutClickListener != null) {
                    moutClickListener.outClick("delete", backListModel.refundId + "");
                }
            }
        });
    }

    private void buildGoods(BackListModel backListModel, LinearLayout goodsLiner) {
        goodsLiner.removeAllViews();
        if (backListModel.refundDetailList != null && backListModel.refundDetailList.size() > 0) {
            for (int i = 0; i < backListModel.refundDetailList.size(); i++) {
                BackListModel.RefundDetailListBean detailsBean = backListModel.refundDetailList.get(i);
                ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout
                        .LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 10, 0, 0);
                LayoutInflater inflater = LayoutInflater.from(context);
                View view = inflater.inflate(R.layout.item_order_flow_goods, null);
                TextView mall_service_title = view.findViewById(R.id.goodsTitle);
                TextView goodsSpace = view.findViewById(R.id.goodsSpace);
                TextView goodsCount = view.findViewById(R.id.goodsCount);
                TextView goodsMoney = view.findViewById(R.id.goodsMoney);
                TextView pointLable = view.findViewById(R.id.pointLable);
                CornerImageView mall_service_img = view.findViewById(R.id.goodsImg);
                mall_service_title.setText(detailsBean.goodsTitle);
                goodsSpace.setText(detailsBean.goodsSpecDesc);
                if (detailsBean.goodsPayPoints != null && !TextUtils.isEmpty(detailsBean.goodsPayPoints)
                        && !"0".equals(FormatUtils.moneyKeep2Decimals(detailsBean.goodsPayPoints))) {//表示是积分退订
                    pointLable.setVisibility(View.VISIBLE);
                    pointLable.getBackground().setAlpha(150);//积分兑换的标签透明度
                    if ("0".equals(FormatUtils.moneyKeep2Decimals(detailsBean.goodsPayAmount))) {
                        goodsMoney.setText(FormatUtils.moneyKeep2Decimals(detailsBean.goodsPayPoints) + "积分");
                    } else {
                        goodsMoney.setText(FormatUtils.moneyKeep2Decimals(detailsBean.goodsPayPoints) + "积分+\n¥" + FormatUtils.moneyKeep2Decimals(detailsBean.goodsPayAmount) + "");
                    }
                } else {
                    pointLable.setVisibility(View.GONE);
                    goodsMoney.setText("¥" + FormatUtils.moneyKeep2Decimals(detailsBean.goodsPayAmount));
                }
                goodsCount.setText("x" + detailsBean.refundQuantity);
                com.healthy.library.businessutil.GlideCopy.with(context).load(detailsBean.goodsImage)
                        .placeholder(R.drawable.img_1_1_default2)
                        .error(R.drawable.img_1_1_default)

                        .into(mall_service_img);
                goodsLiner.addView(view);
            }
        }
    }

    private void buildBottom(BackListModel backListModel, TextView tv_total_price, TextView tv_total_num, int refundQuantity) {
        for (int i = 0; i < backListModel.refundDetailList.size(); i++) {
            refundQuantity = refundQuantity + backListModel.refundDetailList.get(i).refundQuantity;
        }
        tv_total_price.setText("");
        if (backListModel.refundPoints != null && !TextUtils.isEmpty(backListModel.refundPoints)
                && !"0".equals(FormatUtils.moneyKeep2Decimals(backListModel.refundPoints))) {//表示是积分退订
            SpannableString total_price;
            if ("0".equals(FormatUtils.moneyKeep2Decimals(backListModel.refundAmount))) {
                total_price = new SpannableString("退款金额" + FormatUtils.moneyKeep2Decimals(backListModel.refundPoints)
                        + "积分");
            } else {
                total_price = new SpannableString("退款金额" + FormatUtils.moneyKeep2Decimals(backListModel.refundPoints)
                        + "积分+¥" + FormatUtils.moneyKeep2Decimals(backListModel.refundAmount));
            }
            total_price.setSpan(new AbsoluteSizeSpan(13, true), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            total_price.setSpan(new StyleSpan(Typeface.NORMAL), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //正常
            total_price.setSpan(new StyleSpan(Typeface.BOLD), 4, total_price.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv_total_price.setText(total_price);
        } else {
            SpannableString total_price = new SpannableString("退款金额 ¥" + FormatUtils.moneyKeep2Decimals(backListModel.refundAmount));
            total_price.setSpan(new AbsoluteSizeSpan(13, true), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            total_price.setSpan(new ForegroundColorSpan(Color.parseColor("#222222")), 5, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            total_price.setSpan(new StyleSpan(Typeface.NORMAL), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //正常
            total_price.setSpan(new StyleSpan(Typeface.BOLD), 5, total_price.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv_total_price.setText(total_price);
        }
        SpannableString total_num = new SpannableString("退订数量 " + refundQuantity);
        total_num.setSpan(new ForegroundColorSpan(Color.parseColor("#222222")), 5, total_num.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        total_num.setSpan(new StyleSpan(Typeface.NORMAL), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //正常
        total_num.setSpan(new StyleSpan(Typeface.BOLD), 5, total_num.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_total_num.setText(total_num);
    }
}

