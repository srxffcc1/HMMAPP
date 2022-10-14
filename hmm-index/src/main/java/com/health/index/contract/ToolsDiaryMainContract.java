package com.health.index.contract;

import com.health.index.model.UserInfoExModel;
import com.healthy.library.model.UserInfoMonModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/04/04 10:38
 * @des
 */
public interface ToolsDiaryMainContract {
    interface View extends BaseView {
        void onSucessGetAllStatus(List<UserInfoExModel> userInfoExModels);
        void onSucessGetNowDiary(String result);
        void onSucessGetNowSleepDiary(String result);
        void onSucessUpdateDiary();
        void onsucessGetMine(UserInfoMonModel userInfoMonModel);

    }

    interface Presenter extends BasePresenter {
        void getNowStatus();
        void getAllStatus();
        void updateDiary(Map<String, Object> map);
        void getNowDiary(Map<String, Object> map);
        void getNowSleep(Map<String, Object> map);
    }
}
