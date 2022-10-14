package com.health.index.fragment;

import android.content.Context;
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
import com.health.index.adapter.ToolsFoodListAdapter;
import com.health.index.adapter.ToolsFoodTitleAdapter;
import com.health.index.adapter.ToolsFoodTopAdapter;
import com.health.index.contract.ToolsModContract;
import com.healthy.library.model.ToolsFoodList;
import com.healthy.library.model.ToolsFoodTop;
import com.healthy.library.model.UserInfoMonModel;
import com.health.index.presenter.ToolsModPresenter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.constant.Functions;
import com.healthy.library.message.FoodDialogInfoMsg;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.widget.StatusLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ToolsFoodRecipeFragment extends BaseFragment implements ToolsModContract.View, OnRefreshLoadMoreListener {

    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;


    private ToolsFoodTopAdapter toolsFoodTopAdapter;
    private ToolsFoodTitleAdapter toolsFoodTitleAdapter;
    private ToolsFoodListAdapter toolsFoodListAdapter;
    private SmartRefreshLayout refreshLayout;
    private StatusLayout layoutStatus;
    private RecyclerView recyclerQuestion;

    ToolsModPresenter toolsModPresenter;
    public int page=1;

    public String getTopSuggest(){
        return toolsFoodTopAdapter.getSug();
    }


    public static ToolsFoodRecipeFragment newInstance(Map<String, Object> maporg) {
        ToolsFoodRecipeFragment fragment = new ToolsFoodRecipeFragment();
        Bundle args = new Bundle();
        BaseFragment.bundleMap(maporg, args);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.index_fragment_tools_food_main;
    }

    @Override
    protected void findViews() {
        initView();
        toolsModPresenter=new ToolsModPresenter(mActivity,this);
        buildRecyclerView();
        refreshLayout.setOnRefreshLoadMoreListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private void buildRecyclerView() {
        virtualLayoutManager = new VirtualLayoutManager(mContext);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        recyclerQuestion.setLayoutManager(virtualLayoutManager);
        recyclerQuestion.setAdapter(delegateAdapter);

        toolsFoodTopAdapter = new ToolsFoodTopAdapter();
        toolsFoodTopAdapter.setRecipeType(get("recipeType").toString());
        toolsFoodTopAdapter.setRecipeDate(get("recipeDateString").toString());
        toolsFoodTopAdapter.setRecipeDateStringShare(get("recipeDateStringShare").toString());
        delegateAdapter.addAdapter(toolsFoodTopAdapter);

        toolsFoodTitleAdapter = new ToolsFoodTitleAdapter();
        toolsFoodTitleAdapter.setRecipeType(get("recipeType").toString());
        delegateAdapter.addAdapter(toolsFoodTitleAdapter);

        toolsFoodListAdapter = new ToolsFoodListAdapter();
        delegateAdapter.addAdapter(toolsFoodListAdapter);
        toolsFoodListAdapter.setRecipeType(get("recipeType").toString());


    }

    @Override
    public void getData() {
        super.getData();
        toolsModPresenter.getTop(new SimpleHashMapBuilder<String, Object>()
                .puts(Functions.FUNCTION,"8088")
                .puts("recipeType",get("recipeType").toString())
                .puts("address",get("address").toString())
                .puts("recipeDate",get("recipeDate").toString())
                .puts("foods",get("foods").toString())
                .puts("effect",get("effect").toString())
                .puts("currentPage",page+"")
                .puts("pageSize",10+"")


        );
    }

    private void initView() {

        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refresh_layout);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        recyclerQuestion = (RecyclerView) findViewById(R.id.recycler_question);
    }

    @Override
    public void onSucessgetType(String result) {

    }

    @Override
    public void onSucessgetTop(String result) {
        ToolsFoodTop toolsFoodTop=resolveListTop(result);
        toolsFoodTopAdapter.setModel(toolsFoodTop);
        toolsFoodTitleAdapter.setModel("食谱推荐 功效");
        onSucessgetList(resolveList(result),resolveRefreshData(result));
    }

    private void onSucessgetList(ArrayList<ToolsFoodList> resolveList, PageInfoEarly pageInfoEarly) {
        showContent();
        if (pageInfoEarly == null) {
            resolveList=new ArrayList<>();
            resolveList.add(null);
            toolsFoodListAdapter.setData(resolveList);
            refreshLayout.setEnableLoadMore(false);
            return;
        }
        page = pageInfoEarly.currentPage;
        if (page == 1) {
            if (resolveList.size() == 0) {
                resolveList=new ArrayList<>();
                resolveList.add(null);
                toolsFoodListAdapter.setData(resolveList);
            }else {

                toolsFoodListAdapter.setData(resolveList);
            }
        } else {
            toolsFoodListAdapter.addDatas(resolveList);
        }
        if (pageInfoEarly.isMore == 0) {

            refreshLayout.finishLoadMoreWithNoMoreData();
        } else {
            refreshLayout.setNoMoreData(false);
            refreshLayout.setEnableLoadMore(true);
        }
    }

    private ArrayList<ToolsFoodList> resolveList(String obj) {
        ArrayList<ToolsFoodList> result = new ArrayList<>();
        try {
            JSONArray data = new JSONObject(obj).getJSONObject("data").getJSONObject("recipeInfoByConditionsPageBean").getJSONArray("items");
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<ToolsFoodList>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    private PageInfoEarly resolveRefreshData(String obj) {
        PageInfoEarly result = new PageInfoEarly();
        try {
            JSONObject data = new JSONObject(obj).getJSONObject("data").getJSONObject("recipeInfoByConditionsPageBean");
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
    private ToolsFoodTop resolveListTop(String obj) {
        ToolsFoodTop result = null;
        try {
            JSONObject data = new JSONObject(obj).getJSONObject("data").getJSONObject("eatSuggestInfoBySuggestTypeAndFitPeriod");
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<ToolsFoodTop>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public void onSucessgetMine(String result) {

    }

    @Override
    public void onSucessgetCenter(String result) {

    }

    @Override
    public void onSucessgetBottom(String result) {

    }

    @Override
    public void onsucessGetMine(UserInfoMonModel userInfoMonModel) {

    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        getData();

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page=1;
        getData();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateDateInfo(FoodDialogInfoMsg msg) {
        //System.out.println("获得参数");
        if(isfragmenthow){
            //System.out.println("刷新参数");
            getBasemap().put("effect",msg.effectId);
            getBasemap().put("foods",msg.foodId);
            toolsFoodTitleAdapter.setFoodid(msg.food);
            toolsFoodTitleAdapter.setEffectid(msg.effect);
            toolsFoodTitleAdapter.notifyDataSetChanged();
            onRefresh(null);
        }

    }
}
