package com.health.discount.adapter;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.health.discount.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.model.PopDetailInfo;
import com.healthy.library.model.PopListInfo;
import com.healthy.library.utils.DateUtils;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.ScreenUtils;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.AutoClickImageView;
import com.umeng.analytics.MobclickAgent;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlashBuyActAdapter extends BaseAdapter<PopListInfo> {
    public FlashBuyActAdapter(int viewId) {
        super(viewId);
    }

    public FlashBuyActAdapter() {
        this(R.layout.flashbuy_act_adapter_layout);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, final int position) {
        TextView markingName = holder.getView(R.id.markingName);
        ImageView moreBtn = holder.getView(R.id.moreBtn);
        View markingNameLL = holder.getView(R.id.markingNameLL);
        GridLayout flashBuyGrid = holder.getView(R.id.flashBuyGrid);
        TextView mMarkingTime = holder.getView(R.id.markingTime);
        View markHome=holder.getView(R.id.markHome);
        markingName.setText(getDatas().get(position).PopDesc);
        if (!TextUtils.isEmpty(getDatas().get(position).StartDate) && !TextUtils.isEmpty(getDatas().get(position).EndDate)) {
            String mTime = DateUtils.getDateDay(getDatas().get(position).StartDate, "yyyy-MM-dd HH:mm", "MM月dd日") +
                    " - " + DateUtils.getDateDay(getDatas().get(position).EndDate, "yyyy-MM-dd HH:mm", "MM月dd日");
            mMarkingTime.setText(mTime);
            mMarkingTime.setVisibility(View.VISIBLE);
        } else {
            mMarkingTime.setVisibility(View.GONE);
        }
        if(getDatas().get(position).hasGoods){
            markHome.setVisibility(View.VISIBLE);
        }else {
            markHome.setVisibility(View.GONE);
        }
        moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moutClickListener != null) {
                    moutClickListener.outClick("marketingId", getDatas().get(position).PopNo);
                }
            }
        });
        if (getDatas().get(position).popDetail != null &&
                getDatas().get(position).popDetail.goodsDTOList != null) {
            if(getDatas().get(position).popDetail.goodsDTOList.size()>0){
                getDatas().get(position).hasGoods=true;
                markHome.setVisibility(View.VISIBLE);
            }
            addFunctions(context, flashBuyGrid, getDatas().get(position).popDetail.goodsDTOList, getDatas().get(position));
        }
    }

    private void addFunctions(final Context context, final GridLayout gridLayout, final List<PopDetailInfo.GoodsDTOListBean> modelList, final PopListInfo popListInfo) {
        gridLayout.post(new Runnable() {
            @Override
            public void run() {
                gridLayout.removeAllViews();
                int column = 3;
                int needSize = modelList.size();
                if (modelList.size() > 6) {
                    needSize = 6;
                }
                int needfixzzz = 3 - (needSize % 3 == 0 ? 3 : needSize % 3);
                int mMargin = (int) TransformUtil.dp2px(context, 70);
                ViewGroup.LayoutParams gridlayoutparm = gridLayout.getLayoutParams();
                gridLayout.setLayoutParams(gridlayoutparm);
                gridLayout.setColumnCount(column);
                int w = ((ScreenUtils.getScreenWidth(context) - mMargin) / 3);
                for (int i = 0; i < needSize; i++) {
                    final PopDetailInfo.GoodsDTOListBean model = modelList.get(i);
                    View view = LayoutInflater.from(context).inflate(R.layout.item_act_grid_layout, gridLayout, false);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        GridLayout.LayoutParams param = new GridLayout.LayoutParams(GridLayout.spec(
                                GridLayout.UNDEFINED, GridLayout.FILL, 1f),
                                GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f));
                        view.setLayoutParams(param);
                    }
                    ImageView goodsImg = view.findViewById(R.id.goodsImg);
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) goodsImg.getLayoutParams();
                    layoutParams.width = w;
                    goodsImg.setLayoutParams(layoutParams);
                    TextView goodsTitle = view.findViewById(R.id.goodsTitle);
                    TextView moneyValue = view.findViewById(R.id.moneyValue);
                    AutoClickImageView passbasket_goods4 = view.findViewById(R.id.passbasket_goods4);
                    TextView tagText = view.findViewById(R.id.tagText);
                    tagText.setVisibility(View.GONE);
//            if (!TextUtils.isEmpty(model.getTagFirst())) {
//                tagText.setVisibility(View.VISIBLE);
//                if (model.getTagFirst().length() > 3) {
//                    String org = model.getTagFirst();
//                    String resultOrg = org.substring(0, 2) + "\n" + org.substring(2, org.length());
//                    tagText.setBackgroundResource(R.drawable.shape_mall_goods_ol_tagbgbig);
//                    tagText.setText(resultOrg);
//                } else {
//                    tagText.setText(model.getTagFirst());
//                    tagText.setBackgroundResource(R.drawable.shape_mall_goods_ol_tagbgsmall);
//                }
//            }
                    com.healthy.library.businessutil.GlideCopy.with(context)
                            .load(model.headImage)
                            .placeholder(R.drawable.img_1_1_default)
                            .error(R.drawable.img_1_1_default)

                            .into(goodsImg);
                    goodsTitle.setText(model.goodsTitle);
                    moneyValue.setText(FormatUtils.moneyKeep2Decimals(model.platformPrice));
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Map nokmap = new HashMap<String, String>();
                            nokmap.put("soure", "活动中心商品图片/标题点击量");
                            MobclickAgent.onEvent(context, "event2APPActivityCenterGoodsClick", nokmap);
                            if (moutClickListener != null) {
                                moutClickListener.outClick("goodsDetial", model.id);
                            }
                        }
                    });
                    passbasket_goods4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Map nokmap = new HashMap<String, String>();
                            nokmap.put("type", "活动中心商品加入购物车点击量");
                            MobclickAgent.onEvent(context, "event2APPActivityCenterShopCartClick", nokmap);
                            if (moutClickListener != null) {
                                moutClickListener.outClick("popListInfo", popListInfo);
                                moutClickListener.outClick("basket", model);
                            }
                        }
                    });
                    gridLayout.addView(view);
                }
                if (needfixzzz > 0) {
                    for (int i = 0; i < needfixzzz; i++) {
                        View view = LayoutInflater.from(context).inflate(R.layout.item_act_grid_layout, gridLayout, false);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            GridLayout.LayoutParams param = new GridLayout.LayoutParams(GridLayout.spec(
                                    GridLayout.UNDEFINED, GridLayout.FILL, 1f),
                                    GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f));
                            view.setLayoutParams(param);
                        }
                        ImageView goodsImg = view.findViewById(R.id.goodsImg);
                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) goodsImg.getLayoutParams();
                        layoutParams.width = w;
                        goodsImg.setLayoutParams(layoutParams);
                        TextView goodsTitle = view.findViewById(R.id.goodsTitle);
                        TextView moneyValue = view.findViewById(R.id.moneyValue);
                        AutoClickImageView passbasket_goods4 = view.findViewById(R.id.passbasket_goods4);
                        view.setVisibility(View.INVISIBLE);
                        gridLayout.addView(view);
                    }
                }
            }
        });

    }
}
