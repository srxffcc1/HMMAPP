package com.health.second.adapter;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.second.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.KKGroup;
import com.healthy.library.model.Kick;
import com.healthy.library.model.Kill;
import com.healthy.library.routes.SecondRoutes;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.DateUtils;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.ParseUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.ImageTextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SecondActGoodsAdapter extends BaseAdapter<Object> {
    private SparseArray<CountDownTimer> countDownCounters = new SparseArray<>();


    public void stopAllCountDownTimer() {
        for (int i = 0; i < countDownCounters.size(); i++) {
            int key = countDownCounters.keyAt(i);
            CountDownTimer value = countDownCounters.valueAt(i);
            value.cancel();
            value = null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return 29;
    }

    public SecondActGoodsAdapter() {
        this(R.layout.item_second_act);
    }

    private SecondActGoodsAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {
        ImageTextView shopName;
        TextView actPeople;
        CornerImageView goodsPic;
        TextView goodsName;
        LinearLayout actTimeLL;
        LinearLayout goodsTimeLL;
        TextView kickDayT;
        TextView kickDay;
        TextView kickHour;
        TextView kickMin;
        TextView kickSec;
        TextView goodsMoney;
        TextView goodsMoneyOld;
        TextView goodsActRule;
        TextView goodsActRuleTmp;
        shopName = (ImageTextView) baseHolder.itemView.findViewById(R.id.shopName);
        actPeople = (TextView) baseHolder.itemView.findViewById(R.id.actPeople);
        goodsPic = (CornerImageView) baseHolder.itemView.findViewById(R.id.goodsPic);
        goodsName = (TextView) baseHolder.itemView.findViewById(R.id.goodsName);
        actTimeLL = (LinearLayout) baseHolder.itemView.findViewById(R.id.actTimeLL);
        goodsTimeLL = (LinearLayout) baseHolder.itemView.findViewById(R.id.goodsTimeLL);
        kickDayT = (TextView) baseHolder.itemView.findViewById(R.id.kickDayT);
        kickDay = (TextView) baseHolder.itemView.findViewById(R.id.kickDay);
        kickHour = (TextView) baseHolder.itemView.findViewById(R.id.kickHour);
        kickMin = (TextView) baseHolder.itemView.findViewById(R.id.kickMin);
        kickSec = (TextView) baseHolder.itemView.findViewById(R.id.kickSec);
        goodsMoney = (TextView) baseHolder.itemView.findViewById(R.id.goodsMoney);
        goodsMoneyOld = (TextView) baseHolder.itemView.findViewById(R.id.goodsMoneyOld);
        goodsActRule = (TextView) baseHolder.itemView.findViewById(R.id.goodsActRule);
        goodsActRuleTmp = (TextView) baseHolder.itemView.findViewById(R.id.goodsActRuleTmp);
        ImageView goodsMoneyBg=baseHolder.itemView.findViewById(R.id.goodsMoneyBg);
        actTimeLL.setVisibility(View.INVISIBLE);
        goodsMoneyOld.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);
        Object item = getDatas().get(i);
        goodsMoneyOld.setTextColor(Color.parseColor("#FFCCCC"));
        goodsMoneyBg.setImageResource(R.drawable.second_act_button_shark);
        if (item instanceof Kill) {
            actTimeLL.setVisibility(View.VISIBLE);
            final Kill kill= (Kill) item;
            CountDownTimer countDownTimer = countDownCounters.get(kickDayT.hashCode());
            com.healthy.library.businessutil.GlideCopy.with(context)
                    .load(kill.filePath)
                    .placeholder(R.drawable.img_1_1_default)
                    .error(R.drawable.img_1_1_default)

                    .into(goodsPic);
            goodsName.setText(kill.goodsTitle);
            goodsActRule.setText("立即抢购");
            goodsMoney.setText("¥"+ FormatUtils.moneyKeep2Decimals(kill.marketingPrice));
            goodsMoneyOld.setText("￥"+FormatUtils.moneyKeep2Decimals(kill.retailPrice));
            if(kill.sales>=kill.inventory){//
                actTimeLL.setVisibility(View.INVISIBLE);
                goodsActRule.setText("已抢完");
                goodsMoneyOld.setTextColor(Color.parseColor("#FFFFFF"));
                goodsMoneyBg.setImageResource(R.drawable.second_act_button_gray);
            }else{
                checkTimeOutEarly(countDownTimer,kill.endTime,kickDayT,kickDay,kickHour,kickMin,kickSec);
            }
            actPeople.setText(ParseUtils.parseNumberWithAdd(kill.sales+"",10000,"万")+"人已抢");
            shopName.setText(kill.getMerchantName());
            baseHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(kill.sales>=kill.inventory){
                        Toast.makeText(context, "当前商品已抢完，看看其他商品吧~", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(kill.userId.equals(LocUtil.getUserId())&&!TextUtils.isEmpty(LocUtil.getUserId())){
                        ARouter.getInstance()
                                .build(ServiceRoutes.SERVICE_DETAIL)
                                .withString("id", kill.goodsId)
                                .withString("marketingType", "3")
                                .navigation();
                    }else {
                        ARouter.getInstance()
                                .build(SecondRoutes.SECOND_SERVICE_DETAIL)
                                .withString("id", kill.goodsId)
                                .withString("marketingType", "3")
                                .navigation();
                    }
                }
            });

        }
        if (item instanceof KKGroup) {
            final KKGroup kkGroup= (KKGroup) item;
            com.healthy.library.businessutil.GlideCopy.with(context)
                    .load(kkGroup.goodsImage)
                    .placeholder(R.drawable.img_1_1_default)
                    .error(R.drawable.img_1_1_default)

                    .into(goodsPic);
            goodsName.setText(kkGroup.goodsTitle);
            goodsActRule.setText(kkGroup.regimentSize+"人团");
            goodsMoney.setText("¥"+ FormatUtils.moneyKeep2Decimals(kkGroup.assemblePrice));
            goodsMoneyOld.setText("￥"+FormatUtils.moneyKeep2Decimals(kkGroup.retailPrice));
            actPeople.setText(ParseUtils.parseNumberWithAdd(kkGroup.joinNum+"",10000,"万")+"人已抢");
            shopName.setText(kkGroup.getMerchantName());


            if(kkGroup.assembleInventory<=0){//
                actTimeLL.setVisibility(View.INVISIBLE);
                goodsActRule.setText("已抢完");
                goodsMoneyOld.setTextColor(Color.parseColor("#FFFFFF"));
                goodsMoneyBg.setImageResource(R.drawable.second_act_button_gray);
            }else{

            }


            baseHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(kkGroup.assembleInventory<=0){
                        Toast.makeText(context, "当前商品已抢完，看看其他商品吧~", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(kkGroup.merchantId.equals(LocUtil.getUserId())&&!TextUtils.isEmpty(LocUtil.getUserId())){
                        ARouter.getInstance()
                                .build(ServiceRoutes.SERVICE_DETAIL)
                                .withString("assembleId", kkGroup.id)
                                .withString("shopId", SpUtils.getValue(context, SpKey.CHOSE_SHOP))
                                .navigation();
                    }else {
                        ARouter.getInstance()
                                .build(SecondRoutes.SECOND_SERVICE_DETAIL)
                                .withString("assembleId", kkGroup.id)
                                .withString("shopId", SpUtils.getValue(context, SpKey.CHOSE_SHOP))
                                .navigation();
                    }
                }
            });
        }
        if (item instanceof Kick) {
            final Kick kick= (Kick) item;
            com.healthy.library.businessutil.GlideCopy.with(context)
                    .load(kick.goodsImage)
                    .placeholder(R.drawable.img_1_1_default)
                    .error(R.drawable.img_1_1_default)

                    .into(goodsPic);
            goodsName.setText(kick.goodsTitle);
            goodsActRule.setText("去砍价");
            goodsMoney.setText("¥"+ FormatUtils.moneyKeep2Decimals(kick.floorPrice));
            goodsMoneyOld.setText("￥"+FormatUtils.moneyKeep2Decimals(kick.retailPrice));
            actPeople.setText(ParseUtils.parseNumberWithAdd(kick.joinNum+"",10000,"万")+"人已抢");
            shopName.setText(kick.getMerchantName());
            if(kick.bargainInventory<=0){//
                actTimeLL.setVisibility(View.INVISIBLE);
                goodsActRule.setText("已抢完");
                goodsMoneyOld.setTextColor(Color.parseColor("#FFFFFF"));
                goodsMoneyBg.setImageResource(R.drawable.second_act_button_gray);
            }else{

            }
            baseHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(kick.bargainInventory<=0){
                        Toast.makeText(context, "当前商品已抢完，看看其他商品吧~", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(kick.merchantId.equals(LocUtil.getUserId())&&!TextUtils.isEmpty(LocUtil.getUserId())){
                        ARouter.getInstance()
                                .build(ServiceRoutes.SERVICE_DETAIL)
                                .withString("bargainId", kick.id)
                                .withString("bargainMemberId", kick.bargainMemberId)
                                .withString("shopId", SpUtils.getValue(context, SpKey.CHOSE_SHOP))
                                .navigation();
                    }else {
                        ARouter.getInstance()
                                .build(SecondRoutes.SECOND_SERVICE_DETAIL)
                                .withString("bargainId", kick.id)
                                .withString("bargainMemberId", kick.bargainMemberId)
                                .withString("shopId", SpUtils.getValue(context, SpKey.CHOSE_SHOP))
                                .navigation();
                    }
                }
            });
        }
    }

    private void checkTimeOutEarly(CountDownTimer countDownTimer,String endTimeString, final TextView kickDayT,final TextView kickDay,final TextView kickHour, final TextView kickMin, final TextView kickSec) {

        Date endTime = null;
        try {
            endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTimeString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long desorg = 0;
        long timer = 0;
        timer = endTime.getTime();
        desorg = endTime.getTime();
        timer = timer - System.currentTimeMillis();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        kickDayT.setVisibility(View.VISIBLE);
        kickDay.setVisibility(View.VISIBLE);
        if (timer > 0) {
            ////System.out.println("开始计时");
            final long finalTimer = timer;
            final long finalDesorg = desorg;
            countDownTimer = new CountDownTimer(finalTimer, 1000) {
                public void onTick(long millisUntilFinished) {
                    String[] array = DateUtils.getDistanceTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), DateUtils.formatLongAll(finalDesorg + ""));
                    kickDay.setText(array[0]);
                    kickHour.setText(array[1]);
                    kickMin.setText(array[2]);
                    kickSec.setText(array[3]);
                    if("0".equals(array[0])){
                        kickDayT.setVisibility(View.GONE);
                        kickDay.setVisibility(View.GONE);
                    }
                }

                public void onFinish() {
                    kickDay.setText("0");
                    kickHour.setText("00");
                    kickMin.setText("00");
                    kickSec.setText("00");
                    if (moutClickListener != null) {
                        moutClickListener.outClick("倒计时结束", null);
                    }
                }
            }.start();
            //将此 countDownTimer 放入list.
            countDownCounters.put(kickDayT.hashCode(), countDownTimer);
        } else {
            kickDay.setText("0");
            kickHour.setText("00");
            kickMin.setText("00");
            kickSec.setText("00");
        }
    }
}
