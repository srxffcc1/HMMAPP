package com.healthy.library.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.AdModel;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/27 11:20
 * @des
 */

public interface LocContract {
    interface View extends BaseView {
        void onSucessGetLocList(List<AdModel> adModels);
    }


    interface Presenter extends BasePresenter {

        void getLocList(Map<String, Object> map);
    }
}
