package com.health.tencentlive.adapter;

import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.servicecenter.widget.GoodsSimpleDialog;
import com.health.tencentlive.R;
import com.health.tencentlive.activity.LookLiveActivity;
import com.healthy.library.base.BaseDialogFragment;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.base.BaseMultiItemAdapter;
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

public class LiveLookGoodsListAdapter extends BaseMultiItemAdapter<LiveVideoGoods> {
    private LookLiveActivity activity;

    public LiveLookGoodsListAdapter() {
        this(R.layout.item_live_look_goods_list_unselect);
        addItemType(0, R.layout.item_live_look_goods_list_unselect);
        addItemType(1, R.layout.item_live_look_goods_list_select);
    }

    BaseDialogFragment baseDialogFragment;
    String errMsg = null;

    public void setDialog(BaseDialogFragment dialog) {
        baseDialogFragment = dialog;
    }

    public LiveLookGoodsListAdapter(int viewId) {
        super(viewId);
    }
    private LiveRoomInfo mLiveRoomInfo;

    public LiveLookGoodsListAdapter setLiveInfo( LiveRoomInfo mLiveRoomInfo) {
        this.mLiveRoomInfo = mLiveRoomInfo;
        return this;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();

    }

    private String shopId;

    public LiveLookGoodsListAdapter setMerchantId(String merchantId) {
        this.merchantId = merchantId;
        return this;
    }

    private String merchantId;
    private String anchormanId;
    private String courseId;

    public LiveLookGoodsListAdapter setShopId(String shopId, String anchormanId, String courseId) {
        this.shopId = shopId;
        this.anchormanId = anchormanId;
        this.courseId = courseId;
        return this;
    }

