package com.healthy.library.presenter;

import android.content.Context;

import com.healthy.library.BuildConfig;
import com.healthy.library.constant.SpKey;
import com.healthy.library.contract.PlusContract;
import com.healthy.library.net.NoStringObserver;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.utils.SpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * @author Li
 * @date 2019/03/27 11:37
 * @des
 */

public class PlusPresenterCopy implements PlusContract.Presenter {

    private Context mContext;

    public PlusPresenterCopy(Context context) {
        mContext = context;
    }

    @Override
    public void checkPlus(Map<String, Object> map) {
        map.put("function", "210001");
        //System.out.println("获取开屏信息");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(null, mContext,
                        false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        String result= null;
                        try {
                            result = new JSONObject(obj).getJSONObject("data").optString("isPlus");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        SpUtils.store(mContext, SpKey.PLUSSTATUS,"Y".equals(result));
                        if(BuildConfig.DEBUG){
//                            SpUtils.store(mContext, SpKey.PLUSSTATUS,true);
                        }

                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        SpUtils.store(mContext,SpKey.PLUSSTATUS,false);
                        if(BuildConfig.DEBUG){
//                            SpUtils.store(mContext, SpKey.PLUSSTATUS,true);
                        }
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                    }
                });
    }
}