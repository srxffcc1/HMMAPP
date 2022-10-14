package com.health.discount.adapter;

import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.discount.R;
import com.healthy.library.model.Kick;
import com.healthy.library.routes.SecondRoutes;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.ParseUtils;
import com.healthy.library.widget.CornerImageView;

import java.util.Locale;


/**
 * @author Li
 * @date 2019/03/25 11:03
 * @des 推荐阅读
 */

public class KickListAdapter extends BaseQuickAdapter<Kick, BaseViewHolder> {

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String shopId;
    public KickListAdapter() {
        this(R.layout.dis_item_activity_disact);
    }

    private KickListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, final Kick item) {

         CornerImageView goodsIcon;
         TextView goodsName;
         TextView goodsPrice;
         TextView goodsDis;
         TextView goodsJoinNum;
         TextView goodsAddress;
         TextView goodsDistance;
         TextView goodsButton;


        goodsIcon = (CornerImageView) helper.getView(R.id.goodsIcon);
        goodsName = (TextView) helper.getView(R.id.goodsName);
        goodsPrice = (TextView) helper.getView(R.id.goodsPrice);
        goodsDis = (TextView) helper.getView(R.id.goodsDisMoney);
        goodsJoinNum = (TextView) helper.getView(R.id.goodsJoinNum);
        goodsAddress = (TextView) helper.getView(R.id.goodsAddress);
        goodsDistance = (TextView) helper.getView(R.id.goodsDistance);
        goodsButton = (TextView) helper.getView(R.id.goodsButton);

        com.healthy.library.businessutil.GlideCopy.with(mContext)
                .asBitmap()
                .load(item.goodsImage)
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_avatar_default)
                
                .into(goodsIcon);
        goodsName.setText(item.goodsTitle);
        goodsPrice.setText(String.format(Locale.CHINA, "¥%s",
                FormatUtils.moneyKeep2Decimals(item.goodsPlatformPrice)));
        goodsDis.setText(String.format(Locale.CHINA, "¥%s",
                FormatUtils.moneyKeep2Decimals(item.floorPrice)));
        goodsJoinNum.setText("已有"+item.joinNum+"人参加");
        goodsAddress.setVisibility(View.VISIBLE);
        goodsDistance.setVisibility(View.VISIBLE);
        goodsAddress.setText(item.addressDetails);
        try {
            goodsDistance.setText(ParseUtils.parseDistance(Double.parseDouble(item.distance)*1000+""));
        } catch (Exception e) {
            goodsAddress.setVisibility(View.GONE);
            goodsDistance.setVisibility(View.GONE);
            e.printStackTrace();
        }
        goodsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ARouter.getInstance()
                        .build(SecondRoutes.SECOND_SERVICE_DETAIL)
                        .withString("bargainId", item.id)
                        .withString("bargainMemberId", item.bargainMemberId)
                        .withString("shopId", shopId)
                        .navigation();

//                ARouter.getInstance()
//                        .build(DiscountRoutes.DIS_KICKDETAIL)
//                        .withString("bargainId",item.id)
//                        .withString("marketingShopId",item.getDeliveryShopId(mContext))
//                        .withString("deliveryShopId",item.getDeliveryShopId(mContext))
//                        .withString("marketingGoodsChildId",item.marketingGoodsChildId)
//                        .navigation();


            }
        });

    }

}
