package com.health.client.activity;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Base64;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.health.client.R;
import com.health.client.adapter.MessageTipAdapter;
import com.health.client.contract.MessageHelperTipContract;
import com.health.client.model.MonMessageTip;
import com.health.client.presenter.MessageHelperTipPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.SpKey;
import com.healthy.library.interfaces.OnSubmitListener;
import com.healthy.library.model.MonMessage;
import com.healthy.library.routes.AppRoutes;
import com.healthy.library.model.PageInfoEarly;
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

@Route(path = AppRoutes.APP_MESSAGETIP)
public class MessageTipActivity extends BaseActivity implements MessageHelperTipContract.View, OnRefreshLoadMoreListener {
    private TopBar topBar;
    private StatusLayout layoutStatus;
    private SmartRefreshLayout layoutRefresh;
    private RecyclerView recyclerNews;
    private MessageHelperTipPresenter messageHelperPresenter;
    private MessageTipAdapter adapter;
    private int currentPage=1;
    private Map<String, Object> fansmap = new HashMap<>();
    @Autowired
    String type;//我的粉丝 还是我的关注
    @Override
    protected int getLayoutId() {
        return R.layout.activity_messagelist_gray;
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.top_bar);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        recyclerNews = (RecyclerView) findViewById(R.id.recycler_news);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        layoutRefresh.setOnRefreshLoadMoreListener(this);
        messageHelperPresenter=new MessageHelperTipPresenter(mContext,this);
        adapter = new MessageTipAdapter();
        recyclerNews.setLayoutManager(new LinearLayoutManager(mContext));
        adapter.bindToRecyclerView(recyclerNews);
        fansmap.put("memberId",new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)));
        fansmap.put("pageNum",currentPage+"");
        fansmap.put("pageSize",10+"");
//        if("通知".equals(type)){
//            fansmap.put("replyType",new String[]{"2"});
//        }
        topBar.setTitle(type);
        messageHelperPresenter.getMessage(fansmap);
        topBar.setSubmitListener(new OnSubmitListener() {
            @Override
            public void onSubmitBtnPressed() {
                StyledDialog.init(mContext);
                StyledDialog.buildIosAlert("", "是否清除所有信息?", new MyDialogListener() {
                    @Override
                    public void onFirst() {
                        Map<String,Object> clearmap=new HashMap<>();
//                        clearmap.put("contentType",contentType);
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
        fansmap.put("pageNum",currentPage+"");
        messageHelperPresenter.getMessage(fansmap);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        currentPage++;
        getData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        currentPage=1;
        getData();
    }
    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        layoutRefresh.finishRefresh();
        layoutRefresh.finishLoadMore();
    }

    @Override
    public void onSuccessGetMessageList(List<MonMessageTip> results, PageInfoEarly pageInfoEarly) {
        ////System.out.println("回来了");
        if (pageInfoEarly == null) {
            showEmpty();
            ////System.out.println("回来了空");
            layoutRefresh.setEnableLoadMore(false);
            return;
        }
        currentPage = pageInfoEarly.currentPage;
        if (currentPage == 1) {
            adapter.setNewData(results);
            if (results.size() == 0) {
                ////System.out.println("回来了空2");
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
        getData();
    }
}
