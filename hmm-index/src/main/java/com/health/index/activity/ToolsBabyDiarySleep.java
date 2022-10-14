package com.health.index.activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.health.index.R;
import com.health.index.contract.ToolsDiaryContract;
import com.health.index.model.UserInfoExModel;
import com.healthy.library.model.UserInfoMonModel;
import com.health.index.presenter.ToolsDiaryPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.Functions;
import com.healthy.library.business.FeedDateFragment;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.interfaces.OnDateConfirmListener;
import com.healthy.library.interfaces.OnSubmitListener;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.watcher.TextLimitTextWatcher;
import com.healthy.library.widget.TopBar;
import com.hyb.library.PreventKeyboardBlockUtil;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(path = IndexRoutes.INDEX_TOOLS_BABY_DIARY_SLEEP)
public class ToolsBabyDiarySleep extends BaseActivity implements IsFitsSystemWindows, ToolsDiaryContract.View  {
    private TopBar topBar;
    private TextView tlDiv;
    @Autowired
    String childId;
    @Autowired
    String recordId;
    @Autowired
    String init;
    private Date selectDate;
    ToolsDiaryPresenter toolsDiaryPresenter;
    private android.widget.LinearLayout vp;
    private FrameLayout timeFragment;
    private android.widget.EditText backUP;

    @Autowired
    boolean isSleep=true;
    private TextView titleTime;

    @Override
    protected int getLayoutId() {
        return R.layout.index_activity_tools_diary_sleep;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        toolsDiaryPresenter=new ToolsDiaryPresenter(this,this);
        Map<String, Object> maporg = new HashMap<>();
        maporg.put("beginTime", "2020/01/01");//必须是带0的
        maporg.put("endTime", new SimpleDateFormat("yyyy/MM/dd").format(new Date()));//必须是带0的
        maporg.put("format", "yyyy/MM/dd");
        if(init!=null){
            maporg.put("init", init);
        }else {
            maporg.put("init", new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date()));
        }

        try {
            if(init!=null){
                selectDate = new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(init);
            }else {
                selectDate = new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date()));
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.timeFragment, FeedDateFragment.newInstance(maporg).setOnConfirmClick(new OnDateConfirmListener() {
            @Override
            public void onConfirm(int pos, Date date) {
                selectDate = date;
                //System.out.println("改变事件");
            }
        })).commitAllowingStateLoss();
        topBar.setSubmitListener(new OnSubmitListener() {
            @Override
            public void onSubmitBtnPressed() {
                updateGrow();
            }
        });
        backUP.addTextChangedListener(new TextLimitTextWatcher(backUP,100));
        backUP.setHint("写点什么吧");
        PreventKeyboardBlockUtil.getInstance().setContext(mActivity).unRegister();
        PreventKeyboardBlockUtil.getInstance().setContext(mActivity).setBtnView(backUP).register();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public void getData() {
        super.getData();
        if(recordId!=null){
            buildNiView();
        }else {
            Map<String,Object> map=new HashMap<>();
            map.put(Functions.FUNCTION,"9035");
            map.put("childId",childId);
            toolsDiaryPresenter.getNowDiary(map);
        }

    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.top_bar);

        tlDiv = (TextView) findViewById(R.id.tlDiv);
        vp = (LinearLayout) findViewById(R.id.vp);
        timeFragment = (FrameLayout) findViewById(R.id.timeFragment);
        backUP = (EditText) findViewById(R.id.backUP);
        titleTime = (TextView) findViewById(R.id.titleTime);
    }

    public void updateGrow() {
        if (checkIllegal()) {
            Map<String, Object> submap = new HashMap<>();
            submap.put("childId", childId);
            if (recordId != null) {
                submap.put("recordId", recordId);
                submap.put(Functions.FUNCTION, "9029");
            } else {
                submap.put(Functions.FUNCTION, "9028");
            }
            submap.put("recordType", "7");


            submap.put("feedingDateTime",  (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(selectDate)) + "");

            if(!isSleep||recordId==null){
            }
            if(isSleep){

                submap.put("wakeUpTime",  (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(selectDate)) + "");
                submap.put("wakeUpMemo", backUP.getText().toString() + "");
            }else {
                submap.put("sleepTime",  (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(selectDate)) + "");
                submap.put("sleepMemo", backUP.getText().toString() + "");

            }

            toolsDiaryPresenter.updateDiary(submap);
        }
    }

    private boolean checkIllegal() {

        return true;
    }

    @Override
    public void onSucessGetAllStatus(List<UserInfoExModel> userInfoExModels) {

    }

    @Override
    public void onSucessGetNowDiary(String result) {
        JSONObject jsonObject= null;
        try {
            jsonObject = new JSONObject(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        isSleep=jsonObject.optBoolean("data");
        buildNiView();
    }

    private void buildNiView() {
        if(isSleep){
            topBar.setTitle("醒来");
            titleTime.setText("醒来时间");
        }else {
            topBar.setTitle("入睡");
            titleTime.setText("入睡时间");
        }
    }

    @Override
    public void onSucessUpdateDiary() {
        finish();
    }

    @Override
    public void onsucessGetMine(UserInfoMonModel userInfoMonModel) {

    }
}
