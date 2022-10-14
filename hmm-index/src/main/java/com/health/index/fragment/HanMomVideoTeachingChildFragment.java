package com.health.index.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.health.index.R;
import com.health.index.adapter.HanMomVideoTeachingChildAdapter;
import com.health.index.contract.HanMomVideoContract;
import com.healthy.library.model.VideoListModel;
import com.health.index.presenter.HanMomVideoPresenter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.business.VideoListTabPopWindow;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.model.VideoCategory;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.StatusLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HanMomVideoTeachingChildFragment extends BaseFragment implements HanMomVideoContract.View, OnRefreshLoadMoreListener {


    private String categoryCode;
    private String categoryId;
    private List<VideoCategory> childTabList;

    private SmartRefreshLayout refreshLayout;
    private StatusLayout layoutStatus;
    private RecyclerView recyclerList;
    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;
    private HanMomVideoTeachingChildAdapter hanMomVideoTeachingChildAdapter;
    //private StackLabel stackLabelView;
    private LinearLayout downImg;
    private LinearLayout lableLL;
    private ConstraintLayout topLL;
    private VideoListTabPopWindow videoListTabPopWindow;
    private View popBottomView;
    private int pageNum = 1;
    private HanMomVideoPresenter hanMomVideoPresenter;

    public HanMomVideoTeachingChildFragment() {

    }

    public static HanMomVideoTeachingChildFragment newInstance(String categoryId, List<VideoCategory> children) {
        HanMomVideoTeachingChildFragment fragment = new HanMomVideoTeachingChildFragment();
        Bundle args = new Bundle();
        args.putString("categoryId", categoryId);
        args.putSerializable("children", (Serializable) children);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryId = getArguments().getString("categoryId");
            childTabList = (ArrayList<VideoCategory>) getArguments().getSerializable("children");
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_han_mom_video_teaching_child;
    }

    @Override
    protected void findViews() {
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refresh_layout);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        recyclerList = (RecyclerView) findViewById(R.id.recyclerList);
        downImg = (LinearLayout) findViewById(R.id.downImg);
        lableLL = (LinearLayout) findViewById(R.id.lableLL);
        topLL = (ConstraintLayout) findViewById(R.id.topLL);
        popBottomView = (View) findViewById(R.id.popBottomView);
        hanMomVideoPresenter = new HanMomVideoPresenter(mContext, this);
        initList();
    }

    private void initList() {
        refreshLayout.setOnRefreshLoadMoreListener(this);
        virtualLayoutManager = new VirtualLayoutManager(mContext);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        recyclerList.setLayoutManager(virtualLayoutManager);
        recyclerList.setAdapter(delegateAdapter);

        hanMomVideoTeachingChildAdapter = new HanMomVideoTeachingChildAdapter();
        delegateAdapter.addAdapter(hanMomVideoTeachingChildAdapter);

        if (childTabList != null && childTabList.size() > 0) {
            downImg.setVisibility(View.GONE);
            if (childTabList.size() > 3) {
                downImg.setVisibility(View.VISIBLE);
            }
            topLL.setVisibility(View.VISIBLE);
            childTabList.get(0).isSelect = true;
            buildLable();
            downImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    videoListTabPopWindow = new VideoListTabPopWindow(getContext(), new VideoListTabPopWindow.ItemClickCallBack() {
                        @Override
                        public void click(List<VideoCategory> list) {
                            childTabList = list;
                            buildLable();
                        }

                        @Override
                        public void dismiss() {
                            videoListTabPopWindow.dismiss();
                        }
                    });
                    videoListTabPopWindow.setData(childTabList);
                    videoListTabPopWindow.showPopupWindow(popBottomView);
                }
            });
        } else {
            topLL.setVisibility(View.GONE);
            categoryCode = categoryId;
            getData();
        }
    }

    @Override
    public void getData() {
        super.getData();
        hanMomVideoPresenter.getVideoList(new SimpleHashMapBuilder<String, Object>()
                .puts("pageNum", pageNum)
                .puts("pageSize", "10")
                .puts("categoryCode", categoryCode)
                .puts("memberId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT))));
    }

    @Override
    public void onGetTabListSuccess(List<VideoCategory> result) {

    }

    @Override
    public void onGetVideoListSuccess(List<VideoListModel> result, PageInfoEarly pageInfoEarly) {
        showContent();
        if (result == null) {
            return;
        }
        pageNum = pageInfoEarly.pageNum;
        if (pageNum == 1 || pageNum == 0) {
            hanMomVideoTeachingChildAdapter.setData((ArrayList) result);
            if (result.size() == 0) {
                showEmpty();
            }
        } else {
            hanMomVideoTeachingChildAdapter.addDatas((ArrayList) result);
        }
        if (pageInfoEarly.nextPage == 0) {
            refreshLayout.finishLoadMoreWithNoMoreData();
        } else {
            refreshLayout.setNoMoreData(false);
            refreshLayout.setEnableLoadMore(true);
        }
    }

    @Override
    public void onGetVideoDetailSuccess(VideoListModel result, int type) {

    }

    @Override
    public void onAddPraiseSuccess(String result, int type) {

    }

    @Override
    public void onLoadMore(@NonNull @NotNull RefreshLayout refreshLayout) {
        pageNum++;
        getData();
    }

    @Override
    public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
        pageNum = 1;
        getData();
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
    }

    private void buildLable() {
        lableLL.removeAllViews();
        for (int i = 0; i < childTabList.size(); i++) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.item_txt_lable_layout, null);
            TextView lable = view.findViewById(R.id.lable);
            lable.setText(childTabList.get(i).categoryName);
            if (childTabList.get(i).isSelect) {
                categoryCode = childTabList.get(i).categoryCode;
                lable.setTextColor(Color.parseColor("#AC33BA"));
                lable.setBackgroundResource(R.drawable.shape_video_list_lable_bg_select);
            } else {
                lable.setTextColor(Color.parseColor("#333333"));
                lable.setBackgroundResource(R.drawable.shape_video_list_lable_bg_unselect);
            }
            final int pos = i;
            lable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setData(pos);
                    buildLable();
                }
            });
            lableLL.addView(view);
        }
        getData();
    }

    private void setData(int pos) {
        for (int i = 0; i < childTabList.size(); i++) {
            if (i == pos) {
                childTabList.get(i).isSelect = true;
            } else {
                childTabList.get(i).isSelect = false;
            }
        }
    }
}