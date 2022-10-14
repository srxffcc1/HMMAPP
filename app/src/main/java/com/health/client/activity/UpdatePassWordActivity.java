package com.health.client.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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
import com.healthy.library.businessutil.ChannelUtil;
import com.healthy.library.constant.Length;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.interfaces.IsNoNeedPatchShow;
import com.healthy.library.interfaces.OnNetRetryListener;
import com.healthy.library.routes.AppRoutes;
import com.healthy.library.widget.StatusLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Route(path = AppRoutes.APP_UPDATEPASSWORD)
public class UpdatePassWordActivity extends BaseActivity implements TextWatcher, IsFitsSystemWindows, LoginContract.View , IsNoNeedPatchShow {

    @Autowired
    String type;//1表示新设置密码  不用输原密码  2表示修改密码 需要输原密码
    @Autowired
    String mobile;
    @Autowired
    String verifyCode;

    private ImageView ivClose;
    private ImageView pwdVisible;
    private StatusLayout layoutStatus;
    private ConstraintLayout phoneLL;
    private ImageView phoneIcon;
    private EditText etPhone;
    private ConstraintLayout codeLL;
    private ImageView phoneCode;
    private EditText etPwd1;
    private ConstraintLayout pwdLL;
    private ImageView pwdCode;
    private EditText etPwd2;
    private TextView tvLogin;
    private TextView title;
    private LoginPresenter loginPresenter;
    private boolean isVisible = false;//是否显示密码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showContent();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_update_pass_word;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        loginPresenter = new LoginPresenter(this, this);
        etPhone.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(Length.PWD_LENGTH)
        });
        etPwd1.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(Length.PWD_LENGTH)
        });
        etPwd2.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(Length.PWD_LENGTH)
        });
        etPhone.addTextChangedListener(this);
        etPwd1.addTextChangedListener(this);
        etPwd2.addTextChangedListener(this);

        if ("1".equals(type)) {
            phoneLL.setVisibility(View.GONE);
            title.setText("确认密码");
        } else {
            phoneLL.setVisibility(View.VISIBLE);
            title.setText("修改密码");
        }
        layoutStatus.setOnNetRetryListener(new OnNetRetryListener() {
            @Override
            public void onRetryClick() {
                layoutStatus.updateStatus(StatusLayout.Status.STATUS_CONTENT);
            }
        });
    }

    @Override
    protected void findViews() {
        super.findViews();
        ivClose = (ImageView) findViewById(R.id.iv_close);
        pwdVisible = (ImageView) findViewById(R.id.pwdVisible);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        phoneLL = (ConstraintLayout) findViewById(R.id.phoneLL);
        phoneIcon = (ImageView) findViewById(R.id.phone_icon);
        etPhone = (EditText) findViewById(R.id.et_phone);
        codeLL = (ConstraintLayout) findViewById(R.id.codeLL);
        phoneCode = (ImageView) findViewById(R.id.phone_code);
        etPwd1 = (EditText) findViewById(R.id.et_code);
        pwdLL = (ConstraintLayout) findViewById(R.id.pwdLL);
        pwdCode = (ImageView) findViewById(R.id.pwd_code);
        etPwd2 = (EditText) findViewById(R.id.et_pwd);
        tvLogin = (TextView) findViewById(R.id.tv_login);
        title = (TextView) findViewById(R.id.title);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (!ChannelUtil.isRealRelease()) {
            pwdVisible.setVisibility(View.VISIBLE);
        } else {
            pwdVisible.setVisibility(View.GONE);
        }
        pwdVisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeInputType(etPhone);
            }
        });
    }

    private void changeInputType(View v) {//隐藏和显示密码
        if (isVisible) {
            etPhone.setTransformationMethod(PasswordTransformationMethod.getInstance());
            etPwd1.setTransformationMethod(PasswordTransformationMethod.getInstance());
            etPwd2.setTransformationMethod(PasswordTransformationMethod.getInstance());
            pwdVisible.setImageResource(R.drawable.login_password_gone);
            isVisible = false;
        } else {
            etPhone.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            etPwd1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            etPwd2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            pwdVisible.setImageResource(R.drawable.login_password_visible);
            isVisible = true;
        }
        Selection.setSelection(etPhone.getText(), etPhone.getText().length());//移动光标至末尾
        Selection.setSelection(etPwd1.getText(), etPwd1.getText().length());//移动光标至末尾
        Selection.setSelection(etPwd2.getText(), etPwd2.getText().length());//移动光标至末尾
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // 禁止EditText输入空格
        if (type.equals("2")) {
            if (etPhone.getText() != null && etPhone.getText().toString().contains(" ")) {
                String[] str = s.toString().split(" ");
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < str.length; i++) {
                    sb.append(str[i]);
                }
                etPhone.setText(sb.toString());
                etPhone.setSelection(start);
            }
            if (etPhone.getText() != null && etPhone.getText().length() > 7
                    && etPwd1.getText() != null && etPwd1.getText().length() > 7
                    && etPwd2.getText() != null && etPwd2.getText().length() > 7) {
                tvLogin.setEnabled(true);
                tvLogin.setAlpha(1);
            } else {
                tvLogin.setEnabled(false);
                tvLogin.setAlpha(0.5f);
            }
        } else {
            if (etPwd1.getText() != null && etPwd1.getText().length() > 7
                    && etPwd2.getText() != null && etPwd2.getText().length() > 7) {
                tvLogin.setEnabled(true);
                tvLogin.setAlpha(1);
            } else {
                tvLogin.setEnabled(false);
                tvLogin.setAlpha(0.5f);
            }
        }
        if (etPwd1.getText() != null && etPwd1.getText().toString().contains(" ")) {
            String[] str = s.toString().split(" ");
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < str.length; i++) {
                sb.append(str[i]);
            }
            etPwd1.setText(sb.toString());
            etPwd1.setSelection(start);
        }
        if (etPwd2.getText() != null && etPwd2.getText().toString().contains(" ")) {
            String[] str = s.toString().split(" ");
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < str.length; i++) {
                sb.append(str[i]);
            }
            etPwd2.setText(sb.toString());
            etPwd2.setSelection(start);
        }

    }

    public void commit(View view) {
        if (checkPwd()) {
            if (type.equals("1")) {//验证码修改
                loginPresenter.updatePwd(new SimpleHashMapBuilder<>()
                        .puts("password", etPwd1.getText().toString())
                        .puts("password2", etPwd2.getText().toString())
                        .puts("mobile", mobile)
                        .puts("verifyCode", verifyCode)
                        .puts("changeType", "1"));
            } else {//重置密码
                loginPresenter.updatePwd(new SimpleHashMapBuilder<>()
                        .puts("oldPassword", etPhone.getText().toString())
                        .puts("password", etPwd1.getText().toString())
                        .puts("password2", etPwd2.getText().toString())
                        .puts("changeType", "2"));
            }
        }
    }

    private boolean checkPwd() {
        if (type.equals("2")) {
            if (!isPassword(etPhone.getText().toString())) {
                showToast("您输入的原密码格式不正确！");
                return false;
            }
        }
        if (!isPassword(etPwd1.getText().toString())) {
            showToast("密码需包含数字加字母8~20个字符");
            return false;
        }
        if (!isPassword(etPwd2.getText().toString())) {
            showToast("密码需包含数字加字母8~20个字符");
            return false;
        }
        if (!etPwd1.getText().toString().trim().equals(etPwd2.getText().toString().trim())) {
            showToast("两次密码不一致 请重新输入");
            etPwd1.setText("");
            etPwd2.setText("");
            return false;
        }
        return true;
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public boolean isPassword(String password) {
        String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,20}$";//.8~20位同时包含数字和字母
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(password);
        boolean isMatch = m.matches();
        return isMatch;
    }

    @Override
    public void onGetCodeSuccess() {

    }

    @Override
    public void onGetCodeFail() {

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
        if (result != null && result.contains("成功")) {
            showToast("修改成功");
            finish();
        }
    }

    @Override
    public void checkCodeResult(String result) {

    }
}