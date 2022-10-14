package com.health.servicecenter.adapter;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.health.servicecenter.R;
import com.health.servicecenter.model.ConfirmOderModel;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.base.BaseMultiItemAdapter;
import com.healthy.library.model.OrderList;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.ImageTextView;
import com.umeng.analytics.MobclickAgent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OrderListAdapter extends BaseMultiItemAdapter<OrderList> {
    int forSize = 0;
    boolean isOpen = false;

    public OrderListAdapter() {//0   --> 待支付//1   --> 待发货//5   --> 待核销//6   --> 待确认收货//2   --> 已完成//3   --> 已取消//4   --> 已退款
        this(R.layout.item_order_list_good);//5积分订单  其他共用一个item
        //addItemType(1, R.layout.item_order_list_service);
        addItemType(0, R.layout.item_order_list_good);
        //addItemType(3, R.layout.item_order_list_combination);
        addItemType(5, R.layout.item_order_list_point);
    }

    public OrderListAdapter(int viewId) { super(viewId); }

    @Override
    public int getItemViewType(int position) {
        return getDefItemViewType(position);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, final int position) {
        TextView order_goods_pay_price = holder.getView(R.id.order_goods_pay_price);
        TextView order_goods_num_totle = holder.getView(R.id.order_goods_num_totle);
        TextView order_type_no_pay = holder.getView(R.id.order_type_no_pay);
        TextView order_type_cancel = holder.getView(R.id.order_type_cancel);
        TextView deliveryTime = holder.getView(R.id.deliveryTime);
        TextView red_btn = holder.getView(R.id.red_btn);
        TextView grey_btn = holder.getView(R.id.grey_btn);
        TextView subBack = holder.getView(R.id.subBack);
        TextView store_title = holder.getView(R.id.store_title);
        LinearLayout mall_service_liner = holder.getView(R.id.order_goods_list_linear);
        int goodsTotalNum = 0;//订单商品总数量
        final OrderList orderList = getDatas().get(position);
        int status = orderList.getOrderStatus();
        if (orderList.orderChild != null) {
            store_title.setText("订单编号：" + orderList.orderChild.orderNum);
        }
        if (orderList.orderFather != null) {
            store_title.setText("订单编号：" + orderList.orderFather.orderNum);
        }
        if (getItemViewType(position) == 0) {//商品订单
            final LinearLayout underviewll = holder.getView(R.id.underviewll);
            final ImageTextView underview = holder.getView(R.id.underview);
            if (status == 0 || status == 3) {
                if (orderList.orderFather != null) {
                    if (orderList.orderFather.subOrderList != null && orderList.orderFather.subOrderList.size() > 0) {
                        int goodsTotalSize = 0;
                        for (int i = 0; i < orderList.orderFather.subOrderList.size(); i++) {
                            goodsTotalSize += orderList.orderFather.subOrderList.get(i).orderDetailList.size();
                        }
                        goodsTotalNum = goodsTotalSize;
                        underviewll.setVisibility(View.GONE);
                        mall_service_liner.removeAllViews();
                        //设置主订单数据
                        buildMainOrderView(mall_service_liner, orderList);
                        buildMainStatus(order_type_no_pay, order_type_cancel, deliveryTime, red_btn, grey_btn, orderList);
                    }
                }
            } else {
                if (orderList.orderChild != null) {
                    if (orderList.orderChild.orderDetailList != null && orderList.orderChild.orderDetailList.size() > 0) {
                        goodsTotalNum = orderList.orderChild.orderDetailList.size();
                        mall_service_liner.removeAllViews();
                        //设置子订单数据
                        buildChildOrderView(mall_service_liner, orderList, underviewll, underview, position);
                        buildChildStatus(order_type_no_pay, order_type_cancel, deliveryTime, red_btn, grey_btn, orderList);
                    }
                }
            }
        } else if (getItemViewType(position) == 5) {//积分订单
            if (status == 0 || status == 3) {
                if (orderList.orderFather != null) {
                    if (orderList.orderFather.subOrderList != null && orderList.orderFather.subOrderList.size() > 0) {
                        mall_service_liner.removeAllViews();
                        goodsTotalNum = orderList.orderFather.subOrderList.size();
                        //设置积分主订单数据
                        buildPointsMainOrderView(mall_service_liner, orderList);
                        buildMainStatus(order_type_no_pay, order_type_cancel, deliveryTime, red_btn, grey_btn, orderList);
                    }
                }
            } else {
                if (orderList.orderChild != null) {
                    if (orderList.orderChild.orderDetailList != null && orderList.orderChild.orderDetailList.size() > 0) {
                        mall_service_liner.removeAllViews();
                        goodsTotalNum = orderList.orderChild.orderDetailList.size();
                        //设置积分子订单数据
                        buildPointsChildOrderView(mall_service_liner, orderList);
                        buildChildStatus(order_type_no_pay, order_type_cancel, deliveryTime, red_btn, grey_btn, orderList);
                    }
                }
            }
        }

        String strPrice = "";
        if (status == 0) {
            strPrice = "需付款";
        } else if (status == 3) {
            strPrice = "应付款";
        } else {
            strPrice = "实付款";
        }
        order_goods_num_totle.setText("共" + goodsTotalNum + "件");
        SpannableString total_price = new SpannableString(strPrice + " ¥" + FormatUtils.moneyKeep2Decimals(getDatas().get(position).totalPayAmount) + "");
        total_price.setSpan(new AbsoluteSizeSpan(13, true), 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        total_price.setSpan(new StyleSpan(Typeface.NORMAL), 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //正常
        total_price.setSpan(new StyleSpan(Typeface.BOLD), 3, total_price.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//加粗
        order_goods_pay_price.setText(total_price);
        if ("5".equals(getDatas().get(position).orderType)) {
            TextView order_goods_pay_point = holder.getView(R.id.order_goods_pay_point);
            order_goods_pay_point.setVisibility(View.VISIBLE);
            String strPoint = "实付积分";
            SpannableString total_point = new SpannableString(strPoint + FormatUtils.moneyKeep2Decimals(getDatas().get(position).totalPayPoints) + "");
            total_point.setSpan(new AbsoluteSizeSpan(13, true), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            total_point.setSpan(new StyleSpan(Typeface.NORMAL), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //正常
            total_point.setSpan(new StyleSpan(Typeface.BOLD), 4, total_point.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//加粗
            order_goods_pay_point.setText(total_point);
        }
        subBack.setVisibility(View.GONE);
        buildSubBack(status, orderList, subBack);//退订功能
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map nokmap = new HashMap<String, String>();
                nokmap.put("soure", "查看提货码");
                MobclickAgent.onEvent(context, "event2APPOrderLookTickClick", nokmap);
                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_ORDERLISTDETIAL)
                        .withString("orderId", orderList.orderId + "")
                        .withString("function", orderList.isMain == 0 ? "25007" : "25006")
                        .navigation();
            }
        });
    }

    private void buildSubBack(int status, final OrderList orderList, TextView subBack) {
        if (status == 1 || status == 5 || status == 6) {
            if (orderList.orderChild != null) {
                subBack.setVisibility(View.VISIBLE);
                if (orderList.orderChild.refundId != null) {//说明有退款成功的
                    subBack.setText("售后详情");
                    subBack.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ARouter.getInstance().
                                    build(ServiceRoutes.SERVICE_ORDERBACKDETIAL)
                                    .withString("refundId", orderList.orderChild.lastRefundId + "")
                                    .navigation();
                        }
                    });
                } else {
                    subBack.setText("申请售后");
                    subBack.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (isLatestWeek(orderList.paySuccessTime, new Date())) {
                                ARouter.getInstance().
                                        build(ServiceRoutes.SERVICE_ORDERBACKSELECTGOODS)
                                        .withString("orderId", orderList.orderId + "")
                                        .withString("function", "25007")
                                        .navigation();
                            } else {
                                if (moutClickListener != null) {
                                    moutClickListener.outClick("seven", orderList.orderId + "");
                                }
                            }
                        }
                    });
                }
            } else if (orderList.orderFather != null) {
                subBack.setVisibility(View.GONE);
            } else {
                subBack.setVisibility(View.GONE);
            }
        } else {
            subBack.setVisibility(View.GONE);
        }
        if(subBack.getText().toString().equals("申请售后")){
            if(orderList.orderChild!=null&&orderList.orderChild.canRefund==0){//不允许售后
                subBack.setVisibility(View.GONE);
            }
        }
    }

    private void buildPointsChildOrderView(LinearLayout mall_service_liner, final OrderList orderList) {
        for (int i = 0; i < orderList.orderChild.orderDetailList.size(); i++) {
            final OrderList.OrderDetailListBean detailsBean = orderList.orderChild.orderDetailList.get(i);
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
            pointLable.setVisibility(View.VISIBLE);
            pointLable.getBackground().setAlpha(150);//积分兑换的标签透明度
            mall_service_title.setText(detailsBean.goodsTitle);
            goodsSpace.setText(detailsBean.goodsSpecDesc);
            if ("0".equals(FormatUtils.moneyKeep2Decimals(detailsBean.goodsPayAmount))) {
                goodsMoney.setText(FormatUtils.moneyKeep2Decimals(detailsBean.goodsPoints) + "积分");
            } else {
                goodsMoney.setText(FormatUtils.moneyKeep2Decimals(detailsBean.goodsPoints) + "积分\n+¥" + FormatUtils.moneyKeep2Decimals(detailsBean.goodsPayAmount) + "");
            }
            goodsCount.setText("x" + detailsBean.goodsQuantity);
            com.healthy.library.businessutil.GlideCopy.with(context).load(detailsBean.goodsImage)
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_1_1_default)

                    .into(mall_service_img);
            view.setLayoutParams(layoutParams);
            mall_service_liner.addView(view);
        }
    }

    private void buildChildStatus(TextView order_type_no_pay, TextView order_type_cancel, TextView deliveryTime, TextView red_btn,
                                  TextView grey_btn, final OrderList orderList) {
        if (orderList.getOrderStatus() == 1) {//已支付
            order_type_no_pay.setVisibility(View.VISIBLE);
            order_type_cancel.setVisibility(View.GONE);
            red_btn.setVisibility(View.GONE);
            grey_btn.setVisibility(View.VISIBLE);
            if (orderList.orderChild.getDeliverType() == 10 || orderList.orderChild.getDeliverType() == 20) {
                deliveryTime.setVisibility(View.GONE);
                order_type_no_pay.setText("待核销");
//                if (orderList.orderChild.deliveryDate != null && !TextUtils.isEmpty(orderList.orderChild.deliveryDate)) {
//                    deliveryTime.setText("提货时间：" + orderList.orderChild.deliveryDate.split(" ")[0] + orderList.orderChild.deliveryTime);
//                } else {
//                    deliveryTime.setText("");
//                }
            } else if (orderList.orderChild.getDeliverType() == 11) {
                deliveryTime.setVisibility(View.GONE);
                order_type_no_pay.setText("待发货");
            }
            grey_btn.setText("查看核销码");
            grey_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map nokmap = new HashMap<String, String>();
                    nokmap.put("soure", "查看提货码");
                    MobclickAgent.onEvent(context, "event2APPOrderLookTickClick", nokmap);
                    ARouter.getInstance()
                            .build(ServiceRoutes.SERVICE_ORDERLISTDETIAL)
                            .withString("orderId", orderList.orderId + "")
                            .withString("function", orderList.isMain == 0 ? "25007" : "25006")
                            .navigation();
                }
            });
        } else if (orderList.getOrderStatus() == 2) {//已完成
            order_type_no_pay.setVisibility(View.GONE);
            order_type_cancel.setVisibility(View.VISIBLE);
            deliveryTime.setVisibility(View.GONE);
            grey_btn.setVisibility(View.GONE);
            red_btn.setVisibility(View.GONE);
            order_type_cancel.setText("已完成");
        } else if (orderList.getOrderStatus() == 4) {//已退货退款
            order_type_no_pay.setVisibility(View.GONE);
            deliveryTime.setVisibility(View.GONE);
            red_btn.setVisibility(View.GONE);
            order_type_cancel.setVisibility(View.VISIBLE);
            grey_btn.setVisibility(View.GONE);
            order_type_cancel.setText("已退款");
        } else if (orderList.getOrderStatus() == 5) {
            order_type_no_pay.setVisibility(View.VISIBLE);
            order_type_cancel.setVisibility(View.GONE);
            red_btn.setVisibility(View.GONE);
            grey_btn.setVisibility(View.VISIBLE);
            deliveryTime.setVisibility(View.GONE);
            order_type_no_pay.setText("待核销");
            grey_btn.setText("查看核销码");
            grey_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map nokmap = new HashMap<String, String>();
                    nokmap.put("soure", "查看提货码");
                    MobclickAgent.onEvent(context, "event2APPOrderLookTickClick", nokmap);
                    ARouter.getInstance()
                            .build(ServiceRoutes.SERVICE_ORDERLISTDETIAL)
                            .withString("orderId", orderList.orderId + "")
                            .withString("function", orderList.isMain == 0 ? "25007" : "25006")
                            .navigation();
                }
            });
        } else if (orderList.getOrderStatus() == 6) {
            order_type_no_pay.setVisibility(View.GONE);
            order_type_cancel.setVisibility(View.GONE);
            red_btn.setVisibility(View.VISIBLE);
            red_btn.setVisibility(View.VISIBLE);
            grey_btn.setVisibility(View.GONE);
            deliveryTime.setVisibility(View.GONE);
            order_type_no_pay.setText("待确认收货");
            red_btn.setText("确认收货");
            red_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (moutClickListener != null) {
                        moutClickListener.outClick("confirmOder", new ConfirmOderModel(orderList.orderChild.ticket, orderList.orderChild.orderDetailList.get(0).subOrderId));
                    }
                }
            });
        }
    }

    private void buildPointsMainOrderView(LinearLayout mall_service_liner, final OrderList orderList) {
        for (int i = 0; i < orderList.orderFather.subOrderList.size(); i++) {
            final OrderList.OrderDetailListBean detailsBean = orderList.orderFather.subOrderList.get(i).orderDetailList.get(0);
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
            pointLable.setVisibility(View.VISIBLE);
            pointLable.getBackground().setAlpha(150);//积分兑换的标签透明度
            mall_service_title.setText(detailsBean.goodsTitle);
            goodsSpace.setText(detailsBean.goodsSpecDesc);
            if ("0".equals(FormatUtils.moneyKeep2Decimals(detailsBean.goodsPayAmount))) {
                goodsMoney.setText(FormatUtils.moneyKeep2Decimals(detailsBean.goodsPoints) + "积分");
            } else {
                goodsMoney.setText(FormatUtils.moneyKeep2Decimals(detailsBean.goodsPoints) + "积分\n+¥" + FormatUtils.moneyKeep2Decimals(detailsBean.goodsPayAmount) + "");
            }
            goodsCount.setText("x" + detailsBean.goodsQuantity);
            com.healthy.library.businessutil.GlideCopy.with(context).load(detailsBean.goodsImage)
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_1_1_default)

                    .into(mall_service_img);
            view.setLayoutParams(layoutParams);
            mall_service_liner.addView(view);
        }
    }

    private void buildChildOrderView(LinearLayout mall_service_liner, final OrderList orderList, LinearLayout underviewll, final ImageTextView underview, final int position) {
        if (forSize == 0) {
            forSize = orderList.orderChild.orderDetailList.size();
        }
        if (orderList.orderChild.orderDetailList.size() > 3) {
            underviewll.setVisibility(View.VISIBLE);
            if (!isOpen) {
                forSize = 3;
                underview.setText("查看剩余" + (orderList.orderChild.orderDetailList.size() - forSize) + "件商品");
                underview.setDrawable(R.drawable.goods_arrow_down, context);
            } else {
                forSize = orderList.orderChild.orderDetailList.size();
                underview.setText("收起");
                underview.setDrawable(R.drawable.goods_arrow_up, context);
            }
        } else {
            forSize = orderList.orderChild.orderDetailList.size();
            underviewll.setVisibility(View.GONE);

        }
        for (int i = 0; i < forSize; i++) {
            final OrderList.OrderDetailListBean detailsBean = orderList.orderChild.orderDetailList.get(i);
            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout
                    .LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 10, 0, 0);
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.item_order_flow_goods, null);
            TextView mall_service_title = view.findViewById(R.id.goodsTitle);
            TextView goodsSpace = view.findViewById(R.id.goodsSpace);
            TextView goodsCount = view.findViewById(R.id.goodsCount);
            TextView goodsMoney = view.findViewById(R.id.goodsMoney);
            CornerImageView mall_service_img = view.findViewById(R.id.goodsImg);
            mall_service_title.setText(detailsBean.goodsTitle);
            goodsSpace.setText(detailsBean.goodsSpecDesc);
            goodsMoney.setText("¥" + FormatUtils.moneyKeep2Decimals(detailsBean.goodsAmount) + "");
            goodsCount.setText("x" + detailsBean.goodsQuantity);
            com.healthy.library.businessutil.GlideCopy.with(context).load(detailsBean.goodsImage)
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_1_1_default)

                    .into(mall_service_img);
            ImageView actFlag = view.findViewById(R.id.actFlag);
            actFlag.setVisibility(View.GONE);
            if (orderList.orderChild.orderType != null && "0".equals(orderList.orderChild.orderType)) {//普通
                actFlag.setVisibility(View.GONE);
            } else if (orderList.orderChild.orderType != null && "1".equals(orderList.orderChild.orderType)) {//砍价
                actFlag.setVisibility(View.VISIBLE);
                actFlag.setImageResource(R.drawable.act_kick);
            } else if (orderList.orderChild.orderType != null && "4".equals(orderList.orderChild.orderType)) {//拼团
                actFlag.setVisibility(View.VISIBLE);
                actFlag.setImageResource(R.drawable.act_pin);
            }
            view.setLayoutParams(layoutParams);
            mall_service_liner.addView(view);
        }
        underviewll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isOpen) {
                    forSize = orderList.orderChild.orderDetailList.size();
                    underview.setText("收起");
                    underview.setDrawable(R.drawable.goods_arrow_up, context);
                    isOpen = true;
                } else {
                    forSize = 3;
                    underview.setText("查看剩余" + (orderList.orderChild.orderDetailList.size() - forSize) + "件商品");
                    underview.setDrawable(R.drawable.goods_arrow_down, context);
                    isOpen = false;
                }
                notifyItemChanged(position);
            }
        });
    }

    private void buildMainOrderView(LinearLayout mall_service_liner, final OrderList orderList) {
        for (int i = 0; i < orderList.orderFather.subOrderList.size(); i++) {
            for (int j = 0; j < orderList.orderFather.subOrderList.get(i).orderDetailList.size(); j++) {
                final OrderList.OrderDetailListBean detailsBean = orderList.orderFather.subOrderList.get(i).orderDetailList.get(j);
                ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout
                        .LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 10, 0, 0);
                LayoutInflater inflater = LayoutInflater.from(context);
                View view = inflater.inflate(R.layout.item_order_flow_goods, null);
                TextView mall_service_title = view.findViewById(R.id.goodsTitle);
                TextView goodsSpace = view.findViewById(R.id.goodsSpace);
                TextView goodsCount = view.findViewById(R.id.goodsCount);
                TextView goodsMoney = view.findViewById(R.id.goodsMoney);
                CornerImageView mall_service_img = view.findViewById(R.id.goodsImg);
                mall_service_title.setText(detailsBean.goodsTitle);
                goodsSpace.setText(detailsBean.goodsSpecDesc);
                goodsMoney.setText("¥" + FormatUtils.moneyKeep2Decimals(detailsBean.goodsAmount) + "");
                goodsCount.setText("x" + detailsBean.goodsQuantity);
                com.healthy.library.businessutil.GlideCopy.with(context).load(detailsBean.goodsImage)
                        .placeholder(R.drawable.img_1_1_default2)
                        .error(R.drawable.img_1_1_default)

                        .into(mall_service_img);
                ImageView actFlag = view.findViewById(R.id.actFlag);
                actFlag.setVisibility(View.GONE);
                if (orderList.orderFather.orderType != null && "0".equals(orderList.orderFather.orderType)) {//普通
                    actFlag.setVisibility(View.GONE);
                } else if (orderList.orderFather.orderType != null && "1".equals(orderList.orderFather.orderType)) {//砍价
                    actFlag.setVisibility(View.VISIBLE);
                    actFlag.setImageResource(R.drawable.act_kick);
                } else if (orderList.orderFather.orderType != null && "4".equals(orderList.orderFather.orderType)) {//拼团
                    actFlag.setVisibility(View.VISIBLE);
                    actFlag.setImageResource(R.drawable.act_pin);
                }
                view.setLayoutParams(layoutParams);
                mall_service_liner.addView(view);
            }
        }
    }

    private void buildMainStatus(TextView order_type_no_pay, TextView order_type_cancel, TextView deliveryTime, TextView red_btn, TextView grey_btn, final OrderList orderList) {
        if (orderList.getOrderStatus() == 0) {//未支付
            order_type_no_pay.setVisibility(View.VISIBLE);
            order_type_cancel.setVisibility(View.GONE);
            grey_btn.setVisibility(View.GONE);
            deliveryTime.setVisibility(View.GONE);
            order_type_no_pay.setText("待支付");
            red_btn.setText("去支付");
            red_btn.setTextColor(Color.parseColor("#FFFFFF"));
            red_btn.setBackgroundResource(R.drawable.shape_order_to_pay_btn);
            red_btn.setVisibility(View.VISIBLE);
            red_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map nokmap = new HashMap<String, String>();
                    nokmap.put("soure", "订单列表");
                    MobclickAgent.onEvent(context, "event2APPOrderListClick", nokmap);
                    ARouter.getInstance()
                            .build(ServiceRoutes.SERVICE_CHECKOUT_COUNTER)
                            .withString("orderId", orderList.orderId + "")
                            .withString("orderType", orderList.getOrderRace() == 5 ? "-1" : orderList.getItemType() + "")
                            .navigation();

                }
            });
        } else if (orderList.getOrderStatus() == 3) {//已取消
            order_type_no_pay.setVisibility(View.GONE);
            red_btn.setVisibility(View.GONE);
            order_type_cancel.setVisibility(View.VISIBLE);
            grey_btn.setVisibility(View.VISIBLE);
            deliveryTime.setVisibility(View.GONE);
            order_type_cancel.setText("已取消");
            grey_btn.setText("删除订单");
            grey_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (moutClickListener != null) {
                        moutClickListener.outClick("delete", orderList.orderId + "");
                    }
                }
            });
        }
    }

    public boolean isLatestWeek(String orderTime, Date now) {
        if (orderTime == null) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date fromDate = null;
        try {
            fromDate = sdf.parse(orderTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (fromDate == null) {
            return false;
        }
        Calendar calendar = Calendar.getInstance();  //得到日历
        calendar.setTime(now);//把当前时间赋给日历
        calendar.add(Calendar.DAY_OF_MONTH, -7);  //设置为7天前
        Date before7days = calendar.getTime();   //得到7天前的时间
        if (before7days.getTime() < fromDate.getTime()) {
            return true;
        } else {
            return false;
        }
    }

}
