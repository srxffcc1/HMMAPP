package com.health.index.activity;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.health.index.R;
import com.health.index.adapter.VaccineAdapter;
import com.health.index.contract.VaccineListContract;
import com.health.index.model.VaccineModel;
import com.health.index.presenter.VaccineListPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;

import java.util.List;

/**
 * @author Li
 * @date 2019/04/23 10:18
 * @des 疫苗助手界面
 */
@Route(path = IndexRoutes.INDEX_VACCINE_LIST)
public class VaccineListActivity extends BaseActivity implements VaccineListContract.View,
        BaseQuickAdapter.OnItemClickListener {

    @Autowired
    int id;
    private TopBar mTopBar;
    private RecyclerView mRecyclerVaccine;
    private StatusLayout mStatusLayout;
    private VaccineListPresenter mPresenter;
    private VaccineAdapter mVaccineAdapter;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected int getLayoutId() {
        return R.layout.index_activity_vaccine_list;
    }

    @Override
    protected void findViews() {
        mTopBar = findViewById(R.id.top_bar);

        mRecyclerVaccine = findViewById(R.id.recycler_vaccine);
        mStatusLayout = findViewById(R.id.layout_status);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        //System.out.println("疫苗助手id"+id);
        setTopBar(mTopBar);
        setStatusLayout(mStatusLayout);
        mPresenter = new VaccineListPresenter(this, this);
        mVaccineAdapter = new VaccineAdapter();
        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerVaccine.setLayoutManager(mLayoutManager);
        mVaccineAdapter.bindToRecyclerView(mRecyclerVaccine);
        mVaccineAdapter.setOnItemClickListener(this);
        getData();
    }

    @Override
    public void onGetVaccineListSuccess(List<VaccineModel> vaccineModelList, int currentId) {
        mVaccineAdapter.setNewData(vaccineModelList);
        if (id != -1) {
            currentId = id;
        }
        mLayoutManager.scrollToPosition(findPos(vaccineModelList, currentId));
    }

    @Override
    public void getData() {
        mPresenter.getVaccineList();
    }

    /**
     * 根据列表和id来决定跳转的位置
     */
    private int findPos(List<VaccineModel> vaccineModelList, int id) {
        for (VaccineModel vaccineModel : vaccineModelList) {
            if (vaccineModel.getVaccineAgeId() == id) {
                return vaccineModelList.indexOf(vaccineModel);
            }
        }
        return 0;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        String id = mVaccineAdapter.getData().get(position).getId();
        String url = String.format(
                "%s?id=%s&date=%s", SpUtils.getValue(mContext, UrlKeys.H5_VACCINE_DETAIL), id,
                mVaccineAdapter.getData().get(position).getVaccinationTime());
        ARouter.getInstance()
                .build(IndexRoutes.INDEX_WEBVIEW)
                .withString("url", url)
                .withString("title", "疫苗详情")
                .navigation();
    }
}
