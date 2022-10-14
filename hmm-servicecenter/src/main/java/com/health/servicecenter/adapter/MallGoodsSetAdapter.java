package com.health.servicecenter.adapter;

import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Barrier;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;
import com.health.servicecenter.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.model.GoodsBasketCell;
import com.healthy.library.model.GoodsSetAll;
import com.healthy.library.model.GoodsSetCell;
import com.healthy.library.model.GoodsSpecCell;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.DateUtils;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.StringUtils;
import com.healthy.library.widget.CornerImageView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MallGoodsSetAdapter extends BaseAdapter<GoodsSetAll> {

    OnSpecClickListener onSpecClickListener;
    OnShopClickListener onShopClickListener;
    OnShopAllClickListener onShopAllClickListener;
    OnSpecAllClickListener onSpecAllClickListener;
    public void setOnShopClickListener(OnShopClickListener onShopClickListener) {
        this.onShopClickListener = onShopClickListener;
    }

    public void setOnShopAllClickListener(OnShopAllClickListener onShopAllClickListener) {
        this.onShopAllClickListener = onShopAllClickListener;
    }

    public void setOnSpecAllClickListener(OnSpecAllClickListener onSpecAllClickListener) {
        this.onSpecAllClickListener = onSpecAllClickListener;
    }
    public void setOnSpecClickListener(OnSpecClickListener onSpecClickListener) {
        this.onSpecClickListener = onSpecClickListener;
    }

    Map<String, CountDownTimer> timerMap=new HashMap<>();




    @Override
    public int getItemViewType(int position) {
        return 72;
    }

    public MallGoodsSetAdapter() {
        this(R.layout.service_item_set);
    }

    private MallGoodsSetAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, final int i) {
        final GoodsSetAll goodsSetAll = getDatas().get(i);

        ConstraintLayout setTitleTop;
        TextView setNumber;
        TextView setOldMoney;
        TextView setNowMoney;
        final ImageView scrollImg;
        final LinearLayout setTitleUnder;
        final LinearLayout setTitleUnderSplash;
        HorizontalScrollView setHoLL;
        LinearLayout setP;
        ConstraintLayout setRightLL;
        TextView divMoney;
        TextView allMoney;
        final TextView payMoney;
        final ConstraintLayout setTitleUnderBtnLL;
        TextView orderDivMoney;
        final TextView goSetOrder;
        View setdivider;
        setTitleTop = (ConstraintLayout) baseHolder.itemView.findViewById(R.id.setTitleTop);
        setNumber = (TextView) baseHolder.itemView.findViewById(R.id.setNumber);
        setOldMoney = (TextView) baseHolder.itemView.findViewById(R.id.setOldMoney);
        setNowMoney = (TextView) baseHolder.itemView.findViewById(R.id.setNowMoney);
        scrollImg = (ImageView) baseHolder.itemView.findViewById(R.id.scrollImg);
        setTitleUnder = (LinearLayout) baseHolder.itemView.findViewById(R.id.setTitleUnder);
        setTitleUnderSplash = (LinearLayout) baseHolder.itemView.findViewById(R.id.setTitleUnderSplash);
        setHoLL = (HorizontalScrollView) baseHolder.itemView.findViewById(R.id.setHoLL);
        setP = (LinearLayout) baseHolder.itemView.findViewById(R.id.setP);
        setRightLL = (ConstraintLayout) baseHolder.itemView.findViewById(R.id.setRightLL);
        divMoney = (TextView) baseHolder.itemView.findViewById(R.id.divMoney);
        allMoney = (TextView) baseHolder.itemView.findViewById(R.id.allMoney);
        payMoney = (TextView) baseHolder.itemView.findViewById(R.id.payMoney);
        setTitleUnderBtnLL = (ConstraintLayout) baseHolder.itemView.findViewById(R.id.setTitleUnderBtnLL);
        orderDivMoney = (TextView) baseHolder.itemView.findViewById(R.id.orderDivMoney);
        goSetOrder = (TextView) baseHolder.itemView.findViewById(R.id.goSetOrder);
        final TextView payMoneyLater = (TextView) baseHolder.itemView.findViewById(R.id.payMoneyLater);
        final TextView goSetOrderLater=(TextView) baseHolder.itemView.findViewById(R.id.goSetOrderLater);
        final ConstraintLayout goSetOrderLL=(ConstraintLayout) baseHolder.itemView.findViewById(R.id.goSetOrderLL);
        final ConstraintLayout payMoneyLL=(ConstraintLayout) baseHolder.itemView.findViewById(R.id.payMoneyLL);
        setdivider = (View) baseHolder.itemView.findViewById(R.id.setdivider);
        if(goodsSetAll.isShow){
            scrollImg.setImageResource(R.drawable.goods_arrow_up);
            setTitleUnderBtnLL.setVisibility(View.VISIBLE);
            setTitleUnder.setVisibility(View.VISIBLE);
            setTitleUnderSplash.setVisibility(View.GONE);
        }else {
            scrollImg.setImageResource(R.drawable.goods_arrow_down);
            setTitleUnder.setVisibility(View.GONE);
            setTitleUnderBtnLL.setVisibility(View.GONE);
            setTitleUnderSplash.setVisibility(View.VISIBLE);
        }

        if(goodsSetAll.openToBookingCountdown>0){//当离开售还有时间时 进行倒计时
            CountDownTimer countDownTimer = timerMap.get(goodsSetAll.id);
            if(countDownTimer!=null){
                countDownTimer.cancel();
                countDownTimer=null;
            }

            long timer = goodsSetAll.openToBookingCountdown*1000;
            countDownTimer = new CountDownTimer(timer, 1000) {
                public void onTick(long millisUntilFinished) {
                    payMoneyLL.setBackgroundResource(R.drawable.shape_mall_goods_ol_vid_red_btn_g);
                    goSetOrderLL.setBackgroundResource(R.drawable.shape_mall_goods_ol_vid_red_btn_big_g);
                    String[] array= DateUtils.getDistanceTimeNoDay(millisUntilFinished);
                    payMoneyLater.setVisibility(View.VISIBLE);
                    goSetOrderLater.setVisibility(View.VISIBLE);
                    payMoney.setVisibility(View.GONE);
                    goSetOrder.setVisibility(View.GONE);
                    payMoneyLater.setText(""+array[0]+":"+array[1]+":"+array[2]+"\n即将开售");
                    goSetOrderLater.setText(""+array[0]+":"+array[1]+":"+array[2]+" 即将开售");
                }
                public void onFinish() {
                    payMoney.setVisibility(View.VISIBLE);
                    goSetOrder.setVisibility(View.VISIBLE);
                    payMoneyLater.setVisibility(View.GONE);
                    goSetOrderLater.setVisibility(View.GONE);
                    payMoneyLL.setBackgroundResource(R.drawable.shape_mall_goods_ol_vid_red_btn);
                    goSetOrderLL.setBackgroundResource(R.drawable.shape_mall_goods_ol_vid_red_btn_big);
                    goodsSetAll.openToBookingCountdown=0;
                }
            }.start();
            //将此 countDownTimer 放入list.
            timerMap.put(goodsSetAll.id, countDownTimer);
        }
        setNumber.setText("套餐"+ StringUtils.int2chineseNum(i+1));

        buildSetHList(setP, goodsSetAll);
        divMoney.setText("省"+ FormatUtils.moneyKeep2Decimals(goodsSetAll.getSaveAmountOf())+"元");
        allMoney.setText("¥"+FormatUtils.moneyKeep2Decimals(goodsSetAll.combinationPrice));


        orderDivMoney.setText("省"+ FormatUtils.moneyKeep2Decimals(goodsSetAll.getSaveAmountOf())+"元");
        goSetOrder.setText("立即下单 ¥"+FormatUtils.moneyKeep2Decimals(goodsSetAll.combinationPrice));
        payMoney.setText("立即下单");




        goSetOrderLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(goodsSetAll.openToBookingCountdown>0){
                    return;
                }
                if(!checkAllSpecHasShop(goodsSetAll)){
                    GoodsSetCell first=getWhichNoHasShop(goodsSetAll);
                    Toast.makeText(context,"请选择\""+first.goodsTitle+"\"的服务门店",Toast.LENGTH_SHORT).show();
//                    if(onShopAllClickListener!=null){
//                        onShopAllClickListener.onShopAllClick(first,first.goodsId,null);
//                    }
                }else {
                    passOrder(goodsSetAll);
                }
//                goodsSetAll.isShow=true;
//                if(goodsSetAll.isShow){
//                    scrollImg.setImageResource(R.drawable.goods_arrow_up);
//                    setTitleUnderBtnLL.setVisibility(View.VISIBLE);
//                    setTitleUnder.setVisibility(View.VISIBLE);
//                    setTitleUnderSplash.setVisibility(View.GONE);
//                    buildOtherTab(i);
//                }
            }
        });
        payMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(goodsSetAll.openToBookingCountdown>0){
                    return;
                }
                goodsSetAll.isShow=true;
                if(goodsSetAll.isShow){
                    scrollImg.setImageResource(R.drawable.goods_arrow_up);
                    setTitleUnderBtnLL.setVisibility(View.VISIBLE);
                    setTitleUnder.setVisibility(View.VISIBLE);
                    setTitleUnderSplash.setVisibility(View.GONE);
                    buildOtherTab(i);
                }
