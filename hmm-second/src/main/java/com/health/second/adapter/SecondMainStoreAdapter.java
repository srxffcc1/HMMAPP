package com.health.second.adapter;

import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.second.R;
import com.healthy.library.model.SecondSortGoods;
import com.health.second.model.SecondSortShop;
import com.healthy.library.LibApplication;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.constant.SpKey;
import com.healthy.library.routes.SecondRoutes;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.NavigateUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.AutoClickImageView;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.DashLine;
import com.healthy.library.widget.ImageTextView;

import java.util.List;

public class SecondMainStoreAdapter extends BaseAdapter<SecondSortShop> {

    @Override
    public int getItemViewType(int position) {
        return 6;
    }

    public SecondMainStoreAdapter() {
        this(R.layout.item_second_mian_store);
    }

    private SecondMainStoreAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {
        SecondSortShop shopInfo=getDatas().get(i);
         LinearLayout cityShopLin;
         ImageTextView topCityTitle;
         ImageTextView topSecondTitle;
         ImageTextView topMore;
         ConstraintLayout shopCon;
         ImageView shopIcon;
         ImageView videoTip;
         TextView shopName;
         ImageTextView loc;
         TextView locDistance;
         ImageView couponMore;
         HorizontalScrollView goodsCouponPLL;
         LinearLayout goodsCouponLL;
         DashLine couponDash;
         RelativeLayout headNowLL;
         CornerImageView headIcon;
         CornerImageView headIcon3;
         TextView manCount;
        LinearLayout bottomGrid;
         View cityTopTitle=baseHolder.itemView.findViewById(R.id.cityTopTitle);
        cityShopLin = (LinearLayout) baseHolder.itemView.findViewById(R.id.cityShopLin);
        topCityTitle = (ImageTextView) baseHolder.itemView.findViewById(R.id.topCityTitle);
        topSecondTitle = (ImageTextView)baseHolder.itemView. findViewById(R.id.topSecondTitle);
        topMore = (ImageTextView) baseHolder.itemView.findViewById(R.id.topMore);
        shopCon = (ConstraintLayout) baseHolder.itemView.findViewById(R.id.shopCon);
        shopIcon = (ImageView)baseHolder.itemView. findViewById(R.id.shopIcon);
        videoTip = (ImageView) baseHolder.itemView.findViewById(R.id.videoTip);
        shopName = (TextView) baseHolder.itemView.findViewById(R.id.shopName);
        loc = (ImageTextView) baseHolder.itemView.findViewById(R.id.loc);
        locDistance = (TextView)baseHolder.itemView. findViewById(R.id.locDistance);
        couponMore = (ImageView) baseHolder.itemView.findViewById(R.id.couponMore);
        goodsCouponPLL = (HorizontalScrollView) baseHolder.itemView.findViewById(R.id.goodsCouponPLL);
        goodsCouponLL = (LinearLayout)baseHolder.itemView. findViewById(R.id.goodsCouponLL);
        couponDash = (DashLine) baseHolder.itemView.findViewById(R.id.couponDash);
        headNowLL = (RelativeLayout) baseHolder.itemView.findViewById(R.id.headNowLL);
        headIcon = (CornerImageView) baseHolder.itemView.findViewById(R.id.head_icon);
        headIcon3 = (CornerImageView) baseHolder.itemView.findViewById(R.id.head_icon3);
        manCount = (TextView) baseHolder.itemView.findViewById(R.id.manCount);
        bottomGrid = (LinearLayout)baseHolder.itemView. findViewById(R.id.bottomGrid);
        cityTopTitle.setVisibility(i==0?View.GONE:View.GONE);
        onSucessGetList(cityShopLin,shopIcon,shopName,loc,shopCon,locDistance,topMore,bottomGrid,shopInfo.goodsDTOList,shopInfo);
    }

