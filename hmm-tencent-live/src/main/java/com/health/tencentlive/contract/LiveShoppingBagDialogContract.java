package com.health.tencentlive.contract;

import com.health.tencentlive.model.JackpotList;
import com.healthy.library.model.LiveVideoGoods;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.ShopDetailModel;
import com.healthy.library.model.PageInfoEarly;

import java.util.HashMap;
import java.util.List;

/**
 * @author Li
 * @date 2019/03/27 11:20
 * @des
 */

public interface LiveShoppingBagDialogContract {

    interface View extends BaseView {

        void getLiveRoomGoodsListSuccess(List<LiveVideoGoods> result);

        void onGetStoreListSuccess(List<ShopDetailModel> result);

        void onGetMerchantStoreListSuccess(List<ShopDetailModel> result);

        void getSuccessJackpotList(List<JackpotList> result, PageInfoEarly pageInfo, int type);
    }

    interface Presenter extends BasePresenter {

        void getLiveRoomGoodsList(HashMap<String, Object> map);

        void getGoodsShopList(HashMap<String, Object> map);

        void getMerchantGoodsShopList(HashMap<String, Object> map);

        void getJackpotList(HashMap<String, Object> map, int type);
    }
}
