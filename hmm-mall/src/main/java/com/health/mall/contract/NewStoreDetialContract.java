package com.health.mall.contract;

import com.health.mall.model.PeopleListModel;
import com.healthy.library.model.CouponInfoZ;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.ShopDetailModel;
import com.healthy.library.model.RecommendList;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/04/29 16:12
 * @des
 */
public interface NewStoreDetialContract {
    interface View extends BaseView {
        void onGetStoreDetialSuccess(ShopDetailModel storeDetialModel);

        void onGetPeopleListSuccess(List<PeopleListModel> result);

        void onGetGoodsListSuccess(List<RecommendList> list);

        void onGetCouponListSuccess(List<CouponInfoZ> list);

        void successAddShopCat(String result);
    }

    interface Presenter extends BasePresenter {

        void getGoodsList(Map<String, Object> map);//获取推荐商品列表

        void getStoreDetial(String shopId);//获取门店详情

        void getPeopleList(Map<String, Object> map);//获取人员列表

        void getCouponList(Map<String, Object> map);//获取优惠劵

        void addShopCat(Map<String, Object> map);//加入购物车
    }
}
