package com.health.index.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.index.R;
import com.health.index.contract.ToolsModContract;
import com.health.index.fragment.ToolsFoodCanEatTypeFragment;
import com.healthy.library.model.ToolsCEType;
import com.healthy.library.model.UserInfoMonModel;
import com.health.index.presenter.ToolsModPresenter;
import com.healthy.library.adapter.DropDownType;
import com.healthy.library.adapter.FragmentStatePagerItemAdapter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.interfaces.IsNeedShare;
import com.healthy.library.interfaces.OnSubmitListener;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.utils.BitmapUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.business.GridDropDownPop;
import com.healthy.library.widget.TopBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Route(path = IndexRoutes.INDEX_TOOLS_CANEAT_TYPE)
public class ToolsCanEatTypeActivity extends BaseActivity implements IsNeedShare,IsFitsSystemWindows, ToolsModContract.View,TextView.OnEditorActionListener {

    private TopBar topBar;
    private EditText serarchKeyWord;
    private ImageView clearEdit;
    private TextView seachClick;
    private SlidingTabLayout st;
    private ViewPager vp;
    private List<Fragment> fragments = new ArrayList<>();
    FragmentStatePagerItemAdapter fragmentPagerItemAdapter;
    ToolsModPresenter toolsModPresenter;
    int currentIndex = 0;
    @Autowired
    String nowtitle;



    GridDropDownPop typeDropDownPop;

    @Autowired
    String selectType="";//0单页不可滑动 1出现tab 2搜索
    @Autowired
    String activityType="0";//0单页不可滑动 1出现tab 2搜索


    @Autowired
    String eatStage;
    @Autowired
    String eatStageKey;
    @Autowired
    String eatStageValue;

    List<DropDownType> dropDownTypes=new ArrayList<>();

    @Autowired
    String keyWords;
    private androidx.constraintlayout.widget.ConstraintLayout seachLL;
    private androidx.constraintlayout.widget.ConstraintLayout tabLL;
    private android.widget.LinearLayout checkMenuLL;
    private ImageView checkMenu;

