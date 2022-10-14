package com.health.discount.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.health.discount.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.Kick;
import com.healthy.library.routes.SecondRoutes;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.CornerImageView;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

public class NewKickFooterAdapter extends BaseAdapter<Kick> {

    public NewKickFooterAdapter() {
        this(R.layout.new_kick_footer_layout);
    }

    public NewKickFooterAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, final int position) {
        final Kick kick = getDatas().get(position);
        CornerImageView goodsImg = holder.getView(R.id.goodsImg);
        TextView goodsTitle = holder.getView(R.id.goodsTitle);
        TextView goodsPlatformPrice = holder.getView(R.id.goodsPlatformPrice);
        TextView floorPrice = holder.getView(R.id.floorPrice);
        ConstraintLayout footerConstraintLayout = holder.getView(R.id.footerConstraintLayout);

        ImageView mHeadIcon3 = holder.getView(R.id.head_icon3);
        ImageView mHeadIcon2 = holder.getView(R.id.head_icon2);
        ImageView mHeadIcon1 = holder.getView(R.id.head_icon1);
        mHeadIcon3.setVisibility(View.GONE);
        mHeadIcon2.setVisibility(View.GONE);
        mHeadIcon1.setVisibility(View.GONE);
        TextView mKickCount = holder.getView(R.id.kickCount);
        if (kick.joinNum > 0) {
            long joinNum = kick.joinNum;
            List<String> faceUrlList = kick.faceUrlList;
            if (!ListUtil.isEmpty(faceUrlList)) {
                long forSize = joinNum > 3 ? 3 : joinNum;
                if (forSize > faceUrlList.size()) {
                    forSize = faceUrlList.size();
                }
                for (int i = 0; i < forSize; i++) {
                    String mUrl = faceUrlList.get(i);
                    if (i == 0) {
                        mHeadIcon3.setVisibility(View.VISIBLE);
                        Glide.with(mHeadIcon3.getContext())
                                .load(mUrl)
                                .placeholder(R.drawable.img_avatar_default)
                                .error(R.drawable.img_avatar_default)
                                .into(mHeadIcon3);
                    }
                    if (i == 1) {
                        mHeadIcon2.setVisibility(View.VISIBLE);
                        Glide.with(mHeadIcon2.getContext())
                                .load(mUrl)
                                .placeholder(R.drawable.img_avatar_default)
                                .error(R.drawable.img_avatar_default)
                                .into(mHeadIcon2);
                    }
                    if (i == 2) {
                        mHeadIcon1.setVisibility(View.VISIBLE);
                        Glide.with(mHeadIcon1.getContext())
                                .load(mUrl)
                                .placeholder(R.drawable.img_avatar_default)
                                .error(R.drawable.img_avatar_default)
                                .into(mHeadIcon1);
                    }
                }
            }
            mKickCount.setText(joinNum + "人已砍");
        } else {
            mKickCount.setText("");
        }
        com.healthy.library.businessutil.GlideCopy.with(context)
                .load(kick.goodsImage)
                .error(R.drawable.img_1_1_default)
                .placeholder(R.drawable.img_1_1_default2)
                .into(goodsImg);
        if (kick.goodsType == 2) {
            goodsTitle.setMaxLines(2);
        } else {
            goodsTitle.setMaxLines(1);
        }


        goodsTitle.setText(kick.goodsTitle);
        //goodsPlatformPrice.setText("原价 ¥" + FormatUtils.moneyKeep2Decimals(kick.goodsPlatformPrice));
        goodsPlatformPrice.setText("");
        goodsPlatformPrice.setVisibility(View.GONE);
        floorPrice.setText("" + FormatUtils.moneyKeep2Decimals(kick.floorPrice));


        goodsTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(SecondRoutes.SECOND_SERVICE_DETAIL)
                        .withString("bargainId", kick.id)
                        .withString("bargainMemberId", kick.bargainMemberId)
                        .withString("shopId", SpUtils.getValue(context, SpKey.CHOSE_SHOP))
                        .navigation();

            }
        });
        footerConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(SecondRoutes.SECOND_SERVICE_DETAIL)
                        .withString("bargainId", kick.id)
                        .withString("bargainMemberId", kick.bargainMemberId)
                        .withString("shopId", SpUtils.getValue(context, SpKey.CHOSE_SHOP))
                        .navigation();

            }
        });
        goodsImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(context, "event2APPKanListGoodsImgClick", new SimpleHashMapBuilder<String, String>().puts("soure", "砍价商品图片点击量"));
                ARouter.getInstance()
                        .build(SecondRoutes.SECOND_SERVICE_DETAIL)
                        .withString("bargainId", kick.id)
                        .withString("bargainMemberId", kick.bargainMemberId)
                        .withString("shopId", SpUtils.getValue(context, SpKey.CHOSE_SHOP))
                        .navigation();
            }
        });
    }

}
