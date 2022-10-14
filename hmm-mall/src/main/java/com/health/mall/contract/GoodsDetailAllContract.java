package com.health.mall.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.AssemableTeam;
import com.healthy.library.model.CommentModelOld;
import com.healthy.library.model.Goods2ShopModel;
import com.healthy.library.model.Goods2ShopModelKick;
import com.healthy.library.model.Goods2ShopModelPin;
import com.healthy.library.model.GoodsSetAll;
import com.healthy.library.model.GoodsSpecLimit;
import com.healthy.library.model.MallCoupon;
import com.healthy.library.model.MallGroupSimple;
import com.healthy.library.model.StoreMallSimpleModel;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/29 17:26
 * @des
 */

public interface GoodsDetailAllContract {
    interface View extends BaseView {
        void onGetGoodsDetailSuccessNt(Goods2ShopModel goodsModel);
        void onGetGoodsDetailSuccessKill(Goods2ShopModel goodsModel);
        void onGetGoodsDetailSuccessPin(Goods2ShopModelPin goodsModel);
        void onGetGoodsDetailSuccessKick(Goods2ShopModelKick goodsModel);
        void onGetGoodsDetailSuccessNormal(Goods2ShopModel goodsDetail);
        void onGetGoodsDetailSuccess(Goods2ShopModel goodsModel);
        void onGetCommentSuccess(CommentModelOld commentmodel);
        void onGetStoreSimpleSuccess(StoreMallSimpleModel storeMallSimpleModel);
        void onGetGoodsShopSuccess(String shopid);
        void successGetGoodsSet(List<GoodsSetAll> goodsSetCellList);
        void getSucessGoodsAppCouponList(List<MallCoupon> couponInfoByMerchants);
        void getSucessTeamList(List<AssemableTeam> couponInfoByMerchants);
        void onSuccessGetGroupAlreadyList(List<MallGroupSimple> kkGroupSimples);
        void successGetGoodsSkuFinalResult(GoodsSpecLimit result);
        void onSucessIsNewAppMember(int result);
    }

    interface Presenter extends BasePresenter {

        void getStoreDetail(Map<String, Object> map);
        void getGoodsDetail(Map<String, Object> map);
        void getComment(Map<String, Object> map);
        void getGoodsAppCouponList(Map<String, Object> map);
        void getGoodsShop(Map<String, Object> map);
        void getGoodsSet(Map<String, Object> map);

        void getGoodsDetailKick(Map<String, Object> map);
        void getGoodsDetailPin(Map<String, Object> map);


        void getTeamList(Map<String, Object> map);
        void getGroupAlreadyList(Map<String, Object> map);
        void getGoodsLimitResult(Map<String, Object> map);
        void getIsNewAppMember();
    }
}
