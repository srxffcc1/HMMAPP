package com.healthy.library.adapter;

import android.graphics.Color;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.example.lib_ShapeView.layout.ShapeConstraintLayout;
import com.example.lib_ShapeView.layout.ShapeLinearLayout;
import com.healthy.library.LibApplication;
import com.healthy.library.R;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.businessutil.GlideCopy;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.GoodsRank;
import com.healthy.library.routes.MineRoutes;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.FrameActivityManager;
import com.healthy.library.utils.LogUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.SpanUtils;
import com.healthy.library.widget.AutoClickImageView;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.StringDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class GoodsDetailRankingListAdapter extends BaseAdapter<String> {

    private List<GoodsRank> goodsRankList = new ArrayList<>();
    private boolean isAdd = false;
    private boolean isFirstAttach = true;

    public GoodsDetailRankingListAdapter() {
        this(R.layout.item_goods_detail_ranking_list_adapter);
    }

    public void setRankData(List<GoodsRank> data) {
        goodsRankList.clear();
        isFirstAttach = true;
        this.goodsRankList.addAll(data);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        LinearLayout rankingLinear = holder.getView(R.id.rankingLinear);
        AutoClickImageView autoImg = holder.getView(R.id.autoImg);
        ShapeConstraintLayout profitBtn = holder.getView(R.id.profitBtn);
        LinearLayout shareMisc = holder.getView(R.id.shareMisc);
        autoImg.setCanTouch(false);
        if (!ListUtil.isEmpty(goodsRankList)) {
            buildList(rankingLinear, goodsRankList);
        }
        if (!isAdd) {
            autoImg.startLoopScaleAuto();
            isAdd = true;
        }
        profitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(MineRoutes.MINE_VIPPROFIT).navigation();
            }
        });
        shareMisc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringDialog.newInstance()
                        .setUrl(SpanUtils.getBuilder(LibApplication.getAppContext(), "1、")
                                .setForegroundColor(Color.parseColor("#222222")).setBold()
                                .append("分享赚商品分享给好友/朋友圈，好友下单后，").setForegroundColor(Color.parseColor("#222222"))
                                .append("可获得礼包所有内容（部分内容限制次数）").setForegroundColor(Color.parseColor("#222222"))
                                .append("，优惠券权益实时到账，分享赚现金权益默认7天后可提现；7天内好友核销订单，则现金收益可立即提现。\n").setForegroundColor(Color.parseColor("#222222"))
                                .append("2、").setForegroundColor(Color.parseColor("#222222")).setBold()
                                .append("被分享人订单一旦发生退款，分享人的对应权益将自动扣除，可在收益记录中查看。\n").setForegroundColor(Color.parseColor("#222222"))
                                .append("注：").setForegroundColor(Color.parseColor("#222222")).setBold()
                                .append("若出现不正当手段获取平台利益，经平台核实后有权取消用户参与本次活动的资格，并对账号进行封禁；同时有权撤销违规交易订单，并收回活动中发放的奖品（包含已使用的部分），并追求相关法律责任。\n").setForegroundColor(Color.parseColor("#222222"))
                                .create())
                        .setTitle("分享赚攻略")
                        .show(((BaseActivity) FrameActivityManager.instance().topActivity()).getSupportFragmentManager(), "xiaozhishi");
            }
        });
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    private GoodsDetailRankingListAdapter(int layoutResId) {
        super(layoutResId);
    }

    private void buildList(LinearLayout layout, List<GoodsRank> list) {
        layout.removeAllViews();
        int indexOfTen = -1;//标记排行榜前10名内是否存在自己
        int indexOfTenNo = -1;//标记排行榜11名以后是否存在自己
        for (int i = 0; i < list.size(); i++) {
            if (i < 10) {
                if (list.get(i).shareMemberId.equals(new String(Base64.decode(SpUtils.getValue(context, SpKey.USER_ID) != null ? SpUtils.getValue(context, SpKey.USER_ID).getBytes() : null, Base64.DEFAULT)))) {
                    indexOfTen = i;
                }
            } else {
                if (list.get(i).shareMemberId.equals(new String(Base64.decode(SpUtils.getValue(context, SpKey.USER_ID) != null ? SpUtils.getValue(context, SpKey.USER_ID).getBytes() : null, Base64.DEFAULT)))) {
                    indexOfTenNo = i;
                }
            }
        }
        if (indexOfTenNo > -1) {//说明10名之后存在自己，把自己放到第一个
            for (int i = 0; i < indexOfTenNo; i++) {
                Collections.swap(list, i, 0);
            }
        } else {
            LogUtils.e("1");
            if (indexOfTen == -1) {//说明整个排行榜都不存在自己，新增一个空的自己，然后放到第一个
                if (isFirstAttach) {
                    list.add(new GoodsRank("0", " ",
                            SpUtils.getValue(context, SpKey.USER_ICON),
                            SpUtils.getValue(context, SpKey.USER_NICK),
                            SpUtils.getValue(context, new String(Base64.decode(SpUtils.getValue(context, SpKey.USER_ID) != null ? SpUtils.getValue(context, SpKey.USER_ID).getBytes() : null, Base64.DEFAULT))),
                            0));
                    for (int i = 0; i < list.size(); i++) {
                        Collections.swap(list, i, 0);
                    }
                    isFirstAttach = false;
                }
            }
        }
        for (int i = 0; i < list.size(); i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_goods_ranking_list_layout, null);
            TextView rankTxt = (TextView) view.findViewById(R.id.rankTxt);
            ImageView rankImg = (ImageView) view.findViewById(R.id.rankImg);
            CornerImageView userImg = (CornerImageView) view.findViewById(R.id.userImg);
            ImageView crownImg = (ImageView) view.findViewById(R.id.crownImg);
            TextView nickName = (TextView) view.findViewById(R.id.nickName);
            TextView countNum = (TextView) view.findViewById(R.id.countNum);
            TextView money = (TextView) view.findViewById(R.id.money);
            ShapeLinearLayout shapeLinear = (ShapeLinearLayout) view.findViewById(R.id.shapeLinear);
            GoodsRank rankBean = list.get(i);
            rankTxt.setVisibility(View.VISIBLE);
            rankImg.setVisibility(View.GONE);
            crownImg.setVisibility(View.GONE);
            rankTxt.setText(rankBean.rankNum);
            nickName.setText(rankBean.shareMemberName);
            countNum.setText(rankBean.shareMemberCount + "人");
            money.setText(FormatUtils.moneyKeep2Decimals(rankBean.shareResult));
            GlideCopy.with(context)
                    .load(rankBean.shareMemberPic)
                    .error(R.drawable.img_avatar_default)
                    .placeholder(R.drawable.img_avatar_default)
                    .into(userImg);
            if (indexOfTen > -1) {
                if (indexOfTen == i) {
                    shapeLinear.getShapeDrawableBuilder().setSolidColor(Color.parseColor("#FFEAD7")).intoBackground();
                } else {
                    shapeLinear.getShapeDrawableBuilder().setSolidColor(Color.parseColor("#FDF7EE")).intoBackground();
                }
                switch (i) {
                    case 0:
                        rankTxt.setVisibility(View.GONE);
                        rankImg.setVisibility(View.VISIBLE);
                        crownImg.setVisibility(View.VISIBLE);
                        GlideCopy.with(context)
                                .load(R.drawable.goods_detail_ranking_list_no1)
                                .into(rankImg);
                        GlideCopy.with(context)
                                .load(R.drawable.goods_detail_ranking_list_crown1)
                                .into(crownImg);
                        break;
                    case 1:
                        rankTxt.setVisibility(View.GONE);
                        rankImg.setVisibility(View.VISIBLE);
                        crownImg.setVisibility(View.VISIBLE);
                        GlideCopy.with(context)
                                .load(R.drawable.goods_detail_ranking_list_no2)
                                .into(rankImg);
                        GlideCopy.with(context)
                                .load(R.drawable.goods_detail_ranking_list_crown2)
                                .into(crownImg);
                        break;
                    case 2:
                        rankTxt.setVisibility(View.GONE);
                        rankImg.setVisibility(View.VISIBLE);
                        crownImg.setVisibility(View.VISIBLE);
                        GlideCopy.with(context)
                                .load(R.drawable.goods_detail_ranking_list_no3)
                                .into(rankImg);
                        GlideCopy.with(context)
                                .load(R.drawable.goods_detail_ranking_list_crown3)
                                .into(crownImg);
                        break;
                }
            } else {
                if (i == 0) {
                    shapeLinear.getShapeDrawableBuilder().setSolidColor(Color.parseColor("#FFEAD7")).intoBackground();
                } else {
                    shapeLinear.getShapeDrawableBuilder().setSolidColor(Color.parseColor("#FDF7EE")).intoBackground();
                }
                switch (i) {
                    case 1:
                        rankTxt.setVisibility(View.GONE);
                        rankImg.setVisibility(View.VISIBLE);
                        crownImg.setVisibility(View.VISIBLE);
                        GlideCopy.with(context)
                                .load(R.drawable.goods_detail_ranking_list_no1)
                                .into(rankImg);
                        GlideCopy.with(context)
                                .load(R.drawable.goods_detail_ranking_list_crown1)
                                .into(crownImg);
                        break;
                    case 2:
                        rankTxt.setVisibility(View.GONE);
                        rankImg.setVisibility(View.VISIBLE);
                        crownImg.setVisibility(View.VISIBLE);
                        GlideCopy.with(context)
                                .load(R.drawable.goods_detail_ranking_list_no2)
                                .into(rankImg);
                        GlideCopy.with(context)
                                .load(R.drawable.goods_detail_ranking_list_crown2)
                                .into(crownImg);
                        break;
                    case 3:
                        rankTxt.setVisibility(View.GONE);
                        rankImg.setVisibility(View.VISIBLE);
                        crownImg.setVisibility(View.VISIBLE);
                        GlideCopy.with(context)
                                .load(R.drawable.goods_detail_ranking_list_no3)
                                .into(rankImg);
                        GlideCopy.with(context)
                                .load(R.drawable.goods_detail_ranking_list_crown3)
                                .into(crownImg);
                        break;
                }
            }
            layout.addView(view);
        }
    }
}
