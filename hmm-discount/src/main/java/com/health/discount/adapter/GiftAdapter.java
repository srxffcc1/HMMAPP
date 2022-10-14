package com.health.discount.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.discount.R;
import com.healthy.library.model.Coupon;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.widget.AutoFitCheckBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/25 11:03
 * @des 推荐阅读
 */

public class GiftAdapter extends BaseQuickAdapter<Coupon, BaseViewHolder> {


    boolean needshowCheck = true;
//    Map<String, Boolean> checkMap = new HashMap<>();


    public void setNeedshowCheck(boolean needshowCheck) {
        this.needshowCheck = needshowCheck;
    }

    public GiftAdapter() {
        this(R.layout.dis_item_dialog_coupon);
    }

    private GiftAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, final Coupon item) {
        final AutoFitCheckBox getGiftButton;
         TextView cardMoney;
         TextView cardCount;
         TextView cardContent;
         TextView cardTime;
        cardMoney = (TextView) helper.itemView.findViewById(R.id.cardMoney);
        cardCount = (TextView) helper.itemView.findViewById(R.id.cardCount);
        cardContent = (TextView) helper.itemView.findViewById(R.id.cardContent);
        cardTime = (TextView) helper.itemView.findViewById(R.id.cardTime);
        getGiftButton = (AutoFitCheckBox) helper.itemView.findViewById(R.id.getGiftButton);
        cardMoney.setText(item.Price);
        cardCount.setText(FormatUtils.moneyKeep2Decimals(item.Number)+"张");
        cardContent.setText(item.GoodsName);
        cardTime.setText("有效期至："+item.EndDate);
        getGiftButton.setVisibility(View.INVISIBLE);
        if (needshowCheck) {
            getGiftButton.setChecked(item.ischeck);
            getGiftButton.setVisibility(View.VISIBLE);
            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (needshowCheck) {
                        getGiftButton.setChecked(!getGiftButton.isChecked());
                    }
//                    if (getGiftButton.isChecked()) {
//                        item.ischeck = true;
//                    } else {
//                        item.ischeck = false;
//                    }
                }
            });
            getGiftButton.setOnCheckedChangeListener(new AutoFitCheckBox.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(AutoFitCheckBox buttonView, boolean isChecked) {
//                    if (!buttonView.isPressed()) {
//                        return;
//                    }
                    if (isChecked) {
                        System.out.println("设置选中");
                        item.ischeck = true;
                    } else {
                        System.out.println("设置选中否");
                        item.ischeck = false;
                    }
                }
            });
        }

    }
    public List<Coupon> getSelectCouponS(){
        List<Coupon> result=new ArrayList<>();
        List<Coupon> org=getData();
        for (int i = 0; i <org.size() ; i++) {
            if(org.get(i).ischeck){
                result.add(org.get(i));
            }
        }
        return result;
    }
    private void initView() {


    }
}
