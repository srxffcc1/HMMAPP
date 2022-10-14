package com.health.faq.contract;

import com.health.faq.model.ExpertHomepageModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.List;

/**
 * @author Li
 * @date 2019/07/18 14:07
 * @des 专家主页
 */
public interface ExpertHomepageContract {
    interface View extends BaseView {

        /**
         * 获取专家主页信息
         *
         * @param list        主页信息
         * @param answerCount 回答数量
         * @param online      是否在线
         */
        void onGetExpertInfoSuccess(List<ExpertHomepageModel> list, int answerCount, boolean online);
    }

    interface Presenter extends BasePresenter {
        /**
         * 获取专家信息
         *
         * @param id 专家id
         */
        void getExpertInfo(String id);
    }
}
