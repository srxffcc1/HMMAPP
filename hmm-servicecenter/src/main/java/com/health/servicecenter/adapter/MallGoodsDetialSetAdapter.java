package com.health.servicecenter.adapter;

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
import com.bumptech.glide.Glide;
import com.health.servicecenter.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.model.GoodsSetAll;
import com.healthy.library.model.GoodsSetCell;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.DateUtils;
import com.healthy.library.utils.FormatUtils;

import java.util.HashMap;
import java.util.Map;

public class MallGoodsDetialSetAdapter extends BaseAdapter<GoodsSetAll> {
    public String id;
    private int setCount;
    Map<String, CountDownTimer> timerMap = new HashMap<>();

    private int type = 0;
    private boolean isLoadSuccess;

    public void setType(int type) {
        this.type = type;
    }

    public void setLoadSuccess(boolean loadSuccess) {
        this.isLoadSuccess = loadSuccess;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int getItemViewType(int position) {
        return 4;
    }

    public MallGoodsDetialSetAdapter() {
        this(R.layout.service_item_goodsdetail_set);
    }

    private MallGoodsDetialSetAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {
        LinearLayout setP = baseHolder.itemView.findViewById(R.id.setP);
        TextView divMoney;
        TextView allMoney;
        final TextView payMoney;
        TextView setMoreCount;
        divMoney = (TextView) baseHolder.itemView.findViewById(R.id.divMoney);
        allMoney = (TextView) baseHolder.itemView.findViewById(R.id.allMoney);
        payMoney = (TextView) baseHolder.itemView.findViewById(R.id.payMoney);
        setMoreCount = baseHolder.itemView.findViewById(R.id.setMoreCount);

        final TextView payMoneyLater = (TextView) baseHolder.itemView.findViewById(R.id.payMoneyLater);
        final ConstraintLayout payMoneyLL = (ConstraintLayout) baseHolder.itemView.findViewById(R.id.payMoneyLL);

        //type == 1 时才会触发isLoadSuccess变为true操作进行避免多次绘制
        if (isLoadSuccess) {
            return;
        }
        final GoodsSetAll goodsSetAll = getModel();
        divMoney.setText("省" + FormatUtils.moneyKeep2Decimals(goodsSetAll.saveAmountOf) + "元");
        allMoney.setText("¥" + FormatUtils.moneyKeep2Decimals(goodsSetAll.combinationPrice));
        //setMoreCount.setText("("+setCount+")");
        baseHolder.setOnClickListener(R.id.setLLtop, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moutClickListener != null) {
                    moutClickListener.outClick("更多套餐", null);
                }
            }
        });

        if (goodsSetAll.openToBookingCountdown > 0) {//当离开售还有时间时 进行倒计时
            CountDownTimer countDownTimer = timerMap.get(goodsSetAll.id);
            if (countDownTimer != null) {
                countDownTimer.cancel();
                countDownTimer = null;
            }

            long timer = goodsSetAll.openToBookingCountdown * 1000;
            countDownTimer = new CountDownTimer(timer, 1000) {
                public void onTick(long millisUntilFinished) {
                    payMoneyLL.setBackgroundResource(R.drawable.shape_mall_goods_ol_vid_red_btn_g);
                    String[] array = DateUtils.getDistanceTimeNoDay(millisUntilFinished);
                    payMoneyLater.setVisibility(View.VISIBLE);
                    payMoney.setVisibility(View.GONE);
                    payMoneyLater.setText("" + array[0] + ":" + array[1] + ":" + array[2] + "\n即将开售");
                }

                public void onFinish() {
                    payMoney.setVisibility(View.VISIBLE);
                    payMoneyLater.setVisibility(View.GONE);
                    payMoneyLL.setBackgroundResource(R.drawable.shape_mall_goods_ol_vid_red_btn);
                    goodsSetAll.openToBookingCountdown = 0;
                }
            }.start();
            //将此 countDownTimer 放入list.
            timerMap.put(goodsSetAll.id, countDownTimer);
        }

        payMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ////System.out.println("出现问题" + goodsSetAll.id);
