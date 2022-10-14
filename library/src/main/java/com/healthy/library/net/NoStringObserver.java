package com.healthy.library.net;

import android.content.Context;
import android.content.res.Resources;

import com.healthy.library.base.BaseView;
import com.healthy.library.businessutil.ChannelUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;


/**
 * 无感知的观察者 指 不会对页面状态进行修改 甚至不会toast
 */

public class NoStringObserver extends NoInsertStringObserver {
    public NoStringObserver(BaseView baseView, Context context) {
        this(baseView, context, false, false, false, false);
    }

    public NoStringObserver(BaseView baseView, Context context, boolean showSuccessMsg) {
        this(baseView, context, false, false, false, false);
    }

    public NoStringObserver(BaseView baseView, Context context, boolean showSuccessMsg, boolean showNetErr) {
        this(baseView, context, false, false, false, false);
    }
    public NoStringObserver(BaseView baseView, Context context,
                                  boolean showSuccessMsg, boolean showFailureMsg, boolean showErrorMsg,
                                  boolean showNetErr) {
        this(baseView, context, false,
                false, false, false, false, false);

    }
    public NoStringObserver(BaseView baseView, Context context,
                                  boolean showSuccessMsg, boolean showFailureMsg, boolean showErrorMsg,
                                  boolean showNetErr,boolean showDataErr, boolean showLoading) {
        super(baseView, context, false,
                false, false, false, false, false);
        setmInterViewFlag(false);
    }
}
