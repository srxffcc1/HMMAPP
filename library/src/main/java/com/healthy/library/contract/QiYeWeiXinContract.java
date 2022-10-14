package com.healthy.library.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.QiYeWeXin;
import com.healthy.library.model.QiYeWeXinKey;
import com.healthy.library.model.QiYeWeXinWorkShop;
import com.healthy.library.model.TokerWorkerInfoModel;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/27 11:20
 * @des
 */

public interface QiYeWeiXinContract {
    interface View extends BaseView {
        void onSucessGetWeiXinKey(QiYeWeXinKey qiYeWeXinKey);
        void onSucessGetRecommandWeiXinGroup(List<QiYeWeXin> qiYeWeXins);
        void onSucessGetRecommandWorkerGroup(List<QiYeWeXinWorkShop> qiYeWeXins);
        void onSucessGetMineWorker(TokerWorkerInfoModel tokerWorkerInfoModel);
        void onSucessGetPublicWorker(TokerWorkerInfoModel tokerWorkerInfoModel);
    }


    interface Presenter extends BasePresenter {
        /**
         * //获得开关配置
         * @param map
         * @param destination
         */
        void getWeiXinKey(Map<String, Object> map,String destination);

        /**
         * //获得推荐群
         * @param map
         */
        void getRecommandWeiXinGroup(Map<String, Object> map);

        /**
         * //获得推荐导购列表
         * @param map
         */
        void getRecommandWorkerList(Map<String, Object> map);

        /**
         * //获得我的导购
         * @param map
         */
        void getMineWorker(Map<String, Object> map);
        /**
         * //获得公告账号
         * @param map
         */
        void getPublicWorker(Map<String, Object> map);

    }
}
