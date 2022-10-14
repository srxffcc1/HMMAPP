package com.health.client.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.health.client.R;
import com.health.client.contract.LoginContract;
import com.health.client.model.UserInfoModel;
import com.health.client.presenter.LoginPresenter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.constant.Constants;
import com.healthy.library.constant.Length;
import com.healthy.library.constant.SpKey;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.contract.AdContract;
import com.healthy.library.interfaces.OnNetRetryListener;
import com.healthy.library.model.AdModel;
import com.healthy.library.net.RxLifecycleUtils;
import com.healthy.library.presenter.AdPresenter;
import com.healthy.library.presenter.DeleteJiGuangPresenter;
import com.healthy.library.routes.AppRoutes;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.routes.MineRoutes;
import com.healthy.library.utils.CheckUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.AutoFitCheckBox;
import com.healthy.library.widget.StatusLayout;
import com.umeng.analytics.MobclickAgent;

import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;


public class CodeLoginFragment extends BaseFragment implements TextWatcher, LoginContract.View, AdContract.View {

    private StatusLayout layoutStatus;
    private ConstraintLayout phoneLL;
    private ImageView phoneIcon;
    private TextView phoneTips;
    private ImageView ivDel;
    private EditText etPhone;
    private ConstraintLayout codeLL;
    private ImageView phoneCode;
    private TextView tvGetCode;
    private EditText etCode;
    private TextView tvLogin;
    private LinearLayout selectLayout;
    private AutoFitCheckBox selectImg;
    private TextView tvProtocolPre;
    private TextView tvProtocol;
    private TextView tvProtocolTmp;
    private TextView tvProtocol2;

    private LoginPresenter mLoginPresenter;
    private Disposable mCountDownDisposable;
    private AdPresenter adPresenter;

    public CodeLoginFragment() {

    }

    public static CodeLoginFragment newInstance() {
        CodeLoginFragment fragment = new CodeLoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showContent();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_code_login;
    }

