package com.health.discount.activity;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.health.discount.R;
import com.health.discount.adapter.NewAssembleFooterAdapter;
import com.health.discount.adapter.NewAssembleHeaderAdapter;
import com.health.discount.contract.NewAssembleContract;
import com.health.discount.presenter.NewAssemblePresenter;
import com.health.discount.widget.KKGroupDialog;
import com.healthy.library.BuildConfig;
import com.healthy.library.LibApplication;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.business.SeckShareDialog;
import com.healthy.library.businessutil.ChannelUtil;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.constant.SpKey;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.model.KKGroup;
import com.healthy.library.model.MemberAction;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.presenter.ActionPresenterCopy;
import com.healthy.library.presenter.CardBoomPresenter;
import com.healthy.library.routes.DiscountRoutes;
import com.healthy.library.utils.ResUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.StringUtils;
import com.healthy.library.widget.AutoClickImageView;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMMin;
import com.umeng.socialize.media.UMWeb;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(path = DiscountRoutes.DIS_NEWASSEMBLEACTIVITY)//拼团列表
public class NewAssembleActivity extends BaseActivity implements NewAssembleHeaderAdapter.OnActShareClickListener, IsFitsSystemWindows, NewAssembleContract.View, OnRefreshLoadMoreListener {

    @Autowired
    String areaNo;
    @Autowired
    String lng;
    @Autowired
    String lat;
    @Autowired
    String shopId;

    private ImageView img_back;
    private AutoClickImageView mall_scrollUp;
    private NewAssembleHeaderAdapter newAssembleHeaderAdapter;
    private NewAssembleFooterAdapter newAssembleFooterAdapter;
    private SmartRefreshLayout layoutRefresh;
    private RecyclerView recycler;
    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;

    private Map<String, Object> topMap = new HashMap<>();
    private Map<String, Object> map = new HashMap<>();
    private NewAssemblePresenter newAssemblePresenter;
    private int currentPage = 1;

    String surl;
    String sdes;
    String stitle;
    String spath;
    private Bitmap sBitmap;
    boolean isshare = false;

