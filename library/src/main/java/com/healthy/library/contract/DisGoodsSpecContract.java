package com.healthy.library.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.DisGoodsSpecCell;

import java.util.List;

/**
 * @author Li
 * @date 2019/04/29 16:12
 * @des
 */
public interface DisGoodsSpecContract {
    interface View extends BaseView {
        void successGetGoodsSkuResult(List<DisGoodsSpecCell> result);
    }

    interface Presenter extends BasePresenter {

        void getGoodsSkuResult(String mapMarketingGoodsId);
    }
}
