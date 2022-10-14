package com.health.servicecenter.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.health.servicecenter.R;
import com.health.servicecenter.model.RecommendModel;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;

/**
 * @author: long
 * @date: 2021/4/15
 * @des
 */
public class PointsSignInGoodsAdapter extends BaseAdapter<RecommendModel.RecommendGoods> {

    public PointsSignInGoodsAdapter() {
        //super(R.layout.item_container_layout);
        super(R.layout.item_points_signin_goods_data_layout);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(3);
        gridLayoutHelper.setBgColor(Color.parseColor("#ffffff"));
        gridLayoutHelper.setAutoExpand(false);
        return gridLayoutHelper;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        final RecommendModel.RecommendGoods recommendGoods = getDatas().get(position);

        com.healthy.library.businessutil.GlideCopy.with(context)
                .load(recommendGoods.filePath)
                .placeholder(R.drawable.img_1_1_default)
                .error(R.drawable.img_1_1_default)
                .into((ImageView) holder.getView(R.id.item_iv_goods_data));

        holder.setText(R.id.item_signin_goods_title, recommendGoods.goodsTitle);
        holder.setText(R.id.item_PointsRealPrice, recommendGoods.getPointsRealPrice());
        TextView blockGoodsTitle = holder.getView(R.id.item_signin_goods_title);

        ImageView blockGoodsEmpty = (ImageView) holder.itemView.findViewById(R.id.blockGoodsEmpty);
        if ("1".equals(recommendGoods.isSaleOut)) {
            blockGoodsTitle.setTextColor(Color.parseColor("#868799"));
            //blockGoodsPoint.setTextColor(Color.parseColor("#868799"));
            blockGoodsEmpty.setVisibility(View.VISIBLE);
        } else {
            blockGoodsTitle.setTextColor(Color.parseColor("#222222"));
            //blockGoodsPoint.setTextColor(Color.parseColor("#F02846"));
            blockGoodsEmpty.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moutClickListener != null) {
                    moutClickListener.outClick("积分商品", recommendGoods);
                }
            }
        });
    }
}
