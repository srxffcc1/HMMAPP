package com.healthy.library.presenter;

import android.content.Context;
import android.util.Base64;

import com.healthy.library.constant.SpKey;
import com.healthy.library.net.NoStringObserver;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.utils.SpUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/27 11:37
 * @des
 */

public class CardBoomPresenter  {

    private Context mContext;

    public CardBoomPresenter(Context context) {
        mContext = context;
    }

    public void boom(String triggerPageType) {
        Map<String,Object> map=new HashMap<>();
        map.put("function", "80011");
        map.put("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC));
        map.put("memberId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)));
        map.put("triggerType","1");
        map.put("triggerPageType",triggerPageType);
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

    public void boomS(String triggerPageType,String merchantId) {
        Map<String,Object> map=new HashMap<>();
        map.put("function", "80011");
        map.put("merchantId", merchantId);
        map.put("memberId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)));
        map.put("triggerType","1");
        map.put("triggerPageType",triggerPageType);
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
}