package com.health.mine.contract;

import com.health.mine.model.BackDetialModel;
import com.health.mine.model.OrderBackDetailModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.Map;

/**
 * @author Li
 * @date 2019/04/10 16:02
 * @des 订单详情
 */

public interface OrderBackDetailContract {

    interface View extends BaseView {
        void onGetOrderDetailSuccess(OrderBackDetailModel detailModel);

        void onGetBackDetialSuccess(BackDetialModel detialModel);
    }

    interface Presenter extends BasePresenter {
        void getOrderDetail(Map<String, Object> map);

        void getBackDetial(String id);
    }


}
