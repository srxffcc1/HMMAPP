package com.health.mine.widget;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.example.lib_ShapeView.view.ShapeTextView;
import com.health.mine.R;
import com.healthy.library.constant.SpKey;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.vcview.VerificationCodeView;
import com.hss01248.dialog.StyledDialog;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;

/**
 * @version V1.0
 * @Description: (提现验证码校验)
 * @author:LiuWei
 * @date 2022/8/27
 */
public class TakeOutCodeDialog extends DialogFragment implements VerificationCodeView.OnCodeFinishListener {

    private onInputListener listener;

    private ConstraintLayout dialogBg;
    private ImageView dialogCloseButton;
    private TextView payTitle;
    private TextView tipsTxt;
    private VerificationCodeView codeView;
    private TextView codeQuestion;
    private ShapeTextView receivedBtn;
    private Disposable mCountDownDisposable;

    public TakeOutCodeDialog setPrice(String price) {

        return this;
    }

    public TakeOutCodeDialog setType(int type) {

        return this;
    }

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        try {
            super.show(manager, tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static TakeOutCodeDialog newInstance() {

        TakeOutCodeDialog fragment = new TakeOutCodeDialog();
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        //设置背景半透明
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
//        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.talk_out_code_dialog_layout, null);
        builder.setView(view);
        displayDialog(view);
        Dialog result = builder.create();
        return result;
    }

    private void displayDialog(View view) {
        dialogBg = view.findViewById(R.id.dialog_bg);
        dialogCloseButton = view.findViewById(R.id.dialog_close_button);
        payTitle = view.findViewById(R.id.payTitle);
        tipsTxt = view.findViewById(R.id.tipsTxt);
        codeView = view.findViewById(R.id.codeView);
        codeQuestion = view.findViewById(R.id.codeQuestion);
        receivedBtn = view.findViewById(R.id.receivedBtn);
        codeView.setOnCodeFinishListener(this);
        dialogCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        String phone = SpUtils.getValue(getContext(), SpKey.PHONE);
        //showKeyBoard(codeView);
        tipsTxt.setText(String.format("已发送至尾号%s的手机", phone.substring(phone.length() - 4, phone.length())));
        @SuppressLint("AutoDispose") Disposable countdownDisposable = Flowable.intervalRange(0, 61, 0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        long reset = 60 - aLong;
                        if (reset == 0) {
                            receivedBtn.setText("获取验证码");
                            receivedBtn.setEnabled(true);
                        } else {
                            receivedBtn.setText(String.format("%s秒后重发", reset));
                            receivedBtn.setEnabled(false);
                        }
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        //倒计时完毕事件处理
                        receivedBtn.setText("获取验证码");
                        receivedBtn.setEnabled(true);
                    }
                })
                .subscribe();
        receivedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "验证码已重发", Toast.LENGTH_LONG).show();
                listener.sendSms();
                dismiss();
            }
        });
    }

    public static void showKeyBoard(final View edCount){


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    edCount.setFocusable(true);
                    edCount.setFocusableInTouchMode(true);
                    //请求获得焦点
                    edCount.requestFocus();
                    //调用系统输入法
                    InputMethodManager imm = (InputMethodManager) StyledDialog.weakReference.get().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(edCount,InputMethodManager.RESULT_SHOWN);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },800);
        //设置可获得焦点

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCountDownDisposable != null && !mCountDownDisposable.isDisposed()) {
            mCountDownDisposable.dispose();
        }
    }

    @Override
    public void onTextChange(View view, String content) {
        if (view == codeView) {
            //Toast.makeText(getContext(), "输入中：" + content, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onComplete(View view, String content) {
        if (view == codeView) {
            listener.onInput(content);
            //Toast.makeText(getContext(), "输入结束：" + content, Toast.LENGTH_LONG).show();

        }
    }

    public interface onInputListener {

        void onInput(String data);

        void sendSms();
    }

    public void setInputListener(onInputListener getListener) {
        this.listener = getListener;
    }
}
