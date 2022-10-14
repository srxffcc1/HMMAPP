package com.health.discount.adapter;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.health.discount.R;
import com.health.discount.model.PointGoodsList;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.routes.ServiceRoutes;

/**
 * @author Li
 * @date 2019/03/25 11:03
 * @des 推荐阅读
 */

public class PointIcanAdapter extends BaseAdapter<PointGoodsList> {
    public PointIcanAdapter() {
        this(R.layout.dis_item_activity_point_ican);
    }

    public PointIcanAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, final int position) {
        ImageView goodsIcon = holder.getView(R.id.goodsIcon);
        ImageView goodsSoldOutIcon = holder.getView(R.id.goodsSoldOutIcon);
        TextView goodsTitle = holder.getView(R.id.goodsTitle);
        TextView goodsPriceOrg = holder.getView(R.id.goodsPriceOrg);
        TextView goodsPrice = holder.getView(R.id.goodsPrice);
        final TextView passOrder = holder.getView(R.id.passOrder);
        final PointGoodsList listModel = getDatas().get(position);
        if (listModel.isSaleOut != null && "1".equals(listModel.isSaleOut)) {
            goodsTitle.setTextColor(Color.parseColor("#868799"));
            goodsPrice.setTextColor(Color.parseColor("#868799"));
            goodsSoldOutIcon.setVisibility(View.VISIBLE);
            passOrder.setAlpha(0.7f);
        } else {
            goodsTitle.setTextColor(Color.parseColor("#222222"));
            goodsPrice.setTextColor(Color.parseColor("#F02846"));
            goodsSoldOutIcon.setVisibility(View.GONE);
            passOrder.setAlpha(1f);
        }
        com.healthy.library.businessutil.GlideCopy.with(context)
                .load(listModel.filePath)
                .error(R.drawable.img_1_1_default)
                .placeholder(R.drawable.img_1_1_default)
                .into(goodsIcon);
        goodsTitle.setText(listModel.goodsTitle);
        if (listModel.retailPrice != null) {
            goodsPriceOrg.setText("¥" + listModel.retailPrice);
            goodsPriceOrg.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            goodsPriceOrg.setText("");
        }
        goodsPrice.setText(listModel.getPointsRealPrice());
        if (listModel.isSaleOut != null && "1".equals(listModel.isSaleOut)) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "此商品已兑完", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ARouter.getInstance()
                            .build(ServiceRoutes.SERVICE_DETAIL)
                            .withString("id", listModel.id)
                            .withString("marketingType", "5")
                            .navigation();
                }
            });
        }

    }
}
