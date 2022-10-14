package com.health.client.activity;

import android.os.Bundle;
import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.health.client.R;
import com.health.client.adapter.MessageHelperAdapter;
import com.health.client.contract.MessageHelperContract;
import com.health.client.model.MonMessageHelper;
import com.health.client.presenter.MessageHelperPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.SpKey;
import com.healthy.library.interfaces.OnSubmitListener;
import com.healthy.library.model.MonMessage;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.routes.AppRoutes;
import com.healthy.library.utils.OsUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
//第二种样式的
@Route(path = AppRoutes.APP_MESSAGEHELP)
public class MessageHelperActivity extends BaseActivity implements MessageHelperContract.View, OnRefreshLoadMoreListener {
    private com.healthy.library.widget.TopBar topBar;
    private com.healthy.library.widget.StatusLayout layoutStatus;
    private com.scwang.smart.refresh.layout.SmartRefreshLayout layoutRefresh;
    private RecyclerView recyclerNews;
    private MessageHelperPresenter messageHelperPresenter;
    private MessageHelperAdapter adapter;
    private int currentPage=1;
    private Map<String, Object> fansmap = new HashMap<>();;
    @Autowired
    String type;//我的粉丝 还是我的关注

    @Autowired
    String title;//我的粉丝 还是我的关注
    public String contentType;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_messagelist_gray;
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        layoutRefresh.setOnRefreshLoadMoreListener(this);
        messageHelperPresenter=new MessageHelperPresenter(mContext,this);
        adapter = new MessageHelperAdapter();
        recyclerNews.setLayoutManager(new LinearLayoutManager(mContext));
        adapter.bindToRecyclerView(recyclerNews);
        fansmap.put("memberId",new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)));
        fansmap.put("pageNum",currentPage+"");
        fansmap.put("pageSize",10+"");
        if("同城圈小助手".equals(type)){
            fansmap.put("contentType","1");
            contentType="1";
        }
        if("母婴服务小助手".equals(type)){
            fansmap.put("contentType","2");
            contentType="2";
        }
        if("问答小助手".equals(type)){
            fansmap.put("contentType","3");
            contentType="3";
        }
        adapter.setType(type);
        topBar.setTitle(title);
        messageHelperPresenter.getMessage(fansmap);
        topBar.setSubmitListener(new OnSubmitListener() {
            @Override
            public void onSubmitBtnPressed() {
                StyledDialog.init(mContext);
                StyledDialog.buildIosAlert("", "是否清除所有信息?", new MyDialogListener() {
                    @Override
                    public void onFirst() {
                        Map<String,Object> clearmap=new HashMap<>();
                        clearmap.put("contentType",contentType);
                        clearmap.put("memberId",new String(Base64.decode(SpUtils.getValue(getApplication(), SpKey.USER_ID).getBytes(), Base64.DEFAULT)));
                        messageHelperPresenter.clearMessage(clearmap);
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
        });
    }

    @Override
    public void getData() {
        super.getData();
        currentPage=1;
        fansmap.put("pageNum",currentPage+"");
        messageHelperPresenter.getMessage(fansmap);
    }
    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        currentPage++;
        fansmap.put("pageNum",currentPage+"");
        messageHelperPresenter.getMessage(fansmap);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        currentPage=1;
        fansmap.put("pageNum",currentPage+"");
        messageHelperPresenter.getMessage(fansmap);
    }
    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        layoutRefresh.finishRefresh();
        layoutRefresh.finishLoadMore();
    }
    private void initView() {
        topBar = (TopBar) findViewById(R.id.top_bar);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        recyclerNews = (RecyclerView) findViewById(R.id.recycler_news);
    }

    @Override
    public void onSuccessGetMessageList(List<MonMessageHelper> results, PageInfoEarly pageInfoEarly) {
        if (pageInfoEarly == null) {
            showEmpty();
            layoutRefresh.setEnableLoadMore(false);
            return;
        }
        currentPage = pageInfoEarly.currentPage;
        if (currentPage == 1) {
            adapter.setNewData(results);
            if (results.size() == 0) {
                showEmpty();
            }
        } else {
            adapter.addData(results);
        }
        if (pageInfoEarly.isMore != 1) {

            layoutRefresh.finishLoadMoreWithNoMoreData();
        } else {
            layoutRefresh.setNoMoreData(false);
            layoutRefresh.setEnableLoadMore(true);
        }
    }

    @Override
    public void onSuccessClearMessageList() {
        MonMessage monMessageNow= OsUtils.resolveMessageData(SpUtils.getValue(mContext,type));
        monMessageNow.num=0;
        monMessageNow.newmessagecontent="";
        SpUtils.store(mContext,monMessageNow.type,new Gson().toJson(monMessageNow));
        currentPage=1;
        fansmap.put("pageNum",currentPage+"");
        messageHelperPresenter.getMessage(fansmap);
    }
}
