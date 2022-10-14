package com.health.servicecenter.adapter;

import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.servicecenter.R;
import com.healthy.library.BuildConfig;
import com.healthy.library.LibApplication;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.builder.ObjectIteraor;
import com.healthy.library.builder.SimpleArrayListBuilder;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.business.CouponChoseDialog;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.constant.SpKey;
import com.healthy.library.interfaces.IHmmCoupon;
import com.healthy.library.model.CouponInfoZ;
import com.healthy.library.model.GoodsBasketCell;
import com.healthy.library.model.GoodsBasketGroup;
import com.healthy.library.model.GoodsBasketStore;
import com.healthy.library.model.MemberAction;
import com.healthy.library.presenter.ActionPresenterCopy;
import com.healthy.library.routes.DiscountRoutes;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.AutoFitCheckBox;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.ImageTextView;
import com.healthy.library.widget.IncreaseDecreaseView;
import com.umeng.analytics.MobclickAgent;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 门店组
 */
public class MallGoodsBasketStoreAdapter extends BaseAdapter<GoodsBasketStore> {
    Map<String, Boolean> checkMap = new HashMap<>();
    MallGoodsBasketAdapter.OnGoodsChangeListener onGoodsChangeListener;
    MallGoodsBasketAdapter.OnGoodsCountChangeListener onGoodsCountChangeListener;
    private Map<String, String> imageMap;


    @Override
    public int getItemViewType(int position) {
        return 43;
    }

    public MallGoodsBasketStoreAdapter() {
        this(R.layout.service_item_goodsbasket_store);
    }

    private MallGoodsBasketStoreAdapter(int viewId) {
        super(viewId);
    }

    public void setCheckMap(Map<String, Boolean> checkMap) {
        this.checkMap = checkMap;
    }

    public Map<String, Boolean> getCheckMap() {
        return checkMap;
    }

    public void setOnGoodsChangeListener(MallGoodsBasketAdapter.OnGoodsChangeListener onGoodsChangeListener) {
        this.onGoodsChangeListener = onGoodsChangeListener;
    }

    public void setOnGoodsCountChangeListener(MallGoodsBasketAdapter.OnGoodsCountChangeListener onGoodsCountChangeListener) {
        this.onGoodsCountChangeListener = onGoodsCountChangeListener;
    }


    public ArrayList<GoodsBasketCell> getSelectOnlyGoodsList() {//需要
        ArrayList<GoodsBasketCell> result = new ArrayList<>();
        for (int i = 0; i < getDatas().size(); i++) {
            final GoodsBasketStore goodsBasketStore = getDatas().get(i);
            goodsBasketStore.redogoodsStock();
            for (int j = 0; j < goodsBasketStore.goodsBasketGroupList.size(); j++) {
                GoodsBasketGroup goodsBasketGroup = goodsBasketStore.goodsBasketGroupList.get(j);
                for (int k = 0; k < goodsBasketGroup.goodsBasketCellListGift.size(); k++) {
                    final GoodsBasketCell goodsBasketCellChild = goodsBasketGroup.goodsBasketCellListGift.get(k);
                    if (goodsBasketCellChild.ischeck && !goodsBasketCellChild.isNeedFixOrg) {
                        goodsBasketCellChild.isGift = true;
                        result.add(goodsBasketCellChild);
                    }
                }
                for (int k = 0; k < goodsBasketGroup.goodsBasketCellList.size(); k++) {
                    final GoodsBasketCell goodsBasketCellChild = goodsBasketGroup.goodsBasketCellList.get(k);
//                    if("3".equals(goodsBasketCellChild.getGoodsMarketingTypeOrg())||"1".equals(goodsBasketCellChild.getGoodsMarketingTypeOrg())||"2".equals(goodsBasketCellChild.getGoodsMarketingTypeOrg())){
//                        goodsBasketCellChild.goodsMarketingDTO=null;
//                    }
                    if (goodsBasketCellChild.ischeck && !goodsBasketCellChild.isGift) {
                        goodsBasketCellChild.curGoodsPlusAmount=0;
                        result.add(goodsBasketCellChild);
                    }
                }

            }
        }
        return result;
    }
    public int getSelectMapSize() {//需要
        int result=0;
        Set<Map.Entry<String, Boolean>> set = checkMap.entrySet();
        // 遍历键值对对象的集合，得到每一个键值对对象
        for (Map.Entry<String, Boolean> me : set) {
            if(me.getValue()){
                result++;
            }
        }
        return result;
    }
    public ArrayList<GoodsBasketCell> getSelectNeedPassList() {//需要
        ArrayList<GoodsBasketCell> result = new ArrayList<>();
        for (int i = 0; i < getDatas().size(); i++) {
            final GoodsBasketStore goodsBasketStore = getDatas().get(i);
            goodsBasketStore.redogoodsStock();
            for (int j = 0; j < goodsBasketStore.goodsBasketGroupList.size(); j++) {
                GoodsBasketGroup goodsBasketGroup = goodsBasketStore.goodsBasketGroupList.get(j);
                for (int k = 0; k < goodsBasketGroup.goodsBasketCellList.size(); k++) {
                    final GoodsBasketCell goodsBasketCellChild = goodsBasketGroup.goodsBasketCellList.get(k);
                    if("3".equals(goodsBasketCellChild.getGoodsMarketingTypeOrg())
                            ||"4".equals(goodsBasketCellChild.getGoodsMarketingTypeOrg())
                            ||"1".equals(goodsBasketCellChild.getGoodsMarketingTypeOrg())
                            ||"2".equals(goodsBasketCellChild.getGoodsMarketingTypeOrg())){
                        goodsBasketCellChild.goodsMarketingDTO=null;
                    }
                    if(goodsBasketCellChild.ischeck){
                        result.add(goodsBasketCellChild);
                    }else {
                        if(goodsBasketCellChild.isGift){
                            result.add(goodsBasketCellChild);
                        }else if(goodsBasketCellChild.isCardCanUse){
                            result.add(goodsBasketCellChild);
                        }
                    }
                }

            }
        }
        return result;
    }


