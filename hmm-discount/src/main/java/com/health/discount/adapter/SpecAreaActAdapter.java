package com.health.discount.adapter;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.health.discount.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.GoodsDetail;
import com.healthy.library.model.MainBlockDetailModel;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.ScreenUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.AutoClickImageView;
import com.healthy.library.widget.StaggeredGridLayoutHelperFix;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

public class SpecAreaActAdapter extends BaseAdapter<MainBlockDetailModel> {

    public SpecAreaActAdapter(int viewId) {
        super(viewId);
    }

    public SpecAreaActAdapter() {
        this(R.layout.item_specarea_adapter_layout);
    }

    public Map<String, String> imageMap = new HashMap<>();

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        StaggeredGridLayoutHelperFix helper = new StaggeredGridLayoutHelperFix();
        helper.setLane(2);
        helper.setHGap(3);
        return helper;
    }

    public void setOnBasketClickListener(OnBasketClickListener onBasketClickListener) {
        this.onBasketClickListener = onBasketClickListener;
    }

    OnBasketClickListener onBasketClickListener;

    public interface OnBasketClickListener {
        void onBasketClick(View view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, final int position) {
        final ImageView goodsImg = holder.getView(R.id.goodsImg);
        TextView goodsTitle = holder.getView(R.id.goodsTitle);
        TextView moneyValue = holder.getView(R.id.moneyValue);
        AutoClickImageView passbasket_goods4 = holder.getView(R.id.passbasket_goods4);
        final MainBlockDetailModel mainBlockDetailModel = getDatas().get(position);
        if (mainBlockDetailModel.goodsDTO == null) {
            return;
        }
        final GoodsDetail goodsDetail = mainBlockDetailModel.goodsDTO;
        if (!TextUtils.isEmpty(imageMap.get(mainBlockDetailModel.getGoodsImgUrl()))) {
            String value = imageMap.get(mainBlockDetailModel.getGoodsImgUrl());
            int height = Integer.parseInt(value.split(":")[0]);
            int swidth = Integer.parseInt(value.split(":")[1]);
            ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) goodsImg.getLayoutParams();
            layoutParams.height = height;
            layoutParams.width = swidth;
            goodsImg.setLayoutParams(layoutParams);
            com.healthy.library.businessutil.GlideCopy.with(context).load(mainBlockDetailModel.getGoodsImgUrl())
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_1_1_default)

                    .into(goodsImg);
        } else {
            com.healthy.library.businessutil.GlideCopy.with(context).load(mainBlockDetailModel.getGoodsImgUrl())
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_1_1_default)

                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            int swidth = (int)((ScreenUtils.getScreenWidth(context)- TransformUtil.dp2px(context,(5+3+6+3+5)))/2);
                            int height = (int) ((resource.getIntrinsicHeight() * 1.0 / resource.getIntrinsicWidth()) * swidth);
                            ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) goodsImg.getLayoutParams();
                            layoutParams.height = height;
                            layoutParams.width = swidth;
                            imageMap.put(mainBlockDetailModel.getGoodsImgUrl(), height + ":" + swidth);
                            goodsImg.setLayoutParams(layoutParams);
                            com.healthy.library.businessutil.GlideCopy.with(context).load(resource).into(goodsImg);
                        }
                    });
        }
        goodsTitle.setText(goodsDetail.goodsTitle);
        moneyValue.setText(FormatUtils.moneyKeep2Decimals(goodsDetail.platformPrice));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map nokmap = new HashMap<String, String>();
                nokmap.put("soure", "专区列表页商品图片/标题的点击量");
                MobclickAgent.onEvent(context, "event2APPSpecialGoodsClick", nokmap);
                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_DETAIL)
                        .withString("id", goodsDetail.id + "")
                        .withString("shopId", SpUtils.getValue(context, SpKey.CHOSE_SHOP))
                        .navigation();
            }
        });
        TextView tagText=holder.getView(R.id.tagText);
        tagText.setVisibility(View.GONE);
        if(!TextUtils.isEmpty(goodsDetail.getTagFirst())){
            tagText.setVisibility(View.VISIBLE);
            if(goodsDetail.getTagFirst().length()>3){
                String org=goodsDetail.getTagFirst();
                String resultOrg=org.substring(0,2)+"\n"+org.substring(2,org.length());
                tagText.setBackgroundResource(R.drawable.shape_mall_goods_ol_tagbgbig);
                tagText.setText(resultOrg);
            }else {
                tagText.setText(goodsDetail.getTagFirst());
                tagText.setBackgroundResource(R.drawable.shape_mall_goods_ol_tagbgsmall);
            }
        }
        passbasket_goods4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map nokmap = new HashMap<String, String>();
                nokmap.put("soure", "专区加入购物车点击量");
                MobclickAgent.onEvent(context, "event2APPSpecialShopCartClick", nokmap);
                if (onBasketClickListener != null) {
                    onBasketClickListener.onBasketClick(v);
                }
                if (moutClickListener != null) {
                    moutClickListener.outClick("basket", goodsDetail);
                }
            }
        });
    }

}
