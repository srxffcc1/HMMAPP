package com.health.mine.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.health.mine.R;
import com.healthy.library.constant.Constants;

public class HanMomPayDialog extends DialogFragment {

    private ImageView dialog_close_button;
    private TextView pay;

    private View.OnClickListener leftClickListener;
    private View.OnClickListener rightClickListener;
    private String price;
    private int type;//支付入口类型 1 报名支付
    private onGetPayTypeListener listener;
    private CheckBox weixinCheck;
    private CheckBox alpayCheck;

    public HanMomPayDialog setPrice(String price) {
        this.price = price;
        return this;
    }
    public HanMomPayDialog setType(int type){
        this.type = type;
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

    public static HanMomPayDialog newInstance() {

        HanMomPayDialog fragment = new HanMomPayDialog();
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
        final View view = inflater.inflate(R.layout.han_mom_pay_dialog_layout, null);
        builder.setView(view);
        displayDialog(view);
        Dialog result = builder.create();
        return result;
    }

    private void displayDialog(View view) {
        dialog_close_button = (ImageView) view.findViewById(R.id.dialog_close_button);
        pay = (TextView) view.findViewById(R.id.pay);
        alpayCheck = (CheckBox) view.findViewById(com.healthy.library.R.id.alpayCheck);
        weixinCheck = (CheckBox) view.findViewById(com.healthy.library.R.id.weixinCheck);
        dialog_close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        String mPayContent = "¥ " + price;
        if(type == 1){
            mPayContent += " 立即支付";
        } else {
            mPayContent += " 立即开通";
        }
        pay.setText(mPayContent);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alpayCheck.isChecked()) {
                    listener.setPayType(Constants.PAY_IN_A_LI);
                } else {
                    listener.setPayType(Constants.PAY_IN_WX);
                }

            }
        });

    }

    public interface onGetPayTypeListener {

        void setPayType(String type);
    }

    public void setPayTypeListener(onGetPayTypeListener getListener) {
        this.listener = getListener;
    }
}
