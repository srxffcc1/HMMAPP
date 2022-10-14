package com.health.servicecenter.adapter;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.amap.api.maps.model.LatLng;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.health.servicecenter.R;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.base.BaseMultiItemAdapter;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.RecommendList;
import com.healthy.library.routes.MallRoutes;
import com.healthy.library.routes.SecondRoutes;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.utils.ParseUtils;
import com.healthy.library.utils.ScreenUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.ImageTextView;
import com.healthy.library.widget.RatingView;
import com.healthy.library.widget.StaggeredGridLayoutHelperFix;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

public class MallGoodsItemAdapter extends BaseMultiItemAdapter<RecommendList> {
    private LatLng latLng;

    public String key;

    public Map<String, String> imageScalemap = new HashMap<>();

    public void setKey(String key) {
        this.key = key;
    }

    public void setOnBasketClickListener(OnBasketClickListener onBasketClickListener) {
        this.onBasketClickListener = onBasketClickListener;
    }

    OnBasketClickListener onBasketClickListener;

    @Override
    public int getItemViewType(int position) {
        return getDefItemViewType(position);
    }

    public MallGoodsItemAdapter() {
        this(R.layout.item_mall_goods_service);
        addItemType(1, R.layout.item_mall_goods_service);
        addItemType(2, R.layout.item_mall_goods);
        addItemType(3, R.layout.item_mall_goods_store);
    }

