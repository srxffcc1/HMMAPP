package com.health.servicecenter.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.servicecenter.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.model.GoodsTran;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

public class MallGoodsDetialTranAdapter extends BaseAdapter<String> {

    public GoodsTran goodsTran;
    public String actRule;


    public void setGoodsTran(GoodsTran goodsTran) {
        this.goodsTran = goodsTran;
    }

    public void setActRule(String actRule) {
        this.actRule = actRule;
    }

    @Override
    public int getItemViewType(int position) {
        return 4;
    }

    public MallGoodsDetialTranAdapter() {
        this(R.layout.service_item_goodsdetail_tran);
    }

    private MallGoodsDetialTranAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {
        ConstraintLayout tranLL;
        TextView basketTitle;
        TextView basketCount;
        ImageView basketMore;
        tranLL = (ConstraintLayout) baseHolder.itemView.findViewById(R.id.tranLL);
        basketTitle = (TextView) baseHolder.itemView.findViewById(R.id.basketTitle);
        basketCount = (TextView) baseHolder.itemView.findViewById(R.id.basketCount);
        basketMore = (ImageView) baseHolder.itemView.findViewById(R.id.basketMore);
         ConstraintLayout ruleLL;
         TextView ruleTitle;
         TextView ruleCount;
         ImageView ruleMore;
        ruleLL = (ConstraintLayout)baseHolder.itemView. findViewById(R.id.ruleLL);
        ruleTitle = (TextView) baseHolder.itemView.findViewById(R.id.ruleTitle);
        ruleCount = (TextView) baseHolder.itemView.findViewById(R.id.ruleCount);
        ruleMore = (ImageView) baseHolder.itemView.findViewById(R.id.ruleMore);
        ruleLL.setVisibility(View.GONE);
        tranLL.setVisibility(View.GONE);
        if (goodsTran != null) {
            tranLL.setVisibility(View.VISIBLE);
            basketTitle.setText("服务");
//        basketCount.setText("支持到店自提"+" · "+goodsTran.goodsPostageFeeWIthShopDTOS.get(1).content.split("\n")[0]+" · "+goodsTran.goodsPostageFeeWIthShopDTOS.get(1).content.split("\n")[1]);

            try {
                basketCount.setText("支持到店自提" + " · " + goodsTran.goodsPostageFeeWIthShopDTOS.get(1).content.split("\n")[0] + " · " + goodsTran.goodsPostageFeeWIthShopDTOS.get(1).content.split("\n")[1]);
            } catch (Exception e) {
                basketCount.setText("支持到店自提" + " · " + goodsTran.goodsPostageFeeWIthShopDTOS.get(1).content.split("\n")[0]);
                e.printStackTrace();
            }
            tranLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Map nokmap = new HashMap<String, String>();
                    nokmap.put("soure", "商品详情页服务栏点击”>“");
                    MobclickAgent.onEvent(context, "btn_APP_MaternalandChildGoods_CommodityDetails_Services", nokmap);

                    if (moutClickListener != null) {
                        moutClickListener.outClick("更多邮费", null);
                    }
                }
            });
        }
        if(actRule!=null){
            ruleLL.setVisibility(View.VISIBLE);
            ruleTitle.setText("规则");
            ruleCount.setText(actRule);
            ruleLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Map nokmap = new HashMap<String, String>();
//                    nokmap.put("soure", "商品详情页服务栏点击”>“");
//                    MobclickAgent.onEvent(context, "btn_APP_MaternalandChildGoods_CommodityDetails_Services", nokmap);

                    if (moutClickListener != null) {
                        moutClickListener.outClick("更多规则", null);
                    }
                }
            });
        }

    }

    private void initView() {


    }
}
