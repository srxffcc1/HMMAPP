package com.health.discount.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.discount.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;

public class PlusAreaBannerAdapter extends BaseAdapter<String> {

    public PlusAreaBannerAdapter(int viewId) {
        super(viewId);
    }

    public PlusAreaBannerAdapter() {
        this(R.layout.plus_banner_adapter_layout);
    }


    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        ImageView plusBanner = holder.itemView.findViewById(R.id.plusBanner);
        if (getModel().equals("1")) {
            plusBanner.setImageResource(R.drawable.plus_vip);
        } else {
            plusBanner.setImageResource(R.drawable.plus_area);
        }
    }
}
