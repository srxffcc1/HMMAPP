package com.healthy.library.business;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.healthy.library.R;


public class HanMomNoStoreDialog extends DialogFragment {


    private TextView cityTxt;
    private LinearLayout buttonRight;
    private ImageView dialogCloseButton;

    private View.OnClickListener leftClickListener;
    private View.OnClickListener rightClickListener;
    private String cityName;

    public HanMomNoStoreDialog setLeftClickListener(View.OnClickListener leftClickListener) {
        this.leftClickListener = leftClickListener;
        return this;
    }

    public HanMomNoStoreDialog setRightClickListener(View.OnClickListener rightClickListener) {
        //System.out.println("产生一个");
        this.rightClickListener = rightClickListener;
        return this;
    }

    public HanMomNoStoreDialog setCity(String cityName) {
        this.cityName = cityName;
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

    public static HanMomNoStoreDialog newInstance() {

        Bundle args = new Bundle();
        HanMomNoStoreDialog fragment = new HanMomNoStoreDialog();
        fragment.setArguments(args);
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
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.dimAmount= 0.2f;
        window.setAttributes(params);
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);

    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        if (leftClickListener != null) {
            leftClickListener.onClick(null);
//                    EventBus.getDefault().post(new UpdateUserLocationMsg());
        }
        super.onDismiss(dialog);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.index_hanmom_dialog_no_store, null);
        builder.setView(view);
        displayDialog(view);
        Dialog result = builder.create();
        result.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (leftClickListener != null) {
                    leftClickListener.onClick(null);
//                    EventBus.getDefault().post(new UpdateUserLocationMsg());
                }
            }
        });
        return result;
    }

    private void displayDialog(View view) {
        cityTxt = (TextView) view.findViewById(R.id.cityTxt);
        buttonRight = (LinearLayout) view.findViewById(R.id.buttonRight);
        dialogCloseButton = (ImageView) view.findViewById(R.id.dialog_close_button);
//        buttonLeft.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
        if (!TextUtils.isEmpty(cityName)){
            cityTxt.setText("" + cityName);
        }
        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rightClickListener != null) {
                    rightClickListener.onClick(v);
//                    EventBus.getDefault().post(new UpdateUserLocationMsg());
                }
                dismiss();
            }
        });
        dialogCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    private void initView() {

    }
}
