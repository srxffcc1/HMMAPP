package com.health.tencentlive.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.tencentlive.R;
import com.health.tencentlive.model.RosterList;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.base.BaseMultiItemAdapter;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.widget.CornerImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class RosterListFragmentAdapter extends BaseAdapter<RosterList> {

    public RosterListFragmentAdapter() {
        this(R.layout.item_live_roster_list_layout);
    }

    public RosterListFragmentAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        TextView courseTitle = (TextView) holder.getView(R.id.courseTitle);
        CornerImageView shopImg = (CornerImageView) holder.getView(R.id.shopImg);
        TextView shopName = (TextView) holder.getView(R.id.shopName);
        TextView liveTime = (TextView) holder.getView(R.id.liveTime);
        LinearLayout giftLayout = (LinearLayout) holder.getView(R.id.giftLayout);
        RosterList rosterList = getDatas().get(position);
        if (rosterList != null) {
            if (rosterList.liveCourseDto!=null){
                courseTitle.setText(rosterList.liveCourseDto.courseTitle);
                shopName.setText(rosterList.liveCourseDto.merchantName);
                com.healthy.library.businessutil.GlideCopy.with(context)
                        .load(rosterList.liveCourseDto.picUrl)

                        .placeholder(R.drawable.img_1_1_default)
                        .error(R.drawable.img_1_1_default)
                        .into(shopImg);
                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    liveTime.setText(simpleDateFormat.format(simpleDateFormat.parse(rosterList.liveCourseDto.actualBeginTime)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (rosterList.liveBenefitAllItemDtos != null && rosterList.liveBenefitAllItemDtos.size() > 0) {
                giftLayout.removeAllViews();
                buildList(rosterList.liveBenefitAllItemDtos, giftLayout, rosterList.courseId, rosterList.liveCourseDto.merchantId, rosterList.liveCourseDto.shopId);
            }
        }
    }

    private void buildList(List<RosterList.LiveBenefitAllItemDtosBean> list, LinearLayout giftLayout, final String courseId, final String merchantId, final String shopId) {
        for (int i = 0; i < list.size(); i++) {
            final RosterList.LiveBenefitAllItemDtosBean bean = list.get(i);
            View view = null;
            if (bean.itemType == 1) {
                view = LayoutInflater.from(context).inflate(R.layout.item_live_roster_list_coupon_layout, giftLayout, false);
                ConstraintLayout cardParent = (ConstraintLayout) view.findViewById(R.id.cardParent);
                LinearLayout cardLeftLL = (LinearLayout) view.findViewById(R.id.cardLeftLL);
                TextView cardMoneyLeft = (TextView) view.findViewById(R.id.cardMoneyLeft);
                TextView cardMoney = (TextView) view.findViewById(R.id.cardMoney);
                TextView cardMoneyName = (TextView) view.findViewById(R.id.cardMoneyName);
                TextView cardFlag = (TextView) view.findViewById(R.id.cardFlag);
                TextView cardLimit = (TextView) view.findViewById(R.id.cardLimit);
                TextView cardTime = (TextView) view.findViewById(R.id.cardTime);
                TextView cardButton = (TextView) view.findViewById(R.id.cardButton);
                ImageView ivEmptyStock2 = (ImageView) view.findViewById(R.id.iv_empty_stock2);
                TextView cardUseLimit = (TextView) view.findViewById(R.id.cardUseLimit);
                cardMoney.setText(FormatUtils.moneyKeep2Decimals(bean.detail.basic.getCouponDenomination()));
                cardMoneyName.setText(bean.detail.basic.getCouponName());
                cardFlag.setText(bean.detail.basic.getCouponTypeName());
                cardLimit.setText(bean.detail.basic.getCouponUseName());
                cardUseLimit.setText(bean.detail.basic.getCouponLimitName());
                if (bean.detail.basic.usableStartTime != null && bean.detail.basic.usableEndTime != null) {
                    cardTime.setText(bean.detail.basic.usableStartTime.split(" ")[0] + "-" + bean.detail.basic.usableEndTime.split(" ")[0]);
                } else {
                    cardTime.setText("");
                }
                if (bean.status == 0) {
                    cardButton.setVisibility(View.VISIBLE);
                    ivEmptyStock2.setVisibility(View.GONE);
                    cardButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (moutClickListener != null) {
                                bean.courseId = courseId;
                                moutClickListener.outClick("coupon", bean);
                            }
                        }
                    });
                } else {
                    cardButton.setVisibility(View.GONE);
                    ivEmptyStock2.setVisibility(View.VISIBLE);
                }
            } else if (bean.itemType == 2) {
                view = LayoutInflater.from(context).inflate(R.layout.item_live_roster_list_goods_layout, giftLayout, false);
                ConstraintLayout cardParent = (ConstraintLayout) view.findViewById(R.id.cardParent);
                ConstraintLayout goodsLayout = (ConstraintLayout) view.findViewById(R.id.goodsLayout);
                ImageView goodsImg = (ImageView) view.findViewById(R.id.goodsImg);
                ImageView yellowBg = (ImageView) view.findViewById(R.id.yellowBg);
                TextView goodsTitle = (TextView) view.findViewById(R.id.goodsTitle);
                TextView goodsTime = (TextView) view.findViewById(R.id.goodsTime);
                TextView goodsButton = (TextView) view.findViewById(R.id.goodsButton);
                ImageView ivEmptyStock2 = (ImageView) view.findViewById(R.id.iv_empty_stock2);
                if (bean.detail != null && bean.detail.goodsImage != null) {
                    com.healthy.library.businessutil.GlideCopy.with(context)
                            .load(bean.detail.goodsImage)

                            .placeholder(R.drawable.img_1_1_default)
                            .error(R.drawable.img_1_1_default)
                            .into(goodsImg);
                }
                try {
                    if (TextUtils.isEmpty(bean.detail.goodsTitle)) {
                        goodsTitle.setText("");
                    } else {
                        goodsTitle.setText(bean.detail.goodsTitle);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                goodsTime.setText("");
                if (bean.status == 0) {
                    goodsButton.setVisibility(View.VISIBLE);
                    ivEmptyStock2.setVisibility(View.GONE);
                    goodsButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (moutClickListener != null) {
                                try {
                                    bean.detail.merchantId = merchantId;
                                    bean.detail.shopId = shopId;
                                    moutClickListener.outClick("goods", bean);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                } else {
                    goodsButton.setVisibility(View.GONE);
                    ivEmptyStock2.setVisibility(View.VISIBLE);
                }
            } else {

            }
            if (view != null) {
                giftLayout.addView(view);
            }
        }
    }
}
