package com.health.index.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.index.R;
import com.health.index.adapter.ToolsEatTypeListListAdapter;
import com.health.index.contract.ToolsModContract;
import com.healthy.library.model.ToolsCETypeList;
import com.healthy.library.model.UserInfoMonModel;
import com.health.index.presenter.ToolsModPresenter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.constant.Functions;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.widget.StatusLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ToolsFoodCanEatTypeFragment extends BaseFragment implements ToolsModContract.View , OnRefreshLoadMoreListener {


    private SmartRefreshLayout refreshLayout;
    private StatusLayout layoutStatus;
    private RecyclerView recyclerQuestion;
    ToolsModPresenter toolsModPresenter;
    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;
    public int page=1;
    ToolsEatTypeListListAdapter toolsEatTypeListListAdapter;
    public static ToolsFoodCanEatTypeFragment newInstance(Map<String, Object> maporg) {
        ToolsFoodCanEatTypeFragment fragment = new ToolsFoodCanEatTypeFragment();
        Bundle args = new Bundle();
        BaseFragment.bundleMap(maporg, args);
        fragment.setArguments(args);
        return fragment;
    }

    public void seachKey(String keyWords){
            //System.out.println("当前开始搜索");
        if(toolsModPresenter==null){
            return;
        }
            page=1;
            getBasemap().put("keyWords",keyWords);
            getData();

    }



    @Override
    public void onSucessgetType(String result) {

    }

    @Override
    public void onSucessgetTop(String result) {

    }

    @Override
    public void onSucessgetMine(String result) {

    }

    @Override
    public void onSucessgetCenter(String result) {

    }

    @Override
    public void onSucessgetBottom(String result) {
        onSucessgetBottom(resolveList(result),resolveRefreshData(result));

    }

    @Override
    public void onsucessGetMine(UserInfoMonModel userInfoMonModel) {

    }

    private void onSucessgetBottom(ArrayList<ToolsCETypeList> resolveList, PageInfoEarly pageInfoEarly) {
        showContent();
        if (pageInfoEarly == null) {
            showEmpty();
            refreshLayout.setEnableLoadMore(false);
            return;
        }
        page = pageInfoEarly.currentPage;
        if (page == 1) {
            toolsEatTypeListListAdapter.setData(resolveList);
            if (resolveList.size() == 0) {
                showEmpty();
            }
        } else {
            toolsEatTypeListListAdapter.addDatas(resolveList);
        }
        if (pageInfoEarly.isMore == 0) {

            refreshLayout.finishLoadMoreWithNoMoreData();
        } else {
            refreshLayout.setNoMoreData(false);
            refreshLayout.setEnableLoadMore(true);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.index_fragment_tools_caneat_main_type;
    }

    @Override
    protected void findViews() {
        initView();
        toolsModPresenter=new ToolsModPresenter(mActivity,this);
        refreshLayout.setOnRefreshLoadMoreListener(this);
        buildRecyclerView();
        getData();
    }
    private void buildRecyclerView() {
        virtualLayoutManager = new VirtualLayoutManager(mContext);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        recyclerQuestion.setLayoutManager(virtualLayoutManager);
        recyclerQuestion.setAdapter(delegateAdapter);

        toolsEatTypeListListAdapter=new ToolsEatTypeListListAdapter();
        delegateAdapter.addAdapter(toolsEatTypeListListAdapter);



    }
    @Override
    public void getData() {
        super.getData();
        if("0".equals(get("activityType"))){
            toolsModPresenter.getBottom(new SimpleHashMapBuilder<String, Object>()
                    .puts(Functions.FUNCTION,"4015")
                    .puts("pageSize","10")
                    .puts("currentPage",page+"")
                    .puts("foodType",get("foodType").toString()));
        }else if("1".equals(get("activityType"))){

            toolsModPresenter.getBottom(new SimpleHashMapBuilder<String, Object>()
                    .puts(Functions.FUNCTION,"8081")
                    .puts("pageSize","10")
                    .puts("currentPage",page+"")
                    .puts("eatStage",get("eatStage").toString())
                    .puts(get("eatStageKey").toString(),get("eatStageValue").toString())
                    .puts("foodType",get("foodType").toString()));
        }else {
            toolsModPresenter.getBottom(new SimpleHashMapBuilder<String, Object>()
                    .puts(Functions.FUNCTION,"8082")
                    .puts("pageSize","10")
                    .puts("currentPage",page+"")
                    .puts("keyWords",get("keyWords").toString()));
        }
    }

    private void initView() {
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refresh_layout);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        recyclerQuestion = (RecyclerView) findViewById(R.id.recycler_question);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

        page++;
        getData();
    }
    private PageInfoEarly resolveRefreshData(String obj) {
        PageInfoEarly result = new PageInfoEarly();
        try {
            JSONObject data = new JSONObject(obj).getJSONObject("data");
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<PageInfoEarly>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    private ArrayList<ToolsCETypeList> resolveList(String obj) {
        ArrayList<ToolsCETypeList> result = new ArrayList<>();
        try {
            JSONArray data=null;

            if("0".equals(get("activityType"))){
                data=new JSONObject(obj).getJSONArray("data");
            }else if("1".equals(get("activityType"))){
                data=new JSONObject(obj).getJSONObject("data").getJSONArray("items");
            }else {
                data=new JSONObject(obj).getJSONObject("data").getJSONArray("items");
            }
            String userShopInfoDTOS=data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<ToolsCETypeList>>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page=1;
        getData();
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        refreshLayout.finishLoadMore();
        refreshLayout.finishRefresh();
    }
}
