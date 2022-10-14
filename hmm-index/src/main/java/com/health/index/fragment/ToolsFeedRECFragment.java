package com.health.index.fragment;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

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
import com.healthy.library.model.ToolsDiaryMK;
import com.healthy.library.model.ToolsSumType;
import com.health.index.model.UserInfoExModel;
import com.healthy.library.model.UserInfoMonModel;
import com.health.index.presenter.ToolsDiaryPresenter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.constant.Functions;
import com.healthy.library.business.FeedDateFragment;
import com.healthy.library.interfaces.OnDateConfirmListener;
import com.healthy.library.watcher.DecimalInputTextWatcher;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.watcher.TextLimitTextWatcher;
import com.healthy.library.widget.CommonInsertSection;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToolsFeedRECFragment extends ToolsFeedBaseFragment implements ToolsDiaryContract.View {
    private TextView timeTitle;
    private FrameLayout timeFragment;
    ToolsFeedTypeListAdapter toolsFeedTypeListAdapter;
    private CommonInsertSection nailiangLL;
    private EditText backUP;
    private EditText nailiang;
    private String nailiangTime;
    private Date selectDate;
    ToolsDiaryPresenter toolsDiaryPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.index_fragment_tools_diary_rec;
    }

    @Override
    protected void findViews() {
        initView();
        toolsDiaryPresenter=new ToolsDiaryPresenter(getActivity(),this);

        if (get("recordId")!= null) {
            toolsDiaryPresenter.getNowDiary(new SimpleHashMapBuilder<String, Object>().puts(Functions.FUNCTION,"9027").puts("recordId",get("recordId").toString()).puts("recordType","2"));
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
                    selectDate=date;
                }
            })).commitAllowingStateLoss();
            nailiang.addTextChangedListener(new DecimalInputTextWatcher(nailiang,4,0));
            backUP.addTextChangedListener(new TextLimitTextWatcher(backUP,100));
            backUP.setHint("写点什么吧");
        }
    }

    public static ToolsFeedRECFragment newInstance(Map<String, Object> maporg) {
        ToolsFeedRECFragment fragment = new ToolsFeedRECFragment();
        Bundle args = new Bundle();
        BaseFragment.bundleMap(maporg, args);
        fragment.setArguments(args);
        return fragment;
    }
    private ToolsDiaryMK toolsDayNow;

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

                type= new TypeToken<ToolsDiaryMK>() {
                }.getType();
                resultTmp=gson.fromJson(userShopInfoDTOS,type);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultTmp;
    }

    private void initView() {
        timeTitle = (TextView) findViewById(R.id.timeTitle);
        timeFragment = (FrameLayout) findViewById(R.id.timeFragment);
        nailiangLL = (CommonInsertSection) findViewById(R.id.nailiangLL);
        backUP = (EditText) findViewById(R.id.backUP);
        nailiang=nailiangLL.findViewById(R.id.ed);
        nailiangLL.getmTxtTitle().getPaint().setFakeBoldText(true);
        nailiangLL.getmTxtTitle().setGravity(Gravity.LEFT);
        nailiangLL.getmTxtTitle().setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
        ConstraintLayout.LayoutParams pam= (ConstraintLayout.LayoutParams) nailiangLL.getmTxtTitle().getLayoutParams();
        pam.width=ConstraintLayout.LayoutParams.WRAP_CONTENT;
        pam.setMarginStart(0);
        nailiangLL.getmTxtTitle().setLayoutParams(pam);
        nailiang.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
    }

    @Override
    public void onSucessGetAllStatus(List<UserInfoExModel> userInfoExModels) {

    }

    @Override
    public void onSucessGetNowDiary(String result) {
        try {
            toolsDayNow = (ToolsDiaryMK) resolveStatusData(result);
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
                maporg.put("init", new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(toolsDayNow.feedingDateTime)));
                selectDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(toolsDayNow.feedingDateTime);
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

            nailiang.setText(toolsDayNow.milkVolume);

        }
    }
    @Override
    public View getBottomView() {
        return backUP;
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
        if(checkIllegal()){
            try {
                Map<String,Object> submap=new HashMap<>();
                submap.put("childId",get("childId").toString());
                if(get("recordId")!=null){
                    submap.put("recordId",get("recordId").toString());
                    submap.put(Functions.FUNCTION,"9029");
                }else {
                    submap.put(Functions.FUNCTION,"9028");
                }
                submap.put("recordType","2");
                if(selectDate==null){
                    try {
                        selectDate = new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                submap.put("feedingDateTime", (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(selectDate))+"");

                submap.put("milkVolume",nailiang.getText().toString()+"");
                submap.put("memo",backUP.getText().toString()+"");
                toolsDiaryPresenter.updateDiary(submap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkIllegal() {
        if(TextUtils.isEmpty(nailiang.getText().toString())){
            showToast("请输入本次奶量");
            return false;
        }
        return true;
    }
}
