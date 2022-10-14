package com.health.second.contract;

import com.health.second.model.PeopleListModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.CouponInfoZ;
import com.healthy.library.model.OrderListPageInfo;
import com.healthy.library.model.ShopDetailMarketing;
import com.healthy.library.model.ShopDetailModel;
import com.healthy.library.model.RecommendList;
import com.healthy.library.model.SortGoodsListModel;
import com.healthy.library.model.TechnicianResult;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/04/29 16:12
 * @des
 */
public interface ShopDetailContract {
    interface View extends BaseView {
        void onGetStoreDetailSuccess(ShopDetailModel storeDetailModel);

        void onGetPeopleListSuccess(List<PeopleListModel> result);

        void onGetGoodsListSuccess(List<SortGoodsListModel> list, OrderListPageInfo pageInfo);

        void onGetMarketingSuccess(List<ShopDetailMarketing> list);

        void onSuccessManDetail(TechnicianResult result);
    }

    interface Presenter extends BasePresenter {

        void getGoodsList(Map<String, Object> map);//获取推荐商品列表

        void getStoreDetail(String shopId);//获取门店详情

        void getPeopleList(Map<String, Object> map);//获取人员列表

        void getMarketingList(Map<String, Object> map);//获取优惠劵

        void getManDetail(String userId, PeopleListModel model);
    }
}
