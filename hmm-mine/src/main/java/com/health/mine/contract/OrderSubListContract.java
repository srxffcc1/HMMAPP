package com.health.mine.contract;

import com.health.mine.model.OrderSubDetailModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.PageInfoEarly;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/04/10 16:02
 * @des 订单详情
 */

public interface OrderSubListContract {

    interface View extends BaseView {
        void onSucessGetSubList(List<OrderSubDetailModel> orderSubDetailModels, PageInfoEarly pageInfo);
        void onSucessSubUpdate();
    }

    interface Presenter extends BasePresenter {
        void getOrderSubList(Map<String, Object> map);
        void subUpdate(Map<String, Object> map);
    }


}
