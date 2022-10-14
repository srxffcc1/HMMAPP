package com.health.client.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.health.client.R;
import com.health.client.contract.LoginContract;
import com.health.client.model.UserInfoModel;
import com.health.client.presenter.LoginPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.Length;
import com.healthy.library.constant.SpKey;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.interfaces.IsNoNeedPatchShow;
import com.healthy.library.net.RxLifecycleUtils;
import com.healthy.library.routes.AppRoutes;
import com.healthy.library.utils.CheckUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.StatusLayout;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;


@Route(path = AppRoutes.APP_UPDATEPHONE)
public class UpdatePhoneActivity extends BaseActivity implements IsFitsSystemWindows, TextWatcher, LoginContract.View, IsNoNeedPatchShow {

    private ImageView ivClose;
    private StatusLayout layoutStatus;
    private ConstraintLayout codeLayout;
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
    private ConstraintLayout phoneLayout;
    private ImageView img;
    private ConstraintLayout changePhoneLL;
    private ImageView changePhoneIcon;
    private TextView changePhoneTips;
    private TextView title;
    private ImageView changeIvDel;
    private EditText changeEtPhone;
    private TextView tvCommit;

    private LoginPresenter mLoginPresenter;
    private Disposable mCountDownDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showContent();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_update_phone;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        setStatusLayout(layoutStatus);
        mLoginPresenter = new LoginPresenter(mContext, this);
        etPhone.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(Length.PHONE_LEGTH)
        });
        changeEtPhone.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(Length.PHONE_LEGTH)
        });
        etPhone.setInputType(InputType.TYPE_CLASS_NUMBER);

        etCode.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(Length.CODE_LENGTH)
        });
        etCode.setInputType(InputType.TYPE_CLASS_NUMBER);

        etPhone.addTextChangedListener(this);
        etCode.addTextChangedListener(this);
        changeEtPhone.addTextChangedListener(this);

        String phone = SpUtils.getValue(mContext, SpKey.PHONE);
        if (!TextUtils.isEmpty(phone) && phone.length() >= 11) {
            title.setText("当前绑定" + phone.substring(0, 3) + "****" + phone.substring(7, phone.length()));
        }
    }

    @Override
    protected void findViews() {
        super.findViews();
        ivClose = (ImageView) findViewById(R.id.iv_close);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        codeLayout = (ConstraintLayout) findViewById(R.id.codeLayout);
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
        phoneLayout = (ConstraintLayout) findViewById(R.id.phoneLayout);
        img = (ImageView) findViewById(R.id.img);
        changePhoneLL = (ConstraintLayout) findViewById(R.id.change_phoneLL);
        changePhoneIcon = (ImageView) findViewById(R.id.change_phone_icon);
        changePhoneTips = (TextView) findViewById(R.id.change_phoneTips);
        changeIvDel = (ImageView) findViewById(R.id.change_iv_del);
        changeEtPhone = (EditText) findViewById(R.id.change_et_phone);
        tvCommit = (TextView) findViewById(R.id.tv_commit);
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
        changeIvDel.setVisibility(changeEtPhone.getText().length() == 0 ? View.INVISIBLE : View.VISIBLE);
        if (CheckUtils.checkPhone(changeEtPhone.getText())
                && changeEtPhone.getText().length() == Length.PHONE_LEGTH) {
            tvCommit.setEnabled(true);
            tvCommit.setAlpha(1);
        } else {
            tvCommit.setEnabled(false);
            tvCommit.setAlpha(0.5f);
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

    }

    /**
     * 清除手机号
     */
    public void clearPhone(View view) {
        etPhone.getEditableText().clear();
        phoneTips.setVisibility(View.GONE);
    }


    public void next(View v) {
        codeLayout.setVisibility(View.GONE);
        phoneLayout.setVisibility(View.VISIBLE);
    }

    public void commit(View v) {
        if (CheckUtils.checkPhone(changeEtPhone.getText())) {
            isAgree(1, "确认更换绑定", "更换后登陆账号将变为" + changeEtPhone.getText());
        } else {
            if (changeEtPhone.getText() != null && changeEtPhone.getText().length() > 0) {
                phoneTips.setVisibility(View.VISIBLE);
            }
            showToast("请输入正确格式的手机号");
        }
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

    private void isAgree(final int type, String title, String msg) {
        StyledDialog.init(this);
        StyledDialog.buildIosAlert(title, "\n" + msg, new MyDialogListener() {
            @Override
            public void onFirst() {
            }

            @Override
            public void onThird() {
                super.onThird();
            }

            @Override
            public void onSecond() {
                if (type == 1) {

                }
            }
        }).setBtnColor(com.health.servicecenter.R.color.dialogutil_text_black, com.health.servicecenter.R.color.colorPrimaryDark, 0).setBtnText("取消", "确定").show();
    }
}