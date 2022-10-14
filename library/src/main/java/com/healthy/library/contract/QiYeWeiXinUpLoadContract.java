package com.healthy.library.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.List;

/**
 * @author Li
 * @date 2019/03/27 11:20
 * @des
 */

public interface QiYeWeiXinUpLoadContract {
    interface View extends BaseView {
        void onSucessUpload();

        void onUpLoadSuccess(List<String> urls, int type);

        void onFailPost();
    }


    interface Presenter extends BasePresenter {
        void uploadQiWeiXin(String base64Code,String tokerWorkerId);//上传企业微信二维码信息

        void uploadFile(List<String> base64List, int type);
    }
}
