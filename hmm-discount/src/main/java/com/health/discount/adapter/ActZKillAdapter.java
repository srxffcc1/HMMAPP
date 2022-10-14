package com.health.discount.adapter;

import android.graphics.Paint;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.discount.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.model.ActKill;
import com.healthy.library.routes.DiscountRoutes;
import com.healthy.library.utils.DateUtils;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.widget.AutoClickImageView;
import com.healthy.library.widget.ImageTextView;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActZKillAdapter extends BaseAdapter<String> {


    private List<ActKill> killList;
    private boolean isStart = false;//是否秒杀已经开始

    @Override
    public int getItemViewType(int position) {
        return 17;
    }

    public ActZKillAdapter() {
        this(R.layout.dis_item_fragment_kill);
    }

    private ActZKillAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {
        LinearLayout robPLL;
        ConstraintLayout robTopLL;
        ImageTextView robTitle;
        LinearLayout killTitleLeft;
        TextView robTitleTime;
        ImageTextView robMore;
        LinearLayout robLL;
        robPLL = (LinearLayout) baseHolder.itemView.findViewById(R.id.robPLL);
        robTopLL = (ConstraintLayout) baseHolder.itemView.findViewById(R.id.robTopLL);
        robTitle = (ImageTextView) baseHolder.itemView.findViewById(R.id.robTitle);
        killTitleLeft = (LinearLayout) baseHolder.itemView.findViewById(R.id.killTitleLeft);
        robTitleTime = (TextView) baseHolder.itemView.findViewById(R.id.robTitleTime);
        robMore = (ImageTextView) baseHolder.itemView.findViewById(R.id.robMore);
        robLL = (LinearLayout) baseHolder.itemView.findViewById(R.id.robLL);
        onSucessGetActKill(robPLL, robLL, robMore, robTitleTime, killList);
    }

    public void onSucessGetActKill(LinearLayout robPLL, LinearLayout robLL, ImageTextView robMore, TextView robTitleTime, List<ActKill> list) {
        robPLL.setVisibility(View.GONE);
        robLL.removeAllViews();
        if (list != null && list.size() > 0) {
            robPLL.setVisibility(View.VISIBLE);
            robLL.removeAllViews();
            robMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map nokmap = new HashMap<String, String>();
                    nokmap.put("soure", "首页秒杀活动更多按钮点击量");
                    MobclickAgent.onEvent(context, "event2APPShopHomeKillMoreClick", nokmap);
                    ARouter.getInstance()
                            .build(DiscountRoutes.DIS_SECKILLLISTACTIVITY)
                            .navigation();
                }
            });
            checkTimeOut(list.get(0), robTitleTime);
            for (int i = 0; i < list.size(); i++) {
                View view = LayoutInflater.from(context).inflate(R.layout.dis_item_kill_h, robLL, false);
                ImageView goodsImg;
                TextView killPrice;
                TextView killOldPrice;
                TextView mall_goods_sales;
                goodsImg = (ImageView) view.findViewById(R.id.goodsImg);
                killPrice = (TextView) view.findViewById(R.id.killPrice);
                killOldPrice = (TextView) view.findViewById(R.id.killOldPrice);
                mall_goods_sales = (TextView) view.findViewById(R.id.mall_goods_sales);
                AutoClickImageView autoClickImageView=view.findViewById(R.id.goodsShareCoupon);

                ActKill item = list.get(i);
                if(item.shareGiftDTOS!=null&&item.shareGiftDTOS.size()>0){
                    autoClickImageView.setVisibility(View.VISIBLE);
                    autoClickImageView.setCanTouch(false);
                }else {
                    autoClickImageView.setVisibility(View.INVISIBLE);
                }
                com.healthy.library.businessutil.GlideCopy.with(context)
                        .load(item.filePath)
                        .placeholder(R.drawable.img_1_1_default)
                        .error(R.drawable.img_1_1_default)
                        .into(goodsImg);
                if (isStart) {
                    if (item.sales == 0) {
                        mall_goods_sales.setVisibility(View.INVISIBLE);
                    }
                    mall_goods_sales.setText(item.sales + "人已抢");
                } else {
                    mall_goods_sales.setVisibility(View.INVISIBLE);
                }
                killOldPrice.setText("￥" + FormatUtils.moneyKeep2Decimals(item.retailPrice));
                killOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                killPrice.setText("" + FormatUtils.moneyKeep2Decimals(item.marketingPrice));
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map nokmap = new HashMap<String, String>();
                        nokmap.put("soure", "首页秒杀活动商品图片/标题的点击量");
                        MobclickAgent.onEvent(context, "event2APPShopHomeKillGoodsClick", nokmap);
                        ARouter.getInstance()
                                .build(DiscountRoutes.DIS_SECKILLLISTACTIVITY)
                                .navigation();
                    }
                });
                robLL.addView(view);
            }
            robPLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ARouter.getInstance()
                            .build(DiscountRoutes.DIS_SECKILLLISTACTIVITY)
                            .navigation();
                }
            });
        }
    }

    CountDownTimer countDownTimer;

    private void checkTimeOut(ActKill url, final TextView kickTime) {

        long desorg = 0;
        long timer = 0;
        timer = url.endTimestamp * 1000;
        desorg = url.endTimestamp * 1000;

//        timer = timer - System.currentTimeMillis();
        if (countDownTimer != null) {
            //System.out.println("取消了前面的线程");
            countDownTimer.cancel();
            countDownTimer = null;
        }
        if (timer > 0) {
            //System.out.println("开始计时");
            final long finalTimer = timer;
            final long finalDesorg = desorg;
            countDownTimer = new CountDownTimer(finalTimer, 1000) {
                public void onTick(long millisUntilFinished) {
                    String result = DateUtils.getDistanceTimeNoDayString(millisUntilFinished);
                    kickTime.setText(result);
                }

                public void onFinish() {
                    kickTime.setText("00:00");

                }
            }.start();
            //将此 countDownTimer 放入list
        } else {
            kickTime.setText("00:00");

        }
    }

    private void initView() {

    }

    public void setKillList(List<ActKill> killList) {
        this.killList = killList;
    }

    public void setIsStart(boolean isStart) {
        this.isStart = isStart;
    }

    public List<ActKill> getKillList() {
        return killList;
    }
}