//                if(!checkAllSpecHasShop(goodsSetAll)){
//                    GoodsSetCell first=getWhichNoHasShop(goodsSetAll);
//                    Toast.makeText(context,"请选择\""+first.goodsTitle+"\"的服务门店",Toast.LENGTH_SHORT).show();
//                    if(onShopAllClickListener!=null){
//                        onShopAllClickListener.onShopAllClick(first,first.goodsId,null);
//                    }
//                }else {
//                    passOrder(goodsSetAll);
//                }
//                Map nokmap = new HashMap<String, String>();
//                nokmap.put("soure","组合套餐弹窗点击【立即下单】");
//                MobclickAgent.onEvent(context, "btn_APP_MaternalandChildGoods_CommodityDetails_CombinedGoods",nokmap);
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
            }
        });

        setTitleTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goodsSetAll.isShow=!goodsSetAll.isShow;
                if(goodsSetAll.isShow){
                    scrollImg.setImageResource(R.drawable.goods_arrow_up);
                    setTitleUnderBtnLL.setVisibility(View.VISIBLE);
                    setTitleUnder.setVisibility(View.VISIBLE);
                    setTitleUnderSplash.setVisibility(View.GONE);
                    buildOtherTab(i);
                }else {
                    scrollImg.setImageResource(R.drawable.goods_arrow_down);
                    setTitleUnder.setVisibility(View.GONE);
                    setTitleUnderBtnLL.setVisibility(View.GONE);
                    setTitleUnderSplash.setVisibility(View.VISIBLE);
                }
            }
        });
        buildSetList(setTitleUnder, goodsSetAll.goodsList);

    }

    private GoodsSetCell getWhichNoHasShop(GoodsSetAll goodsSetAll) {
        for (int i = 0; i <goodsSetAll.goodsList.size() ; i++) {
            GoodsSetCell goodsSetCell=goodsSetAll.goodsList.get(i);
            if(TextUtils.isEmpty(goodsSetCell.getGoodsShopId(context))){
                return goodsSetCell;
            }
        }
        return null;
    }

    private boolean checkAllSpecHasShop(GoodsSetAll goodsSetAll) {
        boolean result=true;
        for (int i = 0; i <goodsSetAll.goodsList.size() ; i++) {
            GoodsSetCell goodsSetCell=goodsSetAll.goodsList.get(i);
            if(TextUtils.isEmpty(goodsSetCell.getGoodsShopId(context))){
                return false;
            }
        }
        return result;
    }

    private void passOrder(GoodsSetAll goodsSetAll) {
        Map nokmap = new HashMap<String, String>();
        nokmap.put("soure","组合套餐弹窗点击【立即下单】");
        MobclickAgent.onEvent(context, "btn_APP_MaternalandChildGoods_CommodityDetails_CombinedGoods",nokmap);
        ArrayList<GoodsBasketCell> list=new ArrayList<>();
        for (int j = 0; j <goodsSetAll.goodsList.size() ; j++) {
            GoodsSetCell goodsSetCell=goodsSetAll.goodsList.get(j);
            GoodsBasketCell goodsBasketCell=new GoodsBasketCell(goodsSetCell.platformPrice,
                    goodsSetCell.platformPrice,
                    0,
                    goodsSetCell.type,
                    "0",
                    "0",null);
            goodsBasketCell.goodsId=goodsSetCell.goodsId;
            goodsBasketCell.setGoodsSpecId(goodsSetCell.skuId);
            goodsBasketCell.goodsTitle=goodsSetCell.goodsTitle;
            goodsBasketCell.goodsSpecDesc=goodsSetCell.skuName;
            goodsBasketCell.goodsImage=goodsSetCell.filePath;
            goodsBasketCell.goodsType=goodsSetCell.type;
            goodsBasketCell.setGoodsQuantity(1);
            goodsBasketCell.goodsShopId= goodsSetCell.getGoodsShopId(context);
            goodsBasketCell.goodsShopName= goodsSetCell.getGoodsShopName(context);
            goodsBasketCell.shopIdList=goodsSetCell.shopIdList;
            goodsBasketCell.mchId=goodsSetAll.merchantId;
            goodsBasketCell.setIsMaster(goodsSetCell.isMaster);
            goodsBasketCell.setAdditionType("1");
            list.add(goodsBasketCell);
        }
        ARouter.getInstance()
                .build(ServiceRoutes.SERVICE_ORDER)
                .withObject("goodsbasketlist",list)
                .withString("packageId", goodsSetAll.id + "")
                .withString("packageQuantity", 1+"")
                .withString("packageMoney", goodsSetAll.combinationPrice + "")
                .navigation();
    }

    public void buildOtherTab(int i){
        for (int j = 0; j < getDatas().size() ; j++) {
            final GoodsSetAll goodsSetAll = getDatas().get(j);
            if(i!=j){
                goodsSetAll.isShow=false;
            }
        }
        notifyDataSetChanged();
    }
    private void buildSetHList(LinearLayout setP, GoodsSetAll goodsSetAll) {
        setP.removeAllViews();
        for (int j = 0; j < goodsSetAll.goodsList.size(); j++) {
            View view = LayoutInflater.from(context).inflate(R.layout.service_item_goodsdetail_set_cell, null);
            final GoodsSetCell goodsSetCel = goodsSetAll.goodsList.get(j);
            ImageView goodsImg = view.findViewById(R.id.goodsImg);
            TextView goodPrice=view.findViewById(R.id.goodPrice);
//            if (j == 0) {
//                view.findViewById(R.id.goodsImgLeft).setVisibility(View.VISIBLE);
//            }
            if (j == goodsSetAll.goodsList.size() - 1) {
                view.findViewById(R.id.goodsAdd).setVisibility(View.GONE);
            }
            goodPrice.setText("¥"+FormatUtils.moneyKeep2Decimals(goodsSetCel.platformPrice));
            com.healthy.library.businessutil.GlideCopy.with(context)
                    .load(goodsSetCel.filePath)
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_1_1_default)

                    .into(goodsImg);
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
            setP.addView(view);
        }
    }

    private void buildSetList(LinearLayout setTitleUnder, List<GoodsSetCell> goodsList) {
        setTitleUnder.removeAllViews();
        for (int i = 0; i < goodsList.size(); i++) {
            final GoodsSetCell goodsSetCell = goodsList.get(i);
            View view = LayoutInflater.from(context).inflate(R.layout.service_item_set_misc, setTitleUnder,false);

            CornerImageView goodsSetImage;
            TextView goodsSetMoney;
            TextView goodsSetTitle;
            TextView goodsSetCount;
            Barrier barrier;
            FlexboxLayout goodsSetSku;
            Spinner spinnerReal;
            ConstraintLayout spinnerDetailLL;
            TextView spinnerTitle;
            TextView spinnerContext;
            ImageView spinnerImg;
            goodsSetImage = (CornerImageView) view.findViewById(R.id.goodsSetImage);
            goodsSetMoney = (TextView) view.findViewById(R.id.goodsSetMoney);
            goodsSetTitle = (TextView) view.findViewById(R.id.goodsSetTitle);
            goodsSetCount = (TextView) view.findViewById(R.id.goodsSetCount);
            barrier = (Barrier) view.findViewById(R.id.barrier);
            goodsSetSku = (FlexboxLayout) view.findViewById(R.id.goodsSetSku);
            spinnerReal = (Spinner) view.findViewById(R.id.spinnerReal);
            spinnerDetailLL = (ConstraintLayout) view.findViewById(R.id.spinnerDetailLL);
            spinnerTitle = (TextView) view.findViewById(R.id.spinnerTitle);
            spinnerContext = (TextView) view.findViewById(R.id.spinnerContext);
            spinnerImg = (ImageView) view.findViewById(R.id.spinnerImg);

            com.healthy.library.businessutil.GlideCopy.with(context)
                    .load(goodsSetCell.filePath)
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_1_1_default)

                    .into(goodsSetImage);
            goodsSetTitle.setText(goodsSetCell.goodsTitle);
            goodsSetMoney.setText("¥"+ FormatUtils.moneyKeep2Decimals(goodsSetCell.platformPrice));
            for (int j = 0; j < goodsSetCell.getSpecCell().size(); j++) {
                GoodsSpecCell goodsSpecCell= goodsSetCell.getSpecCell().get(j);
                goodsSetCell.setmap.put(goodsSpecCell.id,goodsSpecCell.specValue[0]);
            }
            if("1".equals(goodsSetCell.type)){
                buildShopList2(goodsSetSku, goodsSetCell, goodsSetCell.getSpecCell());
            }else {
                buildSepcList2(goodsSetSku, goodsSetCell, goodsSetCell.getSpecCell());
            }
            goodsSetTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("2".equals(goodsSetCell.type)) {
                        ARouter.getInstance()
                                .build(ServiceRoutes.SERVICE_DETAIL)
                                .withString("id", goodsSetCell.goodsId+"")
                                .navigation();
                    } else {
                        ARouter.getInstance()
                                .build(ServiceRoutes.SERVICE_DETAIL)
                                .withString("goodsId",goodsSetCell.goodsId+"")
                                .navigation();
                    }
                }
            });
            goodsSetImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("2".equals(goodsSetCell.type)) {
                        ARouter.getInstance()
                                .build(ServiceRoutes.SERVICE_DETAIL)
                                .withString("id", goodsSetCell.goodsId+"")
                                .navigation();
                    } else {
                        ARouter.getInstance()
                                .build(ServiceRoutes.SERVICE_DETAIL)
                                .withString("goodsId",goodsSetCell.goodsId+"")
                                .navigation();
                    }
                }
            });
            setTitleUnder.addView(view);


        }
    }

    private void buildSepcList2(FlexboxLayout goodsSetSku, final GoodsSetCell goodsSetCell, List<GoodsSpecCell> specCell) {
        goodsSetSku.removeAllViews();
        View view = LayoutInflater.from(context).inflate(R.layout.service_item_set_misc_spinner2, goodsSetSku,false);
        ConstraintLayout spinnerDetailLL;
        TextView spinnerTitle;
        final TextView spinnerContext;
        ImageView spinnerImg;
        spinnerDetailLL = (ConstraintLayout) view.findViewById(R.id.spinnerDetailLL);
        spinnerTitle = (TextView) view.findViewById(R.id.spinnerTitle);
        spinnerContext = (TextView) view.findViewById(R.id.spinnerContext);
        spinnerImg = (ImageView) view.findViewById(R.id.spinnerImg);
        spinnerContext.setText(goodsSetCell.skuName);
        spinnerDetailLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map nokmap = new HashMap<String, String>();
                nokmap.put("soure","组合套餐弹出框，切换规格>");
                MobclickAgent.onEvent(context, "btn_APP_MaternalandChildGoods_CommodityDetails_CombinedGoods_SelectSpecifications",nokmap);
                if(onSpecAllClickListener!=null){
                    onSpecAllClickListener.onSpecAllClick(goodsSetCell,goodsSetCell.goodsId+"",goodsSetCell.setmap);
                }
            }
        });
        if(!TextUtils.isEmpty(goodsSetCell.skuName)){
            goodsSetSku.addView(view);
        }
    }

    private void buildShopList2(FlexboxLayout goodsSetSku, final GoodsSetCell goodsSetCell, List<GoodsSpecCell> specCell) {
        goodsSetSku.removeAllViews();
        View view = LayoutInflater.from(context).inflate(R.layout.service_item_set_misc_spinner2, goodsSetSku,false);
        ConstraintLayout spinnerDetailLL;
        TextView spinnerTitle;
        final TextView spinnerContext;
        ImageView spinnerImg;
        spinnerDetailLL = (ConstraintLayout) view.findViewById(R.id.spinnerDetailLL);
        spinnerTitle = (TextView) view.findViewById(R.id.spinnerTitle);
        spinnerContext = (TextView) view.findViewById(R.id.spinnerContext);
        spinnerImg = (ImageView) view.findViewById(R.id.spinnerImg);
        spinnerContext.setText(TextUtils.isEmpty(goodsSetCell.goodsShopName)?"请选择服务商品门店":goodsSetCell.goodsShopName);
        spinnerDetailLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Map nokmap = new HashMap<String, String>();
//                nokmap.put("soure","组合套餐弹出框，切换规格>");
//                MobclickAgent.onEvent(context, "btn_APP_MaternalandChildGoods_CommodityDetails_CombinedGoods_SelectSpecifications",nokmap);
                if(onShopAllClickListener!=null){
                    onShopAllClickListener.onShopAllClick(goodsSetCell,goodsSetCell.goodsId+"",null);
                }
            }
        });
