package com.health.index.contract;

import com.health.index.model.IndexBean;
import com.health.index.model.UserInfoExModel;
import com.healthy.library.model.TongLianMemberData;
import com.healthy.library.model.UserInfoMonModel;
import com.healthy.library.model.AppIndexCustom;
import com.healthy.library.model.AppIndexCustomNews;
import com.healthy.library.model.AppIndexCustomOther;
import com.healthy.library.model.FaqExportQuestion;
import com.healthy.library.model.VideoCategory;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.ActGoodsItem;
import com.healthy.library.model.LiveVideoMain;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/04/04 10:38
 * @des
 */
public interface IndexMainContract {
    interface View extends BaseView {
        void getIndexSuccess(IndexBean indexBean);

        void onSuccessGetVideoOnlineList(List<LiveVideoMain> liveVideoMains, boolean isMore); //9133

        void onSuccessGetGoodsHotList(List<ActGoodsItem> result, int firstPageSize);//9119

        //--------------------------------------------------------------------------
        void getAllStatusSuccess(List<UserInfoExModel> userInfoExModels);

        void changeStatusSuccess();

        void getMineSuccess(UserInfoMonModel userInfoMonModel);

        void onSuccessTongLianPhoneStatus(TongLianMemberData tongLianMemberData);
        /*** ------------------- tab -》 经验回调 START -------------------*/
        void onGetTabListSuccess(List<VideoCategory> result);
        /*** ------------------- tab -》 经验回调 START -------------------*/
        void onSuccessGetAPPIndexCustom(AppIndexCustom appIndexCustom);
        void onSuccessGetAPPIndexCustomWithOther(AppIndexCustomOther appIndexCustom);
        void onSuccessGetAPPIndexCustomNews(AppIndexCustomNews result);
        void onSuccessGetQuestionList(List<FaqExportQuestion> result);//5071
    }

    interface Presenter extends BasePresenter {
        void getIndexMain(Map<String, Object> map);// 4002

        void getVideoOnline(Map<String, Object> map);// 9133

        //        void getH5();//4033
        void getGoodsHot(Map<String, Object> map);// 9119

        //--------------------------------------------------------------------------
        void changeStatus(String id);

        void getAllStatus();

        void getMine();

        void getQuestionList(Map<String,Object> map);// 5071

        void getTongLianPhoneStatus(Map<String,Object> map);// allin_10001

        /*** ------------------- tab -》 经验 START -------------------*/
        void getVideoTypeTabList(Map<String, Object> map); // 8095
        /*** ------------------- tab -》 经验 END-------------------*/
        void getAPPIndexCustom(Map<String, Object> map); // app_index_1000 //服务体验
        void getAPPIndexCustomOther(Map<String, Object> map); // home_page_setting_1000  金刚区、小工具、推荐位、底部导航
        void getAPPIndexCustomNews(Map<String, Object> map); // homePageHeadline_10001 憨妈头条
    }
}