    @Override
    protected void findViews() {
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        phoneLL = (ConstraintLayout) findViewById(R.id.phoneLL);
        phoneIcon = (ImageView) findViewById(R.id.phone_icon);
        phoneTips = (TextView) findViewById(R.id.phoneTips);
        ivDel = (ImageView) findViewById(R.id.iv_del);
        etPhone = (EditText) findViewById(R.id.et_phone);
        codeLL = (ConstraintLayout) findViewById(R.id.codeLL);
        phoneCode = (ImageView) findViewById(R.id.phone_code);
        tvGetCode = (TextView) findViewById(R.id.tv_get_code);
        etCode = (EditText) findViewById(R.id.et_code);
        tvLogin = (TextView) findViewById(R.id.tv_login);
        selectLayout = (LinearLayout) findViewById(R.id.selectLayout);
        selectImg = (AutoFitCheckBox) findViewById(R.id.selectImg);
        tvProtocolPre = (TextView) findViewById(R.id.tv_protocol_pre);
        tvProtocol = (TextView) findViewById(R.id.tv_protocol);
        tvProtocolTmp = (TextView) findViewById(R.id.tv_protocol_tmp);
        tvProtocol2 = (TextView) findViewById(R.id.tv_protocol2);

        init();

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(v);
            }
        });
        ivDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearPhone(v);
            }
        });
        tvGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCode(v);
            }
        });
        tvProtocol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProtocol(v);
            }
        });
        tvProtocol2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProtocol2(v);
            }
        });
    }

    private void init() {
        setStatusLayout(layoutStatus);
        MobclickAgent.onEvent(mContext, "event2Login");
        adPresenter = new AdPresenter(mContext, this);
        mLoginPresenter = new LoginPresenter(mContext, this);

        etPhone.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(Length.PHONE_LEGTH)
        });
        etPhone.setInputType(InputType.TYPE_CLASS_NUMBER);

        etCode.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(Length.CODE_LENGTH)
        });
        etCode.setInputType(InputType.TYPE_CLASS_NUMBER);

        etPhone.addTextChangedListener(this);
        etCode.addTextChangedListener(this);

        tvProtocol.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG | Paint.UNDERLINE_TEXT_FLAG);
        tvProtocol2.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG | Paint.UNDERLINE_TEXT_FLAG);

        layoutStatus.setOnNetRetryListener(new OnNetRetryListener() {
            @Override
            public void onRetryClick() {
                layoutStatus.updateStatus(StatusLayout.Status.STATUS_CONTENT);
            }
        });
    }

    /**
     * 清除手机号
     */
    public void clearPhone(View view) {
        etPhone.getEditableText().clear();
        phoneTips.setVisibility(View.GONE);
    }

    public void getCode(View view) {
        String phone = etPhone.getText().toString();
        if ("18866668888".equals(phone)) {
            onGetCodeSuccess();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    etCode.setText("888888");
                }
            }, 1000);
            return;
        }
        if (CheckUtils.checkPhone(phone)) {
            mLoginPresenter.getCode(etPhone.getText().toString());
        } else {
            if (phone != null && phone.length() > 0) {
                phoneTips.setVisibility(View.VISIBLE);
            }
            showToast("请输入正确的手机号");
        }
    }

    /**
     * 登录
     */
    public void login(View v) {
        if (!selectImg.isChecked()) {
            Toast.makeText(mContext, "为保障您的权益，您需同意憨妈妈协议⽅可继续使⽤憨妈妈", Toast.LENGTH_SHORT).show();
            return;
        }
        mLoginPresenter.login(etPhone.getText().toString(), etCode.getText().toString(), null, null);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        ivDel.setVisibility(etPhone.getText().length() == 0 ? View.INVISIBLE : View.VISIBLE);
        if (CheckUtils.checkPhone(etPhone.getText())
                && etCode.getText().length() == Length.CODE_LENGTH) {
            tvLogin.setEnabled(true);
            tvLogin.setAlpha(1);
        } else {
            tvLogin.setEnabled(false);
            tvLogin.setAlpha(0.5f);
        }
    }


    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onGetCodeSuccess() {
        etCode.requestFocus();
        Observable.intervalRange(0, 61, 0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .to(RxLifecycleUtils.bindLifecycle(this))
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCountDownDisposable = d;
                    }

                    @Override
                    public void onNext(Long aLong) {
                        long reset = 60 - aLong;
                        if (reset == 0) {
                            tvGetCode.setText("获取验证码");
                            tvGetCode.setTextColor(Color.parseColor("#0176E5"));
                            tvGetCode.setEnabled(true);
                        } else {
                            tvGetCode.setText(String.format("%sS", reset));
                            tvGetCode.setTextColor(Color.parseColor("#666666"));
                            tvGetCode.setEnabled(false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public void onGetCodeFail() {
        tvGetCode.setEnabled(true);
        tvGetCode.setText("获取验证码");
    }

    @Override
    public void onLoginSuccess(UserInfoModel userInfo) {
        String status = SpUtils.getValue(mContext, SpKey.STATUS);
        MobclickAgent.onEvent(mContext, "event2LoginClick");
        String birthday = SpUtils.getValue(mContext, SpKey.USER_BIRTHDAY);
        try {
            adPresenter.getAd(new SimpleHashMapBuilder<String, Object>().puts("type", "3").puts("triggerPage", "8").puts("advertisingArea", LocUtil.getAreaNo(mContext, SpKey.LOC_ORG)));
            adPresenter.getAd(new SimpleHashMapBuilder<String, Object>().puts("type", "1").puts("triggerPage", "1").puts("advertisingArea", LocUtil.getAreaNo(mContext, SpKey.LOC_ORG)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(TextUtils.isEmpty(birthday)&&!SpUtils.getValue(mContext, SpKey.AUDITSTATUS, true)){
            registerPush();
            ARouter.getInstance().build(MineRoutes.MINE_UPDATE_USER_INFO)
                    .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK)
                    .navigation();
        }else if (Constants.STATUS_NONE.equals(status)) {
            registerPush();
            ARouter.getInstance().build(AppRoutes.APP_CHOOSE_SEX)
                    .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK)
                    .navigation();
        } else {
            registerPush();
            ARouter.getInstance().build(AppRoutes.APP_MAIN)
                    .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK)
                    .navigation();
        }
    }

    @Override
    public void onLoginFail() {

    }

    @Override
    public void onZxingLoginSuccess(String result) {

    }

    @Override
    public void updatePwdResult(String result) {

    }

    @Override
    public void checkCodeResult(String result) {

    }

    private void registerPush() {
        String id = SpUtils.getValue(mContext, SpKey.USER_ID);
        String status = SpUtils.getValue(mContext, SpKey.STATUS);
        String phone = SpUtils.getValue(mContext, SpKey.PHONE);
        int phoneed = 0;
        if (!TextUtils.isEmpty(phone)) {
            phoneed = Integer.parseInt(phone.substring(phone.length() - 4, phone.length()));
        }

        new DeleteJiGuangPresenter(mContext).deleteJiGuang();
        //JPushInterface.resumePush(getActivity().getApplication());
    }

    @Override
    public void onRequestFinish() {
        layoutStatus.updateStatus(StatusLayout.Status.STATUS_CONTENT);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCountDownDisposable != null && !mCountDownDisposable.isDisposed()) {
            mCountDownDisposable.dispose();
        }
    }

    public void showProtocol(View view) {
        ARouter.getInstance().build(IndexRoutes.INDEX_WEBVIEW)
                .withString("url", UrlKeys.URL_PROTOCOL)
                .withBoolean("needfindcollect", false)
                .withString("title", "")
                .navigation();
    }

    public void showProtocol2(View view) {
        ARouter.getInstance().build(IndexRoutes.INDEX_WEBVIEW)
                .withString("url", UrlKeys.URL_HMMPTYSZC)
                .withString("title", "")
                .withBoolean("needfindcollect", false)
                .navigation();
    }

    @Override
    public void onSucessGetAds(List<AdModel> adModels) {
        if (adModels.size() > 0) {
            AdModel adModel = adModels.get(0);
            if (adModel.type == 3 && adModel.triggerPage == 8) {
                SpUtils.store(mContext, SpKey.USER_Wel_AD_Bean, new Gson().toJson(adModel));
            }
            if (adModel.type == 1 && adModel.triggerPage == 1) {
                SpUtils.store(mContext, SpKey.USER_Main_AD_Bean, new Gson().toJson(adModel));
            }
        }
    }
}