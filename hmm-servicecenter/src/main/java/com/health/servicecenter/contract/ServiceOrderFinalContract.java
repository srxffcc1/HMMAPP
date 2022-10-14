package com.health.servicecenter.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.AddressListModel;
import com.healthy.library.model.GoodsBasketStore;
import com.healthy.library.model.ShopDetailModel;

import java.util.List;
import java.util.Map;


public interface ServiceOrderFinalContract {
    interface View extends BaseView {
        void sucessSubmit(String result);
        void sucessSubmitV(String totalPayAmount,String totalPayAmountNoFee);
        void failOrder(String result);
        void onGetAddressListSuccess(List<AddressListModel> listModels);
        void sucessCheck(boolean crossStore,boolean needTime);
        void sucessFeeCheck(boolean result);
        void onSucessIsNewAppMember(int result);
        void onSucessGetShopDetailOnly(GoodsBasketStore goodsBasketStore);
        void onSucessGetShopDetailOnlyVisit(ShopDetailModel shopDetailModel);
    }

    interface Presenter extends BasePresenter {
        void getIsNewAppMember();
        void submitOrder(Map<String, Object> map);
        void submitOrderV(Map<String, Object> map);
        void submitOrderU(Map<String, Object> map);
        void getAddressList(Map<String, Object> map);//获取收货地址列表
        void checkMearchantOpenChange(Map<String, Object> map);
        void checkMearchantFeeOpenChange(Map<String, Object> map);
        void getShopDetailOnly(Map<String, Object> map, final GoodsBasketStore goodsBasketStore);
        void getShopDetailOnlyVisit(Map<String, Object> map);
    }
}
