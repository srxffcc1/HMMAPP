package com.health.tencentlive.adapter;

import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.example.lib_ShapeView.layout.ShapeLinearLayout;
import com.example.lib_ShapeView.view.ShapeTextView;
import com.health.servicecenter.widget.GoodsSimpleDialog;
import com.health.tencentlive.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseDialogFragment;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.GoodsBasketCell;
import com.healthy.library.model.GoodsBasketGroup;
import com.healthy.library.model.GoodsSpecDetail;
import com.healthy.library.model.LiveRoomInfo;
import com.healthy.library.model.LiveVideoGoods;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.AutoClickImageView;
import com.healthy.library.widget.CornerImageView;

import java.util.ArrayList;
import java.util.List;

public class ShoppingBagDialogAdapter extends BaseAdapter<LiveVideoGoods> {

    public ShoppingBagDialogAdapter() {
        this(R.layout.live_shoppingbag_adapter_layout);
    }

    public ShoppingBagDialogAdapter(int viewId) {
        super(viewId);
    }

    BaseDialogFragment baseDialogFragment;

    private String shopId;
    private String anchormanId;
    private String courseId;
    private boolean isHistoryLive = false;//是否是回放 回放购买跟直播购买有点区别
    private LiveRoomInfo mLiveRoomInfo;

    public ShoppingBagDialogAdapter setMerchantId(String merchantId) {
        this.merchantId = merchantId;
        return this;
    }

    private String merchantId;

    public ShoppingBagDialogAdapter setShopId(String shopId, String anchormanId, String courseId) {
        this.shopId = shopId;
        this.anchormanId = anchormanId;
        this.courseId = courseId;
        return this;
    }

    public ShoppingBagDialogAdapter setHistoryLive(boolean historyLive) {
        this.isHistoryLive = historyLive;
        return this;
    }

    public ShoppingBagDialogAdapter setLiveInfo( LiveRoomInfo mLiveRoomInfo) {
        this.mLiveRoomInfo = mLiveRoomInfo;
        return this;
    }