    private MallGoodsItemAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        StaggeredGridLayoutHelperFix helper = new StaggeredGridLayoutHelperFix();
//        helper.setMargin(6, 0, 6, 0);
        helper.setLane(2);
        helper.setHGap(3);
        return helper;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, final int position) {

        if (getDefItemViewType(position) == 1) {

            LinearLayout mall_service_liner = baseHolder.getView(R.id.mall_service_liner);
            if (getDatas().get(position).list == null || getDatas().get(position).list.size() == 0) {
                return;
            }
            mall_service_liner.removeAllViews();
            for (int i = 0; i < getDatas().get(position).list.size(); i++) {
                final RecommendList.ListBean listBean = getDatas().get(position).list.get(i);
                ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout
                        .LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 10, 0, 0);
                LayoutInflater inflater = LayoutInflater.from(context);
                View view = inflater.inflate(R.layout.item_mall_goods_service_banneritem2, null);
                TextView mall_service_title = view.findViewById(R.id.mall_service_title);
                TextView mall_service_money = view.findViewById(R.id.mall_service_money);
                TextView discountTip = view.findViewById(R.id.discountTip);
                CornerImageView mall_service_img = view.findViewById(R.id.mall_service_img);
                mall_service_title.setText(FormatUtils.moneyKeep2Decimals(getDatas().get(position).list.get(i).goodsTitle));
                mall_service_money.setText("¥" + FormatUtils.moneyKeep2Decimals(getDatas().get(position).list.get(i).platformPrice) + "");
                com.healthy.library.businessutil.GlideCopy.with(context).load(getDatas().get(position).list.get(i).filePath)
                        .placeholder(R.drawable.img_1_1_default2)
                        .error(R.drawable.img_1_1_default)

                        .into(mall_service_img);
                view.setLayoutParams(layoutParams);
                mall_service_liner.addView(view);
                discountTip.setVisibility(View.GONE);
                double discount = 1;
                discount = Double.parseDouble(getDatas().get(position).list.get(i).platformPrice) / Double.parseDouble(getDatas().get(position).list.get(i).retailPrice);
                if (discount < 1) {
                    discountTip.setVisibility(View.VISIBLE);
                    discountTip.setText(String.format("%.1f", discount * 10) + "折");
                }
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ("搜索".equals(key)) {
                            Map nokmap = new HashMap<String, String>();
                            nokmap.put("soure", "搜索结果列表中“精选服务”");
                            MobclickAgent.onEvent(getContext(), "event2GoodsDetailClick", nokmap);
                        } else {
                            Map nokmap = new HashMap<String, String>();
                            nokmap.put("soure", "母婴商品“为您精选”中“精选服务”");
                            MobclickAgent.onEvent(getContext(), "event2GoodsDetailClick", nokmap);
                        }
                        ARouter.getInstance()
                                .build(ServiceRoutes.SERVICE_DETAIL)
                                .withString("shopId", listBean.shopId)
                                .withString("goodsId", listBean.goodsId + "")
                                .withString("categoryNo", listBean.categoryNo + "")
                                .withString("bargainId", listBean.bargainId)
                                .withString("assembleId", listBean.assembleId)
                                .withString("cityNo", LocUtil.getCityNo(context, SpKey.LOC_CHOSE) + "")
                                .withString("lng", LocUtil.getLongitude(context, SpKey.LOC_CHOSE) + "")
                                .withString("lat", LocUtil.getLatitude(context, SpKey.LOC_CHOSE) + "")
                                .navigation();
                    }
                });
            }

        } else if (getDefItemViewType(position) == 2) {

            final ImageView mall_goods_img = baseHolder.getView(R.id.mall_goods_img);
            ImageView passbasket = baseHolder.getView(R.id.passbasket);
            TextView mall_goods_context = baseHolder.getView(R.id.mall_goods_context);
            TextView mall_goods_moneyvalue = baseHolder.getView(R.id.mall_goods_moneyvalue);
            TextView mall_goods_space = baseHolder.getView(R.id.mall_goods_space);
            TextView mall_goods_actvip = baseHolder.getView(R.id.mall_goods_actvip);

            View spinnerImg = baseHolder.getView(R.id.spinnerImg);
            if (getDatas().get(position).actVip != null) {
                if (getDatas().get(position).actVip.PopInfo != null && getDatas().get(position).actVip.PopInfo.size() > 0) {
                    mall_goods_actvip.setVisibility(View.VISIBLE);
                    mall_goods_actvip.setText(getDatas().get(position).actVip.PopInfo.get(0).PopDesc);
                } else {
                    mall_goods_actvip.setVisibility(View.GONE);
                }
            } else {
                mall_goods_actvip.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(imageScalemap.get(getDatas().get(position).filePath))) {
                String value = imageScalemap.get(getDatas().get(position).filePath);
                int height = Integer.parseInt(value.split(":")[0]);
                int swidth = Integer.parseInt(value.split(":")[1]);
                ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) mall_goods_img.getLayoutParams();
                layoutParams.height = height;
                layoutParams.width = swidth;
                mall_goods_img.setLayoutParams(layoutParams);
                ////System.out.println("直接设置大小" + position);
                com.healthy.library.businessutil.GlideCopy.with(context).load(getDatas().get(position).filePath)
                        .placeholder(R.drawable.img_1_1_default2)
                        .error(R.drawable.img_1_1_default)

                        .into(mall_goods_img);
            } else {
                com.healthy.library.businessutil.GlideCopy.with(context).load(getDatas().get(position).filePath)
                        .placeholder(R.drawable.img_1_1_default2)
                        .error(R.drawable.img_1_1_default)

                        .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                int swidth = (int)(ScreenUtils.getScreenWidth(context) / 2 - TransformUtil.dp2px(context,6));
                                int height = (int) ((resource.getIntrinsicHeight() * 1.0 / resource.getIntrinsicWidth()) * swidth);
                                ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) mall_goods_img.getLayoutParams();
                                layoutParams.height = height;
                                layoutParams.width = swidth;
                                try {
                                    imageScalemap.put(getDatas().get(position).filePath, height + ":" + swidth);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                mall_goods_img.setLayoutParams(layoutParams);
                                com.healthy.library.businessutil.GlideCopy.with(context).load(resource).into(mall_goods_img);
                            }
                        });
            }
            TextView tagText = baseHolder.getView(R.id.tagText);
            tagText.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(getDatas().get(position).getTagFirst())) {
                tagText.setVisibility(View.VISIBLE);
                if (getDatas().get(position).getTagFirst().length() > 3) {
                    String org = getDatas().get(position).getTagFirst();
                    String resultOrg = org.substring(0, 2) + "\n" + org.substring(2, org.length());
                    tagText.setBackgroundResource(R.drawable.shape_mall_goods_ol_tagbgbig);
                    tagText.setText(resultOrg);
                } else {
                    tagText.setText(getDatas().get(position).getTagFirst());
                    tagText.setBackgroundResource(R.drawable.shape_mall_goods_ol_tagbgsmall);
                }
            }
            mall_goods_context.setText(getDatas().get(position).goodsTitle);
            spinnerImg.setVisibility(View.GONE);

            View tv_video_flag = baseHolder.getView(R.id.tv_video_flag);
            tv_video_flag.setVisibility(View.GONE);
            if (getDatas().get(position).courseFlag == 2) {
                tv_video_flag.setVisibility(View.VISIBLE);
            }
            SpannableString priceStr = new SpannableString("" + getDatas().get(position).platformPrice);
            priceStr.setSpan(new AbsoluteSizeSpan(13, true), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            priceStr.setSpan(new StyleSpan(Typeface.NORMAL), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //正常
            mall_goods_moneyvalue.setText(FormatUtils.moneyKeep2Decimals(getDatas().get(position).platformPrice));
            mall_goods_space.setText(getDatas().get(position).skuName);
            if (!TextUtils.isEmpty(getDatas().get(position).skuName)) {
                mall_goods_space.setVisibility(View.VISIBLE);
                spinnerImg.setVisibility(View.VISIBLE);
            } else {
                mall_goods_space.setVisibility(View.GONE);
            }
            passbasket.setVisibility(View.VISIBLE);
            passbasket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("精挑细选".equals(key)) {
                        Map nokmap = new HashMap<String, String>();
                        nokmap.put("soure", "支付成功页“精挑细选”专题");
                        MobclickAgent.onEvent(context, "eps_APP_MaternalandChildGoods_PaymentMethod_CarefullyChosen", nokmap);
                    }
                    if ("购物车帮你挑".equals(key)) {
                        Map nokmap = new HashMap<String, String>();
                        nokmap.put("soure", "母婴商品页面下方“为您精选”专题");
                        MobclickAgent.onEvent(context, "eps_APP_MaternalandChildGoods_ShoppingCart_SelectedForYou", nokmap);
                    }
                    if ("商品已下架".equals(key)) {
                        Map nokmap = new HashMap<String, String>();
                        nokmap.put("soure", "商品已下架页“相关推荐”专题");
                        MobclickAgent.onEvent(context, "eps_APP_MaternalandChildGoods_ShoppingCart_RelevantRecommendations", nokmap);
                    }
                    Map nokmap = new HashMap<String, String>();
                    nokmap.put("soure", "推荐位商品购物车图标点击量");
                    MobclickAgent.onEvent(context, "event2APPGoodsDetialRecommendCartClick", nokmap);
                    if (moutClickListener != null) {
                        if (onBasketClickListener != null) {
                            onBasketClickListener.onBasketClick(v);
                        }
                        moutClickListener.outClick("addShopCat", getDatas().get(position));
                    }
                }
            });
            baseHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("商品已下架".equals(key)) {
                        Map nokmap = new HashMap<String, String>();
                        nokmap.put("soure", "商品已下架页“相关推荐”点击商品栏");
                        MobclickAgent.onEvent(context, "btn_APP__MaternalandChildGoods_CommodityDetails", nokmap);
                    }
                    if ("购物车帮你挑".equals(key)) {
                        Map nokmap = new HashMap<String, String>();
                        nokmap.put("soure", "购物车下方”购物车帮你挑“专题点击商品栏");
                        MobclickAgent.onEvent(context, "btn_APP__MaternalandChildGoods_CommodityDetails", nokmap);
                    }
                    if ("精挑细选".equals(key)) {
                        Map nokmap = new HashMap<String, String>();
                        nokmap.put("soure", "支付成功页面下方”精挑细选“专题点击商品栏");
                        MobclickAgent.onEvent(context, "btn_APP__MaternalandChildGoods_CommodityDetails", nokmap);
                    }
                    Map nokmap = new HashMap<String, String>();
                    nokmap.put("soure", "母婴商品页“为您精选\"栏商品");
                    MobclickAgent.onEvent(context, "btn_APP__MaternalandChildGoods_CommodityDetails", nokmap);

                    Map nokmap1 = new HashMap<String, String>();
                    nokmap.put("soure", "推荐位商品图片点击量");
                    MobclickAgent.onEvent(context, "event2APPGoodsDetialRecommendImgClick", nokmap1);
                    ARouter.getInstance()
                            .build(ServiceRoutes.SERVICE_DETAIL)
                            .withString("id", getDatas().get(position).goodsId + "")
                            .withString("shopId", SpUtils.getValue(context, SpKey.CHOSE_SHOP))
                            .withString("bargainId", getDatas().get(position).bargainId)
                            .withString("assembleId", getDatas().get(position).assembleId)
                            .navigation();
                }
            });
        } else {
            latLng = new LatLng(Double.parseDouble(LocUtil.getLatitude(context, SpKey.LOC_CHOSE)),
                    Double.parseDouble(LocUtil.getLongitude(context, SpKey.LOC_CHOSE)));//当前定位经纬度
            CornerImageView mall_goods_img = baseHolder.getView(R.id.mall_goods_img);
            TextView mall_goods_name = baseHolder.getView(R.id.mall_goods_name);
            TextView mall_goods_distance = baseHolder.getView(R.id.mall_goods_distance);
            TextView mall_goods_num = baseHolder.getView(R.id.mall_goods_num);
            RatingView rating_customer_score = baseHolder.getView(R.id.rating_customer_score);
            ImageTextView mall_goods_address = baseHolder.getView(R.id.mall_goods_address);
            com.healthy.library.businessutil.GlideCopy.with(context).load(getDatas().get(position).filePath)
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_1_1_default)

                    .into(mall_goods_img);
            mall_goods_name.setText(getDatas().get(position).shopName);
            rating_customer_score.setRating(getDatas().get(position).score);
            mall_goods_address.setText(getDatas().get(position).shopAddress);
            mall_goods_num.setText(getDatas().get(position).score + "");
            mall_goods_distance.setText(ParseUtils.parseDistance(getDatas().get(position).distance + ""));
            baseHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("搜索".equals(key)) {
                        Map nokmap = new HashMap<String, String>();
                        nokmap.put("soure", "母婴商品“为您精选”中“优惠商家”");
                        MobclickAgent.onEvent(getContext(), "event2PhoneStoreClick", nokmap);
                    } else {
                        Map nokmap = new HashMap<String, String>();
                        nokmap.put("soure", "搜索结果列表中“优惠商家”");
                        MobclickAgent.onEvent(getContext(), "event2PhoneStoreClick", nokmap);
                    }
                    if (TextUtils.isEmpty(getDatas().get(position).categoryNo)) {
                        ARouter.getInstance()
                                .build(SecondRoutes.SECOND_SHOP_DETAIL)
                                .withString("shopId", getDatas().get(position).shopName)
                                .withString("categoryNo", getDatas().get(position).categoryNos + "")
                                .withString("cityNo", LocUtil.getCityNo(context, SpKey.LOC_CHOSE) + "")
                                .withString("lng", LocUtil.getLongitude(context, SpKey.LOC_CHOSE) + "")
                                .withString("lat", LocUtil.getLatitude(context, SpKey.LOC_CHOSE) + "")
                                .navigation();
                    } else {
                        ARouter.getInstance()
                                .build(SecondRoutes.SECOND_SHOP_DETAIL)
                                .withString("shopId", getDatas().get(position).shopId)
                                .withString("categoryNo", getDatas().get(position).categoryNos + "")
                                .withString("cityNo", LocUtil.getCityNo(context, SpKey.LOC_CHOSE) + "")
                                .withString("lng", LocUtil.getLongitude(context, SpKey.LOC_CHOSE) + "")
                                .withString("lat", LocUtil.getLatitude(context, SpKey.LOC_CHOSE) + "")
                                .navigation();
                    }
                }
            });
        }
    }

    public interface OnBasketClickListener {
        void onBasketClick(View view);
    }
}
