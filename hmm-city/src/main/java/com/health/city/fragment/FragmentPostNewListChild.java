package com.health.city.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.health.city.R;
import com.healthy.library.adapter.PostAdapter;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.constant.SpKey;
import com.healthy.library.contract.DataStatisticsContract;
import com.healthy.library.contract.PostListSingleContract;
import com.healthy.library.dialog.PostCouponDialog;
import com.healthy.library.interfaces.IsNeedShare;
import com.healthy.library.message.UpdateUpIcon;
import com.healthy.library.message.UpdateUserLocationMsg;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.model.PostDetail;
import com.healthy.library.presenter.DataStatisticsPresenter;
import com.healthy.library.presenter.PostListSinglePresenter;
import com.healthy.library.widget.StatusLayout;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;
import com.hss01248.dialog.interfaces.MyItemDialogListener;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentPostNewListChild extends BaseFragment implements IsNeedShare, PostListSingleContract.View,
        OnRefreshLoadMoreListener, PostAdapter.OnShareClickListener, PostAdapter.OnPostLikeClickListener,
        PostAdapter.OnPostFansClickListener, BaseAdapter.OnOutClickListener, DataStatisticsContract.View {


    private SmartRefreshLayout refreshLayout;
    private StatusLayout layoutStatus;
    private RecyclerView recyclerQuestion;
    int page = 1;
    private int freshtime = 0;
    PostAdapter postAdapter;
    PostListSinglePresenter postListSinglePresenter;
    String surl;
    String sdes;
    String stitle;
    private Dialog reviewandwarndialog;
    private Dialog warndialog;
    private PostCouponDialog couponDialog;
    private int totalDy = 0;
    private DataStatisticsPresenter dataStatisticsPresenter;

    public static FragmentPostNewListChild newInstance(Map<String, Object> maporg) {
        FragmentPostNewListChild fragment = new FragmentPostNewListChild();
        Bundle args = new Bundle();
        BaseFragment.bundleMap(maporg, args);
        fragment.setArguments(args);
        return fragment;
    }

    public void setNeedloadmore(boolean needloadmore) {
        this.needloadmore = needloadmore;
    }

    public FragmentPostNewListChild putMap(String key, String value) {
        getBasemap().put(key, value);
        return this;
    }

    public void setRefreshEnable(boolean flag) {
        if (refreshLayout != null) {
            refreshLayout.setEnableRefresh(false);
        }
    }

    private boolean needloadmore = true;

    @Override
    protected int getLayoutId() {
        return R.layout.city_fragment_child_new_child;
    }

    @Override
    protected void findViews() {
        initView();
        refreshLayout.finishLoadMoreWithNoMoreData();
        postListSinglePresenter = new PostListSinglePresenter(mContext, this);
        dataStatisticsPresenter = new DataStatisticsPresenter(mContext, this);
        postAdapter = new PostAdapter();
        postAdapter.setTopRadius(false);
        recyclerQuestion.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerQuestion.setAdapter(postAdapter);
        refreshLayout.setOnRefreshLoadMoreListener(this);
        refreshLayout.setEnableRefresh(false);
        postAdapter.setOnPostFansClickListener(this);
        postAdapter.setOnPostLikeClickListener(this);
        postAdapter.setOnShareClickListener(this);
        postAdapter.setOutClickListener(this);
        recyclerQuestion.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(-1)) {
                    totalDy = 0;
                }
                totalDy += dy;
                if (totalDy > 1000) {
                    EventBus.getDefault().post(new UpdateUpIcon(1));
                } else {
                    EventBus.getDefault().post(new UpdateUpIcon(0));
                }
            }
        });
    }

    @Override
    protected void onLazyData() {
        super.onLazyData();
        getData();
    }

    @Override
    public boolean isNeedLazy() {
        return false;
    }

    @Override
    public void changeFragmentShow() {
        super.changeFragmentShow();
        String areaNo = LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE);
        //优化第一次地理位置没有同城圈数据获取不到问题 优化只有没有数据的情况下进行获取
        if (!TextUtils.isEmpty(areaNo) && postAdapter != null && ListUtil.isEmpty(postAdapter.getDatas()) && isFirstLoad) {
            try {
                if (TextUtils.isEmpty(areaNo)) {
                    areaNo = "0";
                }
                getBasemap().put("addressCity", (Integer.parseInt(areaNo) / 100 * 100) + "");
                getBasemap().put("addressProvince", (Integer.parseInt(areaNo) / 10000 * 10000) + "");
                getBasemap().put("addressArea", areaNo + "");
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
//            super.onLazyData();
        }
    }

    @Override
    public void getData() {
        getBasemap().put("currentPage", page + "");
        getBasemap().put("pageSize", 10 + "");
        postListSinglePresenter.getPostList(getBasemap());
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        getData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        //System.out.println("刷新数据了2：" + get("type") + "：" + get("type2"));
        page = 1;
        try {
            if (postAdapter != null) {
                postAdapter.clear();
            }
            getData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取RecyclerView滚动距离
     */
    private int getDistance(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        if (firstVisiableChildView == null) {
            return 0;
        }
        int itemHeight = firstVisiableChildView.getHeight();
        int offsetY = (position) * itemHeight - firstVisiableChildView.getTop();
        return offsetY;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initView() {
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refresh_layout);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        recyclerQuestion = (RecyclerView) findViewById(R.id.recycler_question);
    }

    @Override
    public void onSuccessGetPostList(List<PostDetail> posts, PageInfoEarly pageInfoEarly) {
        if (pageInfoEarly == null) {
            showEmpty();
            refreshLayout.setEnableLoadMore(false);
            return;
        }
        page = pageInfoEarly.currentPage;

        //System.out.println("刷新数据了：" + get("type") + "：" + get("type2"));

        if (page == 1) {
            postAdapter.clear();
            if (posts.size() == 0) {
                //System.out.println("第一页了没数据了：" + get("type") + "：" + get("type2"));
                postAdapter.clear();
                showEmpty();
            } else {
                postAdapter.setData((ArrayList) posts);
                for (int i = 0; i < posts.size(); i++) {
                    if (posts.get(i).postingType == 5 || posts.get(i).postingType == 6) {//行内请求主要请求优惠券和视频关联商品 其他帖子不请求
                        PostDetail postDetail = posts.get(i);
                        if (postDetail.postActivityList != null && postDetail.postActivityList.size() > 0) {

                        } else {
                            PostListSinglePresenter postListSinglePresenter = new PostListSinglePresenter(getContext(), this);
                            postListSinglePresenter.getActivityList(new SimpleHashMapBuilder<String, Object>().puts("postingId", postDetail.id), postDetail);
                        }
                    }
                }
            }
        } else {
            postAdapter.addDatas((ArrayList) posts);
            for (int i = 0; i < posts.size(); i++) {
                if (posts.get(i).postingType == 5 || posts.get(i).postingType == 6) {//行内请求主要请求优惠券和视频关联商品 其他帖子不请求
                    PostDetail postDetail = posts.get(i);
                    if (postDetail.postActivityList != null && postDetail.postActivityList.size() > 0) {

                    } else {
                        PostListSinglePresenter postListSinglePresenter = new PostListSinglePresenter(getContext(), this);
                        postListSinglePresenter.getActivityList(new SimpleHashMapBuilder<String, Object>().puts("postingId", postDetail.id), postDetail);
                    }
                }
            }
        }
        freshtime++;
        if (pageInfoEarly.isMore != 1) {
            //System.out.println("没有更多了");
            refreshLayout.setNoMoreData(true);
            refreshLayout.finishLoadMoreWithNoMoreData();
        } else {
            refreshLayout.setNoMoreData(false);
            refreshLayout.setEnableLoadMore(needloadmore ? true : false);
        }
    }

    @Override
    public void onSuccessLike() {

    }

    @Override
    public void onSuccessGetActivityList() {
        if (postAdapter != null) {
            postAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSuccessFan(Object result) {
        if ("0".equals(result)) {
            Toast.makeText(mContext, "关注成功", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSuccessFan() {

    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();

        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUpIcon(UpdateUpIcon msg) {
        if (msg.flag == 2) {
            recyclerQuestion.scrollToPosition(0);
        }
    }

    @Override
    public void postfansclick(View view, String friendId, String type, String frtype) {
        Map<String, Object> map = new HashMap<>();
        map.put("friendId", friendId);
        map.put("friendType", frtype);
        map.put("type", type);
        postListSinglePresenter.fan(map);
    }

    @Override
    public void postlikeclick(View view, String postingId, String type) {
        Map<String, Object> map = new HashMap<>();
        map.put("postingId", postingId);
        map.put("type", type);
        postListSinglePresenter.like(map);
    }

    @Override
    public void postshareclick(View view, String url, String des, String title,String postId) {
        this.surl = url;
        this.sdes = des;
        this.stitle = title;
        dataStatisticsPresenter.shareAndLook(new SimpleHashMapBuilder<String, Object>().puts("sourceId", postId).puts("sourceType", 2).puts("type", 2));
//        if("本地".equals(fragmentType)){
//            Map nokmap = new HashMap<String, String>();
//            nokmap.put("soure","本地列表");
//            MobclickAgent.onEvent(mContext, "event2ShareClick",nokmap);
//        }
//        if("发现".equals(fragmentType)){
//            Map nokmap = new HashMap<String, String>();
//            nokmap.put("soure","发现列表");
//            MobclickAgent.onEvent(mContext, "event2ShareClick",nokmap);
//        }

        showShare();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUserInfo(UpdateUserLocationMsg msg) {
        if (!isFirstLoad) return;
        //System.out.println("帖子修改试试");
        successLocation();
    }

    private void successLocation() {
        String areaNo = LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE);
        if (TextUtils.isEmpty(areaNo)) {
            areaNo = "0";
        }
        getBasemap().put("addressCity", (Integer.parseInt(areaNo) / 100 * 100) + "");
        getBasemap().put("addressProvince", (Integer.parseInt(areaNo) / 10000 * 10000) + "");
        getBasemap().put("addressArea", areaNo + "");


//        basemap.put("addressCity",341500 + "");
//        basemap.put("addressProvince",340000+ "");
//        basemap.put("addressArea",341502 + "");

        //System.out.println("刷新数据了2：" + get("type") + "：" + get("type2"));
        page = 1;
        getBasemap().put("currentPage", page + "");
        postListSinglePresenter.getPostList(getBasemap());
    }

    @Override
    public String getSurl() {
        return surl;
    }

    @Override
    public String getSdes() {
        return sdes;
    }

    @Override
    public String getStitle() {
        return stitle;
    }

    @Override
    public Bitmap getsBitmap() {
        return null;
    }

    @Override
    public void outClick(@NotNull String function, @NotNull Object obj) {
        if (function.equals("coupon")) {
            if (couponDialog == null) {
                couponDialog = PostCouponDialog.newInstance();
            }
            couponDialog.setId((String) obj);
            couponDialog.show(getChildFragmentManager(), "");
            couponDialog.setOnConfirmClick(new PostCouponDialog.OnSelectConfirm() {
                @Override
                public void onClick(int result) {
                    page = 1;
                    postListSinglePresenter.getPostList(getBasemap());
                }
            });

        }
        if (function.equals("submit")) {
            showReviewWarnDialog((String) obj);
        }
        if (function.equals("sharePk")) {
            dataStatisticsPresenter.shareAndLook(new SimpleHashMapBuilder<String, Object>().puts("sourceId", (String) obj).puts("sourceType", 2).puts("type", 2));
        }
    }

    public void showReviewWarnDialog(final String warnid) {
        final List<String> strings = new ArrayList<>();
        final List<Integer> stringsColors = new ArrayList<>();
        stringsColors.add(Color.parseColor("#444444"));
        strings.add("举报");
        StyledDialog.init(getContext());
        reviewandwarndialog = StyledDialog.buildBottomItemDialog(strings, stringsColors, "取消", new MyItemDialogListener() {
            @Override
            public void onItemClick(CharSequence text, int position) {
                if ("举报".equals(text.toString())) {
                    showWarnDialog(warnid);
                }
            }

            @Override
            public void onBottomBtnClick() {

            }
        }).setCancelable(true, true).show();
        reviewandwarndialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                reviewandwarndialog = null;
            }
        });
    }

    /**
     * 回复举报
     */
    public void showWarnDialog(final String warnid) {
        final List<Integer> stringsColors = new ArrayList<>();
        final List<String> strings = new ArrayList<>();
        strings.add("垃圾广告");
        stringsColors.add(Color.parseColor("#29BDA9"));
        strings.add("淫秽色情");
        stringsColors.add(Color.parseColor("#29BDA9"));
        strings.add("诈骗信息");
        stringsColors.add(Color.parseColor("#29BDA9"));
        strings.add("不实违法");
        stringsColors.add(Color.parseColor("#29BDA9"));
        strings.add("违规侵权");
        stringsColors.add(Color.parseColor("#29BDA9"));
        strings.add("其他");
        stringsColors.add(Color.parseColor("#29BDA9"));
        StyledDialog.init(getContext());
        warndialog = StyledDialog.buildBottomItemDialog(strings, stringsColors, "取消", new MyItemDialogListener() {
            @Override
            public void onItemClick(CharSequence text, int position) {
                Map<String, Object> map = new HashMap<>();
                map.put("type", "1");
                map.put("sourceId", warnid);
                map.put("reason", text.toString());
                postListSinglePresenter.warn(map);
            }

            @Override
            public void onBottomBtnClick() {

            }
        }).setTitle("举报内容问题").setTitleColor(R.color.dialogTitleColor).setTitleSize(15).setCancelable(true, true).show();
        warndialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                warndialog = null;
            }
        });
    }
}
