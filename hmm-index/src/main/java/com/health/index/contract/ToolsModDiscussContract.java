package com.health.index.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.Map;

/**
 * @author Li
 * @date 2019/04/28 15:08
 * @des
 */
public interface ToolsModDiscussContract {
    interface View extends BaseView {

        void onSucessgetList(String result);
        void onSucessReView(String result);
        void onSucessReDiscuss(String result);
        void onSucessLike(String result);
        void onSucessGetMine(String result);
        void onSucessSee(String result);
        void onSucessGetDetail(String result);
    }

    interface Presenter extends BasePresenter {

        void see(Map<String, Object> map);
        void getMine(Map<String, Object> map);
        void getList(Map<String, Object> map);
        void reView(Map<String, Object> map);
        void reDiscuss(Map<String, Object> map);
        void like(Map<String, Object> map);
        void detail(Map<String, Object> map);
    }
}
