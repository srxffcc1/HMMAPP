package com.health.index.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.index.R;
import com.health.index.contract.ToolsModContract;
import com.health.index.fragment.ToolsFoodRecipeFragment;
import com.health.index.model.UserInfoExModel;
import com.healthy.library.model.UserInfoMonModel;
import com.health.index.presenter.ToolsModPresenter;
import com.healthy.library.adapter.DropDownType;
import com.healthy.library.adapter.FragmentStatePagerItemAdapter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.interfaces.IsNeedShare;
import com.healthy.library.interfaces.OnSubmitListener;
import com.healthy.library.message.FoodDialogInfoMsg;
import com.healthy.library.message.FoodDialogInfoOpenMsg;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.utils.BitmapUtils;
import com.healthy.library.utils.DateUtils;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.business.GridDropDownPop;
import com.healthy.library.widget.TopBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Route(path = IndexRoutes.INDEX_TOOLS_FOOD)
public class ToolsFoodMainActivity extends BaseActivity implements IsNeedShare,IsFitsSystemWindows, ToolsModContract.View ,TextView.OnEditorActionListener{
    @Autowired
    String activityType="孕期食谱";//孕期食谱 月子食谱 宝宝辅食

    private List<Fragment> fragments = new ArrayList<>();
    FragmentStatePagerItemAdapter fragmentPagerItemAdapter;
    private TopBar topBar;
    private SlidingTabLayout st;
    private ViewPager vp;
    int currentIndex = 0;


    String recipeType;


    ToolsModPresenter toolsModPresenter;
    GridDropDownPop effectdialog;
    GridDropDownPop fooddialog;

    List<DropDownType> dropDownTypesEffects=new ArrayList<>();
    List<DropDownType> dropDownTypesFoods=new ArrayList<>();


    String foodId="";
    String effectId="";

    String food="不限";
    String effect="不限";

    List<String> titles = new ArrayList<>();
    List<String> titlesShare = new ArrayList<>();

    private String surl;
    private String sdes;
    private String stitle;
    private Bitmap sBitmap;



