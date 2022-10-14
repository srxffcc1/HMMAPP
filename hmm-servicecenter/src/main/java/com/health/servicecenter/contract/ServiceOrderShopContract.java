package com.health.servicecenter.contract;

import com.health.servicecenter.activity.StoreBlockChildHolder;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.GoodsBasketCell;
import com.healthy.library.model.GoodsBasketStore;
import com.healthy.library.model.GoodsFee;
import com.healthy.library.model.ShopDetailModel;

import java.util.List;
import java.util.Map;


public interface ServiceOrderShopContract {
    interface View extends BaseView {
        void onSucessGetPickShopOnly(List<com.healthy.library.model.GoodsShop> goodsShopList, String msg, GoodsBasketStore goodsBasketStore, StoreBlockChildHolder storeBlockChildHolder);
        void onSucessGetPickShop(List<com.healthy.library.model.GoodsShop> goodsShopList,GoodsBasketStore goodsBasketStore,StoreBlockChildHolder storeBlockChildHolder);
        void onSucessGetReceiveShop(List<com.healthy.library.model.GoodsShop> goodsShopList,GoodsBasketStore goodsBasketStore,StoreBlockChildHolder storeBlockChildHolder);
        void onSucessGetFeeOnly(GoodsFee goodsFee, GoodsBasketStore goodsBasketStore,StoreBlockChildHolder storeBlockChildHolder);
        void onSucessGetFee(GoodsFee goodsFee, GoodsBasketStore goodsBasketStore, StoreBlockChildHolder storeBlockChildHolder);
        void onSucessGetShopDetailOnly(ShopDetailModel shopDetailModel, GoodsBasketStore goodsBasketStore, StoreBlockChildHolder storeBlockChildHolder);
    }

    interface Presenter extends BasePresenter {
        void getShopDetailOnly(Map<String, Object> map, GoodsBasketStore goodsBasketStore,StoreBlockChildHolder storeBlockChildHolder);
        void getPickShopOnly(Map<String, Object> map, GoodsBasketStore goodsBasketStore,StoreBlockChildHolder storeBlockChildHolder);
        void getPickShop(Map<String, Object> map, GoodsBasketStore goodsBasketStore,StoreBlockChildHolder storeBlockChildHolder);
        void getFeeOnly(Map<String, Object> map, GoodsBasketStore goodsBasketStore,StoreBlockChildHolder storeBlockChildHolder);
        void getFee(Map<String, Object> map, GoodsBasketStore goodsBasketStore,StoreBlockChildHolder storeBlockChildHolder);
        void getSpecDetail(String cellGoodsMarketingType,Map<String, Object> map,GoodsBasketStore goodsBasketStore,GoodsBasketCell goodsBasketCell, GoodsBasketCell.OnItemChange onItemChange);
        void getReceiveShop(Map<String, Object> map, GoodsBasketStore goodsBasketStore,StoreBlockChildHolder storeBlockChildHolder);
    }
}
