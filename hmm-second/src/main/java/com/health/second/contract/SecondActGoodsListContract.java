package com.health.second.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.KKGroup;
import com.healthy.library.model.Kick;
import com.healthy.library.model.Kill;
import com.healthy.library.model.PageInfoEarly;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/04/29 16:12
 * @des
 */
public interface SecondActGoodsListContract {
    interface View extends BaseView {
        void onGetKillGoodsSucess(List<Kill> list, PageInfoEarly pageInfoEarly);
        void onGetPinGoodsSucess(List<KKGroup> list, PageInfoEarly pageInfoEarly);
        void onGetKickGoodsSucess(List<Kick> list, PageInfoEarly pageInfoEarly);
        void onGetActsGoodsTitleSucess(String result);

    }

    interface Presenter extends BasePresenter {
        void getKillGoodsList(Map<String, Object> map);
        void getPinGoodsList(Map<String, Object> map);
        void getKickGoodsList(Map<String, Object> map);
        void getAlreadyKillGoodsList(Map<String, Object> map);
        void getAlreadyPinGoodsList(Map<String, Object> map);
        void getAlreadyKickGoodsList(Map<String, Object> map);
    }
}
