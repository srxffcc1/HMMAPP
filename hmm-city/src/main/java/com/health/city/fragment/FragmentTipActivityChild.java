package com.health.city.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.health.city.R;
import com.healthy.library.adapter.PostAdapter;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.contract.DataStatisticsContract;
import com.healthy.library.contract.PostListSingleContract;
import com.healthy.library.model.PostDetail;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.constant.SpKey;
import com.healthy.library.dialog.PostCouponDialog;
import com.healthy.library.interfaces.IsNeedShare;
import com.healthy.library.message.UpdateUpIcon;
import com.healthy.library.message.UpdateUserLocationMsg;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.presenter.DataStatisticsPresenter;
import com.healthy.library.presenter.PostListSinglePresenter;
import com.healthy.library.routes.CityRoutes;
import com.healthy.library.widget.StatusLayout;
import com.hss01248.dialog.StyledDialog;
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

public class FragmentTipActivityChild extends BaseFragment implements IsNeedShare, PostListSingleContract.View,
        OnRefreshLoadMoreListener, PostAdapter.OnShareClickListener, PostAdapter.OnPostLikeClickListener,
        PostAdapter.OnPostFansClickListener, BaseAdapter.OnOutClickListener , DataStatisticsContract.View{


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
    private int position = 0;
    private PostCouponDialog couponDialog;
    private ImageView passToSendPost;
    private ImageView passToTop;
    private DataStatisticsPresenter dataStatisticsPresenter;

    public static FragmentTipActivityChild newInstance(Map<String, Object> maporg) {
        FragmentTipActivityChild fragment = new FragmentTipActivityChild();
        Bundle args = new Bundle();
        BaseFragment.bundleMap(maporg, args);
        fragment.setArguments(args);
        return fragment;
    }

    public void setNeedloadmore(boolean needloadmore) {
        this.needloadmore = needloadmore;
    }

    public FragmentTipActivityChild putMap(String key, String value) {
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
        return R.layout.city_fragment_focus_layout;
    }

    @Override
    protected void findViews() {
        initView();
        refreshLayout.finishLoadMoreWithNoMoreData();
        refreshLayout.finishLoadMoreWithNoMoreData();
        postListSinglePresenter = new PostListSinglePresenter(mContext, this);
        dataStatisticsPresenter = new DataStatisticsPresenter(mContext, this);
        postAdapter = new PostAdapter();
        recyclerQuestion.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerQuestion.setAdapter(postAdapter);
        refreshLayout.setOnRefreshLoadMoreListener(this);
        refreshLayout.setEnableRefresh(false);
        postAdapter.setOnPostFansClickListener(this);
        postAdapter.setOnPostLikeClickListener(this);
        postAdapter.setOnShareClickListener(this);
        postAdapter.setOutClickListener(this);
        getBasemap().put("currentPage", page + "");
        getBasemap().put("pageSize", 10 + "");
        postListSinglePresenter.getPostList(getBasemap());

        recyclerQuestion.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition();
                if (firstVisibleItemPosition > 3) {
                    passToTop.setVisibility(View.VISIBLE);
                } else {
                    passToTop.setVisibility(View.GONE);
                }

            }
        });
    }

    private void initView() {
        findViewById(R.id.topView).setVisibility(View.GONE);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refresh_layout);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        recyclerQuestion = (RecyclerView) findViewById(R.id.recycler_question);
        passToSendPost = (ImageView) findViewById(R.id.passToSendPost);
        passToTop = (ImageView) findViewById(R.id.passToTop);
        passToSendPost.setVisibility(View.GONE);
        passToSendPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance()
                        .build(CityRoutes.CITY_POSTSEND)
                        .navigation();
            }
        });
        passToTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerQuestion.scrollToPosition(0);
            }
        });
        recyclerQuestion.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstCompletelyVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                if (firstCompletelyVisibleItemPosition > 2) {
                    passToTop.setVisibility(View.VISIBLE);
                } else {
                    passToTop.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onSuccessGetPostList(List<PostDetail> posts, PageInfoEarly pageInfoEarly) {
        if (pageInfoEarly == null) {
            showEmpty();
            refreshLayout.setEnableLoadMore(false);
            return;
        }
        page = pageInfoEarly.currentPage;

        ////System.out.println("??????????????????" + get("type") + "???" + get("type2"));

        if (page == 1) {
            postAdapter.clear();
            if (posts.size() == 0) {
                ////System.out.println("???????????????????????????" + get("type") + "???" + get("type2"));
                postAdapter.clear();
                showEmpty();
            } else {
                postAdapter.setData((ArrayList) posts);
                for (int i = 0; i < posts.size(); i++) {
                    if (posts.get(i).postingType == 5 || posts.get(i).postingType == 6) {//?????????????????????????????????????????????????????? ?????????????????????
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
                if (posts.get(i).postingType == 5 || posts.get(i).postingType == 6) {//?????????????????????????????????????????????????????? ?????????????????????
                    PostDetail postDetail = posts.get(i);
                    if (postDetail.postActivityList != null && postDetail.postActivityList.size() > 0) {

                    } else {
                        PostListSinglePresenter postListSinglePresenter = new PostListSinglePresenter(getContext(), this);
                        postListSinglePresenter.getActivityList(new SimpleHashMapBuilder<String, Object>().puts("postingId", postDetail.id), postDetail);
                    }
                }
            }
        }
        if (position > 0) {
            recyclerQuestion.scrollToPosition(position);
            postAdapter.notifyItemChanged(position);
            position = 0;
        }
        freshtime++;
        if (pageInfoEarly.isMore != 1) {
            ////System.out.println("???????????????");
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
            Toast.makeText(mContext, "????????????", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSuccessFan() {

    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        getBasemap().put("currentPage", page + "");
        postListSinglePresenter.getPostList(getBasemap());
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        ////System.out.println("???????????????2???" + get("type") + "???" + get("type2"));
        page = 1;
        getBasemap().put("currentPage", page + "");
        try {
            if (postAdapter != null) {
                postAdapter.clear();
            }
            postListSinglePresenter.getPostList(getBasemap());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        dataStatisticsPresenter.shareAndLook(new SimpleHashMapBuilder<String, Object>().puts("sourceId", postId).puts("sourceType",2).puts("type", 2));
//        if("??????".equals(fragmentType)){
//            Map nokmap = new HashMap<String, String>();
//            nokmap.put("soure","????????????");
//            MobclickAgent.onEvent(mContext, "event2ShareClick",nokmap);
//        }
//        if("??????".equals(fragmentType)){
//            Map nokmap = new HashMap<String, String>();
//            nokmap.put("soure","????????????");
//            MobclickAgent.onEvent(mContext, "event2ShareClick",nokmap);
//        }

        showShare();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUserInfo(UpdateUserLocationMsg msg) {
        if (!isFirstLoad) return;
        ////System.out.println("??????????????????");
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

        ////System.out.println("???????????????2???" + get("type") + "???" + get("type2"));
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
            couponDialog = PostCouponDialog.newInstance();
            couponDialog.setId((String) obj);
            couponDialog.show(getChildFragmentManager(), "");
            couponDialog.setOnConfirmClick(new PostCouponDialog.OnSelectConfirm() {
                @Override
                public void onClick(int result) {
                    getBasemap().put("currentPage", page + "");
                    postListSinglePresenter.getPostList(getBasemap());
                }
            });

        }
        if (function.equals("position")) {
            position = (Integer) obj;//????????????item
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
        strings.add("??????");
        StyledDialog.init(getContext());
        reviewandwarndialog = StyledDialog.buildBottomItemDialog(strings, stringsColors, "??????", new MyItemDialogListener() {
            @Override
            public void onItemClick(CharSequence text, int position) {
                if ("??????".equals(text.toString())) {
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
     * ????????????
     */
    public void showWarnDialog(final String warnid) {
        final List<Integer> stringsColors = new ArrayList<>();
        final List<String> strings = new ArrayList<>();
        strings.add("????????????");
        stringsColors.add(Color.parseColor("#29BDA9"));
        strings.add("????????????");
        stringsColors.add(Color.parseColor("#29BDA9"));
        strings.add("????????????");
        stringsColors.add(Color.parseColor("#29BDA9"));
        strings.add("????????????");
        stringsColors.add(Color.parseColor("#29BDA9"));
        strings.add("????????????");
        stringsColors.add(Color.parseColor("#29BDA9"));
        strings.add("??????");
        stringsColors.add(Color.parseColor("#29BDA9"));
        StyledDialog.init(getContext());
        warndialog = StyledDialog.buildBottomItemDialog(strings, stringsColors, "??????", new MyItemDialogListener() {
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
        }).setTitle("??????????????????").setTitleColor(R.color.dialogTitleColor).setTitleSize(15).setCancelable(true, true).show();
        warndialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                warndialog = null;
            }
        });
    }
}
