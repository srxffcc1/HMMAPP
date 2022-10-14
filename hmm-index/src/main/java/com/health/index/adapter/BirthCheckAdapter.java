package com.health.index.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.index.R;
import com.health.index.model.BirthCheckModel;

/**
 * @author Li
 * @date 2019/04/24 15:38
 * @des
 */

public class BirthCheckAdapter extends BaseQuickAdapter<BirthCheckModel, BaseViewHolder> {
    public BirthCheckAdapter() {
        this(R.layout.index_item_birth_check);
    }

    private BirthCheckAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, BirthCheckModel item) {
        helper.setText(R.id.tv_date, item.getDate());
        helper.setText(R.id.tv_title, item.getTitle());
        helper.setText(R.id.tv_keys, item.getKeys());
    }
}
