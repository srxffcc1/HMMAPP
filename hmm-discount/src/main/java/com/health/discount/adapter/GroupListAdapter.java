package com.health.discount.adapter;

import android.util.Base64;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.discount.R;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.KKGroup;
import com.healthy.library.routes.SecondRoutes;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.StringUtils;
import com.healthy.library.widget.CornerImageView;

import java.util.Date;

/**
 * @author Li
 * @date 2019/03/25 11:03
 * @des 推荐阅读
 */

public class GroupListAdapter extends BaseQuickAdapter<KKGroup, BaseViewHolder> {
    public String adCode;
    public String lat;
    public String lng;
    public void setLocation(String adCode, String lat, String lng) {
        this.adCode = adCode;
        this.lat = lat;
        this.lng = lng;
    }

    public GroupListAdapter() {
        this(R.layout.dis_item_activity_group_list_main);
    }

    private GroupListAdapter(int layoutResId) {
        super(layoutResId);
    }


    @Override
    protected void convert(final BaseViewHolder helper, final KKGroup item) {
         ConstraintLayout goodsView;
         CornerImageView goodsIcon;
         TextView goodsName;
         TextView goodsSecondName;
         TextView goodsGroupPin;
         TextView goodsGroupPinMoney;
         TextView goodsGroupSingleMoney;
         TextView groupButton;
         TextView goodsAddress;
        TextView goodsAddressSmall;
        goodsView = (ConstraintLayout) helper.itemView.findViewById(R.id.goodsView);
        goodsIcon = (CornerImageView) helper.itemView.findViewById(R.id.goodsIcon);
        goodsName = (TextView)helper.itemView. findViewById(R.id.goodsName);
        goodsSecondName = (TextView)helper.itemView. findViewById(R.id.goodsSecondName);
        goodsGroupPin = (TextView) helper.itemView.findViewById(R.id.goodsGroupPin);
        goodsGroupPinMoney = (TextView) helper.itemView.findViewById(R.id.goodsGroupPinMoney);
        goodsGroupSingleMoney = (TextView) helper.itemView.findViewById(R.id.goodsGroupSingleMoney);
        groupButton = (TextView) helper.itemView.findViewById(R.id.groupButton);
        goodsAddress = (TextView) helper.itemView.findViewById(R.id.goodsAddress);
        goodsAddressSmall=(TextView) helper.itemView.findViewById(R.id.goodsAddressSmall);
        com.healthy.library.businessutil.GlideCopy.with(mContext)
                .load(item.goodsImage)
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_1_1_default)

                .into(goodsIcon);
        goodsName.setText(item.goodsTitle);
        goodsSecondName.setText(item.goodsDescription);
        goodsGroupPin.setText(item.regimentSize+"人团");
        goodsGroupPinMoney.setText("￥"+StringUtils.getResultPrice(item.assemblePrice+""));
        goodsGroupSingleMoney.setText("单买价￥"+StringUtils.getResultPrice(item.goodsPlatformPrice+""));
        goodsAddress.setText(item.shopName);
        goodsAddressSmall.setText("("+item.addressDetails+")");
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(SecondRoutes.SECOND_SERVICE_DETAIL)
                        .withString("cityNo",(Integer.parseInt(adCode) / 100 * 100)+"")
                        .withString("areaNo",adCode)
                        .withString("lng",lng)
                        .withString("lat",lat)
                        .withString("shopId",item.shopId+"")
                        .withString("assembleId",item.id)
                        .navigation();
            }
        });
        groupButton.setOnClickListener(new View.OnClickListener() {
            @Override
//                    map.put("assembleId", assembleId);
//            map.put("teamNum",teamNum);
            public void onClick(View v) {
                String singleNumber=new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT))+new Date().getTime();
//                ARouter.getInstance()
//                        .build(MallRoutes.MALL_ORDER)
//                        .withString("cityNo",(Integer.parseInt(adCode) / 100 * 100)+"")
//                        .withString("areaNo",adCode)
//                        .withString("lng",lng+"")
//                        .withString("lat",lat+"")
//                        .withString("shopId",item.shopId+"")
//                        .withString("goodsId",item.goodsId+"")
//                        .withString("orderType","4")
//                        .withString("platformPrice",item.assemblePrice+"")
//                        .withString("assembleId",item.id)
//                        .withString("teamNum", (TextUtils.isEmpty(item.teamNum)?singleNumber:item.teamNum))
//                        .navigation();
            }
        });

    }

    private void initView() {


    }
}
