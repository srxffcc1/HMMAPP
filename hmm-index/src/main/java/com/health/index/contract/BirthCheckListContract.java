package com.health.index.contract;

import com.health.index.model.BirthCheckModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.List;

/**
 * @author Li
 * @date 2019/04/24 15:29
 * @des 产检表
 */
public interface BirthCheckListContract {
    interface View extends BaseView {
        /**
         * 获取产检列表成功
         *
         * @param list 产检列表
         * @param id   定位id
         */
        void onGetBirthCheckListSuccess(List<BirthCheckModel> list, int id);
    }

    interface Presenter extends BasePresenter {
        /**
         * 获取产检列表
         */
        void getBirthCheckList();
    }
}