    private Map<String, SHARE_MEDIA> mPlatformMap = new HashMap<>();
    private ImageView shareImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        newAssemblePresenter = new NewAssemblePresenter(this, this);
        init(savedInstanceState);
        mPlatformMap.clear();
        mPlatformMap.put(ResUtils.getStrById(this, R.string.lib_share_wx), SHARE_MEDIA.WEIXIN);
        mPlatformMap.put(ResUtils.getStrById(this, R.string.lib_share_circle), SHARE_MEDIA.WEIXIN_CIRCLE);
        mPlatformMap.put(ResUtils.getStrById(this, R.string.lib_share_qq), SHARE_MEDIA.QQ);
        mPlatformMap.put(ResUtils.getStrById(this, R.string.lib_share_qzone), SHARE_MEDIA.QZONE);
        mPlatformMap.put(ResUtils.getStrById(this, R.string.lib_share_sina), SHARE_MEDIA.SINA);
        areaNo = LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE);
        lng = LocUtil.getLongitude(mContext, SpKey.LOC_ORG);
        lat = LocUtil.getLatitude(mContext, SpKey.LOC_ORG);
        if(TextUtils.isEmpty(shopId)){
            shopId = SpUtils.getValue(mContext,SpKey.CHOSE_SHOP);
        }
        new CardBoomPresenter(mContext).boom("1");
        EventBus.getDefault().register(this);
        new ActionPresenterCopy(LibApplication.getAppContext()).posAction(
                new SimpleHashMapBuilder<>()
                        .putObject(new MemberAction(
                                BuildConfig.VERSION_NAME,
                                1,
                                4,
                                getActivitySimpleName(),
                                getActivitySimpleName(),
                                new String(Base64.decode(SpUtils.getValue(getApplication(), SpKey.USER_ID).getBytes(), Base64.DEFAULT)))
                        ));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        Map nokmap = new HashMap<String, String>();
        nokmap.put("soure", "拼团活动列表页浏览时长");
        nokmap.put("time", (System.currentTimeMillis() - mills) / 1000);
        MobclickAgent.onEvent(mContext, "event_APP_PinList_Stop", nokmap);

        if(mall_scrollUp != null){
            mall_scrollUp.destroy();
        }

    }

    private long mills = System.currentTimeMillis();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_new_assemble;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        virtualLayoutManager = new VirtualLayoutManager(this);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        recycler.setLayoutManager(virtualLayoutManager);
        recycler.setAdapter(delegateAdapter);

        newAssembleHeaderAdapter = new NewAssembleHeaderAdapter();
        delegateAdapter.addAdapter(newAssembleHeaderAdapter);
        newAssembleHeaderAdapter.setOnActShareClickListener(this);

        newAssembleFooterAdapter = new NewAssembleFooterAdapter();
        delegateAdapter.addAdapter(newAssembleFooterAdapter);
        newAssembleFooterAdapter.setLocation(areaNo, lat, lng);
    }

    @Override
    public void getData() {
        super.getData();
        topMap.put("addressProvince", (Integer.parseInt(!TextUtils.isEmpty(areaNo) ? areaNo : "0") / 10000 * 10000) + "");
        topMap.put("latitude", lat);
        topMap.put("longitude", lng);
        newAssemblePresenter.getAssembleTopList(topMap);
        map.put("addressProvince", (Integer.parseInt(!TextUtils.isEmpty(areaNo) ? areaNo : "0") / 10000 * 10000) + "");
        map.put("latitude", lat);
        map.put("longitude", lng);
        map.put("pageSize", "10");
        map.put("currentPage", currentPage + "");
        map.put("shopId", shopId);
        map.put("faceUrlNum", "3");//获取头像的数量
        newAssemblePresenter.getAssembleList(map);
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentPage = 1;
        getData();
        if (isshare) {
            KKGroupDialog.newInstance().setResult(lessman).setOnDialogShareclickListener(new KKGroupDialog.DialogShareclickListener() {
                @Override
                public void onClick(SHARE_MEDIA share_media) {
                    buildDes(kkGroupDetail, lessman, bitmap);
//                    shareMin(SHARE_MEDIA.WEIXIN, surl, sdes, stitle, spath);
                    share(SHARE_MEDIA.WEIXIN,surl,sdes,stitle);
                }
            }).show(getSupportFragmentManager(), "kkOkDialog");
            isshare = false;
        }
    }


    /**
     * 分享
     *
     * @param shareMedia 分享平台
     * @param url        链接地址
     * @param des        描述
     * @param title      标题
     */
    private void share(SHARE_MEDIA shareMedia, String url, String des, String title) {
        //System.out.println(url);
        UMWeb web = new UMWeb(url);
        web.setTitle(title);
        web.setThumb(new UMImage(mContext, sBitmap));
        web.setDescription(des);
        new ShareAction(this)
                .withMedia(web)
                .setPlatform(shareMedia)
                .setCallback(this)
                .share();
    }

    private void buildDes(KKGroup kkGroupDetail, String lessman, Bitmap bitmap) {
        sBitmap = bitmap;
        String urlPrefix = SpUtils.getValue(mContext, UrlKeys.H5_BargainUrl);
        String url = String.format("%s?type=8&scheme="+"assembleing"+"&shopId=%s&referral_code=%s&teamNum=%s", urlPrefix,shopId,SpUtils.getValue(mContext,SpKey.GETTOKEN),kkGroupDetail.teamNum + "");
        surl = url;
        String path = String.format("/pages/home/markting/marketStatus/index?shopId=%s&teamNum=%s&referral_code=%s",
                SpUtils.getValue(mContext, SpKey.CHOSE_SHOP), kkGroupDetail.teamNum + "", SpUtils.getValue(mContext, SpKey.GETTOKEN));
        spath = path;
        //System.out.println("分享的连接" + surl);
        stitle = "【仅剩" + lessman + "个名额】跟我拼团￥" + StringUtils.getResultPrice(kkGroupDetail.assemblePrice + "") + "买“" + kkGroupDetail.goodsTitle + "”";
        sdes = "活动火热进行中，快快加入吧";
    }

    @Override
    public void onSuccessGetTopAssembleList(List<KKGroup> kickList) {
        if (kickList == null || kickList.size() == 0) {
            newAssembleHeaderAdapter.setModel(null);
            return;
        }
        List dataList = new ArrayList();
        dataList.clear();
        dataList.add(kickList);
        newAssembleHeaderAdapter.setData((ArrayList) dataList);
    }

    @Override
    public void onSuccessGetAssembleList(List<KKGroup> kickList, PageInfoEarly pageInfoEarly) {
        if (pageInfoEarly == null) {
            newAssembleFooterAdapter.setModel(null);
            layoutRefresh.setEnableLoadMore(false);
            return;
        }
        currentPage = pageInfoEarly.currentPage;
        if (currentPage == 1 || currentPage == 0) {
            newAssembleFooterAdapter.setData((ArrayList) kickList);
        } else {
            newAssembleFooterAdapter.addDatas((ArrayList) kickList);
        }
        if (pageInfoEarly.isMore != 1) {
            layoutRefresh.finishLoadMoreWithNoMoreData();
        } else {
            layoutRefresh.setNoMoreData(false);
            layoutRefresh.setEnableLoadMore(true);
        }
    }


    @Override
    protected void findViews() {
        super.findViews();
        img_back = findViewById(R.id.img_back);
        layoutRefresh = findViewById(R.id.layout_refresh);
        recycler = findViewById(R.id.recycler);
        mall_scrollUp = findViewById(R.id.mall_scrollUp);
        shareImg = findViewById(R.id.shareImg);
        shareImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(mContext, "event2APPPinListShareClick",
                        new SimpleHashMapBuilder<String, String>().puts("soure", "拼团活动列表顶部分享按钮点击量"));

                SeckShareDialog dialog = SeckShareDialog.newInstance();
                //dialog.setBitmap(3, bitmap);
                dialog.setActivityDialog(3, 2, ListUtil.isEmpty(newAssembleFooterAdapter.getDatas()) ? null : newAssembleFooterAdapter.getDatas().get(0));
                dialog.show(getSupportFragmentManager(), "分享");
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        layoutRefresh.setOnRefreshLoadMoreListener(this);
        mall_scrollUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(DiscountRoutes.DIS_MYASSEMBLEACTIVITY)
                        .withString("areaNo", areaNo)
                        .withString("lat", lat)
                        .withString("lng", lng)
                        .navigation();
            }
        });
        mall_scrollUp.setClicktype("null");
        mall_scrollUp.startLoopScaleAuto();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        currentPage++;
        getData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        currentPage = 1;
        getData();
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        layoutRefresh.finishRefresh();
        layoutRefresh.finishLoadMore();
    }

    /**
     * 分享
     *
     * @param shareMedia 分享平台
     * @param url        链接地址
     * @param des        描述
     * @param title      标题
     */
    private void shareMin(SHARE_MEDIA shareMedia, String url, String des, String title, String path) {
        isshare = true;
        //System.out.println(url);
        UMMin umMin = new UMMin(url);
        umMin.setTitle(title);
        umMin.setThumb(new UMImage(mContext, sBitmap));
        umMin.setDescription(des);
        umMin.setPath(path);
        umMin.setUserName("gh_f9b4fbd9d3b8");
        if (ChannelUtil.isRealRelease()) {

        } else {
            com.umeng.socialize.Config.setMiniPreView();
        }
//        com.umeng.socialize.Config.setMiniTest();
        new ShareAction(this)
                .withMedia(umMin)
                .setPlatform(shareMedia)
                .setCallback(this)
                .share();
    }

    KKGroup kkGroupDetail;
    String lessman;
    Bitmap bitmap;

    @Override
    public void clickShare(final KKGroup kkGroupDetail, final String lessman, final Bitmap bitmap) {
        this.kkGroupDetail = kkGroupDetail;
        this.lessman = lessman;
        this.bitmap = bitmap;
//        if(kkGroupDetail.teamMemberList.size()==1){
//            KKGroupDialog.newInstance().setCanfirst(false).setResult(lessman).setOnDialogShareclickListener(new KKGroupDialog.DialogShareclickListener() {
//                @Override
//                public void onClick(SHARE_MEDIA share_media) {
//                    buildDes(kkGroupDetail, lessman, bitmap);
//                    shareMin(SHARE_MEDIA.WEIXIN, surl, sdes, stitle,spath);
//                }
//            }).show(getSupportFragmentManager(), "kkOkDialog");
//        }else {
//            KKGroupDialog.newInstance().setResult(lessman).setOnDialogShareclickListener(new KKGroupDialog.DialogShareclickListener() {
//                @Override
//                public void onClick(SHARE_MEDIA share_media) {
//                    buildDes(kkGroupDetail, lessman, bitmap);
//                    shareMin(SHARE_MEDIA.WEIXIN, surl, sdes, stitle,spath);
//                }
//            }).show(getSupportFragmentManager(), "kkOkDialog");
//        }
        buildDes(kkGroupDetail, lessman, bitmap);
        shareMin(SHARE_MEDIA.WEIXIN, surl, sdes, stitle, spath);

    }
}