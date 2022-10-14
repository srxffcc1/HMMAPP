package com.health.discount.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.Kick;
import com.healthy.library.model.KickResult;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.model.ShopDetailModel;

import java.util.List;
import java.util.Map;

public interface KickHelpDetailContract {
    interface View extends BaseView {
       void onSuccessGetKickList(List<Kick> kickList, PageInfoEarly pageInfoEarly);
        void onSuccessGetKickDetail(Kick kickDetail);
        void onSuccessKick(KickResult result);
        void onFailKick(String result);
        void onSucessGetShopDetailOnly(ShopDetailModel shopDetailModel);
    }
    interface Presenter extends BasePresenter {
        void getKickList(Map<String, Object> map);
        void getKickDetail(Map<String, Object> map);
        void kick(Map<String, Object> map);
        void getShopDetailOnly(Map<String, Object> map);
    }
}
