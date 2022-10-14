package com.health.discount.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.NewUserListModel;

import java.util.List;
import java.util.Map;

public interface NewUserListContract {
    interface View extends BaseView {
        void onSucessGetList(List<NewUserListModel> result);

        void onSucessIsNewAppMember(int result);

        void onSuccessAddRemind(String result,String type);

    }

    interface Presenter extends BasePresenter {
        void getList(Map<String, Object> map);

        void getIsNewAppMember();

        void addRemind(Map<String, Object> map,String type);
    }
}
