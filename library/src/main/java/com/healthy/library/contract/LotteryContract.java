package com.healthy.library.contract;

import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.LotteryModel;
import com.healthy.library.model.UpdatePatchVersion;

import java.util.Map;

/**
 * @author Li
 * @date 2019/03/27 11:20
 * @des
 */

public interface LotteryContract {
    interface View extends BaseView {
        void onLotteryInfoSuccess(LotteryModel lotteryModel);

    }


    interface Presenter extends BasePresenter {
        void getLotteryInfo(Map<String,Object> map);
    }
}
