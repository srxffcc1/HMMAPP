package com.health.index.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.index.R;
import com.health.index.adapter.ToolsDaySumListAdapter;
import com.health.index.contract.ToolsDiaryContract;
import com.healthy.library.model.ToolsDiaryFood;
import com.healthy.library.model.ToolsDiaryMK;
import com.healthy.library.model.ToolsDiaryOut;
import com.healthy.library.model.ToolsDiarySleep;
import com.healthy.library.model.ToolsSumType;
import com.health.index.model.UserInfoExModel;
import com.healthy.library.model.UserInfoMonModel;
import com.health.index.presenter.ToolsDiaryPresenter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.constant.Functions;
import com.healthy.library.dialog.DateLimitDialog;
import com.healthy.library.interfaces.OnDateLimitConfirmListener;
import com.healthy.library.message.UpdateDateInfoMsg;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.widget.ImageTextView;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ToolsFeedSumFragment extends ToolsFeedBaseFragment implements ToolsDiaryContract.View, OnRefreshLoadMoreListener {

    private LinearLayout topLL;
    private RadioButton radio1;
    private RadioButton radio2;
    private RadioButton radio3;
    private RadioButton radio4;
    private RadioButton radio5;
    private RecyclerView recycler;
    ToolsDaySumListAdapter toolsFeedTypeListAdapter;
    public Date selectDateUp;
    public Date selectDateDown;
    ToolsDiaryPresenter toolsDiaryPresenter;
    public int page;
    private StatusLayout layoutStatus;
    private SmartRefreshLayout layoutRefresh;
    public String searchType = "1";

    public DateLimitDialog dateLimitDialog;
    private ImageTextView selectHandTime;


    @Override
    protected int getLayoutId() {
        return R.layout.index_fragment_tools_diary_main_sum;
    }

    public static ToolsFeedSumFragment newInstance(Map<String, Object> maporg) {
        ToolsFeedSumFragment fragment = new ToolsFeedSumFragment();
        Bundle args = new Bundle();
        BaseFragment.bundleMap(maporg, args);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        page=1;
        getData();
    }

    @Override
    protected void findViews() {
        initView();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        selectDateUp = calendar.getTime();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 0);
        selectDateDown = calendar.getTime();
        toolsDiaryPresenter = new ToolsDiaryPresenter(getActivity(), this);
        buildRecyclerView();
        layoutRefresh.setOnRefreshLoadMoreListener(this);
        radio1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    searchType = "1";
                    getData();
                    EventBus.getDefault().post(new UpdateDateInfoMsg(1));
                }
            }
        });
        radio2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    searchType = "2";
                    getData();
                    EventBus.getDefault().post(new UpdateDateInfoMsg(2));
                }
            }
        });
        radio3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    searchType = "3";
                    getData();
                    EventBus.getDefault().post(new UpdateDateInfoMsg(3));
                }
            }
        });
        radio4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    searchType = "4";
                    getData();
                    EventBus.getDefault().post(new UpdateDateInfoMsg(4));
                }
            }
        });
        selectHandTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showLimitDialog();
            }
        });
        radio5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLimitDialog();
                EventBus.getDefault().post(new UpdateDateInfoMsg(5));

            }
        });
        radio5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectHandTime.setVisibility(View.VISIBLE);
                    searchType = "5";
//                    getData();
//                    EventBus.getDefault().post(new UpdateDateInfoMsg(5,selectDateUp,selectDateDown));
                    onSucessGetNowDiary(null, null);
                    if(!buttonView.isPressed()){
                        return;
                    }
