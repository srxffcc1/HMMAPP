package com.healthy.library.net;

import android.content.Context;
import android.content.Intent;

import com.alibaba.android.arouter.launcher.ARouter;
import com.healthy.library.BuildConfig;
import com.healthy.library.LibApplication;
import com.healthy.library.R;
import com.healthy.library.base.BaseView;
import com.healthy.library.businessutil.ChannelUtil;
import com.healthy.library.constant.SpKey;
import com.healthy.library.routes.AppRoutes;
import com.healthy.library.utils.SpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.UUID;

import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * @author Li
 * @date 2019/04/03 14:51
 * @des 泛型为string
 */

public class StringObserver implements Observer<String> {

    private static final String CODE_SUCCESS = "0";
    private static final String CODE_ILLEGAL_TOKEN = "-3";
    private static final String CODE_TOKEN_INVALID = "-4";
    private BaseView mBaseView;
    private Context mContext;
    private boolean mInterViewFlag =true;//与界面有交互的 比如 showContent showError
    private boolean mShowSuccessMsg;
    private boolean mShowFailureMsg;
    private boolean mShowErrMsg;
    private boolean mShowNetErr;
    private boolean mShowLoading;
    private boolean mShowDataErr;
    String uuid= UUID.randomUUID().toString();

    public StringObserver setmInterViewFlag(boolean mInterViewFlag) {
        this.mInterViewFlag = mInterViewFlag;
        return this;
    }

    public StringObserver(BaseView baseView, Context context) {
        this(baseView, context, true, true, false, true);
    }

    public StringObserver(BaseView baseView, Context context, boolean showSuccessMsg) {
        this(baseView, context, showSuccessMsg, true, false, true);
    }

    public StringObserver(BaseView baseView, Context context, boolean showSuccessMsg, boolean showNetErr) {
        this(baseView, context, showSuccessMsg, true, false, showNetErr);
    }


    public StringObserver(BaseView baseView, Context context,
                          boolean showSuccessMsg, boolean showFailureMsg, boolean showErrorMsg,
                          boolean showNetErr) {
        this(baseView, context, showSuccessMsg,
                showFailureMsg, showErrorMsg, showNetErr, true, true);
    }
    /**
     * 文字显示控制
     *
     * @param baseView       baseView
     * @param context        context
     * @param showSuccessMsg 报文为预期时 是否显示toast
     * @param showFailureMsg 报文不为预期时，是否显示toast
     * @param showErrorMsg   网络请求失败时，是否显示toast
     * @param showNetErr     网络请求失败时，是否显示失败界面
     * @param showDataErr    获取数据失败时，是否显示错误界面
     * @param showLoading    是否显示弹框
     */
    public StringObserver(BaseView baseView, Context context,
                          boolean showSuccessMsg, boolean showFailureMsg, boolean showErrorMsg,
                          boolean showNetErr, boolean showDataErr, boolean showLoading) {
        mBaseView = baseView;
//        if(baseView==null){
//            mBaseView=new BaseView() {
//                @Override
//                public void showLoading() {
//
//                }
//
//                @Override
//                public void showToast(CharSequence msg) {
//
//                }
//
//                @Override
//                public void showNetErr() {
//
//                }
//
//                @Override
//                public void onRequestStart(Disposable disposable) {
//
//                }
//
//                @Override
//                public void showContent() {
//
//                }
//
//                @Override
//                public void showEmpty() {
//
//                }
//
//                @Override
//                public void onRequestFinish() {
//
//                }
//
//                @Override
//                public void getData() {
//
//                }
//
//                @Override
//                public void showDataErr() {
//
//                }
//            };
//        }
        mContext=context;
        if(mContext==null){
            mContext = LibApplication.getAppContext();
        }
        mShowSuccessMsg = showSuccessMsg;
        mShowFailureMsg = showFailureMsg;
        mShowErrMsg = showErrorMsg;
        mShowNetErr = showNetErr;
        mShowDataErr = showDataErr;
        mShowLoading = showLoading;

    }
    @Override
    public void onSubscribe(Disposable d) {
        if(mInterViewFlag){
            try {
                mBaseView.onRequestStart(d);
                if (mShowLoading) {
                    mBaseView.showLoading();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    @Override
    public void onNext(String t) {
        if(mInterViewFlag){
            try {
                mBaseView.showContent();
                mBaseView.onRequestFinish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        onFinish();
        try {
            JSONObject jsonObject = new JSONObject(String.valueOf(t));
            String msg = jsonObject.getString("msg");
            switch (jsonObject.getString("code")) {
                case CODE_SUCCESS:
                    if (mShowSuccessMsg) {
                        try {
                            mBaseView.showToast(msg);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    onGetResultSuccess(String.valueOf(t));
                    break;
                case CODE_ILLEGAL_TOKEN:
                case CODE_TOKEN_INVALID:
                    if(mInterViewFlag){
                        try {
                            mBaseView.showToast(msg);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            String phone= SpUtils.getValue(mContext, SpKey.PHONE);
                        } catch (Exception e) {

                        }
                        SpUtils.store(mContext, SpKey.USER_ID,null);
                        SpUtils.store(mContext, SpKey.STATUS,null);
                        ARouter.getInstance().build(AppRoutes.APP_LOGINTRANSFER)
                                .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                        Intent.FLAG_ACTIVITY_NEW_TASK)
                                .navigation();
                    }
                    break;
                default:
                    onGetResultFail(String.valueOf(t));
                    onFailure();
                    if((msg!=null&&!msg.contains("参数错误"))||!ChannelUtil.isIpRealRelease()|| BuildConfig.DEBUG){
                        onFailure(msg);
                        onFailure(msg,String.valueOf(t));
                        if (mShowFailureMsg) {
                            mBaseView.showToast(msg);
                        }
                    }

                    break;
            }
            onGetResult(String.valueOf(t));
        } catch (JSONException e) {
            mBaseView.showToast("数据解析失败！");
            e.printStackTrace();
        }
    }

    @Override
    public void onError(Throwable e) {
        if(mInterViewFlag){
            mBaseView.onRequestFinish();
            if (!mShowDataErr && !mShowNetErr) {
                mBaseView.showContent();
            }
        }
        onFinish();
        onFailure();
        if(mInterViewFlag){
            if (e instanceof ConnectException||e instanceof SocketTimeoutException) {
                if (mShowNetErr) {
                    mBaseView.showNetErr();
                }
            } else {
                if (mShowDataErr) {
                    mBaseView.showDataErr();
                }
            }
        }
        if (mShowErrMsg) {
            mBaseView.showToast(mContext.getResources().getString(R.string.net_err));
        }
    }

    @Override
    public void onComplete() {
        if(mInterViewFlag){
            try {
                mBaseView.onRequestFinish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 请求结束（成功/失败）
     */
    protected void onFinish() {
    }

    /**
     * 请求成功，并且获取到了数据
     *
     * @param obj 报文
     */
    protected void onGetResult(String obj) {
    }

    /**
     * 请求成功，并且数据为预期中的格式
     *
     * @param obj 报文
     */
    protected void onGetResultSuccess(String obj) {

    }

    /**
     * 请求成功，但数据并不是预期中的格式
     *
     * @param result 报文
     */
    protected void onGetResultFail(String result) {

    }

    /**
     * 请求成功，但数据并不是预期中的格式
     */
    protected void onFailure() {

    }

    /**
     * 请求成功，但数据并不是预期中的格式 把msg带回来
     */
    protected void onFailure(String msg) {

    }

    protected void onFailure(String msg,String obj) {

    }

}
