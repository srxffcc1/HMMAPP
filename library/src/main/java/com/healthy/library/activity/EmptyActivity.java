package com.healthy.library.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.healthy.library.R;
import com.healthy.library.adapter.EmptyFragmentAdapter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.routes.LibraryRoutes;
import com.healthy.library.widget.StatusLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

@Route(path = LibraryRoutes.LIBRARY_TMP)
public class EmptyActivity extends BaseActivity implements OnRefreshLoadMoreListener {
    EmptyFragmentAdapter emptyFragmentAdapter;

    public int page = 1;
    @Autowired
    int layoutAdapterItemRes;
    private SmartRefreshLayout refreshLayout;
    private StatusLayout layoutStatus;
    private RecyclerView recyclerQuestion;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_empty_fragment;
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
//        if(layoutAdapterItemRes!=0){
//            emptyFragmentAdapter=new EmptyFragmentAdapter(layoutAdapterItemRes);
//        }else {
//            emptyFragmentAdapter=new EmptyFragmentAdapter();
//        }
//        recyclerQuestion.setLayoutManager(new LinearLayoutManager(mContext));
//        recyclerQuestion.setItemAnimator(new SlideInLeftAnimator());
//        emptyFragmentAdapter.bindToRecyclerView(recyclerQuestion);
//        refreshLayout.setOnRefreshLoadMoreListener(this);
//        refreshLayout.setEnableRefresh(false);
//        getData();
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        for (int i = 0; i < 3; i++) {
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    emptyFragmentAdapter.getData().remove(0);
//                                    emptyFragmentAdapter.notifyItemRemoved(0);
//                                }
//                            });
//                            try {
//                                Thread.sleep(300);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                }).start();
//
//            }
//        },2000);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        getData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page=1;
        getData();
    }
    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        refreshLayout.finishLoadMore();
        refreshLayout.finishRefresh();
    }
    @Override
    public void getData() {
        super.getData();
        showContent();
//        List<String> emptylist=new ArrayList<>();
//        emptylist.clear();
//        for (int i = 0; i <20 ; i++) {
//            emptylist.add(""+i);
//        }
//        if(page==1||page==0){
//            emptyFragmentAdapter.setNewData(emptylist);
//            onRequestFinish();
//        }else {
//            emptyFragmentAdapter.addData(emptylist);
//            onRequestFinish();
//        }
    }

    private void initView() {
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refresh_layout);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        recyclerQuestion = (RecyclerView) findViewById(R.id.recycler_question);
    }
}
