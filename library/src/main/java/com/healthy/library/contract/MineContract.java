package com.healthy.library.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.CouponInfo;
import com.healthy.library.model.MineFans;
import com.healthy.library.model.OrderInfo;
import com.healthy.library.model.OrderNum;
import com.healthy.library.model.UserInfoModel;
import com.healthy.library.model.VipCard;
import com.healthy.library.model.VipInfo;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/04/29 10:26
 * @des 我的
 */

public interface MineContract {
    interface View extends BaseView {
        /**
         * 获取到用户信息
         *
         * @param userInfoModel 用户信息
         */
        void onGetUserInfoSuccess(UserInfoModel userInfoModel);

        void onGetOrderInfoSuccess(OrderInfo orderInfo);

        void onGetUserFanSucess(MineFans mineFans);

        void onGetCouponSucess(CouponInfo mineFans);

        void onGetOrderNumSuccess(OrderNum orderNum);

        void onVipInfoSuccess(VipInfo vipInfo);
        void onVipCardSuccess(List<VipCard> vipCards);
    }

    interface Presenter extends BasePresenter {

        /**
         * 获取用户信息
         */
        void getUserFans(Map<String, Object> map);

        /**
         * 获取用户信息
         */
        void getUserInfo();

        /**
         * 获取用户信息
         */
        void getOrderInfo();

        /**
         * 获取用户信息
         */
        void getCouponInfo();
        void getVipInfo(Map<String, Object> map);
        void getVipCards(Map<String, Object> map);

        void getOrderNum();//获取订单信息
    }
}
