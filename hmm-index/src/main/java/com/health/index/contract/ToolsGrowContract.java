package com.health.index.contract;

import com.healthy.library.model.ToolsGrow;
import com.healthy.library.model.ToolsGrowBase;
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
public interface ToolsGrowContract {
    interface View extends BaseView {
        void onSucessGetAllStatus(List<UserInfoExModel> userInfoExModels);
        void onSucessGetNowGrow(List<ToolsGrow> toolsGrows);
        void onSucessGetNowBaseGrow(List<ToolsGrowBase> toolsGrowBases);
        void onSucessAddGrow();
        void onsucessGetMine(UserInfoMonModel userInfoMonModel);

    }

    interface Presenter extends BasePresenter {
        void getNowStatus();
        void getAllStatus();
        void updateGrow(Map<String, Object> map);
        void getNowGrow(Map<String, Object> map);
        void getNowBaseGrow(Map<String, Object> map);
    }
}
