package com.healthy.library.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.Map;

public interface SeckShareDialogContract {
    interface View extends BaseView {
        void onGetUserInfoSuccess(String faceUrl, String nickName);
        //void getZxingBitmap(String result);

        /**
         * 导购信息
         *
         * @param result   导购码
         * @param personId 工号
         */
        void getZxingCode(String result, String personId);

        /**
         * 门店详情回调
         *
         * @param shopLogo            品牌Logo
         * @param partnerMerchantLogo 异业商家情况为合伙人logo
         */
        void onGetStoreDetialSuccess(String shopLogo, String partnerMerchantLogo);

        /**
         * 小程序二维码图片的base64编码
         *
         * @param data
         */
        void onGetBase64DataSuccess(String data);

        void onGet32DataSuccess(String data);
    }

    interface Presenter extends BasePresenter {
        /**
         * 获取门店详情 (获取品牌Logo)
         *
         * @param shopId
         */
        void getStoreDetial(String shopId);

        /**
         * 缓存小程序参数
         *
         * @param map
         */
        void setProgram(Map<String, Object> map);

        void setAppProgram(Map<String, Object> map);

        void getUserInfo();

        void getZxingCode();

        void addLookLivePeopleNum(Map<String, Object> map);//添加im群组会员统计信息
        //void getZxingBitmap(String referralCode);
    }
}
