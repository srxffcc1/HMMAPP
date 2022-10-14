package com.health.index.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.index.R;
import com.health.index.contract.ToolsModContract;
import com.healthy.library.model.ToolsVaccDivTip;
import com.health.index.model.UserInfoExModel;
import com.healthy.library.model.UserInfoMonModel;
import com.health.index.presenter.ToolsModPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.dialog.DateTipDialog;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.interfaces.OnDateTipConfirmListener;
import com.healthy.library.interfaces.OnSubmitListener;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.CommonInsertSection;
import com.healthy.library.widget.SectionView;
import com.healthy.library.widget.TopBar;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Date;

@Route(path = IndexRoutes.INDEX_TOOLS_VACC_CHECK_TIP)
public class ToolsVaccCheckTipActivity extends BaseActivity implements IsFitsSystemWindows, ToolsModContract.View{
    private com.healthy.library.widget.TopBar topBar;
    private com.healthy.library.widget.CommonInsertSection tipCheckLL;
    private com.healthy.library.widget.SectionView sectionTip;
    Switch tipCheck;
    DateTipDialog dateTipDialog;
    ToolsModPresenter toolsModPresenter;
    String[] arrayTips=new String[]{"疫苗前三天","疫苗前一天","疫苗当天"};
    String[] resultarray=null;

    @Override
    protected int getLayoutId() {
        return R.layout.index_activity_tools_vacc_tip;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        toolsModPresenter=new ToolsModPresenter(mActivity,this);
        tipCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    sectionTip.setVisibility(View.VISIBLE);
//                }else {
//                    sectionTip.setVisibility(View.GONE);
//                }
            }
        });
        sectionTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTipDialog();
            }
        });
        toolsModPresenter.getType(new SimpleHashMapBuilder<String, Object>().puts(Functions.FUNCTION,"8090"));
        topBar.setSubmitListener(new OnSubmitListener() {
            @Override
            public void onSubmitBtnPressed() {
                save();
            }
        });
    }

    private UserInfoExModel resolveTmpData(String obj) {
        UserInfoExModel result = new UserInfoExModel();
        try {
            JSONObject data = new JSONObject(obj);
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<UserInfoExModel>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void save(){
        if(resultarray!=null){

            UserInfoExModel userInfoExModel=resolveTmpData(SpUtils.getValue(mContext, SpKey.USE_TOKEN));
            toolsModPresenter.getTop(new SimpleHashMapBuilder<String, Object>().puts(Functions.FUNCTION,"8084")
                    .puts("vaccineTime",resultarray[1]+":"+resultarray[2])
                    .puts("vaccineDay",resultarray[0]).puts("id",userInfoExModel.id+"")
            );
        }else {
            checkTip(tipCheck.isChecked());
        }



    }
    private void showTipDialog() {
        if (dateTipDialog == null) {

            dateTipDialog = DateTipDialog.newInstance(null,arrayTips);
            dateTipDialog.setOnConfirmClick(new OnDateTipConfirmListener() {
                @Override
                public void onConfirm(int pos, String[] dateDown) {
                    resultarray=dateDown;
                    sectionTip.setDes(arrayTips[(Integer.parseInt(resultarray[0]))-1]+""+resultarray[1]+":"+resultarray[2]);
                }
            });
        }
        dateTipDialog.show(getSupportFragmentManager(), "dateTipDialog");
    }

    private void checkTip(boolean isChecked) {
        toolsModPresenter.getBottom(new SimpleHashMapBuilder<String, Object>().puts(Functions.FUNCTION,"8091")
                .puts("remindVaccine",isChecked?"1":"0"));
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    @Override
    public void onSucessgetType(String result) {
        ToolsVaccDivTip toolsVaccDivTip=resolveTip(result);
        if(!TextUtils.isEmpty(toolsVaccDivTip.vaccineTime)){

            sectionTip.setDes(arrayTips[toolsVaccDivTip.vaccineDay-1]+""+toolsVaccDivTip.vaccineTime);
        }else {

            sectionTip.setDes("不提醒");
        }
        if(sectionTip.getTvDes().getText().toString().contains("不提醒")){
            sectionTip.setDes("不提醒");
        }
        if(toolsVaccDivTip.remindVaccine==0){

            tipCheck.setChecked(false);
        }
    }
    private ToolsVaccDivTip resolveTip(String obj) {
        ToolsVaccDivTip result = null;
        try {
            JSONObject data=new JSONObject(obj).getJSONObject("data");
            String userShopInfoDTOS=data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<ToolsVaccDivTip>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    @Override
    public void onSucessgetTop(String result) {
        checkTip(tipCheck.isChecked());
    }

    @Override
    public void onSucessgetMine(String result) {

    }

    @Override
    public void onSucessgetCenter(String result) {

    }

    @Override
    public void onSucessgetBottom(String result) {
        finish();
    }

    @Override
    public void onsucessGetMine(UserInfoMonModel userInfoMonModel) {

    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.top_bar);
        tipCheckLL = (CommonInsertSection) findViewById(R.id.tipCheckLL);
        sectionTip = (SectionView) findViewById(R.id.section_tip);
        tipCheck=tipCheckLL.findViewById(R.id.tipCheck);

    }
}
