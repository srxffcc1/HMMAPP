package com.health.index.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.index.R;
import com.health.index.adapter.ToolVaccAdapter;
import com.health.index.contract.ToolsModContract;
import com.healthy.library.model.ToolsVacc;
import com.health.index.model.UserInfoExModel;
import com.healthy.library.model.UserInfoMonModel;
import com.health.index.presenter.ToolsModPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.interfaces.OnSubmitListener;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.SpanUtils;
import com.healthy.library.widget.ImageTextView;
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

@Route(path = IndexRoutes.INDEX_TOOLS_VACC_CHECK)
public class ToolsVaccCheckActivity extends BaseActivity implements IsFitsSystemWindows, ToolsModContract.View, OnRefreshLoadMoreListener, ToolVaccAdapter.OnCheckClickListener {


    ToolsModPresenter toolsModPresenter;
    private com.healthy.library.widget.TopBar topBar;
    private com.scwang.smart.refresh.layout.SmartRefreshLayout refreshLayout;
    private androidx.recyclerview.widget.RecyclerView recycler;

    ToolVaccAdapter toolDivAdapter;
    private int vaccintAgeId;
    private int differenceBetweenOfDays;
    private com.healthy.library.widget.ImageTextView leftDay;
    @Autowired
    int status;


    @Override
    protected int getLayoutId() {
        return R.layout.index_activity_tools_vacc;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        toolsModPresenter=new ToolsModPresenter(mActivity,this);

        refreshLayout.setOnRefreshLoadMoreListener(this);
        refreshLayout.setEnableLoadMore(false);
        recycler.setLayoutManager(new LinearLayoutManager(mContext));

        toolDivAdapter=new ToolVaccAdapter();
        toolDivAdapter.bindToRecyclerView(recycler);
        toolDivAdapter.setOnCheckClickListener(this);
        topBar.setSubmitListener(new OnSubmitListener() {
            @Override
            public void onSubmitBtnPressed() {
                ARouter.getInstance()
                        .build(IndexRoutes.INDEX_TOOLS_VACC_CHECK_TIP)
                        .navigation();
            }
        });

    }



    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    @Override
    public void getData() {
        super.getData();
        UserInfoExModel userInfoExModel=resolveTmpData(SpUtils.getValue(mContext, SpKey.USE_TOKEN));
        toolDivAdapter.setStatus(userInfoExModel.stageStatus);
        status=userInfoExModel.stageStatus;
        toolsModPresenter.getTop(new SimpleHashMapBuilder<String, Object>().puts(Functions.FUNCTION,"4036").puts("id",userInfoExModel.id+""));
    }
    @Override
    public void onSucessgetType(String result) {

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




    private ArrayList<ToolsVacc> resolveList(String obj) {
        ArrayList<ToolsVacc> result = new ArrayList<>();
        try {
            JSONArray data=new JSONObject(obj).getJSONObject("data").getJSONArray("vaccineTimeResultDTOS");
            String userShopInfoDTOS=data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<ToolsVacc>>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    boolean isfirst=false;
    @Override
    public void onSucessgetTop(String result) {
        if(status!=3){
            leftDay.setVisibility(View.GONE);
        }
        try {
            JSONObject jsonObject=new JSONObject(result).getJSONObject("data");
            vaccintAgeId = jsonObject.optInt("vaccintAgeId");
            differenceBetweenOfDays = jsonObject.optInt("differenceBetweenOfDays");
            leftDay.setText(SpanUtils.getBuilder(mContext,"距离下次疫苗还有").setForegroundColor(Color.parseColor("#666666"))
                    .append(" "+differenceBetweenOfDays+" ").setForegroundColor(Color.parseColor("#FF6266"))
                    .append("天").setForegroundColor(Color.parseColor("#666666"))
                    .create()

            );
            if(differenceBetweenOfDays==0){
                leftDay.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        toolDivAdapter.setId(vaccintAgeId);

        toolDivAdapter.setNewData(resolveList(result));
        if(!isfirst){
            ArrayList<ToolsVacc> arrayList=resolveList(result);
            for (int i = 0; i <arrayList.size() ; i++) {
                if(vaccintAgeId==arrayList.get(i).vaccineAgeId){
                    LinearLayoutManager linearLayoutManager= (LinearLayoutManager) recycler.getLayoutManager();
                    linearLayoutManager.scrollToPositionWithOffset(i,0);
                }
            }
            isfirst=true;
        }



    }

    @Override
    public void onSucessgetMine(String result) {

    }

    @Override
    public void onSucessgetCenter(String result) {

    }

    @Override
    public void onSucessgetBottom(String result) {
        getData();
    }

    @Override
    public void onsucessGetMine(UserInfoMonModel userInfoMonModel) {

    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.top_bar);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refresh_layout);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        leftDay = (ImageTextView) findViewById(R.id.leftDay);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getData();
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        refreshLayout.finishLoadMore();
        refreshLayout.finishRefresh();
    }

    @Override
    public void onCheck(int flag, ToolsVacc.VaccineTimeResult vaccineTimeResult) {
        String vaccineOver=vaccineTimeResult.prenatalOver;
        //System.out.println("进来的数据:"+vaccineOver);
        if(TextUtils.isEmpty(vaccineOver)){
            vaccineOver="";
        }
        if(flag==0){//说明没勾选
            vaccineOver=vaccineOver+vaccineTimeResult.id+",";
        }else {//说明勾选了
            //System.out.println("勾选的数据:"+vaccineOver);
            String[] array=vaccineOver.split(",");
            vaccineOver="";
            for (int i = 0; i <array.length ; i++) {
                if(!TextUtils.isEmpty(array[i].trim())){
                    if(!array[i].trim().equals(vaccineTimeResult.id+"")){

                        vaccineOver=vaccineOver+array[i]+",";
                    }
                }
            }
            //System.out.println("重置的数据:"+vaccineOver);
//            vaccineOver=vaccineOver.replace(vaccineTimeResult.id+",","");
        }
        //System.out.println("提交的数据:"+vaccineOver);

        UserInfoExModel userInfoExModel=resolveTmpData(SpUtils.getValue(mContext, SpKey.USE_TOKEN));
        toolsModPresenter.getBottom(new SimpleHashMapBuilder<String, Object>()
                .puts(Functions.FUNCTION,"4038")
                .puts("vaccineOver",vaccineOver)
                .puts("id",userInfoExModel.id+"")
        );
    }
}
