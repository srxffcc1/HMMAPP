package com.health.servicecenter.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.health.servicecenter.R;
import com.health.servicecenter.adapter.AppointmentDetailBodyAdapter;
import com.health.servicecenter.adapter.AppointmentDetailSpecAdapter;
import com.health.servicecenter.adapter.AppointmentDetailStoreAdapter;
import com.health.servicecenter.adapter.AppointmentDetailTopAdapter;
import com.health.servicecenter.adapter.MallGoodsDetialStoreAdapter;
import com.health.servicecenter.contract.AppointmentMainContract;
import com.health.servicecenter.presenter.AppointmentMainPresenter;
import com.health.servicecenter.widget.AttributeSelectDataDialog;
import com.healthy.library.adapter.ItemDecorationAdapter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.business.SeckShareDialog;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.constant.SpKey;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.model.AppointmentListItemModel;
import com.healthy.library.model.AppointmentMainItemModel;
import com.healthy.library.model.AppointmentTimeSettingModel;
import com.healthy.library.model.ShopDetailModel;
import com.healthy.library.model.TabEntity;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.FastClickUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.AutoClickImageView;
import com.healthy.library.widget.StatusLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(path = ServiceRoutes.SERVICE_APPOINTMENTDETIAL)
public class AppointmentDetailActivity extends BaseActivity implements IsFitsSystemWindows, BaseAdapter.OnOutClickListener, AppointmentMainContract.View {

    @Autowired
    String projectId;
    @Autowired
    String shopId;
    @Autowired
    String merchantId;

    private String[] titles = {"??????", "??????", "????????????", "????????????"};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private StatusLayout layoutStatus;
    private SmartRefreshLayout layoutRefresh;
    private RecyclerView recyclerView;
    private ImageView scrollUp;
    private ImageView ivBottomShader;
    private TextView servesUnder;
    private LinearLayout goodsUnder;
    private ConstraintLayout topTitle;
    private AutoClickImageView imgBack;
    private CommonTabLayout topTab;
    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;
    private AppointmentDetailTopAdapter appointmentDetailTopAdapter;
    private AppointmentDetailSpecAdapter appointmentDetailSpecAdapter;
    private AppointmentDetailStoreAdapter appointmentDetailStoreAdapter;
    private AppointmentDetailBodyAdapter mDetailBodyAdapter;
    private MallGoodsDetialStoreAdapter mallGoodsDetialStoreAdapter;
    private AppointmentMainPresenter mMainPresenter;
    private Map<String, Object> mMap = new HashMap<>();
    private AppointmentMainItemModel mDetailModel;
    private int dxall = 0;
    private boolean isScrolled = true;

