package com.health.discount.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.ActVip;
import com.healthy.library.model.FlashBuyTab;
import com.healthy.library.model.PopListInfo;

import java.util.List;
import java.util.Map;

public interface FlashBuyContract {
    interface View extends BaseView {
        void onSuccessGetTabList(List<FlashBuyTab> result);

        void successAddShopCat(String result);

        void onSuccessGetActList(List<PopListInfo> result);

        void onSuccessGetYTBShopDetail(ActVip.VipShop actVip);

        void onSuccessGetGoodsList(PopListInfo popListInfo,List<PopListInfo> result);
    }

    interface Presenter extends BasePresenter {
        void getTabList(Map<String, Object> map);

        void getActList(Map<String, Object> map);

        void addShopCat(Map<String, Object> map);

        void getYTBShopDetail(Map<String, Object> map);

        void getGoodsList(Map<String, Object> map, final PopListInfo popListInfo,List<PopListInfo> result);
    }
}
