package com.health.index.activity;

import android.os.Bundle;
import android.util.Base64;

import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.index.BuildConfig;
import com.health.index.R;
import com.health.index.adapter.ToolsEmptyAdapter;
import com.health.index.adapter.ToolsFunctionAdapter;
import com.health.index.adapter.ToolsTitleAdapter;
import com.health.index.contract.ToolsDiaryMainContract;
import com.healthy.library.model.ToolsMenu;
import com.health.index.model.UserInfoExModel;
import com.healthy.library.model.UserInfoMonModel;
import com.health.index.presenter.ToolsDiarySleepPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.constant.SpKey;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.routes.SoundRoutes;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.business.NoFucDialog;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Route(path = IndexRoutes.INDEX_TOOLS_TYPE)
public class ToolTypeActivity extends BaseActivity implements BaseAdapter.OnOutClickListener , ToolsDiaryMainContract.View{
    private com.healthy.library.widget.TopBar topBar;
    private com.healthy.library.widget.StatusLayout layoutStatus;
    private androidx.recyclerview.widget.RecyclerView recyclerNews;


    ToolsTitleAdapter toolsTitleAdapterHistory;
    ToolsTitleAdapter toolsTitleAdapterBaby;
    ToolsTitleAdapter toolsTitleAdapterNormal;
    ToolsFunctionAdapter toolsFunctionAdapterHistory;
    ToolsFunctionAdapter toolsFunctionAdapterBaby;
    ToolsFunctionAdapter toolsFunctionAdapterNormal;
    ToolsEmptyAdapter toolsEmptyAdapter;

    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;

    private ArrayList<ToolsMenu> toolsHistory = new ArrayList<>();
    private ArrayList<ToolsMenu> toolsBaby = new ArrayList<>();
    private ArrayList<ToolsMenu> toolsNormal = new ArrayList<>();
    ToolsDiarySleepPresenter toolsGrowPresenter;

//    int yuzitime=0;
//    int bbtime=0;
//    int yuntime=0;

    @Override
    protected int getLayoutId() {
        return R.layout.index_activity_tools_type;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        buildRecyclerView();
        topBar.setTitle("?????????");
        toolsGrowPresenter=new ToolsDiarySleepPresenter(this,this);
    }

