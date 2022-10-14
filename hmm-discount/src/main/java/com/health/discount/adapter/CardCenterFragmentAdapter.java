package com.health.discount.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.discount.R;
import com.healthy.library.model.CouponGoodsModel;
import com.healthy.library.model.CouponInfoZ;
import com.healthy.library.utils.FormatUtils;

import java.util.List;

public class CardCenterFragmentAdapter extends BaseQuickAdapter<CouponInfoZ, BaseViewHolder> {

    public CardCenterFragmentAdapter() {
        this(R.layout.dis_item_cardcenter);
    }

    public CardCenterFragmentAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, CouponInfoZ item) {
        if (item == null) {
            return;
        }
        RelativeLayout noCardLL = helper.getView(R.id.noCardLL);
        TextView cardICenterLeftTop = helper.getView(R.id.cardICenterLeftTop);
        TextView couponValue = helper.getView(R.id.couponValue);
        TextView couponContent = helper.getView(R.id.couponContent);
        TextView canReceiveTxt = helper.getView(R.id.canReceiveTxt);
        LinearLayout listGoodsLL = helper.getView(R.id.listGoodsLL);
        listGoodsLL.removeAllViews();
        if (item.getCouponUseName() != null && !TextUtils.isEmpty(item.getCouponUseName())) {
            cardICenterLeftTop.setText(item.getCouponUseName());
        }
        if (item.getCouponDenomination() != null && !TextUtils.isEmpty(item.getCouponDenomination())) {
            couponValue.setText(FormatUtils.moneyKeep2Decimals(item.getCouponDenomination()));
            if(item.getCouponDenomination().length()>4){
                couponValue.setTextSize(24);
            }else if(item.getCouponDenomination().length()>3){
                couponValue.setTextSize(27);
            }else if(item.getCouponDenomination().length()>2){
                couponValue.setTextSize(34);
            }else if(item.getCouponDenomination().length()>1){
                couponValue.setTextSize(34);
            }else {
                couponValue.setTextSize(34);
            }
        }
        couponContent.setText(item.getRequirement());

        noCardLL.setVisibility(View.GONE);
        canReceiveTxt.setText("立即领取");
        if (item.isCanReceive()) {
            canReceiveTxt.setText("立即领取");
        }else {
            if(item.availableCount>0){
                canReceiveTxt.setText("立即使用");
            }else {
//                noCardLL.setVisibility(View.VISIBLE);
            }
        }
        if (item.getCouponQuantity() == 0) {
            noCardLL.setVisibility(View.VISIBLE);
        }

        if (item.couponGoodsModelList != null) {
            List<CouponGoodsModel> list = item.couponGoodsModelList;
            int needsize=list.size()>3?3:list.size();
            for (int i = 0; i < needsize; i++) {
                View view = LayoutInflater.from(mContext).inflate(com.healthy.library.R.layout.dis_item_cardcenter_goods, listGoodsLL, false);
                ImageView goodsIcon = (ImageView) view.findViewById(R.id.goodsIcon);
                TextView goodsName = (TextView) view.findViewById(R.id.goodsName);
                TextView goodsNum = (TextView) view.findViewById(R.id.goodsNum);
                CouponGoodsModel model = list.get(i);
                com.healthy.library.businessutil.GlideCopy.with(mContext)
                        .load(model.headImage)
                        .placeholder(R.drawable.img_1_1_default)
                        .error(R.drawable.img_1_1_default)
                        .into(goodsIcon);
                //goodsName.setText(model.goodstitle);
                goodsNum.setText("￥" + FormatUtils.moneyKeep2Decimals(model.platformPrice));
                listGoodsLL.addView(view);
            }
        }

    }
}
