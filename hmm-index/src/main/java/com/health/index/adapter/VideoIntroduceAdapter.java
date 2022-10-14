package com.health.index.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.index.R;
import com.healthy.library.model.VideoListModel;



public class VideoIntroduceAdapter extends BaseQuickAdapter<VideoListModel, BaseViewHolder> {

    public VideoIntroduceAdapter() {
        this(R.layout.item_video_introduce_layout);
    }

    private VideoIntroduceAdapter(int layoutResId) {
        super(layoutResId);

    }

    @Override
    protected void convert(BaseViewHolder helper, final VideoListModel item) {

    }
}
