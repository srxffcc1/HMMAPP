package com.healthy.library.widget;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

import com.healthy.library.R;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.base.BaseView;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.interfaces.OnPasswordInputFinish;
import com.healthy.library.interfaces.PaySuccessFinish;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.utils.SpUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;


public class PayDialog extends DialogFragment implements OnPasswordInputFinish {
    private View view;
    private int state = 0;
    private int nosetstate = 0;//还未设置密码
    /*
     * 状态，1：默认密码已经设置
     *       2：密码还未设置
     * */
    private int type = 1;
    private TextView tv_no_pay_code_title, tv_type_text, tv_phone;
    private CardView cv_pay;
    private PasswordView pv_code;
    private String password = "";
    PaySuccessFinish paySuccessFinish;
    boolean ifCanSetPassword = true;
    Disposable mdDisposable;
    Long MAX_COUNT_TIME = 60L;
    SuperButton sb_get_code;
    private String phone;
    private int getCodeType = 1;

    public static PayDialog newInstance(int type) {

        Bundle args = new Bundle();
        args.putInt("type", type);
        PayDialog fragment = new PayDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (getContext() == null) {
            return super.onCreateDialog(savedInstanceState);
        }
        view = LayoutInflater.from(getContext()).inflate(R.layout.pay_popupwindow, null);
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        initContent();
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消



//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//
//
//        builder.setView(view);
//        AlertDialog dialog = builder.create();

        Window window = dialog.getWindow();
        if (window != null) {
            window.setWindowAnimations(R.style.BottomDialogAnimation);
            View decorView = window.getDecorView();
            decorView.setPadding(0, 0, 0, 0);
            decorView.setBackgroundResource(com.healthy.library.R.drawable.shape_dialog);
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.gravity = Gravity.BOTTOM;
        }

        return dialog;
    }

    private void initContent() {
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        type = bundle.getInt("type");
        findView();
        initView();
    }


    private void findView() {
        tv_no_pay_code_title = view.findViewById(R.id.tv_no_pay_code_title);
        cv_pay = view.findViewById(R.id.cv_pay);
        tv_type_text = view.findViewById(R.id.tv_type_text);
        pv_code = view.findViewById(R.id.pv_code);
        tv_phone = view.findViewById(R.id.tv_phone);
        sb_get_code = view.findViewById(R.id.sb_get_code);

    }


