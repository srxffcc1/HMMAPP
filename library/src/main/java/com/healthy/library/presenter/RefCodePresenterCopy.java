package com.healthy.library.presenter;

import android.content.Context;

import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.constant.Functions;
import com.healthy.library.net.NoStringObserver;
import com.healthy.library.net.ObservableHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/27 11:37
 * @des
 */

public class RefCodePresenterCopy {

    private Context mContext;

    public RefCodePresenterCopy(Context context) {
        mContext = context;
    }

    public void posRefCode(String referral_code) {
        Map<String,Object> map=new HashMap<>();
        map.put("function", "toker_9307");
        map.put("shareReferralCode",referral_code);
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(null, mContext,
                        false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                    }
                });
    }
    public void posAd(String id) {

    }
    public void binding(String workerId, final String merchantId,String isDownload) {
        Map<String, Object> map = new HashMap<>();
        map.put("referral", workerId);
        map.put("isDownload", isDownload);
        map.put("bindType", "2");
        map.put(Functions.FUNCTION, "9113");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(null, mContext, false, true, true, false, false, true) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                    }

                    @Override
                    protected void onFailure(String msg) {
                        super.onFailure(msg);
                    }
                });
    }
}