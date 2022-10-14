package com.health.servicecenter.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.AppointmentListItemModel;
import com.healthy.library.model.PageInfoEarly;

import java.util.List;
import java.util.Map;

/**
 * @author: long
 * @date: 2021/4/1
 */
public interface AppointmentListContract {
    interface View extends BaseView {

        void onGetListSuccess(List<AppointmentListItemModel> modelList);

        void onGetDetailsSuccess(AppointmentListItemModel model);

        void onCleanSuccess();
    }

    interface Presenter extends BasePresenter {

        void getList(Map<String, Object> map);

        void getDetails(Map<String, Object> map);

        void cancleAppointment(Map<String, Object> map);

    }
}
