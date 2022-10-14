package com.health.servicecenter.contract;

import com.healthy.library.model.AddressListModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/04/29 16:12
 * @des
 */
public interface AddressListContract {
    interface View extends BaseView {
        void onGetAddressListSuccess(List<AddressListModel> listModels);
        void onDeleteAddressSuccess();
    }

    interface Presenter extends BasePresenter {
        void getAddressList(Map<String, Object> map);//获取收货地址列表

        void deleteAddress(String id);
    }
}
