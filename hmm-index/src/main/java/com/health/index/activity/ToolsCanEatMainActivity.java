package com.health.index.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.index.R;
import com.health.index.contract.ToolsModContract;
import com.health.index.fragment.ToolsFoodCanEatFragment;
import com.healthy.library.model.ToolsCEType;
import com.healthy.library.model.UserInfoMonModel;
import com.health.index.presenter.ToolsModPresenter;
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
import com.healthy.library.utils.ScreenUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.TopBar;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Route(path = IndexRoutes.INDEX_TOOLS_CANEAT)
public class ToolsCanEatMainActivity extends BaseActivity implements IsNeedShare,IsFitsSystemWindows, ToolsModContract.View ,TextView.OnEditorActionListener{
    private com.healthy.library.widget.TopBar topBar;
    private com.scwang.smart.refresh.layout.SmartRefreshLayout refreshLayout;
    private com.google.android.material.appbar.AppBarLayout appBar;
    private com.google.android.material.appbar.CollapsingToolbarLayout collapsingToolbarLayout;
    private android.widget.EditText serarchKeyWord;
    private android.widget.ImageView clearEdit;
    private android.widget.TextView seachClick;
    private androidx.constraintlayout.widget.ConstraintLayout seachType;
    private android.widget.GridLayout tipGrid;
    private android.view.View divider;
    private com.flyco.tablayout.SlidingTabLayout st;
    private android.view.View dividerStore;
    private androidx.viewpager.widget.ViewPager vp;
    ToolsModPresenter toolsModPresenter;
    List<ToolsCEType> toolsCanEatTypes=new ArrayList<>();
    private String[] mTitles = {"孕期", "坐月子", "哺乳期", "婴幼儿"};
    private List<Fragment> fragments = new ArrayList<>();
    FragmentStatePagerItemAdapter fragmentPagerItemAdapter;
    int currentIndex = 0;
    private TextView seachButton;

    @Override
    protected int getLayoutId() {
        return R.layout.index_activity_tools_caneat_main;
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        toolsModPresenter=new ToolsModPresenter(this,this);
        toolsModPresenter.getType(new SimpleHashMapBuilder<String, Object>().puts(Functions.FUNCTION,"4014"));
        refreshLayout.setEnableLoadMore(false);
        fragments.clear();
        topBar.setSubmitListener(new OnSubmitListener() {
            @Override
            public void onSubmitBtnPressed() {
                showShare();
            }
        });
        List<String> titles = Arrays.asList(mTitles);
        for (int i = 0; i <titles.size() ; i++) {
            fragments.add(ToolsFoodCanEatFragment.newInstance(new SimpleHashMapBuilder<String, Object>().puts("eatStage",(i+1)+"")));
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
        vp.setCurrentItem(currentIndex);
        st.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                currentIndex = position;
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

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
        seachButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(serarchKeyWord.getText().toString())) {
                    return;
                }
                ARouter.getInstance()
                        .build(IndexRoutes.INDEX_TOOLS_CANEAT_TYPE)
                        .withString("keyWords",serarchKeyWord.getText().toString())
                        .withString("nowtitle",serarchKeyWord.getText().toString())
                        .withString("activityType","2")
                        .navigation();
            }
        });
    }

    @Override
    public void onSucessgetType(String result) {
        toolsCanEatTypes.clear();
        toolsCanEatTypes.addAll(resolveTypeListData(result));
        buildTypeGrid(this,tipGrid,toolsCanEatTypes);
    }

    private int mMargin;
    private int mCornerRadius;
    private void buildTypeGrid(final Context context, final GridLayout gridLayout, final List<ToolsCEType> urls) {
        if (mMargin == 0) {
            mMargin = (int) TransformUtil.dp2px(mContext, 2);
            mCornerRadius = (int) TransformUtil.dp2px(mContext, 3);
        }
        gridLayout.removeAllViews();
        if (urls != null && urls.size() > 0) {
            gridLayout.post(new Runnable() {
                @Override
                public void run() {
                    gridLayout.removeAllViews();
                    gridLayout.setColumnCount(4);
                    int w = (ScreenUtils.getScreenWidth(context)/4);
                    for (int i = 0; i < urls.size(); i++) {
                        final ToolsCEType url = urls.get(i);
                        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                        params.width = w;
                        View view = LayoutInflater.from(mContext).inflate(R.layout.index_activity_tools_item_caneat_main_topimg, gridLayout, false);
                        com.healthy.library.businessutil.GlideCopy.with(context)
                                .load(url.foodTypeImage)
                                .placeholder(R.drawable.img_1_1_default2)
                                .error(R.drawable.img_1_1_default)

                                .into((ImageView) view.findViewById(R.id.iv_category));
                        TextView tv_category=view.findViewById(R.id.tv_category);
                        tv_category.setText(url.foodTypeName);
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ARouter.getInstance()
                                        .build(IndexRoutes.INDEX_TOOLS_CANEAT_TYPE)
                                        .withString("selectType",url.foodType+"")
                                        .withString("nowtitle",url.foodTypeName)
                                        .withString("activityType","0")
                                        .navigation();
                            }
                        });

                        gridLayout.addView(view, params);
                    }
                }
            });
        }


    }

    private List<ToolsCEType> resolveTypeListData(String obj) {
        List<ToolsCEType> result = new ArrayList<>();
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

    private void initView() {
        topBar = (TopBar) findViewById(R.id.top_bar);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refresh_layout);
        appBar = (AppBarLayout) findViewById(R.id.app_bar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        serarchKeyWord = (EditText) findViewById(R.id.serarchKeyWord);
        clearEdit = (ImageView) findViewById(R.id.clearEdit);
        seachClick = (TextView) findViewById(R.id.seachClick);
        seachType = (ConstraintLayout) findViewById(R.id.seachType);
        tipGrid = (GridLayout) findViewById(R.id.tip_grid);
        divider = (View) findViewById(R.id.divider);
        st = (SlidingTabLayout) findViewById(R.id.st);
        dividerStore = (View) findViewById(R.id.divider_store);
        vp = (ViewPager) findViewById(R.id.vp);
        seachButton = (TextView) findViewById(R.id.seachButton);
        refreshLayout.setEnableRefresh(false);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            if (TextUtils.isEmpty(serarchKeyWord.getText().toString())) {
                return false;
            }

            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_TOOLS_CANEAT_TYPE)
                    .withString("keyWords",serarchKeyWord.getText().toString())
                    .withString("activityType","2")
                    .withString("nowtitle",serarchKeyWord.getText().toString())
                    .navigation();
        }
        return false;
    }

    @Override
    public String getSurl() {
        String urlPrefix = SpUtils.getValue(mContext, UrlKeys.H5_BargainUrl);
        String url = String.format("%s?type=5&foodtype=CanEat", urlPrefix);
        return url;
    }

    @Override
    public String getSdes() {
        return "怀孕、做月子、哺乳期、小宝宝哪些食物不能吃，点这里看看吧~";
    }

    @Override
    public String getStitle() {
        return "能不能吃";
    }

    @Override
    public Bitmap getsBitmap() {
        return BitmapUtils.changeColor(BitmapFactory.decodeResource(mContext.getResources(),R.drawable.index_share_humb_nbnc));
    }
}