    public void checkAll(boolean b) {
        for (int i = 0; i < getDatas().size(); i++) {
            final GoodsBasketStore goodsBasketStore = getDatas().get(i);
            for (int j = 0; j < goodsBasketStore.goodsBasketGroupList.size(); j++) {
                for (int k = 0; k < goodsBasketStore.goodsBasketGroupList.get(j).goodsBasketCellList.size(); k++) {
                    final GoodsBasketCell goodsBasketCellChild = goodsBasketStore.goodsBasketGroupList.get(j).goodsBasketCellList.get(k);
                    goodsBasketCellChild.ischeck = b;
                    checkMap.put(goodsBasketCellChild.cartDetailId + "", b);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, final int i) {
        ConstraintLayout storeTop;
        ImageTextView passStoreName;
        ImageTextView passCoupon;
        LinearLayout groupLL;
        storeTop = (ConstraintLayout) baseHolder.itemView.findViewById(R.id.storeTop);
        passStoreName = (ImageTextView) baseHolder.itemView.findViewById(R.id.passStoreName);
        passCoupon = (ImageTextView) baseHolder.itemView.findViewById(R.id.passCoupon);
        groupLL = (LinearLayout) baseHolder.itemView.findViewById(R.id.groupLL);
        LinearLayout discountUnder = baseHolder.itemView.findViewById(R.id.discountUnder);
         LinearLayout hasCouponLL;

        hasCouponLL = (LinearLayout) baseHolder.itemView.findViewById(R.id.hasCouponLL);

        final GoodsBasketStore goodsBasketStore = getDatas().get(i);
        passStoreName.setText(goodsBasketStore.shopName);
        goodsBasketStore.undogoodsStock();
        buildGoodsGroup(groupLL, goodsBasketStore.goodsBasketGroupList);
        discountUnder.setVisibility(View.GONE);
        if (goodsBasketStore.actVipResult != null && goodsBasketStore.actVipResult.PopInfo!=null&&goodsBasketStore.actVipResult.PopInfo.size() > 0) {
            discountUnder.setVisibility(View.VISIBLE);
        }
        discountUnder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moutClickListener != null) {
                    moutClickListener.outClick("打开营销活动", goodsBasketStore);
                }
            }
        });
        passCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(DiscountRoutes.DIS_CARDCENTER)
                        .navigation();
                new ActionPresenterCopy(LibApplication.getAppContext()).posAction(
                        new SimpleHashMapBuilder<>()
                                .putObject(new MemberAction(
                                        BuildConfig.VERSION_NAME,
                                        1,
                                        7,
                                        "CardCenterFromBasket",
                                        "",
                                        new String(Base64.decode(SpUtils.getValue(LibApplication.getAppContext(), SpKey.USER_ID).getBytes(), Base64.DEFAULT)))
                                ));
            }
        });
        hasCouponLL.setVisibility(View.GONE);
        if(goodsBasketStore.getUnderCardCanUse().size()>0){//线下存在优惠券
            hasCouponLL.setVisibility(View.VISIBLE);
            buildGoodsCoupon(hasCouponLL,goodsBasketStore.getUnderCardCanUseSelect(),goodsBasketStore.getUnderCardCanUse());
        }
    }

    private void buildGoodsCoupon(LinearLayout view, final List<GoodsBasketCell> underCardCanUseSelect, final List<GoodsBasketCell> underCardCanUse) {
        TextView couponSelectCount;
        ImageTextView couponMore;
        couponSelectCount = (TextView) view.findViewById(R.id.couponSelectCount);
        couponMore = (ImageTextView) view.findViewById(R.id.couponMore);
        couponSelectCount.setVisibility(View.GONE);
        BigDecimal totalDecimalQuan = new BigDecimal(0);
        for (int i = 0; i < underCardCanUseSelect.size(); i++) {
            totalDecimalQuan = totalDecimalQuan.add(
                    new BigDecimal(underCardCanUseSelect.get(i).goodsMarketingDTO.marketingPrice)
                            .multiply(new BigDecimal(underCardCanUseSelect.get(i).getGoodsQuantityInBasket()))
            );
        }
        totalDecimalQuan=totalDecimalQuan.multiply(new BigDecimal(-1));
        if (underCardCanUseSelect.size() > 0) {
            couponSelectCount.setVisibility(View.VISIBLE);
            couponSelectCount.setText("已选" + underCardCanUseSelect.size() + "张");
            couponMore.setText("-¥" + FormatUtils.moneyKeep2Decimals(totalDecimalQuan.doubleValue()));
        } else {
            couponMore.setText("优惠券");
        }
        couponMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildCouponWithDialog(underCardCanUseSelect,underCardCanUse);
            }
        });

    }

    CouponChoseDialog couponChoseDialog;
    private void buildCouponWithDialog(List<GoodsBasketCell> underCardCanUseSelect, final List<GoodsBasketCell> underCardCanUse) {
        couponChoseDialog = CouponChoseDialog.newInstance();
        couponChoseDialog.setSelectList(new SimpleArrayListBuilder<CouponInfoZ>().putList(underCardCanUseSelect, new ObjectIteraor<GoodsBasketCell>() {
            @Override
            public CouponInfoZ getDesObj(GoodsBasketCell o) {
                return new CouponInfoZ(o);
            }
        }));
        couponChoseDialog.setList(new SimpleArrayListBuilder<CouponInfoZ>().putList(underCardCanUse, new ObjectIteraor<GoodsBasketCell>() {
            @Override
            public CouponInfoZ getDesObj(GoodsBasketCell o) {
                return new CouponInfoZ(o);
            }
        }),null);
        couponChoseDialog.setCouponOkButtonClickenlistener(new CouponChoseDialog.CouponOkButtonClickenlistener() {
            @Override
            public void getResult(List<IHmmCoupon> coupons) {
                for (int i = 0; i < underCardCanUse.size(); i++) {
                    underCardCanUse.get(i).isCardSelect=false;
                }
                for (int i = 0; i < underCardCanUse.size(); i++) {
                    if(ListUtil.checkObjIsInList(new SimpleArrayListBuilder<String>().putList(coupons, new ObjectIteraor<IHmmCoupon>() {
                        @Override
                        public Object getDesObj(IHmmCoupon o) {
                            return o.getUseId();
                        }
                    }),underCardCanUse.get(i).getOnlyCouponId())){
                        underCardCanUse.get(i).isCardSelect=true;
                    }
                }
                notifyDataSetChanged();
                if(moutClickListener!=null){
                    moutClickListener.outClick("刷新优惠券结果",null);
                }
            }
        });
        couponChoseDialog.show(((BaseActivity)context).getSupportFragmentManager(), "购物车优惠券");
    }

    private void buildGoodsGroup(LinearLayout groupLL, List<GoodsBasketGroup> goodsBasketGroupList) {
        groupLL.removeAllViews();
        for (int i = 0; i < goodsBasketGroupList.size(); i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.service_item_goodsbasket_group, groupLL, false);
            buildGoodsGroupChild(goodsBasketGroupList.get(i), view, i == goodsBasketGroupList.size() - 1);
            groupLL.addView(view);
        }
    }

    private void buildGoodsGroupChild(GoodsBasketGroup goodsBasketGroup, View view, boolean islastGroup) {
        goodsBasketGroup.checkAct();
        LinearLayout groupCenter;
        LinearLayout groupBottom;
        groupCenter = (LinearLayout) view.findViewById(R.id.groupCenter);
        groupBottom = (LinearLayout) view.findViewById(R.id.groupBottom);
        buildGoodsCenter(groupCenter, goodsBasketGroup.goodsBasketCellList, goodsBasketGroup.goodsBasketCellListGift.size() > 0, islastGroup);
//        buildGoodsBottom(groupBottom, goodsBasketGroup.goodsBasketCellListGift,islastGroup);

    }

    private void buildGoodsBottom(LinearLayout groupBottom, List<GoodsBasketCell> goodsBasketCellListGift, boolean islastGroup) {
        groupBottom.setVisibility(View.GONE);
        groupBottom.removeAllViews();
        for (int i = 0; i < goodsBasketCellListGift.size(); i++) {
            groupBottom.setVisibility(View.VISIBLE);
            View view = LayoutInflater.from(context).inflate(R.layout.service_item_goodsbasket_gift, groupBottom, false);
            TextView limitLineGiftText = view.findViewById(R.id.limitLineGiftText);
            limitLineGiftText.setVisibility(i == goodsBasketCellListGift.size() - 1 ? View.VISIBLE : View.INVISIBLE);
            if (islastGroup) {
                limitLineGiftText.setVisibility(View.INVISIBLE);
            }
            buildGoodsBottomChild(goodsBasketCellListGift.get(i), view);
            if (goodsBasketCellListGift.get(i).ischeck) {
                groupBottom.addView(view);
            }
        }
    }

    private void buildGoodsBottomChild(final GoodsBasketCell goodsBasketCell, View view) {
        CornerImageView basketImg;
        TextView basketImgNo;
        TextView basketFlag;
        TextView basketTitle;
        TextView price;
//        TextView delete;
        TextView limitLineGiftText;
        TextView giftCount;
        giftCount = (TextView) view.findViewById(R.id.giftCount);
        basketImg = (CornerImageView) view.findViewById(R.id.basketImg);
        basketImgNo = (TextView) view.findViewById(R.id.basketImgNo);
        basketFlag = (TextView) view.findViewById(R.id.basketFlag);
        basketTitle = (TextView) view.findViewById(R.id.basketTitle);
        price = (TextView) view.findViewById(R.id.price);
//        delete = (TextView) view.findViewById(R.id.delete);
        limitLineGiftText = (TextView) view.findViewById(R.id.limitLineGiftText);
        if(goodsBasketCell.isUseCard){
            if(goodsBasketCell.goodsImage!=null&&imageMap.get(goodsBasketCell.getGoodsBarCode())==null){
                imageMap.put(goodsBasketCell.getGoodsBarCode(),goodsBasketCell.goodsImage);
            }
            if(!TextUtils.isEmpty(imageMap.get(goodsBasketCell.getGoodsBarCode()))){
                goodsBasketCell.goodsImage=imageMap.get(goodsBasketCell.getGoodsBarCode());
            }
            com.healthy.library.businessutil.GlideCopy.with(context)
                    .load(goodsBasketCell.goodsImage)
                    .placeholder(R.drawable.img_1_1_default_z)
                    .error(R.drawable.img_1_1_default_z)
                    
                    .into(basketImg);
        }else {
            if(goodsBasketCell.goodsImage!=null&&imageMap.get(goodsBasketCell.getGoodsBarCode())==null){
                imageMap.put(goodsBasketCell.getGoodsBarCode(),goodsBasketCell.goodsImage);
            }
            if(!TextUtils.isEmpty(imageMap.get(goodsBasketCell.getGoodsBarCode()))){
                goodsBasketCell.goodsImage=imageMap.get(goodsBasketCell.getGoodsBarCode());
            }
            com.healthy.library.businessutil.GlideCopy.with(context)
                    .load(goodsBasketCell.goodsImage)
                    .placeholder(R.drawable.img_1_1_default_z)
                    .error(R.drawable.img_1_1_default_z)
                    
                    .into(basketImg);
        }

        giftCount.setText("x " + goodsBasketCell.getGoodsQuantityInBasket() + "");
        System.out.println("取出赠品数量"+goodsBasketCell.getGoodsQuantityInBasket());
        basketTitle.setText(goodsBasketCell.goodsTitle);
        basketTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_DETAIL)
                        .withString("id", goodsBasketCell.goodsId)
                        .withString("barcodeSku",goodsBasketCell.getGoodsBarCode())
                        .navigation();
            }
        });
        basketImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_DETAIL)
                        .withString("id", goodsBasketCell.goodsId)
                        .withString("barcodeSku",goodsBasketCell.getGoodsBarCode())
                        .navigation();
            }
        });
