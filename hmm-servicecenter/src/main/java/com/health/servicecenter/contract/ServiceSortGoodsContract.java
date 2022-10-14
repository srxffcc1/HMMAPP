package com.health.servicecenter.contract;

import com.healthy.library.model.OrderListPageInfo;
import com.healthy.library.model.RecommendList;
import com.healthy.library.model.ShopCartModel;
import com.healthy.library.model.SortGoodsListModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/04/29 16:12
 * @des
 */
public interface ServiceSortGoodsContract {
    interface View extends BaseView {

        void onGetGoodsListSuccess(List<SortGoodsListModel> list, OrderListPageInfo pageInfo);

        void successAddShopCat(String result);

        void onGetShopCartSuccess(ShopCartModel shopCartModel);

        void onGetRecommendListSuccess(List<RecommendList> list);
    }

    interface Presenter extends BasePresenter {


        void getGoodsList(Map<String, Object> map);//根据分类获取商品列表

        void addShopCat(Map<String, Object> map);//加入购物车

        void getShopCart();//获取购物车数据

        void getRecommendList(Map<String, Object> map);//获取推荐商品列表

        void addSearchRecord(Map<String, Object> map);//记录搜索历史
    }
}
