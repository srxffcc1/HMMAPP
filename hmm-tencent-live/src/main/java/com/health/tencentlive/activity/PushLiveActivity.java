package com.health.tencentlive.activity;

import android.Manifest;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.health.tencentlive.R;
import com.health.tencentlive.adapter.PushLiveChatRoomAdapter;
import com.health.tencentlive.beautysettingkit.constant.BeautyConstants;
import com.health.tencentlive.beautysettingkit.model.BeautyInfo;
import com.health.tencentlive.beautysettingkit.model.ItemInfo;
import com.health.tencentlive.beautysettingkit.model.TabInfo;
import com.health.tencentlive.beautysettingkit.view.BeautyPanel;
import com.health.tencentlive.contract.ChatRoomContract;
import com.health.tencentlive.contract.LiveShoppingBagDialogContract;
import com.health.tencentlive.contract.PushLiveContract;
import com.health.tencentlive.impl.CameraPush;
import com.health.tencentlive.model.Interaction;
import com.health.tencentlive.model.InteractionDetail;
import com.health.tencentlive.model.JackpotList;
import com.health.tencentlive.model.PushModel;
import com.health.tencentlive.model.RedGift;
import com.health.tencentlive.presenter.ChatRoomPresenter;
import com.health.tencentlive.presenter.LiveShoppingBagDialogPresenter;
import com.health.tencentlive.presenter.PushLivePresenter;
import com.health.tencentlive.utils.Constants;
import com.health.tencentlive.utils.IMMessageMgr;
import com.health.tencentlive.weight.LiveErrorDialog;
import com.health.tencentlive.weight.LiveGetGiftDialog;
import com.health.tencentlive.weight.LiveHelpMarketingDialog;
import com.health.tencentlive.weight.LiveRedEnvelopesDialog;
import com.health.tencentlive.weight.LiveRedEnvelopesGiftDialog;
import com.health.tencentlive.weight.LiveShoppingBagDialog;
import com.healthy.library.LibApplication;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.builder.SimpleArrayListBuilder;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.business.SeckShareDialog;
import com.healthy.library.constant.Ids;
import com.healthy.library.constant.SpKey;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.interfaces.IsNeedShare;
import com.healthy.library.interfaces.IsNeedShareMini;
import com.healthy.library.interfaces.IsNoNeedCardDialog;
import com.healthy.library.interfaces.IsNoNeedPatchShow;
import com.healthy.library.model.AnchormanInfo;
import com.healthy.library.model.ChatMessage;
import com.healthy.library.model.ChatRoomConfigure;
import com.healthy.library.model.LiveRoomDecoration;
import com.healthy.library.model.LiveRoomInfo;
import com.healthy.library.model.LiveVideoGoods;
import com.healthy.library.model.MessageSendCode;
import com.healthy.library.model.OnLineNum;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.model.ShopDetailModel;
import com.healthy.library.routes.TencentLiveRoutes;
import com.healthy.library.utils.DateUtils;
import com.healthy.library.utils.DrawableUtils;
import com.healthy.library.utils.FastClickUtils;
import com.healthy.library.utils.ParseUtils;
import com.healthy.library.utils.PermissionUtils;
import com.healthy.library.utils.ScreenUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.MaxHeightRecyclerView;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;
import com.hyb.library.PreventKeyboardBlockUtil;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMGroupMemberChangeInfo;
import com.tencent.imsdk.v2.V2TIMGroupMemberInfo;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.rtmp.ITXLivePushListener;
import com.tencent.rtmp.TXLiveBase;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.TXLivePusher;
import com.tencent.rtmp.ui.TXCloudVideoView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Route(path = TencentLiveRoutes.LIVE_PUSH)
public class PushLiveActivity extends BaseLiveActivity implements IsNoNeedPatchShow, IsFitsSystemWindows, CameraPush,
        IMMessageMgr.Callback, IMMessageMgr.IMMessageListener, ChatRoomContract.View,
        PushLiveContract.View, LiveShoppingBagDialogContract.View, BaseAdapter.OnOutClickListener, IsNeedShare, IsNeedShareMini, IsNoNeedCardDialog {
    @Autowired
    String pushAddress;//????????????
    @Autowired
    String groupId;//????????????Id
    @Autowired
    String courseId;//??????ID
    @Autowired
    String anchormanId;//??????ID
    @Autowired
    String backImgUrl;//????????????

    List<String> activityIdList;
    private List<ChatMessage> totalMessageList = new ArrayList<>();//??????????????????
    private List<LiveVideoGoods> liveVideoGoods = null;
    private String sdkAppId = null;//IMAPPId
    private String userSig = null;//IMSIg
    private String accountId = null;//accountId
    private static final String TAG = "PushLiveLog";
    private TextView mTextNetBusyTips;   // ????????????Tips
    private ImageView changeCamera;
    private TXCloudVideoView pusherTxCloudView;
    private View viewStatus;
    private CornerImageView anchorImg;
    private ImageView topBannerImg;
    private TextView anchorName;
    private TextView liveTipsTxt;
    private TextView anchorFanceNum;
    private ImageView closeImg;
    private TextView shopCartNum;
    private LinearLayout mappingLeftLin;
    private LinearLayout mappingRgihtLin;
    private ImageView mappingImg;
    private ImageView liveShopingIcon;
    private ImageView liveBeautyIcon;
    private ImageView liveShareIcon;
    private RelativeLayout shopCartRel;
    private ImageView liveFriendLinkIcon;
    private EditText chatEdit;
    private ConstraintLayout editLayout;
    private MaxHeightRecyclerView chatRecycler;
    private ConstraintLayout popularityLayout;
    private ConstraintLayout anchormanLayout;
    private RelativeLayout headNowLL;
    private TextView groupNums;
    private LinearLayout addLayout;
    private TextView userNickName;
    private TextView chartContent;
    private CornerImageView head_icon1, head_icon2, head_icon3;
    private LinearLayout purseLayout;
    private ConstraintLayout helpLayout;
    private LinearLayout topTipsLayout;

    //??????????????????
    private int[] txArray = new int[]{R.drawable.tx01, R.drawable.tx02, R.drawable.tx03, R.drawable.tx04, R.drawable.tx05, R.drawable.tx06, R.drawable.tx07, R.drawable.tx08,
            R.drawable.tx09, R.drawable.tx10, R.drawable.tx11, R.drawable.tx12, R.drawable.tx13, R.drawable.tx14, R.drawable.tx15};
    private int mVideoResolution = TXLiveConstants.VIDEO_QUALITY_ULTRA_DEFINITION;  //?????????????????????1080P
    private int mQualityType = 7;//1080P
    private int mAudioChannels = 1;//?????????
    private int mAudioSample = 48000;//???????????????
    private boolean mIsPushing = false;//??????????????????
    private boolean mIsResume = false;//????????????resume
    private boolean mIsWaterMarkEnable = false;//????????????????????????
    private boolean mIsDebugInfo = false;//????????????????????????
    private boolean mIsMuteAudio = false;//??????????????????
    private boolean mIsPrivateMode = false;//?????????????????????
    private boolean mIsMirrorEnable = false;//?????????????????????????????????
    private boolean mIsFocusEnable = false;//????????????????????????
    private boolean mIsZoomEnable = false;//????????????????????????????????????
    private boolean mIsPureAudio = false;//???????????????????????????
    private boolean mIsEarMonitoringEnable = false;//???????????????????????????
    private boolean mIsHWAcc = false;//??????????????????????????????
    private boolean mFrontCamera = true;//????????????????????????????????????????????????????????????
    private boolean mIsEnableAdjustBitrate = true;//?????????????????????
    private boolean mIsFlashLight = false;//?????????????????????
    private boolean isSendMessage = false;//?????????????????????
    private boolean errIsShow = false;//???????????????
    private boolean isFinish = false;//??????????????????
    private int isDebug = 0;//?????????????????????
    private int historyLookNum = 0;//??????????????????????????????????????????????????????
    private int reconnection = 0;//????????????????????????????????????????????????????????????  3?????????????????????????????????
    private int pushLiveErrorCount = 0;//???????????????????????????   ????????????3???  ?????????????????????   ?????????????????????????????????
    private int mBeautyLevel = 5;                                   // ????????????
    private int mBeautyStyle = TXLiveConstants.BEAUTY_STYLE_SMOOTH; // ????????????
    private int mWhiteningLevel = 3;                                   // ????????????
    private int mRuddyLevel = 2;                                   // ????????????

    private TXCloudVideoView mPusherView;//??????view
    private TXLivePushConfig mLivePushConfig;//????????????
    private TXLivePusher mLivePusher;//Pusher??????
    private Bitmap mWaterMarkBitmap;
    private IMMessageMgr imMessageMgr;
    private BeautyPanel mBeautyControl;  // ????????????????????????
    private PushLiveChatRoomAdapter pushLiveChatRoomAdapter;
    private LiveShoppingBagDialog liveShoppingBagDialog;
    private ChatRoomPresenter chatRoomPresenter;
    private PushLivePresenter pushLivePresenter;
    private LiveShoppingBagDialogPresenter liveShoppingBagDialogPresenter;
    private LiveErrorDialog liveErrorDialog;
    private OnLivePusherCallback mOnLivePusherCallback;
    private Handler handler;
    private LiveRoomInfo mLiveRoomInfo;
    private LiveGetGiftDialog liveGetGiftDialog;//?????????dialog
    private LiveHelpMarketingDialog liveHelpMarketingDialog;//?????????dialog
    private boolean isSureHelp = false;//?????????????????????(?????????????????????????????????????????????????????????)
    private LiveRedEnvelopesDialog liveRedEnvelopesDialog;//????????????????????????
    private boolean isFirstChange = false;
    private boolean isFirstInit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_push_live;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        TXLiveBase.getInstance().setLicence(this, Ids.TENCENT_LIVE_LICENSE_URL, Ids.TENCENT_LIVE_LICENSE_KEY);
        chatRoomPresenter = new ChatRoomPresenter(this, this);
        pushLivePresenter = new PushLivePresenter(this, this);
        liveShoppingBagDialogPresenter = new LiveShoppingBagDialogPresenter(this, this);
        handler = new Handler();
        if (PermissionUtils.hasPermissions(mContext, mPermissions)) {
            initialize();
            pushLivePresenter.getLiveRoomInfo(new SimpleHashMapBuilder<String, Object>().puts("courseId", courseId));
            initChatRoomAdapter();
            checkNetWorkStatusAndAudio();
        } else {
            PermissionUtils.requestPermissions(this, RC_CAMERA, mPermissions);
        }
    }


    private void getChatRoomData() {
        chatRoomPresenter.getChatRoomConfigure(new SimpleHashMapBuilder<String, Object>().puts("tourist", "0"));
        chatRoomPresenter.getLiveRoomMapping(new SimpleHashMapBuilder<String, Object>().puts("anchormanId", anchormanId).puts("roomDecorationType", "1"), 1);
        chatRoomPresenter.getLiveRoomMapping(new SimpleHashMapBuilder<String, Object>().puts("anchormanId", anchormanId).puts("roomDecorationType", "2"), 2);
        chatRoomPresenter.getHost(new SimpleHashMapBuilder<String, Object>().puts("anchormanId", anchormanId));
        liveShoppingBagDialogPresenter.getLiveRoomGoodsList(new SimpleHashMapBuilder<String, Object>().puts("liveActivityIds", activityIdList));
        chatRoomPresenter.getFansNum(new SimpleHashMapBuilder<String, Object>()
                .puts("followedId", new String(Base64.decode(SpUtils.getValue(getApplicationContext(), SpKey.USER_ID).getBytes(), Base64.DEFAULT))));
        List list = getRandomNumList();
        if (list.size() > 0) {
            head_icon1.setImageResource(txArray[(int) list.get(0)]);
            head_icon2.setImageResource(txArray[(int) list.get(1)]);
            head_icon3.setImageResource(txArray[(int) list.get(2)]);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        pause();
        PreventKeyboardBlockUtil.getInstance().setContext(mActivity).unRegister();
    }

    @Override
    protected void onResume() {
        super.onResume();
        resume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                PreventKeyboardBlockUtil.getInstance().setContext(mActivity).setBtnView(chatEdit).register();
            }
        }, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPush();
        mPusherView.onDestroy();
        unInitPhoneListener();
        if (pushLivePresenter != null && mIsPushing) {//???????????????????????????????????????
            pushLivePresenter.stopPushLive(new SimpleHashMapBuilder<String, Object>().puts("courseId", courseId).puts("status", "3"));
        }
        if (liveErrorDialog != null) {
            liveErrorDialog = null;
        }
    }

    @Override
    public void getChatRoomConfigureSuccess(ChatRoomConfigure result) {
        if (result != null && result.account != null) {
            isSendMessage = true;
            chatRoomPresenter.setChatRoomInfo(new SimpleHashMapBuilder<String, Object>().puts("groupId", groupId).puts("accountId", result.account.accountId));
            accountId = result.account.accountId;
            if (result.sdkAppId != null && result.userSig != null) {
                sdkAppId = result.sdkAppId;
                userSig = result.userSig;
                //?????????IM
                imMessageMgr = new IMMessageMgr(this);
                imMessageMgr.setIMMessageListener(this);
                imMessageMgr.initialize(result.account.accountId, userSig
                        , Integer.parseInt(sdkAppId), this);
            }
        } else {//????????????????????????????????????????????????????????????  ????????????
            isSendMessage = false;
            try {
                CrashReport.postCatchedException(new Throwable("?????????????????????????????????  courseId==" + courseId + "   anchormanId==" + anchormanId
                        + "   memberId==" + new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT))));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setChatRoomInfoSuccess() {

    }

    @Override
    public void sendTxtMessageSuccess(MessageSendCode result) {
        if (result != null && result.ErrorCode == 0) {
            Log.e(TAG, "???????????????????????????");
            chatEdit.setText("");
            hideInput();
        } else {
            showToast("??????????????????");
            try {
                CrashReport.postCatchedException(new Throwable("??????????????????  courseId==" + courseId + "   anchormanId==" + anchormanId
                        + "   memberId==" + new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT))));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void sendCustomerTxtMessageSuccess(MessageSendCode result) {
        if (result != null && result.ErrorCode == 0) {
            Log.e(TAG, "???????????????????????????");
            chatEdit.setText("");
            hideInput();
        } else {
            showToast("??????????????????");
            try {
                CrashReport.postCatchedException(new Throwable("??????????????????  courseId==" + courseId + "   anchormanId==" + anchormanId
                        + "   memberId==" + new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT))));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void getLiveRoomMappingSuccess(LiveRoomDecoration result) {
        setLiveRoomMapping(result, mappingLeftLin, mappingRgihtLin);
    }

    @Override
    public void getLiveRoomBannerSuccess(LiveRoomDecoration result) {
        if (result != null) {
            if (result.roomDecorationType == 2) {
                if (result.iconDtoList != null && result.iconDtoList.size() > 0) {
                    if (!isDestroy(this)) {
                        topBannerImg.setVisibility(View.VISIBLE);
                        com.healthy.library.businessutil.GlideCopy.with(mContext)
                                .load(result.iconDtoList.get(0).iconPath)
                                .error(R.drawable.img_1_1_default)
                                .placeholder(R.drawable.img_1_1_default)

                                .into(new SimpleTarget<Drawable>() {
                                    @Override
                                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                        int swidth = ScreenUtils.getScreenWidth(mContext) - (int) TransformUtil.dp2px(mContext, 20);
                                        int height = (int) ((resource.getIntrinsicHeight() * 1.0 / resource.getIntrinsicWidth()) * swidth);
                                        ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) topBannerImg.getLayoutParams();
                                        height = swidth / 2;
                                        layoutParams.height = height;
                                        layoutParams.width = swidth;
                                        topBannerImg.setLayoutParams(layoutParams);
                                        com.healthy.library.businessutil.GlideCopy.with(topBannerImg.getContext()).load(resource).into(topBannerImg);
                                    }
                                });
                    }

                } else {
                    topBannerImg.setVisibility(View.GONE);
                }
            }
        } else {
            topBannerImg.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSucessGetHost(AnchormanInfo anchormanInfo) {
        if (anchormanInfo != null) {
            anchorName.setText(anchormanInfo.memberName);
            com.healthy.library.businessutil.GlideCopy.with(mContext)
                    .asBitmap()
                    .load(anchormanInfo.avatarUrl)
                    .placeholder(R.drawable.img_avatar_default)
                    .error(R.drawable.img_avatar_default)

                    .into(anchorImg);
        }
    }

    @Override
    public void onSuccessGetFansNum(String result) {
//        if (result != null && !TextUtils.isEmpty(result)) {
//            anchorFanceNum.setText(result + " ??????");
//        }
    }

    @Override
    public void onSuccessGetLookNum(OnLineNum result) {

    }

    @Override
    public void onSuccessGetInteractionList(final List<Interaction> result) {
        if (result != null && result.size() > 0) {
            purseLayout.setVisibility(View.VISIBLE);
            purseLayout.removeAllViews();
            int line = 0;
            if (result.size() > 3) {
                line = 3;
            } else {
                line = result.size();
            }
            for (int i = 0; i < line; i++) {
                View view = getLayoutInflater().inflate(R.layout.item_live_interaction_layout, mappingLeftLin, false);
                TextView purseTxt = (TextView) view.findViewById(R.id.purseTxt);
                final Interaction interaction = result.get(i);
                if (interaction != null) {
                    chatRoomPresenter.getInteractionDetail(new SimpleHashMapBuilder<String, Object>().puts("interactiveTaskId", interaction.id)
                            .puts("memberId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT))).puts("courseId", courseId));
                    if (interaction.status == 0) {//?????????
                        lotteryTime(interaction.lotteryTime, purseTxt);
                    } else if (interaction.status == 1) {//?????????
                        purseTxt.setText("?????????");
                    } else if (interaction.status == 2) {//?????????
                        purseTxt.setText("????????????");
                    }
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            liveGetGiftDialog = LiveGetGiftDialog.newInstance();
                            liveGetGiftDialog.setId(interaction.id, courseId);
                            liveGetGiftDialog.setShopId(SpUtils.getValue(mContext, SpKey.CHOSE_SHOP));
                            liveGetGiftDialog.setClickListener(new LiveGetGiftDialog.OnClickListener() {
                                @Override
                                public void onChart(String chart) {
                                    chatEdit.setText(chart);
                                }
                            });
                            liveGetGiftDialog.show(getSupportFragmentManager(), "");
                        }
                    });
                    purseLayout.addView(view);
                }
            }

        } else {
            purseLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSuccessGetInteractionDetail(final InteractionDetail result) {
        if (result != null) {
            if (liveGetGiftDialog != null) {
                liveGetGiftDialog.refresh(result);
            }
        }
    }

    @Override
    public void onSuccessAddHelp(String result) {

    }

    @Override
    public void onSuccessGetRedGift(final RedGift result) {
        if (liveRedEnvelopesDialog != null) {
            try {
                liveRedEnvelopesDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        LiveRedEnvelopesGiftDialog liveRedEnvelopesGiftDialog = LiveRedEnvelopesGiftDialog.newInstance();
        if (result != null) {
            liveRedEnvelopesGiftDialog.setDetail(result);
            liveRedEnvelopesGiftDialog.setType(1);
            liveRedEnvelopesGiftDialog.setShopId(mLiveRoomInfo.shopId);
        } else {
            liveRedEnvelopesGiftDialog.setType(2);
        }
        liveRedEnvelopesGiftDialog.show(getSupportFragmentManager(), "");
    }

    @Override
    public void onSuccessAddGift(String result, String couponName) {

    }

    @Override
    public void getSuccessLiveRoomInfo(LiveRoomInfo result) {
        if (result != null) {
            result.courseId = courseId;
            this.mLiveRoomInfo = result;
            activityIdList = result.activityIdList;
            isDebug = result.isDebug;
            Log.e(TAG, activityIdList.toString());
            if (!isFirstInit) {
                getChatRoomData();
                isFirstInit = true;
            }
            liveShoppingBagDialogPresenter.getLiveRoomGoodsList(new SimpleHashMapBuilder<String, Object>().puts("liveActivityIds", activityIdList));
            if (liveShoppingBagDialog != null) {
                liveShoppingBagDialog.changeList(activityIdList);
            }
            stitle = result.courseTitle;
            com.healthy.library.businessutil.GlideCopy.with(mContext)
                    .load(result.picUrl)
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            sBitmap = DrawableUtils.drawableToBitmap(resource);
                        }
                    });
            liveShoppingBagDialogPresenter.getJackpotList(new SimpleHashMapBuilder<String, Object>()
                    .puts("pageNum", "1")
                    .puts("pageSize", 100)
                    .puts("courseId", courseId)
                    .puts("memberId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT))), 0);
        }
    }

    @Override
    public void getSuccessAgainLive(String pushAddress, final String groupId, final String mCourseId) {
        if (groupId != null && pushAddress != null && mCourseId != null) {
            pushLiveErrorCount = 0;
            this.pushAddress = pushAddress;
            this.groupId = groupId;
            this.courseId = mCourseId;
            this.isFirstChange = false;
            this.isFirstInit = false;
            initialize();
            pushLivePresenter.getLiveRoomInfo(new SimpleHashMapBuilder<String, Object>().puts("courseId", courseId));
            initChatRoomAdapter();
        } else {
            pushLiveErrorCount++;
            if (liveErrorDialog != null) {
                liveErrorDialog.dismiss();
                errIsShow = false;
            }
            if (pushLiveErrorCount > 3) {//??????????????????????????? ???????????????????????????????????????????????????
                showToast("??????????????????????????????,??????????????????~");
                mIsPushing = true;
                isFinish = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        stopPush();
                    }
                }, 1000);
            } else {
                showToast("????????????????????????????????????,????????????????????????~");
                liveErrorDialog = LiveErrorDialog.newInstance();
                liveErrorDialog.setType(3);
                errIsShow = true;
                liveErrorDialog.show(getSupportFragmentManager(), "??????");
                liveErrorDialog.setClickListener(new LiveErrorDialog.OnErrClickListener() {
                    @Override
                    public void onClick(int type) {
                        if (type == 3) {
                            liveErrorDialog.dismiss();
                            pushLivePresenter.againLive(new SimpleHashMapBuilder<String, Object>().puts("courseId", courseId).puts("isDebug", isDebug));
                        }
                    }
                });
            }
            try {
                CrashReport.postCatchedException(new Throwable("?????????????????????????????????9145??????????????????,courseId==" + courseId + "   anchormanId==" + anchormanId
                        + "   memberId==" + new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID) != null ? SpUtils.getValue(mContext, SpKey.USER_ID).getBytes() : null, Base64.DEFAULT))));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void findViews() {
        super.findViews();
        mPusherView = (TXCloudVideoView) findViewById(R.id.pusher_tx_cloud_view);
        changeCamera = (ImageView) findViewById(R.id.changeCamera);
        pusherTxCloudView = (TXCloudVideoView) findViewById(R.id.pusher_tx_cloud_view);
        viewStatus = (View) findViewById(R.id.viewStatus);
        anchorImg = (CornerImageView) findViewById(R.id.anchorImg);
        topBannerImg = findViewById(R.id.topBannerImg);
        anchorName = (TextView) findViewById(R.id.anchorName);
        liveTipsTxt = (TextView) findViewById(R.id.liveTipsTxt);
        anchorFanceNum = (TextView) findViewById(R.id.anchorFanceNum);
        closeImg = (ImageView) findViewById(R.id.closeImg);
        mappingRgihtLin = (LinearLayout) findViewById(R.id.mappingRgihtLin);
        mappingLeftLin = (LinearLayout) findViewById(R.id.mappingLeftLin);
        mappingImg = (ImageView) findViewById(R.id.mappingImg);
        mappingImg = (ImageView) findViewById(R.id.mappingImg);
        liveShopingIcon = (ImageView) findViewById(R.id.live_shoping_icon);
        liveBeautyIcon = (ImageView) findViewById(R.id.live_beauty_icon);
        liveShareIcon = (ImageView) findViewById(R.id.live_share_icon);
        liveFriendLinkIcon = (ImageView) findViewById(R.id.live_friend_link_icon);
        chatEdit = (EditText) findViewById(R.id.chatEdit);
        editLayout = (ConstraintLayout) findViewById(R.id.editLayout);
        anchormanLayout = (ConstraintLayout) findViewById(R.id.anchormanLayout);
        chatRecycler = (MaxHeightRecyclerView) findViewById(R.id.chatRecycler);
        mBeautyControl = (BeautyPanel) findViewById(R.id.beauty_panel);
        popularityLayout = (ConstraintLayout) findViewById(R.id.popularityLayout);
        headNowLL = (RelativeLayout) findViewById(R.id.headNowLL);
        groupNums = (TextView) findViewById(R.id.groupNums);
        shopCartNum = (TextView) findViewById(R.id.shop_cart_num);
        shopCartRel = (RelativeLayout) findViewById(R.id.shop_cart_rel);
        head_icon1 = (CornerImageView) findViewById(R.id.head_icon1);
        head_icon2 = (CornerImageView) findViewById(R.id.head_icon2);
        head_icon3 = (CornerImageView) findViewById(R.id.head_icon3);
        addLayout = (LinearLayout) findViewById(R.id.addLayout);
        userNickName = (TextView) findViewById(R.id.userNickName);
        chartContent = (TextView) findViewById(R.id.chartContent);
        mTextNetBusyTips = findViewById(R.id.livepusher_tv_net_error_warning);
        purseLayout = (LinearLayout) findViewById(R.id.purseLayout);
        helpLayout = (ConstraintLayout) findViewById(R.id.helpLayout);
        topTipsLayout = (LinearLayout) findViewById(R.id.topTipsLayout);
        liveShareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showShareMini();
                if (!FastClickUtils.isFastClick()) {
                    SeckShareDialog seckShareDialog = SeckShareDialog.newInstance();
                    seckShareDialog.setActivityDialog(8, 0, mLiveRoomInfo);
                    seckShareDialog.setGroupId(groupId);
                    seckShareDialog.setExtraMap(new SimpleHashMapBuilder<String, String>()
                            .puts("scheme", "LiveStream")
                            .puts("courseId", courseId)
                            .puts("fromMemberId", new String(Base64.decode(SpUtils.getValue(LibApplication.getAppContext(), SpKey.USER_ID).getBytes(), Base64.DEFAULT)))//?????????????????????
                            .puts("liveShareType", "zbzl")//???????????? zbzl?????????????????????
                            .puts("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC))
                            .puts("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP))
                    );
                    seckShareDialog.show(getSupportFragmentManager(), "PushLiveShare");
                }
            }
        });
        editLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSendMessage) {
                    showInput(chatEdit);
                } else {
                    showToast("????????????????????????????????????????????????");
                }
            }
        });
        anchormanLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(TencentLiveRoutes.LiveHostMain)
                        .withString("anchormanId", anchormanId)
                        .navigation();
            }
        });
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAgree("??????", "????????????????????????", 1, null);
            }
        });
        liveBeautyIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBeautyControl.isShown()) {
                    mBeautyControl.setVisibility(View.GONE);
                } else {
                    mBeautyControl.setVisibility(View.VISIBLE);
                }
            }
        });
        mBeautyControl.setOnBeautyListener(new BeautyPanel.OnBeautyListener() {
            @Override
            public void onTabChange(TabInfo tabInfo, int position) {

            }

            @Override
            public boolean onClose() {
                mBeautyControl.setVisibility(View.GONE);
                return true;
            }

            @Override
            public boolean onClick(TabInfo tabInfo, int tabPosition, ItemInfo itemInfo, int itemPosition) {
                return false;
            }

            @Override
            public boolean onLevelChanged(TabInfo tabInfo, int tabPosition, ItemInfo itemInfo, int itemPosition, int beautyLevel) {
                return false;
            }
        });
        liveShopingIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liveShoppingBagDialog = LiveShoppingBagDialog.newInstance();
                liveShoppingBagDialog.setList(activityIdList);
                liveShoppingBagDialog.setAnchormanId(anchormanId);
                liveShoppingBagDialog.setCourseId(courseId);
                liveShoppingBagDialog.setLiveInfo(mLiveRoomInfo);
                liveShoppingBagDialog.show(getSupportFragmentManager(), "??????");
            }
        });
        helpLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liveHelpMarketingDialog = LiveHelpMarketingDialog.newInstance();
                liveHelpMarketingDialog.setCourseId(courseId, mLiveRoomInfo, mLiveRoomInfo.shopId != null ? mLiveRoomInfo.shopId : SpUtils.getValue(mContext, SpKey.CHOSE_SHOP), mLiveRoomInfo.merchantId);
                liveHelpMarketingDialog.setGroupId(groupId);
                liveHelpMarketingDialog.show(getSupportFragmentManager(), "");
            }
        });
    }

    private void initialize() {//??????????????????IMSDK
        mLivePusher = new TXLivePusher(this);
        mLivePushConfig = new TXLivePushConfig();
        mLivePushConfig.setVideoEncodeGop(5);
        mLivePusher.setConfig(mLivePushConfig);
        // ????????????
        mLivePusher.setBeautyFilter(mBeautyStyle, mBeautyLevel, mWhiteningLevel, mRuddyLevel);
        //mWaterMarkBitmap = decodeResource(mContext.getResources(), R.drawable.livepusher_watermark);//??????
        initListener();
        startPush();

        mBeautyControl.setBeautyManager(mLivePusher.getBeautyManager());
        BeautyInfo beautyInfo = mBeautyControl.getDefaultBeautyInfo();
        beautyInfo.setBeautyBg(BeautyConstants.BEAUTY_BG_GRAY);
        mBeautyControl.setBeautyInfo(beautyInfo);
    }

    private void initChatRoomAdapter() {
        pushLiveChatRoomAdapter = new PushLiveChatRoomAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        chatRecycler.setLayoutManager(layoutManager);
        chatRecycler.setAdapter(pushLiveChatRoomAdapter);
        pushLiveChatRoomAdapter.setData(new SimpleArrayListBuilder<ChatMessage>());
        pushLiveChatRoomAdapter.setOutClickListener(this);
    }

    @Override
    public void startPush() {
        int resultCode = Constants.PLAY_STATUS_SUCCESS;
        String tRTMPURL = "";
        if (!TextUtils.isEmpty(pushAddress)) {
            String url[] = pushAddress.split("###");
            if (url.length > 0) {
                tRTMPURL = url[0];
            }
        }
        if (TextUtils.isEmpty(tRTMPURL) || (!tRTMPURL.trim().toLowerCase().startsWith("rtmp://"))) {
            resultCode = Constants.PLAY_STATUS_INVALID_URL;
        } else {
            // ?????????????????????View
            mPusherView.setVisibility(View.VISIBLE);
            // ??????????????????????????????
            //Bitmap bitmap = decodeResource(mContext.getResources(), R.drawable.img_1_1_default);
            if (backImgUrl != null) {
                com.healthy.library.businessutil.GlideCopy.with(mContext)
                        .load(backImgUrl)
                        .placeholder(R.drawable.img_1_1_default)
                        .error(R.drawable.img_1_1_default)
                        .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                mLivePushConfig.setPauseImg(DrawableUtils.drawableToBitmap(resource));
                            }
                        });
                mLivePushConfig.setPauseImg(300, 5);
                mLivePushConfig.setPauseFlag(TXLiveConstants.PAUSE_FLAG_PAUSE_VIDEO);// ??????????????????????????????????????????????????????????????????
            }
            // ?????????????????????
            mLivePushConfig.setVideoResolution(mVideoResolution);

            // ???????????????????????????
            mLivePusher.setMute(false);

            // ??????????????????
            mLivePushConfig.setHomeOrientation(TXLiveConstants.VIDEO_ANGLE_HOME_DOWN);
            mLivePusher.setRenderRotation(0);

            // ?????????????????????????????????
            mLivePusher.setMirror(mIsMirrorEnable);
            // ????????????????????????
            mPusherView.showLog(mIsDebugInfo);

            // ??????????????????
            if (mIsWaterMarkEnable) {
                mLivePushConfig.setWatermark(mWaterMarkBitmap, 0.02f, 0.05f, 0.2f);
            } else {
                mLivePushConfig.setWatermark(null, 0, 0, 0);
            }

            // ????????????????????????
            mLivePushConfig.setTouchFocus(mIsFocusEnable);
            // ????????????????????????????????????
            mLivePushConfig.setEnableZoom(mIsZoomEnable);
            //???????????????????????????
            mLivePushConfig.enablePureAudioPush(mIsPureAudio);
            //	???????????????????????????
            mLivePushConfig.enableAudioEarMonitoring(mIsEarMonitoringEnable);
            // ????????????
            setPushScene(mQualityType, mIsEnableAdjustBitrate);
            // ????????????
            mLivePushConfig.setAudioChannels(mAudioChannels);
            // ?????????????????????
            mLivePushConfig.setAudioSampleRate(mAudioSample);
            //??????????????????
            setHardwareAcceleration(true);
            // ??????????????????
            mLivePusher.setConfig(mLivePushConfig);
            // ??????????????????View
            mLivePusher.startCameraPreview(mPusherView);
            if (!mFrontCamera) {
                mLivePusher.switchCamera();
            }
            // ????????????
            resultCode = mLivePusher.startPusher(tRTMPURL.trim());
            if (resultCode != 0) {
                System.out.println("??????????????????");
                try {
                    CrashReport.postCatchedException(new Throwable("?????????????????????  courseId==" + courseId + "   anchormanId==" + anchormanId
                            + "   memberId==" + new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT))));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            changeCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchCamera();
                }
            });
        }
        if (mOnLivePusherCallback != null) {
            mOnLivePusherCallback.onPushStart(resultCode);
        }
        mLivePusher.setPushListener(new ITXLivePushListener() {
            @Override
            public void onPushEvent(int event, Bundle bundle) {
                if (isFinish) {//??????????????? ???????????????????????? ??????????????? ????????????
                    return;
                }
                if (event == TXLiveConstants.PUSH_ERR_NET_DISCONNECT) {
                    if (!errIsShow) {
                        showToast("??????????????????????????????,?????????????????????");
                        Log.e(TAG, "???????????????????????????????????????????????????????????????????????????");
                        if (liveErrorDialog != null) {
                            liveErrorDialog.dismiss();
                        }
                        liveErrorDialog = LiveErrorDialog.newInstance();
                        liveErrorDialog.setType(3);
                        errIsShow = true;
                        liveErrorDialog.show(getSupportFragmentManager(), "??????");
                        liveErrorDialog.setClickListener(new LiveErrorDialog.OnErrClickListener() {
                            @Override
                            public void onClick(int type) {
                                if (type == 3) {
                                    liveErrorDialog.dismiss();// ??????????????????????????????,????????????????????? ????????????
                                    pushLivePresenter.stopPushLive(new SimpleHashMapBuilder<String, Object>().puts("courseId", courseId).puts("status", "3"));
                                }
                            }
                        });
                    }
                    try {
                        CrashReport.postCatchedException(new Throwable("?????????????????????  courseId==" + courseId + "   anchormanId==" + anchormanId
                                + "   memberId==" + new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT))));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (event == TXLiveConstants.PUSH_WARNING_RECONNECT) {
                    showNetBusyTips();
                    Log.e(TAG, "??????????????????????????????????????????????????????????????????????????????????????????");
                }
                if (event == TXLiveConstants.PUSH_ERR_OPEN_CAMERA_FAIL || event == TXLiveConstants.PUSH_ERR_OPEN_MIC_FAIL) {
                    // ????????????????????????????????????
                    showToast("???????????????/??????????????????????????????");
                    mLivePusher.stopPusher();
                    mIsPushing = false;
                } else if (event == TXLiveConstants.PUSH_WARNING_NET_BUSY) {
                    //????????????????????????????????????????????????????????????????????????
                    showNetBusyTips();
                }
                if (event == TXLiveConstants.PUSH_EVT_PUSH_BEGIN) {
                    Log.e(TAG, "??????????????????????????????????????????????????????????????????");
                    errIsShow = false;
                    mIsPushing = true;
                    if (!isFirstChange) {
                        pushLivePresenter.changeLiveStatus(new SimpleHashMapBuilder<String, Object>().puts("courseId", courseId));
                        isFirstChange = true;
                    }
                    if (mLiveRoomInfo != null && mLiveRoomInfo.actualBeginTime == null) {
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        mLiveRoomInfo.setActualBeginTime(df.format(new Date()));
                    }
                    chatRoomPresenter.getInteractionList(new SimpleHashMapBuilder<String, Object>().puts("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC)).puts("anchormanId", anchormanId));
//                    try {
//                        CrashReport.postCatchedException(new Throwable("??????????????????????????????????????????????????????????????????  courseId==" + courseId + "   anchormanId==" + anchormanId
//                                + "   memberId==" + new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT))));
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                }
                if (event == TXLiveConstants.PUSH_ERR_VIDEO_ENCODE_FAIL) {
                    showToast("??????????????????");
                }
                if (event == TXLiveConstants.PUSH_ERR_VIDEO_ENCODE_FAIL) {
                    showToast("??????????????????");
                }
            }

            @Override
            public void onNetStatus(Bundle bundle) {

            }
        });
    }

    /**
     * ???????????????????????????
     */
    private void showNetBusyTips() {
        if (mTextNetBusyTips.isShown()) {
            return;
        }
        mTextNetBusyTips.setVisibility(View.VISIBLE);
        mTextNetBusyTips.postDelayed(new Runnable() {
            @Override
            public void run() {
                mTextNetBusyTips.setVisibility(View.GONE);
            }
        }, 5000);
    }

    @Override
    public void stopPush() {
        if (!mIsPushing) {
            return;
        }
        if (!errIsShow && isDebug == 0) {
            ARouter.getInstance()
                    .build(TencentLiveRoutes.LIVEDETAIL)
                    .withString("courseId", courseId)
                    .navigation();
        }
        if (mLivePusher != null) {
            mPusherView.onPause();
            // ??????????????????
            mLivePusher.stopCameraPreview(true);
            // ????????????
            mLivePusher.setPushListener(null);
            // ????????????
            mLivePusher.stopPusher();
            // ?????????????????????View
            mPusherView.setVisibility(View.GONE);
            // ??????????????????
            mLivePushConfig.setPauseImg(null);
        }
        mIsPrivateMode = false;
        mIsPushing = false;
        if (mOnLivePusherCallback != null) {
            mOnLivePusherCallback.onPushStop();
        }
        if (isFinish) {
            finish();
            isFinish = false;
        }
    }

    @Override
    public void togglePush() {
        if (mIsPushing) {
            stopPush();
        } else {
            startPush();
        }
    }

    @Override
    public void resume() {
        Log.i(TAG, "resume: mIsResume -> " + mIsResume);
        if (mIsResume) {
            return;
        }
        if (mPusherView != null) {
            mPusherView.onResume();
        }
        if (mIsPushing && mLivePusher != null) {
            // ???????????????????????????????????????resume
            if (!mIsPrivateMode) {
                mLivePusher.resumePusher();
            }
        }
        mIsResume = true;
        if (mOnLivePusherCallback != null) {
            mOnLivePusherCallback.onPushResume();
        }
    }

    @Override
    public void resumePush() {
        mLivePusher.resumePusher();
    }

    @Override
    public void pause() {
        Log.i(TAG, "pause: mIsResume -> " + mIsResume);
        if (mPusherView != null) {
            mPusherView.onPause();
        }
        if (mIsPushing && mLivePusher != null) {
            // ????????????????????????????????????????????????pause
            if (!mIsPrivateMode) {
                mLivePusher.pausePusher();
            }
        }
        mIsResume = false;
        if (mOnLivePusherCallback != null) {
            mOnLivePusherCallback.onPushPause();
        }
    }

    @Override
    public void pausePush() {
        mLivePusher.pausePusher();
    }

    @Override
    public void destroy() {
        stopPush();
        mPusherView.onDestroy();
        unInitPhoneListener();
        outGroup();
    }

    @Override
    public void finish() {
        super.finish();
        outGroup();
        if (liveErrorDialog != null) {
            liveErrorDialog = null;
        }
    }

    @Override
    public void switchCamera() {
        mFrontCamera = !mFrontCamera;
        mLivePusher.switchCamera();
    }

    @Override
    public void setRotationForActivity() {
        // ?????????????????????Activity??????????????????????????????????????????????????????
    }

    @Override
    public void setOnLivePusherCallback(OnLivePusherCallback callback) {
        mOnLivePusherCallback = callback;
    }

    @Override
    public TXLivePusher getTXLivePusher() {
        return mLivePusher;
    }

    @Override
    public boolean isPushing() {
        return mIsPushing;
    }

    @Override
    public void setPrivateMode(boolean enable) {
        mIsPrivateMode = enable;
        // ???????????????????????????????????????
        if (mIsPushing) {
            if (enable) {
                mLivePusher.pausePusher();
            } else {
                mLivePusher.resumePusher();
            }
        }
    }

    @Override
    public void setMute(boolean enable) {
        mIsMuteAudio = enable;
        mLivePusher.setMute(enable);
    }

    @Override
    public void setMirror(boolean enable) {
        mIsFlashLight = enable;
        mLivePusher.turnOnFlashLight(enable);
    }

    @Override
    public void turnOnFlashLight(boolean enable) {
        mIsDebugInfo = enable;
        mPusherView.showLog(enable);
    }

    @Override
    public void setWatermark(boolean enable) {
        mIsWaterMarkEnable = enable;
        if (enable) {
            mLivePushConfig.setWatermark(mWaterMarkBitmap, 0.02f, 0.05f, 0.2f);
        } else {
            mLivePushConfig.setWatermark(null, 0, 0, 0);
        }
        if (mLivePusher.isPushing()) {
            // ???????????????????????????????????????????????????????????????
            mLivePusher.setConfig(mLivePushConfig);
        }
    }

    @Override
    public void setTouchFocus(boolean enable) {
        mIsFocusEnable = enable;
        mLivePushConfig.setTouchFocus(enable);
        if (mLivePusher.isPushing()) {
            stopPush();
            startPush();
        }
    }

    @Override
    public void setEnableZoom(boolean enable) {
        mIsZoomEnable = enable;
        mLivePushConfig.setEnableZoom(enable);
        if (mLivePusher.isPushing()) {
            stopPush();
            startPush();
        }
    }

    @Override
    public void setHardwareAcceleration(boolean enable) {
        mIsHWAcc = enable;
        if (enable) {
            mLivePushConfig.setHardwareAcceleration(TXLiveConstants.ENCODE_VIDEO_HARDWARE); // ????????????
        } else {
            mLivePushConfig.setHardwareAcceleration(TXLiveConstants.ENCODE_VIDEO_SOFTWARE); // ????????????
        }
        if (mLivePusher.isPushing()) {
            // ?????????????????????????????????????????????????????????????????????
            mLivePusher.setConfig(mLivePushConfig);
        }
    }

    @Override
    public void setAdjustBitrate(boolean enable, int qualityType) {
        mIsEnableAdjustBitrate = enable;
        setPushScene(qualityType, enable);
    }

    @Override
    public void enableAudioEarMonitoring(boolean enable) {
        mIsEarMonitoringEnable = enable;
        if (mLivePusher != null) {
            TXLivePushConfig config = mLivePusher.getConfig();
            config.enableAudioEarMonitoring(enable);
            mLivePusher.setConfig(config);
        }
    }

    @Override
    public void enablePureAudioPush(boolean enable) {
        mIsPureAudio = enable;
    }

    private String[] mPermissions = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO, Manifest.permission.MODIFY_AUDIO_SETTINGS, Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,};
    private static final int RC_CAMERA = 45;
    private static final int RC_RECORD_AUDIO = 46;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RC_CAMERA) {
            if (!PermissionUtils.hasPermissions(mContext, mPermissions)) {
                showToast("???????????????????????????????????????");
                PermissionUtils.requestPermissions(this, RC_CAMERA, mPermissions);
            } else {
                initialize();
                pushLivePresenter.getLiveRoomInfo(new SimpleHashMapBuilder<String, Object>().puts("courseId", courseId));
                initChatRoomAdapter();
                checkNetWorkStatusAndAudio();
            }
        }
        if (requestCode == RC_RECORD_AUDIO) {
            if (!PermissionUtils.hasPermissions(mContext, mPermissions)) {
                showToast("???????????????????????????????????????");
                PermissionUtils.requestPermissions(this, RC_RECORD_AUDIO, mPermissions);
            } else {
                initialize();
                pushLivePresenter.getLiveRoomInfo(new SimpleHashMapBuilder<String, Object>().puts("courseId", courseId));
                initChatRoomAdapter();
                checkNetWorkStatusAndAudio();
            }
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private TXPhoneStateListener mPhoneListener;

    /**
     * ??????????????????????????????????????????????????????
     */
    private void initListener() {
        mPhoneListener = new TXPhoneStateListener();
        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Service.TELEPHONY_SERVICE);
        tm.listen(mPhoneListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    /**
     * ??????
     */
    private void unInitPhoneListener() {
        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Service.TELEPHONY_SERVICE);
        tm.listen(mPhoneListener, PhoneStateListener.LISTEN_NONE);
    }

    /**
     * ??????????????????
     * <p>
     * SDK ?????????????????????????????????????????? ?????????????????????FPS?????????????????????????????????????????????????????? ???????????????
     * <p>
     * ????????????????????????????????????????????????
     * <p>
     * ??????????????????????????? {@link TXLivePushConfig} ??????????????????
     */
    private void setPushScene(int type, boolean enableAdjustBitrate) {
        Log.i(TAG, "setPushScene: type = " + type + " enableAdjustBitrate = " + enableAdjustBitrate);
        mQualityType = type;
        mIsEnableAdjustBitrate = enableAdjustBitrate;
        // ????????????????????????????????????
        boolean autoResolution = true;
        switch (type) {
            case TXLiveConstants.VIDEO_QUALITY_STANDARD_DEFINITION:     /*360p*/
                if (mLivePusher != null) {
                    mLivePusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_STANDARD_DEFINITION, enableAdjustBitrate, autoResolution);
                    mVideoResolution = TXLiveConstants.VIDEO_RESOLUTION_TYPE_360_640;
                }
                break;
            case TXLiveConstants.VIDEO_QUALITY_HIGH_DEFINITION:         /*540p*/
                if (mLivePusher != null) {
                    mLivePusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_HIGH_DEFINITION, enableAdjustBitrate, autoResolution);
                    mVideoResolution = TXLiveConstants.VIDEO_RESOLUTION_TYPE_540_960;
                }
                break;
            case TXLiveConstants.VIDEO_QUALITY_SUPER_DEFINITION:        /*720p*/
                if (mLivePusher != null) {
                    mLivePusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_SUPER_DEFINITION, enableAdjustBitrate, autoResolution);
                    mVideoResolution = TXLiveConstants.VIDEO_RESOLUTION_TYPE_720_1280;
                }
                break;
            case TXLiveConstants.VIDEO_QUALITY_ULTRA_DEFINITION:        /*1080p*/
                if (mLivePusher != null) {
                    mLivePusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_ULTRA_DEFINITION, enableAdjustBitrate, autoResolution);
                    mVideoResolution = TXLiveConstants.VIDEO_RESOLUTION_TYPE_1080_1920;
                }
                break;
            case TXLiveConstants.VIDEO_QUALITY_REALTIEM_VIDEOCHAT:      /*??????*/
                if (mLivePusher != null) {
                    mLivePusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_REALTIEM_VIDEOCHAT, enableAdjustBitrate, autoResolution);
                    mVideoResolution = TXLiveConstants.VIDEO_RESOLUTION_TYPE_360_640;
                }
                break;
            default:
                break;
        }
        // ???????????????????????????SDK ???????????????????????????????????????????????????????????????????????????????????????config?????????????????????????????????
        mLivePushConfig = mLivePusher.getConfig();

        // ????????????????????????
        if (mIsHWAcc) {
            mLivePushConfig.setHardwareAcceleration(TXLiveConstants.ENCODE_VIDEO_HARDWARE);
            mLivePusher.setConfig(mLivePushConfig);
        }
    }

    @Override
    public void onError(int code, String errInfo) {
        Log.e(TAG, "IM???????????????" + errInfo + "??????????????????:" + code);
    }

    @Override
    public void onSuccess(Object... args) {
        Log.e(TAG, "IM???????????????" + args.toString());
    }

    @Override
    public void onConnected() {//IM????????????
    }

    @Override
    public void onLoginSuccess() {//IM????????????
        imMessageMgr.joinGroup(groupId, new IMMessageMgr.Callback() {
            @Override
            public void onError(int code, String errInfo) {
                Log.e(TAG, "IM?????????????????????,???????????????" + reconnection + "???");
                if (reconnection < 2) {
                    reconnection++;
                    if (imMessageMgr == null) {
                        imMessageMgr = new IMMessageMgr(mContext);
                        imMessageMgr.setIMMessageListener(PushLiveActivity.this);
                    }
                    imMessageMgr.initialize(accountId, userSig
                            , Integer.parseInt(sdkAppId), this);//?????????????????????  ?????????????????????????????????
                } else {
                    Toast.makeText(mContext, "???????????????????????????????????????????????????????????????????????????????????????????????????~", Toast.LENGTH_LONG).show();
                    try {
                        CrashReport.postCatchedException(new Throwable("??????????????????????????????????????????????????????????????????????????????????????????  courseId==" + courseId + "   anchormanId==" + anchormanId
                                + "   memberId==" + new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)) + "   groupId==" + groupId));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.e(TAG, "?????????????????????????????????????????????????????????????????????????????????????????? ,???????????????");
                }
            }

            @Override
            public void onSuccess(Object... args) {
                showContent();
                Log.e(TAG, "IM?????????????????????");
                chatRoomPresenter.getLiveRoomMapping(new SimpleHashMapBuilder<String, Object>().puts("anchormanId", anchormanId).puts("roomDecorationType", "1"), 1);
                chatRoomPresenter.getLiveRoomMapping(new SimpleHashMapBuilder<String, Object>().puts("anchormanId", anchormanId).puts("roomDecorationType", "2"), 2);
                try {
                    chatRoomPresenter.addLookLivePeopleNum(new SimpleHashMapBuilder<String, Object>().puts("groupId", groupId)
                            .puts("memberId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT))));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    CrashReport.postCatchedException(new Throwable("??????????????????/?????????????????????  courseId==" + courseId + "   anchormanId==" + anchormanId
                            + "   memberId==" + new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)) + "   groupId==" + groupId));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onjoiniGroupSuccess() {//???????????????
        chatEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //???actionId == XX_SEND ?????? XX_DONE????????????
                //??????event.getKeyCode == ENTER ??? event.getAction == ACTION_DOWN????????????
                //??????????????????????????????event != null???????????????????????????????????????null???
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    //????????????
                    if (chatEdit.getText().toString().trim() != null && chatEdit.getText().toString().trim().length() > 0) {
                        chatRoomPresenter.sendCustomerTxtMessage(new SimpleHashMapBuilder<String, Object>().puts("accountId", accountId)
                                .puts("groupId", groupId).puts("CmdType", "Message").puts("CmdPara", chatEdit.getText().toString()));
                    } else {
                        showToast("????????????????????????");
                    }
                }
                return false;
            }
        });

    }

    @Override
    public void onjoiniGroupFail() {
        chatEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //???actionId == XX_SEND ?????? XX_DONE????????????
                //??????event.getKeyCode == ENTER ??? event.getAction == ACTION_DOWN????????????
                //??????????????????????????????event != null???????????????????????????????????????null???
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    //????????????
                    showToast("?????????????????????????????????????????????????????????????????????????????????");
                }
                return false;
            }
        });
    }

    @Override
    public void onDisconnected() {//IM????????????
        if (imMessageMgr == null) {
            imMessageMgr = new IMMessageMgr(mContext);
            imMessageMgr.setIMMessageListener(this);
        }
        imMessageMgr.initialize(accountId, userSig
                , Integer.parseInt(sdkAppId), this);//?????????????????????  ?????????????????????????????????
        try {
            CrashReport.postCatchedException(new Throwable("??????????????????Im?????????????????????????????????  courseId==" + courseId + "   anchormanId==" + anchormanId
                    + "   memberId==" + new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)) + "   groupId==" + groupId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPusherChanged() {//IM????????????????????????????????????

    }

    @Override
    public void onGroupTextMessage(String groupID, String senderID, String userName, String message) {//?????????????????????
        if (message != null) {
            pushLiveChatRoomAdapter.addDatas(new SimpleArrayListBuilder<ChatMessage>().adds(new ChatMessage(userName, message, 1, senderID)));
            totalMessageList.add(new ChatMessage(userName, message, 1, senderID));
            scrollRec();
        } else {
            try {
                CrashReport.postCatchedException(new Throwable("message??????  courseId==" + courseId + "   anchormanId==" + anchormanId
                        + "   memberId==" + new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)) + "message==" + message + " userName== " + userName));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onGroupCustomMessage(String groupID, String senderID, String userName, String CmdPara, String CmdType) {//???????????????????????????
        if (CmdPara != null && "Message".equals(CmdType)) {//??????
            pushLiveChatRoomAdapter.addDatas(new SimpleArrayListBuilder<ChatMessage>().adds(new ChatMessage(userName, CmdPara, 2, senderID)));
            totalMessageList.add(new ChatMessage(userName, CmdPara, 2, senderID));
            scrollRec();
        } else if (CmdPara != null && "Curtain".equals(CmdType)) {//??????????????????
            refreshTips(CmdPara, liveTipsTxt, topTipsLayout);
        } else if (CmdPara != null && "Change_Focus".equals(CmdType)) {//??????????????????
            liveShoppingBagDialogPresenter.getLiveRoomGoodsList(new SimpleHashMapBuilder<String, Object>().puts("liveActivityIds", activityIdList));
        } else if (CmdPara != null && "Refresh_Card".equals(CmdType)) {//???????????????
            liveShoppingBagDialogPresenter.getLiveRoomGoodsList(new SimpleHashMapBuilder<String, Object>().puts("liveActivityIds", activityIdList));
        } else if (CmdPara != null && "Set_Live_Name".equals(CmdType)) {//??????????????????

        } else if (CmdPara != null && "Refresh_Drawing".equals(CmdType)) {//????????????
            chatRoomPresenter.getLiveRoomMapping(new SimpleHashMapBuilder<String, Object>().puts("anchormanId", anchormanId).puts("roomDecorationType", "1"), 1);
        } else if (CmdPara != null && "Refresh_Carousel".equals(CmdType)) {//???????????????
            chatRoomPresenter.getLiveRoomMapping(new SimpleHashMapBuilder<String, Object>().puts("anchormanId", anchormanId).puts("roomDecorationType", "2"), 2);
        } else if (CmdPara != null && "Refresh_Single_Goods".equals(CmdType)) {//??????????????????
            liveShoppingBagDialogPresenter.getLiveRoomGoodsList(new SimpleHashMapBuilder<String, Object>().puts("liveActivityIds", activityIdList));
            if (liveShoppingBagDialog != null) {
                liveShoppingBagDialog.changeList(activityIdList);
            }
        } else if (CmdPara != null && "Refresh_Goods_List".equals(CmdType)) {//??????????????????
            pushLivePresenter.getLiveRoomInfo(new SimpleHashMapBuilder<String, Object>().puts("courseId", courseId));
        } else if (CmdPara != null && "Refresh_Current_Goods".equals(CmdType)) {//????????????????????????
            liveShoppingBagDialogPresenter.getLiveRoomGoodsList(new SimpleHashMapBuilder<String, Object>().puts("liveActivityIds", activityIdList));
            if (liveShoppingBagDialog != null) {
                liveShoppingBagDialog.changeList(activityIdList);
            }
        } else if ("Refresh_Fans_Count".equals(CmdType)) {//???????????????
//            if (CmdPara != null && !TextUtils.isEmpty(CmdPara)) {
//                if ("0".equals(CmdPara)) {
//                    anchorFanceNum.setText("????????????");
//                } else {
//                    anchorFanceNum.setText(CmdPara + " ??????");
//                }
//            }
        } else if ("Refresh_Popularity".equals(CmdType)) {//????????????
            if (CmdPara != null) {
                refreshPopularity(CmdPara);
            }
        } else if ("Notify_Goods_List".equals(CmdType)) {//????????????
            if (CmdPara != null && liveVideoGoods != null) {
                refreshGoods(CmdPara);
            }
        } else if ("Refresh_Interactive_List".equals(CmdType)) {//????????????????????????
            chatRoomPresenter.getInteractionList(new SimpleHashMapBuilder<String, Object>().puts("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC)).puts("anchormanId", anchormanId));
        } else if ("Refresh_Red_Packet".equals(CmdType)) {//????????????
            if (CmdPara != null) {
                try {
                    buildRedGift(new JSONObject(CmdPara).get("id").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else if ("Refresh_Star_Count".equals(CmdType)) {//??????????????????
            if (CmdPara != null && !TextUtils.isEmpty(CmdPara)) {
                if ("0".equals(CmdPara)) {
                    anchorFanceNum.setText("????????????");
                } else {
                    anchorFanceNum.setText(String.format("%s????????????", ParseUtils.parseNumber(CmdPara, 10000, "???")));
                }
            }
        } else if ("Refresh_Help".equals(CmdType)) {//??????????????????
            if (liveShoppingBagDialogPresenter != null) {
                liveShoppingBagDialogPresenter.getJackpotList(new SimpleHashMapBuilder<String, Object>()
                        .puts("pageNum", "1")
                        .puts("pageSize", 100)
                        .puts("courseId", courseId)
                        .puts("memberId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT))), 1);
            }
        }
    }

    private void buildRedGift(final String id) {
        if (liveRedEnvelopesDialog != null) {
            try {
                liveRedEnvelopesDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        liveRedEnvelopesDialog = LiveRedEnvelopesDialog.newInstance();
        liveRedEnvelopesDialog.setPopularity(historyLookNum).setClickListener(new LiveRedEnvelopesDialog.OnRedClickListener() {
            @Override
            public void onClick() {
                chatRoomPresenter.getRedGift(new SimpleHashMapBuilder<String, Object>().puts("liveBenefitId", id).puts("courseId", courseId));
            }
        }).show(getSupportFragmentManager(), "");
    }


    private void refreshGoods(String CmdPara) {
        List<PushModel> data = resolveData(CmdPara);
        if (data != null && data.size() != liveVideoGoods.size()) {
            liveShoppingBagDialogPresenter.getLiveRoomGoodsList(new SimpleHashMapBuilder<String, Object>().puts("liveActivityIds", activityIdList));
            return;
        }
        if (data != null & data.size() > 0) {
            for (int i = 0; i < liveVideoGoods.size(); i++) {
                liveVideoGoods.get(i).availableInventory = getInventoryByGoodsId(liveVideoGoods.get(i).goodsId, liveVideoGoods.get(i).getGoodsChildrenId(), data, liveVideoGoods.get(i).liveActivityId);
                liveVideoGoods.get(i).ranking = getRankingByGoodsId(liveVideoGoods.get(i).goodsId, liveVideoGoods.get(i).getGoodsChildrenId(), data, liveVideoGoods.get(i).liveActivityId);
            }
        }
        getLiveRoomGoodsListSuccess(sortList(liveVideoGoods));
    }

    private void refreshPopularity(String CmdPara) {
        int fictitious = new BigDecimal(CmdPara).multiply(new BigDecimal(17)).intValue();
        if (fictitious <= historyLookNum) {//??????????????????????????????
            return;
        }
        historyLookNum = fictitious;
        //??????????????????????????????
        if (liveRedEnvelopesDialog != null) {
            liveRedEnvelopesDialog.setPopularity(historyLookNum);
        }
        if (fictitious > 0) {
            popularityLayout.setVisibility(View.VISIBLE);
        } else {
            popularityLayout.setVisibility(View.GONE);
        }
        if (fictitious > 9999) {
            groupNums.setText(ParseUtils.parseNumber(fictitious + "", 10000, "???") + "??????");
        } else {
            groupNums.setText(fictitious + "??????");
        }
    }

    @Override
    public void onGroupDestroyed(String groupID) {//IM??????????????????

    }

    @Override
    public void onDebugLog(String log) {//????????????
        Log.e(TAG, "IM???" + log);
    }

    @Override
    public void onGroupMemberEnter(String groupID, List<V2TIMGroupMemberInfo> users) {//??????????????????
        try {
            if (users != null && !users.get(users.size() - 1).getUserID().equals(accountId)) {
    //            pushLiveChatRoomAdapter.addDatas(new SimpleArrayListBuilder<ChatMessage>().adds(new ChatMessage(users.get(users.size() - 1).getNickName(), "???????????????", 3, users.get(users.size() - 1).getUserID())));
    //            totalMessageList.add(new ChatMessage(users.get(users.size() - 1).getNickName(), "???????????????", 3, users.get(users.size() - 1).getUserID()));
    //            scrollRec();
                addLayout.setVisibility(View.VISIBLE);
                String nickName = null;
                if (users.get(users.size() - 1).getNickName() != null && !TextUtils.isEmpty(users.get(users.size() - 1).getNickName())) {
                    nickName = users.get(users.size() - 1).getNickName();
                } else {
                    nickName = users.get(users.size() - 1).getUserID();
                }
                if (nickName != null && nickName.length() > 5) {
                    nickName = nickName.substring(0, 5) + "...";
                }
                userNickName.setText(nickName);
                chartContent.setText("???????????????");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        addLayout.setVisibility(View.GONE);
                    }
                }, 3000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void scrollRec() {
        if (pushLiveChatRoomAdapter != null) {
            chatRecycler.scrollToPosition(pushLiveChatRoomAdapter.getItemCount() - 1);
        }
    }

    @Override
    public void onGroupMemberExit(String groupID, V2TIMGroupMemberInfo users) {//??????????????????
        Log.e(TAG, users.getNickName() + "??????????????????");
    }

    @Override
    public void onForceOffline() {//???????????????????????????
        Log.e(TAG, "???????????????????????????");
        try {
            CrashReport.postCatchedException(new Throwable("???????????????????????????????????????????????????IM??????????????????,courseId==" + courseId + "   anchormanId==" + anchormanId
                    + "   memberId==" + new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID) != null ? SpUtils.getValue(mContext, SpKey.USER_ID).getBytes() : null, Base64.DEFAULT))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGroupMemberInfoChanged(String groupID, List<V2TIMGroupMemberChangeInfo> v2TIMGroupMemberChangeInfoList) {

    }

    @Override
    public void onStopPushLiveSuccess(int result) {
        showLoading();
        if (result == -2) {
            try {
                CrashReport.postCatchedException(new Throwable("???????????????????????????????????????,courseId==" + courseId + "   anchormanId==" + anchormanId
                        + "   memberId==" + new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID) != null ? SpUtils.getValue(mContext, SpKey.USER_ID).getBytes() : null, Base64.DEFAULT))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            isFinish = true;
            mIsPushing = true;
            stopPush();
        } else {
            if (errIsShow) {
                liveErrorDialog.dismiss();
                errIsShow = false;
                pushLivePresenter.againLive(new SimpleHashMapBuilder<String, Object>().puts("courseId", courseId).puts("isDebug", isDebug));
            } else {
                isFinish = true;
                mIsPushing = true;
                stopPush();
            }
        }
    }

    @Override
    public void onStopSpeedSuccess() {
        showToast("????????????");
    }

    @Override
    public void onChangeLiveStatusSuccess() {

    }

    @Override
    public void getLiveRoomGoodsListSuccess(List<LiveVideoGoods> result) {
        if (result != null && result.size() > 0) {
            if (liveShoppingBagDialog != null) {
                liveShoppingBagDialog.changeGoodsList(result);
            }
            liveVideoGoods = result;
            shopCartRel.setVisibility(View.VISIBLE);
            shopCartNum.setVisibility(View.VISIBLE);
            shopCartNum.setText(result.size() + "");
        } else {
            shopCartRel.setVisibility(View.GONE);
            shopCartNum.setVisibility(View.GONE);
        }
    }

    @Override
    public void onGetStoreListSuccess(List<ShopDetailModel> result) {

    }

    @Override
    public void onGetMerchantStoreListSuccess(List<ShopDetailModel> result) {

    }

    @Override
    public void getSuccessJackpotList(List<JackpotList> result, PageInfoEarly pageInfo, int type) {
        if (result != null && result.size() > 0) {
            helpLayout.setVisibility(View.VISIBLE);
            if (liveHelpMarketingDialog != null) {
                liveHelpMarketingDialog.refresh();
            }
        } else {
            if (type == 1) {
                showToast("?????????????????????????????????");
                if (liveHelpMarketingDialog != null) {
                    liveHelpMarketingDialog.dismiss();
                }
            }
            helpLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void outClick(@NotNull String function, @NotNull Object obj) {
        if ("stopSpeed".equals(function)) {
            ChatMessage chatMessage = (ChatMessage) obj;
            if (!chatMessage.senderID.equals(accountId)) {
                isAgree("??????", "????????????" + chatMessage.userName + "??????", 2, chatMessage);
            }

        }
    }


    /**
     * ????????????
     */
    private class TXPhoneStateListener extends PhoneStateListener {

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            Log.i(TAG, "onCallStateChanged: state -> " + state);
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:   //??????????????????
                case TelephonyManager.CALL_STATE_OFFHOOK:   //????????????
                    pause();
                    break;
                case TelephonyManager.CALL_STATE_IDLE:      //????????????
                    resume();
                    break;
            }
        }
    }

    public class PhoneStateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "PhoneStateReceiver action: " + action);
        }

    }

    private void isAgree(String title, String msg, final int type, final ChatMessage chatMessage) {
        StyledDialog.init(this);
        if(type==1){
            StyledDialog.buildIosAlert(title, "\n" + msg, new MyDialogListener() {
                        @Override
                        public void onFirst() {

                        }

                        @Override
                        public void onThird() {
                            super.onThird();
                            isDebug = 0;
                            finish();
                        }

                        @Override
                        public void onSecond() {
                            if (type == 1) {
                                isFinish = true; //?????????????????? ????????????
                                pushLivePresenter.stopPushLive(new SimpleHashMapBuilder<String, Object>().puts("courseId", courseId).puts("status", "3"));
                            }

                        }
                    }).setBtnColor(R.color.dialogutil_text_black, R.color.colorPrimaryDark, 0)
                    .setBtnText("??????", "??????").show();
        }else {
            StyledDialog.buildIosAlert(title, "\n" + msg, new MyDialogListener() {
                        @Override
                        public void onFirst() {
                        }

                        @Override
                        public void onThird() {
                            super.onThird();
                        }

                        @Override
                        public void onSecond() {
                            pushLivePresenter.stopSpeed(new SimpleHashMapBuilder<String, Object>().puts("accountId", chatMessage.senderID).puts("groupId", groupId));

                        }
                    }).setBtnColor(R.color.dialogutil_text_black, R.color.colorPrimaryDark, 0)
                    .setBtnText("??????", "??????").show();
        }

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            isAgree("??????", "????????????????????????", 1, null);
            //???????????????????????????
            return true;
        }
        //????????????????????????????????????
        return super.onKeyUp(keyCode, event);
    }

    private void outGroup() {//????????????
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                V2TIMManager.getInstance().quitGroup(groupId, new V2TIMCallback() {
                    @Override
                    public void onError(int i, String s) {
                        Log.e(TAG, "??????????????????");
                    }

                    @Override
                    public void onSuccess() {
                        Log.e(TAG, "??????????????????");
                    }
                });
            }
        });
        if (imMessageMgr != null) {
            //????????????IM
            imMessageMgr.unInitialize();
        }
    }

    @Override
    public String getSurl() {
        String urlPrefix = SpUtils.getValue(mContext, UrlKeys.H5_BargainUrl);
        String url = String.format("%s?courseId=%s&scheme=%s&referral_code=%s", urlPrefix, courseId, "HMMLive", SpUtils.getValue(mContext, SpKey.GETTOKEN));
        return url;
    }

    @Override
    public String getSdes() {
        return null;
    }


    @Override
    public String getStitle() {
        return stitle;
    }

    @Override
    public Bitmap getsBitmap() {
        return sBitmap;
    }

    @Override
    public String getPath() {
        return "pages/home/liveRoom/index?id=" + courseId + "&referral_code=" + SpUtils.getValue(mContext, SpKey.GETTOKEN);
    }

    @Override
    public String getUserName() {
        return "gh_f9b4fbd9d3b8";
    }

    String stitle;
    Bitmap sBitmap;

    public void lotteryTime(final String time, final TextView purseTxt) {
        if (time != null) {
            long mSeconds = DateUtils.getDistanceSec(time, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            new CountDownTimer(mSeconds * 1000, 1000) {
                public void onTick(long millisUntilFinished) {
                    if (millisUntilFinished / 1000 > 0) {
                        purseTxt.setText(getTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), time) + "???????????????");
                    } else {
                        purseTxt.setText("?????????");
                        chatRoomPresenter.getInteractionList(new SimpleHashMapBuilder<String, Object>().puts("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC)).puts("anchormanId", anchormanId));
                    }
                }

                public void onFinish() {
                    purseTxt.setText("?????????");
                    chatRoomPresenter.getInteractionList(new SimpleHashMapBuilder<String, Object>().puts("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC)).puts("anchormanId", anchormanId));
                    this.cancel();
                }
            }.start();
        }
    }
}