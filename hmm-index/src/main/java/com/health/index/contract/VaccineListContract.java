package com.health.index.contract;

import com.health.index.model.VaccineModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.List;

/**
 * @author Li
 * @date 2019/04/23 13:33
 * @des 疫苗列表
 */
public interface VaccineListContract {

    interface View extends BaseView {
        /**
         * 获取疫苗列表成功
         *
         * @param vaccineModelList 疫苗列表
         * @param currentId 当前应该定位的id
         */
        void onGetVaccineListSuccess(List<VaccineModel> vaccineModelList,int currentId);
    }

    interface Presenter extends BasePresenter {
        /**
         * 获取疫苗列表
         */
        void getVaccineList();
    }
}
