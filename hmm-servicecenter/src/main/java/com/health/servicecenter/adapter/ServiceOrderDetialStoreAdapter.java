package com.health.servicecenter.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.servicecenter.R;
import com.health.servicecenter.model.StoreSimpleModel;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.utils.IntentUtils;
import com.healthy.library.utils.NavigateUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

public class ServiceOrderDetialStoreAdapter extends BaseAdapter<StoreSimpleModel> {

    public ServiceOrderDetialStoreAdapter() {
        this(R.layout.item_service_order_detial_store);
    }

    public ServiceOrderDetialStoreAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        final TextView storeNum = holder.getView(R.id.storeNum);
        final TextView storeName = holder.getView(R.id.storeName);
        final TextView storeAddress = holder.getView(R.id.storeAddress);
        final ImageView storePhone = holder.getView(R.id.storePhone);
        final StoreSimpleModel model = getModel();
        storeNum.setText(model.otherShopCount+"家门店适用");
        storeName.setText(model.userShopInfoDistanceResults.get(0).shopName);
        storeAddress.setText(model.userShopInfoDistanceResults.get(0).areaDetails);
        storeNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if (moutClickListener!=null){
                  moutClickListener.outClick("更多商户",null);
              }
            }
        });
        storePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map nokmap = new HashMap<String, String>();
                nokmap.put("soure", "订单详情页");
                MobclickAgent.onEvent(context, "event2PhoneStoreClick", nokmap);
                IntentUtils.dial(context, model.userShopInfoDistanceResults.get(0).appointmentPhone);
            }
        });
        storeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigateUtils.navigate(context, model.userShopInfoDistanceResults.get(0).shopName,
                        model.userShopInfoDistanceResults.get(0).latitude + "", model.userShopInfoDistanceResults.get(0).longitude + "");
            }
        });
    }
}
