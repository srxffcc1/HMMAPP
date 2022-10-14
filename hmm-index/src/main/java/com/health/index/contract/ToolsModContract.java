package com.health.index.contract;

import com.healthy.library.model.UserInfoMonModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.Map;

/**
 * @author Li
 * @date 2019/04/28 15:08
 * @des
 */
public interface ToolsModContract {
    interface View extends BaseView {

        void onSucessgetType(String result);
        void onSucessgetTop(String result);
        void onSucessgetMine(String result);
        void onSucessgetCenter(String result);
        void onSucessgetBottom(String result);
        void onsucessGetMine(UserInfoMonModel userInfoMonModel);
    }

    interface Presenter extends BasePresenter {

        void getNowStatus();
        void getType(Map<String,Object> map);
        void getTop(Map<String,Object> map);
        void getMine(Map<String,Object> map);
        void getCenter(Map<String,Object> map);
        void getBottom(Map<String,Object> map);
    }
}
