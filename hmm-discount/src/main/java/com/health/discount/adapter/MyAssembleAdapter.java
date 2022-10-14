package com.health.discount.adapter;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
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
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.health.discount.R;
import com.healthy.library.LibApplication;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.model.KKGroup;
import com.healthy.library.routes.DiscountRoutes;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.DateUtils;
import com.healthy.library.utils.DrawableUtils;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.ImageTextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyAssembleAdapter extends BaseAdapter<KKGroup> {
    private SparseArray<CountDownTimer> countDownCounters = new SparseArray<>();

    public MyAssembleAdapter() {
        this(R.layout.my_assemble_adapter_layout);
    }

    public MyAssembleAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, final int position) {
        ImageTextView orderTitle = holder.getView(R.id.orderTitle);
        CornerImageView goodsImg = holder.getView(R.id.goodsImg);
        TextView goodsName = holder.getView(R.id.goodsName);
        TextView orderStatusValue = holder.getView(R.id.orderStatusValue);
        TextView tv_total_price_pre = holder.getView(R.id.tv_total_price_pre);
        TextView tv_total_price = holder.getView(R.id.tv_total_price);
        TextView kickHour = holder.getView(R.id.kickHour);
        TextView kickMin = holder.getView(R.id.kickMin);
        TextView kickSec = holder.getView(R.id.kickSec);
        TextView buttonLeft = holder.getView(R.id.buttonLeft);
        TextView buttonRight = holder.getView(R.id.buttonRight);
        LinearLayout goodsTimeLL = holder.getView(R.id.goodsTimeLL);
        final KKGroup kick = getDatas().get(position);
        if (kick != null) {
            CountDownTimer countDownTimer = countDownCounters.get(kickHour.hashCode());
            String statusValue = "";
            String statusColor = "#F02846";
            if ("拼团成功".equals(kick.statusStr)) {
                statusValue = "拼团成功";
                statusColor = "#222222";
                buttonLeft.setVisibility(View.VISIBLE);
                buttonLeft.setText("订单详情");
                buttonRight.setVisibility(View.GONE);
                goodsTimeLL.setVisibility(View.GONE);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ARouter.getInstance()
                                .build(DiscountRoutes.DIS_GROUPDETAIL)
                                .withString("teamNum", kick.teamNum)
                                .withString("shopId", kick.shopId)
                                .navigation();
                    }
                });
                buttonLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ARouter.getInstance()
                                .build(ServiceRoutes.SERVICE_ORDERLISTDETIAL)
                                .withString("orderId", kick.orderId + "")
                                .withString("function", "25006")
                                .navigation();
                    }
                });
            } else {
                if ("拼团中".equals(kick.statusStr)) {
                    statusValue = "还差" + kick.remainderNum + "人";
                    statusColor = "#F02846";
                    buttonLeft.setVisibility(View.VISIBLE);
                    buttonLeft.setText("我要退团");
                    buttonRight.setText("邀请好友");
                    buttonRight.setVisibility(View.VISIBLE);
                    goodsTimeLL.setVisibility(View.VISIBLE);
                    checkTimeOut(countDownTimer, kick, kickHour, kickMin, kickSec);
                    com.healthy.library.businessutil.GlideCopy.with(context).load(kick.goodsImage)
                            .placeholder(R.drawable.img_1_1_default)
                            .error(R.drawable.img_1_1_default)

                            .into(new SimpleTarget<Drawable>() {

                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    kick.bitmapShre = DrawableUtils.drawableToBitmap(resource);
                                }
                            });
                    final String lessman = (kick.regimentSize - kick.teamMemberList.size()) + "";
                    buttonRight.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onActShareClickListener != null) {
                                onActShareClickListener.clickShare(kick, lessman, kick.bitmapShre);
                            }

                        }
                    });
                    buttonLeft.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(moutClickListener!=null){
                                moutClickListener.outClick("退团",kick.teamNum);
                            }
                        }
                    });
                    goodsName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ARouter.getInstance()
                                    .build(DiscountRoutes.DIS_GROUPDETAIL)
                                    .withString("teamNum", kick.teamNum)
                                    .withString("shopId", kick.shopId)
                                    .navigation();

                        }
                    });
                    goodsImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ARouter.getInstance()
                                    .build(DiscountRoutes.DIS_GROUPDETAIL)
                                    .withString("teamNum", kick.teamNum)
                                    .withString("shopId", kick.shopId)
                                    .navigation();
                        }
                    });
                } else if ("拼团失败".equals(kick.statusStr)) {
                    statusValue = "拼团失败";
                    statusColor = "#666666";
                    buttonLeft.setVisibility(View.GONE);
                    buttonRight.setVisibility(View.GONE);
                    goodsTimeLL.setVisibility(View.GONE);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ARouter.getInstance()
                                    .build(DiscountRoutes.DIS_GROUPDETAIL)
                                    .withString("teamNum", kick.teamNum)
                                    .withString("shopId", kick.shopId)
                                    .navigation();
                        }
                    });
                }else if("已退团".equals(kick.statusStr)){
                    statusValue = "已退团";
                    statusColor = "#666666";
                    buttonLeft.setVisibility(View.GONE);
                    buttonRight.setVisibility(View.GONE);
                    goodsTimeLL.setVisibility(View.GONE);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(LibApplication.getAppContext(),"您已退团不可查看",Toast.LENGTH_SHORT).show();
                        }
                    });
                    goodsName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Toast.makeText(LibApplication.getAppContext(),"您已退团不可查看",Toast.LENGTH_SHORT).show();

                        }
                    });
                    goodsImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Toast.makeText(LibApplication.getAppContext(),"您已退团不可查看",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }

            orderStatusValue.setText(statusValue);
            orderStatusValue.setTextColor(Color.parseColor(statusColor));
            orderTitle.setText(kick.shopName);
            com.healthy.library.businessutil.GlideCopy.with(context)
                    .load(kick.goodsImage)
                    .error(R.drawable.img_1_1_default)
                    .placeholder(R.drawable.img_1_1_default2)
                    .into(goodsImg);
            goodsName.setText(kick.goodsTitle);
            tv_total_price_pre.setText("共" + 1 + "件 实付款：");
            SpannableString total_price = new SpannableString(" ¥" + FormatUtils.moneyKeep2Decimals(kick.assemblePrice) + "");
            total_price.setSpan(new AbsoluteSizeSpan(13, true), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            total_price.setSpan(new StyleSpan(Typeface.NORMAL), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //正常
            total_price.setSpan(new StyleSpan(Typeface.BOLD), 1, total_price.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//加粗
            tv_total_price.setText(total_price);
        }
    }

    public void setOnActShareClickListener(OnActShareClickListener onActShareClickListener) {
        this.onActShareClickListener = onActShareClickListener;
    }

    OnActShareClickListener onActShareClickListener;

    public interface OnActShareClickListener {
        void clickShare(KKGroup kkGroupDetail, String lessman, Bitmap bitmap);
    }

    private void checkTimeOut(CountDownTimer countDownTimer, KKGroup url, final TextView kickHour, final TextView kickMin, final TextView kickSec) {
        Date startTime = null;
        Date endTime = null;
        try {
            startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(url.joinTime);
            endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(url.endTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long nd = 1000 * url.regimentTimeLength * 60 * 60;//加入时间之后需要多少小时
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
