package com.health.tencentlive.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.lib_ShapeView.view.ShapeTextView;
import com.health.servicecenter.widget.GoodsSimpleDialog;
import com.health.tencentlive.R;
import com.health.tencentlive.adapter.LiveLookGoodsListAdapter;
import com.health.tencentlive.adapter.PushLiveChatRoomAdapter;
import com.health.tencentlive.contract.ChatRoomContract;
import com.health.tencentlive.contract.LiveShoppingBagDialogContract;
import com.health.tencentlive.contract.LookLiveContract;
import com.health.tencentlive.like.TCHeartLayout;
import com.health.tencentlive.model.Interaction;
import com.health.tencentlive.model.InteractionDetail;
import com.health.tencentlive.model.JackpotList;
import com.health.tencentlive.model.PushModel;
import com.health.tencentlive.model.RedGift;
import com.health.tencentlive.presenter.ChatRoomPresenter;
import com.health.tencentlive.presenter.LiveShoppingBagDialogPresenter;
import com.health.tencentlive.presenter.LookLivePresenter;
import com.health.tencentlive.superlike.BitmapProviderFactory;
import com.health.tencentlive.superlike.SuperLikeLayout;
import com.health.tencentlive.utils.IMMessageMgr;
import com.health.tencentlive.weight.LiveErrorDialog;
import com.health.tencentlive.weight.LiveGetGiftDialog;
import com.health.tencentlive.weight.LiveHelpMarketingDialog;
import com.health.tencentlive.weight.LiveRedEnvelopesDialog;
import com.health.tencentlive.weight.LiveRedEnvelopesGiftDialog;
import com.health.tencentlive.weight.LiveShoppingBagDialog;
import com.healthy.library.LibApplication;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.builder.ObjectIteraor;
import com.healthy.library.builder.SimpleArrayListBuilder;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.business.SeckShareDialog;
import com.healthy.library.businessutil.ChannelUtil;
import com.healthy.library.businessutil.GlideCopy;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.dialog.TongLianPhoneBindDialog;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.interfaces.IsNeedShare;
import com.healthy.library.interfaces.IsNeedShareMini;
import com.healthy.library.model.AnchormanInfo;
import com.healthy.library.model.ChatMessage;
import com.healthy.library.model.ChatRoomConfigure;
import com.healthy.library.model.GoodsBasketCell;
import com.healthy.library.model.GoodsBasketGroup;
import com.healthy.library.model.GoodsSpecDetail;
import com.healthy.library.model.LiveRoomDecoration;
import com.healthy.library.model.LiveRoomInfo;
import com.healthy.library.model.LiveVideoGoods;
import com.healthy.library.model.MessageSendCode;
import com.healthy.library.model.OnLineNum;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.model.ShopDetailModel;
import com.healthy.library.model.TongLianMemberData;
import com.healthy.library.net.NetUtil;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.routes.TencentLiveRoutes;
import com.healthy.library.utils.DateUtils;
import com.healthy.library.utils.DrawableUtils;
import com.healthy.library.utils.FastClickUtils;
import com.healthy.library.utils.ObjUtil;
import com.healthy.library.utils.ParseUtils;
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
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import permison.FloatWindowManager;

