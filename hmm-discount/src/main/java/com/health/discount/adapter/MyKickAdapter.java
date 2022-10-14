package com.health.discount.adapter;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.SparseArray;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.health.discount.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.model.MyKickList;
import com.healthy.library.routes.DiscountRoutes;
import com.healthy.library.routes.MallRoutes;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.DateUtils;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.LogUtils;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.ImageTextView;
import com.umeng.commonsdk.debug.D;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyKickAdapter extends BaseAdapter<MyKickList> {

    private SparseArray<CountDownTimer> countDownCounters = new SparseArray<>();

    public MyKickAdapter() {
        this(R.layout.my_kick_adapter_layout);
    }

    public MyKickAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        ImageTextView orderTitle = holder.getView(R.id.orderTitle);
        CornerImageView goodsImg = holder.getView(R.id.goodsImg);
        TextView goodsName = holder.getView(R.id.goodsName);
        TextView orderStatusValue = holder.getView(R.id.orderStatusValue);
        LinearLayout goodsTimeLL = holder.getView(R.id.goodsTimeLL);
        TextView tv_total_price_pre = holder.getView(R.id.tv_total_price_pre);
        TextView tv_total_price = holder.getView(R.id.tv_total_price);
        TextView tv_need_price = holder.getView(R.id.tv_need_price);
        TextView tv_need_price_pre = holder.getView(R.id.tv_need_price_pre);
        TextView kickHour = holder.getView(R.id.kickHour);
        TextView kickMin = holder.getView(R.id.kickMin);
        TextView kickSec = holder.getView(R.id.kickSec);
        TextView buttonLeft = holder.getView(R.id.buttonLeft);
        TextView buttonRight = holder.getView(R.id.buttonRight);
        final MyKickList kick = getDatas().get(position);
        String statusValue = "";
        String statusColor = "#fff02846";
        if (kick != null) {
            CountDownTimer countDownTimer = countDownCounters.get(kickHour.hashCode());
            double chajia = new BigDecimal(kick.goodsPlatformPrice + "").subtract(new BigDecimal(kick.floorPrice + "")).subtract(new BigDecimal(kick.discountMoney + "")).doubleValue();
            double nowjia = new BigDecimal(kick.goodsPlatformPrice + "").subtract(new BigDecimal(kick.discountMoney + "")).doubleValue();
            buttonLeft.setVisibility(View.VISIBLE);
            buttonRight.setVisibility(View.VISIBLE);
            if ("砍价成功-待购买".equals(kick.bargainStatusStr)) {
                buttonLeft.setVisibility(View.GONE);
                goodsTimeLL.setVisibility(View.VISIBLE);
                buttonRight.setVisibility(View.VISIBLE);
                buttonRight.setText("立即购买");
                checkTimeOut(countDownTimer, kick, kickHour, kickMin, kickSec);//活动时间倒计时
                statusValue = "砍价成功-待购买";
                statusColor = "#fff02846";
                tv_total_price.setTextColor(Color.parseColor("#222222"));
                buttonRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ARouter.getInstance()
                                .build(DiscountRoutes.DIS_KICKDETAIL)
                                .withString("bargainMemberId", kick.bargainMemberId)
                                .withString("bargainId", kick.id + "")
                                .navigation();
                    }
                });
            } else if ("砍价失败".equals(kick.bargainStatusStr)) {
                buttonLeft.setVisibility(View.GONE);
                buttonRight.setVisibility(View.GONE);
                goodsTimeLL.setVisibility(View.GONE);
                statusValue = "砍价失败";
                statusColor = "#666666";
                tv_total_price.setTextColor(Color.parseColor("#222222"));

            } else if ("砍价成功-待支付".equals(kick.bargainStatusStr)) {
                if (kick.orderInfo != null) {
                    if (kick.orderInfo.orderStatus == 3) {
                        buttonLeft.setVisibility(View.VISIBLE);
                        buttonRight.setVisibility(View.GONE);
                        goodsTimeLL.setVisibility(View.GONE);
                        buttonLeft.setText("订单详情");
                        statusValue = "砍价成功-已取消";
                        statusColor = "#666666";
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ARouter.getInstance()
                                        .build(ServiceRoutes.SERVICE_ORDERLISTDETIAL)
                                        .withString("orderId", kick.orderInfo.orderId + "")
                                        .withString("function", "25006")
                                        .navigation();
                            }
                        });
                    } else {
                        buttonLeft.setVisibility(View.GONE);
                        buttonRight.setVisibility(View.VISIBLE);
                        buttonRight.setText("立即支付");
                        statusValue = "待支付";
                        statusColor = "#fff02846";
                        if (kick.orderInfo != null) {
                            goodsTimeLL.setVisibility(View.VISIBLE);
                            if (kick.orderInfo.latestPayTime != null) {
                                checkLastPayTimeOut(countDownTimer, kick, kickHour, kickMin, kickSec);
                            }
                        }
                        buttonRight.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ARouter.getInstance()
                                        .build(ServiceRoutes.SERVICE_CHECKOUT_COUNTER)
                                        .withString("orderId", kick.orderInfo.orderId + "")
                                        .navigation();
                            }
                        });
                    }
                }
            } else if ("砍价成功".equals(kick.bargainStatusStr)) {
                buttonLeft.setText("订单详情");
                buttonRight.setVisibility(View.GONE);
                goodsTimeLL.setVisibility(View.GONE);
                statusValue = "砍价成功";
                statusColor = "#222222";
                buttonLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ARouter.getInstance()
                                .build(ServiceRoutes.SERVICE_ORDERLISTDETIAL)
                                .withString("orderId", kick.orderInfo.orderId + "")
                                .withString("function", "25006")
                                .navigation();
                    }
                });
            } else if (kick.bargainStatusStr.contains("砍价中")) {//砍价中
                goodsTimeLL.setVisibility(View.VISIBLE);
                statusValue = "砍价中";
                statusColor = "#fff02846";
//                buttonLeft.setText("直接购买");
                buttonLeft.setVisibility(View.GONE);
                buttonRight.setText("继续砍价");
                checkTimeOut(countDownTimer, kick, kickHour, kickMin, kickSec);//活动时间倒计时
                tv_total_price.setText(FormatUtils.moneyKeep2Decimals(kick.floorPrice));
                tv_need_price_pre.setVisibility(View.VISIBLE);
                tv_need_price.setText(FormatUtils.moneyKeep2Decimals(chajia));
                buttonRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ARouter.getInstance()
                                .build(DiscountRoutes.DIS_KICKDETAIL)
                                .withString("bargainMemberId", kick.bargainMemberId)
                                .withString("bargainId", kick.id + "")
                                .navigation();
                    }
                });
            }

            orderTitle.setDrawable(R.drawable.order_detial_clock_grey, context);
            orderTitle.setText(kick.joinTime);
            tv_total_price.setText(FormatUtils.moneyKeep2Decimals(kick.floorPrice));
            if (kick.bargainStatusStr.contains("砍价中")) {//砍价中
                tv_total_price_pre.setText("底价¥");
            } else {
                tv_total_price_pre.setText("砍成价¥");
                tv_total_price.setText(FormatUtils.moneyKeep2Decimals(nowjia));
            }
            com.healthy.library.businessutil.GlideCopy.with(context)
                    .load(kick.goodsImage)
                    .error(R.drawable.img_1_1_default)
                    .placeholder(R.drawable.img_1_1_default2)
                    .into(goodsImg);
            goodsName.setText(kick.goodsTitle);
            orderStatusValue.setText(statusValue);
            orderStatusValue.setTextColor(Color.parseColor(statusColor));
            goodsName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click(kick);
                }
            });
            goodsImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click(kick);
                }
            });
        }
    }

    private void click(MyKickList kick) {
        ARouter.getInstance()
                .build(DiscountRoutes.DIS_KICKDETAIL)
                .withString("bargainMemberId", kick.bargainMemberId)
                .withString("bargainId", kick.id + "")
                .navigation();

    }

    private void checkTimeOut(CountDownTimer countDownTimer, MyKickList url, final TextView kickHour, final TextView kickMin, final TextView kickSec) {
        Date startTime = null;
        Date endTime = null;
        try {
            startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(url.joinTime);
            endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(url.endTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long nd = 1000 * url.bargainTimeLength * 60 * 60;//加入时间之后需要多少小时
        long desorg = startTime.getTime() + nd;
        long timer = startTime.getTime() + nd;
        if (endTime != null && endTime.getTime() < timer) {
            timer = endTime.getTime();
            desorg = endTime.getTime();
        }
        timer = timer - System.currentTimeMillis();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        if (timer > 0) {
            //System.out.println("开始计时");
            final long finalTimer = timer;
            final long finalDesorg = desorg;
            countDownTimer = new CountDownTimer(finalTimer, 1000) {
                public void onTick(long millisUntilFinished) {
                    String[] array = DateUtils.getDistanceTimeNoDay(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), DateUtils.formatLongAll(finalDesorg + ""));
                    kickHour.setText(array[0]);
                    kickMin.setText(array[1]);
                    kickSec.setText(array[2]);
                }

                public void onFinish() {
                    kickHour.setText("00");
                    kickMin.setText("00");
                    kickSec.setText("00");
                }
            }.start();
            //将此 countDownTimer 放入list
            countDownCounters.put(kickHour.hashCode(), countDownTimer);
        } else {
            kickHour.setText("00");
            kickMin.setText("00");
            kickSec.setText("00");
        }
    }

    private void checkLastPayTimeOut(CountDownTimer countDownTimer, MyKickList url, final TextView kickHour, final TextView kickMin, final TextView kickSec) {
        Date startTime = new Date();
        Date endTime = null;
        try {
            //startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(url.orderInfo.createdTime);
            endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(url.orderInfo.latestPayTime);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        long nd = 1000 * 24 * 60 * 60;//加入时间之后需要多少小时
        long desorg = startTime.getTime() + nd;
        long timer = startTime.getTime() + nd;
        if (endTime != null && endTime.getTime() < timer) {
            timer = endTime.getTime();
            desorg = endTime.getTime();
        }
        timer = timer - System.currentTimeMillis();
//        if (countDownTimer != null) {
//            countDownTimer.cancel();
//            countDownTimer = null;
//        }
        if (timer > 0) {
            //System.out.println("开始计时");
            final long finalTimer = timer;
            final long finalDesorg = desorg;
            countDownTimer = new CountDownTimer(finalTimer, 1000) {
                public void onTick(long millisUntilFinished) {
                    String[] array = DateUtils.getDistanceTimeNoDay(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), DateUtils.formatLongAll(finalDesorg + ""));
                    kickHour.setText(array[0]);
                    kickMin.setText(array[1]);
                    kickSec.setText(array[2]);
                }

                public void onFinish() {
                    kickHour.setText("00");
                    kickMin.setText("00");
                    kickSec.setText("00");
                }
            }.start();
            //将此 countDownTimer 放入list
            countDownCounters.put(kickHour.hashCode(), countDownTimer);
        } else {
            kickHour.setText("00");
            kickMin.setText("00");
            kickSec.setText("00");
        }
    }
}