    @Override
    public int getItemViewType(int position) {
        return getDefItemViewType(position);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        final LiveVideoGoods liveVideoGoods = getDatas().get(position);
        if (liveVideoGoods == null) {
            return;
        }
        if (getDefItemViewType(position) == 0) {
            CornerImageView goodsImg = (CornerImageView) holder.getView(R.id.goodsImg);
            TextView goodsLable = (TextView) holder.getView(R.id.goodsLable);
            if (liveVideoGoods != null) {
                if (liveVideoGoods.headImages != null) {
                    com.healthy.library.businessutil.GlideCopy.with(context)
                            .load(liveVideoGoods.headImages.get(0).filePath)

                            .placeholder(R.drawable.img_1_1_default)
                            .error(R.drawable.img_1_1_default)
                            .into(goodsImg);
                }
                goodsLable.setText(liveVideoGoods.ranking + "");
            }
        } else {
            CornerImageView goodsImg = (CornerImageView) holder.getView(R.id.goodsImg);
            ImageView animImg = (ImageView) holder.getView(R.id.animImg);
            TextView goodsTitle = (TextView) holder.getView(R.id.goodsTitle);
            TextView goodsPrice = (TextView) holder.getView(R.id.goodsPrice);
            TextView redBtn = (TextView) holder.getView(R.id.redBtn);
            TextView greedBtn = (TextView) holder.getView(R.id.greedBtn);
            TextView goodsLable = (TextView) holder.getView(R.id.goodsLable);
            AutoClickImageView autoClickImageView=(AutoClickImageView) holder.itemView.findViewById(R.id.goodsShareCoupon);
            AnimationDrawable anim = (AnimationDrawable) animImg.getBackground();// 获取到动画资源
            anim.setOneShot(false); // 设置是否重复播放
            anim.start();// 开始动画
            if(liveVideoGoods.shareGiftDTOS!=null&&liveVideoGoods.shareGiftDTOS.size()>0){//
                autoClickImageView.setVisibility(View.VISIBLE);
                autoClickImageView.setCanTouch(false);
//                autoClickImageView.startLoopScaleAuto();
            }else {
                autoClickImageView.setVisibility(View.INVISIBLE);
//                autoClickImageView.clearLoop();
            }


            if (liveVideoGoods != null) {
                if (liveVideoGoods.headImages != null) {
                    com.healthy.library.businessutil.GlideCopy.with(context)
                            .load(liveVideoGoods.headImages.get(0).filePath)

                            .placeholder(R.drawable.img_1_1_default)
                            .error(R.drawable.img_1_1_default)
                            .into(goodsImg);
                }
                goodsLable.setText(liveVideoGoods.ranking + "");
//                SpannableString goodsTitleSpan = new SpannableString("正在讲解 " + liveVideoGoods.goodsTitle);
//                goodsTitleSpan.setSpan(new AbsoluteSizeSpan(11, true), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                goodsTitleSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#FA3C5A")), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                goodsTitleSpan.setSpan(new StyleSpan(Typeface.NORMAL), 0, goodsTitleSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //正常

                String goodsTitleTxt = "";
                if (liveVideoGoods.goodsTitle.length() > 12) {
                    goodsTitleTxt = liveVideoGoods.goodsTitle.substring(0, 12);
                    goodsTitleTxt = goodsTitleTxt + "...";
                } else {
                    goodsTitleTxt = liveVideoGoods.goodsTitle;
                }
                goodsTitle.setText(goodsTitleTxt);

                if (liveVideoGoods.goodsChildren != null) {
                    goodsPrice.setText("¥" + FormatUtils.moneyKeep2Decimals(liveVideoGoods.getGoodsChildrenLivePrice()));
                }

            }
            errMsg = null;
            if (liveVideoGoods.offShelf == 0) {//上架可以抢
                if (liveVideoGoods.availableInventory <= 0) {//表示已经售完
                    errMsg = "已抢完";
                }
            } else {
                errMsg = "等待主播开启抢购";

            }
            if (!TextUtils.isEmpty(errMsg) && errMsg.contains("开启抢购")) {
                redBtn.setBackgroundResource(R.drawable.shape_live_shopping_goods_list_gray);
                redBtn.setTextColor(Color.parseColor("#FFFFFF"));
                redBtn.setText("等待开抢");
            } else if (!TextUtils.isEmpty(errMsg) && errMsg.contains("已抢完")) {
                redBtn.setBackgroundResource(R.drawable.shape_live_shopping_goods_list_gray);
                redBtn.setTextColor(Color.parseColor("#FFFFFF"));
                redBtn.setText("已抢完");
            } else {
                redBtn.setBackgroundResource(R.drawable.shape_live_shopping_goods_list_red);
                redBtn.setTextColor(Color.parseColor("#FFFFFF"));
                redBtn.setText("马上抢");
            }
            redBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!TextUtils.isEmpty(errMsg)) {
                        Toast.makeText(context, errMsg, Toast.LENGTH_SHORT).show();
                    } else {

                        goLiveGoods(liveVideoGoods);
                    }

                }
            });
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goLiveGoods(liveVideoGoods);
            }
        });
    }

    private void goLiveGoods(final LiveVideoGoods liveVideoGoods) {
        String errMsg = null;
        if (liveVideoGoods.offShelf == 0 && liveVideoGoods.getGoodsIsOffShelf()) {//上架可以抢
            if (liveVideoGoods.availableInventory == 0) {
                errMsg = "已抢完";
                Toast.makeText(context, "该商品已抢光", Toast.LENGTH_SHORT).show();
            }
        } else {
            errMsg = "等待主播开启抢购";

        }
        GoodsSimpleDialog goodsSimpleDialog = new GoodsSimpleDialog();
        goodsSimpleDialog.setErrMsg(errMsg)
                .setLiveInfo(mLiveRoomInfo)
                .setSeachMap(new SimpleHashMapBuilder<String, Object>().puts(Functions.FUNCTION, "9292").puts("liveGoodsId", liveVideoGoods.id))
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
                .show(activity.getSupportFragmentManager(), "商品弹窗");
    }


    public void setActivity(LookLiveActivity activity) {
        this.activity = activity;
    }

    public LookLiveActivity getActivity() {
        return activity;
    }
}
