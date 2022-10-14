package com.healthy.library.interfaces;

import android.view.View;

/**
 * @author Li
 * @date 2019/03/04 16:05
 * @des 列表item点击监听接口
 */

public interface OnItemClickListener {
    /**
     * item点击
     *
     * @param pos  位置
     * @param view 点击控件
     */
    void onItemClick(int pos, View view);
}
