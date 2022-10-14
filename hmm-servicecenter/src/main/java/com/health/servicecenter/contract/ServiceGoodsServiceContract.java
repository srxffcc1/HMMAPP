package com.health.servicecenter.contract;

import com.healthy.library.model.RecommendList;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/04/29 16:12
 * @des
 */
public interface ServiceGoodsServiceContract {
    interface View extends BaseView {
        void successGetGoodsRecommend(List<RecommendList> result);
    }

    interface Presenter extends BasePresenter {

        void getGoodsRecommend(Map<String, Object> map);
    }
}
