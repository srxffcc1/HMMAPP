package com.healthy.library.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.Map;

/**
 * 创建日期：2021/11/19 15:36
 *
 * @author LiuWei
 * @version 1.0
 * 包名： com.healthy.library.contract
 * 类说明：同城圈数据统计（包括浏览跟分享...）
 */

public interface DataStatisticsContract {
    interface View extends BaseView {
//        void onSetDataSuccess(String result);
    }


    interface Presenter extends BasePresenter {
        void shareAndLook(Map<String, Object> map);

        void bannerClickNum(Map<String, Object> map);
    }
}
