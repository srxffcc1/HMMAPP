package com.health.second.weight;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.lib_ShapeView.view.ShapeTextView;
import com.health.second.R;
import com.healthy.library.base.BaseDialogFragment;

public class OrderTicketDialog extends BaseDialogFragment {

    private View view;
    private OnDialogClickListener onDialogClickListener;

    public void setOnDialogClickListener(OnDialogClickListener onDialogClickListener) {
        this.onDialogClickListener = onDialogClickListener;
    }

    public static OrderTicketDialog newInstance() {
        Bundle args = new Bundle();
        OrderTicketDialog fragment = new OrderTicketDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        window.setWindowAnimations(R.style.BottomDialogAnimation);
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.BOTTOM;
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.dimAmount = 0.5f;
            window.setAttributes(params);
        }
        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {

        }
        view = super.onCreateView(inflater, container, savedInstanceState);
        if (view == null) {
            view = inflater.inflate(R.layout.dialog_order_ticket, container, false);
            initView();
        }
        return view;
    }

    private void initView() {
        final ShapeTextView submitBtn = view.findViewById(R.id.submitBtn);
        new CountDownTimer(4 * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                submitBtn.setText(String.format("返回（%s）", millisUntilFinished / 1000));
            }

            public void onFinish() {
                submitBtn.setText("返回");
                this.cancel();
            }
        }.start();
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                onDialogClickListener.onDialogClick("");
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public interface OnDialogClickListener {
        void onDialogClick(String result);
    }
}
