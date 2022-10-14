package com.health.index.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.fondesa.recyclerviewdivider.RecyclerViewDivider;
import com.health.index.R;
import com.health.index.adapter.ToolsGrowAdapter;
import com.health.index.contract.ToolsGrowContract;
import com.health.index.fragment.ChartHWFragment;
import com.healthy.library.model.ToolsGrow;
import com.healthy.library.model.ToolsGrowBase;
import com.health.index.model.UserInfoExModel;
import com.healthy.library.model.UserInfoMonModel;
import com.health.index.presenter.ToolsGrowPresenter;
import com.health.index.widget.ChosePopupWindowOnlyBaby;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.Constants;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.dialog.DateDialog;
import com.healthy.library.dialog.LunarDateDialog;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.interfaces.IsNeedShare;
import com.healthy.library.interfaces.OnSubmitListener;
import com.healthy.library.routes.AppRoutes;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.routes.MineRoutes;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.utils.DateUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.ImageTextView;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;
import com.lihang.ShadowLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Route(path = IndexRoutes.INDEX_TOOLS_GROW)
public class ToolsGrowChartActivity extends BaseActivity implements IsNeedShare,IsFitsSystemWindows , ToolsGrowContract.View  {
    private StatusLayout layoutStatus;
    private ConstraintLayout toolsButtonLeft;
    private ConstraintLayout toolsButtonRight;
    private TextView textTitle;
    private DateDialog mBornDateDialog;
    private LunarDateDialog lunarDateDialog;
    private com.flyco.tablayout.SegmentTabLayout tab;
    private FrameLayout vp;
    private String[] titlesorg = {"身高", "体重"};
    private ArrayList<ChartHWFragment> fragments = new ArrayList<>();
    private android.widget.ImageView ivBannerHeadBg2;
    private com.healthy.library.widget.CornerImageView picBg;
    private com.healthy.library.widget.CornerImageView pic;
    private android.widget.LinearLayout tvAreall;
    private android.widget.LinearLayout tvArealll;
    private TextView selectClass;
    private android.widget.ImageView selectDown;
    private android.widget.LinearLayout chartMain;
    private com.lihang.ShadowLayout mShadowLayout;
    private android.widget.RelativeLayout growTitle;
    private TextView growAge;
    private ConstraintLayout growAgeUnd;
    private com.healthy.library.widget.ImageTextView growHeightTitle;
    private TextView growHeightValue;
    private TextView growHWSpace;
    private com.healthy.library.widget.ImageTextView growWeightTitle;
    private TextView growWeightValue;
    private TextView growAgeUndLine;
    private TextView recyclerTitle;
    private androidx.recyclerview.widget.RecyclerView recycler;
    private GridLayoutManager virtualLayoutManager;
    private ToolsGrowAdapter toolsGrowAdapter;
    ToolsGrowPresenter toolsGrowPresenter;
    public List<UserInfoExModel> onlyBabyuserInfoExModels=new ArrayList<>();
    public List<UserInfoExModel> orgBabyuserInfoExModels=new ArrayList<>();
    /**
     * 0为没有状态列表 1为有状态列表但是没婴儿 2正常状态 3没有曲线
     */
    int type=0;
    private String childSelectId;//选中的那个id
    private ConstraintLayout changeLL;
    private ConstraintLayout noContent;
    private ImageView noImg;
    private TextView noTxt;
    private ConstraintLayout hasContent;
    private RecyclerView recyclerGrow;
    private ImageTextView noButton;
    private ChosePopupWindowOnlyBaby ChosePopupWindowOnlyBaby;
    private int dialogwidth = 220;

    private boolean isBlack = false;
    private ImageTextView addGrow;
    private UserInfoExModel selectUserInfoExModel;
    private ConstraintLayout passUpGrow;
    private List<String> toolsGrowGraph=new ArrayList<>();
    private TextView growCurrentDate;
    private int nowsex;
    private TopBar topBar;

