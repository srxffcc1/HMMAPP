package com.health.discount.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.discount.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.businessutil.GlideCopy;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.KKGroup;
import com.healthy.library.routes.SecondRoutes;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.CornerImageView;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

public class NewAssembleFooterAdapter extends BaseAdapter<KKGroup> {
    public String adCode;
    public String lat;
    public String lng;

    public void setLocation(String adCode, String lat, String lng) {
        this.adCode = adCode;
        this.lat = lat;
        this.lng = lng;
    }

    public NewAssembleFooterAdapter() {
        this(R.layout.new_assemble_footer_layout);
    }

    public NewAssembleFooterAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, final int position) {
        final KKGroup kick = getDatas().get(position);
        CornerImageView goodsImg = holder.getView(R.id.goodsImg);
        TextView goodsTitle = holder.getView(R.id.goodsTitle);
        TextView goodsPlatformPrice = holder.getView(R.id.goodsPlatformPrice);
        TextView goodsPrice = holder.getView(R.id.goodsPrice);
        TextView goAssemble = holder.getView(R.id.goAssemble);
        TextView regimentSize = holder.getView(R.id.regimentSize);

        ImageView mHeadIcon3 = holder.getView(R.id.head_icon3);
        ImageView mHeadIcon2 = holder.getView(R.id.head_icon2);
        ImageView mHeadIcon1 = holder.getView(R.id.head_icon1);
        mHeadIcon3.setVisibility(View.GONE);
        mHeadIcon2.setVisibility(View.GONE);
        mHeadIcon1.setVisibility(View.GONE);
        TextView mRegimentCount = holder.getView(R.id.regimentCount);
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
                        GlideCopy.with(mHeadIcon3.getContext())
                                .load(mUrl)
                                .placeholder(R.drawable.img_avatar_default)
                                .error(R.drawable.img_avatar_default)
                                .into(mHeadIcon3);
                    }
                    if (i == 1) {
                        mHeadIcon2.setVisibility(View.VISIBLE);
                        GlideCopy.with(mHeadIcon2.getContext())
                                .load(mUrl)
                                .placeholder(R.drawable.img_avatar_default)
                                .error(R.drawable.img_avatar_default)
                                .into(mHeadIcon2);
                    }
                    if (i == 2) {
                        mHeadIcon1.setVisibility(View.VISIBLE);
                        GlideCopy.with(mHeadIcon1.getContext())
                                .load(mUrl)
                                .placeholder(R.drawable.img_avatar_default)
                                .error(R.drawable.img_avatar_default)
                                .into(mHeadIcon1);
                    }
                }
            }
            mRegimentCount.setText(joinNum + "人已拼");
        } else {
            mRegimentCount.setText("");
        }
        com.healthy.library.businessutil.GlideCopy.with(context)
                .load(kick.goodsImage)
                .error(R.drawable.img_1_1_default)
                .placeholder(R.drawable.img_1_1_default2)
                .into(goodsImg);
        if (kick.goodsTitle != null && !TextUtils.isEmpty(kick.goodsTitle)) {
            goodsTitle.setText(kick.goodsTitle);
        }
        //goodsPlatformPrice.setText("单买价 ¥" + FormatUtils.moneyKeep2Decimals(kick.goodsPlatformPrice));
        goodsPlatformPrice.setText("");
        goodsPlatformPrice.setVisibility(View.GONE);
        goodsPrice.setText("¥" + FormatUtils.moneyKeep2Decimals(kick.assemblePrice));
        regimentSize.setText(kick.regimentSize + "人团");
        goAssemble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(context, "event2APPPinListGoodsClick", new SimpleHashMapBuilder<String, String>().puts("soure", "马上拼按钮点击量"));
                ARouter.getInstance()
                        .build(SecondRoutes.SECOND_SERVICE_DETAIL)
                        .withString("assembleId", kick.id)
                        .withString("shopId", SpUtils.getValue(context, SpKey.CHOSE_SHOP))
                        .navigation();
            }
        });
        goodsTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(SecondRoutes.SECOND_SERVICE_DETAIL)
                        .withString("assembleId", kick.id)
                        .withString("shopId", SpUtils.getValue(context, SpKey.CHOSE_SHOP))
                        .navigation();

            }
        });
        goodsImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(context, "event2APPPinListGoodsImgClick", new SimpleHashMapBuilder<String, String>().puts("soure", "拼团商品图片点击量"));
                ARouter.getInstance()
                        .build(SecondRoutes.SECOND_SERVICE_DETAIL)
                        .withString("assembleId", kick.id)
                        .withString("shopId", SpUtils.getValue(context, SpKey.CHOSE_SHOP))
                        .navigation();
            }
        });
    }

}
