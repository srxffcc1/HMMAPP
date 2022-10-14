package com.health.discount.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.health.discount.R;
import com.health.discount.model.PointTab;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.GlideOptions;
import com.healthy.library.utils.TransformUtil;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * @author Li
 * @date 2019/03/25 11:03
 * @des 推荐阅读
 */

public class PointBlockHomeRecommendAdapter extends BaseAdapter<PointTab.PointGoods> {


    public PointBlockHomeRecommendAdapter() {
        this(R.layout.dis_function_block_big);
    }

    public PointBlockHomeRecommendAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(2);
        gridLayoutHelper.setMarginLeft(8);
        gridLayoutHelper.setMarginRight(8);
        gridLayoutHelper.setAutoExpand(false);
        return gridLayoutHelper;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, final int position) {
        final PointTab.PointGoods pointGoods= getDatas().get(position);
         ConstraintLayout parentCategory;
         ImageView blockGoodsIcon;
         TextView blockGoodsTitle;
         TextView blockGoodsPoint;
        parentCategory = (ConstraintLayout) holder.itemView.findViewById(R.id.parent_category);
        blockGoodsIcon = (ImageView) holder.itemView.findViewById(R.id.blockGoodsIcon);
        blockGoodsTitle = (TextView) holder.itemView.findViewById(R.id.blockGoodsTitle);
        blockGoodsPoint = (TextView) holder.itemView.findViewById(R.id.blockGoodsPoint);

        ImageView blockGoodsEmpty= (ImageView) holder.itemView.findViewById(R.id.blockGoodsEmpty);
        if("1".equals(pointGoods.isSaleOut)){
            blockGoodsTitle.setTextColor(Color.parseColor("#868799"));
            blockGoodsPoint.setTextColor(Color.parseColor("#868799"));
            blockGoodsEmpty.setVisibility(View.VISIBLE);
        }else {
            blockGoodsTitle.setTextColor(Color.parseColor("#222222"));
            blockGoodsPoint.setTextColor(Color.parseColor("#F02846"));
            blockGoodsEmpty.setVisibility(View.GONE);
        }

        com.healthy.library.businessutil.GlideCopy.with(context)
                .load(pointGoods.filePath)
                .error(R.drawable.img_1_1_default2)
                .apply(GlideOptions.withRoundedOptions((int) TransformUtil.dp2px(context,5f), RoundedCornersTransformation.CornerType.TOP, R.drawable.img_690_260_default, R.drawable.img_690_260_default))
                .placeholder(R.drawable.img_1_1_default2)
                .into(blockGoodsIcon);
        blockGoodsTitle.setText(pointGoods.goodsTitle);
        blockGoodsPoint.setText(pointGoods.getPointsRealPrice());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_DETAIL)
                        .withString("id", pointGoods.id)
                        .withString("marketingType","5")
                        .navigation();
            }
        });

    }

    private void initView() {

    }
}
