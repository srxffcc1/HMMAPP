package com.health.servicecenter.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.GoodsSetAll;
import com.healthy.library.model.GoodsSetCell;
import com.healthy.library.model.GoodsSpecDetail;
import com.healthy.library.model.ShopDetailModel;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/04/29 16:12
 * @des
 */
public interface ServiceGoodsSetSpecContract {
    interface View extends BaseView {
        void successGetGoodsSkuResult(GoodsSetCell goodsSetCell,List<GoodsSpecDetail> result);
        void successGetGoodsSet(List<GoodsSetAll> goodsSetCellList);
        void onGetStoreListSuccess(List<ShopDetailModel> storeDetialModelList);
    }

    interface Presenter extends BasePresenter {

        void getGoodsSkuResult(GoodsSetCell goodsSetCell,Map<String, Object> map);
        void getGoodsSet(Map<String, Object> map);
        void getStoreList(String shopId);//获得所有门店
    }
}
