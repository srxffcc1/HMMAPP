package com.health.mall.contract;

import com.healthy.library.model.MallCoupon;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/29 17:26
 * @des
 */

public interface AppCouponContract {
    interface View extends BaseView {
        void onSucessGetAppCouponList(List<MallCoupon> mallCouponNoList,List<MallCoupon> mallCouponYesList);
        void onSucessReAppCoupon();
        void onFailReAppCoupon();
    }

    interface Presenter extends BasePresenter {
        void getAppCouponList(Map<String,Object> map);
        void reAppCoupon(Map<String,Object> map);
    }
}
