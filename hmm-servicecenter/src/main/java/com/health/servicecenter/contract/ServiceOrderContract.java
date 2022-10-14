package com.health.servicecenter.contract;

import com.healthy.library.model.AddressListModel;
import com.healthy.library.model.GoodsFee;
import com.healthy.library.model.GoodsShop;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.List;
import java.util.Map;


public interface ServiceOrderContract {
    interface View extends BaseView {
        void onSucessGetPickShopOnly(List<GoodsShop> goodsShopList,String msg);
        void onSucessGetPickShop(List<GoodsShop> goodsShopList);
        void onSucessGetPickServiceShop(List<GoodsShop> goodsShopServiceList);
        void sucessSubmit(String result);
        void onGetAddressListSuccess(List<AddressListModel> listModels);
        void sucessFee(GoodsFee result);
    }

    interface Presenter extends BasePresenter {
        void getPickShopOnly(Map<String, Object> map);
        void getPickShop(Map<String, Object> map);
        void getPickServiceShop(Map<String, Object> map);
        void getPickShopFee(Map<String, Object> map);
        void submitOrder(Map<String, Object> map);
        void getAddressList(Map<String, Object> map);//获取收货地址列表
    }
}