//        delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                goodsBasketCell.ischeck=true;
//            }
//        });
    }

    private void buildGoodsCenter(LinearLayout groupCenter, List<GoodsBasketCell> goodsBasketCellList, boolean hasGift, boolean islastGroup) {
        groupCenter.removeAllViews();
        for (int i = 0; i < goodsBasketCellList.size(); i++) {
            View view = null;
            if (goodsBasketCellList.get(i).isGift) {//赠品
                if (!goodsBasketCellList.get(i).isCardCanUse) {
                    view = LayoutInflater.from(context).inflate(R.layout.service_item_goodsbasket_gift_withdiscount, groupCenter, false);
//                TextView limitLineText = view.findViewById(R.id.limitLineText);
//                limitLineText.setVisibility(i == goodsBasketCellList.size() - 1 && !hasGift && !islastGroup ? View.VISIBLE : View.INVISIBLE);
//            limitLineText.setVisibility(View.VISIBLE);
//                buildGoodsCenterChild(goodsBasketCellList.get(i), view, i == goodsBasketCellList.size() - 1 && !hasGift && !islastGroup);
                    buildGoodsBottomChild(goodsBasketCellList.get(i), view);
                }

            } else {
                view = LayoutInflater.from(context).inflate(R.layout.service_item_goodsbasket, groupCenter, false);
                TextView limitLineText = view.findViewById(R.id.limitLineText);
                limitLineText.setVisibility(i == goodsBasketCellList.size() - 1 && !hasGift && !islastGroup ? View.VISIBLE : View.INVISIBLE);
                View basketActLL = view.findViewById(R.id.basketActLL);
//            limitLineText.setVisibility(View.VISIBLE);
                buildGoodsCenterChild(goodsBasketCellList.get(i), view, i == goodsBasketCellList.size() - 1 && !hasGift && !islastGroup);
                if (i != 0) {
                    basketActLL.setVisibility(View.GONE);
                }
            }
            if (view != null) {
                groupCenter.addView(view);
            }
        }
    }

    /**
     * 构造
     *
     * @param goodsBasketCell
     * @param view
     */
    private void buildGoodsCenterChild(final GoodsBasketCell goodsBasketCell, View view, final boolean islimitLineTextShow) {
        AutoFitCheckBox basketCheck;
        CornerImageView basketImg;
        TextView basketTitle;
        TextView basketSku;
        final TextView basketMoney;
        IncreaseDecreaseView increaseDecrease;
        final ConstraintLayout basketActLL;
        TextView basketFlag;
        TextView basketFlagT;
        TextView basketFlagV;
        ImageTextView basketFlagButton;

        LinearLayout timeLiner;
        TextView kickDay;
        TextView kickDayT;
        TextView kickHour;
        TextView kickMin;
        TextView kickSec;
        TextView basketFlagTT;
        final ConstraintLayout limitCon;
        final TextView limitText;
        limitCon = (ConstraintLayout) view.findViewById(R.id.limitCon);
        limitText = (TextView) view.findViewById(R.id.limitText);
        timeLiner = (LinearLayout) view.findViewById(R.id.timeLiner);
        kickDay = (TextView) view.findViewById(R.id.kickDay);
        kickDayT = (TextView) view.findViewById(R.id.kickDayT);
        kickHour = (TextView) view.findViewById(R.id.kickHour);
        kickMin = (TextView) view.findViewById(R.id.kickMin);
        kickSec = (TextView) view.findViewById(R.id.kickSec);

        basketCheck = (AutoFitCheckBox) view.findViewById(R.id.basketCheck);
        basketImg = (CornerImageView) view.findViewById(R.id.basketImg);
        basketTitle = (TextView) view.findViewById(R.id.basketTitle);
        basketSku = (TextView) view.findViewById(R.id.basketSku);
        basketMoney = (TextView) view.findViewById(R.id.basketMoney);
        increaseDecrease = (IncreaseDecreaseView) view.findViewById(R.id.increase_decrease);

        basketActLL = (ConstraintLayout) view.findViewById(R.id.basketActLL);
        basketFlag = (TextView) view.findViewById(R.id.basketFlag);
        basketFlagT = (TextView) view.findViewById(R.id.basketFlagT);
        basketFlagV = (TextView) view.findViewById(R.id.basketFlagV);
        basketFlagButton = (ImageTextView) view.findViewById(R.id.basketFlagButton);
        basketFlagTT = (TextView) view.findViewById(R.id.basketFlagTT);
        basketFlagButton.setVisibility(View.GONE);
        basketFlagV.setVisibility(View.GONE);
        timeLiner.setVisibility(View.GONE);
        limitCon.setVisibility(islimitLineTextShow ? View.INVISIBLE : View.GONE);
        basketFlagT.setVisibility(View.GONE);
        basketFlagTT.setVisibility(View.GONE);
        basketActLL.setVisibility(View.GONE);
        if (goodsBasketCell.goodsMarketingDTO != null) {
            basketActLL.setVisibility(View.VISIBLE);
            if (goodsBasketCell.goodsMarketingDTO.availableInventory <= 0) {
                basketActLL.setVisibility(View.GONE);
            } else {
                if ("1".equals(goodsBasketCell.goodsMarketingDTO.marketingType)) {//砍价
                    basketFlagT.setVisibility(View.VISIBLE);
                    basketFlagButton.setVisibility(View.VISIBLE);
                    basketFlagV.setVisibility(View.VISIBLE);
                    basketFlag.setText("砍价");
                    basketFlagT.setText("可砍至");
                    basketFlagV.setText("¥" + FormatUtils.moneyKeep2Decimals(goodsBasketCell.goodsMarketingDTO.marketingPrice));
                    basketFlagButton.setText("去砍价");
                } else if ("2".equals(goodsBasketCell.goodsMarketingDTO.marketingType)) {
                    basketFlagT.setVisibility(View.VISIBLE);
                    basketFlagButton.setVisibility(View.VISIBLE);
                    basketFlagV.setVisibility(View.VISIBLE);
                    basketFlag.setText("拼团");
                    basketFlagT.setText("拼团购买价");
                    basketFlagV.setText("¥" + FormatUtils.moneyKeep2Decimals(goodsBasketCell.goodsMarketingDTO.marketingPrice));
                    basketFlagButton.setText("去拼团");

                } else if ("3".equals(goodsBasketCell.goodsMarketingDTO.marketingType)) {
                    basketFlagT.setVisibility(View.VISIBLE);
                    basketFlagButton.setVisibility(View.VISIBLE);
                    basketFlagV.setVisibility(View.VISIBLE);
                    basketFlag.setText("秒杀");
                    basketFlagT.setText("秒杀价");
                    basketFlagV.setText("¥" + FormatUtils.moneyKeep2Decimals(goodsBasketCell.goodsMarketingDTO.marketingPrice));
                    basketFlagButton.setText("去秒杀");
//                    basketFlag.setText("秒杀");
//                    basketFlagT.setText("距离结束还剩");
//                    basketFlagButton.setVisibility(View.VISIBLE);
//                    basketFlagButton.setText("查看");
//                    if (goodsBasketCell.getMarkLimitMin() > goodsBasketCell.getMarkLimitMaxNowWithInventory()) {
//                        limitCon.setVisibility(islimitLineTextShow ? View.INVISIBLE : View.GONE);
//                        basketActLL.setVisibility(View.GONE);
//                    } else {
//                        killgoodsCountChange(goodsBasketCell.getGoodsQuantity(), goodsBasketCell, basketMoney, limitCon, islimitLineTextShow, limitText);
//                    }
//
//                    try {
//                        basketFlagTT.setText(new SimpleDateFormat("MM月dd日HH:mm").format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(goodsBasketCell.goodsMarketingDTO.endTime)) + " 活动结束");
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                } else {
                    basketActLL.setVisibility(View.GONE);

                }
                basketActLL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ("1".equals(goodsBasketCell.goodsMarketingDTO.marketingType)) {//砍价
                            ARouter.getInstance()
                                    .build(ServiceRoutes.SERVICE_DETAIL)
                                    .withString("bargainId", goodsBasketCell.goodsMarketingDTO.marketingId)
                                    .navigation();
                        } else if ("2".equals(goodsBasketCell.goodsMarketingDTO.marketingType)) {
                            ARouter.getInstance()
                                    .build(ServiceRoutes.SERVICE_DETAIL)
                                    .withString("assembleId", goodsBasketCell.goodsMarketingDTO.marketingId)
                                    .navigation();
                        } else if ("3".equals(goodsBasketCell.goodsMarketingDTO.marketingType)) {
                            ARouter.getInstance()
                                    .build(ServiceRoutes.SERVICE_DETAIL)
                                    .withString("type", "3")
                                    .withString("id", goodsBasketCell.goodsId)
                                    .navigation();
                        }
                    }
                });
            }

        }

        if(goodsBasketCell.goodsImage!=null&&imageMap.get(goodsBasketCell.getGoodsBarCode())==null){
            imageMap.put(goodsBasketCell.getGoodsBarCode(),goodsBasketCell.goodsImage);
        }
        if(!TextUtils.isEmpty(imageMap.get(goodsBasketCell.getGoodsBarCode()))){
            goodsBasketCell.goodsImage=imageMap.get(goodsBasketCell.getGoodsBarCode());
        }
        com.healthy.library.businessutil.GlideCopy.with(context)
                .load(goodsBasketCell.goodsImage)
                .placeholder(R.drawable.img_1_1_default_z)
                .error(R.drawable.img_1_1_default_z)
                
                .into(basketImg);
        basketTitle.setText(goodsBasketCell.goodsTitle);
        basketSku.setText(goodsBasketCell.goodsSpecDesc == null ? "" : goodsBasketCell.goodsSpecDesc.replace("无规格，默认值", ""));
        System.out.println("购物车价格"+FormatUtils.moneyKeep2Decimals(goodsBasketCell.getCurGoodsAmountInBasket()));
        basketMoney.setText("￥" + FormatUtils.moneyKeep2Decimals(goodsBasketCell.getCurGoodsAmountInBasket()) + "");
