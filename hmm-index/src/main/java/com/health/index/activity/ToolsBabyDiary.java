package com.health.index.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.index.R;
import com.health.index.adapter.ToolsDayTimeListMainAdapter;
import com.health.index.contract.ToolsDiaryMainContract;
import com.healthy.library.model.ToolsDiaryFood;
import com.healthy.library.model.ToolsDiaryMK;
import com.healthy.library.model.ToolsDiaryOut;
import com.healthy.library.model.ToolsDiarySleep;
import com.healthy.library.model.ToolsSumType;
import com.health.index.model.UserInfoExModel;
import com.healthy.library.model.UserInfoMonModel;
import com.health.index.presenter.ToolsDiarySleepPresenter;
import com.health.index.widget.ChosePopupWindowOnlyBaby;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.Constants;
import com.healthy.library.constant.Functions;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.interfaces.OnSubmitListener;
import com.healthy.library.routes.AppRoutes;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.routes.MineRoutes;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.ImageTextView;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Route(path = IndexRoutes.INDEX_TOOLS_BABY_DIARY)
public class ToolsBabyDiary extends BaseActivity implements IsFitsSystemWindows, OnRefreshLoadMoreListener, ToolsDiaryMainContract.View {
    private com.healthy.library.widget.TopBar topBar;
    private androidx.constraintlayout.widget.ConstraintLayout leftAdd;
    private androidx.constraintlayout.widget.ConstraintLayout centerAdd;
    private androidx.constraintlayout.widget.ConstraintLayout rightAdd;
    private android.widget.ImageView ivBannerHeadBg2;
    private com.healthy.library.widget.CornerImageView picBg;
    private com.healthy.library.widget.CornerImageView pic;
    private android.widget.LinearLayout tvAreall;
    private android.widget.LinearLayout tvArealll;
    private android.widget.TextView selectClass;
    private android.widget.ImageView selectDown;
    private android.widget.LinearLayout toolsChoseMenu;
    private android.widget.ImageView toolsMenu1Icon;
    private android.widget.TextView toolsMenu1Txt;
    private android.widget.ImageView toolsMenu1Add;
    private android.widget.ImageView toolsMenu2Icon;
    private android.widget.TextView toolsMenu2Txt;
    private android.widget.ImageView toolsMenu2Add;
    private android.widget.ImageView toolsMenu3Icon;
    private android.widget.TextView toolsMenu3Txt;
    private android.widget.ImageView toolsMenu3Add;
    private android.widget.TextView toolsDiaryNewTitleDiv;
    private android.widget.LinearLayout toolsDiaryNewTitle;
    private android.widget.TextView titlename;
    private com.healthy.library.widget.StatusLayout layoutStatus;
    private com.scwang.smart.refresh.layout.SmartRefreshLayout layoutRefresh;
    private androidx.recyclerview.widget.RecyclerView recyclerNews;
    ToolsDiarySleepPresenter toolsGrowPresenter;
    private ConstraintLayout noContent;
    private ImageView noImg;
    private TextView noTxt;
    private com.healthy.library.widget.ImageTextView noButton;
    private ConstraintLayout changeLL;
    private ConstraintLayout hasContent;
    private String childSelectId;//选中的那个id
    private UserInfoExModel selectUserInfoExModel;
    private ChosePopupWindowOnlyBaby ChosePopupWindowOnlyBaby;
    private int dialogwidth = 220;
    private int nowsex;
    private List<ToolsSumType> toolsDiaryBases = new ArrayList<>();
    ToolsDayTimeListMainAdapter toolsDayListAdapter;
    public List<UserInfoExModel> orgBabyuserInfoExModels = new ArrayList<>();
    boolean isSleep = true;
    /**
     * 0为没有状态列表 1为有状态列表但是没婴儿 2正常状态 3没有曲线
     */
    int type = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.index_activity_tools_diary_main;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        showEmpty();
        topBar.setSubmitListener(new OnSubmitListener() {
            @Override
            public void onSubmitBtnPressed() {
                if (TextUtils.isEmpty(childSelectId)) {
                    Toast.makeText(mContext, "添加宝宝再查看哦~”", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (type == 3) {
                    Toast.makeText(mContext, "添加记录再查看哦~”", Toast.LENGTH_SHORT).show();
                    return;
                }
                ARouter.getInstance()
                        .build(IndexRoutes.INDEX_TOOLS_BABY_DIARY_SUM_CHART)
                        .withString("childId", childSelectId)
                        .navigation();
//                ARouter.getInstance()
//                        .build(IndexRoutes.INDEX_TOOLS_BABY_DIARY_SUM)
//                        .withString("childId",childSelectId)
//                        .navigation();
            }
        });
        leftAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(IndexRoutes.INDEX_TOOLS_BABY_DIARY_FEED)
                        .withString("childId", childSelectId)
                        .navigation();
            }
        });
        centerAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(IndexRoutes.INDEX_TOOLS_BABY_DIARY_OUT)
                        .withString("childId", childSelectId)
                        .navigation();
            }
        });
        rightAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(IndexRoutes.INDEX_TOOLS_BABY_DIARY_SLEEP)
                        .withString("childId", childSelectId)
                        .navigation();
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == 0) {
                    if (orgBabyuserInfoExModels.size() >= 5) {//已经5个阶段了 不能再新增阶段了

                        Toast.makeText(mContext, "您已有5个阶段，无法再添加了！", Toast.LENGTH_SHORT).show();
                        ARouter.getInstance()
                                .build(MineRoutes.MINE_PERSONAL_INFO_LIST)
                                .navigation();
                    } else {
                        ARouter.getInstance()
                                .build(AppRoutes.APP_STATUS_PARENTING)
                                .withString("sex", nowsex == 1 ? "男" : "女")
                                .withString("isadd", "1")
                                .withString(Constants.STATUS, Constants.STATUS_AFTER_PREGNANCY)
                                .navigation();
                    }


//                    ARouter.getInstance()
//                            .build(AppRoutes.APP_CHOOSE_STATUS)
//                            .withString("isadd","1")
//                            .withString("sex",nowsex==1?"男":"女")
//                            .navigation();
                }
                if (type == 1) {
                    if (orgBabyuserInfoExModels.size() >= 5) {//已经5个阶段了 不能再新增阶段了
                        Toast.makeText(mContext, "您已有5个阶段，无法再添加了！", Toast.LENGTH_SHORT).show();
                        ARouter.getInstance()
                                .build(MineRoutes.MINE_PERSONAL_INFO_LIST)
                                .navigation();

                    } else {
                        ARouter.getInstance()
                                .build(AppRoutes.APP_STATUS_PARENTING)
                                .withString("sex", nowsex == 1 ? "男" : "女")
                                .withString("isadd", "1")
                                .withString(Constants.STATUS, Constants.STATUS_AFTER_PREGNANCY)
                                .navigation();
                    }
                }
                if (type == -1) {
                    ARouter.getInstance()
                            .build(MineRoutes.MINE_PERSONAL_INFO_DETAIL)
                            .withString("id", selectUserInfoExModel.id + "")
                            .navigation();
                }

            }
        });
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
                    if (onlyBabyuserInfoExModels.size() > 0) {
                        //System.out.println("宽度计算:" + ChosePopupWindowOnlyBaby.getWidth() / 2 + ":" + changeLL.getWidth() / 2);
                        ChosePopupWindowOnlyBaby.showAsDropDown(v, -(int) (ChosePopupWindowOnlyBaby.getWidth() / 2 - tvAreall.getWidth() / 2), 0);
                        //System.out.println("弹出");
//                            ChosePopupWindowOnlyBaby.showAsDropDown(v);
                        selectDown.setImageResource(R.drawable.ic_triangle_up_green);
                        WindowManager.LayoutParams lp = getWindow().getAttributes();
                        lp.alpha = 0.4f;
                        getWindow().setAttributes(lp);
                    }

                }
            }
        });
        layoutRefresh.setOnRefreshLoadMoreListener(this);
        toolsGrowPresenter = new ToolsDiarySleepPresenter(this, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public void getData() {
        super.getData();
        type = 0;
        toolsGrowPresenter.getAllStatus();
        toolsGrowPresenter.getNowStatus();
    }

    public List<UserInfoExModel> onlyBabyuserInfoExModels = new ArrayList<>();

    public boolean buildOnlyBaby(List<UserInfoExModel> userInfoExModels) {
        onlyBabyuserInfoExModels.clear();
        for (int i = 0; i < userInfoExModels.size(); i++) {
            UserInfoExModel userInfoExModel = userInfoExModels.get(i);
            if (userInfoExModel.stageStatus >= 3) {
                onlyBabyuserInfoExModels.add(userInfoExModel);
            }
//            onlyBabyuserInfoExModels.add(userInfoExModel);
        }
        return true;
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
        buildRecyclerView();
    }

    private void buildRecyclerView() {
        toolsDayListAdapter = new ToolsDayTimeListMainAdapter();
        recyclerNews.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerNews.setAdapter(toolsDayListAdapter);
    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.top_bar);
        leftAdd = (ConstraintLayout) findViewById(R.id.leftAdd);
        centerAdd = (ConstraintLayout) findViewById(R.id.centerAdd);
        rightAdd = (ConstraintLayout) findViewById(R.id.rightAdd);
        ivBannerHeadBg2 = (ImageView) findViewById(R.id.iv_banner_head_bg2);
        picBg = (CornerImageView) findViewById(R.id.picBg);
        pic = (CornerImageView) findViewById(R.id.pic);
        tvAreall = (LinearLayout) findViewById(R.id.tv_areall);
        tvArealll = (LinearLayout) findViewById(R.id.tv_arealll);
        selectClass = (TextView) findViewById(R.id.selectClass);
        selectDown = (ImageView) findViewById(R.id.selectDown);
        toolsChoseMenu = (LinearLayout) findViewById(R.id.tools_chose_menu);
        toolsMenu1Icon = (ImageView) findViewById(R.id.tools_menu1_icon);
        toolsMenu1Txt = (TextView) findViewById(R.id.tools_menu1_txt);
        toolsMenu1Add = (ImageView) findViewById(R.id.tools_menu1_add);
        toolsMenu2Icon = (ImageView) findViewById(R.id.tools_menu2_icon);
        toolsMenu2Txt = (TextView) findViewById(R.id.tools_menu2_txt);
        toolsMenu2Add = (ImageView) findViewById(R.id.tools_menu2_add);
        toolsMenu3Icon = (ImageView) findViewById(R.id.tools_menu3_icon);
        toolsMenu3Txt = (TextView) findViewById(R.id.tools_menu3_txt);
        toolsMenu3Add = (ImageView) findViewById(R.id.tools_menu3_add);
        toolsDiaryNewTitleDiv = (TextView) findViewById(R.id.tools_diary_new_title_div);
        toolsDiaryNewTitle = (LinearLayout) findViewById(R.id.tools_diary_new_title);
        titlename = (TextView) findViewById(R.id.titlename);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        recyclerNews = (RecyclerView) findViewById(R.id.recycler_news);
        noContent = (ConstraintLayout) findViewById(R.id.noContent);
        noImg = (ImageView) findViewById(R.id.noImg);
        noTxt = (TextView) findViewById(R.id.noTxt);
        noButton = (ImageTextView) findViewById(R.id.noButton);
        changeLL = (ConstraintLayout) findViewById(R.id.changeLL);
        hasContent = (ConstraintLayout) findViewById(R.id.hasContent);
        layoutRefresh.setEnableLoadMore(false);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        onRequestFinish();

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        if (selectUserInfoExModel.stageStatus >= 3) {
            type = 2;
            budildEmptyView();
            buildGrowView(childSelectId);

        } else {
            type = -1;
            //System.out.println("没有孩子");
            budildEmptyView();
            buildGrowView(null);
        }
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        layoutRefresh.finishRefresh();
        layoutRefresh.finishLoadMore();
    }

    @Override
    public void onSucessGetAllStatus(List<UserInfoExModel> userInfoExModels) {
        orgBabyuserInfoExModels = userInfoExModels;
        if (userInfoExModels.size() > 0) {//说明是有状态的
            //增加判断是不是有宝宝 没有宝宝就叫他添加
            boolean hasbaby = false;
            boolean hasbabyCheck = false;
            childSelectId = "";
            if (selectUserInfoExModel != null && selectUserInfoExModel.stageStatus >= 3) {
                childSelectId = selectUserInfoExModel.babyId + "";
                for (int i = 0; i < userInfoExModels.size(); i++) {
                    if (selectUserInfoExModel.id == userInfoExModels.get(i).id) {
                        selectUserInfoExModel = userInfoExModels.get(i);
                    }
                }
                hasbaby = true;
                hasbabyCheck = true;
            } else {
                //System.out.println("初步开始检索宝宝");
                for (int i = 0; i < userInfoExModels.size(); i++) {
                    UserInfoExModel userInfoExModel = userInfoExModels.get(i);
                    if (userInfoExModel.stageStatus >= 3) {//说明有宝宝
                        hasbaby = true;
                    }
                    if (userInfoExModel.useStatus == 1) {//说明宝宝且被选中
//                        childSelectId=userInfoExModel.babyId+"";
//                        selectUserInfoExModel=userInfoExModel;
                        if (userInfoExModel.stageStatus >= 3) {//说明有宝宝
                            childSelectId = userInfoExModel.babyId + "";
                            selectUserInfoExModel = userInfoExModel;
                            hasbabyCheck = true;

                        } else {
//                            childSelectId=null;
//                            selectUserInfoExModel=userInfoExModel;
                        }
                    }

                }
            }
            if (hasbaby) {
                type = 2;
                ;//重新构建宝宝选择器

                //System.out.println("初步开始检索宝宝2");
                if (buildOnlyBaby(userInfoExModels) && selectUserInfoExModel == null) {
                    //System.out.println("初步开始检索宝宝3");
                    childSelectId = onlyBabyuserInfoExModels.get(0).babyId + "";
                    selectUserInfoExModel = onlyBabyuserInfoExModels.get(0);
                    hasbabyCheck = true;
                }

                if (hasbabyCheck) {//说明有宝宝也选中了
                    budildEmptyView(false);
                    //System.out.println("有宝宝并选择了");
                    buildGrowView(childSelectId);
                } else {//说明有宝宝没选中
//                    selectUserInfoExModel=onlyBabyuserInfoExModels.get(0);
//                    childSelectId=onlyBabyuserInfoExModels.get(0).babyId+"";// 没选中就默认第一个宝宝来看
                    type = -1;
                    budildEmptyView();
                    buildGrowView(null);
                }
            } else {
                type = 1;
                budildEmptyView();
            }
        } else {//说明状态list都为空
            //叫他添加
            type = 0;
            budildEmptyView();
        }
    }

    private void buildGrowView(String childSelectId) {
        //System.out.println("构建曲线");
//        budildEmptyView();
        if (selectUserInfoExModel != null) {
            if (selectUserInfoExModel.stageStatus >= 3) {
                toolsDayListAdapter.setChildId(childSelectId);
                toolsGrowPresenter.getNowDiary(new SimpleHashMapBuilder<String, Object>().puts(Functions.FUNCTION, "9025").puts("childId", childSelectId));
                toolsGrowPresenter.getNowSleep(new SimpleHashMapBuilder<String, Object>().puts(Functions.FUNCTION, "9035").puts("childId", childSelectId));
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
            } else {
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

    private void budildEmptyView() {
        budildEmptyView(true);
    }

    private void budildEmptyView(boolean flag) {
        if (flag) {
            if (type == -1) {
                changeLL.setVisibility(View.VISIBLE);
                noContent.setVisibility(View.VISIBLE);
                hasContent.setVisibility(View.GONE);
                noTxt.setText("宝宝出生了才可记录哦");
                noButton.setText("添加宝宝");
                //System.out.println("开始变化");
            }
            if (type == 0) {
                changeLL.setVisibility(View.GONE);
                noContent.setVisibility(View.VISIBLE);
                hasContent.setVisibility(View.GONE);
                noTxt.setText("您还未设置阶段信息");
                noButton.setText("添加阶段");
            }
            if (type == 1) {
                changeLL.setVisibility(View.GONE);
                noContent.setVisibility(View.VISIBLE);
                hasContent.setVisibility(View.GONE);
                noTxt.setText("不要心急，等宝宝出身了再来记录！");
                noButton.setText("添加宝宝");
            }
            if (type == 2) {
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
                    childSelectId = bean.babyId + "";
                    selectUserInfoExModel = bean;
                    if (selectUserInfoExModel.stageStatus >= 3) {
                        type = 2;
                        budildEmptyView();
                        buildGrowView(childSelectId);

                    } else {
                        type = -1;
                        //System.out.println("没有孩子");
                        budildEmptyView();
                        buildGrowView(null);
                    }
//                        buildFragment(childSelectId);
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


    @Override
    public void onSucessGetNowDiary(String result) {
        toolsDiaryBases.clear();
        try {
            JSONArray jsonArray = new JSONObject(result).getJSONArray("data");
            if (jsonArray.length() > 0) {
                toolsDiaryBases = resolveStatusListData(result);
                toolsDayListAdapter.setNewData(toolsDiaryBases);

                toolsDiaryNewTitle.setVisibility(View.VISIBLE);
            } else {
                toolsDiaryNewTitle.setVisibility(View.GONE);
                //System.out.println("设置空数据");
                type = 3;
                showEmpty();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onSucessGetNowSleepDiary(String result) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        isSleep = jsonObject.optBoolean("data");
        buildNiView();
    }

    private void buildNiView() {
        if (isSleep) {
            toolsMenu3Icon.setImageResource(R.drawable.tools_xl);
            toolsMenu3Txt.setText("醒来");
        } else {
            toolsMenu3Icon.setImageResource(R.drawable.tools_sj);
            toolsMenu3Txt.setText("睡觉");
        }
    }

    private List<ToolsSumType> resolveStatusListData(String obj) {
        List<ToolsSumType> result = new ArrayList<>();
        try {
            JSONArray data = new JSONObject(obj).getJSONArray("data");

            for (int i = 0; i < data.length(); i++) {
                GsonBuilder builder = new GsonBuilder();
                builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        return new Date(json.getAsJsonPrimitive().getAsLong());
                    }
                });
                Type type = null;
                Gson gson = builder.create();
                JSONObject jsonObject = data.getJSONObject(i);
                String userShopInfoDTOS = jsonObject.toString();
                if ("1".equals(jsonObject.optString("recordType")) || "2".equals(jsonObject.optString("recordType"))) {
                    type = new TypeToken<ToolsDiaryMK>() {
                    }.getType();
                    ToolsSumType resultTmp = gson.fromJson(userShopInfoDTOS, type);
                    result.add(resultTmp);
                }
                if ("3".equals(jsonObject.optString("recordType")) || "4".equals(jsonObject.optString("recordType"))) {
                    type = new TypeToken<ToolsDiaryFood>() {
                    }.getType();
                    ToolsSumType resultTmp = gson.fromJson(userShopInfoDTOS, type);
                    result.add(resultTmp);
                }
                if ("5".equals(jsonObject.optString("recordType")) || "6".equals(jsonObject.optString("recordType"))) {
                    type = new TypeToken<ToolsDiaryOut>() {
                    }.getType();
                    ToolsSumType resultTmp = gson.fromJson(userShopInfoDTOS, type);
                    result.add(resultTmp);
                }
                if ("7".equals(jsonObject.optString("recordType"))) {
                    type = new TypeToken<ToolsDiarySleep>() {
                    }.getType();
                    ToolsSumType resultTmp = gson.fromJson(userShopInfoDTOS, type);
                    result.add(resultTmp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public void onSucessUpdateDiary() {

    }

    @Override
    public void onsucessGetMine(UserInfoMonModel userInfoMonModel) {
        nowsex = userInfoMonModel.memberSex;
    }
}
