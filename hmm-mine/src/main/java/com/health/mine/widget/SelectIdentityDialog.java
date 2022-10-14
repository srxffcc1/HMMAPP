package com.health.mine.widget;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.health.mine.R;
import com.health.mine.adapter.SelectBankCardAdapter;
import com.healthy.library.base.BaseDialogFragment;
import com.healthy.library.utils.TransformUtil;

import java.util.ArrayList;
import java.util.List;

public class SelectIdentityDialog extends BaseDialogFragment {

    private TextView tvChooseTimeTitle;
    private ImageView closeBtn;
    private TextView identityId;
    private TextView passport;
    private TextView certificate;
    private TextView homecoming;
    private TextView taiWan;
    private TextView police;
    private TextView soldier;
    private View view;
    private ContentListener contentListener;

    public static SelectIdentityDialog newInstance() {
        SelectIdentityDialog fragment = new SelectIdentityDialog();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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
            params.height = (int) TransformUtil.dp2px(getContext(), 420f);
            window.setAttributes(params);

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = super.onCreateView(inflater, container, savedInstanceState);
        if (view == null) {
            view = inflater.inflate(R.layout.dialog_select_identity_layout, container, false);
            initView();
        }
        return view;
    }

    private void initView() {
        tvChooseTimeTitle = view.findViewById(R.id.tv_choose_time_title);
        closeBtn = view.findViewById(R.id.closeBtn);
        identityId = view.findViewById(R.id.identityId);
        passport = view.findViewById(R.id.passport);
        certificate = view.findViewById(R.id.certificate);
        homecoming = view.findViewById(R.id.homecoming);
        taiWan = view.findViewById(R.id.taiWan);
        police = view.findViewById(R.id.police);
        soldier = view.findViewById(R.id.soldier);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        identityId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentListener.resultContent(identityId.getText().toString().trim());
                dismiss();
            }
        });
        identityId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentListener.resultContent(identityId.getText().toString().trim());
                dismiss();
            }
        });
        passport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentListener.resultContent(passport.getText().toString().trim());
                dismiss();
            }
        });
        certificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentListener.resultContent(certificate.getText().toString().trim());
                dismiss();
            }
        });
        homecoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentListener.resultContent(homecoming.getText().toString().trim());
                dismiss();
            }
        });
        taiWan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentListener.resultContent(taiWan.getText().toString().trim());
                dismiss();
            }
        });
        police.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentListener.resultContent(police.getText().toString().trim());
                dismiss();
            }
        });
        soldier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentListener.resultContent(soldier.getText().toString().trim());
                dismiss();
            }
        });
    }

    public interface ContentListener {
        void resultContent(String result);
    }

    public void setResultListener(ContentListener contentListener) {
        this.contentListener = contentListener;
    }

}
