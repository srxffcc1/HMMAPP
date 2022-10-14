package com.health.discount.widget;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.health.discount.R;
import com.health.discount.adapter.GiftAdapter;
import com.health.discount.contract.GiftDialogContract;
import com.health.discount.presenter.GiftDialogPresenter;
import com.healthy.library.LibApplication;
import com.healthy.library.base.BaseDialogFragment;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.businessutil.ChannelUtil;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.Coupon;
import com.healthy.library.routes.DiscountRoutes;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.SpanUtils;
import com.healthy.library.utils.TransformUtil;
import com.hss01248.dialog.StyledDialog;
import com.hyb.library.ScreenUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.net.shoot.sharetracesdk.AppData;
import cn.net.shoot.sharetracesdk.ShareTrace;
import cn.net.shoot.sharetracesdk.ShareTraceInstallListener;
import io.reactivex.rxjava3.disposables.Disposable;

public class GiftDialog extends BaseDialogFragment implements GiftDialogContract.View {

    private Dialog.OnDismissListener mlistener;
    GiftDialogPresenter giftDialogPresenter;
    private ConstraintLayout giftParentLLZ;
    private LinearLayout cardParentUnder;
    private LinearLayout cardParent;
    private LinearLayout cardParentList;
    private ImageView dialogCloseButton;
    private ConstraintLayout giftParentLL;


    public GiftDialog setOnDismissListener(Dialog.OnDismissListener listener) {
        mlistener = listener;
        return this;
    }

    boolean isInit = false;
    GiftAdapter giftAdapterTop;
    List<Coupon> mServiceGift = new ArrayList<>();
    List<Coupon> mGoodsGift = new ArrayList<>();
    List<Coupon> giftTop = new ArrayList<>();
    private Dialog loading;
    public String mreferralCode;

    public static GiftDialog newInstance() {

        Bundle args = new Bundle();
        GiftDialog fragment = new GiftDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog result = null;
        if (getActivity() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            final View view = inflater.inflate(R.layout.dis_dialog_giftplus, null);
            builder.setView(view);
            giftDialogPresenter = new GiftDialogPresenter(getActivity(), this);
            displayDialog(view);
            ShareTrace.getInstallTrace(new ShareTraceInstallListener() {
                @Override
                public void onInstall(AppData data) {//这里说明获取到分享下载的导购码
                    if (data != null) {
                        String result = data.toString();
                        if (result == null) {
                            return;
                        }
                        if (result.contains("paramsData") && result.contains("referral_code")) {//这里说明获取到分享下载的导购码格式正确
                            Map<String, String> map = new HashMap<>();
                            String[] resultarray = data.getParamsData().split("&");
                            for (int i = 0; i < resultarray.length; i++) {
                                map.put(resultarray[i].split("=")[0], resultarray[i].split("=").length > 1 ? resultarray[i].split("=")[1] : "");
                            }
                            String referralCode = map.get("referral_code");
                            mreferralCode = referralCode;
                            String birthday = SpUtils.getValue(getActivity(), SpKey.USER_BIRTHDAY);
                            String partnerId = SpUtils.getValue(LibApplication.getAppContext(), SpKey.CHOSE_PARTNERID);
                            String ytbAppId = LocUtil.getytbAppId();
                            String departId = LocUtil.getHmmDepartId();
                            if (!ChannelUtil.isRealRelease()) {
                                Toast.makeText(LibApplication.getAppContext(), "获得了导购码:" + referralCode, Toast.LENGTH_SHORT).show();
                            }
                            giftDialogPresenter.getCouponList(new SimpleHashMapBuilder<String, Object>()
                                    .puts("referralCode", referralCode)
                                    .puts("birthday", birthday)
                                    .puts("ytbAppId", ytbAppId)
                                    .puts("departId", departId)
                                    .puts("partnerId",partnerId)
                                    .puts("memberId", new String(Base64.decode(SpUtils.getValue(getActivity(), SpKey.USER_ID).getBytes(), Base64.DEFAULT))));
                        } else {
                            String referralCode = null;
                            String birthday = SpUtils.getValue(getActivity(), SpKey.USER_BIRTHDAY);
                            String partnerId = SpUtils.getValue(LibApplication.getAppContext(), SpKey.CHOSE_PARTNERID);
                            String ytbAppId = LocUtil.getytbAppId();
                            String departId = LocUtil.getHmmDepartId();
                            giftDialogPresenter.getCouponList(new SimpleHashMapBuilder<String, Object>()
                                    .puts("referralCode", referralCode)
                                    .puts("birthday", birthday)
                                    .puts("ytbAppId", ytbAppId)
                                    .puts("departId", departId)
                                    .puts("partnerId",partnerId)
                                    .puts("memberId", new String(Base64.decode(SpUtils.getValue(getActivity(), SpKey.USER_ID).getBytes(), Base64.DEFAULT))));
                        }
                    }
                }

                @Override
                public void onError(int code, String msg) {
                    String referralCode = null;
                    String birthday = SpUtils.getValue(getActivity(), SpKey.USER_BIRTHDAY);
                    String ytbAppId = LocUtil.getytbAppId();
                    String departId = LocUtil.getHmmDepartId();
                    giftDialogPresenter.getCouponList(new SimpleHashMapBuilder<String, Object>()
                            .puts("referralCode", referralCode)
                            .puts("birthday", birthday)
                            .puts("ytbAppId", ytbAppId)
                            .puts("departId", departId)
                            .puts("memberId", new String(Base64.decode(SpUtils.getValue(getActivity(), SpKey.USER_ID).getBytes(), Base64.DEFAULT))));
                }
            });

            result = builder.create();
            result.setOnDismissListener(mlistener);
        }

        return result;
    }

