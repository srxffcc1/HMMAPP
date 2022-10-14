package com.health.tencentlive.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.AnchormanInfo;
import com.healthy.library.model.CouponInfoZ;
import com.healthy.library.model.GoodsDetail;
import com.healthy.library.model.LiveVideoMain;

import java.util.HashMap;

/**
 * @author Li
 * @date 2019/03/27 11:20
 * @des
 */

public interface LiveGiftContract {

    interface View extends BaseView {

        void getSuccessGoodsInfo(GoodsDetail result);

        void getSuccessCouponInfo(CouponInfoZ result);

    }


    interface Presenter extends BasePresenter {

        void getGoodsInfo(HashMap<String, Object> map);

        void getCouponInfo(HashMap<String, Object> map);

    }
}
