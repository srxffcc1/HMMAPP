package com.healthy.library.business;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.healthy.library.R;
import com.healthy.library.base.BaseDialogFragment;
import com.healthy.library.message.UpdateUserLocationMsg;

import org.greenrobot.eventbus.EventBus;


public class NoStoreDialog extends BaseDialogFragment {


   // private TextView buttonLeft;
    private ImageView buttonRight;
    private ImageView dialogCloseButton;

    private View.OnClickListener leftClickListener;
    private View.OnClickListener rightClickListener;

    public NoStoreDialog setLeftClickListener(View.OnClickListener leftClickListener) {
        this.leftClickListener = leftClickListener;
        return this;
    }

    public NoStoreDialog setRightClickListener(View.OnClickListener rightClickListener) {
        //System.out.println("产生一个");
        this.rightClickListener = rightClickListener;
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

    public static NoStoreDialog newInstance() {

        Bundle args = new Bundle();
        NoStoreDialog fragment = new NoStoreDialog();
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
        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.index_dialog_no_store, null);
        builder.setView(view);
        displayDialog(view);
        Dialog result = builder.create();
        return result;
    }

    private void displayDialog(View view) {
       // buttonLeft = (TextView)view. findViewById(R.id.buttonLeft);
        buttonRight = (ImageView) view. findViewById(R.id.buttonRight);
        dialogCloseButton = (ImageView) view. findViewById(R.id.dialog_close_button);
//        buttonLeft.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rightClickListener!=null){
                    rightClickListener.onClick(v);
                    EventBus.getDefault().post(new UpdateUserLocationMsg());
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
