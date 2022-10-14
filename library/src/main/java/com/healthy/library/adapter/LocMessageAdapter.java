package com.healthy.library.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.healthy.library.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.LocVip;
import com.healthy.library.utils.SpUtils;

public class LocMessageAdapter extends BaseAdapter<LocVip.Local.MerchantsShop> {

    public LocMessageAdapter() {
        this(R.layout.loc_message_adapter_layout);
    }

    public LocMessageAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        ConstraintLayout backGG;
        TextView currentShopTxt;
        TextView locRefreshTxt;
        TextView locShopName;
        TextView locDistance;
        TextView locShopAddress;
        backGG = (ConstraintLayout) holder.itemView.findViewById(R.id.backGG);
        currentShopTxt = (TextView) holder.itemView.findViewById(R.id.currentShopTxt);
        locRefreshTxt = (TextView) holder.itemView.findViewById(R.id.locRefreshTxt);
        locShopName = (TextView) holder.itemView.findViewById(R.id.locShopName);
        locDistance = (TextView) holder.itemView.findViewById(R.id.locDistance);
        locShopAddress = (TextView) holder.itemView.findViewById(R.id.locShopAddress);

        final LocVip.Local.MerchantsShop merchantsShop = getDatas().get(position);
        if (merchantsShop == null) {
            return;
        }
        locShopName.setText(merchantsShop.shopName);
        locShopAddress.setText(merchantsShop.getAddressDetails());
        backGG.setBackgroundResource(R.drawable.shape_loc_change_item);
        if(SpUtils.getValue(context,SpKey.OPERATIONSTATUS,false)){
            if(SpUtils.getValue(context,SpKey.CHOSE_MC)!=null){
                if(merchantsShop.merchantId.equals(SpUtils.getValue(context, SpKey.CHOSE_MC))){
                    backGG.setBackgroundResource(R.drawable.shape_loc_change_item_near);
                }
            }
        }
        String shopDistanceDes = "";
        if (merchantsShop.distance >= 1000) {
            shopDistanceDes = String.format("%.1f", (double) merchantsShop.distance / 1000) + "km";
        } else {
            shopDistanceDes = merchantsShop.distance + "m";
        }
        locDistance.setText("距您" + shopDistanceDes);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moutClickListener != null) {
                    moutClickListener.outClick("click", merchantsShop);
                }
            }
        });
    }
}
