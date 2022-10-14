package com.health.servicecenter.adapter;

import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.bumptech.glide.Glide;
import com.health.servicecenter.R;
import com.healthy.library.model.SortGoodsListModel;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.widget.CornerImageView;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

public class MallGoodsItemHAdapter extends BaseAdapter<SortGoodsListModel> {


    public MallGoodsItemHAdapter() {
        this(R.layout.item_mall_goods_2);

    }

    public void setOnBasketClickListener(MallGoodsItemAdapter.OnBasketClickListener onBasketClickListener) {
        this.onBasketClickListener = onBasketClickListener;
    }

    MallGoodsItemAdapter.OnBasketClickListener onBasketClickListener;

    private MallGoodsItemHAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new GridLayoutHelper(2);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, final int position) {
        TextView mall_service_title = baseHolder.getView(R.id.mall_service_title);
        TextView mall_service_money_left = baseHolder.getView(R.id.mall_service_money_left);
        TextView mall_service_money = baseHolder.getView(R.id.mall_service_money);
        TextView mall_service_money_old = baseHolder.getView(R.id.mall_service_money_old);
        TextView mall_service_space = baseHolder.getView(R.id.mall_service_space);
        ImageView passbasket = baseHolder.getView(R.id.passbasket);
        CornerImageView mall_service_img = baseHolder.getView(R.id.mall_service_img);
        View spinnerImg = baseHolder.getView(R.id.spinnerImg);
        spinnerImg.setVisibility(View.GONE);
        mall_service_space.setVisibility(View.GONE);
        mall_service_title.setText(getDatas().get(position).getGoodsTitle());

        if (getDatas().get(position).getSpecCell() != null && getDatas().get(position).getSpecCell().size() > 0) {
            if (getDatas().get(position).getSpecCell().get(0).specValue != null && getDatas().get(position).getSpecCell().get(0).specValue.length > 0) {
                mall_service_space.setText(getDatas().get(position).getGoodsChildren().get(0).getGoodsSpecStr().toString());
                mall_service_space.setVisibility(View.VISIBLE);
                spinnerImg.setVisibility(View.VISIBLE);
            }
        }
        mall_service_money_left.setText("¥");
        mall_service_money.setText(FormatUtils.moneyKeep2Decimals(getDatas().get(position).getPlatformPrice()) + "");
        mall_service_money_old.setText("¥" + FormatUtils.moneyKeep2Decimals(getDatas().get(position).getStorePrice()));
        mall_service_money_old.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
        if (getDatas().get(position).getStorePrice() == 0) {
            mall_service_money_old.setVisibility(View.INVISIBLE);
        } else {
            mall_service_money_old.setVisibility(View.VISIBLE);
        }
        com.healthy.library.businessutil.GlideCopy.with(context).load(getDatas().get(position).getHeadImage())
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_1_1_default)

                .into(mall_service_img);
        passbasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moutClickListener != null) {
                    if (onBasketClickListener != null) {
                        onBasketClickListener.onBasketClick(v);
                    }
                    moutClickListener.outClick("addShopCat", getDatas().get(position));
                }
            }
        });
        baseHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map nokmap = new HashMap<String, String>();
                nokmap.put("soure", "搜索结果列表页商品");
                MobclickAgent.onEvent(context, "btn_APP__MaternalandChildGoods_CommodityDetails", nokmap);
                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_DETAIL)
                        .withString("id", getDatas().get(position).getId() + "")
                        .navigation();
            }
        });


    }
}
