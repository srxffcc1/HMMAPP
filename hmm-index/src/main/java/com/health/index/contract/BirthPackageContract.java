package com.health.index.contract;

import com.health.index.model.BirthPackageModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.List;

/**
 * @author Li
 * @date 2019/04/24 16:42
 * @des
 */
public interface BirthPackageContract {
    interface View extends BaseView {
        /**
         * 获取待产包成功
         *
         * @param list 待产包列表
         */
        void onGetBirthPackageSuccess(List<BirthPackageModel> list);

        /**
         * 修改待产包成功
         *
         * @param pos    源位置 用来更新列表数据位置
         * @param status 修改前的状态
         */
        void onChangePackageSuccess(int pos, int status);
    }

    interface Presenter extends BasePresenter {
        /**
         * 获取待产包
         *
         * @param type 0：妈妈的待产包  1：宝宝的待产包
         */
        void getBirthPackage(int type);

        /**
         * 修改待产包状态
         *
         * @param key    key
         * @param id     id
         * @param pos    列表位置
         * @param status 修改时的状态
         */
        void changePackage(String key, int id, int pos, int status);
    }
}
