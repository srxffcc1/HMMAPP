package com.health.faq.contract;

import com.healthy.library.model.FaqExport;
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

public interface MeetProfessorLeftContract {
    interface View extends BaseView {

        /**
         * 获取专家信息
         */
        void onGetExpertSuccess(List<FaqExport> exportList, PageInfoEarly pageInfoEarly);
    }

    interface Presenter extends BasePresenter {

        void getExpertList(int page, Map<String, Object> map);
    }
}
