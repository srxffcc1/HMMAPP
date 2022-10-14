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

import com.google.gson.internal.$Gson$Types;
import com.health.servicecenter.R;
import com.healthy.library.model.GoodsTran;
import com.healthy.library.base.BaseDialogFragment;

public class GoodsTranDialog extends BaseDialogFragment {
    View dialogview = null;
    private GoodsTran tranDetail;
    private ImageView close;
    private ConstraintLayout toStoreTranLL;
    private ImageView toStoreTranTitleLeft;
    private TextView toStoreTranTitle;
    private TextView toStoreTranContent;
    private ConstraintLayout toHomeTranLL;
    private ImageView toHomeTranTitleLeft;
    private TextView toHomeTranTitle;
    private TextView toHomeTranContent;
    private ConstraintLayout toBackTranLL;
    private ImageView toBackTranTitleLeft;
    private TextView toBackTranTitle;
    private TextView toBackTranContent;
    private ConstraintLayout under;
    private TextView submitBtn;
    private int type = -1;

    public GoodsTranDialog setMarkingType(int type) {
        this.type = type;
        return this;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (getContext() == null) {
            return super.onCreateDialog(savedInstanceState);
        }
        dialogview = LayoutInflater.from(getContext()).inflate(R.layout.service_dialog_tran, null);
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
        if (tranDetail != null) {
            toHomeTranContent.setText(tranDetail.goodsPostageFeeWIthShopDTOS.get(1).content);
            if (type == 1) {
                toBackTranTitle.setVisibility(View.GONE);
                toBackTranContent.setVisibility(View.GONE);
                toBackTranTitleLeft.setVisibility(View.GONE);
            } else {
                toBackTranTitle.setVisibility(View.GONE);
                toBackTranContent.setVisibility(View.GONE);
                toBackTranTitleLeft.setVisibility(View.GONE);
                if (tranDetail.goodsPostageFeeWIthShopDTOS.size() > 2) {
                    toBackTranTitle.setVisibility(View.VISIBLE);
                    toBackTranContent.setVisibility(View.VISIBLE);
                    toBackTranTitleLeft.setVisibility(View.VISIBLE);
                    toBackTranTitle.setText(tranDetail.goodsPostageFeeWIthShopDTOS.get(2).title);
                    toBackTranContent.setText(tranDetail.goodsPostageFeeWIthShopDTOS.get(2).content);
                }
            }

        }
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
        toStoreTranLL = (ConstraintLayout) view.findViewById(R.id.toStoreTranLL);
        toStoreTranTitleLeft = (ImageView) view.findViewById(R.id.toStoreTranTitleLeft);
        toStoreTranTitle = (TextView) view.findViewById(R.id.toStoreTranTitle);
        toStoreTranContent = (TextView) view.findViewById(R.id.toStoreTranContent);
        toHomeTranLL = (ConstraintLayout) view.findViewById(R.id.toHomeTranLL);
        toHomeTranTitleLeft = (ImageView) view.findViewById(R.id.toHomeTranTitleLeft);
        toHomeTranTitle = (TextView) view.findViewById(R.id.toHomeTranTitle);
        toHomeTranContent = (TextView) view.findViewById(R.id.toHomeTranContent);
        toBackTranLL = (ConstraintLayout) view.findViewById(R.id.toBackTranLL);
        toBackTranTitleLeft = (ImageView) view.findViewById(R.id.toBackTranTitleLeft);
        toBackTranTitle = (TextView) view.findViewById(R.id.toBackTranTitle);
        toBackTranContent = (TextView) view.findViewById(R.id.toBackTranContent);
        under = (ConstraintLayout) view.findViewById(R.id.under);
        submitBtn = (TextView) view.findViewById(R.id.submitBtn);
    }

    public static GoodsTranDialog newInstance() {

        Bundle args = new Bundle();
        GoodsTranDialog fragment = new GoodsTranDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public void setTranDetail(GoodsTran tranDetail) {
        this.tranDetail = tranDetail;
    }

    public GoodsTran getTranDetail() {
        return tranDetail;
    }
}
