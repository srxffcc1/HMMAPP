package com.health.second.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.health.second.R;
import com.health.second.adapter.SecondMainEmptyAdapter;
import com.health.second.adapter.SecondMainGoodsAdapter;
import com.health.second.adapter.SecondMainStoreAdapter;
import com.health.second.contract.SecondGoodsSortContract;
import com.health.second.contract.SecondTypeContract;
import com.healthy.library.model.SecondSortGoods;
import com.health.second.model.SecondSortShop;
import com.health.second.model.SecondType;
import com.health.second.presenter.SecondGoodsSortPresenter;
import com.health.second.presenter.SecondTypePresenter;
import com.healthy.library.LibApplication;
import com.healthy.library.adapter.DropDownType;
import com.healthy.library.adapter.FragmentStatePagerItemAdapter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.builder.ObjectIteraor;
import com.healthy.library.builder.SimpleArrayListBuilder;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.business.GridDropDownPopNoBack;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.constant.Block;
import com.healthy.library.constant.SpKey;
import com.healthy.library.fragment.EmptyLayoutFragment;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.model.GoodsDetail;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.model.ProvinceCityModel;
import com.healthy.library.routes.SecondRoutes;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.ImageTextView;
import com.healthy.library.widget.StatusLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Route(path = SecondRoutes.MAIN_SEACH)
public class SecondSeachActivity extends BaseActivity implements TextView.OnEditorActionListener, BaseAdapter.OnOutClickListener, SecondTypeContract.View, SecondGoodsSortContract.View, OnRefreshLoadMoreListener, IsFitsSystemWindows {

    SecondMainGoodsAdapter secondMainGoodsAdapter;
    SecondMainStoreAdapter secondMainStoreAdapter;
    SecondMainEmptyAdapter secondMainEmptyAdapter;
    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;

    private SmartRefreshLayout refreshLayout;
    private StatusLayout layoutStatus;
    private RecyclerView recyclerview;
    private RelativeLayout seachTopLL;
    private LinearLayout seachTopBgLL;
    private LinearLayout seachTop;
    private ImageTextView disLoc;
    private LinearLayout serarchKeyWordLL;
    private TextView serarchKeyWord;
    private LinearLayout typeLL;
    private SlidingTabLayout tabType;
    private ViewPager typeVp;
    private List<String> distancetitles = Arrays.asList("商家", "商品");

    public String nowTab = "商家";

    int alldy = 0;
    private List<String> titles = new ArrayList<>();
    private List<Fragment> fragments = new ArrayList<>();
    private FragmentStatePagerItemAdapter fragmentPagerItemAdapter;
    private LinearLayout seachTopTmp;
    private LinearLayout seachTopBgNoLL;
    private LinearLayout distanceTopLL;
    private SlidingTabLayout st;
    private TextView area;
    private LinearLayout distanceLL;
    private TextView txtPrice;
    private android.widget.ImageView priceUpImg;
    private android.widget.ImageView priceDownImg;

    private LinearLayout shopTabLL;
    private LinearLayout areaParent;
    private ImageView areaAr;
    private TextView distanceTxt;
    private ImageView distanceUpImg;
    private ImageView distanceDownImg;
    private LinearLayout goodsTabLL;
    private LinearLayout saleLL;
    private TextView saleTxt;
    private ImageView saleUpImg;
    private ImageView saleDownImg;
    private LinearLayout priceLL;
    private TextView priceTxt;

    public int page = 1;
    public int pageSize = 10;
    public String areaString = "全城";
    //----------------------------------筛选用到的字段-------------------------------
    public String areasCode;
    public String distanceSort = "asc";//desc
    public String appSalesSort = "desc";//ase
    public String platformPriceSort = "";//desc
    public String goodsType;
    @Autowired
    public String categoryIds;
    public boolean isTabClick = false;//
    //----------------------------------筛选用到的字段-------------------------------


