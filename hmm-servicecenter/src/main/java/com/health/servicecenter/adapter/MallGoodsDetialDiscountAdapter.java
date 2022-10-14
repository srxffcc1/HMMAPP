package com.health.servicecenter.adapter;

import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.servicecenter.R;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.business.CouponGoodsDialog;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.ActVip;
import com.healthy.library.model.CouponInfoZ;
import com.healthy.library.utils.SpUtils;

import java.util.List;

public class MallGoodsDetialDiscountAdapter extends BaseAdapter<String> {
    private List<CouponInfoZ> couponList;
    private String shopId;
    private String goodsId;
    private String merchantId;
    private ActVip actVip;
    CouponGoodsDialog couponGoodsDialog;

    public void setActVip(ActVip actVip) {
        this.actVip = actVip;
    }

    public void setId(String shopId, String goodsId, String merchantId) {
        this.shopId = shopId;
        this.goodsId = goodsId;
        this.merchantId = merchantId;
    }

    @Override
    public int getItemViewType(int position) {
        return 4;
    }

    public MallGoodsDetialDiscountAdapter() {
        this(R.layout.service_item_goodsdetail_discount);
    }

    private MallGoodsDetialDiscountAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {
        ConstraintLayout discountLL;
        TextView discountLeft;
        TextView quanTitle;
        ImageView discountMore;
        LinearLayout actLL;
        LinearLayout couponLL;
        LinearLayout quanP;
        TextView spaceBlock;


        quanP = (LinearLayout) baseHolder.itemView.findViewById(R.id.quanP);
        couponLL = (LinearLayout) baseHolder.itemView.findViewById(R.id.couponLL);
        discountLL = (ConstraintLayout) baseHolder.itemView.findViewById(R.id.discountLL);
        discountLeft = (TextView) baseHolder.itemView.findViewById(R.id.discountLeft);
        quanTitle = (TextView) baseHolder.itemView.findViewById(R.id.quanTitle);
        discountMore = (ImageView) baseHolder.itemView.findViewById(R.id.discountMore);
        actLL = (LinearLayout) baseHolder.itemView.findViewById(R.id.actLL);
        spaceBlock = (TextView) baseHolder.itemView.findViewById(R.id.space_block);
        showCouponList(couponLL, quanP);
        baseHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(couponList!=null&&couponList.size()>0){
//
//                }
                ////System.out.println("zzzzzzzzzzz");
                if (couponGoodsDialog != null) {
                    try {
                        couponGoodsDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (couponGoodsDialog == null) {
                    couponGoodsDialog = CouponGoodsDialog.newInstance();
                }
                couponGoodsDialog.show(((BaseActivity) context).getSupportFragmentManager(), "优惠券商品详情");
                couponGoodsDialog.setData(shopId, new String(Base64.decode(SpUtils.getValue(context, SpKey.USER_ID).getBytes(), Base64.DEFAULT)), goodsId, "1", merchantId, actVip);
//                Fragment f = ((FragmentActivity) context).getSupportFragmentManager().findFragmentByTag("优惠券商品详情");
//                if (f != null) {
//                    ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().remove(f).commitNowAllowingStateLoss();
//                }
//                final DialogFragment dialogFragment = (DialogFragment) ARouter.getInstance()
//                        .build(DiscountRoutes.DIS_CARDGOODS)
//                        .withString("shopId", shopId)
//                        .withString("memberId", new String(Base64.decode(SpUtils.getValue(context, SpKey.USER_ID).getBytes(), Base64.DEFAULT)))
//                        .withString("goodsId", goodsId)
//                        .withString("type", "1")
//                        .navigation();
//                dialogFragment.show(((BaseActivity) context).getSupportFragmentManager(), "优惠券商品详情");
            }
        });
        if (actVip != null && actVip.PopInfo != null && actVip.PopInfo.size() > 0) {//有营销活动
            showActList(actLL, spaceBlock, actVip);
        }
    }

