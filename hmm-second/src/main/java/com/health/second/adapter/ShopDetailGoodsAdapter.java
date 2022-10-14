package com.health.second.adapter;

import android.graphics.Paint;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.StaggeredGridLayoutHelper;
import com.health.second.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.model.SortGoodsListModel;
import com.healthy.library.routes.SecondRoutes;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.widget.CornerImageView;

public class ShopDetailGoodsAdapter extends BaseAdapter<SortGoodsListModel> {

    private String merchantType = null;

    public ShopDetailGoodsAdapter() {
        this(R.layout.item_shop_detail_goods_layout);
    }

    public ShopDetailGoodsAdapter(int viewId) {
        super(viewId);
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    @Override
    public void onBindViewHolder(@NonNull final BaseHolder holder, int position) {

        CornerImageView goodsImg = (CornerImageView) holder.getView(R.id.goodsImg);
        TextView goodsTitle = (TextView) holder.getView(R.id.goodsTitle);
        TextView goodsMoneyFlag = (TextView) holder.getView(R.id.goodsMoneyFlag);
        TextView goodsMoneyValue = (TextView) holder.getView(R.id.goodsMoneyValue);
        TextView goodsMoneyValueOld = (TextView) holder.getView(R.id.goodsMoneyValueOld);

        final SortGoodsListModel listModel = getDatas().get(position);
        com.healthy.library.businessutil.GlideCopy.with(context)
                .load(listModel.getHeadImage())
                .into(goodsImg);
        goodsTitle.setText(listModel.getGoodsTitle());
        goodsMoneyValue.setText(FormatUtils.moneyKeep2Decimals(listModel.getPlatformPrice()));
        goodsMoneyValueOld.setText(FormatUtils.moneyKeep2Decimals(listModel.getRetailPrice()));
        goodsMoneyValueOld.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (merchantType != null && merchantType.equals("1")) {
                    ARouter.getInstance()
                            .build(SecondRoutes.SECOND_SERVICE_DETAIL)
                            .withString("goodsId", listModel.getId() + "")
                            .navigation();
                } else {
                    ARouter.getInstance()
                            .build(ServiceRoutes.SERVICE_DETAIL)
                            .withString("id", listModel.getId() + "")
                            .navigation();
                }
            }
        });
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        StaggeredGridLayoutHelper helper = new StaggeredGridLayoutHelper(2);
        helper.setMargin(10, 0, 10, 0);
        helper.setHGap(3);
        return helper;
    }

}
