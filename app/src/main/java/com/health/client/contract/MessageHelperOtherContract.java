package com.health.client.contract;

import com.health.client.model.MonMessageOtherHelper;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.PageInfoEarly;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/27 11:20
 * @des
 */

public interface MessageHelperOtherContract {
    interface View extends BaseView {
        void onSuccessGetMessageList(List<MonMessageOtherHelper> results, PageInfoEarly pageInfoEarly);
    }

    interface Presenter extends BasePresenter {
        void getMessage(Map<String, Object> map);
        void getMessageDis(Map<String, Object> map);
    }
}
