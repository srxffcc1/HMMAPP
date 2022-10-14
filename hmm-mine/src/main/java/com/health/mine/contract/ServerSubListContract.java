package com.health.mine.contract;

import com.health.mine.model.ServerOrderListModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.PageInfoEarly;

import java.util.List;
import java.util.Map;

public interface ServerSubListContract {
    interface View extends BaseView {
        void onSucessGetSubList(List<ServerOrderListModel> orderSubDetailModels, PageInfoEarly pageInfo);
        void onSucessSubUpdate();
    }

    interface Presenter extends BasePresenter {
        void getOrderSubList(Map<String, Object> map);
        void subUpdate(Map<String, Object> map);
    }
}