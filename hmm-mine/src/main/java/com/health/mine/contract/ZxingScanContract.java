package com.health.mine.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.TokerWorkerInfoModel;
import com.healthy.library.model.ZxingReferralCodeModel;

/**
 * @author Li
 * @date 2019/03/27 11:20
 * @des
 */

public interface ZxingScanContract {
    interface View extends BaseView {
        void onGetCodeInfoSuccess(ZxingReferralCodeModel model);

        void onBindingSuccess(String result);

        void onGetTokerWorkerInfoSuccess(TokerWorkerInfoModel model);

        void ticketCoupon();
    }

    interface Presenter extends BasePresenter {
        void getCodeInfo(String referralCode);

        void binding(String workerId, String merchantId, String isDownload, String bindType);

        void getTokerWorkerInfo();

        void ticketCoupon(String couponId);
    }
}
