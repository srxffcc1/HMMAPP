package com.health.index.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.index.R;
import com.health.index.model.TipModel;

import java.util.Locale;

/**
 * @author Li
 * @date 2019/04/28 15:07
 * @des 贴士列表
 */
public class TipAdapter extends BaseQuickAdapter<TipModel, BaseViewHolder> {


    public TipAdapter() {
        this(R.layout.index_item_tip);
    }

    private TipAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, TipModel item) {
        helper.setText(R.id.tv_title, item.getTitle());
        helper.setText(R.id.tv_read_num, String.format(Locale.CHINA, "%d", item.getReadNum()));
        ImageView iv = helper.getView(R.id.iv_tip);
        com.healthy.library.businessutil.GlideCopy.with(mContext)
                .load(item.getImgUrl())
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_1_1_default2)
                .into(iv);
    }
}
