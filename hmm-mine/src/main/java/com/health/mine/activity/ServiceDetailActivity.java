package com.health.mine.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.health.mine.R;
import com.health.mine.contract.ServiceDetailContract;
import com.health.mine.presenter.ServiceDetailPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.routes.MineRoutes;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;

/**
 * @author Li
 * @date 2019/05/28 17:03
 * @des 服务单详情
 */

@Route(path = MineRoutes.MINE_SERVICE_DETAIL)
public class ServiceDetailActivity extends BaseActivity implements ServiceDetailContract.View {

    @Autowired
    String orderServiceId;

    private ServiceDetailPresenter mPresenter;
    private TopBar mTopBar;
    private TextView mTvShopName;
    private TextView mTvService;
    private TextView mTvReserveDate;
    private StatusLayout mStatusLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.mine_activity_service_detail;
    }

    @Override
    protected void findViews() {
        mTopBar = findViewById(R.id.top_bar);
        mTvShopName = findViewById(R.id.tv_shop_name);
        mTvService = findViewById(R.id.tv_service);
        mTvReserveDate = findViewById(R.id.tv_reserve_date);
        mStatusLayout = findViewById(R.id.layout_status);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        setTopBar(mTopBar);
        setStatusLayout(mStatusLayout);
        mPresenter = new ServiceDetailPresenter(this, this);
        getData();
    }

    @Override
    public void getData() {
        super.getData();
        mPresenter.getServiceDetail(orderServiceId);
    }


    @Override
    public void onGetServiceDetailSuccess(String storeName, String serviceDate, String serviceNames) {
        mTvShopName.setText(storeName);
        mTvService.setText(serviceNames);
        mTvReserveDate.setText(serviceDate);
    }
}
