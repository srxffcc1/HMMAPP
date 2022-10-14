package com.healthy.library.business;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
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

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.healthy.library.R;
import com.healthy.library.constant.SpKey;
import com.healthy.library.contract.MineContract;
import com.healthy.library.model.AdModel;
import com.healthy.library.model.CouponInfo;
import com.healthy.library.model.MineFans;
import com.healthy.library.model.OrderInfo;
import com.healthy.library.model.OrderNum;
import com.healthy.library.model.UserInfoModel;
import com.healthy.library.model.VipCard;
import com.healthy.library.model.VipInfo;
import com.healthy.library.presenter.AdPresenter;
import com.healthy.library.presenter.MinePresenter;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.StringUtils;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.CornerImageView;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.List;

import io.reactivex.rxjava3.disposables.Disposable;

//会员电子卡
public class ElcCardDialogView extends DialogFragment implements MineContract.View {


    private ConstraintLayout adDialogBg;
    private ConstraintLayout userLLTop;
    private CornerImageView userIcon;
    private TextView userName;
    private TextView userStatus;
    private TextView userPhone;
    private ImageView userPhoneZxing;
    private LinearLayout moneyLL;
    private LinearLayout llh1;
    private TextView hdye;
    private LinearLayout llh2;
    private TextView tvJf;
    private TextView merchantName;
    private LinearLayout llh3;
    private TextView tvYhq;
    private ImageView dialogCloseButtonTmp;
    private ImageView dialogCloseButton;
    private MinePresenter mPresenter;

    public static ElcCardDialogView newInstance() {

        Bundle args = new Bundle();
        ElcCardDialogView fragment = new ElcCardDialogView();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.index_elc_card_dialog, null);
        builder.setView(view);
        displayDialog(view);
        Dialog result = builder.create();
        result.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        changeAppBrightness(result.getWindow(), 255);
        return result;
    }

    public void changeAppBrightness(Window window, int brightness) {
        WindowManager.LayoutParams lp = window.getAttributes();
        if (brightness == -1) {
            lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
        } else {
            lp.screenBrightness = (brightness <= 0 ? 1 : brightness) / 255f;
        }
        window.setAttributes(lp);
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

    AdPresenter adPresenter;

    private void displayDialog(View view) {
        mPresenter = new MinePresenter(getActivity(), this);
        adDialogBg = (ConstraintLayout) view.findViewById(R.id.ad_dialog_bg);
        userLLTop = (ConstraintLayout) view.findViewById(R.id.userLLTop);
        userIcon = (CornerImageView) view.findViewById(R.id.userIcon);
        userName = (TextView) view.findViewById(R.id.userName);
        userStatus = (TextView) view.findViewById(R.id.userStatus);
        userPhone = (TextView) view.findViewById(R.id.userPhone);
        userPhoneZxing = (ImageView) view.findViewById(R.id.userPhoneZxing);
        moneyLL = (LinearLayout) view.findViewById(R.id.moneyLL);
        llh1 = (LinearLayout) view.findViewById(R.id.llh1);
        hdye = (TextView) view.findViewById(R.id.hdye);
        llh2 = (LinearLayout) view.findViewById(R.id.llh2);
        tvJf = (TextView) view.findViewById(R.id.tv_jf);
        merchantName = (TextView) view.findViewById(R.id.merchantName);
        llh3 = (LinearLayout) view.findViewById(R.id.llh3);
        tvYhq = (TextView) view.findViewById(R.id.tv_yhq);
        dialogCloseButtonTmp = (ImageView) view.findViewById(R.id.dialog_close_button_tmp);
        dialogCloseButton = (ImageView) view.findViewById(R.id.dialog_close_button);
        userPhone.setText(StringUtils.getPhoneHide(SpUtils.getValue(getActivity(), SpKey.PHONE)));
        Bitmap zixng1code = CodeUtils.encodeAsBitmap(SpUtils.getValue(getActivity(), SpKey.PHONE), CodeUtils.barcodeFormat, (int) TransformUtil.dp2px(getActivity(), 260), (int) TransformUtil.dp2px(getActivity(), 64));
        userPhoneZxing.setImageBitmap(zixng1code);
        dialogCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mPresenter.getUserInfo();
        mPresenter.getCouponInfo();

        try {
            if (SpUtils.getValue(getContext(), SpKey.CITYNAMEPARTNERNAME) != null) {
                merchantName.setText(SpUtils.getValue(getContext(), SpKey.CITYNAMEPARTNERNAME));
            } else {
                merchantName.setText("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    AdModel adModel;

    public ElcCardDialogView setAdModel(AdModel adModel) {
        this.adModel = adModel;
        return this;
    }

    private void routePass() {
        dismiss();
    }


    private void initView() {


    }

    @Override
    public void onGetUserInfoSuccess(UserInfoModel userInfoModel) {
        if (userInfoModel != null) {
            com.healthy.library.businessutil.GlideCopy.with(getActivity()).load(userInfoModel.getFaceUrl())
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_1_1_default)

                    .into(userIcon);
            userName.setText(userInfoModel.getNickname());
            userStatus.setText(userInfoModel.getDateContent());
        }
    }

    @Override
    public void onGetOrderInfoSuccess(OrderInfo orderInfo) {

    }

    @Override
    public void onGetUserFanSucess(MineFans mineFans) {

    }

    CouponInfo couponInfo;

    @Override
    public void onGetCouponSucess(CouponInfo mineFans) {
        if (couponInfo != null) {
            tvYhq.setText(couponInfo.couponCount + "");
        } else {
            tvYhq.setText("0");
        }
        mPresenter.getVipInfo(new SimpleHashMapBuilder<String, Object>());
    }

    @Override
    public void onGetOrderNumSuccess(OrderNum orderNum) {

    }

    @Override
    public void onVipInfoSuccess(VipInfo vipInfo) {
        if (vipInfo != null) {
            hdye.setText((vipInfo.yeTotal + "").split("\\.")[0]);
            tvJf.setText((vipInfo.jfTotal + "").split("\\.")[0]);
            tvYhq.setText(vipInfo.yhCount + "");
            if (couponInfo != null) {
                tvYhq.setText(vipInfo.yhCount + "");
            }
        }
        mPresenter.getVipCards(new SimpleHashMapBuilder<String, Object>());
    }

    @Override
    public void onVipCardSuccess(List<VipCard> vipCards) {
//        if (vipCards != null && vipCards.size() > 0) {
//            vipCardSize = true;
//            tvChuzhiCount.setText("储值（" + vipCards.size() + "张)");
//        } else {
//            vipCardSize = false;
//            tvChuzhiCount.setText("储值");
//        }
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
    public void getData() {

    }

    @Override
    public void showDataErr() {

    }
}
