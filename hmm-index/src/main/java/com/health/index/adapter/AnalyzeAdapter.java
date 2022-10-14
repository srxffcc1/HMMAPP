package com.health.index.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.index.R;
import com.health.index.model.AnalyzeModel;

/**
 * @author Li
 * @date 2019/04/25 15:34
 * @des B超常见项目
 */
public class AnalyzeAdapter extends BaseQuickAdapter<AnalyzeModel, BaseViewHolder> {
    public AnalyzeAdapter() {
        this(R.layout.index_item_analyze_project);
    }

    private AnalyzeAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, AnalyzeModel item) {
        helper.setText(R.id.tv_name, item.getName());
    }
}
