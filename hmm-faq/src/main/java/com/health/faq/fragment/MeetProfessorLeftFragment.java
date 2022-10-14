package com.health.faq.fragment;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.health.faq.R;
import com.health.faq.adapter.HotExportListAdapter;
import com.health.faq.contract.MeetProfessorLeftContract;
import com.healthy.library.model.FaqExport;
import com.health.faq.presenter.MeetProfessorLeftPresenter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.TabChangeModelFragment;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.widget.StatusLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class MeetProfessorLeftFragment extends BaseFragment implements MeetProfessorLeftContract.View, OnRefreshLoadMoreListener {
    MeetProfessorLeftPresenter presenter;
    private com.scwang.smart.refresh.layout.SmartRefreshLayout refreshLayout;
    private com.healthy.library.widget.StatusLayout layoutStatus;
    private RecyclerView recyclerQuestion;
    private HotExportListAdapter adapter;
    int page = 1;
    private int freshtime=0;
    private android.widget.TextView recyclerBottom;

    public void setNeedloadmore(boolean needloadmore) {
        this.needloadmore = needloadmore;
    }

    private boolean needloadmore=true;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_faq_left;
    }

    public static MeetProfessorLeftFragment newInstance(Map<String, Object> maporg) {
        MeetProfessorLeftFragment fragment = new MeetProfessorLeftFragment();
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
    public void setBottomVisable(boolean flag){
        recyclerBottom.setVisibility(flag?View.VISIBLE:View.GONE);
    }
    public MeetProfessorLeftFragment putMap(String key, String value) {
        getBasemap().put(key, value);
        return this;
    }

    @Override
    protected void findViews() {
        initView();
        refreshLayout.finishLoadMoreWithNoMoreData();
        presenter = new MeetProfessorLeftPresenter(mContext, this);
        //System.out.println(basemap);
        presenter.getExpertList(page, getBasemap());
        adapter = new HotExportListAdapter();


        try {
            adapter.setCityNo((Integer.parseInt(LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE)) / 100 * 100)+"");
        } catch (Exception e) {
            e.printStackTrace();
        }
        recyclerQuestion.setLayoutManager(new LinearLayoutManager(mContext));
        adapter.bindToRecyclerView(recyclerQuestion);
        refreshLayout.setOnRefreshLoadMoreListener(this);
//        refreshLayout.setEnableLoadMore(false);
        if("1".equals(get("fragmentBottom"))){
            recyclerBottom.setVisibility(View.VISIBLE);
        }else {

            recyclerBottom.setVisibility(View.GONE);
        }

    }

    public void setRefreshEnable(boolean flag) {
        refreshLayout.setEnableRefresh(flag);
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        presenter.getExpertList(++page, getBasemap());
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        presenter.getExpertList(1, getBasemap());
    }

    @Override
    public void onRequestFinish() {
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
    }

    @Override
    public void onGetExpertSuccess(List<FaqExport> exportList, PageInfoEarly pageInfoEarly) {
        if (pageInfoEarly == null) {
            showEmpty();
            refreshLayout.setEnableLoadMore(false);
            return;
        }
        page = pageInfoEarly.currentPage;
        if (page == 1) {
            if(freshtime==0){
                if("本地".equals(get("fragmentCity"))){
                    if(exportList.size()==0){
                        EventBus.getDefault().post(new TabChangeModelFragment(1));
                    }
                }

            }
            adapter.setNewData(exportList);
            if (exportList.size() == 0) {
                showEmpty();
            }
        } else {
            adapter.addData(exportList);
        }
        freshtime++;
        if (pageInfoEarly.isMore != 1) {

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

    private void initView() {
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refresh_layout);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        recyclerQuestion = (RecyclerView) findViewById(R.id.recycler_question);
        recyclerBottom = (TextView) findViewById(R.id.recyclerBottom);
    }


}
