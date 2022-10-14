package com.health.discount.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.health.discount.R;
import com.health.discount.adapter.SeckillListAdapter;
import com.health.discount.contract.NewUserListContract;
import com.health.discount.presenter.NewUserListPresenter;
import com.health.discount.presenter.NewUserSingleListPresenter;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.business.SeckShareDialog;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.NewUserListModel;
import com.healthy.library.presenter.CardBoomPresenter;
import com.healthy.library.utils.LogUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.StatusLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.umeng.socialize.UMShareListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SeckillListFragment extends BaseFragment implements NewUserListContract.View, UMShareListener,
        OnLoadMoreListener, BaseAdapter.OnOutClickListener {

    private String historyQuery;
    private String marketingId;
    private String status;

    private SeckillListAdapter adapter;
    private RecyclerView recycler;
    private SmartRefreshLayout layoutRefresh;
    private StatusLayout layout_status;
    private NewUserSingleListPresenter presenter;
    private Map<String, Object> map = new HashMap<>();
    private int pageNum = 1;
    private Bitmap bitmap = null;
    private boolean isShare = false;
    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;
    private CountDownTimer countDownTimer;
    private ImageView shareImg;

    public SeckillListFragment() {
    }

    public static SeckillListFragment newInstance(String historyQuery, String marketingId, String status) {
        SeckillListFragment fragment = new SeckillListFragment();
        Bundle args = new Bundle();
        args.putString("historyQuery", historyQuery);
        args.putString("marketingId", marketingId);
        args.putString("status", status);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            historyQuery = getArguments().getString("historyQuery");
            marketingId = getArguments().getString("marketingId");
            status = getArguments().getString("status");
            LogUtils.e("status" + status);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_seckill_list;
    }

    private void initData() {
        presenter = new NewUserSingleListPresenter(mContext, this);
        adapter = new SeckillListAdapter();
        virtualLayoutManager = new VirtualLayoutManager(mContext);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        recycler.setLayoutManager(virtualLayoutManager);
        recycler.setAdapter(delegateAdapter);
        delegateAdapter.addAdapter(adapter);
        adapter.setOutClickListener(this);
        layoutRefresh.setOnLoadMoreListener(this);
        layoutRefresh.setEnableRefresh(false);
        getData();
        new CardBoomPresenter(mContext).boom("3");
    }

    @Override
    public void getData() {
        super.getData();
        map.put("pageNum", pageNum + "");
        map.put("pageSize", "10");
        map.put("provinceCode", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE));
        map.put("cityCode", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE));
        map.put("areasCode", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE));
        map.put("marketingType", "3");
        map.put("historyQuery", historyQuery);
        map.put("marketingId", marketingId);
        map.put("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP));
        presenter.getList(map);
    }

    @Override
    protected void findViews() {
        recycler = findViewById(R.id.recycler);
        layoutRefresh = findViewById(R.id.layout_refresh);
        layout_status = findViewById(R.id.layout_status);
        shareImg = (ImageView) findViewById(R.id.shareImg);
        initData();
        shareImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeckShareDialog dialog = SeckShareDialog.newInstance();
                dialog.setActivityDialog(3, 3, ListUtil.isEmpty(adapter.getDatas()) ? null : adapter.getDatas().get(0));
                dialog.show(getChildFragmentManager(), "分享");
            }
        });
    }

    @Override
    public void onSucessGetList(List<NewUserListModel> result) {
        if (getDifference(status)) {
            adapter.setResult(true);
        } else {
            adapter.setResult(false);
        }
        if (pageNum == 1) {
            if (result == null || result.size() == 0) {
                showEmpty();
            } else {
                adapter.clear();
                adapter.setData((ArrayList) result);
            }
        } else {
            if (result == null || result.size() == 0) {
                layoutRefresh.finishLoadMoreWithNoMoreData();
            } else {
                adapter.addDatas((ArrayList) result);
                layoutRefresh.finishLoadMore();
            }
        }
    }

    @Override
    public void onSucessIsNewAppMember(int result) {

    }

    @Override
    public void onSuccessAddRemind(String result, String type) {
        if (type != null) {
            if (type.equals("0")) {
                showToast("设置提醒成功，系统会在秒杀开始前3分钟通知您~");
            } else {
                showToast("已取消提醒，在开抢前我们会提醒不到您哦~");
            }
        } else {
            showToast(result);
        }

        pageNum = 1;
        getData();
    }

    @Override
    public void onLoadMore(@NonNull @NotNull RefreshLayout refreshLayout) {
        pageNum++;
        getData();
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        layoutRefresh.finishRefresh();
        layoutRefresh.finishLoadMore();
    }

    private Bitmap getBitmap(View view) {
        View screenView = getActivity().getWindow().getDecorView();
        screenView.setDrawingCacheEnabled(true);
        screenView.buildDrawingCache();
        //获取屏幕整张图片
        Bitmap bitmap = screenView.getDrawingCache();
        try {
            if (bitmap != null) {
                //需要截取控件的长和宽
                int outWidth = view.getWidth();
                int outHeight = (int) TransformUtil.dp2px(mContext, 330);
                //获取需要截图部分的在屏幕上的坐标(view的左上角坐标）
                int[] viewLocationArray = new int[2];
                view.getLocationOnScreen(viewLocationArray);
                //从屏幕中截取指定区域
                bitmap = Bitmap.createBitmap(bitmap, viewLocationArray[0], viewLocationArray[1], outWidth, outHeight);
                view.setDrawingCacheEnabled(false);  //禁用DrawingCahce否则会影响性能
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private boolean getDifference(String status) {
        if (status != null) {
            if (status.equals("0")) {//0未开始
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public void outClick(@NotNull String function, @NotNull Object obj) {
        if (function.equals("remind")) {
            String[] strings = ((String) obj).split(",");
            presenter.addRemind(new SimpleHashMapBuilder<String, Object>()
                    .puts("goodsId", strings[0])
                    .puts("mapMarketingGoodsId", strings[1])
                    .puts("type", strings[2])
                    .puts("marketingId", marketingId)
                    .puts("marketingType", "3"), strings[2]);
        }
    }
}