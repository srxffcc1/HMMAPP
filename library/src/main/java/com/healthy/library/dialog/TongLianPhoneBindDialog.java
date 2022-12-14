package com.healthy.library.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;

import com.google.gson.Gson;
import com.healthy.library.LibApplication;
import com.healthy.library.R;
import com.healthy.library.base.BaseDialogFragment;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.Length;
import com.healthy.library.constant.SpKey;
import com.healthy.library.contract.TongLianPhoneContract;
import com.healthy.library.model.TongLianMemberData;
import com.healthy.library.net.RxLifecycleUtils;
import com.healthy.library.net.RxThreadUtils;
import com.healthy.library.presenter.TongLianPhonePresenter;
import com.healthy.library.utils.ObjUtil;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.StringUtils;
import com.hss01248.dialog.StyledDialog;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class TongLianPhoneBindDialog extends BaseDialogFragment implements TongLianPhoneContract.View, TextWatcher {

    View view;
    private Dialog loading;
    TongLianPhonePresenter tongLianPhonePresenter;
    private Disposable mCountDownDisposable;
    private ConstraintLayout topLL;
    private TextView tvChooseTimeTitle;
    private ImageView closeBtn;
    private TextView peoplePhoneText;
    private ConstraintLayout verificationCodeLL;
    private EditText verificationCodeEt;
    private ImageView verificationCodeClear;
    private TextView verificationCodeSplit;
    private TextView verificationCodeBtn;
    private ImageView codeTipImg;
    private TextView codeTip;
    private TextView agreeBtn;

    public TongLianPhoneBindDialog setOnDialogAgreeClickListener(OnDialogAgreeClickListener onDialogAgreeClickListener) {
        this.onDialogAgreeClickListener = onDialogAgreeClickListener;
        return this;
    }

    OnDialogAgreeClickListener onDialogAgreeClickListener;
    @Override
    public int show(FragmentTransaction transaction, String tag) {
        return super.show(transaction, tag);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.service_dialog_phonebind, null);
        StyledDialog.init(getContext());
        tongLianPhonePresenter=new TongLianPhonePresenter(getActivity(),this);
        initView(view);
        buildView();
        return view;

    }

    private void buildView() {
        peoplePhoneText.setText("?????????????????????"+StringUtils.getPhoneHide(SpUtils.getValue(LibApplication.getAppContext(), SpKey.PHONE))+"????????????????????????");
        verificationCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tongLianPhonePresenter.sendCode(new SimpleHashMapBuilder<String, Object>().puts(Functions.FUNCTION, "allin_10002")
                        .puts("verificationCodeType","9"));
            }
        });
        agreeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gobindPhone();
            }
        });
        verificationCodeClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificationCodeEt.setText("");
                verificationCodeClear.setVisibility(View.GONE);
            }
        });
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        verificationCodeEt.addTextChangedListener(this);
    }

    private void gobindPhone() {
        if(checkIllegal()){
            tongLianPhonePresenter.bindPhone(new SimpleHashMapBuilder<String, Object>()
                    .puts(Functions.FUNCTION, "allin_10003")
                    .puts("verificationCode",verificationCodeEt.getText().toString()));
        }
    }
    public boolean checkIllegal() {
        if(TextUtils.isEmpty(verificationCodeEt.getText().toString())){
            Toast.makeText(getActivity(),"??????????????????",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        window.setWindowAnimations(R.style.BottomDialogAnimation);
        if (window != null) {
            View decorView = window.getDecorView();
            decorView.setPadding(0, 0, 0, 0);
            decorView.setBackgroundResource(R.drawable.shape_dialog);
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.BOTTOM;
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(params);

        }
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
    }

    public static TongLianPhoneBindDialog newInstance() {

        Bundle args = new Bundle();
        TongLianPhoneBindDialog fragment = new TongLianPhoneBindDialog();
        fragment.setArguments(args);
        return fragment;
    }

    private void initView(View view) {

        topLL = (ConstraintLayout) view.findViewById(R.id.topLL);
        tvChooseTimeTitle = (TextView) view.findViewById(R.id.tv_choose_time_title);
        closeBtn = (ImageView)view. findViewById(R.id.closeBtn);
        peoplePhoneText = (TextView) view.findViewById(R.id.peoplePhoneText);
        verificationCodeLL = (ConstraintLayout)view. findViewById(R.id.verificationCodeLL);
        verificationCodeEt = (EditText) view.findViewById(R.id.verificationCodeEt);
        verificationCodeClear = (ImageView) view.findViewById(R.id.verificationCodeClear);
        verificationCodeSplit = (TextView) view.findViewById(R.id.verificationCodeSplit);
        verificationCodeBtn = (TextView) view.findViewById(R.id.verificationCodeBtn);
        codeTipImg = (ImageView)view. findViewById(R.id.codeTipImg);
        codeTip = (TextView) view.findViewById(R.id.codeTip);
        agreeBtn = (TextView) view.findViewById(R.id.agreeBtn);
    }

    @Override
    public void showLoading() {
        if (loading == null) {
            loading = StyledDialog.buildMdLoading().setForceWidthPercent(0.3f).show();
        } else {
            loading.show();
        }
    }

    @Override
    public void showToast(CharSequence msg) {

    }

    @Override
    public void showNetErr() {

    }

    @Override
    public void onRequestStart(Disposable disposable) {

    }

    @Override
    public void showContent() {
        StyledDialog.dismiss(loading);
    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void onRequestFinish() {
        StyledDialog.dismiss(loading);
    }

    @Override
    public void getData() {

    }

    @Override
    public void showDataErr() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCountDownDisposable != null && !mCountDownDisposable.isDisposed()) {
            mCountDownDisposable.dispose();
        }
    }

    @Override
    public void onSucessSendCode(String result) {
        if (result != null) {
            codeTip.setText("??????????????????");
            codeTip.setVisibility(View.VISIBLE);
            codeTip.postDelayed(new Runnable() {
                @Override
                public void run() {
                    codeTip.setVisibility(View.GONE);
                }
            },3000);
            LifecycleOwner lifecycleOwner = getActivity();
            Observable.intervalRange(0, 61, 0, 1, TimeUnit.SECONDS)
                    .compose(RxThreadUtils.<Long>Obs_io_main())
                    .to(RxLifecycleUtils.<Long>bindLifecycle(lifecycleOwner))
                    .subscribe(new Observer<Long>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            mCountDownDisposable = d;
                        }

                        @Override
                        public void onNext(Long aLong) {
                            long reset = 60 - aLong;
                            if (reset == 0) {

                                verificationCodeBtn.setText("???????????????");
                                verificationCodeBtn.setTextColor(Color.parseColor("#FF256C"));
                                verificationCodeBtn.setEnabled(true);
                            } else {
                                verificationCodeBtn.setText(String.format("%s????????????", reset));
                                verificationCodeBtn.setTextColor(Color.parseColor("#999999"));
                                verificationCodeBtn.setEnabled(false);
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

    }

    @Override
    public void onSucessBindPhone(String result) {
        if(result!=null){
            if(result.contains("??????")){
                Toast.makeText(getActivity(),"????????????",Toast.LENGTH_SHORT).show();
                if(onDialogAgreeClickListener!=null){
                    dismiss();
                    onDialogAgreeClickListener.onDialogAgree();
                }
            }else {
                if(result!=null&&result.contains("?????????")){
                    Toast.makeText(getActivity(),"????????????",Toast.LENGTH_SHORT).show();
                    TongLianMemberData tongLianMemberData= ObjUtil.getObj(SpUtils.getValue(LibApplication.getAppContext(),SpKey.TONGLIANBIZUSER),TongLianMemberData.class);
                    if(tongLianMemberData!=null) {//?????????
                        tongLianMemberData.memberInfo.isPhoneChecked=true;
                    }
                    SpUtils.store(LibApplication.getAppContext(), SpKey.TONGLIANBIZUSER,new Gson().toJson(tongLianMemberData));
                    if(onDialogAgreeClickListener!=null){
                        dismiss();
                        onDialogAgreeClickListener.onDialogAgree();
                    }
                }else {
                    codeTip.setText(result);
                    codeTip.setVisibility(View.VISIBLE);
                    tongLianPhonePresenter.getTongLianPhoneStatus(new SimpleHashMapBuilder<String, Object>());
                }

            }

        }
    }

    @Override
    public void onSuccessTongLianPhoneStatus(TongLianMemberData tongLianMemberData) {
        if(tongLianMemberData.memberInfo.isPhoneChecked){//????????????
            dismiss();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        verificationCodeClear.setVisibility(verificationCodeEt.getText().length() == 0 ? View.INVISIBLE : View.VISIBLE);
        if (verificationCodeEt.getText().length() == Length.CODE_LENGTH) {
            agreeBtn.setEnabled(true);
            agreeBtn.setAlpha(1);
        } else {
            agreeBtn.setEnabled(false);
            agreeBtn.setAlpha(0.5f);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public interface OnDialogAgreeClickListener {
        void onDialogAgree();
    }
}
