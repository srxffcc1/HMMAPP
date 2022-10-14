package com.health.mine.contract;

import com.health.mine.model.JobType;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.InviteAct;
import com.healthy.library.model.InviteJoinInfo;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/27 11:20
 * @des
 */

public interface InviteContract {
    interface View extends BaseView {

        void onSuccessSubMerchantInvite(String result);
        void onSuccessGetMerchantInvite(InviteAct inviteAct);
        void onSuccessGetMerchantInviteDetailJoinList(List<InviteJoinInfo> inviteJoinInfos);
    }


    interface Presenter extends BasePresenter {
        void getMerchantInvite(Map<String, Object> map);
        void getMerchantInviteDetailJoinList(Map<String, Object> map);
        void subMerchantInvite(Map<String, Object> map);
    }
}