    private void showActList(LinearLayout actLL, View spaceBlock, ActVip actVip) {
        actLL.setVisibility(View.GONE);
        spaceBlock.setVisibility(View.GONE);
        actLL.removeAllViews();
        actLL.setVisibility(View.VISIBLE);
        if((couponList==null||couponList.size()==0)){
            spaceBlock.setVisibility(View.GONE);
        }
        for (int i = 0; i < actVip.PopInfo.size(); i++) {
            ActVip.PopInfo selPopInfo = actVip.PopInfo.get(i);
            View view = LayoutInflater.from(context).inflate(R.layout.service_item_goodsdetail_discount_item_noline, actLL, false);
            TextView enoughTitle;
            TextView enoughContext;
            enoughTitle = (TextView) view.findViewById(R.id.enoughTitle);
            enoughContext = (TextView) view.findViewById(R.id.enoughContext);
            if (selPopInfo.PopLabelName != null && !TextUtils.isEmpty(selPopInfo.PopLabelName)) {
                enoughTitle.setVisibility(View.VISIBLE);
                enoughTitle.setText(selPopInfo.PopLabelName);
            } else {
                enoughTitle.setVisibility(View.GONE);
            }
            enoughContext.setText(selPopInfo.PopDesc);
            if (!TextUtils.isEmpty(selPopInfo.PopDesc) && selPopInfo.PopDesc != null) {
                actLL.addView(view);
            } else {
                actLL.setVisibility(View.GONE);
                spaceBlock.setVisibility(View.GONE);
                actLL.removeAllViews();
            }
        }
    }

    private void showCouponList(LinearLayout couponLL, LinearLayout quanP) {
        quanP.setVisibility(View.GONE);
        couponLL.removeAllViews();
        couponLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(couponList!=null&&couponList.size()>0){
//
//                }
                ////System.out.println("zzzzzzzzzzz");
                if (couponGoodsDialog == null) {
                    couponGoodsDialog = CouponGoodsDialog.newInstance();
                }
                couponGoodsDialog.show(((BaseActivity) context).getSupportFragmentManager(), "优惠券商品详情");
                couponGoodsDialog.setData(shopId, new String(Base64.decode(SpUtils.getValue(context, SpKey.USER_ID).getBytes(), Base64.DEFAULT)), goodsId, "1", merchantId, actVip);
//                Fragment f = ((FragmentActivity) context).getSupportFragmentManager().findFragmentByTag("优惠券商品详情");
//                if (f != null) {
//                    ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().remove(f).commitNowAllowingStateLoss();
//                }
//                final DialogFragment dialogFragment = (DialogFragment) ARouter.getInstance()
//                        .build(DiscountRoutes.DIS_CARDGOODS)
//                        .withString("shopId", shopId)
//                        .withString("memberId", new String(Base64.decode(SpUtils.getValue(context, SpKey.USER_ID).getBytes(), Base64.DEFAULT)))
//                        .withString("goodsId", goodsId)
//                        .withString("type", "1")
//                        .navigation();
//                dialogFragment.show(((BaseActivity) context).getSupportFragmentManager(), "优惠券商品详情");
            }
        });
        if (couponList != null && couponList.size() > 0) {
            quanP.setVisibility(View.VISIBLE);
            for (int j = 0; j < couponList.size(); j++) {
                View view = LayoutInflater.from(context).inflate(R.layout.item_mall_goods_coupon, couponLL, false);
                TextView mall_coupon_value = view.findViewById(R.id.mall_coupon_value);
                mall_coupon_value.setText(couponList.get(j).getCouponNormalName());
                couponLL.addView(view);
            }

        }
        if (couponList != null && couponList.size() < 4) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_mall_goods_coupon, couponLL, false);
            TextView mall_coupon_value = view.findViewById(R.id.mall_coupon_value);
            mall_coupon_value.setText("隐藏的一个超长的view");
            view.setVisibility(View.INVISIBLE);
            couponLL.addView(view);
        }
    }

    public void setCouponList(List<CouponInfoZ> couponList) {
        this.couponList = couponList;
    }

    public List<CouponInfoZ> getCouponList() {
        return couponList;
    }

    private void initView() {


    }
}
