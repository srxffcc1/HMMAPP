package com.healthy.library.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.VideoResult;

import java.util.Map;

/**
 * @author Li
 * @date 2019/03/29 17:26
 * @des
 */

public interface GoodsVideoSContract {
    interface View extends BaseView {


        void getSucessGetvideoS(VideoResult videoResult);
        void getSucessGetvideoToken(String token);
    }

    interface Presenter extends BasePresenter {



        void getVideoS(Map<String, Object> map);
        void getVideoToken(Map<String, Object> map);

    }
}
