package com.health.servicecenter.contract;

import com.health.servicecenter.model.PointsSignInModel;
import com.health.servicecenter.model.RecommendModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.RecommendList;

import java.util.List;
import java.util.Map;

public interface PointsSignInContract {
    interface View extends BaseView {
        void onGetSignInDataSuccess(PointsSignInModel model);

        void onGetSignInSuccess();

        void onGetRecommendListSuccess(List<RecommendModel.RecommendGoods> pointGoodsList);

        void onGetGoodsListSuccess(List<RecommendList> list, int firstPageSize);

        void addShopCatSuccess(String result);
    }

    interface Presenter extends BasePresenter {

        void getSignInData(Map<String, Object> map);

        void signIn(Map<String, Object> map);

        void getRecommend(Map<String, Object> map);

        void getGoodsList(Map<String, Object> map);//获取推荐列表

        void addShopCat(Map<String,Object> map);
    }
}
