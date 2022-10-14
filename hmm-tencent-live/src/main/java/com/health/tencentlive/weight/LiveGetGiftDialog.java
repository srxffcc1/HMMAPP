package com.health.tencentlive.weight;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import com.health.tencentlive.model.Interaction;
import com.health.tencentlive.model.InteractionDetail;
import com.health.tencentlive.model.RedGift;
import com.health.tencentlive.presenter.ChatRoomPresenter;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.AnchormanInfo;
import com.healthy.library.model.ChatRoomConfigure;
import com.healthy.library.model.GoodsBasketCell;
import com.healthy.library.model.LiveRoomDecoration;
import com.healthy.library.model.MessageSendCode;
import com.healthy.library.model.OnLineNum;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.DateUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.StatusLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.disposables.Disposable;


public class LiveGetGiftDialog extends DialogFragment implements ChatRoomContract.View {

    private ImageView topImg;
    private ImageView topLightImg;
    private ImageView closeImg;
    private ConstraintLayout centerLayout;
    private TextView topTitle;
    private LinearLayout goodsLayout;
    private CornerImageView goodsImg;
    private TextView goodsTitle;
    private TextView goodsSpace;
    private LinearLayout conditionTitle;
    private TextView condition;
    private TextView totalNum;
    private LinearLayout conditionLayout;
    private ConstraintLayout followLayout;
    private TextView task1Title;
    private TextView task1Btn;
    private ConstraintLayout commentLayout;
    private TextView task2Title;
    private TextView task2Btn;
    private ConstraintLayout buyLayout;
    private TextView task3Title;
    private TextView task3Btn;
    private ConstraintLayout allLayout;
    private TextView task4Title;
    private TextView task4Btn;
    private TextView timeTitle;
    private LinearLayout timeLayout;
    private TextView txtHour;
    private TextView txtMin;
    private TextView txtSec;
    private ScrollView scroll;
    private LinearLayout peopleLayout;
    private ImageView btn;
    private ImageView bgImg;
    private StatusLayout layout_status;

    private InteractionDetail interactionDetail;
    private CountDownTimer countDownTimer;
    private String winId;
    private String shopId;
    private ChatRoomPresenter chatRoomPresenter;
    private String id;
    private String courseId;
    private OnClickListener onClickListener;

    public LiveGetGiftDialog setClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
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

    public LiveGetGiftDialog setId(String id, String courseId) {
        this.id = id;
        this.courseId = courseId;
        return this;
    }

    public LiveGetGiftDialog setShopId(String shopId) {
        this.shopId = shopId;
        return this;
    }

    public void refresh(InteractionDetail data) {
        if (id.equals(data.id)) {
            this.interactionDetail = data;
            settingData();
        }
    }

