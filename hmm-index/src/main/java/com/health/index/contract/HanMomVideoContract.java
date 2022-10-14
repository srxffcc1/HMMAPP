package com.health.index.contract;

import com.healthy.library.model.VideoListModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.model.VideoCategory;

import java.util.List;
import java.util.Map;


public interface HanMomVideoContract {
    interface View extends BaseView {

        void onGetTabListSuccess(List<VideoCategory> result);

        void onGetVideoListSuccess(List<VideoListModel> result, PageInfoEarly pageInfoEarly);

        void onGetVideoDetailSuccess(VideoListModel result,int type);

        void onAddPraiseSuccess(String result, int type);

    }

    interface Presenter extends BasePresenter {

        void getTabList(Map<String, Object> map);

        void getVideoList(Map<String, Object> map);

        void getVideoDetail(Map<String, Object> map, int type);

        void addPlayVolume(Map<String, Object> map);

        void addPraise(Map<String, Object> map, int type);

        void getUserInfo(Map<String, Object> map);

    }
}
