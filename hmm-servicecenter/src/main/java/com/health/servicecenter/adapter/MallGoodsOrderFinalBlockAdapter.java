package com.health.servicecenter.adapter;

import android.graphics.Color;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.servicecenter.R;
import com.health.servicecenter.activity.StoreBlockChildHolder;
import com.health.servicecenter.contract.ServiceOrderShopContract;
import com.health.servicecenter.presenter.ServiceGoodsOrderShopPresenter;
import com.health.servicecenter.widget.GoodsPickDateDialog;
import com.health.servicecenter.widget.ShopOrderPickDialog;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.builder.ObjectIteraor;
import com.healthy.library.builder.SimpleArrayListBuilder;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.businessutil.GlideCopy;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.ActVip;
import com.healthy.library.model.GoodsBasketCell;
import com.healthy.library.model.GoodsBasketStore;
import com.healthy.library.model.GoodsFee;
import com.healthy.library.model.GoodsShop;
import com.healthy.library.model.ShopDetailModel;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.SpanUtils;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.IncreaseDecreaseView;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;
import com.umeng.analytics.MobclickAgent;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.disposables.Disposable;

public class MallGoodsOrderFinalBlockAdapter extends BaseAdapter<GoodsBasketStore> implements ServiceOrderShopContract.View {


    public StoreBlockChildHolder storeBlockChildHolderNow;
    public GoodsBasketStore goodsBasketStoreNow;
    GoodsPickDateDialog goodsPickDateDialog;
    ShopOrderPickDialog shopOrderPickDialog;
    String cellGoodsMarketingType;

    String visitShopId;
    String bargainId;
    String bargainMemberId;
    String bargainMoney;
    //-----------------------------------------------------
    //??????
    String assembleId;
    String teamNum;
    String assemblePrice;
    //-----------------------------------------------------
    String packageMoney;
    String packageId;
    String packageQuantity;
    //-----------------------------------------------------
    String prizeName;
    String activityName;
    //-----------------------------------------------------
    String type = "0";
    String race = "0";
    private String isNtReal;
    private TextView mRanking;
    private TextView mMessageTip;


