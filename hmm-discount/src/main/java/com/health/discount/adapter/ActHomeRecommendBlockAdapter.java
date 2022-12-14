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
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.health.discount.R;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.base.BaseMultiItemAdapter;
import com.healthy.library.model.ActGoodsItem;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.ScreenUtils;
import com.healthy.library.widget.AutoClickImageView;
import com.healthy.library.widget.ImageTextView;
import com.healthy.library.widget.StaggeredGridLayoutHelperFix;

import java.util.HashMap;
import java.util.Map;

public class ActHomeRecommendBlockAdapter extends BaseMultiItemAdapter<ActGoodsItem> {

    public String key;


    public Map<String,String> imageScalemap=new HashMap<>();
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

    public ActHomeRecommendBlockAdapter() {
        this(R.layout.item_mall_goods);
        addItemType(1, R.layout.item_mall_goods);
        addItemType(2, R.layout.item_mall_goods_ser);
    }

    private ActHomeRecommendBlockAdapter(int viewId) {
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
        mallGoodsImgUnder = (ConstraintLayout)baseHolder.itemView. findViewById(R.id.mall_goods_img_under);
        mallGoodsContext = (TextView)baseHolder.itemView. findViewById(R.id.mall_goods_context);
        mallGoodsSpace = (TextView)baseHolder.itemView. findViewById(R.id.mall_goods_space);
        spinnerImg = (ImageView)baseHolder.itemView. findViewById(R.id.spinnerImg);
        mallGoodsMoneyflag = (TextView) baseHolder.itemView.findViewById(R.id.mall_goods_moneyflag);
        mallGoodsMoneyvalue = (TextView) baseHolder.itemView.findViewById(R.id.mall_goods_moneyvalue);
        mallGoodsMoneyvalueOrg = (TextView) baseHolder.itemView.findViewById(R.id.mall_goods_moneyvalue_org);
        mallGoodsSales = (TextView)baseHolder.itemView. findViewById(R.id.mall_goods_sales);
        passbasket = (AutoClickImageView)baseHolder.itemView. findViewById(R.id.passbasket);
        couponListLL = (HorizontalScrollView)baseHolder.itemView. findViewById(R.id.couponListLL);
        couponList = (LinearLayout) baseHolder.itemView.findViewById(R.id.couponList);
        mallGoodsSales2 = (TextView)baseHolder.itemView. findViewById(R.id.mall_goods_sales2);
        mallGoodsSales2Dis = (TextView)baseHolder.itemView. findViewById(R.id.mall_goods_sales2_dis);
        normalTypeGroup = (Group) baseHolder.itemView.findViewById(R.id.normalTypeGroup);
        seachTypeGroup = (Group) baseHolder.itemView.findViewById(R.id.seachTypeGroup);
        final ActGoodsItem actGoodsItem= getDatas().get(position);


        if(!TextUtils.isEmpty(imageScalemap.get(actGoodsItem.filePath))){
            String value=imageScalemap.get(actGoodsItem.filePath);
            int height= Integer.parseInt(value.split(":")[0]);
            int swidth= Integer.parseInt(value.split(":")[1]);
            ViewGroup.LayoutParams layoutParams= (ViewGroup.LayoutParams) mallGoodsImg.getLayoutParams();
            layoutParams.height=height;
            layoutParams.width=swidth;
            mallGoodsImg.setLayoutParams(layoutParams);
            //System.out.println("??????????????????"+position);
            com.healthy.library.businessutil.GlideCopy.with(context).load(actGoodsItem.filePath)
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_1_1_default)

                    .into(mallGoodsImg);
        }else {
            com.healthy.library.businessutil.GlideCopy.with(context).load(actGoodsItem.filePath)
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_1_1_default)

                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            int swidth = ScreenUtils.getScreenWidth(context)/2-12;
                            int height = (int) ((resource.getIntrinsicHeight() * 1.0 / resource.getIntrinsicWidth()) * swidth);

                            ViewGroup.LayoutParams layoutParams= (ViewGroup.LayoutParams) mallGoodsImg.getLayoutParams();
                            layoutParams.height=height;
                            layoutParams.width=swidth;
                            imageScalemap.put(actGoodsItem.filePath,height+":"+swidth);
                            mallGoodsImg.setLayoutParams(layoutParams);
                            com.healthy.library.businessutil.GlideCopy.with(context).load(resource).into(mallGoodsImg);
                        }
                    });
        }
        mallGoodsContext.setText(actGoodsItem.goodsTitle);
        mallGoodsMoneyvalue.setText(FormatUtils.moneyKeep2Decimals(actGoodsItem.platformPrice));
        mallGoodsMoneyvalueOrg.setText("??"+FormatUtils.moneyKeep2Decimals(actGoodsItem.getPrice()));
        mallGoodsMoneyvalueOrg.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);
        buildCouponList(couponList);
        tvVideoFlag.setVisibility(View.GONE);
        if("2".equals(actGoodsItem.courseFlag)){
            tvVideoFlag.setVisibility(View.VISIBLE);
        }
        baseHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actGoodsItem.goodsType == 1) {//??????
                    ARouter.getInstance()
                            .build(ServiceRoutes.SERVICE_DETAIL)
                            .withString("goodsId",actGoodsItem.goodsId+"")
                            .navigation();

                } else {//??????
                    ARouter.getInstance()
                            .build(ServiceRoutes.SERVICE_DETAIL)
                            .withString("id", actGoodsItem.goodsId + "")
                            .navigation();
                }
            }
        });


    }

    private void buildCouponList(LinearLayout couponList) {
        couponList.removeAllViews();
    }

    private void initView() {

    }


    public interface OnBasketClickListener {
        void onBasketClick(View view);
    }
}
