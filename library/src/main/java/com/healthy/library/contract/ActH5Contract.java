package com.healthy.library.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.ActVip;
import com.healthy.library.model.GoodsBasketStore;

import java.util.Map;

/**
 * @author Li
 * @date 2019/03/27 11:20
 * @des
 */

public interface ActH5Contract {
    interface View extends BaseView {

    }


    interface Presenter extends BasePresenter {
        void getH5();//4033
    }
}