    private void buildRecyclerView() {
        virtualLayoutManager = new VirtualLayoutManager(mContext);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        recyclerNews.setLayoutManager(virtualLayoutManager);
        recyclerNews.setAdapter(delegateAdapter);


        toolsTitleAdapterHistory = new ToolsTitleAdapter();
        delegateAdapter.addAdapter(toolsTitleAdapterHistory);

        toolsEmptyAdapter = new ToolsEmptyAdapter();
        delegateAdapter.addAdapter(toolsEmptyAdapter);

        toolsFunctionAdapterHistory = new ToolsFunctionAdapter();
        toolsFunctionAdapterHistory.setOutClickListener(this);
        delegateAdapter.addAdapter(toolsFunctionAdapterHistory);


        toolsTitleAdapterBaby = new ToolsTitleAdapter();
        delegateAdapter.addAdapter(toolsTitleAdapterBaby);

        toolsFunctionAdapterBaby = new ToolsFunctionAdapter();
        toolsFunctionAdapterBaby.setOutClickListener(this);
        delegateAdapter.addAdapter(toolsFunctionAdapterBaby);


        toolsTitleAdapterNormal = new ToolsTitleAdapter();
        delegateAdapter.addAdapter(toolsTitleAdapterNormal);
        toolsFunctionAdapterNormal = new ToolsFunctionAdapter();
        toolsFunctionAdapterNormal.setOutClickListener(this);
        delegateAdapter.addAdapter(toolsFunctionAdapterNormal);


    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public void getData() {
        super.getData();
        toolsGrowPresenter.getNowStatus();

    }
    public void buildView(boolean isHasBaby){
        if(isHasBaby){//??????

            toolsTitleAdapterBaby.setModel("????????????");
            toolsBaby.clear();
            toolsBaby.add(new ToolsMenu("????????????","R.drawable.tools_yebk"));
            toolsBaby.add(new ToolsMenu("????????????","R.drawable.tools_wyjl"));
            toolsBaby.add(new ToolsMenu("????????????","R.drawable.tools_czfy"));
            toolsBaby.add(new ToolsMenu("?????????","R.drawable.tools_yzc"));
            toolsBaby.add(new ToolsMenu("????????????","R.drawable.tools_ymzs"));
            toolsBaby.add(new ToolsMenu("????????????","R.drawable.tools_nbnc"));
            toolsBaby.add(new ToolsMenu("????????????","R.drawable.tools_bbfs"));
            if(!SpUtils.getValue(mContext,SpKey.AUDITSTATUS,true)){
                toolsBaby.add(new ToolsMenu("????????????","R.drawable.tools_ekzj"));
                toolsBaby.add(new ToolsMenu("????????????","R.drawable.tools_bzjx"));
            }

           // toolsBaby.add(new ToolsMenu("????????????","R.drawable.tools_thgs"));
            //toolsBaby.add(new ToolsMenu("????????????","R.drawable.tools_zjeg"));
            if(!SpUtils.getValue(mContext,SpKey.AUDITSTATUS,true)) {
                toolsBaby.add(new ToolsMenu("????????????", "R.drawable.tools_bbqm"));
                toolsBaby.add(new ToolsMenu("????????????", "R.drawable.tools_bbjm"));
            }

            toolsBaby.add(new ToolsMenu("????????????","R.drawable.tools_chyy"));
            toolsFunctionAdapterBaby.setData(toolsBaby);


            toolsTitleAdapterNormal.setModel("????????????");
            toolsNormal.clear();
            toolsNormal.add(new ToolsMenu("????????????","R.drawable.tools_yybk"));
            toolsNormal.add(new ToolsMenu("????????????","R.drawable.tools_cjb"));
            toolsNormal.add(new ToolsMenu("B?????????","R.drawable.tools_bcjdd"));
            toolsNormal.add(new ToolsMenu("????????????","R.drawable.tools_nbnc"));

            if(BuildConfig.VERSION_CODE>=2208){

                toolsNormal.add(new ToolsMenu("????????????","R.drawable.tools_yqsp"));
            }
            toolsNormal.add(new ToolsMenu("?????????","R.drawable.tools_dcb"));
            toolsNormal.add(new ToolsMenu("????????????","R.drawable.tools_snsn"));
            toolsNormal.add(new ToolsMenu("????????????","R.drawable.tools_tegz"));
            if(!SpUtils.getValue(mContext,SpKey.AUDITSTATUS,true)){
                toolsNormal.add(new ToolsMenu("????????????","R.drawable.tools_xlzx"));
            }
            //toolsNormal.add(new ToolsMenu("????????????","R.drawable.tools_tjyy"));
            if(!SpUtils.getValue(mContext,SpKey.AUDITSTATUS,true)){
                toolsNormal.add(new ToolsMenu("????????????","R.drawable.tools_zxmy"));
            }
            toolsFunctionAdapterNormal.setData(toolsNormal);

            showHistory();
        }else {//??????


            toolsTitleAdapterBaby.setModel("????????????");
            toolsBaby.clear();
            toolsBaby.add(new ToolsMenu("????????????","R.drawable.tools_yybk"));
            toolsBaby.add(new ToolsMenu("????????????","R.drawable.tools_cjb"));
            toolsBaby.add(new ToolsMenu("B?????????","R.drawable.tools_bcjdd"));
            toolsBaby.add(new ToolsMenu("????????????","R.drawable.tools_nbnc"));

            if(BuildConfig.VERSION_CODE>=2208){

                toolsBaby.add(new ToolsMenu("????????????","R.drawable.tools_yqsp"));
            }
            toolsBaby.add(new ToolsMenu("?????????","R.drawable.tools_dcb"));
            toolsBaby.add(new ToolsMenu("????????????","R.drawable.tools_snsn"));
            toolsBaby.add(new ToolsMenu("????????????","R.drawable.tools_tegz"));
            if(!SpUtils.getValue(mContext,SpKey.AUDITSTATUS,true)){
                toolsBaby.add(new ToolsMenu("????????????","R.drawable.tools_xlzx"));
            }
            //toolsBaby.add(new ToolsMenu("????????????","R.drawable.tools_tjyy"));
            if(!SpUtils.getValue(mContext,SpKey.AUDITSTATUS,true)){
                toolsBaby.add(new ToolsMenu("????????????","R.drawable.tools_zxmy"));
            }
            toolsFunctionAdapterBaby.setData(toolsBaby);


            toolsTitleAdapterNormal.setModel("????????????");
            toolsNormal.clear();
            toolsNormal.add(new ToolsMenu("????????????", "R.drawable.tools_yebk"));
            toolsNormal.add(new ToolsMenu("????????????","R.drawable.tools_wyjl"));
            toolsNormal.add(new ToolsMenu("????????????","R.drawable.tools_czfy"));
            toolsNormal.add(new ToolsMenu("?????????","R.drawable.tools_yzc"));
            toolsNormal.add(new ToolsMenu("????????????","R.drawable.tools_ymzs"));
            toolsNormal.add(new ToolsMenu("????????????","R.drawable.tools_nbnc"));
            toolsNormal.add(new ToolsMenu("????????????","R.drawable.tools_bbfs"));
            if(!SpUtils.getValue(mContext,SpKey.AUDITSTATUS,true)){
                toolsNormal.add(new ToolsMenu("????????????","R.drawable.tools_ekzj"));
            }

            if(!SpUtils.getValue(mContext,SpKey.AUDITSTATUS,true)) {
                toolsNormal.add(new ToolsMenu("????????????", "R.drawable.tools_bzjx"));
            }
            //toolsNormal.add(new ToolsMenu("????????????","R.drawable.tools_thgs"));
            //toolsNormal.add(new ToolsMenu("????????????","R.drawable.tools_zjeg"));

            if(!SpUtils.getValue(mContext,SpKey.AUDITSTATUS,true)) {
                toolsNormal.add(new ToolsMenu("????????????", "R.drawable.tools_bbqm"));
                toolsNormal.add(new ToolsMenu("????????????", "R.drawable.tools_bbjm"));
            }


            toolsNormal.add(new ToolsMenu("????????????","R.drawable.tools_chyy"));
            toolsFunctionAdapterNormal.setData(toolsNormal);

            showHistory();



        }

    }

    private void showHistory() {
        toolsHistory.clear();
        toolsHistory.addAll(resolveHistoryData(SpUtils.getValue(mContext, SpKey.TOOL_TMP)));
        for (int i = 0; i <toolsHistory.size() ; i++) {
            if(SpUtils.getValue(mContext,SpKey.AUDITSTATUS,true)&&
                    ("????????????".equals(toolsHistory.get(i).name)||
                            "????????????".equals(toolsHistory.get(i).name)||
                            "????????????".equals(toolsHistory.get(i).name)||
                            "????????????".equals(toolsHistory.get(i).name)||
                            "????????????".equals(toolsHistory.get(i).name))) {
                toolsHistory.remove(i);
                i--;
            }
        }
        if (toolsHistory.size() > 0) {
            toolsEmptyAdapter.setModel(null);
            toolsTitleAdapterHistory.setModel("????????????");
            toolsFunctionAdapterHistory.setData(toolsHistory);
        } else {
            toolsEmptyAdapter.setModel("????????????");
            toolsTitleAdapterHistory.setModel("????????????");
            toolsFunctionAdapterHistory.setData(toolsHistory);
        }
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    private List<ToolsMenu> resolveHistoryData(String obj) {
        List<ToolsMenu> result = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(obj);
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<ToolsMenu>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.top_bar);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        recyclerNews = (RecyclerView) findViewById(R.id.recycler_news);
    }

    public void saveFunction(ToolsMenu item){
        List<ToolsMenu> tmp=resolveHistoryData(SpUtils.getValue(mContext, SpKey.TOOL_TMP));
        if(!checkIsInSave(tmp,item)){
            tmp.add(0,item);
        }
        if(tmp.size()>5){
            tmp.remove(tmp.size()-1);
        }
        SpUtils.store(mContext,SpKey.TOOL_TMP,new Gson().toJson(tmp));
    }

    private boolean checkIsInSave(List<ToolsMenu> tmp, ToolsMenu item) {
        for (int i = 0; i <tmp.size() ; i++) {
            if(tmp.get(i).name.equals(item.name)){
                return true;
            }
        }
        return false;
    }



    @Override
    public void outClick(@NotNull String function, @NotNull Object obj) {
        if(passTool(function)){
            saveFunction((ToolsMenu) obj);
        }
    }

    private boolean passTool(String name) {
        if(BuildConfig.VERSION_CODE>=2205){
            if("????????????".equals(name)){
                ARouter.getInstance()
                        .build(IndexRoutes.INDEX_TOOLS_NAME)
                        .navigation();
                return true;
            }
            if("????????????".equals(name)){
                ARouter.getInstance()
                        .build(IndexRoutes.INDEX_WEBVIEW_ALL)
                        .withString("title", name)
                        .withBoolean("isinhome",false)
                        .withBoolean("doctorshop",true)
                        .withString("url", "http://zx.fengxinz100.cn/xingmingxiangpi/index?channel=swhmm000")
                        .navigation();
                return true;
            }
            if("????????????".equals(name)){
                ARouter.getInstance()
                        .build(IndexRoutes.INDEX_WEBVIEW_ALL)
                        .withString("title", name)
                        .withBoolean("isinhome",false)
                        .withBoolean("doctorshop",true)
                        .withString("url", "http://zx.fengxinz100.cn/baobaojingpi/index?channel=swhmm000")
                        .navigation();
                return true;
            }
            if("????????????".equals(name)){
                ARouter.getInstance()
                        .build(IndexRoutes.INDEX_WEBVIEW_ALL)
                        .withString("title", name)
                        .withBoolean("isinhome",false)
                        .withBoolean("doctorshop",true)
                        .withString("url", "http://psychology.tengzhiff.com/detail/chan_hou_yi_yu?channel=swhmm000")
                        .navigation();
                return true;
            }
        }
        if ("????????????".equals(name)) {
            String url="https://h5.yewyw.com/index.html?bmark=hmama&appid=977&flag=app&appUserId=%s&appUserPhone=18511557625#/MentalityList01?dietitianGoodLabelType=7";
            url = String.format(url,
                    new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)));
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_WEBVIEW)
                    .withString("title", "????????????")
                    .withBoolean("doctorshop",true)
                    .withBoolean("isinhome",false)
                    .withString("url", url)
                    .navigation();
            return true;
        }
        if ("????????????".equals(name)) {
            String url="https://h5.yewyw.com/index.html?bmark=hmama&appid=977&flag=app&appUserId=%s&appUserPhone=18511557625#/DoctorList?deptType=3&sortType=0&titleLv=0&labelStr=";
            url = String.format(url,
                    new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)));
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_WEBVIEW)
                    .withString("title", "????????????")
                    .withBoolean("doctorshop",true)
                    .withBoolean("isinhome",false)
                    .withString("url", url)
                    .navigation();
            return true;
        }
        if ("????????????".equals(name)||"????????????".equals(name)) {
            //System.out.println(SpUtils.getValue(mContext, SpKey.USER_ID));
            String url="https://h5.yewyw.com/index.html?bmark=hmama&appid=977&flag=app&appUserId=%s&appUserPhone=18511557625#/ConsultQuick";
            url = String.format(url,
                    new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)));
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_WEBVIEW)
                    .withString("title", "????????????")
                    .withBoolean("isinhome",false)
                    .withBoolean("doctorshop",true)
                    .withString("url", url)
                    .navigation();
            return true;
        }
        if ("????????????".equals(name)) {
            ARouter.getInstance()
                    .build(SoundRoutes.SOUND_MAIN)
                    .withString("audioType","1")
                    .withString("choseTypeName","??????")
                    .navigation();
            return true;
        }
        if ("????????????".equals(name)) {
            ARouter.getInstance()
                    .build(SoundRoutes.SOUND_MAIN)
                    .withString("audioType","1")
                    .withString("choseTypeName","??????")
                    .navigation();
            return true;
        }
        if ("????????????".equals(name)) {
            ARouter.getInstance()
                    .build(SoundRoutes.SOUND_MAIN)
                    .withString("audioType","1")
                    .withString("choseTypeName","??????")
                    .navigation();
            return true;
        }
        if ("????????????".equals(name)) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_TOOLS_BABY_DIARY)
                    .navigation();
            return true;
        }
        if ("????????????".equals(name)) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_TOOLS_BABY_WEIGHT)
                    .navigation();
            return true;
        }
        if ("????????????".equals(name)) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_TOOLS_GROW)
                    .navigation();
            return true;
        }
        if ("????????????".equals(name)) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_TOOLS_TEST_SEX_MAIN)
                    .navigation();
            return true;
        }
        if ("????????????".equals(name)) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_TOOLS)
                    .withInt("currentIndex", 0)
                    .navigation();
            return true;
        }
        if ("????????????".equals(name)) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_TOOLS)
                    .withInt("currentIndex", 1)
                    .navigation();
            return true;
        }
        if ("????????????".equals(name)&&BuildConfig.VERSION_CODE>=2208) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_TOOLS_CANEAT)
                    .navigation();
            return true;
        }
        if ("????????????".equals(name)) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_WEBVIEW)
                    .withString("title", "????????????")
                    .withString("url", SpUtils.getValue(mContext, UrlKeys.H5_CAN_EAT_ALL))
                    .navigation();
            return true;
        }
        if ("????????????".equals(name)&&BuildConfig.VERSION_CODE>=2208) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_TOOLS_FOOD)
                    .withString("activityType","????????????")
                    .navigation();
            return true;
        }
        if ("????????????".equals(name)) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_WEBVIEW)
                    .withString("title", "????????????")
                    .withString("url", SpUtils.getValue(mContext, UrlKeys.H5_FOOD_LIST))
                    .navigation();
            return true;
        }
        if ("????????????".equals(name)&&BuildConfig.VERSION_CODE>=2208) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_TOOLS_DIV_CHECK)
                    .navigation();
            return true;
        }
        if ("????????????".equals(name)) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_BIRTH_CHECK_LIST)
                    .withInt("id", -1)
                    .navigation();
            return true;
        }
        if (name.contains("B?????????")) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_ANALYZE_B)
                    .navigation();
            return true;
        }
        if ("?????????".equals(name)) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_BIRTH_PACKAGE)
                    .navigation();
            return true;
        }
        if ("?????????".equals(name)&&BuildConfig.VERSION_CODE>=2208) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_TOOLS_FOOD)
                    .withString("activityType","????????????")
                    .navigation();
            return true;
        }
        if ("?????????".equals(name)) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_WEBVIEW)
                    .withString("title", "?????????")
                    .withString("url", SpUtils.getValue(mContext, UrlKeys.H5_MEAL))
                    .navigation();
            return true;
        }
        if ("????????????".equals(name)) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_TOOLS_FOOD)
                    .withString("activityType", "????????????")
                    .navigation();
            return true;
        }
        if ("????????????".equals(name)&&BuildConfig.VERSION_CODE>=2208) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_TOOLS_VACC_CHECK)
                    .withInt("status",muserInfoMonModel.status)
                    .navigation();
            return true;
        }
        if ("????????????".equals(name)) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_VACCINE_LIST)
                    .withInt("id", -1)
                    .navigation();
            return true;
        }

        NoFucDialog.newInstance().show(getSupportFragmentManager(),"????????????");
        return false;
    }

    @Override
    public void onSucessGetAllStatus(List<UserInfoExModel> userInfoExModels) {

    }

    @Override
    public void onSucessGetNowDiary(String result) {

    }

    @Override
    public void onSucessGetNowSleepDiary(String result) {

    }

    @Override
    public void onSucessUpdateDiary() {

    }
    UserInfoMonModel muserInfoMonModel;
    @Override
    public void onsucessGetMine(UserInfoMonModel userInfoMonModel) {
        muserInfoMonModel=userInfoMonModel;
//         yuzitime=0;
//         bbtime=0;
//         yuntime=0;

        checkCurrentS(muserInfoMonModel);


        if(userInfoMonModel.status>=3){
            buildView(true);
        }else {
            buildView(false);
        }
    }

    private void checkCurrentS(UserInfoMonModel muserInfoMonModel) {
//        if(!TextUtils.isEmpty(muserInfoMonModel.deliveryDate)){//??????????????? ???????????????
//            int age=0;
//            try {
//                 age= DateUtils.getAgeDay(new SimpleDateFormat("yyyy-MM-dd").parse(muserInfoMonModel.deliveryDate),new Date());
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            if(age<=49){//?????????
//                yuzitime=age-1>=0?age-1:0;
//            }else {
//                if(age<180){
//                    bbtime=0;
//                }else {
//                    bbtime=age/30-5>=0?age/30-5:0;
//                    if(age/30>12){
//                        if(age/30>12&&age/30<24){
//                            bbtime=8;
//                        }else {
//                            bbtime=9;
//                        }
//                    }
//                }
//
//            }
//        }
//        if(muserInfoMonModel.dateContent.contains("???")&&!muserInfoMonModel.dateContent.contains("??????")){
//           if(!muserInfoMonModel.dateContent.contains("??????")){
//               String noYunTmp=muserInfoMonModel.dateContent.split("???")[1].trim();
//               String noZhouTmp=noYunTmp.split("???")[0].trim();
//               int zhou=Integer.parseInt(noZhouTmp);
//               yuntime=zhou-1>=0?zhou-1:0;
//           }
//        }
    }
}
