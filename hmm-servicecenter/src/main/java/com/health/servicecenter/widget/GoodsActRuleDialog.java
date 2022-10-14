package com.health.servicecenter.widget;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.health.servicecenter.R;
import com.healthy.library.base.BaseDialogFragment;

public class GoodsActRuleDialog extends BaseDialogFragment {
    View dialogview = null;
    private String tranDetail;
    private int type = -1;
    private ImageView close;
    private ConstraintLayout toStoreTranLL;
    private ImageView toStoreTranTitleLeft;
    private TextView toStoreTranTitle;
    private TextView toStoreTranContent;
    private ConstraintLayout under;
    private TextView submitBtn;

    public GoodsActRuleDialog setMarkingType(int type) {
        this.type = type;
        return this;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (getContext() == null) {
            return super.onCreateDialog(savedInstanceState);
        }
        dialogview = LayoutInflater.from(getContext()).inflate(R.layout.service_dialog_actrule, null);
        initView(dialogview);
        buildView(dialogview);
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(dialogview);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消


        Window window = dialog.getWindow();
        if (window != null) {
            window.setWindowAnimations(R.style.BottomDialogAnimation);
            View decorView = window.getDecorView();
            decorView.setPadding(0, 0, 0, 0);
            decorView.setBackgroundResource(com.healthy.library.R.drawable.shape_dialog);
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.gravity = Gravity.BOTTOM;
        }

        return dialog;
    }

    private void buildView(View dialogview) {
        toStoreTranContent.setText(tranDetail);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    private void initView(View view) {

        close = (ImageView) view.findViewById(R.id.close);
        toStoreTranLL = (ConstraintLayout)view. findViewById(R.id.toStoreTranLL);
        toStoreTranTitleLeft = (ImageView) view.findViewById(R.id.toStoreTranTitleLeft);
        toStoreTranTitle = (TextView) view.findViewById(R.id.toStoreTranTitle);
        toStoreTranContent = (TextView) view.findViewById(R.id.toStoreTranContent);
        under = (ConstraintLayout) view.findViewById(R.id.under);
        submitBtn = (TextView) view.findViewById(R.id.submitBtn);
    }

    public static GoodsActRuleDialog newInstance() {

        Bundle args = new Bundle();
        GoodsActRuleDialog fragment = new GoodsActRuleDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public void setTranDetail(String tranDetail) {
        this.tranDetail = tranDetail;
    }
}
