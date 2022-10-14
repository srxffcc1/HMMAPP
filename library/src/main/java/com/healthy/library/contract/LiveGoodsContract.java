package com.healthy.library.contract;

import com.healthy.library.model.LiveVideoGoods;
import com.healthy.library.model.LiveVideoMain;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/27 11:20
 * @des
 */

public interface LiveGoodsContract {
    interface View extends BaseView {
        void onSucessGetLiveGoods(List<LiveVideoGoods> liveVideoGoods);

    }


    interface Presenter extends BasePresenter {
        void getLiveGoods(LiveVideoMain liveVideoMain, Map<String, Object> map);
    }
}