    @Override
    protected int getLayoutId() {
        return R.layout.index_activity_tools_chart;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);

        toolsGrowPresenter=new ToolsGrowPresenter(this,this);
//        buildFragment();
        buildRecyclerView();
        topBar.setSubmitListener(new OnSubmitListener() {
            @Override
            public void onSubmitBtnPressed() {
                if(TextUtils.isEmpty(childSelectId)){
                    Toast.makeText(mContext,"添加宝宝再分享哦~”",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(type==3){
                    Toast.makeText(mContext,"添加记录再分享哦~”",Toast.LENGTH_SHORT).show();
                    return;
                }
                    buildDes();
                    showShare();

            }
        });
        addGrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(IndexRoutes.INDEX_TOOLS_GROW_ADD)
                        .withString("childId",childSelectId)
                        .withString("hasaddTime",hasaddTime)
                        .withString("birthday",selectUserInfoExModel.deliveryDate)
                        .withString("age",growAge.getText().toString())
                        .navigation();
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type==-1){
                    ARouter.getInstance()
                            .build(MineRoutes.MINE_PERSONAL_INFO_DETAIL)
                            .withString("id", selectUserInfoExModel.id + "")
                            .navigation();
                }
                if(type==0){
                    if(orgBabyuserInfoExModels.size()>=5){//已经5个阶段了 不能再新增阶段了

                        Toast.makeText(mContext,"您已有5个阶段，无法再添加了！",Toast.LENGTH_SHORT).show();
                        ARouter.getInstance()
                                .build(MineRoutes.MINE_PERSONAL_INFO_LIST)
                                .navigation();
                    }else {
                        ARouter.getInstance()
                                .build(AppRoutes.APP_STATUS_PARENTING)
                                .withString("sex", nowsex==1?"男":"女")
                                .withString("isadd","1")
                                .withString(Constants.STATUS, Constants.STATUS_AFTER_PREGNANCY)
                                .navigation();
                    }


//                    ARouter.getInstance()
//                            .build(AppRoutes.APP_CHOOSE_STATUS)
//                            .withString("isadd","1")
//                            .withString("sex",nowsex==1?"男":"女")
//                            .navigation();
                }
                if(type==1){
                    if(orgBabyuserInfoExModels.size()>=5){//已经5个阶段了 不能再新增阶段了
                        Toast.makeText(mContext,"您已有5个阶段，无法再添加了！",Toast.LENGTH_SHORT).show();
                        ARouter.getInstance()
                                .build(MineRoutes.MINE_PERSONAL_INFO_LIST)
                                .navigation();

                }else {
                    ARouter.getInstance()
                            .build(AppRoutes.APP_STATUS_PARENTING)
                            .withString("sex", nowsex==1?"男":"女")
                            .withString("isadd","1")
                            .withString(Constants.STATUS, Constants.STATUS_AFTER_PREGNANCY)
                            .navigation();
                }

//                    ARouter.getInstance()
//                            .build(AppRoutes.APP_CHOOSE_STATUS)
//                            .withString("isadd","1")
//                            .withString("sex",nowsex==1?"男":"女")
//                            .navigation();
                }
                if(type==3){
                    ARouter.getInstance()
                            .build(IndexRoutes.INDEX_TOOLS_GROW_ADD)
                            .withString("childId",childSelectId)
                            .withString("hasaddTime",hasaddTime)
                            .withString("birthday",selectUserInfoExModel.deliveryDate)
                            .withString("age",growAge.getText().toString())
                            .navigation();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();

    }

    private void buildFragment(final String childId) {
        List<String> titles = Arrays.asList(titlesorg);
        if(fragments.size()==0){
            changeLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //System.out.println("测试进来");
                    int[] location = new int[2];
                    v.getLocationOnScreen(location);
                    if (ChosePopupWindowOnlyBaby != null) {
                        ChosePopupWindowOnlyBaby.setOnDismissListener(new PopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss() {
                                //System.out.println("宽度计算2:" + ChosePopupWindowOnlyBaby.getWidth());
                                selectDown.setImageResource(R.drawable.ic_triangle_down_green);
                                WindowManager.LayoutParams lp = getWindow().getAttributes();
                                lp.alpha = 1f;
                                getWindow().setAttributes(lp);
                            }
                        });
                        if (onlyBabyuserInfoExModels.size() > 0 ) {
                            //System.out.println("宽度计算:" + ChosePopupWindowOnlyBaby.getWidth() / 2 + ":" + changeLL.getWidth() / 2);
                            ChosePopupWindowOnlyBaby.showAsDropDown(v, -(int) (ChosePopupWindowOnlyBaby.getWidth() / 2 - tvAreall.getWidth() / 2), 0);
                            //System.out.println("弹出");
//                            ChosePopupWindowOnlyBaby.showAsDropDown(v);
                            selectDown.setImageResource(R.drawable.ic_triangle_up_green);
                            WindowManager.LayoutParams lp =getWindow().getAttributes();
                            lp.alpha = 0.4f;
                            getWindow().setAttributes(lp);
                        }

                    }
                }
            });
            tab.setTabData(titlesorg);
