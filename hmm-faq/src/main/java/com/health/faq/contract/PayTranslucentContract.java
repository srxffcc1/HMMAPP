package com.health.faq.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.Map;

/**
 * @author Li
 * @date 2019/07/04 14:26
 * @des
 */
public interface PayTranslucentContract {

    interface View extends BaseView {
        /**
         * 检查余额是否充足成功
         *
         * @param balance    余额
         * @param canPay     true:当前余额可以支付
         * @param needSetPwd 是否需要设置密码
         */
        void onCheckBalanceSuccess(String balance, boolean canPay, boolean needSetPwd);

        /**
         * 检查余额失败
         */
        void onCheckBalanceFail();

        /**
         * 上传图片成功
         *
         * @param urls 图片url地址
         */
        void onUploadPictureSuccess(String[] urls);

        /**
         * 获取订单信息成功
         *
         * @param payType 支付类型 0：微信  1：支付宝  2：憨豆豆
         * @param aliInfo 支付宝支付信息
         * @param wxInfo  微信支付信息
         */
        void onGetOrderInfoSuccess(String payType, String aliInfo, Map<String, String> wxInfo);
    }

    interface Presenter extends BasePresenter {
        /**
         * 检查憨豆豆余额是否能够支付
         *
         * @param rewardMoney 悬赏金额
         */
        void checkBalanceEnough(String rewardMoney);

        /**
         * 上传图片
         *
         * @param base64 图片集合
         */
        void uploadPictures(String[] base64);

        /**
         * 提交悬赏问答
         *
         * @param money    支付金额
         * @param title    标题
         * @param content  内容
         * @param isHidden 是否匿名 1：不匿名 2：匿名
         * @param photo    图片数组
         * @param payType  0:wx  1:zfb 2:hdd
         */
        void submitReward(String money, String title, String content, String isHidden, String[] photo,
                          String payType);


        /**
         * 提高悬赏金额
         *
         * @param money      悬赏金额
         * @param questionId 问题id
         * @param payType    支付方式
         */
        void submitImproveReward(String money, String questionId, String payType);

        /**
         * 问专家
         *
         * @param id       专家id
         * @param money    金额
         * @param detail   描述
         * @param isHidden 是否匿名
         * @param photo    图片
         * @param payType  支付方式
         */
        void submitExpert(String id, String money, String detail, String isHidden, String[] photo,
                          String payType);

    }
}