    private int[] poss;
    private int tab1 = 0;
    private int tab2 = 1;
    private int tab3 = 3;
    private int tab4 = 4;
    private View mIvTopShader;
    private AttributeSelectDataDialog mSelectDataDialog;
    //0 ?????? -1 ?????????
    private AppointmentMainItemModel.AttributeList mAttribute;
    private boolean isAttributeListEmpty;
    private String modelPriceType;
    private ItemDecorationAdapter itemDecorationAdapter;
    private ImageView imgShare;
    private ShopDetailModel mStoreDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_appointment_detial;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        showContent();
        buildRecyclerView();
        buildTabNow();
        mTabEntities.clear();
        for (int i = 0; i < titles.length; i++) {
            //??????tab??????
            mTabEntities.add(new TabEntity(titles[i], 0, 0));
        }
        topTab.setTabData(mTabEntities);
        buildTopTab(0);
        mMainPresenter = new AppointmentMainPresenter(this, this);
        if(TextUtils.isEmpty(merchantId)){
            merchantId = SpUtils.getValue(mContext, SpKey.CHOSE_MC);
        }
        getData();
    }

    @Override
    public void getData() {
        super.getData();
        dxall = 0;
        tab1 = 0;
        tab2 = 1;
        tab3 = 3;
        tab4 = 4;
        buildTabNow();
        mMap.clear();
        mMap.put("shopId", shopId);
        if (mStoreDetail == null) {
            mMainPresenter.getStoreDetail(mMap);
        } else {
            mMap.put("projectId", projectId);
            mMap.put("merchantId", merchantId);
            mMainPresenter.getMainDetail(mMap);
        }
    }

    private void buildRecyclerView() {
        virtualLayoutManager = new VirtualLayoutManager(mContext);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        recyclerView.setLayoutManager(virtualLayoutManager);
        recyclerView.setAdapter(delegateAdapter);

        appointmentDetailTopAdapter = new AppointmentDetailTopAdapter();
        delegateAdapter.addAdapter(appointmentDetailTopAdapter);
        appointmentDetailTopAdapter.setOutClickListener(this);

        appointmentDetailSpecAdapter = new AppointmentDetailSpecAdapter();
        //????????????
        appointmentDetailSpecAdapter.setOnSelectAttributeListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!FastClickUtils.isFastClick()) {
                    mSelectDataDialog.show(getSupportFragmentManager(), "");
                }
            }
        });
        delegateAdapter.addAdapter(appointmentDetailSpecAdapter);
        appointmentDetailSpecAdapter.setOutClickListener(this);

        // ??????????????????
        mDetailBodyAdapter = new AppointmentDetailBodyAdapter();
        delegateAdapter.addAdapter(mDetailBodyAdapter);

        appointmentDetailStoreAdapter = new AppointmentDetailStoreAdapter();
        delegateAdapter.addAdapter(appointmentDetailStoreAdapter);
        appointmentDetailStoreAdapter.setOutClickListener(this);

        mallGoodsDetialStoreAdapter = new MallGoodsDetialStoreAdapter();
        mallGoodsDetialStoreAdapter.setAppointmentDetail(true);
        delegateAdapter.addAdapter(mallGoodsDetialStoreAdapter);

        // 2021-04-24 ??????????????????
        itemDecorationAdapter = new ItemDecorationAdapter();
        delegateAdapter.addAdapter(itemDecorationAdapter);

    }

    @Override
    protected void findViews() {
        super.findViews();
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        scrollUp = (ImageView) findViewById(R.id.scrollUp);
        ivBottomShader = (ImageView) findViewById(R.id.iv_bottom_shader);
        servesUnder = (TextView) findViewById(R.id.servesUnder);
        goodsUnder = (LinearLayout) findViewById(R.id.goodsUnder);
        topTitle = (ConstraintLayout) findViewById(R.id.topTitle);
        imgBack = (AutoClickImageView) findViewById(R.id.img_back);
        topTab = (CommonTabLayout) findViewById(R.id.topTab);
        imgShare = (ImageView) findViewById(R.id.img_share);
        mIvTopShader = findViewById(R.id.iv_top_shader);
        layoutRefresh.finishLoadMoreWithNoMoreData();
        initListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDetailBodyAdapter.onDestroy();
    }

    private void initListener() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDetailModel != null) {
                    SeckShareDialog seckShareDialog = SeckShareDialog.newInstance();
                    seckShareDialog.setActivityDialog(7, 0, mDetailModel);
                    seckShareDialog.setExtraMap(new SimpleHashMapBuilder<String, String>()
                            .puts("shopId", shopId)
                            .puts("projectId", projectId)
                            .puts("merchantId", merchantId)
                            .puts("scheme", "AppointmentDetail")
                            .puts("type", "8"));
                    seckShareDialog.show(getSupportFragmentManager(), "AppointmentShare");
                }
            }
        });

        goodsUnder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //04-13??????????????????????????? ???????????????????????????????????????????????????????????????1:?????? 2:??????????????????????????????????????????
                if (!isAttributeListEmpty /*&& mAttribute == null*/ && !FastClickUtils.isFastClick() && "2".equals(modelPriceType)) {
                    mSelectDataDialog.show(getSupportFragmentManager(), "");
                    return;
                }
                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_APPOINTMENTPAY)
                        .withString("projectId", projectId)
                        .withString("merchantId",merchantId)
                        .withSerializable("storeDetail", mStoreDetail)
                        .withSerializable("attribute", mAttribute)
                        .navigation();
            }
        });

        scrollUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                //////System.out.println("???????????????");
                topTab.setCurrentTab(0);
                virtualLayoutManager.scrollToPositionWithOffset(tab1, 0);
                //recyclerView.scrollToPosition(0);
                //moveToPosition(virtualLayoutManager, recyclerView, 0);
                buildTopTab(0);
                dxall = 0;
            }
        });


        topTab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if (position == 0) {
                    dxall = 0;
                    buildTopTab(dxall);
                    ////System.out.println("???????????????:" + tab1);
                    virtualLayoutManager.scrollToPositionWithOffset(tab1, 0);
                }
                if (position == 1) {
                    ////System.out.println("???????????????:" + tab2);
                    virtualLayoutManager.scrollToPositionWithOffset(tab2, 0);
                }
                if (position == 2) {
                    ////System.out.println("?????????????????????:" + tab3);
                    virtualLayoutManager.scrollToPositionWithOffset(tab3, 0);
                }
                if (position == 3) {
                    ////System.out.println("?????????????????????:" + tab4);
                    virtualLayoutManager.scrollToPositionWithOffset(tab4, 0);
                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                dxall += dy;
                if (dxall < 0) {
                    dxall = 0;
                }

                // ////System.out.println("????????????:"+dxall);
                int firstVisibleItemPosition = virtualLayoutManager.findFirstVisibleItemPosition();
                ////System.out.println("??????????????????????????? Position = " + firstVisibleItemPosition);
                int lastVisibleItemPosition = virtualLayoutManager.findLastVisibleItemPosition();
                ////System.out.println("?????????????????????????????? Position = " + lastVisibleItemPosition);
                int firstCompletelyVisibleItemPosition = virtualLayoutManager.findFirstCompletelyVisibleItemPosition();
                buildTopTab((dxall > getResources().getDimension(R.dimen.status_2020) * 2 || firstVisibleItemPosition > 0) && (firstCompletelyVisibleItemPosition != 0) ? 1 : 0);

                if (isScrolled) {
                    int pos = 0;
                    if (firstVisibleItemPosition != -1) {
                        if (!recyclerView.canScrollVertically(1)) {
                            //????????????
                            pos = titles.length - 1;
                        } else if (firstVisibleItemPosition == poss[poss.length - 1]) {
                            //??????top?????????????????????????????????tab?????????
                            pos = poss[poss.length - 1];
                        } else {
                            for (int i = 0; i < poss.length - 1; i++) {
                                if (firstVisibleItemPosition == poss[i]) {
                                    pos = i;
                                    break;
                                }
                                if (firstVisibleItemPosition > poss[i] && firstVisibleItemPosition < poss[i + 1]) {
                                    pos = i;
                                    break;
                                }
                            }
                        }
                        topTab.setCurrentTab(pos);
                    }
                }

            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == 0) {
                    isScrolled = false;
                } else {
                    isScrolled = true;
                }
            }
        });

        layoutRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getData();
            }
        });
    }

    private void buildTabNow() {
        if (poss == null) {
            poss = new int[]{tab1, tab2, tab3, tab4};
        } else {
            poss[0] = tab1;
            poss[1] = tab2;
            poss[2] = tab3;
            poss[3] = tab4;
        }
    }

    private void buildTopTab(int firstCompletelyVisibleItemPosition) {
        if (firstCompletelyVisibleItemPosition == 0) {
            //////System.out.println("??????????????????");
            topTab.setVisibility(View.INVISIBLE);
            scrollUp.setVisibility(View.GONE);
            imgBack.setImageResource(R.drawable.index_ic_back_b);
            imgShare.setImageResource(R.drawable.index_ic_share_b);
            topTitle.setBackgroundColor(Color.parseColor("#00000000"));
            mIvTopShader.setVisibility(View.GONE);
        } else {
            topTab.setVisibility(View.VISIBLE);
            mIvTopShader.setVisibility(View.VISIBLE);
            scrollUp.setVisibility(View.VISIBLE);
            topTitle.setBackgroundColor(Color.parseColor("#ffffff"));
            imgShare.setImageResource(R.drawable.index_ic_share_c);
            imgBack.setImageResource(R.drawable.index_ic_back_c);
        }
    }

    @Override
    public void outClick(@NotNull String function, @NotNull Object obj) {

    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        layoutRefresh.finishRefresh();
    }

    @Override
    public void onGetMainListSuccess(List<AppointmentMainItemModel> modelList) {

    }

    /**
     * ????????????
     *
     * @param newStoreDetailModel
     */
    @Override
    public void onGetStoreDetailSuccess(ShopDetailModel newStoreDetailModel) {
        this.mStoreDetail = newStoreDetailModel;
        //????????????????????????
        getData();
    }

    @Override
    public void onGetMainDetailSuccess(AppointmentMainItemModel detailModel) {
        showContent();
        layoutRefresh.finishRefresh();
        layoutRefresh.finishLoadMoreWithNoMoreData();
        goodsUnder.setVisibility(View.VISIBLE);
        if (detailModel != null) {
            this.mDetailModel = detailModel;
            modelPriceType = detailModel.getPriceType();
            appointmentDetailTopAdapter.setModel(detailModel);
            appointmentDetailSpecAdapter.setModel(detailModel);
            mDetailBodyAdapter.setModel(detailModel);
            appointmentDetailStoreAdapter.setModel(detailModel);
            mallGoodsDetialStoreAdapter.setModel(mStoreDetail);
            //2021-04-24 ??????????????????
            itemDecorationAdapter.setModel("");
            List<AppointmentMainItemModel.AttributeList> attributeList = detailModel.getAttributeList();
            isAttributeListEmpty = ListUtil.isEmpty(attributeList);
            //??????????????????
            if ("1".equals(modelPriceType)) {
                mAttribute = detailModel.getAttribute();
            } else {
                //2021-04-24 ???????????????????????????????????????????????????
                mAttribute = isAttributeListEmpty ? null : attributeList.get(0);
            }
            //??????????????????????????????????????????
            if (!isAttributeListEmpty && mSelectDataDialog == null) {
                mSelectDataDialog = AttributeSelectDataDialog.newInstance(detailModel);
                mSelectDataDialog.setOnTagClickListener(mOnTagClickListener);
            }
        }
    }

    @Override
    public void onGetTimeSettingSuccess(AppointmentTimeSettingModel timeSettingModel) {

    }

    @Override
    public void onGetPayInfoSuccess(Map<String, Object> payInfoMap) {

    }

    @Override
    public void onAddServiceSuccess(long id, String payOrderId) {

    }

    @Override
    public void getPayStatusSuccess(String status) {

    }

    /**
     * ??????????????????
     */
    private AttributeSelectDataDialog.OnTagClickListener mOnTagClickListener = new AttributeSelectDataDialog.OnTagClickListener() {
        @Override
        public void onTagClick(AppointmentMainItemModel.AttributeList attribute, boolean isSubmitClick) {
            //??????????????????
            mAttribute = attribute;
            if (attribute == null) {
                appointmentDetailTopAdapter.notifyDataSetChanged();
                appointmentDetailSpecAdapter.notifyDataSetChanged();
            } else {
                appointmentDetailTopAdapter.setGoodsMoney(mAttribute.getPrice());
                appointmentDetailSpecAdapter.setData(attribute);
                //04-13 ??????????????????????????????????????????????????????????????????
                if (isSubmitClick) {
                    ARouter.getInstance()
                            .build(ServiceRoutes.SERVICE_APPOINTMENTPAY)
                            .withString("projectId", projectId)
                            .withString("merchantId",merchantId)
                            .withSerializable("storeDetail", mStoreDetail)
                            .withSerializable("attribute", mAttribute)
                            .navigation();
                }
            }
        }
    };
}