//            for (String title : titlesorg) {
//
//            }
            if(selectUserInfoExModel!=null){
                fragments.add(ChartHWFragment.newInstance(new SimpleHashMapBuilder<String, Object>().puts("birthday",selectUserInfoExModel.deliveryDate).puts("fargmentType","height").puts("childId",childSelectId)));
                fragments.add(ChartHWFragment.newInstance(new SimpleHashMapBuilder<String, Object>().puts("birthday",selectUserInfoExModel.deliveryDate).puts("fargmentType","weight").puts("childId",childSelectId)));
            }else {
                fragments.add(ChartHWFragment.newInstance(new SimpleHashMapBuilder<String, Object>().puts("fargmentType","height").puts("childId",childSelectId)));
                fragments.add(ChartHWFragment.newInstance(new SimpleHashMapBuilder<String, Object>().puts("fargmentType","weight").puts("childId",childSelectId)));
            }

            tab.setOnTabSelectListener(new OnTabSelectListener() {
                @Override
                public void onTabSelect(int position) {
//                vp.setCurrentItem(position);
                    if(position==0){
                        getSupportFragmentManager().beginTransaction().replace(R.id.vp ,ChartHWFragment.newInstance(new SimpleHashMapBuilder<String, Object>().puts("birthday",selectUserInfoExModel.deliveryDate).puts("fargmentType","height").puts("childId",childSelectId))).commitAllowingStateLoss();

                    }else {
                        getSupportFragmentManager().beginTransaction().replace(R.id.vp ,ChartHWFragment.newInstance(new SimpleHashMapBuilder<String, Object>().puts("birthday",selectUserInfoExModel.deliveryDate).puts("fargmentType","weight").puts("childId",childSelectId))).commitAllowingStateLoss();

                    }

                }

                @Override
                public void onTabReselect(int position) {

                }
            });
            getSupportFragmentManager().beginTransaction().replace(R.id.vp ,fragments.get(0)).commitAllowingStateLoss();
        }else {
//            for (int i = 0; i <fragments.size() ; i++) {
//                ChartHWFragment chartHWFragment= (ChartHWFragment) fragments.get(i);
//                chartHWFragment.setChildId(childId);
//            }
//            fragments.get(tab.getCurrentTab()).setChildId(childSelectId).setBirthday(selectUserInfoExModel.deliveryDate).refresh();
//            //System.out.println("替换测试");
            if (fragments.get(tab.getCurrentTab()).isAdded()) {
                fragments.get(tab.getCurrentTab()).setChildId(childSelectId).setBirthday(selectUserInfoExModel.deliveryDate).refresh();
            } else {
                getSupportFragmentManager().beginTransaction().remove(fragments.get(tab.getCurrentTab())).commitAllowingStateLoss();
                getSupportFragmentManager().beginTransaction().replace(R.id.vp ,fragments.get(tab.getCurrentTab()).setBirthday(selectUserInfoExModel.deliveryDate)).commitAllowingStateLoss();
            }

        }


    }

    private void buildRecyclerView() {
        virtualLayoutManager = new GridLayoutManager(mContext,3);
        recycler.setLayoutManager(virtualLayoutManager);

        toolsGrowAdapter=new ToolsGrowAdapter();
        recycler.setAdapter(toolsGrowAdapter);
        RecyclerViewDivider.with(mContext).hideLastDivider().color(Color.parseColor("#FFC276")).build().addTo(recycler);

    }

    @Override
    public void getData() {
        super.getData();
        type=0;
        toolsGrowPresenter.getAllStatus();
        toolsGrowPresenter.getNowStatus();
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    private void initView() {
        tab = (SegmentTabLayout) findViewById(R.id.tab);
        vp = (FrameLayout) findViewById(R.id.vp);
        ivBannerHeadBg2 = (ImageView) findViewById(R.id.iv_banner_head_bg2);
        picBg = (CornerImageView) findViewById(R.id.picBg);
        pic = (CornerImageView) findViewById(R.id.pic);
        tvAreall = (LinearLayout) findViewById(R.id.tv_areall);
        tvArealll = (LinearLayout) findViewById(R.id.tv_arealll);
        selectClass = (TextView) findViewById(R.id.selectClass);
        selectDown = (ImageView) findViewById(R.id.selectDown);
        chartMain = (LinearLayout) findViewById(R.id.chartMain);
        mShadowLayout = (ShadowLayout) findViewById(R.id.mShadowLayout);
        growTitle = (RelativeLayout) findViewById(R.id.growTitle);
        growAge = (TextView) findViewById(R.id.growAge);
        growAgeUnd = (ConstraintLayout) findViewById(R.id.growAgeUnd);
        growHeightTitle = (ImageTextView) findViewById(R.id.growHeightTitle);
        growHeightValue = (TextView) findViewById(R.id.growHeightValue);
        growHWSpace = (TextView) findViewById(R.id.growHWSpace);
        growWeightTitle = (ImageTextView) findViewById(R.id.growWeightTitle);
        growWeightValue = (TextView) findViewById(R.id.growWeightValue);
        growAgeUndLine = (TextView) findViewById(R.id.growAgeUndLine);
        recyclerTitle = (TextView) findViewById(R.id.recyclerTitle);
        recycler = (RecyclerView) findViewById(R.id.recyclerGrow);
        changeLL = (ConstraintLayout) findViewById(R.id.changeLL);
        noContent = (ConstraintLayout) findViewById(R.id.noContent);
        noImg = (ImageView) findViewById(R.id.noImg);
        noTxt = (TextView) findViewById(R.id.noTxt);
        hasContent = (ConstraintLayout) findViewById(R.id.hasContent);
        recyclerGrow = (RecyclerView) findViewById(R.id.recyclerGrow);
        noButton = (ImageTextView) findViewById(R.id.noButton);
        addGrow = (ImageTextView) findViewById(R.id.addGrow);
        passUpGrow = (ConstraintLayout) findViewById(R.id.passUpGrow);
        growCurrentDate = (TextView) findViewById(R.id.growCurrentDate);
        passUpGrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(childSelectId)&&!TextUtils.isEmpty(growAge.getText().toString())){
                    ARouter.getInstance()
                            .build(IndexRoutes.INDEX_TOOLS_GROW_ADD)
                            .withString("childId",childSelectId)
                            .withString("heightTmp",toolsGrowNow.height+"")
                            .withString("weightTmp",toolsGrowNow.weight+"")
                            .withString("age",growAge.getText().toString())
                            .withString("recordId",recordId)
                            .withString("hasaddTime",hasaddTime)
                            .withString("birthday",selectUserInfoExModel.deliveryDate)
                            .withString("chosedate",growCurrentDate.getText().toString())
                            .navigation();
                }

            }
        });
        topBar = (TopBar) findViewById(R.id.top_bar);
    }
    public boolean buildOnlyBaby(List<UserInfoExModel> userInfoExModels){
        onlyBabyuserInfoExModels.clear();
        for (int i = 0; i < userInfoExModels.size(); i++) {
            UserInfoExModel userInfoExModel=userInfoExModels.get(i);
            if(userInfoExModel.stageStatus>=3){
                onlyBabyuserInfoExModels.add(userInfoExModel);
            }
        }
        return true;
    }
    @Override
    public void onSucessGetAllStatus(List<UserInfoExModel> userInfoExModels) {
        orgBabyuserInfoExModels=userInfoExModels;
        if(userInfoExModels.size()>0){//说明是有状态的

            //增加判断是不是有宝宝 没有宝宝就叫他添加
            boolean hasbaby=false;
            boolean hasbabyCheck=false;
            if(selectUserInfoExModel!=null&&selectUserInfoExModel.stageStatus>=3){
                //System.out.println("已经选择了");
                childSelectId=selectUserInfoExModel.babyId+"";
                for (int i = 0; i <userInfoExModels.size() ; i++) {
                    if(selectUserInfoExModel.id==userInfoExModels.get(i).id){
                        selectUserInfoExModel=userInfoExModels.get(i);
                    }
                }
                hasbabyCheck=true;
                hasbaby=true;
            }else {
                childSelectId = "";
                for (int i = 0; i <userInfoExModels.size() ; i++) {
                    UserInfoExModel userInfoExModel=userInfoExModels.get(i);
                    if(userInfoExModel.stageStatus>=3){//说明有宝宝
                        hasbaby=true;
                    }
                    if(userInfoExModel.useStatus==1){//说明宝宝且被选中
//                        childSelectId=userInfoExModel.babyId+"";
//                        selectUserInfoExModel=userInfoExModel;
                        if(userInfoExModel.stageStatus>=3){//说明有宝宝
                            childSelectId=userInfoExModel.babyId+"";
                            selectUserInfoExModel=userInfoExModel;
                            hasbabyCheck=true;

                        }else {
//                            childSelectId=null;
//                            selectUserInfoExModel=userInfoExModel;
                        }
                    }

                }
            }

            if(hasbaby){
                type=2;
                if(buildOnlyBaby(userInfoExModels)&&selectUserInfoExModel==null){
                    childSelectId=onlyBabyuserInfoExModels.get(0).babyId+"";
                    selectUserInfoExModel=onlyBabyuserInfoExModels.get(0);
                    hasbabyCheck=true;
                }
                if(hasbabyCheck){//说明有宝宝也选中了
                    budildEmptyView(false);
                    //System.out.println("有宝宝并选择了");
                    buildGrowView(childSelectId);
                }else {//说明有宝宝没选中
//                    selectUserInfoExModel=onlyBabyuserInfoExModels.get(0);
//                    childSelectId=onlyBabyuserInfoExModels.get(0).babyId+"";// 没选中就默认第一个宝宝来看
                    type=-1;
                    budildEmptyView();
                    buildGrowView(null);
                }
            }else {
                type=1;
                budildEmptyView();
            }
        }else {//说明状态list都为空
            //叫他添加
            type=0;
            budildEmptyView();
        }
    }
    private void budildEmptyView() {
        budildEmptyView(true);
    }
    private void budildEmptyView(boolean flag) {
        if(flag){
            if(type==-1){
                changeLL.setVisibility(View.VISIBLE);
                noContent.setVisibility(View.VISIBLE);
                hasContent.setVisibility(View.GONE);
                noTxt.setText("宝宝出生了才可记录哦");
                noButton.setText("添加宝宝");
                //System.out.println("开始变化");
            }
            if(type==0){
                changeLL.setVisibility(View.GONE);
                noContent.setVisibility(View.VISIBLE);
                hasContent.setVisibility(View.GONE);
                noTxt.setText("您还未设置阶段信息");
                noButton.setText("添加阶段");
            }
            if(type==1){
                changeLL.setVisibility(View.GONE);
                noContent.setVisibility(View.VISIBLE);
                hasContent.setVisibility(View.GONE);
                noTxt.setText("不要心急，等宝宝出身了再来记录！");
                noButton.setText("添加宝宝");
            }
            if(type==2){
                changeLL.setVisibility(View.VISIBLE);
                noContent.setVisibility(View.GONE);
                hasContent.setVisibility(View.VISIBLE);
                try {
                    if (ChosePopupWindowOnlyBaby != null) {
                        ChosePopupWindowOnlyBaby.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //System.out.println("开始构建");

            }
        }

        ChosePopupWindowOnlyBaby = new ChosePopupWindowOnlyBaby(mContext, (int) TransformUtil.dp2px(mContext, dialogwidth), onlyBabyuserInfoExModels, new ChosePopupWindowOnlyBaby.OnStatusClickListener() {
            @Override
            public void onClick(UserInfoExModel bean, View view) {
                if (view.getId() == R.id.icon || view.getId() == R.id.stut || view.getId() == R.id.day) {
                    ChosePopupWindowOnlyBaby.dismiss();
                    childSelectId=bean.babyId+"";
                    selectUserInfoExModel=bean;
                    if(selectUserInfoExModel.stageStatus>=3){
                        type=2;
                        budildEmptyView();
                        buildGrowView(childSelectId);

                    }else {
                        type=-1;
                        budildEmptyView();
                        buildGrowView(null);
                    }
                } else {
                    ARouter.getInstance()
                            .build(MineRoutes.MINE_PERSONAL_INFO_DETAIL)
                            .withString("id", bean.id + "")
                            .navigation();
                    ChosePopupWindowOnlyBaby.dismiss();
                }


            }
        });
    }

    private void buildGrowView(String childSelectId) {
        //System.out.println("构建曲线");
        buildFragment(childSelectId);
        if(selectUserInfoExModel!=null){
            if(selectUserInfoExModel.stageStatus>=3){
                toolsGrowPresenter.getNowGrow(new SimpleHashMapBuilder<String, Object>().puts("childId",childSelectId));
                com.healthy.library.businessutil.GlideCopy.with(mContext)
                        .asBitmap()
                        .load(selectUserInfoExModel.babyfaceUrl)
                        .placeholder(selectUserInfoExModel.babySex == 1 ? R.drawable.app_status_boy : R.drawable.app_status_girl)
                        .error(selectUserInfoExModel.babySex == 1 ? R.drawable.app_status_boy : R.drawable.app_status_girl)
                        
                        .into(new BitmapImageViewTarget(pic) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                pic.setImageDrawable(circularBitmapDrawable);
                            }
                        });
                selectClass.setText(selectUserInfoExModel.babyName);
                //System.out.println("重新设置宝宝年龄和生日");
                growAge.setText(selectUserInfoExModel.stageStatusStr.replace("宝宝","").trim()+"");
            }else {
                //System.out.println("当前选中为没有婴儿");
                com.healthy.library.businessutil.GlideCopy.with(mContext)
                        .asBitmap()
                        .load(selectUserInfoExModel.faceUrl)
                        .placeholder(selectUserInfoExModel.memberSex == 1 ? R.drawable.img_avatar_default_man : R.drawable.img_avatar_default)
                        .error(selectUserInfoExModel.memberSex == 1 ? R.drawable.img_avatar_default_man : R.drawable.img_avatar_default)
                        
                        .into(new BitmapImageViewTarget(pic) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                pic.setImageDrawable(circularBitmapDrawable);
                            }
                        });
                String statuseName = "";
                int status = selectUserInfoExModel.stageStatus;
                if (status == 1) {
                    statuseName = "备孕中";

                } else {
                    statuseName = "怀孕中";

                }
                selectClass.setText(statuseName);
            }

        }
    }

