package com.health.index.adapter;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.index.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;

/**
 * @author Li
 * @date 2019/03/25 11:03
 * @des 推荐阅读
 */

public class ToolsEatTitleAdapter extends BaseAdapter<String> {

    boolean needMore=true;

    String categoryId;

    public void setAudioType(String audioType) {
        this.audioType = audioType;
    }

    String audioType;

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public void setNeedMore(boolean needMore) {
        this.needMore = needMore;
    }

    public ToolsEatTitleAdapter() {
        this(R.layout.index_activity_tools_item_caneat_main_title);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {

    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    private ToolsEatTitleAdapter(int layoutResId) {
        super(layoutResId);

    }

    private void initView() {

    }
}
