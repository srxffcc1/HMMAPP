package com.health.discount.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.healthy.library.businessutil.GlideCopy;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.model.ActGroup;
import com.healthy.library.routes.DiscountRoutes;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.ImageTextView;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

public class ActZGroupAdapter extends BaseAdapter<String> {


    private List<ActGroup> groupList;

    @Override
    public int getItemViewType(int position) {
        return 15;
    }

    public ActZGroupAdapter() {
        this(R.layout.dis_item_fragment_group);
    }

    private ActZGroupAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {
        LinearLayout pinPLL;
        ConstraintLayout pinTopLL;
        ImageTextView pinTitle;
        LinearLayout pinTitleLfet;
        LinearLayout pinHeadIconLL;
        TextView pinMenber;
        ImageTextView pinMore;
        LinearLayout pinLL;
        pinPLL = (LinearLayout) baseHolder.itemView.findViewById(R.id.pinPLL);
        pinTopLL = (ConstraintLayout) baseHolder.itemView.findViewById(R.id.pinTopLL);
        pinTitle = (ImageTextView) baseHolder.itemView.findViewById(R.id.pinTitle);
        pinTitleLfet = (LinearLayout) baseHolder.itemView.findViewById(R.id.pinTitleLfet);
        pinHeadIconLL = (LinearLayout) baseHolder.itemView.findViewById(R.id.pinHeadIconLL);
        pinMenber = (TextView) baseHolder.itemView.findViewById(R.id.pinMenber);
        pinMore = (ImageTextView) baseHolder.itemView.findViewById(R.id.pinMore);
        pinLL = (LinearLayout) baseHolder.itemView.findViewById(R.id.pinLL);
        onSucessGetActGroup(pinPLL, pinLL, pinHeadIconLL, pinMenber, pinMore, groupList);
    }

    public void onSucessGetActGroup(LinearLayout pinPLL, LinearLayout pinLL, LinearLayout pinIconLL, TextView pinMenber, ImageTextView pinMore, List<ActGroup> list) {
        pinPLL.setVisibility(View.GONE);
        pinLL.removeAllViews();
        pinIconLL.removeAllViews();
        if (list != null && list.size() > 0) {
            pinPLL.setVisibility(View.VISIBLE);
            pinLL.removeAllViews();
            int resultpeoplecount = 0;
            for (int i = 0; i < list.size(); i++) {
                resultpeoplecount += list.get(i).joinNum;
            }
            if (resultpeoplecount > 0) {
                pinMenber.setText(resultpeoplecount + "人正在拼团");
                pinMenber.setVisibility(View.VISIBLE);
                List<String> mUrlList = list.get(0).faceUrlList;
                if (!ListUtil.isEmpty(mUrlList)) {
                    int forSize = resultpeoplecount > 6 ? 6 : resultpeoplecount;
                    if (forSize > mUrlList.size()) {//避免数组越界
                        forSize = mUrlList.size();
                    }
                    for (int i = 0; i < forSize; i++) {
                        CornerImageView cornerImageView = new CornerImageView(context);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) TransformUtil.dp2px(context, 18),
                                (int) TransformUtil.dp2px(context, 18));
                        cornerImageView.setIsCircle(true);
                        if (i > 0) {
                            layoutParams.leftMargin = -10;
                        }
                        cornerImageView.setLayoutParams(layoutParams);
                        GlideCopy.with(cornerImageView.getContext())
                                .load(mUrlList.get(i))
                                .error(R.drawable.img_avatar_default)
                                .into(cornerImageView);
                        pinIconLL.addView(cornerImageView);
                    }
                }
            } else {
                pinMenber.setVisibility(View.GONE);
            }
            if (resultpeoplecount == 0) {
                pinMenber.setVisibility(View.GONE);
            }
            pinMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MobclickAgent.onEvent(context, "event2APPShopHomePinMoreClick", new SimpleHashMapBuilder<String, String>().puts("soure", "首页拼团活动更多按钮点击量"));
                    ARouter.getInstance()
                            .build(DiscountRoutes.DIS_NEWASSEMBLEACTIVITY)
                            .navigation();
                }
            });
            for (int i = 0; i < list.size(); i++) {
                ActGroup group = list.get(i);
                View view = LayoutInflater.from(context).inflate(R.layout.dis_item_pin_h, pinLL, false);
                TextView pinPeopleNum;
                TextView pinPrice;
                TextView pinOldPrice;
                TextView pinTotalNum;
                ImageView pinGoodsImg;
                LinearLayout pinLiner;
                pinLiner = (LinearLayout) view.findViewById(R.id.pinLiner);
                pinPeopleNum = (TextView) view.findViewById(R.id.pinPeopleNum);
                pinPrice = (TextView) view.findViewById(R.id.pinPrice);
                pinOldPrice = (TextView) view.findViewById(R.id.pinOldPrice);
                pinTotalNum = (TextView) view.findViewById(R.id.pinTotalNum);
                pinGoodsImg = (ImageView) view.findViewById(R.id.pinGoodsImg);
                com.healthy.library.businessutil.GlideCopy.with(context)
                        .load(group.goodsImage)
                        .placeholder(R.drawable.img_1_1_default)
                        .error(R.drawable.img_1_1_default)
                        .into(pinGoodsImg);
                pinPrice.setText(FormatUtils.moneyKeep2Decimals(group.assemblePrice));
                pinOldPrice.setText("¥" + FormatUtils.moneyKeep2Decimals(group.goodsPlatformPrice));
                pinOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                pinPeopleNum.setText(group.regimentSize + "");
                if (group.joinNum > 0) {
                    pinLiner.setVisibility(View.VISIBLE);
                    pinTotalNum.setText(group.joinNum + "");
                } else {
                    pinLiner.setVisibility(View.INVISIBLE);
                }
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MobclickAgent.onEvent(context, "event2APPShopHomePinGoodsClick", new SimpleHashMapBuilder<String, String>().puts("soure", "首页拼团活动商品图片/标题的点击量"));
                        ARouter.getInstance()
                                .build(DiscountRoutes.DIS_NEWASSEMBLEACTIVITY)
                                .navigation();
                    }
                });
                pinLL.addView(view);
            }
        }
    }

    private void initView() {
    }

    public void setGroupList(List<ActGroup> groupList) {
        this.groupList = groupList;
    }

    public List<ActGroup> getGroupList() {
        return groupList;
    }
}
