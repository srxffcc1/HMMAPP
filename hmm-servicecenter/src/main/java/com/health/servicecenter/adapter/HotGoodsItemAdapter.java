package com.health.servicecenter.adapter;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.health.servicecenter.R;
import com.healthy.library.model.HotGoodsList;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.widget.CornerImageView;

public class HotGoodsItemAdapter extends BaseAdapter<HotGoodsList> {

    public void setOnBasketClickListener(MallGoodsItemAdapter.OnBasketClickListener onBasketClickListener) {
        this.onBasketClickListener = onBasketClickListener;
    }

    MallGoodsItemAdapter.OnBasketClickListener onBasketClickListener;

    @Override
    public int getItemViewType(int position) {
        return 8;
    }

    public HotGoodsItemAdapter() {
        this(R.layout.item_mall_goods_2);

    }

    private HotGoodsItemAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        LinearLayoutHelper helper = new LinearLayoutHelper();
        return helper;
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

        mall_service_title.setText(getDatas().get(position).getGoodsTitle());
//        if (getData().get(position).getSpecCell() != null) {
//            if (getData().get(position).getSpecCell().get(0).specValue != null && getData().get(position).getSpecCell().get(0).specValue.length > 0) {
//                mall_service_space.setText(getData().get(position).getSpecCell().get(0).specValue[0]);
//            }
//        }
        spinnerImg.setVisibility(View.GONE);
        mall_service_space.setText(getDatas().get(position).getSkuName());
        if (!TextUtils.isEmpty(getDatas().get(position).getSkuName())) {

            spinnerImg.setVisibility(View.VISIBLE);
        }
        mall_service_money_left.setText("");
        SpannableString priceStr = new SpannableString("¥" + FormatUtils.moneyKeep2Decimals(getDatas().get(position).getPlatformPrice()));
        priceStr.setSpan(new AbsoluteSizeSpan(13, true), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        priceStr.setSpan(new StyleSpan(Typeface.NORMAL), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //正常
        mall_service_money.setText(priceStr);
        if (getDatas().get(position).getStorePrice() > 0) {
            mall_service_money_old.setText("¥" + FormatUtils.moneyKeep2Decimals(getDatas().get(position).getStorePrice()));
            mall_service_money_old.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线

        } else {
            mall_service_money_old.setText("");
        }
        com.healthy.library.businessutil.GlideCopy.with(context).load(getDatas().get(position).getFilePath())
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
                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_DETAIL)
                        .withString("id", getDatas().get(position).getGoodsId() + "")
                        .navigation();
            }
        });


    }
}
