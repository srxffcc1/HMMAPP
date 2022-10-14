package com.health.servicecenter.adapter;

import android.graphics.Bitmap;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.servicecenter.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.OrderList;
import com.healthy.library.routes.SecondRoutes;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.DateUtils;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.ImageUtilK;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.ImageTextView;
import com.umeng.analytics.MobclickAgent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OrderDetialHeaderAdapter extends BaseAdapter<OrderList> {

    int mSeconds = 0;
    CountDownTimer countDownTimer;

    public OrderDetialHeaderAdapter() {
        this(R.layout.item_order_detial_top_layput);//0   --> 待支付//1   --> 待发货//5   --> 待核销//6   --> 待确认收货//2   --> 已完成//3   --> 已取消//4   --> 已退款
    }

    public OrderDetialHeaderAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull final BaseHolder holder, int position) {
        LinearLayout topViewLiner = holder.getView(R.id.topViewLiner);
        RelativeLayout no_pay_title_rel = holder.getView(R.id.no_pay_title_rel);
        RelativeLayout extracting_title_rel = holder.getView(R.id.extracting_title_rel);
        RelativeLayout finish_title_rel = holder.getView(R.id.finish_title_rel);
        RelativeLayout cancle_title_rel = holder.getView(R.id.cancle_title_rel);
        RelativeLayout no_receipt_title_rel = holder.getView(R.id.no_receipt_title_rel);
        ImageTextView cancle_title = holder.getView(R.id.cancle_title);
        TextView zxing_value = holder.getView(R.id.zxing_value);
        ImageView img_zxing = holder.getView(R.id.img_zxing);
        ConstraintLayout ticketLayout = holder.getView(R.id.ticketLayout);
        final TextView clock_time = holder.getView(R.id.clock_time);
        TextView extracting_title = holder.getView(R.id.extracting_title);
        ImageView extracting_iv = holder.getView(R.id.extracting_iv);
        final OrderList.OrderChild orderChild = getModel().orderChild;
        final OrderList.OrderFather orderFather = getModel().orderFather;
        if (orderChild == null && orderFather != null) {//主订单
            if (orderFather.subOrderList != null && orderFather.subOrderList.size() > 0) {
                final List<OrderList.OrderFather.SubOrderListBean> subList = orderFather.subOrderList;
                if (orderFather.getOrderStatus() == 0) {
                    no_pay_title_rel.setVisibility(View.VISIBLE);
                    extracting_title_rel.setVisibility(View.GONE);
                    finish_title_rel.setVisibility(View.GONE);
                    cancle_title_rel.setVisibility(View.GONE);
                    no_receipt_title_rel.setVisibility(View.GONE);
                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                    } else {
                        mSeconds = Integer.parseInt(orderFather.residuePayTime);//后台返回剩余支付时间：秒数
                    }
                    if (mSeconds > 0) {
                        countDownTimer = new CountDownTimer(mSeconds * 1000, 1000) {
                            public void onTick(long millisUntilFinished) {
                                millisUntilFinished = millisUntilFinished / 1000;
                                mSeconds = (int) millisUntilFinished;
                                int hours = (int) (millisUntilFinished / (60 * 60));
                                int minutes = (int) millisUntilFinished / 60 % 60;
                                int seconds = (int) millisUntilFinished % 60;
                                clock_time.setText(String.format("%02d", Math.max(0, hours)) + ":"
                                        + String.format("%02d", Math.max(0, minutes)) + ":"
                                        + String.format("%02d", Math.max(0, seconds)));
                            }

                            public void onFinish() {
                                //剩余支付时间结束后进行相应逻辑处理
                                clock_time.setText("0");
                                if (moutClickListener != null) {
                                    moutClickListener.outClick("countDownTime", null);
                                }
                            }
                        }.start();
                    } else {
                        clock_time.setText("0");
                    }
                } else if (orderFather.getOrderStatus() == 1) {
                    no_pay_title_rel.setVisibility(View.GONE);
                    finish_title_rel.setVisibility(View.GONE);
                    cancle_title_rel.setVisibility(View.GONE);
                    no_receipt_title_rel.setVisibility(View.GONE);

                    extracting_title_rel.setVisibility(View.VISIBLE);
                    if (subList.get(0).getDeliverType() == 10 || subList.get(0).getDeliverType() == 20) {
                        extracting_title.setText("待核销");
                        extracting_iv.setImageDrawable(context.getResources().getDrawable(R.mipmap.order_detial_zxing));
                        ticketLayout.setVisibility(View.VISIBLE);
                    } else if (subList.get(0).getDeliverType() == 11) {
                        ticketLayout.setVisibility(View.VISIBLE);
                        extracting_iv.setImageDrawable(context.getResources().getDrawable(R.mipmap.order_detial_extracting));
                        extracting_title.setText("待发货");
                    }
                    if (!TextUtils.isEmpty(subList.get(0).ticket) && !TextUtils.isEmpty(subList.get(0).ticketContent)) {
                        Bitmap zxing = new ImageUtilK().base64ToBitmap(subList.get(0).ticketContent);
                        img_zxing.setImageBitmap(zxing);
                        String bankCard = subList.get(0).ticket;
                        String str = "";
                        str = bankCard.substring(0, 4) + " " + bankCard.substring(4, bankCard.length());
                        zxing_value.setText(str);
                    }
                } else if (orderFather.getOrderStatus() == 2) {
                    ticketLayout.setVisibility(View.GONE);
                    no_pay_title_rel.setVisibility(View.GONE);
                    extracting_title_rel.setVisibility(View.GONE);
                    cancle_title_rel.setVisibility(View.GONE);
                    no_receipt_title_rel.setVisibility(View.GONE);
                    finish_title_rel.setVisibility(View.VISIBLE);
                } else if (orderFather.getOrderStatus() == 3) {
                    no_pay_title_rel.setVisibility(View.GONE);
                    extracting_title_rel.setVisibility(View.GONE);
                    finish_title_rel.setVisibility(View.GONE);
                    no_receipt_title_rel.setVisibility(View.GONE);
                    cancle_title_rel.setVisibility(View.VISIBLE);
                    cancle_title.setText("已取消");
                } else if (orderFather.getOrderStatus() == 4) {
                    no_pay_title_rel.setVisibility(View.GONE);
                    extracting_title_rel.setVisibility(View.GONE);
                    finish_title_rel.setVisibility(View.GONE);
                    no_receipt_title_rel.setVisibility(View.GONE);
                    cancle_title_rel.setVisibility(View.VISIBLE);
                    cancle_title.setText("已退款");
                } else if (orderFather.getOrderStatus() == 5) {
                    no_pay_title_rel.setVisibility(View.GONE);
                    finish_title_rel.setVisibility(View.GONE);
                    cancle_title_rel.setVisibility(View.GONE);
                    no_receipt_title_rel.setVisibility(View.GONE);
                    extracting_title_rel.setVisibility(View.VISIBLE);
                    if (subList.get(0).getDeliverType() == 10 || subList.get(0).getDeliverType() == 20) {
                        extracting_title.setText("待核销");
                        extracting_iv.setImageDrawable(context.getResources().getDrawable(R.mipmap.order_detial_zxing));
                        ticketLayout.setVisibility(View.VISIBLE);
                    } else if (subList.get(0).getDeliverType() == 11) {
                        ticketLayout.setVisibility(View.VISIBLE);
                        extracting_iv.setImageDrawable(context.getResources().getDrawable(R.mipmap.order_detial_extracting));
                        extracting_title.setText("待发货");
                    }
                    ticketLayout.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(subList.get(0).ticket) && !TextUtils.isEmpty(subList.get(0).ticketContent)) {
                        Bitmap zxing = new ImageUtilK().base64ToBitmap(subList.get(0).ticketContent);
                        img_zxing.setImageBitmap(zxing);
                        String bankCard = subList.get(0).ticket;
                        String str = "";
                        str = bankCard.substring(0, 4) + " " + bankCard.substring(4, bankCard.length());
                        zxing_value.setText(str);
                    }
                } else if (orderFather.getOrderStatus() == 6) {
                    no_pay_title_rel.setVisibility(View.GONE);
                    extracting_title_rel.setVisibility(View.GONE);
                    finish_title_rel.setVisibility(View.GONE);
                    cancle_title_rel.setVisibility(View.GONE);
                    no_receipt_title_rel.setVisibility(View.VISIBLE);
//                    if (orderFather.getLatestConfirmDeliveryTime() == null || TextUtils.isEmpty(getModel().getLatestConfirmDeliveryTime())) {
//                        receipt_content.setText("");
//                    } else {
//                        String[] millisFinished = getMinutes(getModel().getLatestConfirmDeliveryTime());
//                        receipt_content.setText("剩余" + millisFinished[0] + "天" + millisFinished[1] + "时自动确认收货");
//                    }
                }
                topViewLiner.removeAllViews();
                for (int i = 0; i < subList.size(); i++) {
                    final OrderList.OrderFather.SubOrderListBean subOrderListBean = subList.get(i);
                    View view = LayoutInflater.from(context).inflate(R.layout.item_order_detial_top_delivery_layout, null);
                    ConstraintLayout topHomeLayout = view.findViewById(R.id.topHomeLayout);
                    ConstraintLayout topShopLayout = view.findViewById(R.id.topShopLayout);
                    LinearLayout orderDetialGoodsLiner = view.findViewById(R.id.orderDetialGoodsLiner);
                    LinearLayout cardLayout = view.findViewById(R.id.cardLayout);
                    View heightView = view.findViewById(R.id.heightView);
                    View mainOrderView = view.findViewById(R.id.mainOrderView);
                    heightView.setVisibility(View.GONE);
                    orderDetialGoodsLiner.removeAllViews();
                    if (i > 0) {//控制间距的
                        mainOrderView.setVisibility(View.VISIBLE);
                    } else {
                        mainOrderView.setVisibility(View.GONE);
                    }
                    cardLayout.setBackground(context.getResources().getDrawable(R.drawable.service_order_detial_card_shape_down));
                    orderDetialGoodsLiner.setBackground(context.getResources().getDrawable(R.drawable.service_order_detial_card_shape_up));
                    if (subOrderListBean.getDeliverType() == 10 || subOrderListBean.getDeliverType() == 20) {
                        topShopLayout.setVisibility(View.VISIBLE);
                        topHomeLayout.setVisibility(View.GONE);
                        TextView address_txt_store = view.findViewById(R.id.address_txt_store);
                        TextView house_number_store = view.findViewById(R.id.house_number_store);
                        TextView time_store = view.findViewById(R.id.time_store);
                        ImageView title_img_clock_store = view.findViewById(R.id.title_img_clock_store);
//                        TextView phone_home = holder.getView(R.id.phone_home);
                        address_txt_store.setText(subOrderListBean.deliveryShopName);
                        house_number_store.setText(subOrderListBean.deliveryShopAddress + "");
                        if (subOrderListBean.deliveryDate != null && !TextUtils.isEmpty(subOrderListBean.deliveryDate)) {
                            title_img_clock_store.setVisibility(View.VISIBLE);
                            time_store.setVisibility(View.VISIBLE);
                            time_store.setText(subOrderListBean.deliveryDate.split(" ")[0] + subOrderListBean.deliveryTime);
//                            name_store.setText("由"+subOrderListBean.deliveryShopName+"配送");
                        } else {
                            title_img_clock_store.setVisibility(View.GONE);
                            time_store.setVisibility(View.GONE);
//                            name_store.setVisibility(View.GONE);
                        }
                        topShopLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ARouter.getInstance()
                                        .build(ServiceRoutes.SERVICE_NEARBYSTORES)
                                        .withString("shopId", subOrderListBean.deliveryShopId + "")
                                        .withString("provinceCode", subOrderListBean.merchantProvince + "")
                                        .withString("cityCode", subOrderListBean.merchantCity + "")
                                        .withString("areasCode", subOrderListBean.merchantDistrict + "")
                                        .navigation();
                            }
                        });
                    } else if (subOrderListBean.getDeliverType() == 11) {
                        topHomeLayout.setVisibility(View.VISIBLE);
                        topShopLayout.setVisibility(View.GONE);
                        TextView address_txt_home = view.findViewById(R.id.address_txt_home);
                        TextView house_number_home = view.findViewById(R.id.house_number_home);
                        TextView name_home = view.findViewById(R.id.name_home);
                        TextView phone_home = view.findViewById(R.id.phone_home);
                        TextView name_store = view.findViewById(R.id.name_store);
                        address_txt_home.setText(subOrderListBean.deliveryConsigneeProvinceName + subOrderListBean.deliveryConsigneeCityName + subOrderListBean.deliveryConsigneeDistrictName);
                        house_number_home.setText(subOrderListBean.deliveryConsigneeAddress);
                        name_home.setText(subOrderListBean.deliveryConsigneeName);
                        phone_home.setText(subOrderListBean.deliveryConsigneePhone);
                        name_store.setText("由"+subOrderListBean.deliveryShopName+"配送");
                    }
                    for (int j = 0; j < subOrderListBean.orderDetailList.size(); j++) {
                        final OrderList.OrderDetailListBean detailsBean = subOrderListBean.orderDetailList.get(j);
                        View goodsView = LayoutInflater.from(context).inflate(R.layout.service_item_order_flow_goods, null);
                        TextView mall_service_title = goodsView.findViewById(R.id.goodsTitle);
                        ConstraintLayout goods_finish_btn_constraintLayout = goodsView.findViewById(R.id.goods_finish_btn_constraintLayout);
                        TextView btn_commitBack = goodsView.findViewById(R.id.btn_commitBack);
                        TextView btn_assess = goodsView.findViewById(R.id.btn_assess);
                        TextView pointLable = goodsView.findViewById(R.id.pointLable);
                        TextView goodsSpace = goodsView.findViewById(R.id.goodsSpec);
                        TextView goodsCount = goodsView.findViewById(R.id.goodsCount);
                        TextView goodsMoney = goodsView.findViewById(R.id.goodsMoney);
                        ImageView actFlag = goodsView.findViewById(R.id.actFlag);
                        CornerImageView mall_service_img = goodsView.findViewById(R.id.goodsImg);
                        actFlag.setVisibility(View.GONE);
                        btn_assess.setVisibility(View.GONE);
                        pointLable.setVisibility(View.GONE);

                        goods_finish_btn_constraintLayout.setVisibility(View.GONE);
                        btn_commitBack.setVisibility(View.GONE);
                        if (orderFather.orderType != null) {
                            if ("0".equals(orderFather.orderType)) {//普通
                                actFlag.setVisibility(View.GONE);
                            } else if ("1".equals(orderFather.orderType)) {//砍价
                                actFlag.setVisibility(View.VISIBLE);
                                actFlag.setImageResource(R.drawable.act_kick);
                            } else if ("4".equals(orderFather.orderType)) {//拼团
                                actFlag.setVisibility(View.VISIBLE);
                                actFlag.setImageResource(R.drawable.act_pin);
                            } else if ("5".equals(orderFather.orderType)) {
                                pointLable.setVisibility(View.VISIBLE);
                                pointLable.getBackground().setAlpha(150);
                            } else if ("7".equals(orderFather.orderType)) {

                            }
                        }
                        if ("5".equals(orderFather.orderType)) {
                            if ("0".equals(FormatUtils.moneyKeep2Decimals(detailsBean.goodsPayAmount))) {
                                goodsMoney.setText(FormatUtils.moneyKeep2Decimals(detailsBean.goodsPoints) + "积分");
                            } else {
                                goodsMoney.setText(FormatUtils.moneyKeep2Decimals(detailsBean.goodsPoints) + "积分\n+¥" + FormatUtils.moneyKeep2Decimals(detailsBean.goodsPayAmount) + "");
                            }
                        } else {
                            goodsMoney.setText("¥" + FormatUtils.moneyKeep2Decimals(detailsBean.goodsAmount));
                        }
                        goodsCount.setText("x" + detailsBean.goodsQuantity);
                        mall_service_title.setText(detailsBean.goodsTitle);
                        goodsSpace.setText("");
                        if (!TextUtils.isEmpty(detailsBean.goodsSpecDesc)) {
                            goodsSpace.setText(detailsBean.goodsSpecDesc);
                        }
                        com.healthy.library.businessutil.GlideCopy.with(context).load(detailsBean.goodsImage)
                                .placeholder(R.drawable.img_1_1_default2)
                                .error(R.drawable.img_1_1_default)

                                .into(mall_service_img);
                        if (getModel().getOrderStatus() == 1) {
                            goods_finish_btn_constraintLayout.setVisibility(View.VISIBLE);
                        }
                        mall_service_img.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Map nokmap = new HashMap<String, String>();
                                nokmap.put("soure", "订单详情页点击商品栏");
                                MobclickAgent.onEvent(context, "btn_APP__MaternalandChildGoods_CommodityDetails", nokmap);
                                ARouter.getInstance().
                                        build(SecondRoutes.SECOND_SERVICE_DETAIL)
                                        .withString("id", detailsBean.goodsId + "")
                                        .withString("shopId", orderFather.shopId)
                                        .withString("marketingType", "5".equals(orderFather.orderType) ? "5" : null)
                                        .navigation();
                            }
                        });
                        mall_service_title.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Map nokmap = new HashMap<String, String>();
                                nokmap.put("soure", "订单详情页点击商品栏");
                                MobclickAgent.onEvent(context, "btn_APP__MaternalandChildGoods_CommodityDetails", nokmap);
                                ARouter.getInstance().
                                        build(SecondRoutes.SECOND_SERVICE_DETAIL)
                                        .withString("id", detailsBean.goodsId + "")
                                        .withString("shopId", orderFather.shopId)
                                        .withString("marketingType", "5".equals(orderFather.orderType) ? "5" : null)
                                        .navigation();
                            }
                        });
                        orderDetialGoodsLiner.addView(goodsView);
                    }
                    topViewLiner.addView(view);
                }
            }
        } else if (orderChild != null && orderFather == null) {//子订单
            if (orderChild.getOrderStatus() == 0) {
                no_pay_title_rel.setVisibility(View.VISIBLE);
                extracting_title_rel.setVisibility(View.GONE);
                finish_title_rel.setVisibility(View.GONE);
                cancle_title_rel.setVisibility(View.GONE);
                no_receipt_title_rel.setVisibility(View.GONE);
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                } else {
                    mSeconds = Integer.parseInt(orderChild.residuePayTime);//后台返回剩余支付时间：秒数
                }
                if (mSeconds > 0) {
                    countDownTimer = new CountDownTimer(mSeconds * 1000, 1000) {
                        public void onTick(long millisUntilFinished) {
                            millisUntilFinished = millisUntilFinished / 1000;
                            mSeconds = (int) millisUntilFinished;
                            int hours = (int) (millisUntilFinished / (60 * 60));
                            int minutes = (int) millisUntilFinished / 60 % 60;
                            int seconds = (int) millisUntilFinished % 60;
                            clock_time.setText(String.format("%02d", Math.max(0, hours)) + ":"
                                    + String.format("%02d", Math.max(0, minutes)) + ":"
                                    + String.format("%02d", Math.max(0, seconds)));
                        }

                        public void onFinish() {
                            //剩余支付时间结束后进行相应逻辑处理
                            clock_time.setText("0");
                        }
                    }.start();
                } else {
                    clock_time.setText("0");
                }
            } else if (orderChild.getOrderStatus() == 1) {
                no_pay_title_rel.setVisibility(View.GONE);
                finish_title_rel.setVisibility(View.GONE);
                cancle_title_rel.setVisibility(View.GONE);
                no_receipt_title_rel.setVisibility(View.GONE);
                extracting_title_rel.setVisibility(View.VISIBLE);
                if (orderChild.getDeliverType() == 10 || orderChild.getDeliverType() == 20) {
                    extracting_title.setText("待核销");
                    extracting_iv.setImageDrawable(context.getResources().getDrawable(R.mipmap.order_detial_zxing));
                    ticketLayout.setVisibility(View.VISIBLE);
                } else if (orderChild.getDeliverType() == 11) {
                    extracting_title.setText("待发货");
                    extracting_iv.setImageDrawable(context.getResources().getDrawable(R.mipmap.order_detial_extracting));
                    ticketLayout.setVisibility(View.VISIBLE);
                }
                if (!TextUtils.isEmpty(orderChild.ticket) && !TextUtils.isEmpty(orderChild.ticketContent)) {
                    Bitmap zxing = new ImageUtilK().base64ToBitmap(orderChild.ticketContent);
                    img_zxing.setImageBitmap(zxing);
                    String bankCard = orderChild.ticket;
                    String str = "";
                    str = bankCard.substring(0, 4) + " " + bankCard.substring(4, bankCard.length());
                    zxing_value.setText(str);
                }
            } else if (orderChild.getOrderStatus() == 2) {
                ticketLayout.setVisibility(View.GONE);
                no_pay_title_rel.setVisibility(View.GONE);
                extracting_title_rel.setVisibility(View.GONE);
                cancle_title_rel.setVisibility(View.GONE);
                no_receipt_title_rel.setVisibility(View.GONE);
                finish_title_rel.setVisibility(View.VISIBLE);
            } else if (orderChild.getOrderStatus() == 3) {
                no_pay_title_rel.setVisibility(View.GONE);
                extracting_title_rel.setVisibility(View.GONE);
                finish_title_rel.setVisibility(View.GONE);
                no_receipt_title_rel.setVisibility(View.GONE);
                cancle_title_rel.setVisibility(View.VISIBLE);
                cancle_title.setText("已取消");
            } else if (orderChild.getOrderStatus() == 4) {
                no_pay_title_rel.setVisibility(View.GONE);
                extracting_title_rel.setVisibility(View.GONE);
                finish_title_rel.setVisibility(View.GONE);
                no_receipt_title_rel.setVisibility(View.GONE);
                cancle_title_rel.setVisibility(View.VISIBLE);
                cancle_title.setText("已退款");
            } else if (orderChild.getOrderStatus() == 5) {
                no_pay_title_rel.setVisibility(View.GONE);
                finish_title_rel.setVisibility(View.GONE);
                cancle_title_rel.setVisibility(View.GONE);
                no_receipt_title_rel.setVisibility(View.GONE);
                extracting_title_rel.setVisibility(View.VISIBLE);
                if (orderChild.getDeliverType() == 10 || orderChild.getDeliverType() == 20) {
                    extracting_title.setText("待核销");
                    extracting_iv.setImageDrawable(context.getResources().getDrawable(R.mipmap.order_detial_zxing));
                    ticketLayout.setVisibility(View.VISIBLE);
                } else if (orderChild.getDeliverType() == 11) {
                    extracting_title.setText("待发货");
                    extracting_iv.setImageDrawable(context.getResources().getDrawable(R.mipmap.order_detial_extracting));
                    ticketLayout.setVisibility(View.VISIBLE);
                }
                if (!TextUtils.isEmpty(orderChild.ticket) && !TextUtils.isEmpty(orderChild.ticketContent)) {
                    Bitmap zxing = new ImageUtilK().base64ToBitmap(orderChild.ticketContent);
                    img_zxing.setImageBitmap(zxing);
                    String bankCard = orderChild.ticket;
                    String str = "";
                    str = bankCard.substring(0, 4) + " " + bankCard.substring(4, bankCard.length());
                    zxing_value.setText(str);
                }
            } else if (orderChild.getOrderStatus() == 6) {
                no_pay_title_rel.setVisibility(View.GONE);
                extracting_title_rel.setVisibility(View.GONE);
                finish_title_rel.setVisibility(View.GONE);
                cancle_title_rel.setVisibility(View.GONE);
                no_receipt_title_rel.setVisibility(View.VISIBLE);
//                    if (orderFather.getLatestConfirmDeliveryTime() == null || TextUtils.isEmpty(getModel().getLatestConfirmDeliveryTime())) {
//                        receipt_content.setText("");
//                    } else {
//                        String[] millisFinished = getMinutes(getModel().getLatestConfirmDeliveryTime());
//                        receipt_content.setText("剩余" + millisFinished[0] + "天" + millisFinished[1] + "时自动确认收货");
//                    }
            }
            topViewLiner.removeAllViews();
            View view = LayoutInflater.from(context).inflate(R.layout.item_order_detial_top_delivery_layout, null);
            ConstraintLayout topHomeLayout = view.findViewById(R.id.topHomeLayout);
            ConstraintLayout topShopLayout = view.findViewById(R.id.topShopLayout);
            LinearLayout orderDetialGoodsLiner = view.findViewById(R.id.orderDetialGoodsLiner);
            LinearLayout cardLayout = view.findViewById(R.id.cardLayout);
            View heightView = view.findViewById(R.id.heightView);
            heightView.setVisibility(View.VISIBLE);
            orderDetialGoodsLiner.removeAllViews();
            cardLayout.setBackground(context.getResources().getDrawable(R.drawable.shape_order_detial_cardview));
            orderDetialGoodsLiner.setBackground(context.getResources().getDrawable(R.drawable.shape_order_detial_cardview));
            if (orderChild.getDeliverType() == 10 || orderChild.getDeliverType() == 20) {
                topShopLayout.setVisibility(View.VISIBLE);
                topHomeLayout.setVisibility(View.GONE);
                TextView address_txt_store = view.findViewById(R.id.address_txt_store);
                TextView house_number_store = view.findViewById(R.id.house_number_store);
                TextView time_store = view.findViewById(R.id.time_store);
                ImageView title_img_clock_store = view.findViewById(R.id.title_img_clock_store);
                //TextView phone_home = holder.getView(R.id.phone_home);
                address_txt_store.setText(orderChild.deliveryShopName);
                house_number_store.setText(orderChild.deliveryShopAddress + "");
                if (orderChild.deliveryDate != null && !TextUtils.isEmpty(orderChild.deliveryDate)) {
                    title_img_clock_store.setVisibility(View.VISIBLE);
                    time_store.setVisibility(View.VISIBLE);
                    time_store.setText(orderChild.deliveryDate.split(" ")[0] + orderChild.deliveryTime);
                } else {
                    title_img_clock_store.setVisibility(View.GONE);
                    time_store.setVisibility(View.GONE);
                }
                topShopLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ARouter.getInstance()
                                .build(ServiceRoutes.SERVICE_NEARBYSTORES)
                                .withString("shopId", orderChild.deliveryShopId + "")
                                .withString("provinceCode", orderChild.merchantProvince + "")
                                .withString("cityCode", orderChild.merchantCity + "")
                                .withString("areasCode", orderChild.merchantDistrict + "")
                                .navigation();
                    }
                });
            } else if (orderChild.getDeliverType() == 11) {
                topHomeLayout.setVisibility(View.VISIBLE);
                topShopLayout.setVisibility(View.GONE);
                TextView address_txt_home = view.findViewById(R.id.address_txt_home);
                TextView house_number_home = view.findViewById(R.id.house_number_home);
                TextView name_home = view.findViewById(R.id.name_home);
                TextView phone_home = view.findViewById(R.id.phone_home);
                TextView name_store = view.findViewById(R.id.name_store);
                address_txt_home.setText(orderChild.deliveryConsigneeProvinceName + orderChild.deliveryConsigneeCityName + orderChild.deliveryConsigneeDistrictName);
                house_number_home.setText(orderChild.deliveryConsigneeAddress);
                name_home.setText(orderChild.deliveryConsigneeName);
                phone_home.setText(orderChild.deliveryConsigneePhone);
                name_store.setText("由"+orderChild.deliveryShopName+"配送");
            }

            if (orderChild.orderDetailList != null && orderChild.orderDetailList.size() > 0) {
                for (int i = 0; i < orderChild.orderDetailList.size(); i++) {
                    final OrderList.OrderDetailListBean detailsBean = orderChild.orderDetailList.get(i);
                    View goodsView = LayoutInflater.from(context).inflate(R.layout.service_item_order_flow_goods, null);
                    TextView mall_service_title = goodsView.findViewById(R.id.goodsTitle);
                    ConstraintLayout goods_finish_btn_constraintLayout = goodsView.findViewById(R.id.goods_finish_btn_constraintLayout);
                    TextView btn_commitBack = goodsView.findViewById(R.id.btn_commitBack);
                    TextView btn_assess = goodsView.findViewById(R.id.btn_assess);
                    TextView goodsSpace = goodsView.findViewById(R.id.goodsSpec);
                    TextView goodsCount = goodsView.findViewById(R.id.goodsCount);
                    TextView goodsMoney = goodsView.findViewById(R.id.goodsMoney);
                    TextView pointLable = goodsView.findViewById(R.id.pointLable);
                    ImageView actFlag = goodsView.findViewById(R.id.actFlag);
                    CornerImageView mall_service_img = goodsView.findViewById(R.id.goodsImg);
                    actFlag.setVisibility(View.GONE);
                    btn_assess.setVisibility(View.GONE);
                    pointLable.setVisibility(View.GONE);

                    goods_finish_btn_constraintLayout.setVisibility(View.GONE);
                    btn_commitBack.setVisibility(View.GONE);
                    if (orderChild.orderType != null) {
                        if ("0".equals(orderChild.orderType)) {//普通
                            actFlag.setVisibility(View.GONE);
                        } else if ("1".equals(orderChild.orderType)) {//砍价
                            actFlag.setVisibility(View.VISIBLE);
                            actFlag.setImageResource(R.drawable.act_kick);
                        } else if ("4".equals(orderChild.orderType)) {//拼团
                            actFlag.setVisibility(View.VISIBLE);
                            actFlag.setImageResource(R.drawable.act_pin);
                        } else if ("5".equals(orderChild.orderType)) {
                            pointLable.setVisibility(View.VISIBLE);
                            pointLable.getBackground().setAlpha(150);
                        } else if ("7".equals(orderChild.orderType)) {

                        }
                    }

                    if ("5".equals(orderChild.orderType)) {
                        if ("0".equals(FormatUtils.moneyKeep2Decimals(detailsBean.goodsPayAmount))) {
                            goodsMoney.setText(FormatUtils.moneyKeep2Decimals(detailsBean.goodsPoints) + "积分");
                        } else {
                            goodsMoney.setText(FormatUtils.moneyKeep2Decimals(detailsBean.goodsPoints) + "积分\n+¥" + FormatUtils.moneyKeep2Decimals(detailsBean.goodsPayAmount) + "");
                        }
                    } else {
                        goodsMoney.setText("¥" + FormatUtils.moneyKeep2Decimals(detailsBean.goodsAmount));
                    }
                    goodsCount.setText("x" + detailsBean.goodsQuantity);
                    mall_service_title.setText(detailsBean.goodsTitle);
                    goodsSpace.setText("");
                    if (!TextUtils.isEmpty(detailsBean.goodsSpecDesc)) {
                        goodsSpace.setText(detailsBean.goodsSpecDesc);
                    }
                    com.healthy.library.businessutil.GlideCopy.with(context).load(detailsBean.goodsImage)
                            .placeholder(R.drawable.img_1_1_default2)
                            .error(R.drawable.img_1_1_default)

                            .into(mall_service_img);
                    if (getModel().getOrderStatus() == 1) {
                        goods_finish_btn_constraintLayout.setVisibility(View.VISIBLE);
                    }
                    mall_service_img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Map nokmap = new HashMap<String, String>();
                            nokmap.put("soure", "订单详情页点击商品栏");
                            MobclickAgent.onEvent(context, "btn_APP__MaternalandChildGoods_CommodityDetails", nokmap);
                            ARouter.getInstance().
                                    build(SecondRoutes.SECOND_SERVICE_DETAIL)
                                    .withString("id", detailsBean.goodsId + "")
                                    .withString("shopId", orderChild.shopId != null ? orderChild.shopId : SpUtils.getValue(context, SpKey.CHOSE_SHOP))
                                    .withString("marketingType", "5".equals(orderChild.orderType) ? "5" : null)
                                    .navigation();
                        }
                    });
                    mall_service_title.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Map nokmap = new HashMap<String, String>();
                            nokmap.put("soure", "订单详情页点击商品栏");
                            MobclickAgent.onEvent(context, "btn_APP__MaternalandChildGoods_CommodityDetails", nokmap);
                            ARouter.getInstance().
                                    build(SecondRoutes.SECOND_SERVICE_DETAIL)
                                    .withString("id", detailsBean.goodsId + "")
                                    .withString("shopId", orderChild.shopId != null ? orderChild.shopId : SpUtils.getValue(context, SpKey.CHOSE_SHOP))
                                    .withString("marketingType", "5".equals(orderChild.orderType) ? "5" : null)
                                    .navigation();
                        }
                    });
                    orderDetialGoodsLiner.addView(goodsView);
                }
                topViewLiner.addView(view);
            }
        }
    }

    // 获取剩余自动确认收货时间
    public String[] getMinutes(String newTime) {
        long diff = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = dateFormat.format(new Date());
        try {
            Date dateNew = dateFormat.parse(newTime);
            Date dateOld = dateFormat.parse(time);
            long NTime = dateNew.getTime();
            long OTime = dateOld.getTime();
            diff = NTime - OTime;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return DateUtils.getDistanceTimeOnlySecond(diff);
    }

    /**
     * 重设 view 的高度
     */
    public void setViewLayoutParams(View view, int nHeight) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp.height != nHeight) {
            lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            lp.height = nHeight;
            view.setLayoutParams(lp);
        }
    }
}
