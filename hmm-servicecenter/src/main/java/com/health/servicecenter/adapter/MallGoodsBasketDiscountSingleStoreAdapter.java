package com.health.servicecenter.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.health.servicecenter.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.model.ActVip;
import com.healthy.library.model.GoodsBasketCell;
import com.healthy.library.model.GoodsBasketGroup;
import com.healthy.library.model.GoodsBasketStore;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.widget.AutoFitCheckBox;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.ImageTextView;
import com.healthy.library.widget.IncreaseDecreaseView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 门店组
 */
public class MallGoodsBasketDiscountSingleStoreAdapter extends BaseAdapter<GoodsBasketStore> {

    Map<String, Boolean> checkMap = new HashMap<>();

    public List<ActVip.PopDetail> getPopDetails() {
        List<ActVip.PopDetail> PopDetails = new ArrayList<>();
        final GoodsBasketStore goodsBasketStore = getModel();
        for (int j = 0; j < goodsBasketStore.getGoodsBasketCellAllList().size(); j++) {
            GoodsBasketCell basketCell = goodsBasketStore.getGoodsBasketCellAllList().get(j);
            if (basketCell.ischeck) {
                PopDetails.add(new ActVip.PopDetail(basketCell));
            }
        }
        return PopDetails;
    }

    @Override
    public int getItemViewType(int position) {
        return 31;
    }

    public MallGoodsBasketDiscountSingleStoreAdapter() {
        this(R.layout.service_item_goodsbasket_store_single_discount);
    }

    private MallGoodsBasketDiscountSingleStoreAdapter(int viewId) {
        super(viewId);
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
        storeTop.setVisibility(View.GONE);
        discountUnder.setVisibility(View.GONE);
        GoodsBasketStore goodsBasketStore = getModel();
        passStoreName.setText(goodsBasketStore.shopName);
        goodsBasketStore.undogoodsStock();
        buildGoodsGroup(groupLL, goodsBasketStore.goodsBasketGroupList);
    }

    private void buildGoodsGroup(LinearLayout groupLL, List<GoodsBasketGroup> goodsBasketGroupList) {
        groupLL.removeAllViews();
        for (int i = 0; i < goodsBasketGroupList.size(); i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.service_item_goodsbasket_group_discount, groupLL, false);
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
        buildGoodsCenter(groupCenter, goodsBasketGroup,goodsBasketGroup.goodsBasketCellList, goodsBasketGroup.goodsBasketCellListGift.size() > 0, islastGroup);

    }

    private void buildGoodsCenter(LinearLayout groupCenter, GoodsBasketGroup goodsBasketGroup,List<GoodsBasketCell> goodsBasketCellList, boolean hasGift, boolean islastGroup) {
        groupCenter.removeAllViews();
        for (int i = 0; i < goodsBasketCellList.size(); i++) {
            View view;
            view = LayoutInflater.from(context).inflate(R.layout.service_item_goodsbasket_discount, groupCenter, false);
            TextView limitLineText = view.findViewById(R.id.limitLineText);
            limitLineText.setVisibility(i == goodsBasketCellList.size() - 1 && !hasGift && !islastGroup ? View.VISIBLE : View.INVISIBLE);
            View basketActLL = view.findViewById(R.id.basketActLL);
            buildGoodsCenterChild(goodsBasketCellList.get(i),goodsBasketGroup, view, i == goodsBasketCellList.size() - 1 && !hasGift && !islastGroup);
            if (i != 0) {
                basketActLL.setVisibility(View.GONE);
            }
            groupCenter.addView(view);
        }
    }

