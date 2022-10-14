package com.health.mall.adapter;

import com.amap.api.services.help.Tip;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.mall.R;

/**
 * @author Li
 * @date 2019/03/07 15:21
 * @des 关键字搜索
 */

public class KeywordAdapter extends BaseQuickAdapter<Tip, BaseViewHolder> {

    public KeywordAdapter() {
        this(R.layout.item_keywords);
    }

    private KeywordAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, Tip item) {
        helper.setText(R.id.txt_name, item.getName());
        helper.setText(R.id.txt_address, item.getAddress());
    }
}
