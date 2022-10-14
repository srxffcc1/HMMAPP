package com.health.discount.adapter;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.discount.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;

/**
 * @author Li
 * @date 2019/03/25 11:03
 * @des 推荐阅读
 */

public class PointBlockHomeRecommendTitleAdapter extends BaseAdapter<String> {
    public PointBlockHomeRecommendTitleAdapter() {
        this(R.layout.dis_item_activity_point_recommand_title);
    }

    public PointBlockHomeRecommendTitleAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, final int position) {


    }
}
