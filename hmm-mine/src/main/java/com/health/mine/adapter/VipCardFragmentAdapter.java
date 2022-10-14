package com.health.mine.adapter;

import android.view.View;
import android.widget.ScrollView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.health.mine.R;

/**
 * @author Li
 * @date 2019/03/25 11:03
 * @des 推荐阅读
 */

public class VipCardFragmentAdapter extends BaseQuickAdapter<String, VipCardFragmentAdapter.ScrollViewHolder> {

    public VipCardFragmentAdapter() {
        this(R.layout.mine_item_vip_card);
    }

    private VipCardFragmentAdapter(int layoutResId) {
        super(layoutResId);

    }

    @Override
    protected void convert(ScrollViewHolder helper, final String item) {


    }
    public class ScrollViewHolder extends com.chad.library.adapter.base.BaseViewHolder{
        ScrollView mall_nsv;

        public ScrollViewHolder(View view) {
            super(view);
            mall_nsv=view.findViewById(R.id.canUseCardStoreSc);
        }

        public boolean isTouchNsv(float x, float y) {
            int[] pos = new int[2];
            //获取sv在屏幕上的位置
            mall_nsv.getLocationOnScreen(pos);
            int width = mall_nsv.getMeasuredWidth();
            int height = mall_nsv.getMeasuredHeight();
            return x >= pos[0] && x <= pos[0] + width && y >= pos[1] && y <= pos[1] + height;
        }
    }
}
