package com.health.mine.activity;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.health.mine.R;
import com.health.mine.adapter.ServiceRecordAdapter;
import com.health.mine.contract.ServiceRecordContract;
import com.health.mine.decoration.ServiceRecordDecoration;
import com.health.mine.model.ServiceRecordModel;
import com.health.mine.presenter.ServiceRecordPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.Constants;
import com.healthy.library.routes.MineRoutes;
import com.healthy.library.utils.ParseUtils;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;

import java.util.List;
import java.util.Locale;

/**
 * @author Li
 * @date 2019/05/28 10:53
 * @des 服务记录
 */
@Route(path = MineRoutes.MINE_SERVICE_RECORD)
public class ServiceRecordActivity extends BaseActivity implements ServiceRecordContract.View {

    @Autowired
    String serviceId;
    @Autowired
    int shopId;
    @Autowired
    int shopBrand;
    @Autowired
    String serviceName;
    @Autowired
    int totalCount;
    @Autowired
    String courseStyle;
    @Autowired
    String endDate;

    private ServiceRecordPresenter mPresenter;
    private TopBar mTopBar;
    private TextView mTvServiceName;
    private TextView mTvTotalCount;
    private ImageView mIvLogo;
    private TextView mTvNoRecord;
    private RecyclerView mRecyclerRecord;
    private ServiceRecordAdapter mAdapter;
    private StatusLayout mStatusLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.mine_activity_service_record;
    }

    @Override
    protected void findViews() {
        mTopBar = findViewById(R.id.top_bar);
        mTvServiceName = findViewById(R.id.tv_service_name);
        mIvLogo = findViewById(R.id.iv_logo);
        mTvTotalCount = findViewById(R.id.tv_count);
        mTvNoRecord = findViewById(R.id.tv_no_record);
        mRecyclerRecord = findViewById(R.id.recycler_record);
        mStatusLayout = findViewById(R.id.layout_status);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        setTopBar(mTopBar);
        setStatusLayout(mStatusLayout);
        mTvServiceName.setText(serviceName);
        if (shopBrand == ParseUtils.parseInt(Constants.BRAND_TM)) {
            mIvLogo.setImageResource(R.drawable.mine_logo_tm);
        } else if (shopBrand == ParseUtils.parseInt(Constants.BRAND_ZYT)) {
            mIvLogo.setImageResource(R.drawable.mine_logo_zyt);
        } else if (shopBrand == ParseUtils.parseInt(Constants.BRAND_MBS)) {
            mIvLogo.setImageResource(R.drawable.mine_logo_mbs);
        }

        if (Constants.SERVICE_IN_TIME.equals(courseStyle)) {
            mTvTotalCount.setText(endDate);
        } else {
            mTvTotalCount.setText(String.format(Locale.CHINA, "共%d次", totalCount));
        }
        mAdapter = new ServiceRecordAdapter(R.layout.mine_item_service_record,courseStyle);

        mPresenter = new ServiceRecordPresenter(this, this);
        mRecyclerRecord.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter.bindToRecyclerView(mRecyclerRecord);
        mRecyclerRecord.addItemDecoration(new ServiceRecordDecoration(mContext));
        getData();
    }

    @Override
    public void getData() {
        mPresenter.getServiceRecord(serviceId, shopId, shopBrand);
    }

    @Override
    public void onGetServiceRecordListSuccess(List<ServiceRecordModel> list) {
        if (list.size() == 0) {
            mTvNoRecord.setVisibility(View.VISIBLE);
            mRecyclerRecord.setVisibility(View.GONE);
        } else {
            ServiceRecordModel model = new ServiceRecordModel();
            model.setServiceDate("调理时间");
            model.setTechnicianName("调理师");
            model.setLeftCount("剩余次数");
            list.add(0, model);
            mTvNoRecord.setVisibility(View.GONE);
            mRecyclerRecord.setVisibility(View.VISIBLE);
            mAdapter.setNewData(list);
        }
    }

    @Override
    public void showNetErr() {

    }

    @Override
    public void showDataErr() {

    }
}