    public void onSucessGetList(LinearLayout cityShopLin, ImageView shopIcon, TextView shopName, ImageTextView loc, ConstraintLayout shopCon, TextView locDistance, ImageTextView topMore, LinearLayout bottomGrid, List<SecondSortGoods> result, final SecondSortShop shopInfo) {
        if (shopInfo != null) {
            cityShopLin.setVisibility(View.VISIBLE);
            com.healthy.library.businessutil.GlideCopy.with(context).load(shopInfo.filePath)
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_1_1_default)

                    .into(shopIcon);
            shopName.setText(shopInfo.shopName);
            loc.setText(shopInfo.provinceName + shopInfo.cityName + shopInfo.districtName + shopInfo.shopAddress);
            loc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(shopInfo.latitude) && !TextUtils.isEmpty(shopInfo.longitude)) {
                        NavigateUtils.navigate(context, shopInfo.shopAddress,
                                shopInfo.latitude + "", shopInfo.longitude + "");
                    }

                }
            });
            shopCon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ARouter.getInstance()
                            .build(SecondRoutes.SECOND_SHOP_DETAIL)
                            .withString("shopId", shopInfo.shopId + "")
                            .navigation();
                }
            });
            String shopDistanceDes = "";
            if (shopInfo.distance >= 1000) {
                shopDistanceDes = String.format("%.1f", (double) shopInfo.distance / 1000) + "km";
            } else {
                shopDistanceDes = shopInfo.distance + "m";
            }
            locDistance.setText(shopDistanceDes);
            topMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ARouter.getInstance()
                            .build(SecondRoutes.SECOND_SHOP_DETAIL)
                            .withString("shopId", shopInfo.shopId + "")
                            .navigation();
                }
            });
            if (result != null) {
                buildShopGoods(bottomGrid,result,shopInfo);
            }
        } else {
            cityShopLin.setVisibility(View.GONE);
        }
    }
    int mMargin;
    private void buildShopGoods(LinearLayout bottomGrid,List<SecondSortGoods> result,final SecondSortShop secondSortShop) {
        bottomGrid.removeAllViews();
        int needsize=(result!=null&&result.size()>=6)?6:result.size();
        for (int i = 0; i < needsize; i++) {


            View viewparent = LayoutInflater.from(context).inflate(R.layout.item_second_main_hgoods, bottomGrid, false);
            final SecondSortGoods goodsCityItem = result.get(i);
            ImageView goodsImg = viewparent.findViewById(R.id.goodsImg);
            TextView goodsName = viewparent.findViewById(R.id.goodsName);
            TextView goodsMoney = viewparent.findViewById(R.id.goodsMoney);
            TextView pinOldPrice = viewparent.findViewById(R.id.pinOldPrice);
            AutoClickImageView passbasket = viewparent.findViewById(R.id.passbasket);

            com.healthy.library.businessutil.GlideCopy.with(context).load(goodsCityItem.getFilePath())
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_1_1_default)

                    .into(goodsImg);
            goodsName.setText(goodsCityItem.goodsTitle);
            goodsMoney.setText(FormatUtils.moneyKeep2Decimals(goodsCityItem.platformPrice) + "");
            pinOldPrice.setText("¥" + FormatUtils.moneyKeep2Decimals(goodsCityItem.retailPrice));
            pinOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            viewparent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(goodsCityItem.userId.equals(LocUtil.getUserId())&&!TextUtils.isEmpty(LocUtil.getUserId())){
                        ARouter.getInstance()
                                .build(ServiceRoutes.SERVICE_DETAIL)
                                .withString("id", goodsCityItem.id)
                                .withString("merchantId", SpUtils.getValue(LibApplication.getAppContext(), SpKey.CHOSE_MC))
                                .navigation();
                    }else {
                        ARouter.getInstance()
                                .build(SecondRoutes.SECOND_SERVICE_DETAIL)
                                .withString("id", goodsCityItem.id)
                                .withString("merchantId", SpUtils.getValue(LibApplication.getAppContext(), SpKey.CHOSE_MC))
                                .navigation();
                    }
                }
            });
            bottomGrid.addView(viewparent);
        }
    }
}
