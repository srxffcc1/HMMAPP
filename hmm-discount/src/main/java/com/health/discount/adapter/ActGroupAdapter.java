package com.health.discount.adapter;

import android.view.View;
import android.widget.Space;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.discount.R;
import com.healthy.library.model.ActGroup;
import com.healthy.library.routes.DiscountRoutes;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.widget.CornerImageView;

/**
 * @author Li
 * @date 2019/03/25 11:03
 * @des 推荐阅读
 */

public class ActGroupAdapter extends BaseQuickAdapter<ActGroup, BaseViewHolder> {




    public ActGroupAdapter() {
        super(R.layout.dis_function_acts);
    }


    @Override
    protected void convert(final BaseViewHolder helper, final ActGroup item) {
         ConstraintLayout parentCategory;
         CornerImageView ivCategory;
         Space space;
         TextView hotTip;
         TextView tvCategory;
        parentCategory = (ConstraintLayout) helper.itemView.findViewById(R.id.parent_category);
        ivCategory = (CornerImageView) helper.itemView.findViewById(R.id.iv_category);
        space = (Space) helper.itemView.findViewById(R.id.space);
        hotTip = (TextView)helper.itemView. findViewById(R.id.hotTip);
        tvCategory = (TextView) helper.itemView.findViewById(R.id.tv_category);

        com.healthy.library.businessutil.GlideCopy.with(mContext)
                .load(item.goodsImage)
                .error(R.drawable.img_1_1_default)
                .placeholder(R.drawable.img_1_1_default2)
                .into(ivCategory);
        tvCategory.setText("￥"+ FormatUtils.moneyKeep2Decimals(item.assemblePrice));
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(DiscountRoutes.DIS_NEWASSEMBLEACTIVITY)
                        .navigation();
            }
        });
    }

    private void initView() {



    }
}
