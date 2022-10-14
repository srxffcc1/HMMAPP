package com.health.second.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.second.R;
import com.health.second.model.SecondAct;
import com.healthy.library.LibApplication;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.businessutil.GlideCopy;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.constant.SpKey;
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
import java.util.List;

public class SecondMainLogosAdapter extends BaseAdapter<String> {


    private List<String> logos;
    private List<SecondAct> secondActs;


    @Override
    public int getItemViewType(int position) {
        return 35;
    }

    public SecondMainLogosAdapter() {
        this(R.layout.item_second_logos);
    }

    private SecondMainLogosAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {
        ConstraintLayout adLL;
        ImageView topAd;
        ImageView logosMore;
        HorizontalScrollView logosLL;
        GridLayout topRecommend;
        LinearLayout logosLLS = baseHolder.itemView.findViewById(R.id.logosLLS);
        adLL = (ConstraintLayout) baseHolder.itemView.findViewById(R.id.adLL);
        topAd = (ImageView) baseHolder.itemView.findViewById(R.id.topAd);
        logosMore = (ImageView) baseHolder.itemView.findViewById(R.id.logosMore);
        logosLL = (HorizontalScrollView) baseHolder.itemView.findViewById(R.id.logosLL);
        topRecommend = (GridLayout) baseHolder.itemView.findViewById(R.id.topRecommend);
        topAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moutClickListener != null) {
                    moutClickListener.outClick("综合活动", null);
                }
            }
        });
        addFunctions(context, logosLLS, logos);
        addFunctions(context, topRecommend, secondActs);
    }

    private void addFunctions(final Context context, final LinearLayout logosLLS, final List<String> logos) {
        if(logosLLS.getChildCount()>0){
            return;
        }
        logosLLS.removeAllViews();
        if (logos != null && logos.size() > 0) {
            for (int i = 0; i < logos.size(); i++) {
                View view = LayoutInflater.from(context).inflate(R.layout.item_second_logo, logosLLS, false);
                ImageView shoplogo;
                shoplogo = (ImageView) view.findViewById(R.id.shoplogo);
                GlideCopy.with(context)
                        .load(logos.get(i))
                        .placeholder(R.drawable.img_1_1_default2)
                        .error(R.drawable.img_1_1_default)

                        .into(shoplogo);

                logosLLS.addView(view);
            }

        }
    }

    private void addFunctions(final Context context, final GridLayout logosLLS, final List<SecondAct> logos) {
        logosLLS.removeAllViews();
        if (logos != null && logos.size() > 0) {
            for (int i = 0; i < logos.size(); i++) {
                View view = LayoutInflater.from(context).inflate(R.layout.item_second_toprecommand, logosLLS, false);

                final SecondAct secondAct = logos.get(i);
                ImageTextView shopName;
                CornerImageView goodsPic;
                TextView goodsName;
                TextView goodsMoneyActFlag;
                TextView goodsMoneyFlag;
                TextView goodsMoney;
                TextView goodsAct;
                TextView goodsMoneyOld;
                LinearLayout actTimeLL;
                LinearLayout goodsTimeLL;
//                TextView kickHour;
//                TextView kickMin;
//                TextView kickSec;
                TextView goodsTimeText=view.findViewById(R.id.goodsTimeText);
                TextView actPeople;
                ImageView soldOutImg;
                shopName = (ImageTextView) view.findViewById(R.id.shopName);
                goodsPic = (CornerImageView) view.findViewById(R.id.goodsPic);
                goodsName = (TextView) view.findViewById(R.id.goodsName);
                goodsMoneyActFlag = (TextView) view.findViewById(R.id.goodsMoneyActFlag);
                goodsMoneyFlag = (TextView) view.findViewById(R.id.goodsMoneyFlag);
                goodsMoney = (TextView) view.findViewById(R.id.goodsMoney);
                goodsAct = (TextView) view.findViewById(R.id.goodsAct);
                goodsMoneyOld = (TextView) view.findViewById(R.id.goodsMoneyOld);
                actTimeLL = (LinearLayout) view.findViewById(R.id.actTimeLL);
                goodsTimeLL = (LinearLayout) view.findViewById(R.id.goodsTimeLL);
//                kickHour = (TextView) view.findViewById(R.id.kickHour);
//                kickMin = (TextView) view.findViewById(R.id.kickMin);
//                kickSec = (TextView) view.findViewById(R.id.kickSec);
                actPeople = (TextView) view.findViewById(R.id.actPeople);
                soldOutImg=view.findViewById(R.id.soldOutImg);
                actTimeLL.setVisibility(View.INVISIBLE);
                goodsMoneyActFlag.setVisibility(View.GONE);
                goodsAct.setVisibility(View.VISIBLE);
                goodsMoneyOld.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);
                soldOutImg.setVisibility(View.GONE);
                if (secondAct.marketingType == 3) {
                    goodsAct.setText("秒杀价");
                    actPeople.setText(ParseUtils.parseNumberWithAdd(secondAct.sales+"",10000,"万") +"人已抢");
                    actTimeLL.setVisibility(View.VISIBLE);
                    goodsName.setText(secondAct.goodsTitle);
                    goodsMoneyOld.setText("￥"+ FormatUtils.moneyKeep2Decimals(secondAct.retailPrice));
                    goodsMoney.setText(FormatUtils.moneyKeep2Decimals(secondAct.getMarketingPrice()));
                    if(secondAct.flashSaleInfo!=null){

                        String[] limits= DateUtils.getDistanceTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),secondAct.flashSaleInfo.endTime);
                        goodsTimeText.setText("距结束"+""+limits[0]+"天"+limits[1]+":"+limits[2]+":"+limits[3]+"");
                    }else {

                        goodsTimeText.setText("距结束"+""+0+"天"+0+":"+0+":"+0+"");
                    }
//                    kickHour.setText(limits[0]);
//                    kickMin.setText(limits[1]);
//                    kickSec.setText(limits[2]);

                    shopName.setText(secondAct.getMerchantName());
                    com.healthy.library.businessutil.GlideCopy.with(context).load(secondAct.filePath)
                            .placeholder(R.drawable.img_1_1_default2)
                            .error(R.drawable.img_1_1_default)

                            .into(goodsPic);
                    if(secondAct.availableInventory<=0){
                        soldOutImg.setVisibility(View.VISIBLE);
                    }
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(secondAct.availableInventory<=0){
                                Toast.makeText(context, "当前商品已抢完，看看其他商品吧~", Toast.LENGTH_LONG).show();
                                return;
                            }
                            if(secondAct.flashSaleInfo.userId.equals(LocUtil.getUserId())&&!TextUtils.isEmpty(LocUtil.getUserId())){
                                ARouter.getInstance()
                                        .build(ServiceRoutes.SERVICE_DETAIL)
                                        .withString("id", secondAct.goodsId)
                                        .withString("marketingType", "3")
                                        .navigation();
                            }else {
                                ARouter.getInstance()
                                        .build(SecondRoutes.SECOND_SERVICE_DETAIL)
                                        .withString("id", secondAct.goodsId)
                                        .withString("merchantId", SpUtils.getValue(LibApplication.getAppContext(), SpKey.CHOSE_MC))
                                        .withString("marketingType", "3")
                                        .navigation();
                            }
                        }
                    });
                } else if (secondAct.marketingType == 2) {
                    goodsAct.setText("拼团价");
                    actPeople.setText(ParseUtils.parseNumberWithAdd(secondAct.assembleInfo.joinNum+"",10000,"万") +"人已抢");
                    goodsName.setText(secondAct.assembleInfo.goodsTitle);
                    goodsMoneyOld.setText("￥"+FormatUtils.moneyKeep2Decimals(secondAct.assembleInfo.marketingGoodsChildDTOS.get(0).retailPrice));
                    goodsMoney.setText(FormatUtils.moneyKeep2Decimals(secondAct.assembleInfo.assemblePrice));
                    shopName.setText(secondAct.getMerchantName());
                    com.healthy.library.businessutil.GlideCopy.with(context).load(secondAct.assembleInfo.goodsImage)
                            .placeholder(R.drawable.img_1_1_default2)
                            .error(R.drawable.img_1_1_default)

                            .into(goodsPic);
                    if(secondAct.assembleInfo.marketingGoodsChildDTOS.get(0).availableInventory<=0){
                        soldOutImg.setVisibility(View.VISIBLE);
                    }
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(secondAct.assembleInfo.marketingGoodsChildDTOS.get(0).availableInventory<=0){
                                Toast.makeText(context, "当前商品已抢完，看看其他商品吧~", Toast.LENGTH_LONG).show();
                                return;
                            }
                            if(secondAct.assembleInfo.merchantId.equals(LocUtil.getUserId())&&!TextUtils.isEmpty(LocUtil.getUserId())){
                                ARouter.getInstance()
                                        .build(ServiceRoutes.SERVICE_DETAIL)
                                        .withString("assembleId",secondAct.assembleInfo.id+"")
                                        .withString("shopId", SpUtils.getValue(context, SpKey.CHOSE_SHOP))
                                        .navigation();
                            }else {
                                ARouter.getInstance()
                                        .build(SecondRoutes.SECOND_SERVICE_DETAIL)
                                        .withString("assembleId",secondAct.assembleInfo.id+"")
                                        .withString("shopId", SpUtils.getValue(context, SpKey.CHOSE_SHOP))
                                        .navigation();
                            }
                        }
                    });
                } else {
                    actPeople.setText(ParseUtils.parseNumberWithAdd(secondAct.bargainInfo.joinNum+"",10000,"万") +"人已抢");
                    goodsMoneyActFlag.setVisibility(View.VISIBLE);
                    goodsAct.setVisibility(View.GONE);
                    goodsName.setText(secondAct.bargainInfo.goodsTitle);
                    goodsMoneyOld.setText("￥"+FormatUtils.moneyKeep2Decimals(secondAct.bargainInfo.marketingGoodsChildDTOS.get(0).retailPrice));
                    goodsMoney.setText(FormatUtils.moneyKeep2Decimals(secondAct.bargainInfo.floorPrice));
                    shopName.setText(secondAct.getMerchantName());
                    com.healthy.library.businessutil.GlideCopy.with(context).load(secondAct.bargainInfo.goodsImage)
                            .placeholder(R.drawable.img_1_1_default2)
                            .error(R.drawable.img_1_1_default)

                            .into(goodsPic);
                    if(secondAct.bargainInfo.marketingGoodsChildDTOS.get(0).availableInventory<=0){
                        soldOutImg.setVisibility(View.VISIBLE);
                    }
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(secondAct.bargainInfo.marketingGoodsChildDTOS.get(0).availableInventory<=0){
                                Toast.makeText(context, "当前商品已抢完，看看其他商品吧~", Toast.LENGTH_LONG).show();
                                return;
                            }
                            if(secondAct.bargainInfo.merchantId.equals(LocUtil.getUserId())&&!TextUtils.isEmpty(LocUtil.getUserId())){
                                ARouter.getInstance()
                                        .build(ServiceRoutes.SERVICE_DETAIL)
                                        .withString("bargainId", secondAct.bargainInfo.id+"")
                                        .withString("shopId", SpUtils.getValue(context, SpKey.CHOSE_SHOP))
                                        .navigation();
                            }else {
                                ARouter.getInstance()
                                        .build(SecondRoutes.SECOND_SERVICE_DETAIL)
                                        .withString("bargainId", secondAct.bargainInfo.id+"")
                                        .withString("shopId", SpUtils.getValue(context, SpKey.CHOSE_SHOP))
                                        .navigation();
                            }
                        }
                    });
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    GridLayout.LayoutParams param = new GridLayout.LayoutParams(GridLayout.spec(
                            GridLayout.UNDEFINED, GridLayout.FILL, 1f),
                            GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f));
                    view.setLayoutParams(param);
                }
                logosLLS.addView(view);
            }

        }
    }

    private void initView() {

    }

    public void setLogos(List<String> logos) {
        this.logos = logos;
    }

    public void setSecondActs(List<SecondAct> secondActs) {
        this.secondActs = secondActs;
    }

    public List<SecondAct> getSecondActs() {
        return secondActs;
    }
}