//                Map nokmap = new HashMap<String, String>();
//                nokmap.put("soure", "商品详情页组合套餐栏【立即下单】");
//                MobclickAgent.onEvent(context, "btn_APP_MaternalandChildGoods_CommodityDetails_CombinedGoods", nokmap);
//                ArrayList<GoodsBasketCell> list=new ArrayList<>();
//                for (int j = 0; j <goodsSetAll.goodsList.size() ; j++) {
//                    GoodsSetCell goodsSetCell=goodsSetAll.goodsList.get(j);
//                    GoodsBasketCell goodsBasketCell=new GoodsBasketCell(goodsSetCell.platformPrice,goodsSetCell.platformPrice,goodsSetCell.type);
//                    goodsBasketCell.goodsId=goodsSetCell.goodsId;
//                    goodsBasketCell.goodsSpecId=goodsSetCell.skuId;
//                    goodsBasketCell.goodsTitle=goodsSetCell.goodsTitle;
//                    goodsBasketCell.goodsSpecDesc=goodsSetCell.skuName;
//                    goodsBasketCell.goodsImage=goodsSetCell.filePath;
//                    goodsBasketCell.goodsType=goodsSetCell.type;
//                    goodsBasketCell.goodsQuantity=1;
//                    goodsBasketCell.goodsShopId= SpUtils.getValue(context, SpKey.CHOSE_SHOP);
//                    goodsBasketCell.goodsShopName= SpUtils.getValue(context, SpKey.CHOSE_SHOPNAME);
//                    goodsBasketCell.shopIdList=goodsSetCell.shopIdList;
//                    goodsBasketCell.mchId=goodsSetAll.merchantId;
//                    goodsBasketCell.isMaster=goodsSetCell.isMaster;
//                    goodsBasketCell.additionType="1";
//                    list.add(goodsBasketCell);
//                }
//                ARouter.getInstance()
//                        .build(ServiceRoutes.SERVICE_ORDER)
//                        .withObject("goodsbasketlist",list)
//                        .withString("packageId", goodsSetAll.id + "")
//                        .withString("packageQuantity", 1+"")
//                        .withString("packageMoney", goodsSetAll.combinationPrice + "")
//                        .navigation();
                if (moutClickListener != null) {
                    moutClickListener.outClick("更多套餐", null);
                }
            }
        });
        buildSetHList(setP, goodsSetAll);
        //type如果为1 = ServiceGoodsDetail 优化滑动问题，也用于避免影响其他页面
        if (type == 1) {
            isLoadSuccess = true;
        }
    }

    private void buildSetHList(LinearLayout setP, GoodsSetAll goodsSetAll) {
        setP.removeAllViews();
        for (int j = 0; j < goodsSetAll.goodsList.size(); j++) {
            View view = LayoutInflater.from(context).inflate(R.layout.service_item_goodsdetail_set_cell, null);
            final GoodsSetCell goodsSetCel = goodsSetAll.goodsList.get(j);
            ImageView goodsImg = view.findViewById(R.id.goodsImg);
            TextView goodPrice = view.findViewById(R.id.goodPrice);
            if (j == 0) {
                view.findViewById(R.id.goodsImgLeft).setVisibility(View.VISIBLE);
            }
            if (j == goodsSetAll.goodsList.size() - 1) {
                view.findViewById(R.id.goodsAdd).setVisibility(View.GONE);
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("2".equals(goodsSetCel.type)) {
                        ARouter.getInstance()
                                .build(ServiceRoutes.SERVICE_DETAIL)
                                .withString("id", goodsSetCel.goodsId+"")
                                .navigation();
                    } else {
                        ARouter.getInstance()
                                .build(ServiceRoutes.SERVICE_DETAIL)
                                .withString("goodsId",goodsSetCel.goodsId+"")
                                .navigation();
                    }
                }
            });
            goodPrice.setText("¥" + FormatUtils.moneyKeep2Decimals(goodsSetCel.platformPrice));
            com.healthy.library.businessutil.GlideCopy.with(context)
                    .load(goodsSetCel.filePath)
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_1_1_default)

                    .into(goodsImg);
            setP.addView(view);
        }
    }

    private void initView() {

    }

    public void setSetCount(int setCount) {
        this.setCount = setCount;
    }

    public int getSetCount() {
        return setCount;
    }
}
