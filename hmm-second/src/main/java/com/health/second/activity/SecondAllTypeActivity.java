package com.health.second.activity;

import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.health.second.R;
import com.health.second.adapter.SecondAllTypeAdapter;
import com.health.second.contract.SecondTypeContract;
import com.health.second.model.SecondType;
import com.health.second.presenter.SecondTypePresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.constant.SpKey;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.model.ProvinceCityModel;
import com.healthy.library.routes.SecondRoutes;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;

import java.util.ArrayList;
import java.util.List;

@Route(path = SecondRoutes.MAIN_ALLTYPE)
public class SecondAllTypeActivity extends BaseActivity implements SecondTypeContract.View,IsFitsSystemWindows, BaseAdapter.OnOutClickListener {


    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;
    private RecyclerView recyclerview;
    SecondAllTypeAdapter secondAllTypeAdapter;
    private com.healthy.library.widget.TopBar topBar;
    private StatusLayout layoutStatus;
    SecondTypePresenter secondTypePresenter;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_second_alltype;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        secondTypePresenter=new SecondTypePresenter(this,this);
        buildRecyclerView();
        getData();
    }


    private void buildRecyclerView() {
        virtualLayoutManager = new VirtualLayoutManager(mContext);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        recyclerview.setLayoutManager(virtualLayoutManager);
        recyclerview.setAdapter(delegateAdapter);
        secondAllTypeAdapter =new SecondAllTypeAdapter();
        delegateAdapter.addAdapter(secondAllTypeAdapter);
        secondAllTypeAdapter.setOutClickListener(this);
    }

    @Override
    public void getData() {
        super.getData();
        secondTypePresenter.getFirstTypeMenu(new SimpleHashMapBuilder<String, Object>()
                .puts("partnerId", SpUtils.getValue(mContext, SpKey.CHOSE_PARTNERID)));
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    private void initView() {
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        topBar = (TopBar) findViewById(R.id.top_bar);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
    }

    @Override
    public void onGetFirstTypeMenuSucess(List<SecondType> list) {
        secondAllTypeAdapter.setData((ArrayList<SecondType>) list);
        for (int i = 0; i < list.size(); i++) {
            SecondType secondType=list.get(i);
            secondType.index=i;
            secondTypePresenter.getTypeMenuBindType(new SimpleHashMapBuilder<String, Object>()
                    .puts("partnerId", SpUtils.getValue(mContext, SpKey.CHOSE_PARTNERID))
                            .puts("allianceMenuNavigationCategoryId", secondType.id)
                    ,secondType);
        }
    }

    @Override
    public void onGetRecommendTypeMenuSucess(List<SecondType.SecondTypeMenu> list) {

    }

    @Override
    public void onGetTypeMenuSucess(List<SecondType.SecondTypeMenu> list) {

    }

    @Override
    public void onGetTypeMenuBindTypeSucess(List<SecondType.SecondTypeMenu> list, SecondType secondType) {
        secondType.secondTypeMenuList=list;
        secondAllTypeAdapter.notifyItemChanged(secondType.index);
    }

    @Override
    public void onGetLocationListSuccess(List<ProvinceCityModel> provinceCityModels) {

    }

    @Override
    public void outClick(String function, Object obj) {
        if ("服务分类".equals(function)) {
            SecondType.SecondTypeMenu secondTypeMenu= (SecondType.SecondTypeMenu) obj;
            if(secondTypeMenu.goodsCategoryIdList==null){
                ARouter.getInstance()
                        .build(SecondRoutes.MAIN_ALLTYPE)
                        .navigation();
            }else {
                ARouter.getInstance()
                        .build(SecondRoutes.MAIN_SEACH)
                        .withString("categoryIds",secondTypeMenu.goodsCategoryIdList)
                        .navigation();
            }

        }
    }
}
