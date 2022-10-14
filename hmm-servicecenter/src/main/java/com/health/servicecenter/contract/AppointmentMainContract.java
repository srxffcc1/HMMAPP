package com.health.servicecenter.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.AppointmentListItemModel;
import com.healthy.library.model.AppointmentMainItemModel;
import com.healthy.library.model.AppointmentTimeSettingModel;
import com.healthy.library.model.ShopDetailModel;

import java.util.List;
import java.util.Map;

/**
 * @author long
 * @date 2021/03/30 11:02
 * @des
 */
public interface AppointmentMainContract {

    interface View extends BaseView {

        void onGetMainListSuccess(List<AppointmentMainItemModel> modelList);

        void onGetStoreDetailSuccess(ShopDetailModel shopDetailModel);

        void onGetMainDetailSuccess(AppointmentMainItemModel detailModel);

        void onGetTimeSettingSuccess(AppointmentTimeSettingModel timeSettingModel);

        void onGetPayInfoSuccess(Map<String, Object> payInfoMap);

        void onAddServiceSuccess(long id,String payOrderId);

        void getPayStatusSuccess(String status);

    }

    interface Presenter extends BasePresenter {

        void getMainList(Map<String, Object> map);

        void getStoreDetail(Map<String,Object> map);

        void getMainDetail(Map<String, Object> map);

        void addService(Map<String, Object> map);

        void getSettingTimeList(Map<String, Object> map);

        void getPayInfo(Map<String, Object> map);

        void getPayStatus(String payId);

    }
}
