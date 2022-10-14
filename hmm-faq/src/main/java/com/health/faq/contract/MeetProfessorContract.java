package com.health.faq.contract;

import com.healthy.library.model.Faq;
import com.healthy.library.model.FaqHotExpertFieldChose;
import com.healthy.library.model.FaqVideo;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/07/22 14:43
 * @des
 */

public interface MeetProfessorContract {
    interface View extends BaseView {

        void onGetFieldList(List<FaqHotExpertFieldChose> faqHotExpertFieldChoses);


        void onGetHotFieldList(List<FaqHotExpertFieldChose> faqHotExpertFieldChoses);
        /**
         * 获取专家信息
         *
         * @param expertInfoModel 专家信息
         */
        void onGetHomeSuccess(Faq expertInfoModel);
        void onGetVideoSuccess(List<FaqVideo> videolist);
        void subVideoSucess();

    }

    interface Presenter extends BasePresenter {
        /**
         * 获取专家详情
         *
         */
        void getHome();

        void getType();

        void getHotType();

        void getVideoOnline();



        void subVideo(Map<String,Object> map);
    }
}
