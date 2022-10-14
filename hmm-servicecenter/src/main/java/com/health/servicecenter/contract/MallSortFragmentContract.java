package com.health.servicecenter.contract;

import com.healthy.library.model.CategoryChildModel;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/04/29 16:12
 * @des
 */
public interface MallSortFragmentContract {
    interface View extends BaseView {

        void onGetCategoryListSuccess(List<CategoryChildModel> list);
    }

    interface Presenter extends BasePresenter {


        void getCategoryList(Map<String, Object> map);//获取分类列表

    }
}
