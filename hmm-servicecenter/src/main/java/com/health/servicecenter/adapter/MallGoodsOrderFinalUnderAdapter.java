package com.health.servicecenter.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.servicecenter.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.model.CouponInfoZ;
import com.healthy.library.model.GoodsBasketCell;
import com.healthy.library.model.GoodsBasketStore;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.widget.ImageTextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MallGoodsOrderFinalUnderAdapter extends BaseAdapter<String> {

    List<GoodsBasketCell> goodsbasketlist;
    List<GoodsBasketStore> goodsBasketStoreList = new ArrayList<>();
    List<GoodsBasketStore> goodsBasketStoreListEx = new ArrayList<>();
    List<CouponInfoZ> selectInfo = new ArrayList<>();
    String type = "0";
    String race = "0";
    String goodsAllMarketingType;


    String bargainId;
    String bargainMemberId;
    String bargainMoney;
    //-----------------------------------------------------
    String assembleId;
    String teamNum;
    String assemblePrice;
    //-----------------------------------------------------
    String packageMoney;
    String packageId;
    String packageQuantity;
    //------------------------------------------------------
    public LinearLayout hasCouponLL;
    public TextView couponSelectCount;
    public ImageTextView couponMore;
    public LinearLayout hasDiscountLL;
    public LinearLayout detalAllLL;
    public TextView detalAllLLV;
    public LinearLayout detalTranLL;
    public TextView detalTranLLV;
    public LinearLayout detalDisLL;
    public TextView detalDisLLV;
    public LinearLayout detalCouponLL;
    public TextView detalCouponLLV;
    public LinearLayout pointLL;
    public TextView pointLLV;
    private String totalPayAmount;
    private String goodsMarketingType;


    public void setExtra(String bargainId,
                         String bargainMemberId,
                         String bargainMoney,
                         String assembleId,
                         String teamNum,
                         String assemblePrice,
                         String packageMoney,
                         String packageId,
                         String packageQuantity,
                         String goodsMarketingType,
                         String type,
                         String race) {
        this.bargainId=bargainId;
        this.bargainMemberId=bargainMemberId;
        this.bargainMoney=bargainMoney;
        this.assembleId=assembleId;
        this.teamNum=teamNum;
        this.assemblePrice=assemblePrice;
        this.packageMoney=packageMoney;
        this.packageId=packageId;
        this.packageQuantity=packageQuantity;
        this.goodsMarketingType=goodsMarketingType;
        this.type=type;
        this.race=race;
    }

//    public void setDisTotalPayAmount(String disTotalPayAmount) {
//        this.disTotalPayAmount = disTotalPayAmount;
//    }

//    private String disTotalPayAmount;


    @Override
    public int getItemViewType(int position) {
        return 48;
    }

    public MallGoodsOrderFinalUnderAdapter() {
        this(R.layout.service_activity_goodsorder_group_under);
    }

    private MallGoodsOrderFinalUnderAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int postion) {


        hasCouponLL = (LinearLayout) baseHolder.itemView.findViewById(R.id.hasCouponLL);
        couponSelectCount = (TextView) baseHolder.itemView.findViewById(R.id.couponSelectCount);
        couponMore = (ImageTextView) baseHolder.itemView.findViewById(R.id.couponMore);
        hasDiscountLL = (LinearLayout) baseHolder.itemView.findViewById(R.id.hasDiscountLL);
        detalAllLL = (LinearLayout) baseHolder.itemView.findViewById(R.id.detalAllLL);
        detalAllLLV = (TextView) baseHolder.itemView.findViewById(R.id.detalAllLLV);
        detalTranLL = (LinearLayout) baseHolder.itemView.findViewById(R.id.detalTranLL);
        detalTranLLV = (TextView) baseHolder.itemView.findViewById(R.id.detalTranLLV);
        detalDisLL = (LinearLayout) baseHolder.itemView.findViewById(R.id.detalDisLL);
        detalDisLLV = (TextView) baseHolder.itemView.findViewById(R.id.detalDisLLV);
        detalCouponLL = (LinearLayout) baseHolder.itemView.findViewById(R.id.detalCouponLL);
        detalCouponLLV = (TextView) baseHolder.itemView.findViewById(R.id.detalCouponLLV);
        pointLL = (LinearLayout) baseHolder.itemView.findViewById(R.id.pointLL);
        pointLLV = (TextView) baseHolder.itemView.findViewById(R.id.pointLLV);
        hasCouponLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(moutClickListener!=null){
                    moutClickListener.outClick("getNowAllCouponInfoList",null);
                    moutClickListener.outClick("buildCouponWithDialog",null);
                }
//                getNowAllCouponInfoList();
//                buildCouponWithDialog();

            }
        });
        buildNowPayMoney();

    }


    public void buildNowPayMoney() {

        BigDecimal totalDecimalDiscount = new BigDecimal(0);
        BigDecimal totalDecimal = new BigDecimal(0);
        BigDecimal totalDecimalPay = new BigDecimal(0);
        BigDecimal totalDecimalPoint = new BigDecimal(0);
        BigDecimal totalDecimalFee = new BigDecimal(0);
        BigDecimal totalDecimalQuan = new BigDecimal(0);
        if("-5".equals(goodsMarketingType)){
            totalPayAmount="0";
        }
        for (int i = 0; i < selectInfo.size(); i++) {
            totalDecimalQuan = totalDecimalQuan.add(new BigDecimal(selectInfo.get(i).getCouponDenomination()));
        }
        for (int i = 0; i < goodsBasketStoreList.size(); i++) {
            GoodsBasketStore goodsBasketStoreTmp = goodsBasketStoreList.get(i);
//            totalDecimalDiscount = totalDecimalDiscount.add(new BigDecimal(goodsBasketStoreTmp.getgCurDiscount()));
            totalDecimal = totalDecimal.add(new BigDecimal(goodsBasketStoreTmp.getgCurPrice()));
            totalDecimalFee = totalDecimalFee.add(new BigDecimal(goodsBasketStoreTmp.getFee()));//算上邮费
            totalDecimalPoint = totalDecimalPoint.add(new BigDecimal(goodsBasketStoreTmp.getgCurPoint()));
        }

        for (int i = 0; i < goodsBasketStoreListEx.size(); i++) {
            GoodsBasketStore goodsBasketStoreTmp = goodsBasketStoreListEx.get(i);
            goodsBasketStoreTmp.checkAct();
            totalDecimalDiscount = totalDecimalDiscount.add(new BigDecimal(goodsBasketStoreTmp.getgCurDiscount()));
        }
        if (!"5".equals(type)) {
            totalDecimalPoint = new BigDecimal(0);
        }
        if (!TextUtils.isEmpty(bargainMoney)) {
            if(goodsBasketStoreList.size()==1&&goodsBasketStoreList.get(0).getGoodsBasketCellAllListExpFixGift().size()==1){
//                if("1".equals(goodsBasketStoreList.get(0).getGoodsBasketCellAllListExpFixGift().get(0).getGoodsMarketingType())){
//                    totalDecimal = totalDecimal.subtract(new BigDecimal(bargainMoney));
//                }
            }
        }
        if (!TextUtils.isEmpty(packageMoney)) {
            totalDecimal = new BigDecimal(packageMoney);
        }
        //计算最后支付金额 商品+邮费-促销-优惠券
        totalDecimalPay = new BigDecimal(totalDecimal.doubleValue() + totalDecimalFee.doubleValue() - totalDecimalDiscount.doubleValue() - totalDecimalQuan.doubleValue());

        if (totalDecimalPay.doubleValue() < 0) {
            //存在优惠券过多得问题
            totalDecimalPay = new BigDecimal(0);
        }
        if(moutClickListener!=null){
            if(!TextUtils.isEmpty(totalPayAmount)){
                moutClickListener.outClick("orderMoney",FormatUtils.moneyKeep2Decimals(totalPayAmount));
            }else {
                moutClickListener.outClick("orderMoney",FormatUtils.moneyKeep2Decimals(totalDecimalPay.doubleValue()));
            }
        }
        if(pointLLV==null){
            return;
        }
        pointLLV.setText(FormatUtils.moneyKeep2Decimals(totalDecimalPoint.doubleValue()));
        detalTranLLV.setText("¥" + FormatUtils.moneyKeep2Decimals(totalDecimalFee.doubleValue()));
        detalAllLLV.setText("¥" + FormatUtils.moneyKeep2Decimals(totalDecimal.doubleValue()));
        detalDisLLV.setText("-¥" + FormatUtils.moneyKeep2Decimals(totalDecimalDiscount.doubleValue()));
        detalCouponLLV.setText("-¥" + FormatUtils.moneyKeep2Decimals(totalDecimalQuan.doubleValue()));

        couponMore.setText(couponMoreText);

        couponSelectCount.setVisibility(View.GONE);
        if (selectInfo.size() > 0) {
            couponSelectCount.setVisibility(View.VISIBLE);
            couponSelectCount.setText("已选" + selectInfo.size() + "张");
            couponMore.setText("-¥" + FormatUtils.moneyKeep2Decimals(totalDecimalQuan.doubleValue()));
        } else {
            if (!"暂无可用".equals(couponMoreText) && !"与促销不同享".equals(couponMoreText)) {
                couponMore.setText("优惠券");
            }
        }
    }
    String couponMoreText;
    public void setCouponMoreText(String couponMoreText){
        this.couponMoreText=couponMoreText;
        if(couponMore==null){
            return;
        }
        couponMore.setText(couponMoreText);
    }
    public void setGoodsList(List<GoodsBasketStore> goodsBasketStoreList, List<GoodsBasketStore> goodsBasketStoreListEx,List<GoodsBasketCell> goodsbasketlist) {
        this.goodsBasketStoreList = goodsBasketStoreList;
        this.goodsBasketStoreListEx=goodsBasketStoreListEx;
        this.goodsbasketlist = goodsbasketlist;
        notifyDataSetChanged();
    }

    private void initView() {

    }

    public void setSelectCoupons(List<CouponInfoZ> selectInfo) {
        this.selectInfo=selectInfo;
    }
    public void setTotalPayAmount(String totalPayAmount) {
        if("-5".equals(goodsMarketingType)){
            totalPayAmount="0";
        }
        this.totalPayAmount = totalPayAmount;

    }

}