//        ////System.out.println("购物车行" + goodsBasketCell.ischeck + ":" + goodsBasketCell.id);
        basketCheck.setChecked(goodsBasketCell.ischeck);
        increaseDecrease.setMinCount(1);
        increaseDecrease.setMaxCount(goodsBasketCell.getStockInBasket());
        increaseDecrease.setNoCount(goodsBasketCell.getGoodsQuantityInBasket());
        increaseDecrease.setCheckTextDialog(true);
        increaseDecrease.setOnNumChangedListener(new IncreaseDecreaseView.OnNumChangedListener() {
            @Override
            public void onNumChanged(int num) {
                if (onGoodsCountChangeListener != null) {
                    ////System.out.println("购物车加减回调");
                    goodsBasketCell.setGoodsQuantity(num);
//                    killgoodsCountChange(num, goodsBasketCell, basketMoney, limitCon, islimitLineTextShow, limitText);
                    onGoodsCountChangeListener.onGoodsCountChange(goodsBasketCell, num);
//                    if (onGoodsChangeListener != null) {
//                        onGoodsChangeListener.onGoodsAdd();
//                    }
                }
            }
        });
        basketTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map nokmap = new HashMap<String, String>();
                nokmap.put("soure", "购物车中点击商品栏");
                MobclickAgent.onEvent(context, "btn_APP__MaternalandChildGoods_CommodityDetails", nokmap);

                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_DETAIL)
                        .withString("id", goodsBasketCell.goodsId)
                        .withString("barcodeSku",goodsBasketCell.getGoodsBarCode())
                        .navigation();
            }
        });
        basketImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map nokmap = new HashMap<String, String>();
                nokmap.put("soure", "购物车中点击商品栏");
                MobclickAgent.onEvent(context, "btn_APP__MaternalandChildGoods_CommodityDetails", nokmap);

                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_DETAIL)
                        .withString("id", goodsBasketCell.goodsId)
                        .withString("barcodeSku",goodsBasketCell.getGoodsBarCode())
                        .navigation();
            }
        });
        checkMap.put(goodsBasketCell.cartDetailId + "", goodsBasketCell.ischeck);
        basketCheck.setOnCheckedChangeListener(new AutoFitCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(AutoFitCheckBox buttonView, boolean isChecked) {
                if (!buttonView.isPressed()) {
                    return;
                }
                if (isChecked) {
                    goodsBasketCell.ischeck = true;
                } else {
                    goodsBasketCell.ischeck = false;
                }
                checkMap.put(goodsBasketCell.cartDetailId + "", isChecked);
                if (onGoodsChangeListener != null) {
                    if (isChecked) {
                        onGoodsChangeListener.onGoodsAdd();
                    } else {
                        onGoodsChangeListener.onGoodsRemove();
                    }
                }
            }
        });
    }

