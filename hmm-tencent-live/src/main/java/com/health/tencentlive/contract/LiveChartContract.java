package com.health.tencentlive.contract;

import com.health.tencentlive.model.LineChartModel;
import com.health.tencentlive.model.RoundChartModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.HashMap;

/**
 * @author Li
 * @date 2019/03/27 11:20
 * @des
 */

public interface LiveChartContract {

    interface View extends BaseView {

        void onSuccessGetLiveData(RoundChartModel result);

        void onSuccessGetYearData(LineChartModel model);
    }


    interface Presenter extends BasePresenter {

        void getLiveData(HashMap<String, Object> map);//获取直播数据统计

        void getYearData(HashMap<String, Object> map);//获取年度数据

    }
}
