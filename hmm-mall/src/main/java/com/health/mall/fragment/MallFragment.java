//package com.health.mall.fragment;
//
//import android.Manifest;
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import androidx.annotation.NonNull;
//import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.TextView;
//
//import com.alibaba.android.arouter.facade.annotation.Route;
//import com.alibaba.android.arouter.launcher.ARouter;
//import com.amap.api.location.AMapLocation;
//import com.amap.api.location.AMapLocationClient;
//import com.amap.api.location.AMapLocationClientOption;
//import com.amap.api.location.AMapLocationListener;
//import com.amap.api.maps.model.LatLng;
//import com.health.mall.R;
//import com.health.mall.adapter.MallIndexAdapter;
//import com.health.mall.contract.MallContract;
//import com.health.mall.decoration.MallIndexDecoration;
//import com.healthy.library.model.MallIndexModel;
//import com.health.mall.presenter.MallPresenter;
//import com.healthy.library.base.BaseFragment;
//import com.healthy.library.routes.MallRoutes;
//import com.healthy.library.utils.LocationUtil;
//import com.healthy.library.utils.PermissionUtils;
//import com.healthy.library.widget.StatusLayout;
//
//import org.jetbrains.annotations.NotNull;
//
//import java.util.List;
//
///**
// * @author Li
// * @date 2019/03/01 11:24
// * @des 商城
// */
//@Route(path = MallRoutes.MALL_MODULE)
//public class MallFragment extends BaseFragment implements
//        MallIndexAdapter.OnMallIndexItemClickListener, AMapLocationListener,
//        MallContract.View, SwipeRefreshLayout.OnRefreshListener {
//    private RecyclerView mRecyclerIndex;
//    private MallIndexAdapter mIndexAdapter;
//    private TextView mTxtLocationCity;
//    private SwipeRefreshLayout mLayoutRefresh;
//    private StatusLayout mStatusLayout;
//    private static final int RC_LOCATION = 102;
//    public AMapLocationClient mLocationClient;
//    public AMapLocationClientOption mLocationOption = null;
//    private String[] mPermissions = new String[]{
//            Manifest.permission.ACCESS_COARSE_LOCATION,
//            Manifest.permission.ACCESS_FINE_LOCATION
//    };
//    private LatLng mLocationLatLng;
//    private MallPresenter mMallPresenter;
//    private String mAdCode;
//    private static final int RC_PROVINCE_CITY = 469;
//    private LocationUtil mLocationUtil;
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.fragment_mall;
//    }
//
//    @Override
//    protected void findViews() {
//        mRecyclerIndex = getContentView().findViewById(R.id.recycler_index);
//        mTxtLocationCity = getContentView().findViewById(R.id.txt_location_city);
//        mLayoutRefresh = getContentView().findViewById(R.id.layout_refresh);
//        mStatusLayout = getContentView().findViewById(R.id.layout_status);
//    }
//
//
//    @Override
//    protected void onCreate() {
//        super.onCreate();
//        init();
//    }
//
//    @Override
//    protected void onFirstVisible() {
//        super.onFirstVisible();
//        if (PermissionUtils.hasPermissions(mContext, mPermissions)) {
////            locate();
//            mLocationUtil.startLocation();
//        } else {
//            requestPermissions(mPermissions, RC_LOCATION);
//        }
//    }
//
//
//    private void init() {
//        setStatusLayout(mStatusLayout);
//        mMallPresenter = new MallPresenter(mContext, this, getLifecycle());
//
//        mTxtLocationCity.setOnClickListener(this);
//        mIndexAdapter = new MallIndexAdapter(mContext);
//        getLifecycle().addObserver(mIndexAdapter);
//        mIndexAdapter.setItemClickListener(this);
//        mRecyclerIndex.setLayoutManager(new LinearLayoutManager(mContext));
//        mRecyclerIndex.addItemDecoration(new MallIndexDecoration(mContext));
//        mRecyclerIndex.setAdapter(mIndexAdapter);
//        mLayoutRefresh.setOnRefreshListener(this);
//
//        mLocationUtil = new LocationUtil(mContext, new LocationUtil.OnLocationListener() {
//            @Override
//            public void onLocationStart() {
//                showLoading();
//            }
//
//            @Override
//            public void onLocationSuccess(@NotNull AMapLocation aMapLocation) {
//                double lat = aMapLocation.getLatitude();
//                double lng = aMapLocation.getLongitude();
//                mTxtLocationCity.setText(aMapLocation.getCity());
//                mLocationLatLng = new LatLng(lat, lng);
//                mAdCode = aMapLocation.getAdCode();
//                mMallPresenter.getData(mAdCode, String.valueOf(lat), String.valueOf(lng));
//            }
//
//            @Override
//            public void onLocationFail(int errCode) {
//                mLocationUtil.startLocation();
//            }
//        });
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == RC_LOCATION) {
//            if (PermissionUtils.hasPermissions(mContext, permissions)) {
//                locate();
//            } else {
//                if (PermissionUtils.somePermissionPermanentlyDenied(mActivity, permissions)) {
//                    PermissionUtils.showRationale(mActivity);
//                } else {
//                    requestPermissions(mPermissions, RC_LOCATION);
//                }
//            }
//        }
//    }
//
//    @Override
//    public void onGoodsClick(int pos, View view) {
//        MallIndexModel model = mIndexAdapter.getData().get(pos);
//        ARouter.getInstance().build(ServiceRoutes.SERVICE_DETAIL)
//                .withString("selfId", model.getId())
//                .withString("shopId", model.getShopId())
//                .withString("systemType", model.getBrand())
//                .navigation();
//    }
//
//    @Override
//    public void onStoreClick(int pos, View view) {
//        MallIndexModel model = mIndexAdapter.getData().get(pos);
//        ARouter.getInstance().build(MallRoutes.MALL_STORE_DETAIL)
//                .withString("shopId", model.getId())
//                .withString("shopBrand", model.getBrand())
//                .navigation();
//    }
//
//    @Override
//    public void onGoodsTitleClick() {
//        ARouter.getInstance()
//                .build(MallRoutes.MALL_GOODS_LIST)
//                .withString("adCode", mAdCode)
//                .withString("lat", String.valueOf(mLocationLatLng.latitude))
//                .withString("lng", String.valueOf(mLocationLatLng.longitude)).navigation();
//    }
//
//    @Override
//    public void onStoreTitleClick() {
//        ARouter.getInstance()
//                .build(MallRoutes.MALL_STORE_LIST)
//                .withString("adCode", mAdCode)
//                .withString("lat", String.valueOf(mLocationLatLng.latitude))
//                .withString("lng", String.valueOf(mLocationLatLng.longitude))
//                .navigation();
//    }
//
//
//    @Override
//    public void onClick(View v) {
//        super.onClick(v);
//        ARouter.getInstance().build(LibraryRoutes.LIBRARY_LOC)
//                .navigation(mActivity, RC_PROVINCE_CITY);
//    }
//
//    /**
//     * 定位
//     */
//    private void locate() {
//        showLoading();
//        mLocationClient = new AMapLocationClient(mContext);
//        mLocationOption = new AMapLocationClientOption();
//        mLocationOption.setOnceLocation(true);
//        //设置定位监听
//        mLocationClient.setLocationListener(this);
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//        mLocationClient.setLocationOption(mLocationOption);
//        mLocationClient.startLocation();
//    }
//
//    @Override
//    public void onLocationChanged(AMapLocation aMapLocation) {
//        if (aMapLocation == null) {
//            return;
//        }
//        if (aMapLocation.getErrorCode() == 0) {
//            double lat = aMapLocation.getLatitude();
//            double lng = aMapLocation.getLongitude();
//            mTxtLocationCity.setText(aMapLocation.getCity());
//            mLocationLatLng = new LatLng(lat, lng);
//            mAdCode = aMapLocation.getAdCode();
//            mMallPresenter.getData(mAdCode, String.valueOf(lat), String.valueOf(lng));
//        } else {
//            mLocationClient.startLocation();
//        }
//    }
//
//    @Override
//    public void onGetDataSuccess(List<MallIndexModel> indexModels) {
//
//        if (indexModels.size() == 0) {
//            MallIndexModel model = new MallIndexModel();
//            model.setType(MallIndexAdapter.TYPE_NONACTIVATED);
//            indexModels.add(model);
//        }
//        mIndexAdapter.setData(indexModels);
//        mLayoutRefresh.setRefreshing(false);
//    }
//
//    @Override
//    public void onGetDataFail() {
//        mLayoutRefresh.setRefreshing(false);
//    }
//
//    @Override
//    public void onRefresh() {
//        if (mLocationLatLng != null && !TextUtils.isEmpty(mAdCode)) {
//            mMallPresenter.getData(mAdCode, String.valueOf(mLocationLatLng.latitude),
//                    String.valueOf(mLocationLatLng.longitude));
//        } else {
////            locate();
//            mLocationUtil.startLocation();
//        }
//    }
//
//    @Override
//    public void onRetryClick() {
//        onRefresh();
//    }
//
//
//    @Override
//    public void onChooseCityClick() {
//        ARouter.getInstance().build(LibraryRoutes.LIBRARY_LOC)
//                .navigation(mActivity, RC_PROVINCE_CITY);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == RC_PROVINCE_CITY && resultCode == Activity.RESULT_OK) {
//            if (data != null && data.getExtras() != null) {
//                Bundle extras = data.getExtras();
//                mTxtLocationCity.setText(extras.getString("city"));
//                mAdCode = extras.getString("code");
//                mLocationLatLng = new LatLng(extras.getDouble("lat"),
//                        extras.getDouble("lng"));
//                onRefresh();
//            }
//        }
//    }
//}