    private void displayDialog(View view) {
        {//只能手动全屏了
            giftParentLLZ = (ConstraintLayout) view.findViewById(R.id.giftParentLLZ);
            ViewGroup.LayoutParams params = giftParentLLZ.getLayoutParams();
            params.height = (int) (ScreenUtils.getAppScreenHeight(LibApplication.getAppContext()));
            giftParentLLZ.setLayoutParams(params);
        }
        giftParentLL = (ConstraintLayout) view.findViewById(R.id.giftParentLL);
        giftParentLLZ = (ConstraintLayout) view.findViewById(R.id.giftParentLLZ);
        cardParentUnder = (LinearLayout) view.findViewById(R.id.cardParentUnder);
        cardParent = (LinearLayout) view.findViewById(R.id.cardParent);
        cardParentList = (LinearLayout) view.findViewById(R.id.cardParentList);
        dialogCloseButton = (ImageView) view.findViewById(R.id.dialog_close_button);
        dialogCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        giftParentLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passToCoupon();
            }
        });
        StyledDialog.init(getContext());
        loading = StyledDialog.buildMdLoading().setForceWidthPercent(0.3f).show();

    }

    @Override
    public void onStart() {
        super.onStart();
        //设置背景半透明
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);

    }

    private void initView() {


    }

    @Override
    public void onSuccessGetCouponList(List<Coupon> coupons) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                StyledDialog.dismiss(loading);
            }
        }, 500);
        if (coupons != null) {
            giftTop.clear();
            mServiceGift.clear();
            mGoodsGift.clear();
            for (Coupon coupon : coupons) {
                if("0".equals(coupon.GoodsType)){
                    mServiceGift.add(coupon);
                }
                if("1".equals(coupon.GoodsType)){
                    mGoodsGift.add(coupon);
                }
            }
            giftTop.addAll(mServiceGift);
            giftTop.addAll(mGoodsGift);
            buildData();
        }

    }

    private void buildData() {
        cardParentList.removeAllViews();
        int needSize = giftTop.size() > 3 ? 3 : giftTop.size();
        {//只能手动全屏了
            ViewGroup.LayoutParams params = giftParentLL.getLayoutParams();
            params.height = (int) TransformUtil.dp2px(LibApplication.getAppContext(), 164 + (82 * needSize));
            giftParentLL.setLayoutParams(params);
        }
        for (int i = 0; i < giftTop.size(); i++) {
            if (getActivity() == null) {
                dismiss();
                return;
            }
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.dis_item_dialog_coupon_plus, cardParentList, false);
            Coupon coupon = giftTop.get(i);
            TextView cardMoney;
            TextView cardCount;
            TextView cardContent;
            TextView cardButton;
            TextView cardTime;
            cardMoney = (TextView) view.findViewById(R.id.cardMoney);
            cardCount = (TextView) view.findViewById(R.id.cardCount);
            cardContent = (TextView) view.findViewById(R.id.cardContent);
            cardButton = (TextView) view.findViewById(R.id.cardButton);
            cardTime = (TextView) view.findViewById(R.id.cardTime);
            if ("0".equals(coupon.GoodsType)) {
                cardContent.setText(SpanUtils.getBuilder(getActivity(), "")
                        .append(" ")
                        .setResourceId(R.drawable.giftcardbg_plus_flag1)
                        .append(" ")
                        .append(coupon.GoodsName).create());
            } else {
                cardContent.setText(SpanUtils.getBuilder(getActivity(), "")
                        .append(coupon.GoodsName)
                        .append(" ")
                        .append(" ")
                        .setResourceId(R.drawable.giftcardbg_plus_flag2)
                        .create());
            }
            cardMoney.setText(coupon.Price);
            cardCount.setText(coupon.getGbTypeName());
            cardTime.setText("有效期至：" + coupon.EndDate);
            cardButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    passToCoupon();
                }
            });
            cardParentList.addView(view);
        }
    }

    private void passToCoupon() {
        dismiss();
        ARouter.getInstance().build(DiscountRoutes.MINE_NEW_USER_GIFT)
                .navigation();
    }

    @Override
    public void onSucessGiftCheck() {
        dismiss();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showToast(CharSequence msg) {
        try {
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            dismiss();
            e.printStackTrace();
        }
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
