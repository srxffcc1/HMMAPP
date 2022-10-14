package com.health.index.contract;

import com.health.index.model.TipModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.Date;
import java.util.List;

/**
 * @author Li
 * @date 2019/04/28 15:08
 * @des
 */
public interface TipsContract {
    interface View extends BaseView {
        /**
         * 获取贴士列表成功
         *
         * @param list      贴士列表
         * @param des       描述  类似于几周几天
         * @param status    当前状态  用于切换小图标
         * @param queryDate 查询的日期
         */
        void onGetTipListSuccess(List<TipModel> list, String des, String status, Date queryDate);
    }

    interface Presenter extends BasePresenter {
        /**
         * 获取贴士列表
         *
         * @param date 查询时间
         */
        void getTipList(Date date);
    }
}
