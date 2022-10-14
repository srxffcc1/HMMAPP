package com.health.second.adapter;

import android.graphics.Paint;
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
import com.health.second.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.model.GoodsDetail;
import com.healthy.library.model.MainBlockDetailModel;
import com.healthy.library.routes.SecondRoutes;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.ScreenUtils;
import com.healthy.library.widget.AutoClickImageView;
import com.healthy.library.widget.StaggeredGridLayoutHelperFix;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

public class SecondBlockGoodsAdapter extends BaseAdapter<MainBlockDetailModel> {
    public Map<String, String> imageScalemap = new HashMap<>();

    @Override
    public int getItemViewType(int position) {
        return 29;
    }

    public SecondBlockGoodsAdapter() {
        this(R.layout.item_mall_goods_city);
    }

    private SecondBlockGoodsAdapter(int viewId) {
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
    public void onBindViewHolder(@NonNull BaseHolder holder, final int position) {
        final ImageView goodsImg = holder.getView(R.id.mall_goods_img);
        TextView goodsTitle = holder.getView(R.id.mall_goods_context);
        TextView moneyValue = holder.getView(R.id.mall_goods_moneyvalue);
        TextView moneyOrg = holder.getView(R.id.mall_goods_moneyvalue_org);
        moneyOrg.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        TextView mGoodsStore = holder.getView(R.id.mall_goods_store);

        AutoClickImageView passbasket = holder.getView(R.id.passbasket);
        final MainBlockDetailModel mainBlockDetailModel = getDatas().get(position);
        if (mainBlockDetailModel.goodsDTO == null) {
            return;
        }
        final GoodsDetail goodsDetail = mainBlockDetailModel.goodsDTO;
        if (!TextUtils.isEmpty(imageScalemap.get(goodsDetail.getFilePath()))) {
            String value = imageScalemap.get(goodsDetail.getFilePath());
            int height = Integer.parseInt(value.split(":")[0]);
            int swidth = Integer.parseInt(value.split(":")[1]);
            ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) goodsImg.getLayoutParams();
            layoutParams.height = height;
            layoutParams.width = swidth;
            goodsImg.setLayoutParams(layoutParams);
            //System.out.println("直接设置大小" + position);
            com.healthy.library.businessutil.GlideCopy.with(context).load(goodsDetail.getFilePath())
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_1_1_default)

                    .into(goodsImg);
        } else {
            com.healthy.library.businessutil.GlideCopy.with(context).load(goodsDetail.getFilePath())
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_1_1_default)

                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            int swidth = ScreenUtils.getScreenWidth(context) / 2 - 12;
                            int height = (int) ((resource.getIntrinsicHeight() * 1.0 / resource.getIntrinsicWidth()) * swidth);

                            ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) goodsImg.getLayoutParams();
                            layoutParams.height = height;
                            layoutParams.width = swidth;
                            imageScalemap.put(goodsDetail.getFilePath(), height + ":" + swidth);
                            goodsImg.setLayoutParams(layoutParams);
                            com.healthy.library.businessutil.GlideCopy.with(context).load(resource).into(goodsImg);
                        }
                    });
        }

        goodsTitle.setText(goodsDetail.goodsTitle);
        moneyValue.setText(FormatUtils.moneyKeep2Decimals(goodsDetail.platformPrice));
        moneyOrg.setText("¥" + FormatUtils.moneyKeep2Decimals(goodsDetail.retailPrice));
        mGoodsStore.setText(goodsDetail.getMerchantName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map nokmap = new HashMap<String, String>();
                nokmap.put("soure", "专区列表页商品图片/标题的点击量");
                MobclickAgent.onEvent(context, "event2APPSpecialGoodsClick", nokmap);
                if (moutClickListener != null) {

                    if (goodsDetail.differentSymbol == 1||"0".equals(goodsDetail.getShopId())) {
                        ARouter.getInstance()
                                .build(SecondRoutes.SECOND_SERVICE_DETAIL)
                                .withString("id", goodsDetail.goodsId)
                                .withString("marketingType", goodsDetail.marketingType)
                                .navigation();
                    } else {
                        ARouter.getInstance()
                                .build(ServiceRoutes.SERVICE_DETAIL)
                                .withString("id", goodsDetail.goodsId)
                                .withString("marketingType", goodsDetail.marketingType)
                                .navigation();
                    }
//                    moutClickListener.outClick("goods", goodsDetail);
                }
            }
        });
        /*TextView tagText = holder.getView(R.id.tagText);
        tagText.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(goodsDetail.getTagFirst())) {
            tagText.setVisibility(View.VISIBLE);
            if (goodsDetail.getTagFirst().length() > 3) {
                String org = goodsDetail.getTagFirst();
                String resultOrg = org.substring(0, 2) + "\n" + org.substring(2, org.length());
                tagText.setBackgroundResource(R.drawable.shape_mall_goods_ol_tagbgbig);
                tagText.setText(resultOrg);
            } else {
                tagText.setText(goodsDetail.getTagFirst());
                tagText.setBackgroundResource(R.drawable.shape_mall_goods_ol_tagbgsmall);
            }
        }*/
        passbasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map nokmap = new HashMap<String, String>();
                nokmap.put("soure", "专区加入购物车点击量");
                MobclickAgent.onEvent(context, "event2APPSpecialShopCartClick", nokmap);
                /*if (onBasketClickListener != null) {
                    onBasketClickListener.onBasketClick(v);
                }*/
                if (moutClickListener != null) {
                    moutClickListener.outClick("basket", goodsDetail);
                }
            }
        });
    }
}
