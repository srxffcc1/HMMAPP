package com.health.index.contract;

import com.health.index.model.ChangeModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/04/26 11:28
 * @des 变化
 */

public interface ChangeContract {
    interface View extends BaseView {

        /**
         * 获取变化成功
         *
         * @param list    变化
         * @param weekPos 第几周
         */
        void onGetChangesSuccess(List<ChangeModel> list, int weekPos);
    }

    interface Presenter extends BasePresenter {
        /**
         * 获取变化
         *
         * @param map 参数
         */
        void getChange(Map<String, Object> map);

    }
}