    private Map<String, String> imageMap;
    public String mShopPhone;
    private String totalAmount;
    private ActVip actVip;


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
                         String race,
                         String visitShopId,
                         String cellGoodsMarketingType,
                         String prizeName,
                         String activityName,ActVip actVip) {
        this.bargainId = bargainId;
        this.bargainMemberId = bargainMemberId;
        this.bargainMoney = bargainMoney;
        this.assembleId = assembleId;
        this.teamNum = teamNum;
        this.assemblePrice = assemblePrice;
        this.packageMoney = packageMoney;
        this.packageId = packageId;
        this.packageQuantity = packageQuantity;
        this.type = type;
        this.race = race;
        this.visitShopId = visitShopId;
        this.cellGoodsMarketingType = cellGoodsMarketingType;
        this.prizeName = prizeName;
        this.activityName = activityName;
        this.actVip = actVip;
    }

    @Override
    public int getItemViewType(int position) {
        return 41;
    }

    public MallGoodsOrderFinalBlockAdapter() {
        this(R.layout.service_activity_goodsorder_group);
    }

    private MallGoodsOrderFinalBlockAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull final BaseHolder baseHolder, int i) {
        final GoodsBasketStore goodsBasketStore = getDatas().get(i);
        final StoreBlockChildHolder storeBlockChildHolder = new StoreBlockChildHolder() {
            @Override
            public View getView() {
                return baseHolder.itemView;
            }
        };
        baseHolder.itemView.findViewById(R.id.passToPointEnd).setVisibility(View.GONE);
        if ("5".equals(type)) {
            baseHolder.itemView.findViewById(R.id.passToPointEnd).setVisibility(View.VISIBLE);
        }
        baseHolder.itemView.findViewById(R.id.checkGroup).setVisibility(View.VISIBLE);
        baseHolder.itemView.findViewById(R.id.toStoreOnly).setVisibility(View.GONE);
        if (!TextUtils.isEmpty(packageMoney)) {
            baseHolder.itemView.findViewById(R.id.toStoreOnly).setVisibility(View.VISIBLE);
            baseHolder.itemView.findViewById(R.id.checkGroup).setVisibility(View.GONE);
        }

        if ("9".equals(cellGoodsMarketingType) || "-4".equals(cellGoodsMarketingType)) {
            ConstraintLayout mPrizeLayout = baseHolder.getView(R.id.prizeLayout);
            mRanking = baseHolder.getView(R.id.ranking);
            mMessageTip = baseHolder.getView(R.id.messageTip);
            RadioGroup mCheckGroup = baseHolder.getView(R.id.checkGroup);
            mPrizeLayout.setVisibility(View.VISIBLE);
            mCheckGroup.setVisibility(View.GONE);

            mRanking.setText(prizeName);
            mMessageTip.setText(context.getResources().getString(R.string.share_winning_jackpot, activityName));
        }
        if (goodsBasketStore.supportNeedTime){
            storeBlockChildHolder.toStoreTime.setVisibility(View.VISIBLE);
            storeBlockChildHolder.passToStoreTime.setVisibility(View.VISIBLE);
        }else {
            storeBlockChildHolder.toStoreTime.setVisibility(View.GONE);
            storeBlockChildHolder.passToStoreTime.setVisibility(View.GONE);
        }
        buildStoreBlock(baseHolder.itemView, goodsBasketStore, getItemCount() == 1);

    }

    private void buildStoreBlock(final View view, final GoodsBasketStore goodsBasketStore, final boolean isOnlyOneStore) {
        final StoreBlockChildHolder storeBlockChildHolder = new StoreBlockChildHolder() {
            @Override
            public View getView() {
                return view;
            }
        };
        if (goodsBasketStore.shopType == 1) {

        } else {

        }
        goodsBasketStore.setOnItemChange(new GoodsBasketStore.OnItemChange() {
            @Override
            public void bitNice() {
                storeBlockChildHolder.toStoreName.setText(goodsBasketStore.goodsPickShop.shopName);
                storeBlockChildHolder.toStoreAddress.setText(goodsBasketStore.goodsPickShop.getShopAddressDetail());
                onSucessGetPickShop(null, goodsBasketStore, storeBlockChildHolder);
                //???????????????????????????
                buildStoreBlock(((StoreBlockChildHolder) goodsBasketStore.storeBlockChildHolder).getView(), goodsBasketStore, getItemCount() == 1);

            }
        });
        goodsBasketStore.setOnFeeChange(new GoodsBasketStore.OnItemChange() {//??????????????????
            @Override
            public void bitNice() {
                buildCheckResult(goodsBasketStore.checkedId, goodsBasketStore, storeBlockChildHolder);
            }
        });

        if (goodsBasketStore.storeBlockChildHolder != null) {
            if (moutClickListener != null) {
                moutClickListener.outClick("getNowAllCouponInfoList", null);
            }
            if (moutClickListener != null) {
                moutClickListener.outClick("buildCouponWithNoDialog", null);
            }
            if (moutClickListener != null) {
                moutClickListener.outClick("buildNowPayMoney", null);
            }
        }
        goodsBasketStore.storeBlockChildHolder = storeBlockChildHolder;
        ServiceGoodsOrderShopPresenter serviceGoodsOrderShopPresenter = new ServiceGoodsOrderShopPresenter(context, this);
        //????????????????????????????????????
//        serviceGoodsOrderShopPresenter.getPickShopOnly(new SimpleHashMapBuilder<String, Object>()
//                .puts("mchId", goodsBasketStore.getMchId())
//                .puts("details", goodsBasketStore.getDetails())
//                .puts("deliverType", "11")
//                .puts("lat", LocUtil.getLatitude(mContext, SpKey.LOC_ORG))
//                .puts("lng", LocUtil.getLongitude(mContext, SpKey.LOC_ORG)), goodsBasketStore, storeBlockChildHolder);

//        if(!"-2".equals(cellGoodsMarketingType)&&!"0".equals(cellGoodsMarketingType)){//??????-2?????? ??????????????????????????? ?????????????????????????????????????????????????????? ?????????????????????????????????????????????
//                goodsBasketStore.removeUnderGift();
//                System.out.println("?????????????????????????????????s:"+goodsBasketStore.getGoodsBasketCellAllExpCardList().size());
//        }
        if ("10".equals(goodsBasketStore.deliver.deliveryType) || "20".equals(goodsBasketStore.deliver.deliveryType)) {
            if (goodsBasketStore.checkAllIsService()) {//?????? ??????????????????
                //////System.out.println("??????????????????????????????");
                goodsBasketStore.notcheck = R.id.checkB;//???????????????????????????A
                goodsBasketStore.msg = "?????????????????????????????????";
                storeBlockChildHolder.checkA.setChecked(true);
                buildCheckResult(R.id.checkA, goodsBasketStore, storeBlockChildHolder);
                MobclickAgent.onEvent(context, "event2APPOrderConfirmDeliveryHomeNoSupport", new SimpleHashMapBuilder<String, String>().puts("soure", "???????????????-??????????????????????????????"));
            } else {
                serviceGoodsOrderShopPresenter.getFeeOnly(new SimpleHashMapBuilder<String, Object>()
                        .puts("shopId", goodsBasketStore.goodsPickShop.shopId)
                        .puts("amount", goodsBasketStore.getgCurPrice(bargainMoney))
                        .puts("lat", LocUtil.getLatitude(context, SpKey.LOC_ORG))
                        .puts("lng", LocUtil.getLongitude(context, SpKey.LOC_ORG)), goodsBasketStore, storeBlockChildHolder);

            }
        }
        if (goodsBasketStore.goodsPickShop.shopId.equals(goodsBasketStore.getGoodsBasketCellAllList().get(0).goodsShopId)) {//???????????????????????????????????????
            serviceGoodsOrderShopPresenter.getShopDetailOnly(new SimpleHashMapBuilder<String, Object>()
                    .puts("shopId", goodsBasketStore.goodsPickShop.shopId)
                    .puts("latitude", LocUtil.getLatitude(context, SpKey.LOC_ORG))
                    .puts("longitude", LocUtil.getLongitude(context, SpKey.LOC_ORG)), goodsBasketStore, storeBlockChildHolder);
        }

        if (goodsBasketStore.supportOtherdeliveryShop && !"5".equals(type) && !"5".equals(race) && !"3".equals(race)) {//???????????????????????????????????????
            if ("-4".equals(cellGoodsMarketingType) || "-5".equals(cellGoodsMarketingType) || "9".equals(cellGoodsMarketingType)) {//???????????????
                serviceGoodsOrderShopPresenter.getPickShop(new SimpleHashMapBuilder<String, Object>()
                        .puts("shopId", visitShopId)
                        .puts("shopTypeList", "1,2,3".split(","))
                        .puts("latitude", LocUtil.getLatitude(context, SpKey.LOC_ORG))
                        .puts("longitude", LocUtil.getLongitude(context, SpKey.LOC_ORG)), goodsBasketStore, storeBlockChildHolder);

            } else {
                if ("-1".equals(goodsBasketStore.goodsCategoryFirstId)) {
                    serviceGoodsOrderShopPresenter.getPickShop(new SimpleHashMapBuilder<String, Object>()
                            .puts("shopId", visitShopId)
                            .puts("shopTypeList", "2,3".split(","))
                            .puts("latitude", LocUtil.getLatitude(context, SpKey.LOC_ORG))
                            .puts("longitude", LocUtil.getLongitude(context, SpKey.LOC_ORG)), goodsBasketStore, storeBlockChildHolder);
                }
            }

        }


        storeBlockChildHolder.passToStore.setVisibility(View.GONE);
        storeBlockChildHolder.canReceivePass.setVisibility(View.GONE);
        if (goodsBasketStore.supportOtherdeliveryShop && !"5".equals(type) && !"5".equals(race) && !"3".equals(race)) {//???????????????????????????????????????
            if ("-4".equals(cellGoodsMarketingType) || "-5".equals(cellGoodsMarketingType) || "9".equals(cellGoodsMarketingType)) {//???????????????
                storeBlockChildHolder.passToStore.setVisibility(View.VISIBLE);
                storeBlockChildHolder.canReceivePass.setVisibility(View.VISIBLE);
            } else {
                if ("-1".equals(goodsBasketStore.goodsCategoryFirstId)) {
                    storeBlockChildHolder.passToStore.setVisibility(View.VISIBLE);
                    storeBlockChildHolder.canReceivePass.setVisibility(View.VISIBLE);
                }
            }


        }
        storeBlockChildHolder.toStoreAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShopPick(goodsBasketStore, storeBlockChildHolder);

            }
        });
        storeBlockChildHolder.toStoreName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShopPick(goodsBasketStore, storeBlockChildHolder);

            }
        });
        storeBlockChildHolder.passToStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShopPick(goodsBasketStore, storeBlockChildHolder);

            }
        });
        storeBlockChildHolder.addressShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShopReceive(goodsBasketStore, storeBlockChildHolder);
            }
        });
        storeBlockChildHolder.backDetail.setText(goodsBasketStore.deliver.remark);

        storeBlockChildHolder.backDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBackView(storeBlockChildHolder.backDetail, goodsBasketStore);
            }
        });
        storeBlockChildHolder.checkGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.checkA) {
                    if (goodsBasketStore.notcheck == R.id.checkA) {
                        storeBlockChildHolder.checkB.setChecked(true);
                        Toast.makeText(context, "?????????????????????", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        //////System.out.println("????????????");
                        buildCheckResult(checkedId, goodsBasketStore, storeBlockChildHolder);
//                        goodsBasketStore.changeItem();
                    }
                }
                if (checkedId == R.id.checkB) {
                    if (goodsBasketStore.notcheck == R.id.checkB) {
                        MobclickAgent.onEvent(context, "event2APPOrderConfirmDeliveryHomeClick", new SimpleHashMapBuilder<String, String>().puts("soure", "???????????????-?????????????????????"));
                        storeBlockChildHolder.checkA.setChecked(true);
                        Toast.makeText(context, "" + goodsBasketStore.msg + "???????????????~", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        //////System.out.println("????????????2");
                        buildCheckResult(checkedId, goodsBasketStore, storeBlockChildHolder);
//                        goodsBasketStore.changeItem();
                    }
                }
            }
        });
        buildCheckResult(goodsBasketStore.checkedId, goodsBasketStore, storeBlockChildHolder);
        storeBlockChildHolder.toStoreTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (goodsPickDateDialog == null) {
                    goodsPickDateDialog = GoodsPickDateDialog.newInstance();
                } else {
                    if (goodsPickDateDialog != null) {

                        goodsPickDateDialog.dismiss();
                    }
                    goodsPickDateDialog = GoodsPickDateDialog.newInstance();
                }
                goodsPickDateDialog.show(((BaseActivity) context).getSupportFragmentManager(), "????????????");

                goodsPickDateDialog.setOnDialogDateClickListener(new GoodsPickDateDialog.OnDialogDateClickListener() {
                    @Override
                    public void onDialogDateClick(String day, String time) {
                        goodsBasketStore.deliver.deliveryDate = day;
                        goodsBasketStore.deliver.deliveryTime = time;
                        storeBlockChildHolder.toStoreTime.setText(goodsBasketStore.deliver.deliveryDate + " " + goodsBasketStore.deliver.deliveryTime);

                    }
                });
            }
        });


        if (true) {
//            storeBlockChildHolder.goodOrderSTop.setBackgroundResource(R.drawable.ic_no);
            storeBlockChildHolder.goodOrderSTop.setBackgroundResource(R.drawable.shape_mall_goods_ol_vid_float);
            if (goodsBasketStore.getGoodsBasketCellAllExpCardList(cellGoodsMarketingType).size() > 3) {
                storeBlockChildHolder.underviewll.setVisibility(View.VISIBLE);
//                buildStoreBlockOnlyOne(goodsBasketStore, storeBlockChildHolder.goodsListLL, 3, isOnlyOneStore, goodsBasketStore.getGoodsBasketCellAllExpCardList().size() == 1, storeBlockChildHolder);
                storeBlockChildHolder.underview.setText("????????????" + (goodsBasketStore.getGoodsBasketCellAllExpCardList(cellGoodsMarketingType).size() - 3) + "?????????");
                storeBlockChildHolder.underviewll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (storeBlockChildHolder.underview.getText().toString().contains("??????")) {
                            storeBlockChildHolder.underview.setText("????????????" + (goodsBasketStore.getGoodsBasketCellAllExpCardList(cellGoodsMarketingType).size() - 3) + "?????????");
                            storeBlockChildHolder.underview.setDrawable(R.drawable.goods_arrow_down, context);
                            buildStoreBlockOnlyOne(goodsBasketStore, storeBlockChildHolder.goodsListLL, 3, isOnlyOneStore, goodsBasketStore.getGoodsBasketCellAllExpCardList(cellGoodsMarketingType).size() == 1, storeBlockChildHolder);

                        } else {
                            storeBlockChildHolder.underview.setText("??????");
                            buildStoreBlockOnlyOne(goodsBasketStore, storeBlockChildHolder.goodsListLL, goodsBasketStore.getGoodsBasketCellAllExpCardList(cellGoodsMarketingType).size(), isOnlyOneStore, goodsBasketStore.getGoodsBasketCellAllExpCardList(cellGoodsMarketingType).size() == 1, storeBlockChildHolder);
                            storeBlockChildHolder.underview.setDrawable(R.drawable.goods_arrow_up, context);
                        }
                    }
                });
                if (storeBlockChildHolder.underview.getText().toString().contains("??????")) {
                    storeBlockChildHolder.underview.setText("??????");
                    buildStoreBlockOnlyOne(goodsBasketStore, storeBlockChildHolder.goodsListLL, goodsBasketStore.getGoodsBasketCellAllExpCardList(cellGoodsMarketingType).size(), isOnlyOneStore, goodsBasketStore.getGoodsBasketCellAllExpCardList(cellGoodsMarketingType).size() == 1, storeBlockChildHolder);
                    storeBlockChildHolder.underview.setDrawable(R.drawable.goods_arrow_up, context);
                } else {
                    storeBlockChildHolder.underview.setText("????????????" + (goodsBasketStore.getGoodsBasketCellAllExpCardList(cellGoodsMarketingType).size() - 3) + "?????????");
                    storeBlockChildHolder.underview.setDrawable(R.drawable.goods_arrow_down, context);
                    buildStoreBlockOnlyOne(goodsBasketStore, storeBlockChildHolder.goodsListLL, 3, isOnlyOneStore, goodsBasketStore.getGoodsBasketCellAllExpCardList(cellGoodsMarketingType).size() == 1, storeBlockChildHolder);
                }
            } else {
                buildStoreBlockOnlyOne(goodsBasketStore, storeBlockChildHolder.goodsListLL, goodsBasketStore.getGoodsBasketCellAllExpCardList(cellGoodsMarketingType).size(), isOnlyOneStore, goodsBasketStore.getGoodsBasketCellAllExpCardList(cellGoodsMarketingType).size() == 1, storeBlockChildHolder);
            }
        } else {
            storeBlockChildHolder.goodOrderSTop.setBackgroundResource(R.drawable.shape_mall_goods_ol_vid_float);
            buildStoreBlockMore(goodsBasketStore, storeBlockChildHolder.goodsListLL);
        }


    }

    private View buildStoreBlockOnlyOneItem(LinearLayout setP, GoodsBasketCell goodsBasketCell) {
        View view = LayoutInflater.from(context).inflate(R.layout.service_activity_goodsorder_group_splash_item, setP, false);
        ImageView goodsImg = view.findViewById(R.id.goodsImg);
        TextView pointLable = (TextView) view.findViewById(R.id.pointLable);
        ImageView actFlag = (ImageView) view.findViewById(R.id.actFlag);
        GlideCopy.with(context)
                .load(goodsBasketCell.goodsImage)
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_1_1_default)

                .into(goodsImg);
        pointLable.setVisibility(View.GONE);
        actFlag.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(bargainId) && "1".equals(goodsBasketCell.getGoodsMarketingType())) {
            actFlag.setVisibility(View.VISIBLE);
            actFlag.setImageResource(R.drawable.act_kick);
        } else if (!TextUtils.isEmpty(assembleId) && "2".equals(goodsBasketCell.getGoodsMarketingType())) {
            actFlag.setVisibility(View.VISIBLE);
            actFlag.setImageResource(R.drawable.act_pin);
        } else if ("3".equals(goodsBasketCell.getGoodsMarketingType())) {
            actFlag.setVisibility(View.VISIBLE);
            actFlag.setImageResource(R.drawable.act_kill);
        } else if ("2".equals(goodsBasketCell.getGoodsMarketingType())) {
            actFlag.setVisibility(View.VISIBLE);
            actFlag.setImageResource(R.drawable.act_pin);
        } else if ("1".equals(goodsBasketCell.getGoodsMarketingType())) {
            actFlag.setVisibility(View.VISIBLE);
            actFlag.setImageResource(R.drawable.act_kick);
        } else if (goodsBasketCell.isGift) {
            pointLable.setVisibility(View.VISIBLE);
            pointLable.setText("??????");
            pointLable.setAlpha(0.5f);
        } else if ("5".equals(goodsBasketCell.getGoodsMarketingType())) {
            pointLable.setVisibility(View.VISIBLE);
            pointLable.setText("????????????");
            pointLable.setAlpha(0.5f);
        }
        return view;
    }

    private void buildStoreBlockMore(GoodsBasketStore goodsBasketStore, LinearLayout goodsListLL) {
        goodsListLL.removeAllViews();
        View view = LayoutInflater.from(context).inflate(R.layout.service_activity_goodsorder_group_splash, goodsListLL, false);
        LinearLayout setP = view.findViewById(R.id.setP);
        TextView goodsCount = view.findViewById(R.id.goodsCount);
        goodsCount.setText("???" + goodsBasketStore.getGoodsBasketCellAllList().size() + "?????????");
        setP.removeAllViews();
        for (int i = 0; i < goodsBasketStore.getGoodsBasketCellAllList().size(); i++) {
            GoodsBasketCell goodsBasketCell = goodsBasketStore.getGoodsBasketCellAllList().get(i);
            setP.addView(buildStoreBlockOnlyOneItem(setP, goodsBasketCell));
        }
        goodsListLL.addView(view);
    }

    private void buildStoreBlockOnlyOneData(final GoodsBasketStore goodsBasketStore, final boolean isOnlyOneStore, final boolean isOnlyOneItem, final StoreBlockChildHolder storeBlockChildHolder, final GoodsBasketCell goodsBasketCell, final View view) {
        ConstraintLayout top;
        CornerImageView goodsImg;
        TextView goodsTitle;
        TextView goodsMoney;
        TextView goodsSpec;
        IncreaseDecreaseView increaseDecrease;
        TextView goodsCount;
        ConstraintLayout goodsFinishBtnConstraintLayout;
        TextView btnCommitBack;
        TextView btnAssess;
        TextView goodsGiftOtherTip;
        TextView goodsOtherTip;
        TextView goodsMoneyChange;
        LinearLayout actLL;
        goodsGiftOtherTip = (TextView) view.findViewById(R.id.goodsGiftOtherTip);
        goodsOtherTip = (TextView) view.findViewById(R.id.goodsOtherTip);
        top = (ConstraintLayout) view.findViewById(R.id.top);
        goodsImg = (CornerImageView) view.findViewById(R.id.goodsImg);
        goodsMoneyChange = (TextView) view.findViewById(R.id.goodsMoneyChange);
        TextView pointLable = (TextView) view.findViewById(R.id.pointLable);
        ImageView actFlag = (ImageView) view.findViewById(R.id.actFlag);
        goodsTitle = (TextView) view.findViewById(R.id.goodsTitle);
        goodsMoney = (TextView) view.findViewById(R.id.goodsMoney);
        goodsSpec = (TextView) view.findViewById(R.id.goodsSpec);
        increaseDecrease = (IncreaseDecreaseView) view.findViewById(R.id.increase_decrease);
        goodsCount = (TextView) view.findViewById(R.id.goodsCount);
        goodsFinishBtnConstraintLayout = (ConstraintLayout) view.findViewById(R.id.goods_finish_btn_constraintLayout);
        btnCommitBack = (TextView) view.findViewById(R.id.btn_commitBack);
        btnAssess = (TextView) view.findViewById(R.id.btn_assess);
        actLL = (LinearLayout) view.findViewById(R.id.actLL);
        increaseDecrease.setVisibility(View.GONE);
        final ServiceGoodsOrderShopPresenter serviceGoodsOrderShopPresenter = new ServiceGoodsOrderShopPresenter(context, this);
        //////System.out.println("????????????????????????");
        boolean hasFocus = false;//???????????????????????????????????????
        goodsBasketCell.setOnItemChange(new GoodsBasketCell.OnItemChange() {
            @Override
            public void bitNice() {
                buildStoreBlockOnlyOneData(goodsBasketStore, isOnlyOneStore, isOnlyOneItem, storeBlockChildHolder, goodsBasketCell, view);
            }
        });
        {
//            if (actVip != null && actVip.PopInfo != null && actVip.PopInfo.size() > 0) {//???????????????
//                showActList(actLL, actVip);
//            }
            if (!goodsBasketCell.isInit) {//?????????????????? ??????????????????????????????????????????
                goodsBasketCell.isInit = true;
                if ("-4".equals(goodsBasketCell.getGoodsMarketingTypeOrgExpUnder())
                        || "-5".equals(goodsBasketCell.getGoodsMarketingTypeOrgExpUnder())
                        || "9".equals(goodsBasketCell.getGoodsMarketingTypeOrgExpUnder())
                        || "-1".equals(goodsBasketCell.getGoodsMarketingTypeOrgExpUnder())
                ) {
                    if(goodsBasketStore.goodsPickShopList!=null&&goodsBasketStore.goodsPickShopList.size()>0){//????????????????????? ????????????????????????????????????
                        if(!ListUtil.checkObjIsInList(new SimpleArrayListBuilder<String>().putList(goodsBasketStore.goodsPickShopList, new ObjectIteraor<GoodsShop>() {
                            @Override
                            public String getDesObj(GoodsShop o) {
                                return o.shopId;
                            }
                        }),goodsBasketCell.goodsShopId)){//?????????????????? ??????????????? ?????????????????????
                            goodsBasketCell.isInit=false;
                            goodsBasketCell.goodsShopId=goodsBasketStore.goodsPickShopList.get(0).shopId;

                        }
                    }
                    hasFocus = true;
                    serviceGoodsOrderShopPresenter.getSpecDetail(cellGoodsMarketingType, new SimpleHashMapBuilder<String, Object>()
                                    .puts("shopId", goodsBasketStore.goodsPickShop.shopId)
                                    .puts("goodsId", goodsBasketCell.goodsId)
                                    .puts("goodsChildId", goodsBasketCell.getGoodsSpecIdOrg())
                                    .puts("mapMarketingGoodsId", goodsBasketCell.getGoodsMarketingGoodsIdOrg())
                                    .puts("marketingType", goodsBasketCell.getGoodsMarketingTypeOrgExpUnder())
                                    .puts("mapMarketingGoodsChildId", goodsBasketCell.getGoodsMarketingGoodsSpecOrg())
                            , goodsBasketStore, goodsBasketCell, new GoodsBasketCell.OnItemChange() {
                                @Override
                                public void bitNice() {
                                    if (goodsBasketCell.extraGoodsBasketCell != null) {//??????????????????id
                                        goodsBasketCell.resetGoodsBasketCell(goodsBasketCell.extraGoodsBasketCell);
                                    }
                                    if(goodsBasketStore.goodsPickShopList!=null&&goodsBasketStore.goodsPickShopList.size()>0){//????????????????????? ????????????????????????????????????
                                        if(!ListUtil.checkObjIsInList(new SimpleArrayListBuilder<String>().putList(goodsBasketStore.goodsPickShopList, new ObjectIteraor<GoodsShop>() {
                                            @Override
                                            public String getDesObj(GoodsShop o) {
                                                return o.shopId;
                                            }
                                        }),goodsBasketCell.goodsShopId)){//?????????????????? ??????????????? ?????????????????????
                                            goodsBasketCell.goodsShopId=goodsBasketStore.goodsPickShopList.get(0).shopId;
                                            if(goodsBasketCell.errorCount<1){//?????????????????????????????? ??????????????????
                                                goodsBasketCell.isInit=false;
                                                goodsBasketCell.errorCount++;
                                                goodsBasketCell.changeItem();
                                            }

                                        }
                                    }
                                    goodsBasketCell.isInit = true;
                                    if (moutClickListener != null) {
                                        moutClickListener.outClick("buildCouponChange", goodsBasketCell);
                                    }
                                    serviceGoodsOrderShopPresenter.getFeeOnly(new SimpleHashMapBuilder<String, Object>()
                                            .puts("shopId", goodsBasketStore.goodsPickShop.shopId)
                                            .puts("amount", goodsBasketStore.getgCurPrice(bargainMoney))
                                            .puts("lat", LocUtil.getLatitude(context, SpKey.LOC_ORG))
                                            .puts("lng", LocUtil.getLongitude(context, SpKey.LOC_ORG)), goodsBasketStore, storeBlockChildHolder);

                                }
                            });
                    return;
                }
            } else {
                if (goodsBasketCell.getGoodsShopIdOrg().equals(goodsBasketStore.goodsPickShop.shopId)) {
                    if (goodsBasketCell.extraGoodsBasketCell != null) {
                        goodsBasketCell.setExtraGoodsBasketCell(null);
                        if (moutClickListener != null) {
                            moutClickListener.outClick("buildCouponChange", goodsBasketCell);
                        }
                        if (moutClickListener != null) {
                            moutClickListener.outClick("buildActChange", goodsBasketCell);
                        }
                        System.out.println("???????????????1");
                        serviceGoodsOrderShopPresenter.getFeeOnly(new SimpleHashMapBuilder<String, Object>()
                                .puts("shopId", goodsBasketStore.goodsPickShop.shopId)
                                .puts("amount", goodsBasketStore.getgCurPrice(bargainMoney))
                                .puts("lat", LocUtil.getLatitude(context, SpKey.LOC_ORG))
                                .puts("lng", LocUtil.getLongitude(context, SpKey.LOC_ORG)), goodsBasketStore, storeBlockChildHolder);
                    }

                } else {
                    if (goodsBasketCell.extraGoodsBasketCell == null || !goodsBasketCell.extraGoodsBasketCell.getGoodsShopId().equals(goodsBasketStore.goodsPickShop.shopId)) {
//                    goodsBasketCell.setExtraGoodsBasketCell(null);//???????????????????????????getGoodsMarketingTypeOrg ??????????????????
                        hasFocus = true;//????????????????????? ??????????????? ex???????????? ??????ex???????????????????????????????????????
                        //////System.out.println("????????????????????????2");
                        serviceGoodsOrderShopPresenter.setIsNtReal(isNtReal);

                        serviceGoodsOrderShopPresenter.getSpecDetail(cellGoodsMarketingType, new SimpleHashMapBuilder<String, Object>()
                                        .puts("shopId", goodsBasketStore.goodsPickShop.shopId)
                                        .puts("goodsId", goodsBasketCell.goodsId)
                                        .puts("goodsChildId", goodsBasketCell.getGoodsSpecIdOrg())
                                        .puts("mapMarketingGoodsId", goodsBasketCell.getGoodsMarketingGoodsIdOrg())
                                        .puts("marketingType", goodsBasketCell.getGoodsMarketingTypeOrgExpUnder())
                                        .puts("mapMarketingGoodsChildId", goodsBasketCell.getGoodsMarketingGoodsSpecOrg())
                                , goodsBasketStore, goodsBasketCell, new GoodsBasketCell.OnItemChange() {
                                    @Override
                                    public void bitNice() {
                                        goodsBasketCell.extraGoodsBasketCell.goodsShopId = goodsBasketStore.goodsPickShop.shopId;
                                        goodsBasketCell.extraGoodsBasketCell.setIsSupportOverSold(goodsBasketStore.goodsPickShop.isSupportOverSold);
                                        if (goodsBasketCell.extraGoodsBasketCell != null) {
                                            if ("6".equals(goodsBasketCell.extraGoodsBasketCell.getGoodsMarketingTypeOrg()) || "7".equals(goodsBasketCell.extraGoodsBasketCell.getGoodsMarketingTypeOrg())) {
                                                //???????????????????????????
                                            }
                                        }
                                        goodsBasketCell.undo();
                                        int orgGoodsMarktype = goodsBasketCell.getGoodsMarketingTypeInt();
                                        goodsBasketCell.redo();
                                        if (orgGoodsMarktype == 6 || orgGoodsMarktype == 7) {//???????????????6??????7 ?????????????????????
                                            if (moutClickListener != null) {
                                                moutClickListener.outClick("buildActChange", goodsBasketCell);
                                            }
                                        } else {
                                            if (moutClickListener != null) {
                                                moutClickListener.outClick("buildCouponChange", goodsBasketCell);
                                            }
                                        }
                                        serviceGoodsOrderShopPresenter.getFeeOnly(new SimpleHashMapBuilder<String, Object>()
                                                .puts("shopId", goodsBasketStore.goodsPickShop.shopId)
                                                .puts("amount", goodsBasketStore.getgCurPrice(bargainMoney))
                                                .puts("lat", LocUtil.getLatitude(context, SpKey.LOC_ORG))
                                                .puts("lng", LocUtil.getLongitude(context, SpKey.LOC_ORG)), goodsBasketStore, storeBlockChildHolder);
                                    }
                                });
                    } else {
                        System.out.println("???????????????2");
                    }
                }
            }

        }

//        System.out.println("?????????????????????");
        if (hasFocus) {
//            System.out.println("?????????????????????");
            return;
        }//???????????????????????? ?????????????????????
        if ("-1".equals(cellGoodsMarketingType)//??????
                ||"3".equals(cellGoodsMarketingType)//??????
                ||"-2".equals(cellGoodsMarketingType)//????????????
                ||"0".equals(cellGoodsMarketingType)//????????????
                ||"5".equals(cellGoodsMarketingType)) {//??????
            //??????????????????????????????????????? ????????????????????????

            if (goodsBasketCell.getGoodsMarketingTypeInt() == 4 && "1".equals(isNtReal)) {//?????????????????????

            } else {
                if(!goodsBasketCell.isGift&&
                        getItemCount()==1&&TextUtils.isEmpty(goodsBasketStore.getGoodsBasketCellOnlyGoodsList().get(0).cartDetailId)){//????????????????????????????????? ???????????????
                    increaseDecrease.setChangeVisable(true);
                    //???????????????????????????
                    increaseDecrease.setVisibility(View.VISIBLE);
                    increaseDecrease.setCheckTextDialog(true);
                    increaseDecrease.setOnNumChangedListener(new IncreaseDecreaseView.OnNumChangedListener() {
                        @Override
                        public void onNumChanged(int num) {
                            //?????????
                            goodsNumChange(num, goodsBasketCell, goodsBasketStore, isOnlyOneStore, isOnlyOneItem, storeBlockChildHolder, view, serviceGoodsOrderShopPresenter);

                        }
                    });
                }

            }

        }


        goodsImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(goodsBasketCell.isGift){
                    return;
                }
//                ARouter.getInstance()
//                        .build(SecondRoutes.SECOND_SERVICE_DETAIL)
//                        .withString("id", goodsBasketCell.goodsId + "")
//                        .withString("marketingType", goodsBasketCell.getGoodsMarketingTypeCanOut())
//                        .withString("shopId", goodsBasketCell.getGoodsShopId())
//                        .withString("assembleId", assembleId)
//                        .withString("bargainId", bargainId)
//                        .withString("bargainMemberId", bargainMemberId)
//                        .navigation();
            }
        });
        goodsTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(goodsBasketCell.isGift){
                    return;
                }
