package com.healthy.library.net;

import android.content.Context;

import com.healthy.library.base.BaseView;

/**
 * 无感知的观察者 指 不会对页面状态进行修改 但是会toast出对应
 */

public class NoInsertStringObserver extends StringObserver {

    public NoInsertStringObserver(BaseView baseView, Context context) {
        this(baseView, context, true, true, false, true);
    }

    public NoInsertStringObserver(BaseView baseView, Context context, boolean showSuccessMsg) {
        this(baseView, context, showSuccessMsg, true, false, true);
    }

    public NoInsertStringObserver(BaseView baseView, Context context, boolean showSuccessMsg, boolean showNetErr) {
        this(baseView, context, showSuccessMsg, true, false, showNetErr);
    }


    public NoInsertStringObserver(BaseView baseView, Context context,
                          boolean showSuccessMsg, boolean showFailureMsg, boolean showErrorMsg,
                          boolean showNetErr) {
        this(baseView, context, showSuccessMsg,
                showFailureMsg, showErrorMsg, showNetErr, true, true);

    }
    public NoInsertStringObserver(BaseView baseView, Context context,
                                  boolean showSuccessMsg, boolean showFailureMsg, boolean showErrorMsg,
                                  boolean showNetErr,boolean showDataErr, boolean showLoading) {
        super(baseView, context, showSuccessMsg,
                showFailureMsg, showErrorMsg, showNetErr, showDataErr, showDataErr);
        setmInterViewFlag(false);
    }
}
