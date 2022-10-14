package com.healthy.library.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.healthy.library.R;

/**
 * @author Li
 * @date 2019/03/25 11:03
 * @des 推荐阅读
 */

public class EmptyFragmentAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public EmptyFragmentAdapter() {
        this(R.layout.item_empty_fragment);
    }

    public EmptyFragmentAdapter(int layoutResId) {
        super(layoutResId);

    }

    @Override
    protected void convert(final BaseViewHolder helper, final String item) {

        TextView tv_type=helper.getView(R.id.tv_type);
        tv_type.setText("第"+item+"行");
        tv_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData.remove(helper.getAdapterPosition());
                notifyItemRemoved(helper.getAdapterPosition());
            }
        });
    }

}
