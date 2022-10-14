package com.health.index.contract;

import com.health.index.model.IndexVideoOnline;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.Map;

/**
 * @author Li
 * @date 2019/04/04 10:38
 * @des
 */
public interface IndexVideoOnlineDetailContract {
    interface View extends BaseView {
        void getVideoSuccess(IndexVideoOnline videoOnline);


    }

    interface Presenter extends BasePresenter {
        void getVideo(Map<String, Object> map);
        void openVideo(Map<String, Object> map);

    }
}
