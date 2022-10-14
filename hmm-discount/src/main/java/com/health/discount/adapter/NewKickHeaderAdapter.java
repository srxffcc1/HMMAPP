package com.health.discount.adapter;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.discount.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.interfaces.OnEventFucListener;
import com.healthy.library.model.Kick;
import com.healthy.library.routes.DiscountRoutes;
import com.healthy.library.utils.DateUtils;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.SpanUtils;
import com.healthy.library.widget.CornerImageView;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewKickHeaderAdapter extends BaseAdapter<ArrayList<Kick>> {
    //    CountDownTimer countDownTimer;
    private SparseArray<CountDownTimer> countDownCounters = new SparseArray<>();
    OnEventFucListener onEventFucListener;

    public void setOnEventFucListener(OnEventFucListener onEventFucListener) {
        this.onEventFucListener = onEventFucListener;
    }

    public NewKickHeaderAdapter() {
        this(R.layout.dis_item_activity_disact_top_sll);
    }

    public NewKickHeaderAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        List<Kick> list = getDatas().get(0);
        LinearLayout headerLiner = holder.getView(R.id.disact_top_sll_liner);
        headerLiner.removeAllViews();
        for (int i = 0; i < list.size(); i++) {
            final Kick kick = list.get(i);
            CountDownTimer countDownTimer = countDownCounters.get(kick.hashCode());

            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.dis_item_activity_disact_top_s, headerLiner, false);
//            if (kick.goodsType == 1) {
//                view = inflater.inflate(R.layout.dis_item_activity_disact_top_s2, headerLiner, false);
//            } else {
//                view = inflater.inflate(R.layout.dis_item_activity_disact_top_s, headerLiner, false);
//            }
            CornerImageView goodsImg = view.findViewById(R.id.goodsImg);
            TextView continueKick = view.findViewById(R.id.continueKick);
            TextView goodsDiscountValue = view.findViewById(R.id.goodsDiscountValue);
            TextView kickHour = view.findViewById(R.id.kickHour);
            TextView kickMin = view.findViewById(R.id.kickMin);
            TextView kickSec = view.findViewById(R.id.kickSec);
            com.healthy.library.businessutil.GlideCopy.with(context)
                    .load(list.get(i).goodsImage)
                    .error(R.drawable.img_1_1_default)
                    .placeholder(R.drawable.img_1_1_default2)
                    .into(goodsImg);
            double chajia = new BigDecimal(kick.goodsPlatformPrice + "").subtract(new BigDecimal(kick.floorPrice + "")).subtract(new BigDecimal(kick.discountMoney + "")).doubleValue();
            if (chajia < 0) {
                chajia = 0;
            }

            checkTimeOut(countDownTimer, list.get(i), kickHour, kickMin, kickSec);//倒计时

            double biliOrg = (kick.discountMoney * 1.0 / (kick.goodsPlatformPrice - kick.floorPrice) * 1000);
            int bili = (int) (kick.discountMoney * 1.0 / (kick.goodsPlatformPrice - kick.floorPrice) * 1000);
            if (bili > 1000) {
                biliOrg = 1000;
                bili = 1000;
            }

            TextView goodsProFFL = view.findViewById(R.id.goodsProFFL);
            TextView goodsProFFR = view.findViewById(R.id.goodsProFFR);
            LinearLayout.LayoutParams layoutParamsl = (LinearLayout.LayoutParams) goodsProFFL.getLayoutParams();
            LinearLayout.LayoutParams layoutParamsr = (LinearLayout.LayoutParams) goodsProFFR.getLayoutParams();
            layoutParamsl.weight = bili;
            layoutParamsr.weight = 1000 - bili;
            goodsProFFL.setLayoutParams(layoutParamsl);
            goodsProFFR.setLayoutParams(layoutParamsr);


            TextView goodsProFFLU = view.findViewById(R.id.goodsProFFLU);
            TextView goodsProFFRU = view.findViewById(R.id.goodsProFFRU);

            LinearLayout.LayoutParams layoutParamslU = (LinearLayout.LayoutParams) goodsProFFLU.getLayoutParams();
            LinearLayout.LayoutParams layoutParamsrU = (LinearLayout.LayoutParams) goodsProFFRU.getLayoutParams();

            layoutParamslU.weight = bili;
            layoutParamsrU.weight = 1000 - bili;


            goodsProFFLU.setLayoutParams(layoutParamslU);
            goodsProFFRU.setLayoutParams(layoutParamsrU);


            if (chajia <= 0) {
                continueKick.setText("立即购买");
                goodsDiscountValue.setText("恭喜您！终于砍到最低价了～");
            } else {
                continueKick.setText("继续砍价");
                goodsDiscountValue.setText(
                        SpanUtils.getBuilder(context, "已砍").setForegroundColor(Color.parseColor("#ff222222"))
                                .append(FormatUtils.moneyKeep2Decimals(list.get(i).discountMoney)).setForegroundColor(Color.parseColor("#F02846"))
                                .append("元,仅差").setForegroundColor(Color.parseColor("#ff222222"))
                                .append(FormatUtils.moneyKeep2Decimals(((1000 - biliOrg) * 100.0) / 1000) + "%").setForegroundColor(Color.parseColor("#F02846")).create());
            }

            goodsImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (kick.goodsType == 1) {
                        ARouter.getInstance()
                                .build(DiscountRoutes.DIS_KICKDETAIL)
                                .withString("bargainMemberId", kick.bargainMemberId)
                                .withString("bargainId", kick.id)
                                .withString("marketingShopId", kick.getMarketingShopId())
                                .navigation();
                    } else {
                        ARouter.getInstance()
                                .build(DiscountRoutes.DIS_KICKDETAIL)
                                .withString("bargainMemberId", kick.bargainMemberId)
                                .withString("bargainId", kick.id)
                                .withString("marketingShopId", kick.getMarketingShopId())
                                .navigation();
                    }
                }
            });
            continueKick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (kick.goodsType == 1) {
                        ARouter.getInstance()
                                .build(DiscountRoutes.DIS_KICKDETAIL)
                                .withString("bargainMemberId", kick.bargainMemberId)
                                .withString("bargainId", kick.id)
                                .withString("marketingShopId", kick.getMarketingShopId())
                                .navigation();
                    } else {
                        ARouter.getInstance()
                                .build(DiscountRoutes.DIS_KICKDETAIL)
                                .withString("bargainMemberId", kick.bargainMemberId)
                                .withString("bargainId", kick.id)
                                .withString("marketingShopId", kick.getMarketingShopId())
                                .navigation();
                    }
                }
            });
            headerLiner.addView(view);
        }
    }

    private void checkTimeOut(CountDownTimer countDownTimer, Kick url, final TextView kickHour, final TextView kickMin, final TextView kickSec) {

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
//                    if(onEventFucListener!=null){
//                        onEventFucListener.onEventFucDo(null);
//                    }
                }
            }.start();
            countDownCounters.put(url.hashCode(), countDownTimer);
            //将此 countDownTimer 放入list.
        } else {
            kickHour.setText("00");
            kickMin.setText("00");
            kickSec.setText("00");
//            if(onEventFucListener!=null){
//                onEventFucListener.onEventFucDo(null);
//            }
        }
    }
}
