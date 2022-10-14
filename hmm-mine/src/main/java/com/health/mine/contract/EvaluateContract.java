package com.health.mine.contract;

import com.health.mine.model.UserInfoMineModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/04/13 15:47
 * @des
 */

public interface EvaluateContract {
    interface View extends BaseView {
        /**
         * 上传成功
         *
         * @param urls 图片地址
         */
        void onUpLoadSuccess(List<String> urls,int type);

        /**
         * 评论成功
         */
        void ongetMineFail();
        void onEvaluateSuccess();
        void onMineSuccess(UserInfoMineModel userInfoModel);
    }

    interface Presenter extends BasePresenter {
        /**
         * 上传图片
         *
         * @param base64List 图片集合
         */
        void uploadFile(List<String> base64List,int type);

        /**
         * 评论
         *
         * @param map 参数
         */
        void evaluate(Map<String, Object> map);
        void getMine();

    }
}
