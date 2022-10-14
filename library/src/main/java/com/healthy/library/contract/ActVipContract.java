package com.healthy.library.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.ActVip;
import com.healthy.library.model.GoodsBasketStore;
import com.healthy.library.model.LocVip;

import java.util.Map;

/**
 * @author Li
 * @date 2019/03/27 11:20
 * @des
 */

public interface ActVipContract {
    interface View extends BaseView {
        void onSucessGetVipActs(ActVip actVip,GoodsBasketStore goodsBasketStore);//购物车整体数据
        void onSucessGetVipShopDetail(ActVip.VipShop actVip);//

    }


    interface Presenter extends BasePresenter {
        void getVipActs(Map<String, Object> map, GoodsBasketStore goodsBasketStore);
        void getVipShopDetail(Map<String, Object> map);
    }
}