    private void buildGoodsCenterChild(final GoodsBasketCell goodsBasketCell, final GoodsBasketGroup goodsBasketGroup, View view, final boolean islimitLineTextShow) {
        final AutoFitCheckBox basketCheck;
        CornerImageView basketImg;
        TextView basketTitle;
        TextView basketSku;
        final TextView basketMoney;
        final IncreaseDecreaseView increaseDecrease;
        final ConstraintLayout basketActLL;
        TextView basketFlag;
        TextView basketFlagT;
        TextView basketFlagV;
        ImageTextView basketFlagButton;
        LinearLayout timeLiner;
        TextView basketFlagTT;
        final ConstraintLayout limitCon;
        final TextView limitText;
        limitCon = (ConstraintLayout) view.findViewById(R.id.limitCon);
        limitText = (TextView) view.findViewById(R.id.limitText);
        timeLiner = (LinearLayout) view.findViewById(R.id.timeLiner);
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
//                if ("-2".equals(goodsBasketCell.goodsMarketingDTO.marketingType)) {//砍价
//
//                } else {
//                    basketActLL.setVisibility(View.GONE);
//                }
                basketFlagT.setVisibility(View.VISIBLE);
                basketFlag.setText(goodsBasketCell.popInfo.PopLabelName);
                basketFlagT.setText(goodsBasketCell.popInfo.PopDesc);
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
        com.healthy.library.businessutil.GlideCopy.with(context)
                .load(goodsBasketCell.goodsImage)
                .placeholder(R.drawable.img_1_1_default)
                .error(R.drawable.img_1_1_default)

                .into(basketImg);
        basketTitle.setText(goodsBasketCell.goodsTitle);
        basketSku.setText(goodsBasketCell.goodsSpecDesc == null ? "" : goodsBasketCell.goodsSpecDesc.replace("无规格，默认值", ""));
        basketMoney.setText("￥" + FormatUtils.moneyKeep2Decimals(goodsBasketCell.getCurGoodsAmountInBasket()) + "");
        basketCheck.setChecked(goodsBasketCell.ischeck);
        increaseDecrease.setMinCount(1);
        increaseDecrease.setMaxCount(goodsBasketCell.getStockInBasketUnderLine());
        increaseDecrease.setNoCount(goodsBasketCell.getGoodsQuantityInBasket());
//        increaseDecrease.setCheckTextDialog(true);
        increaseDecrease.setOnNumChangedListener(new IncreaseDecreaseView.OnNumChangedListener() {
            @Override
            public void onNumChanged(int num) {
                int oldnumber = goodsBasketCell.getGoodsQuantity();
                goodsBasketCell.setGoodsQuantity(num);
                String result = goodsBasketGroup.checkPopInfoCountLimit(goodsBasketCell);
                if (!TextUtils.isEmpty(result)) {
                    Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
                    goodsBasketCell.setGoodsQuantity(oldnumber);
                    increaseDecrease.setNoCount(oldnumber);
                }
                if (moutClickListener != null) {
                    moutClickListener.outClick("展示底部", null);
                }

            }
        });
        if("-1".equals(goodsBasketCell.getGoodsMarketingTypeOrg())){
            increaseDecrease.setChangeVisable(true);
        }else if("-2".equals(goodsBasketCell.getGoodsMarketingTypeOrg())){
            increaseDecrease.setChangeVisable(false);
        }else {
            increaseDecrease.setChangeVisable(false);
        }
        basketTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        basketImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        basketCheck.setOnCheckedChangeListener(new AutoFitCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(AutoFitCheckBox buttonView, boolean isChecked) {
                if (!buttonView.isPressed()) {
                    return;
                }
                if (isChecked) {
                    String result = goodsBasketGroup.checkPopInfoCountLimit(goodsBasketCell);
                    if (TextUtils.isEmpty(result)) {
                        goodsBasketCell.ischeck = true;
                    } else {
                        Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
                        goodsBasketCell.ischeck = false;
                        basketCheck.setChecked(false);
                    }
                } else {
                    goodsBasketCell.ischeck = false;
                }
                if (moutClickListener != null) {
                    moutClickListener.outClick("展示底部", null);
                }

            }
        });
    }

    private void initView() {
    }
}