//    @Override
//    public void onSucessGetNowStatus(UserInfoMonModel userInfoMonModel) {
//        if(userInfoMonModel.status>=3){//说明有宝宝了
//            type=3;
//            toolsGrowPresenter.getNowGrow();
//            buildFragment(userInfoMonModel.);
//        }else {
//            type=2;
//        }
//    }
    ToolsGrow toolsGrowNow=null;
    @Override
    public void onSucessGetNowGrow(List<ToolsGrow> toolsGrows) {
        toolsGrowGraph.clear();
        toolsGrowGraph.add("日期");
        toolsGrowGraph.add("身高(cm)");
        toolsGrowGraph.add("体重(kg)");
        //System.out.println("刷新宝宝页面数据");
        hasaddTime="";
        recyclerTitle.setVisibility(View.VISIBLE);
        recyclerGrow.setVisibility(View.VISIBLE);
        growAgeUnd.setVisibility(View.VISIBLE);
        if(toolsGrows.size()==0){
            changeLL.setVisibility(View.VISIBLE);
            noContent.setVisibility(View.VISIBLE);
            hasContent.setVisibility(View.GONE);
            growAgeUnd.setVisibility(View.GONE);
            recyclerTitle.setVisibility(View.GONE);
            recyclerGrow.setVisibility(View.GONE);
            type=3;
            noTxt.setText("宝宝暂无成长记录");
            noButton.setText("添加记录");
        }else {
            budildEmptyView();
        }
        for (int i = 0; i < toolsGrows.size(); i++) {
            toolsGrowGraph.add(toolsGrows.get(i).recordDate);
            toolsGrowGraph.add(toolsGrows.get(i).height+"");
            toolsGrowGraph.add(toolsGrows.get(i).weight+"");
            hasaddTime=hasaddTime+","+toolsGrows.get(i).recordDate;
            if(i==0){
                toolsGrowNow=toolsGrows.get(i);
                growHeightValue.setText(toolsGrows.get(i).height+"cm");
                growWeightValue.setText(toolsGrows.get(i).weight+"kg");
                recordId=toolsGrows.get(i).recordId+"";
                if(DateUtils.withToday(toolsGrows.get(i).recordDate,"yyyy-MM-dd")==0){//说明是今天

                    growCurrentDate.setText("今天");
                }else if(DateUtils.withToday(toolsGrows.get(i).recordDate,"yyyy-MM-dd")==-1){//说明是昨天
                    growCurrentDate.setText("昨天");
                }else {
                    growCurrentDate.setText(toolsGrows.get(i).recordDate);
                }

            }
        }
        toolsGrowAdapter.setNewData(toolsGrowGraph);
    }
    String hasaddTime="";
    String recordId="";

    @Override
    public void onSucessGetNowBaseGrow(List<ToolsGrowBase> toolsGrowBases) {

    }

    @Override
    public void onSucessAddGrow() {

    }

    @Override
    public void onsucessGetMine(UserInfoMonModel userInfoMonModel) {
        nowsex=userInfoMonModel.memberSex;
    }



    String surl;
    String sdes;
    String stitle;




    private void buildDes() {
        String urlPrefix = SpUtils.getValue(mContext, UrlKeys.H5_babyGrowthUrl);
        String url = String.format("%s?id=%s", urlPrefix,recordId);
        surl=url;

        stitle="宝宝成长发育报告";

        sdes="快来看看我家宝宝"+selectUserInfoExModel.babyName+"的成长发育报告吧！";

    }

    @Override
    public String getSurl() {
        return surl;
    }

    @Override
    public String getSdes() {
        return sdes;
    }

    @Override
    public String getStitle() {
        return stitle;
    }

    @Override
    public Bitmap getsBitmap() {
        return null;
    }
}
