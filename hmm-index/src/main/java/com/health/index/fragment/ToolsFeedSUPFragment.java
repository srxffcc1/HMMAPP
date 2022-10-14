package com.health.index.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.healthy.library.model.ToolsDiaryFood;
import com.healthy.library.model.ToolsMedType;
import com.healthy.library.model.ToolsSumType;
import com.health.index.model.UserInfoExModel;
import com.healthy.library.model.UserInfoMonModel;
import com.health.index.presenter.ToolsDiaryPresenter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.business.FeedDateFragment;
import com.healthy.library.interfaces.OnDateConfirmListener;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.watcher.TextLimitTextWatcher;
import com.healthy.library.utils.SpUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToolsFeedSUPFragment extends ToolsFeedBaseFragment implements ToolsDiaryContract.View {
    private RecyclerView typeRecycler;
    private TextView timeTitle;
    private FrameLayout timeFragment;
    ToolsFeedTypeListAdapter toolsFeedTypeListAdapter;
    private Date selectDate;
    ToolsDiaryPresenter toolsDiaryPresenter;
    private EditText backUP;

    @Override
    protected int getLayoutId() {
        return R.layout.index_fragment_tools_diary_sup;
    }

    @Override
    protected void findViews() {
        initView();
        buildRecyclerView();
        try {
            selectDate = new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        toolsDiaryPresenter = new ToolsDiaryPresenter(getActivity(), this);

        if (get("recordId")!= null) {
            toolsDiaryPresenter.getNowDiary(new SimpleHashMapBuilder<String, Object>().puts(Functions.FUNCTION,"9027").puts("recordId",get("recordId").toString()).puts("recordType","3"));
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
    }
    @Override
    public View getBottomView() {
        return backUP;
    }
    private ToolsDiaryFood toolsDayNow;

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


                type= new TypeToken<ToolsDiaryFood>() {
                }.getType();
                resultTmp=gson.fromJson(userShopInfoDTOS,type);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultTmp;
    }
    private void buildRecyclerView() {
        toolsFeedTypeListAdapter = new ToolsFeedTypeListAdapter();
        typeRecycler.setLayoutManager(new GridLayoutManager(getContext(), 3));
//        toolsFeedTypeListAdapter.setChoseCount(1);
        List<ToolsMedType> toolsMedTypes = new ArrayList<>();
        toolsMedTypes.add(new ToolsMedType("果汁", "R.drawable.guozhi", "R.drawable.guozhi_w"));
        toolsMedTypes.add(new ToolsMedType("米粉", "R.drawable.mifen", "R.drawable.mifen_w"));
        toolsMedTypes.add(new ToolsMedType("牛奶", "R.drawable.niunai", "R.drawable.niunai_w"));


        toolsMedTypes.add(new ToolsMedType("水果", "R.drawable.shuiguo", "R.drawable.shuiguo_w"));
        toolsMedTypes.add(new ToolsMedType("蔬菜", "R.drawable.shucai", "R.drawable.shucai_w"));
        toolsMedTypes.add(new ToolsMedType("肉类", "R.drawable.roulei", "R.drawable.roulei_w"));


        toolsMedTypes.add(new ToolsMedType("鱼类", "R.drawable.yulei", "R.drawable.yulei_w"));
        toolsMedTypes.add(new ToolsMedType("汤", "R.drawable.tang", "R.drawable.tang_w"));
        toolsMedTypes.add(new ToolsMedType("鸡蛋", "R.drawable.jidan", "R.drawable.jidan_w"));
        toolsMedTypes.addAll(resolveHistoryData(SpUtils.getValue(mContext, SpKey.SUP_TMP)));

        toolsMedTypes.add(new ToolsMedType("自定义", "R.drawable.zidingyi", "R.drawable.zidingyi_w"));
        typeRecycler.setAdapter(toolsFeedTypeListAdapter);
        toolsFeedTypeListAdapter.setNewData(toolsMedTypes);
        toolsFeedTypeListAdapter.setSaveFlag(true,SpKey.SUP_TMP);
        toolsFeedTypeListAdapter.setDialogtitle("输入辅食名称");

    }
    private List<ToolsMedType> resolveHistoryData(String obj) {
        List<ToolsMedType> result = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(obj);
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<ToolsMedType>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    public static ToolsFeedSUPFragment newInstance(Map<String, Object> maporg) {
        ToolsFeedSUPFragment fragment = new ToolsFeedSUPFragment();
        Bundle args = new Bundle();
        BaseFragment.bundleMap(maporg, args);
        fragment.setArguments(args);
        return fragment;
    }

    private void initView() {
        typeRecycler = (RecyclerView) findViewById(R.id.typeRecycler);
        timeTitle = (TextView) findViewById(R.id.timeTitle);
        timeFragment = (FrameLayout) findViewById(R.id.timeFragment);
        backUP = (EditText) findViewById(R.id.backUP);
    }

    @Override
    public void onSucessGetAllStatus(List<UserInfoExModel> userInfoExModels) {

    }

    @Override
    public void onSucessGetNowDiary(String result) {
        try {
            toolsDayNow = (ToolsDiaryFood) resolveStatusData(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(toolsDayNow!=null&&isfragmenthow){
            backUP.setText(toolsDayNow.memo);
            Map<String, Object> maporg = new HashMap<>();
            maporg.put("beginTime", "2020/01/01");//必须是带0的
            maporg.put("endTime", new SimpleDateFormat("yyyy/MM/dd").format(new Date()));//必须是带0的
            maporg.put("format", "yyyy/MM/dd");
            try {
                maporg.put("init", new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(toolsDayNow.eatingTime)));
                selectDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(toolsDayNow.eatingTime);
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
            boolean isNeedNew=true;
            for (int i = 0; i <toolsFeedTypeListAdapter.getData().size() ; i++) {
                if(toolsFeedTypeListAdapter.getData().get(i).name.equals(toolsDayNow.itemName)){
                    isNeedNew=false;
                }
            }
            if(isNeedNew){
                toolsFeedTypeListAdapter.addData(new ToolsMedType(toolsDayNow.itemName, "R.drawable.zidingyi", "R.drawable.zidingyi_w") );
                toolsFeedTypeListAdapter.saveFunction(new ToolsMedType(toolsDayNow.itemName, "R.drawable.zidingyi", "R.drawable.zidingyi_w"),SpKey.SUP_TMP);
            }
            toolsFeedTypeListAdapter.setSelectS(toolsDayNow.itemName+",");
            toolsFeedTypeListAdapter.notifyDataSetChanged();
        }
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
            Map<String, Object> submap = new HashMap<>();
            submap.put("childId", get("childId").toString());
            if (get("recordId") != null) {
                submap.put("recordId", get("recordId").toString());
                submap.put(Functions.FUNCTION, "9029");
            } else {
                submap.put(Functions.FUNCTION, "9028");
            }
            submap.put("recordType", "3");
            try {
                submap.put("feedingDateTime", (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(selectDate)) + "");
            } catch (Exception e) {
                e.printStackTrace();
            }
            submap.put("itemName", toolsFeedTypeListAdapter.getSelectS() + "");
            submap.put("eatingTime", (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(selectDate)) + "");
            submap.put("memo", backUP.getText().toString() + "");
            toolsDiaryPresenter.updateDiary(submap);
        }
    }

    private boolean checkIllegal() {
        if (TextUtils.isEmpty(toolsFeedTypeListAdapter.getSelectS())) {
            showToast("请选择辅食");
            return false;
        }
        return true;
    }
}
