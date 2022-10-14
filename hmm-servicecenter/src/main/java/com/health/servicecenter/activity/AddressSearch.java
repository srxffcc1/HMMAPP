package com.health.servicecenter.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.health.servicecenter.R;
import com.health.servicecenter.adapter.SearchAddressAdapter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.AddressListModel;
import com.healthy.library.model.AddressModel;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.IntentUtils;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.utils.NavigateUtils;
import com.healthy.library.business.MessageDialog;

import java.util.ArrayList;
import java.util.List;

@Route(path = ServiceRoutes.SERVICE_SEARCH_ADDRESS)
public class AddressSearch extends BaseActivity implements
        AMap.OnMapClickListener, AMap.OnMarkerDragListener, AMap.OnMarkerClickListener,
        PoiSearch.OnPoiSearchListener, AMap.OnCameraChangeListener, AMapLocationListener {
    private ImageView img_back;
    private MapView mMapView;
    AMap aMap = null;//初始化地图控制器对象
    private int currentPage = 1;// 当前页面，从1开始计数
    private LatLonPoint lp;

    private PoiResult poiResult; // 写字楼poi返回的结果
    private PoiSearch.Query poiQuery;// 写字楼Poi查询条件类
    private PoiSearch poiSearch;//写字楼poi查询
    private List<PoiItem> poiItemList;// 写字楼poi数据

    private String keyWord = "";
    private TextView mSearchText;
    private BottomSheetBehavior behavior;
    private RecyclerView behavior_recycle;
    private SearchAddressAdapter searchAddressListAdapter;
    private TextView city_title;
    //声明mlocationClient对象
    private AMapLocationClient mLocationClient;
    //声明mLocationOption对象
    private AMapLocationClientOption mLocationOption = null;

    private Double latitude;//纬度
    private Double longitude;//经度

    private LatLng latLng;
    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_address_search;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mMapView.onCreate(savedInstanceState);// 此方法须覆写，虚拟机需要在很多情况下保存地图绘制的当前状态。
        locate();

    }

    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery() {
        //keyWord = mSearchText.getText().toString().trim();
        currentPage = 1;
        poiQuery = new PoiSearch.Query("", "商务写字楼|住宅小区", "");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        poiQuery.setPageSize(50);// 设置每页最多返回多少条poiitem
        poiQuery.setPageNum(currentPage);// 设置查第一页
        poiQuery.setCityLimit(true);

        if (lp != null) {
            poiSearch = new PoiSearch(this, poiQuery);
            poiSearch.setOnPoiSearchListener(this);
            poiSearch.setBound(new PoiSearch.SearchBound(lp, 5000, true));//
            // 设置搜索区域为以lp点为圆心，其周围5000米范围
            poiSearch.searchPOIAsyn();// 异步搜索
        }
    }

    private void initMap(int type) {

        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        if (type == 0) {
            if (getIntent().getSerializableExtra("model") != null) {
                AddressListModel addressListModel = (AddressListModel) getIntent().getSerializableExtra("model");
                latLng = new LatLng(Double.parseDouble(addressListModel.getLat()), Double.parseDouble(addressListModel.getLng()));//设置为用户选择的地址
                lp = new LatLonPoint(Double.parseDouble(addressListModel.getLat()), Double.parseDouble(addressListModel.getLng()));
                mSearchText.setHint(addressListModel.getAddress() + "");
                city_title.setText(addressListModel.getCityName() + "");
            } else {
                latLng = new LatLng(latitude, longitude);//初始化经纬度
                lp = new LatLonPoint(latitude, longitude);
            }
        } else {
            latLng = new LatLng(latitude, longitude);//初始化经纬度
            lp = new LatLonPoint(latitude, longitude);
        }
//        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
//        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
//        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.showIndoorMap(true);//显示室内地图
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));//设置默认缩放比例
        aMap.getUiSettings().setScrollGesturesEnabled(true);//启用滑动手势
        aMap.getUiSettings().setZoomGesturesEnabled(true);//启用缩放手势
        aMap.getUiSettings().setRotateGesturesEnabled(false);//禁用旋转手势
        aMap.getUiSettings().setTiltGesturesEnabled(false);//禁用倾斜手势

        addMarker(latLng);
        aMap.setOnMarkerDragListener(this);
        aMap.setOnMapClickListener(this);
        aMap.setOnMarkerClickListener(this);
        // 定义 Marker 点击事件监听
        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
//                if (!isItemClickAction) {
//                    locationMarker.setPosition(cameraPosition.target);
//                }
            }
        });
    }

    @Override
    protected void findViews() {
        super.findViews();
        img_back = findViewById(R.id.img_back);
        mMapView = findViewById(R.id.map);
        mSearchText = findViewById(R.id.serarchKeyWord);
        behavior_recycle = findViewById(R.id.behavior_recycle);
        city_title = findViewById(R.id.city_title);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        behavior_recycle.setLayoutManager(new LinearLayoutManager(this));
        searchAddressListAdapter = new SearchAddressAdapter();
        behavior_recycle.setAdapter(searchAddressListAdapter);
        city_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddressSearch.this, ProvinceCity.class);
                startActivityForResult(intent, 1);
            }
        });
        mSearchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddressSearch.this, AddressSearchList.class);
                intent.putExtra("city", city_title.getText().toString());
                startActivityForResult(intent, 2);
            }
        });