    SecondTypePresenter secondTypePresenter;
    SecondGoodsSortPresenter secondGoodsSortPresenter;
    private List<ProvinceCityModel> provinceCityModels = new ArrayList<>();
    GridDropDownPopNoBack locationDropDownPop;
    private ImageView clearEdit;
    private TextView seachButton;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_second_seach;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        System.out.println("输入类别:" + categoryIds);
        refreshLayout.setOnRefreshLoadMoreListener(this);
        refreshLayout.finishLoadMoreWithNoMoreData();
//        areasCode=LocUtil.getAreaNo(mContext,SpKey.LOC_CHOSE);
//        areaString=LocUtil.getAreaName(mContext,SpKey.LOC_CHOSE);
        buildRecyclerView();
        secondTypePresenter = new SecondTypePresenter(this, this);
        secondGoodsSortPresenter = new SecondGoodsSortPresenter(this, this);
        secondTypePresenter.getLocationList(LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE));
        secondTypePresenter.getTypeMenu(new SimpleHashMapBuilder<String, Object>()
                .puts("partnerId", SpUtils.getValue(mContext, SpKey.CHOSE_PARTNERID)));
        getData();
    }

    @Override
    public void getData() {
        super.getData();

    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
    }

    private void buildRecyclerView() {
        virtualLayoutManager = new VirtualLayoutManager(mContext);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        recyclerview.setLayoutManager(virtualLayoutManager);
        recyclerview.setAdapter(delegateAdapter);

        secondMainEmptyAdapter = new SecondMainEmptyAdapter();
        delegateAdapter.addAdapter(secondMainEmptyAdapter);

        secondMainGoodsAdapter = new SecondMainGoodsAdapter();
        delegateAdapter.addAdapter(secondMainGoodsAdapter);
        secondMainGoodsAdapter.setOutClickListener(this);

        secondMainStoreAdapter = new SecondMainStoreAdapter();
        delegateAdapter.addAdapter(secondMainStoreAdapter);
        st.setViewPager(null, (String[]) distancetitles.toArray());
        st.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                nowTab = distancetitles.get(position);
                changeTabWithLine();
                onRefresh(null);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        changeTabWithLine();
        disLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        areaParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (provinceCityModels.size() > 0) {
                    openLocDialog(area, areaAr);
                }
            }
        });

        seachButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTab(false, nowTab);
            }
        });
        serarchKeyWord.setOnEditorActionListener(this);
