package com.health.index.contract;

import com.health.index.model.AnalyzeModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.List;

/**
 * @author Li
 * @date 2019/04/25 15:07
 * @des
 */
public interface AnalyzeContract {
    interface View extends BaseView {
        /**
         * 获取当前id
         *
         * @param weekId 周id（从1-40）
         * @param list   常见项目集合
         */
        /**
         * 获取成功
         *
         * @param projectList  常见项目结合
         * @param analysisList 分析的项目
         * @param weekId       周id（从1-40）
         * @param status       非空：解读有内容  空：解读无内容
         */
        void onGetAnalysisProjectsSuccess(List<AnalyzeModel> projectList,
                                          List<AnalyzeModel> analysisList,
                                          String weekId, String status);

    }

    interface Presenter extends BasePresenter {
        /**
         * 获取B超信息
         *
         * @param weekId 周数
         */
        void getAnalysis(String weekId);
    }
}
