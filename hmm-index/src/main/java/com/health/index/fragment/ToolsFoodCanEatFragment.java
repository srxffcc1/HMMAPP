package com.health.index.fragment;

import android.os.Bundle;

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
import com.health.index.adapter.ToolsEatListAdapter;
import com.health.index.adapter.ToolsEatTitleAdapter;
import com.health.index.adapter.ToolsEatTopAdapter;
import com.health.index.contract.ToolsModContract;
import com.healthy.library.model.ToolsCEList;
import com.healthy.library.model.ToolsCETopMenu;
import com.healthy.library.model.UserInfoMonModel;
import com.health.index.presenter.ToolsModPresenter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.builder.SimpleArrayListBuilder;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.widget.StatusLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ToolsFoodCanEatFragment extends BaseFragment implements ToolsModContract.View {
    private StatusLayout layoutStatus;
    private RecyclerView recyclerQuestion;
    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;

    private ToolsEatTopAdapter toolsEatTopAdapter;
    private ToolsEatTitleAdapter toolsEatTitleAdapter;
    private ToolsEatListAdapter toolsEatListAdapter;
    ToolsModPresenter toolsModPresenter;

    public static ToolsFoodCanEatFragment newInstance(Map<String, Object> maporg) {
        ToolsFoodCanEatFragment fragment = new ToolsFoodCanEatFragment();
        Bundle args = new Bundle();
        BaseFragment.bundleMap(maporg, args);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.index_fragment_tools_caneat_main;
    }

    @Override
    protected void findViews() {
        initView();
        buildRecyclerView();
        toolsModPresenter = new ToolsModPresenter(mActivity, this);
    }

    @Override
    protected void onLazyData() {
        super.onLazyData();
        String address = LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE) + "-" + LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE) + "-" + LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE);
        toolsModPresenter.getTop(new SimpleHashMapBuilder<String, Object>()
                .puts(Functions.FUNCTION, "8080")
                .puts("eatStage", get("eatStage").toString())
                .puts("address", address)
        );
    }

    private void buildRecyclerView() {
        virtualLayoutManager = new VirtualLayoutManager(mContext);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        recyclerQuestion.setLayoutManager(virtualLayoutManager);
        recyclerQuestion.setAdapter(delegateAdapter);

        toolsEatTopAdapter = new ToolsEatTopAdapter();
        delegateAdapter.addAdapter(toolsEatTopAdapter);

        toolsEatTopAdapter.setData(new SimpleArrayListBuilder<ToolsCETopMenu>()
                .adds(new ToolsCETopMenu("R.drawable.tools_eat_big_bg","R.drawable.tools_eat_big","放心吃",get("eatStage").toString()))
                .adds(new ToolsCETopMenu("R.drawable.tools_eat_small_bg","R.drawable.tools_eat_small","少点吃",get("eatStage").toString()))
                .adds(new ToolsCETopMenu("R.drawable.tools_eat_normal_bg","R.drawable.tools_eat_normal","谨慎吃",get("eatStage").toString()))
                .adds(new ToolsCETopMenu("R.drawable.tools_eat_no_bg","R.drawable.tools_eat_no","不能吃",get("eatStage").toString()))
        );
        toolsEatTopAdapter.setEatStage(get("eatStage").toString());



        toolsEatTitleAdapter = new ToolsEatTitleAdapter();
        delegateAdapter.addAdapter(toolsEatTitleAdapter);

        toolsEatListAdapter = new ToolsEatListAdapter();
        delegateAdapter.addAdapter(toolsEatListAdapter);
        toolsEatListAdapter.setFragmentType(get("eatStage").toString());

    }

    private void initView() {
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        recyclerQuestion = (RecyclerView) findViewById(R.id.recycler_question);
    }

    @Override
    public void onSucessgetType(String result) {

    }

    @Override
    public void onSucessgetTop(String result) {
        toolsEatTitleAdapter.setModel("热门食物");
        toolsEatListAdapter.setData(resolveListTop(result));
    }
    private ArrayList<ToolsCEList> resolveListTop(String obj) {
        ArrayList<ToolsCEList> result = new ArrayList<>();
        try {
            JSONArray data=new JSONObject(obj).getJSONArray("data");
            String userShopInfoDTOS=data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<ToolsCEList>>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
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
}
