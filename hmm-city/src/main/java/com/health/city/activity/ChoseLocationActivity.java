package com.health.city.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.health.city.R;
import com.health.city.adapter.LocationAdapter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.interfaces.OnCustomRetryListener;
import com.healthy.library.routes.CityRoutes;
import com.healthy.library.utils.IntentUtils;
import com.healthy.library.utils.NavigateUtils;
import com.healthy.library.utils.PermissionUtils;
import com.healthy.library.business.MessageDialog;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

@Route(path = CityRoutes.CITY_LOCATION)
public class ChoseLocationActivity extends BaseActivity implements AMapLocationListener , PoiSearch.OnPoiSearchListener, OnRefreshLoadMoreListener, LocationAdapter.OnLocationClickListener {

    private String areaNo;
    private String locCityName;
    private String areaName;
    private int RC_LOCATION = 111020;
    private int RC_PROVINCE_CITY = 114697;
    private int reLocTime = 0;

    public AMapLocationClient mLocationClient;
    public AMapLocationClientOption mLocationOption = null;
    private com.healthy.library.widget.TopBar topBar;
    private android.widget.EditText serarchKeyWord;
    private android.widget.TextView noShowLocation;
    private android.widget.TextView nowCity;
    private com.scwang.smart.refresh.layout.SmartRefreshLayout refreshLayout;
    private StatusLayout layoutStatus;
    private RecyclerView recycler;
    private PoiSearch poisearch;
    private LatLonPoint searchLatlonPoint;
    LocationAdapter locationAdapter;
    private String keyword="";

    @Autowired
    String areaNameSelect;
    @Override
    protected int getLayoutId() {
        return R.layout.city_activity_location;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        //System.out.println("选择得:"+areaNameSelect);
        locationAdapter=new LocationAdapter();
        locationAdapter.setSelectAreaName(areaNameSelect);
        recycler.setLayoutManager(new LinearLayoutManager(mContext));
        locationAdapter.bindToRecyclerView(recycler);
        locationAdapter.setOnLocationClickListener(this);
        refreshLayout.setOnRefreshLoadMoreListener(this);
        serarchKeyWord.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        serarchKeyWord.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i== EditorInfo.IME_ACTION_SEARCH){
                    keyword=serarchKeyWord.getText().toString();
                    locateCheck();
                }
                return false;
            }
        });
        noShowLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.putExtra("limitsStatus","1");
                intent.putExtra("postingAddress","");
                intent.putExtra("cityNo","");
                intent.putExtra("provinceNo", "");
                intent.putExtra("areaNo","");
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });
        nowCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent=new Intent();
                    if(TextUtils.isEmpty(areaNo)){
                        areaNo="0";
                    }
                    intent.putExtra("limitsStatus","1");
                    intent.putExtra("locCityName",locCityName);
                    intent.putExtra("postingAddress",locCityName+areaName);
                    intent.putExtra("cityNo",(Integer.parseInt(areaNo) / 100 * 100) + "");
                    intent.putExtra("provinceNo",(Integer.parseInt(areaNo) / 10000 * 10000) + "");
                    intent.putExtra("areaNo",areaNo + "");

                    setResult(Activity.RESULT_OK,intent);
                    finish();
                } catch (NumberFormatException e) {
                    Toast.makeText(mContext,"为获取到定位权限",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
        locateCheck();
    }
    private String[] mPermissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    private void locateCheck() {
        if (PermissionUtils.hasPermissions(mContext, mPermissions)) {
            locate();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(mPermissions, RC_LOCATION);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RC_LOCATION) {
            if (PermissionUtils.hasPermissions(mContext, mPermissions)) {
                locate();
            } else {
                if (PermissionUtils.somePermissionPermanentlyDenied(mActivity, mPermissions)) {
                    PermissionUtils.showRationale(mActivity);
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(mPermissions, RC_LOCATION);
                    }
                }
            }
        }
    }
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

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation == null) {
            return;
        }
        if (aMapLocation.getErrorCode() == 0) {
            locCityName = aMapLocation.getCity();
            areaName=aMapLocation.getDistrict();
            areaNo = aMapLocation.getAdCode();
            nowCity.setText(locCityName);
            searchLatlonPoint = new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude());
            successLocation();
        } else {
            layoutStatus.updateStatus(StatusLayout.Status.STATUS_CUSTOM);
            layoutStatus.setmOnCustomNetRetryListener(new OnCustomRetryListener() {
                @Override
                public void onRetryClick() {
                    mLocationClient.startLocation();
                }
            });
            if (NavigateUtils.openGPSSettings(mContext) && reLocTime <= 1) {
                mLocationClient.startLocation();
                reLocTime++;
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
    public int page=1;
    private void doSearchQuery(LatLonPoint centerpoint) {
        PoiSearch.Query query = new PoiSearch.Query(keyword, "");
        query.setPageSize(30);
        query.setPageNum(page);
        poisearch = new PoiSearch(this, query);
        poisearch.setOnPoiSearchListener(this);
        poisearch.setBound(new PoiSearch.SearchBound(centerpoint, 5000, true));
        poisearch.searchPOIAsyn();
    }
    private void successLocation() {
        showContent();
        //System.out.println("搜索"+keyword);
        doSearchQuery(searchLatlonPoint);
    }



    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.top_bar);
        serarchKeyWord = (EditText) findViewById(R.id.serarchKeyWord);
        noShowLocation = (TextView) findViewById(R.id.noShowLocation);
        nowCity = (TextView) findViewById(R.id.nowCity);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refresh_layout);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        recycler = (RecyclerView) findViewById(R.id.recycler);
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int resultCode) {
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
        List<PoiItem> poiItems = new ArrayList<>();
        if (resultCode == AMapException.CODE_AMAP_SUCCESS) {
            if (poiResult != null && poiResult.getPois().size() > 0) {
                poiItems.addAll(poiResult.getPois());
                //System.out.println("搜索"+keyword);
                //System.out.println("搜索"+page);
                if(page==0){

                    locationAdapter.setNewData(poiItems);
                }else {

                    locationAdapter.addData(poiItems);
                }
            } else {

                Toast.makeText(this, "未搜索到位置信息，请更换搜索关键字", Toast.LENGTH_SHORT).show();
                locationAdapter.setNewData(poiItems);
            }
        } else {
            locationAdapter.setNewData(poiItems);
            Toast.makeText(this, "未搜索到位置信息，请更换搜索关键字", Toast.LENGTH_SHORT).show();
        }
        if (resultCode != AMapException.CODE_AMAP_SUCCESS) {

            refreshLayout.finishLoadMoreWithNoMoreData();
        } else {
            refreshLayout.setNoMoreData(false);
            refreshLayout.setEnableLoadMore(true);
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        page++;
        locateCheck();
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        page=0;
        locateCheck();
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();

    }

    @Override
    public void onClick(View view, PoiItem item) {
        Intent intent=new Intent();
        if(TextUtils.isEmpty(areaNo)){
            areaNo="0";
        }
        intent.putExtra("limitsStatus","1");
        intent.putExtra("locCityName",locCityName);
        intent.putExtra("postingAddress",locCityName+areaName+"·"+item.getTitle());
        intent.putExtra("cityNo",(Integer.parseInt(areaNo) / 100 * 100) + "");
        intent.putExtra("provinceNo",(Integer.parseInt(areaNo) / 10000 * 10000) + "");
        intent.putExtra("areaNo",areaNo + "");

        setResult(Activity.RESULT_OK,intent);
        finish();
    }
}
