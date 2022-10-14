package com.health.second.contract;

import com.health.second.model.PeopleListModel;
import com.health.second.model.SecondType;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.CouponInfoZ;
import com.healthy.library.model.ProvinceCityModel;
import com.healthy.library.model.RecommendList;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/04/29 16:12
 * @des
 */
public interface SecondTypeContract {
    interface View extends BaseView {
        void onGetFirstTypeMenuSucess(List<SecondType> list);
        void onGetRecommendTypeMenuSucess(List<SecondType.SecondTypeMenu> list);
        void onGetTypeMenuSucess(List<SecondType.SecondTypeMenu> list);
        void onGetTypeMenuBindTypeSucess(List<SecondType.SecondTypeMenu> list,SecondType secondType);
        void onGetLocationListSuccess(List<ProvinceCityModel> provinceCityModels);

    }

    interface Presenter extends BasePresenter {
        void getFirstTypeMenu(Map<String, Object> map);
        void getRecommendTypeMenu(Map<String, Object> map);
        void getTypeMenu(Map<String, Object> map);
        void getTypeMenuBindType(Map<String, Object> map,SecondType secondType);
        void getLocationList(String fatherId);
    }
}
