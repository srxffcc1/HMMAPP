package com.health.servicecenter.activity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.health.servicecenter.R;
import com.health.servicecenter.adapter.NearbyStoreAdapter;
import com.health.servicecenter.contract.NearbyStoresContract;
import com.healthy.library.model.StoreListModel;
import com.health.servicecenter.presenter.NearbyStorePresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.constant.SpKey;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.IntentUtils;
import com.healthy.library.businessutil.LocUtil;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;
import com.liys.doubleclicklibrary.click.DoubleClickCancel;

import org.jetbrains.annotations.NotNull;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(path = ServiceRoutes.SERVICE_NEARBYSTORES)
public class NearbyStores extends BaseActivity implements DoubleClickCancel, IsFitsSystemWindows, BaseAdapter.OnOutClickListener, NearbyStoresContract.View {
    @Autowired
    String shopId;
    @Autowired
    String provinceCode;
    @Autowired
    String cityCode;
    @Autowired
    String areasCode;

    private MapView mMapView;
    private AMap aMap = null;//初始化地图控制器对象
    private LatLng latLng;

    private TextView makerTxt;
    private TextView top_addressTxt;
    private ImageView img_back;
    private View behavior_top_indicator;
    private List<StoreListModel> markerList = new ArrayList<>();
    private List<StoreListModel> dataList = new ArrayList<>();
    private Marker markerLast;//记录最后一次点击的maker
    private Marker markerFirst;//记录初始化的maker
    private int lastId = -1;
    private boolean isFirstClick = false;
    private RecyclerView recyclerView;
    private NearbyStoreAdapter nearbyStoreAdapter;
    private BottomSheetBehavior behavior;
    private NearbyStorePresenter nearbyStorePresenter;
    public int expandHeight = 1500;
    public int collHeight = 550;
    public int nowPeekHeight = collHeight;
    private Map<String, Object> map = new HashMap<>();
    private ImageView behaviorTopIndicator2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_nearby_stores;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        recyclerView = findViewById(R.id.dialog_recycle);
        top_addressTxt = findViewById(R.id.top_addressTxt);
        img_back = findViewById(R.id.img_back);
        behavior_top_indicator = findViewById(R.id.behavior_top_indicator);
        top_addressTxt.setText(LocUtil.getCityNameOld(mContext, SpKey.LOC_CHOSE));
        nearbyStorePresenter = new NearbyStorePresenter(this, this);
        mMapView.onCreate(savedInstanceState);// 此方法须覆写，虚拟机需要在很多情况下保存地图绘制的当前状态。

