package com.healthy.library.widget;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.healthy.library.R;
import com.healthy.library.base.BaseDialogFragment;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.contract.CardPackContract;
import com.healthy.library.model.CouponInfoZ;
import com.healthy.library.presenter.CardPickPresenter;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.TransformUtil;
import com.hss01248.dialog.StyledDialog;

import java.util.List;

import io.reactivex.rxjava3.disposables.Disposable;

public class CardPackDialog extends BaseDialogFragment implements CardPackContract.View {
    private ConstraintLayout packMain;
    private ImageView packTopIcon;
    private Space packTopIconTmp;
    private ScrollView packLL;
    private LinearLayout packLLContent;
    private ImageView packEndButton;
    private ImageView closeBtn;
    private ImageView packTopTitle;
    private LinearLayout packLLP;
    private String packId;
    private boolean hasInsert=true;
    private boolean isVote=false;

    public CardPackDialog setDetailId(String detailId) {
        this.detailId = detailId;
        return this;
    }

    private String detailId;
    private CardPickPresenter cardPickPresenter;
    private Dialog loading;

    public static CardPackDialog newInstance() {
        Bundle args = new Bundle();
        CardPackDialog fragment = new CardPackDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dis_cardpack_dialog, null);
        cardPickPresenter = new CardPickPresenter(getContext(), this);
        builder.setView(view);
        displayDialog(view);
        Dialog result = builder.create();
        return result;
    }

    private void displayDialog(View view) {
        packTopTitle = (ImageView) view.findViewById(R.id.packTopTitle);
        packLLP = (LinearLayout) view.findViewById(R.id.packLLP);
        packMain = (ConstraintLayout) view.findViewById(R.id.packMain);
        packTopIcon = (ImageView) view.findViewById(R.id.packTopIcon);
        packTopIconTmp = (Space) view.findViewById(R.id.packTopIconTmp);
        packLL = (ScrollView) view.findViewById(R.id.packLL);
        packLLContent = (LinearLayout) view.findViewById(R.id.packLLContent);
        packEndButton = (ImageView) view.findViewById(R.id.packEndButton);
        closeBtn = (ImageView) view.findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!hasInsert&&isVote){
                    ARouter.getInstance()
                            .build(ServiceRoutes.SERVICE_COUPON_PEOPLE)
                            .withString("memberCouponId", detailId)
                            .navigation();
                }
                dismiss();
            }
        });
        packEndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!hasInsert&&isVote){
                    ARouter.getInstance()
                            .build(ServiceRoutes.SERVICE_COUPON_PEOPLE)
                            .withString("memberCouponId", detailId)
                            .navigation();
                }
                dismiss();
            }
        });
        try {
            loading = StyledDialog.buildMdLoading().setForceWidthPercent(0.3f).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        getData();
    }


    @Override
    public void onStart() {
        super.onStart();
        //设置背景半透明
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
//        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);

    }

    public void setPackId(String packId) {
        this.packId = packId;
    }

    public String getPackId() {
        return packId;
    }

    @Override
    public void getData() {
        if (!TextUtils.isEmpty(packId) & packId != null) {
            cardPickPresenter.getCouponList(new SimpleHashMapBuilder<String, Object>().puts("activityId", packId));
        }
        if (!TextUtils.isEmpty(detailId) & detailId != null) {
            cardPickPresenter.getCouponDetail(new SimpleHashMapBuilder<String, Object>().puts("memberCouponId", detailId));
            cardPickPresenter.checkInsert(new SimpleHashMapBuilder<>());
        }
    }

    @Override
    public void onSucessGetCouponList(List<CouponInfoZ> couponInfoZList) {
        StyledDialog.dismiss(loading);
        if (couponInfoZList != null && couponInfoZList.size() > 0) {
            packLLContent.removeAllViews();
            ViewGroup.LayoutParams lp = packLL.getLayoutParams();
            if (couponInfoZList.size() > 2) {
                lp.height = (int) TransformUtil.dp2px(this.getContext(), 200f);
            } else {
                lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            }
            packLL.setLayoutParams(lp);
            if(getActivity()==null){
                dismiss();
                return;
            }
            for (int i = 0; i < couponInfoZList.size(); i++) {
                CouponInfoZ couponInfoZ = couponInfoZList.get(i);
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.dis_item_cardpack_dialog, packLLContent, false);
                TextView cardName;
                TextView cardMoney;
                TextView cardTitle;
                TextView cardTime;
                cardMoney = (TextView) view.findViewById(R.id.cardMoney);
                cardName = (TextView) view.findViewById(R.id.cardName);
                cardTitle = (TextView) view.findViewById(R.id.cardTitle);
                cardTime = (TextView) view.findViewById(R.id.cardTime);
                cardMoney.setText(FormatUtils.moneyKeep2Decimals(couponInfoZ.getCouponDenomination()));
                if (couponInfoZ.getCouponDenomination().length() > 4) {
                    cardMoney.setTextSize(24);
                } else if (couponInfoZ.getCouponDenomination().length() > 3) {
                    cardMoney.setTextSize(27);
                } else if (couponInfoZ.getCouponDenomination().length() > 2) {
                    cardMoney.setTextSize(34);
                } else if (couponInfoZ.getCouponDenomination().length() > 1) {
                    cardMoney.setTextSize(34);
                } else {
                    cardMoney.setTextSize(34);
                }
                cardName.setText(couponInfoZ.getCouponUseName());

                cardTitle.setText(couponInfoZ.getCouponNormalName());
                if (couponInfoZ.usableStartTime != null && couponInfoZ.usableEndTime != null) {
                    cardTime.setText(couponInfoZ.usableStartTime.split(" ")[0] + "-" + couponInfoZ.usableEndTime.split(" ")[0]);
                } else {
                    cardTime.setText("");
                }
                packLLContent.addView(view);
            }
        } else {

        }
    }

    @Override
    public void onSucessGetCoupon(CouponInfoZ adModels) {
        StyledDialog.dismiss(loading);
        if(getActivity()==null){
            dismiss();
            return;
        }
        if (adModels != null) {
            if(adModels.isShareGift==1){
                packTopTitle.setImageResource(R.drawable.pack_head_title3);
            }
            if(adModels.isVoteGift==1){
                isVote=true;
                packTopTitle.setImageResource(R.drawable.pack_head_title4);
                packEndButton.setImageResource(R.drawable.pack_end_button2);
            }
            packLLContent.removeAllViews();
            CouponInfoZ couponInfoZ = adModels;
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.dis_item_cardpack_dialog, packLLContent, false);
            TextView cardMoney;
            TextView cardTitle;
            TextView cardTime;
            TextView cardName;
            cardName = (TextView) view.findViewById(R.id.cardName);
            cardMoney = (TextView) view.findViewById(R.id.cardMoney);
            cardTitle = (TextView) view.findViewById(R.id.cardTitle);
            cardTime = (TextView) view.findViewById(R.id.cardTime);
            cardMoney.setText(FormatUtils.moneyKeep2Decimals(couponInfoZ.getCouponDenomination()));
            if (couponInfoZ.getCouponDenomination().length() > 4) {
                cardMoney.setTextSize(24);
            } else if (couponInfoZ.getCouponDenomination().length() > 3) {
                cardMoney.setTextSize(27);
            } else if (couponInfoZ.getCouponDenomination().length() > 2) {
                cardMoney.setTextSize(34);
            } else if (couponInfoZ.getCouponDenomination().length() > 1) {
                cardMoney.setTextSize(34);
            } else {
                cardMoney.setTextSize(34);
            }
            cardName.setText(couponInfoZ.getCouponUseName());
            cardTitle.setText(couponInfoZ.getCouponNormalName());
            if (couponInfoZ.usableStartTime != null && couponInfoZ.usableEndTime != null) {
                cardTime.setText(couponInfoZ.usableStartTime.split(" ")[0] + "-" + couponInfoZ.usableEndTime.split(" ")[0]);
            } else {
                cardTime.setText("");
            }
            packLLContent.addView(view);
        }
    }

    @Override
    public void onSucessGetInsert(Boolean hasinsert) {
        this.hasInsert=hasinsert;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showToast(CharSequence msg) {

    }

    @Override
    public void showNetErr() {

    }

    @Override
    public void onRequestStart(Disposable disposable) {

    }

    @Override
    public void showContent() {

    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void onRequestFinish() {

    }

    @Override
    public void showDataErr() {

    }
}
