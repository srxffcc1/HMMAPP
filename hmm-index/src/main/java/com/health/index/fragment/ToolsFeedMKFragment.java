package com.health.index.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.index.R;
import com.health.index.contract.ToolsDiaryContract;
import com.healthy.library.model.ToolsDiaryMK;
import com.healthy.library.model.ToolsSumType;
import com.health.index.model.UserInfoExModel;
import com.healthy.library.model.UserInfoMonModel;
import com.health.index.presenter.ToolsDiaryPresenter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.constant.Functions;
import com.healthy.library.business.DurDateFragment;
import com.healthy.library.business.FeedDateFragment;
import com.healthy.library.interfaces.OnDateConfirmListener;
import com.healthy.library.interfaces.OnDateDurConfirmListener;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.watcher.TextLimitTextWatcher;
import com.healthy.library.utils.DateUtils;
import com.hyb.library.PreventKeyboardBlockUtil;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToolsFeedMKFragment extends ToolsFeedBaseFragment implements ToolsDiaryContract.View {

    private String[] titlesorg = {"计时", "手动"};
    private SegmentTabLayout tab;
    private CheckBox leftCheck;
    private CheckBox leftPlayCheck;
    private Chronometer leftPlayTime;
    private CheckBox rightCheck;
    private CheckBox rightPlayCheck;
    private Chronometer rightPlayTime;
    private FrameLayout timeFragment;
    private FrameLayout durFragment;
    private LinearLayout noautoLL;
    private int nowMode = 1;
    private Handler mHandler;

    private long mRecordTimeLeft;
    private long mRecordTimeRight;
    private Date selectDate;

    private String leftdur="00:00";
    private String rightdur="00:00";

    DurDateFragment durDateFragment;
    private TextView leftHandTime;
    private TextView rightHandTime;
    ToolsDiaryPresenter toolsDiaryPresenter;
    private EditText backUP;
    private boolean isInit;


    @Override
    protected int getLayoutId() {
        return R.layout.index_fragment_tools_diary_mk;
    }

    @Override
    protected void findViews() {
        initView();

        toolsDiaryPresenter = new ToolsDiaryPresenter(getActivity(), this);

        Map<String, Object> maporgDur = new HashMap<>();
        durDateFragment = DurDateFragment.newInstance(maporgDur).setOnConfirmClick(new OnDateDurConfirmListener() {
            @Override
            public void onConfirm(int pos, String date) {
                if (leftCheck.isChecked()) {
                    leftdur = date;
                    leftHandTime.setText(date + ":00");
                }
                if (rightCheck.isChecked()) {
                    rightdur = date;
                    rightHandTime.setText(date + ":00");
                }
                if (!leftCheck.isChecked() && !rightCheck.isChecked() && tab.getCurrentTab() == 1) {
                    Toast.makeText(mContext, "请选择一侧", Toast.LENGTH_SHORT).show();
                }
            }
        });
        getChildFragmentManager().beginTransaction().replace(R.id.durFragment, durDateFragment).commitAllowingStateLoss();

        if (get("recordId")!= null) {
            toolsDiaryPresenter.getNowDiary(new SimpleHashMapBuilder<String, Object>().puts(Functions.FUNCTION,"9027").puts("recordId",get("recordId").toString()).puts("recordType","1"));
        }else {
            tab.setTabData(titlesorg);
            tab.setOnTabSelectListener(new OnTabSelectListener() {
                @Override
                public void onTabSelect(int position) {
                    if (position == 0) {
                        nowMode = 1;
                    } else {
                        nowMode = 2;
                    }
                    buildTab(true);
                }

                @Override
                public void onTabReselect(int position) {

                }
            });
            leftCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (tab.getCurrentTab() == 0) {
                        if (isChecked) {
                            leftPlayCheck.setEnabled(true);
//            rightPlayCheck.setEnabled(false);
                            leftPlayTime.setTextColor(Color.parseColor("#fff97a87"));
//            rightPlayTime.setTextColor(Color.parseColor("#9596A4"));
                            mRecordTimeLeft = 0;
                            leftPlayTime.setBase(SystemClock.elapsedRealtime());
                            leftPlayCheck.setChecked(true);
//            leftPlayTime.start();
                        } else {
                            leftPlayCheck.setEnabled(false);
                            leftPlayTime.setTextColor(Color.parseColor("#9596A4"));
                            leftPlayCheck.setChecked(false);
                        }
                    } else {
                        if (isChecked) {
                            rightCheck.setChecked(false);
                            if("00:00".equals(leftHandTime.getText().toString())){
                                leftdur = "30:00";
                                leftHandTime.setText("30:00");
                            }
                            if (!rightCheck.isChecked()) {

                                durDateFragment.reset(getMill(leftHandTime));
                            }



                            leftPlayCheck.setEnabled(true);
                            leftPlayTime.setTextColor(Color.parseColor("#fff97a87"));
                            leftPlayCheck.setChecked(true);
                        } else {
                            leftPlayCheck.setEnabled(false);
                            leftPlayTime.setTextColor(Color.parseColor("#9596A4"));
                            leftPlayCheck.setChecked(false);
                        }

                    }


                }
            });
            rightCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (tab.getCurrentTab() == 0) {
                        if (isChecked) {
                            rightPlayCheck.setEnabled(true);
                            rightPlayTime.setTextColor(Color.parseColor("#fff97a87"));
                            mRecordTimeRight = 0;
                            rightPlayTime.setBase(SystemClock.elapsedRealtime());
                            rightPlayCheck.setChecked(true);
                        } else {
                            rightPlayCheck.setEnabled(false);
                            rightPlayTime.setTextColor(Color.parseColor("#9596A4"));
                            rightPlayCheck.setChecked(false);
                        }
                    } else {
                        if (isChecked) {
                            if("00:00".equals(rightHandTime.getText().toString())){
                                rightdur = "30:00";
                                rightHandTime.setText("30:00");
                            }

                            leftCheck.setChecked(false);
                            if (!leftCheck.isChecked()) {
                                durDateFragment.reset(getMill(rightHandTime));
                            }
                            rightPlayCheck.setEnabled(true);
                            rightPlayTime.setTextColor(Color.parseColor("#fff97a87"));
                            rightPlayCheck.setChecked(true);
                        } else {
                            rightPlayCheck.setEnabled(false);
                            rightPlayTime.setTextColor(Color.parseColor("#9596A4"));
                            rightPlayCheck.setChecked(false);
                        }

                    }


                }
            });
            leftPlayCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (leftPlayCheck.isEnabled() && isChecked) {
                        if (mRecordTimeLeft != 0) {
                            leftPlayTime.setBase(leftPlayTime.getBase() + (SystemClock.elapsedRealtime() - mRecordTimeLeft));
                        } else {
                            leftPlayTime.setBase(SystemClock.elapsedRealtime());
                        }
                        leftPlayTime.start();
                    } else {
                        leftPlayTime.stop();
                        mRecordTimeLeft = SystemClock.elapsedRealtime();
                    }
                }
            });
            rightPlayCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (rightPlayCheck.isEnabled() && isChecked) {
                        if (mRecordTimeRight != 0) {
                            rightPlayTime.setBase(rightPlayTime.getBase() + (SystemClock.elapsedRealtime() - mRecordTimeRight));
                        } else {
                            rightPlayTime.setBase(SystemClock.elapsedRealtime());
                        }
                        rightPlayTime.start();
                    } else {
                        rightPlayTime.stop();
                        mRecordTimeRight = SystemClock.elapsedRealtime();
                    }
                }
            });
            buildTab(true);
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


    private void buildTab(boolean needReset) {
        leftCheck.setChecked(false);
        rightCheck.setChecked(false);
        if (nowMode == 1) {
            leftPlayCheck.setVisibility(View.VISIBLE);
            leftPlayTime.setVisibility(View.VISIBLE);
            rightPlayCheck.setVisibility(View.VISIBLE);
            rightPlayTime.setVisibility(View.VISIBLE);
            noautoLL.setVisibility(View.GONE);

            leftHandTime.setVisibility(View.GONE);
            rightHandTime.setVisibility(View.GONE);
            if(needReset){
                mRecordTimeLeft = 0;
                mRecordTimeRight = 0;
                rightdur = "00:00";
                leftdur = "00:00";
                leftHandTime.setText("00:00");
                rightHandTime.setText("00:00");
                rightPlayTime.setBase(SystemClock.elapsedRealtime());
                leftPlayTime.setBase(SystemClock.elapsedRealtime());
                rightPlayTime.stop();
                leftPlayTime.stop();
                try {
                    selectDate = new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } else {
            leftHandTime.setVisibility(View.VISIBLE);
            rightHandTime.setVisibility(View.VISIBLE);
            leftPlayCheck.setVisibility(View.GONE);
            leftPlayTime.setVisibility(View.GONE);
            rightPlayCheck.setVisibility(View.GONE);
            rightPlayTime.setVisibility(View.GONE);
            noautoLL.setVisibility(View.VISIBLE);
        }
        PreventKeyboardBlockUtil.getInstance().setContext(mActivity).unRegister();
        PreventKeyboardBlockUtil.getInstance().setContext(mActivity).setBtnView(backUP).register();
    }

    public static ToolsFeedMKFragment newInstance(Map<String, Object> maporg) {
        ToolsFeedMKFragment fragment = new ToolsFeedMKFragment();
        Bundle args = new Bundle();
        BaseFragment.bundleMap(maporg, args);
        fragment.setArguments(args);
        return fragment;
    }

    private void initView() {
        tab = (SegmentTabLayout) findViewById(R.id.tab);
        leftCheck = (CheckBox) findViewById(R.id.leftCheck);
        leftPlayCheck = (CheckBox) findViewById(R.id.leftPlayCheck);
        leftPlayTime = (Chronometer) findViewById(R.id.leftPlayTime);
        rightCheck = (CheckBox) findViewById(R.id.rightCheck);
        rightPlayCheck = (CheckBox) findViewById(R.id.rightPlayCheck);
        rightPlayTime = (Chronometer) findViewById(R.id.rightPlayTime);
        timeFragment = (FrameLayout) findViewById(R.id.timeFragment);
        durFragment = (FrameLayout) findViewById(R.id.durFragment);
        noautoLL = (LinearLayout) findViewById(R.id.noautoLL);

        rightPlayTime.setBase(SystemClock.elapsedRealtime());
        leftPlayTime.setBase(SystemClock.elapsedRealtime());

        leftHandTime = (TextView) findViewById(R.id.leftHandTime);
        rightHandTime = (TextView) findViewById(R.id.rightHandTime);
        backUP = (EditText) findViewById(R.id.backUP);
    }

    @Override
    public void onSucessGetAllStatus(List<UserInfoExModel> userInfoExModels) {

    }

    private ToolsDiaryMK toolsDayNow;
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



            backUP.addTextChangedListener(new TextLimitTextWatcher(backUP,100));
            backUP.setHint("写点什么吧");
            tab.setTabData(titlesorg);
            tab.setOnTabSelectListener(new OnTabSelectListener() {
                @Override
                public void onTabSelect(int position) {
                    if (position == 0) {
                        nowMode = 1;
                    } else {
                        nowMode = 2;
                    }
                    buildTab(true);
                }

                @Override
                public void onTabReselect(int position) {

                }
            });


            if("2".equals(toolsDayNow.operate)){
                tab.setCurrentTab(1);
                nowMode = 2;
                buildTab(true);
            }else {
                tab.setCurrentTab(0);
                nowMode = 1;
                buildTab(true);
            }
            {
                if("2".equals(toolsDayNow.operate)){
                    if(!"0".equals(toolsDayNow.leftKeepTime)){

                        leftHandTime.setText(DateUtils.getDistanceTimeOnlySecondString(Integer.parseInt(toolsDayNow.leftKeepTime)*1000));
                        try {
                            durDateFragment.reset(getMill(leftHandTime));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if(!rightCheck.isChecked()){
                            leftCheck.setChecked(true);
                        }
                    }
                    if(!"0".equals(toolsDayNow.rightKeepTime)){

                        rightHandTime.setText(DateUtils.getDistanceTimeOnlySecondString(Integer.parseInt(toolsDayNow.rightKeepTime)*1000));
                        try {
                            durDateFragment.reset(getMill(rightHandTime));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if(!leftCheck.isChecked()){
                            rightCheck.setChecked(true);
                        }
                    }

                }else {
                    if(!TextUtils.isEmpty(toolsDayNow.leftKeepTime)){
                        try {
                            leftPlayTime.setBase(SystemClock.elapsedRealtime()-Integer.parseInt(toolsDayNow.leftKeepTime)*1000);
                            mRecordTimeLeft=SystemClock.elapsedRealtime();
                            leftCheck.setChecked(true);
                            leftPlayTime.setTextColor(Color.parseColor("#fff97a87"));
                            leftPlayCheck.setEnabled(true);
                            leftPlayCheck.setChecked(false);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    if(!TextUtils.isEmpty(toolsDayNow.rightKeepTime)){
                        try {
                            rightPlayTime.setBase(SystemClock.elapsedRealtime()-Integer.parseInt(toolsDayNow.rightKeepTime)*1000);
                            mRecordTimeRight=SystemClock.elapsedRealtime();
                            rightCheck.setChecked(true);
                            rightPlayTime.setTextColor(Color.parseColor("#fff97a87"));
                            rightPlayCheck.setEnabled(true);
                            rightPlayCheck.setChecked(false);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
            leftCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (tab.getCurrentTab() == 0) {
                        if (isChecked) {
                            leftPlayCheck.setEnabled(true);

//            rightPlayCheck.setEnabled(false);
                            leftPlayTime.setTextColor(Color.parseColor("#fff97a87"));
//            rightPlayTime.setTextColor(Color.parseColor("#9596A4"));
                            mRecordTimeLeft = 0;
                            leftPlayTime.setBase(SystemClock.elapsedRealtime());
                            leftPlayCheck.setChecked(true);
//            leftPlayTime.start();
                        } else {
                            leftPlayCheck.setEnabled(false);
                            leftPlayTime.setTextColor(Color.parseColor("#9596A4"));
                            leftPlayCheck.setChecked(false);
                        }
                    } else {
                        if (isChecked) {
                            rightCheck.setChecked(false);
                            if("00:00".equals(leftHandTime.getText().toString())){
                                leftdur = "30:00";
                                leftHandTime.setText("30:00");
                            }
                            if (!rightCheck.isChecked()) {

                                durDateFragment.reset(getMill(leftHandTime));
                            }
                            leftPlayCheck.setEnabled(true);
                            leftPlayTime.setTextColor(Color.parseColor("#fff97a87"));
                            leftPlayCheck.setChecked(true);
                        } else {
                            leftPlayCheck.setEnabled(false);
                            leftPlayTime.setTextColor(Color.parseColor("#9596A4"));
                            leftPlayCheck.setChecked(false);
                        }

                    }


                }
            });
            rightCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (tab.getCurrentTab() == 0) {
                        if (isChecked) {
                            rightPlayCheck.setEnabled(true);
                            rightPlayTime.setTextColor(Color.parseColor("#fff97a87"));
                            mRecordTimeRight = 0;
                            rightPlayTime.setBase(SystemClock.elapsedRealtime());
                            rightPlayCheck.setChecked(true);
                        } else {
                            rightPlayCheck.setEnabled(false);
                            rightPlayTime.setTextColor(Color.parseColor("#9596A4"));
                            rightPlayCheck.setChecked(false);
                        }
                    } else {
                        if (isChecked) {
                            if("00:00".equals(rightHandTime.getText().toString())){
                                rightdur = "30:00";
                                rightHandTime.setText("30:00");
                            }
                            leftCheck.setChecked(false);
                            if (!leftCheck.isChecked()) {
                                durDateFragment.reset(getMill(rightHandTime));
                            }
                            rightPlayCheck.setEnabled(true);
                            rightPlayTime.setTextColor(Color.parseColor("#fff97a87"));
                            rightPlayCheck.setChecked(true);
                        } else {
                            rightPlayCheck.setEnabled(false);
                            rightPlayTime.setTextColor(Color.parseColor("#9596A4"));
                            rightPlayCheck.setChecked(false);
                        }

                    }


                }
            });
            leftPlayCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (leftPlayCheck.isEnabled() && isChecked) {
                        //System.out.println("记录的"+mRecordTimeLeft);
                        if (mRecordTimeLeft != 0) {
                            leftPlayTime.setBase(leftPlayTime.getBase() + (SystemClock.elapsedRealtime() - mRecordTimeLeft));
                        } else {
                            leftPlayTime.setBase(SystemClock.elapsedRealtime());
                        }
                        leftPlayTime.start();
                    } else {
                        leftPlayTime.stop();
                        mRecordTimeLeft = SystemClock.elapsedRealtime();
                    }
                }
            });
            rightPlayCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (rightPlayCheck.isEnabled() && isChecked) {
                        if (mRecordTimeRight != 0) {
                            rightPlayTime.setBase(rightPlayTime.getBase() + (SystemClock.elapsedRealtime() - mRecordTimeRight));
                        } else {
                            rightPlayTime.setBase(SystemClock.elapsedRealtime());
                        }
                        rightPlayTime.start();
                    } else {
                        rightPlayTime.stop();
                        mRecordTimeRight = SystemClock.elapsedRealtime();
                    }
                }
            });



        }
    }

    @Override
    public void onSucessUpdateDiary() {

        getActivity().finish();
    }

    @Override
    public void onsucessGetMine(UserInfoMonModel userInfoMonModel) {

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

                type= new TypeToken<ToolsDiaryMK>() {
                }.getType();
                 resultTmp=gson.fromJson(userShopInfoDTOS,type);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultTmp;
    }

    @Override
    public void updateGrow() {
        leftPlayCheck.setChecked(false);
        rightPlayCheck.setChecked(false);
        //System.out.println("母乳测试" + leftdur + "----" + rightdur);
        if (checkIllegal()) {
            Map<String, Object> submap = new HashMap<>();
            submap.put("childId", get("childId").toString());
            if (get("recordId") != null) {
                submap.put("recordId", get("recordId").toString());
                submap.put(Functions.FUNCTION, "9029");
            } else {
                submap.put(Functions.FUNCTION, "9028");
            }
            submap.put("recordType", "1");
            submap.put("feedingDateTime", (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(selectDate)) + "");



            submap.put("leftBeginDateTime", (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(selectDate)) + "");
            if(tab.getCurrentTab()==0){
                submap.put("leftKeepTime", (getMill(leftPlayTime) ) + "");
                submap.put("rightKeepTime", (getMill(rightPlayTime)) + "");
            }else {
                submap.put("leftKeepTime", (getMill(leftHandTime) ) + "");
                submap.put("rightKeepTime", (getMill(rightHandTime)) + "");
            }
            submap.put("rightBeginDateTime", (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(selectDate)) + "");

            submap.put("operate",(tab.getCurrentTab()+1)+ "");

            submap.put("memo", backUP.getText().toString() + "");
            toolsDiaryPresenter.updateDiary(submap);
        }
    }

    @Override
    public View getBottomView() {
        return backUP;
    }

    private boolean checkIllegal() {
        if (!rightCheck.isChecked() && !leftCheck.isChecked()&&getMill(leftPlayTime)==0&&getMill(rightPlayTime)==0&&getMill(leftHandTime)==0&&getMill(rightHandTime)==0) {
            Toast.makeText(mContext, "至少选择一侧母乳", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(tab.getCurrentTab()==0){
            //System.out.println("左边");
            if (getMill(leftPlayTime)==0&&getMill(rightPlayTime)==0) {
                Toast.makeText(mContext, "至少选择一侧母乳", Toast.LENGTH_SHORT).show();
                return false;
            }
        }else {
            //System.out.println("右边");
            if (getMill(leftHandTime)==0&&getMill(rightHandTime)==0) {
                Toast.makeText(mContext, "请选择持续时间", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    public long getMill(TextView timer) {
        if(timer.getText().toString().split(":").length>2){
            long hour = Integer.parseInt(timer.getText().toString().split(":")[0]);
            long min = Integer.parseInt(timer.getText().toString().split(":")[1]);
            long sec = Integer.parseInt(timer.getText().toString().split(":")[2]);
            long result = hour * 60 * 60 + min * 60 + sec;
            //System.out.println(result);
            return result;
        }else {
            long min = Integer.parseInt(timer.getText().toString().split(":")[0]);
            long sec = Integer.parseInt(timer.getText().toString().split(":")[1]);
            long result =  min * 60 + sec;

            //System.out.println(result);
            return result;
        }
    }

}
