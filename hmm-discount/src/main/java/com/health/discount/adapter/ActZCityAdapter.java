//package com.health.discount.adapter;
//
//import android.graphics.Paint;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.HorizontalScrollView;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.constraintlayout.widget.ConstraintLayout;
//
//import com.alibaba.android.arouter.launcher.ARouter;
//import com.alibaba.android.vlayout.LayoutHelper;
//import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
//import com.health.discount.R;
//import com.healthy.library.base.BaseAdapter;
//import com.healthy.library.base.BaseHolder;
//import com.healthy.library.model.ActGoodsCityItem;
//import com.healthy.library.model.ShopInfo;
//import com.healthy.library.routes.SecondRoutes;
//import com.healthy.library.utils.FormatUtils;
//import com.healthy.library.utils.NavigateUtils;
//import com.healthy.library.utils.ScreenUtils;
//import com.healthy.library.utils.TransformUtil;
//import com.healthy.library.widget.AutoClickImageView;
//import com.healthy.library.widget.CornerImageView;
//import com.healthy.library.widget.DashLine;
//import com.healthy.library.widget.ImageTextView;
//
//import java.util.List;
//
//public class ActZCityAdapter extends BaseAdapter<ShopInfo> {
//
//
//
//    @Override
//    public int getItemViewType(int position) {
//        return 14;
//    }
//
//    public ActZCityAdapter() {
//        this(R.layout.dis_item_fragment_city);
//    }
//
//    private ActZCityAdapter(int viewId) {
//        super(viewId);
//    }
//
//    @Override
//    public LayoutHelper onCreateLayoutHelper() {
//        return new LinearLayoutHelper();
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {
//         ShopInfo shopInfo=getDatas().get(i);
//         LinearLayout cityShopLin;
//         ImageTextView topCityTitle;
//         ImageTextView topSecondTitle;
//         ImageTextView topMore;
//         ConstraintLayout shopCon;
//         ImageView shopIcon;
//         ImageView videoTip;
//         TextView shopName;
//         ImageTextView loc;
//         TextView locDistance;
//         ImageView couponMore;
//         HorizontalScrollView goodsCouponPLL;
//         LinearLayout goodsCouponLL;
//         DashLine couponDash;
//         RelativeLayout headNowLL;
//         CornerImageView headIcon;
//         CornerImageView headIcon3;
//         TextView manCount;
//        LinearLayout bottomGrid;
//         View cityTopTitle=baseHolder.itemView.findViewById(R.id.cityTopTitle);
//        cityShopLin = (LinearLayout) baseHolder.itemView.findViewById(R.id.cityShopLin);
//        topCityTitle = (ImageTextView) baseHolder.itemView.findViewById(R.id.topCityTitle);
//        topSecondTitle = (ImageTextView)baseHolder.itemView. findViewById(R.id.topSecondTitle);
//        topMore = (ImageTextView) baseHolder.itemView.findViewById(R.id.topMore);
//        shopCon = (ConstraintLayout) baseHolder.itemView.findViewById(R.id.shopCon);
//        shopIcon = (ImageView)baseHolder.itemView. findViewById(R.id.shopIcon);
//        videoTip = (ImageView) baseHolder.itemView.findViewById(R.id.videoTip);
//        shopName = (TextView) baseHolder.itemView.findViewById(R.id.shopName);
//        loc = (ImageTextView) baseHolder.itemView.findViewById(R.id.loc);
//        locDistance = (TextView)baseHolder.itemView. findViewById(R.id.locDistance);
//        couponMore = (ImageView) baseHolder.itemView.findViewById(R.id.couponMore);
//        goodsCouponPLL = (HorizontalScrollView) baseHolder.itemView.findViewById(R.id.goodsCouponPLL);
//        goodsCouponLL = (LinearLayout)baseHolder.itemView. findViewById(R.id.goodsCouponLL);
//        couponDash = (DashLine) baseHolder.itemView.findViewById(R.id.couponDash);
//        headNowLL = (RelativeLayout) baseHolder.itemView.findViewById(R.id.headNowLL);
//        headIcon = (CornerImageView) baseHolder.itemView.findViewById(R.id.head_icon);
//        headIcon3 = (CornerImageView) baseHolder.itemView.findViewById(R.id.head_icon3);
//        manCount = (TextView) baseHolder.itemView.findViewById(R.id.manCount);
//        bottomGrid = (LinearLayout)baseHolder.itemView. findViewById(R.id.bottomGrid);
//        cityTopTitle.setVisibility(i==0?View.VISIBLE:View.GONE);
//        onSucessGetList(cityShopLin,shopIcon,shopName,loc,shopCon,locDistance,topMore,bottomGrid,shopInfo.actGoodsCityItems,shopInfo);
//    }
//
//    public void onSucessGetList(LinearLayout cityShopLin,ImageView shopIcon,TextView shopName,ImageTextView loc,ConstraintLayout shopCon,TextView locDistance,ImageTextView topMore,LinearLayout bottomGrid,List<ActGoodsCityItem> result, final ShopInfo shopInfo) {
//        if (shopInfo != null) {
//            cityShopLin.setVisibility(View.VISIBLE);
//            com.healthy.library.businessutil.GlideCopy.with(context).load(shopInfo.filePath)
//                    .placeholder(R.drawable.img_1_1_default2)
//                    .error(R.drawable.img_1_1_default)
//
//                    .into(shopIcon);
//            shopName.setText(shopInfo.shopName);
//            loc.setText(shopInfo.addressProvince + shopInfo.addressCity + shopInfo.addressArea + shopInfo.shopAddress);
//            loc.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (!TextUtils.isEmpty(shopInfo.latitude) && !TextUtils.isEmpty(shopInfo.longitude)) {
//                        NavigateUtils.navigate(context, shopInfo.shopAddress,
//                                shopInfo.latitude + "", shopInfo.longitude + "");
//                    }
//
//                }
//            });
//            shopCon.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ARouter.getInstance()
//                            .build(SecondRoutes.SECOND_SHOP_DETAIL)
//                            .withString("shopId", shopInfo.shopId + "")
//                            .navigation();
//                }
//            });
//            String shopDistanceDes = "";
//            if (shopInfo.distance >= 1000) {
//                shopDistanceDes = String.format("%.1f", (double) shopInfo.distance / 1000) + "km";
//            } else {
//                shopDistanceDes = shopInfo.distance + "m";
//            }
//            locDistance.setText(shopDistanceDes);
//            topMore.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ARouter.getInstance()
//                            .build(SecondRoutes.SECOND_SHOP_DETAIL)
//                            .withString("shopId", shopInfo.shopId + "")
//                            .navigation();
//                }
//            });
//            if (result != null) {
//                buildShopGoods(bottomGrid,result);
//            }
//        } else {
//            cityShopLin.setVisibility(View.GONE);
//        }
//    }
//    int mMargin;
//    private void buildShopGoods(LinearLayout bottomGrid,List<ActGoodsCityItem> result) {
//        bottomGrid.removeAllViews();
////        if (mMargin == 0) {
////            mMargin = (int) TransformUtil.dp2px(context, 2);
////        }
////        int size = 0;
////        if (result.size() <= 3) {
////            size = result.size();
////        } else if (result.size() > 3 && result.size() < 6) {
////            size = 3;
////        } else {
////            size = 6;
////        }
////        bottomGrid.setRowCount(3);
//        for (int i = 0; i < result.size(); i++) {
//            int row = 3;
//            int w = (int)((ScreenUtils.getScreenWidth(context)- TransformUtil.dp2px(context, 12+14))/ row);
//
//            View viewparent = LayoutInflater.from(context).inflate(R.layout.dis_item_block_child2_item_sq, bottomGrid, false);
//            final ActGoodsCityItem goodsCityItem = result.get(i);
//            ImageView goodsImg = viewparent.findViewById(R.id.goodsImg);
//            TextView goodsName = viewparent.findViewById(R.id.goodsName);
//            TextView goodsMoney = viewparent.findViewById(R.id.goodsMoney);
//            TextView pinOldPrice = viewparent.findViewById(R.id.pinOldPrice);
//            AutoClickImageView passbasket = viewparent.findViewById(R.id.passbasket);
//
//            com.healthy.library.businessutil.GlideCopy.with(context).load(goodsCityItem.filePath)
//                    .placeholder(R.drawable.img_1_1_default2)
//                    .error(R.drawable.img_1_1_default)
//
//                    .into(goodsImg);
//            goodsName.setText(goodsCityItem.goodsTitle);
//            goodsMoney.setText(FormatUtils.moneyKeep2Decimals(goodsCityItem.platformPrice) + "");
//            pinOldPrice.setText("¥" + FormatUtils.moneyKeep2Decimals(goodsCityItem.retailPrice));
//            pinOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//            if(goodsCityItem.retailPrice==0){
//                pinOldPrice.setVisibility(View.INVISIBLE);
//            }else {
//                pinOldPrice.setVisibility(View.VISIBLE);
//            }
//            viewparent.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ARouter.getInstance()
//                            .build(SecondRoutes.SECOND_SERVICE_DETAIL)
//                            .withString("id", goodsCityItem.goodsId + "")
//                            .withString("shopId", goodsCityItem.shopId + "")
//                            .navigation();
//                }
//            });
//            bottomGrid.addView(viewparent);
//        }
//    }
////    List<ActGoodsCityItem> actGoodsCityItems;
////    ShopInfo shopInfo;
////    public void setCityData(List<ActGoodsCityItem> actGoodsCityItems, ShopInfo shopInfo) {
////        this.actGoodsCityItems=actGoodsCityItems;
////        this.shopInfo=shopInfo;
////    }
//}
