//package com.health.discount.fragment;
//
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.view.View;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.health.discount.R;
//import com.health.discount.adapter.GroupMyListAdapter;
//import com.health.discount.contract.GroupListContract;
//import com.healthy.library.constant.SpKey;
//import com.healthy.library.model.KKGroup;
//import com.health.discount.presenter.GroupMyListPresenter;
//import com.health.discount.widget.KKGroupDialog;
//import com.healthy.library.base.BaseFragment;
//import com.healthy.library.constant.UrlKeys;
//import com.healthy.library.model.PageInfoEarly;
//import com.healthy.library.utils.ResUtils;
//import com.healthy.library.utils.SpUtils;
//import com.healthy.library.utils.StringUtils;
//import com.healthy.library.widget.StatusLayout;
//import com.scwang.smart.refresh.layout.SmartRefreshLayout;
//import com.scwang.smart.refresh.layout.api.RefreshLayout;
//import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
//import com.umeng.socialize.ShareAction;
//import com.umeng.socialize.bean.SHARE_MEDIA;
//import com.umeng.socialize.media.UMImage;
//import com.umeng.socialize.media.UMMin;
//import com.umeng.socialize.media.UMWeb;
//
//import java.io.Serializable;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class AssembleRightChildFragment extends BaseFragment implements GroupListContract.View, OnRefreshLoadMoreListener, GroupMyListAdapter.GroupShareClicker {
//    GroupMyListAdapter groupListAdapter;
//    private com.scwang.smart.refresh.layout.SmartRefreshLayout layoutRefresh;
//    private com.healthy.library.widget.StatusLayout layoutStatus;
//    private RecyclerView recycler;
//    GroupMyListPresenter groupListPresenter;
//    Map<String,Object> listmap=new HashMap<>();
//    private int page=1;
//    @Override
//    protected int getLayoutId() {
//        return R.layout.dis_fragment_group_right_child;
//    }
//
//    @Override
//    protected void findViews() {
//        initView();
//        groupListAdapter=new GroupMyListAdapter();
//        groupListAdapter.setGroupShareClicker(this);
//        groupListPresenter=new GroupMyListPresenter(mContext,this);
//       groupListAdapter.setLocation(get("areaNo").toString(),get("lat").toString(),get("lng").toString());
//        recycler.setLayoutManager(new LinearLayoutManager(mContext));
//        groupListAdapter.bindToRecyclerView(recycler);
//        layoutRefresh.setOnRefreshLoadMoreListener(this);
////        getData();
//        mPlatformMap.put(ResUtils.getStrById(getActivity(), R.string.lib_share_wx), SHARE_MEDIA.WEIXIN);
//        mPlatformMap.put(ResUtils.getStrById(getActivity(), R.string.lib_share_circle), SHARE_MEDIA.WEIXIN_CIRCLE);
//        mPlatformMap.put(ResUtils.getStrById(getActivity(), R.string.lib_share_qq), SHARE_MEDIA.QQ);
//        mPlatformMap.put(ResUtils.getStrById(getActivity(), R.string.lib_share_qzone), SHARE_MEDIA.QZONE);
//        mPlatformMap.put(ResUtils.getStrById(getActivity(), R.string.lib_share_sina), SHARE_MEDIA.SINA);
//    }
//
//    @Override
//    public void getData() {
//        super.getData();
//        listmap.put("addressProvince", (Integer.parseInt(get("areaNo").toString()) / 10000 * 10000) + "");
//        listmap.put("pageSize", "10");
//        listmap.put("currentPage",page+"");
//        listmap.put("type",get("type").toString());
//        listmap.put("longitude", get("lng").toString());
//        listmap.put("latitude", get("lat").toString());
//        groupListPresenter.getGroupList(listmap);
//    }
//
//
//    @Override
//    public void onSuccessGetGroupList(List<KKGroup> kickList, PageInfoEarly pageInfoEarly) {
//        if (pageInfoEarly == null) {
//            showEmpty();
//            layoutRefresh.setEnableLoadMore(false);
//            return;
//        }
//        page = pageInfoEarly.currentPage;
//        if (page == 1) {
//            groupListAdapter.setNewData(kickList);
//            if (kickList.size() == 0) {
//                showEmpty();
//            }
//        } else {
//            groupListAdapter.addData(kickList);
//        }
//        if (pageInfoEarly.isMore != 1) {
//
//            layoutRefresh.finishLoadMoreWithNoMoreData();
//        } else {
//            layoutRefresh.setNoMoreData(false);
//            layoutRefresh.setEnableLoadMore(true);
//        }
//    }
//
//    @Override
//    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
//        page++;
//        listmap.put("currentPage",page+"");
//        groupListPresenter.getGroupList(listmap);
//    }
//
//    @Override
//    public void onRequestFinish() {
//        super.onRequestFinish();
//        layoutRefresh.finishRefresh();
//        layoutRefresh.finishLoadMore();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        getData();
//    }
//
//    @Override
//    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
//        page=1;
//        listmap.put("currentPage",page+"");
//        getData();
//    }
//
//    private void initView() {
//        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
//        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
//        recycler = (RecyclerView) findViewById(R.id.recycler);
//    }
//    public static AssembleRightChildFragment newInstance(Map<String, Object> maporg) {
//        AssembleRightChildFragment fragment = new AssembleRightChildFragment();
//        Bundle args = new Bundle();
//        for (Map.Entry<String, Object> entry : maporg.entrySet()) {
//            String key = entry.getKey();
//            Object value = entry.getValue();
//            if (value instanceof Integer) {
//                args.putInt(key, (Integer) value);
//            } else if (value instanceof Boolean) {
//                args.putBoolean(key, (Boolean) value);
//            } else if (value instanceof String) {
//                args.putString(key, (String) value);
//            } else {
//                args.putSerializable(key, (Serializable) value);
//            }
//        }
//        fragment.setArguments(args);
//        return fragment;
//    }
//    String surl;
//    String sdes;
//    String stitle;
//    String spath;
//
//    private void buildDes(KKGroup kkGroup) {
//        String urlPrefix = SpUtils.getValue(mContext, UrlKeys.H5_BargainUrl);
//        String url = String.format("%s?type=2&teamNum=%s", urlPrefix, kkGroup.teamNum+"");
//        surl=url;
//        String path=String.format("pages/home/markting/marketStatus/index?shopId="+SpUtils.getValue(mContext, SpKey.CHOSE_SHOP)+"&teamNum=%s&addressCity=%s&longitude=%s&latitude=%s",
//                kkGroup.teamNum+"",(Integer.parseInt(get("areaNo").toString()) / 100 * 100)+"",get("lng").toString()+"",get("lat").toString()+"");
//        spath=path;
//
//
//        //System.out.println("分享的连接"+surl);
//        stitle="【仅剩"+kkGroup.remainderNum+"个名额】跟我拼团"+ StringUtils.getResultPrice(kkGroup.assemblePrice+"")+"元买“"+kkGroup.goodsTitle+"”";
//        sdes="活动火热进行中，快快加入吧";
//    }
//
//    private Map<String, SHARE_MEDIA> mPlatformMap = new HashMap<>();
//    @Override
//    public void onGroupShareClick(View view, final KKGroup kkGroup) {
//
//
//        KKGroupDialog.newInstance().setResult(kkGroup.remainderNum+"").setOnDialogShareclickListener(new KKGroupDialog.DialogShareclickListener() {
//            @Override
//            public void onClick(SHARE_MEDIA share_media) {
//                buildDes(kkGroup);
//                share(share_media, surl, sdes, stitle,kkGroup.bitmap);
//            }
//        }).show(getChildFragmentManager(),"kkOkDialog");
//
//    }
//    /**
//     * 分享
//     *
//     * @param shareMedia 分享平台
//     * @param url        链接地址
//     * @param des        描述
//     * @param title      标题
//     */
//    private void share(SHARE_MEDIA shareMedia, String url, String des, String title, Bitmap bitmap) {
//        //System.out.println(url);
//        UMWeb web = new UMWeb(url);
//        web.setTitle(title);
//        web.setThumb(new UMImage(mContext, bitmap));
//        web.setDescription(des);
//        new ShareAction(getActivity())
//                .withMedia(web)
//                .setPlatform(shareMedia)
//                .setCallback(this)
//                .share();
//    }
//    /**
//     * 分享
//     *
//     * @param shareMedia 分享平台
//     * @param url        链接地址
//     * @param des        描述
//     * @param title      标题
//     */
//    private void shareMin(SHARE_MEDIA shareMedia, String url, String des, String title,String path) {
//        //System.out.println(url);
//        UMMin umMin = new UMMin(url);
//        umMin.setTitle(title);
//        umMin.setThumb(new UMImage(mContext, R.drawable.index_share_humb));
//        umMin.setDescription(des);
//        umMin.setPath(path);
//        umMin.setUserName("gh_64e697e1d7a0");
//        com.umeng.socialize.Config.setMiniPreView();
//        com.umeng.socialize.Config.setMiniTest();
//        new ShareAction(getActivity())
//                .withMedia(umMin)
//                .setPlatform(shareMedia)
//                .setCallback(this)
//                .share();
//
//    }
//}
