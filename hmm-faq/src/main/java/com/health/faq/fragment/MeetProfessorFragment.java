package com.health.faq.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.github.florent37.viewtooltip.ViewTooltip;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.health.faq.R;
import com.health.faq.activity.FqaActivity;
import com.health.faq.adapter.VideoListAdapter;
import com.health.faq.contract.MeetProfessorContract;
import com.healthy.library.model.Faq;
import com.healthy.library.model.FaqHotExpertFieldChose;
import com.healthy.library.model.FaqVideo;
import com.health.faq.presenter.MeetProfessorPresenter;
import com.healthy.library.adapter.FragmentStatePagerItemAdapter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.constant.SpKey;
import com.healthy.library.routes.AppRoutes;
import com.healthy.library.routes.FaqRoutes;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.business.GridDropDownPop;
import com.healthy.library.widget.ImageTextView;
import com.healthy.library.widget.TopBar;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.umeng.analytics.MobclickAgent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MeetProfessorFragment extends BaseFragment implements VideoListAdapter.SubListener, MeetProfessorContract.View, OnRefreshLoadMoreListener, View.OnClickListener {
    MeetProfessorPresenter presenter;
    private TopBar topBar;
    private SmartRefreshLayout layoutRefresh;
    private AppBarLayout appBar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    //    private android.widget.LinearLayout rewardTitle;
//    private RecyclerView rewardRecycle;
    private SlidingTabLayout st;
    private ViewPager vp;
    private RelativeLayout needS;
    private ConstraintLayout clAdapterBottom;
    private View viewBottom;
    private TextView textView;
    private View ivClose;
    private TextView tvAskExpert;
    private TextView tvReward;
    private List<Fragment> fragments = new ArrayList<>();
    private MeetProfessorLeftFragment meetProfessorLeftFragment;
        private MeetProfessorRightFragment meetProfessorRightFragment;
    private MeetProfessorRightFragment2 meetProfessorRightFragment2;
    FragmentStatePagerItemAdapter fragmentPagerItemAdapter;
    int currentIndex = 0;
    private ImageTextView tvArea;
    private GridDropDownPop typeDropDownPop;
    String areaString = "全部领域";
    String areaSelect = "";
    private int isinit = 0;
    private int isinit2 = 0;
    private List<FaqHotExpertFieldChose> faqHotExpertFieldChoses = new ArrayList<>();
    private TextView moreHot;
    private ImageView postimage;
    private View dividerStore;
    private RecyclerView videoRecycle;
    private VideoListAdapter videoListAdapter;
    private ConstraintLayout faqTopShowLL;


    private ConstraintLayout topIc1LL;
    private ConstraintLayout topIc2LL;
    private ConstraintLayout topIc3LL;
    private ConstraintLayout topIc4LL;

    private TextView faqCellShow1;
    private TextView faqCellShow2;
    private TextView faqCellShow3;
    private TextView faqCellShow4;

    private TextView faqCell1;
    private TextView faqCell2;
    private TextView faqCell3;
    private TextView faqCell4;

    public static MeetProfessorFragment newInstance(Map<String, Object> maporg) {
        MeetProfessorFragment fragment = new MeetProfessorFragment();
        Bundle args = new Bundle();
        for (Map.Entry<String, Object> entry : maporg.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof Integer) {
                args.putInt(key, (Integer) value);
            } else if (value instanceof Boolean) {
                args.putBoolean(key, (Boolean) value);
            } else if (value instanceof String) {
                args.putString(key, (String) value);
            } else {
                args.putSerializable(key, (Serializable) value);
            }
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_meetprofessor;
    }

    @Override
    protected void findViews() {
        initView();
        topBar.getImgBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        needS.setVisibility(View.GONE);
        topIc1LL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println(SpUtils.getValue(mContext, SpKey.USER_ID));
                String url="https://h5.yewyw.com/index.html?bmark=hmama&appid=977&flag=app&appUserId=%s&appUserPhone=18511557625#/ConsultQuick";
                url = String.format(url,
                        new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)));
                ARouter.getInstance()
                        .build(IndexRoutes.INDEX_WEBVIEW)
                        .withBoolean("isNeedRef",true)
                        .withString("title", "极速问诊")
                        .withBoolean("isinhome",false)
                        .withBoolean("doctorshop",true)
                        .withString("url", url)
                        .navigation();
            }
        });

        faqCellShow1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println(SpUtils.getValue(mContext, SpKey.USER_ID));
                String url="https://h5.yewyw.com/index.html?bmark=hmama&appid=977&flag=app&appUserId=%s&appUserPhone=18511557625#/ConsultQuick";
                url = String.format(url,
                        new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)));
                ARouter.getInstance()
                        .build(IndexRoutes.INDEX_WEBVIEW)
                        .withBoolean("isNeedRef",true)
                        .withString("title", "极速问诊")
                        .withBoolean("isinhome",false)
                        .withBoolean("doctorshop",true)
                        .withString("url", url)
                        .navigation();
            }
        });


        faqCellShow2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(AppRoutes.MODULE_EXPERT_LEFT)
                        .withString("cityNo", get("addressCity").toString())
                        .navigation();
            }
        });
        topIc2LL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(AppRoutes.MODULE_EXPERT_LEFT)
                        .withString("cityNo", get("addressCity").toString())
                        .navigation();
            }
        });


        topIc3LL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url="https://h5.yewyw.com/index.html?bmark=hmama&appid=977&flag=app&appUserId=%s&appUserPhone=18511557625#/DoctorList?deptType=3&sortType=0&titleLv=0&labelStr=";
                url = String.format(url,
                        new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)));
                ARouter.getInstance()
                        .build(IndexRoutes.INDEX_WEBVIEW)
                        .withBoolean("isNeedRef",true)
                        .withString("title", "名医在线")
                        .withBoolean("doctorshop",true)
                        .withBoolean("isinhome",false)
                        .withString("url", url)
                        .navigation();
            }
        });

        faqCellShow3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url="https://h5.yewyw.com/index.html?bmark=hmama&appid=977&flag=app&appUserId=%s&appUserPhone=18511557625#/DoctorList?deptType=3&sortType=0&titleLv=0&labelStr=";
                url = String.format(url,
                        new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)));
                ARouter.getInstance()
                        .build(IndexRoutes.INDEX_WEBVIEW)
                        .withBoolean("isNeedRef",true)
                        .withString("title", "名医在线")
                        .withBoolean("doctorshop",true)
                        .withBoolean("isinhome",false)
                        .withString("url", url)
                        .navigation();
            }
        });

        topIc4LL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url="https://h5.yewyw.com/index.html?bmark=hmama&appid=977&flag=app&appUserId=%s&appUserPhone=18511557625#/MentalityList01?dietitianGoodLabelType=7";
                url = String.format(url,
                        new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)));
                ARouter.getInstance()
                        .build(IndexRoutes.INDEX_WEBVIEW)
                        .withBoolean("isNeedRef",true)
                        .withString("title", "心理咨询")
                        .withBoolean("doctorshop",true)
                        .withBoolean("isinhome",false)
                        .withString("url", url)
                        .navigation();
            }
        });

        faqCellShow4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url="https://h5.yewyw.com/index.html?bmark=hmama&appid=977&flag=app&appUserId=%s&appUserPhone=18511557625#/MentalityList01?dietitianGoodLabelType=7";
                url = String.format(url,
                        new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)));
                ARouter.getInstance()
                        .build(IndexRoutes.INDEX_WEBVIEW)
                        .withBoolean("isNeedRef",true)
                        .withString("title", "心理咨询")
                        .withBoolean("doctorshop",true)
                        .withBoolean("isinhome",false)
                        .withString("url", url)
                        .navigation();
            }
        });










        tvAskExpert.setOnClickListener(this);
        tvReward.setOnClickListener(this);
        ivClose.setOnClickListener(this);
        List<String> titles = Arrays.asList("人气专家","热门问题","最新问题");
        layoutRefresh.setOnRefreshLoadMoreListener(this);
        layoutRefresh.setEnableLoadMore(false);
        meetProfessorLeftFragment = new MeetProfessorLeftFragment();
        meetProfessorLeftFragment.putMap("fragmentBottom", "1");
        meetProfessorLeftFragment.putMap("addressCityOrg", get("addressCityOrg").toString());



        meetProfessorRightFragment = new MeetProfessorRightFragment();
        meetProfessorRightFragment.putMap("fragmentBottom", "1");
        meetProfessorRightFragment.putMap("addressCityOrg", get("addressCityOrg").toString());

        meetProfessorRightFragment2 = new MeetProfessorRightFragment2();
        meetProfessorRightFragment2.putMap("fragmentBottom", "1");
        meetProfessorRightFragment2.putMap("searchType","1");
        meetProfessorRightFragment2.putMap("addressCityOrg", get("addressCityOrg").toString());


        meetProfessorLeftFragment.setNeedloadmore(false);
        meetProfessorRightFragment.setNeedloadmore(false);
        meetProfessorRightFragment2.setNeedloadmore(false);
        fragments.add(meetProfessorLeftFragment);
        fragments.add(meetProfessorRightFragment);
        fragments.add(meetProfessorRightFragment2);
        if (fragmentPagerItemAdapter == null) {
            fragmentPagerItemAdapter = new FragmentStatePagerItemAdapter(getChildFragmentManager(), fragments, titles);
            // adapter
            vp.setAdapter(fragmentPagerItemAdapter);
        } else {
            fragmentPagerItemAdapter.setDataSource(fragments, titles);
        }
        fragmentPagerItemAdapter.notifyDataSetChanged();


        videoListAdapter = new VideoListAdapter();
        LinearLayoutManager videolayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        videoRecycle.setAdapter(videoListAdapter);
        videoRecycle.setLayoutManager(videolayoutManager);
        videoListAdapter.setSubListener(this);


        vp.setOffscreenPageLimit(fragments.size());
        // bind to SmartTabLayout
        st.setViewPager(vp);