    private void initView() {
        phone = SpUtils.getValue(getContext(), SpKey.PHONE);
        pv_code.setVisibility(true);
        pv_code.setOnFinishInput(this);
        pv_code.setForgetPassword(this);
        switch (type) {
            case 1://密码已经设置
                //顶部密码未设置标题隐藏
                tv_no_pay_code_title.setVisibility(View.GONE);
                //获取验证码不显示
                cv_pay.setVisibility(View.GONE);
                tv_type_text.setVisibility(View.VISIBLE);
                //输入支付密码验证身份
                String pop_set_pay_code = "请输入<font color='#FF6266'>支付密码</font>验证身份";
                tv_type_text.setText(Html.fromHtml(pop_set_pay_code));
                break;
            case 2://密码还未设置
                tv_no_pay_code_title.setVisibility(View.VISIBLE);
                cv_pay.setVisibility(View.VISIBLE);
                tv_type_text.setVisibility(View.GONE);
                String pop_code = "您还<font color='#FF6266'>未设置支付密码</font>验证身份后设置";
                tv_no_pay_code_title.setText(Html.fromHtml(pop_code));
                //获取验证码显示
                tv_phone.setText("当前账号已与" + getPhone(phone) + "绑定");
                break;
        }
        //获取验证码
        sb_get_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map map = new HashMap();
                map.put("function", Functions.FUNCTION_GET_PAY_CODE);
                map.put("mobile", phone);
                map.put("type", getCodeType);
                ObservableHelper.createObservable(getContext(), map)
                        .subscribe(new StringObserver((BaseActivity) getActivity(), getContext(), false, true, true, false, false, false) {
                            @Override
                            protected void onGetResultSuccess(String obj) {
                                super.onGetResultSuccess(obj);
                                mdDisposable = Flowable.intervalRange(0, MAX_COUNT_TIME, 0, 1, TimeUnit.SECONDS)
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .doOnNext(new Consumer<Long>() {
                                            @Override
                                            public void accept(Long aLong) throws Exception {
                                                sb_get_code.setText("剩余 " + (MAX_COUNT_TIME - aLong) + "秒");
                                            }
                                        }).doOnComplete(new Action() {
                                            @Override
                                            public void run() throws Exception {
                                                sb_get_code.setEnabled(true);
                                                sb_get_code.setText("获取验证码");
                                            }
                                        }).subscribe();

                            }

                            @Override
                            protected void onFailure() {
                                super.onFailure();
                                sb_get_code.setEnabled(true);
                            }
                        });
            }
        });

    }

    // 设置监听方法，在第6位输入完成后触发
    public void paySuccessFinish(final PaySuccessFinish pass) {
        paySuccessFinish = pass;
    }

    @Override
    public void inputFinish() {
        switch (type) {
            /*----输入支付密码----*/
            case 1:
                switch (state) {
                    case 0:
                        if (ifCanSetPassword) {
                            //第一次输入。调用接口对比密码是否正确
                            Map map = new HashMap<String, String>();
                            map.put("function", Functions.FUNCTION_PAY_MOWTME);
                            map.put("payPassword", pv_code.getStrPassword());
                            BaseView baseView = ((BaseActivity) getActivity());
                            ObservableHelper.createObservable(getActivity(), map)
                                    .subscribe(new StringObserver(baseView, getActivity(), false, true, true, false, false, false) {
                                        @Override
                                        protected void onGetResultSuccess(String obj) {
                                            super.onGetResultSuccess(obj);
                                            paySuccessFinish.passwordRight();
                                        }

                                        @Override
                                        protected void onGetResultFail(String result) {
                                            super.onGetResultFail(result);
                                            try {
                                                JSONObject jsonObject = new JSONObject(result);
                                                if (null == jsonObject.get("data")) {
                                                    return;
                                                }
                                                JSONObject jsonObject1 = new JSONObject(jsonObject.get("data").toString());
                                                int count = Integer.valueOf(jsonObject1.get("freeCount").toString());
                                                if (count > 0) {
                                                    //如果密码不正确
                                                    pv_code.clearAllPassword();
                                                    //改变顶部标题
                                                    String s = String.format(getActivity().getString(R.string.pop_code_fail), String.valueOf(count));
                                                    tv_type_text.setText(Html.fromHtml("<font color='#FF6266'>" + s + "</font>"));
                                                    //忘记密码显示出来
                                                    pv_code.setForgetPasswordVisibility(true);
                                                } else {
                                                    String toast = jsonObject1.get("msgContent").toString();
                                                    //如果尝试次数已经超过,应该把忘记密码显示出来
                                                    tv_type_text.setText(Html.fromHtml("<font color='#FF6266'>" + toast + "</font>"));
                                                    ifCanSetPassword = false;
                                                    pv_code.setForgetPasswordVisibility(true);

                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }


                                        }
                                    });
                        }


                        break;
                }
                break;
            /*-------还未设置密码----*/
            case 2:
                switch (nosetstate) {
                    /*------验证码----------*/
                    case 0:
                        //验证码出入完成，进行校验
                        Map map = new HashMap();
                        map.put("function", Functions.FUNCTION_GET_YANZHENG_CODE);
                        map.put("mobile", phone);
                        map.put("verifyCode", pv_code.getStrPassword());
                        ObservableHelper.createObservable(getContext(), map)
                                .subscribe(new StringObserver((BaseActivity) getActivity(), getContext(), false, true, true, false, false, false) {
                                    @Override
                                    protected void onGetResultSuccess(String obj) {
                                        super.onGetResultSuccess(obj);
                                        //校验成功
                                        //设置标题，隐藏顶部
                                        tv_no_pay_code_title.setVisibility(View.GONE);
                                        cv_pay.setVisibility(View.GONE);
                                        tv_type_text.setVisibility(View.VISIBLE);
                                        tv_type_text.setText("请设置6位数字密码");
                                        nosetstate = 1;
                                        pv_code.clearAllPassword();
                                    }

                                    @Override
                                    protected void onFailure() {
                                        super.onFailure();
                                    }
                                });

                        break;
                    case 1:
                        /*-----------输入6位密码之后-----*/
                        //记录6位密码
                        password = pv_code.getStrPassword();
                        tv_type_text.setText("重输6位数字密码");
                        tv_type_text.setPadding(0, 30, 0, 0);
                        nosetstate = 2;
                        pv_code.clearAllPassword();
                        break;
                    case 2:
                        /*------再次输入密码之后----*/
                        //对比两次密码
                        if (password.equals(pv_code.getStrPassword())) {
                            //对比成功,调用接口
                            //如果两次密码一致，进行提交
                            Map map1 = new HashMap();
                            map1.put("function", Functions.FUNCTION_SET_PASSWORD);
                            map1.put("payPassword", password);
                            map1.put("payPasswordAgain", pv_code.getStrPassword());
                            /**
                             * payPasswordType 1-设置支付密码 2-忘记密码
                             * */
                            map1.put("payPasswordType", getCodeType);
                            ObservableHelper.createObservable(getContext(), map1)
                                    .subscribe(new StringObserver((BaseActivity) getActivity(), getContext(), false, true, true, false, false, false) {
                                        @Override
                                        protected void onGetResultSuccess(String obj) {
                                            super.onGetResultSuccess(obj);
                                            Toast.makeText(getContext(), "密码设置成功！", Toast.LENGTH_SHORT).show();
                                            //如果对比正确，回到输入密码页面
                                            //顶部密码未设置标题隐藏
                                            type = 1;
                                            state = 0;
                                            ifCanSetPassword = true;
                                            tv_no_pay_code_title.setVisibility(View.GONE);
                                            pv_code.clearAllPassword();
                                            //获取验证码不显示
                                            cv_pay.setVisibility(View.GONE);
                                            tv_type_text.setVisibility(View.VISIBLE);
                                            //输入支付密码验证身份
                                            String pop_set_pay_code = "请输入<font color='#FF6266'>支付密码</font>验证身份";
                                            tv_type_text.setText(Html.fromHtml(pop_set_pay_code));
                                        }
                                    });


                        } else {
                            //对比失败
                            Toast.makeText(getActivity(), "两次密码不一致", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }


                break;
        }

    }


    /*
     * 忘记密码
     * */
    @Override
    public void onForgetPassword() {
        getCodeType = 2;
        //顶部密码未设置标题隐藏
        tv_no_pay_code_title.setVisibility(View.GONE);
        //获取验证码显示。标题隐藏
        cv_pay.setVisibility(View.VISIBLE);
        tv_type_text.setVisibility(View.GONE);
        pv_code.setForgetPasswordVisibility(false);
        tv_phone.setText("当前账号已与" + getPhone(phone) + "绑定");
        //和未设置密码一样流程
        type = 2;
        pv_code.clearAllPassword();

    }


    private String getPhone(String phone) {

        return phone.substring(0, 3) + "****" + phone.substring(7, phone.length());
    }

}

