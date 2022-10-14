package com.health.tencentlive.fragment;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.health.tencentlive.R;
import com.health.tencentlive.adapter.MyHelpListFragmentAdapter;
import com.health.tencentlive.contract.LiveHelpContract;
import com.health.tencentlive.impl.OnHelpClickListener;
import com.health.tencentlive.model.JackpotList;
import com.health.tencentlive.model.LiveHelpList;
import com.health.tencentlive.presenter.LiveHelpPresenter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.businessutil.GlideCopy;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.StatusLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

public class MyHelpListFragment extends BaseFragment implements LiveHelpContract.View, OnRefreshLoadMoreListener {

    private String courseId;
    private String mParam2;
    private StatusLayout layoutStatus;
    private SmartRefreshLayout layoutRefresh;
    private RecyclerView recyclerView;
    private ConstraintLayout bottomLayout;
    private View topLine;
    private TextView num;
    private CornerImageView userAvatar;
    private TextView userName;
    private TextView invitationNum;
    private TextView invitation;
    private View bottomLine;
    private OnHelpClickListener onClickListener;


    private MyHelpListFragmentAdapter myHelpListFragmentAdapter;
    private LiveHelpPresenter liveHelpPresenter;
    private int pageNum = 1;

    public MyHelpListFragment() {
        // Required empty public constructor
    }

    public static MyHelpListFragment newInstance(String param1, String param2) {
        MyHelpListFragment fragment = new MyHelpListFragment();
        Bundle args = new Bundle();
        args.putString("courseId", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            courseId = getArguments().getString("courseId");
            mParam2 = getArguments().getString("param2");
        }
    }

    public void setClickListener(OnHelpClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_help_list;
    }

    @Override
    protected void findViews() {
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        bottomLayout = (ConstraintLayout) findViewById(R.id.bottomLayout);
        topLine = (View) findViewById(R.id.topLine);
        num = (TextView) findViewById(R.id.num);
        userAvatar = (CornerImageView) findViewById(R.id.userAvatar);
        userName = (TextView) findViewById(R.id.userName);
        invitationNum = (TextView) findViewById(R.id.invitationNum);
        invitation = (TextView) findViewById(R.id.invitation);
        bottomLine = (View) findViewById(R.id.bottomLine);
        invitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onShare();
                }
            }
        });
        initView();
    }

    private void initView() {
        liveHelpPresenter = new LiveHelpPresenter(getContext(), this);
        layoutRefresh.setOnRefreshListener(this);
        myHelpListFragmentAdapter = new MyHelpListFragmentAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(myHelpListFragmentAdapter);
    }

    @Override
    protected void onLazyData() {
        super.onLazyData();
        showLoading();
        getData();
    }

    @Override
    public void getData() {
        super.getData();
        liveHelpPresenter.getHelpRankingList(new SimpleHashMapBuilder<String, Object>()
                .puts("pageNum", pageNum)
                .puts("pageSize", 10)
                .puts("liveCourseId", courseId)
                .puts("fromMemberId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT))));
        liveHelpPresenter.getLiveHelpInfo(new SimpleHashMapBuilder<String, Object>()
                .puts("memberId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)))
                .puts("pageSize", 10).puts("liveCourseId", courseId));
    }

    @Override
    public void getSuccessHelpRankingList(List<LiveHelpList> result, PageInfoEarly pageInfo) {
        myHelpListFragmentAdapter.clear();
        showContent();
        if (pageInfo == null) {
            showEmpty();
            layoutRefresh.setEnableLoadMore(false);
            return;
        }
        if (pageNum == 1) {
            if (result == null || result.size() == 0) {
                showEmpty();
            } else {
                myHelpListFragmentAdapter.setData((ArrayList) result);
            }
        } else {
            myHelpListFragmentAdapter.addDatas((ArrayList) result);
        }
        if (pageInfo.isMore != 1) {
            layoutRefresh.setNoMoreData(true);
            layoutRefresh.finishLoadMoreWithNoMoreData();
        } else {
            layoutRefresh.setNoMoreData(false);
            layoutRefresh.setEnableLoadMore(false);
        }
    }

    @Override
    public void getSuccessLiveHelpList(List<LiveHelpList> result, PageInfoEarly pageInfo) {

    }

    @Override
    public void getSuccessLiveHelpInfo(LiveHelpList result) {
        String countMember = "";
        if (result != null) {
            num.setText(result.ranking);
            GlideCopy.with(getContext())
                    .load(result.fromMember.faceUrl)

                    .error(R.drawable.img_avatar_default)
                    .into(userAvatar);
            userName.setText(result.fromMember.nickName);
            countMember = result.countMember;
        } else {
            num.setText("0");
            GlideCopy.with(getContext())
                    .load(SpUtils.getValue(getContext(), SpKey.USER_ICON))

                    .error(R.drawable.img_avatar_default)
                    .into(userAvatar);
            userName.setText(SpUtils.getValue(getContext(), SpKey.USER_NICK));
            countMember = "0";
        }
        invitationNum.setText(countMember);
    }

    @Override
    public void getSuccessCoupon(String result, String couponName) {

    }

    @Override
    public void getSuccessWinId(String result) {

    }

    @Override
    public void getSuccessHelpStatus(int result) {

    }

    @Override
    public void getSuccessJackpotList(List<JackpotList> result, PageInfoEarly pageInfo) {

    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        pageNum++;
        getData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        pageNum = 1;
        getData();
    }

}