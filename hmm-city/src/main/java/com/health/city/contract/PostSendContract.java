package com.health.city.contract;

import com.health.city.model.UserInfoCityModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.List;
import java.util.Map;

public interface PostSendContract {
    interface View extends BaseView {
        void onSuccessSendPost();
        void onFailSendPost();
        /**
         * 上传成功
         *
         * @param urls 图片地址
         */
        void onFailPost();
        void onUpLoadSuccess(List<String> urls,int type);
        void onUpLoadSuccess2(List<String> urls,int type);//视频缩略图第一帧
        void onMineSuccess(UserInfoCityModel userInfoModel);

    }
    interface Presenter extends BasePresenter {
        void sendPost(Map<String, Object> map);
        void uploadFile(List<String> base64List,int type);
        void uploadFile2(List<String> base64List,int type);//上传视频缩略图第一帧
        void getMine();
    }
}
