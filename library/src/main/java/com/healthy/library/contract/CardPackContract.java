package com.healthy.library.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.CouponInfoZ;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/27 11:20
 * @des
 */

public interface CardPackContract {
    interface View extends BaseView {
        void onSucessGetCouponList(List<CouponInfoZ> adModels);
        void onSucessGetCoupon(CouponInfoZ adModels);
        void onSucessGetInsert(Boolean hasinsert);

    }


    interface Presenter extends BasePresenter {
        void checkInsert(Map<String, Object> map);
        void getCouponList(Map<String, Object> map);
        void getCouponDetail(Map<String, Object> map);
    }
}
