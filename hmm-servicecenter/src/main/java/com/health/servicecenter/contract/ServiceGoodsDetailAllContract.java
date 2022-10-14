package com.health.servicecenter.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.ActVip;
import com.healthy.library.model.AssemableTeam;
import com.healthy.library.model.CouponInfoZ;
import com.healthy.library.model.Goods2DetailKick;
import com.healthy.library.model.Goods2DetailPin;
import com.healthy.library.model.GoodsDetail;
import com.healthy.library.model.GoodsRank;
import com.healthy.library.model.GoodsSetAll;
import com.healthy.library.model.GoodsSpecDetail;
import com.healthy.library.model.GoodsSpecLimit;
import com.healthy.library.model.GoodsTran;
import com.healthy.library.model.IntegralModel;
import com.healthy.library.model.MallGroupSimple;
import com.healthy.library.model.ShopDetailModel;
import com.healthy.library.model.RecommendList;
import com.healthy.library.model.ShopCartModel;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/04/29 16:12
 * @des
 */
public interface ServiceGoodsDetailAllContract {
    interface View extends BaseView {
        void successAddGoodsBasket(String result);

        void successGetActRule(String result);

        void successGetGoodsBasket(ShopCartModel result);

        void successGetGoodsDetail(GoodsDetail goodsDetail);

        void successGetGoodsDetailWithCode(GoodsDetail goodsDetail);

        void successGetGoodsDetailNormal(GoodsDetail goodsDetail);

        void successGetGoodsDetailKill(GoodsDetail goodsDetail);

        void successGetGoodsDetailAct(GoodsDetail goodsDetail);

        void successGetGoodsDetailNt(GoodsDetail goodsDetail);

        void successGetGoodsDetailPin(Goods2DetailPin goodsDetail);

        void successGetGoodsDetailKick(Goods2DetailKick goodsDetail);

        void successGetGoodsRecommendUnder(List<RecommendList> result);

        void successGetGoodsRecommend(List<RecommendList> result);

        void successGetGoodsChose(List<RecommendList> result);

        void successGetGoodsSet(List<GoodsSetAll> goodsSetCellList);

        void successGetGoodsTran(GoodsTran result);

        void getSucessTeamList(List<AssemableTeam> couponInfoByMerchants);

        void onSuccessGetGroupAlreadyList(List<MallGroupSimple> kkGroupSimples);

        void successGetGoodsSkuFinalResult(GoodsSpecLimit result);

        void onSucessIsNewAppMember(int result);

        void onSucessGetSkuExList(List<GoodsSpecDetail> list);

        void onVipInfoSuccess(IntegralModel vipInfo);

        void sucessGetCouponList(List<CouponInfoZ> result);

        void sucessGetActShop(ActVip.VipShop result);

        void successGetActVip(ActVip result);

        /**
         * 门店详情回调
         * @param storeDetialModel 门店详情数据模型
         */
        void onGetStoreDetialSuccess(ShopDetailModel storeDetialModel);

        void onGetStoreListSuccess(List<ShopDetailModel> storeDetialModelList);

        void onGetGoodsRankListSuccess(List<GoodsRank> list);
    }

    interface Presenter extends BasePresenter {
        void getStoreDetial(String shopId);

        void getStoreList(String shopId);//获得所有门店

        void getGoodsTran(Map<String, Object> map);

        void getGoodsActRule(Map<String, Object> map);

        void addGoodsBasket(Map<String, Object> map);

        void getGoodsBasket(Map<String, Object> map);

        void getGoodsDetail(Map<String, Object> map);

        void getGoodsDetailWithCode(Map<String, Object> map);

        void getGoodsDetailKick(Map<String, Object> map);

        void getGoodsDetailKickSkuEx(Map<String, Object> map);

        void getGoodsDetailPinSkuEx(Map<String, Object> map);

        void getGoodsDetailPin(Map<String, Object> map);

        void getGoodsSet(Map<String, Object> map);

        void getGoodsChose(Map<String, Object> map);

        void getGoodsRecommend(Map<String, Object> map);

        void getGoodsRecommendUnder(Map<String, Object> map);

        void getTeamList(Map<String, Object> map);

        void getGroupAlreadyList(Map<String, Object> map);

        void getGoodsLimitResult(Map<String, Object> map);

        void getIsNewAppMember();

        void getVipInfo(Map<String, Object> map);

        void getGoodsCouponList(Map<String, Object> map);

        void getGoodsActShop(Map<String, Object> map);

        void getActVip(Map<String, Object> map);

        void getGoodsRankList();
    }
}
