package com.health.tencentlive.weight;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.health.tencentlive.R;
import com.health.tencentlive.model.JackpotList;
import com.healthy.library.utils.FormatUtils;

import java.util.List;


public class LiveHelpMarketingTipsDialog extends DialogFragment {

    private LinearLayout topView;
    private TextView activityDate;
    private TextView activityDis;
    private TextView activityContent;
    private TextView activitySend;
    private ImageView closeImg;
    private List<JackpotList> result;

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        try {
            super.show(manager, tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LiveHelpMarketingTipsDialog setDetail(List<JackpotList> result) {
        this.result = result;
        return this;
    }

    public static LiveHelpMarketingTipsDialog newInstance() {

        Bundle args = new Bundle();
        LiveHelpMarketingTipsDialog fragment = new LiveHelpMarketingTipsDialog();
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
        params.dimAmount = 0.5f;
        window.setAttributes(params);
        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_live_help_marketing_tips_layout, null);
        builder.setView(view);
        displayDialog(view);
        Dialog result = builder.create();
        return result;
    }

    private void displayDialog(View view) {
        topView = (LinearLayout) view.findViewById(R.id.topView);
        activityDate = (TextView) view.findViewById(R.id.activityDate);
        activityDis = (TextView) view.findViewById(R.id.activityDis);
        activityContent = (TextView) view.findViewById(R.id.activityContent);
        activitySend = (TextView) view.findViewById(R.id.activitySend);
        closeImg = (ImageView) view.findViewById(R.id.closeImg);
        initData();
    }

    private void initData() {
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        if (result != null) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < result.size(); i++) {
                JackpotList jackpotList = result.get(i);
                sb.append(jackpotList.name);
                if (jackpotList.helpWinCouponList != null && jackpotList.helpWinCouponList.size() > 0) {
                    for (int j = 0; j < jackpotList.helpWinCouponList.size(); j++) {
                        sb.append(String.format("%s券%s张",
                                //FormatUtils.moneyKeep2Decimals(jackpotList.helpWinCouponList.get(j).detail.basic.getCouponDenomination()),
                                jackpotList.helpWinCouponList.get(j).detail.basic.getCouponNormalName(),
                                jackpotList.helpWinCouponList.get(j).num));
                        if (j == jackpotList.helpWinCouponList.size() - 1) {
                            sb.append("；");
                        }
                    }
                }
                if (jackpotList.helpWinGoodsList != null && jackpotList.helpWinGoodsList.size() > 0) {
                    for (int j = 0; j < jackpotList.helpWinGoodsList.size(); j++) {
                        if (jackpotList.helpWinGoodsList.get(j).detail!=null) {
                            sb.append(String.format("%s商品%s件",
                                    FormatUtils.moneyKeep2Decimals(jackpotList.helpWinGoodsList.get(j).detail.getGoodsTitle()),
                                    jackpotList.helpWinGoodsList.get(j).num));
                        }
                        if (j == jackpotList.helpWinGoodsList.size() - 1) {
                            sb.append("；");
                        }
                    }
                }
                if (jackpotList.helpCouponList != null && jackpotList.helpCouponList.size() > 0) {
                    for (int j = 0; j < jackpotList.helpCouponList.size(); j++) {
                        sb.append(String.format("%s券%s张",
                                //FormatUtils.moneyKeep2Decimals(jackpotList.helpCouponList.get(j).detail.basic.getCouponDenomination()),
                                jackpotList.helpCouponList.get(j).detail.basic.getCouponNormalName(),
                                jackpotList.helpCouponList.get(j).num));
                        if (j == jackpotList.helpCouponList.size() - 1) {
                            sb.append("；");
                        }
                    }
                }
                if (jackpotList.helpGoodsList != null && jackpotList.helpGoodsList.size() > 0) {
                    for (int j = 0; j < jackpotList.helpGoodsList.size(); j++) {
                        if (jackpotList.helpGoodsList.get(j).detail!=null) {
                            sb.append(String.format("%s商品%s件",
                                    FormatUtils.moneyKeep2Decimals(jackpotList.helpGoodsList.get(j).detail.getGoodsTitle()),
                                    jackpotList.helpGoodsList.get(j).num));
                        }
                        if (j == jackpotList.helpGoodsList.size() - 1) {
                            sb.append("；");
                        }
                    }
                }
                sb.append("\n");
            }
            activityContent.setText(sb.toString());
        } else {
            activityContent.setText("");
        }
    }


}
