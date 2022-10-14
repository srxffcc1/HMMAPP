package com.health.mall.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
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
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.health.mall.R;
import com.health.mall.adapter.KeywordAdapter;
import com.health.mall.adapter.SurroundingAdapter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.SpKey;
import com.healthy.library.message.UpdateUserLocationMsg;
import com.healthy.library.model.AMapLocationBd;
import com.healthy.library.routes.MallRoutes;
import com.healthy.library.utils.InputMethodUtils;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.utils.PermissionUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.TopBar;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * @author Li
 * @date 2019/03/07 15:07
 * @des 从地图上选择地址
 */

@Route(path = MallRoutes.MALL_CHOOSE_MAP_ADDRESS)
public class ChooseMapAddressActivity extends BaseActivity implements TextWatcher,
        View.OnClickListener, AMap.OnCameraChangeListener, AMapLocationListener {

    private static final int RC_PERMISSION = 284;
    private TopBar mTopBar;
    private MapView mMapView;
    private AMap mAMap;
    private EditText mEtKeyword;
    private RecyclerView mRecyclerSurroundings;
    private TextView mTxtCancel;
    private SurroundingAdapter mSurroundingAdapter;
    private RecyclerView mRecyclerSearch;
    private KeywordAdapter mKeywordAdapter;
    private static final int CODE_SUCCESS = 1000;
    private TextView mTxtCity;
    private ImageView mImgClear;
    private ImageView mImgMapCenter;
    /**
     * 地图中心小图标跳动动画
     */
    private Animation mBeatAnimation;
    /**
     * 定位成功时获取到的经纬度
     */
    private LatLng mLocationLatLng;
    public AMapLocationClient mLocationClient;
    public AMapLocationClientOption mLocationOption = null;
    private String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_choose_map_address;
    }

    @Override
    protected void findViews() {
        mTopBar = findViewById(R.id.top_bar);
        mMapView = findViewById(R.id.map);
        mEtKeyword = findViewById(R.id.et_keyword);
        mRecyclerSurroundings = findViewById(R.id.recycler_surrounding);
        mRecyclerSearch = findViewById(R.id.recycler_search);
        mTxtCancel = findViewById(R.id.txt_cancel);
        mTxtCity = findViewById(R.id.txt_city);
        mImgMapCenter = findViewById(R.id.img_map_center);
        mImgClear = findViewById(R.id.img_clear);

    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setTopBar(mTopBar);
        mTxtCancel.setOnClickListener(this);
        mImgClear.setOnClickListener(this);
        mRecyclerSurroundings.setLayoutManager(new LinearLayoutManager(this));
        mSurroundingAdapter = new SurroundingAdapter();
        mSurroundingAdapter.bindToRecyclerView(mRecyclerSurroundings);
        mSurroundingAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                PoiItem poiItem=mSurroundingAdapter.getData().get(position);
                AMapLocationBd local=new AMapLocationBd(poiItem.getTitle(), poiItem.getAdName(), poiItem.getCityName(), poiItem.getProvinceName(), poiItem.getAdCode(), poiItem.getLatLonPoint().getLatitude(), poiItem.getLatLonPoint().getLongitude());
                String result = null;
                try {
                    result = new Gson().toJson(local);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                SpUtils.store(mContext, SpKey.LOC_TEST, result);
                LocUtil.storeLocation(mContext, local.build());
                LocUtil.storeLocationChose(mContext, local.build());
                EventBus.getDefault().post(new UpdateUserLocationMsg());

//                intent.putExtra("address", mSurroundingAdapter.getData().get(position)
//                        .getTitle());
//                intent.putExtra("areaNo", mSurroundingAdapter.getData().get(position)
//                        .getAdCode());
//                intent.putExtra("lng", mSurroundingAdapter.getData().get(position)
//                        .getLatLonPoint().getLongitude()+"");
//                intent.putExtra("lat", mSurroundingAdapter.getData().get(position)
//                        .getLatLonPoint().getLatitude()+"");
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        mBeatAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_map_location_beat);
        mRecyclerSearch.setLayoutManager(new LinearLayoutManager(this));
        mKeywordAdapter = new KeywordAdapter();
        mKeywordAdapter.bindToRecyclerView(mRecyclerSearch);
        mKeywordAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                Tip poiItem=mKeywordAdapter.getData().get(position);
                AMapLocationBd local=new AMapLocationBd(poiItem.getName(), poiItem.getDistrict(), poiItem.getDistrict(), poiItem.getDistrict(), poiItem.getAdcode(), poiItem.getPoint().getLatitude(), poiItem.getPoint().getLongitude());
                String result = null;
                try {
                    result = new Gson().toJson(local);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                SpUtils.store(mContext, SpKey.LOC_TEST, result);
                LocUtil.storeLocation(mContext, local.build());
                LocUtil.storeLocationChose(mContext, local.build());
                EventBus.getDefault().post(new UpdateUserLocationMsg());
//                intent.putExtra("address", mKeywordAdapter.getData().get(position)
//                        .getName());
//                intent.putExtra("areaNo", mKeywordAdapter.getData().get(position)
//                        .getAdcode());
//                intent.putExtra("lng", mKeywordAdapter.getData().get(position)
//                        .getPoint().getLongitude()+"");
//                intent.putExtra("lat", mKeywordAdapter.getData().get(position)
//                        .getPoint().getLatitude()+"");
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        mEtKeyword.addTextChangedListener(this);
        mEtKeyword.setText("");
        mMapView.onCreate(savedInstanceState);
        mAMap = mMapView.getMap();
        mAMap.getUiSettings().setZoomControlsEnabled(false);
        mAMap.setOnCameraChangeListener(this);
        if (!PermissionUtils.hasPermissions(this, permissions)) {
            ActivityCompat.requestPermissions(this, permissions, RC_PERMISSION);
        } else {
            locate();
        }
    }

    /**
     * 回到当前位置
     *
     * @param view view
     */
    public void resetLocation(View view) {
        if (mLocationLatLng != null) {
            mAMap.moveCamera(CameraUpdateFactory.newLatLng(mLocationLatLng));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RC_PERMISSION) {
            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_DENIED) {
                    return;
                }
            }
            locate();
        }
    }

    /**
     * 定位
     */
    private void locate() {
        mLocationClient = new AMapLocationClient(this);
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setOnceLocation(true);
        //设置定位监听
        mLocationClient.setLocationListener(this);
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.startLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.onDestroy();
        }
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    /**
     * 查看周边
     *
     * @param lat 维度
     * @param lng 经度
     */
    private void search(double lat, double lng) {
        PoiSearch.Query query = new PoiSearch.Query("", "", "");
        query.setPageSize(15);
        PoiSearch search = new PoiSearch(this, query);
        search.setBound(new PoiSearch.SearchBound(new LatLonPoint(lat, lng), 1000));
        search.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int i) {
                if (i == CODE_SUCCESS) {
                    mSurroundingAdapter.setNewData(poiResult.getPois());
                }
            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {
            }
        });
        search.searchPOIAsyn();
    }

    /**
     * 关键字搜索
     *
     * @param keyword 关键字
     */
    private void searchByKeyword(String keyword) {
        InputtipsQuery query = new InputtipsQuery(keyword, mTxtCity.getText().toString());
        query.setCityLimit(true);

        Inputtips inputtips = new Inputtips(this, query);
        inputtips.setInputtipsListener(new Inputtips.InputtipsListener() {
            @Override
            public void onGetInputtips(List<Tip> list, int i) {
                if (i == CODE_SUCCESS) {
                    mKeywordAdapter.setNewData(list);
                }

            }
        });
        inputtips.requestInputtipsAsyn();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mRecyclerSearch.setVisibility(s.length() == 0 ? View.GONE : View.VISIBLE);
        mTxtCancel.setVisibility(s.length() == 0 ? View.GONE : View.VISIBLE);
        mImgClear.setVisibility(s.length() == 0 ? View.GONE : View.VISIBLE);
        if (!TextUtils.isEmpty(s)) {
            searchByKeyword(String.valueOf(s));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onClick(View v) {
        mEtKeyword.setText("");
        InputMethodUtils.hideKeyboard(this, v);
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    /**
     * 移动地图结束
     *
     * @param cameraPosition 地图中心位置
     */
    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        showLoading();
        mImgMapCenter.startAnimation(mBeatAnimation);
        search(cameraPosition.target.latitude, cameraPosition.target.longitude);
    }

    /**
     * 定位回调
     *
     * @param aMapLocation 位置信息
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation == null) {
            return;
        }
        if (aMapLocation.getErrorCode() == 0) {
            double lat = aMapLocation.getLatitude();
            double lng = aMapLocation.getLongitude();
//            mTxtCity.setText(aMapLocation.getCity());
            LatLng latLng = new LatLng(lat, lng);
            mLocationLatLng = latLng;
            CameraPosition position = new CameraPosition(latLng, 17, 0, 0);
            mAMap.moveCamera(CameraUpdateFactory.newCameraPosition(position));
            //添加点标记
            MarkerOptions markerOptions = new MarkerOptions();

            BitmapDescriptor descriptor = BitmapDescriptorFactory.fromBitmap(
                    BitmapFactory.decodeResource(ChooseMapAddressActivity.this.getResources(),
                            R.drawable.mall_ic_map_marker
                    ));
            ConstraintLayout.LayoutParams params =
                    (ConstraintLayout.LayoutParams) mImgMapCenter.getLayoutParams();
            params.bottomMargin = (int) (descriptor.getHeight() * 2.0f);
            markerOptions.position(latLng)
                    .icon(descriptor);
            mAMap.addMarker(markerOptions);
        } else {
            mLocationClient.startLocation();
        }
    }
}