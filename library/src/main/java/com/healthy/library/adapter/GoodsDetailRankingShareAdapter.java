package com.healthy.library.adapter;

import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.healthy.library.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.model.ShareGiftDTO;
import com.healthy.library.utils.FormatUtils;

import java.util.ArrayList;
import java.util.List;


public class GoodsDetailRankingShareAdapter extends BaseAdapter<String> {

    private List<ShareGiftDTO> goodsRankList = new ArrayList<>();
    private boolean isAdd = false;
    private boolean isFirstAttach = true;

    public GoodsDetailRankingShareAdapter() {
        this(R.layout.item_goods_detail_ranking_share_adapter);
    }

    public void setRankData(List<ShareGiftDTO> data) {
        goodsRankList.clear();
        this.goodsRankList.addAll(data);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        LinearLayout rankingLinear = holder.getView(R.id.rankingLinear);
        LinearLayout topTitle = holder.getView(R.id.topTitle);
        if (!ListUtil.isEmpty(goodsRankList)) {
            buildList(rankingLinear, goodsRankList);
        }
        topTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClickInit()){
                    moutClickListener.outClick("shareRank",null);
                }
            }
        });
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    private GoodsDetailRankingShareAdapter(int layoutResId) {
        super(layoutResId);
    }

    private void buildList(LinearLayout layout, List<ShareGiftDTO> list) {
        layout.removeAllViews();
        for (int i = 0; i < list.size(); i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_goods_ranking_share_layout, null);
            TextView giftMoney = (TextView) view.findViewById(R.id.giftMoney);
            TextView giftTitle = (TextView) view.findViewById(R.id.giftTitle);
            TextView giftLab = (TextView) view.findViewById(R.id.giftLab);
            TextView limitTxt = (TextView) view.findViewById(R.id.limitTxt);
            ShareGiftDTO bean = list.get(i);
            SpannableString  price = new SpannableString("￥" + FormatUtils.moneyKeep2Decimals(bean.shareMoney));
            price.setSpan(new AbsoluteSizeSpan(13, true), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            giftMoney.setText(price);
            if (bean.shareResultType == 0) {
                giftLab.setText("可提现");
                giftTitle.setText(String.format("现金%s元", FormatUtils.moneyKeep2Decimals(bean.shareMoney)));
            } else if (bean.shareResultType == 1) {
                giftLab.setText("");
                //giftTitle.setText(String.format("优惠券%s元", FormatUtils.moneyKeep2Decimals(bean.shareMoney)));
                giftTitle.setText(bean.getShareName());
                //giftTitle.setText("分享赚优惠券");
            }
            if (bean.shareResultLimit > 0) {
                limitTxt.setText(String.format("每人限%s次", bean.shareResultLimit));
            } else {
                limitTxt.setText("不限次数");
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isClickInit()){
                        moutClickListener.outClick("shareRank",null);
                    }
                }
            });
            layout.addView(view);
        }
    }
}
