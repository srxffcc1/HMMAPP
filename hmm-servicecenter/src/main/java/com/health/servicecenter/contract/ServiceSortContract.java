package com.health.servicecenter.contract;

import com.healthy.library.model.CategoryListModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.List;

/**
 * @author Li
 * @date 2019/04/29 16:12
 * @des
 */
public interface ServiceSortContract {
    interface View extends BaseView {

        void onGetCategoryListSuccess(List<CategoryListModel> list);
    }

    interface Presenter extends BasePresenter {


        void getCategoryList();//获取分类列表

    }
}
