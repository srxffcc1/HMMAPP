package com.health.second.adapter;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.util.SparseArray;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.example.lib_ShapeView.view.ShapeTextView;
import com.health.second.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.model.ShopDetailMarketing;
import com.healthy.library.routes.SecondRoutes;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.DateUtils;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.widget.CornerImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 创建日期：2021/10/21 16:40
 *
 * @author LiuWei
 * @version 1.0
 * 包名： com.health.second.adapter
 * 类说明：
 */

public class ShopDetailMarketingAdapter extends BaseAdapter<ShopDetailMarketing> {

    private SparseArray<CountDownTimer> countDownCounters = new SparseArray<>();
    private String merchantType = null;
    private String shopId = null;

    public ShopDetailMarketingAdapter() {
        this(R.layout.item_shop_detail_marketing_goods_layout);
    }

    public ShopDetailMarketingAdapter(int viewId) {
        super(viewId);
    }

    public void setMerchantType(String merchantType, String shopId) {
        this.merchantType = merchantType;
        this.shopId = shopId;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        CornerImageView goodsImg = (CornerImageView) holder.getView(R.id.goodsImg);
        TextView goodsTitle = (TextView) holder.getView(R.id.goodsTitle);
        TextView goodsMoney = (TextView) holder.getView(R.id.goodsMoney);
        TextView pinNum = (TextView) holder.getView(R.id.pinNum);
        ShapeTextView marketingTag = (ShapeTextView) holder.getView(R.id.marketingTag);
        LinearLayout actTimeLL = (LinearLayout) holder.getView(R.id.actTimeLL);
        LinearLayout goodsTimeLL = (LinearLayout) holder.getView(R.id.goodsTimeLL);
        TextView kickHour = (TextView) holder.getView(R.id.kickHour);
        TextView kickMin = (TextView) holder.getView(R.id.kickMin);
        TextView kickSec = (TextView) holder.getView(R.id.kickSec);
        TextView kickDayTxt = (TextView) holder.getView(R.id.kickDayTxt);
        TextView kickDay = (TextView) holder.getView(R.id.kickDay);
        TextView salesCount = (TextView) holder.getView(R.id.salesCount);
        ShapeTextView subBtn = (ShapeTextView) holder.getView(R.id.subBtn);
        actTimeLL.setVisibility(View.GONE);

        final ShopDetailMarketing model = getDatas().get(position);
        pinNum.setVisibility(View.GONE);
        SpannableString goodsPrice = new SpannableString("￥" + FormatUtils.moneyKeep2Decimals(model.getMarketingPrice()));
        goodsPrice.setSpan(new AbsoluteSizeSpan(12, true), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        goodsPrice.setSpan(new StyleSpan(Typeface.NORMAL), 1, goodsPrice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //正常
        goodsMoney.setText(goodsPrice);
        if (model.marketingType == 1 && model.bargainInfo != null) {//砍价

            if (model.bargainInfo.marketingGoodsChildDTOS.get(0).availableInventory > 0) {
                subBtn.setText("去砍价");
                subBtn.getShapeDrawableBuilder().setGradientColors(Color.parseColor("#806FFF"),
                        Color.parseColor("#705DFF")).intoBackground();
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (merchantType != null && merchantType.equals("1")) {
                            ARouter.getInstance()
                                    .build(SecondRoutes.SECOND_SERVICE_DETAIL)
                                    .withString("bargainId", model.bargainInfo.id + "")
                                    .navigation();
                        } else {
                            ARouter.getInstance()
                                    .build(ServiceRoutes.SERVICE_DETAIL)
                                    .withString("bargainId", model.bargainInfo.id + "")
                                    .navigation();
                        }
                    }
                });
            } else {
                subBtn.setText("已抢完");
                subBtn.getShapeDrawableBuilder().setGradientColors(Color.parseColor("#D9D9D9"),
                        Color.parseColor("#CDCDCD")).intoBackground();
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "该商品已抢完", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            salesCount.setText(String.format("%s人已抢", getNumberWanTwo(model.bargainInfo.joinNum)));
            marketingTag.setText("该商品正在参与砍价");
            com.healthy.library.businessutil.GlideCopy.with(context)
                    .load(model.bargainInfo.goodsImage)
                    .into(goodsImg);
            goodsTitle.setText(model.bargainInfo.goodsTitle);
        } else if (model.marketingType == 2 && model.assembleInfo != null) {//拼团
            if (model.assembleInfo.marketingGoodsChildDTOS.get(0).availableInventory > 0) {
                subBtn.setText("去拼团");
                pinNum.setVisibility(View.VISIBLE);
                pinNum.setText(model.assembleInfo.regimentSize+"人团");
                subBtn.getShapeDrawableBuilder().setGradientColors(Color.parseColor("#806FFF"),
                        Color.parseColor("#705DFF")).intoBackground();
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (merchantType != null && merchantType.equals("1")) {
                            ARouter.getInstance()
                                    .build(SecondRoutes.SECOND_SERVICE_DETAIL)
                                    .withString("assembleId", model.assembleInfo.id + "")
                                    .navigation();
                        } else {
                            ARouter.getInstance()
                                    .build(ServiceRoutes.SERVICE_DETAIL)
                                    .withString("assembleId", model.assembleInfo.id + "")
                                    .navigation();
                        }
                    }
                });
            } else {
                subBtn.setText("已抢完");
                subBtn.getShapeDrawableBuilder().setGradientColors(Color.parseColor("#D9D9D9"),
                        Color.parseColor("#CDCDCD")).intoBackground();
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "该商品已抢完", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            salesCount.setText(String.format("%s人已抢", getNumberWanTwo(model.assembleInfo.joinNum)));
            marketingTag.setText("该商品正在参与拼团");
            com.healthy.library.businessutil.GlideCopy.with(context)
                    .load(model.assembleInfo.goodsImage)
                    .into(goodsImg);
            goodsTitle.setText(model.assembleInfo.goodsTitle);
        } else if (model.marketingType == 3 && model.flashSaleInfo != null) {//秒杀
            actTimeLL.setVisibility(View.VISIBLE);
            com.healthy.library.businessutil.GlideCopy.with(context)
                    .load(model.filePath)
                    .into(goodsImg);
            goodsTitle.setText(model.goodsTitle);
            if (model.maxInventory > 0) {
                subBtn.getShapeDrawableBuilder().setGradientColors(Color.parseColor("#806FFF"),
                        Color.parseColor("#705DFF")).intoBackground();
                subBtn.setText("去抢购");
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (merchantType != null && merchantType.equals("1")) {
                            ARouter.getInstance()
                                    .build(SecondRoutes.SECOND_SERVICE_DETAIL)
                                    .withString("id", model.goodsId)
                                    .withString("marketingType", "3")
                                    .navigation();
                        } else {
                            ARouter.getInstance()
                                    .build(ServiceRoutes.SERVICE_DETAIL)
                                    .withString("id", model.goodsId)
                                    .withString("marketingType", "3")
                                    .navigation();
                        }
                    }
                });
            } else {
                subBtn.setText("已抢完");
                subBtn.getShapeDrawableBuilder().setGradientColors(Color.parseColor("#D9D9D9"),
                        Color.parseColor("#CDCDCD")).intoBackground();
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "该商品已抢完", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            CountDownTimer countDownTimer = countDownCounters.get(kickHour.hashCode());
            checkTimeOut(countDownTimer, model, kickHour, kickMin, kickSec,kickDay);
            salesCount.setText(String.format("%s人已抢", getNumberWanTwo(model.sales)));
            marketingTag.setText("该商品正在参与秒杀");

        } else {
            subBtn.setText("");
            salesCount.setText("");
            marketingTag.setText("活动商品信息错误");
            goodsTitle.setText("活动商品信息错误");
        }

    }

    public String getNumberWanTwo(int value) {
        if (value < 10000) {
            return value + "";
        } else {//销量超过万处理下
            double n = (double) value / 10000;
            double result = Double.valueOf(String.format("%.0f", n));
            return FormatUtils.moneyKeep2Decimals(result) + "万+";
        }
    }

    private void checkTimeOut(CountDownTimer countDownTimer, final ShopDetailMarketing model, final TextView kickHour, final TextView kickMin, final TextView kickSec, final TextView kickDay) {
        if (model.flashSaleInfo.beginTime == null || model.flashSaleInfo.endTime == null) {
            kickDay.setText("00");
            kickHour.setText("00");
            kickMin.setText("00");
            kickSec.setText("00");
            return;
        }
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date endTime = null;
        try {
            endTime = simpleDateFormat.parse(model.flashSaleInfo.endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final long finalDesorg = endTime.getTime();
        long mSeconds = DateUtils.getDistanceSec(model.flashSaleInfo.beginTime, model.flashSaleInfo.endTime);
        if (mSeconds > 0) {
            countDownTimer = new CountDownTimer(mSeconds, 1000) {
                public void onTick(long millisUntilFinished) {
                    String[] array = DateUtils.getDistanceTime(simpleDateFormat.format(new Date()), DateUtils.formatLongAll(finalDesorg + ""));
//                    LogUtils.e(array[1] + ":" + array[2] + ":" + array[3]);
                    kickDay.setText(array[0]);
                    kickHour.setText(array[1]);
                    kickMin.setText(array[2]);
                    kickSec.setText(array[3]);
                }

                public void onFinish() {
                    kickDay.setText("00");
                    kickHour.setText("00");
                    kickMin.setText("00");
                    kickSec.setText("00");
                }
            }.start();
            //将此 countDownTimer 放入list
            countDownCounters.put(kickHour.hashCode(), countDownTimer);
        } else {
            kickDay.setText("00");
            kickHour.setText("00");
            kickMin.setText("00");
            kickSec.setText("00");
        }
    }
}
