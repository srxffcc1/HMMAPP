package com.health.servicecenter.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.GoodsDetail;
import com.healthy.library.model.GoodsRank;
import com.healthy.library.model.MallGroupSimple;
import com.healthy.library.model.ShopDetailModel;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/04/29 16:12
 * @des
 */
public interface ServiceGoodsDetailContract {
    interface View extends BaseView {
        void successGetGoodsDetail(GoodsDetail goodsDetail);
        void successGetGoodsDetailNormal(GoodsDetail goodsDetail);
        void onGetStoreDetialSuccess(ShopDetailModel storeDetialModel);
        void onGetStoreListSuccess(List<ShopDetailModel> storeDetialModelList);
        void onGetGoodsRankListSuccess(List<GoodsRank> list);
        void onSuccessGetGroupAlreadyList(List<MallGroupSimple> kkGroupSimples);
    }

    interface Presenter extends BasePresenter {
        void getGoodsDetail(Map<String, Object> map);
        void getGoodsDetailNormal(Map<String, Object> map);
        void getStoreDetial(String shopId);
        void getStoreList(String shopId);
        void getGoodsRankList();
        void getGroupAlreadyList(Map<String, Object> map);
    }
}