//        if(!TextUtils.isEmpty(goodsSetCell.skuName)){
            goodsSetSku.addView(view);
//        }
    }


    private void buildSepcList(FlexboxLayout goodsSetSku, final GoodsSetCell goodsSetCell, List<GoodsSpecCell> specCell) {
        goodsSetSku.removeAllViews();
        for (int i = 0; i < specCell.size(); i++) {
            final GoodsSpecCell goodsSpecCell = specCell.get(i);
            View view = LayoutInflater.from(context).inflate(R.layout.service_item_set_misc_spinner, goodsSetSku,false);
            final Spinner spinnerReal;
            ConstraintLayout spinnerDetailLL;
            TextView spinnerTitle;
            final TextView spinnerContext;
            ImageView spinnerImg;
            spinnerReal = (Spinner) view.findViewById(R.id.spinnerReal);
            spinnerDetailLL = (ConstraintLayout) view.findViewById(R.id.spinnerDetailLL);
            spinnerTitle = (TextView) view.findViewById(R.id.spinnerTitle);
            spinnerContext = (TextView) view.findViewById(R.id.spinnerContext);
            spinnerImg = (ImageView) view.findViewById(R.id.spinnerImg);
            spinnerContext.setText(goodsSpecCell.specValue[0]);
            spinnerDetailLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    spinnerReal.performClick();
                }
            });
            spinnerTitle.setText(goodsSpecCell.getSpecName());
            spinnerReal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ////System.out.println("点击的位置:"+position);
                    spinnerContext.setText(goodsSpecCell.specValue[position]);
                    goodsSetCell.setmap.put(goodsSpecCell.id,goodsSpecCell.specValue[position]);
                    if(onSpecClickListener!=null){
                        onSpecClickListener.onSpecClick(goodsSetCell,goodsSetCell.goodsId+"",goodsSetCell.setmap);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            spinnerReal.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if(spinnerReal.getWidth()!=0){
                        spinnerReal.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        spinnerReal.setDropDownWidth(spinnerReal.getWidth()); //下拉宽度
                    }
                }
            });
            ArrayAdapter<String> adapter=new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, goodsSpecCell.specValue);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerReal.setAdapter(adapter);
            goodsSetSku.addView(view);


        }
    }
    public interface OnSpecClickListener{
        void onSpecClick(GoodsSetCell goodsSetCell, String goodsId, Map<String,Object> specValue);
    }

    public interface OnSpecAllClickListener{
        void onSpecAllClick(GoodsSetCell goodsSetCell, String goodsId, Map<String,Object> specValue);
    }

    public interface OnShopClickListener{
        void onShopClick(GoodsSetCell goodsSetCell, String goodsId, Map<String,Object> specValue);
    }

    public interface OnShopAllClickListener{
        void onShopAllClick(GoodsSetCell goodsSetCell, String goodsId, Map<String,Object> specValue);
    }
}
