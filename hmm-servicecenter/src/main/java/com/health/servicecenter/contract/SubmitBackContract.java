package com.health.servicecenter.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/04/29 16:12
 * @des
 */
public interface SubmitBackContract {
    interface View extends BaseView {

        void onAddBackSuccess(String result,String refundId);

        void onUpLoadSuccess(List<String> urls, int type);

        void onFailPost();
    }

    interface Presenter extends BasePresenter {


        void addBack(Map<String, Object> map);//提交售后

        void uploadFile(List<String> base64List, int type);

    }
}
