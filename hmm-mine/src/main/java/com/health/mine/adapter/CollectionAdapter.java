package com.health.mine.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.mine.R;
import com.health.mine.model.CollectionModel;

/**
 * @author Li
 * @date 2019/04/29 17:12
 * @des 收藏夹列表
 */

public class CollectionAdapter extends BaseQuickAdapter<CollectionModel, BaseViewHolder> {

    public CollectionAdapter() {
        this(R.layout.mine_index_item_collection);
    }

    private CollectionAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, CollectionModel item) {
        ImageView iv = helper.getView(R.id.iv_cover);
        com.healthy.library.businessutil.GlideCopy.with(iv.getContext())
                .load(item.getImgUrl())
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_1_1_default2)

                .into(iv);
        helper.setText(R.id.tv_title, item.getTitle());
        helper.setText(R.id.tv_read_num, String.valueOf(item.getReadNum()));
    }
}
