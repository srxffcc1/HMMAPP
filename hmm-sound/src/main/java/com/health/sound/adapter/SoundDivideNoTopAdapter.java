package com.health.sound.adapter;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.sound.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;

/**
 * @author Li
 * @date 2019/03/25 11:03
 * @des 推荐阅读
 */

public class SoundDivideNoTopAdapter extends BaseAdapter<String> {

    public SoundDivideNoTopAdapter() {
        this(R.layout.sound_item_divide_no_top);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {

    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    private SoundDivideNoTopAdapter(int layoutResId) {
        super(layoutResId);

    }

}
