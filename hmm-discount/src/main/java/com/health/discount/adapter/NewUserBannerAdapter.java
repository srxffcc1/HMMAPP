package com.health.discount.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.discount.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.model.AdModel;
import com.healthy.library.presenter.AdPresenterCopy;

import java.util.ArrayList;

public class NewUserBannerAdapter extends BaseAdapter<ArrayList<AdModel>> {
    AdPresenterCopy adPresenterCopy;

    public NewUserBannerAdapter() {
        this(R.layout.new_user_top_layout);
        adPresenterCopy = new AdPresenterCopy(context);
    }

    public NewUserBannerAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, final int position) {
        ImageView iv_topBanner = holder.getView(R.id.iv_topBanner);

    }
}
