package com.health.servicecenter.contract;

import com.healthy.library.model.CategoryModel;
import com.healthy.library.model.HotGoodsList;
import com.healthy.library.model.RecommendList;
import com.healthy.library.model.ShopCartModel;
import com.healthy.library.model.StoreListModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/04/29 16:12
 * @des
 */
public interface ServiceMainContract {
    interface View extends BaseView {
        void onGetRecommendListSuccess(List<RecommendList> list);

        void onGetHotGoodsListSuccess(List<HotGoodsList> list);

        void onGetStoreListSuccess(List<StoreListModel> list);

        void onGetBannerListSuccess(List<String> list);

        void onGetCategoryListSuccess(List<CategoryModel> list);

        void onGetShopCartSuccess(ShopCartModel shopCartModel);

        void successAddShopCat(String result);

        void ckeckShopList(String code);
    }

    interface Presenter extends BasePresenter {
        void getCategoryList();//查询一级品类列表

        void getStoreList();//获取实体门店列表

        void onGetHotGoodsList();//获取爆款商品

        void getBannerList();//获取Banner列表

        void getRecommendList(Map<String, Object> map);//获取推荐列表

        void getShopCart();//获取购物车数据

        void addShopCat(Map<String, Object> map);//加入购物车

    }
}
