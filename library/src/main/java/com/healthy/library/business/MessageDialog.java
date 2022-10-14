package com.healthy.library.business;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.healthy.library.R;
import com.healthy.library.base.BaseDialogFragment;
import com.healthy.library.utils.NotificationUtil;

public class MessageDialog extends BaseDialogFragment {
    private ConstraintLayout dialogBg;
    private RecyclerView contentRecycler;
    private android.widget.ImageView dialogCloseButton;
    private android.widget.ImageView passButton;
    private android.widget.ImageView iconMessageDialog;
    private android.widget.TextView titleMessageDialog;
    private android.widget.TextView contentMessageDialog;
    private android.widget.Space space;
    private android.widget.ImageView closeMessageDialog;
    private TextView buttonMessageDialog;
    private MessageOkClickListener messageOkClickListener;

    public MessageDialog setMessageCancelClickListener(MessageCancelClickListener messageCancelClickListener) {
        this.messageCancelClickListener = messageCancelClickListener;
        return this;
    }

    private MessageCancelClickListener messageCancelClickListener;

    public int messageTopRes;
    public String messageCenterText;
    public String messageBottomText;

    public MessageDialog setMessageTopRes(int messageTopRes,String messageCenterText,String messageBottomText) {
        this.messageTopRes = messageTopRes;
        this.messageCenterText = messageCenterText;
        this.messageBottomText = messageBottomText;
        return this;
    }

    public MessageDialog setMessageOkClickListener(MessageOkClickListener messageOkClickListener) {
        this.messageOkClickListener = messageOkClickListener;
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


    public static MessageDialog newInstance() {

        Bundle args = new Bundle();
        MessageDialog fragment = new MessageDialog();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_message, null);
        builder.setView(view);
        displayDialog(view);
        return builder.create();
    }

    private void displayDialog(View view) {
        dialogBg = (ConstraintLayout) view.findViewById(R.id.dialog_bg);
        iconMessageDialog = (ImageView) view.findViewById(R.id.iconMessageDialog);

        titleMessageDialog = (TextView) view.findViewById(R.id.titleMessageDialog);
        contentMessageDialog = (TextView) view.findViewById(R.id.contentMessageDialog);
        space = (Space) view.findViewById(R.id.space);
        closeMessageDialog = (ImageView) view.findViewById(R.id.closeMessageDialog);
        buttonMessageDialog = (TextView) view.findViewById(R.id.buttonMessageDialog);

        if(messageTopRes!=0){
            iconMessageDialog.setImageResource(messageTopRes);
            titleMessageDialog.setText(messageCenterText);
            contentMessageDialog.setText(messageBottomText);
        }

        closeMessageDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(messageCancelClickListener!=null){
                    messageCancelClickListener.onMessageCancelClick(v);
                }else {
                    NotificationUtil.gotoSet(getContext());
                }
                dismiss();
            }
        });
        buttonMessageDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(messageOkClickListener!=null){
                    messageOkClickListener.onMessageOkClick(v);
                }else {
                    NotificationUtil.gotoSet(getContext());
                }
                dismiss();
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
        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);

    }
    public interface MessageOkClickListener{
         void onMessageOkClick(View view);
    }
    public interface MessageCancelClickListener{
         void onMessageCancelClick(View view);
    }
}
