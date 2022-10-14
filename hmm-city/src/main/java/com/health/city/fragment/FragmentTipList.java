package com.health.city.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.health.city.R;
import com.health.city.adapter.TipAdapter;
import com.health.city.contract.TipListContract;
import com.healthy.library.model.Topic;
import com.health.city.presenter.TipListPresenter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.constant.SpKey;
import com.healthy.library.interfaces.OnCustomRetryListener;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.utils.IntentUtils;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.utils.NavigateUtils;
import com.healthy.library.utils.PermissionUtils;
import com.healthy.library.business.MessageDialog;
import com.healthy.library.widget.StatusLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class FragmentTipList extends BaseFragment implements TipListContract.View, OnRefreshLoadMoreListener, AMapLocationListener, TipAdapter.OnTipClickListener {

    private com.scwang.smart.refresh.layout.SmartRefreshLayout layoutRefresh;
    private com.healthy.library.widget.StatusLayout layoutStatus;
    private RecyclerView recycler;
    private TipAdapter tipAdapter;
    private int page=1;
    private TipListPresenter tipListPresenter;
    private String fragmentType="本地";//本地 全国



    private String cityNo;
    private String provinceNo;
    private String areaNo;
    private String longitude;
    private String latitude;
    private String locCityName;
    private String newCityName;
    private int RC_LOCATION = 11020;
    private int RC_PROVINCE_CITY = 14697;
    private int reLocTime = 0;
    private boolean isNoFollow=false;

    public AMapLocationClient mLocationClient;
    public AMapLocationClientOption mLocationOption = null;

    @Override
    protected int getLayoutId() {
        return R.layout.city_fragment_tip_board;
    }

    @Override
    protected void findViews() {
        initView();

    }
    public static FragmentTipList newInstance(Map<String, Object> maporg) {
        FragmentTipList fragment = new FragmentTipList();
        Bundle args = new Bundle();
        for (Map.Entry<String, Object> entry : maporg.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof Integer) {
                args.putInt(key, (Integer) value);
            } else if (value instanceof Boolean) {
                args.putBoolean(key, (Boolean) value);
            } else if (value instanceof String) {
                args.putString(key, (String) value);
            } else {
                args.putSerializable(key, (Serializable) value);
            }
        }
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected void onCreate() {
        super.onCreate();
        tipAdapter=new TipAdapter();
        recycler.setLayoutManager(new LinearLayoutManager(mContext));
        tipAdapter.bindToRecyclerView(recycler);
        layoutRefresh.setOnRefreshLoadMoreListener(this);
        layoutRefresh.setEnableLoadMore(false);
        fragmentType=get("fragmentType");
        tipAdapter.setTipType(fragmentType);
        tipAdapter.setOnTipClickListener(this);
        getBasemap().remove("fragmentType");
        tipListPresenter=new TipListPresenter(mContext,this);
        if("本地".equals(fragmentType)){
            locateCheck();
        }else {
//            basemap.put("addressProvince", (Integer.parseInt(areaNo) / 10000 * 10000) + "");
//            basemap.put("addressCity", (Integer.parseInt(areaNo) / 100 * 100) + "");
//            basemap.put("addressArea", areaNo + "");
            getBasemap().put("pageSize",20+"");
            getBasemap().put("limitsStatus",0+"");
            getBasemap().put("currentPage",page+"");
            tipListPresenter.getTipList(getBasemap());
        }
    }

    private void initView() {
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        recycler = (RecyclerView) findViewById(R.id.recycler);
    }

    @Override
    public void onSuccessGetTipList(List<Topic> topics, PageInfoEarly pageInfoEarly) {
        if (pageInfoEarly == null) {
            showEmpty();
            layoutRefresh.setEnableLoadMore(false);
            return;
        }
        page = pageInfoEarly.currentPage;
        if (page == 1) {
            tipAdapter.setNewData(topics);
            if (topics.size() == 0) {
                showEmpty();
            }
        } else {
            tipAdapter.addData(topics);
        }
        if (pageInfoEarly.isMore != 1) {

            layoutRefresh.finishLoadMoreWithNoMoreData();
        } else {
            layoutRefresh.setNoMoreData(false);
            layoutRefresh.setEnableLoadMore(true);
        }
    }


    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        page++;
        getBasemap().put("currentPage",page);
        tipListPresenter.getTipList(getBasemap());

    }

    public void putMap(String key,Object value){
        getBasemap().put(key,value);
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        page=1;
        getBasemap().put("currentPage",page);
        tipListPresenter.getTipList(getBasemap());
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        layoutRefresh.finishRefresh();
        layoutRefresh.finishLoadMore();
    }


    private String[] mPermissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

    private void locateCheck() {
        locate();
        if (PermissionUtils.hasPermissions(mContext, mPermissions)) {
        } else {
            requestPermissions(mPermissions, RC_LOCATION);
        }
    }

    private void locate() {
        if(LocUtil.getLocationBean(mContext, SpKey.LOC_CHOSE)!=null){
            successLocation();
        }else {
            mLocationClient = new AMapLocationClient(mContext);
            mLocationOption = new AMapLocationClientOption();
            mLocationOption.setOnceLocation(true);
            //设置定位监听
            mLocationClient.setLocationListener(this);
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocationClient.setLocationOption(mLocationOption);
            mLocationClient.startLocation();
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation == null) {
            return;
        }
        if (aMapLocation.getErrorCode() == 0) {
            LocUtil.storeLocation(mContext,aMapLocation);
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
                        .show(getChildFragmentManager(),"打开定位");

            }
        }
    }

    private void successLocation() {
        latitude = LocUtil.getLatitude(mContext,SpKey.LOC_CHOSE);
        longitude = LocUtil.getLongitude(mContext,SpKey.LOC_CHOSE);
        areaNo = LocUtil.getAreaNo(mContext,SpKey.LOC_CHOSE);
        cityNo = LocUtil.getCityNo(mContext,SpKey.LOC_CHOSE);
        locCityName = LocUtil.getCityNameOld(mContext,SpKey.LOC_ORG);
        newCityName = LocUtil.getCityNameOld(mContext,SpKey.LOC_CHOSE);
        if(TextUtils.isEmpty(areaNo)){
            areaNo="0";
        }
        getBasemap().put("pageSize",20+"");
        getBasemap().put("limitsStatus",1+"");
        getBasemap().put("currentPage",page+"");
        getBasemap().put("addressProvince", (Integer.parseInt(areaNo) / 10000 * 10000) + "");
        getBasemap().put("addressCity", (Integer.parseInt(areaNo) / 100 * 100) + "");
        getBasemap().put("addressArea", areaNo + "");
        tipListPresenter.getTipList(getBasemap());

    }

    @Override
    public void clickTip(View view, Topic topic) {
        Activity activity=getActivity();
        Intent intent=new Intent();
        intent.putExtra("id",topic.id);
        intent.putExtra("topicName",topic.topicName);
        activity.setResult(Activity.RESULT_OK,intent);
        activity.finish();
    }
}
