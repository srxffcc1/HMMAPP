package com.health.index.contract;

import com.health.index.model.IndexVideo;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/04/04 10:38
 * @des
 */
public interface IndexVideoContract {
    interface View extends BaseView {
        void getVideoSuccess(List<IndexVideo> videolist);


    }

    interface Presenter extends BasePresenter {
        void getVideoList(Map<String,Object> map);

    }
}
