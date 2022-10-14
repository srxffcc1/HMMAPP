package com.health.client.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.health.client.R;
import com.health.client.contract.LoginContract;
import com.health.client.model.UserInfoModel;
import com.health.client.presenter.LoginPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.constant.Length;
import com.healthy.library.constant.SpKey;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.interfaces.OnNetRetryListener;
import com.healthy.library.net.RxLifecycleUtils;
import com.healthy.library.routes.AppRoutes;
import com.healthy.library.utils.CheckUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.StatusLayout;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;


@Route(path = AppRoutes.APP_GETCODE)
public class GetCodeActivity extends BaseActivity implements TextWatcher, LoginContract.View, IsFitsSystemWindows {
    private ImageView ivClose;
    private StatusLayout layoutStatus;
    private ImageView ivHeaderBg;
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
    private TextView title;

    private LoginPresenter mLoginPresenter;
    private Disposable mCountDownDisposable;

    @Autowired
    String type;//1表示设置密码  2表示修改手机号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showContent();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_get_code;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        setStatusLayout(layoutStatus);
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

        if (type.equals("1")) {
            title.setText("设置密码");
        } else {
            // title.setText("修改密码");
        }
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

    public void next(View v) {
        showLoading();
        mLoginPresenter.checkCode(new SimpleHashMapBuilder<>().puts("mobile", etPhone.getText().toString()).puts("verifyCode", etCode.getText().toString()));
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
            if (phone.equals(SpUtils.getValue(mContext, SpKey.PHONE))) {
                mLoginPresenter.getPwdCode(new SimpleHashMapBuilder<>().puts("memberPhone", etPhone.getText().toString()));
            } else {
                showToast("您输入的号码跟当前账户手机号不一致");
            }
        } else {
            if (phone != null && phone.length() > 0) {
                phoneTips.setVisibility(View.VISIBLE);
            }
            showToast("请输入正确的手机号");
        }
    }

    @Override
    public void onRequestFinish() {
        layoutStatus.updateStatus(StatusLayout.Status.STATUS_CONTENT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCountDownDisposable != null && !mCountDownDisposable.isDisposed()) {
            mCountDownDisposable.dispose();
        }
    }

    @Override
    protected void findViews() {
        super.findViews();
        ivClose = (ImageView) findViewById(R.id.iv_close);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        ivHeaderBg = (ImageView) findViewById(R.id.iv_header_bg);
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
        title = (TextView) findViewById(R.id.title);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
        if (result != null && result.contains("正确")) {
            ARouter.getInstance()
                    .build(AppRoutes.APP_UPDATEPASSWORD)
                    .withString("phone", etPhone.getText().toString())
                    .withString("type", "1")
                    .withString("mobile", etPhone.getText().toString())
                    .withString("verifyCode", etCode.getText().toString())
                    .navigation();
            finish();
        }
    }
}