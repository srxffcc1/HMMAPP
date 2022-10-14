package com.health.servicecenter.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.health.servicecenter.R;
import com.healthy.library.model.StoreListModel;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.utils.ParseUtils;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.DrawableTextView;

public class NearbyStoreAdapter extends BaseAdapter<StoreListModel> {

    public NearbyStoreAdapter() {
        this(R.layout.item_nearby_store_bottom_layout);
    }


    public NearbyStoreAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, final int position) {
        TextView store_name = holder.getView(R.id.store_name);
        ConstraintLayout top_layout = holder.getView(R.id.top_layout);
        final TextView store_time_top = holder.getView(R.id.store_time_top);
        final TextView store_time_bottom = holder.getView(R.id.store_time_bottom);
        final TextView store_margin = holder.getView(R.id.store_margin);
        final TextView store_address = holder.getView(R.id.store_address);
        final DrawableTextView nearby_store_phoneTxt = holder.getView(R.id.nearby_store_phoneTxt);
        final DrawableTextView nearby_store_navigationTxt = holder.getView(R.id.nearby_store_navigationTxt);
        CornerImageView iv_store = holder.getView(R.id.iv_store);
        com.healthy.library.businessutil.GlideCopy.with(holder.itemView)
                .load(getDatas().get(position).getFilePath())
                .into(iv_store);
        store_name.setText(getDatas().get(position).getShopName());
        store_margin.setText(ParseUtils.parseDistance(getDatas().get(position).getDistance() + ""));
        store_address.setText(getDatas().get(position).getShopAddress());
        store_time_top.setText("");
        store_time_bottom.setText("");
        if (getDatas().get(position).getBusinessTime() != null) {
            if (getDatas().get(position).getBusinessTime().size() == 2) {
                store_time_top.setText(getDatas().get(position).getBusinessTime().get(0));
                store_time_bottom.setText(getDatas().get(position).getBusinessTime().get(1));
            } else {
                try {
                    store_time_top.setText(getDatas().get(position).getBusinessTime().get(0));
                    store_time_top.setVisibility(View.INVISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        store_time_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (store_time_bottom.getVisibility() == View.GONE) {
                    store_time_top.setCompoundDrawablesWithIntrinsicBounds
                            (null, null, context.getResources().getDrawable(R.mipmap.nearby_store_time_up), null);
                    store_time_bottom.setVisibility(View.VISIBLE);
                } else {
                    store_time_top.setCompoundDrawablesWithIntrinsicBounds
                            (null, null, context.getResources().getDrawable(R.mipmap.nearby_store_time_down), null);
                    store_time_bottom.setVisibility(View.GONE);
                }
            }
        });
        nearby_store_phoneTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moutClickListener != null) {
                    if (TextUtils.isEmpty(getDatas().get(position).getAppointmentPhone()) || getDatas().get(position).getAppointmentPhone() == null) {
                        Toast.makeText(context, "该商家未预留电话信息", Toast.LENGTH_LONG).show();
                    } else {
                        moutClickListener.outClick("phone", getDatas().get(position).getAppointmentPhone());
                    }
                }
            }
        });
        nearby_store_navigationTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (moutClickListener != null) {
                    moutClickListener.outClick("navigation", position);
                }
            }
        });
        top_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClick(position, getDatas().get(position).getShopId(), getDatas().get(position).getLongitude(), getDatas().get(position).getLatitude());
            }
        });

    }

    private ShopOnClickListener onClickListener;

    public void setShopOnClickListener(ShopOnClickListener shopOnClickListener) {
        this.onClickListener = shopOnClickListener;
    }

    public interface ShopOnClickListener {

        void onClick(int position, int shopId, double longitude, double latitude);
    }

}
