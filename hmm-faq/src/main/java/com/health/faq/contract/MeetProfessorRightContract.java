package com.health.faq.contract;

import com.healthy.library.model.FaqExportQuestion;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.PageInfoEarly;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/07/22 14:43
 * @des
 */

public interface MeetProfessorRightContract {
    interface View extends BaseView {

        /**
         */
        void onGetQuestionSuccess(List<FaqExportQuestion> faqHotQuestionList, PageInfoEarly pageInfoEarly);
    }

    interface Presenter extends BasePresenter {
        /**
         * 获取专家详情
         */
        void getQuestionList(int page, Map<String, Object> map);
    }
}