    int yuzitime=0;
    int bbtime=0;
    int yuntime=0;
    private void checkCurrentS(UserInfoExModel muserInfoMonModel) {
        if(muserInfoMonModel!=null){
            if(!TextUtils.isEmpty(muserInfoMonModel.deliveryDate)){//可能是月子 可能是宝宝
                int age=0;
                try {
                    age= DateUtils.getAgeDay(new SimpleDateFormat("yyyy-MM-dd").parse(muserInfoMonModel.deliveryDate),new Date());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(age<=42){//月子期
                    yuzitime=age-1>=0?age-1:0;
                }else {
                    if(age<180){
                        bbtime=0;
                    }else {
                        bbtime=age/30-5>=0?age/30-5:0;
                        if(age/30>12){
                            if(age/30>12&&age/30<=24){
                                bbtime=8;
                            }else {
                                bbtime=9;
                            }
                        }
                    }

                }
            }
            if(!TextUtils.isEmpty(muserInfoMonModel.stageStatusStr)){
                if(muserInfoMonModel.stageStatusStr.contains("孕")&&!muserInfoMonModel.stageStatusStr.contains("备孕")){
                    if(!muserInfoMonModel.stageStatusStr.contains("怀孕")){
                        String noYunTmp=muserInfoMonModel.stageStatusStr.split("孕")[1].trim();
                        String noZhouTmp=noYunTmp.split("周")[0].trim();
                        int zhou=Integer.parseInt(noZhouTmp);
                        yuntime=zhou-1>=0?zhou-1:0;
                    }
                }

            }
        }

    }

    public void showEffectDialog(String id){
        if(effectdialog ==null){
            effectdialog =new GridDropDownPop(this, 3,new GridDropDownPop.ItemClickCallBack() {
                @Override
                public void click(String id, String name) {
                    effectId=id;
                    effect=name;
                    EventBus.getDefault().post(new FoodDialogInfoMsg(effectId,foodId,effect,food));
                }

                @Override
                public void dismiss() {

                }
            });
//            List<DropDownType> droplist=new ArrayList<>();
//            for (int i = 0; i <20 ; i++) {
//                droplist.add(new DropDownType(i+"","分类"+i));
//            }
            effectdialog.setData(dropDownTypesEffects);

        }else {

        }
        for (int i = 0; i <dropDownTypesEffects.size() ; i++) {
            if(dropDownTypesEffects.get(i).getName().equals(id)){

                effectdialog.setSelectId(dropDownTypesEffects.get(i).getId());
            }
        }
        if(!isFinishing()){
            try {
                effectdialog.showPopupWindow(topBar);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(fooddialog!=null){

                fooddialog.dismiss();
            }
        }
    }
    public void showFoodDialog(String id){
        if(fooddialog ==null){
            fooddialog =new GridDropDownPop(this, 3,new GridDropDownPop.ItemClickCallBack() {
                @Override
                public void click(String id, String name) {
                    foodId=id;
                    food=name;
                    EventBus.getDefault().post(new FoodDialogInfoMsg(effectId,foodId,effect,food));
                }

                @Override
                public void dismiss() {

                }
            });
//            List<DropDownType> droplist=new ArrayList<>();
//            for (int i = 0; i <20 ; i++) {
//                droplist.add(new DropDownType(i+"","分类"+i));
//            }
            fooddialog.setData(dropDownTypesFoods);

        }else {

        }
        for (int i = 0; i <dropDownTypesFoods.size() ; i++) {
            if(dropDownTypesFoods.get(i).getName().equals(id)){

                fooddialog.setSelectId(dropDownTypesFoods.get(i).getId());
            }
        }
        if(!isFinishing()){
            try {
                fooddialog.showPopupWindow(topBar);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(effectdialog!=null){

                effectdialog.dismiss();
            }
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        return false;
    }

    @Override
    public void onSucessgetType(String result) {

    }

    @Override
    public void onSucessgetTop(String result) {
        dropDownTypesFoods.clear();
        try {
            JSONArray jsonArray=new JSONObject(result).getJSONArray("data");
            dropDownTypesFoods.add(new DropDownType("","不限"));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                dropDownTypesFoods.add(new DropDownType(jsonObject.optString("id"),jsonObject.optString("food")));
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        dropDownTypesEffects.clear();
        try {
            JSONArray jsonArray=new JSONObject(result).getJSONArray("data");
            dropDownTypesEffects.add(new DropDownType("","不限"));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                dropDownTypesEffects.add(new DropDownType(jsonObject.optString("id"),jsonObject.optString("effectName")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onsucessGetMine(UserInfoMonModel userInfoMonModel) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.index_activity_tools_food_main;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        buildTab();
        topBar.setSubmitListener(new OnSubmitListener() {
            @Override
            public void onSubmitBtnPressed() {
                showShare();
            }
        });
        topBar.setTitle(activityType);
        toolsModPresenter=new ToolsModPresenter(this,this);
        toolsModPresenter.getTop(new SimpleHashMapBuilder<String, Object>().puts(Functions.FUNCTION,"8086"));
        toolsModPresenter.getBottom(new SimpleHashMapBuilder<String, Object>().puts(Functions.FUNCTION,"8085").puts("recipeType",recipeType));
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
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
    private void buildTab() {
        checkCurrentS(resolveTmpData(SpUtils.getValue(mContext, SpKey.USE_TOKEN)));
        titles.clear();
        titlesShare.clear();
        if("孕期食谱".equals(activityType)){
            recipeType="3";
            for (int i = 1; i < 41; i++) {
                titles.add("孕"+i+"周");
                titlesShare.add("孕期食谱-第"+i+"周");
            }
            currentIndex=yuntime;
        }else if("月子食谱".equals(activityType)||"月子餐".equals(activityType)){
            recipeType="1";
            for (int i = 1; i < 43; i++) {
                titles.add("第"+i+"天");
                titlesShare.add("月子餐-第"+i+"天");
            }
            currentIndex=yuzitime;
        }else {
            recipeType="2";
            titles.add("4-5个"+"月");
            titlesShare.add("宝宝辅食-4-5个"+"月");
            for (int i = 6; i < 13; i++) {
                titles.add(""+i+"个月");
                titlesShare.add("宝宝辅食-第"+i+"个月");
            }
            titles.add("1-2"+"岁");
            titles.add("2-3"+"岁");

            titlesShare.add("宝宝辅食-1-2"+"岁");
            titlesShare.add("宝宝辅食-2-3"+"岁");
            currentIndex=bbtime;
        }
        for (int i = 0; i <titles.size() ; i++) {
            String address = LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE) + "-" + LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE) + "-" + LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE);
            fragments.add(ToolsFoodRecipeFragment.newInstance(new SimpleHashMapBuilder<String, Object>()
                    .puts("activityType",activityType)
                    .puts("address",address)
                    .puts("recipeType",recipeType)
                    .puts("foods","")
                    .puts("effect","")
                    .puts("recipeDateString",titles.get(i))
                    .puts("recipeDateStringShare",titlesShare.get(i))
                    .puts("recipeDate",(i+1)+"")));
        }
        if (fragmentPagerItemAdapter == null) {
            fragmentPagerItemAdapter = new FragmentStatePagerItemAdapter(getSupportFragmentManager(), fragments, titles);
            // adapter
            vp.setAdapter(fragmentPagerItemAdapter);
        } else {
            fragmentPagerItemAdapter.setDataSource(fragments, titles);
        }
        vp.setOffscreenPageLimit(fragments.size());
        // bind to SmartTabLayout
        st.setViewPager(vp);
        vp.setCurrentItem(currentIndex);
        st.setCurrentTab(currentIndex,true);
        BaseFragment baseFragment= (BaseFragment) fragments.get(currentIndex>fragments.size()-1?0:currentIndex);
        baseFragment.isfragmenthow=true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.top_bar);
        st = (SlidingTabLayout) findViewById(R.id.st);
        vp = (ViewPager) findViewById(R.id.vp);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateDateInfo(FoodDialogInfoOpenMsg msg) {
        if(msg.dialogType==1){
            showEffectDialog(msg.id);
        }else {
            showFoodDialog(msg.id);
        }

    }

    @Override
    public String getSurl() {
        String url;
        String urlPrefix = SpUtils.getValue(mContext, UrlKeys.H5_BargainUrl);
        if("孕期食谱".equals(activityType)){
            url= String.format("%s?type=5&foodtype=pregnancyFood",urlPrefix);
        }else if("月子食谱".equals(activityType)){
            url= String.format("%s?type=5&foodtype=monthFood",urlPrefix);
        }else {
            url= String.format("%s?type=5&foodtype=babyFood",urlPrefix);
        }
        return url;
    }

    @Override
    public String getSdes() {
        ToolsFoodRecipeFragment toolsFoodRecipeFragment= (ToolsFoodRecipeFragment) fragments.get(st.getCurrentTab());
        return toolsFoodRecipeFragment.getTopSuggest();
    }

    @Override
    public String getStitle() {
        return titlesShare.get(st.getCurrentTab());
    }

    @Override
    public Bitmap getsBitmap() {
        if("孕期食谱".equals(activityType)){
            return BitmapUtils.changeColor(BitmapFactory.decodeResource(mContext.getResources(),R.drawable.index_share_humb_yqsp));
        }else if("月子食谱".equals(activityType)){
            return BitmapUtils.changeColor(BitmapFactory.decodeResource(mContext.getResources(),R.drawable.index_share_humb_yzsp));
        }else {
            return BitmapUtils.changeColor(BitmapFactory.decodeResource(mContext.getResources(),R.drawable.index_share_humb_bbfs));
        }
    }
}
