package com.health.mine.contract;

import com.health.mine.model.HanMomInfoModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/27 11:20
 * @des
 */

public interface HanMomContract {
    interface View extends BaseView {
        void onGetInfoSuccess(HanMomInfoModel.MemberInfoModel memberInfoModel,
                              List<HanMomInfoModel.StarListModel> starList,
                              List<HanMomInfoModel.ScrollListModel> scrollList,
                              HanMomInfoModel.SettingModel settingModel,
                              HanMomInfoModel.PartnerModel partnerModel,
                              HanMomInfoModel.LastPartnerModel lastPartnerModel,
                              List<HanMomInfoModel.RightsListModel> rights);

        void onGetBuyIdSuccess(String buyId);

        void getPayInfoSuccess(String payOrderId, String payType);

        void getAliPayUrlSuccess(String url);

        void getPayStatusSuccess(String status);

    }


    interface Presenter extends BasePresenter {
        void getInfo(Map<String, Object> map);

        void getBuyId(String partnerId);

        void getPayInfo(Map<String, Object> map);

        void getAliPayUrl(Map<String, Object> map);

        void getPayStatus(String payId);
    }
}
