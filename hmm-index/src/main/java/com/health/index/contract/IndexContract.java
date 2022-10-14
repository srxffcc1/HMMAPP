//package com.health.index.contract;
//
//import com.health.index.model.IndexDataModel;
//import com.health.index.model.UserInfoModel;
//import com.healthy.library.base.BasePresenter;
//import com.healthy.library.base.BaseView;
//
//import java.util.Date;
//import java.util.List;
//
///**
// * @author Li
// * @date 2019/04/04 10:38
// * @des
// */
//public interface IndexContract {
//    interface View extends BaseView {
//        /**
//         * 获取首页数据成功
//         *
//         * @param dataModels    数据
//         * @param userInfoModel 用户信息
//         * @param date          查询的日期
//         */
//        void onGetDataSuccess(List<IndexDataModel> dataModels, UserInfoModel userInfoModel,
//                              Date date);
//
//        /**
//         * 获取首页数据请求结束
//         */
//        void onGetDataFinish();
//
//
//    }
//
//    interface Presenter extends BasePresenter {
//        /**
//         * 获取首页详情
//         *
//         * @param queryDate 查询日期 “2019-04-04”
//         */
//        void getIndexInfo(Date queryDate);
//    }
//}
