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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.health.tencentlive.R;
import com.health.tencentlive.contract.ChatRoomContract;
import com.health.tencentlive.contract.LiveGiftContract;
import com.health.tencentlive.model.Interaction;
import com.health.tencentlive.model.InteractionDetail;
import com.health.tencentlive.model.RedGift;
import com.health.tencentlive.presenter.ChatRoomPresenter;
import com.health.tencentlive.presenter.LiveGiftPresenter;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.model.AnchormanInfo;
import com.healthy.library.model.ChatRoomConfigure;
import com.healthy.library.model.CouponInfoZ;
import com.healthy.library.model.GoodsBasketCell;
import com.healthy.library.model.GoodsDetail;
import com.healthy.library.model.LiveRoomDecoration;
import com.healthy.library.model.MessageSendCode;
import com.healthy.library.model.OnLineNum;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.FrameActivityManager;
import com.healthy.library.widget.CornerImageView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.disposables.Disposable;


public class LiveRedEnvelopesGiftDialog extends DialogFragment implements LiveGiftContract.View, ChatRoomContract.View {

    private ConstraintLayout topView;
    private LinearLayout successCouponLayout;
    private LinearLayout successGoodsLayout;
    private TextView couponMoney;
    private TextView couponType;
    private TextView giftTips;
    private TextView goodsTitle;
    private TextView goodsSpace;
    private LinearLayout unSuccessfulLayout;
    private ImageView commit;
    private CornerImageView goodsImg;
    private RedGift redGift;
    private int type;
    private String shopId;
    private OnClickListener onClickListener;
    private LiveGiftPresenter liveGiftPresenter;
    private ImageView topImg;
    private ChatRoomPresenter chatRoomPresenter;

