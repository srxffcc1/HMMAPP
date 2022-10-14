package com.healthy.library.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.LocVip;

import java.util.Map;

/**
 * @author Li
 * @date 2019/03/27 11:20
 * @des
 */

public interface ChangeVipContract {
    interface View extends BaseView {

    }


    interface Presenter extends BasePresenter {
        void changeVip(Map<String, Object> map);
        void getLocVip(Map<String, Object> map);
    }
}
