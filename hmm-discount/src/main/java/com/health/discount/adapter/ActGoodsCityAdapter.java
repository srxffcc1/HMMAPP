//package com.health.discount.adapter;
//
//import android.view.View;
//import android.widget.HorizontalScrollView;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.constraintlayout.widget.ConstraintLayout;
//import androidx.constraintlayout.widget.Group;
//
//import com.alibaba.android.arouter.launcher.ARouter;
//import com.alibaba.android.vlayout.LayoutHelper;
//import com.bumptech.glide.Glide;
//import com.health.discount.R;
//import com.healthy.library.base.BaseHolder;
//import com.healthy.library.base.BaseMultiItemAdapter;
//import com.healthy.library.model.ActGoodsCityItem;
//import com.healthy.library.routes.ServiceRoutes;
//import com.healthy.library.utils.FormatUtils;
//import com.healthy.library.widget.AutoClickImageView;
//import com.healthy.library.widget.ImageTextView;
//import com.healthy.library.widget.StaggeredGridLayoutHelperFix;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class ActGoodsCityAdapter extends BaseMultiItemAdapter<ActGoodsCityItem> {
//
//    public String key;
//
//
//    public Map<String, String> imageScalemap = new HashMap<>();
//
//    public void setKey(String key) {
//        this.key = key;
//    }
//
//    public void setOnBasketClickListener(OnBasketClickListener onBasketClickListener) {
//        this.onBasketClickListener = onBasketClickListener;
//    }
//
//    OnBasketClickListener onBasketClickListener;
//
//    @Override
//    public int getItemViewType(int position) {
//        return getDefItemViewType(position);
//    }
//
//    public ActGoodsCityAdapter() {
//        this(R.layout.item_mall_goodswithsamecity);
//        addItemType(1, R.layout.item_mall_goodswithsamecity);
//        addItemType(2, R.layout.item_mall_goodswithsamecity);
//        addItemType(3, R.layout.item_mall_goodswithsamecity);
//        addItemType(4, R.layout.item_mall_goodswithsamecity);
//    }
//
//    ActGoodsCityAdapter(int viewId) {
//        super(viewId);
//    }
//
//    @Override
//    public LayoutHelper onCreateLayoutHelper() {
//        StaggeredGridLayoutHelperFix helper = new StaggeredGridLayoutHelperFix();
//        helper.setMargin(6, 0, 6, 0);
//        helper.setLane(2);
//        helper.setHGap(3);
//        return helper;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull BaseHolder baseHolder, final int position) {
//        ImageView mallGoodsImg;
//        TextView numberTmp;
//        ImageTextView mallOnlineTip;
//        ConstraintLayout mallGoodsImgUnder;
//        TextView mallGoodsContext;
//        TextView mallGoodsSpace;
//        ImageView spinnerImg;
//        TextView mallGoodsMoneyflag;
//        TextView mallGoodsMoneyvalue;
//        TextView mallGoodsMoneyvalueOrg;
//        TextView mallGoodsMoneyvalueNowFlag;
//        TextView mallGoodsSales;
//        AutoClickImageView passbasket;
//        LinearLayout vipGoldP;
//        HorizontalScrollView couponListLL;
//        LinearLayout couponList;
//        TextView mallGoodsSales2;
//        TextView mallGoodsSales2Dis;
//        Group normalTypeGroup;
//        Group seachTypeGroup;
//        ImageTextView mallService;
//        ImageTextView mallGoodsAddress;
//        TextView mallGoodsDistance;
//        mallGoodsImg = (ImageView) baseHolder.itemView.findViewById(R.id.mall_goods_img);
//        numberTmp = (TextView) baseHolder.itemView.findViewById(R.id.numberTmp);
//        mallOnlineTip = (ImageTextView) baseHolder.itemView.findViewById(R.id.mall_online_tip);
//        mallGoodsImgUnder = (ConstraintLayout) baseHolder.itemView.findViewById(R.id.mall_goods_img_under);
//        mallGoodsContext = (TextView) baseHolder.itemView.findViewById(R.id.mall_goods_context);
//        mallGoodsSpace = (TextView) baseHolder.itemView.findViewById(R.id.mall_goods_space);
//        spinnerImg = (ImageView) baseHolder.itemView.findViewById(R.id.spinnerImg);
//        mallGoodsMoneyflag = (TextView) baseHolder.itemView.findViewById(R.id.mall_goods_moneyflag);
//        mallGoodsMoneyvalue = (TextView) baseHolder.itemView.findViewById(R.id.mall_goods_moneyvalue);
//        mallGoodsMoneyvalueOrg = (TextView) baseHolder.itemView.findViewById(R.id.mall_goods_moneyvalue_org);
//        mallGoodsMoneyvalueNowFlag = (TextView) baseHolder.itemView.findViewById(R.id.mall_goods_moneyvalue_now_flag);
//        mallGoodsSales = (TextView) baseHolder.itemView.findViewById(R.id.mall_goods_sales);
//        passbasket = (AutoClickImageView) baseHolder.itemView.findViewById(R.id.passbasket);
//        vipGoldP = (LinearLayout) baseHolder.itemView.findViewById(R.id.vipGoldP);
//        couponListLL = (HorizontalScrollView) baseHolder.itemView.findViewById(R.id.couponListLL);
//        couponList = (LinearLayout) baseHolder.itemView.findViewById(R.id.couponList);
//        mallGoodsSales2 = (TextView) baseHolder.itemView.findViewById(R.id.mall_goods_sales2);
//        mallGoodsSales2Dis = (TextView) baseHolder.itemView.findViewById(R.id.mall_goods_sales2_dis);
//        normalTypeGroup = (Group) baseHolder.itemView.findViewById(R.id.normalTypeGroup);
//        seachTypeGroup = (Group) baseHolder.itemView.findViewById(R.id.seachTypeGroup);
//        mallService = (ImageTextView) baseHolder.itemView.findViewById(R.id.mall_service);
//        mallGoodsAddress = (ImageTextView) baseHolder.itemView.findViewById(R.id.mall_goods_address);
//        mallGoodsDistance = (TextView) baseHolder.itemView.findViewById(R.id.mall_goods_distance);
//        final ActGoodsCityItem actGoodsCityItem = getDatas().get(position);
//        if (actGoodsCityItem != null) {
//            com.healthy.library.businessutil.GlideCopy.with(context)
//                    .load(actGoodsCityItem.filePath)
//                    .placeholder(R.drawable.img_1_1_default)
//                    .error(R.drawable.img_1_1_default)
//                    .into(mallGoodsImg);
//            mallGoodsContext.setText(actGoodsCityItem.goodsTitle);
//            mallGoodsMoneyvalue.setText(FormatUtils.moneyKeep2Decimals(actGoodsCityItem.platformPrice));
//            mallGoodsMoneyvalueOrg.setText("");
//            //mallGoodsMoneyvalueOrg.setText(FormatUtils.moneyKeep2Decimals(actGoodsCityItem.retailPrice));
//            //mallGoodsMoneyvalueOrg.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
//            baseHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ARouter.getInstance()
//                            .build(ServiceRoutes.SERVICE_DETAIL)
//                            .withString("id", actGoodsCityItem.goodsId)
//                            .withString("shopId", actGoodsCityItem.shopId)
//                            .navigation();
//                }
//            });
//            mallService.setText(actGoodsCityItem.shopName);
//            mallGoodsAddress.setText(actGoodsCityItem.shopAddress);
//            String shopDistanceDes = "";
//            if (actGoodsCityItem.distance >= 1000) {
//                shopDistanceDes = String.format("%.1f", (double) actGoodsCityItem.distance / 1000) + "km";
//            } else {
//                shopDistanceDes = actGoodsCityItem.distance + "m";
//            }
//            mallGoodsDistance.setText(shopDistanceDes);
//        }
//    }
//
//    void buildCouponList(LinearLayout couponList) {
//        couponList.removeAllViews();
//    }
//
//    void initView() {
//
//    }
//
//
//    public interface OnBasketClickListener {
//        void onBasketClick(View view);
//    }
//}
