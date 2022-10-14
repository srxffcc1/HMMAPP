package com.healthy.library.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.TongLianMemberData;

import java.util.Map;

/**
 * @author Li
 * @date 2019/03/27 11:20
 * @des
 */

public interface TongLianPhoneContract {
    interface View extends BaseView {
        void onSucessSendCode(String result);
        void onSucessBindPhone(String result);
        void onSuccessTongLianPhoneStatus(TongLianMemberData tongLianMemberData);

    }


    interface Presenter extends BasePresenter {
        void sendCode(Map<String, Object> map);
        void bindPhone(Map<String, Object> map);
        void getTongLianPhoneStatus(Map<String,Object> map);// allin_10001
    }
}
