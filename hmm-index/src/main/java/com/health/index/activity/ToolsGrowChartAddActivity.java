package com.health.index.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.health.index.R;
import com.health.index.contract.ToolsGrowContract;
import com.healthy.library.model.ToolsGrow;
import com.healthy.library.model.ToolsGrowBase;
import com.health.index.model.UserInfoExModel;
import com.healthy.library.model.UserInfoMonModel;
import com.health.index.presenter.ToolsGrowPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.Functions;
import com.healthy.library.dialog.DateDialog;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.interfaces.OnDateConfirmListener;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.watcher.DecimalInputTextWatcher;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.utils.DateUtils;
import com.healthy.library.widget.CommonInsertSection;
import com.healthy.library.widget.ImageTextView;
import com.healthy.library.widget.TopBar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Route(path = IndexRoutes.INDEX_TOOLS_GROW_ADD)
public class ToolsGrowChartAddActivity extends BaseActivity implements IsFitsSystemWindows, ToolsGrowContract.View {

    @Autowired
    String chosedate;
    @Autowired
    String recordId;
    @Autowired
    String childId;
    @Autowired
    String heightTmp;
    @Autowired
    String weightTmp;
    private TopBar topBar;
    private ConstraintLayout choseTimeLL;
    private ImageTextView timeChoseTime;
    private TextView timeBabyDate;
    private com.healthy.library.widget.CommonInsertSection shengaoLL;
    private com.healthy.library.widget.CommonInsertSection tizhongLL;
    private DateDialog mDateDialog;
    private EditText height;
    private EditText weight;
    private ImageView heightImg;
    private ImageView weightImg;
    ToolsGrowPresenter toolsGrowPresenter;
    public String selectdate;
    @Autowired
    String age;
    @Autowired
    String birthday;
    @Autowired
    String hasaddTime;

    @Override
    protected int getLayoutId() {
        return R.layout.index_activity_tools_chart_add;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        toolsGrowPresenter = new ToolsGrowPresenter(this, this);
        if (TextUtils.isEmpty(chosedate)) {
            selectdate=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            timeChoseTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showTimePick();
                }
            });
            try {
                timeBabyDate.setText(DateUtils.getAgeWithMonth(new SimpleDateFormat("yyyy-MM-dd").parse(birthday),new Date()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else {
            selectdate= chosedate;
            timeChoseTime.setText(chosedate);
            try {
                timeBabyDate.setText(DateUtils.getAgeWithMonth(new SimpleDateFormat("yyyy-MM-dd").parse(birthday),new SimpleDateFormat("yyyy-MM-dd").parse(chosedate)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
//            timeChoseTime.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    showTimePick();
//                }
//            });
        }


        height.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length()>0){
                    heightImg.setVisibility(View.VISIBLE);
                }else {
                    heightImg.setVisibility(View.GONE);
                }
            }
        });
        heightImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                height.setText("");
            }
        });


        weight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length()>0){
                    weightImg.setVisibility(View.VISIBLE);
                }else {
                    weightImg.setVisibility(View.GONE);
                }
            }
        });
        weightImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weight.setText("");
            }
        });
        if(!TextUtils.isEmpty(recordId)){
            height.setText(heightTmp);
            weight.setText(weightTmp);
            topBar.setTitle("修改记录");
        }
        height.addTextChangedListener(new DecimalInputTextWatcher(height,3,1));
        weight.addTextChangedListener(new DecimalInputTextWatcher(weight,3,2));
    }
    private boolean checkIllegal() {
        if(TextUtils.isEmpty(height.getText().toString())){
            showToastIgnoreLife("请输入身高");
            return false;
        }
        if(TextUtils.isEmpty(weight.getText().toString())){
            showToastIgnoreLife("请输入体重");
            return false;
        }
        if(TextUtils.isEmpty(chosedate)&&hasaddTime.contains(selectdate)){
            showToastIgnoreLife("存在同日期记录，请选择其他日期！");
            return false;
        }

        return true;
    }
    public void chooseButtonOK(View view) {

        if(checkIllegal()){
            if(TextUtils.isEmpty(recordId)){
                toolsGrowPresenter.updateGrow(new SimpleHashMapBuilder<String, Object>()
                        .puts("childId", childId)
                        .puts(Functions.FUNCTION, "9038")
                        .puts("recordDate", selectdate)
                        .puts("height", height.getText().toString())
                        .puts("weight", weight.getText().toString()));
            }else {
                toolsGrowPresenter.updateGrow(new SimpleHashMapBuilder<String, Object>()
                        .puts("childId", childId)
                        .puts(Functions.FUNCTION, "9039")
                        .puts("recordId",recordId)
                        .puts("recordDate", selectdate)
                        .puts("height", height.getText().toString())
                        .puts("weight", weight.getText().toString()));
            }
        }

    }

    private void showTimePick() {
        if (mDateDialog == null) {
            long currentMill = System.currentTimeMillis();
            long duration = 0;
            try {
                duration = new SimpleDateFormat("yyyy-MM-dd").parse(birthday).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            duration = duration;
            mDateDialog = DateDialog.newInstance(
                    currentMill, duration, currentMill, ""
            );
            mDateDialog.setOnConfirmClick(new OnDateConfirmListener() {
                @Override
                public void onConfirm(int pos, Date date) {
                    selectdate=new SimpleDateFormat("yyyy-MM-dd").format(date);
                    if(DateUtils.withToday(selectdate,"yyyy-MM-dd")==0){//说明是今天

                        timeChoseTime.setText("今天");
                    }else if(DateUtils.withToday(selectdate,"yyyy-MM-dd")==-1){//说明是昨天
                        timeChoseTime.setText("昨天");
                    }else {
                        timeChoseTime.setText(new SimpleDateFormat("yyyy-MM-dd").format(date));
                    }
                    try {
                        timeBabyDate.setText(DateUtils.getAgeWithMonth(new SimpleDateFormat("yyyy-MM-dd").parse(birthday),date));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        if (mDateDialog.isAdded()) {
            mDateDialog.dismiss();
        } else {
            mDateDialog.show(getSupportFragmentManager(), "timePick");
        }

    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.top_bar);
        choseTimeLL = (ConstraintLayout) findViewById(R.id.choseTimeLL);
        timeChoseTime = (ImageTextView) findViewById(R.id.timeChoseTime);
        timeBabyDate = (TextView) findViewById(R.id.timeBabyDate);
        shengaoLL = (CommonInsertSection) findViewById(R.id.shengaoLL);
        tizhongLL = (CommonInsertSection) findViewById(R.id.tizhongLL);

        height=shengaoLL.findViewById(R.id.endTxt);
        weight=tizhongLL.findViewById(R.id.endTxt);

        heightImg=shengaoLL.findViewById(R.id.endImg);
        weightImg=tizhongLL.findViewById(R.id.endImg);


        height.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        weight.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
    }

    @Override
    public void onSucessGetAllStatus(List<UserInfoExModel> userInfoExModels) {

    }

    @Override
    public void onSucessGetNowGrow(List<ToolsGrow> toolsGrows) {

    }

    @Override
    public void onSucessGetNowBaseGrow(List<ToolsGrowBase> toolsGrowBases) {

    }

    @Override
    public void onSucessAddGrow() {
        finish();
    }

    @Override
    public void onsucessGetMine(UserInfoMonModel userInfoMonModel) {

    }
}
