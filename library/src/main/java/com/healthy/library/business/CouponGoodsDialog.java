package com.healthy.library.business;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.healthy.library.R;
import com.healthy.library.adapter.CardAdapter;
import com.healthy.library.base.BaseDialogFragment;
import com.healthy.library.constant.Functions;
import com.healthy.library.contract.CouponGoodsContract;
import com.healthy.library.model.ActVip;
import com.healthy.library.model.CouponInfoZ;
import com.healthy.library.net.RxLifecycleUtils;
import com.healthy.library.presenter.CouponGoodsPresenter;
import com.healthy.library.routes.DiscountRoutes;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.TransformUtil;
import com.hss01248.dialog.StyledDialog;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


//@Route(path = DiscountRoutes.DIS_CARDGOODS)
public class CouponGoodsDialog extends BaseDialogFragment implements CouponGoodsContract.View {


    private TextView tvChooseTimeTitle, couponTxt;
    private ImageView closeBtn;
    private LinearLayout cardActsLL;
    private LinearLayout cardLL;
    private LinearLayout cardActsP;
    String shopId;
    String merchantId;
    String memberId;
    String goodsId;
    String type;
    ActVip actVip;
    private Dialog loading;
    CouponGoodsPresenter couponGoodsPresenter;


    public static CouponGoodsDialog newInstance() {

        Bundle args = new Bundle();
        CouponGoodsDialog fragment = new CouponGoodsDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dis_coupon_good_dialog, null);
        builder.setView(view);
        displayDialog(view);
        Dialog result = builder.create();
        return result;
    }


    private void displayDialog(View view) {
        couponGoodsPresenter = new CouponGoodsPresenter(getContext(), this);
        tvChooseTimeTitle = (TextView) view.findViewById(R.id.tv_choose_time_title);
        couponTxt = (TextView) view.findViewById(R.id.couponTxt);
        closeBtn = (ImageView) view.findViewById(R.id.closeBtn);
        cardActsLL = (LinearLayout) view.findViewById(R.id.cardActsLL);
        cardLL = (LinearLayout) view.findViewById(R.id.cardLL);
        cardActsP = (LinearLayout) view.findViewById(R.id.cardActsP);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        StyledDialog.init(getContext());
        loading = StyledDialog.buildMdLoading().setForceWidthPercent(0.3f).show();
//        StatusLayout statusLayout = view.findViewById(R.id.empty);
//        statusLayout.updateStatus(StatusLayout.Status.STATUS_EMPTY);
        getData();

    }

    @Override
    public void getData() {
        showLoading();
        if ("1".equals(type)) {
            cardActsP.setVisibility(View.VISIBLE);
            if (actVip != null && actVip.PopInfo != null) {
                buildDiscountInGoods(cardActsLL, actVip);
            } else {
                cardActsP.setVisibility(View.GONE);
            }
            couponGoodsPresenter.getCouponList(new SimpleHashMapBuilder<String, Object>()
                    .puts(Functions.FUNCTION, "80001")
                    .puts("merchantId", merchantId)
                    .puts("shopId", shopId)
                    .puts("page", "1")
                    .puts("pageSize", "999")
                    .puts("memberId", memberId)
                    .puts("goodsId", goodsId)
            );
        } else {
            cardActsP.setVisibility(View.GONE);
            couponGoodsPresenter.getCouponList(new SimpleHashMapBuilder<String, Object>()
                    .puts(Functions.FUNCTION, "80002")
                    .puts("merchantId", merchantId)
                    .puts("shopId", shopId)
                    .puts("page", "1")
                    .puts("pageSize", "999")
                    .puts("memberId", memberId)
            );
        }
    }

    private void buildDiscountInGoods(LinearLayout cardActsLL, final ActVip actVip) {
        cardActsLL.removeAllViews();
        for (int i = 0; i < actVip.PopInfo.size(); i++) {
            final ActVip.PopInfo selPopInfo = actVip.PopInfo.get(i);
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.service_item_goodsdetail_discount_item, cardActsLL, false);
            TextView enoughTitle;
            ImageView discountMore;
            TextView enoughContext;
            enoughTitle = (TextView) view.findViewById(R.id.enoughTitle);
            discountMore = (ImageView) view.findViewById(R.id.discountMore);
            enoughContext = (TextView) view.findViewById(R.id.enoughContext);
            if (selPopInfo.PopLabelName != null && !TextUtils.isEmpty(selPopInfo.PopLabelName)) {
                enoughTitle.setVisibility(View.VISIBLE);
                enoughTitle.setText(selPopInfo.PopLabelName);
            } else {
                enoughTitle.setVisibility(View.GONE);
            }
            enoughContext.setText(selPopInfo.PopDesc);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ARouter.getInstance()
                            .build(DiscountRoutes.DIS_DISCOUNTLIST)
                            .withString("appID", actVip.AppID)
                            .withString("popNo", selPopInfo.PopNo)
                            .withString("departID", actVip.DepartID)
                            .navigation();
                }
            });
            cardActsLL.addView(view);
        }
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
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);

    }

    private void initView() {


    }

    @Override
    public void sucessGetCouponList(List<CouponInfoZ> result) {
        showContent();
        if (result.size() > 0) {
            cardLL.removeAllViews();
            couponTxt.setVisibility(View.VISIBLE);
        } else {
            couponTxt.setVisibility(View.GONE);
        }
        for (int i = 0; i < result.size(); i++) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.dis_item_card, cardLL, false);
            CardAdapter.CardViewHolder cardViewHolder = new CardAdapter.CardViewHolder(view);
            buildCardInGoods(cardViewHolder, result.get(i));
            cardLL.addView(view);
        }
        StyledDialog.dismiss(loading);
    }

    @Override
    public void onSucessReceivedCoupon(String msg) {
        getData();
        if (msg.contains("成功")) {
            showToast("领取成功");
        } else {
            showToast(msg);

        }
    }


    private void buildCardInGoods(CardAdapter.CardViewHolder helper, final CouponInfoZ item) {
        helper.cardParent.setBackgroundResource(R.drawable.shape_packcener_item_bg);
        helper.cardLeftLL.setBackgroundResource(R.drawable.packcenter_left_bg);
        helper.cardRightBottom.setVisibility(View.GONE);
        ConstraintLayout.LayoutParams cardParentlayoutParams = (ConstraintLayout.LayoutParams) helper.cardParent.getLayoutParams();
        cardParentlayoutParams.height = (int) TransformUtil.dp2px(getContext(), 70);
        helper.cardParent.setLayoutParams(cardParentlayoutParams);
        ConstraintLayout.LayoutParams cardLeftLLlayoutParams = (ConstraintLayout.LayoutParams) helper.cardLeftLL.getLayoutParams();
        cardLeftLLlayoutParams.width = (int) TransformUtil.dp2px(getContext(), 100);
        helper.cardLeftLL.setLayoutParams(cardLeftLLlayoutParams);
        helper.cardButton.setVisibility(View.VISIBLE);
        helper.cardButton.setBackgroundResource(R.drawable.shape_pack_btn_go);
        helper.cardButton.setTextColor(Color.parseColor("#FFFFFF"));
        helper.checkInOrder.setVisibility(View.GONE);
        helper.goodsLL.setVisibility(View.GONE);
        //helper.cardMoney.setText(item.getCouponQuantity());
        helper.cardLimit.setText(item.getCouponUseName());
        helper.cardFlag.setText(item.getCouponTypeName());
        helper.ivEmptyStock2.setVisibility(View.GONE);


        helper.cardMoneyName.setText(item.getRequirement());

        if (item.availableCount > 0) {//说明有可用了
            helper.cardButton.setVisibility(View.VISIBLE);
            helper.cardButton.setBackgroundResource(R.drawable.shape_pack_btn);
            helper.cardButton.setTextColor(Color.parseColor("#FA3C5A"));
            helper.cardButton.setText("可用商品");
            helper.ivEmptyStock2.setVisibility(View.VISIBLE);
            helper.cardButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ARouter.getInstance().build(DiscountRoutes.DIS_PUBLICCOUPON)
                            .withString("cardId", item.getCouponId())
                            .withString("cardName", item.getCouponNormalName())
                            .withString("time", item.getTimeValidity())
                            .navigation();
                }
            });
        } else {
            helper.cardButton.setVisibility(View.VISIBLE);
            helper.cardButton.setBackgroundResource(R.drawable.shape_pack_btn_go);
            helper.cardButton.setTextColor(Color.parseColor("#FFFFFF"));
            if (item.isCanReceive()) {
                helper.cardButton.setText("立即领取");
                helper.cardButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loading = StyledDialog.buildMdLoading().setForceWidthPercent(0.3f).show();
                        couponGoodsPresenter.receivedCoupon(item.getActivityId(), item.getCouponId(), memberId);
                    }
                });
            } else {
                helper.cardButton.setVisibility(View.VISIBLE);
                helper.cardButton.setBackgroundResource(R.drawable.shape_pack_btn);
                helper.cardButton.setTextColor(Color.parseColor("#FA3C5A"));
                helper.cardButton.setText("已领完");
            }
        }
        helper.cardTime.setText(item.getTimeValidity());

        helper.cardMoney.setText(FormatUtils.moneyKeep2Decimals(item.getCouponDenomination()));
        if (item.getCouponDenomination().length() > 4) {
            helper.cardMoney.setTextSize(24);
        } else if (item.getCouponDenomination().length() > 3) {
            helper.cardMoney.setTextSize(27);
        } else if (item.getCouponDenomination().length() > 2) {
            helper.cardMoney.setTextSize(34);
        } else if (item.getCouponDenomination().length() > 1) {
            helper.cardMoney.setTextSize(34);
        } else {
            helper.cardMoney.setTextSize(34);
        }

    }


    @Override
    public void showLoading() {

    }

    @Override
    public void showToast(CharSequence msg) {
        Toast roast = Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT);
        if (cantoast) {
            cantoast = false;
            //System.out.println("展示Toast");
            roast.show();
            /*new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    cantoast = true;
                }
            }, 2000);*/
            changeStatus(2000);
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
    public void showDataErr() {

    }

    public void setData(String shopId, String memberId, String goodsId, String type, String merchantId, ActVip actVip) {
        this.shopId = shopId;
        this.merchantId = merchantId;
        this.memberId = memberId;
        this.goodsId = goodsId;
        this.type = type;
        this.actVip = actVip;
    }

    private void changeStatus(long period) {
        Observable.intervalRange(0,1,0, period, TimeUnit.MILLISECONDS, Schedulers.io())
                .to(RxLifecycleUtils.bindLifecycle(this))
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull Long aLong) { }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        cantoast = true;
                    }
                });
    }
}
