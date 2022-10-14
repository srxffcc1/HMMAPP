package com.health.mall.contract;

import com.health.mall.model.TypeModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.List;

/**
 * @author Li
 * @date 2019/03/29 14:30
 * @des
 */

public interface TypeListContract {
    interface View extends BaseView {

        /**
         * 获取列表成功
         *
         * @param list 门店列表
         */
        void onGetListSuccess(List<TypeModel> list);

        /**
         * 获取门店列表失败
         */
        void onGetStoreListFail();
    }

    interface Presenter extends BasePresenter {
        /**
         * 获取列表
         *
         * @param pageNumber page
         */
        void getList(String shopId);
    }
}
