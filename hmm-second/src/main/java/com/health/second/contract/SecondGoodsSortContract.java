package com.health.second.contract;

import com.healthy.library.model.SecondSortGoods;
import com.health.second.model.SecondSortShop;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.GoodsDetail;
import com.healthy.library.model.PageInfoEarly;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/04/29 16:12
 * @des
 */
public interface SecondGoodsSortContract {
    interface View extends BaseView {


        void onGetActSortGoodsSuccess(List<SecondSortGoods> list, PageInfoEarly pageInfo);
        void onGetActSortShopsSuccess(List<SecondSortShop> list, PageInfoEarly pageInfo);
        void onGetActSortGoodDetailSucess(GoodsDetail result);
        void addShopCatSuccess(String result);

    }

    interface Presenter extends BasePresenter {
        void getActSortGoodDetail(Map<String, Object> map);
        void getActSortGoods(Map<String, Object> map);
        void getActSortShops(Map<String, Object> map);
        void addShopCat(Map<String,Object> map);
    }
}
