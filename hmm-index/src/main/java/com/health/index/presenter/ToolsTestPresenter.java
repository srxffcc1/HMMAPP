package com.health.index.presenter;

import android.app.Activity;

import com.health.index.contract.ToolsTestContract;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.utils.HttpUrlConnectUtil;

import java.util.Map;

/**
 * @author Li
 * @date 2019/04/04 10:41
 * @des
 */
public class ToolsTestPresenter implements ToolsTestContract.Presenter {

    private Activity mContext;
    private ToolsTestContract.View mView;

    public ToolsTestPresenter(Activity context, ToolsTestContract.View view) {
        mContext = context;
        mView = view;
    }


    @Override
    public void getBaseWithHMM(Map<String, Object> map) {
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSucessGetBaseHMM(obj);
                    }
                });
    }

    @Override
    public void updateBaseWithHMM(Map<String, Object> map) {

    }

    @Override
    public void getBaseWithAL(final String url,final Map<String, Object> map,final Map<String, Object> mapHead) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final String result=HttpUrlConnectUtil.doGet(url,map,mapHead);
                    mContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mView.onSucessGetAL(result);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}