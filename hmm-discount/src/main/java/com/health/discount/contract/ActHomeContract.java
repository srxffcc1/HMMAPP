package com.health.discount.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.ActGroup;
import com.healthy.library.model.ActKick;
import com.healthy.library.model.ActKill;
import com.healthy.library.model.ActTabInfo;
import com.healthy.library.model.AdModel;

import java.util.List;
import java.util.Map;

public interface ActHomeContract {
    interface View extends BaseView {
        void onSucessGetActTabs(List<ActTabInfo> actTabInfos);
        void onSucessGetActMan(boolean hasman);
        void onSucessGetTopAds(List<AdModel> adModels);
        void onSucessGetCenterAds(List<AdModel> adModels);
        void onSucessGetBottomAds(List<AdModel> adModels);
        void onSucessGetActKick(List<ActKick> list);
        void onSucessGetActGroup(List<ActGroup> list);
        void onSucessGetActKill(List<ActKill> list);

    }
    interface Presenter extends BasePresenter {
        void getActKick(Map<String, Object> map);
        void getActPin(Map<String, Object> map);
        void getActKill(Map<String, Object> map);
        void getActLocVip(Map<String, Object> map);
        void getActBlockMan(Map<String, Object> map);
        void getActTabs(Map<String, Object> map);
        void getBannerTop(Map<String, Object> map);
        void getBannerCenter(Map<String, Object> map);
        void getBannerBottom(Map<String, Object> map);
    }
}
