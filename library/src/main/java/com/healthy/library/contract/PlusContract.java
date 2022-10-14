package com.healthy.library.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.Map;

/**
 * @author Li
 * @date 2019/03/27 11:20
 * @des
 */

public interface PlusContract {
    interface View extends BaseView {
        void onSucessCheckPlus(boolean isplus);

    }


    interface Presenter extends BasePresenter {

        void checkPlus(Map<String, Object> map);
    }
}