//                    showLimitDialog();
                }else {
                    selectHandTime.setVisibility(View.GONE);
                }
            }
        });
        selectHandTime.setText(new SimpleDateFormat("yyyy/MM/dd").format(selectDateUp)+"-"+new SimpleDateFormat("yyyy/MM/dd").format(selectDateDown));

    }
    @Override
    public View getBottomView() {
        return null;
    }
    private void showLimitDialog() {
        if (dateLimitDialog == null) {
            long min = 0;
            try {
                min = new SimpleDateFormat("yyyy/MM/dd").parse("2019/01/01").getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long max = System.currentTimeMillis();
            dateLimitDialog = DateLimitDialog.newInstance(min, max);
            dateLimitDialog.setOnConfirmClick(new OnDateLimitConfirmListener() {
                @Override
                public void onConfirm(int pos, Date dateUp, Date dateDown) {
                    selectDateUp = dateUp;
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(dateDown);
                    calendar.set(Calendar.HOUR_OF_DAY, 23);
                    calendar.set(Calendar.MINUTE, 59);
                    calendar.set(Calendar.SECOND, 0);
                    selectDateDown = calendar.getTime();
                    //System.out.println(new SimpleDateFormat("yyyy/MM/dd").format(dateUp));
                    //System.out.println(new SimpleDateFormat("yyyy/MM/dd").format(dateDown));
                    selectHandTime.setText(new SimpleDateFormat("yyyy/MM/dd").format(selectDateUp)+"-"+new SimpleDateFormat("yyyy/MM/dd").format(selectDateDown));

                    EventBus.getDefault().post(new UpdateDateInfoMsg(5,selectDateUp,selectDateDown));
                    getData();
                }
            });
        }
        dateLimitDialog.show(getChildFragmentManager(), "bornDate");
    }

    @Override
    public void getData() {
        super.getData();
        if("5".equals(searchType)){
            toolsDiaryPresenter.getNowDiary(new SimpleHashMapBuilder<String, Object>()
                    .puts(Functions.FUNCTION, "9026")
                    .puts("childId", get("childId").toString())
                    .puts("type", get("type").toString())
                    .puts("pageNum", page + "")
                    .puts("pageSize", "10")
                    .puts("searchType", searchType)
                    .puts("beginDateTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(selectDateUp))
                    .puts("endDateTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(selectDateDown))

            );
        }else {
            toolsDiaryPresenter.getNowDiary(new SimpleHashMapBuilder<String, Object>()
                    .puts(Functions.FUNCTION, "9026")
                    .puts("childId", get("childId").toString())
                    .puts("type", get("type").toString())
                    .puts("pageNum", page + "")
                    .puts("pageSize", "10")
                    .puts("searchType", searchType)

            );
        }

    }

    private void buildRecyclerView() {
        toolsFeedTypeListAdapter = new ToolsDaySumListAdapter();
        recycler.setLayoutManager(new LinearLayoutManager(mContext));
        recycler.setAdapter(toolsFeedTypeListAdapter);
        try {
            toolsFeedTypeListAdapter.setChildId(get("childId").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
//        toolsFeedTypeListAdapter.setNewData(toolsMedTypes);
        toolsFeedTypeListAdapter.setOnRecordLongClickListener(new ToolsDaySumListAdapter.OnRecordLongClickListener() {
            @Override
            public void onRecordLongClick(Map<String, Object> clearmap) {
                toolsDiaryPresenter.updateDiary(clearmap);
            }
        });
    }

    private void initView() {
        topLL = (LinearLayout) findViewById(R.id.topLL);
        radio1 = (RadioButton) findViewById(R.id.radio1);
        radio2 = (RadioButton) findViewById(R.id.radio2);
        radio3 = (RadioButton) findViewById(R.id.radio3);
        radio4 = (RadioButton) findViewById(R.id.radio4);
        radio5 = (RadioButton) findViewById(R.id.radio5);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        selectHandTime = (ImageTextView) findViewById(R.id.selectHandTime);
    }

    @Override
    public void onSucessGetAllStatus(List<UserInfoExModel> userInfoExModels) {

    }

    @Override
    public void onSucessGetNowDiary(String result) {
        onSucessGetNowDiary(resolveStatusListData(result), resolveRefreshData(result));
    }

    private void onSucessGetNowDiary(List<ToolsSumType> resolveStatusListData, PageInfoEarly pageInfoEarly) {
        if (pageInfoEarly == null) {
            showEmpty();
            layoutRefresh.setEnableLoadMore(false);
            return;
        }
        page = pageInfoEarly.pageNum;
        if (page == 1) {
            toolsFeedTypeListAdapter.setNewData(resolveStatusListData);
            if (resolveStatusListData.size() == 0) {
                showEmpty();
            }
        } else {
            toolsFeedTypeListAdapter.addData(resolveStatusListData);
        }
        if (pageInfoEarly.nextPage == 0) {

            layoutRefresh.finishLoadMoreWithNoMoreData();
        } else {
            layoutRefresh.setNoMoreData(false);
            layoutRefresh.setEnableLoadMore(true);
        }
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

    private List<ToolsSumType> resolveStatusListData(String obj) {
        List<ToolsSumType> result = new ArrayList<>();
        try {
            JSONArray data = new JSONObject(obj).getJSONObject("data").getJSONArray("list");

            for (int i = 0; i < data.length(); i++) {
                GsonBuilder builder = new GsonBuilder();
                builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        return new Date(json.getAsJsonPrimitive().getAsLong());
                    }
                });
                Type type = null;
                Gson gson = builder.create();
                JSONObject jsonObject = data.getJSONObject(i);
                String userShopInfoDTOS = jsonObject.toString();
                if ("1".equals(jsonObject.optString("recordType")) || "2".equals(jsonObject.optString("recordType"))) {
                    type = new TypeToken<ToolsDiaryMK>() {
                    }.getType();
                    ToolsSumType resultTmp = gson.fromJson(userShopInfoDTOS, type);
                    result.add(resultTmp);
                }
                if ("3".equals(jsonObject.optString("recordType")) || "4".equals(jsonObject.optString("recordType"))) {
                    type = new TypeToken<ToolsDiaryFood>() {
                    }.getType();
                    ToolsSumType resultTmp = gson.fromJson(userShopInfoDTOS, type);
                    result.add(resultTmp);
                }
                if ("5".equals(jsonObject.optString("recordType")) || "6".equals(jsonObject.optString("recordType"))) {
                    type = new TypeToken<ToolsDiaryOut>() {
                    }.getType();
                    ToolsSumType resultTmp = gson.fromJson(userShopInfoDTOS, type);
                    result.add(resultTmp);
                }
                if ("7".equals(jsonObject.optString("recordType"))) {
                    type = new TypeToken<ToolsDiarySleep>() {
                    }.getType();
                    ToolsSumType resultTmp = gson.fromJson(userShopInfoDTOS, type);
                    result.add(resultTmp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    @Override
    public void onSucessUpdateDiary() {
        page=1;
        getData();
    }

    @Override
    public void onsucessGetMine(UserInfoMonModel userInfoMonModel) {

    }

    @Override
    public void updateGrow() {

    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        getData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = 1;
        getData();
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        layoutRefresh.finishLoadMore();
        layoutRefresh.finishRefresh();
    }

    @Override
    protected void onCreate() {
        super.onCreate();
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
    public void updateDateInfo(UpdateDateInfoMsg msg) {
        if(!isfragmenthow){
            //System.out.println("判断修改状态");
            if(msg.postion==1){
                radio1.setChecked(true);
                page=1;
                getData();
            }
            if(msg.postion==2){
                radio2.setChecked(true);
                page=1;
                getData();
            }
            if(msg.postion==3){
                radio3.setChecked(true);
                page=1;
                getData();
            }
            if(msg.postion==4){
                radio4.setChecked(true);
                page=1;
                getData();
            }
            if(msg.postion==5){
                radio5.setChecked(true);
                if(msg.start!=null){
                    selectDateUp=msg.start;
                    selectDateDown=msg.end;
                    selectHandTime.setText(new SimpleDateFormat("yyyy/MM/dd").format(selectDateUp)+"-"+new SimpleDateFormat("yyyy/MM/dd").format(selectDateDown));

                }
                page=1;
                getData();
            }
        }

    }
}
