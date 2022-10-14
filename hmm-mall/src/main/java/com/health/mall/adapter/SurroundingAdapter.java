package com.health.mall.adapter;

import com.amap.api.services.core.PoiItem;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.mall.R;

/**
 * @author Li
 * @date 2019/03/07 15:21
 * @des 周边搜索
 */

public class SurroundingAdapter extends BaseQuickAdapter<PoiItem, BaseViewHolder> {
    public SurroundingAdapter() {
        this(R.layout.item_surrounding);
    }

    public SurroundingAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, PoiItem item) {
        helper.setText(R.id.txt_name, item.getTitle());
        helper.setText(R.id.txt_address, item.getSnippet());
    }
}
