//package com.health.servicecenter.adapter;
//
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.constraintlayout.widget.ConstraintLayout;
//
//import com.alibaba.android.arouter.launcher.ARouter;
//import com.alibaba.android.vlayout.LayoutHelper;
//import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
//import com.bumptech.glide.Glide;
//import com.health.servicecenter.R;
//import com.healthy.library.base.BaseHolder;
//import com.healthy.library.base.BaseMultiItemAdapter;
//import com.healthy.library.model.OrderList;
//import com.healthy.library.model.SubmitBackModel;
//import com.healthy.library.routes.ServiceRoutes;
//import com.healthy.library.utils.FormatUtils;
//import com.healthy.library.widget.CornerImageView;
//import com.umeng.analytics.MobclickAgent;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class OrderDetialGoodsAdapter extends BaseMultiItemAdapter<OrderList> {
//    public OrderDetialGoodsAdapter() {
//        this(R.layout.item_order_detial_goods);
//        addItemType(0, R.layout.item_order_detial_goods);//标品订单
//        //addItemType(3, R.layout.item_order_detial_combination);//组合套餐
//        addItemType(4, R.layout.item_order_detial_point);//积分单
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull BaseHolder holder, final int position) {
//        if (getItemViewType(position) == 0) {
//            LinearLayout order_detial_goods_liner = holder.getView(R.id.order_detial_goods_liner);
//            LinearLayout detial_goods_combination = holder.getView(R.id.detial_goods_combination);
//            LinearLayout order_detial_service_liner = holder.getView(R.id.order_detial_service_liner);
//            ConstraintLayout goods_num_constraintLayout = holder.getView(R.id.goods_num_constraintLayout);
//            final OrderList.OrderChild orderChild = getModel().orderChild;
//            final OrderList.OrderFather orderFather = getModel().orderFather;
//            order_detial_service_liner.setVisibility(View.GONE);
//            goods_num_constraintLayout.setVisibility(View.GONE);
//            order_detial_goods_liner.removeAllViews();
//            if (orderChild == null && orderFather != null) {//主订单.
//                if (orderFather.subOrderList != null && orderFather.subOrderList.size() > 0) {
//                    if (orderFather.subOrderList.get(0).orderDetailList == null || orderFather.subOrderList.get(0).orderDetailList.size() == 0) {
//                        return;
//                    }
//                    for (int i = 0; i < orderFather.subOrderList.get(0).orderDetailList.size(); i++) {
//                        final OrderList.OrderDetailListBean subList = orderFather.subOrderList.get(0).orderDetailList.get(i);
//                        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout
//                                .LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
//                        layoutParams.setMargins(0, 10, 0, 0);
//                        LayoutInflater inflater = LayoutInflater.from(context);
//                        View view = inflater.inflate(R.layout.service_item_order_flow_goods, null);
//                        TextView mall_service_title = view.findViewById(R.id.goodsTitle);
//                        ConstraintLayout goods_finish_btn_constraintLayout = view.findViewById(R.id.goods_finish_btn_constraintLayout);
//                        TextView btn_commitBack = view.findViewById(R.id.btn_commitBack);
//                        TextView btn_assess = view.findViewById(R.id.btn_assess);
//                        TextView goodsSpace = view.findViewById(R.id.goodsSpec);
//                        TextView goodsCount = view.findViewById(R.id.goodsCount);
//                        TextView goodsMoney = view.findViewById(R.id.goodsMoney);
//                        ImageView actFlag = view.findViewById(R.id.actFlag);
//                        CornerImageView mall_service_img = view.findViewById(R.id.goodsImg);
//                        actFlag.setVisibility(View.GONE);
//                        btn_assess.setVisibility(View.GONE);
//                        if (orderFather.subOrderList.get(0).getOrderStatus() == 1 || orderFather.subOrderList.get(0).getOrderStatus() == 2 || orderFather.subOrderList.get(0).getOrderStatus() == 4 || orderFather.subOrderList.get(0).getOrderStatus() == 5) {
//                            if (orderFather.subOrderList.get(0).getOrderType() == 1 || orderFather.subOrderList.get(0).getOrderType() == 4) {
//                                btn_commitBack.setVisibility(View.GONE);
//                            } else {
//                                goods_finish_btn_constraintLayout.setVisibility(View.VISIBLE);
//                                btn_commitBack.setVisibility(View.VISIBLE);
//                                if ("1".equals(subList.allowRefund)) {
//                                    btn_commitBack.setText("申请售后");
//                                } else {
//                                    btn_commitBack.setText("售后详情");
//                                }
//                            }
//
//                        } else {
//                            btn_commitBack.setVisibility(View.GONE);
//                        }
//                        goodsMoney.setText("¥" + FormatUtils.moneyKeep2Decimals(subList.goodsPayAmount));
//                        goodsCount.setText("x" + subList.goodsQuantity);
//                        mall_service_title.setText(subList.goodsTitle);
//                        goodsSpace.setText("");
//                        if (!TextUtils.isEmpty(subList.goodsSpecDesc)) {
//                            goodsSpace.setText(subList.goodsSpecDesc);
//                        }
//                        //goodsMoney.setText("￥" + FormatUtils.moneyKeep2Decimals(detailsBean.getGoodsPayAmount() ));
//                        //goodsMoney.setText("￥" + detailsBean.getPlatformPrice() + "");
//                        com.healthy.library.businessutil.GlideCopy.with(context).load(subList.goodsImage)
//                                .placeholder(R.drawable.img_1_1_default2)
//                                .error(R.drawable.img_1_1_default)
//
//                                .into(mall_service_img);
//                        if (getModel().getOrderStatus() == 1) {
//                            goods_finish_btn_constraintLayout.setVisibility(View.VISIBLE);
//                        }
//                        btn_commitBack.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//                                if ("1".equals(subList.allowRefund)) {
//                                    Map nokmap = new HashMap<String, String>();
//                                    nokmap.put("soure", "订单详情页【申请售后】点击");
//                                    MobclickAgent.onEvent(context, "btn_APP_MaternalandChildGoods_MyOrder_AfterSalesApply", nokmap);
//                                    ARouter.getInstance().
//                                            build(ServiceRoutes.SERVICE_COMITBACK)
//                                            .withString("type", "2")
//                                            .navigation();
//                                } else {
//                                    ARouter.getInstance().
//                                            build(ServiceRoutes.SERVICE_ORDERBACKDETIAL)
//                                            .withString("refundId", subList.latestRefundId)
//                                            .navigation();
//                                }
//                            }
//                        });
//                        mall_service_img.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Map nokmap = new HashMap<String, String>();
//                                nokmap.put("soure", "订单详情页点击商品栏");
//                                MobclickAgent.onEvent(context, "btn_APP__MaternalandChildGoods_CommodityDetails", nokmap);
//                                ARouter.getInstance().
//                                        build(ServiceRoutes.SERVICE_DETAIL)
//                                        .withString("id", subList.goodsId + "")
//                                        .withString("shopId",orderFather.shopId)
//                                        .navigation();
//                            }
//                        });
//                        mall_service_title.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Map nokmap = new HashMap<String, String>();
//                                nokmap.put("soure", "订单详情页点击商品栏");
//                                MobclickAgent.onEvent(context, "btn_APP__MaternalandChildGoods_CommodityDetails", nokmap);
//                                ARouter.getInstance().
//                                        build(ServiceRoutes.SERVICE_DETAIL)
//                                        .withString("id", subList.goodsId + "")
//                                        .withString("shopId",orderFather.shopId)
//                                        .navigation();
//                            }
//                        });
//                        view.setLayoutParams(layoutParams);
//                        order_detial_goods_liner.addView(view);
//
//                    }
//                }
//            } else if (orderChild != null && orderFather == null) {//子订单
//                for (int i = 0; i < orderChild.orderDetailList.size(); i++) {
//                    final OrderList.OrderDetailListBean detailsBean = orderChild.orderDetailList.get(i);
//                    ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout
//                            .LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
//                    layoutParams.setMargins(0, 10, 0, 0);
//                    LayoutInflater inflater = LayoutInflater.from(context);
//                    View view = inflater.inflate(R.layout.service_item_order_flow_goods, null);
//                    TextView mall_service_title = view.findViewById(R.id.goodsTitle);
//                    ConstraintLayout goods_finish_btn_constraintLayout = view.findViewById(R.id.goods_finish_btn_constraintLayout);
//                    TextView btn_commitBack = view.findViewById(R.id.btn_commitBack);
//                    TextView btn_assess = view.findViewById(R.id.btn_assess);
//                    TextView goodsSpace = view.findViewById(R.id.goodsSpec);
//                    TextView goodsCount = view.findViewById(R.id.goodsCount);
//                    TextView goodsMoney = view.findViewById(R.id.goodsMoney);
//                    ImageView actFlag = view.findViewById(R.id.actFlag);
//                    CornerImageView mall_service_img = view.findViewById(R.id.goodsImg);
//                    actFlag.setVisibility(View.GONE);
//                    btn_assess.setVisibility(View.GONE);
//                    if (orderChild.getOrderStatus() == 1 || orderChild.getOrderStatus() == 2 || orderChild.getOrderStatus() == 4 || orderChild.getOrderStatus() == 5) {
//                        if (orderChild.getOrderType() == 1 || orderChild.getOrderType() == 4) {
//                            btn_commitBack.setVisibility(View.GONE);
//                        } else {
//                            goods_finish_btn_constraintLayout.setVisibility(View.VISIBLE);
//                            btn_commitBack.setVisibility(View.VISIBLE);
//                            if ("1".equals(detailsBean.allowRefund)) {
//                                btn_commitBack.setText("申请售后");
//                            } else {
//                                btn_commitBack.setText("售后详情");
//                            }
//                        }
//
//                    } else {
//                        btn_commitBack.setVisibility(View.GONE);
//                    }
//                    goodsMoney.setText("¥" + FormatUtils.moneyKeep2Decimals(detailsBean.goodsPayAmount));
//                    goodsCount.setText("x" + detailsBean.goodsQuantity);
//                    mall_service_title.setText(detailsBean.goodsTitle);
//                    goodsSpace.setText("");
//                    if (!TextUtils.isEmpty(detailsBean.goodsSpecDesc)) {
//                        goodsSpace.setText(detailsBean.goodsSpecDesc);
//                    }
////                goodsMoney.setText("￥" + FormatUtils.moneyKeep2Decimals(detailsBean.getGoodsPayAmount() ));
//                    //goodsMoney.setText("￥" + detailsBean.getPlatformPrice() + "");
//                    com.healthy.library.businessutil.GlideCopy.with(context).load(detailsBean.goodsImage)
//                            .placeholder(R.drawable.img_1_1_default2)
//                            .error(R.drawable.img_1_1_default)
//
//                            .into(mall_service_img);
//                    if (getModel().getOrderStatus() == 1) {
//                        goods_finish_btn_constraintLayout.setVisibility(View.VISIBLE);
//                    }
//                    mall_service_img.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Map nokmap = new HashMap<String, String>();
//                            nokmap.put("soure", "订单详情页点击商品栏");
//                            MobclickAgent.onEvent(context, "btn_APP__MaternalandChildGoods_CommodityDetails", nokmap);
//                            ARouter.getInstance().
//                                    build(ServiceRoutes.SERVICE_DETAIL)
//                                    .withString("id", detailsBean.goodsId + "")
//                                    .withString("shopId",orderFather.shopId)
//                                    .navigation();
//                        }
//                    });
//                    mall_service_title.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Map nokmap = new HashMap<String, String>();
//                            nokmap.put("soure", "订单详情页点击商品栏");
//                            MobclickAgent.onEvent(context, "btn_APP__MaternalandChildGoods_CommodityDetails", nokmap);
//                            ARouter.getInstance().
//                                    build(ServiceRoutes.SERVICE_DETAIL)
//                                    .withString("id", detailsBean.goodsId + "")
//                                    .withString("shopId",orderFather.shopId)
//                                    .navigation();
//                        }
//                    });
//                    view.setLayoutParams(layoutParams);
//                    order_detial_goods_liner.addView(view);
//
//                }
//
//            }
//
//        } else if (getItemViewType(position) == 3) {
////            LinearLayout order_detial_goods_liner = holder.getView(R.id.order_detial_goods_liner);
////            LinearLayout detial_goods_combination = holder.getView(R.id.detial_goods_combination);
////            LinearLayout order_detial_service_liner = holder.getView(R.id.order_detial_service_liner);
////            ConstraintLayout goods_num_constraintLayout = holder.getView(R.id.goods_num_constraintLayout);
////            ConstraintLayout order_detial_goods_cardview = holder.getView(R.id.order_detial_goods_cardview);
////            View goods_red_view = holder.getView(R.id.goods_red_view);
////            if (getModel().getOrderRace() == 3) {
////                int orderGoodsNum = 0;
////                for (int i = 0; i < getModel().getSubList().size(); i++) {
////                    if (getModel().getSubList().get(i).getOrderRace() == 2) {
////                        orderGoodsNum++;
////                    }
////                }
////                if (orderGoodsNum > 0) {
////                    goods_red_view.setVisibility(View.GONE);
////                    order_detial_goods_cardview.setVisibility(View.VISIBLE);
////                } else {
////                    goods_red_view.setVisibility(View.VISIBLE);
////                    order_detial_goods_cardview.setVisibility(View.GONE);
////                }
////                goods_num_constraintLayout.setVisibility(View.VISIBLE);
////                order_detial_service_liner.setVisibility(View.VISIBLE);
////                TextView goods_order_num = holder.getView(R.id.goods_order_num);
////                TextView order_type_store_goods_status = holder.getView(R.id.order_type_store_goods_status);
////                order_detial_goods_liner.removeAllViews();
////                order_detial_service_liner.removeAllViews();
////                final List<OrderDetialModel.SubListBean> subList = getModel().getSubList();
////                for (int i = 0; i < subList.size(); i++) {
////                    if (!ListUtil.isEmpty(subList.get(i).getDetails())) {
////                        final String orderId = subList.get(i).getId() + "";
////                        if (subList.get(i).getOrderRace() == 2) {
////                            for (int j = 0; j < subList.get(i).getDetails().size(); j++) {
////                                final OrderDetialModel.SubListBean.DetailsBean sublistDetailsBean = subList.get(i).getDetails().get(j);
////                                final SubmitBackModel submitBackModel = new SubmitBackModel(
////                                        sublistDetailsBean.getId(),
////                                        sublistDetailsBean.getGoodsId(),
////                                        sublistDetailsBean.getGoodsTitle(),
////                                        sublistDetailsBean.getGoodsSpecDesc(),
////                                        sublistDetailsBean.getGoodsImage(),
////                                        sublistDetailsBean.getGoodsQuantity() - sublistDetailsBean.getRefundTotalQuantity(),
////                                        sublistDetailsBean.getGoodsPayAmount());
////                                ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout
////                                        .LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
////                                layoutParams.setMargins(0, 10, 0, 0);
////                                LayoutInflater inflater = LayoutInflater.from(context);
////                                View view = inflater.inflate(R.layout.service_item_order_flow_goods, null);
////                                TextView mall_service_title = view.findViewById(R.id.goodsTitle);
////                                TextView goodsSpace = view.findViewById(R.id.goodsSpace);
////                                TextView goodsCount = view.findViewById(R.id.goodsCount);
////                                TextView goodsMoney = view.findViewById(R.id.goodsMoney);
////                                TextView btn_commitBack = view.findViewById(R.id.btn_commitBack);
////                                TextView btn_assess = view.findViewById(R.id.btn_assess);
////                                CornerImageView mall_service_img = view.findViewById(R.id.goodsImg);
////                                ConstraintLayout goods_finish_btn_constraintLayout = view.findViewById(R.id.goods_finish_btn_constraintLayout);
////                                ConstraintLayout.LayoutParams layoutParams1 = (ConstraintLayout.LayoutParams) btn_commitBack.getLayoutParams();
////                                layoutParams1.rightMargin = 0;
////                                order_type_store_goods_status.setVisibility(View.GONE);
////                                if (subList.get(i).getOrderStatus() == 0) {
////                                    goods_num_constraintLayout.setVisibility(View.GONE);
////                                    //order_type_store_goods_status.setText("");
////                                } else if (subList.get(i).getOrderStatus() == 1) {
////                                    goods_finish_btn_constraintLayout.setVisibility(View.VISIBLE);
////                                    if (subList.get(i).getDeliverType() == 10) {
////                                        //order_type_store_goods_status.setText("待自提");
////                                    } else if (subList.get(i).getDeliverType() == 11) {
////                                        // order_type_store_goods_status.setText("待发货");
////                                    }
////
////                                    btn_assess.setVisibility(View.GONE);
////                                    if (sublistDetailsBean.getAllowApplyRefund() == 1) {
////
////                                        btn_commitBack.setLayoutParams(layoutParams1);
////                                        btn_commitBack.setVisibility(View.VISIBLE);
////                                        btn_commitBack.setText("申请售后");
////                                        btn_commitBack.setOnClickListener(new View.OnClickListener() {
////                                            @Override
////                                            public void onClick(View v) {
////                                                Map nokmap = new HashMap<String, String>();
////                                                nokmap.put("soure", "订单详情页【申请售后】点击");
////                                                MobclickAgent.onEvent(context, "btn_APP_MaternalandChildGoods_MyOrder_AfterSalesApply", nokmap);
////                                                ARouter.getInstance().
////                                                        build(ServiceRoutes.SERVICE_COMITBACK)
////                                                        .withSerializable("detials", submitBackModel)
////                                                        .withString("type", "2")
////                                                        .navigation();
////                                            }
////                                        });
////                                    } else {
////                                        btn_commitBack.setVisibility(View.GONE);
////                                    }
//////                        btn_assess.setText("确认收货");
//////                        btn_assess.setOnClickListener(new View.OnClickListener() {
//////                            @Override
//////                            public void onClick(View v) {
//////                                if (moutClickListener != null) {
//////                                    moutClickListener.outClick("confirmOder", sublistDetailsBean.getId() + "");
//////                                }
//////                            }
//////                        });
////                                    goods_num_constraintLayout.setVisibility(View.VISIBLE);
////                                    goods_order_num.setText("子订单编号：" + subList.get(i).getOrderNum());
////                                } else if (subList.get(i).getOrderStatus() == 2) {
////                                    goods_finish_btn_constraintLayout.setVisibility(View.VISIBLE);
////                                    if (sublistDetailsBean.getAllowApplyRefund() == 1) {
////                                        if (subList.get(i).getOrderType() == 1 || subList.get(i).getOrderType() == 4) {
////                                            btn_commitBack.setVisibility(View.GONE);
////                                        } else {
////                                            btn_commitBack.setLayoutParams(layoutParams1);
////                                            btn_commitBack.setVisibility(View.VISIBLE);
////                                            btn_commitBack.setText("申请售后");
////                                            btn_commitBack.setOnClickListener(new View.OnClickListener() {
////                                                @Override
////                                                public void onClick(View v) {
////                                                    Map nokmap = new HashMap<String, String>();
////                                                    nokmap.put("soure", "订单详情页【申请售后】点击");
////                                                    MobclickAgent.onEvent(context, "btn_APP_MaternalandChildGoods_MyOrder_AfterSalesApply", nokmap);
////                                                    ARouter.getInstance().
////                                                            build(ServiceRoutes.SERVICE_COMITBACK)
////                                                            .withSerializable("detials", submitBackModel)
////                                                            .withString("type", "2")
////                                                            .navigation();
////                                                }
////                                            });
////                                        }
////
////                                    } else {
////                                        btn_commitBack.setVisibility(View.GONE);
////                                    }
////                                    btn_assess.setVisibility(View.GONE);
////                                    //order_type_store_goods_status.setVisibility(View.VISIBLE);
////                                    // order_type_store_goods_status.setText("已完成");
////                                    //order_type_store_goods_status.setTextColor(Color.parseColor("#222222"));
////                                    goods_num_constraintLayout.setVisibility(View.VISIBLE);
////                                    goods_order_num.setText("子订单编号：" + subList.get(i).getOrderNum());
////                                } else if (subList.get(i).getOrderStatus() == 3) {
////                                    goods_num_constraintLayout.setVisibility(View.GONE);
////                                    //order_type_store_goods_status.setVisibility(View.VISIBLE);
////                                    //order_type_store_goods_status.setText("");
////                                    // order_type_store_goods_status.setTextColor(Color.parseColor("#222222"));
////                                } else if (subList.get(i).getOrderStatus() == 4) {
////                                    goods_num_constraintLayout.setVisibility(View.GONE);
////                                    //order_type_store_goods_status.setVisibility(View.VISIBLE);
////                                    //order_type_store_goods_status.setText("已退订");
////                                    //order_type_store_goods_status.setTextColor(Color.parseColor("#222222"));
////                                    if (sublistDetailsBean.getAllowApplyRefund() == 1) {
////                                        btn_commitBack.setLayoutParams(layoutParams1);
////                                        btn_commitBack.setVisibility(View.VISIBLE);
////                                        btn_commitBack.setText("售后详情");
////                                        btn_commitBack.setOnClickListener(new View.OnClickListener() {
////                                            @Override
////                                            public void onClick(View v) {
////                                                ARouter.getInstance().
////                                                        build(ServiceRoutes.SERVICE_ORDERBACKDETIAL)
////                                                        .withString("refundId", sublistDetailsBean.getLatestRefundId() + "")
////                                                        .navigation();
////                                            }
////                                        });
////                                    } else {
////                                        btn_commitBack.setVisibility(View.GONE);
////                                    }
////                                } else if (subList.get(i).getOrderStatus() == 5) {
////                                    goods_finish_btn_constraintLayout.setVisibility(View.VISIBLE);
////                                    //order_type_store_goods_status.setVisibility(View.VISIBLE);
////                                    //order_type_store_goods_status.setText("待确认收货");
////                                    if (j == subList.get(i).getDetails().size() - 1) {
////                                        layoutParams1.setMargins(0, 0, 30, 0);
////                                        btn_commitBack.setLayoutParams(layoutParams1);
////                                        btn_assess.setVisibility(View.VISIBLE);
////                                    } else {
////                                        btn_assess.setVisibility(View.GONE);
////                                    }
////                                    btn_commitBack.setVisibility(View.VISIBLE);
////                                    btn_assess.setText("确认收货");
////                                    btn_assess.setOnClickListener(new View.OnClickListener() {
////                                        @Override
////                                        public void onClick(View v) {
////                                            if (moutClickListener != null) {
////                                                moutClickListener.outClick("confirmOder", orderId + "");
////                                            }
////                                        }
////                                    });
////                                    goods_num_constraintLayout.setVisibility(View.VISIBLE);
////                                    goods_order_num.setText("子订单编号：" + subList.get(i).getOrderNum());
////                                }
////                                goodsMoney.setText("¥" + FormatUtils.moneyKeep2Decimals(sublistDetailsBean.getGoodsPayAmount()));
////                                goodsCount.setText("x" + sublistDetailsBean.getGoodsQuantity());
////                                mall_service_title.setText(sublistDetailsBean.getGoodsTitle());
////                                //goodsSpace.setText(detailsBean.getGoodsSpecDesc());
////                                //goodsMoney.setText("￥" + detailsBean.getPlatformPrice() + "");
////                                com.healthy.library.businessutil.GlideCopy.with(context).load(sublistDetailsBean.getGoodsImage())
////                                        .placeholder(R.drawable.img_1_1_default2)
////                                        .error(R.drawable.img_1_1_default)
////
////                                        .into(mall_service_img);
////                                mall_service_img.setOnClickListener(new View.OnClickListener() {
////                                    @Override
////                                    public void onClick(View v) {
////                                        Map nokmap = new HashMap<String, String>();
////                                        nokmap.put("soure", "订单详情页点击商品栏");
////                                        MobclickAgent.onEvent(context, "btn_APP__MaternalandChildGoods_CommodityDetails", nokmap);
////                                        ARouter.getInstance().
////                                                build(ServiceRoutes.SERVICE_DETAIL)
////                                                .withSerializable("id", sublistDetailsBean.getGoodsId() + "")
////                                                .navigation();
////                                    }
////                                });
////                                mall_service_title.setOnClickListener(new View.OnClickListener() {
////                                    @Override
////                                    public void onClick(View v) {
////                                        Map nokmap = new HashMap<String, String>();
////                                        nokmap.put("soure", "订单详情页点击商品栏");
////                                        MobclickAgent.onEvent(context, "btn_APP__MaternalandChildGoods_CommodityDetails", nokmap);
////                                        ARouter.getInstance().
////                                                build(ServiceRoutes.SERVICE_DETAIL)
////                                                .withSerializable("id", sublistDetailsBean.getGoodsId() + "")
////                                                .navigation();
////                                    }
////                                });
////                                view.setLayoutParams(layoutParams);
////                                order_detial_goods_liner.addView(view);
////                            }
////
////                        } else if (subList.get(i).getOrderRace() == 1) {
////                            final OrderDetialModel.SubListBean.DetailsBean sublistDetailsBean = subList.get(i).getDetails().get(0);
////                            final SubmitBackModel submitBackModel = new SubmitBackModel(
////                                    sublistDetailsBean.getId(),
////                                    sublistDetailsBean.getGoodsId(),
////                                    sublistDetailsBean.getGoodsTitle(),
////                                    sublistDetailsBean.getGoodsSpecDesc(),
////                                    sublistDetailsBean.getGoodsImage(),
////                                    sublistDetailsBean.getGoodsQuantity(),
////                                    sublistDetailsBean.getGoodsPayAmount(),
////                                    sublistDetailsBean.getGoodsQuantity() - sublistDetailsBean.getRefundTotalQuantity());
////                            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout
////                                    .LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
////                            layoutParams.setMargins(0, 10, 0, 0);
////                            LayoutInflater inflater = LayoutInflater.from(context);
////                            View view = inflater.inflate(R.layout.item_order_detial_service, null);
////
////                            ImageTextView goodsListStoreName = view.findViewById(R.id.goodsListStoreName);
////                            ConstraintLayout service_num_constraintLayout = view.findViewById(R.id.service_num_constraintLayout);
////                            TextView mall_service_title = view.findViewById(R.id.goodsTitle);
////                            TextView goodsSpace = view.findViewById(R.id.goodsSpace);
////                            TextView goodsCount = view.findViewById(R.id.goodsCount);
////                            TextView goodsMoney = view.findViewById(R.id.goodsMoney);
////                            TextView red_btn = view.findViewById(R.id.red_btn);
////                            TextView service_red_btn = view.findViewById(R.id.service_red_btn);
////                            TextView grey_btn = view.findViewById(R.id.grey_btn);
////                            TextView service_order_num = view.findViewById(R.id.service_order_num);
////                            TextView order_type_store_service_status = view.findViewById(R.id.order_type_store_service_status);
////                            CornerImageView mall_service_img = view.findViewById(R.id.goodsImg);
////                            final String shopId = subList.get(i).getShopId() + "";
////                            if (subList.get(i).getOrderStatus() == 0) {
////                                service_num_constraintLayout.setVisibility(View.GONE);
////                                order_type_store_service_status.setTextColor(Color.parseColor("#f02846"));
////                                order_type_store_service_status.setText("");
////                                red_btn.setVisibility(View.GONE);
////                                grey_btn.setVisibility(View.GONE);
////                            } else {
////                                if (subList.get(i).getOrderStatus() == 1) {
////                                    if (sublistDetailsBean.getAllowApplyRefund() == 1) {
////                                        order_type_store_service_status.setTextColor(Color.parseColor("#f02846"));
////                                        order_type_store_service_status.setText("待使用");
////                                        if (subList.get(i).getOrderType() == 1 || subList.get(i).getOrderType() == 4) {
////                                            red_btn.setVisibility(View.GONE);
////                                        } else {
////                                            red_btn.setVisibility(View.VISIBLE);
////                                            red_btn.setText("申请售后");
////                                            red_btn.setOnClickListener(new View.OnClickListener() {
////                                                @Override
////                                                public void onClick(View v) {
////                                                    Map nokmap = new HashMap<String, String>();
////                                                    nokmap.put("soure", "订单详情页【申请售后】点击");
////                                                    MobclickAgent.onEvent(context, "btn_APP_MaternalandChildGoods_MyOrder_AfterSalesApply", nokmap);
////                                                    ARouter.getInstance()
////                                                            .build(ServiceRoutes.SERVICE_COMITBACK)
////                                                            .withSerializable("detials", submitBackModel)
////                                                            .withString("type", "1")
////                                                            .navigation();
////                                                }
////                                            });
////                                        }
////                                        grey_btn.setVisibility(View.VISIBLE);
////                                        grey_btn.setText("查看核销码");
////                                        grey_btn.setOnClickListener(new View.OnClickListener() {
////                                            @Override
////                                            public void onClick(View v) {
////                                                ARouter.getInstance()
////                                                        .build(MineRoutes.MINE_ORDER_ZXING_DETAIL)
////                                                        .withString("orderId", orderId)
////                                                        .navigation();
////                                            }
////                                        });
////                                    } else {
////                                        red_btn.setVisibility(View.VISIBLE);
////                                        order_type_store_service_status.setTextColor(Color.parseColor("#222222"));
////                                        order_type_store_service_status.setText("已退订");
////                                        grey_btn.setVisibility(View.GONE);
////                                        red_btn.setText("售后详情");
////                                        red_btn.setOnClickListener(new View.OnClickListener() {
////                                            @Override
////                                            public void onClick(View v) {
////                                                ARouter.getInstance().
////                                                        build(ServiceRoutes.SERVICE_ORDERBACKDETIAL)
////                                                        .withString("refundId", sublistDetailsBean.getLatestRefundId() + "")
////                                                        .navigation();
////                                            }
////                                        });
////                                    }
////
////                                } else if (subList.get(i).getOrderStatus() == 2) {
////
////                                    if (subList.get(i).getCommentStatus() == 0) {
////                                        final String goodsName = subList.get(i).getShopName();
////                                        final String shopBrand = subList.get(i).getShopBrandName();
////                                        red_btn.setVisibility(View.GONE);
////                                        order_type_store_service_status.setTextColor(Color.parseColor("#f02846"));
////                                        order_type_store_service_status.setText("待评价");
////                                        grey_btn.setVisibility(View.GONE);
////                                        service_red_btn.setVisibility(View.VISIBLE);
////                                        service_red_btn.setText("评价晒单");
////                                        service_red_btn.setOnClickListener(new View.OnClickListener() {
////                                            @Override
////                                            public void onClick(View v) {
////                                                ARouter.getInstance()
////                                                        .build(MineRoutes.MINE_EVALUATE)
////                                                        .withString("goodsName", goodsName + "")
////                                                        .withString("shopId", shopId + "")
////                                                        .withString("orderId", orderId + "")
////                                                        .withString("shopBrand", shopBrand + "")
////                                                        .navigation();
////                                            }
////                                        });
////                                    } else {
////                                        red_btn.setVisibility(View.GONE);
////                                        order_type_store_service_status.setTextColor(Color.parseColor("#222222"));
////                                        order_type_store_service_status.setText("已完成");
////                                        grey_btn.setVisibility(View.GONE);
////                                    }
////
////                                } else if (subList.get(i).getOrderStatus() == 3) {
////                                    red_btn.setVisibility(View.GONE);
////                                    order_type_store_service_status.setTextColor(Color.parseColor("#222222"));
////                                    order_type_store_service_status.setText("");
////                                    grey_btn.setVisibility(View.GONE);
////                                } else if (subList.get(i).getOrderStatus() == 4) {
////                                    red_btn.setVisibility(View.GONE);
////                                    order_type_store_service_status.setTextColor(Color.parseColor("#222222"));
////                                    order_type_store_service_status.setText("已退订");
////                                    red_btn.setVisibility(View.VISIBLE);
////                                    grey_btn.setVisibility(View.GONE);
////                                    red_btn.setText("售后详情");
////                                    red_btn.setOnClickListener(new View.OnClickListener() {
////                                        @Override
////                                        public void onClick(View v) {
////                                            ARouter.getInstance()
////                                                    .build(ServiceRoutes.SERVICE_ORDERBACKDETIAL)
////                                                    .withString("refundId", sublistDetailsBean.getLatestRefundId() + "")
////                                                    .navigation();
////                                        }
////                                    });
////                                }
////                                service_num_constraintLayout.setVisibility(View.VISIBLE);
////                                service_order_num.setText("子订单编号：" + subList.get(i).getOrderNum());
////                            }
////                            goodsMoney.setText("¥" + FormatUtils.moneyKeep2Decimals(sublistDetailsBean.getGoodsPayAmount()));
////                            goodsListStoreName.setText(subList.get(i).getShopName());
////                            goodsCount.setText("x" + sublistDetailsBean.getGoodsQuantity());
////                            mall_service_title.setText(sublistDetailsBean.getGoodsTitle());
////                            // goodsSpace.setText(detailsBean.getGoodsSpecDesc());
////                            //goodsMoney.setText("￥" + detailsBean.getPlatformPrice() + "");
////                            com.healthy.library.businessutil.GlideCopy.with(context).load(sublistDetailsBean.getGoodsImage())
////                                    .placeholder(R.drawable.img_1_1_default2)
////                                    .error(R.drawable.img_1_1_default)
////
////                                    .into(mall_service_img);
////                            view.setOnClickListener(new View.OnClickListener() {
////                                @Override
////                                public void onClick(View v) {
////                                    ARouter.getInstance()
////                                            .build(ServiceRoutes.SERVICE_DETAIL)
////                                            .withString("shopId", shopId + "")
////                                            .withString("goodsId", sublistDetailsBean.getGoodsId() + "")
////                                            .withString("categoryNo", "null")
////                                            .withString("cityNo", LocUtil.getCityNo(context, SpKey.LOC_CHOSE) + "")
////                                            .withString("lng", LocUtil.getLongitude(context, SpKey.LOC_CHOSE) + "")
////                                            .withString("lat", LocUtil.getLatitude(context, SpKey.LOC_CHOSE) + "")
////                                            .navigation();
////                                }
////                            });
////                            view.setLayoutParams(layoutParams);
////
////                            order_detial_service_liner.addView(view);
////                        }
////
////                    }
////                }
////            }
//        } else if (getItemViewType(position) == 4) {//积分单
////            LinearLayout order_detial_goods_liner = holder.getView(R.id.order_detial_goods_liner);
////            ConstraintLayout goods_num_constraintLayout = holder.getView(R.id.goods_num_constraintLayout);
////            if (getModel().getOrderRace() == 4) {
////                goods_num_constraintLayout.setVisibility(View.GONE);
////                order_detial_goods_liner.removeAllViews();
////                for (int i = 0; i < getModel().getDetails().size(); i++) {
////                    final OrderDetialModel.DetailsBean detailsBean = getModel().getDetails().get(i);
////                    final SubmitBackModel submitBackModel = new SubmitBackModel(
////                            detailsBean.getId(),
////                            detailsBean.getGoodsId(),
////                            detailsBean.getGoodsTitle(),
////                            detailsBean.getGoodsSpecDesc(),
////                            detailsBean.getGoodsImage(),
////                            detailsBean.getGoodsQuantity() - detailsBean.getRefundTotalQuantity(),
////                            detailsBean.getGoodsPayAmount(),
////                            detailsBean.getTotalPayPoints());
////                    ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout
////                            .LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
////                    layoutParams.setMargins(0, 10, 0, 0);
////                    LayoutInflater inflater = LayoutInflater.from(context);
////                    View view = inflater.inflate(R.layout.service_item_order_flow_goods, null);
////                    TextView mall_service_title = view.findViewById(R.id.goodsTitle);
////                    ConstraintLayout goods_finish_btn_constraintLayout = view.findViewById(R.id.goods_finish_btn_constraintLayout);
////                    TextView btn_commitBack = view.findViewById(R.id.btn_commitBack);
////                    TextView btn_assess = view.findViewById(R.id.btn_assess);
////                    TextView goodsSpace = view.findViewById(R.id.goodsSpec);
////                    TextView goodsCount = view.findViewById(R.id.goodsCount);
////                    TextView goodsMoney = view.findViewById(R.id.goodsMoney);
////                    TextView pointLable = view.findViewById(R.id.pointLable);
////                    pointLable.setVisibility(View.VISIBLE);
////                    pointLable.getBackground().setAlpha(150);//积分兑换的标签透明度
////                    ImageView actFlag = view.findViewById(R.id.actFlag);
////                    CornerImageView mall_service_img = view.findViewById(R.id.goodsImg);
////                    actFlag.setVisibility(View.GONE);
////                    btn_assess.setVisibility(View.GONE);
////                    if (getModel().getOrderStatus() == 1) {
////                        goods_finish_btn_constraintLayout.setVisibility(View.VISIBLE);
////                        btn_commitBack.setVisibility(View.VISIBLE);
////                        if (detailsBean.getAllowApplyRefund() == 1) {
////                            btn_commitBack.setText("申请售后");
////                        } else {
////                            btn_commitBack.setText("售后详情");
////                        }
////                    } else {
////                        if (detailsBean.getRefundTotalQuantity() > 0) {
////                            ////System.out.println("售后详情");
////                            goods_finish_btn_constraintLayout.setVisibility(View.VISIBLE);
////                            btn_commitBack.setVisibility(View.VISIBLE);
////                            btn_commitBack.setText("售后详情");
////                        } else {
////                            goods_finish_btn_constraintLayout.setVisibility(View.GONE);
////                            btn_commitBack.setVisibility(View.GONE);
////                        }
////                    }
////
////                    if (FormatUtils.moneyKeep2Decimals(detailsBean.getGoodsPayAmount()).equals("0")) {
////                        goodsMoney.setText(FormatUtils.moneyKeep2Decimals(detailsBean.getGoodsPoints()) + "积分");
////                    } else {
////                        goodsMoney.setText(FormatUtils.moneyKeep2Decimals(detailsBean.getGoodsPoints()) + "积分\n+" + "¥" + FormatUtils.moneyKeep2Decimals(detailsBean.getGoodsPayAmount()));
////                    }
////
////                    goodsCount.setText("x" + detailsBean.getGoodsQuantity());
////                    mall_service_title.setText(detailsBean.getGoodsTitle());
////                    goodsSpace.setText("");
////                    if (!TextUtils.isEmpty(detailsBean.getGoodsSpecDesc())) {
////                        goodsSpace.setText(detailsBean.getGoodsSpecDesc());
////                    }
//////                goodsMoney.setText("￥" + FormatUtils.moneyKeep2Decimals(detailsBean.getGoodsPayAmount() ));
////                    //goodsMoney.setText("￥" + detailsBean.getPlatformPrice() + "");
////                    com.healthy.library.businessutil.GlideCopy.with(context).load(detailsBean.getGoodsImage())
////                            .placeholder(R.drawable.img_1_1_default2)
////                            .error(R.drawable.img_1_1_default)
////
////                            .into(mall_service_img);
////                    if (getModel().getOrderStatus() == 1) {
////                        goods_finish_btn_constraintLayout.setVisibility(View.VISIBLE);
////                    }
////                    btn_commitBack.setOnClickListener(new View.OnClickListener() {
////                        @Override
////                        public void onClick(View v) {
////
////                            if (detailsBean.getAllowApplyRefund() == 1) {
////                                Map nokmap = new HashMap<String, String>();
////                                nokmap.put("soure", "订单详情页【申请售后】点击");
////                                MobclickAgent.onEvent(context, "btn_APP_MaternalandChildGoods_MyOrder_AfterSalesApply", nokmap);
////                                ARouter.getInstance().
////                                        build(ServiceRoutes.SERVICE_COMITBACK)
////                                        .withSerializable("detials", submitBackModel)
////                                        .withString("type", "3")
////                                        .navigation();
////                            } else {
////                                ARouter.getInstance().
////                                        build(ServiceRoutes.SERVICE_ORDERBACKDETIAL)
////                                        .withString("refundId", detailsBean.getLatestRefundId())
////                                        .navigation();
////                            }
////
////                        }
////                    });
////                    mall_service_img.setOnClickListener(new View.OnClickListener() {
////                        @Override
////                        public void onClick(View v) {
////                            Map nokmap = new HashMap<String, String>();
////                            nokmap.put("soure", "订单详情页点击商品栏");
////                            MobclickAgent.onEvent(context, "btn_APP__MaternalandChildGoods_CommodityDetails", nokmap);
////                            ARouter.getInstance().
////                                    build(ServiceRoutes.SERVICE_DETAIL)
////                                    .withString("id", detailsBean.getGoodsId() + "")
////                                    .withString("marketingType" , "5")
////                                    .navigation();
////                        }
////                    });
////                    mall_service_title.setOnClickListener(new View.OnClickListener() {
////                        @Override
////                        public void onClick(View v) {
////                            Map nokmap = new HashMap<String, String>();
////                            nokmap.put("soure", "订单详情页点击商品栏");
////                            MobclickAgent.onEvent(context, "btn_APP__MaternalandChildGoods_CommodityDetails", nokmap);
////                            ARouter.getInstance().
////                                    build(ServiceRoutes.SERVICE_DETAIL)
////                                    .withString("id", detailsBean.getGoodsId() + "")
////                                    .withString("marketingType" , "5")
////                                    .navigation();
////                        }
////                    });
////                    view.setLayoutParams(layoutParams);
////                    order_detial_goods_liner.addView(view);
////                }
////            }
//        }
//
//    }
//
//    public OrderDetialGoodsAdapter(int viewId) {
//        super(viewId);
//    }
//
//    @Override
//    public LayoutHelper onCreateLayoutHelper() {
//        return new LinearLayoutHelper();
//    }
//
//}
