package com.health.mine.contract;

import com.health.mine.model.JobType;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/27 11:20
 * @des
 */

public interface JobContract {
    interface View extends BaseView {
        void onSuccessGetJobType(List<JobType> results);
        void onSuccessSendCode();
        void onGetCodeFail();
        void onSuccessAdd();
        void onUpLoadCretSuccess(List<String> urls,int type);
        void onUpLoadHealthSuccess(List<String> urls,int type);
    }


    interface Presenter extends BasePresenter {
        void addJobDetail(Map<String,Object> map);
        void sendJobCode(Map<String,Object> map);
        void getJobType(Map<String,Object> map);
        void uploadCretFile(List<String> base64List, final int type);
        void uploadHealthFile(List<String> base64List, final int type);
    }
}
