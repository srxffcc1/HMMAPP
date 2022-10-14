//package com.health.mine.adapter;
//
//import android.graphics.Color;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.alibaba.android.arouter.launcher.ARouter;
//import com.bumptech.glide.Glide;
//import com.chad.library.adapter.base.BaseQuickAdapter;
//import com.chad.library.adapter.base.BaseViewHolder;
//import com.health.mine.R;
//import com.health.mine.model.OrderDetailModel;
//import com.healthy.library.routes.MallRoutes;
//import com.healthy.library.routes.MineRoutes;
//import com.healthy.library.routes.ServiceRoutes;
//import com.healthy.library.utils.FormatUtils;
//import com.healthy.library.utils.ParseUtils;
//import com.healthy.library.watcher.AlphaTextView;
//
//import java.util.Locale;
//
///**
// * @author Li
// * @date 2019/04/10 10:51
// * @des 订单
// */
//
//public class OrderAdapter extends BaseQuickAdapter<OrderDetailModel, BaseViewHolder> {
//
//
//
//    public OrderAdapter() {
//        this(R.layout.mine_item_order);
//    }
//
//    private OrderAdapter(int layoutResId) {
//        super(layoutResId);
//    }
//
//    @Override
//    protected void convert(BaseViewHolder helper, final OrderDetailModel item) {
//        helper.setText(R.id.tv_store_name, item.shopName);
//        if(item.orderGoodsList!=null&&item.orderGoodsList.size()>0){
//            helper.setText(R.id.tv_product_name, item.orderGoodsList.get(0).goodsTitle);
////        helper.setText(R.id.tv_introduction, item.getGoodsIntroduction());
//            com.healthy.library.businessutil.GlideCopy.with(mContext)
//                    .load(item.orderGoodsList.get(0).goodsImage)
//                    .placeholder(R.drawable.img_1_1_default)
//                    .error(R.drawable.img_1_1_default)
//                    
//                    .into((ImageView) helper.getView(R.id.iv_product));
//            helper.setText(R.id.tv_single_price,
//                    String.format("¥ %s", FormatUtils.moneyKeep2Decimals(item.orderGoodsList.get(0).goodsPrice)));
//
//            helper.setText(R.id.tv_num, String
//                    .format(Locale.CHINA, "x%d", ParseUtils.parseInt(item.orderGoodsList.get(0).goodsNumber + "")));
//        }else {
//            helper.setText(R.id.tv_product_name, item.shopName);
////        helper.setText(R.id.tv_introduction, item.getGoodsIntroduction());
//            com.healthy.library.businessutil.GlideCopy.with(mContext)
//                    .load(R.drawable.img_1_1_default)
//                    .placeholder(R.drawable.img_1_1_default)
//                    .error(R.drawable.img_1_1_default)
//                    
//                    .into((ImageView) helper.getView(R.id.iv_product));
//            helper.setText(R.id.tv_single_price,
//                    String.format("¥ %s", 0));
//
//            helper.setText(R.id.tv_num, String
//                    .format(Locale.CHINA, "x%d", 0));
//        }
//
//        helper.setText(R.id.tv_total_price,
//                "¥"+String.format("%s", FormatUtils.moneyKeep2Decimals(item.payMoney)));
//
//
//         final View dividerBottom;
//         final AlphaTextView tvPay;
//         final AlphaTextView tvCancel;
//        final AlphaTextView tvCancelgoods;
//        dividerBottom = (View) helper.getView(R.id.divider_bottom);
//        tvPay = (AlphaTextView) helper.getView(R.id.tv_pay);
//        tvCancel = (AlphaTextView) helper.getView(R.id.tv_cancel);
//        tvCancelgoods = (AlphaTextView) helper.getView(R.id.tv_cancel_goods);
//        tvCancelgoods.setVisibility(View.GONE);
//        TextView tvStatus = helper.getView(R.id.tv_order_status);
//        String statusContent;
//        String statusColor;
//        if (item.cancelStatus == 1) {
//            statusContent = "已取消";
//            statusColor = "#666666";
//            helper.itemView.setAlpha(0.8f);
//        } else {
//            helper.itemView.setAlpha(1.0f);
//            if (item.payStatus == 1) {
//                if (item.status == 2) {//已支付已用完
//                    if (item.commentStatus == 1) {
//                        statusContent = "已完成";
//                        statusColor = "#222222";
//                    } else {
//                        statusContent = "待点评";
//                        statusColor = "#222222";
//                    }
//                } else {//已支付没用完
//                    statusContent = "待使用";
//                    statusColor = "#222222";
//                    if(item.status == 4){
//                        statusContent = "已退订";
//                        statusColor = "#222222";
//                    }
//                }
//            } else {
//                statusContent = "待付款";
//                statusColor = "#ffff6266";
//            }
//        }
//        tvStatus.setText(statusContent);
//        tvStatus.setTextColor(Color.parseColor(statusColor));
//        dividerBottom.setVisibility(View.VISIBLE);
//        tvPay.setVisibility(View.VISIBLE);
//        tvCancel.setVisibility(View.VISIBLE);
//        if ("已取消".equals(statusContent)) {
//            dividerBottom.setVisibility(View.GONE);
//            tvPay.setVisibility(View.GONE);
//            tvCancel.setVisibility(View.GONE);
//        }
//        if ("已退订".equals(statusContent)) {
//            dividerBottom.setVisibility(View.GONE);
//            tvPay.setVisibility(View.GONE);
//            tvCancel.setVisibility(View.GONE);
//        }
//        if ("已完成".equals(statusContent)) {
//            dividerBottom.setVisibility(View.GONE);
//            tvPay.setVisibility(View.GONE);
//            tvCancel.setVisibility(View.GONE);
//        }
//        if ("待使用".equals(statusContent)) {
//            tvPay.setVisibility(View.GONE);
//            tvCancel.setVisibility(View.VISIBLE);
//            tvCancel.setText("查看核销码");
//            tvCancelgoods.setVisibility(View.VISIBLE);
//        }
//        if ("待点评".equals(statusContent)) {
//            tvPay.setVisibility(View.GONE);
//            tvCancel.setVisibility(View.VISIBLE);
//            tvCancel.setText("点评");
//        }
//        if ("待付款".equals(statusContent)) {
//            tvCancel.setVisibility(View.GONE);
//            tvPay.setText("支付");
//        }
//
//        if(item.orderGoodsList!=null&&item.orderGoodsList.size()>0){
//            if ("待使用".equals(statusContent)) {
//
//                helper.setText(R.id.tv_introduction, "有效期至 "+item.orderGoodsList.get(0).expiredTime+"");
//                if(TextUtils.isEmpty(item.orderGoodsList.get(0).expiredTime)){
//                    helper.setVisible(R.id.tv_introduction,false);
//                }else {
//
//                    helper.setVisible(R.id.tv_introduction,true);
//                }
//            }else {
//                helper.setText(R.id.tv_introduction, item.orderGoodsList.get(0).description+"");
//            }
//        }else {
//            helper.setText(R.id.tv_introduction, "");
//        }
//
//
//        final String finalStatusContent = statusContent;
//        tvPay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if ("已取消".equals(finalStatusContent)) {
//
//                }
//                if ("已完成".equals(finalStatusContent)) {
//
//                }
//                if ("待使用".equals(finalStatusContent)) {
//
//                }
//                if ("待点评".equals(finalStatusContent)) {
//
//                }
//                if ("待付款".equals(finalStatusContent)) {
//                    ARouter.getInstance()
//                            .build(MallRoutes.MALL_CHECKOUT_COUNTER)
//                            .withString("orderId", item.id+"")
//                            .navigation();
//                }
//            }
//        });
//        tvCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if ("已取消".equals(finalStatusContent)) {
//
//                }
//                if ("已完成".equals(finalStatusContent)) {
//
//                }
//                if ("待使用".equals(finalStatusContent)) {
//                    ARouter.getInstance()
//                            .build(MineRoutes.MINE_ORDER_ZXING_DETAIL)
//                            .withString("orderId", item.id+"")
//                            .navigation();
//                }
//                if ("待点评".equals(finalStatusContent)) {
//                    ARouter.getInstance()
//                            .build(MineRoutes.MINE_EVALUATE)
//                            .withString("shopId", item.shopId+"")
//                            .withString("orderId", item.id+"")
//                            .withString("goodsName", item.shopName+"")
//                            .withString("shopBrand", item.shopBrandName+"")
//                            .navigation();
//                }
//                if ("待付款".equals(finalStatusContent)) {
//
//                }
//            }
//        });
//        tvCancelgoods.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ARouter.getInstance()
//                        .build(MallRoutes.MALL_ORDERBACK)
//                        .withString("orderId", item.id+"")
//                        .navigation();
//            }
//        });
//
//        helper.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                        ARouter.getInstance().build(ServiceRoutes.SERVICE_SERVICEORDERDETIAL)
//                                .withString("orderId", item.id+"")
//                                .withString("type", finalStatusContent+"")
//                                .navigation();
//            }
//        });
//
//
////        helper.addOnClickListener(R.id.layout_item, R.id.tv_store_name,
////                R.id.tv_pay, R.id.tv_cancel, R.id.tv_evaluate);
//    }
//
//    private void initView() {
//
//    }
//}