    public void setDialog(BaseDialogFragment dialog) {
        baseDialogFragment = dialog;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        CornerImageView goodsImg;
        ShapeLinearLayout invalidImg;
        TextView goodsTitle;
        TextView goodsPrice;
        ShapeTextView redBtn;
        ShapeTextView greedBtn;
        TextView waitTxt;
        TextView goodsSpace;
        TextView goodsLable;
        TextView goodsPriceOld;
        ImageView isShopTakeOnly;
        ImageView animImg;
        LinearLayout speakLayout;

        goodsImg = (CornerImageView) holder.itemView.findViewById(R.id.goodsImg);
        invalidImg = (ShapeLinearLayout) holder.itemView.findViewById(R.id.invalidImg);
        goodsTitle = (TextView) holder.itemView.findViewById(R.id.goodsTitle);
        goodsPrice = (TextView) holder.itemView.findViewById(R.id.goodsPrice);
        redBtn = (ShapeTextView) holder.itemView.findViewById(R.id.redBtn);
        greedBtn = (ShapeTextView) holder.itemView.findViewById(R.id.greedBtn);
        waitTxt = (TextView) holder.itemView.findViewById(R.id.waitTxt);
        goodsSpace = (TextView) holder.itemView.findViewById(R.id.goodsSpace);
        goodsLable = (TextView) holder.itemView.findViewById(R.id.goodsLable);
        goodsPriceOld = (TextView) holder.itemView.findViewById(R.id.goodsPriceOld);
        isShopTakeOnly = (ImageView) holder.itemView.findViewById(R.id.isShopTakeOnly);
        animImg = (ImageView) holder.itemView.findViewById(R.id.animImg);
        speakLayout = (LinearLayout) holder.itemView.findViewById(R.id.speakLayout);
        AutoClickImageView autoClickImageView=(AutoClickImageView) holder.itemView.findViewById(R.id.goodsShareCoupon);
        AnimationDrawable anim = (AnimationDrawable) animImg.getBackground();// 获取到动画资源


        anim.setOneShot(false); // 设置是否重复播放
        final LiveVideoGoods liveVideoGoods = getDatas().get(position);
        if(liveVideoGoods.shareGiftDTOS!=null&&liveVideoGoods.shareGiftDTOS.size()>0){//
            autoClickImageView.setVisibility(View.VISIBLE);
            autoClickImageView.setCanTouch(false);
//            autoClickImageView.startLoopScaleAuto();
        }else {
            autoClickImageView.setVisibility(View.INVISIBLE);
//            autoClickImageView.clearLoop();
        }
        if (liveVideoGoods != null) {
            if (liveVideoGoods.goodsChildren != null && liveVideoGoods.goodsChildren.size() > 0) {
                goodsSpace.setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(liveVideoGoods.getGoodsChildrenSpecStr())) {
                    goodsSpace.setVisibility(View.GONE);
                } else {
                    goodsSpace.setText(liveVideoGoods.getGoodsChildrenSpecStr());
                    goodsSpace.setVisibility(View.VISIBLE);
                }
            } else {
                goodsSpace.setVisibility(View.GONE);
            }
            if (liveVideoGoods.headImages != null) {
                com.healthy.library.businessutil.GlideCopy.with(context)
                        .load(liveVideoGoods.headImages.get(0).filePath)

                        .placeholder(R.drawable.img_1_1_default)
                        .error(R.drawable.img_1_1_default)
                        .into(goodsImg);
            }
            goodsLable.setText(liveVideoGoods.ranking + "");
            goodsTitle.setText(liveVideoGoods.goodsTitle);
            if (liveVideoGoods.goodsChildren != null) {
                goodsPrice.setText(FormatUtils.moneyKeep2Decimals(liveVideoGoods.getGoodsChildrenLivePrice()) + "元");
                goodsPriceOld.setText(FormatUtils.moneyKeep2Decimals(liveVideoGoods.getGoodsChildrenRetailPricePrice()) + "元");
                goodsPriceOld.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
            }
            if (liveVideoGoods.isShopTakeOnly == 1) {//支持到店自提
                isShopTakeOnly.setVisibility(View.VISIBLE);
            } else {
                isShopTakeOnly.setVisibility(View.GONE);
            }
            if (isHistoryLive) {
                if (liveVideoGoods.availableInventory <= 0 || liveVideoGoods.getGoodsChildrenInventory() <= 0) {//表示已经售完
                    speakLayout.setVisibility(View.GONE);
                    redBtn.setVisibility(View.GONE);
                    invalidImg.setVisibility(View.VISIBLE);
                    greedBtn.setVisibility(View.VISIBLE);
                    greedBtn.setText("已抢光");
                } else {
                    invalidImg.setVisibility(View.GONE);
                    greedBtn.setVisibility(View.GONE);
                    speakLayout.setVisibility(View.GONE);
                    redBtn.setVisibility(View.VISIBLE);
                }
            } else {
                if (liveVideoGoods.offShelf == 0) {//正在上架抢购
                    if (liveVideoGoods.availableInventory <= 0 ) {//表示已经售完
                        speakLayout.setVisibility(View.GONE);
                        redBtn.setVisibility(View.GONE);
                        invalidImg.setVisibility(View.VISIBLE);
                        greedBtn.setVisibility(View.VISIBLE);
                        greedBtn.setText("已抢光");
                    } else {
                        invalidImg.setVisibility(View.GONE);
                        greedBtn.setVisibility(View.GONE);
                        speakLayout.setVisibility(View.GONE);
                        redBtn.setVisibility(View.VISIBLE);
                    }
                } else if (liveVideoGoods.offShelf == 1) {//可查看商品  但未开始抢购
                    invalidImg.setVisibility(View.GONE);
                    greedBtn.setVisibility(View.VISIBLE);
                    speakLayout.setVisibility(View.GONE);
                    redBtn.setVisibility(View.GONE);
                    greedBtn.setText("等待开抢");
                }
                if (liveVideoGoods.isSpeak == 0) {//主播正在讲解
                    speakLayout.setVisibility(View.GONE);
                } else {
                    speakLayout.setVisibility(View.VISIBLE);
                    anim.start();// 开始动画
                }
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goLiveGoods(liveVideoGoods);
                }
            });
        }

    }

    private void goLiveGoods(final LiveVideoGoods liveVideoGoods) {
        String errMsg = null;
        if (liveVideoGoods.offShelf == 0 && liveVideoGoods.getGoodsIsOffShelf()) {//上架可以抢
            if (liveVideoGoods.availableInventory == 0 || liveVideoGoods.getGoodsChildrenInventory() <= 0) {
                errMsg = "已抢完";
                Toast.makeText(context, "该商品已抢光", Toast.LENGTH_SHORT).show();
            }
        } else {
            errMsg = "等待主播开启抢购";

        }
        GoodsSimpleDialog goodsSimpleDialog = new GoodsSimpleDialog();
        goodsSimpleDialog.setErrMsg(errMsg)
                .setSeachMap(new SimpleHashMapBuilder<String, Object>().puts(Functions.FUNCTION, "9292").puts("liveGoodsId", liveVideoGoods.id))
                .setLiveInfo(mLiveRoomInfo)
                .setOnDialogButtonOrderListener(new GoodsSimpleDialog.OnGoodsDialogOrderButtonListener() {
                    @Override
                    public void onOrderClick(GoodsSpecDetail goodsSpecDetailNew) {
                        if (goodsSpecDetailNew.getAvailableInventory() < goodsSpecDetailNew.salesMin) {//可够小于最小起购商品库存不足
                            Toast.makeText(context, "已抢光", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        GoodsBasketCell.GoodsMarketing goodsMarketing = new GoodsBasketCell.GoodsMarketing(null);
                        goodsMarketing.availableInventory = goodsSpecDetailNew.getAvailableInventory();
                        goodsMarketing.mapMarketingGoodsId = liveVideoGoods.id;
                        goodsMarketing.marketingType = "-1";
                        goodsMarketing.id = goodsSpecDetailNew.id;
                        goodsMarketing.marketingPrice = goodsSpecDetailNew.marketingPrice;
                        goodsMarketing.pointsPrice = 0;
                        goodsMarketing.salesMin = liveVideoGoods.salesMin;
                        goodsMarketing.salesMax = liveVideoGoods.salesMax;
                        GoodsBasketCell goodsBasketCell = new GoodsBasketCell(goodsSpecDetailNew.marketingPrice,
                                goodsSpecDetailNew.marketingPrice,
                                0,
                                (liveVideoGoods.isShopTakeOnly == 1 ? 1 : 2) + "",
                                "0",
                                "0", null);
                        goodsBasketCell.goodsSpecDesc = goodsSpecDetailNew.goodsSpecStr;
                        goodsBasketCell.goodsStock = goodsSpecDetailNew.getAvailableInventory();
                        goodsBasketCell.goodsMarketingDTO = goodsMarketing;
                        goodsBasketCell.mchId = SpUtils.getValue(context, SpKey.CHOSE_MC) + "";
                        goodsBasketCell.goodsId = liveVideoGoods.goodsId;
                        try {
                            goodsBasketCell.setGoodsSpecId(goodsSpecDetailNew.goodsChildId);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        goodsBasketCell.goodsTitle = liveVideoGoods.goodsTitle;
                        goodsBasketCell.goodsImage = liveVideoGoods.headImages.get(0).filePath;
                        goodsBasketCell.setGoodsQuantity(Integer.parseInt(goodsSpecDetailNew.getCount()) == 0 ? 1 : Integer.parseInt(goodsSpecDetailNew.getCount()));
                        GoodsBasketGroup goodsBasketGroup = new GoodsBasketGroup(goodsBasketCell);
                        List<GoodsBasketCell> list = new ArrayList<>();
                        goodsBasketCell.shopIdList = liveVideoGoods.shopIds;
                        goodsBasketCell.ischeck = true;
                        goodsBasketCell.goodsShopId = goodsSpecDetailNew.shopDetailModelSelect.id;
                        goodsBasketCell.goodsShopName = goodsSpecDetailNew.shopDetailModelSelect.shopName;
                        goodsBasketCell.goodsShopAddress = goodsSpecDetailNew.shopDetailModelSelect.addressDetails;
                        list.add(goodsBasketCell);

                        ARouter.getInstance()
                                .build(ServiceRoutes.SERVICE_ORDER)
                                .withString("visitShopId", goodsSpecDetailNew.shopDetailModelSelect.id)
                                .withString("token", null)
                                .withString("course_id", null)
                                .withString("liveStatus", null)
                                .withString("live_anchor", anchormanId)
                                .withString("live_course", courseId)
                                .withObject("goodsbasketlist", list)
                                .withString("goodsMarketingType", "-1")
                                .navigation();
                    }
                })
                .show(baseDialogFragment.getChildFragmentManager(), "商品弹窗");
    }
}
