package com.health.mine.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.bumptech.glide.Glide;
import com.health.mine.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.model.GoodsDetail;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.widget.StaggeredGridLayoutHelperFix;

public class VipPlusGoodsAdapter extends BaseAdapter<GoodsDetail> {



    public VipPlusGoodsAdapter() {
        this(R.layout.item_vip_right_good);
    }

    public VipPlusGoodsAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        StaggeredGridLayoutHelperFix helper = new StaggeredGridLayoutHelperFix();
        helper.setMargin(6, 0, 6, 0);
        helper.setLane(2);
        helper.setHGap(3);
        return helper;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, final int position) {
        ImageView plusGoodsImg;
        ConstraintLayout plusFlagLL;
        TextView plusMoneyFlag;
        TextView plusMoney;
        LinearLayout vipGoldP;
        TextView plusGoodsTitle;
         TextView normalMoney;
        normalMoney = (TextView) holder.itemView.findViewById(R.id.normal_money);
        plusGoodsImg = (ImageView) holder.itemView.findViewById(R.id.plus_goods_img);
        plusFlagLL = (ConstraintLayout) holder.itemView.findViewById(R.id.plus_flag_LL);
        plusMoneyFlag = (TextView) holder.itemView.findViewById(R.id.plus_money_flag);
        plusMoney = (TextView) holder.itemView.findViewById(R.id.plus_money);
        vipGoldP = (LinearLayout) holder.itemView.findViewById(R.id.vipGoldP);
        plusGoodsTitle = (TextView) holder.itemView.findViewById(R.id.plus_goods_title);
        final GoodsDetail goodsDetail = getDatas().get(position);
        com.healthy.library.businessutil.GlideCopy.with(context)
                .load(goodsDetail.getFilePath() != null ? goodsDetail.getFilePath() : null)
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_1_1_default)

                .into(plusGoodsImg);
        plusGoodsTitle.setText(goodsDetail.goodsTitle);
        normalMoney.setText(FormatUtils.moneyKeep2Decimals(goodsDetail.platformPrice));
        if (goodsDetail.getPlusPriceShow() > 0) {
            vipGoldP.setVisibility(View.VISIBLE);
            plusMoney.setText("¥"+FormatUtils.moneyKeep2Decimals(goodsDetail.getPlusPriceShow()));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_DETAIL)
                        .withString("id", goodsDetail.id + "")
                        .navigation();
            }
        });
    }

    private void initView() {

    }
}
