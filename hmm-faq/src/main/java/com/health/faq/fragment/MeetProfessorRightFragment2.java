package com.health.faq.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.health.faq.R;
import com.health.faq.adapter.HotQuestionListAdapter;
import com.health.faq.contract.MeetProfessorRightContract;
import com.health.faq.presenter.MeetProfessorRightPresenter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.FaqExportQuestion;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.model.TabChangeModelFragment;
import com.healthy.library.widget.StatusLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class MeetProfessorRightFragment2 extends BaseFragment implements MeetProfessorRightContract.View, OnRefreshLoadMoreListener {
    MeetProfessorRightPresenter presenter;
    private SmartRefreshLayout refreshLayout;
    private StatusLayout layoutStatus;
    private RecyclerView recyclerQuestion;
    private HotQuestionListAdapter adapter;
    int page=1;
    private int freshtime=0;
    private TextView recyclerBottom;

    public void setNeedloadmore(boolean needloadmore) {
        this.needloadmore = needloadmore;
    }

    private boolean needloadmore=true;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_faq_right;
    }
    public void setRefreshEnable(boolean flag) {
        refreshLayout.setEnableRefresh(flag);
    }
    @Override
    protected void findViews() {
        initView();
        refreshLayout.finishLoadMoreWithNoMoreData();
        presenter=new MeetProfessorRightPresenter(mContext,this);
        presenter.getQuestionList(page, getBasemap());
        adapter=new HotQuestionListAdapter();
        try {
            adapter.setCityNo(get("addressCityOrg").toString());
        } catch (Exception e) {
            adapter.setCityNo(LocUtil.getCityNo(mContext, SpKey.LOC_ORG));
            e.printStackTrace();
        }
        recyclerQuestion.setLayoutManager(new LinearLayoutManager(mContext));
        adapter.bindToRecyclerView(recyclerQuestion);
        refreshLayout.setOnRefreshLoadMoreListener(this);
//        refreshLayout.setEnableLoadMore(true);
        if("1".equals(get("fragmentBottom"))){
            recyclerBottom.setVisibility(View.VISIBLE);
        }else {
            recyclerBottom.setVisibility(View.GONE);
        }
    }
    public void setBottomVisable(boolean flag){
        recyclerBottom.setVisibility(flag?View.VISIBLE:View.GONE);
    }
    public static MeetProfessorRightFragment2 newInstance(Map<String, Object> maporg) {
        MeetProfessorRightFragment2 fragment = new MeetProfessorRightFragment2();
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
    public MeetProfessorRightFragment2 putMap(String key, String value) {
        getBasemap().put(key, value);
        return this;
    }
    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        presenter.getQuestionList(++page, getBasemap());
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        presenter.getQuestionList(1, getBasemap());
    }


    private void initView() {
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refresh_layout);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        recyclerQuestion = (RecyclerView) findViewById(R.id.recycler_question);
        recyclerBottom = (TextView) findViewById(R.id.recyclerBottom);
    }
    @Override
    public void onRequestFinish() {
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
    }
    @Override
    public void onGetQuestionSuccess(List<FaqExportQuestion> faqHotQuestionList, PageInfoEarly pageInfoEarly) {
        if(pageInfoEarly==null){
            showEmpty();
            refreshLayout.setEnableLoadMore(false);
            return;
        }
        page=pageInfoEarly.currentPage;
        if (page==1||page==0) {
            if(freshtime==0){
                if("本地".equals(get("fragmentCity"))){
                    if(faqHotQuestionList.size()==0){
                        EventBus.getDefault().post(new TabChangeModelFragment(1));
                    }
                }

            }
            adapter.setNewData(faqHotQuestionList);
            if (faqHotQuestionList.size() == 0) {
                showEmpty();
            }
        } else {
            adapter.addData(faqHotQuestionList);
        }
        freshtime++;
        if (pageInfoEarly.isMore!=1) {
            refreshLayout.finishLoadMoreWithNoMoreData();
        } else {
            if(!needloadmore){

                refreshLayout.finishLoadMoreWithNoMoreData();
            }else {
                refreshLayout.setNoMoreData(false);
                refreshLayout.setEnableLoadMore(true);
            }
        }
    }
}
