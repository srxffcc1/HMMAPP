package com.health.second.contract;

import com.health.second.model.SecondAct;
import com.health.second.model.SecondType;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/04/29 16:12
 * @des
 */
public interface SecondMainContract {
    interface View extends BaseView {

        void onGetHotShopLogosSucess(List<String> list);
        void onActGoodsSucess(List<SecondAct> list);

    }

    interface Presenter extends BasePresenter {

        void getActGoods(Map<String, Object> map);
        void getHotShopLogos(Map<String, Object> map);
    }
}