    public static LiveGetGiftDialog newInstance() {

        Bundle args = new Bundle();
        LiveGetGiftDialog fragment = new LiveGetGiftDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        //设置背景半透明
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.dimAmount = 0.5f;
        params.width = dm.widthPixels - (dm.widthPixels / 6);
        window.setGravity(Gravity.CENTER);
        window.setAttributes(params);
        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_live_get_gift_layout, null);
        builder.setView(view);
        displayDialog(view);
        Dialog result = builder.create();
        return result;
    }

    private void displayDialog(View view) {
        topImg = (ImageView) view.findViewById(R.id.topImg);
        topLightImg = (ImageView) view.findViewById(R.id.topLightImg);
        closeImg = (ImageView) view.findViewById(R.id.closeImg);
        centerLayout = (ConstraintLayout) view.findViewById(R.id.centerLayout);
        topTitle = (TextView) view.findViewById(R.id.topTitle);
        goodsLayout = (LinearLayout) view.findViewById(R.id.goodsLayout);
        goodsImg = (CornerImageView) view.findViewById(R.id.goodsImg);
        goodsTitle = (TextView) view.findViewById(R.id.goodsTitle);
        goodsSpace = (TextView) view.findViewById(R.id.goodsSpace);
        conditionTitle = (LinearLayout) view.findViewById(R.id.conditionTitle);
        condition = (TextView) view.findViewById(R.id.condition);
        totalNum = (TextView) view.findViewById(R.id.totalNum);
        conditionLayout = (LinearLayout) view.findViewById(R.id.conditionLayout);
        followLayout = (ConstraintLayout) view.findViewById(R.id.followLayout);
        task1Title = (TextView) view.findViewById(R.id.task1Title);
        task1Btn = (TextView) view.findViewById(R.id.task1Btn);
        commentLayout = (ConstraintLayout) view.findViewById(R.id.commentLayout);
        task2Title = (TextView) view.findViewById(R.id.task2Title);
        task2Btn = (TextView) view.findViewById(R.id.task2Btn);
        buyLayout = (ConstraintLayout) view.findViewById(R.id.buyLayout);
        task3Title = (TextView) view.findViewById(R.id.task3Title);
        task3Btn = (TextView) view.findViewById(R.id.task3Btn);
        allLayout = (ConstraintLayout) view.findViewById(R.id.allLayout);
        task4Title = (TextView) view.findViewById(R.id.task4Title);
        task4Btn = (TextView) view.findViewById(R.id.task4Btn);
        timeTitle = (TextView) view.findViewById(R.id.timeTitle);
        timeLayout = (LinearLayout) view.findViewById(R.id.timeLayout);
        txtHour = (TextView) view.findViewById(R.id.txtHour);
        txtMin = (TextView) view.findViewById(R.id.txtMin);
        txtSec = (TextView) view.findViewById(R.id.txtSec);
        scroll = (ScrollView) view.findViewById(R.id.scroll);
        peopleLayout = (LinearLayout) view.findViewById(R.id.peopleLayout);
        btn = (ImageView) view.findViewById(R.id.btn);
        bgImg = (ImageView) view.findViewById(R.id.bgImg);
        layout_status = (StatusLayout) view.findViewById(R.id.layout_status);
        layout_status.updateStatus(StatusLayout.Status.STATUS_LOADING);
        initView();
    }

    private void initView() {
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        chatRoomPresenter = new ChatRoomPresenter(getContext(), this);
        getData();

    }

    private void settingData() {
        if (interactionDetail.status == 0 || interactionDetail.status == 1) {//未开奖||开奖中
            peopleLayout.setVisibility(View.VISIBLE);
            conditionLayout.setVisibility(View.GONE);
            totalNum.setVisibility(View.GONE);
            topTitle.setVisibility(View.VISIBLE);
            topTitle.setText(String.format("共%s份，先到先得哦，%s人已参与", interactionDetail.numberOfWinners, interactionDetail.joinCount));
            condition.setText("参与条件");
            topTitle.setTextSize(12f);
            buildConditionList();//条件列表
            buildGoodsList("#A75816");//商品信息
            if (interactionDetail.status == 0) {
                timeTitle.setText("开奖倒计时");
                buildTime(interactionDetail.lotteryTime);
            } else {
                timeTitle.setText("开奖中");
            }
            boolean finished = false;//是否已经完成所有条件
            for (int i = 0; i < interactionDetail.conditionList.size(); i++) {
                if (interactionDetail.conditionList.get(i).finished) {
                    finished = true;
                } else {
                    finished = false;
                }
            }
            if (interactionDetail.status == 0) {
                if (finished) {
                    btn.setImageResource(R.drawable.live_gift_btn2);
                } else {
                    btn.setImageResource(R.drawable.live_gift_btn1);
                }
            } else {
                btn.setImageResource(R.drawable.live_gift_btn2);
            }
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        } else if (interactionDetail.status == 2) {//已开奖
            if (interactionDetail.join) {
                topTitle.setVisibility(View.VISIBLE);
                topTitle.setTextSize(16f);
                if (interactionDetail.win) {//已中奖
                    btn.setVisibility(View.VISIBLE);
                    btn.setImageResource(R.drawable.live_gift_btn4);
                    topTitle.setText("恭喜您，获得本轮大奖");
                    buildRosterList("#A75816");
                    buildGoodsList("#A75816");
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            buildOrder();
                        }
                    });
                } else {
                    btn.setVisibility(View.GONE);
                    topImg.setImageResource(R.drawable.live_gift_dialog_top_fail);
                    bgImg.setImageResource(R.drawable.live_gift_dialog_bg_fail);
                    topLightImg.setImageResource(R.drawable.live_gift_dialog_top_light_fail);
                    topTitle.setText("很遗憾，本轮您未中奖");
                    buildRosterList("#0A4AB3");
                    buildGoodsList("#A75816");
                }
            } else {
                btn.setImageResource(R.drawable.live_gift_btn3);
                topTitle.setVisibility(View.INVISIBLE);
                conditionLayout.setVisibility(View.GONE);
                buildRosterList("#A75816");//中奖人列表
                buildGoodsList("#A75816");//商品信息
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });
            }
        }
    }

    private void buildOrder() {
        InteractionDetail.Goods goodsDetail = interactionDetail.goodsList.get(0);
        if (goodsDetail.availableInventory <= 0) {//说明已抢光
            Toast.makeText(getContext(), "当前奖品已抢光", Toast.LENGTH_SHORT).show();
            return;
        }
        GoodsBasketCell.GoodsMarketing goodsMarketing = new GoodsBasketCell.GoodsMarketing(null);
        goodsMarketing.availableInventory = goodsDetail.availableInventory;
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
        goodsBasketCell.goodsStock = goodsDetail.availableInventory;
        goodsBasketCell.goodsMarketingDTO = goodsMarketing;
        goodsBasketCell.mchId = SpUtils.getValue(getContext(), SpKey.CHOSE_MC);
        goodsBasketCell.goodsId = goodsDetail.goodsId;
        try {
            goodsBasketCell.setGoodsSpecId(goodsDetail.goodsChildId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        goodsBasketCell.goodsTitle = goodsDetail.goodsTitle;
        goodsBasketCell.goodsImage = goodsDetail.goodsImage;
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
                .withString("winType", "2")
                .withString("winId", winId)
                .navigation();

        dismiss();
    }

    private void buildGoodsList(String color) {
        goodsLayout.removeAllViews();
        for (int i = 0; i < interactionDetail.goodsList.size(); i++) {
            if (getContext() == null) {
                return;
            }
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_live_gift_dialog_goods_layout, goodsLayout, false);
            CornerImageView goodsImg = view.findViewById(R.id.goodsImg);
            TextView goodsTitle = view.findViewById(R.id.goodsTitle);
            TextView goodsSpace = view.findViewById(R.id.goodsSpace);
            com.healthy.library.businessutil.GlideCopy.with(getContext())
                    .load(interactionDetail.goodsList.get(i).goodsImage)
                    
                    .placeholder(R.drawable.img_1_1_default)
                    .error(R.drawable.img_1_1_default)
                    .into(goodsImg);
            goodsTitle.setText(interactionDetail.goodsList.get(i).goodsTitle);
            goodsSpace.setText(interactionDetail.goodsList.get(i).goodsSpecStr);
            goodsTitle.setTextColor(Color.parseColor(color));
            goodsSpace.setTextColor(Color.parseColor(color));
            if (interactionDetail.status == 0) {
                view.setBackgroundResource(R.drawable.shape_live_gift_dialog_item);
            } else {
                if (interactionDetail.win) {
                    view.setBackgroundResource(R.drawable.shape_live_gift_dialog_item);
                } else {
                    view.setBackgroundResource(R.drawable.shape_live_gift_dialog_item_fail);
                }
            }
            goodsLayout.addView(view);
        }
    }

    private void buildConditionList() {
        if (interactionDetail.conditionList != null && interactionDetail.conditionList.size() > 0) {//说明有限制条件
            followLayout.setVisibility(View.GONE);
            commentLayout.setVisibility(View.GONE);
            buyLayout.setVisibility(View.GONE);
            allLayout.setVisibility(View.GONE);
            conditionLayout.setVisibility(View.VISIBLE);
            for (int i = 0; i < interactionDetail.conditionList.size(); i++) {
                if (interactionDetail.conditionList.get(i).conditionId == 1) {//关注主播
                    followLayout.setVisibility(View.VISIBLE);
                    task1Title.setText("关注主播");
                    if (interactionDetail.conditionList.get(i).finished) {
                        task1Btn.setText("已完成");
                        task2Btn.setTextColor(Color.parseColor("#666666"));
                        task1Btn.setBackgroundResource(R.drawable.shape_live_task_finish);
                    } else {
                        task1Btn.setText("未完成");
                        task2Btn.setTextColor(Color.parseColor("#A75816"));
                        task1Btn.setBackgroundResource(R.drawable.shape_live_task_no_finish);
                    }
                } else if (interactionDetail.conditionList.get(i).conditionId == 2) {//指定评论
                    commentLayout.setVisibility(View.VISIBLE);
                    task2Title.setText("发送评论：" + interactionDetail.conditionList.get(i).content);
                    if (interactionDetail.conditionList.get(i).finished) {
                        task2Btn.setText("已完成");
                        task2Btn.setTextColor(Color.parseColor("#666666"));
                        task2Btn.setBackgroundResource(R.drawable.shape_live_task_finish);
                    } else {
                        task2Btn.setText("未完成");
                        task2Btn.setTextColor(Color.parseColor("#A75816"));
                        task2Btn.setBackgroundResource(R.drawable.shape_live_task_no_finish);
                        final String str = interactionDetail.conditionList.get(i).content;
                        task2Btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (onClickListener != null) {
                                    Toast.makeText(getContext(), "评论内容已复制", Toast.LENGTH_SHORT).show();
                                    onClickListener.onChart(str);
                                    dismiss();
                                }
                            }
                        });
                    }
                } else if (interactionDetail.conditionList.get(i).conditionId == 3) {//消费满额
                    buyLayout.setVisibility(View.VISIBLE);
                    task3Title.setText(String.format("直播间消费满%s元", interactionDetail.conditionList.get(i).content));
                    if (interactionDetail.conditionList.get(i).finished) {
                        task3Btn.setText("已完成");
                        task2Btn.setTextColor(Color.parseColor("#666666"));
                        task3Btn.setBackgroundResource(R.drawable.shape_live_task_finish);
                    } else {
                        task3Btn.setText("未完成");
                        task2Btn.setTextColor(Color.parseColor("#A75816"));
                        task3Btn.setBackgroundResource(R.drawable.shape_live_task_no_finish);
                    }
                }
            }
        } else {
            followLayout.setVisibility(View.GONE);
            commentLayout.setVisibility(View.GONE);
            buyLayout.setVisibility(View.GONE);
            allLayout.setVisibility(View.VISIBLE);
            conditionLayout.setVisibility(View.VISIBLE);
            task4Title.setText("直播间所有用户均可参与");
            task4Btn.setVisibility(View.GONE);
        }
    }

    private void buildRosterList(String color) {
        if (interactionDetail.rosterList == null && interactionDetail.rosterList.size() == 0) {
            centerLayout.setVisibility(View.INVISIBLE);
            return;
        }
        centerLayout.setVisibility(View.VISIBLE);
        peopleLayout.setVisibility(View.VISIBLE);
        conditionLayout.setVisibility(View.GONE);
        totalNum.setVisibility(View.VISIBLE);
        condition.setText("获奖名单");
        condition.setTextColor(Color.parseColor(color));
        totalNum.setTextColor(Color.parseColor(color));
        totalNum.setText(String.format("(共%s名)", interactionDetail.rosterList.size()));
        peopleLayout.removeAllViews();
        for (int i = 0; i < interactionDetail.rosterList.size(); i++) {
            if (getContext() == null) {
                return;
            }
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_live_gift_dialog_people_layout, peopleLayout, false);
            CornerImageView avatarImg = view.findViewById(R.id.avatarImg);
            TextView username = view.findViewById(R.id.username);
            String nickName = interactionDetail.rosterList.get(i).member.nickName;
            if (nickName.length() > 10) {
                nickName = nickName.substring(0, 9) + "...";
            }
            if (interactionDetail.rosterList.get(i).self) {
                winId = interactionDetail.rosterList.get(i).mapInteractiveRosterGoodsId;
                username.setTextColor(Color.parseColor("#1065D3"));
                username.setText(nickName + "(本人)");
            } else {
                username.setTextColor(Color.parseColor(color));
                username.setText(nickName);
            }
            com.healthy.library.businessutil.GlideCopy.with(getContext())
                    .load(interactionDetail.rosterList.get(i).member.faceUrl)
                    
                    .placeholder(R.drawable.img_avatar_default)
                    .error(R.drawable.img_avatar_default)
                    .into(avatarImg);
            peopleLayout.addView(view);
        }
    }

    private void buildTime(String lotteryTime) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date endTime = null;
        try {
            endTime = simpleDateFormat.parse(lotteryTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final long finalDesorg = endTime.getTime();
        long mSeconds = DateUtils.getDistanceSec(lotteryTime, simpleDateFormat.format(new Date()));
        if (mSeconds > 0) {
            new CountDownTimer(mSeconds, 1000) {
                public void onTick(long millisUntilFinished) {
                    String[] array = DateUtils.getDistanceTime(simpleDateFormat.format(new Date()), DateUtils.formatLongAll(finalDesorg + ""));
//                    LogUtils.e(array[1] + ":" + array[2] + ":" + array[3]);
                    txtHour.setText(array[1]);
                    txtMin.setText(array[2]);
                    txtSec.setText(array[3]);
                }

                public void onFinish() {
                    txtHour.setText("0");
                    txtMin.setText("0");
                    txtSec.setText("0");
                    getData();
                }
            }.start();
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
        chatRoomPresenter.getInteractionDetail(new SimpleHashMapBuilder<String, Object>().puts("interactiveTaskId", id)
                .puts("memberId", new String(Base64.decode(SpUtils.getValue(getContext(), SpKey.USER_ID).getBytes(), Base64.DEFAULT))).puts("courseId", courseId));
    }

    @Override
    public void showDataErr() {

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
        if (result != null) {
            this.interactionDetail = result;
            layout_status.updateStatus(StatusLayout.Status.STATUS_CONTENT);
            settingData();
        }
    }

    @Override
    public void onSuccessAddHelp(String result) {

    }

    @Override
    public void onSuccessGetRedGift(RedGift result) {

    }

    @Override
    public void onSuccessAddGift(String result, String couponName) {

    }

    public interface OnClickListener {
        void onChart(String chart);
    }
}