        final View bottomSheet = findViewById(R.id.bottom_sheet);
        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setPeekHeight(nowPeekHeight);//设置首次弹出的高度
        behavior.setHideable(false);//禁止滑动隐藏

        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        //int widthPixels = outMetrics.widthPixels;
        int heightPixels = outMetrics.heightPixels;
        expandHeight = (int) (heightPixels * 0.7);//设置behavior最大高度为屏幕高度的70%
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
//                ////System.out.println("设置高度状态;" + behavior.getState());
//                if(behavior.getState()==BottomSheetBehavior.STATE_EXPANDED){
//                    nowPeekHeight=expandHeight;
//                }if(behavior.getState()==BottomSheetBehavior.STATE_COLLAPSED){
//                    if(nowPeekHeight==expandHeight){
//                        nowPeekHeight=collHeight;
//                    }else {
//                        nowPeekHeight=expandHeight;
//                    }
//                }

            }

            @Override
            public void onSlide(@NonNull View view, float v) {
//                ////System.out.println("设置高度滑动");
//                nowPeekHeight = expandHeight;
//                behavior.setPeekHeight(collHeight);//设置首次弹出的高度
                ViewGroup.LayoutParams params = view.getLayoutParams();
                params.height = expandHeight;
                view.setLayoutParams(params);
            }
        });
        behavior_top_indicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu();

            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        map.put("provinceCode", provinceCode);
        map.put("cityCode", cityCode);
        map.put("areasCode", areasCode);
        map.put("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_CHOSE));
        map.put("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_CHOSE));
        map.put("shopId", shopId + "");
        getData();
    }

    private void showMenu() {
//        if (nowPeekHeight == collHeight) {
//            nowPeekHeight = expandHeight;
//        } else {
//            nowPeekHeight = collHeight;
//        }
//        ////System.out.println("设置高度" + nowPeekHeight);
//        behavior.setPeekHeight(nowPeekHeight);//设置首次弹出的高度
//        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void initMap(LatLng latLng) {
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        //aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        //aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        //aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.showIndoorMap(true);//显示室内地图
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));//设置默认缩放比例
        aMap.getUiSettings().setRotateGesturesEnabled(false);//禁用旋转手势
        aMap.getUiSettings().setTiltGesturesEnabled(false);//禁用倾斜手势
    }

    @Override
    public void getData() {
        super.getData();
        map.put("pageNum", 1 + "");
        map.put("pageSize", 1000 + "");
        nearbyStorePresenter.getStoreList(map);
    }

    @Override
    public void onGetStoreListSuccess(List<StoreListModel> list) {
        if (list == null || list.size() == 0) {
            showToast("获取门店失败");
            return;
        }
        markerList.addAll(list);
        dataList.addAll(list);
        initDataList();
        if (!TextUtils.isEmpty(shopId) && markerList != null) {
            for (int i = 0; i < markerList.size(); i++) {
                if (markerList.get(i).getIsSelected() == 1) {
                    latLng = new LatLng(markerList.get(i).getLatitude(), markerList.get(i).getLongitude());//初始化经纬度
                }
            }
        } else {
            latLng = new LatLng(markerList.get(0).getLatitude(), markerList.get(0).getLongitude());//初始化经纬度
//            latLng = new LatLng(Double.parseDouble(LocUtil.getLatitude(mContext, SpKey.LOC_CHOSE)),
//                    Double.parseDouble(LocUtil.getLongitude(mContext, SpKey.LOC_CHOSE)));//初始化经纬度
        }
        initMap(latLng);
        addMarkersToMap();
        nearbyStoreAdapter = new NearbyStoreAdapter();
        nearbyStoreAdapter.setOutClickListener(this);
        nearbyStoreAdapter.setData((ArrayList) dataList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(nearbyStoreAdapter);
        // 绑定 Marker 被点击事件
        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                if (aMap != null) {
                    if (!isFirstClick) {
                        clearMarkers();//清空第一个marker
                    }
                    marker.hideInfoWindow();
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);//将弹窗恢复为初始高度
                    Marker markerNew = changeMarkerImg(marker);//改变当前选中的maker背景
                    resetMarker(markerNew.getPeriod());//重置除点击按钮外的所有按钮
                    markerLast = markerNew;// 记录当前marker对象，用于下次点击时判断
                    if (changeList(markerNew.getPeriod())) {//改变集合数据顺序,把用户点击的门店数据设置到0号位置
                        nearbyStoreAdapter.setData((ArrayList) dataList);
                        recyclerView.scrollToPosition(0);
                    }
                }
                return false;
            }
        });
        nearbyStoreAdapter.setShopOnClickListener(new NearbyStoreAdapter.ShopOnClickListener() {
            @Override
            public void onClick(int position, int shopId, double longitude, double latitude) {
                latLng = new LatLng(latitude, longitude);//初始化经纬度
                initMap(latLng);
                List<Marker> list = aMap.getMapScreenMarkers();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getPeriod() == shopId) {
                        if (aMap != null) {
                            if (!isFirstClick) {
                                clearMarkers();//清空第一个marker
                            }
                            list.get(i).setZIndex(10);
                            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);//将弹窗恢复为初始高度
                            Marker markerNew = changeMarkerImg(list.get(i));//改变当前选中的maker背景
                            resetMarker(markerNew.getPeriod());//重置除点击按钮外的所有按钮
                            markerLast = markerNew;// 记录当前marker对象，用于下次点击时判断
                            if (changeList(markerNew.getPeriod())) {//改变集合数据顺序,把用户点击的门店数据设置到0号位置
                                nearbyStoreAdapter.setData((ArrayList) dataList);
                                recyclerView.scrollToPosition(0);
                            }
                        }
                    } else {
                        list.get(i).setZIndex(0);
                        list.get(i).hideInfoWindow();
                    }
                }
            }
        });
    }

    //判断用户点击 改变集合中的数据顺序
    private Boolean changeList(int id) {
        if (id == lastId) {
            return false;
        }
        if (dataList != null && dataList.size() > 0) {
            for (int i = 0; i < dataList.size(); i++) {
                if (dataList.get(i).getShopId() == id) {
                    StoreListModel bean = dataList.get(i);
                    dataList.remove(i);
                    lastId = bean.getShopId();
                    dataList.add(0, bean);
                    return true;
                }
            }
        }
        return false;
    }

    private void initDataList() {
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).getIsSelected() == 1) {
                StoreListModel bean = dataList.get(i);
                dataList.remove(i);
                dataList.add(0, bean);
            }
        }
    }

    /**
     * func:批量添加marker到地图上
     */
    private void addMarkersToMap() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < markerList.size(); i++) {
                    if (!TextUtils.isEmpty(shopId)) {
                        if (shopId.equals(markerList.get(i).getShopId()+"")) {
                            addMarker(markerList.get(i), markerList.get(i).getShopId(), 1);
                        } else {
                            addMarker(markerList.get(i), markerList.get(i).getShopId(), 2);
                        }
                    } else {
                        if (i == 0) {
                            addMarker(markerList.get(i), markerList.get(i).getShopId(), 1);
                        } else {
                            addMarker(markerList.get(i), markerList.get(i).getShopId(), 2);
                        }

                    }


                }
            }
        });

    }


    private void addMarker(final StoreListModel bean, int id, int type) {
        //往map地图中添加marker
        double lat;
        double lon;
        lat = bean.getLatitude();
        lon = bean.getLongitude();
        LatLng latLng = new LatLng(lat, lon);

        String title = bean.getShopName();
        final MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.setFlat(true);
        //markerOptions.anchor(0.5f, 0.5f);
        markerOptions.title(title);
        markerOptions.position(latLng).period(id);
        if (type == 1) {//type=1表示选中  2表示未选中
            markerOptions.icon(customizeMarkerIcon(title, R.layout.location_marker_unselect_layout));
            markerFirst = aMap.addMarker(markerOptions);
            markerFirst.setZIndex(2);
            markerFirst.hideInfoWindow();
        } else if (type == 2) {
            markerOptions.icon(customizeMarkerIcon(title, R.layout.location_marker_unselect_layout));
            aMap.addMarker(markerOptions).hideInfoWindow();
        }
        if (markerFirst != null) {
            changeMarkerImg(markerFirst);
        }

    }

    private Marker changeMarkerImg(Marker marker) {
        //改变当前marker背景
        MarkerOptions options = marker.getOptions();
        options.getIcon().recycle();// 回收原bitmap
        for (int i = 0; i < markerList.size(); i++) {
            if (marker.getPeriod() == markerList.get(i).getShopId()) {
                marker.setIcon(customizeMarkerIcon(markerList.get(i).getShopName(), R.layout.location_marker_select_layout));
            }
        }
        return marker;
    }

    private void resetMarker(int curMarkerId) {
        //重置其他marker
        if (null != markerLast) {
            for (int i = 0; i < markerList.size(); i++) {
                if (curMarkerId != markerLast.getPeriod()) {
                    if (markerList.get(i).getShopId() == markerLast.getPeriod()) {
                        markerLast.setIcon(customizeMarkerIcon(markerList.get(i).getShopName(), R.layout.location_marker_unselect_layout));
                    }
//                    if (markerList.get(i).getShopId() == markerFirst.getPeriod()) {
//                        markerFirst.setIcon(customizeMarkerIcon(markerList.get(i).getShopName(), R.layout.location_marker_unselect_layout));
//                        markerFirst.setPeriod(-1);
//                    }
                }
            }
        }

    }

    //重置第一个Marker
    private void clearMarkers() {
        if (markerFirst != null) {
            //改变当前marker背景
            MarkerOptions options = markerFirst.getOptions();
            options.getIcon().recycle();// 回收原bitmap
            for (int i = 0; i < markerList.size(); i++) {
                if (markerFirst.getPeriod() == markerList.get(i).getShopId()) {
                    markerFirst.setIcon(customizeMarkerIcon(markerList.get(i).getShopName(), R.layout.location_marker_unselect_layout));
                    isFirstClick = true;
                }
            }
            //aMap.reloadMap();
        }
    }

    private BitmapDescriptor customizeMarkerIcon(String title, int id) {
        //返回markerIcon
        final View markerView = LayoutInflater.from(this).inflate(id, null);
        makerTxt = markerView.findViewById(R.id.maker_txt);
        makerTxt.setText(title);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    return;
                }
                top_addressTxt.setText(data.getStringExtra("province"));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void findViews() {
        super.findViews();
        mMapView = findViewById(R.id.map);
        initView();
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
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void outClick(@NotNull String function, @NotNull Object obj) {
        if ("phone".equals(function)) {
            showPhoneDialog(obj.toString());
        }
        if ("navigation".equals(function)) {
            showNavigationDialog(Integer.parseInt(obj.toString()));
        }
    }

    private void showNavigationDialog(final int position) {
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.nearby_store_navigation_dialog_layout, null);
        TextView baiduMapTxt = view.findViewById(R.id.baiduMapTxt);
        TextView gaodeMapTxt = view.findViewById(R.id.gaodeMapTxt);
        TextView cancelTxt = view.findViewById(R.id.cancelTxt);
        dialog.setContentView(view);
        //设置透明背景
        dialog.getWindow().findViewById(R.id.design_bottom_sheet)
                .setBackgroundResource(android.R.color.transparent);
        dialog.show();
        cancelTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        baiduMapTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkApkExist("com.baidu.BaiduMap")) {
                    isAgreeToMap(1, position, "百度地图");
                } else {
                    showToast("检测到您未安装百度地图~");
                }
            }
        });
        gaodeMapTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkApkExist("com.autonavi.minimap")) {
                    isAgreeToMap(2, position, "高德地图");
                } else {
                    showToast("检测到您未安装高德地图~");
                }
            }
        });
    }

    private void isAgreeToMap(final int type, final int position, String title) {
        boolean isAgree = false;
        StyledDialog.init(this);
        StyledDialog.buildIosAlert("提示", "\n憨妈妈想要打开" + title, new MyDialogListener() {
            @Override
            public void onFirst() {
            }

            @Override
            public void onThird() {
                super.onThird();
            }

            @Override
            public void onSecond() {
                if (type == 1) {
                    goToBaidu(String.valueOf(dataList.get(position).getLatitude()), String.valueOf(dataList.get(position).getLongitude()), dataList.get(position).getShopName());
                } else if (type == 2) {
                    goToGaode(String.valueOf(dataList.get(position).getLatitude()), String.valueOf(dataList.get(position).getLongitude()), dataList.get(position).getShopName());
                }
            }
        }).setBtnColor(R.color.dialogutil_text_black, R.color.colorPrimaryDark, 0).setBtnText("取消", "确定").show();
    }

    private void showPhoneDialog(final String phone) {
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.nearby_store_phone_dialog_layout, null);
        TextView phoneTxt = view.findViewById(R.id.phoneTxt);
        TextView cancelTxt = view.findViewById(R.id.cancelTxt);
        dialog.setContentView(view);
        //设置透明背景
        dialog.getWindow().findViewById(R.id.design_bottom_sheet)
                .setBackgroundResource(android.R.color.transparent);
        dialog.show();
        cancelTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        phoneTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtils.dial(mContext, phone);
            }
        });
    }

    /**
     * 根据包名判断应用是否安装
     * 高德地图包名：com.autonavi.minimap
     * 百度地图包名：com.baidu.BaiduMap
     *
     * @param packageName
     * @return
     */
    private boolean checkApkExist(String packageName) {
        if (packageName == null || "".equals(packageName)) {
            return false;
        }
        try {
            ApplicationInfo info = getPackageManager().getApplicationInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    //跳转百度地图
    private void goToBaidu(String lat, String lon, String address) {
        Intent intent = null;
        try {
            intent = Intent.getIntent("intent://map/direction?destination=latlng:" + lat + "," + lon + "|name:" +
                    address + "&mode=driving&src=andr.health.servicecenter#Intent;" + "scheme=bdapp;package=com.baidu.BaiduMap;end");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        startActivity(intent);
    }

    //跳转高德地图
    private void goToGaode(String lat, String lon, String address) {
        StringBuffer stringBuffer = new StringBuffer("androidamap://route?sourceApplication=").append("amap");
        stringBuffer.append("&dlat=").append(lat)
                .append("&dlon=").append(lon)
                .append("&dname=").append(address)
                .append("&dev=").append(0)
                .append("&t=").append(0);

        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(stringBuffer.toString()));
        intent.setPackage("com.autonavi.minimap");
        startActivity(intent);
    }


    private void initView() {
        behaviorTopIndicator2 = (ImageView) findViewById(R.id.behavior_top_indicator2);
    }
}