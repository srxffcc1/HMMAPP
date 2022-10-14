package com.health.tencentlive.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.health.tencentlive.R;
import com.health.tencentlive.fragment.LiveMainBodyFragment;
import com.healthy.library.adapter.DropDownType;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.SpKey;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.interfaces.IsNeedVideo;
import com.healthy.library.routes.TencentLiveRoutes;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.business.GridDropDownPop;
import com.healthy.library.business.GridDropDownPopNoBack;
import com.healthy.library.widget.ImageTextView;
import com.healthy.library.widget.TopBar;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

@Route(path = TencentLiveRoutes.LiveMainBody)
public class LiveMainBodyActivity extends BaseActivity implements IsFitsSystemWindows , View.OnClickListener , IsNeedVideo {
    private com.healthy.library.widget.TopBar topBar;
    private android.widget.LinearLayout tabLL;
    private android.widget.LinearLayout llArea;
    private com.healthy.library.widget.ImageTextView tvArea;
    private android.widget.LinearLayout llStrength;
    private com.healthy.library.widget.ImageTextView tvStrength;
    private android.widget.FrameLayout fragmentParent;
    GridDropDownPop typeDropDownPop;
    GridDropDownPopNoBack locationDropDownPop;
    String areaString="排序";
    String strengthString="观看方式";

    String areaCode="";
    String strengthCode="";
    @Autowired
    String category;
    @Autowired
    String categoryName;
    private LiveMainBodyFragment liveMainBodyFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_live_main_body;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        topBar.setTitle(categoryName);
        liveMainBodyFragment = LiveMainBodyFragment.newInstance(new SimpleHashMapBuilder<String, Object>()
                .puts("statusList","1,2,4")
                .puts("category",category)
        );
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentParent , liveMainBodyFragment).commitAllowingStateLoss();
        llArea.setOnClickListener(this);
        llStrength.setOnClickListener(this);
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.top_bar);
        tabLL = (LinearLayout) findViewById(R.id.tabLL);
        llArea = (LinearLayout) findViewById(R.id.ll_area);
        tvArea = (ImageTextView) findViewById(R.id.tv_area);
        llStrength = (LinearLayout) findViewById(R.id.ll_strength);
        tvStrength = (ImageTextView) findViewById(R.id.tv_strength);
        fragmentParent = (FrameLayout) findViewById(R.id.fragmentParent);
    }


    public void setAreaArrow(String name) {
        if("推荐".equals(name)){
            tvArea.setTextColor(Color.parseColor("#333333"));
            tvArea.setText("排序");
        }else {
            tvArea.setTextColor(Color.parseColor("#FF5353"));
            tvArea.setText(name);
        }
    }
    public void setStrengthArrow(String name) {
        if("全部".equals(name)){
            tvStrength.setTextColor(Color.parseColor("#333333"));
            tvStrength.setText("观看方式");
        }else {
            tvStrength.setTextColor(Color.parseColor("#FF5353"));
            tvStrength.setText(name);
        }
    }
    public void setAreaArrowR(boolean flag) {
        tvArea.setDrawable(flag ? R.drawable.ic_solid_triangle_down_red : R.drawable.ic_solid_triangle_up_red, mContext);
    }
    public void setAreaArrow(boolean flag) {
        tvArea.setDrawable(flag ? R.drawable.ic_solid_triangle_down_gray : R.drawable.ic_solid_triangle_up_gray, mContext);
    }
    public void setStrengthArrowR(boolean flag) {
        tvStrength.setDrawable(flag ? R.drawable.ic_solid_triangle_down_red : R.drawable.ic_solid_triangle_up_red, mContext);
    }
    public void setStrengthArrow(boolean flag) {
        tvStrength.setDrawable(flag ? R.drawable.ic_solid_triangle_down_gray : R.drawable.ic_solid_triangle_up_gray, mContext);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ll_area) {
            if(typeDropDownPop ==null){
                typeDropDownPop =new GridDropDownPop(this, new GridDropDownPop.ItemClickCallBack() {
                    @Override
                    public void click(String id, String name) {
                        tvArea.setText(name);
                        areaString=name;
                        areaCode= "".equals(id)?null:id;
                        onRefresh(null);
                        setAreaArrow(name);
                        if("推荐".equals(name)){
                            setAreaArrow(false);
                        }else {
                            setAreaArrowR(false);
                        }
                    }

                    @Override
                    public void dismiss() {
                        if("推荐".equals(areaString)||"排序".equals(areaString)){
                            setAreaArrow(true);
                        }else {
                            setAreaArrowR(true);
                        }
                    }
                });
                List<DropDownType> droplist=new ArrayList<>();
                droplist.add(new DropDownType("","推荐"));
                droplist.add(new DropDownType("1","观看最多"));
                typeDropDownPop.setData(droplist);
                typeDropDownPop.setSelectId("");

            }else {

            }
            if(!isFinishing()){
                if("排序".equals(areaString)||"推荐".equals(areaString)){
                    setAreaArrow(false);
                }else {
                    setAreaArrowR(false);
                }
                typeDropDownPop.showPopupWindow(view);
            }
            if(locationDropDownPop!=null){

                locationDropDownPop.dismiss();
            }
        }
        if (view.getId() == R.id.ll_strength) {
            if(locationDropDownPop ==null){
                locationDropDownPop =new GridDropDownPopNoBack(this, new GridDropDownPopNoBack.ItemClickCallBack() {
                    @Override
                    public void click(String id, String name) {
                        tvStrength.setText(name);
                        strengthString=name;
                        strengthCode= "".equals(id)?null:id;
                        onRefresh(null);
                        setStrengthArrow(name);
                        if("全部".equals(name)){
                            setStrengthArrow(false);
                        }else {
                            setStrengthArrowR(false);
                        }

                    }

                    @Override
                    public void dismiss() {
                        if("全部".equals(strengthString)||"观看方式".equals(strengthString)){
                            setStrengthArrow(true);
                        }else {
                            setStrengthArrowR(true);
                        }
                    }
                });
                List<DropDownType> droplist=new ArrayList<>();
                droplist.add(new DropDownType("","全部"));
                droplist.add(new DropDownType("1","免费"));
                droplist.add(new DropDownType("2","私密"));
//                droplist.add(new DropDownType("3","收费"));
                locationDropDownPop.setData(droplist);
                locationDropDownPop.setSelectId("");

            }else {

            }
            if(!isFinishing()){
                if("观看方式".equals(strengthString)||"全部".equals(strengthString)){
                    setStrengthArrow(false);
                }else {
                    setStrengthArrowR(false);
                }
                locationDropDownPop.showPopupWindow(view);
            }

            if(typeDropDownPop!=null){
                typeDropDownPop.dismiss();
            }
        }
    }
    public void onRefresh(RefreshLayout refreshLayout) {
        liveMainBodyFragment.getBasemap().put("sortType",areaCode);
        liveMainBodyFragment.getBasemap().put("type",strengthCode);
        liveMainBodyFragment.onRefresh(null);
    }

    @Override
    public String getToken() {
        return SpUtils.getValue(mContext, SpKey.LIVETMPCOURSEADDRESS);
    }

    @Override
    public String getCourseId() {
        return SpUtils.getValue(mContext,SpKey.LIVETMPCOURSEID);
    }

    @Override
    public String getLiveStatus() {
        return "2";
    }

    @Override
    public Handler getVideoHandler() {
        return null;
    }
}