@Route(path = TencentLiveRoutes.LIVE_LOOK)
public class LookLiveActivity extends BaseLiveActivity implements IsNeedShareMini, IsFitsSystemWindows, IMMessageMgr.Callback,
        IMMessageMgr.IMMessageListener, ChatRoomContract.View, LookLiveContract.View, LiveShoppingBagDialogContract.View,
        IsNeedShare, SeekBar.OnSeekBarChangeListener {

    @Autowired
    String courseId;//直播ID
    @Autowired
    String merchantId;//商户id
    @Autowired
    String shopId;//门店id
    @Autowired
    String liveShareType;//可能分享类型
    @Autowired
    String fromMemberId;
    @Autowired
    String referral_code;
    @Autowired
    String shareLiveGoodsId;//可能分享商品了

    private String videoUrl;//回放URl
    private int status; //直播状态
    private String fileId; //回放文件ID
    private String pullAddress;//拉流地址
    private String groupId;//聊天群组ID
    private String anchormanId;//主播ID
    private String memberId;//主播memberId
    private List<String> activityIdList;//直播关联活动id集合
    private String sdkAppId = null;//IMAPPId
    private String userSig = null;//IMSIg
    private String accountId = null;//accountId
    private String TAG = "LookLive";
    private TXLivePlayer mLivePlayer;

    private ConstraintLayout activityView;
    private TXCloudVideoView videoView;
    private View viewStatus;
    private ConstraintLayout topView;
    private ConstraintLayout anchormanLayout;
    private CornerImageView anchorImg;
    private LinearLayout nameLayout;
    private TextView anchorName;
    private TextView anchorFanceNum;
    private TextView followBtn;
    private ImageView closeImg;
    private ConstraintLayout popularityLayout;
    private RelativeLayout headNowLL;
    private CornerImageView headIcon1;
    private CornerImageView headIcon2;
    private CornerImageView headIcon3;
    private TextView groupNums;
    private ImageView topBannerImg;
    private LinearLayout purseLayout;
    private ConstraintLayout helpLayout;
    private LinearLayout mappingLeftLin;
    private LinearLayout mappingRgihtLin;
    private RelativeLayout centerHeartLayout;
    private SuperLikeLayout superLikeLayout;
    private LinearLayout topTipsLayout;
    private TextView liveTipsTxt;
    private LinearLayout addLayout;
    private TextView userNickName;
    private TextView chartContent;
    private MaxHeightRecyclerView chatRecycler;
    private RecyclerView goodsRecycle;
    private ConstraintLayout bottomLayout;
    private ConstraintLayout editLayout;
    private EditText chatEdit;
    private ConstraintLayout rightLayout;
    private RelativeLayout shopCartRel;
    private ImageView liveShopingIcon;
    private TextView shopCartNum;
    private ImageView liveClickLike;
    private ImageView liveShareIcon;
    private ImageView liveMiniIcon;
    private ImageView liveFriendLinkIcon;
    private TextView clickNum;
    private LinearLayout bottomPlayerLayout;
    private RelativeLayout shopCartRel2;
    private ImageView liveShopingIcon2;
    private TextView shopCartNum2;
    private TextView startTime;
    private SeekBar seekBar;
    private TextView endTime;
    private TCHeartLayout heartLayout;
    private ImageView livePlayerImg;

    private IMMessageMgr imMessageMgr;
    private PushLiveChatRoomAdapter pushLiveChatRoomAdapter;
    private List<ChatMessage> totalMessageList = new ArrayList<>();//总的消息列表
    private LiveShoppingBagDialog liveShoppingBagDialog;
    private LiveShoppingBagDialogPresenter liveShoppingBagDialogPresenter;
    private LiveLookGoodsListAdapter liveLookGoodsListAdapter;
    private LiveErrorDialog liveErrorDialog;
    private List<LiveVideoGoods> liveVideoGoods = null;
    private String[] videoUrls = null;//用来存放后台返回的可能是多个videoUrl
    private ChatRoomPresenter chatRoomPresenter;
    private LookLivePresenter lookLivePresenter;

    //随机头像数组
    private int[] txArray = new int[]{R.drawable.tx01, R.drawable.tx02, R.drawable.tx03, R.drawable.tx04, R.drawable.tx05, R.drawable.tx06, R.drawable.tx07, R.drawable.tx08,
            R.drawable.tx09, R.drawable.tx10, R.drawable.tx11, R.drawable.tx12, R.drawable.tx13, R.drawable.tx14, R.drawable.tx15};
    private boolean isStopSpeak = true;//是否可以发消息 是否被禁言
    private boolean joinGroupSuccess = true;//是否可以发消息  或者加入聊天室失败了
    private boolean isFollow = false;//是否关注
    private boolean isFirstGet = true;//是否第一次初始化直播间
    private boolean isAutoConnect = false;//是否自动重连
    private boolean isHandConnect = false;//是否手动重连
    private boolean isCloseLive = false;//是否关闭了直播
    private int totalSecond = 0;//视频总时长  秒数
    private int currentSecond = 0;//当前播放时长  秒数
    private boolean isSetSeekBar = false;//是否初始化了SeekBar
    private Handler handler;//控制加入消息消失的handler
    private int isDebug = 0;//是否是调试模式
    private int playerPosition = 0;//记录当前播放的是第几个videoUrl
    private int historyLookNum = 0;//记录历史人气数值，按最大的数值来显示
    private int reconnection = 0;//记录加入聊天室失败后重新加入聊天室的次数  3次还没加进去就提示用户
    private LiveRoomInfo mLiveRoomInfo;
    private LiveGetGiftDialog liveGetGiftDialog;//抽奖的dialog
    private LiveHelpMarketingDialog liveHelpMarketingDialog;//助力的dialog
    private LiveRedEnvelopesGiftDialog liveRedEnvelopesGiftDialog;//直播间收下礼物弹窗
    private LiveRedEnvelopesDialog liveRedEnvelopesDialog;//直播间抢红包弹窗
    private int mClickNum = 0;//记录当前用户点赞数量
    private int totalClickNum = 0;//记录推送过来的点赞数量
    private LinearLayout centerLayout;
    private ShapeTextView centerEdit;

    TongLianPhoneBindDialog tongLianPhoneBindDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_look_live;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        checkNetWorkStatusAndAudio();//判断网络状况
        EventBus.getDefault().register(this);
        stopOnlineVideoFloat();
        SpUtils.store(LibApplication.getAppContext(), courseId + "Pass", true);
        handler = new Handler();
        chatRoomPresenter = new ChatRoomPresenter(this, this);
        lookLivePresenter = new LookLivePresenter(this, this);
        liveShoppingBagDialogPresenter = new LiveShoppingBagDialogPresenter(this, this);
        initChatRoomAdapter();
        getData();
        TongLianMemberData tongLianMemberData= ObjUtil.getObj(SpUtils.getValue(LibApplication.getAppContext(),SpKey.TONGLIANBIZUSER),TongLianMemberData.class);
        if(tongLianMemberData!=null&&tongLianMemberData.memberInfo.isPhoneChecked) {//绑定了

        }else {
            if (tongLianPhoneBindDialog == null) {
                tongLianPhoneBindDialog = TongLianPhoneBindDialog.newInstance();
            }
            tongLianPhoneBindDialog.show(getSupportFragmentManager(), "手机绑定");
            tongLianPhoneBindDialog.setOnDialogAgreeClickListener(new TongLianPhoneBindDialog.OnDialogAgreeClickListener() {
                @Override
                public void onDialogAgree() {

                }
            });
        }

    }

    private Handler clickHandler = new Handler() {//控制点赞数量的handler
        public void handleMessage(Message msg) {

        }
    };

    @Override
    public void getData() {
        super.getData();
        lookLivePresenter.getLiveRoomInfo(new SimpleHashMapBuilder<String, Object>().puts("courseId", courseId));
    }

    private void initLive() {
        liveShopingIcon.setVisibility(View.VISIBLE);
        editLayout.setVisibility(View.VISIBLE);
        topView.setVisibility(View.VISIBLE);
        liveShareIcon.setVisibility(View.VISIBLE);
        bottomPlayerLayout.setVisibility(View.GONE);
        bottomLayout.setVisibility(View.VISIBLE);
        if (status == 1) {//未开始
            showToast("当前直播未开始");
            goodsRecycle.setVisibility(View.GONE);
            bottomLayout.setVisibility(View.GONE);
        } else if (status == 3) {//直播已经结束
            showToast("当前直播未生成回放");
            goodsRecycle.setVisibility(View.GONE);
            bottomLayout.setVisibility(View.GONE);
            topView.setVisibility(View.GONE);
            helpLayout.setVisibility(View.GONE);
            centerHeartLayout.setVisibility(View.GONE);
            editLayout.setVisibility(View.GONE);
            if (liveErrorDialog != null) {
                liveErrorDialog.dismiss();
            }
            isCloseLive = true;
            liveErrorDialog = LiveErrorDialog.newInstance();
            liveErrorDialog.setType(5);
            liveErrorDialog.show(getSupportFragmentManager(), "异常");
            liveErrorDialog.setClickListener(new LiveErrorDialog.OnErrClickListener() {
                @Override
                public void onClick(int type) {
                    if (type == 5) {
                        finish(1);
                    }
                }
            });
            SpUtils.store(mContext, SpKey.LIVETMPCOURSEADDRESS, null);
            SpUtils.store(mContext, SpKey.LIVETMPCOURSEID, null);
        } else if (status == 4) {//回放
            helpLayout.setVisibility(View.GONE);
            centerHeartLayout.setVisibility(View.GONE);
            goodsRecycle.setVisibility(View.GONE);
            chatRecycler.setVisibility(View.GONE);
            mappingRgihtLin.setVisibility(View.GONE);
            mappingLeftLin.setVisibility(View.GONE);
            popularityLayout.setVisibility(View.GONE);
            bottomLayout.setVisibility(View.GONE);
            bottomPlayerLayout.setVisibility(View.VISIBLE);
        } else {
            goodsRecycle.setVisibility(View.VISIBLE);
            bottomLayout.setVisibility(View.VISIBLE);
            rightLayout.setVisibility(View.VISIBLE);
        }
        initPlayer();
        getChatRoomData();
    }

    private void getChatRoomData() {
        if (imMessageMgr != null) {
            imMessageMgr.unInitialize();
        }
        chatRoomPresenter.getChatRoomConfigure(new SimpleHashMapBuilder<String, Object>().puts("tourist", "0"));
        if (status == 2) {
            chatRoomPresenter.getLiveRoomMapping(new SimpleHashMapBuilder<String, Object>().puts("anchormanId", anchormanId).puts("roomDecorationType", "1"), 1);
            chatRoomPresenter.getLiveRoomMapping(new SimpleHashMapBuilder<String, Object>().puts("anchormanId", anchormanId).puts("roomDecorationType", "2"), 2);
        }
        if (status == 2) {
            liveShoppingBagDialogPresenter.getLiveRoomGoodsList(new SimpleHashMapBuilder<String, Object>().puts("liveActivityIds", activityIdList));
        } else if (status == 4) {
            liveShoppingBagDialogPresenter.getLiveRoomGoodsList(new SimpleHashMapBuilder<String, Object>().puts("liveActivityIds", activityIdList).puts("isEncore", 1));
        }
        liveShoppingBagDialogPresenter.getGoodsShopList(new SimpleHashMapBuilder<String, Object>().puts("shopId", shopId));
        chatRoomPresenter.getHost(Objects.requireNonNull(new SimpleHashMapBuilder<String, Object>().puts("anchormanId", anchormanId)));
        if (status == 2 || status == 4) {
            lookLivePresenter.addLookLiveNum(new SimpleHashMapBuilder<String, Object>().puts("courseId", courseId).puts("timesWatchedType", status == 2 ? "1" : (status == 4 ? "2" : "1")));
        }
        chatRoomPresenter.getFansNum(new SimpleHashMapBuilder<String, Object>()
                .puts("followedId", memberId));
        List list = getRandomNumList();
        if (list.size() > 0) {
            headIcon1.setImageResource(txArray[(int) list.get(0)]);
            headIcon2.setImageResource(txArray[(int) list.get(1)]);
            headIcon3.setImageResource(txArray[(int) list.get(2)]);
        }
    }

    private void initPlayer() {
        //创建 player 对象
        mLivePlayer = new TXLivePlayer(mContext);
        //关键 player 对象与界面 view
        mLivePlayer.setPlayerView(videoView);
        TXLivePlayConfig mPlayConfig = new TXLivePlayConfig();
        //自动模式
        mPlayConfig.setAutoAdjustCacheTime(true);
        mPlayConfig.setMinAutoAdjustCacheTime(1);
        mPlayConfig.setMaxAutoAdjustCacheTime(5);
        mLivePlayer.setConfig(mPlayConfig);
        if (status == 2) {
            String flvUrl = pullAddress;
            Log.e(TAG, "pullAddress:" + pullAddress);
            mLivePlayer.startPlay(flvUrl, TXLivePlayer.PLAY_TYPE_LIVE_RTMP);
        } else if (status == 4) {
            if (videoUrl.contains(",")) {
                videoUrls = videoUrl.split(",");
                if (videoUrls != null && videoUrls.length > 1) {
                    playerPosition = 0;
                    mLivePlayer.startPlay(videoUrls[0], TXLivePlayer.PLAY_TYPE_VOD_HLS);
                }
                showToast("该回放共有" + videoUrls.length + "段视频，当前段播放完后会自动播放下一段");
            } else {
                mLivePlayer.startPlay(videoUrl, TXLivePlayer.PLAY_TYPE_VOD_HLS);
            }
            mLivePlayer.prepareLiveSeek(videoUrl, 0);     // 后台请求直播起始时间
        }
        // 设置画面渲染方向
        mLivePlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
        //Android 示例代码
        mLivePlayer.setPlayListener(new ITXLivePlayListener() {
            @Override
            public void onPlayEvent(int event, Bundle param) {
                if (event == TXLiveConstants.PLAY_ERR_NET_DISCONNECT) {
                    if (liveErrorDialog != null && liveErrorDialog.getDialog() != null && liveErrorDialog.getDialog().isShowing()) {
                        return;
                    }
                    if (isAutoConnect) {//表示正在自动重连  先不要手动重连
                        return;
                    }
                    if (isCloseLive) {//表示已经关播  先不要手动重连
                        return;
                    }
                    if (!isHandConnect) {
                        showAgainDialog();
                    }
//                    try {
//                        CrashReport.postCatchedException(new Throwable("当前直播拉流失败  courseId==" + courseId + "   anchormanId==" + anchormanId
//                                + "   memberId==" + new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT))));
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                } else if (event == TXLiveConstants.PLAY_EVT_RTMP_STREAM_BEGIN) {
                    isFirstGet = false;
                    SpUtils.store(mContext, SpKey.LIVETMPCOURSEADDRESS, pullAddress);
                    SpUtils.store(mContext, SpKey.LIVETMPCOURSEID, courseId);
                    if (liveErrorDialog != null) {
                        liveErrorDialog.dismiss();
                    }
                    if (isAutoConnect) {
                        isAutoConnect = false;
                    }
                    if (isHandConnect) {
                        isHandConnect = false;
                    }
                    Log.e(TAG, "已经连接服务器，开始拉流");
                    if (liveShareType != null && liveShareType.equals("zbzl")) {
                        if (ChannelUtil.isIpRealRelease()) {
                            downTime(3 * 60 * 1000);
                        } else {
                            downTime(3 * 1000);//非正式环境3秒助力
                        }
                    }
                    chatRoomPresenter.getInteractionList(new SimpleHashMapBuilder<String, Object>().puts("merchantId", merchantId).puts("anchormanId", anchormanId));
                }
                if (event == TXLiveConstants.PLAY_EVT_PLAY_BEGIN) {
                    Log.e(TAG, "视频播放开始");
                }
                if (event == TXLiveConstants.PLAY_EVT_GET_PLAYINFO_SUCC) {
                    Log.e(TAG, "获取点播文件信息成功");
                }
                if (event == TXLiveConstants.PLAY_ERR_FILE_NOT_FOUND) {
                    Log.e(TAG, "播放文件不存在");
                }
                if (event == TXLiveConstants.PLAY_ERR_HEVC_DECODE_FAIL) {
                    Log.e(TAG, "H265 解码失败");
                }
                if (event == TXLiveConstants.PLAY_ERR_HLS_KEY) {
                    Log.e(TAG, "HLS 解码 key 获取失败");
                }
                if (event == TXLiveConstants.PLAY_ERR_GET_PLAYINFO_FAIL) {
                    Log.e(TAG, "获取点播文件信息失败");
                }
                if (event == TXLiveConstants.PLAY_EVT_PLAY_PROGRESS) {
                    int str2 = param.getInt("EVT_MSG");
                    int str3 = param.getInt("EVT_PLAY_PROGRESS_MS");
                    int str4 = param.getInt("EVT_PLAYABLE_DURATION_MS");
                    int str5 = param.getInt("EVT_PLAY_DURATION");
                    int str7 = param.getInt("EVT_PLAYABLE_DURATION");
                    totalSecond = param.getInt("EVT_PLAY_DURATION_MS");//视频总时长  秒数
                    currentSecond = param.getInt("EVT_PLAY_PROGRESS");//当前播放进度  秒数
                    //Log.e(TAG, "视频总时长：" + totalSecond);
                    //Log.e(TAG, "当前播放到：" + currentSecond);
                    setProgress();
                }
                if (event == TXLiveConstants.PLAY_WARNING_SEVER_CONN_FAIL) {
                    SpUtils.store(mContext, SpKey.LIVETMPCOURSEADDRESS, null);
                    SpUtils.store(mContext, SpKey.LIVETMPCOURSEID, null);
                    showToast("服务器连接失败");
                    Log.e(TAG, "服务器连接失败（仅播放 RTMP:// 地址时会抛送）");
                }
                if (event == TXLiveConstants.PLAY_WARNING_RECONNECT) {
                    if (liveErrorDialog != null && liveErrorDialog.getDialog() != null && liveErrorDialog.getDialog().isShowing()) {
                        return;
                    }
                    if (!isAutoConnect) {
                        isAutoConnect = true;
                        getData();
                        isFirstGet = false;
                        liveErrorDialog = LiveErrorDialog.newInstance();
                        liveErrorDialog.setType(1);
                        liveErrorDialog.show(getSupportFragmentManager(), "异常");
                        liveErrorDialog.setClickListener(new LiveErrorDialog.OnErrClickListener() {
                            @Override
                            public void onClick(int type) {
                                if (type == 1) {
                                    if (!isCloseLive) {
                                        showAgainDialog();
                                    }
                                }
                            }
                        });
                    }
                }
                if (event == TXLiveConstants.PLAY_EVT_CHANGE_RESOLUTION) {
                    int width = param.getInt("EVT_PARAM1");
                    int height = param.getInt("EVT_PARAM2");
                    setInsideModel(width, height);
                }
            }

            @Override
            public void onNetStatus(Bundle status) {
            }
        });

    }

    private void setInsideModel(int width, int height) {
        // 设置填充模式
        if (height > 0) {
            double size = (double) width / height;
            if (size > 1.0) {
                mLivePlayer.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION);
            } else {
                mLivePlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
            }
        } else {
            mLivePlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
        }
    }

    private void showAgainDialog() {
        if (!NetUtil.isNetworkAvalible(mContext)) {
            Log.e(TAG, "网络坏的，拉流失败，走手动重连");
            Toast.makeText(mContext, "网络异常，请检查您的网络连接", Toast.LENGTH_LONG).show();
        }
        if (liveErrorDialog != null) {
            liveErrorDialog.dismiss();
        }
        SpUtils.store(mContext, SpKey.LIVETMPCOURSEADDRESS, null);
        SpUtils.store(mContext, SpKey.LIVETMPCOURSEID, null);
        isHandConnect = true;
        isFirstGet = false;
        liveErrorDialog = LiveErrorDialog.newInstance();
        liveErrorDialog.setType(2);
        liveErrorDialog.show(getSupportFragmentManager(), "异常");
        liveErrorDialog.setClickListener(new LiveErrorDialog.OnErrClickListener() {
            @Override
            public void onClick(int type) {
                if (type == 2) {
                    isAutoConnect = false;//重置状态位
                    liveErrorDialog.dismiss();
                    lookLivePresenter.getAnchormanLiveing(new SimpleHashMapBuilder<String, Object>().puts("anchormanId", anchormanId).puts("isDebug", isDebug));
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mLivePlayer != null) {
            // 继续
            mLivePlayer.resume();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                PreventKeyboardBlockUtil.getInstance().setContext(mActivity).setBtnView(editLayout).register();
            }
        }, 1500);
        if (lookLivePresenter != null && chatRoomPresenter != null) {
            if (accountId != null) {
                lookLivePresenter.getFollowInfo(new SimpleHashMapBuilder<String, Object>()
                        .puts("fansId", new String(Base64.decode(SpUtils.getValue(getApplicationContext(), SpKey.USER_ID).getBytes(), Base64.DEFAULT)))
                        .puts("followedId", memberId));
                chatRoomPresenter.getFansNum(new SimpleHashMapBuilder<String, Object>()
                        .puts("followedId", memberId));
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mLivePlayer != null) {
            // 暂停
            mLivePlayer.pause();
        }
        PreventKeyboardBlockUtil.getInstance().setContext(mActivity).unRegister();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLivePlayer != null) {
            mLivePlayer.pause();
            mLivePlayer.stopPlay(true); // true 代表清除最后一帧画面
            videoView.onDestroy();
        }
        outGroup();
        if (liveErrorDialog != null) {
            liveErrorDialog = null;
        }
        EventBus.getDefault().unregister(this);
    }

    public void finish(int type) {
        if (type == 1) {
            SpUtils.store(mContext, SpKey.LIVETMPCOURSEADDRESS, null);//确认关闭了 则删除小窗的可能性 如果要继续弹出小窗 则不要滞空的关闭 比如点缩小的时候区分下
            SpUtils.store(mContext, SpKey.LIVETMPCOURSEID, null);//确认关闭了 则删除小窗的可能性 如果要继续弹出小窗 则不要滞空
        } else {
            if (FloatWindowManager.isHideShow) {
                FloatWindowManager.isHideShow = false;//重置下
            }

        }
        finish();
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
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            finish(1);
            //不执行父类点击事件
            return true;
        }
        //继续执行父类其他点击事件
        return super.onKeyUp(keyCode, event);
    }

    private void initChatRoomAdapter() {
        pushLiveChatRoomAdapter = new PushLiveChatRoomAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        chatRecycler.setLayoutManager(layoutManager);
        chatRecycler.setAdapter(pushLiveChatRoomAdapter);
        pushLiveChatRoomAdapter.setData(new SimpleArrayListBuilder<ChatMessage>());

        liveLookGoodsListAdapter = new LiveLookGoodsListAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        goodsRecycle.setLayoutManager(linearLayoutManager);
        goodsRecycle.setAdapter(liveLookGoodsListAdapter);
        //PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        //pagerSnapHelper.attachToRecyclerView(goodsRecycle);
    }

    @Override
    protected void findViews() {
        super.findViews();
        activityView = (ConstraintLayout) findViewById(R.id.activityView);
        videoView = (TXCloudVideoView) findViewById(R.id.video_view);
        viewStatus = (View) findViewById(R.id.viewStatus);
        topView = (ConstraintLayout) findViewById(R.id.topView);
        anchormanLayout = (ConstraintLayout) findViewById(R.id.anchormanLayout);
        anchorImg = (CornerImageView) findViewById(R.id.anchorImg);
        nameLayout = (LinearLayout) findViewById(R.id.nameLayout);
        anchorName = (TextView) findViewById(R.id.anchorName);
        anchorFanceNum = (TextView) findViewById(R.id.anchorFanceNum);
        followBtn = (TextView) findViewById(R.id.followBtn);
        closeImg = (ImageView) findViewById(R.id.closeImg);
        popularityLayout = (ConstraintLayout) findViewById(R.id.popularityLayout);
        headNowLL = (RelativeLayout) findViewById(R.id.headNowLL);
        headIcon1 = (CornerImageView) findViewById(R.id.head_icon1);
        headIcon2 = (CornerImageView) findViewById(R.id.head_icon2);
        headIcon3 = (CornerImageView) findViewById(R.id.head_icon3);
        groupNums = (TextView) findViewById(R.id.groupNums);
        topBannerImg = findViewById(R.id.topBannerImg);
        purseLayout = (LinearLayout) findViewById(R.id.purseLayout);
        helpLayout = (ConstraintLayout) findViewById(R.id.helpLayout);
        mappingLeftLin = (LinearLayout) findViewById(R.id.mappingLeftLin);
        mappingRgihtLin = (LinearLayout) findViewById(R.id.mappingRgihtLin);
        centerHeartLayout = (RelativeLayout) findViewById(R.id.centerHeart_layout);
        superLikeLayout = (SuperLikeLayout) findViewById(R.id.super_like_layout);
        topTipsLayout = (LinearLayout) findViewById(R.id.topTipsLayout);
        liveTipsTxt = (TextView) findViewById(R.id.liveTipsTxt);
        addLayout = (LinearLayout) findViewById(R.id.addLayout);
        userNickName = (TextView) findViewById(R.id.userNickName);
        chartContent = (TextView) findViewById(R.id.chartContent);
        chatRecycler = (MaxHeightRecyclerView) findViewById(R.id.chatRecycler);
        goodsRecycle = (RecyclerView) findViewById(R.id.goodsRecycle);
        bottomLayout = (ConstraintLayout) findViewById(R.id.bottomLayout);
        editLayout = (ConstraintLayout) findViewById(R.id.editLayout);
        chatEdit = (EditText) findViewById(R.id.chatEdit);
        rightLayout = (ConstraintLayout) findViewById(R.id.rightLayout);
        shopCartRel = (RelativeLayout) findViewById(R.id.shop_cart_rel);
        liveShopingIcon = (ImageView) findViewById(R.id.live_shoping_icon);
        shopCartNum = (TextView) findViewById(R.id.shop_cart_num);
        liveClickLike = (ImageView) findViewById(R.id.live_click_like);
        liveShareIcon = (ImageView) findViewById(R.id.live_share_icon);
        liveMiniIcon = (ImageView) findViewById(R.id.live_mini_icon);
        liveFriendLinkIcon = (ImageView) findViewById(R.id.live_friend_link_icon);
        clickNum = (TextView) findViewById(R.id.clickNum);
        bottomPlayerLayout = (LinearLayout) findViewById(R.id.bottomPlayerLayout);
        shopCartRel2 = (RelativeLayout) findViewById(R.id.shop_cart_rel2);
        liveShopingIcon2 = (ImageView) findViewById(R.id.live_shoping_icon2);
        shopCartNum2 = (TextView) findViewById(R.id.shop_cart_num2);
        startTime = (TextView) findViewById(R.id.startTime);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        endTime = (TextView) findViewById(R.id.endTime);
        heartLayout = (TCHeartLayout) findViewById(R.id.heart_layout);
        livePlayerImg = (ImageView) findViewById(R.id.livePlayerImg);
        centerLayout = (LinearLayout) findViewById(R.id.centerLayout);
        centerEdit = (ShapeTextView) findViewById(R.id.centerEdit);
        seekBar.setOnSeekBarChangeListener(this);
        superLikeLayout.setProvider(BitmapProviderFactory.getHDProvider(this));
        centerHeartLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] itemPosition = new int[2];
                int[] superLikePosition = new int[2];
                v.getLocationOnScreen(itemPosition);
                superLikeLayout.getLocationOnScreen(superLikePosition);
                int x = v.getWidth() / 2 - (int) TransformUtil.dp2px(mContext, 30f);
                int y = (v.getHeight() + (int) TransformUtil.dp2px(mContext, 160f)) / 2;
                superLikeLayout.launch(x, y);
                buildClick();
            }
        });
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
                            .puts("fromMemberId", new String(Base64.decode(SpUtils.getValue(LibApplication.getAppContext(), SpKey.USER_ID).getBytes(), Base64.DEFAULT)))//助力分享记得填
                            .puts("liveShareType", "zbzl")//普通分享 zbzl助力分享记得改
                            .puts("merchantId", merchantId)
                            .puts("shopId", shopId)
                    );
                    seckShareDialog.show(getSupportFragmentManager(), "LookLiveShare");
                }
            }
        });
        editLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStopSpeak && joinGroupSuccess) {
                    showInput(chatEdit);
                } else {
//                    if (ShuttedUntil != null) {
//                        isAgree("提示", new String("您已被主播禁言至" + stampToTime(ShuttedUntil)));
//                    }
                    hideInput();
                }
            }
        });
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outGroup();
                finish(1);
            }
        });
        liveClickLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildClick();
                heartLayout.addFavor();//点赞动画
            }
        });
        anchorImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(TencentLiveRoutes.LiveHostMain)
                        .withString("anchormanId", anchormanId)
                        .navigation();
            }
        });
        anchorName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(TencentLiveRoutes.LiveHostMain)
                        .withString("anchormanId", anchormanId)
                        .navigation();
            }
        });
        shopCartRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGoodsBag();
            }
        });
        shopCartRel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGoodsBag();
            }
        });
        activityView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status == 4) {//回放点播视频
                    if (mLivePlayer.isPlaying()) {//正在播放
                        livePlayerImg.setImageDrawable(getResources().getDrawable(R.drawable.live_player_play));
                    } else {
                        livePlayerImg.setImageDrawable(getResources().getDrawable(R.drawable.live_player_stop));
                    }
                    livePlayerImg.setVisibility(View.VISIBLE);
                    if (livePlayerImg.getVisibility() == View.VISIBLE) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                livePlayerImg.setVisibility(View.GONE);
                            }
                        }, 4000);

                    }
                }
            }
        });
        livePlayerImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLivePlayer != null) {
                    if (mLivePlayer.isPlaying()) {//正在播放
                        livePlayerImg.setImageDrawable(getResources().getDrawable(R.drawable.live_player_stop));
                        mLivePlayer.pause();
                    } else {
                        livePlayerImg.setImageDrawable(getResources().getDrawable(R.drawable.live_player_play));
                        mLivePlayer.resume();
                    }
                    if (livePlayerImg.getVisibility() == View.VISIBLE) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                livePlayerImg.setVisibility(View.GONE);
                            }
                        }, 4000);

                    }
                }
            }
        });
        helpLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liveHelpMarketingDialog = LiveHelpMarketingDialog.newInstance();
                liveHelpMarketingDialog.setCourseId(courseId, mLiveRoomInfo, shopId != null ? shopId : SpUtils.getValue(mContext, SpKey.CHOSE_SHOP), merchantId);
                liveHelpMarketingDialog.setGroupId(groupId);
                liveHelpMarketingDialog.show(getSupportFragmentManager(), "");
            }
        });
        liveMiniIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(2);
            }
        });
    }

    private void showGoodsBag() {
        liveShoppingBagDialog = LiveShoppingBagDialog.newInstance();
        liveShoppingBagDialog.setList(activityIdList);
        liveShoppingBagDialog.setAnchormanId(anchormanId);
        liveShoppingBagDialog.setCourseId(courseId);
        liveShoppingBagDialog.setMerchantId(merchantId);
        liveShoppingBagDialog.setShopId(shopId);
        liveShoppingBagDialog.setLiveInfo(mLiveRoomInfo);
        if (status == 4) {
            liveShoppingBagDialog.setHistoryLive(true);
        }
        liveShoppingBagDialog.show(getSupportFragmentManager(), "商品");
        shareLiveGoodsId=null;
    }

    private void buildClick() {
        mClickNum++;
        anchorFanceNum.setText(String.format("%s本场点赞", ParseUtils.parseNumber((mClickNum + totalClickNum) + "", 10000, "万")));
        getSuccessClickNum(mClickNum + totalClickNum);
        clickHandler.removeCallbacksAndMessages(null);
        clickHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                lookLivePresenter.clickLive(new SimpleHashMapBuilder<String, Object>()
                        .puts("courseId", courseId)
                        .puts("imMemberId", accountId)
                        .puts("num", mClickNum));//增加点赞量
                mClickNum = 0;
            }
        }, 1000);
    }

    @Override
    public void onError(int code, String errInfo) {
        Log.e(TAG, "IM执行出错：" + errInfo + "——错误编号:" + code);
    }

    @Override
    public void onSuccess(Object... args) {
        Log.e(TAG, "IM执行成功：" + args.toString());
    }

    @Override
    public void onConnected() {

    }

    @Override
    public void onDisconnected() {
        if (imMessageMgr == null) {
            imMessageMgr = new IMMessageMgr(mContext);
            imMessageMgr.setIMMessageListener(LookLiveActivity.this);
        }
        imMessageMgr.initialize(accountId, userSig
                , Integer.parseInt(sdkAppId), this);//这块重新初始化  并且重新加入下群组试试
//        try {
//            CrashReport.postCatchedException(new Throwable("当前用户腾讯Im断开连接，已走重连操作  courseId==" + courseId + "   anchormanId==" + anchormanId
//                    + "   memberId==" + new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)) + "   groupId==" + groupId));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onLoginSuccess() {
        if (status == 4) {
            return;
        }
        imMessageMgr.joinGroup(groupId, new IMMessageMgr.Callback() {
            @Override
            public void onError(int code, String errInfo) {
                joinGroupSuccess = false;
                Log.e(TAG, "IM：加入群组失败,正在重试第" + reconnection + "次");
                if (reconnection < 2) {
                    reconnection++;
                    if (imMessageMgr == null) {
                        imMessageMgr = new IMMessageMgr(mContext);
                        imMessageMgr.setIMMessageListener(LookLiveActivity.this);
                    }
                    imMessageMgr.initialize(accountId, userSig
                            , Integer.parseInt(sdkAppId), this);//这块重新初始化  并且重新加入下群组试试
                } else {
                    Toast.makeText(mContext, "加入聊天群组失败，暂时不能发消息，请退出直播间重进试试~", Toast.LENGTH_LONG).show();
                    try {
                        CrashReport.postCatchedException(new Throwable("当前用户加入直播群失败，重新加入三次无效，已提醒用户重新进入直播间  courseId==" + courseId + "   anchormanId==" + anchormanId
                                + "   memberId==" + new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)) + "   groupId==" + groupId));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.e(TAG, "前用户加入直播群失败，重新加入三次无效，已提醒用户重新进入直播间,错误已上报");
                }
                SpUtils.store(mContext, SpKey.LIVETMPCOURSEADDRESS, null);
                SpUtils.store(mContext, SpKey.LIVETMPCOURSEID, null);
            }

            @Override
            public void onSuccess(Object... args) {
                showContent();
                isStopSpeak = true;
                joinGroupSuccess = true;
                Log.e(TAG, "IM：加入群组成功");
                lookLivePresenter.getNoSpeakInfo(new SimpleHashMapBuilder<String, Object>().puts("accountId", accountId).puts("groupId", groupId));
                try {
                    if (status == 2) {
                        chatRoomPresenter.addLookLivePeopleNum(new SimpleHashMapBuilder<String, Object>().puts("groupId", groupId)
                                .puts("memberId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)))
                                .puts("type", "0")
                                .puts("referral_code", referral_code));
                    }
                    if (status == 4) {
                        chatRoomPresenter.addLookLivePeopleNum(new SimpleHashMapBuilder<String, Object>().puts("groupId", groupId)
                                .puts("memberId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)))
                                .puts("type", "1"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onjoiniGroupSuccess() {
        chatEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //当actionId == XX_SEND 或者 XX_DONE时都触发
                //或者event.getKeyCode == ENTER 且 event.getAction == ACTION_DOWN时也触发
                //注意，这是一定要判断event != null。因为在某些输入法上会返回null。
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    //处理事件
                    //发送消息
                    if (joinGroupSuccess) {
//                        if (isStopSpeak) {
                        if (chatEdit.getText().toString().trim() != null && chatEdit.getText().toString().trim().length() > 0) {
                            chatRoomPresenter.sendTxtMessage(new SimpleHashMapBuilder<String, Object>().puts("accountId", accountId)
                                    .puts("groupId", groupId).puts("msg", chatEdit.getText().toString()));
                        } else {
                            showToast("不能发送空消息！");
                        }
//                        } else {
//                            showToast("您已被禁言");
//                        }
                    } else {
                        Toast.makeText(mContext, "加入聊天群组失败，暂时不能发消息，请退出直播间重进试试~", Toast.LENGTH_LONG).show();
                    }
                    hideInput();
                }
                return false;
            }
        });
        centerEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (joinGroupSuccess) {
//                        if (isStopSpeak) {
                    if (chatEdit.getText().toString().trim() != null && chatEdit.getText().toString().trim().length() > 0) {
                        chatRoomPresenter.sendTxtMessage(new SimpleHashMapBuilder<String, Object>().puts("accountId", accountId)
                                .puts("groupId", groupId).puts("msg", chatEdit.getText().toString()));
                    } else {
                        showToast("不能发送空消息！");
                    }
//                        } else {
//                            showToast("您已被禁言");
//                        }
                } else {
                    Toast.makeText(mContext, "加入聊天群组失败，暂时不能发消息，请退出直播间重进试试~", Toast.LENGTH_LONG).show();
                }
                hideInput();
            }
        });
        chatEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    centerEdit.setVisibility(View.VISIBLE);
                    rightLayout.setVisibility(View.GONE);
                } else {
                    centerEdit.setVisibility(View.GONE);
                    rightLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onjoiniGroupFail() {//加入群组失败了
        chatEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //当actionId == XX_SEND 或者 XX_DONE时都触发
                //或者event.getKeyCode == ENTER 且 event.getAction == ACTION_DOWN时也触发
                //注意，这是一定要判断event != null。因为在某些输入法上会返回null。
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    //处理事件
                    //发送消息
                    if (joinGroupSuccess) {
//                        if (isStopSpeak) {
                        if (chatEdit.getText().toString().trim() != null && chatEdit.getText().toString().trim().length() > 0) {
                            chatRoomPresenter.sendTxtMessage(new SimpleHashMapBuilder<String, Object>().puts("accountId", accountId)
                                    .puts("groupId", groupId).puts("msg", chatEdit.getText().toString()));
                        } else {
                            showToast("不能发送空消息！");
                        }
//                        } else {
//                            showToast("您已被禁言");
//                        }
                    } else {
                        Toast.makeText(mContext, "加入聊天群组失败，暂时不能发消息，请退出直播间重进试试~", Toast.LENGTH_LONG).show();
                    }
                    hideInput();
                }
                return false;
            }
        });
    }

    @Override
    public void onPusherChanged() {

    }

    @Override
    public void onGroupTextMessage(String groupID, String senderID, String userName, String message) {
        if (message != null) {
            pushLiveChatRoomAdapter.addDatas(new SimpleArrayListBuilder<ChatMessage>().adds(new ChatMessage(userName, message, 1, senderID)));
            totalMessageList.add(new ChatMessage(userName, message, 1, senderID));
            chatRecycler.scrollToPosition(pushLiveChatRoomAdapter.getItemCount() - 1);
        } else {
            try {
                CrashReport.postCatchedException(new Throwable("message为空  courseId==" + courseId + "   anchormanId==" + anchormanId
                        + "   memberId==" + new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)) + "message==" + message + " userName== " + userName));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onGroupCustomMessage(String groupID, String senderID, String userName, String CmdPara, String CmdType) {
        if (CmdPara != null && "Message".equals(CmdType)) {//消息
            pushLiveChatRoomAdapter.addDatas(new SimpleArrayListBuilder<ChatMessage>().adds(new ChatMessage(userName, CmdPara, 2, senderID)));
            totalMessageList.add(new ChatMessage(userName, CmdPara, 2, senderID));
            chatRecycler.scrollToPosition(pushLiveChatRoomAdapter.getItemCount() - 1);
        } else if (CmdPara != null && "Curtain".equals(CmdType)) {//置顶消息刷新
            refreshTips(CmdPara, liveTipsTxt, topTipsLayout);
        } else if (CmdPara != null && "Change_Focus".equals(CmdType)) {//更换讲解商品
            if (status == 2) {
                liveShoppingBagDialogPresenter.getLiveRoomGoodsList(new SimpleHashMapBuilder<String, Object>().puts("liveActivityIds", activityIdList));
            } else if (status == 4) {
                liveShoppingBagDialogPresenter.getLiveRoomGoodsList(new SimpleHashMapBuilder<String, Object>().puts("liveActivityIds", activityIdList).puts("isEncore", 1));
            }
        } else if (CmdPara != null && "Refresh_Card".equals(CmdType)) {//刷新购物袋
            if (status == 2) {
                liveShoppingBagDialogPresenter.getLiveRoomGoodsList(new SimpleHashMapBuilder<String, Object>().puts("liveActivityIds", activityIdList));
            } else if (status == 4) {
                liveShoppingBagDialogPresenter.getLiveRoomGoodsList(new SimpleHashMapBuilder<String, Object>().puts("liveActivityIds", activityIdList).puts("isEncore", 1));
            }
        } else if (CmdPara != null && "Set_Live_Name".equals(CmdType)) {//更新直播名称

        } else if (CmdPara != null && "Refresh_Drawing".equals(CmdType)) {//更新贴图
            if (status == 2) {
                chatRoomPresenter.getLiveRoomMapping(new SimpleHashMapBuilder<String, Object>().puts("anchormanId", anchormanId).puts("roomDecorationType", "1"), 1);
            }
        } else if (CmdPara != null && "Refresh_Carousel".equals(CmdType)) {//更新轮播图
            if (status == 2) {
                chatRoomPresenter.getLiveRoomMapping(new SimpleHashMapBuilder<String, Object>().puts("anchormanId", anchormanId).puts("roomDecorationType", "2"), 2);
            }
        } else if ("Refresh_Single_Goods".equals(CmdType)) {//刷新单个商品
            if (status == 2) {
                liveShoppingBagDialogPresenter.getLiveRoomGoodsList(new SimpleHashMapBuilder<String, Object>().puts("liveActivityIds", activityIdList));
            } else if (status == 4) {
                liveShoppingBagDialogPresenter.getLiveRoomGoodsList(new SimpleHashMapBuilder<String, Object>().puts("liveActivityIds", activityIdList).puts("isEncore", 1));
            }
            if (liveShoppingBagDialog != null) {
                liveShoppingBagDialog.changeList(activityIdList);
            }
        } else if ("Refresh_Goods_List".equals(CmdType)) {//刷新商品列表
            if (status == 2) {
                lookLivePresenter.getLiveRoomInfo(new SimpleHashMapBuilder<String, Object>().puts("courseId", courseId));
            }
        } else if ("Refresh_Current_Goods".equals(CmdType)) {//刷新当前讲解商品
            if (status == 2) {
                liveShoppingBagDialogPresenter.getLiveRoomGoodsList(new SimpleHashMapBuilder<String, Object>().puts("liveActivityIds", activityIdList));
            } else if (status == 4) {
                liveShoppingBagDialogPresenter.getLiveRoomGoodsList(new SimpleHashMapBuilder<String, Object>().puts("liveActivityIds", activityIdList).puts("isEncore", 1));
            }
            if (liveShoppingBagDialog != null) {
                liveShoppingBagDialog.changeList(activityIdList);
            }
        } else if ("Speechless".equals(CmdType)) {//禁言用户通知
            if (status == 2) {
                lookLivePresenter.getNoSpeakInfo(new SimpleHashMapBuilder<String, Object>().puts("accountId", accountId).puts("groupId", groupId));
            }
        } else if ("Refresh_Fans_Count".equals(CmdType)) {//刷新粉丝数
//            if (CmdPara != null && !TextUtils.isEmpty(CmdPara)) {
//                if ("0".equals(CmdPara)) {
//                    anchorFanceNum.setText("暂无粉丝");
//                } else {
//                    anchorFanceNum.setText(CmdPara + " 粉丝");
//                }
//            }
        } else if ("Refresh_Popularity".equals(CmdType)) {//刷新人气
            if (CmdPara != null) {
                refreshPopularity(CmdPara);
            }
        } else if ("Notify_Goods_List".equals(CmdType)) {//刷新商品数量
            if (CmdPara != null && liveVideoGoods != null) {
                refreshGoods(CmdPara);
            }
        } else if ("Refresh_Interactive_List".equals(CmdType)) {//刷新互动活动列表
            chatRoomPresenter.getInteractionList(new SimpleHashMapBuilder<String, Object>().puts("merchantId", merchantId).puts("anchormanId", anchormanId));
        } else if ("Refresh_Red_Packet".equals(CmdType)) {//刷新红包
            if (CmdPara != null) {
                try {
                    buildRedGift(new JSONObject(CmdPara).get("id").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else if ("Refresh_Star_Count".equals(CmdType)) {//刷新点赞数量
            if (CmdPara != null && !TextUtils.isEmpty(CmdPara)) {
                if ("0".equals(CmdPara)) {
                    anchorFanceNum.setText("暂无点赞");
                } else {
                    if (Integer.parseInt(CmdPara) <= totalClickNum + mClickNum) {
                        return;
                    }
                    totalClickNum = Integer.parseInt(CmdPara);
                    anchorFanceNum.setText(String.format("%s本场点赞", ParseUtils.parseNumber(CmdPara, 10000, "万")));
                }
                getSuccessClickNum(Integer.parseInt(CmdPara));
            }
        } else if ("Refresh_Help".equals(CmdType)) {//刷新助力活动
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
        if (fictitious <= historyLookNum) {//按最大的人气数量显示
            return;
        }
        historyLookNum = fictitious;
        //刷新人气更新红包人气
        if (liveRedEnvelopesDialog != null) {
            liveRedEnvelopesDialog.setPopularity(historyLookNum);
        }
        if (fictitious > 0) {
            popularityLayout.setVisibility(View.VISIBLE);
        } else {
            popularityLayout.setVisibility(View.GONE);
        }
        if (fictitious > 9999) {
            groupNums.setText(ParseUtils.parseNumber(fictitious + "", 10000, "万") + "人气");
        } else {
            groupNums.setText(fictitious + "人气");
        }
    }

    @Override
    public void onGroupDestroyed(String groupID) {

    }

    @Override
    public void onDebugLog(String log) {
        Log.e(TAG, "IM：" + log);
    }

    @Override
    public void onGroupMemberEnter(String groupID, List<V2TIMGroupMemberInfo> users) {
        if (users != null) {
//            pushLiveChatRoomAdapter.addDatas(new SimpleArrayListBuilder<ChatMessage>().adds(new ChatMessage(users.get(users.size() - 1).getNickName(), "加入直播间", 3, users.get(users.size() - 1).getUserID())));
//            totalMessageList.add(new ChatMessage(users.get(users.size() - 1).getNickName(), "加入直播间", 3, users.get(users.size() - 1).getUserID()));
//            chatRecycler.scrollToPosition(pushLiveChatRoomAdapter.getItemCount() - 1);
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
            chartContent.setText("加入直播间");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    addLayout.setVisibility(View.GONE);
                }
            }, 3000);

        } else {
            try {
                CrashReport.postCatchedException(new Throwable("加入直播间信息为空  courseId==" + courseId + "   anchormanId==" + anchormanId
                        + "   memberId==" + new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT))));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onGroupMemberExit(String groupID, V2TIMGroupMemberInfo users) {
        Log.e(TAG, users.getNickName() + "退出了直播间");
    }

    @Override
    public void onForceOffline() {
        try {
            CrashReport.postCatchedException(new Throwable("当前用户账号多端进入直播间，此设备IM聊天室已被强制下线,courseId==" + courseId + "   anchormanId==" + anchormanId
                    + "   memberId==" + new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID) != null ? SpUtils.getValue(mContext, SpKey.USER_ID).getBytes() : null, Base64.DEFAULT))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override//成员信息更变通知  包括禁言信息
    public void onGroupMemberInfoChanged(String groupID, List<V2TIMGroupMemberChangeInfo> v2TIMGroupMemberChangeInfoList) {
//        if (v2TIMGroupMemberChangeInfoList != null) {
//            for (int i = 0; i < v2TIMGroupMemberChangeInfoList.size(); i++) {
//                if (accountId.equals(v2TIMGroupMemberChangeInfoList.get(i).getUserID())) {
//                    V2TIMGroupMemberChangeInfo v2TIMGroupMemberChangeInfo = v2TIMGroupMemberChangeInfoList.get(i);
//                    if (v2TIMGroupMemberChangeInfo.getMuteTime() > 0) {//说明被禁言
//                        isStopSpeak = false;
//                        chatEdit.setHint("您已被禁言");
//                        //showToast("您已被禁言" + v2TIMGroupMemberChangeInfo.getMuteTime() + "秒");
//                    } else {
//                        isStopSpeak = true;
//                    }
//                }
//            }
//        }
    }


    @Override
    public void getChatRoomConfigureSuccess(ChatRoomConfigure result) {
        if (result != null && result.account != null) {
            accountId = result.account.accountId;
            chatRoomPresenter.setChatRoomInfo(new SimpleHashMapBuilder<String, Object>().puts("groupId", groupId).puts("accountId", result.account.accountId));
            lookLivePresenter.getFollowInfo(new SimpleHashMapBuilder<String, Object>().puts("fansId", new String(Base64.decode(SpUtils.getValue(getApplicationContext(), SpKey.USER_ID).getBytes(), Base64.DEFAULT)))
                    .puts("followedId", memberId));
            if (result.sdkAppId != null && result.userSig != null) {
                sdkAppId = result.sdkAppId;
                userSig = result.userSig;
                //初始化IM
                imMessageMgr = new IMMessageMgr(this);
                imMessageMgr.setIMMessageListener(this);
                imMessageMgr.initialize(result.account.accountId, userSig
                        , Integer.parseInt(sdkAppId), this);
            }
        } else {//获取聊天室配置信息失败，则不能加入聊天室  不能聊天
            isStopSpeak = false;
            joinGroupSuccess = false;
            try {
                CrashReport.postCatchedException(new Throwable("获取聊天室配置信息失败  courseId==" + courseId + "   anchormanId==" + anchormanId
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
            Log.e(TAG, "发送文本消息成功");
            chatEdit.setText("");
            hideInput();
        } else {
            showToast("发送消息失败");
            try {
                CrashReport.postCatchedException(new Throwable("发送消息失败  courseId==" + courseId + "   anchormanId==" + anchormanId
                        + "   memberId==" + new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT))));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void sendCustomerTxtMessageSuccess(MessageSendCode result) {

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
                        GlideCopy.with(mContext)
                                .load(result.iconDtoList.get(0).iconPath)
                                .error(R.drawable.img_1_1_default)
                                .placeholder(R.drawable.img_1_1_default)

                                .into(new SimpleTarget<Drawable>() {
                                    @Override
                                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                        int swidth = ScreenUtils.getScreenWidth(mContext) - (int) TransformUtil.dp2px(mContext, 20);
                                        int height = (int) ((resource.getIntrinsicHeight() * 1.0 / resource.getIntrinsicWidth()) * swidth);
                                        height = swidth / 2;
                                        ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) topBannerImg.getLayoutParams();
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
            GlideCopy.with(mContext)
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
//            if ("0".equals(result)) {
//                anchorFanceNum.setText("暂无粉丝");
//            } else {
//                anchorFanceNum.setText(result + " 粉丝");
//            }
//
//        } else {
//            anchorFanceNum.setText("暂无粉丝");
//        }
    }

    @Override
    public void onSuccessGetLookNum(OnLineNum result) {

    }

    @Override
    public void onSuccessGetInteractionList(List<Interaction> result) {
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
                    if (interaction.status == 0) {//待开奖
                        lotteryTime(interaction.lotteryTime, purseTxt);
                    } else if (interaction.status == 1) {//开奖中
                        purseTxt.setText("开奖中");
                    } else if (interaction.status == 2) {//已开奖
                        purseTxt.setText("开奖结束");
                    }
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            liveGetGiftDialog = LiveGetGiftDialog.newInstance();
                            liveGetGiftDialog.setId(interaction.id, courseId);
                            liveGetGiftDialog.setShopId(shopId != null ? shopId : SpUtils.getValue(mContext, SpKey.CHOSE_SHOP));
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
    public void onAddLookLiveNumSuccess() {

    }

    @Override
    public void onClickLiveSuccess() {
    }

    @Override
    public void onGetFollowInfoSuccess(boolean result) {
        followBtn.setVisibility(View.VISIBLE);
        if (result) {//已关注
            isFollow = true;
            followBtn.setText("");
            followBtn.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.flow_icon), null, null, null);
            followBtn.setPadding((int) TransformUtil.dp2px(mContext, 22), (int) TransformUtil.dp2px(mContext, 2), (int) TransformUtil.dp2px(mContext, 18), (int) TransformUtil.dp2px(mContext, 4));
            followBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //取关
                    lookLivePresenter.clickFollow(new SimpleHashMapBuilder<String, Object>()
                            .puts("fansId", new String(Base64.decode(SpUtils.getValue(getApplicationContext(), SpKey.USER_ID).getBytes(), Base64.DEFAULT)))
                            .puts("followedId", memberId)
                            .puts("status", "2"));
                }
            });
        } else {
            followBtn.setText("关注");
            isFollow = false;
            followBtn.setPadding((int) TransformUtil.dp2px(mContext, 8), (int) TransformUtil.dp2px(mContext, 4), (int) TransformUtil.dp2px(mContext, 8), (int) TransformUtil.dp2px(mContext, 4));
            followBtn.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.add_white), null, null, null);
            followBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //关注
                    lookLivePresenter.clickFollow(new SimpleHashMapBuilder<String, Object>()
                            .puts("fansId", new String(Base64.decode(SpUtils.getValue(getApplicationContext(), SpKey.USER_ID).getBytes(), Base64.DEFAULT)))
                            .puts("followedId", memberId)
                            .puts("status", "1"));
                }
            });
        }
    }

    @Override
    public void onClickFollowSuccess(String result) {
        if (isFollow) {
            showToast("已取消");
        } else {
            showToast("已关注");
        }
        lookLivePresenter.getFollowInfo(new SimpleHashMapBuilder<String, Object>()
                .puts("fansId", new String(Base64.decode(SpUtils.getValue(getApplicationContext(), SpKey.USER_ID).getBytes(), Base64.DEFAULT)))
                .puts("followedId", memberId));
        chatRoomPresenter.getFansNum(new SimpleHashMapBuilder<String, Object>()
                .puts("followedId", memberId));
    }

    @Override
    public void getSuccessLiveRoomInfo(LiveRoomInfo result) {
        if (result != null) {
            if (result.status == 4 && result.isPlayback == 0) {//不允许观看回放
                showToast("当前直播回放已经删除");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1500);
                return;
            }
            try {
                if (result.liveAnchorman != null && result.status == 2) {
                    //如果当前用户的memberId跟主播的一样，并且正在直播中
                    String memberId = new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT));
                    if (memberId.equals(result.liveAnchorman.memberId)) {
                        showToast("主播账号无法同时观看自己正在进行的直播，请更换其他账号观看直播");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 1500);
                        return;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            result.courseId = courseId;
            this.mLiveRoomInfo = result;
            if (isFirstGet) {
                if (!SpUtils.getValue(mContext, SpKey.CHOSE_MC).equals(result.merchantId)) {//已选的当前直播的不一致
                    if (TextUtils.isEmpty(shopId)) {//进入的门店不为空
                        shopId = result.shopId;
                        //目前可能为空 也可能不是综合门店 还需要做进一步的清洗
                    }
                    merchantId = result.merchantId;
                    if (shopId != null) {
                        liveShoppingBagDialogPresenter.getMerchantGoodsShopList(new SimpleHashMapBuilder<String, Object>()
                                .puts("shopId", result.shopId)
                                .puts("shopTypeList", "2,3".split(",")));
                    } else {
                        liveShoppingBagDialogPresenter.getMerchantGoodsShopList(new SimpleHashMapBuilder<String, Object>()
                                .puts("merchantId", merchantId)
                                .puts("shopTypeList", "2,3".split(",")));
                    }

                } else {
                    if (TextUtils.isEmpty(merchantId)) {
                        merchantId = SpUtils.getValue(mContext, SpKey.CHOSE_MC);
                    }
                    if (TextUtils.isEmpty(shopId)) {
                        shopId = SpUtils.getValue(mContext, SpKey.CHOSE_SHOP);
                    }
                }
                liveLookGoodsListAdapter.setMerchantId(merchantId);
                liveLookGoodsListAdapter.setShopId(shopId, anchormanId, courseId);
                liveLookGoodsListAdapter.setLiveInfo(this.mLiveRoomInfo);
                videoUrl = result.videoUrl;
                fileId = result.fileId;
                status = result.status;
                pullAddress = result.pullAddress;
                groupId = result.groupId;
                anchormanId = result.anchormanId;
                activityIdList = result.activityIdList;
                memberId = result.liveAnchorman.memberId;
                isDebug = result.isDebug;
                isFirstGet = false;
                initLive();
                if (status == 2) {
                    liveShoppingBagDialogPresenter.getLiveRoomGoodsList(new SimpleHashMapBuilder<String, Object>().puts("liveActivityIds", activityIdList));
                } else if (status == 4) {
                    liveShoppingBagDialogPresenter.getLiveRoomGoodsList(new SimpleHashMapBuilder<String, Object>().puts("liveActivityIds", activityIdList).puts("isEncore", 1));
                }
                if (liveShoppingBagDialog != null) {
                    liveShoppingBagDialog.changeList(activityIdList);
                }
                lookLivePresenter.getFollowInfo(new SimpleHashMapBuilder<String, Object>()
                        .puts("fansId", new String(Base64.decode(SpUtils.getValue(getApplicationContext(), SpKey.USER_ID).getBytes(), Base64.DEFAULT)))
                        .puts("followedId", memberId));
                stitle = result.courseTitle;
                GlideCopy.with(mContext)
                        .load(result.picUrl)
                        .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                sBitmap = DrawableUtils.drawableToBitmap(resource);
                            }
                        });
            } else {
                if (result.isClosed == 0) {
                    isAutoConnect = false;//重置状态位
                    isFirstGet = true;
                    lookLivePresenter.getAnchormanLiveing(new SimpleHashMapBuilder<String, Object>().puts("anchormanId", anchormanId));
                } else {
                    isCloseLive = true;
                    if (liveErrorDialog != null) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (liveErrorDialog == null) {
                                    return;
                                }
                                liveErrorDialog.dismiss();
                                liveErrorDialog = LiveErrorDialog.newInstance();
                                liveErrorDialog.setType(4);
                                liveErrorDialog.show(getSupportFragmentManager(), "异常");
                                liveErrorDialog.setClickListener(new LiveErrorDialog.OnErrClickListener() {
                                    @Override
                                    public void onClick(int type) {
                                        if (type == 4) {
                                            finish(1);
                                            isCloseLive = false;
                                        }
                                    }
                                });
                            }
                        }, 1000);
                    }
                    SpUtils.store(mContext, SpKey.LIVETMPCOURSEADDRESS, null);
                    SpUtils.store(mContext, SpKey.LIVETMPCOURSEID, null);
                }
            }
            liveShoppingBagDialogPresenter.getJackpotList(new SimpleHashMapBuilder<String, Object>()
                    .puts("pageNum", "1")
                    .puts("pageSize", 100)
                    .puts("courseId", courseId)
                    .puts("memberId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT))), 0);
        } else {
            //获取直播间信息失败了  管他是第一次进来还是自动重连的  都弹手动刷新
            showToast("获取直播间信息失败");
            if (!isCloseLive) {
                showAgainDialog();
            }
        }
    }

    @Override
    public void getSuccessAgain(LiveRoomInfo result) {
        if (result != null) {
            videoUrl = result.videoUrl;
            status = result.status;
            pullAddress = result.pullAddress;
            groupId = result.groupId;
            anchormanId = result.anchormanId;
            activityIdList = result.activityIdList;
            memberId = result.liveAnchorman.memberId;
            if (isFirstGet) {
                isFirstGet = false;
                initPlayer();
                getChatRoomData();
            }
            if (status == 2) {
                liveShoppingBagDialogPresenter.getLiveRoomGoodsList(new SimpleHashMapBuilder<String, Object>().puts("liveActivityIds", activityIdList));
            } else if (status == 4) {
                liveShoppingBagDialogPresenter.getLiveRoomGoodsList(new SimpleHashMapBuilder<String, Object>().puts("liveActivityIds", activityIdList).puts("isEncore", 1));
            }
            if (liveShoppingBagDialog != null) {
                liveShoppingBagDialog.changeList(activityIdList);
            }
            lookLivePresenter.getFollowInfo(new SimpleHashMapBuilder<String, Object>()
                    .puts("fansId", new String(Base64.decode(SpUtils.getValue(getApplicationContext(), SpKey.USER_ID).getBytes(), Base64.DEFAULT)))
                    .puts("followedId", memberId));
            GlideCopy.with(mContext)
                    .load(result.picUrl)
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            sBitmap = DrawableUtils.drawableToBitmap(resource);
                        }
                    });
            stitle = result.courseTitle;
            if (liveErrorDialog != null) {
                liveErrorDialog.dismiss();
            }
            liveShoppingBagDialogPresenter.getJackpotList(new SimpleHashMapBuilder<String, Object>()
                    .puts("pageNum", "1")
                    .puts("pageSize", 100)
                    .puts("courseId", courseId)
                    .puts("memberId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT))), 0);
        } else {
            isCloseLive = true;
            if (liveErrorDialog != null) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        liveErrorDialog.dismiss();
                        liveErrorDialog = LiveErrorDialog.newInstance();
                        liveErrorDialog.setType(4);
                        liveErrorDialog.show(getSupportFragmentManager(), "异常");
                        liveErrorDialog.setClickListener(new LiveErrorDialog.OnErrClickListener() {
                            @Override
                            public void onClick(int type) {
                                if (type == 4) {
                                    finish(1);
                                    isCloseLive = false;
                                }
                            }
                        });
                    }
                }, 1000);
            }
            SpUtils.store(mContext, SpKey.LIVETMPCOURSEADDRESS, null);
            SpUtils.store(mContext, SpKey.LIVETMPCOURSEID, null);
        }
    }

    @Override
    public void getSuccessClickNum(int count) {
        if (count > 9999) {
            clickNum.setText("9999+");
        } else {
            if (count > 0) {
                clickNum.setText(count + "");
            } else {
                clickNum.setText("");
            }
        }
    }

    @Override
    public void getSuccessNoSpeakInfo(String Member_Account, String ShuttedUntil) {
        chatEdit.setHint("聊点什么吧～");
        isStopSpeak = true;
        if (Member_Account == null && ShuttedUntil == null) {
            return;
        }
        if (ShuttedUntil != null && !TextUtils.isEmpty(ShuttedUntil)) {
            isStopSpeak = false;
            chatEdit.setHint("您已被禁言");
            //你已被禁言，剩余xx分钟”
            showToast(String.format("您已被禁言，剩余%s分钟", DateUtils.getDistanceHour(stampToTime(ShuttedUntil), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))));
        } else {
            chatEdit.setHint("聊点什么吧～");
            isStopSpeak = true;
        }

    }

    @Override
    public void getLiveRoomGoodsListSuccess(List<LiveVideoGoods> result) {
        liveLookGoodsListAdapter.clear();
        liveLookGoodsListAdapter.setActivity(this);
        if (result != null && result.size() > 0) {
            if (status == 2) {
                liveVideoGoods = result;
                if (liveShoppingBagDialog != null) {
                    liveShoppingBagDialog.changeGoodsList(result);
                }
                goodsRecycle.setVisibility(View.VISIBLE);
                List<LiveVideoGoods> mList = new ArrayList<>();
                mList.clear();
                for (int i = 0; i < result.size(); i++) {
                    if (result.get(i).isSpeak == 1) {
                        mList.add(result.get(i));
                        //goodsRecycle.scrollToPosition(i);
                    }
                }
                if (mList.size() == 0) {
                    goodsRecycle.setVisibility(View.GONE);
                } else {
                    goodsRecycle.setVisibility(View.VISIBLE);
                    liveLookGoodsListAdapter.clear();
                    liveLookGoodsListAdapter.setData((ArrayList) mList);
                }
            } else {
                goodsRecycle.setVisibility(View.GONE);
            }
            shopCartRel.setVisibility(View.VISIBLE);
            shopCartRel2.setVisibility(View.VISIBLE);
            shopCartNum.setVisibility(View.VISIBLE);
            shopCartNum2.setVisibility(View.VISIBLE);
            shopCartNum.setText(result.size() + "");
            shopCartNum2.setText(result.size() + "");
        } else {
            shopCartRel.setVisibility(View.GONE);
            shopCartRel2.setVisibility(View.GONE);
            goodsRecycle.setVisibility(View.GONE);
        }
        if(shareLiveGoodsId!=null){
            int shareIndex=ListUtil.checkObjIsInListWithResult(new SimpleArrayListBuilder<String>().putList(result, new ObjectIteraor<LiveVideoGoods>() {
                @Override
                public String getDesObj(LiveVideoGoods object) {
                    return object.id;
                }
            }),shareLiveGoodsId);
            if(shareIndex!=-1){
                LiveVideoGoods tmpshow=result.get(shareIndex);
                boolean isHistoryLive=false;
                if (status == 4) {
                    isHistoryLive=true;
                }
                if (isHistoryLive) {
                    if (tmpshow.availableInventory <= 0 || tmpshow.getGoodsChildrenInventory() <= 0) {//表示已经售完

                    } else {
                        goLiveGoods(tmpshow);
                    }
                } else {
                    if (tmpshow.offShelf == 0) {//正在上架抢购
                        if (tmpshow.availableInventory <= 0 ) {//表示已经售完

                        } else {
                            goLiveGoods(tmpshow);
                        }
                    } else if (tmpshow.offShelf == 1) {//可查看商品  但未开始抢购

                    }
                }
            }
        }
        shareLiveGoodsId=null;
    }

    @Override
    public void onGetStoreListSuccess(final List<ShopDetailModel> result) {
        if (result != null && result.size() > 0) {
            liveLookGoodsListAdapter.setOutClickListener(new BaseAdapter.OnOutClickListener() {
                @Override
                public void outClick(@NotNull String function, @NotNull Object obj) {
                    if ("click".equals(function)) {
                        LiveVideoGoods liveVideoGoods = (LiveVideoGoods) obj;
                        goLiveGoods(liveVideoGoods);
                    }
                }
            });
        }
    }

    @Override
    public void onGetMerchantStoreListSuccess(List<ShopDetailModel> result) {
        if (result != null && result.size() > 0) {
            if (TextUtils.isEmpty(shopId)) {//如果shopid是空需要找到可用的最近的门店
                shopId = result.get(0).id;//shopid取到最近的了
                if (liveLookGoodsListAdapter != null) {
                    liveLookGoodsListAdapter.setShopId(shopId, anchormanId, courseId);
                }
            } else {//开始判断shopid是否合法 是不是标品或者综合
                if (!ListUtil.checkObjIsInList(new SimpleArrayListBuilder<String>().putList(result, new ObjectIteraor<ShopDetailModel>() {
                    @Override
                    public String getDesObj(ShopDetailModel o) {
                        return o.id;
                    }
                }), shopId)) {
                    shopId = result.get(0).id;//shopid取到最近的了
                    if (liveLookGoodsListAdapter != null) {
                        liveLookGoodsListAdapter.setShopId(shopId, anchormanId, courseId);
                    }
                }
            }
        }

    }

    @Override
    public void getSuccessJackpotList(List<JackpotList> result, PageInfoEarly pageInfo, int type) {
        if (result != null && result.size() > 0) {
            if (status == 2) {
                helpLayout.setVisibility(View.VISIBLE);
            } else {
                helpLayout.setVisibility(View.GONE);
            }
            if (liveHelpMarketingDialog != null) {
                liveHelpMarketingDialog.refresh();
            }
        } else {
            if (type == 1) {
                showToast("当前直播助力活动已关闭");
                if (liveHelpMarketingDialog != null) {
                    liveHelpMarketingDialog.dismiss();
                }
            }
            helpLayout.setVisibility(View.GONE);
        }
    }

    private void goLiveGoods(final LiveVideoGoods liveVideoGoods) {
        String errMsg = null;
        if (liveVideoGoods.offShelf == 0) {//上架可以抢
            if (liveVideoGoods.availableInventory == 0) {
                errMsg = "已抢完";
            }
        } else {
            errMsg = "等待主播开启抢购";

        }
        GoodsSimpleDialog goodsSimpleDialog = new GoodsSimpleDialog();
        goodsSimpleDialog.setErrMsg(errMsg)
                .setSeachMap(new SimpleHashMapBuilder<String, Object>().puts(Functions.FUNCTION, "9292").puts("liveGoodsId", liveVideoGoods.id))
                .setLiveInfo(mLiveRoomInfo)
                .setOnDialogButtonOrderListener(new GoodsSimpleDialog.OnGoodsDialogOrderButtonListener() {
                    @Override
                    public void onOrderClick(GoodsSpecDetail goodsSpecDetailNew) {
                        if (goodsSpecDetailNew.getAvailableInventory() < goodsSpecDetailNew.salesMin) {//可够小于最小起购商品库存不足
                            showToast("已抢光");
                            return;
                        }
                        GoodsBasketCell.GoodsMarketing goodsMarketing = new GoodsBasketCell.GoodsMarketing(null);
                        goodsMarketing.availableInventory = goodsSpecDetailNew.getAvailableInventory();
                        goodsMarketing.mapMarketingGoodsId = liveVideoGoods.id;
                        goodsMarketing.marketingType = "-1";
                        goodsMarketing.id = goodsSpecDetailNew.id;
                        goodsMarketing.marketingPrice = goodsSpecDetailNew.marketingPrice;
                        goodsMarketing.pointsPrice = 0;
                        goodsMarketing.salesMin = liveVideoGoods.salesMin;
                        goodsMarketing.salesMax = liveVideoGoods.salesMax;
                        GoodsBasketCell goodsBasketCell = new GoodsBasketCell(goodsSpecDetailNew.marketingPrice,
                                goodsSpecDetailNew.marketingPrice,
                                0,
                                (liveVideoGoods.isShopTakeOnly == 1 ? 1 : 2) + "",
                                "0",
                                "0", null);
                        goodsBasketCell.goodsSpecDesc = goodsSpecDetailNew.goodsSpecStr;
                        goodsBasketCell.goodsStock = goodsSpecDetailNew.getAvailableInventory();
                        goodsBasketCell.goodsMarketingDTO = goodsMarketing;
                        goodsBasketCell.mchId = merchantId;
                        goodsBasketCell.goodsId = liveVideoGoods.goodsId;
                        try {
                            goodsBasketCell.setGoodsSpecId(goodsSpecDetailNew.goodsChildId);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        goodsBasketCell.goodsTitle = liveVideoGoods.goodsTitle;
                        goodsBasketCell.goodsImage = liveVideoGoods.headImages.get(0).filePath;
                        goodsBasketCell.setGoodsQuantity(Integer.parseInt(goodsSpecDetailNew.getCount()) == 0 ? 1 : Integer.parseInt(goodsSpecDetailNew.getCount()));
                        GoodsBasketGroup goodsBasketGroup = new GoodsBasketGroup(goodsBasketCell);
                        List<GoodsBasketCell> list = new ArrayList<>();
                        goodsBasketCell.shopIdList = liveVideoGoods.shopIds;
                        goodsBasketCell.ischeck = true;
                        goodsBasketCell.goodsShopId = goodsSpecDetailNew.shopDetailModelSelect.id;
                        goodsBasketCell.goodsShopName = goodsSpecDetailNew.shopDetailModelSelect.shopName;
                        goodsBasketCell.goodsShopAddress = goodsSpecDetailNew.shopDetailModelSelect.addressDetails;
                        list.add(goodsBasketCell);

                        ARouter.getInstance()
                                .build(ServiceRoutes.SERVICE_ORDER)
                                .withString("visitShopId", goodsSpecDetailNew.shopDetailModelSelect.id)
                                .withString("token", null)
                                .withString("course_id", null)
                                .withString("liveStatus", null)
                                .withString("live_anchor", anchormanId)
                                .withString("live_course", courseId)
                                .withObject("goodsbasketlist", list)
                                .withString("goodsMarketingType", "-1")
                                .navigation();
                    }
                })
                .show(getSupportFragmentManager(), "商品弹窗");
    }

    private void outGroup() {//退出群组
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                V2TIMManager.getInstance().quitGroup(groupId, new V2TIMCallback() {
                    @Override
                    public void onError(int i, String s) {
                        Log.e(TAG, "退出群组失败");
                    }

                    @Override
                    public void onSuccess() {
                        Log.e(TAG, "退出群组成功");
                    }
                });
            }
        });
        if (imMessageMgr != null) {
            //反初始化IM
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

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        //进度发生改变时会触发
        seekBar.setProgress(seekBar.getProgress());
        startTime.setText(FormatRunTime(seekBar.getProgress()));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        //按住SeekBar时会触发
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        //放开SeekBar时触发
        if (mLivePlayer != null) {
            mLivePlayer.seek(seekBar.getProgress());
            mLivePlayer.resumeLive();//恢复播放
        }
    }

    private void setProgress() {
        if (!isSetSeekBar) {
            seekBar.setMax(totalSecond / 1000);
            startTime.setText(FormatRunTime(currentSecond));
            endTime.setText(FormatRunTime(totalSecond / 1000));
            isSetSeekBar = true;
        }
        seekBar.setProgress(currentSecond);
        startTime.setText(FormatRunTime(currentSecond));
        if (currentSecond == totalSecond / 1000) {//说明进度条走完了
            mLivePlayer.pause();
            playerPosition++;
            if (videoUrls != null && videoUrls.length > playerPosition) {
                mLivePlayer.stopPlay(true);
                isSetSeekBar = false;
                mLivePlayer.startPlay(videoUrls[playerPosition], TXLivePlayer.PLAY_TYPE_VOD_HLS);
                mLivePlayer.prepareLiveSeek(videoUrl, 0);
            } else {
                livePlayerImg.setVisibility(View.VISIBLE);
                livePlayerImg.setImageDrawable(getResources().getDrawable(R.drawable.live_player_stop));
            }
        }
    }

    private void downTime(int millis) {//会员加入助力（用户进入直播间"三分钟"[需求]后调用）
        new CountDownTimer(millis, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                this.cancel();
                chatRoomPresenter.addHelp(new SimpleHashMapBuilder<String, Object>().puts("liveCourseId", courseId)
                        .puts("fromMemberId", fromMemberId != null ? fromMemberId : new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)))
                        .puts("memberId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT))));
            }
        }.start();
    }

    @Override
    public void onSuccessAddHelp(String result) {
        if (result.contains("成功")) {

        }
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

    public void lotteryTime(final String time, final TextView purseTxt) {
        if (time != null) {
            long mSeconds = DateUtils.getDistanceSec(time, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            new CountDownTimer(mSeconds * 1000, 1000) {
                public void onTick(long millisUntilFinished) {
                    if (millisUntilFinished / 1000 > 0) {
                        purseTxt.setText(getTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), time) + "后开始开奖");
                    } else {
                        purseTxt.setText("开奖中");
                        chatRoomPresenter.getInteractionList(new SimpleHashMapBuilder<String, Object>().puts("merchantId", merchantId).puts("anchormanId", anchormanId));
                    }
                }

                public void onFinish() {
                    purseTxt.setText("开奖中");
                    chatRoomPresenter.getInteractionList(new SimpleHashMapBuilder<String, Object>().puts("merchantId", merchantId).puts("anchormanId", anchormanId));
                    this.cancel();
                }
            }.start();
        }
    }

    private void isAgree(String title, String msg) {
        StyledDialog.init(this);
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


            }
        }).setBtnColor(R.color.dialogutil_text_black, R.color.colorPrimaryDark, 0).setBtnText("取消", "确定").show();
    }

    private void initView() {

    }

}