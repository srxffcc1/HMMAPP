package com.health.mine.contract;

import com.health.mine.model.CollectionModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.List;

/**
 * @author Li
 * @date 2019/04/29 17:23
 * @des
 */
public interface CollectionsContract {
    interface View extends BaseView {
        /**
         * 获取收藏列表成功
         *
         * @param list 列表
         */
        void onGetCollectionsSuccess(List<CollectionModel> list);
    }

    interface Presenter extends BasePresenter {
        /**
         * 获取收藏列表
         */
        void getCollections();
    }
}
