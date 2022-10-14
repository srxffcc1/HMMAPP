package com.health.mine.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.mine.R;
import com.healthy.library.model.VipConsume;
import com.healthy.library.utils.FormatUtils;

import java.util.List;

/**
 * @author Li
 * @date 2019/03/25 11:03
 * @des 推荐阅读
 */

public class VipConsumeHistoryAdapter extends BaseQuickAdapter<VipConsume, BaseViewHolder> {




    public VipConsumeHistoryAdapter() {
        this(R.layout.mine_item_consume);
    }

    private VipConsumeHistoryAdapter(int layoutResId) {
        super(layoutResId);

    }

    @Override
    protected void convert(BaseViewHolder helper, final VipConsume item) {
        ConstraintLayout consumeTopLL;
        ImageView consumeStoreLeft;
        TextView consumeStore;
        TextView consumeDate;
        LinearLayout consumeLL;
        TextView sumCount;
        TextView sumMoney;
        consumeTopLL = (ConstraintLayout) helper.itemView.findViewById(R.id.consumeTopLL);
        consumeStoreLeft = (ImageView) helper.itemView.findViewById(R.id.consumeStoreLeft);
        consumeStore = (TextView) helper.itemView.findViewById(R.id.consumeStore);
        consumeDate = (TextView) helper.itemView.findViewById(R.id.consumeDate);
        consumeLL = (LinearLayout) helper.itemView.findViewById(R.id.consumeLL);
        sumCount = (TextView) helper.itemView.findViewById(R.id.sumCount);
        sumMoney = (TextView) helper.itemView.findViewById(R.id.sumMoney);
        consumeStore.setText(item.DepartName);
        String[] time = item.OperDate.split(" ");
        consumeDate.setText(time[0].replaceAll("-", "/"));
        double j=0;
        for (int x = 0; x <item.cardSaleDetList.size() ; x++) {
            j+=Double.parseDouble(item.cardSaleDetList.get(x).Number);
        }
        sumCount.setText("共"+ FormatUtils.moneyKeep2Decimals(j)+"件,");
        sumMoney.setText("￥"+item.OperMoney);
        buildGoodsList(consumeLL, item.cardSaleDetList);


    }

    private void buildGoodsList(LinearLayout consumeLL, List<VipConsume.CardSaleDet> cardSaleDetList) {
        consumeLL.removeAllViews();
        if (cardSaleDetList != null) {
            for (int i = 0; i < cardSaleDetList.size(); i++) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.mine_item_consume_content, consumeLL, false);
                VipConsume.CardSaleDet cardSaleDet = cardSaleDetList.get(i);

                TextView consumeStore;
                TextView consumeMoney;
                TextView consumeCount;

                consumeStore = (TextView) view.findViewById(R.id.consumeStore);
                consumeMoney = (TextView) view.findViewById(R.id.consumeMoney);
                consumeCount = (TextView) view.findViewById(R.id.consumeCount);

                consumeStore.setText(cardSaleDet.GoodsName);
                consumeMoney.setText("￥" + cardSaleDet.FactPrice);
                double prices=Double.parseDouble(cardSaleDet.FactPrice);
                if(prices<0){
                    consumeMoney.setText("-￥" + cardSaleDet.FactPrice.split("-")[1]);
                }
                consumeCount.setText("X " + cardSaleDet.Number);
                consumeLL.addView(view);
            }
        }

    }

    private void initView() {



    }
}