//        serarchKeyWord.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                InputMethodManager inputMethodManager = (InputMethodManager) ((Activity) mContext).getSystemService(Context.INPUT_METHOD_SERVICE);
//                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//                serarchKeyWord.requestFocus();
//            }
//        }, 300);
        serarchKeyWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    clearEdit.setVisibility(View.VISIBLE);
                } else {
                    clearEdit.setVisibility(View.GONE);
                }
            }
        });
        clearEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serarchKeyWord.setText("");
                clearEdit.setVisibility(View.GONE);
                changeTab(false, nowTab);
            }
        });


        distanceLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ("desc".equals(distanceSort)) {
                    distanceSort = "asc";
                } else {
                    distanceSort = "desc";
                }
                setOrderArrow(distanceUpImg, distanceDownImg, distanceSort);
                outClick("请求底部数据", null);


            }
        });
        priceLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ("desc".equals(platformPriceSort)) {
                    platformPriceSort = "asc";
                } else {
                    platformPriceSort = "desc";
                }
                appSalesSort = "";
                setOrderArrow(saleUpImg, saleDownImg, appSalesSort);
                setOrderArrow(priceUpImg, priceDownImg, platformPriceSort);

                outClick("请求底部数据", null);


            }
        });
        saleLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ("desc".equals(appSalesSort)) {
                    appSalesSort = "asc";
                } else {
                    appSalesSort = "desc";
                }
                platformPriceSort = "";
                setOrderArrow(priceUpImg, priceDownImg, platformPriceSort);
                setOrderArrow(saleUpImg, saleDownImg, appSalesSort);

                outClick("请求底部数据", null);


            }
        });

        if ("全城".equals(areaString)) {
            setStrengthArrowNo(areaAr, true);
        } else {
            setStrengthArrowRNo(areaAr, true);
        }
        setStrengthArrowNo(area, areaString);

    }

    private void changeTabWithLine() {
        if ("商家".equals(nowTab)) {
            shopTabLL.setVisibility(View.VISIBLE);
            goodsTabLL.setVisibility(View.GONE);
        } else {
            shopTabLL.setVisibility(View.GONE);
            goodsTabLL.setVisibility(View.VISIBLE);
        }
    }


    private void openLocDialog(final TextView area, final ImageView areaAr) {
        if (locationDropDownPop == null) {
            locationDropDownPop = new GridDropDownPopNoBack(mContext, new GridDropDownPopNoBack.ItemClickCallBack() {
                @Override
                public void click(String id, String name) {
                    areasCode = id;
                    area.setText(name);
                    areaString = name;
                    distanceSort = "asc";
                    outClick("请求底部数据", null);
                    setStrengthArrow(area, name);
                    if ("全城".equals(name)) {
                        setStrengthArrow(areaAr, false);
                    } else {
                        setStrengthArrowR(areaAr, false);
                    }

                }

                @Override
                public void dismiss() {
                    if ("全城".equals(areaString)) {
                        setStrengthArrow(areaAr, true);
                    } else {
                        setStrengthArrowR(areaAr, true);
                    }
                }
            });
            List<DropDownType> droplist = new ArrayList<>();
            droplist.add(new DropDownType("", "全城"));
            boolean iscodeinarea = false;
            for (int i = 0; i < provinceCityModels.size(); i++) {
                droplist.add(new DropDownType(provinceCityModels.get(i).getAreaNo(), provinceCityModels.get(i).getName()));
                if (provinceCityModels.get(i).getAreaNo().equals(areasCode)) {
                    iscodeinarea = true;
                }
            }
            locationDropDownPop.setData(droplist);
            locationDropDownPop.setSelectId(areasCode);
        }
        if ("全城".equals(areaString)) {
            setStrengthArrow(areaAr, false);
        } else {
            setStrengthArrowR(areaAr, false);
        }
        locationDropDownPop.showPopupWindow(area);
    }


    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    private void initView() {
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refresh_layout);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        seachTopLL = (RelativeLayout) findViewById(R.id.seachTopLL);
        seachTopBgLL = (LinearLayout) findViewById(R.id.seachTopBgLL);
        seachTop = (LinearLayout) findViewById(R.id.seachTop);
        disLoc = (ImageTextView) findViewById(R.id.dis_loc);
        serarchKeyWordLL = (LinearLayout) findViewById(R.id.serarchKeyWordLL);
        serarchKeyWord = (TextView) findViewById(R.id.serarchKeyWord);
        typeLL = (LinearLayout) findViewById(R.id.seachTopTmp);
        tabType = (SlidingTabLayout) findViewById(R.id.tabType);
        typeVp = (ViewPager) findViewById(R.id.typeVp);
        seachTopTmp = (LinearLayout) findViewById(R.id.seachTopTmp);
        seachTopBgNoLL = (LinearLayout) findViewById(R.id.seachTopBgNoLL);
        distanceTopLL = (LinearLayout) findViewById(R.id.distanceTopLL);
        st = (SlidingTabLayout) findViewById(R.id.st);
        area = (TextView) findViewById(R.id.area);
        distanceLL = (LinearLayout) findViewById(R.id.distanceLL);
        txtPrice = (TextView) findViewById(R.id.distanceTxt);
        priceUpImg = (ImageView) findViewById(R.id.price_up_img);
        priceDownImg = (ImageView) findViewById(R.id.price_down_img);
        shopTabLL = (LinearLayout) findViewById(R.id.shopTabLL);
        areaParent = (LinearLayout) findViewById(R.id.areaParent);
        areaAr = (ImageView) findViewById(R.id.areaAr);
        distanceTxt = (TextView) findViewById(R.id.distanceTxt);
        distanceUpImg = (ImageView) findViewById(R.id.distance_up_img);
        distanceDownImg = (ImageView) findViewById(R.id.distance_down_img);
        goodsTabLL = (LinearLayout) findViewById(R.id.goodsTabLL);
        saleLL = (LinearLayout) findViewById(R.id.saleLL);
        saleTxt = (TextView) findViewById(R.id.saleTxt);
        saleUpImg = (ImageView) findViewById(R.id.sale_up_img);
        saleDownImg = (ImageView) findViewById(R.id.sale_down_img);
        priceLL = (LinearLayout) findViewById(R.id.priceLL);
        priceTxt = (TextView) findViewById(R.id.priceTxt);
        clearEdit = (ImageView) findViewById(R.id.clearEdit);
        seachButton = (TextView) findViewById(R.id.seachButton);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        changeTab(false, nowTab);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = 1;
        changeTab(false, nowTab);
    }

    @Override
    public void onGetActSortGoodsSuccess(List<SecondSortGoods> result, PageInfoEarly pageInfo) {
        secondMainEmptyAdapter.setModel(null);
        if (result == null || result.size() == 0) {
            refreshLayout.finishLoadMoreWithNoMoreData();
            if (page == 1 || page == 0) {
                showAdpaterEmpty();
            }
        } else {
            if (page == 1) {
                clearAllUnderAdpaterEmpty();
                ArrayList<SecondSortGoods> tmp = new ArrayList<>();
                for (int i = 0; i < result.size(); i++) {
                    tmp.add(result.get(i));
                }
                secondMainGoodsAdapter.setData(tmp);
            } else {
                ArrayList<SecondSortGoods> tmp = new ArrayList<>();
                for (int i = 0; i < result.size(); i++) {
                    tmp.add(result.get(i));
                }
                secondMainGoodsAdapter.addDatas(tmp);

            }
            if (pageInfo.nextPage == 0) {
                refreshLayout.finishLoadMoreWithNoMoreData();
            } else {
                refreshLayout.setNoMoreData(false);
                refreshLayout.setEnableLoadMore(true);
            }
        }
    }

    private void clearAllUnderAdpaterEmpty() {
        secondMainGoodsAdapter.clear();
        secondMainStoreAdapter.clear();
        if (isTabClick) {
            virtualLayoutManager.scrollToPositionWithOffset(delegateAdapter.getItemCount() - 1, 0);
            isTabClick = false;
        }
    }

    private void showAdpaterEmpty() {
        secondMainEmptyAdapter.setModel("null");
        clearAllUnderAdpaterEmpty();
    }

    @Override
    public void onGetActSortShopsSuccess(List<SecondSortShop> result, PageInfoEarly pageInfo) {
        secondMainEmptyAdapter.setModel(null);
        if (result == null || result.size() == 0) {
            refreshLayout.finishLoadMoreWithNoMoreData();
            if (page == 1 || page == 0) {
                showAdpaterEmpty();
            }
        } else {
            clearAllUnderAdpaterEmpty();
            ArrayList<SecondSortShop> tmp = new ArrayList<>();
            for (int i = 0; i < result.size(); i++) {
                tmp.add(result.get(i));
            }
            secondMainStoreAdapter.setData(tmp);
            refreshLayout.finishLoadMoreWithNoMoreData();

        }
    }

    @Override
    public void onGetActSortGoodDetailSucess(GoodsDetail result) {

    }

    @Override
    public void addShopCatSuccess(String result) {
        showToast("已加入购物车");

    }

    @Override
    public void onGetFirstTypeMenuSucess(List<SecondType> list) {

    }

    @Override
    public void onGetRecommendTypeMenuSucess(List<SecondType.SecondTypeMenu> list) {

    }

    @Override
    public void onGetTypeMenuSucess(final List<SecondType.SecondTypeMenu> list) {
        if (list != null && list.size() > 0) {
            list.add(0, new SecondType.SecondTypeMenu("全部", null));
            if (TextUtils.isEmpty(categoryIds)) {
                categoryIds = list.get(0).goodsCategoryIdList;
            }
            titles.clear();
            titles.addAll(new SimpleArrayListBuilder<String>().putList(list, new ObjectIteraor<SecondType.SecondTypeMenu>() {
                @Override
                public String getDesObj(SecondType.SecondTypeMenu o) {
                    return o.name;
                }
            }));
            fragments.clear();
            for (int i = 0; i < titles.size(); i++) {
                fragments.add(EmptyLayoutFragment.newInstance(null));
            }
            if (fragmentPagerItemAdapter == null) {
                fragmentPagerItemAdapter = new FragmentStatePagerItemAdapter(getSupportFragmentManager(), fragments, titles);
                // adapter
                typeVp.setAdapter(fragmentPagerItemAdapter);
            } else {
                fragmentPagerItemAdapter.setDataSource(fragments, titles);
            }
            typeVp.setOffscreenPageLimit(fragments.size());
            tabType.setViewPager(typeVp, titles);
            tabType.setOnTabSelectListener(new OnTabSelectListener() {
                @Override
                public void onTabSelect(int position) {
                    categoryIds = list.get(position).goodsCategoryIdList;
                    onRefresh(null);
                }

                @Override
                public void onTabReselect(int position) {

                }
            });
            tabType.setCurrentTab(0);
            if (!TextUtils.isEmpty(categoryIds)) {
                for (int i = 0; i < list.size(); i++) {
                    if (categoryIds.equals(list.get(i).goodsCategoryIdList)) {
                        tabType.setCurrentTab(i);
                    }
                }
            }
        }
        changeTab(false, nowTab);
    }

    @Override
    public void onGetTypeMenuBindTypeSucess(List<SecondType.SecondTypeMenu> list, SecondType secondType) {

    }


    public void changeTab(boolean needTabTop, String index) {
        if ("商家".equals(nowTab) && 0 == page && secondMainGoodsAdapter.getDatas().size() > 0) {//就不重复请求了 如果是1
            return;
        }
        if ("商品".equals(nowTab) && 0 == page && secondMainGoodsAdapter.getDatas().size() > 0) {//就不重复请求了 如果是1
            return;
        }
        if (page == 0) {
            page = 1;
        }
        nowTab = index;
        if (page == 1 || page == 0) {
            refreshLayout.setNoMoreData(false);
            refreshLayout.setEnableLoadMore(true);
        }
        if ("商品".equals(nowTab)) {//请求新闻
            secondGoodsSortPresenter.getActSortGoods(new SimpleHashMapBuilder<String, Object>()
                    .puts("publish", 1)
                    .puts("pageNum", page)
                    .puts("pageSize", pageSize)
                    .puts("goodsTitle", serarchKeyWord.getText().toString())
                    .puts("categoryIds", categoryIds == null ? null : categoryIds.split(","))
                    .puts("provinceCode", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE))
                    .puts("cityCode", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE))
                    .puts("areasCode", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE))
                    .puts("appSalesSort", appSalesSort)
                    .puts("platformPriceSort", platformPriceSort)
                    .puts("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC))
            );
        } else {
            secondGoodsSortPresenter.getActSortShops(new SimpleHashMapBuilder<String, Object>()
                    .puts("publish", 1)
                    .puts("level", 3)
                    .puts("goodsTitle", serarchKeyWord.getText().toString())
                    .puts("categoryIds", categoryIds == null ? null : categoryIds.split(","))
                    .puts("provinceCode", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE))
                    .puts("cityCode", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE))
                    .puts("areasCode", areasCode)
                    .puts("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG))
                    .puts("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG))
                    .puts("distanceSort", distanceSort)
                    .puts("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC))


            );
        }
    }

    public void setOrderArrow(ImageView price_up_img, ImageView price_down_img, String distanceOrderBy) {
        if ("desc".equals(distanceOrderBy)) {
            price_up_img.setImageResource(R.mipmap.service_price_sort_black);
            price_down_img.setImageResource(R.mipmap.service_price_sort_red);
        } else if(TextUtils.isEmpty(distanceOrderBy)){
            price_up_img.setImageResource(R.mipmap.service_price_sort_black);
            price_down_img.setImageResource(R.mipmap.service_price_sort_black);
        }else {
            price_up_img.setImageResource(R.mipmap.service_price_sort_red);
            price_down_img.setImageResource(R.mipmap.service_price_sort_black);
        }
    }


    public void setStrengthArrowNo(ImageView area, boolean flag) {

        area.setImageResource(flag ? R.drawable.ic_solid_triangle_down_gray : R.drawable.ic_solid_triangle_up_gray);

    }

    public void setStrengthArrowRNo(ImageView area, boolean flag) {
        area.setImageResource(flag ? R.drawable.ic_solid_triangle_down_red : R.drawable.ic_solid_triangle_up_red);

    }

    public void setStrengthArrowNo(TextView area, String name) {
        if ("全城".equals(name)) {
            area.setTextColor(Color.parseColor("#333333"));
            area.setText("全城");
        } else {
            area.setTextColor(Color.parseColor("#FF5353"));
            area.setText(name);
        }
    }


    public void setStrengthArrow(ImageView areaAr, boolean flag) {
        areaAr.setImageResource(flag ? R.drawable.ic_solid_triangle_down_gray : R.drawable.ic_solid_triangle_up_gray);

    }

    public void setStrengthArrowR(ImageView areaAr, boolean flag) {
        areaAr.setImageResource(flag ? R.drawable.ic_solid_triangle_down_red : R.drawable.ic_solid_triangle_up_red);

    }

    public void setStrengthArrow(TextView area, String name) {
        if ("全城".equals(name)) {
            area.setTextColor(Color.parseColor("#333333"));
            area.setText("全城");
        } else {
            area.setTextColor(Color.parseColor("#FF5353"));
            area.setText(name);
        }

    }

    @Override
    public void onGetLocationListSuccess(List<ProvinceCityModel> provinceCityModels) {
        this.provinceCityModels = provinceCityModels;
    }

    @Override
    public void outClick(String function, Object obj) {
        if ("请求底部数据".equals(function)) {
            page = 1;
            changeTab(true, nowTab);

        }
        if ("加入购物车".equals(function)) {
            SecondSortGoods goodsDetail = (SecondSortGoods) obj;
            if (goodsDetail != null) {
                secondGoodsSortPresenter.addShopCat(new SimpleHashMapBuilder<String, Object>()
                        .puts("shopId", SpUtils.getValue(LibApplication.getAppContext(), SpKey.CHOSE_SHOP))
                        .puts("goodsShopId", goodsDetail.getExShopId())
                        .puts("goodsSource", "1")
                        .puts("goodsType", goodsDetail.goodsType)
                        .puts("goodsId", goodsDetail.id)
                        .puts("goodsSpecId", goodsDetail.goodsChildren != null ? goodsDetail.goodsChildren.get(0).id : null)
                        .puts("goodsQuantity", "1")
                );
            }
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            if (TextUtils.isEmpty(serarchKeyWord.getText().toString())) {

            } else {
                if (Block.checkIsBlockReplace(serarchKeyWord.getText().toString())) {

                } else {

                }
            }
            page = 1;
            onRefresh(null);
        }
        return false;
    }
}
