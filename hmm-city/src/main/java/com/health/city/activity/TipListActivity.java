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

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.health.city.R;
import com.health.city.contract.AddTipContract;
import com.health.city.fragment.FragmentTipList;
import com.healthy.library.model.Topic;
import com.health.city.presenter.AddTipPresenter;
import com.health.city.widget.TipDialog;
import com.healthy.library.adapter.FragmentStatePagerItemAdapter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.SpKey;
import com.healthy.library.interfaces.OnSubmitListener;
import com.healthy.library.routes.CityRoutes;
import com.healthy.library.utils.IntentUtils;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.utils.NavigateUtils;
import com.healthy.library.utils.PermissionUtils;
import com.healthy.library.business.MessageDialog;
import com.healthy.library.widget.TopBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 话题列表
 */
@Route(path = CityRoutes.CITY_TIPLIST)
public class TipListActivity extends BaseActivity implements AddTipContract.View, OnSubmitListener, TipDialog.OnTipDialogClickListener, AMapLocationListener {
    private com.healthy.library.widget.TopBar topBar;
    private android.widget.EditText serarchKeyWord;
    private com.flyco.tablayout.SlidingTabLayout st;
    private ViewPager vp;
    private TipDialog tipDialog;

    public AMapLocationClient mLocationClient;
    public AMapLocationClientOption mLocationOption = null;

    private List<Fragment> fragments = new ArrayList<>();
    FragmentTipList fragmentTipListleft;
    FragmentTipList fragmentTipListright;
    FragmentStatePagerItemAdapter fragmentPagerItemAdapter;
    int currentIndex = 0;

    private String cityNo;
    private String provinceNo;
    private String areaNo;
    private double longitude;
    private double latitude;
    private String locCityName;
    private String newCityName;
    private int RC_LOCATION = 11020;
    private int RC_PROVINCE_CITY = 14697;
    private int reLocTime = 0;
    private boolean isNoFollow=false;
    private AddTipPresenter addTipPresenter;
    private TextView noTip;


    @Override
    protected int getLayoutId() {
        return R.layout.city_activity_tip_list;
    }

    @Override
    protected void init(Bundle savedInstanceState) {


        areaNo= LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE);

        topBar.setSubmitListener(this);
        addTipPresenter=new AddTipPresenter(mContext,this);
        List<String> titles = Arrays.asList("本地", "全国");
        Map<String,Object> mapleft=new HashMap<>();
        mapleft.put("fragmentType","本地");
        fragmentTipListleft=FragmentTipList.newInstance(mapleft);
        Map<String,Object> mapright=new HashMap<>();
        mapright.put("fragmentType","全国");
        fragmentTipListright=FragmentTipList.newInstance(mapright);
        fragments.add(fragmentTipListleft);
        fragments.add(fragmentTipListright);

        if (fragmentPagerItemAdapter == null) {
            fragmentPagerItemAdapter = new FragmentStatePagerItemAdapter(getSupportFragmentManager(), fragments, titles);
            // adapter
            vp.setAdapter(fragmentPagerItemAdapter);
        } else {
            fragmentPagerItemAdapter.setDataSource(fragments, titles);
        }
        fragmentPagerItemAdapter.notifyDataSetChanged();
        vp.setOffscreenPageLimit(fragments.size());
        // bind to SmartTabLayout
        st.setViewPager(vp);
        vp.setCurrentItem(currentIndex);
        st.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                currentIndex = position;
//                if(position!=0){
//                    moreHot.setVisibility(View.VISIBLE);
//                    tvArea.setVisibility(View.GONE);
//                }else {
//                    moreHot.setVisibility(View.GONE);
//                    tvArea.setVisibility(View.VISIBLE);
//                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        serarchKeyWord.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId== EditorInfo.IME_ACTION_SEARCH){
                    fragmentTipListleft.putMap("topicName",serarchKeyWord.getText().toString());
                    fragmentTipListleft.onRefresh(null);
                    fragmentTipListright.putMap("topicName",serarchKeyWord.getText().toString());
                    fragmentTipListright.onRefresh(null);
                }
                return false;
            }
        });
//        locateCheck();
        noTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.putExtra("showTip",false);
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });

    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }


    private void initView() {
        topBar = (TopBar) findViewById(R.id.top_bar);
        serarchKeyWord = (EditText) findViewById(R.id.serarchKeyWord);
        st = (SlidingTabLayout) findViewById(R.id.st);
        vp = (ViewPager) findViewById(R.id.vp);
        noTip = (TextView) findViewById(R.id.noTip);
    }

    @Override
    public void onSubmitBtnPressed() {
//        ARouter.getInstance()
//                .build(CityRoutes.CITY_POSTSEND)
//                .navigation();
        tipDialog = new TipDialog(this, R.style.customdialogstyle);
        //System.out.println("进来");
        if(TextUtils.isEmpty(areaNo)){
            areaNo="0";
        }
        tipDialog.setAddressProvince((Integer.parseInt(areaNo) / 10000 * 10000) + "");
        tipDialog.setAddressCity( (Integer.parseInt(areaNo) / 100 * 100) + "");
        tipDialog.setAddressArea(areaNo + "");
        tipDialog.setOnTipDialogClickListener(this);
        tipDialog.show();


//        final List<String> strings = new ArrayList<>();
//        strings.add("版本更新");
//        strings.add("反馈");
//        StyledDialog.init(mContext);
//        Dialog menudialog = StyledDialog.buildBottomItemDialog(strings, "取消", new MyItemDialogListener() {
//            @Override
//            public void onItemClick(CharSequence text, int position) {
//            }
//
//            @Override
//            public void onBottomBtnClick() {
//
//            }
//        }).show();

    }

    @Override
    public void onClick(View view, Map<String, Object> map) {
        addTipPresenter.addTip(map);
    }

    @Override
    public void onSuccessAdd(Topic topic) {
        Intent intent=new Intent();
        intent.putExtra("id",topic.id);
        intent.putExtra("topicName",topic.topicName);
        setResult(Activity.RESULT_OK,intent);
        finish();


//        fragmentTipListleft.onRefresh(null);
//        fragmentTipListright.onRefresh(null);
    }

    private String[] mPermissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

    private void locateCheck() {
        locate();
        if (PermissionUtils.hasPermissions(mContext, mPermissions)) {
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(mPermissions, RC_LOCATION);
            }
        }
    }

    private void locate() {
//        showLoading();
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
            newCityName = aMapLocation.getCity();
            latitude = aMapLocation.getLatitude();
            longitude = aMapLocation.getLongitude();
            provinceNo=aMapLocation.getProvince();
            areaNo = aMapLocation.getAdCode();
            successLocation();
        } else {
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

    private void successLocation() {

    }

}
