package com.health.faq.activity;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.health.faq.R;
import com.health.faq.contract.MeetProfessorContract;
import com.health.faq.fragment.MeetProfessorLeftFragment;
import com.healthy.library.model.Faq;
import com.healthy.library.model.FaqHotExpertFieldChose;
import com.healthy.library.model.FaqVideo;
import com.health.faq.presenter.MeetProfessorPresenter;
import com.healthy.library.adapter.DropDownType;
import com.healthy.library.adapter.FragmentStatePagerItemAdapter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.model.TabChangeModelFragment;
import com.healthy.library.routes.AppRoutes;
import com.healthy.library.business.GridDropDownPopFaq;
import com.healthy.library.widget.ImageTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(path = AppRoutes.MODULE_EXPERT_LEFT)
public class MeetProfessorLeftActivity extends BaseActivity implements MeetProfessorContract.View, View.OnClickListener, TextView.OnEditorActionListener {

    @Autowired
    String cityNo;
    @Autowired
    String expertCategoryNo;
    @Autowired
    String expertCategoryName;
    MeetProfessorPresenter presenter;
    private android.widget.EditText serarchKeyWord;
    private android.widget.ImageView imgBack;
    private View divider;
    private com.flyco.tablayout.SlidingTabLayout st;
    private com.healthy.library.widget.ImageTextView tvArea;
    private View dividerStore;
    private ViewPager vp;
//    private MeetProfessorLeftFragment meetProfessorLeftFragmentleft;
    private MeetProfessorLeftFragment meetProfessorLeftFragmentright;
    private List<Fragment> fragments = new ArrayList<>();
    FragmentStatePagerItemAdapter fragmentPagerItemAdapter;
    int currentIndex = 0;
    private List<FaqHotExpertFieldChose> faqHotExpertFieldChoses = new ArrayList<>();
    private GridDropDownPopFaq typeDropDownPop;
    String areaString = "全部科室";
    String areaSelect = "";
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_area) {
            if (typeDropDownPop == null) {
                typeDropDownPop = new GridDropDownPopFaq(mContext, new GridDropDownPopFaq.ItemClickCallBack() {
                    @Override
                    public void click(String id, String name) {
                        areaSelect = id;
                        tvArea.setText(name);
                        areaString = name;
                        onRefreshChildFragment(id);
                        setAreaArrow(name);
                        if ("全部科室".equals(name)) {
                            setAreaArrow(false);
                        } else {
                            setAreaArrowR(false);
                        }
                    }

                    @Override
                    public void dismiss() {
                        if ("全部科室".equals(areaString)) {
                            setAreaArrow(true);
                        } else {
                            setAreaArrowR(true);
                        }
                    }
                });
                List<DropDownType> droplist = new ArrayList<>();
                droplist.add(new DropDownType("", "全部科室"));
                for (int i = 0; i < faqHotExpertFieldChoses.size(); i++) {
                    droplist.add(new DropDownType(faqHotExpertFieldChoses.get(i).expertCategoryNo + "", faqHotExpertFieldChoses.get(i).expertCategoryName));
                }
                typeDropDownPop.setData(droplist);
                typeDropDownPop.setSelectId(areaSelect);

            } else {

            }
            if (!isFinishing()) {
                if ("全部科室".equals(areaString)) {
                    setAreaArrow(false);
                } else {
                    setAreaArrowR(false);
                }
                typeDropDownPop.showPopupWindow(view);
            }
        }
    }



    public void setAreaArrow(String name) {
        if ("全部科室".equals(name)) {
            tvArea.setTextColor(Color.parseColor("#333333"));
            tvArea.setText("全部科室");
        } else {
            tvArea.setTextColor(Color.parseColor("#FF5353"));
            tvArea.setText(name);
        }
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


    public void setAreaArrowR(boolean flag) {
        tvArea.setDrawable(flag ? R.drawable.ic_solid_triangle_down_red : R.drawable.ic_solid_triangle_up_red, mContext);
    }

    public void setAreaArrow(boolean flag) {
        tvArea.setDrawable(flag ? R.drawable.ic_solid_triangle_down_gray : R.drawable.ic_solid_triangle_up_gray, mContext);
    }
    @Override
    protected void findViews() {
        super.findViews();
        initView();

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_loc_contry_list;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        serarchKeyWord.setOnEditorActionListener(this);
        tvArea.setOnClickListener(this);
        if(expertCategoryName!=null&&!"".equals(expertCategoryName)){
           areaString=expertCategoryName;
           areaSelect=expertCategoryNo;
           setAreaArrow(areaString);
           setAreaArrowR(true);
        }
        List<String> titles = Arrays.asList("保健专家");
//        Map<String,Object> mapleft=new HashMap<>();
//        mapleft.put("fragmentCity","本地");
//        mapleft.put("addressCity",cityNo);
//        mapleft.put("addressCityOrg",cityNo);//用来判断本地专家标签的
//        mapleft.put("keyWords","");
//        if(expertCategoryNo!=null&&!"".equals(expertCategoryNo)){
//            mapleft.put("expertCategoryNo",expertCategoryNo);
//        }else {
//            mapleft.put("expertCategoryNo","");
//        }
//        meetProfessorLeftFragmentleft=MeetProfessorLeftFragment.newInstance(mapleft);
        Map<String,Object> mapright=new HashMap<>();
        if(expertCategoryNo!=null&&!"".equals(expertCategoryNo)){
            mapright.put("expertCategoryNo",expertCategoryNo);
        }else {
            mapright.put("expertCategoryNo","");
        }
        mapright.put("fragmentCity","全国");
        mapright.put("addressCityOrg",cityNo);//用来判断本地专家标签的
        mapright.put("addressCity","");
        mapright.put("keyWords","");


        meetProfessorLeftFragmentright=MeetProfessorLeftFragment.newInstance(mapright);
//        fragments.add(meetProfessorLeftFragmentleft);
        fragments.add(meetProfessorLeftFragmentright);
        if (fragmentPagerItemAdapter == null) {
            fragmentPagerItemAdapter = new FragmentStatePagerItemAdapter(getSupportFragmentManager(), fragments, titles);
            // adapter
            vp.setAdapter(fragmentPagerItemAdapter);
        } else {
            fragmentPagerItemAdapter.setDataSource(fragments, titles);
        }
        fragmentPagerItemAdapter.notifyDataSetChanged();
        vp.setOffscreenPageLimit(fragments.size());
        // bind to SmartTabLayout
        st.setViewPager(vp);
        vp.setCurrentItem(currentIndex);
        st.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                currentIndex = position;
//                if(position!=0){
//                    moreHot.setVisibility(View.VISIBLE);
//                    tvArea.setVisibility(View.GONE);
//                }else {
//                    moreHot.setVisibility(View.GONE);
//                    tvArea.setVisibility(View.VISIBLE);
//                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });


        presenter=new MeetProfessorPresenter(mContext,this);
        presenter.getType();

    }

    private void initView() {
        serarchKeyWord = (EditText) findViewById(R.id.serarchKeyWord);
        imgBack = (ImageView) findViewById(R.id.img_back);
        divider = (View) findViewById(R.id.divider);
        st = (SlidingTabLayout) findViewById(R.id.st);
        tvArea = (ImageTextView) findViewById(R.id.tv_area);
        dividerStore = (View) findViewById(R.id.divider_store);
        vp = (ViewPager) findViewById(R.id.vp);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void onRefreshChildFragment(String id) {
//        meetProfessorLeftFragmentleft.putMap("expertCategoryNo", id);
//        meetProfessorLeftFragmentleft.onRefresh(null);
        meetProfessorLeftFragmentright.putMap("expertCategoryNo", id);
        meetProfessorLeftFragmentright.onRefresh(null);
    }
    @Override
    public void onGetFieldList(List<FaqHotExpertFieldChose> faqHotExpertFieldChoses) {
        this.faqHotExpertFieldChoses.clear();
        this.faqHotExpertFieldChoses.addAll(faqHotExpertFieldChoses);
    }

    @Override
    public void onGetHotFieldList(List<FaqHotExpertFieldChose> faqHotExpertFieldChoses) {

    }

    @Override
    public void onGetHomeSuccess(Faq expertInfoModel) {

    }

    @Override
    public void onGetVideoSuccess(List<FaqVideo> videolist) {

    }

    @Override
    public void subVideoSucess() {

    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if(actionId== EditorInfo.IME_ACTION_SEARCH){
            onRefreshChildFragmentKeyWord(serarchKeyWord.getText().toString());
        }
        return false;
    }

    private void onRefreshChildFragmentKeyWord(String keyword) {
//        meetProfessorLeftFragmentleft.putMap("keyWords", keyword);
//        meetProfessorLeftFragmentleft.onRefresh(null);
        meetProfessorLeftFragmentright.putMap("keyWords", keyword);
        meetProfessorLeftFragmentright.onRefresh(null);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateTab(TabChangeModelFragment msg) {
        st.setCurrentTab(1);
    }
}
