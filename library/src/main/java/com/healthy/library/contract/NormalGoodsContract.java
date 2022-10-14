package com.healthy.library.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.LiveVideoGoods;
import com.healthy.library.model.LiveVideoMain;
import com.healthy.library.model.RecommendList;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/27 11:20
 * @des
 */

public interface NormalGoodsContract {
    interface View extends BaseView {
        void successGetGoodsRecommend(List<RecommendList> result,int firstPageSize);

    }


    interface Presenter extends BasePresenter {
        void getGoodsRecommend(Map<String, Object> map);
    }
}
