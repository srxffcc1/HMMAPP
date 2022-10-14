package com.healthy.library.presenter;

import android.content.Context;

import com.healthy.library.contract.LivePasswordContract;
import com.healthy.library.constant.Functions;
import com.healthy.library.net.NoStringObserver;
import com.healthy.library.net.ObservableHelper;

import java.util.Map;

/**
 * @author Li
 * @date 2019/03/27 11:37
 * @des
 */

public class LivePasswordPresenter implements LivePasswordContract.Presenter {

    private Context mContext;
    private LivePasswordContract.View mView;

    public LivePasswordPresenter(Context context, LivePasswordContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void checkPassword(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "9139");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
//                        JSONObject jsonObject= null;
//                        try {
//                            jsonObject = new JSONObject(obj);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                        mView.onSucessGetCheckResult(true);
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onSucessGetCheckResult(false);

                    }
                });
    }
}