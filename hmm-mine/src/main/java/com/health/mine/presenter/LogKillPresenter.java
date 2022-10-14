package com.health.mine.presenter;

import android.content.Context;

import com.health.mine.contract.SettingsContract;
import com.healthy.library.constant.Functions;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Li
 * @date 2019/04/17 14:52
 * @des 退出登录
 */

public class LogKillPresenter implements SettingsContract.Presenter {
    private Context mContext;
    private SettingsContract.View mView;

    public LogKillPresenter(Context context, SettingsContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void logout() {
        mView.onLogoutSuccess();
//        Map<String, Object> map = new HashMap<>(1);
//        map.put(Functions.FUNCTION, "zx_1003");
//        ObservableHelper.createObservable(mContext, map)
//                .subscribe(new StringObserver(mView, mContext, false, false,false,false) {
//                    @Override
//                    protected void onGetResultSuccess(String obj) {
//                        super.onGetResultSuccess(obj);
//                        mView.onLogoutSuccess();
//                    }
//
//                    @Override
//                    protected void onFinish() {
//                        super.onFinish();
//                        mView.onRequestFinish();
//                    }
//
//                    @Override
//                    protected void onFailure() {
//                        super.onFailure();
//                        mView.onLogoutSuccess();
//                    }
//                });
    }

    @Override
    public void getUserInfo() {

    }
}