//                ARouter.getInstance()
//                        .build(SecondRoutes.SECOND_SERVICE_DETAIL)
//                        .withString("id", goodsBasketCell.goodsId + "")
//                        .withString("marketingType", goodsBasketCell.getGoodsMarketingTypeCanOut())
//                        .withString("shopId", goodsBasketCell.getGoodsShopId())
//                        .withString("assembleId", assembleId)
//                        .withString("bargainId", bargainId)
//                        .withString("bargainMemberId", bargainMemberId)
//                        .navigation();
            }
        });

        //////System.out.println("????????????:" + goodsBasketCell.getStockOrg());
        if("5".equals(goodsBasketCell.getGoodsMarketingTypeOrg())
                ||"3".equals(goodsBasketCell.getGoodsMarketingTypeOrg())
                ||"-1".equals(goodsBasketCell.getGoodsMarketingTypeOrg())
                ||("4".equals(goodsBasketCell.getGoodsMarketingTypeOrg())&&"1".equals(isNtReal))){
            increaseDecrease.setMaxCount(goodsBasketCell.getMarkLimitMax());
        }else {
            System.out.println("????????????????????????:"+goodsBasketCell.getStock());
            increaseDecrease.setMaxCount(goodsBasketCell.getStock());
        }
        if ("-1".equals(goodsBasketCell.getGoodsMarketingTypeOrg())) {
            increaseDecrease.setMinCount(goodsBasketCell.getMarkLimitMin());
        } else {
            if("5".equals(goodsBasketCell.getGoodsMarketingTypeOrg())
                    ||"-1".equals(goodsBasketCell.getGoodsMarketingTypeOrg())
                    ||"3".equals(goodsBasketCell.getGoodsMarketingTypeOrg())
                    ||("4".equals(goodsBasketCell.getGoodsMarketingTypeOrg())&&"1".equals(isNtReal))){
                increaseDecrease.setMinCount(goodsBasketCell.getMarkLimitMin());
            }else {
                increaseDecrease.setMinCount(1);
            }
        }

        increaseDecrease.setNoCount(goodsBasketCell.getGoodsQuantity());
        increaseDecrease.setLimitMaxString("?????????????????????");
        if (goodsBasketCell.getStock() > 0) {
            if (goodsBasketCell.getGoodsQuantity() == 0) {
                increaseDecrease.setNoCount(1);
                goodsBasketCell.setGoodsQuantity(1);
                goodsNumChange(1, goodsBasketCell, goodsBasketStore, isOnlyOneStore, isOnlyOneItem, storeBlockChildHolder, view, serviceGoodsOrderShopPresenter);
            }
        } else {

        }
        actFlag.setVisibility(View.GONE);
        pointLable.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(bargainId) && "1".equals(goodsBasketCell.getGoodsMarketingType())) {
            actFlag.setVisibility(View.VISIBLE);
            actFlag.setImageResource(R.drawable.act_kick);
        } else if (!TextUtils.isEmpty(assembleId) && "2".equals(goodsBasketCell.getGoodsMarketingType())) {
            actFlag.setVisibility(View.VISIBLE);
            actFlag.setImageResource(R.drawable.act_pin);
        } else if ("3".equals(goodsBasketCell.getGoodsMarketingTypeOrg())) {
            actFlag.setVisibility(View.VISIBLE);
            actFlag.setImageResource(R.drawable.act_kill);
            if (moutClickListener != null) {
                moutClickListener.outClick("????????????", goodsBasketCell);
            }
//            killgoodsCountChange(goodsBasketCell.getGoodsQuantity(), goodsBasketCell, limitCon, false, limitText);
            if (!"3".equals(goodsBasketCell.getGoodsMarketingType())) {//??????????????????????????? ??????????????????????????? ??????????????????
                actFlag.setVisibility(View.GONE);
            }
        } else if ("2".equals(goodsBasketCell.getGoodsMarketingType())) {
            actFlag.setVisibility(View.VISIBLE);
            actFlag.setImageResource(R.drawable.act_pin);
        } else if ("1".equals(goodsBasketCell.getGoodsMarketingType())) {
            actFlag.setVisibility(View.VISIBLE);
            actFlag.setImageResource(R.drawable.act_kick);
        } else if (goodsBasketCell.isGift) {
            pointLable.setVisibility(View.VISIBLE);
            pointLable.setText("??????");
            if (Double.parseDouble(goodsBasketCell.getCurGoodsAmount()) > 0 && !goodsBasketCell.isUseCard) {
                pointLable.setText("??????");
            }
            pointLable.setAlpha(0.5f);
        } else if ("5".equals(goodsBasketCell.getGoodsMarketingType())) {
            pointLable.setVisibility(View.VISIBLE);
            pointLable.setText("????????????");
            pointLable.setAlpha(0.5f);
        }
        goodsOtherTip.setVisibility(View.GONE);
        goodsOtherTip.setBackgroundResource(R.drawable.shape_order_list_tip_lable);
        goodsOtherTip.setTextColor(Color.parseColor("#999999"));
        if ("1".equals(goodsBasketCell.goodsType) && !"-1".equals(goodsBasketCell.getGoodsMarketingTypeOrg())) {
            goodsOtherTip.setVisibility(View.VISIBLE);
            goodsOtherTip.setBackgroundResource(R.drawable.shape_order_list_tip_lable_no);
            goodsOtherTip.setText("??????????????????????????????");
            goodsOtherTip.setTextColor(Color.parseColor("#fffa3c5a"));
        }

        if (goodsBasketCell.goodsImage != null && imageMap.get(goodsBasketCell.getGoodsBarCode()) == null) {
            imageMap.put(goodsBasketCell.getGoodsBarCode(), goodsBasketCell.goodsImage);
        }
        if (imageMap.get(goodsBasketCell.getGoodsBarCode()) != null) {
            goodsBasketCell.goodsImage = imageMap.get(goodsBasketCell.getGoodsBarCode());
        }
        GlideCopy.with(context)
                .load(goodsBasketCell.goodsImage)
                .placeholder(R.drawable.img_1_1_default_z)
                .error(R.drawable.img_1_1_default_z)

                .into(goodsImg);
        goodsTitle.setText(goodsBasketCell.goodsTitle);