    public LiveRedEnvelopesGiftDialog setClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        return this;
    }

    public LiveRedEnvelopesGiftDialog setType(int type) {
        this.type = type;
        return this;
    }

    public LiveRedEnvelopesGiftDialog setShopId(String shopId) {
        this.shopId = shopId;
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

    public LiveRedEnvelopesGiftDialog setDetail(RedGift data) {
        this.redGift = data;
        return this;
    }

    public static LiveRedEnvelopesGiftDialog newInstance() {

        Bundle args = new Bundle();
        LiveRedEnvelopesGiftDialog fragment = new LiveRedEnvelopesGiftDialog();
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
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_live_red_envelopes_gift_layout, null);
        builder.setView(view);
        displayDialog(view);
        Dialog result = builder.create();
        return result;
    }

    private void displayDialog(View view) {

        topView = (ConstraintLayout) view.findViewById(R.id.topView);
        topImg = view.findViewById(R.id.topImg);
        successCouponLayout = (LinearLayout) view.findViewById(R.id.successCouponLayout);
        successGoodsLayout = (LinearLayout) view.findViewById(R.id.successGoodsLayout);
        couponMoney = (TextView) view.findViewById(R.id.couponMoney);
        couponType = (TextView) view.findViewById(R.id.couponType);
        giftTips = (TextView) view.findViewById(R.id.giftTips);
        unSuccessfulLayout = (LinearLayout) view.findViewById(R.id.unSuccessfulLayout);
        commit = (ImageView) view.findViewById(R.id.commit);
        goodsImg = (CornerImageView) view.findViewById(R.id.goodsImg);
        goodsTitle = (TextView) view.findViewById(R.id.goodsTitle);
        goodsSpace = (TextView) view.findViewById(R.id.goodsSpace);
        liveGiftPresenter = new LiveGiftPresenter(getContext(), this);
        chatRoomPresenter = new ChatRoomPresenter(getContext(), this);
        initData();
    }

    private void initData() {
        if (type == 1) {
            topImg.setImageResource(R.drawable.live_red_envelopes_red_bg);
            unSuccessfulLayout.setVisibility(View.GONE);
            giftTips.setVisibility(View.VISIBLE);
            if (redGift.itemType == 1) {
                successCouponLayout.setVisibility(View.VISIBLE);
                successGoodsLayout.setVisibility(View.GONE);
                giftTips.setText("注：奖品已放入【我的】-【优惠券】");
                commit.setImageResource(R.drawable.live_red_envelopes_red_btn);
                liveGiftPresenter.getCouponInfo(new SimpleHashMapBuilder<String, Object>().puts("couponId", redGift.itemRealId + ""));
            } else if (redGift.itemType == 2) {
                commit.setImageResource(R.drawable.live_red_envelopes_red2_btn);
                successCouponLayout.setVisibility(View.GONE);
                successGoodsLayout.setVisibility(View.VISIBLE);
                giftTips.setText("注：点击【确认奖品】，进入【确认订单】页面选择发货方式");
                if (redGift.detail != null) {
                    buildGoods(redGift.detail);
                }
            } else {

            }
        } else {
            topImg.setImageResource(R.drawable.live_red_envelopes_blue_bg);
            unSuccessfulLayout.setVisibility(View.VISIBLE);
            successCouponLayout.setVisibility(View.GONE);
            successGoodsLayout.setVisibility(View.GONE);
            giftTips.setVisibility(View.GONE);
            commit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
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

    @Override
    public void getSuccessGoodsInfo(GoodsDetail result) {
        if (result != null) {

        }
    }

    @Override
    public void getSuccessCouponInfo(final CouponInfoZ result) {
        if (result != null) {
            couponMoney.setText(FormatUtils.moneyKeep2Decimals(result.getCouponDenomination()));
            couponType.setText(result.getCouponNormalName());
            commit.setImageResource(R.drawable.live_red_envelopes_red_btn);
            commit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chatRoomPresenter.addGift(new SimpleHashMapBuilder<String, Object>().puts("liveBenefitId", redGift.benefitId), result.getCouponNormalName());
                }
            });
        }
    }

    private void buildGoods(final GoodsDetail goodsDetail) {
        com.healthy.library.businessutil.GlideCopy.with(getContext())
                .load(goodsDetail.headImage)
                
                .placeholder(R.drawable.img_1_1_default)
                .error(R.drawable.img_1_1_default)
                .into(goodsImg);
        goodsTitle.setText(goodsDetail.goodsTitle);
        goodsSpace.setText(goodsDetail.goodsSpecStr);
        //跳下单
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (redGift.detail.availableInventory <= 0) {//已抢光
                    Toast.makeText(getContext(), "当前奖品已抢光", Toast.LENGTH_SHORT).show();
                    dismiss();
                    return;
                }
                GoodsBasketCell.GoodsMarketing goodsMarketing = new GoodsBasketCell.GoodsMarketing(null);
                goodsMarketing.availableInventory = redGift.detail.availableInventory;
                goodsMarketing.mapMarketingGoodsId = "";
                goodsMarketing.marketingType = "-5";
                goodsMarketing.id = "";
                goodsMarketing.pointsPrice = 0;
                GoodsBasketCell goodsBasketCell = new GoodsBasketCell(goodsDetail.platformPrice, goodsDetail.platformPrice,
                        goodsDetail.platformPrice,
                        goodsDetail.goodsType,
                        "0",
                        "0", null);
                goodsBasketCell.goodsSpecDesc = goodsDetail.goodsSpecStr;
                goodsBasketCell.goodsStock = redGift.detail.availableInventory;
                goodsBasketCell.goodsMarketingDTO = goodsMarketing;
                goodsBasketCell.mchId = redGift.merchantId;
                goodsBasketCell.goodsId = goodsDetail.goodsId;
                try {
                    goodsBasketCell.setGoodsSpecId(goodsDetail.goodsChildId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                goodsBasketCell.goodsTitle = goodsDetail.goodsTitle;
                goodsBasketCell.goodsImage = goodsDetail.headImage;
                goodsBasketCell.setGoodsQuantity(1);
                goodsBasketCell.shopIdList = goodsDetail.shopIds;
                goodsBasketCell.goodsShopId = "";
                List<GoodsBasketCell> list = new ArrayList<>();
                list.add(goodsBasketCell);

                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_ORDER)
                        .withString("visitShopId", shopId)
                        .withString("token", null)
                        .withString("course_id", null)
                        .withString("liveStatus", null)
                        .withString("live_anchor", null)
                        .withString("live_course", null)
                        .withObject("goodsbasketlist", list)
                        .withString("goodsMarketingType", "-5")
                        .withString("winType", "3")
                        .withString("winId", redGift.winId)
                        .navigation();

                dismiss();
            }
        });
    }

    @Override
    public void getChatRoomConfigureSuccess(ChatRoomConfigure result) {

    }

    @Override
    public void setChatRoomInfoSuccess() {

    }

    @Override
    public void sendTxtMessageSuccess(MessageSendCode result) {

    }

    @Override
    public void sendCustomerTxtMessageSuccess(MessageSendCode result) {

    }

    @Override
    public void getLiveRoomMappingSuccess(LiveRoomDecoration result) {

    }

    @Override
    public void getLiveRoomBannerSuccess(LiveRoomDecoration result) {

    }

    @Override
    public void onSucessGetHost(AnchormanInfo anchormanInfo) {

    }

    @Override
    public void onSuccessGetFansNum(String result) {

    }

    @Override
    public void onSuccessGetLookNum(OnLineNum result) {

    }

    @Override
    public void onSuccessGetInteractionList(List<Interaction> result) {

    }

    @Override
    public void onSuccessGetInteractionDetail(InteractionDetail result) {

    }

    @Override
    public void onSuccessAddHelp(String result) {

    }

    @Override
    public void onSuccessGetRedGift(RedGift result) {

    }

    @Override
    public void onSuccessAddGift(String result, String couponName) {
        if (result.contains("成功")) {
            Toast.makeText(FrameActivityManager.instance().topActivity(), String.format("您的奖品%s已发放到我的-优惠券", couponName), Toast.LENGTH_SHORT).show();
            dismiss();
        }
    }

    public interface OnClickListener {
        void onClick();

        void onDisMiss();
    }

}