    @Override
    public void onSucessgetType(String result) {
        ArrayList<ToolsCEType> soundIndices=resolveListType(result);
        dropDownTypes.clear();
        for (int i = 0; i <soundIndices.size() ; i++) {
            dropDownTypes.add(new DropDownType(i+"",soundIndices.get(i).foodTypeName));
        }

        List<String> titles = new ArrayList<>();
        if("1".equals(activityType)){
            for (int i = 0; i <soundIndices.size() ; i++) {
                titles.add(soundIndices.get(i).foodTypeName);
                if(soundIndices.get(i).foodTypeName.equals(selectType)){
                    currentIndex=i;
                }
            }
            fragments.clear();
            for (int i = 0; i <titles.size() ; i++) {
                fragments.add(ToolsFoodCanEatTypeFragment.newInstance(new SimpleHashMapBuilder<String, Object>().puts("eatStage",eatStage)
                        .puts("eatStageKey",eatStageKey)
                        .puts("eatStageValue",eatStageValue)
                        .puts("foodType",soundIndices.get(i).foodType+"")
                        .puts("activityType",activityType)
                        .puts("keyWords",keyWords)
                ));
            }
        }else {
            if("2".equals(activityType)){
                titles.add("搜索");
            }else {
                titles.add(selectType);
            }

            fragments.clear();
            fragments.add(ToolsFoodCanEatTypeFragment.newInstance(new SimpleHashMapBuilder<String, Object>().puts("eatStage",eatStage)
                    .puts("eatStageKey",eatStageKey)
                    .puts("eatStageValue",eatStageValue)
                    .puts("foodType",selectType)
                    .puts("activityType",activityType)
                    .puts("keyWords",keyWords)
            ));
        }


        if (fragmentPagerItemAdapter == null) {
            fragmentPagerItemAdapter = new FragmentStatePagerItemAdapter(getSupportFragmentManager(), fragments, titles);
            // adapter
            vp.setAdapter(fragmentPagerItemAdapter);
        } else {
            fragmentPagerItemAdapter.setDataSource(fragments, titles);
        }
        vp.setOffscreenPageLimit(4);
        // bind to SmartTabLayout
        st.setViewPager(vp);
        st.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                currentIndex=position;
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        vp.setCurrentItem(currentIndex);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                currentIndex=position;
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        checkMenuLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(topBar);
            }
        });
    }

    public void showMenu(View view){
        if(typeDropDownPop ==null){
            typeDropDownPop =new GridDropDownPop(ToolsCanEatTypeActivity.this, new GridDropDownPop.ItemClickCallBack() {
                @Override
                public void click(String id, String name) {
                    currentIndex=Integer.parseInt(id);
                    st.setCurrentTab(currentIndex);
                }

                @Override
                public void dismiss() {

                }
            });
//            List<DropDownType> droplist=new ArrayList<>();
//            for (int i = 0; i <20 ; i++) {
//                droplist.add(new DropDownType(i+"","分类"+i));
//            }
            typeDropDownPop.setData(dropDownTypes);

        }else {

        }
        //System.out.println("show:"+currentIndex);
        typeDropDownPop.setSelectId(currentIndex+"");
        if(!isFinishing()){
            try {
                typeDropDownPop.showPopupWindow(view);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

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

    }

    @Override
    public void onsucessGetMine(UserInfoMonModel userInfoMonModel) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.index_activity_tools_caneat_main_type;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        toolsModPresenter=new ToolsModPresenter(this,this);
        toolsModPresenter.getType(new SimpleHashMapBuilder<String, Object>().puts(Functions.FUNCTION,"4014"));
        if("0".equals(activityType)){
            topBar.setTitle(nowtitle);
            seachLL.setVisibility(View.GONE);
            tabLL.setVisibility(View.GONE);
        }else if("1".equals(activityType)){
            topBar.setTitle(nowtitle);
            seachLL.setVisibility(View.GONE);
        }else {
            //System.out.println("搜索点击拿来;"+nowtitle);
            topBar.setTitle(nowtitle);
            tabLL.setVisibility(View.GONE);
        }

        if("0".equals(activityType)){
            topBar.getSubmitBack().setVisibility(View.VISIBLE);
        }else {
            topBar.getSubmitBack().setVisibility(View.GONE);
        }
        topBar.setSubmitListener(new OnSubmitListener() {
            @Override
            public void onSubmitBtnPressed() {
                showShare();
            }
        });

        serarchKeyWord.setText(keyWords);

        serarchKeyWord.setOnEditorActionListener(this);
        clearEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serarchKeyWord.setText("");
            }
        });
        serarchKeyWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length()>0){
                    clearEdit.setVisibility(View.VISIBLE);
                }else {
                    clearEdit.setVisibility(View.GONE);
                }
            }
        });
        seachClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(serarchKeyWord.getText().toString())) {
                    return;
                }
                try {
                    ToolsFoodCanEatTypeFragment toolsFoodCanEatTypeFragment= (ToolsFoodCanEatTypeFragment) fragments.get(0);
                    toolsFoodCanEatTypeFragment.seachKey(serarchKeyWord.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private ArrayList<ToolsCEType> resolveListType(String obj) {
        ArrayList<ToolsCEType> result = new ArrayList<>();
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
            Type type = new TypeToken<List<ToolsCEType>>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.top_bar);
        serarchKeyWord = (EditText) findViewById(R.id.serarchKeyWord);
        clearEdit = (ImageView) findViewById(R.id.clearEdit);
        seachClick = (TextView) findViewById(R.id.seachClick);
        st = (SlidingTabLayout) findViewById(R.id.st);
        vp = (ViewPager) findViewById(R.id.vp);
        seachLL = (ConstraintLayout) findViewById(R.id.seachLL);
        tabLL = (ConstraintLayout) findViewById(R.id.tabLL);
        checkMenuLL = (LinearLayout) findViewById(R.id.checkMenuLL);
        checkMenu = (ImageView) findViewById(R.id.checkMenu);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            if (TextUtils.isEmpty(serarchKeyWord.getText().toString())) {
                return false;
            }

            ToolsFoodCanEatTypeFragment toolsFoodCanEatTypeFragment= (ToolsFoodCanEatTypeFragment) fragments.get(0);
            toolsFoodCanEatTypeFragment.seachKey(serarchKeyWord.getText().toString());
//            ARouter.getInstance()
//                    .build(IndexRoutes.INDEX_TOOLS_CANEAT_TYPE)
//                    .withString("keyWords",serarchKeyWord.getText().toString())
//                    .withString("activityType","2")
//                    .navigation();
        }
        return false;
    }

    @Override
    public String getSurl() {
        String urlPrefix = SpUtils.getValue(mContext, UrlKeys.H5_CAN_EAT_LIST);
        String url = String.format("%s?foodType=%s&foodName=%s&scheme=CanEatList", urlPrefix,selectType, URLEncoder.encode(nowtitle));
        return url;
    }

    @Override
    public String getSdes() {
        return "怀孕、哺乳、小宝宝哪些东西能吃，哪些东西不能吃，点这里查查看吧~";
    }

    @Override
    public String getStitle() {
        return "能不能吃 - "+nowtitle;
    }

    @Override
    public Bitmap getsBitmap() {
        return BitmapUtils.changeColor(BitmapFactory.decodeResource(mContext.getResources(),R.drawable.index_share_humb_nbnc));
    }
}
