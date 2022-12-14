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
    AMap aMap = null;//??????????????????????????????
    private int currentPage = 1;// ??????????????????1????????????
    private LatLonPoint lp;

    private PoiResult poiResult; // ?????????poi???????????????
    private PoiSearch.Query poiQuery;// ?????????Poi???????????????
    private PoiSearch poiSearch;//?????????poi??????
    private List<PoiItem> poiItemList;// ?????????poi??????

    private String keyWord = "";
    private TextView mSearchText;
    private BottomSheetBehavior behavior;
    private RecyclerView behavior_recycle;
    private SearchAddressAdapter searchAddressListAdapter;
    private TextView city_title;
    //??????mlocationClient??????
    private AMapLocationClient mLocationClient;
    //??????mLocationOption??????
    private AMapLocationClientOption mLocationOption = null;

    private Double latitude;//??????
    private Double longitude;//??????

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
        mMapView.onCreate(savedInstanceState);// ??????????????????????????????????????????????????????????????????????????????????????????
        locate();

    }

    /**
     * ????????????poi??????
     */
    protected void doSearchQuery() {
        //keyWord = mSearchText.getText().toString().trim();
        currentPage = 1;
        poiQuery = new PoiSearch.Query("", "???????????????|????????????", "");// ????????????????????????????????????????????????????????????poi????????????????????????????????????poi??????????????????????????????????????????
        poiQuery.setPageSize(50);// ?????????????????????????????????poiitem
        poiQuery.setPageNum(currentPage);// ??????????????????
        poiQuery.setCityLimit(true);

        if (lp != null) {
            poiSearch = new PoiSearch(this, poiQuery);
            poiSearch.setOnPoiSearchListener(this);
            poiSearch.setBound(new PoiSearch.SearchBound(lp, 5000, true));//
            // ????????????????????????lp????????????????????????5000?????????
            poiSearch.searchPOIAsyn();// ????????????
        }
    }

    private void initMap(int type) {

        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        if (type == 0) {
            if (getIntent().getSerializableExtra("model") != null) {
                AddressListModel addressListModel = (AddressListModel) getIntent().getSerializableExtra("model");
                latLng = new LatLng(Double.parseDouble(addressListModel.getLat()), Double.parseDouble(addressListModel.getLng()));//??????????????????????????????
                lp = new LatLonPoint(Double.parseDouble(addressListModel.getLat()), Double.parseDouble(addressListModel.getLng()));
                mSearchText.setHint(addressListModel.getAddress() + "");
                city_title.setText(addressListModel.getCityName() + "");
            } else {
                latLng = new LatLng(latitude, longitude);//??????????????????
                lp = new LatLonPoint(latitude, longitude);
            }
        } else {
            latLng = new LatLng(latitude, longitude);//??????????????????
            lp = new LatLonPoint(latitude, longitude);
        }
//        aMap.setMyLocationStyle(myLocationStyle);//?????????????????????Style
//        aMap.getUiSettings().setMyLocationButtonEnabled(true);//?????????????????????????????????????????????????????????
//        aMap.setMyLocationEnabled(true);// ?????????true?????????????????????????????????false??????????????????????????????????????????????????????false???
        aMap.showIndoorMap(true);//??????????????????
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));//????????????????????????
        aMap.getUiSettings().setScrollGesturesEnabled(true);//??????????????????
        aMap.getUiSettings().setZoomGesturesEnabled(true);//??????????????????
        aMap.getUiSettings().setRotateGesturesEnabled(false);//??????????????????
        aMap.getUiSettings().setTiltGesturesEnabled(false);//??????????????????

        addMarker(latLng);
        aMap.setOnMarkerDragListener(this);
        aMap.setOnMapClickListener(this);
        aMap.setOnMarkerClickListener(this);
        // ?????? Marker ??????????????????
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
//        behavior.setPeekHeight(1030);//???????????????????????????
//        behavior.setHideable(false);//??????????????????
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
            mSearchText.setHint("???????????????????????????");
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
            if (result != null && result.getQuery() != null) {// ??????poi?????????
                if (result.getQuery().equals(poiQuery)) {// ??????????????????
                    poiResult = result;
                    poiItemList = poiResult.getPois();// ??????????????????poiitem????????????????????????0??????
                    if (poiItemList != null && poiItemList.size() > 0) {
                        //??????POI????????????
                        setAdapter();
                    } else {
                        showToast("???????????????????????????????????????");
                    }
                }
            } else {
                showToast("???????????????????????????????????????");
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
        //???map???????????????marker

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.setFlat(true);
        //markerOptions.anchor(0.5f, 0.5f);
        //markerOptions.title(title);
        markerOptions.draggable(true);//??????Marker?????????
        markerOptions.position(latLng);
        markerOptions.icon(customizeMarkerIcon(null, R.layout.item_address_search_map_marker_layout));
        marker = aMap.addMarker(markerOptions);

    }

    private BitmapDescriptor customizeMarkerIcon(String title, int id) {
        //??????markerIcon
        final View markerView = LayoutInflater.from(this).inflate(id, null);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(convertViewToBitmap(markerView));
        return bitmapDescriptor;

    }

    private Bitmap convertViewToBitmap(View view) {
        //view ???bitmap
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
            latitude = aMapLocation.getLatitude();//????????????
            longitude = aMapLocation.getLongitude();//????????????
            successLocation();
            initMap(0);
            doSearchQuery();
        } else {
            if (NavigateUtils.openGPSSettings(mContext)) {
                mLocationClient.startLocation();
            } else {
                MessageDialog.newInstance()
                        .setMessageTopRes(R.drawable.dialog_message_icon_loc,"????????????","????????????????????????????????????????????????????????????????????????GPS??????")
                        .setMessageOkClickListener(new MessageDialog.MessageOkClickListener() {
                            @Override
                            public void onMessageOkClick(View view) {
                                IntentUtils.toLocationSetting(mContext);
                            }
                        })
                        .show(getSupportFragmentManager(),"????????????");

            }
        }
    }

    /**
     * ??????
     */
    private void locate() {
        showLoading();
        mLocationClient = new AMapLocationClient(mContext);
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setOnceLocation(true);
        //??????????????????
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