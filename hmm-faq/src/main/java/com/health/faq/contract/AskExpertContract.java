package com.health.faq.contract;

import com.health.faq.model.ExpertInfoModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

/**
 * @author Li
 * @date 2019/07/22 14:43
 * @des
 */

public interface AskExpertContract {
    interface View extends BaseView {

        /**
         * 获取专家信息
         *
         * @param expertInfoModel 专家信息
         */
        void onGetExpertInfoSuccess(ExpertInfoModel expertInfoModel);
    }

    interface Presenter extends BasePresenter {
        /**
         * 获取专家详情
         *
         * @param id 专家id
         */
        void getExpertInfo(String id);
    }
}