//    /**
//     * 检测秒杀活动的提示
//     *
//     * @param num
//     * @param goodsBasketCell
//     * @param basketMoney
//     * @param limitCon
//     * @param islimitLineTextShow
//     * @param limitText
//     */
//    private void killgoodsCountChange(int num, GoodsBasketCell goodsBasketCell, TextView basketMoney, ConstraintLayout limitCon, boolean islimitLineTextShow, TextView limitText) {
//        if (goodsBasketCell.goodsMarketingDTO != null && "3".equals(goodsBasketCell.goodsMarketingDTO.marketingType)) {
//            if (num > goodsBasketCell.getMarkLimitMaxNowWithInventory() || num < goodsBasketCell.getMarkLimitMin()) {//说明超过限制了
//                basketMoney.setText("￥" + FormatUtils.moneyKeep2Decimals(goodsBasketCell.getCurGoodsAmount()) + "");
//                if (goodsBasketCell.getGoodsQuantity() > goodsBasketCell.getMarkLimitMaxNowWithInventory() || goodsBasketCell.getGoodsQuantity() < goodsBasketCell.getMarkLimitMin()) {
//                    limitCon.setVisibility(islimitLineTextShow ? View.VISIBLE : View.VISIBLE);
//                    if (goodsBasketCell.getMarkLimitMin() > goodsBasketCell.getMarkLimitMaxNowWithInventory()) {//起购大于可购
//                        limitCon.setVisibility(islimitLineTextShow ? View.INVISIBLE : View.GONE);
//
//                    } else {
//                        if (goodsBasketCell.getGoodsQuantity() > goodsBasketCell.getMarkLimitMaxNowWithInventory()) {//数量大于可够超了 可能是超过库存了 可能是超过限购了
//                            limitText.setText("购买" + goodsBasketCell.getMarkLimitMaxNowWithInventory() + "件及以下时享优惠！");
//                            if (goodsBasketCell.getMarkLimitMinOrg() > 0) {
//                                limitText.setText("购买" + goodsBasketCell.getMarkLimitMinOrg() + "-" + goodsBasketCell.getMarkLimitMaxNowWithInventory() + "件时享受优惠！");
//                                if (goodsBasketCell.getMarkLimitMin() == goodsBasketCell.getMarkLimitMaxNowWithInventory()) {//起购等于可够
//                                    limitText.setText("购买" + goodsBasketCell.getMarkLimitMinOrg() + "件时享优惠！");
//                                }
//                            }else {
//                                if(goodsBasketCell.getMarkLimitMinOrg()==0&&goodsBasketCell.getMarkLimitMaxNowWithInventory()==1){//不限制起购
//                                    limitText.setText("购买" + goodsBasketCell.getMarkLimitMaxNowWithInventory() + "件时享优惠！");
//                                }
//                            }
//                            if (goodsBasketCell.getHasBuy() > 0) {
//                                limitText.setText("您已购" + goodsBasketCell.getHasBuy() + "件，购买" + goodsBasketCell.getMarkLimitMinOrg() + "-" + goodsBasketCell.getMarkLimitMaxNowWithInventory() + "件时享优惠！");
//                                if (goodsBasketCell.getMarkLimitMin() == goodsBasketCell.getMarkLimitMaxNowWithInventory()) {//起购等于可够
//                                    limitText.setText("购买" + goodsBasketCell.getMarkLimitMinOrg() + "件时享优惠！");
//                                }
//                            }
//                        }
//                        if (goodsBasketCell.getGoodsQuantity() < goodsBasketCell.getMarkLimitMin() && goodsBasketCell.getMarkLimitMinOrg() > 0) {
//                            limitText.setText("购买" + goodsBasketCell.getMarkLimitMin() + "件及以上时享优惠！");
//                            if (goodsBasketCell.getMarkLimitMaxNowWithInventory() > 0) {
//                                limitText.setText("购买" + goodsBasketCell.getMarkLimitMinOrg() + "-" + goodsBasketCell.getMarkLimitMaxNowWithInventory() + "件时享受优惠！");
//                                if (goodsBasketCell.getMarkLimitMin() == goodsBasketCell.getMarkLimitMaxNowWithInventory()) {//起购等于可够
//                                    limitText.setText("购买" + goodsBasketCell.getMarkLimitMinOrg() + "件时享优惠！");
//                                }
//                            }
//                            if (goodsBasketCell.getHasBuy() > 0) {
//                                limitText.setText("您已购" + goodsBasketCell.getHasBuy() + "件，购买" + goodsBasketCell.getMarkLimitMinOrg() + "-" + goodsBasketCell.getMarkLimitMaxNowWithInventory() + "件时享优惠！");
//                                if (goodsBasketCell.getMarkLimitMin() == goodsBasketCell.getMarkLimitMaxNowWithInventory()) {//起购等于可够
//                                    limitText.setText("购买" + goodsBasketCell.getMarkLimitMinOrg() + "件时享优惠！");
//                                }
//                            }
//                        }
//                    }
//                } else {
//                    limitCon.setVisibility(islimitLineTextShow ? View.INVISIBLE : View.GONE);
//                }
//            }else {
//                limitCon.setVisibility(islimitLineTextShow ? View.INVISIBLE : View.GONE);
//            }
//        }
//    }


    private void initView() {



    }

    public List<GoodsBasketStore> getStoreList() {
        List<GoodsBasketStore> result=new ArrayList<>();
        for (int i = 0; i < getDatas().size(); i++) {
            result.add(getDatas().get(i));
        }
        return result;
    }

    public void setImageMap(Map<String, String> imageMap) {
        this.imageMap = imageMap;
    }

    public Map<String, String> getImageMap() {
        return imageMap;
    }
}