//        final View bottomSheet = findViewById(R.id.bottom_sheet);
//        behavior = BottomSheetBehavior.from(bottomSheet);
//        behavior.setPeekHeight(1030);//设置首次弹出的高度
//        behavior.setHideable(false);//禁止滑动隐藏
//        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
//            @Override
//            public void onStateChanged(@NonNull View view, int i) {
//            }
//
//            @Override
//            public void onSlide(@NonNull View view, float v) {
//                ViewGroup.LayoutParams params = view.getLayoutParams();
//                params.height = 1030;
//                view.setLayoutParams(params);
//            }
//        });
        searchAddressListAdapter.setmItemClickListener(new SearchAddressAdapter.ItemClickListener() {
            @Override
            public void onClick(AddressModel model) {
                Intent intent = new Intent();
                intent.putExtra("model", model);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (data == null) {
                return;
            }
            city_title.setText(data.getStringExtra("city"));
            mSearchText.setHint("请输入您的收货地址");
//            longitude = Double.parseDouble(LocUtil.getLongitude(mContext, SpKey.LOC_ORG));
//            latitude = Double.parseDouble(LocUtil.getLatitude(mContext, SpKey.LOC_ORG));
//            successLocation();
//            initMap(1);
//            doSearchQuery();

        }
        if (requestCode == 2) {
            Intent intent = new Intent();
            if (data == null) {
                return;
            }
            //intent.putExtra("district", data.getStringExtra("district"));
            intent.putExtra("model", data.getSerializableExtra("model"));
            setResult(RESULT_OK, data);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onPoiSearched(PoiResult result, int rcode) {
        if (rcode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(poiQuery)) {// 是否是同一条
                    poiResult = result;
                    poiItemList = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    if (poiItemList != null && poiItemList.size() > 0) {
                        //清除POI信息显示
                        setAdapter();
                    } else {
                        showToast("对不起，没有搜索到附近地址");
                    }
                }
            } else {
                showToast("对不起，没有搜索到附近地址");
            }
        } else {
            showToast(rcode + "");
        }
    }

    private void setAdapter() {
        searchAddressListAdapter.setData((ArrayList) poiItemList);
        searchAddressListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    private void addMarker(LatLng latLng) {
        //往map地图中添加marker

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.setFlat(true);
        //markerOptions.anchor(0.5f, 0.5f);
        //markerOptions.title(title);
        markerOptions.draggable(true);//设置Marker可拖动
        markerOptions.position(latLng);
        markerOptions.icon(customizeMarkerIcon(null, R.layout.item_address_search_map_marker_layout));
        marker = aMap.addMarker(markerOptions);

    }

    private BitmapDescriptor customizeMarkerIcon(String title, int id) {
        //返回markerIcon
        final View markerView = LayoutInflater.from(this).inflate(id, null);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(convertViewToBitmap(markerView));
        return bitmapDescriptor;

    }

    private Bitmap convertViewToBitmap(View view) {
        //view 转bitmap
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;

    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation == null) {
            return;
        }
        if (aMapLocation.getErrorCode() == 0) {
            LocUtil.storeLocation(mContext, aMapLocation);
            latitude = aMapLocation.getLatitude();//获取纬度
            longitude = aMapLocation.getLongitude();//获取经度
            successLocation();
            initMap(0);
            doSearchQuery();
        } else {
            if (NavigateUtils.openGPSSettings(mContext)) {
                mLocationClient.startLocation();
            } else {
                MessageDialog.newInstance()
                        .setMessageTopRes(R.drawable.dialog_message_icon_loc,"开启定位","为给您提供更好的本地化服务，请您到系统设置中打开GPS定位")
                        .setMessageOkClickListener(new MessageDialog.MessageOkClickListener() {
                            @Override
                            public void onMessageOkClick(View view) {
                                IntentUtils.toLocationSetting(mContext);
                            }
                        })
                        .show(getSupportFragmentManager(),"打开定位");

            }
        }
    }

    /**
     * 定位
     */
    private void locate() {
        showLoading();
        mLocationClient = new AMapLocationClient(mContext);
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setOnceLocation(true);
        //设置定位监听
        mLocationClient.setLocationListener(this);
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.startLocation();
    }

    private void successLocation() {
        city_title.setText(LocUtil.getCityName(mContext, SpKey.LOC_ORG));
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        if (marker != null) {
            marker.setPosition(cameraPosition.target);
        }
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }
}