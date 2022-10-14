package com.health.servicecenter.contract;

import com.healthy.library.model.Goods2DetailKick;
import com.healthy.library.model.GoodsBasketMisc;
import com.healthy.library.model.GoodsSetAll;
import com.healthy.library.model.GoodsTran;
import com.healthy.library.model.RecommendList;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/04/29 16:12
 * @des
 */
public interface ServiceGoodsDetailKickContract {
    interface View extends BaseView {
        void successAddGoodsBasket(String result);
        void successGetGoodsBasket(GoodsBasketMisc result);
        void successGetGoodsDetail(Goods2DetailKick goodsDetail);
        void successGetGoodsRecommendUnder(List<RecommendList> result);
        void successGetGoodsRecommend(List<RecommendList> result);
        void successGetGoodsChose(List<RecommendList> result);
        void successGetGoodsSet(List<GoodsSetAll> goodsSetCellList);
        void successGetGoodsTran(GoodsTran result);
    }

    interface Presenter extends BasePresenter {
        void getGoodsTran(Map<String, Object> map);
        void addGoodsBasket(Map<String, Object> map);
        void getGoodsBasket(Map<String, Object> map);
        void getGoodsDetail(Map<String, Object> map);
        void getGoodsSet(Map<String, Object> map);
        void getGoodsChose(Map<String, Object> map);
        void getGoodsRecommend(Map<String, Object> map);
        void getGoodsRecommendUnder(Map<String, Object> map);
    }
}
