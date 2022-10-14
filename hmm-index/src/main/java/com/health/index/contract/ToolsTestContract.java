package com.health.index.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.Map;

/**
 * @author Li
 * @date 2019/04/04 10:38
 * @des
 */
public interface ToolsTestContract {
    interface View extends BaseView {
        void onSucessGetBaseHMM(String result);
        void onSucessUpdateBaseHMM(String result);
        void onSucessGetAL(String result);


    }

    interface Presenter extends BasePresenter {
        void getBaseWithHMM(Map<String,Object> map);
        void updateBaseWithHMM(Map<String,Object> map);
        void getBaseWithAL(String url,Map<String, Object> map,Map<String, Object> mapHead);
    }
}
