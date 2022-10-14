package com.health.mall.contract;

import com.healthy.library.model.AssemableTeam;
import com.healthy.library.model.CommentModelOld;
import com.healthy.library.model.Goods2ShopModelKick;
import com.healthy.library.model.MallGroupSimple;
import com.healthy.library.model.StoreMallSimpleModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/29 17:26
 * @des
 */

public interface GoodsDetailKickContract {
    interface View extends BaseView {


        void onGetGoodsDetailSuccess(Goods2ShopModelKick goodsModel);

        void onGetCommentSuccess(CommentModelOld commentmodel);

        void onGetGoodsDetailFail();

        void onGetStoreSimpleSuccess(StoreMallSimpleModel storeMallSimpleModel);

        void getSucessTeamList(List<AssemableTeam> couponInfoByMerchants);
        void onSuccessGetGroupAlreadyList(List<MallGroupSimple> kkGroupSimples);
    }

    interface Presenter extends BasePresenter {

        void getGoodsDetail(String goodsId, String shopId, String cityNo, String areaNo, String longitude, String latitude);

        void getComment(String shopId, String searchType, String currentPage, String pageSize);

        void getTeamList(Map<String, Object> map);

        void getGroupAlreadyList(Map<String, Object> map);

        void getStoreDetail(String shopId, String goodsId, String cityNo, String lng, String lat);

    }
}
