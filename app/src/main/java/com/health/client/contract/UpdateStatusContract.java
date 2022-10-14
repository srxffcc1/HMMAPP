package com.health.client.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.Map;

/**
 * @author Li
 * @date 2019/03/28 11:14
 * @des 更改用户状态
 */

public interface UpdateStatusContract {
    interface View extends BaseView {
        /**
         * 更改状态成功
         */
        void onUpdateSuccess();

        /**
         * 更改状态成功
         */
        void onUpdateSuccessEx();

        /**
         * 更改状态失败
         */
        void onUpdateFail();
    }

    interface Presenter extends BasePresenter {

        /**
         * 更改状态
         *
         * @param map 参数
         */
        void updateStatus(Map<String, Object> map);

        /**
         * 更改状态
         *
         * @param map 参数
         */
        void updateStatusEx(Map<String, Object> map);
    }
}
