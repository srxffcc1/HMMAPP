package com.health.servicecenter.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.servicecenter.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.constant.Constants;
import com.healthy.library.model.OrderList;

public class OrderDetialPayTypeAdapter extends BaseAdapter<OrderList> {
    private boolean isAdd = false;//是否加载过
    boolean isShow = false;//是否展示更多

    public OrderDetialPayTypeAdapter() {
        this(R.layout.item_order_detial_pay_type);
        isAdd = false;
    }

    public OrderDetialPayTypeAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        final TextView img_to_down = holder.itemView.findViewById(R.id.img_to_down);
        final ConstraintLayout pay_down = holder.itemView.findViewById(R.id.pay_down);
        final TextView order_num = holder.getView(R.id.order_num);
        final TextView order_time = holder.getView(R.id.order_time);
        final TextView pay_type = holder.getView(R.id.pay_type);
        final TextView pay_time = holder.getView(R.id.pay_time);
        final TextView send_time = holder.getView(R.id.send_time);
        final TextView create_time = holder.getView(R.id.create_time);

        final LinearLayout orderRemarkLL = holder.getView(R.id.orderRemarkLL);
        final OrderList.OrderChild orderChild = getModel().orderChild;
        final OrderList.OrderFather orderFather = getModel().orderFather;
        orderRemarkLL.removeAllViews();
        if (orderChild == null && orderFather != null) {//主订单.
            if (orderFather.subOrderList != null && orderFather.subOrderList.size() > 0) {
                order_num.setText("订单编号：" + orderFather.orderNum);
                order_time.setText("创建时间：" + orderFather.createTime);
                order_num.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        copyContentToClipboard(orderFather.orderNum, context);
                    }
                });
                if (orderFather.getOrderRace() == 3) {
                    OrderList.OrderFather.SubOrderListBean subOrderListBean = orderFather.subOrderList.get(0);
                    View view = LayoutInflater.from(context).inflate(R.layout.item_order_detial_remark_layout, null);
                    TextView order_remark = view.findViewById(R.id.order_remark);
                    if (subOrderListBean.remark != null && !TextUtils.isEmpty(subOrderListBean.remark)) {
                        order_remark.setText("订单备注：" + subOrderListBean.remark);
                        orderRemarkLL.addView(view);
                    }
                } else {
                    for (int i = 0; i < orderFather.subOrderList.size(); i++) {
                        OrderList.OrderFather.SubOrderListBean subOrderListBean = orderFather.subOrderList.get(i);
                        View view = LayoutInflater.from(context).inflate(R.layout.item_order_detial_remark_layout, null);
                        TextView order_remark = view.findViewById(R.id.order_remark);
                        if (subOrderListBean.remark != null && !TextUtils.isEmpty(subOrderListBean.remark)) {
                            order_remark.setText("订单备注：" + subOrderListBean.remark);
                            orderRemarkLL.addView(view);
                        }
                    }
                }
                if (orderFather.getOrderStatus() == 0) {//未支付
                    img_to_down.setVisibility(View.GONE);
                    pay_type.setVisibility(View.GONE);
                    pay_time.setVisibility(View.GONE);
                    send_time.setVisibility(View.GONE);
                    create_time.setVisibility(View.GONE);
                } else if (orderFather.getOrderStatus() == 3) {//已取消
                    img_to_down.setVisibility(View.GONE);
                    pay_type.setVisibility(View.VISIBLE);
                    pay_time.setVisibility(View.GONE);
                    send_time.setVisibility(View.GONE);
                    create_time.setVisibility(View.GONE);
                    pay_type.setText("取消时间：" + orderFather.cancelTime + "");
                } else {
                    img_to_down.setVisibility(View.VISIBLE);
                    pay_down.setVisibility(View.GONE);
                    if (orderFather.getOrderRace() == 4) {
                        if (!TextUtils.isEmpty(orderFather.payType) && orderFather.payType != null) {
                            if (Integer.parseInt(orderFather.payType) == Integer.parseInt(Constants.PAY_IN_A_LI) || Integer.parseInt(orderFather.payType) == 1) {
                                pay_type.setText("支付方式：积分" + "+支付宝");
                            } else if (Integer.parseInt(orderFather.payType) == Integer.parseInt(Constants.PAY_IN_WX) || Integer.parseInt(orderFather.payType) == 2) {
                                pay_type.setText("支付方式：积分" + "+微信");
                            } else if (Integer.parseInt(orderFather.payType) == 3) {
                                pay_type.setText("支付方式：积分" + "+微信小程序");
                            } else {
                                pay_type.setVisibility(View.GONE);
                            }
                        } else {
                            pay_type.setText("支付方式：积分");
                        }
                    } else {
                        if (!TextUtils.isEmpty(orderFather.payType) && orderFather.payType != null) {
                            if (Integer.parseInt(orderFather.payType) == Integer.parseInt(Constants.PAY_IN_A_LI) || Integer.parseInt(orderFather.payType) == 1) {
                                pay_type.setText("支付方式：支付宝");
                            } else if (Integer.parseInt(orderFather.payType) == Integer.parseInt(Constants.PAY_IN_WX) || Integer.parseInt(orderFather.payType) == 2) {
                                pay_type.setText("支付方式：微信");
                            } else if (Integer.parseInt(orderFather.payType) == 3) {
                                pay_type.setText("支付方式：微信小程序");
                            } else {
                                pay_type.setVisibility(View.GONE);
                            }
                        } else {
                            pay_type.setVisibility(View.GONE);
                        }
                    }
                    pay_time.setText("付款时间：" + orderFather.paySuccessTime + "");
                    if (TextUtils.isEmpty(orderFather.paySuccessTime)) {
                        pay_time.setVisibility(View.GONE);
                    }
                    send_time.setVisibility(View.GONE);
                    create_time.setVisibility(View.GONE);
//                    if (!TextUtils.isEmpty(subList.get(0).merchantDeliveryTime) & subList.get(0).merchantDeliveryTime != null) {
//                        send_time.setVisibility(View.VISIBLE);
//                        send_time.setText("发货时间：" + subList.get(0).merchantDeliveryTime);
//                    } else {
//                        send_time.setVisibility(View.GONE);
//                    }
//                    if (!TextUtils.isEmpty(subList.get(0).takeDeliveryTime) & subList.get(0).takeDeliveryTime != null) {
//                        create_time.setVisibility(View.VISIBLE);
//                        create_time.setText("成交时间：" + subList.get(0).takeDeliveryTime);
//                    } else {
//                        create_time.setVisibility(View.GONE);
//                    }
                }
            }
        } else if (orderChild != null && orderFather == null) {//子订单
            order_num.setText("订单编号：" + orderChild.orderNum);
            order_time.setText("创建时间：" + orderChild.createTime);
            order_num.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    copyContentToClipboard(orderChild.orderNum, context);
                }
            });
            orderRemarkLL.removeAllViews();
            View view = LayoutInflater.from(context).inflate(R.layout.item_order_detial_remark_layout, null);
            TextView order_remark = view.findViewById(R.id.order_remark);
            if (orderChild.remark != null && !TextUtils.isEmpty(orderChild.remark)) {
                order_remark.setVisibility(View.VISIBLE);
                order_remark.setText("订单备注：" + orderChild.remark);
                orderRemarkLL.addView(view);
            } else {
                order_remark.setVisibility(View.GONE);
            }
            if (orderChild.getOrderStatus() == 0) {//未支付
                img_to_down.setVisibility(View.GONE);
                pay_type.setVisibility(View.GONE);
                pay_time.setVisibility(View.GONE);
                send_time.setVisibility(View.GONE);
                create_time.setVisibility(View.GONE);
            } else if (orderChild.getOrderStatus() == 3) {//已取消
                img_to_down.setVisibility(View.GONE);
                pay_type.setVisibility(View.VISIBLE);
                pay_time.setVisibility(View.GONE);
                send_time.setVisibility(View.GONE);
                create_time.setVisibility(View.GONE);
                pay_type.setText("取消时间：" + orderChild.cancelTime + "");
            } else {
                img_to_down.setVisibility(View.VISIBLE);
                pay_down.setVisibility(View.GONE);
                if (orderChild.getOrderRace() == 4) {
                    if (!TextUtils.isEmpty(orderChild.payType) && orderChild.payType != null) {
                        if (Integer.parseInt(orderChild.payType) == Integer.parseInt(Constants.PAY_IN_A_LI) || Integer.parseInt(orderChild.payType) == 1) {
                            pay_type.setText("支付方式：积分" + "+支付宝");
                        } else if (Integer.parseInt(orderChild.payType) == Integer.parseInt(Constants.PAY_IN_WX) || Integer.parseInt(orderChild.payType) == 2) {
                            pay_type.setText("支付方式：积分" + "+微信");
                        } else if (Integer.parseInt(orderChild.payType) == 3) {
                            pay_type.setText("支付方式：积分" + "+微信小程序");
                        } else {
                            pay_type.setVisibility(View.GONE);
                        }
                    } else {
                        pay_type.setText("支付方式：积分");
                    }
                } else {
                    if (!TextUtils.isEmpty(orderChild.payType) && orderChild.payType != null) {
                        if (Integer.parseInt(orderChild.payType) == Integer.parseInt(Constants.PAY_IN_A_LI) || Integer.parseInt(orderChild.payType) == 1) {
                            pay_type.setText("支付方式：支付宝");
                        } else if (Integer.parseInt(orderChild.payType) == Integer.parseInt(Constants.PAY_IN_WX) || Integer.parseInt(orderChild.payType) == 2) {
                            pay_type.setText("支付方式：微信");
                        } else if (Integer.parseInt(orderChild.payType) == 3) {
                            pay_type.setText("支付方式：微信小程序");
                        } else {
                            pay_type.setVisibility(View.GONE);
                        }
                    } else {
                        pay_type.setVisibility(View.GONE);
                    }
                }
                pay_time.setText("付款时间：" + orderChild.paySuccessTime + "");
                if (TextUtils.isEmpty(orderChild.paySuccessTime)) {
                    pay_time.setVisibility(View.GONE);
                }
                send_time.setVisibility(View.GONE);
                create_time.setVisibility(View.GONE);
//                if (!TextUtils.isEmpty(orderChild.merchantDeliveryTime) & orderChild.merchantDeliveryTime != null) {
//                    send_time.setVisibility(View.VISIBLE);
//                    send_time.setText("发货时间：" + orderChild.merchantDeliveryTime);
//                } else {
//                    send_time.setVisibility(View.GONE);
//                }
//                if (!TextUtils.isEmpty(orderChild.takeDeliveryTime) & orderChild.takeDeliveryTime != null) {
//                    create_time.setVisibility(View.VISIBLE);
//                    create_time.setText("成交时间：" + orderChild.takeDeliveryTime);
//                } else {
//                    create_time.setVisibility(View.GONE);
//                }
            }
        }
        if (!isAdd) {
            isAdd = true;
        }
        if (isAdd && isShow) {//说明在展开状态 并且在刷新数据  重置下
            isShow = false;
            img_to_down.setVisibility(View.VISIBLE);
            img_to_down.setText("查看更多");
            Drawable drawableRight = context.getResources().getDrawable(
                    R.mipmap.order_detial_pay_type_up);

            img_to_down.setCompoundDrawablesWithIntrinsicBounds(null,
                    null, drawableRight, null);
            pay_down.setVisibility(View.GONE);
        }
        img_to_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isShow) {
                    img_to_down.setVisibility(View.VISIBLE);
                    img_to_down.setText("收起");
                    Drawable drawableRight = context.getResources().getDrawable(
                            R.mipmap.order_detial_pay_type_down);

                    img_to_down.setCompoundDrawablesWithIntrinsicBounds(null,
                            null, drawableRight, null);
                    pay_down.setVisibility(View.VISIBLE);
                    isShow = true;
                } else {
                    img_to_down.setVisibility(View.VISIBLE);
                    img_to_down.setText("查看更多");
                    Drawable drawableRight = context.getResources().getDrawable(
                            R.mipmap.order_detial_pay_type_up);

                    img_to_down.setCompoundDrawablesWithIntrinsicBounds(null,
                            null, drawableRight, null);
                    pay_down.setVisibility(View.GONE);
                    isShow = false;
                }
            }
        });
    }

    /**
     * 复制内容到剪贴板
     *
     * @param content
     * @param context
     */
    public void copyContentToClipboard(String content, Context context) {
        try {
            //获取剪贴板管理器：
            ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            // 创建普通字符型ClipData
            ClipData mClipData = ClipData.newPlainText("Label", content);
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData);
            Toast.makeText(context, "已复制订单号到剪贴板", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
