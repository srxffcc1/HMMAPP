//package com.health.mine.adapter;
//
//import android.graphics.Color;
//import androidx.constraintlayout.widget.ConstraintLayout;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.alibaba.android.arouter.launcher.ARouter;
//import com.bumptech.glide.Glide;
//import com.chad.library.adapter.base.BaseQuickAdapter;
//import com.chad.library.adapter.base.BaseViewHolder;
//import com.health.mine.R;
//import com.health.mine.model.OrderBackListModel;
//import com.healthy.library.routes.MineRoutes;
//import com.healthy.library.routes.ServiceRoutes;
//import com.healthy.library.utils.FormatUtils;
//import com.healthy.library.utils.ParseUtils;
//import com.healthy.library.watcher.AlphaTextView;
//import com.healthy.library.widget.CornerImageView;
//import com.healthy.library.widget.DrawableTextView;
//
//import java.util.Locale;
//
///**
// * @author Li
// * @date 2019/04/10 10:51
// * @des 订单
// */
//
//public class OrderBackListAdapter extends BaseQuickAdapter<OrderBackListModel, BaseViewHolder> {
//
//
//
//
//    public OrderBackListAdapter() {
//        this(R.layout.mine_item_order);
//    }
//
//    private OrderBackListAdapter(int layoutResId) {
//        super(layoutResId);
//    }
//
//    @Override
//    protected void convert(BaseViewHolder helper, final OrderBackListModel item) {
//
//         ConstraintLayout layoutItem;
//         DrawableTextView tvStoreName;
//         ImageView ivStore;
//         TextView tvOrderStatus;
//         View dividerHeader;
//         CornerImageView ivProduct;
//         TextView tvProductName;
//         TextView tvIntroduction;
//         TextView tvSinglePrice;
//         TextView tvNum;
//         TextView tvTotalPrice;
//         TextView tvTotalPricePre;
//         View dividerBottom;
//         AlphaTextView tvPay;
//         AlphaTextView tvCancel;
//         AlphaTextView tvCancelGoods;
//
//
//        layoutItem = (ConstraintLayout) helper.itemView.findViewById(R.id.layout_item);
//        tvStoreName = (DrawableTextView) helper.itemView.findViewById(R.id.tv_store_name);
//        ivStore = (ImageView) helper.itemView.findViewById(R.id.iv_store);
//        tvOrderStatus = (TextView) helper.itemView.findViewById(R.id.tv_order_status);
//        dividerHeader = (View) helper.itemView.findViewById(R.id.divider_header);
//        ivProduct = (CornerImageView) helper.itemView.findViewById(R.id.iv_product);
//        tvProductName = (TextView) helper.itemView.findViewById(R.id.tv_product_name);
//        tvIntroduction = (TextView) helper.itemView.findViewById(R.id.tv_introduction);
//        tvSinglePrice = (TextView) helper.itemView.findViewById(R.id.tv_single_price);
//        tvNum = (TextView) helper.itemView.findViewById(R.id.tv_num);
//        tvTotalPrice = (TextView) helper.itemView.findViewById(R.id.tv_total_price);
//        tvTotalPricePre = (TextView) helper.itemView.findViewById(R.id.tv_total_price_pre);
//        dividerBottom = (View) helper.itemView.findViewById(R.id.divider_bottom);
//        tvPay = (AlphaTextView) helper.itemView.findViewById(R.id.tv_pay);
//        tvCancel = (AlphaTextView) helper.itemView.findViewById(R.id.tv_cancel);
//        tvCancelGoods = (AlphaTextView) helper.itemView.findViewById(R.id.tv_cancel_goods);
//        tvPay.setVisibility(View.GONE);
//        tvCancel.setVisibility(View.VISIBLE);
//        if(item.orderGoodsRefundsList!=null&&item.orderGoodsRefundsList.size()>0){
//            OrderBackListModel.OrderBackGood itembean=item.orderGoodsRefundsList.get(0);
//            if(itembean!=null){
//                String refundStatus="";
//                tvOrderStatus.setTextColor(Color.parseColor("#222222"));
//                if(item.refundStatus==1){
//                    refundStatus="退款中";
//                    tvCancel.setText("退款进度");
//                    tvOrderStatus.setTextColor(Color.parseColor("#FF6266"));
//                }
//                if(item.refundStatus==2){
//                    refundStatus="退款失败";
//                    tvCancel.setText("重新提交");
//                    tvCancel.setVisibility(View.GONE);
//
//                }
//                if(item.refundStatus==3){
//                    refundStatus="退款成功";
//                    tvCancel.setText("退款进度");
//
//                }
//                tvStoreName.setText(item.merchantName);
//                tvOrderStatus.setText(refundStatus);
//                tvProductName.setText(itembean.goodsTitle);
//
//                com.healthy.library.businessutil.GlideCopy.with(mContext)
//                        .load(itembean.goodsImage)
//                        .placeholder(R.drawable.img_1_1_default)
//                        .error(R.drawable.img_1_1_default)
//                        
//                        .into(ivProduct);
//
//                tvSinglePrice.setText(
//                        String.format("¥ %s", FormatUtils.moneyKeep2Decimals((itembean.refundMoney/itembean.refundNumber)+"")));
//
//                tvNum.setText(String
//                        .format(Locale.CHINA, "x%d", ParseUtils.parseInt(itembean.refundNumber + "")));
//
//                tvTotalPrice.setText(
//                        String.format("¥ %s", FormatUtils.moneyKeep2Decimals((itembean.refundMoney)+"")));
//
//                helper.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        ARouter.getInstance().build(ServiceRoutes.SERVICE_SERVICEORDERDETIAL)
//                                .withString("orderId", item.orderId+"")
//                                .navigation();
//                    }
//                });
//                tvCancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if(item.refundStatus==1){
//                            ARouter.getInstance()
//                                    .build(MineRoutes.MINE_ORDER_BACK_DETAIL)
//                                    .withString("refundId", item.refundId+"")
//                                    .navigation();
//                        }
//                        if(item.refundStatus==2){
////                            ARouter.getInstance()
////                                    .build(MallRoutes.MALL_CHECKOUT_COUNTER)
////                                    .withString("orderId", item.id+"")
////                                    .navigation();
//                        }
//                        if(item.refundStatus==3){
//                            ARouter.getInstance()
//                                    .build(MineRoutes.MINE_ORDER_BACK_DETAIL)
//                                    .withString("refundId", item.refundId+"")
//                                    .navigation();
//                        }
//                    }
//                });
//
//            }
//
//        }
//    }
//
//    private void initView() {
//
//
//    }
//}