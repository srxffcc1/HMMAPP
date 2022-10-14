package com.health.index.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.healthy.library.routes.IndexRoutes;
import com.health.index.R;
import com.health.index.adapter.BirthPackageAdapter;
import com.health.index.contract.BirthPackageContract;
import com.health.index.decoration.PackageDecoration;
import com.health.index.model.BirthPackageModel;
import com.health.index.presenter.BirthPackagePresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;

import java.util.List;

/**
 * @author Li
 * @date 2019/04/24 10:07
 * @des 待产包页
 */
@Route(path = IndexRoutes.INDEX_BIRTH_PACKAGE)
public class BirthPackageActivity extends BaseActivity implements BirthPackageContract.View,
        BirthPackageAdapter.OnChangeStatusListener {

    private TopBar mTopBar;
    private RecyclerView mRecyclerPackage;
    private BirthPackagePresenter mPresenter;
    private BirthPackageAdapter mAdapter;
    private StatusLayout mStatusLayout;
    private TextView mTvMom;
    private TextView mTvBaby;
    private int mCurrentStatus;
    private View mViewMom;
    private View mViewBaby;

    @Override
    protected int getLayoutId() {
        return R.layout.index_activity_birth_package;
    }

    @Override
    protected void findViews() {
        mTopBar = findViewById(R.id.top_bar);
        mStatusLayout = findViewById(R.id.layout_status);
        mRecyclerPackage = findViewById(R.id.recycler_packages);
        mTvMom = findViewById(R.id.tv_mom);
        mTvBaby = findViewById(R.id.tv_baby);
        mViewMom = findViewById(R.id.bottom_mom);
        mViewBaby = findViewById(R.id.bottom_baby);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setTopBar(mTopBar);
        setStatusLayout(mStatusLayout);
        mPresenter = new BirthPackagePresenter(this, this);
        mRecyclerPackage.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerPackage.addItemDecoration(new PackageDecoration(mContext));
        mAdapter = new BirthPackageAdapter(mContext);
        mAdapter.setListener(this);
        mRecyclerPackage.setAdapter(mAdapter);
        mTvMom.performClick();
    }

    @Override
    public void onGetBirthPackageSuccess(List<BirthPackageModel> list) {
        mAdapter.setData(list);
    }

    @Override
    public void onChangePackageSuccess(int pos, int status) {
        int toPosition = 0;
        BirthPackageModel originModel = mAdapter.getData().get(pos);

        if (status == BirthPackageModel.PREPARED) {
            for (BirthPackageModel model : mAdapter.getData()) {
                if (model.getPrepareStatus() == BirthPackageModel.PREPARED) {
                    toPosition = mAdapter.getData().indexOf(model);
                    break;
                }
            }
            originModel.setPrepareStatus(BirthPackageModel.UNPREPARED);
            mAdapter.getData().remove(pos);
            mAdapter.getData().add(toPosition, originModel);
            mAdapter.notifyDataSetChanged();
        } else {
            originModel.setPrepareStatus(BirthPackageModel.PREPARED);
            mAdapter.getData().remove(pos);
            mAdapter.getData().add(originModel);
            mAdapter.notifyDataSetChanged();

        }


    }

    @Override
    public void getData() {
        mPresenter.getBirthPackage(mCurrentStatus);
    }

    /**
     * 显示妈妈待产包
     */
    public void showMomPackage(View view) {
        mTvMom.setTypeface(Typeface.DEFAULT_BOLD);
        mTvBaby.setTypeface(Typeface.DEFAULT);
        mViewMom.setVisibility(View.VISIBLE);
        mViewBaby.setVisibility(View.GONE);
        mCurrentStatus = BirthPackageModel.PACKAGE_MOM;
        getData();
    }

    /**
     * 显示宝宝待产包
     */
    public void showBabyPackage(View view) {
        mTvMom.setTypeface(Typeface.DEFAULT);
        mTvBaby.setTypeface(Typeface.DEFAULT_BOLD);
        mViewMom.setVisibility(View.GONE);
        mViewBaby.setVisibility(View.VISIBLE);
        mCurrentStatus = BirthPackageModel.PACKAGE_BABY;
        getData();
    }

    @Override
    public void onChangeStatusClick(String key, int id, int pos, int status) {
        mPresenter.changePackage(key, id, pos, status);
    }
}
