package com.healthy.library.interfaces;

import java.util.Date;

/**
 * @author Li
 * @date 2019/03/19 15:52
 * @des 滚轮
 */

public interface OnDateConfirmListener {
    /**
     * 点击完成事件
     *
     * @param pos  滚轮位置
     * @param date 时间
     */
    void onConfirm(int pos, Date date);
}
