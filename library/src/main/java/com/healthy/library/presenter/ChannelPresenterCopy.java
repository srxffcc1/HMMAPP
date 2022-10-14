package com.healthy.library.presenter;

import android.content.Context;

import com.healthy.library.BuildConfig;
import com.healthy.library.businessutil.ChannelUtil;
import com.healthy.library.constant.SpKey;
import com.healthy.library.contract.ChannelContract;
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

public class ChannelPresenterCopy implements ChannelContract.Presenter{

    private Context mContext;

    public ChannelPresenterCopy(Context context) {
        mContext = context;
    }


    @Override
    public void getIsAuditing(Map<String, Object> map) {
        map.put("function", "6002");
        map.put("channel", ChannelUtil.getChannel(mContext));
        map.put("version", BuildConfig.VERSION_NAME);
        //System.out.println("获取审核信息");
        ObservableHelper.createObservableNoLife(mContext, map)
                .subscribe(new NoStringObserver(null, mContext,
                        false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        boolean result=false;
                        try {
                            result=(0==(new JSONObject(obj).optJSONObject("data").optInt("auditStatus")));// 0为审核中 1为通过
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        SpUtils.store(mContext, SpKey.AUDITSTATUS,result); //true 为审核中 fasle为通过
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        SpUtils.store(mContext, SpKey.AUDITSTATUS,true);
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                    }
                });
    }

    @Override
    public void checkVersion(Map<String, Object> map) {

    }

    @Override
    public void getMine() {

    }
}