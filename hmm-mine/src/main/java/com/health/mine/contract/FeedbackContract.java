package com.health.mine.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

/**
 * @author Li
 * @date 2019/05/10 14:34
 * @des
 */
public interface FeedbackContract {
    interface View extends BaseView {
        /**
         * 提交意见反馈成功
         */
        void onCommitFeedbackSuccess();
    }

    interface Presenter extends BasePresenter {
        /**
         * 提交意见反馈
         *
         * @param content 内容
         */
        void commitFeedback(String content);
    }
}
