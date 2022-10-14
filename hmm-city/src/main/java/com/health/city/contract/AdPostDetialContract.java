package com.health.city.contract;

import com.healthy.library.model.PostImgDetial;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.Map;

public interface AdPostDetialContract {
    interface View extends BaseView {

        void onSuccessGetDetial(PostImgDetial postImgDetial);
    }

    interface Presenter extends BasePresenter {
        void getImgDetial(Map<String, Object> map,PostImgDetial postImgDetial);
    }
}
