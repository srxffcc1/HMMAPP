package com.health.discount.contract;

import com.health.discount.model.PointOption;
import com.health.discount.model.PointTab;
import com.health.discount.model.UserPoint;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.AdModel;
import com.healthy.library.model.IntegralModel;
import com.healthy.library.model.LotteryModel;

import java.util.List;
import java.util.Map;

public interface PointHomeContract {
    interface View extends BaseView {

        void onVipInfoSuccess(IntegralModel vipInfo);
        void onPointTabSuccess(List<PointTab> pointTab);
        void onPointOptionSuccess(List<PointOption> optionList);
        void onPointRecommendSuccess(List<PointTab.PointGoods> pointGoodsList);
        void onGetUserInfoSuccess(UserPoint userInfoModel);
        void onSucessGetTopAds(List<AdModel> adModels);
        void onSucessGetCenterAds(List<AdModel> adModels);
        void onLotteryInfoSuccess(LotteryModel lotteryModel);
    }
    interface Presenter extends BasePresenter {
        void getVipInfo(Map<String, Object> map);
        void getPointTab(Map<String, Object> map);
        void getPointOption(Map<String, Object> map);
        void getRecommend(Map<String, Object> map);
        void getUserInfo();
        void getBannerTop(Map<String, Object> map);
        void getBannerCenter(Map<String, Object> map);
        void getLotteryInfo(Map<String,Object> map);
    }
}
