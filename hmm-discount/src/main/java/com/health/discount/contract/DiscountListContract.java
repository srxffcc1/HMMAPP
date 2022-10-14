package com.health.discount.contract;

import com.healthy.library.model.DiscountTopModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.GoodsBasketAll;
import com.healthy.library.model.MarketingGoodsList;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.model.PopDetailInfo;
import com.healthy.library.model.PopListInfo;

import java.util.List;
import java.util.Map;

public interface DiscountListContract {
    interface View extends BaseView {
        void onSucessGetList(List<PopDetailInfo.GoodsDTOListBean> result);

        void onSucessGetTopTitle(PopListInfo result);

        void successAddShopCat(String result);

        void onSucessGetBasketList(GoodsBasketAll goodsBasketAll);
    }

    interface Presenter extends BasePresenter {
        void getGoodsList(Map<String, Object> map);

       // void getTopTitle(String id,String marketingType);

        void getGoodsSpec(String goodsId);

        void addShopCat(Map<String, Object> map);

        void getBasketList(Map<String, Object> map);
    }
}
