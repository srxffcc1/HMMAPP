package com.health.servicecenter.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.flexbox.FlexboxLayout;
import com.health.servicecenter.R;
import com.health.servicecenter.adapter.AppointmentMainAdapter;
import com.health.servicecenter.contract.AppointmentMainContract;
import com.health.servicecenter.contract.AppointmentShopListContract;
import com.health.servicecenter.presenter.AppointmentMainPresenter;
import com.health.servicecenter.presenter.AppointmentShopListPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.constant.Constants;
import com.healthy.library.constant.SpKey;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.model.AppointmentListItemModel;
import com.healthy.library.model.AppointmentMainItemModel;
import com.healthy.library.model.AppointmentTimeSettingModel;
import com.healthy.library.model.ShopDetailModel;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.NavigateUtils;
import com.healthy.library.utils.PhoneUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.ImageTextView;
import com.healthy.library.widget.StatusLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务预约首页
 */
@Route(path = ServiceRoutes.SERVICE_APPOINTMENTMAIN)
public class AppointmentMainActivity extends BaseActivity implements IsFitsSystemWindows,
        AppointmentMainContract.View, AppointmentShopListContract.View, View.OnClickListener {

    private StatusLayout layoutStatus;
    private RefreshLayout layoutRefresh;
    private ImageView imgBack;
    private TextView shopName;
    private TextView shopAddress;
    private RecyclerView recyclerList;

    private AppointmentMainAdapter mMainListAdapter;
    private Map<String, Object> mMap = new HashMap<>();
    private AppointmentMainPresenter mMainPresenter;
    private int currentPage = 1;
    private List<AppointmentMainItemModel> modelLists = new ArrayList<>();
    private View mChangeShop;
    private AppointmentShopListPresenter mShopListPresenter;
    private ViewGroup mTopLayout;
    private String mShopId;
    private ImageTextView mTvPhone;
    private String mPhone;
    private ImageTextView mImgNavigation;
    private ShopDetailModel shopDetailModel;
    private List<ShopDetailModel> mShopList = new ArrayList<>();
    private TextView mShopAddressDetails;
    private int mRequestFinishCount = 0;
    private FlexboxLayout mFlexboxLayout;
    private NestedScrollView mScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_appointment_main;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        buildRecyclerView();
        mShopListPresenter = new AppointmentShopListPresenter(this, this);
        mMainPresenter = new AppointmentMainPresenter(this, this);
        getData();
    }

    private void buildRecyclerView() {
        if (mMainListAdapter != null) {
            mMainListAdapter.notifyDataSetChanged();
            return;
        }
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerList.setLayoutManager(mLayoutManager);
        mMainListAdapter = new AppointmentMainAdapter();
        recyclerList.setAdapter(mMainListAdapter);
        recyclerList.setNestedScrollingEnabled(false);
        mMainListAdapter.setNewData(modelLists);
        mMainListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (modelLists.get(position) == null) return;
                if (shopDetailModel != null && !TextUtils.isEmpty(shopDetailModel.id)) {
                    ARouter.getInstance()
                            .build(ServiceRoutes.SERVICE_APPOINTMENTDETIAL)
                            .withString("projectId", String.valueOf(modelLists.get(position).getId()))
                            .withString("shopId", shopDetailModel.id)
                            .withString("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC))
                            //.withSerializable("storeDetail", newStoreDetialModel)
                            .navigation();
                }
            }
        });
    }

    @Override
    protected void findViews() {
        super.findViews();
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        setStatusLayout(layoutStatus);
        mScrollView = findViewById(R.id.scrollView);
        layoutRefresh = (RefreshLayout) findViewById(R.id.layout_refresh);
        imgBack = (ImageView) findViewById(R.id.img_back);
        recyclerList = (RecyclerView) findViewById(R.id.recycler_list);
        shopName = findViewById(R.id.shopName);
        shopAddress = findViewById(R.id.shopAddress);
        mChangeShop = findViewById(R.id.changeShop);
        mTopLayout = findViewById(R.id.top_layout);
        mTvPhone = findViewById(R.id.phone);
        mImgNavigation = findViewById(R.id.navigation);
        mShopAddressDetails = findViewById(R.id.shopAddressDetails);
        mFlexboxLayout = findViewById(R.id.tipTitle);
        layoutStatus.setmEmptyContent("暂无服务可预约");
        layoutRefresh.finishLoadMoreWithNoMoreData();
        initListener();
    }

    @Override
    public void getData() {
        super.getData();
        //请求门店列表
        mShopListPresenter.getShopList(new SimpleHashMapBuilder<String, Object>());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.STATUS_APPOINTMENT_SHOP_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                currentPage = 1;
                //更新数据
                String id = data.getStringExtra("id");
                //优化门店列表数据不为空且size不为0
                if (!ListUtil.isEmpty(mShopList)) {
                    for (int i = 0; i < mShopList.size(); i++) {
                        if (id.equals(mShopList.get(i).id)) {
                            shopDetailModel = mShopList.get(i);
                            break;
                        }
                    }
                    mShopId = shopDetailModel.id;
                    getStoreDetail();
                }
            }
        }

    }

    @Override
    public void onGetMainListSuccess(List<AppointmentMainItemModel> modelList) {
        if (currentPage == 1) {
            modelLists.clear();
        }
        mRequestFinishCount = 0;
        if (ListUtil.isEmpty(modelList)) {
            layoutRefresh.finishLoadMoreWithNoMoreData();
        } else {
            layoutRefresh.resetNoMoreData();
            layoutRefresh.finishLoadMore();
        }
        modelLists.addAll(modelList);

        if (modelLists.size() == 0) {
            showEmpty();
            return;
        }
        //不满10条不赋予加载功能
        if (modelLists.size() < 10) {
            layoutRefresh.finishLoadMoreWithNoMoreData();
        }
        buildRecyclerView();
        if (currentPage == 1) {
            mScrollView.scrollTo(0, 0);
        }
    }

    @Override
    public void onGetMainDetailSuccess(AppointmentMainItemModel detailsModel) {

    }

    @Override
    public void onGetTimeSettingSuccess(AppointmentTimeSettingModel timeSettingModel) {

    }

    @Override
    public void onGetPayInfoSuccess(Map<String, Object> payInfoMap) {

    }

    @Override
    public void onAddServiceSuccess(long id, String payOrderId) {

    }

    @Override
    public void getPayStatusSuccess(String status) {

    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        mRequestFinishCount++;
        layoutRefresh.finishRefresh();
        if (mRequestFinishCount == 2) {
            layoutRefresh.finishLoadMore();
            mRequestFinishCount = 0;
        }
        if (ListUtil.isEmpty(mShopList)) {
            mTopLayout.setVisibility(View.GONE);
            showEmpty();
        }
    }

    /**
     * 门店列表成功回调
     *
     * @param shopList
     */
    @Override
    public void onGetShopListSuccess(ArrayList<ShopDetailModel> shopList) {

        mShopList.clear();
        mShopList.addAll(shopList);

        if (ListUtil.isEmpty(mShopList)) {
            mTopLayout.setVisibility(View.GONE);
            showEmpty();
            return;
        }
        shopDetailModel = mShopList.get(0);
        mTopLayout.setVisibility(View.VISIBLE);

        mShopId = shopDetailModel.id;
        getStoreDetail();
    }

    /**
     * 门店详情成功回调
     *
     * @param detailModel
     */
    @Override
    public void onGetStoreDetailSuccess(ShopDetailModel detailModel) {
        this.shopDetailModel = detailModel;
        setModelData();
    }

    /**
     * 获取门店详情数据
     */
    private void getStoreDetail() {
        mMap.clear();
        mMap.put("shopId", mShopId);
        mShopListPresenter.getStoreDetail(mMap);
    }

    /**
     * 设置门店内容及获取对应商品
     */
    private void setModelData() {
        if (currentPage == 1) {
            mScrollView.scrollTo(0, 0);
        }
        //产品列表进行清空
        modelLists.clear();
        String mShopName = "";
        if (!TextUtils.isEmpty(shopDetailModel.chainShopName)) {
            mShopName = shopDetailModel.shopName + "(" + shopDetailModel.chainShopName + ")";
        } else {
            mShopName = shopDetailModel.shopName;
        }
        shopName.setText(mShopName);
        shopAddress.setText(shopDetailModel.provinceName + shopDetailModel.cityName + shopDetailModel.addressAreaName);
        mShopAddressDetails.setText(shopDetailModel.addressDetails);

        mFlexboxLayout.removeAllViews();
        if (!ListUtil.isEmpty(shopDetailModel.shopBusinessOfArea)) {
            List<String> shopBusinessOfArea = shopDetailModel.shopBusinessOfArea;
            for (int i = 0; i < shopBusinessOfArea.size(); i++) {
                View tipchild = LayoutInflater.from(mContext).inflate(R.layout.item_shop_business_area_layout, null);
                TextView tipname = tipchild.findViewById(R.id.item_shop_business_area_body);
                tipname.setText(shopBusinessOfArea.get(i));
                mFlexboxLayout.addView(tipchild);
            }
        }
        mPhone = shopDetailModel.appointmentPhone;
        getMainList();
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        if (vId == R.id.phone) {
            PhoneUtils.callPhone(mContext.getApplicationContext(), mPhone);
        } else if (vId == R.id.img_back) {
            finish();
        } else if (vId == R.id.changeShop) {
            //跳转门店列表页
            ARouter.getInstance()
                    .build(ServiceRoutes.SERVICE_APPOINTMENTSHOPLIST)
                    .navigation(mActivity, Constants.STATUS_APPOINTMENT_SHOP_CODE);
        } else if (vId == R.id.navigation) {
            //跳转地图
            NavigateUtils.navigate(mContext, shopDetailModel.addressDetails,
                    String.valueOf(shopDetailModel.latitude), String.valueOf(shopDetailModel.longitude));
        }
    }

    /**
     * 获取 当前选择门店下的商品
     */
    private void getMainList() {
        mMap.clear();
        mMap.put("pageNum", String.valueOf(currentPage));
        mMap.put("shopId", mShopId);
        mMainPresenter.getMainList(mMap);
        if (currentPage == 1) {
            mScrollView.scrollTo(0, 0);
        }
    }

    private void initListener() {
        imgBack.setOnClickListener(this);
        mChangeShop.setOnClickListener(this);
        mTvPhone.setOnClickListener(this);
        mImgNavigation.setOnClickListener(this);
        layoutRefresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                currentPage++;
                getMainList();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                currentPage = 1;
                if (ListUtil.isEmpty(mShopList)) {
                    getData();
                } else {
                    getMainList();
                }
            }
        });
    }
}