package com.healthy.library.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.ActVip;
import com.healthy.library.model.GoodsBasketCell;
import com.healthy.library.model.GoodsDetail;

import java.util.Map;

/**
 * @author Li
 * @date 2019/03/27 11:20
 * @des
 */

public interface ActVipOnlineContract {
    interface View extends BaseView {
        void onSucessGetVipOnlineGoods();//

    }


    interface Presenter extends BasePresenter {
        void getVipOnlineGoodsWithCell(Map<String, Object> map, GoodsBasketCell goodsBasketCell);//
        void getVipOnlineGoodsWithVipCell(Map<String, Object> map, ActVip.PopDetail popDetail);//
        void getVipOnlineGoodsWithVipSale(Map<String, Object> map, ActVip.SaleInfo saleInfo);//
    }
}
