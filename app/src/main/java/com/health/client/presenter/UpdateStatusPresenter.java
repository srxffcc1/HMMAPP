package com.health.client.presenter;

import android.content.Context;

import com.health.client.contract.UpdateStatusContract;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.utils.SpUtils;

import java.util.Map;

/**
 * @author Li
 * @date 2019/03/28 11:24
 * @des 更改用户状态
 */

public class UpdateStatusPresenter implements UpdateStatusContract.Presenter {


    private Context mContext;
    private UpdateStatusContract.View mView;

    public UpdateStatusPresenter(Context context, UpdateStatusContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void updateStatus(final Map<String, Object> map) {
        map.put(Functions.FUNCTION, Functions.FUNCTION_CHANGE_STATUS_NEW);
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false,
                        true, true, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        SpUtils.store(mContext,
                                SpKey.STATUS, String.valueOf(map.get("stageStatus")));
                        mView.onUpdateSuccess();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onUpdateFail();
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }

                });
    }

    @Override
    public void updateStatusEx(final Map<String, Object> map) {
        map.put(Functions.FUNCTION, "1014");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false,
                        true, true, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onUpdateSuccessEx();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onUpdateFail();
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }

                });
    }
}
