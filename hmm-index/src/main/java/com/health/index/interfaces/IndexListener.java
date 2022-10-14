package com.health.index.interfaces;

import java.util.Date;

/**
 * @author Li
 * @date 2019/03/22 15:26
 * @des 首页列表监听
 */

public interface IndexListener {


    /**
     * 选择前一天
     *
     * @param date 前一天日期
     */
    void onDateDecrease(Date date);

    /**
     * 选择后一天
     *
     * @param date 后一天日期
     */
    void onDateIncrease(Date date);
}
