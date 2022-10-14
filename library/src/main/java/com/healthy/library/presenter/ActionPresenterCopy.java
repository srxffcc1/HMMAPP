package com.healthy.library.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.healthy.library.LibApplication;
import com.healthy.library.constant.SpKey;
import com.healthy.library.net.NoStringObserver;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.utils.FrameActivityManager;
import com.healthy.library.utils.SpUtils;

import java.util.Map;

/**
 * @author Li
 * @date 2019/03/27 11:37
 * @des
 */

public class ActionPresenterCopy {

    private Context mContext;

    public ActionPresenterCopy(Context context) {
        mContext = context;
        if(mContext == null){
            mContext = FrameActivityManager.instance().topActivity();
        }
    }

    public void posAction(Map<String, Object> map) {
        if(mContext!=null){
            if(!TextUtils.isEmpty(SpUtils.getValue(LibApplication.getAppContext(), SpKey.USER_ID))){
                map.put("function", "action_record_001");
                ObservableHelper.createObservableNoLife(mContext, map)
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


    }
}