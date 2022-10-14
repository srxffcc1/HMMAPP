package com.healthy.library.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.healthy.library.BuildConfig;
import com.healthy.library.R;
import com.healthy.library.adapter.LocListAdapter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.business.MessageDialog;
import com.healthy.library.businessutil.ChannelUtil;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.constant.SpKey;
import com.healthy.library.contract.LocVipContract;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.interfaces.OnCustomRetryListener;
import com.healthy.library.message.UpdateUserLocationMsg;
import com.healthy.library.model.LocVip;
import com.healthy.library.presenter.LocVipPresenter;
import com.healthy.library.routes.LibraryRoutes;
import com.healthy.library.routes.MallRoutes;
import com.healthy.library.utils.IntentUtils;
import com.healthy.library.utils.NavigateUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.ImageTextView;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;
import com.hss01248.dialog.StyledDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

@Route(path = LibraryRoutes.LIBRARY_LOC)
public class LocChoseActivity extends BaseActivity implements IsFitsSystemWindows, AMapLocationListener, LocVipContract.View,TextView.OnEditorActionListener {
    private com.healthy.library.widget.TopBar topBar;
    private com.healthy.library.widget.StatusLayout layoutStatus;
    private android.widget.TextView txtLocationPre;
    private com.healthy.library.widget.ImageTextView tvLocation;
    private com.healthy.library.widget.ImageTextView tvRelocation;
    private android.view.View dividerLoc;
    private androidx.recyclerview.widget.RecyclerView recyclerProvince;
    public AMapLocationClient mLocationClient;
    public AMapLocationClientOption mLocationOption = null;
    LocVipPresenter locVipPresenter;
    LocListAdapter locListAdapter;
    private androidx.constraintlayout.widget.ConstraintLayout locLL;
    private Dialog loading;
    private ConstraintLayout seachLLZ;
    private android.widget.LinearLayout seachLL;
    private android.widget.EditText serarchKeyWord;
    private android.widget.ImageView clearEdit;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_loc_change;
    }
    public void reLocation(View v) {
        locate();
    }
    @Override
    protected void init(Bundle savedInstanceState) {
        StyledDialog.init(this);
        loading = StyledDialog.buildMdLoading().setForceWidthPercent(0.3f).show();
        locVipPresenter=new LocVipPresenter(this,this);
        recyclerProvince.setLayoutManager(new LinearLayoutManager(mContext));
        locListAdapter=new LocListAdapter();
        locListAdapter.bindToRecyclerView(recyclerProvince);
        locListAdapter.setLocChangeListener(new LocListAdapter.LocChangeListener() {
            @Override
            public void onLocClick(LocVip.Local local) {
                AMapLocation aMapLocation= LocUtil.getLocationBean(mContext, SpKey.LOC_CHOSE);
                aMapLocation.setAdCode(local.getDistrict());//默认市级加5 得到模糊区县
                aMapLocation.setProvince(local.provinceName);
                aMapLocation.setCity(local.getCityName());
                aMapLocation.setDistrict(local.getDistrictName());
                LocUtil.storeLocationChose(mContext,aMapLocation);
                LocUtil.setNowShop(local.getNearShop());
                EventBus.getDefault().post(new UpdateUserLocationMsg());
                finish();
            }
        });
        getData();
        tvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AMapLocation aMapLocation= LocUtil.getLocationBean(mContext, SpKey.LOC_ORG);
                if(mlocVip!=null){
                    LocUtil.setNowShop(mlocVip.local.getNearShop());
                }
                LocUtil.storeLocationChose(mContext,aMapLocation);
                EventBus.getDefault().post(new UpdateUserLocationMsg());
                finish();
            }
        });
    }

    @Override
    public void getData() {
        super.getData();
        locVipPresenter.getLocVip(new SimpleHashMapBuilder<String, Object>());
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
    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }
    private void successLocation() {
//        tvLocation.setText(LocUtil.getCityNameOld(mContext, SpKey.LOC_ORG));
        getData();
    }
    private void initView() {
        topBar = (TopBar) findViewById(R.id.top_bar);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        txtLocationPre = (TextView) findViewById(R.id.txt_location_pre);
        tvLocation = (ImageTextView) findViewById(R.id.tv_location);
        tvRelocation = (ImageTextView) findViewById(R.id.tv_relocation);
        dividerLoc = (View) findViewById(R.id.divider_loc);
        recyclerProvince = (RecyclerView) findViewById(R.id.recycler_province);
        locLL = (ConstraintLayout) findViewById(R.id.locLL);
        seachLLZ = (ConstraintLayout) findViewById(R.id.seachLLZ);
        seachLL = (LinearLayout) findViewById(R.id.seachLL);
        serarchKeyWord = (EditText) findViewById(R.id.serarchKeyWord);
        clearEdit = (ImageView) findViewById(R.id.clearEdit);
        if (SpUtils.getValue(this, SpKey.OPERATIONSTATUS, false)) {//运营模式
            seachLLZ.setVisibility(View.VISIBLE);
        }
        serarchKeyWord.setOnEditorActionListener(this);
        clearEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serarchKeyWord.setText("");
                filterLoc(serarchKeyWord.getText().toString());
            }
        });
        serarchKeyWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    clearEdit.setVisibility(View.VISIBLE);
                    if (s.toString().length() > 1) {
                        filterLoc(serarchKeyWord.getText().toString());
                    }
                } else {
                    clearEdit.setVisibility(View.GONE);
                    filterLoc(serarchKeyWord.getText().toString());
                }
            }
        });
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation == null) {
            return;
        }
        if (aMapLocation.getErrorCode() == 0) {
            if(BuildConfig.VERSION_CODE==2309){
//                aMapLocation=new AMapLocationBd("青岛北站", "李沧区", "青岛市", "山东省", "370213", 36.175661, 120.380807).build();
//                aMapLocation=new AMapLocationBd("宁波地", "市辖区", "宁波市", "浙江省", "330201", 29.8642, 121.543299).build();
            }

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
        }
    }

    List<LocVip.Local> localList=new ArrayList<>();
    List<LocVip.Local> localShopList=new ArrayList<>();
    LocVip mlocVip;
    @Override
    public void onSucessGetLocVip(LocVip locVip) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                StyledDialog.dismiss(loading);
            }
        },500);
        mlocVip=locVip;
        localList.clear();
        localShopList.clear();
        if(locVip!=null){
            localList.addAll(locVip.getAllMerchantWithMe());
            filterLoc(serarchKeyWord.getText().toString());
        }else{
            if(("check".equals(ChannelUtil.getChannel(null))|| "debug".equals(ChannelUtil.getChannel(null)))){//测试环境2309 版本则跳转老的选择地址
                Toast.makeText(mContext,"检测到测试环境且接口未部署",Toast.LENGTH_LONG).show();
                ARouter.getInstance().build(MallRoutes.MALL_PROVINCE_CITY_ORG)
                        .navigation();
                finish();
            }
        }

    }

    public void filterLoc(String key) {
        if (TextUtils.isEmpty(key)) {
            key = null;
        }
        localShopList.clear();
        for (int i = 0; i <localList.size() ; i++) {
            if(localList.get(i).getMerchantsMap()!=null&&localList.get(i).check(key)){
                localShopList.add(localList.get(i));
            }
        }
        for (int i = 0; i < localShopList.size(); i++) {
            LocVip.Local item =localShopList.get(i);
            System.out.println("白名单-"+"城市:"+item.getCityName()+ "·合伙人:" +item.partnerName);
        }
        locListAdapter.setNewData(localShopList);
    }

    private void buildLocView() {
        localShopList.clear();
        for (int i = 0; i <localList.size() ; i++) {
            if(localList.get(i).getMerchantsMap()!=null){
                localShopList.add(localList.get(i));
            }
        }
        for (int i = 0; i < localShopList.size(); i++) {
            LocVip.Local item =localShopList.get(i);
            System.out.println("白名单-"+"城市:"+item.getCityName()+ "·合伙人:" +item.partnerName);
        }
        locListAdapter.setNewData(localShopList);
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        showContent();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//            if (TextUtils.isEmpty(serarchKeyWord.getText().toString())) {
//                return false;
//            }
            hideSoftKeyBoard(v);
            filterLoc(serarchKeyWord.getText().toString());
        }
        return false;
    }
}
