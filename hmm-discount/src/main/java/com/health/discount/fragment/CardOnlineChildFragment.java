//package com.health.discount.fragment;
//
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.health.discount.R;
//import com.health.discount.adapter.CardCenterFragmentAdapter;
//import com.healthy.library.base.BaseFragment;
//import com.healthy.library.widget.StatusLayout;
//import com.scwang.smart.refresh.layout.SmartRefreshLayout;
//import com.scwang.smart.refresh.layout.api.RefreshLayout;
//import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//public class CardOnlineChildFragment extends BaseFragment implements OnRefreshLoadMoreListener {
//    private SmartRefreshLayout layoutRefresh;
//    private StatusLayout layoutStatus;
//    private RecyclerView recycler;
//    CardCenterFragmentAdapter cardCenterListAdapter;
//    public int page = 1;
//
//    public static CardOnlineChildFragment newInstance(Map<String, Object> maporg) {
//        CardOnlineChildFragment fragment = new CardOnlineChildFragment();
//        Bundle args = new Bundle();
//        BaseFragment.bundleMap(maporg, args);
//        fragment.setArguments(args);
//        return fragment;
//    }
//    @Override
//    protected int getLayoutId() {
//        return R.layout.dis_fragment_child_coupon;
//    }
//
//    @Override
//    protected void findViews() {
//        initView();
//        cardCenterListAdapter=new CardCenterFragmentAdapter();
//        recycler.setLayoutManager(new LinearLayoutManager(mContext));
//        cardCenterListAdapter.bindToRecyclerView(recycler);
//        layoutRefresh.setOnRefreshLoadMoreListener(this);
//        layoutRefresh.setEnableRefresh(false);
//        getData();
//    }
//
//    @Override
//    public void getData() {
//        super.getData();
//        showContent();
//        List<String> emptylist=new ArrayList<>();
//        emptylist.clear();
//        for (int i = 0; i <20 ; i++) {
//            emptylist.add(""+i);
//        }
//        if(page==1||page==0){
//           // cardCenterListAdapter.setNewData(emptylist);
//            onRequestFinish();
//        }else {
//            //cardCenterListAdapter.addData(emptylist);
//            onRequestFinish();
//        }
//    }
//    @Override
//    public void onRequestFinish() {
//        super.onRequestFinish();
//        layoutRefresh.finishLoadMore();
//        layoutRefresh.finishRefresh();
//    }
//    @Override
//    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
//        page++;
//        getData();
//    }
//
//    @Override
//    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
//        page=1;
//        getData();
//    }
//
//    private void initView() {
//        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
//        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
//        recycler = (RecyclerView) findViewById(R.id.recycler);
//    }
//}
