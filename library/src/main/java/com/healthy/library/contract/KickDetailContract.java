package com.healthy.library.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.Goods2DetailKick;
import com.healthy.library.model.Kick;
import com.healthy.library.model.KickResult;
import com.healthy.library.model.KickResultFail;
import com.healthy.library.model.KickUser;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.model.ShopDetailModel;

import java.util.List;
import java.util.Map;

public interface KickDetailContract {
    interface View extends BaseView {
        void onSuccessGetKickManList(List<KickUser> kickList, PageInfoEarly pageInfoEarly);
        void onSuccessGetKickDetail(Kick kickDetail);
        void onSuccessKick(KickResult result);
        void onFailKick(String result);
        void onFailKick(String result, KickResultFail kickResultFail);
        void onSuccessKickHelp(KickResult result);
        void successGetGoodsDetail(Goods2DetailKick goodsDetail);
        void onSucessGetShopDetailOnly(ShopDetailModel shopDetailModel);
    }
    interface Presenter extends BasePresenter {
        void getKickManList(Map<String, Object> map);
        void getKickDetail(Map<String, Object> map);
        void kick(Map<String, Object> map);
        void kickHelp(Map<String, Object> map);
        void getGoodsDetail(Map<String, Object> map);
        void getUserInfo();
        void getShopDetailOnly(Map<String, Object> map);
    }
}
