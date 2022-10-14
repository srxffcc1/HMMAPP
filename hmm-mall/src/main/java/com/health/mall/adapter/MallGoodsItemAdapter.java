package com.health.mall.adapter;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.amap.api.maps.model.LatLng;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.health.mall.R;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.base.BaseMultiItemAdapter;
import com.healthy.library.model.RecommendList;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.ScreenUtils;
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
        addItemType(2, R.layout.item_mall_goods);
    }

    private MallGoodsItemAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        StaggeredGridLayoutHelperFix helper = new StaggeredGridLayoutHelperFix();
        helper.setMargin(6, 0, 6, 0);
        helper.setLane(2);
        helper.setHGap(3);
        return helper;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, final int position) {
        if (getDefItemViewType(position) == 2) {

            final ImageView mall_goods_img = baseHolder.getView(R.id.mall_goods_img);
            ImageView passbasket = baseHolder.getView(R.id.passbasket);
            TextView mall_goods_context = baseHolder.getView(R.id.mall_goods_context);
            TextView mall_goods_moneyvalue = baseHolder.getView(R.id.mall_goods_moneyvalue);
            TextView mall_goods_space = baseHolder.getView(R.id.mall_goods_space);

            View spinnerImg = baseHolder.getView(R.id.spinnerImg);
            if (!TextUtils.isEmpty(imageScalemap.get(getDatas().get(position).filePath))) {
                String value = imageScalemap.get(getDatas().get(position).filePath);
                int height = Integer.parseInt(value.split(":")[0]);
                int swidth = Integer.parseInt(value.split(":")[1]);
                ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) mall_goods_img.getLayoutParams();
                layoutParams.height = height;
                layoutParams.width = swidth;
                mall_goods_img.setLayoutParams(layoutParams);
                //System.out.println("直接设置大小" + position);
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
                                int swidth = ScreenUtils.getScreenWidth(context) / 2 - 12;
                                int height = (int) ((resource.getIntrinsicHeight() * 1.0 / resource.getIntrinsicWidth()) * swidth);

                                ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) mall_goods_img.getLayoutParams();
                                layoutParams.height = height;
                                layoutParams.width = swidth;
                                imageScalemap.put(getDatas().get(position).filePath, height + ":" + swidth);
                                mall_goods_img.setLayoutParams(layoutParams);
                                com.healthy.library.businessutil.GlideCopy.with(context).load(resource).into(mall_goods_img);
                            }
                        });
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

                    ARouter.getInstance()
                            .build(ServiceRoutes.SERVICE_DETAIL)
                            .withString("id", getDatas().get(position).goodsId + "")
                            .navigation();
                }
            });
        }
        TextView tagText=baseHolder.getView(R.id.tagText);
        tagText.setVisibility(View.GONE);
        if(!TextUtils.isEmpty(getDatas().get(position).getTagFirst())){
            tagText.setVisibility(View.VISIBLE);
            if(getDatas().get(position).getTagFirst().length()>3){
                String org=getDatas().get(position).getTagFirst();
                String resultOrg=org.substring(0,2)+"\n"+org.substring(2,org.length());
                tagText.setBackgroundResource(R.drawable.shape_mall_goods_ol_tagbgbig);
                tagText.setText(resultOrg);
            }else {
                tagText.setText(getDatas().get(position).getTagFirst());
                tagText.setBackgroundResource(R.drawable.shape_mall_goods_ol_tagbgsmall);
            }
        }
    }

    public interface OnBasketClickListener {
        void onBasketClick(View view);
    }
}
