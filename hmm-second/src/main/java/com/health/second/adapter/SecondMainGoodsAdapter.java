package com.health.second.adapter;

import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
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
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.lib_ShapeView.view.ShapeView;
import com.health.second.R;
import com.healthy.library.model.SecondSortGoods;
import com.healthy.library.LibApplication;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.builder.ObjectIteraor;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.constant.SpKey;
import com.healthy.library.routes.SecondRoutes;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.ScreenUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.AutoClickImageView;
import com.healthy.library.widget.StaggeredGridLayoutHelperFix;

import java.util.HashMap;
import java.util.Map;

public class SecondMainGoodsAdapter extends BaseAdapter<SecondSortGoods> {


    public Map<String, String> imageScalemap = new HashMap<>();

    @Override
    public int getItemViewType(int position) {
        return 4;
    }

    public SecondMainGoodsAdapter() {
        this(R.layout.item_mall_goods_city);
    }

    private SecondMainGoodsAdapter(int viewId) {
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
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int position) {
         final ImageView mallGoodsImg;
         ConstraintLayout timeLL;
         LinearLayout goodsTimeLL;
         TextView kickHour;
         TextView kickMin;
         TextView kickSec;
         ConstraintLayout mallGoodsImgUnder;
         TextView mallGoodsContext;
         TextView mallGoodsKickFlag;
         TextView mallGoodsMoneyflag;
         TextView mallGoodsMoneyvalue;
         TextView mallGoodsMoneyvalueOrg;
         ShapeView svLine;
         TextView mallGoodsStore;
         AutoClickImageView passbasket;
        mallGoodsImg = (ImageView) baseHolder.itemView.findViewById(R.id.mall_goods_img);
        timeLL = (ConstraintLayout) baseHolder.itemView.findViewById(R.id.timeLL);
        goodsTimeLL = (LinearLayout)baseHolder.itemView. findViewById(R.id.goodsTimeLL);
        kickHour = (TextView) baseHolder.itemView.findViewById(R.id.kickHour);
        kickMin = (TextView)baseHolder.itemView. findViewById(R.id.kickMin);
        kickSec = (TextView)baseHolder.itemView. findViewById(R.id.kickSec);
        mallGoodsImgUnder = (ConstraintLayout)baseHolder.itemView. findViewById(R.id.mall_goods_img_under);
        mallGoodsContext = (TextView) baseHolder.itemView.findViewById(R.id.mall_goods_context);
        mallGoodsKickFlag = (TextView)baseHolder.itemView. findViewById(R.id.mall_goods_kick_flag);
        mallGoodsMoneyflag = (TextView)baseHolder.itemView. findViewById(R.id.mall_goods_moneyflag);
        mallGoodsMoneyvalue = (TextView)baseHolder.itemView. findViewById(R.id.mall_goods_moneyvalue);
        mallGoodsMoneyvalueOrg = (TextView)baseHolder.itemView. findViewById(R.id.mall_goods_moneyvalue_org);
        svLine = (ShapeView) baseHolder.itemView.findViewById(R.id.sv_line);
        mallGoodsStore = (TextView) baseHolder.itemView.findViewById(R.id.mall_goods_store);
        passbasket = (AutoClickImageView) baseHolder.itemView.findViewById(R.id.passbasket);

        final SecondSortGoods actGoodsItem = (SecondSortGoods) getDatas().get(position);

        if (!TextUtils.isEmpty(imageScalemap.get(actGoodsItem.getFilePath()))) {
            String value = imageScalemap.get(actGoodsItem.getFilePath());
            int height = Integer.parseInt(value.split(":")[0]);
            int swidth = Integer.parseInt(value.split(":")[1]);
            ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) mallGoodsImg.getLayoutParams();
            layoutParams.height = height;
            layoutParams.width = swidth;
            mallGoodsImg.setLayoutParams(layoutParams);
            //System.out.println("直接设置大小" + position);
            com.healthy.library.businessutil.GlideCopy.with(context).load(actGoodsItem.getFilePath())
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_1_1_default)

                    .into(mallGoodsImg);
        } else {
            com.healthy.library.businessutil.GlideCopy.with(context).load(actGoodsItem.getFilePath())
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_1_1_default)

                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            int swidth = ScreenUtils.getScreenWidth(context) / 2 - 12;
                            int height = (int) ((resource.getIntrinsicHeight() * 1.0 / resource.getIntrinsicWidth()) * swidth);

                            ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) mallGoodsImg.getLayoutParams();
                            layoutParams.height = height;
                            layoutParams.width = swidth;
                            imageScalemap.put(actGoodsItem.getFilePath(), height + ":" + swidth);
                            mallGoodsImg.setLayoutParams(layoutParams);
                            com.healthy.library.businessutil.GlideCopy.with(context).load(resource).into(mallGoodsImg);
                        }
                    });
        }
        passbasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(moutClickListener!=null){
                    try {
                        moutClickListener.outClick("加入购物车",actGoodsItem);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        mallGoodsStore.setText(actGoodsItem.getMerchantName());
        mallGoodsContext.setText(actGoodsItem.goodsTitle);
        mallGoodsMoneyvalue.setText(FormatUtils.moneyKeep2Decimals(actGoodsItem.platformPrice));
        mallGoodsMoneyvalueOrg.setText("¥" + FormatUtils.moneyKeep2Decimals(actGoodsItem.retailPrice));
        mallGoodsMoneyvalueOrg.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        baseHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(actGoodsItem.userId.equals(LocUtil.getUserId())&&!TextUtils.isEmpty(LocUtil.getUserId())){
                    ARouter.getInstance()
                            .build(ServiceRoutes.SERVICE_DETAIL)
                            .withString("id", actGoodsItem.id)
                            .withString("merchantId", SpUtils.getValue(LibApplication.getAppContext(), SpKey.CHOSE_MC))
                            .navigation();
                }else {
                    ARouter.getInstance()
                            .build(SecondRoutes.SECOND_SERVICE_DETAIL)
                            .withString("id", actGoodsItem.id)
                            .withString("merchantId", SpUtils.getValue(LibApplication.getAppContext(), SpKey.CHOSE_MC))
                            .navigation();
                }



            }
        });


    }
    @Nullable
    @Override
    public ObjectIteraor<SecondSortGoods> getDuplicateObjectIterator() {
        return new ObjectIteraor<SecondSortGoods>() {
            @Override
            public String getDesObj(SecondSortGoods o) {
                final SecondSortGoods actGoodsItem = (SecondSortGoods) o;
                return actGoodsItem.id;
            }
        };
    }
}
