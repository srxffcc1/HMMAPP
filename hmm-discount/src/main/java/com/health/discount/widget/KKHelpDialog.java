package com.health.discount.widget;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.health.discount.R;
import com.healthy.library.base.BaseDialogFragment;
import com.healthy.library.model.KickResult;
import com.healthy.library.widget.CornerImageView;

public class KKHelpDialog extends BaseDialogFragment {
    public KickResult kickResult;
    private android.widget.TextView kkMoney;
    private android.widget.TextView kkRed;
    private android.widget.TextView kkYellow;
    private com.healthy.library.widget.CornerImageView kkIcon;
    private View.OnClickListener redOnclickListener;
    private View.OnClickListener yellowOnclickListener;

    public KKHelpDialog setRedOnclickListener(View.OnClickListener redOnclickListener) {
        this.redOnclickListener = redOnclickListener;
        return this;
    }

    public KKHelpDialog setYellowOnclickListener(View.OnClickListener yellowOnclickListener) {
        this.yellowOnclickListener = yellowOnclickListener;
        return this;
    }

    public KKHelpDialog setKickResult(KickResult kickResult) {
        this.kickResult = kickResult;
        return this;
    }

    public static KKHelpDialog newInstance() {

        Bundle args = new Bundle();

        KKHelpDialog fragment = new KKHelpDialog();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dis_dialog_kk_help, null);
        builder.setView(view);
        displayDialog(view);
        return builder.create();
    }

    private void displayDialog(View view) {
        kkMoney = (TextView) view.findViewById(R.id.kkMoney);
        kkRed = (TextView) view.findViewById(R.id.kkRed);
        kkYellow = (TextView) view.findViewById(R.id.kkYellow);
        kkIcon = (CornerImageView) view.findViewById(R.id.kk_icon);
        if(kickResult.joinStatus==1){
            kkRed.setVisibility(View.GONE);
        }
        kkRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(redOnclickListener!=null){

                    redOnclickListener.onClick(v);
                }
            }
        });
        kkYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(yellowOnclickListener!=null){

                    yellowOnclickListener.onClick(v);
                }
            }
        });
        kkMoney.setText("铁们~谢谢你帮我砍掉"+kickResult.discountMoney+"元，你也可以参加哦~");
    }

    @Override
    public void onStart() {
        super.onStart();
        //设置背景半透明
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);

    }

    private void initView() {


    }
}
