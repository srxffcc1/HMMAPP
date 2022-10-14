package com.health.discount.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.health.discount.R;
import com.health.discount.adapter.MyAssembleAdapter;
import com.health.discount.contract.MyAssembleContract;
import com.health.discount.presenter.MyAssemblePresenter;
import com.health.discount.widget.KKGroupDialog;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.businessutil.ChannelUtil;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.constant.SpKey;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.model.KKGroup;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.utils.ResUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.StringUtils;
import com.healthy.library.widget.StatusLayout;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMMin;
import com.umeng.socialize.media.UMWeb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MyAssembleFragment extends BaseFragment implements MyAssembleContract.View, OnRefreshLoadMoreListener, MyAssembleAdapter.OnActShareClickListener, BaseAdapter.OnOutClickListener {
    private com.scwang.smart.refresh.layout.SmartRefreshLayout layoutRefresh;
    private com.healthy.library.widget.StatusLayout layoutStatus;
    private RecyclerView recycler;
    private MyAssembleAdapter assembleAdapter;
    private Map<String, Object> listMap = new HashMap<>();
    private int currentPage = 1;
    private MyAssemblePresenter presenter;

    String surl;
    String sdes;
    String stitle;
    String spath;
    private Bitmap sBitmap;
    boolean isshare = false;
    private Map<String, SHARE_MEDIA> mPlatformMap = new HashMap<>();
    public MyAssembleFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public void onResume() {
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
            }).show(getChildFragmentManager(), "kkOkDialog");
            isshare = false;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_assemble;
    }

    @Override
    protected void findViews() {
        initView();
        presenter = new MyAssemblePresenter(getContext(), this);
        assembleAdapter = new MyAssembleAdapter();
        assembleAdapter.setOnActShareClickListener(this);
        assembleAdapter.setOutClickListener(this);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(assembleAdapter);
        layoutRefresh.setOnRefreshLoadMoreListener(this);
        mPlatformMap.clear();
        mPlatformMap.put(ResUtils.getStrById(getContext(), R.string.lib_share_wx), SHARE_MEDIA.WEIXIN);
        mPlatformMap.put(ResUtils.getStrById(getContext(), R.string.lib_share_circle), SHARE_MEDIA.WEIXIN_CIRCLE);
        mPlatformMap.put(ResUtils.getStrById(getContext(), R.string.lib_share_qq), SHARE_MEDIA.QQ);
        mPlatformMap.put(ResUtils.getStrById(getContext(), R.string.lib_share_qzone), SHARE_MEDIA.QZONE);
        mPlatformMap.put(ResUtils.getStrById(getContext(), R.string.lib_share_sina), SHARE_MEDIA.SINA);
//        getData();
    }

    @Override
    public void getData() {
        super.getData();
        listMap.put("currentPage", currentPage + "");
        listMap.put("pageSize", 10 + "");
        listMap.put("type", get("type") + "");
        listMap.put("addressProvince", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE));
        listMap.put("longitude", get("lng") + "");
        listMap.put("latitude", get("lat") + "");
        presenter.getAssembleTopList(listMap);
    }


    @Override
    public void onSuccessGetTopAssembleList(List<KKGroup> kickList, PageInfoEarly pageInfoEarly) {
        if (kickList == null || kickList.size() == 0) {
            showEmpty();
            return;
        }
        if (pageInfoEarly == null) {
//            showEmpty();
            layoutRefresh.setEnableLoadMore(false);
            return;
        }
        currentPage = pageInfoEarly.currentPage;
        if (currentPage == 1) {
            assembleAdapter.setData((ArrayList) kickList);

        } else {
            assembleAdapter.addDatas((ArrayList) kickList);
        }
        if (pageInfoEarly.isMore != 1) {
            layoutRefresh.finishLoadMoreWithNoMoreData();
        } else {
            layoutRefresh.setNoMoreData(false);
            layoutRefresh.setEnableLoadMore(true);
        }
    }

    @Override
    public void onSuccessUnjoinAssemble() {
        onRefresh(null);
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

    private void initView() {
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        recycler = (RecyclerView) findViewById(R.id.recycler);
    }

    public static MyAssembleFragment newInstance(Map<String, Object> maporg) {
        MyAssembleFragment fragment = new MyAssembleFragment();
        Bundle args = new Bundle();
        for (Map.Entry<String, Object> entry : maporg.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof Integer) {
                args.putInt(key, (Integer) value);
            } else if (value instanceof Boolean) {
                args.putBoolean(key, (Boolean) value);
            } else if (value instanceof String) {
                args.putString(key, (String) value);
            } else {
                args.putSerializable(key, (Serializable) value);
            }
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        layoutRefresh.finishRefresh();
        layoutRefresh.finishLoadMore();
    }
    KKGroup kkGroupDetail;
    String lessman;
    Bitmap bitmap;
    @Override
    public void clickShare(final KKGroup kkGroupDetail, final String lessman, final Bitmap bitmap) {
        this.kkGroupDetail = kkGroupDetail;
        this.lessman = lessman;
        this.bitmap = bitmap;
//        KKGroupDialog.newInstance().setResult(lessman).setOnDialogShareclickListener(new KKGroupDialog.DialogShareclickListener() {
//            @Override
//            public void onClick(SHARE_MEDIA share_media) {
//                buildDes(kkGroupDetail, lessman, bitmap);
//                share(share_media, surl, sdes, stitle);
//            }
//        }).show(getChildFragmentManager(), "kkOkDialog");
        buildDes(kkGroupDetail, lessman, bitmap);
//        shareMin(SHARE_MEDIA.WEIXIN, surl, sdes, stitle, spath);
        share(SHARE_MEDIA.WEIXIN,surl,sdes,stitle);
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
        new ShareAction(getActivity())
                .withMedia(umMin)
                .setPlatform(shareMedia)
                .setCallback(this)
                .share();
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
        new ShareAction(getActivity())
                .withMedia(web)
                .setPlatform(shareMedia)
                .setCallback(this)
                .share();
    }

    private void buildDes(KKGroup kkGroupDetail, String lessman, Bitmap bitmap) {
        sBitmap = bitmap;
        String urlPrefix = SpUtils.getValue(mContext, UrlKeys.H5_BargainUrl);
        String url = String.format("%s?type=8&scheme="+"assembleing"+"&shopId=%s&referral_code=%s&teamNum=%s", urlPrefix,SpUtils.getValue(mContext,SpKey.CHOSE_SHOP),SpUtils.getValue(mContext,SpKey.GETTOKEN),kkGroupDetail.teamNum + "");
        surl = url;
        String path = String.format("/pages/home/markting/marketStatus/index?shopId=%s&teamNum=%s&referral_code=%s",
                SpUtils.getValue(mContext, SpKey.CHOSE_SHOP), kkGroupDetail.teamNum + "", SpUtils.getValue(mContext, SpKey.GETTOKEN));
        spath = path;
        //System.out.println("分享的连接" + surl);
        stitle = "【仅剩" + lessman + "个名额】跟我拼团￥" + StringUtils.getResultPrice(kkGroupDetail.assemblePrice + "") + "买“" + kkGroupDetail.goodsTitle + "”";
        sdes = "活动火热进行中，快快加入吧";
    }

    @Override
    public void outClick(String function, final Object obj) {
        if("退团".equals(function)){
            StyledDialog.init(mContext);
            StyledDialog.buildIosAlert("", "是否退团?", new MyDialogListener() {
                @Override
                public void onFirst() {
                    String teamNum= (String) obj;
                    presenter.unjoinAssemble(new SimpleHashMapBuilder<String, Object>()
                            .puts("teamNum",teamNum)
                            .puts("memberId",new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)))
                    );
                }

                @Override
                public void onThird() {
                    super.onThird();
                }

                @Override
                public void onSecond() {

                }
            }).setCancelable(true,true).setBtnColor(R.color.dialogutil_text_black, R.color.colorPrimaryDark,0).setBtnText("确定", "取消").show();

        }
    }
}