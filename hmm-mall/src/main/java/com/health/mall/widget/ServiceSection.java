package com.health.mall.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.health.mall.R;
import com.healthy.library.model.GoodsModel;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.StringUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/05 10:00
 * @des 门店详情-可约服务
 */

public class ServiceSection extends FrameLayout implements View.OnClickListener {

    private GoodsModel mGoodsModel;
    private ImageView ivGoodsUrl;
    private TextView tvGoodsDiscount;
    private TextView tvGoodsName;
    private TextView tvGoodsEffect;
    private TextView tvGoodsPrice;
    String cityNo;
    String areaNo;
    String lng;
    String lat;
    String shopId;


    public ServiceSection(@NonNull Context context) {
        this(context, null);
    }

    public ServiceSection(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ServiceSection(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.item_available_goods, this);
        ivGoodsUrl = (ImageView) findViewById(R.id.iv_goods_url);
        tvGoodsDiscount = (TextView) findViewById(R.id.tv_goods_discount);
        tvGoodsName = (TextView) findViewById(R.id.tv_goods_name);
        tvGoodsEffect = (TextView) findViewById(R.id.tv_goods_effect);
        tvGoodsPrice = (TextView) findViewById(R.id.tv_goods_price);
        setOnClickListener(this);
    }
    public void setGoodsModel(GoodsModel model,String lng,String lat,String shopId,String cityNo) {
        mGoodsModel = model;
        this.cityNo=cityNo;
        this.lat=lat;
        this.lng=lng;
        this.shopId=shopId;
        if (model.storePlatformPriceDiscount == 0.0||model.storePlatformPriceDiscount==1) {
            tvGoodsDiscount.setVisibility( View.GONE);
        } else {
            tvGoodsDiscount.setVisibility( View.VISIBLE);
            tvGoodsDiscount.setText(String.format("%s折", String.format("%.1f", model.storePlatformPriceDiscount*10)));
        }
        tvGoodsName.setText(model.goodsTitle);
        tvGoodsEffect.setText(model.description);
        tvGoodsPrice.setText("¥"+ StringUtils.getResultPrice(model.platformPrice+""));
        com.healthy.library.businessutil.GlideCopy.with(getContext())
                .load(model.headImages.get(0))
                .into(ivGoodsUrl);
    }

    public void setGoodsModel(GoodsModel model) {
        mGoodsModel = model;
    }

    @Override
    public void onClick(View v) {
        if (mGoodsModel != null) {
//            ARouter.getInstance()
//                    .build(ServiceRoutes.SERVICE_DETAIL)
//                    .withString("selfId", mGoodsModel.getId())
//                    .withString("shopId", mGoodsModel.getShopId())
//                    .withString("systemType", mGoodsModel.getSystemType())
//                    .navigation();
            Map nokmap = new HashMap<String, String>();
            nokmap.put("soure","商户详情页");
            MobclickAgent.onEvent(getContext(), "event2GoodsDetailClick",nokmap);


            ARouter.getInstance()
                    .build(ServiceRoutes.SERVICE_DETAIL)
                    .withString("cityNo",cityNo)
                    .withString("areaNo",cityNo)
                    .withString("lng",lng)
                    .withString("lat",lat)
                    .withString("shopId",shopId)
                    .withString("goodsId",mGoodsModel.id)
                    .navigation();
        }
    }

    private void initView() {

    }
}