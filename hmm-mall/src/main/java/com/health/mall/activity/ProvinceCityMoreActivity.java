package com.health.mall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.health.mall.R;
import com.health.mall.adapter.ChoseWhereAdapter;
import com.health.mall.contract.ProvinceCityContract;
import com.health.mall.decoration.ProvinceDecoration;
import com.health.mall.presenter.ProvinceCityPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.SpKey;
import com.healthy.library.interfaces.OnCustomRetryListener;
import com.healthy.library.model.ProvinceCityModel;
import com.healthy.library.routes.MallRoutes;
import com.healthy.library.utils.IntentUtils;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.utils.NavigateUtils;
import com.healthy.library.business.MessageDialog;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;

import java.util.List;

/**
 * @author Li
 * @date 2019/03/04 14:41
 * @des 选择城市界面
 */

@Route(path = MallRoutes.MALL_PROVINCE_CITY_MORE)
public class ProvinceCityMoreActivity extends BaseActivity implements ProvinceCityContract.View,
        AMapLocationListener, ChoseWhereAdapter.ChoseItemClickListener {

    private TopBar mTopBar;
    private RecyclerView mRecyclerProvince;
    private ChoseWhereAdapter mProvinceAdapter;
    public AMapLocationClient mLocationClient;
    private static final int SPAN_COUNT = 2;
    public AMapLocationClientOption mLocationOption = null;
    private ProvinceCityPresenter mPresenter;
    private TextView mTvLocation;



    int nowType=0;//12

    ProvinceCityModel mProvModel;
    ProvinceCityModel mCityModel;
    ProvinceCityModel mStreetModel;

    private String nProv;
    private String mCity;
    private String mAstreet;



    private StatusLayout mStatusLayout;
    private TextView choseProvince;
    private TextView choseCity;
    private TextView choseStreet;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_province_city;
    }

    @Override
    protected void findViews() {
        mTopBar = findViewById(R.id.top_bar);
        mRecyclerProvince = findViewById(R.id.recycler_province);
        mTvLocation = findViewById(R.id.tv_location);
        mStatusLayout = findViewById(R.id.layout_status);
        initView();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setTopBar(mTopBar);
        setStatusLayout(mStatusLayout);
        mPresenter = new ProvinceCityPresenter(mContext, this);
        mRecyclerProvince.setLayoutManager(new LinearLayoutManager(mContext));


        mRecyclerProvince.addItemDecoration(new ProvinceDecoration());

        mProvinceAdapter = new ChoseWhereAdapter(mContext);

        mProvinceAdapter.setChoseItemClickListener(this);
        mTvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!"定位中".equals(mTvLocation.getText().toString())){
                    Intent intent = new Intent();
//                    AMapLocation aMapLocation= LocUtil.getLocationBean(mContext, SpKey.LOC_ORG);
//                    LocUtil.storeLocationChose(mContext,aMapLocation);
                    setResult(RESULT_OK, intent);
//                    EventBus.getDefault().post(new UpdateUserLocationMsg());
                    finish();
                }

            }
        });

        mRecyclerProvince.setAdapter(mProvinceAdapter);
        locate();
        choseProvince.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getProvinceList("0");
                mProvModel=null;
                mCityModel=null;
                mStreetModel=null;
                buildChose();
            }
        });
        choseCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mProvModel!=null){
                    mPresenter.getCityList(mProvModel.getAreaNo());
                }else {
                    Toast.makeText(mContext,"请先选择省",Toast.LENGTH_SHORT).show();
                }
                mCityModel=null;
                mStreetModel=null;
                buildChose();
            }
        });
        choseStreet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCityModel!=null){
                    mPresenter.getStreetList(mCityModel.getAreaNo());
                }else {
                    Toast.makeText(mContext,"请先选择区",Toast.LENGTH_SHORT).show();
                }
                mStreetModel=null;
                buildChose();
            }
        });

    }

    public void buildChose(){
        if(mProvModel==null){
            choseProvince.setText("选择省");
        }else {
            choseProvince.setText(mProvModel.getName());
        }
        if(mCityModel==null){
            choseCity.setText("选择市");
        }else {
            choseCity.setText(mCityModel.getName());
        }
        if(mStreetModel==null){

            choseStreet.setText("选择地区");
        }else {
            choseStreet.setText(mStreetModel.getName());
        }
    }

    @Override
    public void getData() {
        locate();
    }

    @Override
    public void onGetProvinceListSuccess(List<ProvinceCityModel> provinceList) {
        mProvinceAdapter.setData(provinceList);
        nowType=0;

        mRecyclerProvince.smoothScrollToPosition(0);

    }

    @Override
    public void onGetCityListSuccess(List<ProvinceCityModel> cityList) {
        mProvinceAdapter.setData(cityList);
        nowType=1;

        mRecyclerProvince.smoothScrollToPosition(0);
    }

    @Override
    public void onGetStreetListSuccess(List<ProvinceCityModel> streetList) {
        mProvinceAdapter.setData(streetList);
        nowType=2;

        mRecyclerProvince.smoothScrollToPosition(0);
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
        mTvLocation.setText(LocUtil.getCityNameOld(mContext,SpKey.LOC_ORG));
    }
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation == null) {
            return;
        }
        if (aMapLocation.getErrorCode() == 0) {
//            LocUtil.storeLocation(mContext,aMapLocation);
            successLocation();
            mPresenter.getProvinceList("0");
        } else {
            mStatusLayout.updateStatus(StatusLayout.Status.STATUS_CUSTOM);
            mStatusLayout.setmOnCustomNetRetryListener(new OnCustomRetryListener() {
                @Override
                public void onRetryClick() {
                    mLocationClient.startLocation();
                }
            });
            if(NavigateUtils.openGPSSettings(mContext)){
                mLocationClient.startLocation();
            }else{
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
//            mLocationClient.startLocation();
        }
    }

    public void reLocation(View v) {
        locate();
    }

    private void initView() {
        choseProvince = (TextView) findViewById(R.id.choseProvince);
        choseCity = (TextView) findViewById(R.id.choseCity);
        choseStreet = (TextView) findViewById(R.id.choseStreet);
    }

    @Override
    public void onChoseClick(ProvinceCityModel provinceCityModel) {
        if(nowType==0){
            mProvModel=provinceCityModel;
            mPresenter.getCityList(provinceCityModel.getAreaNo());
        }
        if(nowType==1){
            mCityModel=provinceCityModel;
            mPresenter.getStreetList(provinceCityModel.getAreaNo());
        }
        if(nowType==2){
            mStreetModel=provinceCityModel;

            Intent intent = new Intent();
//            AMapLocation aMapLocation= LocUtil.getLocationBean(mContext, SpKey.LOC_CHOSE);
//            aMapLocation.setAdCode((Integer.parseInt(mStreetModel.getAreaNo()))+"");//默认市级加5 得到模糊区县
//            aMapLocation.setProvince(mProvModel.getName());
//            aMapLocation.setCity(mCityModel.getName());
//            aMapLocation.setDistrict(mStreetModel.getName());
//            LocUtil.storeLocationChose(mContext,aMapLocation);
//            EventBus.getDefault().post(new UpdateUserLocationMsg());
            setResult(RESULT_OK, intent);
            finish();

        }
        buildChose();
    }
}
