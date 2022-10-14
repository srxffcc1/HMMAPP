package com.health.mine.contract

import com.healthy.library.base.BasePresenter
import com.healthy.library.base.BaseView
import com.healthy.library.model.LotteryModel

/**
 * @author long
 * @description
 * @date 2021/8/2
 */
interface LotteryListContract {

    interface View : BaseView {

        /**
         * 取获奖记录列表回调
         * @param resultData 接口回调数据
         * @param isMore 是否还有更多
         */
        fun getLotteryListSuccess(resultData: MutableList<LotteryModel>, isMore: Boolean);

    }

    interface Presenter : BasePresenter {
        /**
         * 获取获奖记录列表数据
         * @param map 接口所需参数
         */
        fun getLotteryList(map: MutableMap<String, Any>)
    }

}