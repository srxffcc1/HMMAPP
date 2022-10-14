package com.health.mine.contract;

import com.health.mine.model.HanMomGoodsListModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/27 11:20
 * @des
 */

public interface HanMomFragmentContract {
    interface View extends BaseView {
        void onGetGoodsSuccess(List<HanMomGoodsListModel> listModels);
    }


    interface Presenter extends BasePresenter {
        void getGoods(Map<String, Object> map);
    }
}
