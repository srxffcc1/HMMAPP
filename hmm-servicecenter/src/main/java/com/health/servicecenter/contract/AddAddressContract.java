package com.health.servicecenter.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.Map;

/**
 * @author Li
 * @date 2019/04/29 16:12
 * @des
 */
public interface AddAddressContract {
    interface View extends BaseView {
        void onPutAddressSuccess();

        void onDeleteAddressSuccess();
    }

    interface Presenter extends BasePresenter {
        void putAddress(Map<String, Object> map);//新增/编辑地址

        void deleteAddress(String id);
    }
}
