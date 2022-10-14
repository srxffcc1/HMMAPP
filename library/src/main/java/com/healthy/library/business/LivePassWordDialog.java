package com.healthy.library.business;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.healthy.library.LibApplication;
import com.healthy.library.R;
import com.healthy.library.base.BaseDialogFragment;
import com.healthy.library.contract.LivePasswordContract;
import com.healthy.library.presenter.LivePasswordPresenter;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.utils.SpUtils;

import io.reactivex.rxjava3.disposables.Disposable;


public class LivePassWordDialog extends BaseDialogFragment implements LivePasswordContract.View{


    private EditText passwordText;
    private ImageView closeMessageDialog;
    private TextView passWordOk;
    private String courseId;
    LivePasswordPresenter livePasswordPresenter;

    public LivePassWordDialog setLivePassCancelListener(LivePassCancelListener livePassCancelListener) {
        this.livePassCancelListener = livePassCancelListener;
        return this;
    }

    LivePassCancelListener livePassCancelListener;

    public LivePassWordDialog setNeedCancelable(boolean needCancelable) {
        this.needCancelable = needCancelable;
        return this;
    }

    private boolean needCancelable=true;


    public LivePassWordDialog setCourseId(String courseId) {
        this.courseId = courseId;
        return this;
    }

    public LivePassWordDialog setLivePassWordListener(LivePassWordListener livePassWordListener) {
        this.livePassWordListener = livePassWordListener;
        return this;
    }

    public static LivePassWordDialog newInstance() {

        Bundle args = new Bundle();
        LivePassWordDialog fragment = new LivePassWordDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        livePasswordPresenter=new LivePasswordPresenter(getActivity(),this);
        final View view = inflater.inflate(R.layout.live_dialog_password, null);
        builder.setView(view);
        displayDialog(view);
        return builder.create();
    }

    private void displayDialog(View view) {
        passWordOk = (TextView) view.findViewById(R.id.passWordOk);
        passwordText = (EditText) view.findViewById(R.id.passwordText);
        closeMessageDialog = (ImageView) view.findViewById(R.id.closeMessageDialog);
        closeMessageDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(needCancelable){
                    dismiss();
                }else {
                    dismiss();
                    if(livePassCancelListener!=null){
                        livePassCancelListener.onClose();
                    }
                }
            }
        });
        passWordOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPass(passwordText.getText().toString());
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        //设置背景半透明
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        getDialog().setCancelable(needCancelable);
        getDialog().setCanceledOnTouchOutside(needCancelable);

    }

    private void initView() {

    }

    public void onPass(String password){
        livePasswordPresenter.checkPassword(new SimpleHashMapBuilder<String, Object>().puts("coursePwd",password).puts("courseId",courseId));
    }

    @Override
    public void showLoading() {

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

    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void onRequestFinish() {

    }

    @Override
    public void getData() {

    }

    @Override
    public void showDataErr() {

    }
    LivePassWordListener livePassWordListener;
    @Override
    public void onSucessGetCheckResult(boolean result) {
        if(livePassWordListener!=null){
            if(result){
                livePassWordListener.onPassSucess();
                dismiss();
            }else {
                Toast.makeText(LibApplication.getAppContext(),"密码错误",Toast.LENGTH_SHORT).show();
            }
        }
    }
    public interface LivePassWordListener {
        public void onPassSucess();
    }
    public interface LivePassCancelListener {
        public void onClose();
    }
}
