package com.health.discount.adapter;

import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.health.discount.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.builder.ObjectIteraor;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.ActGoodsItem;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.ScreenUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.AutoClickImageView;
import com.healthy.library.widget.ImageTextView;
import com.healthy.library.widget.StaggeredGridLayoutHelperFix;

import java.util.HashMap;
import java.util.Map;

public class ActZTabViewPagerAdapterS1 extends BaseAdapter<MultiItemEntity> {


    public Map<String, String> imageScalemap = new HashMap<>();
    @Override
    public int getItemViewType(int position) {
        return 24;
    }
    public ActZTabViewPagerAdapterS1() {
        this(R.layout.item_mall_goods);

    }
    private ActZTabViewPagerAdapterS1(int viewId) {
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
        ConstraintLayout tvVideoFlag;
        ImageView tvVideoFlagC;
        TextView tvVideoFlagB;
        TextView numberTmp;
        ImageTextView mallOnlineTip;
        ConstraintLayout mallGoodsImgUnder;
        TextView mallGoodsContext;
        TextView mallGoodsSpace;
        ImageView spinnerImg;
        TextView mallGoodsMoneyflag;
        TextView mallGoodsMoneyvalue;
        TextView mallGoodsMoneyvalueOrg;
        TextView mallGoodsSales;
        AutoClickImageView passbasket;
        HorizontalScrollView couponListLL;
        LinearLayout couponList;
        TextView mallGoodsSales2;
        TextView mallGoodsSales2Dis;
        Group normalTypeGroup;
        Group seachTypeGroup;

        mallGoodsImg = (ImageView) baseHolder.itemView.findViewById(R.id.mall_goods_img);
        tvVideoFlag = (ConstraintLayout) baseHolder.itemView.findViewById(R.id.tv_video_flag);
        tvVideoFlagC = (ImageView) baseHolder.itemView.findViewById(R.id.tv_video_flag_c);
        tvVideoFlagB = (TextView) baseHolder.itemView.findViewById(R.id.tv_video_flag_b);
        numberTmp = (TextView) baseHolder.itemView.findViewById(R.id.numberTmp);
        mallOnlineTip = (ImageTextView) baseHolder.itemView.findViewById(R.id.mall_online_tip);
        mallGoodsImgUnder = (ConstraintLayout) baseHolder.itemView.findViewById(R.id.mall_goods_img_under);
        mallGoodsContext = (TextView) baseHolder.itemView.findViewById(R.id.mall_goods_context);
        mallGoodsSpace = (TextView) baseHolder.itemView.findViewById(R.id.mall_goods_space);
        spinnerImg = (ImageView) baseHolder.itemView.findViewById(R.id.spinnerImg);
        mallGoodsMoneyflag = (TextView) baseHolder.itemView.findViewById(R.id.mall_goods_moneyflag);
        mallGoodsMoneyvalue = (TextView) baseHolder.itemView.findViewById(R.id.mall_goods_moneyvalue);
        mallGoodsMoneyvalueOrg = (TextView) baseHolder.itemView.findViewById(R.id.mall_goods_moneyvalue_org);
        mallGoodsSales = (TextView) baseHolder.itemView.findViewById(R.id.mall_goods_sales);
        passbasket = (AutoClickImageView) baseHolder.itemView.findViewById(R.id.passbasket);
        couponListLL = (HorizontalScrollView) baseHolder.itemView.findViewById(R.id.couponListLL);
        couponList = (LinearLayout) baseHolder.itemView.findViewById(R.id.couponList);
        mallGoodsSales2 = (TextView) baseHolder.itemView.findViewById(R.id.mall_goods_sales2);
        mallGoodsSales2Dis = (TextView) baseHolder.itemView.findViewById(R.id.mall_goods_sales2_dis);
        normalTypeGroup = (Group) baseHolder.itemView.findViewById(R.id.normalTypeGroup);
        seachTypeGroup = (Group) baseHolder.itemView.findViewById(R.id.seachTypeGroup);
        final ActGoodsItem actGoodsItem = (ActGoodsItem) getDatas().get(position);


        if (!TextUtils.isEmpty(imageScalemap.get(actGoodsItem.filePath))) {
            String value = imageScalemap.get(actGoodsItem.filePath);
            int height = Integer.parseInt(value.split(":")[0]);
            int swidth = Integer.parseInt(value.split(":")[1]);
            ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) mallGoodsImg.getLayoutParams();
            layoutParams.height = height;
            layoutParams.width = swidth;
            mallGoodsImg.setLayoutParams(layoutParams);
            //System.out.println("直接设置大小" + position);
            com.healthy.library.businessutil.GlideCopy.with(context).load(actGoodsItem.filePath)
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_1_1_default)
                    
                    .into(mallGoodsImg);
        } else {
            com.healthy.library.businessutil.GlideCopy.with(context).load(actGoodsItem.filePath)
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
                            imageScalemap.put(actGoodsItem.filePath, height + ":" + swidth);
                            mallGoodsImg.setLayoutParams(layoutParams);
                            com.healthy.library.businessutil.GlideCopy.with(context).load(resource).into(mallGoodsImg);
                        }
                    });
        }
        TextView tagText=baseHolder.getView(R.id.tagText);
        tagText.setVisibility(View.GONE);
        if(!TextUtils.isEmpty(actGoodsItem.getTagFirst())){
            tagText.setVisibility(View.VISIBLE);
            if(actGoodsItem.getTagFirst().length()>3){
                String org=actGoodsItem.getTagFirst();
                String resultOrg=org.substring(0,2)+"\n"+org.substring(2,org.length());
                tagText.setBackgroundResource(R.drawable.shape_mall_goods_ol_tagbgbig);
                tagText.setText(resultOrg);
            }else {
                tagText.setText(actGoodsItem.getTagFirst());
                tagText.setBackgroundResource(R.drawable.shape_mall_goods_ol_tagbgsmall);
            }
        }
        mallGoodsContext.setText(actGoodsItem.goodsTitle);
        mallGoodsMoneyvalue.setText(FormatUtils.moneyKeep2Decimals(actGoodsItem.platformPrice));
        mallGoodsMoneyvalueOrg.setText("¥" + FormatUtils.moneyKeep2Decimals(actGoodsItem.retailPrice));
        mallGoodsMoneyvalueOrg.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        buildCouponList(couponList);
        tvVideoFlag.setVisibility(View.GONE);
        if ("2".equals(actGoodsItem.courseFlag)) {
            tvVideoFlag.setVisibility(View.VISIBLE);
        }
        baseHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_DETAIL)
                        .withString("id", actGoodsItem.goodsId)
                        .withString("bargainId", actGoodsItem.bargainId)
                        .withString("assembleId", actGoodsItem.assembleId)
                        .withString("shopId", SpUtils.getValue(context, SpKey.CHOSE_SHOP))
                        .navigation();
            }
        });
    }
    private void buildCouponList(LinearLayout couponList) {
        couponList.removeAllViews();
    }

    @Nullable
    @Override
    public ObjectIteraor<MultiItemEntity> getDuplicateObjectIterator() {
        return new ObjectIteraor<MultiItemEntity>() {
            @Override
            public String getDesObj(MultiItemEntity o) {
                final ActGoodsItem actGoodsItem = (ActGoodsItem) o;
                return actGoodsItem.goodsId;
            }
        };
    }
}
