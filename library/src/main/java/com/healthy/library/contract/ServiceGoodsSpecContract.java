package com.healthy.library.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.GoodsSpecDetail;
import com.healthy.library.model.GoodsSpecLimit;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/04/29 16:12
 * @des
 */
public interface ServiceGoodsSpecContract {
    interface View extends BaseView {
        void successGetGoodsSkuResult(List<GoodsSpecDetail> result);
        void successGetGoodsSkuFinalResult(GoodsSpecLimit result);
    }

    interface Presenter extends BasePresenter {

        void getGoodsSkuResult(Map<String, Object> map);
        void getGoodsLimitResult(Map<String, Object> map);
    }
}
