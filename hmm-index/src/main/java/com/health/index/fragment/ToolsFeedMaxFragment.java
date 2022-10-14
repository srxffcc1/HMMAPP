package com.health.index.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.index.R;
import com.health.index.adapter.ToolsFeedTypeListAdapter;
import com.health.index.contract.ToolsDiaryContract;
import com.healthy.library.model.ToolsDiaryOut;
import com.healthy.library.model.ToolsMedType;
import com.healthy.library.model.ToolsSumType;
import com.health.index.model.UserInfoExModel;
import com.healthy.library.model.UserInfoMonModel;
import com.health.index.presenter.ToolsDiaryPresenter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.constant.Functions;
import com.healthy.library.business.FeedDateFragment;
import com.healthy.library.interfaces.OnDateConfirmListener;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.model.TabEntity;
import com.healthy.library.watcher.TextLimitTextWatcher;
import com.hyb.library.PreventKeyboardBlockUtil;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToolsFeedMaxFragment extends ToolsFeedBaseFragment implements ToolsDiaryContract.View {
    private RecyclerView typeRecycler;
    private CommonTabLayout tlColor;
    private RecyclerView typeRecycler2;
    private RadioButton checkYes;
    private RadioButton checkNo;
    private FrameLayout timeFragment;

    ToolsFeedTypeListAdapter toolsFeedTypeListAdapter;
    ToolsFeedTypeListAdapter toolsFeedTypeListAdapter2;
    private Date selectDate;
    ToolsDiaryPresenter toolsDiaryPresenter;

    private String[] mTitles = {"灰白", "墨绿", "褐色", "黑色", "黄色", "红色"};
    private int[] mIconUnselectIds = {
            R.drawable.tools_color_normal, R.drawable.tools_color_normal,
            R.drawable.tools_color_normal, R.drawable.tools_color_normal,
            R.drawable.tools_color_normal, R.drawable.tools_color_normal};
    private int[] mIconSelectIds = {
            R.drawable.tools_color_select, R.drawable.tools_color_select,
            R.drawable.tools_color_select, R.drawable.tools_color_select,
            R.drawable.tools_color_select, R.drawable.tools_color_select};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private EditText backUP;

    public static ToolsFeedMaxFragment newInstance(Map<String, Object> maporg) {
        ToolsFeedMaxFragment fragment = new ToolsFeedMaxFragment();
        Bundle args = new Bundle();
        BaseFragment.bundleMap(maporg, args);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View getBottomView() {
        return backUP;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.index_fragment_tools_diary_max;
    }

    @Override
    protected void findViews() {
        initView();
        buildTab();
        buildRecyclerView();
        toolsDiaryPresenter = new ToolsDiaryPresenter(getActivity(), this);
        if (get("recordId")!= null) {
            toolsDiaryPresenter.getNowDiary(new SimpleHashMapBuilder<String, Object>().puts(Functions.FUNCTION,"9027").puts("recordId",get("recordId").toString()).puts("recordType","6"));
        }else {
            Map<String, Object> maporg = new HashMap<>();
            maporg.put("beginTime", "2020/01/01");//必须是带0的
            maporg.put("endTime", new SimpleDateFormat("yyyy/MM/dd").format(new Date()));//必须是带0的
            maporg.put("format", "yyyy/MM/dd");
            if(get("init")!=null){
                maporg.put("init", get("init").toString());
            }else {
                maporg.put("init", new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date()));
            }
            try {
                if(get("init")!=null){
                    selectDate = new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(get("init").toString());
                }else {

                    selectDate = new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date()));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            getChildFragmentManager().beginTransaction().replace(R.id.timeFragment, FeedDateFragment.newInstance(maporg).setOnConfirmClick(new OnDateConfirmListener() {
                @Override
                public void onConfirm(int pos, Date date) {
                    selectDate = date;
                }
            })).commitAllowingStateLoss();
            backUP.addTextChangedListener(new TextLimitTextWatcher(backUP,100));
            backUP.setHint("写点什么吧");
        }
        PreventKeyboardBlockUtil.getInstance().setContext(mActivity).unRegister();
        PreventKeyboardBlockUtil.getInstance().setContext(mActivity).setBtnView(backUP).register();
    }

    private void buildRecyclerView() {
        toolsFeedTypeListAdapter = new ToolsFeedTypeListAdapter();
        typeRecycler.setLayoutManager(new GridLayoutManager(getContext(), 3));
        toolsFeedTypeListAdapter.setChoseCount(1);
        List<ToolsMedType> toolsMedTypes = new ArrayList<>();

        toolsMedTypes.add(new ToolsMedType("一般"));
        toolsMedTypes.add(new ToolsMedType("水样"));
        toolsMedTypes.add(new ToolsMedType("很稀"));
        toolsMedTypes.add(new ToolsMedType("粘稠"));
        toolsMedTypes.add(new ToolsMedType("较干"));
        toolsMedTypes.add(new ToolsMedType("干硬"));


        typeRecycler.setAdapter(toolsFeedTypeListAdapter);
        toolsFeedTypeListAdapter.setNewData(toolsMedTypes);


        toolsFeedTypeListAdapter2 = new ToolsFeedTypeListAdapter();
        typeRecycler2.setLayoutManager(new GridLayoutManager(getContext(), 3));
        toolsFeedTypeListAdapter2.setChoseCount(1);
        List<ToolsMedType> toolsMedTypes2 = new ArrayList<>();
        toolsMedTypes2.add(new ToolsMedType("少"));
        toolsMedTypes2.add(new ToolsMedType("中等"));
        toolsMedTypes2.add(new ToolsMedType("多"));
        typeRecycler2.setAdapter(toolsFeedTypeListAdapter2);
        toolsFeedTypeListAdapter2.setNewData(toolsMedTypes2);
    }

    private void buildTab() {
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        tlColor.setTabData(mTabEntities);

        tlColor.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                for (int i = 0; i < tlColor.getTabCount(); i++) {
                    if (i == 0) {
                        tlColor.getIconView(i).setBackgroundColor(Color.parseColor("#D7D7D7"));
                    }
                    if (i == 1) {
                        tlColor.getIconView(i).setBackgroundColor(Color.parseColor("#4B7902"));
                    }
                    if (i == 2) {
                        tlColor.getIconView(i).setBackgroundColor(Color.parseColor("#B8741A"));
                    }
                    if (i == 3) {
                        tlColor.getIconView(i).setBackgroundColor(Color.parseColor("#333333"));
                    }
                    if (i == 4) {
                        tlColor.getIconView(i).setBackgroundColor(Color.parseColor("#F59A23"));
                    }
                    if (i == 5) {
                        tlColor.getIconView(i).setBackgroundColor(Color.parseColor("#EC808D"));
                    }

                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        tlColor.setCurrentTab(2);
        for (int i = 0; i < tlColor.getTabCount(); i++) {
            if (i == 0) {
                tlColor.getIconView(i).setBackgroundColor(Color.parseColor("#D7D7D7"));
            }
            if (i == 1) {
                tlColor.getIconView(i).setBackgroundColor(Color.parseColor("#4B7902"));
            }
            if (i == 2) {
                tlColor.getIconView(i).setBackgroundColor(Color.parseColor("#B8741A"));
            }
            if (i == 3) {
                tlColor.getIconView(i).setBackgroundColor(Color.parseColor("#333333"));
            }
            if (i == 4) {
                tlColor.getIconView(i).setBackgroundColor(Color.parseColor("#F59A23"));
            }
            if (i == 5) {
                tlColor.getIconView(i).setBackgroundColor(Color.parseColor("#EC808D"));
            }

        }
    }

    private void initView() {
        typeRecycler = (RecyclerView) findViewById(R.id.typeRecycler);
        tlColor = (CommonTabLayout) findViewById(R.id.tl_color);
        typeRecycler2 = (RecyclerView) findViewById(R.id.typeRecycler2);
        checkYes = (RadioButton) findViewById(R.id.checkYes);
        checkNo = (RadioButton) findViewById(R.id.checkNo);
        timeFragment = (FrameLayout) findViewById(R.id.timeFragment);
        backUP = (EditText) findViewById(R.id.backUP);
    }

    @Override
    public void onSucessGetAllStatus(List<UserInfoExModel> userInfoExModels) {

    }




    private ToolsDiaryOut toolsDayNow;
    @Override
    public void onSucessGetNowDiary(String result) {
        try {
            toolsDayNow = (ToolsDiaryOut) resolveStatusData(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println("开始回显");
        if(toolsDayNow!=null&&isfragmenthow){
            backUP.setText(toolsDayNow.memo);
            Map<String, Object> maporg = new HashMap<>();
            maporg.put("beginTime", "2020/01/01");//必须是带0的
            maporg.put("endTime", new SimpleDateFormat("yyyy/MM/dd").format(new Date()));//必须是带0的
            maporg.put("format", "yyyy/MM/dd");
            try {
                maporg.put("init", new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(toolsDayNow.defecationTime)));
                selectDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(toolsDayNow.defecationTime);
            } catch (Exception e) {
                if(get("init")!=null){
                    maporg.put("init", get("init").toString());
                    try {
                        selectDate = new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(get("init").toString());

                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
            getChildFragmentManager().beginTransaction().replace(R.id.timeFragment, FeedDateFragment.newInstance(maporg).setOnConfirmClick(new OnDateConfirmListener() {
                @Override
                public void onConfirm(int pos, Date date) {
                    selectDate = date;
                }
            })).commitAllowingStateLoss();
            backUP.addTextChangedListener(new TextLimitTextWatcher(backUP,100));
            toolsFeedTypeListAdapter.setSelectS(toolsDayNow.defecationShape+",");
            toolsFeedTypeListAdapter.notifyDataSetChanged();
            toolsFeedTypeListAdapter2.setSelectS(toolsDayNow.defecationVolume+",");
            toolsFeedTypeListAdapter2.notifyDataSetChanged();
            for (int i = 0; i <mTitles.length ; i++) {
                if(mTitles[i].equals(toolsDayNow.defecationColor)){
                    tlColor.setCurrentTab(i);
                    break;
                }
            }
            if("0".equals(toolsDayNow.hasPee)){
                checkNo.setChecked(true);
            }else {
                checkYes.setChecked(true);
            }
        }
    }
    private ToolsSumType resolveStatusData(String obj) {
        ToolsSumType resultTmp = null;
        try {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Type type=null;
            Gson gson = builder.create();
            JSONObject jsonObject=new JSONObject(obj).getJSONObject("data");
            String userShopInfoDTOS=jsonObject.toString();


                type= new TypeToken<ToolsDiaryOut>() {
                }.getType();
                resultTmp=gson.fromJson(userShopInfoDTOS,type);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultTmp;
    }

    @Override
    public void onSucessUpdateDiary() {
        getActivity().finish();
    }

    @Override
    public void onsucessGetMine(UserInfoMonModel userInfoMonModel) {

    }

    @Override
    public void updateGrow() {
        if (checkIllegal()) {
            try {
                Map<String, Object> submap = new HashMap<>();
                submap.put("childId", get("childId").toString());
                if (get("recordId") != null) {
                    submap.put("recordId", get("recordId").toString());
                    submap.put(Functions.FUNCTION, "9029");
                } else {
                    submap.put(Functions.FUNCTION, "9028");
                }
                submap.put("recordType", "6");
                submap.put("feedingDateTime", (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(selectDate)) + "");
                submap.put("defecationColor", mTitles[tlColor.getCurrentTab()] + "");
                submap.put("defecationVolume", toolsFeedTypeListAdapter2.getSelectS() + "");
                submap.put("defecationShape", toolsFeedTypeListAdapter.getSelectS() + "");
                submap.put("hasPee", checkYes.isChecked()?"1":"0");
                submap.put("defecationTime", (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(selectDate)) + "");
                submap.put("memo", backUP.getText().toString() + "");
                toolsDiaryPresenter.updateDiary(submap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkIllegal() {
        if (TextUtils.isEmpty(toolsFeedTypeListAdapter.getSelectS())) {
            showToast("请选择便便性状");
            return false;
        }
        if (TextUtils.isEmpty(toolsFeedTypeListAdapter2.getSelectS())) {
            showToast("请选择大便量");
            return false;
        }
        return true;
    }
}