//        goodsBasketCell.undo();
        if ("9".equals(cellGoodsMarketingType) || "-4".equals(cellGoodsMarketingType)) {
            goodsMoney.setVisibility(View.GONE);
        }

        goodsMoney.setText("??" + FormatUtils.moneyKeep2Decimals(goodsBasketCell.getCurGoodsAmount()));
        if (goodsBasketCell.isGift) {
            goodsMoney.setText("");
        }
//        if(goodsBasketCell.extraGoodsBasketCell!=null&&goodsBasketCell.extraGoodsBasketCell.isError){
//            goodsBasketCell.undo();
//            goodsMoney.setText("??" + FormatUtils.moneyKeep2Decimals(goodsBasketCell.getCurGoodsAmount()));
//            System.out.println("???????????????????????????????????????1:"+goodsBasketCell.getCurGoodsAmount());
//            goodsBasketCell.redo();
//        }

        System.out.println("???????????????????????????????????????:" + goodsBasketCell.getCurGoodsAmount());
        if (!"0".equals(FormatUtils.moneyKeep2Decimals(goodsBasketCell.getGoodsPoint()))) {//?????????????????????0??????????????????
            goodsMoney.setText(FormatUtils.moneyKeep2Decimals(goodsBasketCell.getGoodsPoint()) + "??????\n" + "+??" + FormatUtils.moneyKeep2Decimals(goodsBasketCell.getCurGoodsAmount()));
            if ("0".equals(FormatUtils.moneyKeep2Decimals(goodsBasketCell.getCurGoodsAmount()))) {
                goodsMoney.setText(FormatUtils.moneyKeep2Decimals(goodsBasketCell.getGoodsPoint()) + "??????");
            }
        }
