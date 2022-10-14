package com.health.discount.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.healthy.library.model.ActKick;
import com.healthy.library.routes.DiscountRoutes;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.ImageTextView;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

public class ActZKickAdapter extends BaseAdapter<String> {

    private List<ActKick> kickList;


    @Override
    public int getItemViewType(int position) {
        return 16;
    }

    public ActZKickAdapter() {
        this(R.layout.dis_item_fragment_kick);
    }

    private ActZKickAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {
        LinearLayout kickPLL;
        ConstraintLayout kickTopLL;
        ImageTextView topTitle;
        LinearLayout kickTitleLfet;
        LinearLayout kickHeadIconLL;
        TextView kickMenber;
        ImageTextView kickMore;
        LinearLayout kickLL;
        kickPLL = (LinearLayout) baseHolder.itemView.findViewById(R.id.kickPLL);
        kickTopLL = (ConstraintLayout) baseHolder.itemView.findViewById(R.id.kickTopLL);
        topTitle = (ImageTextView) baseHolder.itemView.findViewById(R.id.topTitle);
        kickTitleLfet = (LinearLayout) baseHolder.itemView.findViewById(R.id.kickTitleLfet);
        kickHeadIconLL = (LinearLayout) baseHolder.itemView.findViewById(R.id.kickHeadIconLL);
        kickMenber = (TextView) baseHolder.itemView.findViewById(R.id.kickMenber);
        kickMore = (ImageTextView) baseHolder.itemView.findViewById(R.id.kickMore);
        kickLL = (LinearLayout) baseHolder.itemView.findViewById(R.id.kickLL);
        onSucessGetActKick(kickPLL, kickTopLL, kickLL, kickHeadIconLL, kickMenber, kickMore, kickList);

    }

    public void setKickList(List<ActKick> kickList) {
        this.kickList = kickList;
    }

    public List<ActKick> getKickList() {
        return kickList;
    }

    public void onSucessGetActKick(LinearLayout kickPLL, ConstraintLayout kickTopLL, LinearLayout kickLL, LinearLayout kickHeadIconLL, TextView kickMenber, ImageTextView kickMore, List<ActKick> list) {
        kickPLL.setVisibility(View.GONE);
        kickLL.removeAllViews();
        kickHeadIconLL.removeAllViews();
        if (list != null && list.size() > 0) {
            kickPLL.setVisibility(View.VISIBLE);
            kickLL.removeAllViews();

            int resultpeoplecount = 0;
            for (int i = 0; i < list.size(); i++) {
                resultpeoplecount += list.get(i).joinNum;
            }
            if (resultpeoplecount > 0) {
                kickMenber.setVisibility(View.VISIBLE);
                kickMenber.setText(resultpeoplecount + "人正在砍价");
                List<String> mUrlList = list.get(0).faceUrlList;
                if (!ListUtil.isEmpty(mUrlList)) {
                    int forSize = resultpeoplecount > 6 ? 6 : resultpeoplecount;
                    if(forSize>mUrlList.size()){//避免数组越界
                        forSize=mUrlList.size();
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
                        kickHeadIconLL.addView(cornerImageView);
                    }
                }
            } else {
                kickMenber.setVisibility(View.GONE);
            }
            if (resultpeoplecount == 0) {
                kickMenber.setVisibility(View.GONE);
            }


            kickMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MobclickAgent.onEvent(context, "event2APPShopHomeKanMoreClick", new SimpleHashMapBuilder<String, String>().puts("soure", "首页砍价活动更多按钮点击量"));
                    ARouter.getInstance()
                            .build(DiscountRoutes.DIS_NEWKICKLIST)
                            .navigation();
                }
            });
            for (int i = 0; i < list.size(); i++) {
                ActKick kick = list.get(i);
                View view = LayoutInflater.from(context).inflate(R.layout.dis_item_kick_h, kickLL, false);
                View killTotalNumLL = view.findViewById(R.id.killTotalNumLL);
                ImageView killImg;
                TextView killPrice;
                TextView killOldPrice;
                TextView killTotalNum;
                killImg = (ImageView) view.findViewById(R.id.killImg);
                killPrice = (TextView) view.findViewById(R.id.killPrice);
                killOldPrice = (TextView) view.findViewById(R.id.killOldPrice);
                killTotalNum = (TextView) view.findViewById(R.id.killTotalNum);
                com.healthy.library.businessutil.GlideCopy.with(context)
                        .load(kick.goodsImage)
                        .placeholder(R.drawable.img_1_1_default)
                        .error(R.drawable.img_1_1_default)
                        .into(killImg);
                killPrice.setText(FormatUtils.moneyKeep2Decimals(kick.floorPrice));
                killOldPrice.setText("¥" + FormatUtils.moneyKeep2Decimals(kick.goodsPlatformPrice));
                killOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                killTotalNum.setText(kick.joinNum + "");
                if (kick.joinNum == 0) {
                    killTotalNumLL.setVisibility(View.INVISIBLE);
                }
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MobclickAgent.onEvent(context, "event2APPShopHomeKanGoodsClick", new SimpleHashMapBuilder<String, String>().puts("soure", "首页砍价活动商品图片标题的点击量"));
                        ARouter.getInstance()
                                .build(DiscountRoutes.DIS_NEWKICKLIST)
                                .navigation();
                    }
                });
                kickLL.addView(view);
            }
        }
    }

    private void initView() {

    }
}
