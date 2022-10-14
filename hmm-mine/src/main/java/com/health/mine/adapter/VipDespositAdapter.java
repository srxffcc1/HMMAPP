package com.health.mine.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.mine.R;
import com.healthy.library.model.VipDeposit;
import com.healthy.library.routes.MineRoutes;
import com.healthy.library.utils.SpanUtils;
import com.healthy.library.widget.ExpandTextView;
import com.healthy.library.widget.ImageTextView;

/**
 * @author Li
 * @date 2019/03/25 11:03
 * @des 推荐阅读
 */

public class VipDespositAdapter extends BaseQuickAdapter<VipDeposit, BaseViewHolder> {

    private String cardNo;
    private String ytbAppId;

    public VipDespositAdapter() {
        this(R.layout.mine_item_deposit);
    }

    private VipDespositAdapter(int layoutResId) {
        super(layoutResId);

    }

    public void setAPPId(String cardNo, String ytbAppId) {
        this.cardNo = cardNo;
        this.ytbAppId = ytbAppId;
    }

    @Override
    protected void convert(BaseViewHolder helper, final VipDeposit item) {

        ConstraintLayout unitLL;
        ConstraintLayout shopLL;
        ImageView unitGoodsLeft;
        TextView unitGoods;
        TextView unitSKU;
        TextView unitLess;
        TextView unitAll;
        ImageView canUseCardStoreButtonLeft;
        ImageTextView canUseCardStoreButton;
        TextView lasterShop;
        TextView detial;
        final LinearLayout canUseCardStoreScP;
        final ExpandTextView tvCommentContent;
        final ImageTextView recoverpagecontent;
        ScrollView canUseCardStoreSc;
        LinearLayout canUseCardStoreScLL;

        unitLL = (ConstraintLayout) helper.itemView.findViewById(R.id.unitLL);
        shopLL = (ConstraintLayout) helper.itemView.findViewById(R.id.shopLL);
        unitGoodsLeft = (ImageView) helper.itemView.findViewById(R.id.unitGoodsLeft);
        unitGoods = (TextView) helper.itemView.findViewById(R.id.unitGoods);
        unitSKU = (TextView) helper.itemView.findViewById(R.id.unitSKU);
        unitLess = (TextView) helper.itemView.findViewById(R.id.unitLess);
        unitAll = (TextView) helper.itemView.findViewById(R.id.unitAll);
        canUseCardStoreButtonLeft = (ImageView) helper.itemView.findViewById(R.id.canUseCardStoreButtonLeft);
        canUseCardStoreButton = (ImageTextView) helper.itemView.findViewById(R.id.canUseCardStoreButton);
        lasterShop = (TextView) helper.itemView.findViewById(R.id.lasterShop);
        detial = (TextView) helper.itemView.findViewById(R.id.detial);
        canUseCardStoreScP = (LinearLayout) helper.itemView.findViewById(R.id.canUseCardStoreScP);
        tvCommentContent = (ExpandTextView) helper.itemView.findViewById(R.id.tv_comment_content);
        recoverpagecontent = (ImageTextView) helper.itemView.findViewById(R.id.recoverpagecontent);
        canUseCardStoreSc = (ScrollView) helper.itemView.findViewById(R.id.canUseCardStoreSc);
        canUseCardStoreScLL = (LinearLayout) helper.itemView.findViewById(R.id.canUseCardStoreScLL);

        unitGoods.setText(item.GoodsName);
//        unitLess.setText(SpanUtils.getBuilder(mContext, "剩余").setForegroundColor(Color.parseColor("#1C1C1C"))
//                .append(item.YeNumber).setForegroundColor(Color.parseColor("#F94060"))
//                .append("件").setForegroundColor(Color.parseColor("#1C1C1C"))
//                .create());
//        canUseCardStoreButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                item.isShowCan = !item.isShowCan;
//                if (item.isShowCan) {
//                    canUseCardStoreScP.setVisibility(View.VISIBLE);
//                } else {
//                    canUseCardStoreScP.setVisibility(View.GONE);
//                }
//            }
//        });
        unitSKU.setText("SKU " + item.StuffNo);
        unitLess.setText("x" + item.YeNumber);
        tvCommentContent.clearText();
        recoverpagecontent.setVisibility(View.GONE);
        if (item.depositList != null && item.depositList.size() > 0) {
            shopLL.setVisibility(View.VISIBLE);
            lasterShop.setText("最后服务门店 " + getDepartName(item.depositList.get(0).DepartName.trim()));
        } else {
            shopLL.setVisibility(View.GONE);
        }
        if (item.GoodsType != null && item.GoodsType.equals("1")) {
            detial.setText("存取明细");
        } else {
            detial.setText("服务明细");
        }
        //        if (item.LimiteDepartID.contains("不限")) {
//            tvCommentContent.setText("不限门店，所有门店通用。");
//        } else {
//            tvCommentContent.setText(item.LimiteDepartID);
//        }
//        tvCommentContent.setCallback(new ExpandTextView.Callback() {
//            @Override
//            public void onExpand() {
//                ////System.out.println("需要收起");
//                recoverpagecontent.setVisibility(View.VISIBLE);
//                recoverpagecontent.setText("收起");
//
//            }
//
//            @Override
//            public void onCollapse() {
////                ////System.out.println("需要展开");
//                recoverpagecontent.setVisibility(View.VISIBLE);
//                recoverpagecontent.setText("查看更多");
//
//            }
//
//            @Override
//            public void onLoss() {
////                ////System.out.println("需要消失");
//                recoverpagecontent.setVisibility(View.GONE);
//            }
//        });
//        tvCommentContent.setChanged(item.isShowContent);
//        recoverpagecontent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                item.isShowContent = !item.isShowContent;
//                tvCommentContent.setChanged(item.isShowContent);
//            }
//        });
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(MineRoutes.MINE_VIPDEPOSITDETIAL)
                        .withString("GoodsType", item.GoodsType)
                        .withString("cardNo", cardNo)
                        .withString("ytbAppId", ytbAppId)
                        .withObject("VipDeposit", item)
                        .navigation();
            }
        });

    }

    private String getDepartName(String DepartName) {
        if (DepartName.contains("】")) {
            return DepartName.substring(DepartName.lastIndexOf("】") + 1, DepartName.length());
        }
        return DepartName;
    }
}
