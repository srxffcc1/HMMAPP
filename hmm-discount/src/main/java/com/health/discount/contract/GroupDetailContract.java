package com.health.discount.contract;

import com.healthy.library.model.Goods2DetailPin;
import com.healthy.library.model.KKGroupDetail;
import com.healthy.library.model.KKGroupSimple;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.List;
import java.util.Map;

public interface GroupDetailContract {
    interface View extends BaseView {
        void onSuccessGetGroupDetail(KKGroupDetail result);
        void successGetGoodsDetail(Goods2DetailPin goodsDetail);
        void onSuccessGetGroupAlreadyList(List<KKGroupSimple> kkGroupSimples);
    }
    interface Presenter extends BasePresenter {
        void getGroupDetail(Map<String, Object> map);
        void getGroupAlreadyList(Map<String, Object> map);
        void getGoodsDetail(Map<String, Object> map);
    }
}