//        vp.setCurrentItem(currentIndex,true);
        st.setCurrentTab(currentIndex);
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
        presenter = new MeetProfessorPresenter(mContext, this);

        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {


                if(verticalOffset!=verticalOffsetold){
                    meetProfessorLeftFragment.setRefreshEnable(verticalOffset == 0);
                    meetProfessorRightFragment.setRefreshEnable(verticalOffset == 0);
                    meetProfessorRightFragment2.setRefreshEnable(verticalOffset == 0);
                    meetProfessorLeftFragment.setBottomVisable(false);
                    meetProfessorRightFragment.setBottomVisable(false);
                    meetProfessorRightFragment2.setBottomVisable(false);
                    faqTopShowLL.setVisibility((verticalOffset >= -20) ? View.GONE : View.VISIBLE);
                    topBar.setVisibility(((appBarLayout.getHeight() + verticalOffset - collapsingToolbarLayout.getMinimumHeight()) <= 20) ? View.GONE : View.VISIBLE);


                    //System.out.println("测试bar:" + appBarLayout.getHeight() + ":" + verticalOffset + ":" + collapsingToolbarLayout.getMinimumHeight());

                    float alpha = Math.min(((appBarLayout.getHeight() - collapsingToolbarLayout.getMinimumHeight()) - (appBarLayout.getHeight() + verticalOffset - collapsingToolbarLayout.getMinimumHeight())) * 1.0f / (appBarLayout.getHeight() - collapsingToolbarLayout.getMinimumHeight()), 1);
                    faqTopShowLL.setAlpha(alpha);


                    float alpha2 = Math.min(((appBarLayout.getHeight() + verticalOffset - collapsingToolbarLayout.getMinimumHeight())) * 1.0f / (appBarLayout.getHeight() - collapsingToolbarLayout.getMinimumHeight()), 1);
                    topBar.setAlpha(alpha2);
                }
                verticalOffsetold=verticalOffset;

                //System.out.println("测试barZZ:" + (appBarLayout.getHeight() + verticalOffset - collapsingToolbarLayout.getMinimumHeight()));



            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            appBar.setOutlineProvider(null);
            collapsingToolbarLayout.setOutlineProvider(ViewOutlineProvider.BOUNDS);
        }
        tvArea.setOnClickListener(this);


        if (isinit != 0) {
            meetProfessorLeftFragment.onRefresh(null);
            meetProfessorRightFragment.onRefresh(null);
            meetProfessorRightFragment2.onRefresh(null);


        }
        isinit = 1;


        if (SpUtils.isFirst(mContext, "floatFaq")) {


            String main = " 悬赏求助<br>快速回复";
            String main2 = " 海量专家<br>正确解答";

            ViewTooltip.on(tvReward)
                    .autoHide(true, 7000)
                    .corner(19)
                    .color(Color.parseColor("#FF7A8B"))
                    .distanceWithView(5)
                    .position(ViewTooltip.Position.TOP)
                    .padding(20, 20, 20, 20)
                    .text(main)
                    .withShadow(false)
                    .textSize(TypedValue.COMPLEX_UNIT_SP, 12)
                    .textColor(Color.WHITE)
                    .show();
            ViewTooltip.on(tvAskExpert)
                    .autoHide(true, 7000)
                    .corner(19)
                    .withShadow(false)
                    .color(Color.parseColor("#FF7A8B"))
                    .distanceWithView(5)
                    .position(ViewTooltip.Position.TOP)
                    .padding(20, 20, 20, 20)
                    .text(main2)
                    .textSize(TypedValue.COMPLEX_UNIT_SP, 12)
                    .textColor(Color.WHITE)
                    .show();
        }
    }

    int verticalOffsetold=0;
    public void setAreaArrow(String name) {
        if ("全部领域".equals(name)) {
            tvArea.setTextColor(Color.parseColor("#ff9596a4"));
            tvArea.setText("保健专家");
        } else {
            tvArea.setTextColor(Color.parseColor("#FF5353"));
            tvArea.setText(name);
        }
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {

    }

    public void setAreaArrowR(boolean flag) {
        tvArea.setDrawable(flag ? R.drawable.ic_solid_triangle_down_red : R.drawable.ic_solid_triangle_up_red, mContext);
    }

    public void setAreaArrow(boolean flag) {
        tvArea.setDrawable(flag ? R.drawable.ic_solid_triangle_down_gray : R.drawable.ic_solid_triangle_up_gray, mContext);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);

        if (view.getId() == R.id.hotpro_title) {
            ARouter.getInstance().build(FaqRoutes.FAQ_EXPERT_TYPELIST)
                    .withString("cityNo", get("addressCity").toString())
                    .navigation();
            //System.out.println("跳转次数");
        }
        if (view.getId() == R.id.reward_title) {

            Map nokmap = new HashMap<String, String>();
            nokmap.put("soure", "专家答疑");
            MobclickAgent.onEvent(mContext, "event2RewardClick", nokmap);


            startActivity(new Intent(mContext, FqaActivity.class).putExtra("cityNo", get("addressCity").toString()));
        }
        if (view.getId() == R.id.ivClose) {
            meetProfessorLeftFragment.setBottomVisable(false);
            meetProfessorRightFragment.setBottomVisable(false);
            meetProfessorRightFragment2.setBottomVisable(false);
            needS.setVisibility(View.GONE);
        }
        if (view.getId() == R.id.tvReward) {

            Map nokmap = new HashMap<String, String>();
            nokmap.put("soure", "专家答疑首页-悬赏求助");
            MobclickAgent.onEvent(mContext, "event2RewardFrom", nokmap);

            ARouter.getInstance().build(FaqRoutes.FAQ_REWARD).navigation();
        }
        if (view.getId() == R.id.tvAskExpert) {

            Map nokmap = new HashMap<String, String>();
            nokmap.put("soure", "专家答疑首页-问专家");
            MobclickAgent.onEvent(mContext, "event2QuestExportFrom", nokmap);

            ARouter.getInstance()
                    .build(AppRoutes.MODULE_EXPERT_LEFT)
                    .withString("cityNo", get("addressCity").toString())
                    .navigation();
//            startActivity(new Intent(mContext, ExpertListActivity.class));
        }


//        if (view.getId() == R.id.tv_area) {
//            if (typeDropDownPop == null) {
//                typeDropDownPop = new GridDropDownPop(mContext, new GridDropDownPop.ItemClickCallBack() {
//                    @Override
//                    public void click(String id, String name) {
//                        areaSelect = id;
//                        tvArea.setText(name);
//                        areaString = name;
//                        onRefreshChildFragment(id);
//                        setAreaArrow(name);
//                        if ("全部领域".equals(name)) {
//                            setAreaArrow(false);
//                        } else {
//                            setAreaArrowR(false);
//                        }
//                    }
//
//                    @Override
//                    public void dismiss() {
//                        if ("全部领域".equals(areaString)) {
//                            setAreaArrow(true);
//                        } else {
//                            setAreaArrowR(true);
//                        }
//                    }
//                });
//                List<DropDownType> droplist = new ArrayList<>();
//                droplist.add(new DropDownType("", "全部领域"));
//                for (int i = 0; i < faqHotExpertFieldChoses.size(); i++) {
//                    droplist.add(new DropDownType(faqHotExpertFieldChoses.get(i).expertCategoryNo + "", faqHotExpertFieldChoses.get(i).expertCategoryName));
//                }
//                typeDropDownPop.setData(droplist);
//                typeDropDownPop.setSelectId(areaSelect);
//
//            } else {
//
//            }
//            if (!getActivity().isFinishing()) {
//                if ("全部领域".equals(areaString)) {
//                    setAreaArrow(false);
//                } else {
//                    setAreaArrowR(false);
//                }
//                typeDropDownPop.showPopupWindow(view);
//            }
//        }


    }

    private void onRefreshChildFragment(String id) {
        meetProfessorLeftFragment.putMap("expertCategoryNo", id);
        meetProfessorLeftFragment.onRefresh(null);
        meetProfessorRightFragment.putMap("expertCategoryNo", id);
        meetProfessorRightFragment.onRefresh(null);
        meetProfessorRightFragment2.putMap("expertCategoryNo", id);
        meetProfessorRightFragment2.onRefresh(null);
    }

    @Override
    public void getData() {
        super.getData();
        presenter.getHome();
        presenter.getType();
        presenter.getHotType();
        presenter.getVideoOnline();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getHome();
        presenter.getType();
        presenter.getHotType();
        presenter.getVideoOnline();

    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        presenter.getHome();
        presenter.getHotType();
        meetProfessorLeftFragment.onRefresh(null);
        meetProfessorRightFragment.onRefresh(null);
        meetProfessorRightFragment2.onRefresh(null);
    }


    @Override
    public void onGetFieldList(final List<FaqHotExpertFieldChose> faqHotExpertFieldChoses) {
        this.faqHotExpertFieldChoses.clear();
        this.faqHotExpertFieldChoses.addAll(faqHotExpertFieldChoses);


        if (faqHotExpertFieldChoses.size() == 4) {
            faqCell1.setText(faqHotExpertFieldChoses.get(0).expertCategoryName);
            faqCell1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ARouter.getInstance()
                            .build(AppRoutes.MODULE_EXPERT_LEFT)
                            .withString("expertCategoryName",faqHotExpertFieldChoses.get(0).expertCategoryName+"")
                            .withString("expertCategoryNo",faqHotExpertFieldChoses.get(0).expertCategoryNo+"")
                            .withString("cityNo", get("addressCity").toString())
                            .navigation();
                }
            });
            faqCell2.setText(faqHotExpertFieldChoses.get(1).expertCategoryName);
            faqCell2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ARouter.getInstance()
                            .build(AppRoutes.MODULE_EXPERT_LEFT)
                            .withString("expertCategoryName",faqHotExpertFieldChoses.get(1).expertCategoryName+"")
                            .withString("expertCategoryNo",faqHotExpertFieldChoses.get(1).expertCategoryNo+"")
                            .withString("cityNo", get("addressCity").toString())
                            .navigation();
                }
            });
            faqCell3.setText(faqHotExpertFieldChoses.get(2).expertCategoryName);
            faqCell3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ARouter.getInstance()
                            .build(AppRoutes.MODULE_EXPERT_LEFT)
                            .withString("expertCategoryName",faqHotExpertFieldChoses.get(2).expertCategoryName+"")
                            .withString("expertCategoryNo",faqHotExpertFieldChoses.get(2).expertCategoryNo+"")
                            .withString("cityNo", get("addressCity").toString())
                            .navigation();
                }
            });
            faqCell4.setText(faqHotExpertFieldChoses.get(3).expertCategoryName);
            faqCell4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ARouter.getInstance()
                            .build(AppRoutes.MODULE_EXPERT_LEFT)
                            .withString("expertCategoryName",faqHotExpertFieldChoses.get(3).expertCategoryName+"")
                            .withString("expertCategoryNo",faqHotExpertFieldChoses.get(3).expertCategoryNo+"")
                            .withString("cityNo", get("addressCity").toString())
                            .navigation();
                }
            });
        }


    }

    @Override
    public void onGetHotFieldList(List<FaqHotExpertFieldChose> faqHotExpertFieldChoses) {
//                addTypes(hotproImgs, faqHotExpertFieldChoses);
    }

    @Override
    public void onGetHomeSuccess(Faq expertInfoModel) {
        //System.out.println("回来了");
        if (expertInfoModel != null) {
            //System.out.println("不空");
//            List<Integer> banners=new ArrayList<>();
//            banners.add(R.drawable.post_insert_expoet);
//            mallBanner.setDelayTime(3000);
//            mallBanner.setImageLoader(new BannerImageLoader(TransformUtil.dp2px(mContext, 10f)));
//            mallBanner.setImages(banners);
//            mallBanner.start();
//            addTypes(hotproImgs, expertInfoModel.hotExpertInfoDTOList);
        }


    }

    @Override
    public void onGetVideoSuccess(List<FaqVideo> list) {
        findViewById(R.id.video_recycle_bg).setVisibility(View.VISIBLE);
        if (list == null || list.size() == 0) {

            findViewById(R.id.video_recycle_bg).setVisibility(View.GONE);
        }


        List<FaqVideo> listtmp = new ArrayList<>();
        if (list != null) {
            if (list.size() > 3) {
                listtmp.add(list.get(0));
                listtmp.add(list.get(1));
                listtmp.add(list.get(2));
            } else {
                for (int i = 0; i < list.size(); i++) {
                    listtmp.add(list.get(i));
                }
            }
        }
        videoListAdapter.setNewData(listtmp);
    }

    @Override
    public void subVideoSucess() {
        presenter.getVideoOnline();
    }

    @Override
    public void onRequestFinish() {
        layoutRefresh.finishRefresh();
        layoutRefresh.finishLoadMore();
    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.top_bar);
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        appBar = (AppBarLayout) findViewById(R.id.app_bar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        st = (SlidingTabLayout) findViewById(R.id.st);
        vp = (ViewPager) findViewById(R.id.vp);
        needS = (RelativeLayout) findViewById(R.id.need_s);
        clAdapterBottom = (ConstraintLayout) findViewById(R.id.clAdapterBottom);
        viewBottom = (View) findViewById(R.id.view_bottom);
        textView = (TextView) findViewById(R.id.textView);
        ivClose = (View) findViewById(R.id.ivClose);
        tvAskExpert = (TextView) findViewById(R.id.tvAskExpert);
        tvReward = (TextView) findViewById(R.id.tvReward);
        tvArea = (ImageTextView) findViewById(R.id.tv_area);

        moreHot = (TextView) findViewById(R.id.moreHot);
        postimage = (ImageView) findViewById(R.id.postimage);
        dividerStore = (View) findViewById(R.id.divider_store);
        videoRecycle = (RecyclerView) findViewById(R.id.video_recycle);
        faqTopShowLL = (ConstraintLayout) findViewById(R.id.faq_top_showLL);
        topIc1LL = (ConstraintLayout) findViewById(R.id.top_ic1LL);
        topIc2LL = (ConstraintLayout) findViewById(R.id.top_ic2LL);
        topIc3LL = (ConstraintLayout) findViewById(R.id.top_ic3LL);
        topIc4LL = (ConstraintLayout) findViewById(R.id.top_ic4LL);
        faqCellShow1 = (TextView) findViewById(R.id.faq_cell_show1);
        faqCellShow2 = (TextView) findViewById(R.id.faq_cell_show2);
        faqCellShow3 = (TextView) findViewById(R.id.faq_cell_show3);
        faqCellShow4 = (TextView) findViewById(R.id.faq_cell_show4);
        faqCell1 = (TextView) findViewById(R.id.faq_cell1);
        faqCell2 = (TextView) findViewById(R.id.faq_cell2);
        faqCell3 = (TextView) findViewById(R.id.faq_cell3);
        faqCell4 = (TextView) findViewById(R.id.faq_cell4);
    }

    @Override
    public void clickSub(FaqVideo indexVideo) {
        Map<String, Object> map = new HashMap<>();
        map.put("course_id", indexVideo.course_id + "");
        presenter.subVideo(map);
    }
}