//        goodsBasketCell.redo();
        if (!TextUtils.isEmpty(assembleId) && "2".equals(goodsBasketCell.getGoodsMarketingType())) {
            if(assemblePrice!=null){

                goodsMoney.setText("??" + FormatUtils.moneyKeep2Decimals(new BigDecimal(assemblePrice).doubleValue()));
            }
        }

//        if (!TextUtils.isEmpty(bargainId)&&"1".equals(goodsBasketCell.getGoodsMarketingType())) {
//            goodsMoney.setText("??" + FormatUtils.moneyKeep2Decimals(new BigDecimal(goodsBasketCell.getCurGoodsAmount()).subtract(new BigDecimal(bargainMoney)).doubleValue()));
//
//        }
        goodsSpec.setTextColor(Color.parseColor("#ff222222"));
        goodsTitle.setTextColor(Color.parseColor("#ff222222"));
        goodsMoney.setTextColor(Color.parseColor("#222222"));
        goodsCount.setText("x " + (goodsBasketCell.getGoodsQuantity() + goodsBasketCell.getGoodsQuantityGiftNeedFixOrg()));

        if ((goodsBasketCell.getStock()) == 0) {
            goodsOtherTip.setVisibility(View.VISIBLE);
            goodsOtherTip.setText("?????????????????????");
            goodsCount.setVisibility(View.GONE);
            goodsMoney.setTextColor(Color.parseColor("#999999"));
            goodsTitle.setTextColor(Color.parseColor("#999999"));
            goodsSpec.setTextColor(Color.parseColor("#999999"));
        } else {
            System.out.println("??????????????????");
            System.out.println("??????????????????????????????:"+goodsBasketCell.getGoodsQuantityInBasket());
            System.out.println("????????????????????????????????????:"+goodsBasketCell.getStock());
            System.out.println("??????????????????-----------------------------------------------------");
            if (goodsBasketCell.getGoodsQuantityInBasket() > 0 && (goodsBasketCell.getGoodsQuantityInBasket()) > goodsBasketCell.getStock()) {//????????????
                goodsOtherTip.setVisibility(View.VISIBLE);
                goodsOtherTip.setText("????????????????????????");
                int tmpstock = goodsBasketCell.getStock();
                if (tmpstock > 0) {
//                    goodsBasketCell.undo();
//                    goodsBasketCell.setGoodsQuantity(tmpstock);
//                    goodsBasketCell.redo();
                }

            }
            goodsCount.setVisibility(View.VISIBLE);
        }
        goodsGiftOtherTip.setVisibility(View.GONE);
        if (goodsBasketCell.getGoodsQuantityGiftNeedFixOrg() != 0 && goodsBasketCell.getGoodsQuantity() > 0) {
            goodsGiftOtherTip.setVisibility(View.VISIBLE);
            goodsGiftOtherTip.setText("???" + goodsBasketCell.getGoodsQuantityGiftNeedFixOrg() + "?????????");
        }
        goodsMoneyChange.setVisibility(View.GONE);
        goodsSpec.setText(goodsBasketCell.goodsSpecDesc);

        if (goodsBasketCell.extraGoodsBasketCell != null && !hasFocus) {
            System.out.println("????????????????????????" + goodsBasketCell.extraGoodsBasketCell.goodsTitle);
            goodsBasketCell.undo();
            int orgGoodsMarktype = goodsBasketCell.getGoodsMarketingTypeInt();
            goodsBasketCell.redo();
            int exGoodsMarktype = goodsBasketCell.getGoodsMarketingTypeInt();
            goodsBasketCell.undo();

            String orgCurGoodsAmount = goodsBasketCell.getCurGoodsAmount();
            goodsBasketCell.redo();
            String exCurGoodsAmount = goodsBasketCell.getCurGoodsAmount();
            System.out.println("????????????:" + orgCurGoodsAmount + "-" + exCurGoodsAmount);
            increaseDecrease.setChangeVisable(true);
            if (orgGoodsMarktype == exGoodsMarktype || (orgGoodsMarktype != 0 && exGoodsMarktype == 0 && goodsBasketCell.isError())) {
                if (goodsBasketCell.getStock() <= 0) {//???????????? ?????????????????????
                    increaseDecrease.setChangeVisable(false);
                    goodsOtherTip.setVisibility(View.VISIBLE);
                    goodsOtherTip.setText("?????????????????????");
                    increaseDecrease.setNoCount(goodsBasketCell.extraGoodsBasketCell.goodsQuantity);
                    goodsMoney.setTextColor(Color.parseColor("#999999"));
                    goodsTitle.setTextColor(Color.parseColor("#999999"));
                    goodsSpec.setTextColor(Color.parseColor("#999999"));
                } else {
                    if (Double.parseDouble(orgCurGoodsAmount) != Double.parseDouble(exCurGoodsAmount)) {//????????? ????????? ???????????????
                        goodsMoneyChange.setVisibility(View.VISIBLE);
                        if (Double.parseDouble(orgCurGoodsAmount) < Double.parseDouble(exCurGoodsAmount)) {//????????????
                            goodsMoneyChange.setText("??????????????????+" + FormatUtils.moneyKeep2Decimals((Double.parseDouble(exCurGoodsAmount) - Double.parseDouble(orgCurGoodsAmount))) + "???");
                        } else {//????????????
                            goodsMoneyChange.setText("??????????????????-" + FormatUtils.moneyKeep2Decimals((Double.parseDouble(orgCurGoodsAmount) - Double.parseDouble(exCurGoodsAmount))) + "???");
                        }
                    }
                    goodsBasketCell.undo();
                    int orgGoodsQuantity = goodsBasketCell.getGoodsQuantity();
                    goodsBasketCell.redo();
                    if (goodsBasketCell.getStock() < orgGoodsQuantity) {
                        goodsOtherTip.setVisibility(View.VISIBLE);
                        goodsOtherTip.setText("?????????????????????????????????????????????????????????");
                        goodsBasketCell.setGoodsQuantity(goodsBasketCell.extraGoodsBasketCell.getStock());
                        if (increaseDecrease.getNum() > goodsBasketCell.extraGoodsBasketCell.getStock()) {
                            increaseDecrease.setNoCount(goodsBasketCell.extraGoodsBasketCell.getStock());
                        }

                    }

                }
            } else {//??????????????????????????? //??????????????????????????? ???????????????????????????
                if (orgGoodsMarktype != 0) {//????????????????????????????????????
                    if (exGoodsMarktype == 0) {//???????????????????????????
                        if (goodsBasketCell.getStock() <= 0) {//???????????? ?????????????????????
                            increaseDecrease.setChangeVisable(false);
                            goodsOtherTip.setVisibility(View.VISIBLE);
                            goodsOtherTip.setText("?????????????????????");
                            increaseDecrease.setNoCount(goodsBasketCell.extraGoodsBasketCell.goodsQuantity);
                            goodsMoney.setTextColor(Color.parseColor("#999999"));
                            goodsTitle.setTextColor(Color.parseColor("#999999"));
                            goodsSpec.setTextColor(Color.parseColor("#999999"));
                        } else {
                            if (Double.parseDouble(orgCurGoodsAmount) != Double.parseDouble(exCurGoodsAmount)) {//????????? ????????? ???????????????
                                goodsMoneyChange.setVisibility(View.VISIBLE);
                                if (Double.parseDouble(orgCurGoodsAmount) < Double.parseDouble(exCurGoodsAmount)) {//????????????
                                    goodsMoneyChange.setText("??????????????????+" + FormatUtils.moneyKeep2Decimals((Double.parseDouble(exCurGoodsAmount) - Double.parseDouble(orgCurGoodsAmount))) + "???");
                                } else {//????????????
                                    goodsMoneyChange.setText("??????????????????-" + FormatUtils.moneyKeep2Decimals((Double.parseDouble(orgCurGoodsAmount) - Double.parseDouble(exCurGoodsAmount))) + "???");
                                }
                            }
                            goodsBasketCell.undo();
                            int orgGoodsQuantity = goodsBasketCell.getGoodsQuantity();
                            goodsBasketCell.redo();
                            if (goodsBasketCell.getStock() < orgGoodsQuantity) {
                                goodsOtherTip.setVisibility(View.VISIBLE);
                                goodsOtherTip.setText("?????????????????????????????????????????????????????????");
//                                goodsBasketCell.setGoodsQuantity(goodsBasketCell.extraGoodsBasketCell.getStockOrg());
                                if (increaseDecrease.getNum() > goodsBasketCell.extraGoodsBasketCell.getStock()) {
                                    increaseDecrease.setNoCount(goodsBasketCell.extraGoodsBasketCell.getStock());
                                }

                            }

                        }
                    }
                }
            }
        }


    }
    private void showActList(LinearLayout actLL, ActVip actVip) {
        actLL.setVisibility(View.GONE);
        actLL.removeAllViews();
        actLL.setVisibility(View.VISIBLE);
        for (int i = 0; i < actVip.PopInfo.size(); i++) {
            ActVip.PopInfo selPopInfo = actVip.PopInfo.get(i);
            View view = LayoutInflater.from(context).inflate(R.layout.service_item_goods_order_discount_item_noline, actLL, false);
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
                actLL.removeAllViews();
            }
        }
    }
    private void goodsNumChange(final int num, final GoodsBasketCell goodsBasketCell, final GoodsBasketStore goodsBasketStore, final boolean isOnlyOneStore, final boolean isOnlyOneItem, final StoreBlockChildHolder storeBlockChildHolder, final View view, final ServiceGoodsOrderShopPresenter serviceGoodsOrderShopPresenter) {
        goodsBasketCell.setGoodsQuantity(num);
//                    couponInfoLeftZList.clear();
        if (moutClickListener != null) {
            moutClickListener.outClick("getNowAllActs", null);
        }
        buildStoreBlockOnlyOneData(goodsBasketStore, isOnlyOneStore, isOnlyOneItem, storeBlockChildHolder, goodsBasketCell, view);
        if (!goodsBasketStore.checkAllIsService()) {//???????????????????????????????????????????????????
            serviceGoodsOrderShopPresenter.getFeeOnly(new SimpleHashMapBuilder<String, Object>()
                    .puts("shopId", goodsBasketStore.goodsPickShop.shopId)
                    .puts("amount", goodsBasketStore.getgCurPrice(bargainMoney))
                    .puts("lat", LocUtil.getLatitude(context, SpKey.LOC_ORG))
                    .puts("lng", LocUtil.getLongitude(context, SpKey.LOC_ORG)), goodsBasketStore, storeBlockChildHolder);
        }
        if (moutClickListener != null) {
            moutClickListener.outClick("getNowAllCouponInfoList", null);
        }
        if (moutClickListener != null) {
            moutClickListener.outClick("buildCouponWithNoDialog", null);
        }
        if (moutClickListener != null) {
            moutClickListener.outClick("buildMatchCoupon", null);
        }
        if (moutClickListener != null) {
            moutClickListener.outClick("buildNowPayMoney", null);
        }
    }

    private void buildStoreBlockOnlyOne(final GoodsBasketStore goodsBasketStore, LinearLayout goodsListLL, int needSize, boolean isOnlyOneStore, boolean isOnlyOneItem, final StoreBlockChildHolder storeBlockChildHolder) {
        goodsListLL.removeAllViews();
        List<GoodsBasketCell> basketCellList = goodsBasketStore.getGoodsBasketCellAllExpCardList(cellGoodsMarketingType);
//        System.out.println("?????????????????????????????????????????????" + basketCellList.size());
//        System.out.println("?????????????????????????????????????????????m" + needSize);
        if (needSize > basketCellList.size()) {
            needSize = basketCellList.size();
        }
        for (int i = 0; i < needSize; i++) {

//            System.out.println("?????????????????????????????????????????????z+" + needSize);
            try {
                final GoodsBasketCell goodsBasketCell = basketCellList.get(i);
                if (!goodsBasketCell.isCardCanUse) {//?????????????????????????????????????????? ????????????????????????
                    View view = LayoutInflater.from(context).inflate(R.layout.service_activity_goodsorder_group_vgoods, goodsListLL, false);
                    buildStoreBlockOnlyOneData(goodsBasketStore, isOnlyOneStore, isOnlyOneItem, storeBlockChildHolder, goodsBasketCell, view);
                    if (!goodsBasketCell.isNeedFixOrg) {
                        goodsListLL.addView(view);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void showBackView(final TextView textview, final GoodsBasketStore goodsBasketStore) {
        StyledDialog.init(context);
        StyledDialog.buildMdInput("????????????", "????????????", "",
                textview.getText().toString(), "", new InputFilter[]{}, null, new MyDialogListener() {
                    @Override
                    public void onFirst() {

                    }

                    @Override
                    public void onSecond() {

                    }

                    @Override
                    public boolean onInputValid(CharSequence input1, CharSequence input2, EditText editText1, EditText editText2) {

                        textview.setText(input1 + "");
                        goodsBasketStore.deliver.remark = input1.toString();
                        return true;
                    }

                    @Override
                    public void onGetInput(CharSequence input1, CharSequence input2) {
                        super.onGetInput(input1, input2);
                    }
                })
                .setInput2HideAsPassword(true)
                .setCancelable(true, true)
                .show();
    }

    private void showShopReceive(final GoodsBasketStore goodsBasketStore, final StoreBlockChildHolder storeBlockChildHolder) {
        if (goodsBasketStore.goodsPickShopList == null || goodsBasketStore.goodsPickShopList.size() == 0) {
            return;
        }
        if (shopOrderPickDialog == null) {
            shopOrderPickDialog = ShopOrderPickDialog.newInstance();
        }
        try {
            shopOrderPickDialog.setSelectId(goodsBasketStore.goodsPickShop.shopId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        shopOrderPickDialog.show(((BaseActivity) context).getSupportFragmentManager(), "????????????");
        shopOrderPickDialog.setGoodsShopList(goodsBasketStore.buildFlashList(goodsBasketStore.goodsPickShopList));
        shopOrderPickDialog.setTitle("??????????????????");
        shopOrderPickDialog.setOnDialogShopClickListener(new ShopOrderPickDialog.OnDialogShopClickListener() {
            @Override
            public void onDialogShopClick(GoodsShop goodsShop) {
                goodsBasketStore.goodsPickShop = goodsShop;
                goodsBasketStore.deliver.deliveryShopId = goodsShop.shopId;
                goodsBasketStore.deliver.deliveryShopDeptId = goodsShop.ytbDepartID;
                onSucessGetReceiveShop(null, goodsBasketStore, storeBlockChildHolder);

                goodsBasketStore.changeItem();
//                buildStoreBlock(((StoreBlockChildHolder)goodsBasketStore.storeBlockChildHolder).getView(), goodsBasketStore, getItemCount() == 1);
//                if (moutClickListener != null) {
//                    moutClickListener.outClick("getNowAllActs", null);
//                }
                if (moutClickListener != null) {
                    moutClickListener.outClick("??????????????????", goodsBasketStore);
                }
            }
        });
    }

    private void showShopPick(final GoodsBasketStore goodsBasketStore, final StoreBlockChildHolder storeBlockChildHolder) {
        if (goodsBasketStore.goodsPickShopList == null || goodsBasketStore.goodsPickShopList.size() == 0) {
            return;
        }
        if (shopOrderPickDialog == null) {
            shopOrderPickDialog = ShopOrderPickDialog.newInstance();
        } else {
            shopOrderPickDialog.dismiss();
        }
        try {
            shopOrderPickDialog.setSelectId(goodsBasketStore.goodsPickShop.shopId);
        } catch (Exception e) {
//            e.printStackTrace();
        }
        shopOrderPickDialog.show(((BaseActivity) context).getSupportFragmentManager(), "????????????");
        shopOrderPickDialog.setGoodsShopList(goodsBasketStore.buildFlashList(goodsBasketStore.goodsPickShopList));
        shopOrderPickDialog.setTitle("??????????????????");
        shopOrderPickDialog.setOnDialogShopClickListener(new ShopOrderPickDialog.OnDialogShopClickListener() {
            @Override
            public void onDialogShopClick(GoodsShop goodsShop) {
                goodsBasketStore.goodsPickShop = goodsShop;
                goodsBasketStore.deliver.deliveryShopId = goodsShop.shopId;
                goodsBasketStore.deliver.deliveryShopDeptId = goodsShop.ytbDepartID;
                onSucessGetPickShop(null, goodsBasketStore, storeBlockChildHolder);

                //???????????????????????????
                buildStoreBlock(((StoreBlockChildHolder) goodsBasketStore.storeBlockChildHolder).getView(), goodsBasketStore, getItemCount() == 1);
//                if (moutClickListener != null) {
//                    moutClickListener.outClick("getNowAllActs", null);
//                }
                if (moutClickListener != null) {
                    moutClickListener.outClick("??????????????????", goodsBasketStore);
                }
            }
        });
    }


    public void buildCheckResult(int checkedId, final GoodsBasketStore goodsBasketStore, final StoreBlockChildHolder storeBlockChildHolder) {
        storeBlockChildHolder.checkA.getPaint().setFakeBoldText(false);
        storeBlockChildHolder.checkA.getPaint().setFakeBoldText(false);
        if (checkedId == 0) {
            checkedId = R.id.checkA;
        }
        goodsBasketStore.checkedId = checkedId;
        storeBlockChildHolder.checkB.setBackgroundResource(R.drawable.selector_goods_radio_backgr);
        if (goodsBasketStore.notcheck == R.id.checkB) {
            storeBlockChildHolder.checkB.setBackgroundResource(R.drawable.selector_goods_radio_backgrno);
        }
        if (checkedId == 0 || checkedId == R.id.checkA) {
            goodsBasketStore.redo();
            goodsBasketStore.deliver.deliveryType = "10";
            if (goodsBasketStore.goodsPickShop != null) {

                goodsBasketStore.deliver.deliveryShopId = goodsBasketStore.goodsPickShop.shopId;
            }
            storeBlockChildHolder.toHomeLL.setVisibility(View.GONE);
            storeBlockChildHolder.toStoreLL.setVisibility(View.VISIBLE);
//            goodsBasketStore.fee = 0;
            if (moutClickListener != null) {
                //////System.out.println("SRX570");
//                System.out.println("????????????????????????????????????????????????");
                moutClickListener.outClick("buildNowPayMoney", null);
            }
//            buildNowPayMoney();
            if(goodsBasketStore.feeChange){
                goodsBasketStore.feeChange=false;
                ServiceGoodsOrderShopPresenter serviceGoodsOrderShopPresenter = new ServiceGoodsOrderShopPresenter(context, this);
                serviceGoodsOrderShopPresenter.getFeeOnly(new SimpleHashMapBuilder<String, Object>()
                        .puts("shopId", goodsBasketStore.goodsPickShop.shopId)
                        .puts("amount", goodsBasketStore.getgCurPrice(bargainMoney))
                        .puts("lat", LocUtil.getLatitude(context, SpKey.LOC_ORG))
                        .puts("lng", LocUtil.getLongitude(context, SpKey.LOC_ORG)), goodsBasketStore, storeBlockChildHolder);
            }

        } else {
            goodsBasketStore.undo();
            if (goodsBasketStore.notcheck == R.id.checkB) {
                Toast.makeText(context, "" + goodsBasketStore.msg + "???????????????~", Toast.LENGTH_SHORT).show();
            }
            goodsBasketStore.deliver.deliveryType = "11";
            if (goodsBasketStore.goodsPickShop != null) {

                goodsBasketStore.deliver.deliveryShopId = goodsBasketStore.goodsPickShop.shopId;
            }
            storeBlockChildHolder.toHomeLL.setVisibility(View.VISIBLE);
            storeBlockChildHolder.toStoreLL.setVisibility(View.GONE);
            storeBlockChildHolder.toHomeDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goodsBasketStoreNow = goodsBasketStore;
                    storeBlockChildHolderNow = storeBlockChildHolder;
                    if (moutClickListener != null) {
                        moutClickListener.outClick("????????????????????????", null);
                    }
//                    ARouter.getInstance()
//                            .build(ServiceRoutes.SERVICE_ADDRESS_LIST)
//                            .withBoolean("isNeedSelect", true)
//                            .navigation(context, SELECT_ADDRESS_REQUEST);
                }
            });
            storeBlockChildHolder.addressShop.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(goodsBasketStore.deliver.deliveryConsigneeAddress)) {
                storeBlockChildHolder.addAddressLL.setVisibility(View.GONE);
                storeBlockChildHolder.toHomeDetail.setVisibility(View.VISIBLE);
                storeBlockChildHolder.homeCity.setText(goodsBasketStore.deliver.deliveryConsigneeProvinceName + goodsBasketStore.deliver.deliveryConsigneeCityName + goodsBasketStore.deliver.deliveryConsigneeDistrictName);
                storeBlockChildHolder.homeAddress.setText((goodsBasketStore.deliver.deliveryConsigneeAddress).replace(goodsBasketStore.deliver.deliveryConsigneeProvinceName + goodsBasketStore.deliver.deliveryConsigneeCityName + goodsBasketStore.deliver.deliveryConsigneeDistrictName, ""));
                storeBlockChildHolder.homeMasterName.setText(SpanUtils.getBuilder(context, goodsBasketStore.deliver.deliveryConsigneeName + " ").setForegroundColor(Color.parseColor("#222222"))
                        .append(goodsBasketStore.deliver.deliveryConsigneePhone).setForegroundColor(Color.parseColor("#666666"))
                        .create());
                ServiceGoodsOrderShopPresenter serviceGoodsOrderShopPresenter = new ServiceGoodsOrderShopPresenter(context, this);
                //////System.out.println("??????????????????????????????");
                serviceGoodsOrderShopPresenter.getFee(new SimpleHashMapBuilder<String, Object>()
                        .puts("shopId", goodsBasketStore.goodsPickShop.shopId)
                        .puts("amount", goodsBasketStore.getgCurPrice(bargainMoney))
                        .puts("lat", goodsBasketStore.deliver.deliveryLatitude)
                        .puts("lng", goodsBasketStore.deliver.deliveryLongitude), goodsBasketStore, storeBlockChildHolder);
            } else {
                storeBlockChildHolder.addAddressLL.setVisibility(View.VISIBLE);
                storeBlockChildHolder.toHomeDetail.setVisibility(View.GONE);
                storeBlockChildHolder.addAddressLL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goodsBasketStoreNow = goodsBasketStore;
                        storeBlockChildHolderNow = storeBlockChildHolder;
                        if (moutClickListener != null) {
                            moutClickListener.outClick("????????????????????????", null);
                        }
//                        ARouter.getInstance()
//                                .build(ServiceRoutes.SERVICE_ADDRESS_LIST)
//                                .withBoolean("isNeedSelect", true)
//                                .navigation(ServiceGoodsOrderFinal.this, SELECT_ADDRESS_REQUEST);
                    }
                });
            }


        }
//        goodsBasketStore.changeItem();
    }


    @Override
    public void onSucessGetPickShopOnly(List<GoodsShop> goodsShopList, String msg, GoodsBasketStore goodsBasketStore, StoreBlockChildHolder storeBlockChildHolder) {

        if (goodsBasketStore.notcheck == R.id.checkB && storeBlockChildHolder.checkB.isChecked()) {//?????????????????????B???
            storeBlockChildHolder.checkA.setChecked(true);
//            Toast.makeText(mContext, "" + goodsBasketStore.msg + "???????????????~", Toast.LENGTH_SHORT).show();
            buildCheckResult(R.id.checkA, goodsBasketStore, storeBlockChildHolder);

        } else if (goodsBasketStore.notcheck == R.id.checkA && storeBlockChildHolder.checkA.isChecked()) {//?????????????????????B???
            storeBlockChildHolder.checkB.setChecked(true);
            Toast.makeText(context, "?????????????????????", Toast.LENGTH_SHORT).show();
            buildCheckResult(R.id.checkB, goodsBasketStore, storeBlockChildHolder);

        } else {
            buildCheckResult(goodsBasketStore.checkedId, goodsBasketStore, storeBlockChildHolder);
        }

    }

    @Override
    public void onSucessGetPickShop(List<GoodsShop> goodsShopList, GoodsBasketStore goodsBasketStore, StoreBlockChildHolder storeBlockChildHolder) {

        if (goodsBasketStore.goodsPickShop != null) {
//            goodsBasketStore.fee=0;
            if(goodsShopList!=null){
                if("-4".equals(cellGoodsMarketingType) || "-5".equals(cellGoodsMarketingType) || "9".equals(cellGoodsMarketingType)){//??????????????????????????? ??????????????????????????? ??? ?????????????????????
                    try {
                        GoodsBasketCell goodsBasketCell=goodsBasketStore.getGoodsBasketCellAllList().get(0);
                        if(!ListUtil.checkObjIsInList(new SimpleArrayListBuilder<String>().putList(goodsShopList, new ObjectIteraor<GoodsShop>() {
                            @Override
                            public String getDesObj(GoodsShop o) {
                                return o.shopId;
                            }
                        }),goodsBasketCell.goodsShopId)){//?????????????????? ??????????????? ?????????????????????
                            GoodsShop newStoreDetialModel=goodsBasketStore.goodsPickShopList.get(0);
                            mShopPhone = newStoreDetialModel.appointmentPhone;
                            if (goodsBasketStore.orgGoodsPickShop == null) {
                                goodsBasketStore.orgGoodsPickShop = newStoreDetialModel;
                            }
                            if (goodsBasketStore.orgGoodsPickShop == null) {
                                goodsBasketStore.orgGoodsPickShop = newStoreDetialModel;
                            }
                            goodsBasketStore.goodsPickShop = newStoreDetialModel;
                            goodsBasketStore.deliver.deliveryShopDeptId = newStoreDetialModel.ytbDepartID;
                            if(goodsBasketCell.isInit){//????????????
                                goodsBasketCell.isInit=false;
                                goodsBasketCell.goodsShopId=newStoreDetialModel.shopId;
                                goodsBasketCell.changeItem();
                            }else {
                                goodsBasketCell.isInit=false;
                                goodsBasketCell.goodsShopId=goodsShopList.get(0).shopId;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            storeBlockChildHolder.toStoreName.setText(goodsBasketStore.goodsPickShop.shopName);
            storeBlockChildHolder.toStoreAddress.setText(goodsBasketStore.goodsPickShop.getShopAddressDetail());
//            notifyDataSetChanged();
        }
    }

    @Override
    public void onSucessGetReceiveShop(List<GoodsShop> goodsShopList, GoodsBasketStore goodsBasketStore, StoreBlockChildHolder storeBlockChildHolder) {
        if (goodsBasketStore.goodsPickShop != null) {
            storeBlockChildHolder.addressShop.setVisibility(View.VISIBLE);
            goodsBasketStore.fee = goodsBasketStore.goodsPickShop.fee;//??????
            storeBlockChildHolder.receiveShop.setText(goodsBasketStore.goodsPickShop.shopName);

        }
    }

    @Override
    public void onSucessGetFeeOnly(GoodsFee goodsFee, GoodsBasketStore goodsBasketStore, StoreBlockChildHolder storeBlockChildHolder) {
        if ("-5".equals(cellGoodsMarketingType) && goodsFee != null) {
            goodsFee.fee = 0;//????????????
        }
        if (goodsBasketStore.notcheck == R.id.checkB && storeBlockChildHolder.checkB.isChecked()) {//?????????????????????B???
            storeBlockChildHolder.checkA.setChecked(true);
            Toast.makeText(context, "" + goodsBasketStore.msg + "???????????????~", Toast.LENGTH_SHORT).show();
//            Toast.makeText(mContext, "" + goodsBasketStore.msg + "???????????????~", Toast.LENGTH_SHORT).show();
            buildCheckResult(R.id.checkA, goodsBasketStore, storeBlockChildHolder);

        } else if (goodsBasketStore.notcheck == R.id.checkA && storeBlockChildHolder.checkA.isChecked()) {//?????????????????????B???
            storeBlockChildHolder.checkB.setChecked(true);
//            Toast.makeText(context, "??????????????????", Toast.LENGTH_SHORT).show();
            buildCheckResult(R.id.checkB, goodsBasketStore, storeBlockChildHolder);
        } else {
            buildCheckResult(goodsBasketStore.checkedId, goodsBasketStore, storeBlockChildHolder);
        }
    }

    @Override
    public void onSucessGetFee(GoodsFee goodsFee, final GoodsBasketStore goodsBasketStore, final StoreBlockChildHolder storeBlockChildHolder) {
        if ("-5".equals(cellGoodsMarketingType)) {
            goodsFee.fee = 0;//????????????
        }
        if (goodsBasketStore.goodsPickShop != null && goodsFee != null) {
            storeBlockChildHolder.addressShop.setVisibility(View.VISIBLE);
            goodsBasketStore.fee = goodsFee.fee;//??????
            storeBlockChildHolder.receiveShop.setText(goodsBasketStore.goodsPickShop.shopName);
//            storeBlockChildHolder.addressShop.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    showShopReceive(goodsBasketStore, storeBlockChildHolder);
//                }
//            });
            if (moutClickListener != null) {
                //////System.out.println("SRX704");
                moutClickListener.outClick("buildNowPayMoney", null);
            }
//            buildNowPayMoney();
        }
    }

    @Override
    public void onSucessGetShopDetailOnly(ShopDetailModel shopDetailModel, GoodsBasketStore goodsBasketStore, StoreBlockChildHolder storeBlockChildHolder) {
        if (shopDetailModel != null) {
            mShopPhone = shopDetailModel.appointmentPhone;
            if (goodsBasketStore.orgGoodsPickShop == null) {
                goodsBasketStore.orgGoodsPickShop = new GoodsShop(shopDetailModel);
            }
            if (goodsBasketStore.orgGoodsPickShop == null) {
                goodsBasketStore.orgGoodsPickShop = new GoodsShop(shopDetailModel);
            }
            goodsBasketStore.goodsPickShop = new GoodsShop(shopDetailModel);
            goodsBasketStore.goodsPickShop = new GoodsShop(shopDetailModel);
            goodsBasketStore.deliver.deliveryShopDeptId = shopDetailModel.getYtbDepartID();
            for (int i = 0; i < goodsBasketStore.getGoodsBasketCellAllList().size(); i++) {
                goodsBasketStore.getGoodsBasketCellAllList().get(i).setIsSupportOverSold(shopDetailModel.isSupportOverSold);
                goodsBasketStore.getGoodsBasketCellAllList().get(i).changeItem();
            }
            storeBlockChildHolder.toStoreName.setText(goodsBasketStore.goodsPickShop.shopName);
            storeBlockChildHolder.toStoreAddress.setText(goodsBasketStore.goodsPickShop.getShopAddressDetail());

        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showToast(CharSequence msg) {

    }

    @Override
    public void showNetErr() {

    }

    @Override
    public void onRequestStart(Disposable disposable) {

    }

    @Override
    public void showContent() {

    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void onRequestFinish() {

    }

    @Override
    public void getData() {

    }

    @Override
    public void showDataErr() {

    }

    public void setIsNtReal(String isNtReal) {
        this.isNtReal = isNtReal;
    }

    public String getIsNtReal() {
        return isNtReal;
    }

    public void setImageMap(Map<String, String> imageMap) {
        this.imageMap = imageMap;
    }

    public Map<String, String> getImageMap() {
        return imageMap;
    }

    public String getTotalAmount() {
        return totalAmount;
    }
}
